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
 * @since 1.0.0
 *
 */
public class RectifiedGridCoverage
        implements DiscreteCoverage<SortedMap<QuantityValued<?, ?>, Value<?>>> {

    private static final String GML_ID_PREFIX = "rgc_";
    private final String gmlId;
    private final SortedMap<QuantityValued<?, ?>, Value<?>> value = Maps.newTreeMap();
    private UoM unit;
    private String rangeParameters;

    public RectifiedGridCoverage(String gmlId) {
        if (Strings.isNullOrEmpty(gmlId)) {
            this.gmlId = GML_ID_PREFIX + JavaHelper.generateID(toString());
        } else if (!gmlId.startsWith(GML_ID_PREFIX)) {
            this.gmlId = GML_ID_PREFIX + gmlId;
        } else {
            this.gmlId = gmlId;
        }
    }

    @Override
    public String getGmlId() {
        return gmlId;
    }

    @Override
    public RectifiedGridCoverage setValue(SortedMap<QuantityValued<?, ?>, Value<?>> value) {
        this.value.clear();
        addValue(value);
        return this;
    }

    public void addValue(QuantityValued<?, ?> key, Value<?> value) {
        this.value.put(key, value);
    }

    public void addValue(SortedMap<QuantityValued<?, ?>, Value<?>> value) {
        this.value.putAll(value);
    }

    public void addValue(Double key, Value<?> value) {
        this.value.put(new QuantityValue(key), value);
    }

    public void addValue(Double from, Double to, Value<?> value) {
        this.value.put(new QuantityRangeValue(from, to), value);
    }

    @Override
    public SortedMap<QuantityValued<?, ?>, Value<?>> getValue() {
        return value;
    }

    @Override
    public void setUnit(String unit) {
        this.unit = new UoM(unit);
    }

    @Override
    public RectifiedGridCoverage setUnit(UoM unit) {
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
    public boolean isSetUnit() {
        return getUnitObject() != null && !getUnitObject().isEmpty();
    }

    @Override
    public boolean isSetValue() {
        return CollectionHelper.isNotEmpty(value);
    }

    @Override
    public <X, E extends Exception> X accept(ValueVisitor<X, E> visitor)
            throws E {
        return visitor.visit(this);
    }

    /**
     * Get the domainSet
     *
     * @return The domainSet as {@link QuantityValued<?, ?>} {@link List}
     */
    public List<QuantityValued<?, ?>> getDomainSet() {
        return Lists.newArrayList(getValue().keySet());
    }

    @Override
    public Collection<Value<?>> getRangeSet() {
        return getValue().values();
    }

    @Override
    public String getRangeParameters() {
        return rangeParameters;
    }

    @Override
    public void setRangeParameters(String rangeParameters) {
        this.rangeParameters = rangeParameters;
    }

    @Override
    public boolean isSetRangeParameters() {
        return !Strings.isNullOrEmpty(getRangeParameters());
    }
}
