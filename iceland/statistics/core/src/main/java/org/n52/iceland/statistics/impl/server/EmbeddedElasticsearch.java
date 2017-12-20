/*
 * Copyright 2015-2017 52°North Initiative for Geospatial Open Source
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
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.settings.Settings.Builder;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.iceland.statistics.api.utils.FileDownloader;

import com.google.common.collect.ImmutableList;

public class EmbeddedElasticsearch {

    private static final Logger LOG = LoggerFactory.getLogger(EmbeddedElasticsearch.class);
    private static final String RESOURCE_BASE = "/statistics/embedded";
    private static final List<String> SCRIPT_FILE_NAMES = ImmutableList
            .<String>of("getcapabilities_sections_concat.groovy",
                        "most_requested_observedproperties.groovy", "most_requested_procedures.groovy");
    private static final String LOGGING_CONFIG_PATH = "/config/logging.yml";
    private static final String CONFIG_PATH = "/elasticsearch_embedded.yml";

    private String homePath;
    private Node embeddedNode;
    private Client client;

    public void destroy() {
        if (client != null) {
            client.close();
        }
        LOG.info("Closing embedded elasticsearch node");
        if (embeddedNode != null) {
            embeddedNode.close();
        }
    }

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
        Builder setting = Settings.settingsBuilder()
                .loadFromStream(resource, getClass().getResourceAsStream(RESOURCE_BASE + CONFIG_PATH));
        setting.put("cluster.name", "elasticsearch");
        setting.put("node.name", "Embedded Server");
        setting.put("path.home", homePath);
        setting.put("path.logs", homePath + "/logs");

        Settings esSettings = setting.build();
        // LogConfigurator.configure(esSettings);
        embeddedNode = NodeBuilder.nodeBuilder().settings(esSettings).build();
        embeddedNode.start();
        try {
            LOG.info("Waiting 8 seconds to startup the Elasticsearch");
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            LOG.error(e.getMessage(), e);
        }
        LOG.info("Started embedded elasticsearch node");
    }

    private void copyLoggingFile() throws FileNotFoundException, IOException {
        InputStream inputLogigng = EmbeddedElasticsearch.class.getResourceAsStream(RESOURCE_BASE + "/logging.yml");
        FileOutputStream out = new FileOutputStream(new File(homePath + LOGGING_CONFIG_PATH));
        IOUtils.copy(inputLogigng, out);
        out.close();
    }

    private void downlaodGroovyLibrary() throws IOException {
        String groovyDir = homePath + "/plugins/groovy";
        FileUtils.forceMkdir(new File(groovyDir));
        FileDownloader.downloadFile(
                "http://central.maven.org/maven2/org/codehaus/groovy/groovy-all/2.4.4/groovy-all-2.4.4.jar",
                groovyDir + "/groovy-all-2.4.4.jar");
    }

    private void copyScriptFiles() throws IOException {
        File scripts = new File(homePath + "/config/scripts");
        FileUtils.forceMkdir(scripts);

        // read the files list at least on windows works
        for (String line : SCRIPT_FILE_NAMES) {
            InputStream scriptFile = EmbeddedElasticsearch.class
                    .getResourceAsStream(RESOURCE_BASE + "/scripts/" + line);
            FileOutputStream scriptFileOut = new FileOutputStream(scripts.getAbsolutePath() + "/" + line);
            IOUtils.copy(scriptFile, scriptFileOut);
            scriptFileOut.close();
        }
    }

    public String getHomePath() {
        return homePath;
    }

    public void setHomePath(String homePath) {
        this.homePath = homePath;
    }

    public Client getClient() {
        return embeddedNode.client();
    }
}
