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
import java.util.List;

import org.locationtech.jts.geom.Geometry;
import org.n52.shetland.ogc.gml.time.Time;

import com.google.common.collect.Lists;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public abstract class AbstractPofileTrajectoryElement<
        T> {

    private List<Value<?>> value = Lists.newArrayList();
    private Geometry location;
    private Time phenomenonTime;

    public AbstractPofileTrajectoryElement() {
        this(null, null, null);
    }

    public AbstractPofileTrajectoryElement(Collection<Value<?>> value) {
        this(null, null, value);
    }

    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public AbstractPofileTrajectoryElement(Time phenomenonTime, Geometry location, Collection<Value<?>> values) {
        setPhenomenonTime(phenomenonTime);
        setLocation(location);
        setValue(values);
    }

    /**
     * @return the value
     */
    public List<Value<?>> getValue() {
        return Collections.unmodifiableList(value);
    }

    /**
     * @param value
     *            the value to set
     * @return {@code this}
     */
    public T setValue(Collection<Value<?>> value) {
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
        return !getValue().isEmpty();
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
    @SuppressFBWarnings({ "EI_EXPOSE_REP" })
    public Geometry getLocation() {
        return location;
    }

    /**
     * @param location
     *            the location to set
     * @return {@code this}
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
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
    @SuppressFBWarnings({ "EI_EXPOSE_REP" })
    public Time getPhenomenonTime() {
        return phenomenonTime;
    }

    /**
     * @param phenomenonTime
     *            the phenomenonTime to set
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public void setPhenomenonTime(Time phenomenonTime) {
        this.phenomenonTime = phenomenonTime;
    }

    public boolean isSetPhenomenonTime() {
        return getPhenomenonTime() != null;
    }

}
