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
package org.n52.shetland.util;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.jupiter.api.Test;

import org.n52.shetland.util.DateTimeHelper;

public class DateTimeHelperTest {

    private final String testTimePositiveTimeZone = "2014-01-28T10:16:35.945+02:00";

    private final String testTimeNegativeTimeZone = "2014-01-28T10:16:35.945-02:00";

    private final String testTimeZTimeZone = "2014-01-28T10:16:35.945Z";

    private final int TIME_LENGTH = 23;

    private final DateTime DT_1950 = new DateTime(1950, 1, 1, 0, 0, DateTimeZone.UTC);

    private final DateTime DT_END = new DateTime(2015, 7, 27, 11, 25, DateTimeZone.UTC);

    private final int DAYS_SINCE = 23948;

    private final double DAYS_SINCE_PR = 23948.475694444445;

    @Test
    public void testGetTimeLengthBeforeTimeZone() {
        assertThat(DateTimeHelper.getTimeLengthBeforeTimeZone(testTimePositiveTimeZone), is(TIME_LENGTH));
        assertThat(DateTimeHelper.getTimeLengthBeforeTimeZone(testTimeNegativeTimeZone), is(TIME_LENGTH));
        assertThat(DateTimeHelper.getTimeLengthBeforeTimeZone(testTimeZTimeZone), is(TIME_LENGTH));
    }

    @Test
    public void testMakeDateTime() {
        long current = System.currentTimeMillis();
        DateTime currentDateTime = new DateTime(current, DateTimeZone.UTC);
        assertThat(currentDateTime.equals(DateTimeHelper.makeDateTime(currentDateTime)), is(true));
        assertThat(currentDateTime.equals(DateTimeHelper.makeDateTime(new java.util.Date(current))), is(true));
        assertThat(currentDateTime.equals(DateTimeHelper.makeDateTime(new java.sql.Date(current))), is(true));
        assertThat(currentDateTime.equals(DateTimeHelper.makeDateTime(new java.sql.Timestamp(current))), is(true));
        assertThat(currentDateTime.equals(DateTimeHelper.makeDateTime(new java.sql.Time(current))), is(true));
    }

    @Test
    public void testGetDaysSince() {
        assertThat(DateTimeHelper.getDaysSince(DT_1950, DT_END), is(DAYS_SINCE));
        assertThat(DateTimeHelper.getDaysSinceWithPrecision(DT_1950, DT_END), is(DAYS_SINCE_PR));
    }

}
