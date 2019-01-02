/*
 * Copyright 2015-2019 52°North Initiative for Geospatial Open Source
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

import org.joda.time.DateTime;
import org.n52.shetland.ogc.UoM;
import org.n52.shetland.ogc.swe.RangeValue;
import org.n52.shetland.ogc.swe.SweConstants.SweDataComponentType;
import org.n52.shetland.w3c.xlink.Referenceable;
import org.n52.shetland.ogc.swe.SweDataComponentVisitor;
import org.n52.shetland.ogc.swe.VoidSweDataComponentVisitor;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk
 *         J&uuml;rrens</a>
 *
 * @since 1.0.0
 */
public class SweTimeRange
        extends SweAbstractUomType<RangeValue<DateTime>> {

    private RangeValue<DateTime> value;
    private Referenceable<SweAllowedTimes> constraint;

    public SweTimeRange() {
    }

    public SweTimeRange(RangeValue<DateTime> value) {
        this.value = value;
    }

    public SweTimeRange(RangeValue<DateTime> value, String uom) {
        this.value = value;
        setUom(uom);
    }

    public SweTimeRange(RangeValue<DateTime> value, UoM uom) {
        this.value = value;
        setUom(uom);
    }

    @Override
    public RangeValue<DateTime> getValue() {
        return value;
    }

    @Override
    public String getStringValue() {
        return value.toString();
    }

    @Override
    public boolean isSetValue() {
        return value != null && value.isSetStartValue() && value.isSetEndValue();
    }

    @Override
    public SweTimeRange setValue(final RangeValue<DateTime> value) {
        this.value = value;
        return this;
    }

    /**
     * @return the constraint
     */
    public Referenceable<SweAllowedTimes> getConstraint() {
        return constraint;
    }

    /**
     * @param constraint the constraint to set
     */
    public void setConstraint(SweAllowedTimes constraint) {
        this.constraint = Referenceable.of(constraint);
    }

    /**
     * @param constraint the constraint to set
     */
    public void setConstraint(Referenceable<SweAllowedTimes> constraint) {
        this.constraint = constraint;
    }

    public boolean isSetContstraint() {
        return getConstraint() != null && !getConstraint().isAbsent();
    }

    @Override
    public SweDataComponentType getDataComponentType() {
        return SweDataComponentType.TimeRange;
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
    public SweTimeRange copy() {
        SweTimeRange copy = new SweTimeRange();
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
}
