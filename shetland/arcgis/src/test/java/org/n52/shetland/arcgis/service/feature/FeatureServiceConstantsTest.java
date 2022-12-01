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
package org.n52.shetland.arcgis.service.feature;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class FeatureServiceConstantsTest implements FeatureServiceConstants.WhereOperators {

    private static final String FIELD = "field";
    private static final String VALUE = "value";
    private static final String VALUE_2 = "value2";
    private static final Double VALUE_DOUBLE = 1.2;
    private static final Double VALUE_2_DOUBLE = 2.1;

    @Test
    public void greaterThanString() {
        assertEquals("field >= 'value'", greaterThan(FIELD, VALUE));
    }

    @Test
    public void greaterThanDouble() {
        assertEquals("field >= 1.2", greaterThan(FIELD, VALUE_DOUBLE));
    }

    @Test
    public void lesThanString() {
        assertEquals("field <= 'value'", lessThan(FIELD, VALUE));
    }

    @Test
    public void lessThanDouble() {
        assertEquals("field <= 1.2", lessThan(FIELD, VALUE_DOUBLE));
    }

    @Test
    public void greaterString() {
        assertEquals("field > 'value'", greater(FIELD, VALUE));
    }

    @Test
    public void greaterDouble() {
        assertEquals("field > 1.2", greater(FIELD, VALUE_DOUBLE));
    }

    @Test
    public void lessString() {
        assertEquals("field < 'value'", less(FIELD, VALUE));
    }

    @Test
    public void lessDouble() {
        assertEquals("field < 1.2", less(FIELD, VALUE_DOUBLE));
    }

    @Test
    public void equalString() {
        assertEquals("field = 'value'", equal(FIELD, VALUE));
    }

    @Test
    public void equalDouble() {
        assertEquals("field = 1.2", equal(FIELD, VALUE_DOUBLE));
    }

    @Test
    public void notEqualString() {
        assertEquals("field <> 'value'", notEqual(FIELD, VALUE));
    }

    @Test
    public void notEqualDouble() {
        assertEquals("field <> 1.2", notEqual(FIELD, VALUE_DOUBLE));
    }

    @Test
    public void likeString() {
        assertEquals("field LIKE 'value'", like(FIELD, VALUE));
    }

    @Test
    public void likeDouble() {
        assertEquals("field LIKE 1.2", like(FIELD, VALUE_DOUBLE));
    }

    @Test
    public void andString() {
        assertEquals("field AND value", and(FIELD, VALUE));
    }

    @Test
    public void andDouble() {
        assertEquals("field AND 1.2", and(FIELD, VALUE_DOUBLE));
    }

    @Test
    public void orString() {
        assertEquals("field OR value", or(FIELD, VALUE));
    }

    @Test
    public void orDouble() {
        assertEquals("field OR 1.2", or(FIELD, VALUE_DOUBLE));
    }

    @Test
    public void isString() {
        assertEquals("field IS 'value'", is(FIELD, VALUE));
    }

    @Test
    public void isDouble() {
        assertEquals("field IS 1.2", is(FIELD, VALUE_DOUBLE));
    }

    @Test
    public void isNotString() {
        assertEquals("field IS_NOT 'value'", isNot(FIELD, VALUE));
    }

    @Test
    public void isNotDouble() {
        assertEquals("field IS_NOT 1.2", isNot(FIELD, VALUE_DOUBLE));
    }

    @Test
    public void inString() {
        assertEquals("field IN ('value')", in(FIELD, VALUE));
    }

    @Test
    public void inDouble() {
        assertEquals("field IN (1.2)", in(FIELD, VALUE_DOUBLE));
    }

    @Test
    public void inStringMulti() {
        assertEquals("field IN ('value','value2')", in(FIELD, VALUE, VALUE_2));
    }

    @Test
    public void inDoubleMulti() {
        assertEquals("field IN (1.2,2.1)", in(FIELD, VALUE_DOUBLE, VALUE_2_DOUBLE));
    }

    @Test
    public void notInString() {
        assertEquals("field NOT_IN ('value')", notIn(FIELD, VALUE));
    }

    @Test
    public void notInDouble() {
        assertEquals("field NOT_IN (1.2)", notIn(FIELD, VALUE_DOUBLE));
    }

    @Test
    public void notInStringMulti() {
        assertEquals("field NOT_IN ('value','value2')", notIn(FIELD, VALUE, VALUE_2));
    }

    @Test
    public void notInDoubleMulti() {
        assertEquals("field NOT_IN (1.2,2.1)", notIn(FIELD, VALUE_DOUBLE, VALUE_2_DOUBLE));
    }

    @Test
    public void betweenString() {
        assertEquals("field BETWEEN 'value' AND 'value2'", between(FIELD, VALUE, VALUE_2));
    }

    @Test
    public void betweenDouble() {
        assertEquals("field BETWEEN 1.2 AND 2.1", between(FIELD, VALUE_DOUBLE, VALUE_2_DOUBLE));
    }
}
