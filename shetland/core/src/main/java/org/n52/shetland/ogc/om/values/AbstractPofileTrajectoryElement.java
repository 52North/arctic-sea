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

import java.util.List;

import org.locationtech.jts.geom.Geometry;
import org.n52.shetland.ogc.gml.time.Time;
import com.google.common.collect.Lists;

public abstract class AbstractPofileTrajectoryElement<T> {

    private List<Value<?>> value = Lists.newArrayList();
    private Geometry location;
    private Time phenomenonTime;


    public AbstractPofileTrajectoryElement() {
        this(null, null, null);
    }


    public AbstractPofileTrajectoryElement(List<Value<?>> value) {
        this(null, null, value);
    }

    public AbstractPofileTrajectoryElement(Time phenomenonTime, Geometry location, List<Value<?>> values) {
        setPhenomenonTime(phenomenonTime);
        setLocation(location);
        setValue(values);
    }


    /**
     * @return the value
     */
    public List<Value<?>> getValue() {
        return value;
    }

    /**
     * @param value
     *            the value to set
     * @return {@code this}
     */
    public T setValue(List<Value<?>> value) {
        this.value.clear();
        if (value != null) {
            this.value.addAll(value);
        }
        return (T) this;
    }

    /**
     * @param value
     *            the value to set
     * @return {@code this}
     */
    public T addValue(Value<?> value) {
        if (value != null) {
            this.value.add(value);
        }
        return (T) this;
    }

    public boolean isSetValue() {
        return getValue() != null;
    }

    /**
     * @return the simpleValue
     */
    public Value<?> getSimpleValue() {
        return value.iterator().next();
    }

    /**
     * @return the location
     */
    public Geometry getLocation() {
        return location;
    }

    /**
     * @param location
     *            the location to set
     * @return {@code this}
     */
    public T setLocation(Geometry location) {
        this.location = location;
        return (T) this;
    }

    public boolean isSetLocation() {
        return getLocation() != null;
    }

    /**
     * @return the phenomenonTime
     */
    public Time getPhenomenonTime() {
        return phenomenonTime;
    }

    /**
     * @param phenomenonTime
     *            the phenomenonTime to set
     */
    public void setPhenomenonTime(Time phenomenonTime) {
        this.phenomenonTime = phenomenonTime;
    }

    public boolean isSetPhenomenonTime() {
        return getPhenomenonTime() != null;
    }

}
