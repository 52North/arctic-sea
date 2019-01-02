/*
 * Copyright 2015-2019 52°North Initiative for Geospatial Open Source
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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.joda.time.DateTime;
import org.junit.Test;


/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk
 *         J&uuml;rrens</a> TODO test extent to methods!!!
 *
 * @since 1.0.0
 */
public class TimePeriodTest {

    @Test
    public void isEmptyForDefaultConstructorTest() {
        assertTrue("new Timeperiod is NOT empty", new TimePeriod().isEmpty());
    }

    @Test
    public void isEmptyForConstructorWithNullStartAndEndTimeTest() {
        assertTrue("new TimePeriod(null, null) is NOT empty",
                new TimePeriod((DateTime) null, (DateTime) null).isEmpty());
    }

    @Test
    public void isEmptyForConstructorWithAllNullTest() {
        assertTrue("new TimePeriod(null, null, null) is NOT empty", new TimePeriod(null, null, null).isEmpty());
    }

    @Test
    public void isEmptyForConstructorWithNullStartAndEndTimeAndGmlIdTest() {
        assertTrue("new TimePeriod(null, null, \"gmlId\") is NOT empty", new TimePeriod(null, null, "gmlId").isEmpty());
    }

    @Test
    public void isEmptyForConstructorWithStartTimeAndNullEndTimeTest() {
        assertFalse("new TimePeriod(new DateTime(), null) is empty", new TimePeriod(new DateTime(), null).isEmpty());
    }

    @Test
    public void isEmptyForConstructorWithNullStartTimeAndEndTimeTest() {
        assertFalse("new TimePeriod(null, ew DateTime()) is empty", new TimePeriod(null, new DateTime()).isEmpty());
    }

    @Test
    public void isEmptyForConstructorWithNullStartAndEndTimeInstantTest() {
        assertTrue("new TimePeriod(null, null) is NOT empty",
                new TimePeriod((TimeInstant) null, (TimeInstant) null).isEmpty());
    }

    @Test
    public void isSetStartTest() {
        assertTrue("new TimePeriod(new DateTime(),null).isSetStart() == false",
                new TimePeriod(new DateTime(), null).isSetStart());
    }

    @Test
    public void isSetEndTest() {
        assertTrue("new TimePeriod(null,new DateTime()).isSetEnd() == false",
                new TimePeriod(null, new DateTime()).isSetEnd());
    }

    @Test
    public void isSetStartTestTimeInstant() {
        assertTrue("new TimePeriod(new DateTime(),null).isSetStart() == false", new TimePeriod(new TimeInstant(
                new DateTime()), null).isSetStart());
    }

    @Test
    public void isSetEndTestTimeInstant() {
        assertTrue("new TimePeriod(null,new DateTime()).isSetEnd() == false", new TimePeriod(null, new TimeInstant(
                new DateTime())).isSetEnd());
    }

    @Test
    public void emptyTimePeriodExtendedByTimeInstantShouldHaveTheSameValueForStartAndEnd() {
        TimePeriod timePeriod = new TimePeriod();

        timePeriod.extendToContain(new TimeInstant(new DateTime()));

        assertFalse("TimePeriod is emtpy after extending", timePeriod.isEmpty());
        assertTrue("Start value not set", timePeriod.isSetStart());
        assertTrue("End value not set", timePeriod.isSetEnd());
    }

    @Test
    public void shouldRemoveReferencPrefixForGetGmlIdTest() {
        TimePeriod timePeriod = new TimePeriod();
        timePeriod.setGmlId("#test");
        assertTrue("GmlId starts with '#' for getGmlId()", !timePeriod.getGmlId().startsWith("#"));
    }

    @Test
    public void isReferencedTest() {
        TimePeriod timePeriod = new TimePeriod();
        timePeriod.setGmlId("#test");
        assertTrue("TimePeriod is NOT referenced", timePeriod.isReferenced());
        timePeriod.setGmlId("test");
        assertFalse("TimePeriod is referenced", timePeriod.isReferenced());
    }

    @Test
    public void testIndeterminateNowStart() {
        TimePeriod timePeriod = new TimePeriod(null, IndeterminateValue.NOW, new DateTime(), null);
        DateTime beforeAccess = new DateTime();
        DateTime nowValue = timePeriod.resolveStart();
        assertNotNull("TimePeriod start now value is null", nowValue);
        assertTrue("TimePeriod start now value is too early", nowValue.isAfter(beforeAccess) || nowValue.isEqual(beforeAccess));
        DateTime now = new DateTime();
        assertTrue("TimePeriod start now value is too late", nowValue.isBefore(now) || nowValue.isEqual(now));
    }

    @Test
    public void testIndeterminateNowEnd() {
        DateTime beforeResolve = new DateTime();
        DateTime resolvedValue = new TimePeriod(new DateTime(), null, null, IndeterminateValue.NOW).resolveEnd();
        DateTime afterResolve = new DateTime();
        assertNotNull("TimePeriod end now value is null", resolvedValue);
        assertTrue("TimePeriod end now value is too early", resolvedValue.isAfter(beforeResolve) || resolvedValue.isEqual(beforeResolve));
        assertTrue("TimePeriod end now value is too late", resolvedValue.isBefore(afterResolve)|| resolvedValue.isEqual(afterResolve));
    }

}
