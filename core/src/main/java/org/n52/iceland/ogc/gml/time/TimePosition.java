/*
 * Copyright 2015 52°North Initiative for Geospatial Open Source
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
package org.n52.iceland.ogc.gml.time;

import java.util.Optional;
import org.joda.time.DateTime;
import org.n52.iceland.ogc.gml.time.Time.TimeFormat;
import org.n52.iceland.ogc.gml.time.Time.TimeIndeterminateValue;
import org.n52.iceland.util.Constants;

/**
 * Representation class for GML TimePosition. Used by TimeInstant and TimePeriod
 * during encoding to reduce duplicate code.
 *
 * @since 1.0.0
 *
 */
public class TimePosition {

    private Optional<DateTime> time = Optional.empty();

    private Optional<TimeIndeterminateValue> indeterminateValue = Optional.empty();

    private Optional<TimeFormat> timeFormat = Optional.empty();

    /**
     * constructor
     *
     * @param time
     *            Time postion time
     */
    public TimePosition(DateTime time) {
        this(time, null);
    }

    /**
     * constructor
     *
     * @param indeterminateValue
     *            Indeterminate value of time position
     */
    public TimePosition(TimeIndeterminateValue indeterminateValue) {
        this.indeterminateValue = Optional.of(indeterminateValue);
    }

    /**
     * constructor
     *
     * @param time
     *            Time position time
     * @param timeFormat
     *            Time format
     */
    public TimePosition(DateTime time, TimeFormat timeFormat) {
        this.time = Optional.of(time);
        this.timeFormat = Optional.of(timeFormat);
    }

    /**
     * Get time position time
     *
     * @return the time Time position time
     */
    public DateTime getTime() {
        return time.get();
    }

    /**
     * Set time position time
     *
     * @param time
     *            the time to set
     */
    public void setTime(DateTime time) {
        this.time = Optional.of(time);
    }

    /**
     * Get time position indeterminate value
     *
     * @return the indeterminateValue time position indeterminate value
     */
    public TimeIndeterminateValue getIndeterminateValue() {
        return indeterminateValue.get();
    }

    /**
     * Get time position time format
     *
     * @return the timeFormat Time position time format
     */
    public TimeFormat getTimeFormat() {
        return timeFormat.get();
    }

    /**
     * Set time position time format
     *
     * @param timeFormat
     *            the timeFormat to set
     */
    public void setTimeFormat(TimeFormat timeFormat) {
        this.timeFormat = Optional.of(timeFormat);
    }

    /**
     * Set time position indeterminat value
     *
     * @param indeterminateValue
     *            the indeterminateValue to set
     */
    public void setIndeterminateValue(TimeIndeterminateValue indeterminateValue) {
        this.indeterminateValue = Optional.of(indeterminateValue);
    }

    /**
     * Check if time value is set
     *
     * @return <tt>true</tt>, if time is set
     */
    public boolean isSetTime() {
        return time.isPresent();
    }

    /**
     * Check if indeterminateValue is set
     *
     * @return <tt>true</tt>, if indeterminateValue is set
     */
    public boolean isSetIndeterminateValue() {
        return indeterminateValue.isPresent();
    }

    /**
     * Check if time format is set
     *
     * @return <tt>true</tt>, if time format is set
     */
    public boolean isSetTimeFormat() {
        return timeFormat.isPresent();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("Time position: ");
        if (isSetTime()) {
            result.append(getTime().toString()).append(Constants.COMMA_STRING);
        }
        result.append(getIndeterminateValue());
        return result.toString();
    }
}
