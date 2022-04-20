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
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.TreeMap;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.n52.shetland.ogc.UoM;
import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.gml.CodeWithAuthority;
import org.n52.shetland.ogc.gml.time.Time;
import org.n52.shetland.ogc.gml.time.TimePeriod;
import org.n52.shetland.ogc.om.values.visitor.ValueVisitor;
import org.n52.shetland.ogc.swe.SweDataRecord;
import org.n52.shetland.ogc.swe.SweField;

import com.google.common.collect.Lists;

public class TrajectoryValue extends AbstractFeature implements Value<List<TrajectoryElement>> {

    private List<TrajectoryElement> values = Lists.newArrayList();

    public TrajectoryValue(String identifier) {
        super(identifier);
    }

    public TrajectoryValue(CodeWithAuthority identifier) {
        super(identifier);
    }

    public TrajectoryValue(CodeWithAuthority identifier, String gmlId) {
        super(identifier, gmlId);
    }

    @Override
    public TrajectoryValue setValue(List<TrajectoryElement> value) {
        this.values.clear();
        if (value != null) {
            this.values.addAll(value);
        }
        return null;
    }

    public TrajectoryValue addValue(TrajectoryElement value) {
        if (value != null) {
            this.values.add(value);
        }
        return this;
    }

    public TrajectoryValue addValues(Collection<TrajectoryElement> value) {
        if (value != null) {
            this.values.addAll(value);
        }
        return this;
    }

    @Override
    public List<TrajectoryElement> getValue() {
        return Collections.unmodifiableList(values);
    }

    @Override
    public void setUnit(String unit) {
    }

    @Override
    public Value<List<TrajectoryElement>> setUnit(UoM unit) {
        // nothing to do
        return this;
    }

    @Override
    public UoM getUnitObject() {
        return null;
    }

    @Override
    public String getUnit() {
        return null;
    }

    @Override
    public <X, E extends Exception> X accept(ValueVisitor<X, E> visitor) throws E {
        return visitor.visit(this);
    }

    public boolean isSetPhenomenonTime() {
        return values.stream().anyMatch(l -> l.isSetPhenomenonTime());
    }

    public Time getPhenomenonTime() {
        TimePeriod time = new TimePeriod();
        for (TrajectoryElement trajectoryElement : values) {
            if (trajectoryElement.isSetPhenomenonTime()) {
                time.extendToContain(trajectoryElement.getPhenomenonTime());
            }
        }
        return time;
    }

    public boolean isSetGeometry() {
        return isSetValue() && getValue().iterator().next().isSetLocation();
    }

    public Geometry getGeometry() {
        if (isSetGeometry()) {
            TreeMap<Time, Coordinate> map = new TreeMap<>();
            int srid = -1;
            for (TrajectoryElement element : getValue()) {
                if (element.isSetPhenomenonTime() && element.isSetLocation()) {
                    if (srid < 0) {
                        srid = element.getLocation().getSRID();
                    }
                    map.put(element.getPhenomenonTime(), element.getLocation().getCoordinate());
                }
            }
            if (!map.isEmpty()) {
                if (new HashSet<>(map.values()).size() == 1) {
                    return getValue().iterator().next().getLocation();
                } else {
                    return new GeometryFactory(new PrecisionModel(), srid)
                            .createLineString(map.values().toArray(new Coordinate[1]));
                }
            }
        }
        return null;
    }

    public SweDataRecord asDataRecord() {
        SweDataRecord dataRecord = new SweDataRecord();
        if (isSetIdentifier()) {
            dataRecord.setIdentifier(getIdentifier());
        }
        if (isSetName()) {
            dataRecord.setName(getName());
        }
        if (isSetDescription()) {
            dataRecord.setDescription(getDescription());
        }
        int counter = 0;
        for (TrajectoryElement elem : getValue()) {
            dataRecord.addField(new SweField("element_" + counter++, elem.asDataRecord()));
        }
        return dataRecord;
    }

}
