/*
 * Copyright 2016-2017 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.util;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.regex.Pattern;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.n52.janmayen.Times;
import org.n52.shetland.ogc.gml.time.Time;
import org.n52.shetland.ogc.gml.time.Time.TimeFormat;
import org.n52.shetland.ogc.gml.time.TimeInstant;
import org.n52.shetland.ogc.gml.time.TimePeriod;
import org.n52.shetland.ogc.gml.time.TimePosition;

import com.google.common.base.Strings;

/**
 * Utility class for Time formatting and parsing. Uses Joda Time.
 *
 * @since 1.0.0
 *
 */
public final class DateTimeHelper {

    private static final DateTime ZERO = new DateTime(0000, 01, 01, 00, 00, 00, 000, DateTimeZone.UTC);
    /**
     * Response format for time.
     */
    private static String responseFormat;
    private static final String YMD_RESPONSE_FORMAT = "yyyy-MM-dd";
    private static final String YM_RESPONSE_FORMAT = "yyyy-MM";
    private static final String Y_RESPONSE_FORMAT = "yyyy";
    private static final int YEAR = 4;
    private static final int YEAR_MONTH = 7;
    private static final int YEAR_MONTH_DAY = 10;
    private static final int YEAR_MONTH_DAY_HOUR = 13;
    private static final int YEAR_MONTH_DAY_HOUR_MINUTE = 16;
    private static final int YEAR_MONTH_DAY_HOUR_MINUTE_SECOND = 19;
    private static final String Z = "Z";
    private static final double SECONDS_OF_DAY = 86400;

    /**
     * lease value
     */
    private static int lease;
    /**
     * Hidden utility constructor.
     */
    private DateTimeHelper() {
    }

    /**
     * Parses a time String to a Joda Time DateTime object
     *
     * @param timeString
     *            Time String
     * @return DateTime object
     * @throws DateTimeParseException
     *             If an error occurs.
     */
    public static DateTime parseIsoString2DateTime(final String timeString) throws DateTimeParseException {
        return parseString2DateTime(timeString, null);
    }

    public static DateTime parseString2DateTime(final String timeString, final String dateFormat)
            throws DateTimeParseException {
        checkForValidity(timeString);
        if (Strings.isNullOrEmpty(timeString)) {
            return null;
        }
        try {
            if (!Strings.isNullOrEmpty(dateFormat)) {
                if (checkOffset(timeString)) {
                    return DateTimeFormat.forPattern(dateFormat).withOffsetParsed().parseDateTime(timeString);
                } else {
                    return DateTimeFormat.forPattern(dateFormat).withZone(DateTimeZone.UTC).parseDateTime(timeString);
                }
            }
            if (checkOffset(timeString)) {
                return ISODateTimeFormat.dateOptionalTimeParser().withOffsetParsed().parseDateTime(timeString);
            } else {
                return ISODateTimeFormat.dateOptionalTimeParser().withZone(DateTimeZone.UTC).parseDateTime(timeString);
            }
        } catch (final RuntimeException uoe) {
            throw new DateTimeParseException(timeString, uoe);
        }
    }

    /**
     * Parses the given ISO 8601 String to a {@link Time} including
     * {@link TimeInstant} and {@link TimePeriod}
     *
     * @param timeString
     *            a ISO 8601 formatted time string
     * @return a Time object
     * @throws DateTimeParseException
     *             If an error occurs.
     */
    public static Time parseIsoString2DateTime2Time(String timeString) throws DateTimeParseException {
        if (timeString.contains("/")) {
            String[] subTokens = timeString.split("/");
            return new TimePeriod(parseIsoString2DateTime(subTokens[0]), parseIsoString2DateTime(subTokens[1]));
        } else {
            return new TimeInstant(parseIsoString2DateTime(timeString));
        }
    }

