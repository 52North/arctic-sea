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

package org.n52.svalbard.odata.core;

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
import org.n52.svalbard.odata.core.expr.MemberExpr;
import org.n52.svalbard.odata.core.expr.StringValueExpr;
import org.n52.svalbard.odata.core.expr.arithmetic.NumericValueExpr;
import org.n52.svalbard.odata.core.expr.binary.ComparisonExpr;

import java.util.HashSet;
import java.util.Set;

/**
 * @author <a href="mailto:j.speckamp@52north.org">Jan Speckamp</a>
 */
@SuppressWarnings("unchecked")
public class ExpandQueryOptionTest extends QueryOptionTests {

    @Test
    public void testValidExpandOption() {
        String val, nested;
        ExpandFilter actual, reference;
        QueryOptions defaultQO = new QueryOptions("", null);
        Set<FilterClause> nestedFilters;

        // Simple
        val = "test";
        init(ODataConstants.QueryOptions.EXPAND + EQ + val);
        actual = (ExpandFilter) ((QueryOptions) parser.queryOptions()
                                                      .accept(new STAQueryOptionVisitor())).getExpandOption();
        reference = new ExpandFilter(new ExpandItem(val, defaultQO));
        Assertions.assertEquals(reference, actual);

        // Resolve nested Expand
        val = "test";
        nested = "nested";
        init(ODataConstants.QueryOptions.EXPAND + EQ + val + "/" + nested);
        actual = (ExpandFilter) ((QueryOptions) parser.queryOptions()
                                                      .accept(new STAQueryOptionVisitor())).getExpandOption();
        nestedFilters = new HashSet<>();
        nestedFilters.add(new ExpandFilter(new ExpandItem(nested, defaultQO)));
        reference = new ExpandFilter(new ExpandItem(val, new QueryOptions("", nestedFilters)));
        Assertions.assertEquals(reference, actual);

        // Expand with queryOptions
        val = "test";
        String queryOption = "($filter=id eq '2')";
        init(ODataConstants.QueryOptions.EXPAND + EQ + val + queryOption);
        actual = (ExpandFilter) ((QueryOptions) parser.queryOptions()
                                                      .accept(new STAQueryOptionVisitor())).getExpandOption();
        nestedFilters = new HashSet<>();
        nestedFilters.add(new FilterFilter(new ComparisonExpr(FilterConstants.ComparisonOperator.PropertyIsEqualTo,
                                                              new MemberExpr("id"),
                                                              new StringValueExpr("2"))));
        reference = new ExpandFilter(new ExpandItem(val, new QueryOptions("", nestedFilters)));
        Assertions.assertEquals(reference, actual);

        // Expand with queryOptions
        val = "test";
        queryOption = "($top=52)";
        init(ODataConstants.QueryOptions.EXPAND + EQ + val + queryOption);
        actual = (ExpandFilter) ((QueryOptions) parser.queryOptions()
                                                      .accept(new STAQueryOptionVisitor())).getExpandOption();
        nestedFilters = new HashSet<>();
        nestedFilters.add(new SkipTopFilter(FilterConstants.SkipTopOperator.Top, 52L));
        reference = new ExpandFilter(new ExpandItem(val, new QueryOptions("", nestedFilters)));
        Assertions.assertEquals(reference, actual);

        // Expand with queryOptions
        val = "test";
        queryOption = "($skip=52)";
        init(ODataConstants.QueryOptions.EXPAND + EQ + val + queryOption);
        actual = (ExpandFilter) ((QueryOptions) parser.queryOptions()
                                                      .accept(new STAQueryOptionVisitor())).getExpandOption();
        nestedFilters = new HashSet<>();
        nestedFilters.add(new SkipTopFilter(FilterConstants.SkipTopOperator.Skip, 52L));
        reference = new ExpandFilter(new ExpandItem(val, new QueryOptions("", nestedFilters)));
        Assertions.assertEquals(reference, actual);

        // Expand with queryOptions
        val = "test";
        queryOption = "($select=id)";
        init(ODataConstants.QueryOptions.EXPAND + EQ + val + queryOption);
        actual = (ExpandFilter) ((QueryOptions) parser.queryOptions()
                                                      .accept(new STAQueryOptionVisitor())).getExpandOption();
        nestedFilters = new HashSet<>();
        nestedFilters.add(new SelectFilter("id"));
        reference = new ExpandFilter(new ExpandItem(val, new QueryOptions("", nestedFilters)));
        Assertions.assertEquals(reference, actual);

        // Expand with queryOptions
        val = "test";
        queryOption = "($expand=nested)";
        init(ODataConstants.QueryOptions.EXPAND + EQ + val + queryOption);
        actual = (ExpandFilter) ((QueryOptions) parser.queryOptions()
                                                      .accept(new STAQueryOptionVisitor())).getExpandOption();
        nestedFilters = new HashSet<>();
        nestedFilters.add(new ExpandFilter(new ExpandItem(nested, defaultQO)));
        reference = new ExpandFilter(new ExpandItem(val, new QueryOptions("", nestedFilters)));
        Assertions.assertEquals(reference, actual);

        // Expand with queryOptions
        val = "test";
        queryOption = "($orderby=id asc)";
        init(ODataConstants.QueryOptions.EXPAND + EQ + val + queryOption);
        actual = (ExpandFilter) ((QueryOptions) parser.queryOptions()
                                                      .accept(new STAQueryOptionVisitor())).getExpandOption();
        nestedFilters = new HashSet<>();
        nestedFilters.add(new OrderByFilter(new OrderProperty("id", FilterConstants.SortOrder.ASC)));
        reference = new ExpandFilter(new ExpandItem(val, new QueryOptions("", nestedFilters)));
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
        Assertions.assertTrue(options.hasExpandOption());
        Assertions.assertTrue(options.getExpandOption() instanceof ExpandFilter);
        Set<ExpandItem> items = (options.getExpandOption().getItems());
        Assertions.assertNotNull(items);
        Assertions.assertEquals(2, items.size());

        for (ExpandItem obs : items) {
            if (obs.getPath().equals("Observations")) {
                Assertions.assertEquals("Observations", obs.getPath());
                Assertions.assertTrue(obs.getQueryOptions().hasExpandOption());
                Assertions.assertTrue(obs.getQueryOptions().hasFilterOption());
                Assertions.assertTrue(obs.getQueryOptions().hasSelectOption());
                Assertions.assertTrue(obs.getQueryOptions().hasOrderByOption());
                Assertions.assertTrue(obs.getQueryOptions().hasSkipOption());
                Assertions.assertTrue(obs.getQueryOptions().hasTopOption());
                Assertions.assertTrue(obs.getQueryOptions().hasCountOption());

                //TODO: Implements equals() Method on Expr Interface to properly compare filters
                Set<FilterClause> filters = new HashSet<>();
                filters.add(new FilterFilter(new ComparisonExpr(FilterConstants.ComparisonOperator.PropertyIsEqualTo,
                                                                new MemberExpr("result"),
                                                                new NumericValueExpr("1"))));

                filters.add(new ExpandFilter(
                        new ExpandItem("FeatureOfInterest", new QueryOptions("", null))));
                filters.add(new SelectFilter("id"));
                filters.add(new OrderByFilter(new OrderProperty("id")));
                filters.add(new SkipTopFilter(FilterConstants.SkipTopOperator.Skip, 5L));
                filters.add(new SkipTopFilter(FilterConstants.SkipTopOperator.Top, 10L));
                filters.add(new CountFilter(true));

                Assertions.assertEquals(new QueryOptions("", filters), obs.getQueryOptions());
            } else if (obs.getPath().equals("ObservedProperty")) {
                Assertions.assertEquals("ObservedProperty", obs.getPath());
                Assertions.assertNotNull(obs.getQueryOptions());
                Assertions.assertTrue(obs.getQueryOptions().hasTopOption());

                Assertions.assertFalse(obs.getQueryOptions().hasExpandOption());
                Assertions.assertFalse(obs.getQueryOptions().hasFilterOption());
                Assertions.assertFalse(obs.getQueryOptions().hasSelectOption());
                Assertions.assertFalse(obs.getQueryOptions().hasOrderByOption());
                Assertions.assertFalse(obs.getQueryOptions().hasSkipOption());
                Assertions.assertFalse(obs.getQueryOptions().hasCountOption());
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
        init(ODataConstants.QueryOptions.EXPAND
                     + EQ
                     + "Datastreams/Sensors/Datastreams");
        QueryOptions options =
                (QueryOptions) parser.queryOptions().accept(new STAQueryOptionVisitor());
        Assertions.assertTrue(options.hasExpandOption());
        Assertions.assertTrue(options.getExpandOption() instanceof ExpandFilter);

        Set<ExpandItem> items = (options.getExpandOption().getItems());

        Assertions.assertNotNull(items);
        Assertions.assertEquals(1, items.size());

        ExpandItem level1 = items.toArray(new ExpandItem[] {})[0];
        Assertions.assertTrue(level1.getQueryOptions().hasExpandOption());
        Assertions.assertFalse(level1.getQueryOptions().hasFilterOption());
        Assertions.assertFalse(level1.getQueryOptions().hasSelectOption());
        Assertions.assertFalse(level1.getQueryOptions().hasOrderByOption());
        Assertions.assertFalse(level1.getQueryOptions().hasSkipOption());
        Assertions.assertTrue(level1.getQueryOptions().hasTopOption());
        Assertions.assertFalse(level1.getQueryOptions().hasCountOption());
        Assertions.assertEquals("Datastreams", level1.getPath());

        ExpandItem level2 = level1.getQueryOptions().getExpandOption().getItems().toArray(new ExpandItem[] {})[0];
        Assertions.assertTrue(level2.getQueryOptions().hasExpandOption());
        Assertions.assertFalse(level2.getQueryOptions().hasFilterOption());
        Assertions.assertFalse(level2.getQueryOptions().hasSelectOption());
        Assertions.assertFalse(level2.getQueryOptions().hasOrderByOption());
        Assertions.assertFalse(level2.getQueryOptions().hasSkipOption());
        Assertions.assertTrue(level2.getQueryOptions().hasTopOption());
        Assertions.assertFalse(level2.getQueryOptions().hasCountOption());
        Assertions.assertEquals("Sensors", level2.getPath());
        Assertions.assertEquals(1, level2.getQueryOptions().getExpandOption().getItems().size());

        ExpandItem level3 = level2.getQueryOptions().getExpandOption().getItems().toArray(new ExpandItem[] {})[0];
        Assertions.assertFalse(level3.getQueryOptions().hasExpandOption());
        Assertions.assertFalse(level3.getQueryOptions().hasFilterOption());
        Assertions.assertFalse(level3.getQueryOptions().hasSelectOption());
        Assertions.assertFalse(level3.getQueryOptions().hasOrderByOption());
        Assertions.assertFalse(level3.getQueryOptions().hasSkipOption());
        Assertions.assertTrue(level3.getQueryOptions().hasTopOption());
        Assertions.assertFalse(level3.getQueryOptions().hasCountOption());
        Assertions.assertEquals("Datastreams", level3.getPath());
    }
}
