/*
 * Copyright 2015-2018 52°North Initiative for Geospatial Open Source
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

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.elasticsearch.action.index.IndexResponse;
import org.junit.Assert;
import org.junit.Test;
import org.n52.iceland.statistics.api.ElasticsearchSettings;
import org.n52.iceland.statistics.api.ElasticsearchSettingsKeys;
import org.n52.iceland.statistics.basetests.SpringBaseTest;

public class EmbeddedServerIT extends SpringBaseTest {
    @Inject
    private ElasticsearchAdminHandler adminHandler;

    @Inject
    private ElasticsearchDataHandler dataHandler;

    @Inject
    private ElasticsearchSettings settings;

    @Test
    public void connectEmbeddedMode() throws Exception {

        settings.setNodeConnectionMode(ElasticsearchSettingsKeys.CONNECTION_MODE_EMBEDDED_SERVER);
        adminHandler.init();

        Map<String, Object> data = new HashMap<>();
        data.put("test", "test-string");
        IndexResponse idx = dataHandler.persist(data);

        Thread.sleep(2000);

        String ret = dataHandler.getClient().prepareGet(idx.getIndex(), idx.getType(), idx.getId()).get().getSourceAsString();
        Assert.assertNotNull(ret);

        adminHandler.destroy();

        try {
            FileUtils.deleteDirectory(new File("./elasticsearch"));
        } catch (IOException e) {
            logger.info(e.getMessage(), e);
        }
    }
}
