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

import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Interval;
import org.joda.time.Period;
import org.joda.time.format.ISOPeriodFormat;
import org.n52.shetland.util.CollectionHelper;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Class represents a GML conform timePeriod element.
 *
 * @since 1.0.0
 */
public class TimePeriod extends Time {
    /**
     * serial number
     */
    private static final long serialVersionUID = -1784577421975774171L;

    /** start Date of timePeriod */
    private DateTime start;

    /** indeterminate position of startPosition */
    private IndeterminateValue startIndet;

    /** end Date of timePeriod */
    private DateTime end;

    /** indeterminate position of endPosition */
    private IndeterminateValue endIndet;

    /** duration value ISO8601 format */
    private Period duration;

    /** interval value ISO8601 format */
    private String interval;

    /**
     * default constructor
     *
     */
    public TimePeriod() {
    }

    /**
     * Creates a new {@code TimePeriod} from an {@code Interval}.
     *
     * @param interval
     *            the interval
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public TimePeriod(Interval interval) {
        this(interval.getStart(), interval.getEnd());
    }

    /**
     * constructor with start and end date as parameters
     *
     * @param start
     *            start date of the time period
     * @param end
     *            end date of the time period
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public TimePeriod(DateTime start, DateTime end) {
        this.start = start;
        this.end = end;
    }

    /**
     * constructor
     *
     * @param start
     *            start date of the time period
     * @param end
     *            end date of the time period
     * @param id
     *            GML id
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public TimePeriod(DateTime start, DateTime end, String id) {
        super(id);
        this.start = start;
        this.end = end;
    }

    /**
     * constructor
     *
     * @param start
     *            start date of the time period
     * @param startIndet
     *            indeterminate time position of start
     * @param end
     *            end date of the time period
     * @param endIndet
     *            indeterminate time value of end position
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public TimePeriod(DateTime start, IndeterminateValue startIndet, DateTime end, IndeterminateValue endIndet) {
        this.start = start;
        this.startIndet = startIndet;
        this.end = end;
        this.endIndet = endIndet;
    }

    /**
     * standard constructor
     *
     * @param start
     *            timeString of start position in ISO8601 format
     * @param startIndet
     *            indeterminate time position of start
     * @param end
     *            timeString of end position in ISO8601 format
     * @param endIndet
     *            indeterminate time value of end position
     * @param duration
     *            duration in ISO8601 format
     * @param id
     *            the optional GML id
     * @throws ParseException
     *             if parsing the time strings of start or end into java.util.Date failed
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public TimePeriod(DateTime start, IndeterminateValue startIndet, DateTime end, IndeterminateValue endIndet,
            String duration, String id) throws ParseException {
        super(id);
        this.start = start;
        this.startIndet = startIndet;
        this.end = end;
        this.endIndet = endIndet;
        this.duration = ISOPeriodFormat.standard().parsePeriod(duration);
    }

    /**
     * Constructor using {@link TimeInstant}s
     *
     * @param startTime
     *            Start TimeInstant
     * @param endTime
     *            End TimeInstant
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public TimePeriod(TimeInstant startTime, TimeInstant endTime) {
        if (startTime != null) {
            this.start = startTime.getValue();
            this.startIndet = startTime.getIndeterminateValue();
        }
        if (endTime != null) {
            this.end = endTime.getValue();
            this.endIndet = endTime.getIndeterminateValue();
        }
    }

    /**
     * Constructor using Java {@link Date}s, setting unknown indeterminate values if null
     *
     * @param start
     *            start Date
     * @param end
     *            end Date
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public TimePeriod(Date start, Date end) {
        if (start != null) {
            this.start = new DateTime(start, DateTimeZone.UTC);
        } else {
            this.startIndet = IndeterminateValue.UNKNOWN;
        }
        if (end != null) {
            this.end = new DateTime(end, DateTimeZone.UTC);
        } else {
            this.endIndet = IndeterminateValue.UNKNOWN;
        }
    }

    /**
     * Constructor using Java {@link Object}s, setting unknown indeterminate values if null
     *
     * @param start
     *            start {@link Object}
     * @param end
     *            end {@link Object}
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public TimePeriod(Object start, Object end) {
        if (start != null) {
            this.start = new DateTime(start, DateTimeZone.UTC);
        } else {
            this.startIndet = IndeterminateValue.UNKNOWN;
        }
        if (end != null) {
            this.end = new DateTime(end, DateTimeZone.UTC);
        } else {
            this.endIndet = IndeterminateValue.UNKNOWN;
        }
    }

    /**
     * Get duration
     *
     * @return Returns the duration.
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP" })
    public Period getDuration() {
        return duration;
    }

    /**
     * Set duration
     *
     * @param duration
     *            The duration to set.
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public void setDuration(Period duration) {
        this.duration = duration;
    }

    /**
     * Get start time
     *
     * @return Returns the start.
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP" })
    public DateTime getStart() {
        return start;
    }

    /**
     * Set start time
     *
     * @param start
     *            The start to set.
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public void setStart(DateTime start) {
        this.start = start;
    }

    /**
     * Get start time, resolving indeterminate value if start is null
     *
     * @return Returns the resolved start time.
     */
    public DateTime resolveStart() {
        return resolveDateTime(start, startIndet);
    }

