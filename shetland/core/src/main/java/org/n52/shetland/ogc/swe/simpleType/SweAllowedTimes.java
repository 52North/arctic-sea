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
package org.n52.shetland.ogc.swe.simpleType;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

import org.joda.time.DateTime;
import org.n52.shetland.ogc.swe.RangeValue;
import org.n52.shetland.ogc.swes.AbstractSWES;
import org.n52.shetland.util.CollectionHelper;

import com.google.common.collect.Lists;

public class SweAllowedTimes extends AbstractSWES {

    private List<DateTime> value = Lists.newArrayList();
    private List<RangeValue<DateTime>> interval = Lists.newArrayList();
    private BigInteger significantFigures;

    /**
     * @return the value
     */
    public List<DateTime> getValue() {
        return Collections.unmodifiableList(value);
    }

    /**
     * @param value
     *            the value to set
     */
    public void setValue(List<DateTime> value) {
        this.value.clear();
        if (value != null) {
            this.value.addAll(value);
        }
    }

    public void addValue(DateTime value) {
        this.value.add(value);
    }

    public boolean isSetValue() {
        return CollectionHelper.isNotEmpty(getValue());
    }

    /**
     * @return the interval
     */
    public List<RangeValue<DateTime>> getInterval() {
        return Collections.unmodifiableList(interval);
    }

    /**
     * @param interval
     *            the interval to set
     */
    public void setInterval(List<RangeValue<DateTime>> interval) {
        this.interval.clear();
        if (interval != null) {
            this.interval.addAll(interval);
        }
    }

    /**
     * @param interval
     *            the interval to add
     */
    public void addInterval(RangeValue<DateTime> interval) {
        this.interval.add(interval);
    }

    public boolean isSetInterval() {
        return CollectionHelper.isNotEmpty(getInterval());
    }

    /**
     * @return the significantFigures
     */
    public BigInteger getSignificantFigures() {
        return significantFigures;
    }

    /**
     * @param significantFigures
     *            the significantFigures to set
     */
    public void setSignificantFigures(BigInteger significantFigures) {
        this.significantFigures = significantFigures;
    }

    public boolean isSetSignificantFigures() {
        return getSignificantFigures() != null;
    }
}
