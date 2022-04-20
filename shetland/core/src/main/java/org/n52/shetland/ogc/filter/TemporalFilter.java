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
package org.n52.shetland.ogc.filter;

import org.n52.shetland.ogc.filter.FilterConstants.TimeOperator;
import org.n52.shetland.ogc.gml.time.Time;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Temporal filter class
 *
 * @since 1.0.0
 *
 */
public class TemporalFilter
        extends Filter<TimeOperator> {

    private TimeOperator operator;

    private Time time;

    public TemporalFilter() {
        this((TimeOperator) null, null, null);
    }

    /**
     * constructor
     *
     * @param operator
     *            Temporal filter operator
     * @param time
     *            Filter time
     * @param valueReference
     *            value reference
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public TemporalFilter(TimeOperator operator, Time time, String valueReference) {
        super(valueReference);
        this.operator = operator;
        this.time = time;
    }

    /**
     * constructor
     *
     * @param operatorName
     *            Temporal filter operator name
     * @param time
     *            Filter time
     * @param valueReference
     *            value reference
     */
    public TemporalFilter(String operatorName, Time time, String valueReference) {
        this(TimeOperator.valueOf(operatorName), time, valueReference);
    }

    @Override
    public TimeOperator getOperator() {
        return operator;
    }

    @Override
    public TemporalFilter setOperator(TimeOperator operator) {
        this.operator = operator;
        return this;
    }

    /**
     * Get filter time
     *
     * @return filter time
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP" })
    public Time getTime() {
        return time;
    }

    /**
     * Set filter time
     *
     * @param time
     *            filter time
     * @return This filter
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public TemporalFilter setTime(Time time) {
        this.time = time;
        return this;
    }

    @Override
    public String toString() {
        return "Temporal filter: " + operator + time.toString();
    }

}
