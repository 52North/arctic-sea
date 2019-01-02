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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.IndicesAdminClient;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import org.n52.faroe.ConfigurationError;
import org.n52.iceland.statistics.api.ElasticsearchSettings;
import org.n52.iceland.statistics.api.ElasticsearchSettingsKeys;
import org.n52.iceland.statistics.api.mappings.MetadataDataMapping;
import org.n52.iceland.statistics.basetests.ElasticsearchAwareTest;

public class ElasticsearchAdminHandlerIT extends ElasticsearchAwareTest {

    @Inject
    private ElasticsearchAdminHandler adminHandler;

    @Inject
    private ElasticsearchDataHandler dataHandler;

    @Inject
    private ElasticsearchSettings settings;

    @Test
    public void createNewDatabase() throws InterruptedException {
        adminHandler.createSchema();

        IndicesAdminClient indices = getEmbeddedClient().admin().indices();

        IndicesExistsResponse index = indices.prepareExists(clientSettings.getIndexId()).get();
        Assert.assertTrue(index.isExists());

        GetResponse resp = getEmbeddedClient()
                .prepareGet(clientSettings.getIndexId(), MetadataDataMapping.METADATA_TYPE_NAME, MetadataDataMapping.METADATA_ROW_ID)
                .setOperationThreaded(false).get();

        Assert.assertEquals(1, resp.getSourceAsMap().get(MetadataDataMapping.METADATA_VERSION_FIELD.getName()));
    }

    @Test
    public void createSchemaTwiceWithoutError() {
        adminHandler.createSchema();
        adminHandler.createSchema();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void addnewUuidOnConnect() {
        adminHandler.createSchema();
        clientSettings.setUuid("lofasz janos");

        adminHandler.createSchema();

        GetResponse resp = getEmbeddedClient()
                .prepareGet(clientSettings.getIndexId(), MetadataDataMapping.METADATA_TYPE_NAME, MetadataDataMapping.METADATA_ROW_ID)
                .setOperationThreaded(false).get();

        Map<String, Object> map = resp.getSourceAsMap();
        Assert.assertNotNull(map.get(MetadataDataMapping.METADATA_CREATION_TIME_FIELD.getName()));
        Assert.assertNotNull(map.get(MetadataDataMapping.METADATA_UUIDS_FIELD.getName()));
        Assert.assertNotNull(map.get(MetadataDataMapping.METADATA_UPDATE_TIME_FIELD.getName()));

        List<String> object = (List<String>) map.get(MetadataDataMapping.METADATA_UUIDS_FIELD.getName());
        Assert.assertEquals(2, object.size());
        Assert.assertThat(object, CoreMatchers.hasItem("lofasz janos"));
    }

    @Test(
            expected = ConfigurationError.class)
    public void failOnVersionMismatch()
            throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InterruptedException {
        Map<String, Object> data = new HashMap<>();
        data.put(MetadataDataMapping.METADATA_VERSION_FIELD.getName(), 123456);
        getEmbeddedClient().prepareIndex(clientSettings.getIndexId(), MetadataDataMapping.METADATA_TYPE_NAME, MetadataDataMapping.METADATA_ROW_ID)
                .setSource(data).get();

        Thread.sleep(1500);

        adminHandler.createSchema();
    }

    @Test
    public void connectNodeMode() throws Exception {
        settings.setNodeConnectionMode(ElasticsearchSettingsKeys.CONNECTION_MODE_NODE);
        adminHandler.init();

        Map<String, Object> data = new HashMap<>();
        data.put("test", "test-string");
        IndexResponse idx = dataHandler.persist(data);

        Thread.sleep(2000);

        String ret = getEmbeddedClient().prepareGet(idx.getIndex(), idx.getType(), idx.getId()).get().getSourceAsString();
        Assert.assertNotNull(ret);
    }

    @Test
    public void connectTransportMode() throws InterruptedException {
        settings.setNodeConnectionMode(ElasticsearchSettingsKeys.CONNECTION_MODE_TRANSPORT_CLIENT);
        adminHandler.init();

        Map<String, Object> data = new HashMap<>();
        data.put("test", "test-string");
        IndexResponse idx = dataHandler.persist(data);

        Thread.sleep(2000);

        String ret = getEmbeddedClient().prepareGet(idx.getIndex(), idx.getType(), idx.getId()).get().getSourceAsString();
        Assert.assertNotNull(ret);
    }

    @Test
    public void enableKibanaPreConfLoadingFromDefaultFile() throws InterruptedException {
        settings.setKibanaConfigEnable(true);
        settings.setKibanaConfPath(null);

        adminHandler.init();

        Thread.sleep(2500);

        Assert.assertTrue(getEmbeddedClient().prepareExists(".kibana").get().exists());
    }
}
