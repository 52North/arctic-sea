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

import org.n52.shetland.ogc.swe.RangeValue;
import org.n52.shetland.ogc.swe.SweConstants.SweDataComponentType;
import org.n52.shetland.ogc.swe.SweDataComponentVisitor;
import org.n52.shetland.ogc.swe.VoidSweDataComponentVisitor;
import org.n52.shetland.w3c.xlink.Referenceable;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * @since 1.0.0
 *
 */
public class SweCountRange extends SweAbstractSimpleType<RangeValue<Integer>> {

    private RangeValue<Integer> value;
    private Referenceable<SweAllowedValues> constraint;

    @Override
    @SuppressFBWarnings({ "EI_EXPOSE_REP" })
    public RangeValue<Integer> getValue() {
        return value;
    }

    @Override
    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public SweCountRange setValue(final RangeValue<Integer> value) {
        this.value = value;
        return this;
    }

    @Override
    public boolean isSetValue() {
        return value != null;
    }

    @Override
    public void setStringValue(String s) {
        if (s != null && !s.isEmpty() && s.contains("/")) {
            String[] split = s.split("/");
            setValue(new RangeValue<Integer>(Integer.parseInt(split[0]), Integer.parseInt(split[1])));
        }
    }

    @Override
    public String getStringValue() {
        return value.toString();
    }

    /**
     * @return the constraint
     */
    public Referenceable<SweAllowedValues> getConstraint() {
        return constraint;
    }

    /**
     * @param constraint
     *            the constraint to set
     */
    public void setConstraint(SweAllowedValues constraint) {
        this.constraint = Referenceable.of(constraint);
    }

    /**
     * @param constraint
     *            the constraint to set
     */
    public void setConstraint(Referenceable<SweAllowedValues> constraint) {
        this.constraint = constraint;
    }

    public boolean isSetContstraint() {
        return getConstraint() != null && !getConstraint().isAbsent();
    }

    @Override
    public SweDataComponentType getDataComponentType() {
        return SweDataComponentType.CountRange;
    }

    @Override
    public <
            T,
            X extends Throwable> T accept(SweDataComponentVisitor<T, X> visitor) throws X {
        return visitor.visit(this);
    }

    @Override
    public <
            X extends Throwable> void accept(VoidSweDataComponentVisitor<X> visitor) throws X {
        visitor.visit(this);
    }

    @Override
    public SweCountRange copy() {
        SweCountRange copy = new SweCountRange();
        copyValueTo(copy);
        copyQuality(copy);
        if (isSetValue()) {
            copy.setValue(getValue().copy());
        }
        if (isSetContstraint()) {
            copy.setConstraint(getConstraint());
        }
        return copy;
    }

}