    /**
     * Get end time
     *
     * @return Returns the end.
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP" })
    public DateTime getEnd() {
        return end;
    }

    /**
     * Set end time
     *
     * @param end
     *            The end to set.
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public void setEnd(DateTime end) {
        this.end = end;
    }

    /**
     * Get end time, resolving indeterminate value if start is null
     *
     * @return Returns the resolved end time.
     */
    public DateTime resolveEnd() {
        return resolveDateTime(end, endIndet);
    }

    /**
     * Get start indet time
     *
     * @return Returns the startIndet.
     */
    public IndeterminateValue getStartIndet() {
        return startIndet;
    }

    /**
     * Set start indet time
     *
     * @param startIndet
     *            The startIndet to set.
     */
    public void setStartIndet(IndeterminateValue startIndet) {
        this.startIndet = startIndet;
    }

    /**
     * Get end indet time
     *
     * @return Returns the endIndet.
     */
    public IndeterminateValue getEndIndet() {
        return endIndet;
    }

    /**
     * Set end indet time
     *
     * @param endIndet
     *            The endIndet to set.
     */
    public void setEndIndet(IndeterminateValue endIndet) {
        this.endIndet = endIndet;
    }

    /**
     * Get interval
     *
     * @return Interval string
     */
    public String getInterval() {
        return this.interval;
    }

    /**
     * Set interval
     *
     * @param interval
     *            the interval
     */
    public void setInterval(String interval) {
        this.interval = interval;
    }

    /**
     * Get TimePosition for start.
     *
     * @return Start TimePosition object
     */
    public TimePosition getStartTimePosition() {
        if (isSetStartIndeterminateValue()) {
            return new TimePosition(getStartIndet());
        } else {
            return new TimePosition(getStart(), getTimeFormat());
        }
    }

    /**
     * Get TimePosition for end.
     *
     * @return End TimePosition object
     */
    public TimePosition getEndTimePosition() {
        if (isSetEndIndeterminateValue()) {
            return new TimePosition(getEndIndet());
        } else {
            return new TimePosition(getEnd(), getTimeFormat());
        }
    }

    /**
     * Extend TimePeriod to contain provided collection of {@link Time}s
     *
     * @param times
     *            the times to contain
     */
    public void extendToContain(Collection<Time> times) {
        if (CollectionHelper.isNotEmpty(times)) {
            times.forEach(this::extendToContain);
        }
    }

    /**
     * Extend TimePeriod to contain provided {@link Time}
     *
     * @param time
     *            To contain {@link Time}
     */
    public void extendToContain(Time time) {
        if (time instanceof TimeInstant) {
            extendToContain((TimeInstant) time);
        } else if (time instanceof TimePeriod) {
            extendToContain((TimePeriod) time);
        } else {
            String errorMsg = String.format("Received Time type \"%s\" unknown.",
                    Optional.ofNullable(time).map(Object::getClass).map(Class::getName).orElse("null"));
            throw new IllegalArgumentException(errorMsg);
        }
    }

    /**
     * Extend TimePeriod to contain another {@link TimePeriod}
     *
     * @param period
     *            To contain {@link TimePeriod}
     */
    public void extendToContain(TimePeriod period) {
        extendToContain(period.getStart());
        extendToContain(period.getEnd());
        checkTimeFormat(period.getTimeFormat());
    }

    /**
     * Extend TimePeriod to contain {@link TimeInstant}
     *
     * @param instant
     *            To contain {@link TimeInstant}
     */
    public void extendToContain(TimeInstant instant) {
        if (instant != null) {
            extendToContain(instant.getValue());
            checkTimeFormat(instant.getTimeFormat());
        }
    }

    /**
     * Extend TimePeriod to contain DateTime. Used by other extendToContain methods.
     *
     * @param time
     *            the time to contain
     */
    public void extendToContain(DateTime time) {
        if (time != null) {
            if (!isSetStart() || time.isBefore(getStart())) {
                setStart(time);
            }
            if (!isSetEnd() || time.isAfter(getEnd())) {
                setEnd(time);
            }
        }
    }

