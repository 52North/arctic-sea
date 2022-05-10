/*
 * Copyright 2015-2022 52°North Spatial Information Research GmbH
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
package org.n52.svalbard.odata.core;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.n52.shetland.filter.CountFilter;
import org.n52.shetland.filter.ExpandFilter;
import org.n52.shetland.filter.ExpandItem;
import org.n52.shetland.filter.FilterFilter;
import org.n52.shetland.filter.OrderByFilter;
import org.n52.shetland.filter.OrderProperty;
import org.n52.shetland.filter.SelectFilter;
import org.n52.shetland.filter.SkipTopFilter;
import org.n52.shetland.oasis.odata.ODataConstants;
import org.n52.shetland.oasis.odata.query.option.QueryOptions;
import org.n52.shetland.ogc.filter.FilterClause;
import org.n52.shetland.ogc.filter.FilterConstants;
import org.n52.shetland.ogc.sta.exception.STAInvalidQueryError;
import org.n52.svalbard.odata.core.expr.MemberExpr;
import org.n52.svalbard.odata.core.expr.StringValueExpr;
import org.n52.svalbard.odata.core.expr.arithmetic.NumericValueExpr;
import org.n52.svalbard.odata.core.expr.bool.ComparisonExpr;

/**
 * @author <a href="mailto:j.speckamp@52north.org">Jan Speckamp</a>
 */
@SuppressWarnings("unchecked")
public class ExpandQueryOptionTest extends QueryOptionTests {

    @Test
    public void testValidExpandOption() {
        String val, nested;
        ExpandFilter actual, reference;
        QueryOptions defaultQO = new QueryOptions(null);
        Set<FilterClause> nestedFilters;

        // Simple
        val = "test";
        init(ODataConstants.QueryOptions.EXPAND + EQ + val);
        actual = ((QueryOptions) parser.queryOptions()
                                       .accept(new STAQueryOptionVisitor())).getExpandFilter();
        reference = new ExpandFilter(new ExpandItem(val, defaultQO));
        Assertions.assertEquals(reference, actual);

        // Resolve nested Expand
        val = "test";
        nested = "nested";
        init(ODataConstants.QueryOptions.EXPAND + EQ + val + "/" + nested);
        actual = ((QueryOptions) parser.queryOptions()
                                       .accept(new STAQueryOptionVisitor())).getExpandFilter();
        nestedFilters = new HashSet<>();
        nestedFilters.add(new ExpandFilter(new ExpandItem(nested, defaultQO)));
        reference = new ExpandFilter(new ExpandItem(val, new QueryOptions(nestedFilters)));
        Assertions.assertEquals(reference, actual);

        // Expand with queryOptions
        val = "test";
        String queryOption = "($filter=id eq '2')";
        init(ODataConstants.QueryOptions.EXPAND + EQ + val + queryOption);
        actual = ((QueryOptions) parser.queryOptions()
                                       .accept(new STAQueryOptionVisitor())).getExpandFilter();
        nestedFilters = new HashSet<>();
        nestedFilters.add(new FilterFilter(new ComparisonExpr(FilterConstants.ComparisonOperator.PropertyIsEqualTo,
                                                              new MemberExpr("id"),
                                                              new StringValueExpr("2"))));
        reference = new ExpandFilter(new ExpandItem(val, new QueryOptions(nestedFilters)));
        Assertions.assertEquals(reference, actual);

        // Expand with queryOptions
        val = "test";
        queryOption = "($top=52)";
        init(ODataConstants.QueryOptions.EXPAND + EQ + val + queryOption);
        actual = ((QueryOptions) parser.queryOptions()
                                       .accept(new STAQueryOptionVisitor())).getExpandFilter();
        nestedFilters = new HashSet<>();
        nestedFilters.add(new SkipTopFilter(FilterConstants.SkipTopOperator.Top, 52L));
        reference = new ExpandFilter(new ExpandItem(val, new QueryOptions(nestedFilters)));
        Assertions.assertEquals(reference, actual);

        // Expand with queryOptions
        val = "test";
        queryOption = "($skip=52)";
        init(ODataConstants.QueryOptions.EXPAND + EQ + val + queryOption);
        actual = ((QueryOptions) parser.queryOptions()
                                       .accept(new STAQueryOptionVisitor())).getExpandFilter();
        nestedFilters = new HashSet<>();
        nestedFilters.add(new SkipTopFilter(FilterConstants.SkipTopOperator.Skip, 52L));
        reference = new ExpandFilter(new ExpandItem(val, new QueryOptions(nestedFilters)));
        Assertions.assertEquals(reference, actual);

        // Expand with queryOptions
        val = "test";
        queryOption = "($select=id)";
        init(ODataConstants.QueryOptions.EXPAND + EQ + val + queryOption);
        actual = ((QueryOptions) parser.queryOptions()
                                       .accept(new STAQueryOptionVisitor())).getExpandFilter();
        nestedFilters = new HashSet<>();
        nestedFilters.add(new SelectFilter("id"));
        reference = new ExpandFilter(new ExpandItem(val, new QueryOptions(nestedFilters)));
        Assertions.assertEquals(reference, actual);

        // Expand with queryOptions
        val = "test";
        queryOption = "($expand=nested)";
        init(ODataConstants.QueryOptions.EXPAND + EQ + val + queryOption);
        actual = ((QueryOptions) parser.queryOptions()
                                       .accept(new STAQueryOptionVisitor())).getExpandFilter();
        nestedFilters = new HashSet<>();
        nestedFilters.add(new ExpandFilter(new ExpandItem(nested, defaultQO)));
        reference = new ExpandFilter(new ExpandItem(val, new QueryOptions(nestedFilters)));
        Assertions.assertEquals(reference, actual);

        // Expand with queryOptions
        val = "test";
        queryOption = "($orderby=id asc)";
        init(ODataConstants.QueryOptions.EXPAND + EQ + val + queryOption);
        actual = ((QueryOptions) parser.queryOptions()
                                       .accept(new STAQueryOptionVisitor())).getExpandFilter();
        nestedFilters = new HashSet<>();
        nestedFilters.add(new OrderByFilter(new OrderProperty("id", FilterConstants.SortOrder.ASC)));
        reference = new ExpandFilter(new ExpandItem(val, new QueryOptions(nestedFilters)));
        Assertions.assertEquals(reference, actual);
    }

