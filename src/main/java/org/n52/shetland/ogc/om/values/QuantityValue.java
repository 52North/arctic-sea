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

import org.n52.shetland.ogc.UoM;
import org.n52.shetland.ogc.om.values.visitor.ValueVisitor;
import org.n52.shetland.ogc.swe.simpleType.SweQuantity;

/**
 * Quantity measurement representation for observation
 *
 * @since 1.0.0
 *
 */
public class QuantityValue
        extends SweQuantity
        implements ComparableValue<Double, QuantityValue> {

    /**
     * constructor
     *
     * @param value
     *            Measurement value
     */
    public QuantityValue(Double value) {
        super();
        super.setValue(value);
    }

    /**
     * constructor
     *
     * @param value
     *            Measurement value
     * @param unit
     *            Unit of measure
     */
    public QuantityValue(Double value, String unit) {
        super(value, unit);
    }

    /**
     * constructor
     *
     * @param value
     *            Measurement value
     * @param unit
     *            Unit of measure
     */
    public QuantityValue(Double value, UoM unit) {
        super(value, unit);
    }

    @Override
    public QuantityValue setValue(final Double value) {
        super.setValue(value);
        return this;
    }

    @Override
    public void setUnit(String unit) {
        super.setUom(unit);
    }

    @Override
    public QuantityValue setUnit(UoM unit) {
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
        return String.format("QuantityValue [value=%s, unit=%s]", getValue(), getUnit());
    }

    @Override
    public <X, E extends Exception> X accept(ValueVisitor<X, E> visitor) throws E {
        return visitor.visit(this);
    }

    @Override
    public int compareTo(QuantityValue o) {
        if (o == null) {
            throw new NullPointerException();
        }
        if (getValue() == null ^ o.getValue() == null) {
            return (getValue() == null) ? -1 : 1;
        }
        if (getValue() == null && o.getValue() == null) {
            return 0;
        }
        return getValue().compareTo(o.getValue());
    }
}