    /**
     * Is this TimePeriod contained by another TimePeriod? Equal start/end times are considered to be
     * containing, as are equal indeterminate times.
     *
     * @param otherTimePeriod
     *            Potentially containing TimePeriod
     * @return Whether the argument TimePeriod contains this one
     */
    public boolean isWithin(TimePeriod otherTimePeriod) {
        if (otherTimePeriod == null) {
            throw new NullPointerException("otherTimePeriod cannot be null");
        }
        boolean startWithin = false;
        boolean endWithin = false;
        if (start != null && otherTimePeriod.getStart() != null) {
            startWithin = start.isEqual(otherTimePeriod.getStart()) || start.isAfter(otherTimePeriod.getStart());
        } else if (start == null && otherTimePeriod.getStart() == null && startIndet != null
                && otherTimePeriod.getStartIndet() != null && startIndet.equals(otherTimePeriod.getStartIndet())) {
            startWithin = true;
        }
        if (end != null && otherTimePeriod.getEnd() != null) {
            endWithin = end.isEqual(otherTimePeriod.getEnd()) || end.isBefore(otherTimePeriod.getEnd());
        } else if (end == null && otherTimePeriod.getEnd() == null && endIndet != null
                && otherTimePeriod.getEndIndet() != null && endIndet.equals(otherTimePeriod.getEndIndet())) {
            endWithin = true;
        }
        return startWithin && endWithin;
    }

    /**
     * Checks this timeFormat with passed
     *
     * @param timeFormat
     *            TimeFormat to check with local
     */
    private void checkTimeFormat(TimeFormat timeFormat) {
        if (this.getTimeFormat().equals(TimeFormat.NOT_SET)) {
            this.setTimeFormat(timeFormat);
        } else {
            if (!this.getTimeFormat().equals(timeFormat) && TimeFormat.SUPPORTED_FORMATS.contains(timeFormat)) {
                this.setTimeFormat(timeFormat);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("Time period: ");
        if (isSetStart()) {
            result.append(getStart().toString()).append(", ");
        }
        result.append(getStartIndet()).append(", ");
        if (isSetEnd()) {
            result.append(getEnd().toString()).append(", ");
        }
        result.append(getEndIndet());
        return result.toString();
    }

    @Override
    public int compareTo(Time o) {
        if (o instanceof TimeInstant) {
            TimeInstant ti = (TimeInstant) o;
            if (getEnd().isBefore(ti.getValue())) {
                return -1;
            } else if (getStart().isAfter(ti.getValue())) {
                return 1;
            }
        } else if (o instanceof TimePeriod) {
            TimePeriod tp = (TimePeriod) o;
            if (getStart().isBefore(tp.getStart())) {
                return -1;
            } else if (getEnd().isAfter(tp.getEnd())) {
                return 1;
            }
        }
        return 0;
    }

    /**
     * @return <tt>true</tt>, if start and end are NOT set
     * @see #isSetStart()
     * @see #isSetEnd()
     */
    @Override
    public boolean isEmpty() {
        return !isSetEnd() && !isSetStart();
    }

    /**
     * @return <tt>true</tt>, if start is set
     * @see #isEmpty()
     * @see #isSetEnd()
     */
    public boolean isSetStart() {
        return getStart() != null;
    }

    /**
     * @return <tt>true</tt>, if start IndeterminateValue is set
     */
    public boolean isSetStartIndeterminateValue() {
        return getStartIndet() != null;
    }

    /**
     * @return <tt>true</tt>, if end IndeterminateValue is set
     */
    public boolean isSetEndIndeterminateValue() {
        return getEndIndet() != null;
    }

    /**
     * @return <tt>true</tt>, if end is set
     * @see #isSetStart()
     * @see #isEmpty()
     */
    public boolean isSetEnd() {
        return getEnd() != null;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getDuration() != null) ? getDuration().hashCode() : 0);
        result = prime * result + ((getEnd() != null) ? getEnd().hashCode() : 0);
        result = prime * result + ((getEndIndet() != null) ? getEndIndet().hashCode() : 0);
        result = prime * result + ((getInterval() != null) ? getInterval().hashCode() : 0);
        result = prime * result + ((getStart() != null) ? getStart().hashCode() : 0);
        result = prime * result + ((getStartIndet() != null) ? getStartIndet().hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof TimePeriod)) {
            return false;
        }
        TimePeriod other = (TimePeriod) obj;
        if (getDuration() == null) {
            if (other.getDuration() != null) {
                return false;
            }
        } else if (!getDuration().equals(other.getDuration())) {
            return false;
        }
        if (getEnd() == null) {
            if (other.getEnd() != null) {
                return false;
            }
        } else if (!getEnd().equals(other.getEnd())) {
            return false;
        }
        if (getEndIndet() == null) {
            if (other.getEndIndet() != null) {
                return false;
            }
        } else if (!getEndIndet().equals(other.getEndIndet())) {
            return false;
        }
        if (getInterval() == null) {
            if (other.getInterval() != null) {
                return false;
            }
        } else if (!getInterval().equals(other.getInterval())) {
            return false;
        }
        if (getStart() == null) {
            if (other.getStart() != null) {
                return false;
            }
        } else if (!getStart().equals(other.getStart())) {
            return false;
        }
        if (getStartIndet() == null) {
            if (other.getStartIndet() != null) {
                return false;
            }
        } else if (!getStartIndet().equals(other.getStartIndet())) {
            return false;
        }
        return true;
    }
}