    @Test
    public void testInvalidExpandOption() {

        // May not be empty
        init(ODataConstants.QueryOptions.EXPAND + EQ + "");
        Assertions.assertThrows(
                Exception.class,
                () -> parser.queryOptions().accept(new STAQueryOptionVisitor())
        );

        // May not be malformed
        init(ODataConstants.QueryOptions.EXPAND + EQ + "test.test");
        Assertions.assertThrows(
                Exception.class,
                () -> parser.queryOptions().accept(new STAQueryOptionVisitor())
        );
        init(ODataConstants.QueryOptions.EXPAND + EQ + "test()");
        Assertions.assertThrows(
                Exception.class,
                () -> parser.queryOptions().accept(new STAQueryOptionVisitor())
        );
        init(ODataConstants.QueryOptions.EXPAND + EQ + "test($to)");
        Assertions.assertThrows(
                Exception.class,
                () -> parser.queryOptions().accept(new STAQueryOptionVisitor())
        );
    }

    @Test
    public void complexExpandOption() {
        init(ODataConstants.QueryOptions.EXPAND
                     + EQ
                     + "Observations($filter=result eq 1;$expand=FeatureOfInterest;$select=id;$orderby=id;"
                     + "$skip=5;$top=10;$count=true),ObservedProperty");
        QueryOptions options =
                (QueryOptions) parser.queryOptions().accept(new STAQueryOptionVisitor());
        Assertions.assertTrue(options.hasExpandFilter());
        Assertions.assertTrue(options.getExpandFilter() instanceof ExpandFilter);
        Set<ExpandItem> items = (options.getExpandFilter().getItems());
        Assertions.assertNotNull(items);
        Assertions.assertEquals(2, items.size());

        for (ExpandItem obs : items) {
            if (obs.getPath().equals("Observations")) {
                Assertions.assertEquals("Observations", obs.getPath());
                Assertions.assertTrue(obs.getQueryOptions().hasExpandFilter());
                Assertions.assertTrue(obs.getQueryOptions().hasFilterFilter());
                Assertions.assertTrue(obs.getQueryOptions().hasSelectFilter());
                Assertions.assertTrue(obs.getQueryOptions().hasOrderByFilter());
                Assertions.assertTrue(obs.getQueryOptions().hasSkipFilter());
                Assertions.assertTrue(obs.getQueryOptions().hasTopFilter());
                Assertions.assertTrue(obs.getQueryOptions().hasCountFilter());

                Set<FilterClause> filters = new HashSet<>();
                filters.add(new FilterFilter(new ComparisonExpr(FilterConstants.ComparisonOperator.PropertyIsEqualTo,
                                                                new MemberExpr("result"),
                                                                new NumericValueExpr("1"))));

                filters.add(new ExpandFilter(
                        new ExpandItem("FeatureOfInterest", new QueryOptions(null))));
                filters.add(new SelectFilter("id"));
                filters.add(new OrderByFilter(new OrderProperty("id")));
                filters.add(new SkipTopFilter(FilterConstants.SkipTopOperator.Skip, 5L));
                filters.add(new SkipTopFilter(FilterConstants.SkipTopOperator.Top, 10L));
                filters.add(new CountFilter(true));

                Assertions.assertEquals(new QueryOptions(filters), obs.getQueryOptions());
            } else if (obs.getPath().equals("ObservedProperty")) {
                Assertions.assertEquals("ObservedProperty", obs.getPath());
                Assertions.assertNotNull(obs.getQueryOptions());
                Assertions.assertTrue(obs.getQueryOptions().hasTopFilter());

                Assertions.assertFalse(obs.getQueryOptions().hasExpandFilter());
                Assertions.assertFalse(obs.getQueryOptions().hasFilterFilter());
                Assertions.assertFalse(obs.getQueryOptions().hasSelectFilter());
                Assertions.assertFalse(obs.getQueryOptions().hasOrderByFilter());
                Assertions.assertFalse(obs.getQueryOptions().hasSkipFilter());
                Assertions.assertFalse(obs.getQueryOptions().hasCountFilter());
            } else {
                Assertions.fail("Did not find expected expandItem!");
            }
        }
    }

