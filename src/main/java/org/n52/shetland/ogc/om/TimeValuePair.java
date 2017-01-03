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
package org.n52.shetland.ogc.om;

import java.util.Objects;

import org.n52.shetland.ogc.gml.time.Time;
import org.n52.shetland.ogc.om.values.Value;

/**
 * Class representing a time value pair
 *
 * @since 4.0.0
 *
 */
public class TimeValuePair implements Comparable<TimeValuePair> {

    /**
     * Time value pair time
     */
    private Time time;

    /**
     * Time value pair value
     */
    private Value<?> value;

    /**
     * Constructor
     *
     * @param time
     *            Time value pair time
     * @param value
     *            Time value pair value
     */
    public TimeValuePair(Time time, Value<?> value) {
        this.time = time;
        this.value = value;
    }

    /**
     * Get time value pair time
     *
     * @return Time value pair time
     */
    public Time getTime() {
        return time;
    }

    /**
     * Get time value pair value
     *
     * @return Time value pair value
     */
    public Value<?> getValue() {
        return value;
    }

    /**
     * Set time value pair time
     *
     * @param time
     *            Time value pair time to set
     */
    public void setTime(Time time) {
        this.time = time;
    }

    /**
     * Set time value pair value
     *
     * @param value
     *            Time value pair value to set
     */
    public void setValue(Value<?> value) {
        this.value = value;
    }

    @Override
    public int compareTo(TimeValuePair o) {
        return time.compareTo(o.time);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.time);
        hash = 37 * hash + Objects.hashCode(this.value);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TimeValuePair other = (TimeValuePair) obj;
        if (!Objects.equals(this.time, other.time)) {
            return false;
        }
        if (!Objects.equals(this.value, other.value)) {
            return false;
        }
        return true;
    }

}
