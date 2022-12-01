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
package org.n52.svalbard.odata.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.n52.shetland.filter.SkipTopFilter;
import org.n52.shetland.oasis.odata.ODataConstants;
import org.n52.shetland.oasis.odata.query.option.QueryOptions;
import org.n52.shetland.ogc.filter.FilterConstants;

/**
 * Basic Test cases for testing $top Query Option.
 * Note: Parser must start from root queryOptions() to match EOF
 *
 * @author <a href="mailto:j.speckamp@52north.org">Jan Speckamp</a>
 */
@SuppressWarnings("unchecked")
public class TopQueryOptionTest extends QueryOptionTests {

    @Test
    public void testInvalidTopOption() {
        init(ODataConstants.QueryOptions.TOP + EQ + "sdf");
        Assertions.assertThrows(
                Exception.class,
                () -> parser.queryOptions().accept(new STAQueryOptionVisitor())
        );

        init(ODataConstants.QueryOptions.TOP + EQ + "1.1");
        Assertions.assertThrows(
                Exception.class,
                () -> parser.queryOptions().accept(new STAQueryOptionVisitor())
        );

        init(ODataConstants.QueryOptions.TOP + EQ + "");
        Assertions.assertThrows(
                Exception.class,
                () -> parser.queryOptions().accept(new STAQueryOptionVisitor())
        );

        init(ODataConstants.QueryOptions.TOP + EQ + "123" + Long.MAX_VALUE);
        Assertions.assertThrows(
                NumberFormatException.class,
                () -> parser.queryOptions().accept(new STAQueryOptionVisitor())
        );
    }

    @Test
    public void testValidTopOption() {
        int val = 1;
        SkipTopFilter filter;
        init(ODataConstants.QueryOptions.TOP + EQ + val);
        filter = ((QueryOptions) parser.queryOptions().accept(new STAQueryOptionVisitor())).getTopFilter();
        Assertions.assertEquals(filter.getOperator(), FilterConstants.SkipTopOperator.Top);
        Assertions.assertEquals(filter.getValue(), val);

        val = 0;
        init(ODataConstants.QueryOptions.TOP + EQ + val);
        filter = ((QueryOptions) parser.queryOptions().accept(new STAQueryOptionVisitor())).getTopFilter();
        Assertions.assertEquals(filter.getOperator(), FilterConstants.SkipTopOperator.Top);
        Assertions.assertEquals(filter.getValue(), val);

        val = 100000;
        init(ODataConstants.QueryOptions.TOP + EQ + val);
        filter = ((QueryOptions) parser.queryOptions().accept(new STAQueryOptionVisitor())).getTopFilter();
        Assertions.assertEquals(filter.getOperator(), FilterConstants.SkipTopOperator.Top);
        Assertions.assertEquals(filter.getValue(), val);

        long longval = Long.MAX_VALUE;
        init(ODataConstants.QueryOptions.TOP + EQ + longval);
        filter = ((QueryOptions) parser.queryOptions().accept(new STAQueryOptionVisitor())).getTopFilter();
        Assertions.assertEquals(filter.getOperator(), FilterConstants.SkipTopOperator.Top);
        Assertions.assertEquals(filter.getValue(), longval);
    }
}
