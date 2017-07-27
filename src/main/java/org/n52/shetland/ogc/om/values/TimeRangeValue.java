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
package org.n52.shetland.ogc.om.values;

import org.joda.time.DateTime;
import org.n52.shetland.ogc.UoM;
import org.n52.shetland.ogc.om.values.visitor.ValueVisitor;
import org.n52.shetland.ogc.swe.RangeValue;
import org.n52.shetland.ogc.swe.simpleType.SweTimeRange;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class TimeRangeValue
        extends SweTimeRange
        implements Value<RangeValue<DateTime>> {
    /**
     * constructor
     *
     * @param value
     *            Measurement value
     */
    public TimeRangeValue(RangeValue<DateTime> value) {
        super(value);
    }

    /**
     * * constructor
     *
     * @param value
     *            Measurement value
     * @param unit
     *            Unit of measure
     */
    public TimeRangeValue(RangeValue<DateTime> value, String unit) {
        super(value, unit);
    }

    /**
     * * constructor
     *
     * @param value
     *            Measurement value
     * @param unit
     *            Unit of measure
     */
    public TimeRangeValue(RangeValue<DateTime> value, UoM unit) {
        super(value, unit);
    }

    @Override
    public TimeRangeValue setValue(final RangeValue<DateTime> value) {
        super.setValue(value);
        return this;
    }

    @Override
    public void setUnit(String unit) {
        super.setUom(unit);
    }

    @Override
    public TimeRangeValue setUnit(UoM unit) {
        super.setUom(unit);
        return this;
    }

    @Override
    public String getUnit() {
        return super.getUom();
    }

    @Override
    public UoM getUnitObject() {
        return super.getUomObject();
    }

    @Override
    public boolean isSetUnit() {
        return super.isSetUom();
    }

    @Override
    public String toString() {
        return String.format("TimeValue [value=%s, unit=%s]", getValue(), getUnit());
    }

    @Override
    public <X, E extends Exception> X accept(ValueVisitor<X, E> visitor) throws E {
        return visitor.visit(this);
    }

}
