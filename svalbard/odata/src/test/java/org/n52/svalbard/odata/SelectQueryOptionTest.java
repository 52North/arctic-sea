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
import org.n52.shetland.oasis.odata.ODataConstants;

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
        init(ODataConstants.QueryOptions.SELECT + EQ + ",");
        Assertions.assertThrows(
                NullPointerException.class,
                () -> parser.queryOptions().systemQueryOption(0).select().accept(new ODataQueryVisitor())
        );

        // May not have blank subgroups
        init(ODataConstants.QueryOptions.SELECT + EQ + "test,");
        Assertions.assertThrows(
                IllegalStateException.class,
                () -> parser.queryOptions().systemQueryOption(0).select().accept(new ODataQueryVisitor())
        );
        init(ODataConstants.QueryOptions.SELECT + EQ + ",test");
        Assertions.assertThrows(
                NullPointerException.class,
                () -> parser.queryOptions().systemQueryOption(0).select().accept(new ODataQueryVisitor())
        );
        init(ODataConstants.QueryOptions.SELECT + EQ + "test,,test");
        Assertions.assertThrows(
                NullPointerException.class,
                () -> parser.queryOptions().systemQueryOption(0).select().accept(new ODataQueryVisitor())
        );
        init(ODataConstants.QueryOptions.SELECT + EQ + "test , test");
        Assertions.assertThrows(
                IllegalStateException.class,
                () -> parser.queryOptions().systemQueryOption(0).select().accept(new ODataQueryVisitor())
        );

        // May not be numeric
        init(ODataConstants.QueryOptions.SELECT + EQ + "4444");
        Assertions.assertThrows(
                IllegalStateException.class,
                () -> parser.queryOptions().systemQueryOption(0).select().accept(new ODataQueryVisitor())
        );

        // May not include numbers
        init(ODataConstants.QueryOptions.SELECT + EQ + "features123");
        Assertions.assertThrows(
                NullPointerException.class,
                () -> parser.queryOptions().systemQueryOption(0).select().accept(new ODataQueryVisitor())
        );

        // May not be empty
        init(ODataConstants.QueryOptions.SELECT + EQ + "");
        Assertions.assertThrows(
                NullPointerException.class,
                () -> parser.queryOptions().systemQueryOption(0).select().accept(new ODataQueryVisitor())
        );
    }

    @Test
    public void testValidSelectOption() {
        SelectFilter filter;
        String val;
        String[] split;

        // Check simple property
        val = "test";
        init(ODataConstants.QueryOptions.SELECT + EQ + val);
        filter = (SelectFilter) parser.queryOptions().systemQueryOption(0).select().accept(new ODataQueryVisitor());
        Assertions.assertEquals(val, filter.getItems().toArray(new String[] {})[0]);

        // check two properties no space
        val = "test,testTwo";
        split = val.split(",");
        init(ODataConstants.QueryOptions.SELECT + EQ + val);
        filter = (SelectFilter) parser.queryOptions().systemQueryOption(0).select().accept(new ODataQueryVisitor());
        for (int i = 0; i < split.length; i++) {
            Assertions.assertEquals(split[i].trim(), filter.getItems().toArray(new String[] {})[i]);
        }

        // check two properties with space
        val = "test, testTwo";
        split = val.split(",");
        init(ODataConstants.QueryOptions.SELECT + EQ + val);
        filter = (SelectFilter) parser.queryOptions().systemQueryOption(0).select().accept(new ODataQueryVisitor());
        for (int i = 0; i < split.length; i++) {
            Assertions.assertEquals(split[i].trim(), filter.getItems().toArray(new String[] {})[i]);
        }

        // check two properties with more space
        val = "test, testTwo";
        split = val.split(",");
        init(ODataConstants.QueryOptions.SELECT + EQ + val);
        filter = (SelectFilter) parser.queryOptions().systemQueryOption(0).select().accept(new ODataQueryVisitor());
        for (int i = 0; i < split.length; i++) {
            Assertions.assertEquals(split[i].trim(), filter.getItems().toArray(new String[] {})[i]);
        }

        // check 2+ properties
        val = "testZero, testOne, testTwo, testThree";
        split = val.split(",");
        init(ODataConstants.QueryOptions.SELECT + EQ + val);
        filter = (SelectFilter) parser.queryOptions().systemQueryOption(0).select().accept(new ODataQueryVisitor());
        for (int i = 0; i < split.length; i++) {
            Assertions.assertTrue(filter.getItems().contains(split[i].trim()));
        }
    }
}
