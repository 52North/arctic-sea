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
 * SOS internal representation of SWE simpleType observableProperty
 *
 * @since 4.0.0
 */
public class SweObservableProperty extends SweAbstractSimpleType<String> {

    /**
     * value
     */
    private String value;

    /**
     * constructor
     */
    public SweObservableProperty() {
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public SweObservableProperty setValue(final String value) {
        this.value = value;
        return this;
    }

    @Override
    public String getStringValue() {
        return value;
    }

    @Override
    public boolean isSetValue() {
        return value != null && !value.isEmpty();
    }

    @Override
    public SweDataComponentType getDataComponentType() {
        return SweDataComponentType.ObservableProperty;
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
    public SweObservableProperty clone() {
        SweObservableProperty clone = new SweObservableProperty();
        copyValueTo(clone);
        clone.setValue(getValue());
        return clone;
    }
}
