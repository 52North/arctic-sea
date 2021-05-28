/*
 * Copyright (C) 2015-2021 52°North Spatial Information Research GmbH
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

/**
 * @since 1.0.0
 *
 */
public class SweBoolean extends SweAbstractSimpleType<Boolean> {

    private Boolean value;

    @Override
    public Boolean getValue() {
        return value;
    }

    @Override
    public SweBoolean setValue(Boolean value) {
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
            setValue(Boolean.parseBoolean(s));
        }
    }

    @Override
    public String getStringValue() {
        return Boolean.toString(value);
    }

    @Override
    public SweDataComponentType getDataComponentType() {
        return SweDataComponentType.Boolean;
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
    public SweBoolean copy() {
        SweBoolean copy = new SweBoolean();
        copyValueTo(copy);
        if (isSetQuality()) {
            copy.setQuality(cloneQuality());
        }
        if (isSetValue()) {
            copy.setValue(getValue());
        }
        return copy;
    }

}