    /**
     * Checks if Requests such as
     * $expand=Datastreams/Sensors
     * are rewritten to
     * $expand=Datastreams($expand=Sensors)
     */
    @Test
    public void complexExpandOptionRewrite() {
        QueryOptions options;
        Set<ExpandItem> items;

        init(ODataConstants.QueryOptions.EXPAND
                     + EQ
                     + "Datastreams/Sensors/Datastreams");
        options =
                (QueryOptions) parser.queryOptions().accept(new STAQueryOptionVisitor());
        Assertions.assertTrue(options.hasExpandFilter());
        Assertions.assertNotNull(options.getExpandFilter());

        items = (options.getExpandFilter().getItems());

        Assertions.assertNotNull(items);
        Assertions.assertEquals(1, items.size());

        ExpandItem level1 = items.toArray(new ExpandItem[] {})[0];
        Assertions.assertTrue(level1.getQueryOptions().hasExpandFilter());
        Assertions.assertFalse(level1.getQueryOptions().hasFilterFilter());
        Assertions.assertFalse(level1.getQueryOptions().hasSelectFilter());
        Assertions.assertFalse(level1.getQueryOptions().hasOrderByFilter());
        Assertions.assertFalse(level1.getQueryOptions().hasSkipFilter());
        Assertions.assertTrue(level1.getQueryOptions().hasTopFilter());
        Assertions.assertFalse(level1.getQueryOptions().hasCountFilter());
        Assertions.assertEquals("Datastreams", level1.getPath());

        ExpandItem level2 = level1.getQueryOptions().getExpandFilter().getItems().toArray(new ExpandItem[] {})[0];
        Assertions.assertTrue(level2.getQueryOptions().hasExpandFilter());
        Assertions.assertFalse(level2.getQueryOptions().hasFilterFilter());
        Assertions.assertFalse(level2.getQueryOptions().hasSelectFilter());
        Assertions.assertFalse(level2.getQueryOptions().hasOrderByFilter());
        Assertions.assertFalse(level2.getQueryOptions().hasSkipFilter());
        Assertions.assertTrue(level2.getQueryOptions().hasTopFilter());
        Assertions.assertFalse(level2.getQueryOptions().hasCountFilter());
        Assertions.assertEquals("Sensors", level2.getPath());
        Assertions.assertEquals(1, level2.getQueryOptions().getExpandFilter().getItems().size());

        ExpandItem level3 = level2.getQueryOptions().getExpandFilter().getItems().toArray(new ExpandItem[] {})[0];
        Assertions.assertFalse(level3.getQueryOptions().hasExpandFilter());
        Assertions.assertFalse(level3.getQueryOptions().hasFilterFilter());
        Assertions.assertFalse(level3.getQueryOptions().hasSelectFilter());
        Assertions.assertFalse(level3.getQueryOptions().hasOrderByFilter());
        Assertions.assertFalse(level3.getQueryOptions().hasSkipFilter());
        Assertions.assertTrue(level3.getQueryOptions().hasTopFilter());
        Assertions.assertFalse(level3.getQueryOptions().hasCountFilter());
        Assertions.assertEquals("Datastreams", level3.getPath());

        init(ODataConstants.QueryOptions.EXPAND
                     + EQ
                     + "Things/Datastreams/Thing,Things/Locations," +
                     "Things/Datastreams/ObservedProperty,Things/Datastreams/Sensor");
        options = (QueryOptions) parser.queryOptions().accept(new STAQueryOptionVisitor());
        Assertions.assertTrue(options.hasExpandFilter());
        Assertions.assertNotNull(options.getExpandFilter());

        items = (options.getExpandFilter().getItems());

        Assertions.assertNotNull(items);
        Assertions.assertEquals(1, items.size());

        level1 = items.toArray(new ExpandItem[] {})[0];
        Assertions.assertTrue(level1.getQueryOptions().hasExpandFilter());
        Assertions.assertFalse(level1.getQueryOptions().hasFilterFilter());
        Assertions.assertFalse(level1.getQueryOptions().hasSelectFilter());
        Assertions.assertFalse(level1.getQueryOptions().hasOrderByFilter());
        Assertions.assertFalse(level1.getQueryOptions().hasSkipFilter());
        Assertions.assertTrue(level1.getQueryOptions().hasTopFilter());
        Assertions.assertFalse(level1.getQueryOptions().hasCountFilter());
        Assertions.assertEquals("Things", level1.getPath());

        items = level1.getQueryOptions().getExpandFilter().getItems();
        Assertions.assertNotNull(items);
        Assertions.assertEquals(2, items.size());

        for (ExpandItem expandItem : items) {
            if (expandItem.getPath().equals("Datastreams")) {
                Assertions.assertEquals("Datastreams", expandItem.getPath());
                Assertions.assertTrue(expandItem.getQueryOptions().hasExpandFilter());
                Assertions.assertTrue(expandItem.getQueryOptions().hasTopFilter());

                Assertions.assertFalse(expandItem.getQueryOptions().hasFilterFilter());
                Assertions.assertFalse(expandItem.getQueryOptions().hasSelectFilter());
                Assertions.assertFalse(expandItem.getQueryOptions().hasOrderByFilter());
                Assertions.assertFalse(expandItem.getQueryOptions().hasSkipFilter());
                Assertions.assertFalse(expandItem.getQueryOptions().hasCountFilter());

                Set<ExpandItem> nested = expandItem.getQueryOptions().getExpandFilter().getItems();
                Assertions.assertNotNull(nested);
                Assertions.assertEquals(3, nested.size());

                for (ExpandItem Level2expandItem : nested) {
                    switch (Level2expandItem.getPath()) {
                        case "Thing":
                            Assertions.assertEquals("Thing", Level2expandItem.getPath());
                            Assertions.assertNotNull(Level2expandItem.getQueryOptions());
                            Assertions.assertTrue(Level2expandItem.getQueryOptions().hasTopFilter());

                            Assertions.assertFalse(Level2expandItem.getQueryOptions().hasExpandFilter());
                            Assertions.assertFalse(Level2expandItem.getQueryOptions().hasFilterFilter());
                            Assertions.assertFalse(Level2expandItem.getQueryOptions().hasSelectFilter());
                            Assertions.assertFalse(Level2expandItem.getQueryOptions().hasOrderByFilter());
                            Assertions.assertFalse(Level2expandItem.getQueryOptions().hasSkipFilter());
                            Assertions.assertFalse(Level2expandItem.getQueryOptions().hasCountFilter());
                            break;
                        case "Sensor":
                            Assertions.assertEquals("Sensor", Level2expandItem.getPath());
                            Assertions.assertNotNull(Level2expandItem.getQueryOptions());
                            Assertions.assertTrue(Level2expandItem.getQueryOptions().hasTopFilter());

                            Assertions.assertFalse(Level2expandItem.getQueryOptions().hasExpandFilter());
                            Assertions.assertFalse(Level2expandItem.getQueryOptions().hasFilterFilter());
                            Assertions.assertFalse(Level2expandItem.getQueryOptions().hasSelectFilter());
                            Assertions.assertFalse(Level2expandItem.getQueryOptions().hasOrderByFilter());
                            Assertions.assertFalse(Level2expandItem.getQueryOptions().hasSkipFilter());
                            Assertions.assertFalse(Level2expandItem.getQueryOptions().hasCountFilter());
                            break;
                        case "ObservedProperty":
                            Assertions.assertEquals("ObservedProperty", Level2expandItem.getPath());
                            Assertions.assertNotNull(Level2expandItem.getQueryOptions());
                            Assertions.assertTrue(Level2expandItem.getQueryOptions().hasTopFilter());

                            Assertions.assertFalse(Level2expandItem.getQueryOptions().hasExpandFilter());
                            Assertions.assertFalse(Level2expandItem.getQueryOptions().hasFilterFilter());
                            Assertions.assertFalse(Level2expandItem.getQueryOptions().hasSelectFilter());
                            Assertions.assertFalse(Level2expandItem.getQueryOptions().hasOrderByFilter());
                            Assertions.assertFalse(Level2expandItem.getQueryOptions().hasSkipFilter());
                            Assertions.assertFalse(Level2expandItem.getQueryOptions().hasCountFilter());
                            break;
                        default:
                            Assertions.fail("Did not find expected expandItem!");
                            break;
                    }
                }
            } else if (expandItem.getPath().equals("Locations")) {
                Assertions.assertEquals("Locations", expandItem.getPath());
                Assertions.assertNotNull(expandItem.getQueryOptions());
                Assertions.assertTrue(expandItem.getQueryOptions().hasTopFilter());

                Assertions.assertFalse(expandItem.getQueryOptions().hasExpandFilter());
                Assertions.assertFalse(expandItem.getQueryOptions().hasFilterFilter());
                Assertions.assertFalse(expandItem.getQueryOptions().hasSelectFilter());
                Assertions.assertFalse(expandItem.getQueryOptions().hasOrderByFilter());
                Assertions.assertFalse(expandItem.getQueryOptions().hasSkipFilter());
                Assertions.assertFalse(expandItem.getQueryOptions().hasCountFilter());
            } else {
                Assertions.fail("Did not find expected expandItem!");
            }
        }

        init(ODataConstants.QueryOptions.EXPAND
                     + EQ
                     + "Observations/FeatureOfInterest($select=id),Thing/Locations");
        options = (QueryOptions) parser.queryOptions().accept(new STAQueryOptionVisitor());
        Assertions.assertTrue(options.hasExpandFilter());
        Assertions.assertNotNull(options.getExpandFilter());

        items = (options.getExpandFilter().getItems());

        for (ExpandItem expandItem : items) {
            // Assert that rewrite worked
            Assertions.assertFalse(expandItem.getPath().contains("/"));
        }
    }

