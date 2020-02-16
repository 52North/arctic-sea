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
import org.n52.shetland.filter.SelectFilter;
import org.n52.shetland.filter.SkipTopFilter;
import org.n52.shetland.ogc.filter.FilterConstants;

/**
 * Basic Test cases for testing $select Query Option.
 * Note: Parser must start from root queryOptions() to match EOF
 *
 * @author <a href="mailto:j.speckamp@52north.org">Jan Speckamp</a>
 */
public class SelectQueryOptionTest extends QueryOptionTests {

    @Test
    public void testInvalidSelectOption() {
        // May not be blank
        init(SELECT_QO + ",");
        Assertions.assertThrows(
                NullPointerException.class,
                () -> parser.queryOptions().systemQueryOption(0).select().accept(new ODataQueryVisitor())
        );

        // May not have blank subgroups
        init(SELECT_QO + "test,");
        Assertions.assertThrows(
                IllegalStateException.class,
                () -> parser.queryOptions().systemQueryOption(0).select().accept(new ODataQueryVisitor())
        );
        init(SELECT_QO + ",test");
        Assertions.assertThrows(
                NullPointerException.class,
                () -> parser.queryOptions().systemQueryOption(0).select().accept(new ODataQueryVisitor())
        );
        init(SELECT_QO + "test,,test");
        Assertions.assertThrows(
                NullPointerException.class,
                () -> parser.queryOptions().systemQueryOption(0).select().accept(new ODataQueryVisitor())
        );
        init(SELECT_QO + "test , test");
        Assertions.assertThrows(
                IllegalStateException.class,
                () -> parser.queryOptions().systemQueryOption(0).select().accept(new ODataQueryVisitor())
        );

        // May not be numeric
        init(SELECT_QO + "4444");
        Assertions.assertThrows(
                IllegalStateException.class,
                () -> parser.queryOptions().systemQueryOption(0).select().accept(new ODataQueryVisitor())
        );

        // May not include numbers
        init(SELECT_QO + "features123");
        Assertions.assertThrows(
                NullPointerException.class,
                () -> parser.queryOptions().systemQueryOption(0).select().accept(new ODataQueryVisitor())
        );

        // May not be empty
        init(SELECT_QO + "");
        Assertions.assertThrows(
                NullPointerException.class,
                () -> parser.queryOptions().systemQueryOption(0).select().accept(new ODataQueryVisitor())
        );

        // May not include navigationProperties empty
        init(SELECT_QO + "test/test");
        Assertions.assertThrows(
                IllegalStateException.class,
                () -> parser.queryOptions().systemQueryOption(0).select().accept(new ODataQueryVisitor())
        );
    }

    @Test
    public void testValidTopOption() {
        SelectFilter filter;
        String val;
        String[] split;

        // Check simple property
        val = "test";
        init(SELECT_QO + val);
        filter = (SelectFilter) parser.queryOptions().systemQueryOption(0).select().accept(new ODataQueryVisitor());
        Assertions.assertEquals(filter.getItems().get(0).getPath(), val);

        // check two properties no space
        val = "test,testTwo";
        split = val.split(",");
        init(SELECT_QO + val);
        filter = (SelectFilter) parser.queryOptions().systemQueryOption(0).select().accept(new ODataQueryVisitor());
        for (int i = 0; i < split.length; i++) {
            Assertions.assertEquals(filter.getItems().get(i).getPath(), split[i].trim());
        }

        // check two properties with space
        val = "test, testTwo";
        split = val.split(",");
        init(SELECT_QO + val);
        filter = (SelectFilter) parser.queryOptions().systemQueryOption(0).select().accept(new ODataQueryVisitor());
        for (int i = 0; i < split.length; i++) {
            Assertions.assertEquals(filter.getItems().get(i).getPath(), split[i].trim());
        }

        // check two properties with more space
        val = "test, testTwo";
        split = val.split(",");
        init(SELECT_QO + val);
        filter = (SelectFilter) parser.queryOptions().systemQueryOption(0).select().accept(new ODataQueryVisitor());
        for (int i = 0; i < split.length; i++) {
            Assertions.assertEquals(filter.getItems().get(i).getPath(), split[i].trim());
        }

        // check 2+ properties
        val = "testZero, testOne, testTwo, testThree";
        split = val.split(",");
        init(SELECT_QO + val);
        filter = (SelectFilter) parser.queryOptions().systemQueryOption(0).select().accept(new ODataQueryVisitor());
        for (int i = 0; i < split.length; i++) {
            Assertions.assertEquals(filter.getItems().get(i).getPath(), split[i].trim());
        }
    }
}
