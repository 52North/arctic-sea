/*
 * Copyright 2016-2017 52°North Initiative for Geospatial Open Source
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

import org.n52.shetland.ogc.swe.SweConstants.SweDataComponentType;
import org.n52.shetland.ogc.swe.SweDataComponentVisitor;
import org.n52.shetland.ogc.swe.VoidSweDataComponentVisitor;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk
 * J&uuml;rrens</a>
 * @since 4.0.0
 */
public class SweCategory extends SweAbstractUomType<String> implements SweQuality {

    private String value;

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public SweCategory setValue(final String value) {
        this.value = value;
        return this;
    }

    public SweCategory setCodeSpace(final String codeSpace) {
        setUom(codeSpace);
        return this;
    }

    public String getCodeSpace() {
        return getUom();
    }

    public boolean isSetCodeSpace() {
        return isSetUom();
    }

    @Override
    public String toString() {
        return String.format("SosSweCategory [quality=%s, value=%s, codeSpace=%s, simpleType=%s]", getQuality(),
                             value, getUom(), getDataComponentType());
    }

    @Override
    public boolean isSetValue() {
        return value != null && !value.isEmpty();
    }

    @Override
    public String getStringValue() {
        return value;
    }

    @Override
    public SweDataComponentType getDataComponentType() {
        return SweDataComponentType.Category;
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
    public SweCategory copy() {
        SweCategory copy = new SweCategory();
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
