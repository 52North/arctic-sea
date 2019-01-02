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
package org.n52.janmayen;

import java.util.Optional;
import java.util.regex.Pattern;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Interval;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.joda.time.format.ISOPeriodFormat;

import com.google.common.base.Strings;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public final class Times {
    private static final DateTime ZERO = new DateTime(0, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC);
    private static final String UTC_OFFSET = "+00:00";
    private static final String Z = "Z";

    private Times() {
    }

    public static String encodeDateTime(DateTime dt) {
        if (dt == null) {
            return ZERO.toString().replace(Z, UTC_OFFSET);
        }
        return dt.toString();
    }

    public static String encodeDateTime(DateTime dateTime, String dateFormat) {
        if (Strings.isNullOrEmpty(dateFormat)) {
            return encodeDateTime(dateTime);
        } else if (dateTime == null) {
            return ZERO.toString(DateTimeFormat.forPattern(dateFormat));
        } else {
            return dateTime.toString(DateTimeFormat.forPattern(dateFormat)).replace(Z, UTC_OFFSET);
        }
    }

    public static String encodeDateTime(DateTime dateTime, DateTimeFormatter formatter) {
        if (formatter == null) {
            return encodeDateTime(dateTime);
        } else if (dateTime == null) {
            return ZERO.toString(formatter);
        } else {
            return dateTime.toString(formatter).replace(Z, UTC_OFFSET);
        }
    }

    public static DateTime decodeDateTime(String string) {
        if (Strings.isNullOrEmpty(string)) {
            return null;
        }
        if (string.contains("+") || Pattern.matches("-\\d", string) || string.contains(Z) || string.contains("z")) {
            return ISODateTimeFormat.dateOptionalTimeParser().withOffsetParsed().parseDateTime(string);
        } else {
            return ISODateTimeFormat.dateOptionalTimeParser().withZone(DateTimeZone.UTC).parseDateTime(string);
        }
    }

    /**
     * Parse a duration from a string representation.
     *
     * @param period Duration as String
     *
     * @return Period object of duration
     */
    public static Period decodePeriod(String period) {
        return ISOPeriodFormat.standard().parsePeriod(period);
    }

    public static Interval decodeInterval(String interval) {
        String[] split = interval.split("/", 2);
        return new Interval(decodeDateTime(split[0]), decodeDateTime(split[1]));
    }

    public static String encodeInterval(Interval interval) {
        return String.format("%s/%s", encodeDateTime(interval.getStart()), encodeDateTime(interval.getEnd()));
    }

    /**
     * Transforms the supplied {@code DateTime} to UTC.
     *
     * @param datetime the date time (may be {@code null})
     *
     * @return the UTC time (or {@code null}
     */
    public static DateTime toUTC(DateTime datetime) {
        return Optional.ofNullable(datetime).map(dt -> dt.withZone(DateTimeZone.UTC)).orElse(null);
    }

    public static DateTime max(DateTime dt1, DateTime dt2) {
        if (dt2 == null) {
            return dt1;
        } else if (dt1 == null) {
            return dt2;
        } else if (dt2.isAfter(dt1)) {
            return dt2;
        }
        return dt1;
    }

    public static DateTime min(DateTime dt1, DateTime dt2) {
        if (dt2 == null) {
            return dt1;
        } else if (dt1 == null) {
            return dt2;
        } else if (dt2.isBefore(dt1)) {
            return dt2;
        }
        return dt1;
    }

}
