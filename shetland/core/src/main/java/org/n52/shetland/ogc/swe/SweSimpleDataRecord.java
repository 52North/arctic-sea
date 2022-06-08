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
package org.n52.shetland.ogc.swe;

import java.util.Objects;

import org.n52.shetland.ogc.swe.SweConstants.SweDataComponentType;

/**
 * @since 1.0.0
 *
 */
public class SweSimpleDataRecord extends SweAbstractDataRecord {

    @Override
    public SweDataComponentType getDataComponentType() {
        return SweDataComponentType.SimpleDataRecord;
    }

    @Override
    public SweSimpleDataRecord addField(final SweField field) {
        return (SweSimpleDataRecord) super.addField(field);
    }

    @Override
    public int hashCode() {
        return Objects.hash(42, super.hashCode(), 7, getDataComponentType());
    }

    @Override
    public boolean equals(Object obj) {
        return Objects.equals(this, obj);
    }

    @Override
    public String toString() {
        return String.format("SweSimpleDataRecord [fields=%s, definition=%s, label=%s, identifier=%s, xml=%s]",
                getFields(), getDefinition(), getLabel(), getIdentifier(), getXml());
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
    public SweSimpleDataRecord copy() {
        SweSimpleDataRecord clone = new SweSimpleDataRecord();
        copyValueTo(clone);
        getFields().stream().map(SweField::copy).forEach(clone::addField);
        return clone;
    }
}
