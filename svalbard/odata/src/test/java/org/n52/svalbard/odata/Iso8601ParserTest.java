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

import org.joda.time.DateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.n52.shetland.ogc.gml.time.TimeInstant;
import org.n52.svalbard.odata.expr.temporal.TimeValueExpr;

/**
 * @author <a href="mailto:j.speckamp@52north.org">Jan Speckamp</a>
 */
public class Iso8601ParserTest extends QueryOptionTests {

    private final String ERROR_NOT_EQUAL = "Could not parse time!";

    @Test
    public void testMissingDigits() {
        // may not me malformed (missing digits)
        init("201-06-01T00:00:00");
        Assertions.assertThrows(
                Exception.class,
                () -> parser.timeExpr().accept(new ODataQueryVisitor())
        );
        init("2010-0-01T00:00:00");
        Assertions.assertThrows(
                Exception.class,
                () -> parser.timeExpr().accept(new ODataQueryVisitor())
        );
        init("2010-06-0T00:00:00");
        Assertions.assertThrows(
                Exception.class,
                () -> parser.timeExpr().accept(new ODataQueryVisitor())
        );
        init("2010-06-01T0:00:00");
        Assertions.assertThrows(
                Exception.class,
                () -> parser.timeExpr().accept(new ODataQueryVisitor())
        );
        init("2010-06-01T00:0:00");
        Assertions.assertThrows(
                Exception.class,
                () -> parser.timeExpr().accept(new ODataQueryVisitor())
        );
        init("2010-06-01T00:00:0");
        Assertions.assertThrows(
                Exception.class,
                () -> parser.timeExpr().accept(new ODataQueryVisitor())
        );
    }

    @Test
    public void testMissingControlCharacters() {
        init("2010-06-0100:00:00Z");
        Assertions.assertThrows(
                Exception.class,
                () -> parser.timeExpr().accept(new ODataQueryVisitor())
        );
        init("2010-06-01T00:00:00");
        Assertions.assertThrows(
                Exception.class,
                () -> parser.timeExpr().accept(new ODataQueryVisitor())
        );
        init("2010-06-0100:00:00");
        Assertions.assertThrows(
                Exception.class,
                () -> parser.timeExpr().accept(new ODataQueryVisitor())
        );
        init("2010-06-0100:00:00");
        Assertions.assertThrows(
                Exception.class,
                () -> parser.timeExpr().accept(new ODataQueryVisitor())
        );
        init("2010-06-0100:00:00");
        Assertions.assertThrows(
                Exception.class,
                () -> parser.timeExpr().accept(new ODataQueryVisitor())
        );
    }

    @Test
    public void testInvalidFormat() {
        // May no be empty
        init("");
        Assertions.assertThrows(
                Exception.class,
                () -> parser.timeExpr().accept(new ODataQueryVisitor())
        );
        init("201006-01T00:00:00");
        Assertions.assertThrows(
                Exception.class,
                () -> parser.timeExpr().accept(new ODataQueryVisitor())
        );
        init("2010-0601T00:00:00");
        Assertions.assertThrows(
                Exception.class,
                () -> parser.timeExpr().accept(new ODataQueryVisitor())
        );
        init("2010-06-01T0000:00");
        Assertions.assertThrows(
                Exception.class,
                () -> parser.timeExpr().accept(new ODataQueryVisitor())
        );
        init("2010-06-01T00:00.00");
        Assertions.assertThrows(
                Exception.class,
                () -> parser.timeExpr().accept(new ODataQueryVisitor())
        );
        init("2010-06-01T00!0000");
        Assertions.assertThrows(
                Exception.class,
                () -> parser.timeExpr().accept(new ODataQueryVisitor())
        );
        init("2010-06-01T00:0000");
        Assertions.assertThrows(
                Exception.class,
                () -> parser.timeExpr().accept(new ODataQueryVisitor())
        );
        init("2010-06-01T00:0000");
        Assertions.assertThrows(
                Exception.class,
                () -> parser.timeExpr().accept(new ODataQueryVisitor())
        );

    }

