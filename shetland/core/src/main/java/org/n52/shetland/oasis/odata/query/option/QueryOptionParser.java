/*
 * Copyright 2015-2020 52°North Initiative for Geospatial Open Source
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

///*
// * Copyright 2015-2020 52°North Initiative for Geospatial Open Source
// * Software GmbH
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *    http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package org.n52.shetland.oasis.odata.query.option;
//
//import java.util.LinkedHashSet;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
//import java.util.Map.Entry;
//
//import org.n52.shetland.filter.AbstractPathFilter;
//import org.n52.shetland.filter.CountFilter;
//import org.n52.shetland.filter.ExpandFilter;
//import org.n52.shetland.filter.OrderByFilter;
//import org.n52.shetland.filter.OrderProperty;
//import org.n52.shetland.filter.PathFilterItem;
//import org.n52.shetland.filter.SelectFilter;
//import org.n52.shetland.filter.SkipTopFilter;
//import org.n52.shetland.ogc.filter.AbstractSelectionClause;
//import org.n52.shetland.ogc.filter.ComparisonFilter;
//import org.n52.shetland.ogc.filter.FilterClause;
//import org.n52.shetland.ogc.filter.FilterConstants.SkipTopOperator;
//import org.n52.shetland.ogc.filter.FilterConstants.SortOrder;
//
//import com.google.common.base.Splitter;
//
//import org.n52.shetland.oasis.odata.ODataConstants;
//
//import java.util.Set;
//
//public class QueryOptionParser {
//
//    public QueryOptions createQueryOptions(Map<String, String> parameters, String baseURL) {
//        if (parameters != null && !parameters.isEmpty()) {
//            return new QueryOptions(baseURL, getOptions(parameters));
//        }
//        return new QueryOptions(baseURL);
//    }
//
//    private Set<FilterClause> getOptions(Map<String, String> parameters) {
//        Set<FilterClause> options = new LinkedHashSet<>();
//        for (Entry<String, String> entry : parameters.entrySet()) {
//            if (entry.getValue() != null && !entry.getValue().isEmpty()) {
//                switch (entry.getKey()) {
//                case ODataConstants.QueryOptions.COUNT:
//                    options.add(new CountFilter(entry.getValue()));
//                    break;
//                case ODataConstants.QueryOptions.EXPAND:
//                    options.add(parsePathFilterItems(new ExpandFilter(), entry.getValue()));
//                    break;
//                case ODataConstants.QueryOptions.FILTER:
//                    options.add(parseFilter(entry.getValue()));
//                    break;
//                case ODataConstants.QueryOptions.ORDERBY:
//                    options.add(parseOrderBy(entry.getValue()));
//                    break;
//                case ODataConstants.QueryOptions.SELECT:
//                    options.add(parsePathFilterItems(new SelectFilter(), entry.getValue()));
//                    break;
//                case ODataConstants.QueryOptions.SKIP:
//                    options.add(new SkipTopFilter(SkipTopOperator.Skip, Long.parseLong(entry.getValue())));
//                    break;
//                case ODataConstants.QueryOptions.TOP:
//                    options.add(new SkipTopFilter(SkipTopOperator.Top, Long.parseLong(entry.getValue())));
//                    break;
//                default:
//                    break;
//                }
//            }
//        }
//        return options;
//    }
//
//    private OrderByFilter parseOrderBy(String value) {
//        List<OrderProperty> properties = new LinkedList<>();
//        for (String split : splitComma(value)) {
//            properties.add(parseOrderProperty(split));
//        }
//        return new OrderByFilter(properties);
//    }
//
//    private OrderProperty parseOrderProperty(String value) {
//        if (value.contains(" ")) {
//            String[] split = value.split(" ");
//            return new OrderProperty(split[0], SortOrder.valueOf(split[1].toUpperCase()));
//        }
//        return new OrderProperty(value);
//    }
//
//    private AbstractPathFilter parsePathFilterItems(AbstractPathFilter filter, String value) {
//        for (String v : splitComma(value)) {
//            filter.addItem(parsePathFilterItems(v));
//        }
//        return filter;
//    }
//
//    private PathFilterItem parsePathFilterItems(String value) {
//        if (containsOpenBracket(value)) {
//            return new PathFilterItem(value.substring(0, value.indexOf("(")),
//                    getOptions(toMap(getContentBetweenBrackets(value))));
//        }
//        return new PathFilterItem(value);
//    }
//
//    private Map<String, String> toMap(String value) {
//        return Splitter.on(';').trimResults().withKeyValueSeparator('=').split(value);
//    }
//
//    private AbstractSelectionClause parseFilter(String value) {
//     // TODO Auto-generated method stub
//        return new ComparisonFilter();
//    }
//
//    private boolean contains(String value, String c) {
//        return value.contains(c);
//    }
//
//    private boolean containsOpenBracket(String value) {
//        return contains(value, "(");
//    }
//
//    private String getContentBetweenBrackets(String value) {
//        return value.substring(value.indexOf('(') + 1, value.lastIndexOf(')'));
//    }
//
//    private String[] splitComma(String value) {
//        return value.split(",(?=(?:[^()]*\\([^()]*\\))*[^()]*$)");
//    }
//
//}
