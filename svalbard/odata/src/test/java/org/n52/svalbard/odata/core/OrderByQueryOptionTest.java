/*
 * Copyright 2015-2021 52°North Initiative for Geospatial Open Source
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
import org.n52.shetland.filter.OrderByFilter;
import org.n52.shetland.oasis.odata.ODataConstants;
import org.n52.shetland.oasis.odata.query.option.QueryOptions;
import org.n52.shetland.ogc.filter.FilterConstants;

/**
 * @author <a href="mailto:j.speckamp@52north.org">Jan Speckamp</a>
 */
@SuppressWarnings("unchecked")
public class OrderByQueryOptionTest extends QueryOptionTests {

    @Test
    public void testInvalidOrderByFilter() {
        // May no be empty
        init(ODataConstants.QueryOptions.ORDERBY + EQ + "");
        Assertions.assertThrows(
                Exception.class,
                () -> parser.queryOptions().accept(new STAQueryOptionVisitor())
        );

        // May not be malformed
        init(ODataConstants.QueryOptions.ORDERBY + EQ + "test/test//");
        Assertions.assertThrows(
                NullPointerException.class,
                () -> parser.queryOptions().accept(new STAQueryOptionVisitor())
        );

        // May not have invalid sortOrder
        init(ODataConstants.QueryOptions.ORDERBY + EQ + "test nosc");
        Assertions.assertThrows(
                IllegalStateException.class,
                () -> parser.queryOptions().accept(new STAQueryOptionVisitor())
        );
    }

    @Test
    public void testValidOrderByFilter() {
        OrderByFilter filter;
        String val, sortOrder;

        // Check simple property
        val = "test";
        init(ODataConstants.QueryOptions.ORDERBY + EQ + val);
        filter = ((QueryOptions) parser.queryOptions()
                                       .accept(new STAQueryOptionVisitor())).getOrderByFilter();
        Assertions.assertEquals(val, filter.getSortProperties().get(0).getValueReference());
        Assertions.assertFalse(filter.getSortProperties().get(0).isSetSortOrder());

        // Check simple property asc
        val = "test";
        sortOrder = "asc";
        init(ODataConstants.QueryOptions.ORDERBY + EQ + val + " " + sortOrder);
        filter = ((QueryOptions) parser.queryOptions()
                                       .accept(new STAQueryOptionVisitor())).getOrderByFilter();
        Assertions.assertEquals(val, filter.getSortProperties().get(0).getValueReference());
        Assertions.assertEquals(FilterConstants.SortOrder.ASC, filter.getSortProperties().get(0).getSortOrder());

        // Check simple property desc
        val = "test";
        sortOrder = "desc";
        init(ODataConstants.QueryOptions.ORDERBY + EQ + val + " " + sortOrder);
        filter = ((QueryOptions) parser.queryOptions()
                                       .accept(new STAQueryOptionVisitor())).getOrderByFilter();
        Assertions.assertEquals(val, filter.getSortProperties().get(0).getValueReference());
        Assertions.assertEquals(FilterConstants.SortOrder.DESC, filter.getSortProperties().get(0).getSortOrder());

        // Check multiple property asc and desc
        String firstProp = "test";
        String secondProp = "testTwo";
        val = firstProp + ", " + secondProp;
        sortOrder = "asc";
        init(ODataConstants.QueryOptions.ORDERBY + EQ + val + " " + sortOrder);
        filter = ((QueryOptions) parser.queryOptions()
                                       .accept(new STAQueryOptionVisitor())).getOrderByFilter();
        Assertions.assertEquals(firstProp, filter.getSortProperties().get(0).getValueReference());
        Assertions.assertFalse(filter.getSortProperties().get(0).isSetSortOrder());
        Assertions.assertEquals(secondProp, filter.getSortProperties().get(1).getValueReference());
        Assertions.assertEquals(FilterConstants.SortOrder.ASC, filter.getSortProperties().get(1).getSortOrder());

        // TODO: expand tests
    }
}
