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
package org.n52.iceland.statistics.impl;

import javax.inject.Inject;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Assert;
import org.junit.Test;
import org.n52.iceland.event.events.ExceptionEvent;
import org.n52.iceland.statistics.api.interfaces.datahandler.IStatisticsDataHandler;
import org.n52.iceland.statistics.api.mappings.ServiceEventDataMapping;
import org.n52.iceland.statistics.basetests.ElasticsearchAwareTest;
import org.n52.iceland.statistics.impl.resolvers.DefaultServiceEventResolver;

public class DefaultServiceEventResolverIT extends ElasticsearchAwareTest {

    @Inject
    private DefaultServiceEventResolver resolver;

    @Inject
    private IStatisticsDataHandler dataHandler;

    @Test
    public void saveSosEvent() throws InterruptedException {

        ExceptionEvent evt = new ExceptionEvent(new NullPointerException("sos event exception"));
        resolver.setEvent(evt);

        dataHandler.persist(resolver.resolve());
        Thread.sleep(2000);

        Client client = getEmbeddedClient();
        SearchResponse resp = client.prepareSearch(clientSettings.getIndexId()).setTypes(clientSettings.getTypeId())
                .setSearchType(SearchType.DFS_QUERY_AND_FETCH)
                .setQuery(QueryBuilders.matchQuery(ServiceEventDataMapping.UNHANDLED_SERVICEEVENT_TYPE.getName(), evt.getClass().toString())).get();

        Assert.assertEquals(1, resp.getHits().getTotalHits());
    }

}