    private static void checkForValidity(final String timeString) throws DateTimeParseException {
        if (!(timeString.length() == YEAR || timeString.length() == YEAR_MONTH
                || timeString.length() >= YEAR_MONTH_DAY)) {
            throw new DateTimeParseException(timeString);
        }
    }

    private static boolean checkOffset(String timeString) {
        return timeString.contains("+") || Pattern.matches("-\\d", timeString) || timeString.contains(Z)
                || timeString.contains("z");
    }

    /**
     * Formats the given Time to ISO 8601 string.
     *
     * @param time
     *            an {@link Time} object to be formatted
     * @return an ISO 8601 conform {@link String}.
     * @throws IllegalArgumentException
     *             in the case of receiving <tt>null</tt> or not supported
     *             types.
     * @see #formatDateTime2IsoString(DateTime)
     */
    public static String format(Time time) {
        if (time instanceof TimeInstant) {
            try {
                return formatDateTime2String(((TimeInstant) time).getTimePosition());
            } catch (DateTimeFormatException e) {
                throw new IllegalArgumentException(e);
            }
    //return formatDateTime2IsoString(((TimeInstant) time).getValue());
        } else if (time instanceof TimePeriod) {
            TimePeriod period = (TimePeriod) time;
            return String.format("%s/%s",
                                 formatDateTime2IsoString(period.getStart()),
                                 formatDateTime2IsoString(period.getEnd()));
        }
        throw new IllegalArgumentException(String.format("Given Time object is not valid: %s", time));
    }

    /**
     * Formats a DateTime to a ISO-8601 String
     *
     * @param dateTime
     *            Time object
     * @return ISO-8601 formatted time String
     */
    public static String formatDateTime2IsoString(DateTime dateTime) {
        return Times.encodeDateTime(dateTime);
    }

    /**
     * Formats a DateTime to a String using the response format
     *
     * @param dateTime
     *            Time object
     * @return Response formatted time String
     *
     * @throws DateTimeFormatException
     *             If an error occurs.
     */
    public static String formatDateTime2ResponseString(DateTime dateTime) throws DateTimeFormatException {
        return formatDateTime2FormattedString(dateTime, responseFormat);
    }

    /**
     * Get formatted string from {@link DateTime} as defined {@link TimeFormat}
     *
     * @param dateTime
     *            {@link DateTime} to format
     * @param timeFormat
     *            The response {@link TimeFormat}
     * @return The formatted {@link DateTime}
     * @throws DateTimeFormatException
     *             If an error occurs when formatting the {@link DateTime}
     */
    public static String formatDateTime2String(DateTime dateTime, TimeFormat timeFormat)
            throws DateTimeFormatException {
        switch (timeFormat) {
        case Y:
            return formatDateTime2YearDateString(dateTime);
        case YM:
            return formatDateTime2YearMonthDateString(dateTime);
        case YMD:
            return formatDateTime2YearMonthDayDateStringYMD(dateTime);
        default:
            return formatDateTime2ResponseString(dateTime);
        }
    }

    /**
     * Get formatted string from {@link TimePosition}
     *
     * @param timePosition
     *            {@link TimePosition} to format
     * @return The formatted {@link TimePosition}
     * @throws DateTimeFormatException
     *             If an error occurs when formatting the {@link TimePosition}
     */
    public static String formatDateTime2String(TimePosition timePosition)
            throws DateTimeFormatException {
        switch (timePosition.getTimeFormat()) {
        case Y:
            return formatDateTime2YearDateString(timePosition.getTime());
        case YM:
            return formatDateTime2YearMonthDateString(timePosition.getTime());
        case YMD:
            return formatDateTime2YearMonthDayDateStringYMD(timePosition.getTime());
        default:
            return formatDateTime2ResponseString(timePosition.getTime());
        }
    }

