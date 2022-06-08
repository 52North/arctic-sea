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
package org.n52.shetland.ogc.om.values;

import org.n52.shetland.ogc.UoM;
import org.n52.shetland.ogc.om.values.visitor.ValueVisitor;

/**
 * Nil template value for observation
 *
 * @since 1.0.0
 *
 */
public class NilTemplateValue implements Value<String> {

    /**
     * Unit of measure
     */
    private UoM unit;

    @Override
    public NilTemplateValue setValue(String value) {
        return this;
    }

    @Override
    public String getValue() {
        return "template";
    }

    @Override
    public void setUnit(String unit) {
        this.unit = new UoM(unit);
    }

    @Override
    public NilTemplateValue setUnit(UoM unit) {
        this.unit = unit;
        return this;
    }

    @Override
    public String getUnit() {
        if (isSetUnit()) {
            return unit.getUom();
        }
        return null;
    }

    @Override
    public UoM getUnitObject() {
        return this.unit;
    }

    @Override
    public String toString() {
        return String.format("NilTemplateValue [value=%s, unit=%s]", getValue(), getUnit());
    }

    @Override
    public boolean isSetValue() {
        return true;
    }

    @Override
    public <
            X,
            E extends Exception> X accept(ValueVisitor<X, E> visitor) throws E {
        return visitor.visit(this);
    }
}