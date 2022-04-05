/*
 * Copyright 2015-${currentYear} 52°North Spatial Information Research GmbH
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
package org.n52.iceland.statistics.basetests;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpHost;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeValidationException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.n52.iceland.statistics.api.ElasticsearchSettings;
import org.n52.iceland.statistics.impl.ElasticsearchAdminHandler;

import pl.allegro.tech.embeddedelasticsearch.EmbeddedElastic;
import pl.allegro.tech.embeddedelasticsearch.PopularProperties;

public abstract class ElasticsearchAwareTest extends SpringBaseTest {

    private static EmbeddedElastic embeddedNode;

    @Inject
    protected ElasticsearchSettings clientSettings;

    @Inject
    protected ElasticsearchAdminHandler adminHandler;

    @BeforeAll
    public static void init() throws IOException, InterruptedException, NodeValidationException {

        logger.debug("Starting embedded node");
        String resource = "elasticsearch_embedded.yml";
        Settings.Builder settings = Settings.builder().loadFromStream(resource,
                ElasticsearchAwareTest.class.getResourceAsStream(resource), false);

        embeddedNode = EmbeddedElastic.builder().withElasticVersion("5.0.0")
                .withSetting(PopularProperties.TRANSPORT_TCP_PORT, 9300)
                .withSetting(PopularProperties.CLUSTER_NAME, "elasticsearch").build();
        embeddedNode.start();

        logger.debug("Started embedded node");

    }

    @BeforeEach
    public void setUp() throws InterruptedException, IOException {
        try {
            logger.info("Deleting {} index", clientSettings.getIndexId());
            Thread.sleep(2000);
            getEmbeddedClient().indices()
                    .delete(new DeleteIndexRequest(clientSettings.getIndexId()), RequestOptions.DEFAULT)
                    .isAcknowledged();
            Thread.sleep(2000);
        } catch (ElasticsearchException e) {
        }
        setUpHook();
    }

    protected void setUpHook() {
    }

    @AfterAll
    public static void destroy() throws IOException {
        logger.debug("Closing embedded node");
        embeddedNode.stop();
        FileUtils.deleteDirectory(new File(".\\data"));
    }

    protected static RestHighLevelClient getEmbeddedClient() {
        return new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", embeddedNode.getTransportTcpPort())));
    }

}