    /**
     * Formats a DateTime to a String using specified format
     *
     * @param dateTime
     *            Time object
     * @param dateFormat
     *            the date time format
     *
     * @return Specified formatted time String
     *
     * @throws DateTimeFormatException
     *             If an error occurs.
     */
    public static String formatDateTime2FormattedString(DateTime dateTime, String dateFormat)
            throws DateTimeFormatException {
        try {
            return Times.encodeDateTime(dateTime, dateFormat);
        } catch (IllegalArgumentException e) {
            throw new DateTimeFormatException(e);
        }
    }

    /**
     * Formats a DateTime to a DAte using specified {@link DateTimeFormatter}
     *
     * @param dateTime
     *            Time object
     * @param formatter
     *            the {@link DateTimeFormatter}
     *
     * @return Specified formatted time String
     *
     * @throws DateTimeFormatException
     *             If an error occurs.
     */
    public static String formatDateTime2FormattedString(DateTime dateTime, DateTimeFormatter formatter)
            throws DateTimeFormatException {
            try {
                return Times.encodeDateTime(dateTime, formatter);
            } catch (IllegalArgumentException e) {
                throw new DateTimeFormatException(e);
            }
    }

    /**
     * formats a DateTime to a string with year-month-day.
     *
     * @param dateTime
     *            The DateTime.
     * @return Returns formatted time String.
     *
     * @throws DateTimeFormatException
     */
    public static String formatDateTime2YearMonthDayDateStringYMD(DateTime dateTime)
            throws DateTimeFormatException {
        try {
            DateTime result = checkAndGetDateTimeWithZoneUtc(dateTime);
            return result.toString(DateTimeFormat.forPattern(YMD_RESPONSE_FORMAT));
        } catch (IllegalArgumentException iae) {
            throw new DateTimeFormatException(dateTime, iae);
        }
    }

    /**
     * formats a DateTime to a string with year-month.
     *
     * @param dateTime
     *            The DateTime.
     * @return Returns formatted time String.
     *
     * @throws DateTimeFormatException
     */
    public static String formatDateTime2YearMonthDateString(DateTime dateTime) throws DateTimeFormatException {
        try {
            DateTime result = checkAndGetDateTimeWithZoneUtc(dateTime);
            return result.toString(DateTimeFormat.forPattern(YM_RESPONSE_FORMAT));
        } catch (IllegalArgumentException iae) {
            throw new DateTimeFormatException(dateTime, iae);
        }
    }

    /**
     * formats a DateTime to a string with year.
     *
     * @param dateTime
     *            The DateTime.
     * @return Returns formatted time String.
     *
     * @throws DateTimeFormatException
     */
    public static String formatDateTime2YearDateString(DateTime dateTime) throws DateTimeFormatException {
        try {
            DateTime result = checkAndGetDateTimeWithZoneUtc(dateTime);
            return result.toString(DateTimeFormat.forPattern(Y_RESPONSE_FORMAT));
        } catch (IllegalArgumentException iae) {
            throw new DateTimeFormatException(dateTime, iae);
        }
    }

    private static DateTime checkAndGetDateTimeWithZoneUtc(DateTime dateTime) {
        return Optional.ofNullable(dateTime).map(dt -> dt.withZone(DateTimeZone.UTC)).orElse(ZERO);
    }

    public static int getTimeLengthBeforeTimeZone(String time) {
        String valueSplit = null;
        if (time.contains("Z")) {
            valueSplit = time.substring(0, time.indexOf('Z'));
        } else if (time.contains("+")) {
            valueSplit = time.substring(0, time.indexOf('+'));
        } else if (StringHelper.getCharacterCount(time, '-') == 3) {
            valueSplit = time.substring(0, time.lastIndexOf('-'));
        }
        return valueSplit != null ? valueSplit.length() : time.length();
    }

