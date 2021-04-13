/*
 * Copyright 2015-2021 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.ogc.swe.simpleType;

import java.math.BigDecimal;

import org.n52.shetland.ogc.swe.RangeValue;
import org.n52.shetland.ogc.swe.SweConstants.SweDataComponentType;
import org.n52.shetland.ogc.swe.SweDataComponentVisitor;
import org.n52.shetland.ogc.swe.VoidSweDataComponentVisitor;
import org.n52.shetland.w3c.xlink.Referenceable;

/**
 * SOS internal representation of SWE simpleType quantity
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 */
public class SweQuantityRange extends SweAbstractUomType<RangeValue<BigDecimal>> implements SweQuality {

    private String axisID;
    private RangeValue<BigDecimal> value;
    private Referenceable<SweAllowedValues> constraint;

    /**
     * constructor
     */
    public SweQuantityRange() {
    }

    /**
     * Get axis ID
     *
     * @return the axisID
     */
    public String getAxisID() {
        return axisID;
    }

    /**
     * set axis ID
     *
     * @param axisID
     *            the axisID to set
     * @return This SweQuantityRange
     */
    public SweQuantityRange setAxisID(final String axisID) {
        this.axisID = axisID;
        return this;
    }

    @Override
    public RangeValue<BigDecimal> getValue() {
        return value;
    }

    @Override
    public SweQuantityRange setValue(RangeValue<BigDecimal> value) {
        this.value = value;
        return this;
    }

    @Override
    public int hashCode() {
        final int prime = 97;
        int hash = 7;
        hash = prime * hash + super.hashCode();
        hash = prime * hash + (axisID != null ? axisID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SweQuantityRange other = (SweQuantityRange) obj;
        if ((getAxisID() == null) ? (other.getAxisID() != null) : !getAxisID().equals(other.getAxisID())) {
            return false;
        }
        return super.equals(obj);
    }

    @Override
    public void setStringValue(String s) {
        if (s != null && !s.isEmpty() && s.contains("/")) {
            String[] split = s.split("/");
            setValue(new RangeValue<BigDecimal>(new BigDecimal(split[0]), new BigDecimal(split[1])));
        }
    }

    @Override
    public String getStringValue() {
        return value.toString();
    }

    @Override
    public boolean isSetValue() {
        return value != null;
    }

    public boolean isSetAxisID() {
        return axisID != null && !axisID.isEmpty();
    }

    /**
     * @return the constraint
     */
    public Referenceable<SweAllowedValues> getConstraint() {
        return constraint;
    }

    /**
     * @param constraint the constraint to set
     */
    public void setConstraint(SweAllowedValues constraint) {
        this.constraint = Referenceable.of(constraint);
    }

    /**
     * @param constraint the constraint to set
     */
    public void setConstraint(Referenceable<SweAllowedValues> constraint) {
        this.constraint = constraint;
    }

    public boolean isSetContstraint() {
        return getConstraint() != null && !getConstraint().isAbsent();
    }

    @Override
    public SweDataComponentType getDataComponentType() {
        return SweDataComponentType.QuantityRange;
    }

    @Override
    public <T, X extends Throwable> T accept(SweDataComponentVisitor<T, X> visitor) throws X {
        return visitor.visit(this);
    }

    @Override
    public <X extends Throwable> void accept(VoidSweDataComponentVisitor<X> visitor) throws X {
        visitor.visit(this);
    }

    @Override
    public SweQuantityRange copy() {
        SweQuantityRange copy = new SweQuantityRange();
        copyValueTo(copy);
        if (isSetQuality()) {
            copy.setQuality(cloneQuality());
        }
        if (isSetAxisID()) {
            copy.setAxisID(getAxisID());
        }
        if (isSetValue()) {
            copy.setValue(getValue().copy());
        }
        if (isSetContstraint()) {
            copy.setConstraint(getConstraint());
        }
        return copy;
    }

}
