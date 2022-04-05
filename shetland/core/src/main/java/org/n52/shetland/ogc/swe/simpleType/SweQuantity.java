/*
 * Copyright 2015-${currentYear} 52°North Spatial Information Research GmbH
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
import java.util.Collection;

import org.n52.shetland.ogc.UoM;
import org.n52.shetland.ogc.swe.SweConstants.SweDataComponentType;
import org.n52.shetland.w3c.xlink.Referenceable;
import org.n52.shetland.ogc.swe.SweDataComponentVisitor;
import org.n52.shetland.ogc.swe.VoidSweDataComponentVisitor;

/**
 * SOS internal representation of SWE simpleType quantity
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 */
public class SweQuantity extends SweAbstractUomType<BigDecimal> implements SweQuality {

    private String axisID;
    private BigDecimal value;
    private Referenceable<SweAllowedValues> constraint;

    /**
     * constructor
     */
    public SweQuantity() {
    }

    /**
     * constructor
     */
    public SweQuantity(BigDecimal value, String uom) {
        this.value = value;
        setUom(uom);
    }

    /**
     * constructor
     */
    public SweQuantity(BigDecimal value, UoM uom) {
        this.value = value;
        setUom(uom);
    }

    /**
     * constructor
     */
    public SweQuantity(Double value, String uom) {
        this.value = BigDecimal.valueOf(value);
        setUom(uom);
    }

    /**
     * constructor
     */
    public SweQuantity(Double value, UoM uom) {
        this.value = BigDecimal.valueOf(value);
        setUom(uom);
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
     * @return This SweQuantity
     */
    public SweQuantity setAxisID(final String axisID) {
        this.axisID = axisID;
        return this;
    }

    @Override
    public BigDecimal getValue() {
        return value;
    }

    public Double getValueAsDouble() {
        if (isSetValue()) {
            return value.doubleValue();
        }
        return null;
    }

    @Override
    public SweQuantity setValue(final BigDecimal value) {
        this.value = value;
        return this;
    }

    public SweQuantity setValue(final Double value) {
        return setValue(BigDecimal.valueOf(value));
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
        final SweQuantity other = (SweQuantity) obj;
        if ((getAxisID() == null) ? (other.getAxisID() != null) : !getAxisID().equals(other.getAxisID())) {
            return false;
        }
        if ((getValue() == null) ? (other.getValue() != null) : !getValue().equals(other.getValue())) {
            return false;
        }
        return super.equals(obj);
    }

    @Override
    public void setStringValue(String s) {
        if (s != null && !s.isEmpty()) {
            setValue(new BigDecimal(s));
        }
    }

    @Override
    public String getStringValue() {
        if (isSetValue()) {
            return value.toPlainString();
        }
        return null;
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
        return SweDataComponentType.Quantity;
    }

    @Override
    public SweQuantity setUom(String uom) {
        return (SweQuantity) super.setUom(uom);
    }

    @Override
    public SweQuantity setQuality(Collection<SweQuality> quality) {
        return (SweQuantity) super.setQuality(quality);
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
    public SweQuantity copy() {
        SweQuantity copy = new SweQuantity();
        copyValueTo(copy);
        if (isSetAxisID()) {
            copy.setAxisID(getAxisID());
        }
        if (isSetValue()) {
            copy.setValue(getValue());
        }
        if (isSetContstraint()) {
            copy.setConstraint(getConstraint());
        }
        return copy;
    }

}
