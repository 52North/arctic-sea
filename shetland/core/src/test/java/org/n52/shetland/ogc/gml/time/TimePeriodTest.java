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
package org.n52.shetland.ogc.gml.time;

import static org.junit.jupiter.api.Assertions.*;

import org.joda.time.DateTime;
import org.junit.jupiter.api.Test;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk
 *         J&uuml;rrens</a> TODO test extent to methods!!!
 *
 * @since 1.0.0
 */
public class TimePeriodTest {

    @Test
    public void isEmptyForDefaultConstructorTest() {
        assertTrue(new TimePeriod().isEmpty(), "new Timeperiod is NOT empty");
    }

    @Test
    public void isEmptyForConstructorWithNullStartAndEndTimeTest() {
        assertTrue(new TimePeriod((DateTime) null, (DateTime) null).isEmpty(),
                "new TimePeriod(null, null) is NOT empty");
    }

    @Test
    public void isEmptyForConstructorWithAllNullTest() {
        assertTrue(new TimePeriod(null, null, null).isEmpty(), "new TimePeriod(null, null, null) is NOT empty");
    }

    @Test
    public void isEmptyForConstructorWithNullStartAndEndTimeAndGmlIdTest() {
        assertTrue(new TimePeriod(null, null, "gmlId").isEmpty(),
                "new TimePeriod(null, null, \"gmlId\") is NOT empty");
    }

    @Test
    public void isEmptyForConstructorWithStartTimeAndNullEndTimeTest() {
        assertFalse(new TimePeriod(new DateTime(), null).isEmpty(), "new TimePeriod(new DateTime(), null) is empty");
    }

    @Test
    public void isEmptyForConstructorWithNullStartTimeAndEndTimeTest() {
        assertFalse(new TimePeriod(null, new DateTime()).isEmpty(), "new TimePeriod(null, ew DateTime()) is empty");
    }

    @Test
    public void isEmptyForConstructorWithNullStartAndEndTimeInstantTest() {
        assertTrue(new TimePeriod((TimeInstant) null, (TimeInstant) null).isEmpty(),
                "new TimePeriod(null, null) is NOT empty");
    }

    @Test
    public void isSetStartTest() {
        assertTrue(new TimePeriod(new DateTime(), null).isSetStart(),
                "new TimePeriod(new DateTime(),null).isSetStart() == false");
    }

    @Test
    public void isSetEndTest() {
        assertTrue(new TimePeriod(null, new DateTime()).isSetEnd(),
                "new TimePeriod(null,new DateTime()).isSetEnd() == false");
    }

    @Test
    public void isSetStartTestTimeInstant() {
        assertTrue(new TimePeriod(new TimeInstant(new DateTime()), null).isSetStart(),
                "new TimePeriod(new DateTime(),null).isSetStart() == false");
    }

    @Test
    public void isSetEndTestTimeInstant() {
        assertTrue(new TimePeriod(null, new TimeInstant(new DateTime())).isSetEnd(),
                "new TimePeriod(null,new DateTime()).isSetEnd() == false");
    }

    @Test
    public void emptyTimePeriodExtendedByTimeInstantShouldHaveTheSameValueForStartAndEnd() {
        TimePeriod timePeriod = new TimePeriod();

        timePeriod.extendToContain(new TimeInstant(new DateTime()));

        assertFalse(timePeriod.isEmpty(), "TimePeriod is emtpy after extending");
        assertTrue(timePeriod.isSetStart(), "Start value not set");
        assertTrue(timePeriod.isSetEnd(), "End value not set");
    }

    @Test
    public void shouldRemoveReferencPrefixForGetGmlIdTest() {
        TimePeriod timePeriod = new TimePeriod();
        timePeriod.setGmlId("#test");
        assertTrue(!timePeriod.getGmlId().startsWith("#"), "GmlId starts with '#' for getGmlId()");
    }

    @Test
    public void isReferencedTest() {
        TimePeriod timePeriod = new TimePeriod();
        timePeriod.setGmlId("#test");
        assertTrue(timePeriod.isReferenced(), "TimePeriod is NOT referenced");
        timePeriod.setGmlId("test");
        assertFalse(timePeriod.isReferenced(), "TimePeriod is referenced");
    }

    @Test
    public void testIndeterminateNowStart() {
        TimePeriod timePeriod = new TimePeriod(null, IndeterminateValue.NOW, new DateTime(), null);
        DateTime beforeAccess = new DateTime();
        DateTime nowValue = timePeriod.resolveStart();
        assertNotNull(nowValue, "TimePeriod start now value is null");
        assertTrue(nowValue.isAfter(beforeAccess) || nowValue.isEqual(beforeAccess),
                "TimePeriod start now value is too early");
        DateTime now = new DateTime();
        assertTrue(nowValue.isBefore(now) || nowValue.isEqual(now), "TimePeriod start now value is too late");
    }

    @Test
    public void testIndeterminateNowEnd() {
        DateTime beforeResolve = new DateTime();
        DateTime resolvedValue = new TimePeriod(new DateTime(), null, null, IndeterminateValue.NOW).resolveEnd();
        DateTime afterResolve = new DateTime();
        assertNotNull(resolvedValue, "TimePeriod end now value is null");
        assertTrue(resolvedValue.isAfter(beforeResolve) || resolvedValue.isEqual(beforeResolve),
                "TimePeriod end now value is too early");
        assertTrue(resolvedValue.isBefore(afterResolve) || resolvedValue.isEqual(afterResolve),
                "TimePeriod end now value is too late");
    }

}