    /**
     * Checks if Requests such as
     * $expand=Datastreams($select=name),Datastreams($select=test)
     * are flagged as incorrect as they are ambiguous
     */
    @Test
    public void invalidAmbiguousQueryOptions() {
        init(ODataConstants.QueryOptions.EXPAND
                     + EQ
                     + "Datastreams($select=name),Datastreams($select=test)");
        Assertions.assertThrows(
                STAInvalidQueryError.class,
                () -> parser.queryOptions().accept(new STAQueryOptionVisitor())
        );

        init(ODataConstants.QueryOptions.EXPAND
                     + EQ
                     + "Datastreams($orderby=name),Datastreams($orderby=test)");
        Assertions.assertThrows(
                STAInvalidQueryError.class,
                () -> parser.queryOptions().accept(new STAQueryOptionVisitor())
        );

        init(ODataConstants.QueryOptions.EXPAND
                     + EQ
                     + "Datastreams($filter=id eq '2'),Datastreams($filter=id eq '4')");
        Assertions.assertThrows(
                STAInvalidQueryError.class,
                () -> parser.queryOptions().accept(new STAQueryOptionVisitor())
        );

        init(ODataConstants.QueryOptions.EXPAND
                     + EQ
                     + "Datastreams($skip=15),Datastreams($skip=52)");
        Assertions.assertThrows(
                STAInvalidQueryError.class,
                () -> parser.queryOptions().accept(new STAQueryOptionVisitor())
        );

        init(ODataConstants.QueryOptions.EXPAND
                     + EQ
                     + "Datastreams($top=15),Datastreams($top=52)");
        Assertions.assertThrows(
                STAInvalidQueryError.class,
                () -> parser.queryOptions().accept(new STAQueryOptionVisitor())
        );
    }
}
