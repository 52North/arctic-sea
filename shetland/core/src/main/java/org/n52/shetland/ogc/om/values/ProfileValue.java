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
package org.n52.shetland.ogc.om.values;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.n52.shetland.ogc.UoM;
import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.gml.CodeWithAuthority;
import org.n52.shetland.ogc.gml.ReferenceType;
import org.n52.shetland.ogc.gml.time.Time;
import org.n52.shetland.ogc.gml.time.TimePeriod;
import org.n52.shetland.ogc.gwml.GWMLConstants;
import org.n52.shetland.ogc.om.NamedValue;
import org.n52.shetland.ogc.om.values.visitor.ValueVisitor;
import org.n52.shetland.ogc.swe.SweDataRecord;
import org.n52.shetland.ogc.swe.SweField;

import com.google.common.collect.Lists;

/**
 * Represents the GroundWaterML 2.0 GW_GeologyLogCoverage
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public class ProfileValue
        extends AbstractFeature
        implements Value<List<ProfileLevel>> {

    private QuantityValue fromLevel;
    private boolean queriedFromLevel;
    private QuantityValue toLevel;
    private boolean queriedToLevel;
    private List<ProfileLevel> values = Lists.newArrayList();

    public ProfileValue(String identifier) {
        super(identifier);
    }

    public ProfileValue(CodeWithAuthority identifier) {
        super(identifier);
    }

    public ProfileValue(CodeWithAuthority identifier, String gmlId) {
        super(identifier, gmlId);
    }

    @Override
    public ProfileValue setValue(List<ProfileLevel> value) {
        this.values.clear();
        this.values.addAll(value);
        return this;
    }

    public ProfileValue addValue(ProfileLevel value) {
        this.values.add(value);
        return this;
    }

    public ProfileValue addValues(List<ProfileLevel> value) {
        this.values.addAll(value);
        return this;
    }

    @Override
    public List<ProfileLevel> getValue() {
        return values;
    }

    @Override
    public void setUnit(String unit) {

    }

    @Override
    public ProfileValue setUnit(UoM unit) {
        // nothing to do
        return this;
    }

    @Override
    public String getUnit() {
        return null;
    }

    @Override
    public UoM getUnitObject() {
        return null;
    }

    @Override
    public boolean isSetUnit() {
        return false;
    }

    @Override
    public boolean isSetValue() {
        return !getValue().isEmpty();
    }

    /**
     * @return the fromLevel
     */
    public QuantityValue getFromLevel() {
        if (!isFromLevel() && !queriedFromLevel) {
            if (isSetValue()) {
                QuantityValue from = null;
                for (ProfileLevel profileLevel : values) {
                    if (profileLevel.isSetLevelStart()) {
                        if (from == null) {
                            from = profileLevel.getLevelStart();
                        } else if (profileLevel.getLevelStart().getValue().compareTo(from.getValue()) < 0) {
                            from = profileLevel.getLevelStart();
                        }
                    } else if (profileLevel.isSetLevelEnd()) {
                        if (from == null) {
                            from = profileLevel.getLevelEnd();
                        } else if (profileLevel.getLevelEnd().getValue().compareTo(from.getValue()) < 0) {
                            from = profileLevel.getLevelEnd();
                        }
                    }
                }
                if (from != null) {
                    setFromLevel(from);
                    if (!isSetUnit() && from.isSetUnit()) {
                        setUnit(from.getUomObject());
                    }
                }
            }
            queriedFromLevel = true;
        }
        return fromLevel;
    }

    /**
     * @param fromLevel
     *            the fromLevel to set
     */
    public ProfileValue setFromLevel(QuantityValue fromLevel) {
        this.fromLevel = fromLevel;
        return this;
    }

    public boolean isSetFromLevel() {
        return getFromLevel() != null;
    }

    public boolean isFromLevel() {
        return this.fromLevel != null;
    }

    /**
     * @return the toLevel
     */
    public QuantityValue getToLevel() {
        if (!isToLevel() && !queriedToLevel) {
            if (isSetValue()) {
                QuantityValue to = null;
                for (ProfileLevel profileLevel : values) {
                    if (profileLevel.isSetLevelEnd()) {
                        if (to == null) {
                            to = profileLevel.getLevelEnd();
                        } else if (profileLevel.getLevelEnd().getValue().compareTo(to.getValue()) > 0) {
                            to = profileLevel.getLevelEnd();
                        }
                    } else if (profileLevel.isSetLevelStart()) {
                        if (to == null) {
                            to = profileLevel.getLevelStart();
                        } else if (profileLevel.getLevelStart().getValue().compareTo(to.getValue()) > 0) {
                            to = profileLevel.getLevelStart();
                        }
                    }
                }
                if (to != null) {
                    setToLevel(to);
                    if (!isSetUnit() && to.isSetUnit()) {
                        setUnit(to.getUomObject());
                    }
                }
            }
            queriedToLevel = true;

        }
        return toLevel;
    }

    /**
     * @param toLevel
     *            the toLevel to set
     */
    public ProfileValue setToLevel(QuantityValue toLevel) {
        this.toLevel = toLevel;
        return this;
    }

    public boolean isSetToLevel() {
        return getToLevel() != null;
    }

    private boolean isToLevel() {
        return this.toLevel != null;
    }

    @Override
    public String getDefaultElementEncoding() {
        return GWMLConstants.NS_GWML_22;
    }

    @Override
    public <X, E extends Exception> X accept(ValueVisitor<X, E> visitor) throws E {
        return visitor.visit(this);
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
        for (ProfileLevel level : getValue()) {
            dataRecord.addField(new SweField("level_" + counter++, level.asDataRecord()));
        }
        return dataRecord;
    }

    public boolean isSetPhenomenonTime() {
        return values.stream().anyMatch(l -> l.isSetPhenomenonTime());
    }

    public Time getPhenomenonTime() {
        TimePeriod time = new TimePeriod();
        for (ProfileLevel profileLevel : values) {
            if (profileLevel.isSetPhenomenonTime()) {
                time.extendToContain(profileLevel.getPhenomenonTime());
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
            for (ProfileLevel level : getValue()) {
                if (level.isSetPhenomenonTime() && level.isSetLocation()) {
                    if (srid < 0) {
                        srid = level.getLocation().getSRID();
                    }
                    map.put(level.getPhenomenonTime(), level.getLocation().getCoordinate());
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

    public Collection<NamedValue<?>> getLevelFromToAsParameter() {
        SortedSet<NamedValue<?>> parameter = new TreeSet<>();
        if (isSetFromLevel() && getFromLevel().isSetDefinition()) {
            parameter.add(new NamedValue<>(new ReferenceType(getFromLevel().getDefinition()), getFromLevel()));
        }
        if (isSetToLevel() && getToLevel().isSetDefinition()) {
            parameter.add(new NamedValue<>(new ReferenceType(getToLevel().getDefinition()), getToLevel()));
        }
        return parameter;
    }

}
