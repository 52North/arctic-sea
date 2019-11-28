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
package org.n52.shetland.oasis.odata.query.option;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;

import org.n52.shetland.oasis.odata.ODataConstants;

import java.util.Set;

public class QueryOptionFactory {

    public QueryOptions createQueryOptions(Map<String, String> parameters, String baseURL) {
        if (parameters != null && !parameters.isEmpty()) {
            return new QueryOptions(baseURL, getOptions(parameters));
        }
        return new QueryOptions(baseURL);
    }

    private Set<QueryOption<?>> getOptions(Map<String, String> parameters) {
        Set<QueryOption<?>> options = new LinkedHashSet<>();
        for (Entry<String, String> entry : parameters.entrySet()) {
            switch (entry.getKey()) {
            case ODataConstants.QueryOptions.COUNT:
                options.add(new CountOption(entry.getValue()));
                break;
            case ODataConstants.QueryOptions.EXPAND:
                options.add(new ExpandOption(entry.getValue()));
                break;
            case ODataConstants.QueryOptions.FILTER:
                options.add(new FilterOption(entry.getValue()));
                break;
            case ODataConstants.QueryOptions.ORDERBY:
                options.add(new OrderByOption(entry.getValue()));
                break;
            case ODataConstants.QueryOptions.SELECT:
                options.add(new SelectOption(entry.getValue()));
                break;
            case ODataConstants.QueryOptions.SKIP:
                options.add(new SkipOption(entry.getValue()));
                break;
            case ODataConstants.QueryOptions.TOP:
                options.add(new TopOption(entry.getValue()));
                break;
            default:
                break;
            }
        }
        return options;
    }
}
