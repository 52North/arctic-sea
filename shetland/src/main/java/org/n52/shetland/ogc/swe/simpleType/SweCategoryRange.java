/*
 * Copyright 2015-2017 52°North Initiative for Geospatial Open Source
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

import org.n52.shetland.ogc.swe.RangeValue;
import org.n52.shetland.ogc.swe.SweConstants.SweDataComponentType;
import org.n52.shetland.w3c.xlink.Referenceable;
import org.n52.shetland.ogc.swe.SweDataComponentVisitor;
import org.n52.shetland.ogc.swe.VoidSweDataComponentVisitor;

public class SweCategoryRange extends SweAbstractUomType<RangeValue<String>> implements SweQuality {

    /**
     * value
     */
    private RangeValue<String> value;
    private Referenceable<SweAllowedTokens> constraint;

    @Override
    public RangeValue<String> getValue() {
        return value;
    }

    @Override
    public String getStringValue() {
        return value.toString();
    }

    @Override
    public boolean isSetValue() {
        return value != null;
    }

    @Override
    public SweAbstractSimpleType<RangeValue<String>> setValue(RangeValue<String> value) {
        this.value = value;
        return this;
    }

    /**
     * @return the constraint
     */
    public Referenceable<SweAllowedTokens> getConstraint() {
        return constraint;
    }

    /**
     * @param constraint the constraint to set
     */
    public void setConstraint(SweAllowedTokens constraint) {
        this.constraint = Referenceable.of(constraint);
    }

    /**
     * @param constraint the constraint to set
     */
    public void setConstraint(Referenceable<SweAllowedTokens> constraint) {
        this.constraint = constraint;
    }

    public boolean isSetContstraint() {
        return getConstraint() != null && !getConstraint().isAbsent();
    }

    @Override
    public SweDataComponentType getDataComponentType() {
        return SweDataComponentType.CategoryRange;
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
    public SweCategoryRange copy() {
        SweCategoryRange copy = new SweCategoryRange();
        copyValueTo(copy);
        if (isSetQuality()) {
            copy.setQuality(cloneQuality());
        }
        if (isSetValue()) {
            copy.setValue(getValue().copy());
        }
        if (isSetContstraint()) {
            copy.setConstraint(getConstraint());
        }
        return copy;
    }

    @Override
    public int hashCode() {
        final int prime = 97;
        int hash = 7;
        hash = prime * hash + super.hashCode();
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
        return super.equals(obj);
    }
}