    @Test
    public void testTooManyDigits() {
        // may not me malformed (too many digits)
        init("20111-06-01T00:00:00");
        Assertions.assertThrows(
                Exception.class,
                () -> parser.timeExpr().accept(new ODataQueryVisitor())
        );
        init("2010-011-01T00:00:00");
        Assertions.assertThrows(
                Exception.class,
                () -> parser.timeExpr().accept(new ODataQueryVisitor())
        );
        init("2010-06-011T00:00:00");
        Assertions.assertThrows(
                Exception.class,
                () -> parser.timeExpr().accept(new ODataQueryVisitor())
        );
        init("2010-06-01T011:00:00");
        Assertions.assertThrows(
                Exception.class,
                () -> parser.timeExpr().accept(new ODataQueryVisitor())
        );
        init("2010-06-01T00:011:00");
        Assertions.assertThrows(
                Exception.class,
                () -> parser.timeExpr().accept(new ODataQueryVisitor())
        );
        init("2010-06-01T00:00:011");
        Assertions.assertThrows(
                Exception.class,
                () -> parser.timeExpr().accept(new ODataQueryVisitor())
        );
    }

    @Test
    public void testInvalidTimezone() {
        // May not have malformed timezone
        // Can't be tested because timeExpr() does not match EOL and last digit is therefore not tokenized
        //        init("2010-06-01T00:00:00Z2");
        //        Object accept = parser.timeExpr().accept(new ODataQueryVisitor());
        //        Assertions.assertThrows(
        //                Exception.class,
        //                () -> parser.timeExpr().accept(new ODataQueryVisitor())
        //        );
        // Can't be tested because timeExpr() does not match EOL and last digit is therefore not tokenized
        //        init("2010-06-01T00:00:00Z+02:00");
        //        Assertions.assertThrows(
        //                Exception.class,
        //                () -> parser.timeExpr().accept(new ODataQueryVisitor())
        //        );
        init("2010-06-01T00:00:00~02:00");
        Assertions.assertThrows(
                Exception.class,
                () -> parser.timeExpr().accept(new ODataQueryVisitor())
        );
        init("2010-06-01T00:00:00+-02:00");
        Assertions.assertThrows(
                Exception.class,
                () -> parser.timeExpr().accept(new ODataQueryVisitor())
        );
        init("2010-06-01T00:00:00+222:00");
        Assertions.assertThrows(
                Exception.class,
                () -> parser.timeExpr().accept(new ODataQueryVisitor())
        );
        init("2010-06-01T00:00:00-02:002");
        Assertions.assertThrows(
                Exception.class,
                () -> parser.timeExpr().accept(new ODataQueryVisitor())
        );
    }

    @Test
    public void testValidDate() {
        TimeInstant reference, actual;
        String data;
        // Default Timezon
        data = "2010-06-01T00:00:00Z";
        init(data);
        actual = (TimeInstant) ((TimeValueExpr) parser.timeExpr().accept(new ODataQueryVisitor())).getTime();
        reference = new TimeInstant(DateTime.parse(data));
        Assertions.assertEquals(reference,
                                actual,
                                ERROR_NOT_EQUAL);

        // Specify timezone
        data = "2010-06-01T00:00:00+02:00";
        init(data);
        actual = (TimeInstant) ((TimeValueExpr) parser.timeExpr().accept(new ODataQueryVisitor())).getTime();
        reference = new TimeInstant(DateTime.parse(data));
        Assertions.assertEquals(reference,
                                actual,
                                ERROR_NOT_EQUAL);

        // Specify millis
        data = "2010-06-01T00:00:00.123+02:00";
        init(data);
        actual = (TimeInstant) ((TimeValueExpr) parser.timeExpr().accept(new ODataQueryVisitor())).getTime();
        reference = new TimeInstant(DateTime.parse(data));
        Assertions.assertEquals(reference,
                                actual,
                                ERROR_NOT_EQUAL);
    }
}
