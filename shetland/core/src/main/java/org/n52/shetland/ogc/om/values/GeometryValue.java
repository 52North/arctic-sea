/*
 * Copyright 2015-2020 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.ogc.om.values;

import org.locationtech.jts.geom.Geometry;
import org.n52.shetland.ogc.UoM;
import org.n52.shetland.ogc.gml.AbstractGeometry;
import org.n52.shetland.ogc.om.values.visitor.ValueVisitor;
import org.n52.shetland.util.IdGenerator;

/**
 * Geometry measurement representation for observation
 *
 * @since 1.0.0
 *
 */
public class GeometryValue
        extends AbstractGeometry
        implements Value<Geometry> {
    private static final String GML_ID_PREFIX = "sp_";
    /**
     * Unit of measure
     */
    private UoM unit;

    public GeometryValue(AbstractGeometry abstractGeometry) {
        setDescription(abstractGeometry.getDescription());
        setGeometry(abstractGeometry.getGeometry());
        setIdentifier(abstractGeometry.getIdentifierCodeWithAuthority());
        setName(abstractGeometry.getName());
        setGmlId(GML_ID_PREFIX + IdGenerator.generate(toString()));
    }

    /**
     * construcor
     *
     * @param value
     *            Geometry value
     */
    public GeometryValue(Geometry value) {
        setValue(value);
        setGmlId(GML_ID_PREFIX + IdGenerator.generate(toString()));
    }

    @Override
    public GeometryValue setValue(Geometry value) {
        setGeometry(value);
        return this;
    }

    @Override
    public Geometry getValue() {
        return getGeometry();
    }

    @Override
    public void setUnit(String unit) {
        this.unit = new UoM(unit);
    }

    @Override
    public GeometryValue setUnit(UoM unit) {
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
        return String.format("GeometryValue [value=%s, unit=%s]", getValue(), getUnit());
    }

    @Override
    public boolean isSetValue() {
        return isSetGeometry();
    }

    @Override
    public <X, E extends Exception> X accept(ValueVisitor<X, E> visitor) throws E {
        return visitor.visit(this);
    }
}
