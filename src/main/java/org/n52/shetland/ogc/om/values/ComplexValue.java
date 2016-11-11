/*
 * Copyright 2016 52°North Initiative for Geospatial Open Source
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

import org.n52.shetland.ogc.om.values.visitor.ValueVisitor;
import org.n52.shetland.ogc.swe.SweAbstractDataRecord;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

public class ComplexValue implements Value<SweAbstractDataRecord> {
    private static final long serialVersionUID = 7864029515468084800L;
    private SweAbstractDataRecord value;
    private String unit;

    public ComplexValue() {
        this(null);
    }

    public ComplexValue(SweAbstractDataRecord value) {
        this.value = value;
    }

    @Override
    public void setValue(SweAbstractDataRecord value) {
        this.value = value;
    }

    @Override
    public SweAbstractDataRecord getValue() {
        return this.value;
    }

    @Override
    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public String getUnit() {
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
        return Objects.hashCode(this.value, this.unit);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ComplexValue) {
            ComplexValue that = (ComplexValue) obj;
            return Objects.equal(this.getValue(), that.getValue()) &&
                   Objects.equal(this.getUnit(), that.getUnit());
        }
        return false;
    }

    @Override
    public <X, E extends Exception> X accept(ValueVisitor<X, E> visitor) throws E {
        return visitor.visit(this);
    }
}
