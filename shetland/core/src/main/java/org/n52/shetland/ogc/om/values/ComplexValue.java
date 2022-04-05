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

import java.util.Objects;

import org.n52.shetland.ogc.UoM;
import org.n52.shetland.ogc.om.values.visitor.ValueVisitor;
import org.n52.shetland.ogc.swe.SweAbstractDataRecord;

import com.google.common.base.MoreObjects;

public class ComplexValue
        implements Value<SweAbstractDataRecord> {
    private SweAbstractDataRecord value;
    private UoM unit;

    public ComplexValue() {
        this(null);
    }

    public ComplexValue(SweAbstractDataRecord value) {
        this.value = value;
    }

    @Override
    public ComplexValue setValue(SweAbstractDataRecord value) {
        this.value = value;
        return this;
    }

    @Override
    public SweAbstractDataRecord getValue() {
        return this.value;
    }

    @Override
    public boolean isSetValue() {
        return this.value != null;
    }

    @Override
    public void setUnit(String unit) {
        this.unit = new UoM(unit);
    }

    @Override
    public ComplexValue setUnit(UoM unit) {
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
        return MoreObjects.toStringHelper(this)
                .add("value", this.value)
                .add("unit", this.unit)
                .toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.value, this.unit);
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
        final ComplexValue other = (ComplexValue) obj;
        return Objects.equals(this.value, other.value) &&
               Objects.equals(this.unit, other.unit);
    }

    @Override
    public <X, E extends Exception> X accept(ValueVisitor<X, E> visitor) throws E {
        return visitor.visit(this);
    }

}
