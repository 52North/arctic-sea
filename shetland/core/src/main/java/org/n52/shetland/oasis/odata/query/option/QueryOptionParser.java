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

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.n52.shetland.filter.CountFilter;
import org.n52.shetland.filter.ExpandFilter;
import org.n52.shetland.filter.ExpandItem;
import org.n52.shetland.filter.OrderByFilter;
import org.n52.shetland.filter.OrderProperty;
import org.n52.shetland.filter.ProjectionFilter;
import org.n52.shetland.filter.SkipTopFilter;
import org.n52.shetland.ogc.filter.AbstractSelectionClause;
import org.n52.shetland.ogc.filter.ComparisonFilter;
import org.n52.shetland.ogc.filter.FilterClause;
import org.n52.shetland.ogc.filter.FilterConstants.SkipTopOperator;
import org.n52.shetland.ogc.filter.FilterConstants.SortOrder;
import org.n52.shetland.oasis.odata.ODataConstants;

import java.util.Set;

public class QueryOptionParser {

    public QueryOptions createQueryOptions(Map<String, String> parameters, String baseURL) {
        if (parameters != null && !parameters.isEmpty()) {
            return new QueryOptions(baseURL, getOptions(parameters));
        }
        return new QueryOptions(baseURL);
    }

    private Set<FilterClause> getOptions(Map<String, String> parameters) {
        Set<FilterClause> options = new LinkedHashSet<>();
        for (Entry<String, String> entry : parameters.entrySet()) {
            switch (entry.getKey()) {
            case ODataConstants.QueryOptions.COUNT:
                options.add(new CountFilter(entry.getValue()));
                break;
            case ODataConstants.QueryOptions.EXPAND:
                options.add(new ExpandFilter(parseExpand(entry.getValue())));
                break;
            case ODataConstants.QueryOptions.FILTER:
                options.add(parseFilter(entry.getValue()));
                break;
            case ODataConstants.QueryOptions.ORDERBY:
                options.add(parseOrderBy(entry.getValue()));
                break;
            case ODataConstants.QueryOptions.SELECT:
                options.add(new ProjectionFilter(parseSelection(entry.getValue())));
                break;
            case ODataConstants.QueryOptions.SKIP:
                options.add(new SkipTopFilter(SkipTopOperator.Skip, Long.parseLong(entry.getValue())));
                break;
            case ODataConstants.QueryOptions.TOP:
                options.add(new SkipTopFilter(SkipTopOperator.Top, Long.parseLong(entry.getValue())));
                break;
            default:
                break;
            }
        }
        return options;
    }

    private OrderByFilter parseOrderBy(String value) {
        List<OrderProperty> properties = new LinkedList<OrderProperty>();
        for (String split : splitComma(value)) {
            properties.add(parseOrderProperty(split));
        }
        return new OrderByFilter(properties);
    }

    private OrderProperty parseOrderProperty(String value) {
        if (value.contains(" ")) {
            String[] split = value.split(" ");
            return new OrderProperty(split[0], SortOrder.valueOf(split[1].toUpperCase()));
        }
        return new OrderProperty(value);
    }

    private Set<String> parseSelection(String value) {
        // TODO Auto-generated method stub
        return new LinkedHashSet<>(Arrays.asList(splitComma(value)));
    }

    private ExpandItem parseExpand(String value) {
        // TODO Auto-generated method stub
        return null;
    }

    private AbstractSelectionClause parseFilter(String value) {
        // TODO Auto-generated method stub
        return new ComparisonFilter();
    }

    private String[] splitComma(String value) {
        return value.split(",");
    }
}
