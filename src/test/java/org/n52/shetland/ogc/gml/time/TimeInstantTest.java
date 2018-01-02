/*
 * Copyright 2016-2018 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.ogc.gml.time;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.joda.time.DateTime;
import org.junit.Test;


/**
 * @since 1.0.0
 *
 */
public class TimeInstantTest {

    @Test
    public void isEmptyForDefaultConstructorTest() {
        assertTrue("new TimeInstant is NOT empty", new TimeInstant().isEmpty());
    }

    @Test
    public void isEmptyForConstructorWithNullTimeTest() {
        assertTrue("new TimeInstant(null) is NOT empty", new TimeInstant((DateTime) null).isEmpty());
    }

    @Test
    public void isNotEmptyForConstructorWithTimeAndNullIndeterminateValueTest() {
        assertFalse("new TimeInstant(new DateTime()) is empty", new TimeInstant(new DateTime(), null).isEmpty());
    }

    @Test
    public void isNotEmptyForConstructorWithNullTimeAndIndeterminateValueTest() {
        assertFalse("new TimeInstant(null) is empty", new TimeInstant((IndeterminateValue) null)
                .setIndeterminateValue(IndeterminateValue.NOW).isEmpty());
    }

    @Test
    public void isNotEmptyForConstructorWithDate() {
        assertFalse("new TimeInstant(new DateTime()) is empty", new TimeInstant(new Date()).isEmpty());
    }

    @Test
    public void shouldEqualTime() {
        DateTime dateTime = new DateTime();
        TimeInstant timeInstant = new TimeInstant(dateTime);
        TimeInstant equalTimeInstant = new TimeInstant(dateTime);
        assertTrue("TimeInstants are NOT equal", timeInstant.equals(equalTimeInstant));
    }

    @Test
    public void shouldEqualIndeterminateValue() {
        IndeterminateValue tiv = IndeterminateValue.AFTER;
        TimeInstant timeInstant = new TimeInstant(tiv);
        TimeInstant equalTimeInstant = new TimeInstant(tiv);
        assertTrue("TimeInstants are NOT equal", timeInstant.equals(equalTimeInstant));
    }

    @Test
    public void shouldEqualTimeAndIndeterminateValue() {
        DateTime dateTime = new DateTime();
        IndeterminateValue tiv = IndeterminateValue.AFTER;
        TimeInstant timeInstant = new TimeInstant(dateTime, tiv);
        TimeInstant equalTimeInstant = new TimeInstant(dateTime, tiv);
        assertTrue("TimeInstants are NOT equal", timeInstant.equals(equalTimeInstant));
    }

    @Test
    public void testCompareTo() {
        TimeInstant timeInstantOne = new TimeInstant();
        TimeInstant timeInstantTwo = new TimeInstant();
        assertTrue("TimeInstants are equal", (timeInstantOne.compareTo(timeInstantTwo) == 0));
    }

}
