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

import org.joda.time.DateTime;
import org.n52.shetland.ogc.swe.SweConstants.SweDataComponentType;
import org.n52.shetland.ogc.swe.SweDataComponentVisitor;
import org.n52.shetland.ogc.swe.VoidSweDataComponentVisitor;
import org.n52.shetland.util.DateTimeHelper;
import org.n52.shetland.w3c.xlink.Referenceable;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * SOS internal representation of SWE simpleType time
 *
 * @since 1.0.0
 */
public class SweTime extends SweAbstractUomType<DateTime> {

    /**
     * value
     */
    private DateTime value;
    private Referenceable<SweAllowedTimes> constraint;

    @Override
    @SuppressFBWarnings({ "EI_EXPOSE_REP" })
    public DateTime getValue() {
        return value;
    }

    @Override
    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public SweTime setValue(final DateTime value) {
        this.value = value;
        return this;
    }

    @Override
    public void setStringValue(String s) {
        if (s != null && !s.isEmpty()) {
            setValue(DateTimeHelper.parseIsoString2DateTime(s));
        }
    }

    @Override
    public String getStringValue() {
        if (isSetValue()) {
            return DateTimeHelper.formatDateTime2IsoString(value);
        }
        return null;
    }

    @Override
    public boolean isSetValue() {
        return value != null;
    }

    /**
     * @return the constraint
     */
    public Referenceable<SweAllowedTimes> getConstraint() {
        return constraint;
    }

    /**
     * @param constraint
     *            the constraint to set
     */
    public void setConstraint(SweAllowedTimes constraint) {
        this.constraint = Referenceable.of(constraint);
    }

    /**
     * @param constraint
     *            the constraint to set
     */
    public void setConstraint(Referenceable<SweAllowedTimes> constraint) {
        this.constraint = constraint;
    }

    public boolean isSetContstraint() {
        return getConstraint() != null && !getConstraint().isAbsent();
    }

    @Override
    public SweDataComponentType getDataComponentType() {
        return SweDataComponentType.Time;
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
    public SweTime copy() {
        SweTime copy = new SweTime();
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
