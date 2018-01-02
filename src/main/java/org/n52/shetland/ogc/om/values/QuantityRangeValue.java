/*
 * Copyright 2016-2018 52°North Initiative for Geospatial Open Source
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

import org.n52.janmayen.Comparables;
import org.n52.shetland.ogc.UoM;
import org.n52.shetland.ogc.om.values.visitor.ValueVisitor;
import org.n52.shetland.ogc.swe.RangeValue;
import org.n52.shetland.ogc.swe.simpleType.SweQuantityRange;

public class QuantityRangeValue extends SweQuantityRange
        implements ComparableValue<RangeValue<Double>, QuantityRangeValue> {

    /**
     * Creates a new {@code QuantityRangeValue}.
     *
     * @param rangeStart the start of the range
     * @param rangeEnd   the end of the range
     */
    public QuantityRangeValue(Double rangeStart, Double rangeEnd) {
        super();
        super.setValue(new RangeValue<>(rangeStart, rangeEnd));
    }

    /**
     * Creates a new {@code QuantityRangeValue}.
     *
     * @param rangeStart the start of the range
     * @param rangeEnd   the end of the range
     * @param unit Unit of measure
     */
    public QuantityRangeValue(Double rangeStart, Double rangeEnd, String unit) {
        this(rangeStart, rangeEnd);
        this.setUnit(unit);
    }

    @Override
    public QuantityRangeValue setValue(RangeValue<Double> value) {
        super.setValue(value);
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
    public void setUnit(String unit) {
        super.setUom(unit);
    }

    @Override
    public QuantityRangeValue setUnit(UoM unit) {
        super.setUom(unit);
        return this;
    }

    @Override
    public boolean isSetUnit() {
        return super.isSetUom();
    }

    @Override
    public String toString() {
        return String.format("QuantityValue [value=%s, unit=%s]", getValue(), getUnit());
    }

    @Override
    public <X, E extends Exception> X accept(ValueVisitor<X, E> visitor) throws E {
        return visitor.visit(this);
    }

    @Override
    public int compareTo(QuantityRangeValue o) {
        if (o == null) {
            throw new NullPointerException();
        }
        return Comparables.compare(getValue(), o.getValue());
    }

}
