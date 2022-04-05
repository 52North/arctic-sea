/*
 * Copyright 2015-2022 52°North Spatial Information Research GmbH
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

/**
 * Unknown value for observation if type is unknown
 *
 * @since 1.0.0
 *
 */
public class UnknownValue
        implements Value<Object> {

    /**
     * Measurement
     */
    private Object value;

    /**
     * Unit of measure
     */
    private UoM unit;

    /**
     * Constructor
     *
     * @param value
     *            Measurement value
     */
    public UnknownValue(Object value) {
        this.value = value;
    }

    @Override
    public UnknownValue setValue(Object value) {
        this.value = value;
        return this;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public void setUnit(String unit) {
        this.unit = new UoM(unit);
    }

    @Override
    public UnknownValue setUnit(UoM unit) {
        this.unit = unit;
        return this;
    }

    @Override
    public String getUnit() {
        if (isSetUnit()) {
            return unit.getUom();
        }
        return null;
    }

    @Override
    public UoM getUnitObject() {
        return this.unit;
    }

    @Override
    public String toString() {
        return String.format("UnknownValue [value=%s, unit=%s]", getValue(), getUnit());
    }

    @Override
    public <X, E extends Exception> X accept(ValueVisitor<X, E> visitor) throws E {
        return visitor.visit(this);
    }
}
