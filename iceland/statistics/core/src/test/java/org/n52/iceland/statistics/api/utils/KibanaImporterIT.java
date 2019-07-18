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
package org.n52.iceland.statistics.api.utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.inject.Inject;

import org.apache.commons.io.IOUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.n52.iceland.statistics.api.ElasticsearchSettings;
import org.n52.iceland.statistics.basetests.ElasticsearchAwareTest;
import org.n52.iceland.statistics.impl.ElasticsearchAdminHandler;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class KibanaImporterIT extends ElasticsearchAwareTest {

    @Inject
    private ElasticsearchSettings settings;

    @Inject
    private ElasticsearchAdminHandler admin;

    @Test
    public void importValidJson() throws IOException, InterruptedException {
        String json = IOUtils.toString(KibanaImporter.class.getResourceAsStream("/kibana/kibana_config.json"),
                                       StandardCharsets.UTF_8);
        new KibanaImporter(getEmbeddedClient(), ".kibana", "my-index").importJson(json);
        Thread.sleep(1500);
        Assertions.assertTrue(getEmbeddedClient().admin().indices().prepareExists(".kibana").get() != null);

        SearchResponse resp = getEmbeddedClient().prepareSearch(".kibana").setTypes("visualization").get();
        Assertions.assertTrue(resp.getHits().getTotalHits().value > 0);

        for (SearchHit hit : resp.getHits().getHits()) {
            Assertions.assertFalse(hit.getSourceAsString().contains(KibanaImporter.INDEX_NEEDLE));
        }

        SearchResponse resp2 = getEmbeddedClient().prepareSearch(".kibana").setTypes("dashboard").get();
        Assertions.assertTrue(resp2.getHits().getTotalHits().value > 0);

        for (SearchHit hit : resp2.getHits().getHits()) {
            Assertions.assertFalse(hit.getSourceAsString().contains(KibanaImporter.INDEX_NEEDLE));
        }
    }

    @Test
    public void importInvalidJson() throws InterruptedException, JsonParseException, JsonMappingException, IOException {
        new KibanaImporter(getEmbeddedClient(), "local-index", "").importJson("semmi latnivali nincs itt");
        Thread.sleep(1500);
        Assertions.assertFalse(getEmbeddedClient().admin().indices().prepareExists(".kibana").get() != null);
    }

    @Test
    public void setKibanaImporterFalseAfterRun() {
        settings.setKibanaConfigEnable(true);
        admin.init();

        Assertions.assertFalse(settings.isKibanaConfigEnable());
    }

}
