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
package org.n52.shetland.ogc.swe.simpleType;

import java.util.Collection;

import org.n52.iceland.ogc.swe.SweConstants.SweDataComponentType;
import org.n52.shetland.ogc.swe.SweDataComponentVisitor;
import org.n52.shetland.ogc.swe.VoidSweDataComponentVisitor;

/**
 * SOS internal representation of SWE simpleType quantity
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 4.0.0
 */
public class SweQuantity extends SweAbstractUomType<Double> implements SweQuality {

    /**
     * axis ID
     */
    private String axisID;

    /**
     * value
     */
    private Double value;

    /**
     * constructor
     */
    public SweQuantity() {
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
    public Double getValue() {
        return value;
    }

    @Override
    public SweQuantity setValue(final Double value) {
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
        final SweQuantity other = (SweQuantity) obj;
        if ((getAxisID() == null) ? (other.getAxisID() != null) : !getAxisID().equals(other.getAxisID())) {
            return false;
        }
        return super.equals(obj);
    }

    @Override
    public String getStringValue() {
        if (isSetValue()) {
            return Double.toString(value.intValue());
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
    public SweQuantity clone() {
        SweQuantity clone = new SweQuantity();
        copyValueTo(clone);
        if (isSetAxisID()) {
            clone.setAxisID(getAxisID());
        }
        if (isSetValue()) {
            clone.setValue(getValue());
        }
        return clone;
    }

}
