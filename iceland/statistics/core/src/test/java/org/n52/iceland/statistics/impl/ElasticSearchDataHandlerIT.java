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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.elasticsearch.ElasticsearchGenerationException;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.n52.iceland.statistics.basetests.ElasticsearchAwareTest;

public class ElasticSearchDataHandlerIT extends ElasticsearchAwareTest {

    @Inject
    private ElasticsearchDataHandler dataHandler;

    @Test
    public void persistBasicData() throws InterruptedException, ElasticsearchGenerationException, IOException {
        Thread.sleep(2500);
        Map<String, Object> data = new HashMap<>();
        data.put("alma", "korte");
        dataHandler.persist(data);

        logger.debug("Waiting 3s");
        Thread.sleep(2500);

        SearchResponse response = getEmbeddedClient().search(
                new SearchRequest(clientSettings.getIndexId()).types(clientSettings.getTypeId()),
                RequestOptions.DEFAULT);
        Assertions.assertEquals("korte", response.getHits().getHits()[0].getSourceAsMap().get("alma"));
    }
}