    /**
     * Set the time object to the end values (seconds, minutes, hours, days,..)
     * if the time Object has not all values
     *
     * @param dateTime
     *            Time object
     * @param isoTimeLength
     *            Length of the time object
     * @return Modified time object.
     */
    public static DateTime setDateTime2EndOfMostPreciseUnit4RequestedEndPosition(DateTime dateTime, int isoTimeLength) {
        switch (isoTimeLength) {
            case YEAR:
                return dateTime.plusYears(1).minusMillis(1);
            case YEAR_MONTH:
                return dateTime.plusMonths(1).minusMillis(1);
            case YEAR_MONTH_DAY:
                return dateTime.plusDays(1).minusMillis(1);
            case YEAR_MONTH_DAY_HOUR:
                return dateTime.plusHours(1).minusMillis(1);
            case YEAR_MONTH_DAY_HOUR_MINUTE:
                return dateTime.plusMinutes(1).minusMillis(1);
            case YEAR_MONTH_DAY_HOUR_MINUTE_SECOND:
                return dateTime.plusSeconds(1).minusMillis(1);
            default:
                return dateTime;
        }
    }

      public static DateTime setDateTime2EndOfMostPreciseUnit4RequestedEndPosition(String time) throws DateTimeParseException {
        return setDateTime2EndOfMostPreciseUnit4RequestedEndPosition(parseIsoString2DateTime(time), getTimeLengthBeforeTimeZone(time));
    }

    /**
     * Parse a duration from a String representation
     *
     * @param duration
     *            Duration as String
     * @return Period object of duration
     */
    public static Period parseDuration(String duration) {
        return Times.decodePeriod(duration);
    }

    /**
     * Calculates the expire time for a time object
     *
     * @param start
     *            Time object
     * @return Expire time
     */
    public static DateTime calculateExpiresDateTime(DateTime start) {
        return start.plusMinutes(lease);
    }

    /**
     * Set the response format
     *
     * @param responseFormat
     *            Defined response format
     */
    public static void setResponseFormat(String responseFormat) {
        DateTimeHelper.responseFormat = responseFormat;
    }

    /**
     * Set the lease value
     *
     * @param lease
     *            Defined lease value
     */
    public static void setLease(final int lease) {
        DateTimeHelper.lease = lease;
    }

    /**
     * Make a new UTC DateTime from an object
     *
     * @param object
     * @return DateTime, or null if object was null
     */
    public static DateTime makeDateTime(Object object) {
        return object == null ? null : new DateTime(object, DateTimeZone.UTC);
    }

    /**
     * Transforms the supplied {@code DateTime} to UTC.
     * @param datetime the date time (may be {@code null})
     * @return the UTC time (or {@code null}
     */
    public static DateTime toUTC(DateTime datetime) {
        return Optional.ofNullable(datetime).map(dt -> dt.withZone(DateTimeZone.UTC)).orElse(null);
    }

    /**
     * Find the max of two dates (null safe)
     *
     * @param dt1
     * @param dt2
     * @return Max of two dates
     */
    public static DateTime max(DateTime dt1, DateTime dt2) {
        return Times.max(dt1, dt2);
    }

    /**
     * Get days between the given {@link DateTime}s
     *
     * @param start
     *            Start {@link DateTime}
     * @param end
     *            End {@link DateTime}
     * @return Days between the two {@link DateTime}s
     */
    public static int getDaysSince(DateTime start, DateTime end) {
        return Days.daysBetween(start, end).getDays();
    }

    /**
     * Get days between the given {@link DateTime}s with precision
     *
     * @param start
     *            Start {@link DateTime}
     * @param end
     *            End {@link DateTime}
     * @return Days with precisions between the two {@link DateTime}s
     */
    public static double getDaysSinceWithPrecision(DateTime start, DateTime end) {
        double value = Days.daysBetween(start, end).getDays() + end.getSecondOfDay()/SECONDS_OF_DAY;
        return new BigDecimal(value).doubleValue();
    }

    /**
     * Get seconds since epoch
     *
     * @param time
     *            {@link DateTime} to get seconds for
     * @return Seconds since epoch
     */
    public static double getSecondsSinceEpoch(DateTime time) {
        return time.getMillis() / 1000.0;
    }

}
