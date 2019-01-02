/*
 * Copyright 2015-2019 52°North Initiative for Geospatial Open Source
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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;
import javax.servlet.ServletContext;

import org.apache.commons.io.IOUtils;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.faroe.ConfigurationError;
import org.n52.iceland.statistics.api.ElasticsearchSettings;
import org.n52.iceland.statistics.api.ElasticsearchSettingsKeys;
import org.n52.iceland.statistics.api.interfaces.datahandler.IAdminDataHandler;
import org.n52.iceland.statistics.api.mappings.MetadataDataMapping;
import org.n52.iceland.statistics.api.utils.KibanaImporter;
import org.n52.iceland.statistics.impl.schemabuilders.DefaultElasticsearchSchemas;
import org.n52.iceland.statistics.impl.server.EmbeddedElasticsearch;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.common.base.Joiner;

public class ElasticsearchAdminHandler implements IAdminDataHandler {

    private static final Logger logger = LoggerFactory.getLogger(ElasticsearchAdminHandler.class);

    private Client client;
    private Node node;
    @Inject
    private ElasticsearchSettings settings;
    private EmbeddedElasticsearch embeddedServer;
    @Inject
    private ServletContext context;
    @Inject
    private DefaultElasticsearchSchemas schemas;

    @Override
    public synchronized void deleteIndex(String index) {
        client.admin().indices().prepareDelete(index).get();
    }

    @Override
    public synchronized void createSchema() {
        IndicesAdminClient indices = client.admin().indices();

        if (indices.prepareExists(settings.getIndexId()).get().isExists()) {
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
                        "Database schema version inconsistency. Version numbers don't match. " +
                        "Database version number %d <-> Application version number %d",
                        version, schemas.getSchemaVersion());
            }
            addUuidToMetadataIfNeeded(settings.getUuid());
        } else {
            logger.info("Index {} not exists creating a new one now.", settings.getIndexId());
            // create metadata table and index table table
            CreateIndexResponse response = indices.prepareCreate(settings.getIndexId())
                    .addMapping(MetadataDataMapping.METADATA_TYPE_NAME, schemas.getMetadataSchema())
                    .addMapping(settings.getTypeId(), schemas.getSchema())
                    .get();
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
        new KibanaImporter(client, ".kibana", settings.getIndexId()).importJson(json);
        // set to false after successful import
        settings.saveBooleanValueToConfigFile(ElasticsearchSettingsKeys.KIBANA_CONFIG_ENABLE, false);
        settings.setKibanaConfigEnable(false);

    }

    private Integer getCurrentVersion() {
        GetResponse resp = client.prepareGet(settings.getIndexId(),
                                             MetadataDataMapping.METADATA_TYPE_NAME,
                                             MetadataDataMapping.METADATA_ROW_ID)
                .setOperationThreaded(false).get();
        if (resp.isExists()) {
            Object versionString = resp.getSourceAsMap().get(MetadataDataMapping.METADATA_VERSION_FIELD.getName());
            if (versionString == null) {
                throw new ElasticsearchException(String
                        .format("Database inconsistency. Version can't be found in row %s/%s/%s",
                                settings.getIndexId(), MetadataDataMapping.METADATA_TYPE_NAME,
                                MetadataDataMapping.METADATA_ROW_ID));
            }
            return Integer.valueOf(versionString.toString());
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private void addUuidToMetadataIfNeeded(String uuid) throws ElasticsearchException {
        GetResponse resp = client
                .prepareGet(settings.getIndexId(), MetadataDataMapping.METADATA_TYPE_NAME,
                            MetadataDataMapping.METADATA_ROW_ID)
                .setOperationThreaded(false).get();

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
            uuids.put(MetadataDataMapping.METADATA_UPDATE_TIME_FIELD.getName(), Calendar.getInstance(DateTimeZone.UTC
                      .toTimeZone()));
            client.prepareUpdate(settings.getIndexId(),
                                 MetadataDataMapping.METADATA_TYPE_NAME, "1").setDoc(uuids).get();
            logger.info("UUID {} is added to the {} type", uuid, MetadataDataMapping.METADATA_TYPE_NAME);
        }
    }

    private void createMetadataType(int version) {
        Map<String, Object> data = new HashMap<>();
        Calendar time = Calendar.getInstance(DateTimeZone.UTC.toTimeZone());
        data.put(MetadataDataMapping.METADATA_CREATION_TIME_FIELD.getName(), time);
        data.put(MetadataDataMapping.METADATA_UPDATE_TIME_FIELD.getName(), time);
        data.put(MetadataDataMapping.METADATA_VERSION_FIELD.getName(), version);
        data.put(MetadataDataMapping.METADATA_UUIDS_FIELD.getName(), settings.getUuid());
        client.prepareIndex(settings.getIndexId(),
                            MetadataDataMapping.METADATA_TYPE_NAME,
                            MetadataDataMapping.METADATA_ROW_ID)
                .setSource(data).get();
        logger.info("Initial metadata is created ceated in {}/{}",
                    settings.getIndexId(),
                    MetadataDataMapping.METADATA_TYPE_NAME);
    }

    /**
     * Starts client mode in local Node mode.
     */
    private void initNodeMode() {
        Objects.requireNonNull(settings.getClusterName());
        Objects.requireNonNull(settings.getClusterNodes());

        Settings.Builder settingsBuilder = Settings.settingsBuilder();
        settingsBuilder.put("discovery.zen.ping.unicast.hosts", Joiner.on(",").join(settings.getClusterNodes()));

        node = NodeBuilder.nodeBuilder().settings(settingsBuilder)
                .client(true)
                .clusterName(settings.getClusterName())
                .node();
        client = node.client();
        logger.info("ElasticSearch data handler starting in LAN mode");

    }

    /**
     * Starts client mode in {@link TransportClient} remote mode
     */
    private void initTransportMode() {
        Objects.requireNonNull(settings.getClusterName());
        Objects.requireNonNull(settings.getClusterNodes());

        Settings.Builder tcSettings = Settings.settingsBuilder();
        tcSettings.put("cluster.name", settings.getClusterName());

        TransportClient cl = TransportClient.builder()
                .settings(tcSettings).build();
        // nodes has format host[:port]
        settings.getClusterNodes().stream().forEach(i -> {
            InetSocketTransportAddress address = null;
            if (i.contains(":")) {
                try {
                    String[] split = i.split(":");
                    address = new InetSocketTransportAddress(InetAddress.getByName(split[0]),
                                                             Integer.parseInt(split[1]));
                } catch (UnknownHostException e) {
                    logConnectionError(i, e);
                }
            } else {
                try {
                    // default communication port
                    address = new InetSocketTransportAddress(InetAddress.getByName(i), 9300);
                } catch (UnknownHostException e) {
                    logConnectionError(i, e);
                }
            }

            if (address != null) {
                cl.addTransportAddress(address);
            }
        });
        this.client = cl;
        logger.info("ElasticSearch data handler starting in Remote mode");

    }

    private void initEmbeddedMode() {
        embeddedServer = new EmbeddedElasticsearch();
        embeddedServer.setHomePath(context.getRealPath("/WEB-INF").concat("/elasticsearch"));
        embeddedServer.init();
        client = embeddedServer.getClient();
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
            if (settings.getNodeConnectionMode().equalsIgnoreCase(ElasticsearchSettingsKeys.CONNECTION_MODE_NODE)) {
                initNodeMode();
            } else if (settings.getNodeConnectionMode()
                    .equalsIgnoreCase(ElasticsearchSettingsKeys.CONNECTION_MODE_TRANSPORT_CLIENT)) {
                initTransportMode();
            } else {
                initEmbeddedMode();
            }

            if (client != null) {
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
            if (client != null) {
                logger.info("Closing ElasticSearch client");
                client.close();
            }
            if (node != null) {
                if (!node.isClosed()) {
                    logger.info("Closing ElasticSearch node");
                    node.close();
                }
            }
        } catch (ElasticsearchException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public ElasticsearchSettings getElasticsearchSettings() {
        return settings;
    }

    @Override
    public Client getElasticsearchClient() {
        return client;
    }

    private void logConnectionError(String i, UnknownHostException e) {
        logger.error("Could not create address for given host and port: {}", i, e);
    }

}
