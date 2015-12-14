/*
 * Copyright 2015 52°North Initiative for Geospatial Open Source
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
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.n52.iceland.statistics.api.utils.FileDownloader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;
import org.elasticsearch.common.settings.Settings.Builder;

public class EmbeddedElasticsearch {

    private static final Logger logger = LoggerFactory.getLogger(EmbeddedElasticsearch.class);

    private String homePath;
    private Node embeddedNode;
    private Client client;
    private static final String resourceBase = "/statistics/embedded";
    private static final List<String> scriptsFileNames = ImmutableList.<String>of("getcapabilities_sections_concat.groovy",
            "most_requested_observedproperties.groovy", "most_requested_procedures.groovy");

    public void destroy() {
        if (client != null) {
            client.close();
        }
        logger.info("Closing embedded elasticsearch node");
        if (embeddedNode != null) {
            embeddedNode.close();
        }
    }

    public void init() {
        Objects.requireNonNull(homePath);

        logger.info("Home path for Embedded Elasticsearch: {}", homePath);

        logger.info("Starting embedded elasticsearch node");
        try {
            if (!new File(homePath).exists()) {
                FileUtils.forceMkdir(new File(homePath));
                copyScriptFiles();
                // copyLoggingFile();
                downlaodGroovyLibrary();
            } else {
                logger.info("Path " + homePath + " for embedded elasticsearch is exsits. Continue.");
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

        String resource = resourceBase + "/elasticsearch_embedded.yml";
        Builder setting = Settings.settingsBuilder()
                .loadFromStream(resource, getClass().getResourceAsStream(resourceBase + "/elasticsearch_embedded.yml"));
        setting.put("cluster.name", "elasticsearch");
        setting.put("node.name", "Embedded Server");
        setting.put("path.home", homePath);
        setting.put("path.logs", homePath + "/logs");

        Settings esSettings = setting.build();
        // LogConfigurator.configure(esSettings);
        embeddedNode = NodeBuilder.nodeBuilder().settings(esSettings).build();
        embeddedNode.start();
        try {
            logger.info("Waiting 8 seconds to startup the Elasticsearch");
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
        logger.info("Started embedded elasticsearch node");
    }

    private void copyLoggingFile() throws FileNotFoundException, IOException {
        InputStream inputLogigng = EmbeddedElasticsearch.class.getResourceAsStream(resourceBase + "/logging.yml");
        FileOutputStream out = new FileOutputStream(new File(homePath + "/config/logging.yml"));
        IOUtils.copy(inputLogigng, out);
        out.close();
    }

    private void downlaodGroovyLibrary() throws IOException {
        String groovyDir = homePath + "/plugins/groovy";
        FileUtils.forceMkdir(new File(groovyDir));
        FileDownloader.downloadFile("http://central.maven.org/maven2/org/codehaus/groovy/groovy-all/2.4.4/groovy-all-2.4.4.jar",
                groovyDir + "/groovy-all-2.4.4.jar");
    }

    private void copyScriptFiles() throws IOException {
        File scripts = new File(homePath + "/config/scripts");
        FileUtils.forceMkdir(scripts);

        // read the files list at least on windows works
        for (String line : scriptsFileNames) {
            InputStream scriptFile = EmbeddedElasticsearch.class.getResourceAsStream(resourceBase + "/scripts/" + line);
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
