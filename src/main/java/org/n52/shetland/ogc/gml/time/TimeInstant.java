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
package org.n52.shetland.ogc.gml.time;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

/**
 * Class represents a GML conform timeInstant element
 *
 * @since 1.0.0
 */
public class TimeInstant
        extends Time {
    /**
     * serial number
     */
    private static final long serialVersionUID = -1426561343329882331L;

    /** date for this timeInstant */
    private DateTime value;

    /** length of timeInstant date */
    private int requestedTimeLength;

    /**
     * Indeterminate value
     */
    private IndeterminateValue indeterminateValue;

    /**
     * default constructor
     */
    public TimeInstant() {
    }

    /**
     * constructor
     *
     * @param dateTime
     *            Time position of this time instant
     */
    public TimeInstant(DateTime dateTime) {
        this(dateTime, 0, null);
    }

    /**
     * constructor
     *
     * @param indeterminateValue
     *            Indeterminate value
     */
    public TimeInstant(IndeterminateValue indeterminateValue) {
        this(null, 0, indeterminateValue);
    }

    /**
     * constructor with date and indeterminateValue.
     *
     * @param dateValue
     *            date of the timeInstante
     * @param indeterminateValue
     *            Indeterminate value
     */
    public TimeInstant(DateTime dateValue, IndeterminateValue indeterminateValue) {
        this(dateValue, 0, indeterminateValue);
    }

    /**
     * Constructor using Java Dates, setting unknown indeterminate values if
     * null
     *
     * @param date
     *            Value date
     */
    public TimeInstant(Date date) {
        if (date != null) {
            this.value = new DateTime(date, DateTimeZone.UTC);
        } else {
            this.indeterminateValue = IndeterminateValue.UNKNOWN;
        }
    }

    public TimeInstant(DateTime value, int requestedTimeLength) {
        this(value, requestedTimeLength, null);
    }

    private TimeInstant(DateTime value, int requestedTimeLength, IndeterminateValue indeterminateValue) {
        this.value = value;
        this.requestedTimeLength = requestedTimeLength;
        this.indeterminateValue = indeterminateValue;
    }

    /**
     * Get time value
     *
     * @return Returns the value.
     */
    public DateTime getValue() {
        return value;
    }

    /**
     * Set time value
     *
     * @param value
     *            The value to set.
     */
    public void setValue(final DateTime value) {
        this.value = value;
    }

    /**
     * Get time value, resolving indeterminate value if value is null
     *
     * @return Returns the resolved value.
     */
    public DateTime resolveValue() {
        return resolveDateTime(value, getIndeterminateValue());
    }

    /**
     * Set requested time length
     *
     * @param requestedTimeLength
     *            the requestedTimeLength to set
     */
    public void setRequestedTimeLength(final int requestedTimeLength) {
        this.requestedTimeLength = requestedTimeLength;
    }

    /**
     * Get requested time length
     *
     * @return the requestedTimeLength
     */
    public int getRequestedTimeLength() {
        return requestedTimeLength;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("Time instant: ");
        if (isSetValue()) {
            result.append(getValue().toString()).append(", ");
        }
        result.append(getIndeterminateValue());
        return result.toString();
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(final Time o) {
        if (o instanceof TimeInstant) {
            final TimeInstant ti = (TimeInstant) o;
            if (isSetValue() && ti.isSetValue()) {
                if (getValue().isBefore(ti.getValue())) {
                    return -1;
                } else if (getValue().isAfter(ti.getValue())) {
                    return 1;
                }
            }
        } else if (o instanceof TimePeriod) {
            final TimePeriod tp = (TimePeriod) o;
            if (isSetValue() && tp.isSetStart() && tp.isSetEnd()) {
                if (getValue().isBefore(tp.getStart())) {
                    return -1;
                } else if (getValue().isAfter(tp.getEnd())) {
                    return 1;
                }
            }
        }
        return 0;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object paramObject) {
        if (paramObject instanceof TimeInstant) {
            TimeInstant toCheck = (TimeInstant) paramObject;
            if (isSetValue() && toCheck.isSetValue()) {
                return getValue().isEqual(toCheck.getValue());
            } else if (isSetIndeterminateValue() && toCheck.isSetIndeterminateValue()) {
                return getIndeterminateValue().equals(toCheck.getIndeterminateValue());
            } else if (isSetValue() && toCheck.isSetValue() && isSetIndeterminateValue()
                    && toCheck.isSetIndeterminateValue()) {
                return getValue().isEqual(toCheck.getValue())
                        && getIndeterminateValue().equals(toCheck.getIndeterminateValue());
            }
        }
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + ((isSetValue()) ? getValue().hashCode() : 0);
        return hash;
    }

    /**
     * Check if time value is set
     *
     * @return <tt>true</tt> if set
     */
    public boolean isSetValue() {
        return getValue() != null;
    }

    /**
     * Get indeterminate value
     *
     * @return Returns the indeterminateValue.
     */
    public IndeterminateValue getIndeterminateValue() {
        return indeterminateValue;
    }

    /**
     * Get the time position of this time instant
     *
     * @return Time position
     */
    public TimePosition getTimePosition() {
        if (isSetIndeterminateValue()) {
            return new TimePosition(getIndeterminateValue());
        } else {
            return new TimePosition(getValue(), getTimeFormat());
        }
    }

    /**
     * Set indeterminate value
     *
     * @param indeterminateValue
     *            The indeterminateValue to set.
     * @return this
     */
    public Time setIndeterminateValue(IndeterminateValue indeterminateValue) {
        this.indeterminateValue = indeterminateValue;
        return this;
    }

    /**
     * Check if indeterminate value is set
     *
     * @return <tt>true</tt> if set
     */
    public boolean isSetIndeterminateValue() {
        return getIndeterminateValue() != null;
    }

    /**
     * Check whether this indeterminate value equals expected value
     *
     * @param value
     *            Expected value
     * @return <code>true</code>, if this indeterminate value equals expected
     *         value
     */
    @SuppressWarnings("rawtypes")
    public boolean isIndeterminateValueEqualTo(IndeterminateValue value) {
        return isSetIndeterminateValue() && getIndeterminateValue().equals(value);
    }

    /**
     * @return <tt>true</tt>, if value and indeterminateValue are NOT set
     * @see #isSetValue()
     * @see #isSetIndeterminateValue()
     */
    @Override
    public boolean isEmpty() {
        return !isSetValue() && !isSetIndeterminateValue() && super.isEmpty();
    }
}
