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

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.locationtech.jts.geom.Geometry;
import org.n52.shetland.ogc.gml.time.Time;
import org.n52.shetland.ogc.om.values.visitor.TrajectoryElementVisitor;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.shetland.ogc.swe.SweAbstractDataComponent;
import org.n52.shetland.ogc.swe.SweDataRecord;
import org.n52.shetland.ogc.swe.SweField;

public class TrajectoryElement extends AbstractPofileTrajectoryElement<TrajectoryElement>
        implements Comparable<TrajectoryElement> {

    public TrajectoryElement() {
        super();
    }

    public TrajectoryElement(Time phenomenonTime, Geometry location, List<Value<?>> values) {
        super(phenomenonTime, location, values);
    }

    @Override
    public int compareTo(TrajectoryElement o) {
        if (o == null) {
            throw new NullPointerException();
        }
        if (getPhenomenonTime() == null ^ o.getPhenomenonTime() == null) {
            return (getPhenomenonTime() == null) ? -1 : 1;
        }
        if (getPhenomenonTime() == null && o.getPhenomenonTime() == null) {
            return 0;
        }
        return this.getPhenomenonTime()
                .equals(o.getPhenomenonTime()) ? 0 : 1;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TrajectoryElement other = (TrajectoryElement) obj;
        if ((getPhenomenonTime() == null) ? (other.getPhenomenonTime() != null)
                : !getPhenomenonTime().equals(other.getPhenomenonTime())) {
            return false;
        }
        if ((getLocation() == null) ? (other.getLocation() != null) : !getLocation().equals(other.getLocation())) {
            return false;
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 37 * hash + Objects.hashCode(this.getLocation());
        hash = 37 * hash + Objects.hashCode(this.getPhenomenonTime());
        hash = 37 * hash + Objects.hashCode(this.getValue());
        return hash;
    }

    public <X> Collection<X> accept(TrajectoryElementVisitor<X> visitor) throws OwsExceptionReport {
        return visitor.visit(this);
    }

    public SweDataRecord asDataRecord() {
        return valueAsDataRecord(new SweDataRecord());
    }

    public SweDataRecord valueAsDataRecord() {
        return valueAsDataRecord(new SweDataRecord());
    }

    public SweDataRecord valueAsDataRecord(SweDataRecord dataRecord) {
        int counter = 1;
        for (Value<?> v : getValue()) {
            if (v instanceof SweAbstractDataComponent) {
                SweAbstractDataComponent adc = (SweAbstractDataComponent) v;
                String name;
                if (adc.isSetName()) {
                    name = adc.getName()
                            .getValue();
                } else if (adc.isSetDefinition()) {
                    name = adc.getDefinition();
                } else {
                    name = "component_" + counter++;
                }
                dataRecord.addField(new SweField(name, adc));
            }
        }
        if (counter == 1 && dataRecord.getFields()
                .size() > 1 && dataRecord.getFields()
                        .stream()
                        .map(f -> f.getName()
                                .getValue())
                        .collect(Collectors.toSet())
                        .size() != dataRecord.getFields()
                                .size()) {
            for (SweField field : dataRecord.getFields()) {
                field.getName()
                        .setValue(field.getName()
                                .getValue() + "_" + counter++);
            }
        }
        return dataRecord;
    }
}
