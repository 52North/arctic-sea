/*
 * Copyright 2015-2017 52°North Initiative for Geospatial Open Source
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

import java.util.Optional;

import org.joda.time.DateTime;

import org.n52.shetland.ogc.gml.time.Time.TimeFormat;

import com.google.common.base.MoreObjects;

/**
 * Representation class for GML TimePosition. Used by TimeInstant and TimePeriod
 * during encoding to reduce duplicate code.
 *
 * @since 1.0.0
 *
 */
public class TimePosition {

    private static final TimeFormat DEFAULT_TIME_FORMAT = TimeFormat.ISO8601;

    private Optional<DateTime> time = Optional.empty();

    private Optional<IndeterminateValue> indeterminateValue = Optional.empty();

    private Optional<TimeFormat> timeFormat = Optional.empty();

    /**
     * constructor using default time format ISO 8601
     *
     * @param time
     *            Time postion time
     */
    public TimePosition(DateTime time) {
        this(time, DEFAULT_TIME_FORMAT);
    }

    /**
     * constructor
     *
     * @param indeterminateValue
     *            Indeterminate value of time position
     */
    public TimePosition(IndeterminateValue indeterminateValue) {
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
        this.timeFormat = Optional.ofNullable(timeFormat);
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
     * Get time position indeterminate value
     *
     * @return the indeterminateValue time position indeterminate value
     */
    public IndeterminateValue getIndeterminateValue() {
        return indeterminateValue.get();
    }

    /**
     * Get time position time format
     *
     * @return the time format if set, the default time format otherwise
     */
    public TimeFormat getTimeFormat() {
        return timeFormat.orElse(DEFAULT_TIME_FORMAT);
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
        return MoreObjects.toStringHelper(this)
                .add("time", this.time.get())
                .add("indeterminate time", this.indeterminateValue.get())
                .omitNullValues().toString();
    }
}
