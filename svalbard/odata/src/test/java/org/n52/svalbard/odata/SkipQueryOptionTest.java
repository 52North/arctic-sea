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
import org.n52.shetland.filter.SkipTopFilter;
import org.n52.shetland.oasis.odata.ODataConstants;
import org.n52.shetland.ogc.filter.FilterConstants;

/**
 * Basic Test cases for testing $skip Query Option.
 * Note: Parser must start from root queryOptions() to match EOF
 *
 * @author <a href="mailto:j.speckamp@52north.org">Jan Speckamp</a>
 */
public class SkipQueryOptionTest extends QueryOptionTests {

    @Test
    public void testInvalidSkipOption() {
        init(ODataConstants.QueryOptions.SKIP + EQ + "sdf");
        Assertions.assertThrows(
                IllegalStateException.class,
                () -> parser.queryOptions().systemQueryOption(0).skip().accept(new ODataQueryVisitor())
        );

        init(ODataConstants.QueryOptions.SKIP + EQ + "1.1");
        Assertions.assertThrows(
                IllegalStateException.class,
                () -> parser.queryOptions().systemQueryOption(0).skip().accept(new ODataQueryVisitor())
        );

        init(ODataConstants.QueryOptions.SKIP + EQ + "");
        Assertions.assertThrows(
                NullPointerException.class,
                () -> parser.queryOptions().systemQueryOption(0).skip().accept(new ODataQueryVisitor())
        );

        init(ODataConstants.QueryOptions.SKIP + EQ + "123" + Long.MAX_VALUE);
        Assertions.assertThrows(
                NumberFormatException.class,
                () -> parser.queryOptions().systemQueryOption(0).skip().accept(new ODataQueryVisitor())
        );
    }

    @Test
    public void testValidSkipOption() {
        int val = 1;
        SkipTopFilter filter;
        init(ODataConstants.QueryOptions.SKIP + EQ + val);
        filter = (SkipTopFilter) parser.queryOptions().systemQueryOption(0).skip().accept(new ODataQueryVisitor());
        Assertions.assertEquals(filter.getOperator(), FilterConstants.SkipTopOperator.Skip);
        Assertions.assertEquals(filter.getValue(), val);

        val = 0;
        init(ODataConstants.QueryOptions.SKIP + EQ + val);
        filter = (SkipTopFilter) parser.queryOptions().systemQueryOption(0).skip().accept(new ODataQueryVisitor());
        Assertions.assertEquals(filter.getOperator(), FilterConstants.SkipTopOperator.Skip);
        Assertions.assertEquals(filter.getValue(), val);

        val = 100000;
        init(ODataConstants.QueryOptions.SKIP + EQ + val);
        filter = (SkipTopFilter) parser.queryOptions().systemQueryOption(0).skip().accept(new ODataQueryVisitor());
        Assertions.assertEquals(filter.getOperator(), FilterConstants.SkipTopOperator.Skip);
        Assertions.assertEquals(filter.getValue(), val);

        long longval = Long.MAX_VALUE;
        init(ODataConstants.QueryOptions.SKIP + EQ + longval);
        filter = (SkipTopFilter) parser.queryOptions().systemQueryOption(0).skip().accept(new ODataQueryVisitor());
        Assertions.assertEquals(filter.getOperator(), FilterConstants.SkipTopOperator.Skip);
        Assertions.assertEquals(filter.getValue(), longval);
    }
}
