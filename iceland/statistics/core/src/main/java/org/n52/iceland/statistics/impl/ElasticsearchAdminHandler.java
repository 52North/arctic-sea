/*
 * Copyright 2015-2022 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.n52.iceland.statistics.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.inject.Inject;
import javax.servlet.ServletContext;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHost;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.node.Node;
import org.joda.time.DateTimeZone;
import org.n52.faroe.ConfigurationError;
import org.n52.iceland.statistics.api.ElasticsearchSettings;
import org.n52.iceland.statistics.api.ElasticsearchSettingsKeys;
import org.n52.iceland.statistics.api.interfaces.datahandler.IAdminDataHandler;
import org.n52.iceland.statistics.api.mappings.MetadataDataMapping;
import org.n52.iceland.statistics.api.utils.KibanaImporter;
import org.n52.iceland.statistics.impl.schemabuilders.DefaultElasticsearchSchemas;
import org.n52.iceland.statistics.impl.server.EmbeddedElasticsearch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class ElasticsearchAdminHandler implements IAdminDataHandler {

    private static final Logger logger = LoggerFactory.getLogger(ElasticsearchAdminHandler.class);

    private static final String HTTP = "http";

    private RestHighLevelClient client;

    private Node node;

    @Inject
    private ElasticsearchSettings settings;

    private EmbeddedElasticsearch embeddedServer;

    @Inject
    private ServletContext context;

    @Inject
    private DefaultElasticsearchSchemas schemas;

    @Override
    public synchronized void deleteIndex(String index) throws IOException {
        getClient().delete(new DeleteRequest(index), RequestOptions.DEFAULT);
    }

    @Override
    public synchronized void createSchema() throws IOException {
        IndicesClient indices = getClient().indices();

        if (indices.exists(new GetIndexRequest(settings.getIndexId()), RequestOptions.DEFAULT)) {
            logger.info("Index {} already exists", settings.getIndexId());

            // update mapping
            Integer version = getCurrentVersion();
            logger.info("Elasticsearch schema version is {}", version);
            if (version == null) {
                throw new ConfigurationError("Database inconsistency. Metadata version not found in type %s",
                        MetadataDataMapping.METADATA_TYPE_NAME);
            }
            if (version != schemas.getSchemaVersion()) {
                throw new ConfigurationError(
                        "Database schema version inconsistency. Version numbers don't match. "
                                + "Database version number %d <-> Application version number %d",
                        version, schemas.getSchemaVersion());
            }
            addUuidToMetadataIfNeeded(settings.getUuid());
        } else {
            logger.info("Index {} not exists creating a new one now.", settings.getIndexId());
            // create metadata table and index table table
            Map<String, Object> mapping = new HashMap<>();
            mapping.put(MetadataDataMapping.METADATA_TYPE_NAME, schemas.getMetadataSchema());
            mapping.put(settings.getTypeId(), schemas.getSchema());
            CreateIndexResponse response = indices
                    .create(new CreateIndexRequest(settings.getIndexId()).mapping(mapping), RequestOptions.DEFAULT);
            logger.debug("Created indices: {}", response);
            // insert metadata values
            createMetadataType(schemas.getSchemaVersion());
        }
    }

    public void importPreconfiguredKibana() throws JsonParseException, JsonMappingException, IOException {
        String json;
        if (settings.getKibanaConfPath() == null || settings.getKibanaConfPath().trim().isEmpty()) {
            logger.info("No path is defined. Use default settings values");
            json = IOUtils.toString(this.getClass().getResourceAsStream("/kibana/kibana_config.json"),
                    StandardCharsets.UTF_8);
        } else {
            try (FileInputStream fio = new FileInputStream(settings.getKibanaConfPath());) {
                logger.info("Use content of path {}", settings.getKibanaConfPath());
                json = IOUtils.toString(fio, StandardCharsets.UTF_8);
            }
        }
        new KibanaImporter(getClient(), ".kibana", settings.getIndexId()).importJson(json);
        // set to false after successful import
        settings.saveBooleanValueToConfigFile(ElasticsearchSettingsKeys.KIBANA_CONFIG_ENABLE, false);
        settings.setKibanaConfigEnable(false);

    }

    private Integer getCurrentVersion() throws IOException {

        IndexResponse resp =
                getClient().index(new IndexRequest(settings.getIndexId()).id(MetadataDataMapping.METADATA_ROW_ID),
                        RequestOptions.DEFAULT);

        if (getClient().exists(new GetRequest(settings.getIndexId()), RequestOptions.DEFAULT)) {
            // Long versionString = resp.getVersion();
            // if (versionString == null) {
            // throw new ElasticsearchException(String.format(
            // "Database inconsistency. Version can't be found in row %s/%s/%s",
            // settings.getIndexId(),
            // MetadataDataMapping.METADATA_TYPE_NAME,
            // MetadataDataMapping.METADATA_ROW_ID));
            // }
            return ((Long) resp.getVersion()).intValue();
        } else {
            return null;
        }
    }

    private void addUuidToMetadataIfNeeded(String uuid) throws ElasticsearchException, IOException {
        GetResponse resp = getClient().get(
                new GetRequest(settings.getIndexId()).id(MetadataDataMapping.METADATA_ROW_ID), RequestOptions.DEFAULT);
        // GetResponse resp = getClient().prepareGet(settings.getIndexId(),
        // MetadataDataMapping.METADATA_TYPE_NAME,
        // MetadataDataMapping.METADATA_ROW_ID).get();

        Object retValues = resp.getSourceAsMap().get(MetadataDataMapping.METADATA_UUIDS_FIELD.getName());
        List<String> values;

        if (retValues instanceof String) {
            values = new LinkedList<>();
            values.add((String) retValues);
        } else if (retValues instanceof List<?>) {
            values = (List<String>) retValues;
        } else {
            throw new ConfigurationError("Invalid %s field type %s should have String or java.util.Collection<String>",
                    MetadataDataMapping.METADATA_UUIDS_FIELD, retValues.getClass());
        }

        // add new uuid if needed
        if (!values.stream().anyMatch(m -> m.equals(uuid))) {
            Map<String, Object> uuids = new HashMap<>();
            values.add(uuid);
            uuids.put(MetadataDataMapping.METADATA_UUIDS_FIELD.getName(), values);
            uuids.put(MetadataDataMapping.METADATA_UPDATE_TIME_FIELD.getName(),
                    Calendar.getInstance(DateTimeZone.UTC.toTimeZone()));
            getClient().update(new UpdateRequest().index(settings.getIndexId()).id("1").doc(uuids),
                    RequestOptions.DEFAULT);
            // getClient().prepareUpdate(settings.getIndexId(),
            // MetadataDataMapping.METADATA_TYPE_NAME, "1").setDoc(uuids)
            // .get();
            logger.info("UUID {} is added to the {} type", uuid, MetadataDataMapping.METADATA_TYPE_NAME);
        }
    }

    private void createMetadataType(int version) throws IOException {
        Map<String, Object> data = new HashMap<>();
        Calendar time = Calendar.getInstance(DateTimeZone.UTC.toTimeZone());
        data.put(MetadataDataMapping.METADATA_CREATION_TIME_FIELD.getName(), time);
        data.put(MetadataDataMapping.METADATA_UPDATE_TIME_FIELD.getName(), time);
        data.put(MetadataDataMapping.METADATA_VERSION_FIELD.getName(), version);
        data.put(MetadataDataMapping.METADATA_UUIDS_FIELD.getName(), settings.getUuid());
        getClient().indices().create(new CreateIndexRequest(settings.getIndexId()).mapping(data),
                RequestOptions.DEFAULT);
        // getClient().prepareIndex(settings.getIndexId(),
        // MetadataDataMapping.METADATA_TYPE_NAME,
        // MetadataDataMapping.METADATA_ROW_ID).setSource(data).get();
        logger.info("Initial metadata is created ceated in {}/{}", settings.getIndexId(),
                MetadataDataMapping.METADATA_TYPE_NAME);
    }

    /**
     * Starts client mode in local Node mode.
     */
    private void initNodeMode() {
        // Objects.requireNonNull(settings.getClusterName());
        // Objects.requireNonNull(settings.getClusterNodes());
        //
        // Settings.Builder settingsBuilder = Settings.builder();
        // settingsBuilder.put("discovery.zen.ping.unicast.hosts",
        // Joiner.on(",").join(settings.getClusterNodes()));
        // node =
        // NodeBuilder.nodeBuilder().settings(settingsBuilder).client(true).clusterName(settings.getClusterName())
        // .node();
        // client = node.client();
        // logger.info("ElasticSearch data handler starting in LAN mode");

    }

    /**
     * Starts client mode in {@link TransportClient} remote mode
     */
    private void initTransportMode() {
        // client = new RestHighLevelClient(
        // RestClient.builder(
        // new HttpHost(InetAddress.getByName(split[0]),
        // Integer.parseInt(split[1], 10), HTTP)));

        // Objects.requireNonNull(settings.getClusterName());
        // Objects.requireNonNull(settings.getClusterNodes());
        //
        // Settings.Builder tcSettings = Settings.settingsBuilder();
        // tcSettings.put("cluster.name", settings.getClusterName());

        // TransportClient cl =
        // TransportClient.builder().settings(tcSettings).build();
        Set<HttpHost> hosts = new LinkedHashSet<>();

        // nodes has format host[:port]
        settings.getClusterNodes().stream().forEach(i -> {
            HttpHost address = null;
            if (i.contains(":")) {
                try {
                    String[] split = i.split(":");
                    address = new HttpHost(InetAddress.getByName(split[0]), Integer.parseInt(split[1]), HTTP);
                } catch (UnknownHostException e) {
                    logConnectionError(i, e);
                }
            } else {
                try {
                    // default communication port
                    address = new HttpHost(InetAddress.getByName(i), 9300, HTTP);
                } catch (UnknownHostException e) {
                    logConnectionError(i, e);
                }
            }

            if (address != null) {
                hosts.add(address);
            }
        });
        setClient(new RestHighLevelClient(RestClient.builder(hosts.toArray(new HttpHost[0]))));
        logger.info("ElasticSearch data handler starting in Remote mode");

    }

    private void initEmbeddedMode() {
        embeddedServer = new EmbeddedElasticsearch();
        embeddedServer.setHomePath(context.getRealPath("/WEB-INF").concat("/config").concat("/elasticsearch"));
        embeddedServer.init();
        setClient(embeddedServer.getClient());
        logger.info("ElasticSearch data handler starting in EMBEDDED mode");
    }

    @Override
    public void init() {
        Objects.requireNonNull(settings);

        logger.info("Initializing ElasticSearch Statatistics connection");
        logger.info("Settings {}", settings.toString());

        Objects.requireNonNull(settings.getIndexId());
        Objects.requireNonNull(settings.getTypeId());
        Objects.requireNonNull(settings.getNodeConnectionMode());

        if (settings.isLoggingEnabled()) {
            // init client and local node or embedded mode
            if (settings.getNodeConnectionMode()
                    .equalsIgnoreCase(ElasticsearchSettingsKeys.CONNECTION_MODE_TRANSPORT_CLIENT)) {
                initTransportMode();
                logger.info("Transport client mode is currently not supported! Embedded mode would be used!");
            } else {
                initEmbeddedMode();
            }
            initEmbeddedMode();

            if (getClient() != null) {
                // create schema
                try {
                    createSchema();
                } catch (Exception e) {
                    logger.error("Error during schema creation", e);
                    destroy();
                }

                // deploy kibana configurations
                if (settings.isKibanaConfigEnable()) {
                    logger.info("Install preconfigured kibana settings");
                    try {
                        importPreconfiguredKibana();
                    } catch (Exception e) {
                        logger.error("Error during kibana config deployment", e);
                    }
                }
            }
        } else {
            logger.info("Statistics collection is not enabled. Data will not will be collected.");
        }

    }

    @Override
    public void destroy() {
        try {
            if (embeddedServer != null) {
                embeddedServer.destroy();
            }
            if (getClient() != null) {
                logger.info("Closing ElasticSearch client");
                getClient().close();
            }
            if (node != null) {
                if (!node.isClosed()) {
                    logger.info("Closing ElasticSearch node");
                    node.close();
                }
            }
        } catch (ElasticsearchException | IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public ElasticsearchSettings getElasticsearchSettings() {
        return settings;
    }

    @Override
    public RestHighLevelClient getElasticsearchClient() {
        return getClient();
    }

    private synchronized RestHighLevelClient getClient() {
        return client;
    }

    private synchronized void setClient(RestHighLevelClient client) {
        this.client = client;
    }

    private void logConnectionError(String i, UnknownHostException e) {
        logger.error("Could not create address for given host and port: {}", i, e);
    }

}
