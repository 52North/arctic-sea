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
package org.n52.shetland.ogc.om.values;

import java.util.Collection;
import java.util.List;
import java.util.SortedMap;

import org.n52.shetland.ogc.UoM;
import org.n52.shetland.ogc.om.values.visitor.ValueVisitor;
import org.n52.shetland.util.CollectionHelper;
import org.n52.shetland.util.JavaHelper;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * Class that represents a rectified grid coverage
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 4.4.0
 *
 */
public class RectifiedGridCoverage implements DiscreteCoverage<SortedMap<Double, Value<?>>> {

    private final String gmlId;
    private final SortedMap<Double, Value<?>> value = Maps.newTreeMap();
    private UoM unit;

    public RectifiedGridCoverage(String gmlId) {
        if (Strings.isNullOrEmpty(gmlId)) {
            this.gmlId = JavaHelper.generateID(toString());
        } else if (!gmlId.startsWith("rgc_")) {
            this.gmlId = "rgc_" + gmlId;
        } else {
            this.gmlId = gmlId;
        }
    }

    @Override
    public String getGmlId() {
        return gmlId;
    }

    @Override
    public RectifiedGridCoverage setValue(SortedMap<Double, Value<?>> value) {
        this.value.clear();
        addValue(value);
        return this;
    }

    public void addValue(Double key, Value<?> value) {
        this.value.put(key, value);
    }

    public void addValue(SortedMap<Double, Value<?>> value) {
        this.value.putAll(value);
    }

    @Override
    public SortedMap<Double, Value<?>> getValue() {
        return value;
    }

    @Override
    public void setUnit(String unit) {
        this.unit = new UoM(unit);
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
    public RectifiedGridCoverage setUnit(UoM unit) {
        this.unit = unit;
        return this;
    }

    @Override
    public boolean isSetUnit() {
        return getUnitObject() != null && !getUnitObject().isEmpty();
    }

    @Override
    public boolean isSetValue() {
        return CollectionHelper.isNotEmpty(value);
    }

    @Override
    public <X, E extends Exception> X accept(ValueVisitor<X, E> visitor) throws E {
        return visitor.visit(this);
    }

    /**
     * Get the domainSet
     *
     * @return The domainSet as {@link Double} {@link List}
     */
    public List<Double> getDomainSet() {
        return Lists.newArrayList(getValue().keySet());
    }

    @Override
    public Collection<Value<?>> getRangeSet() {
        return getValue().values();
    }

}
