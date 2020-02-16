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

package org.n52.svalbard.odata;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.n52.shetland.filter.AbstractPathFilter;
import org.n52.shetland.filter.CountFilter;
import org.n52.shetland.filter.ExpandFilter;
import org.n52.shetland.filter.FilterFilter;
import org.n52.shetland.filter.OrderByFilter;
import org.n52.shetland.filter.OrderProperty;
import org.n52.shetland.filter.PathFilterItem;
import org.n52.shetland.filter.SelectFilter;
import org.n52.shetland.filter.SkipTopFilter;
import org.n52.shetland.oasis.odata.ODataConstants;
import org.n52.shetland.oasis.odata.query.option.QueryOptions;
import org.n52.shetland.ogc.filter.AbstractSelectionClause;
import org.n52.shetland.ogc.filter.FilterClause;
import org.n52.shetland.ogc.filter.FilterConstants;
import org.n52.svalbard.odata.expr.MemberExpr;
import org.n52.svalbard.odata.expr.arithmetic.NumericValueExpr;
import org.n52.svalbard.odata.expr.binary.ComparisonExpr;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * @author <a href="mailto:j.speckamp@52north.org">Jan Speckamp</a>
 */
public class ComplexQueryOptionTest extends QueryOptionTests {

    @Test
    public void complexExpandOption() {
        init(ODataConstants.QueryOptions.EXPAND
                     + EQ
                     + "Observations($filter=result eq 1;$expand=FeatureOfInterest;$select=id;$orderby=id;"
                     + "$skip=5;$top=10;$count=true),ObservedProperty");
        QueryOptions options =
                (QueryOptions) parser.queryOptions().accept(new ODataQueryVisitor());
        Assertions.assertTrue(options.hasExpandOption());
        Assertions.assertTrue(options.getExpandOption() instanceof ExpandFilter);
        List<PathFilterItem> items = ((AbstractPathFilter) options.getExpandOption()).getItems();
        Assertions.assertTrue(items != null);
        Assertions.assertEquals(2, items.size());

        PathFilterItem obs = items.get(0);
        Assertions.assertEquals("Observations", obs.getPath());
        Assertions.assertEquals(7, obs.getFilters().size());

        List<FilterClause> filters = Arrays.asList(
                //TODO: Implemente equals() Method on Expr Interface to properly compare filters
                new FilterFilter(new ComparisonExpr(FilterConstants.ComparisonOperator.PropertyIsEqualTo,
                                                    new MemberExpr("result"),
                                                    new NumericValueExpr("1"))),
                new ExpandFilter(new PathFilterItem("FeatureOfInterest")),
                new SelectFilter(new PathFilterItem("id")),
                new OrderByFilter(new OrderProperty("id")),
                new SkipTopFilter(FilterConstants.SkipTopOperator.Skip, 5L),
                new SkipTopFilter(FilterConstants.SkipTopOperator.Top, 10L),
                new CountFilter(true)
        );
        Assertions.assertTrue(obs.getFilters().containsAll(filters));

        PathFilterItem obsProp = items.get(1);
        Assertions.assertEquals("ObservedProperty", obsProp.getPath());
        Assertions.assertTrue(obsProp.getFilters() == null);
    }

    @Test
    public void complexSelectListOption() {
        init(ODataConstants.QueryOptions.SELECT
                     + EQ
                     + "NamespacePreferredSupplier/AccountRepresentative,Address/Street,Location");
        QueryOptions options =
                (QueryOptions) parser.queryOptions().accept(new ODataQueryVisitor());
        Assertions.assertTrue(options.hasSelectOption());
        Assertions.assertTrue(options.getSelectOption() instanceof SelectFilter);
        List<PathFilterItem> items = ((AbstractPathFilter) options.getSelectOption()).getItems();
        Assertions.assertTrue(items != null);
        Assertions.assertEquals(3, items.size());
        PathFilterItem item1 = items.get(0);
        Assertions.assertEquals("NamespacePreferredSupplier/AccountRepresentative", item1.getPath());
        PathFilterItem item2 = items.get(1);
        Assertions.assertEquals("Address/Street", item2.getPath());
        PathFilterItem item3 = items.get(2);
        Assertions.assertEquals("Location", item3.getPath());
    }

    @Test
    public void complexSelectOption() {
        init(ODataConstants.QueryOptions.SELECT
                     + EQ
                     + "Addresses($filter=startswith(City,'H');$top=5;$orderby=Country/Name,City,Street)");
        QueryOptions options =
                (QueryOptions) parser.queryOptions().accept(new ODataQueryVisitor());
        Assertions.assertTrue(options.hasSelectOption());
        Assertions.assertTrue(options.getSelectOption() instanceof SelectFilter);
        List<PathFilterItem> items = ((AbstractPathFilter) options.getSelectOption()).getItems();
        Assertions.assertTrue(items != null);
        Assertions.assertEquals(1, items.size());
        PathFilterItem item = items.get(0);
        Assertions.assertEquals("Addresses", item.getPath());
        Assertions.assertEquals(3, item.getFilters().size());
        Set<FilterClause> filters = item.getFilters();
        for (FilterClause filterClause : filters) {
            if (filterClause instanceof SkipTopFilter) {
                Assertions.assertEquals(FilterConstants.SkipTopOperator.Top,
                                        ((SkipTopFilter) filterClause).getOperator());
                Assertions.assertEquals(5, ((SkipTopFilter) filterClause).getValue());
            } else if (filterClause instanceof OrderByFilter) {
                Assertions.assertEquals(3, ((OrderByFilter) filterClause).getSortProperties().size());
            } else if (filterClause instanceof AbstractSelectionClause) {
                // TODO expand
                Assertions.assertTrue(true);
            }
        }
    }
}
