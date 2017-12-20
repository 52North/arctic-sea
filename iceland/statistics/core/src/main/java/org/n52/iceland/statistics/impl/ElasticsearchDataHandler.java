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
package org.n52.iceland.statistics.impl;

import java.util.Map;

import javax.inject.Inject;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.n52.iceland.statistics.api.ElasticsearchSettings;
import org.n52.iceland.statistics.api.interfaces.datahandler.IAdminDataHandler;
import org.n52.iceland.statistics.api.interfaces.datahandler.IStatisticsDataHandler;
import org.n52.iceland.statistics.api.mappings.ServiceEventDataMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ElasticsearchDataHandler implements IStatisticsDataHandler {

    private static final Logger logger = LoggerFactory.getLogger(ElasticsearchDataHandler.class);

    @Inject
    private ElasticsearchSettings settings;

    @Inject
    private IAdminDataHandler adminHandler;

    @Override
    public IndexResponse persist(Map<String, Object> dataMap) {
        if (!settings.isLoggingEnabled()) {
            return null;
        }
        if (adminHandler.getElasticsearchClient() == null) {
            throw new NullPointerException("Client is not initialized. Data will not be persisted.");
        }

        dataMap.put(ServiceEventDataMapping.TIMESTAMP_FIELD.getName(), DateTime.now(DateTimeZone.UTC));
        dataMap.put(ServiceEventDataMapping.UUID_FIELD.getName(), settings.getUuid());
        logger.debug("Persisting {}", dataMap);
        IndexResponse response = adminHandler.getElasticsearchClient().prepareIndex(settings.getIndexId(),
                settings.getTypeId()).setSource(dataMap)
                .get();
        return response;
    }

    @Override
    public boolean isLoggingEnabled() {
        return settings.isLoggingEnabled() && adminHandler.getElasticsearchClient() != null;
    }

    @Override
    public Client getClient() {
        return adminHandler.getElasticsearchClient();
    }
}
