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
package org.n52.shetland.oasis.odata.query.options;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.n52.shetland.filter.AbstractPathFilter;
import org.n52.shetland.filter.ExpandFilter;
import org.n52.shetland.filter.OrderByFilter;
import org.n52.shetland.filter.PathFilterItem;
import org.n52.shetland.filter.SelectFilter;
import org.n52.shetland.filter.SkipTopFilter;
import org.n52.shetland.oasis.odata.ODataConstants;
import org.n52.shetland.oasis.odata.query.option.QueryOptionParser;
import org.n52.shetland.oasis.odata.query.option.QueryOptions;
import org.n52.shetland.ogc.filter.AbstractSelectionClause;
import org.n52.shetland.ogc.filter.FilterClause;
import org.n52.shetland.ogc.filter.FilterConstants.SkipTopOperator;

public class QueryOptionsParserTest {

    private static final String URL = "http://test.org/";
    private QueryOptionParser factory = new QueryOptionParser();

    @Test
    public void countOptionTrue() {
        QueryOptions options = factory.createQueryOptions(createMap(ODataConstants.QueryOptions.COUNT, "true"), URL);
        assertTrue(options.hasCountOption());
        assertTrue(options.getCountOption().getValue());
    }

    @Test
    public void countOptionFalse() {
        QueryOptions options = factory.createQueryOptions(createMap(ODataConstants.QueryOptions.COUNT, "false"), URL);
        assertTrue(options.hasCountOption());
        assertFalse(options.getCountOption().getValue());
    }

    @Test
    public void expandOption() {
        QueryOptions options = factory.createQueryOptions(createMap(ODataConstants.QueryOptions.EXPAND, "test"), URL);
        assertTrue(options.hasExpandOption());
    }

    @Test
    public void filterOption() {
        QueryOptions options = factory.createQueryOptions(createMap(ODataConstants.QueryOptions.FILTER, "test"), URL);
        assertTrue(options.hasFilterOption());
    }

    @Test
    public void orderByOption() {
        QueryOptions options = factory.createQueryOptions(createMap(ODataConstants.QueryOptions.ORDERBY, "test"), URL);
        assertTrue(options.hasOrderByOption());
    }

    @Test
    public void selectOption() {
        QueryOptions options = factory.createQueryOptions(createMap(ODataConstants.QueryOptions.SELECT, "test"), URL);
        assertTrue(options.hasSelectOption());
    }

    @Test
    public void skipOption() {
        QueryOptions options = factory.createQueryOptions(createMap(ODataConstants.QueryOptions.SKIP, "1"), URL);
        assertTrue(options.hasSkipOption());
        assertEquals(1, options.getSkipOption().getValue());
    }

    @Test
    public void topOption() {
        QueryOptions options = factory.createQueryOptions(createMap(ODataConstants.QueryOptions.TOP, "1"), URL);
        assertTrue(options.hasTopOption());
        assertEquals(1, options.getTopOption().getValue());
    }


    @Test
    public void complexExpandOption() {
        Map<String, String> map = createMap(ODataConstants.QueryOptions.EXPAND, "Observations($filter=result eq 1;$expand=FeatureOfInterest;$select=@iot.id;$orderby=id;$skip=5;$top=10;$count=true),ObservedProperty");
        map.put(ODataConstants.QueryOptions.TOP, "10");
        QueryOptions options = factory.createQueryOptions(map, URL);
        assertTrue(options.hasTopOption());
        assertEquals(10, options.getTopOption().getValue());
        assertTrue(options.hasExpandOption());
        assertTrue(options.getExpandOption() instanceof ExpandFilter);
        List<PathFilterItem> items = ((AbstractPathFilter) options.getExpandOption()).getItems();
        assertTrue(items != null);
        assertEquals(2, items.size());
        PathFilterItem obs = items.get(0);
        assertEquals("Observations", obs.getPath());
        assertEquals(7, obs.getFilters().size());
        PathFilterItem obsProp = items.get(1);
        assertEquals("ObservedProperty", obsProp.getPath());
        assertTrue(obsProp.getFilters() == null);
    }

    @Test
    public void complexSelectListOption() {
        Map<String, String> map = createMap(ODataConstants.QueryOptions.SELECT, "Namespace.PreferredSupplier/AccountRepresentative,Address/Street,Address/Namespace.AddressWithLocation/Location");
        QueryOptions options = factory.createQueryOptions(map, URL);
        assertTrue(options.hasSelectOption());
        assertTrue(options.getSelectOption() instanceof SelectFilter);
        List<PathFilterItem> items = ((AbstractPathFilter) options.getSelectOption()).getItems();
        assertTrue(items != null);
        assertEquals(3, items.size());
        PathFilterItem item1 = items.get(0);
        assertEquals("Namespace.PreferredSupplier/AccountRepresentative", item1.getPath());
        PathFilterItem item2 = items.get(1);
        assertEquals("Address/Street", item2.getPath());
        PathFilterItem item3 = items.get(2);
        assertEquals("Address/Namespace.AddressWithLocation/Location", item3.getPath());
    }

    @Test
    public void complexSelectOption() {
        Map<String, String> map = createMap(ODataConstants.QueryOptions.SELECT, "Addresses($filter=startswith(City,'H');$top=5;$orderby=Country/Name,City,Street)");
        QueryOptions options = factory.createQueryOptions(map, URL);
        assertTrue(options.hasSelectOption());
        assertTrue(options.getSelectOption() instanceof SelectFilter);
        List<PathFilterItem> items = ((AbstractPathFilter) options.getSelectOption()).getItems();
        assertTrue(items != null);
        assertEquals(1, items.size());
        PathFilterItem item = items.get(0);
        assertEquals("Addresses", item.getPath());
        assertEquals(3, item.getFilters().size());
        Set<FilterClause> filters = item.getFilters();
        for (FilterClause filterClause : filters) {
            if (filterClause instanceof SkipTopFilter) {
                assertEquals(SkipTopOperator.Top, ((SkipTopFilter) filterClause).getOperator());
                assertEquals(5, ((SkipTopFilter) filterClause).getValue());
            } else if (filterClause instanceof OrderByFilter) {
                assertEquals(3, ((OrderByFilter) filterClause).getSortProperties().size());
            } else if (filterClause instanceof AbstractSelectionClause) {
                // TODO expand
                assertTrue(true);
            }
        }
    }

    private Map<String, String> createMap(String key, String value) {
        Map<String, String> map = new LinkedHashMap<>();
        map.put(key, value);
        return map;
    }

}
