/*
 * Copyright 2015-2022 52°North Spatial Information Research GmbH
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.n52.shetland.ogc.UoM;
import org.n52.shetland.ogc.gml.time.Time;
import org.n52.shetland.ogc.gml.time.TimePeriod;
import org.n52.shetland.ogc.om.TimeLocationValueTriple;
import org.n52.shetland.ogc.om.values.visitor.ValueVisitor;
import org.n52.shetland.util.CollectionHelper;

/**
 * {@link MultiValue} representing a time location value triple for observations
 *
 * @since 1.0.0
 *
 */
public class TLVTValue
        implements MultiValue<List<TimeLocationValueTriple>> {

    /**
     * Mesurement values
     */
    private List<TimeLocationValueTriple> value = new ArrayList<TimeLocationValueTriple>(0);

    /**
     * Unit of measure
     */
    private UoM unit;

    @Override
    public TLVTValue setValue(List<TimeLocationValueTriple> value) {
        this.value.clear();
        this.value.addAll(value);
        return this;
    }

    @Override
    public List<TimeLocationValueTriple> getValue() {
        Collections.sort(value);
        return value;
    }

    /**
     * Add time value pair value
     *
     * @param value
     *            Time value pair value to add
     */
    public void addValue(TimeLocationValueTriple value) {
        this.value.add(value);
    }

    /**
     * Add time value pair values
     *
     * @param values
     *            Time value pair values to add
     */
    public void addValues(List<TimeLocationValueTriple> values) {
        this.value.addAll(values);
    }

    @Override
    public void setUnit(String unit) {
        this.unit = new UoM(unit);
    }

    @Override
    public TLVTValue setUnit(UoM unit) {
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
    public Time getPhenomenonTime() {
        TimePeriod timePeriod = new TimePeriod();
        if (isSetValue()) {
            for (TimeLocationValueTriple timeValuePair : getValue()) {
                timePeriod.extendToContain(timeValuePair.getTime());
            }
        }
        return timePeriod;
    }

    @Override
    public boolean isSetValue() {
        return CollectionHelper.isNotEmpty(getValue());
    }

    @Override
    public <X, E extends Exception> X accept(ValueVisitor<X, E> visitor) throws E {
        return visitor.visit(this);
    }
}
