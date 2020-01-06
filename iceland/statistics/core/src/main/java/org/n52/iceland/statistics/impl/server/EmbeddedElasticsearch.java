/*
 * Copyright 2015-2020 52°North Initiative for Geospatial Open Source
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
package org.n52.iceland.statistics.impl.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.settings.Settings.Builder;
import org.n52.iceland.statistics.api.utils.FileDownloader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import pl.allegro.tech.embeddedelasticsearch.EmbeddedElastic;
import pl.allegro.tech.embeddedelasticsearch.PopularProperties;

public class EmbeddedElasticsearch {

    private static final Logger LOG = LoggerFactory.getLogger(EmbeddedElasticsearch.class);

    private static final String RESOURCE_BASE = "/statistics/embedded";

    private static final List<String> SCRIPT_FILE_NAMES =
            ImmutableList.<String> of("getcapabilities_sections_concat.groovy",
                    "most_requested_observedproperties.groovy", "most_requested_procedures.groovy");

    private static final String LOGGING_CONFIG_PATH = "/config/logging.yml";

    private static final String CONFIG_PATH = "/elasticsearch_embedded.yml";

    private String homePath;

    private EmbeddedElastic embeddedNode;

    private RestHighLevelClient client;

    public void destroy() {
        if (client != null) {
            try {
                client.close();
            } catch (IOException e) {
                LOG.error("Error while closing client", e);
            }
        }
        LOG.info("Closing embedded elasticsearch node");
        if (embeddedNode != null) {
            embeddedNode.stop();
        }
    }

    @SuppressFBWarnings({ "OBL_UNSATISFIED_OBLIGATION", "DMI_HARDCODED_ABSOLUTE_FILENAME" })
    public void init() {
        Objects.requireNonNull(homePath);

        LOG.info("Home path for Embedded Elasticsearch: {}", homePath);

        LOG.info("Starting embedded elasticsearch node");
        try {
            if (!new File(homePath).exists()) {
                FileUtils.forceMkdir(new File(homePath));
                copyScriptFiles();
                // copyLoggingFile();
                downlaodGroovyLibrary();
            } else {
                LOG.info("Path " + homePath + " for embedded elasticsearch is exsits. Continue.");
            }
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }

        String resource = RESOURCE_BASE + CONFIG_PATH;
        Builder setting;
        try (InputStream stream = getClass().getResourceAsStream(RESOURCE_BASE + CONFIG_PATH)) {
            setting = Settings.builder().loadFromStream(resource, stream, false);
        } catch (IOException ex) {
            LOG.error(ex.getMessage(), ex);
            return;
        }
        Settings settings = setting.build();
        // setting.put("cluster.name", "elasticsearch");
        // setting.put("node.name", "Embedded Server");
        // setting.put("path.home", homePath);
        // setting.put("path.logs", homePath + "/logs");

        // Settings esSettings = setting.build();
        // LogConfigurator.configure(esSettings);
        // embeddedNode = new Node(new Environment(esSettings,
        // Paths.get(resource)));

        pl.allegro.tech.embeddedelasticsearch.EmbeddedElastic.Builder builder =
                EmbeddedElastic.builder().withElasticVersion("6.3.0").withPlugin("groovy");
        builder.withSetting(PopularProperties.TRANSPORT_TCP_PORT, 9300);
        builder.withSetting("cluster.name", "elasticsearch");
        builder.withSetting("node.name", "Embedded Server");
        builder.withSetting("path.home", homePath);
        builder.withSetting("path.logs", homePath + "/logs");
        for (String key : settings.keySet()) {
            builder.withSetting(key, settings.get(key));
        }
        embeddedNode = builder.build();
        try {
            embeddedNode.start();
        } catch (IOException | InterruptedException e1) {
            LOG.error("Error while starting node", e1);
        }
        try {
            LOG.info("Waiting 8 seconds to startup the Elasticsearch");
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            LOG.error(e.getMessage(), e);
        }
        LOG.info("Started embedded elasticsearch node");

    }

    private void copyLoggingFile() throws FileNotFoundException, IOException {
        try (InputStream inputLogigng =
                EmbeddedElasticsearch.class.getResourceAsStream(RESOURCE_BASE + "/logging.yml");
                FileOutputStream out = new FileOutputStream(new File(homePath + LOGGING_CONFIG_PATH))) {
            IOUtils.copy(inputLogigng, out);
        }
    }

    private void downlaodGroovyLibrary() throws IOException {
        String groovyDir = homePath + "/plugins/groovy";
        FileUtils.forceMkdir(new File(groovyDir));
        FileDownloader.downloadFile(
                "http://central.maven.org/maven2/org/codehaus/groovy/groovy-all/2.4.17/groovy-all-2.4.17.jar",
                groovyDir + "/groovy-all-2.4.17.jar");
    }

    private void copyScriptFiles() throws IOException {
        File scripts = new File(homePath + "/config/scripts");
        FileUtils.forceMkdir(scripts);

        // read the files list at least on windows works
        for (String line : SCRIPT_FILE_NAMES) {
            try (InputStream scriptFile =
                    EmbeddedElasticsearch.class.getResourceAsStream(RESOURCE_BASE + "/scripts/" + line);
                    FileOutputStream scriptFileOut = new FileOutputStream(scripts.getAbsolutePath() + "/" + line)) {
                IOUtils.copy(scriptFile, scriptFileOut);
            }
        }
    }

    public String getHomePath() {
        return homePath;
    }

    public void setHomePath(String homePath) {
        this.homePath = homePath;
    }

    public RestHighLevelClient getClient() {
        return new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", embeddedNode.getTransportTcpPort())));
    }
}
