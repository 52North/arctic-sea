/*
 * Copyright (C) 2015-2022 52°North Spatial Information Research GmbH
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

import org.n52.shetland.ogc.swe.SweConstants.SweDataComponentType;
import org.n52.shetland.ogc.swe.SweDataComponentVisitor;
import org.n52.shetland.ogc.swe.VoidSweDataComponentVisitor;
import org.n52.shetland.w3c.xlink.Referenceable;

/**
 * @since 1.0.0
 *
 */
public class SweCount extends SweAbstractSimpleType<Integer> {

    private Integer value;
    private Referenceable<SweAllowedValues> constraint;

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public SweCount setValue(final Integer value) {
        this.value = value;
        return this;
    }

    @Override
    public boolean isSetValue() {
        return value != null;
    }

    @Override
    public void setStringValue(String s) {
        if (s != null && !s.isEmpty()) {
            setValue(Integer.parseInt(s));
        }
    }

    @Override
    public String getStringValue() {
        if (isSetValue()) {
            return Integer.toString(value);
        }
        return null;
    }

    @Override
    public SweDataComponentType getDataComponentType() {
        return SweDataComponentType.Count;
    }

    public void increaseCount() {
        increaseCount(1);
    }

    public void increaseCount(int count) {
        this.value += count;
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
    public <T, X extends Throwable> T accept(SweDataComponentVisitor<T, X> visitor) throws X {
        return visitor.visit(this);
    }

    @Override
    public <X extends Throwable> void accept(VoidSweDataComponentVisitor<X> visitor) throws X {
        visitor.visit(this);
    }

    @Override
    public SweCount copy() {
        SweCount copy = new SweCount();
        copyValueTo(copy);
        if (isSetValue()) {
            copy.setValue(getValue());
        }
        if (isSetContstraint()) {
            copy.setConstraint(getConstraint());
        }
        return copy;
    }
}
