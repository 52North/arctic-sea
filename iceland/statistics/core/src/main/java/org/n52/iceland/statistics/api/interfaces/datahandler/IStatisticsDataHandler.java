/*
 * Copyright (C) 2015-2022 52°North Spatial Information Research GmbH
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
package org.n52.iceland.statistics.api.interfaces.datahandler;

import java.io.IOException;
import java.util.Map;

import org.elasticsearch.ElasticsearchGenerationException;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RestHighLevelClient;

public interface IStatisticsDataHandler {

    /**
     * Persist the date to the database
     *
     * @param dataMap
     *            keys are property names and the values are the objects
     *
     * @return the response
     * @throws IOException
     *             If an error occurs
     * @throws ElasticsearchGenerationException
     *             If an error occurs
     *
     */
    IndexResponse persist(Map<String, Object> dataMap) throws ElasticsearchGenerationException, IOException;

    /**
     * Returns true if the statistics module is enabled otherwise false
     *
     * @return is the logging enabled
     */
    boolean isLoggingEnabled();

    /**
     * Returns the underlying elasticsearch client
     *
     * @return the opened ready to used client.
     */
    RestHighLevelClient getClient();

}
