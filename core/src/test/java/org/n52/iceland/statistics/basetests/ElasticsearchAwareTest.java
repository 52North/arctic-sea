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
package org.n52.iceland.statistics.basetests;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.n52.iceland.statistics.api.ElasticsearchSettings;
import org.n52.iceland.statistics.impl.ElasticsearchAdminHandler;

public abstract class ElasticsearchAwareTest extends SpringBaseTest {

    private static Node embeddedNode;

    @Inject
    protected ElasticsearchSettings clientSettings;

    @Inject
    protected ElasticsearchAdminHandler adminHandler;

    @BeforeClass
    public static void init() throws IOException, InterruptedException {

        logger.debug("Starting embedded node");
        Settings settings = ImmutableSettings.settingsBuilder().loadFromClasspath("elasticsearch_embedded.yml").build();
        embeddedNode = NodeBuilder.nodeBuilder().settings(settings).build();
        embeddedNode.start();

        logger.debug("Started embedded node");

    }

    @Before
    public void setUp() throws InterruptedException {
        try {
            logger.info("Deleting {} index", clientSettings.getIndexId());
            Thread.sleep(2000);
            embeddedNode.client().admin().indices().prepareDelete(clientSettings.getIndexId()).get().isAcknowledged();
            Thread.sleep(2000);
        } catch (ElasticsearchException e) {
        }
        setUpHook();
    }

    protected void setUpHook() {
    }

    @AfterClass
    public static void destroy() throws IOException {
        logger.debug("Closing embedded node");
        embeddedNode.close();

        FileUtils.deleteDirectory(new File(".\\data"));

    }

    protected static Client getEmbeddedClient() {
        return embeddedNode.client();
    }

}
