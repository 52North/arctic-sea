/*
 * Copyright 2016-2018 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.ogc.om;

import java.util.Objects;

import org.n52.shetland.ogc.gml.ReferenceType;
import org.n52.shetland.ogc.om.values.Value;

/**
 * Class representing a O&M conform NamedValue
 *
 * @since 1.0.0
 *
 * @param <T>
 *            value type
 */
public class NamedValue<T> implements Comparable<NamedValue<T>> {

    /**
     * Value name
     */
    private ReferenceType name;

    /**
     * Value
     */
    private Value<T> value;

    public NamedValue(ReferenceType name, Value<T> value) {
        this.name = name;
        this.value = value;
    }

    public NamedValue() {
        this(null, null);
    }

    public NamedValue(Value<T> value) {
        this(null, value);
    }

    /**
     * Get value name
     *
     * @return Value name
     */
    public ReferenceType getName() {
        return name;
    }

    /**
     * Set value name
     *
     * @param name
     *            Value name to set
     */
    public void setName(ReferenceType name) {
        this.name = name;
    }

    /**
     * Get value
     *
     * @return Value
     */
    public Value<T> getValue() {
        return value;
    }

    /**
     * Set value
     *
     * @param value
     *            Value to set
     */
    public void setValue(Value<T> value) {
        this.value = value;
    }

    /**
     * Check whether value name is set
     *
     * @return <code>true</code>, if value name is set
     */
    public boolean isSetName() {
        return name != null && name.isSetHref();
    }

    /**
     * Check whether value is set
     *
     * @return <code>true</code>, if value is set
     */
    public boolean isSetValue() {
        return value != null && value.isSetValue();
    }

    @Override
    public int compareTo(NamedValue<T> o) {
        if (o == null) {
            throw new NullPointerException();
        }
        if (getName() == null ^ o.getName() == null) {
            return (getName() == null) ? -1 : 1;
        }
        if (getName() == null && o.getName() == null) {
            return 0;
        }
        return getName().compareTo(o.getName());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.name);
        hash = 17 * hash + Objects.hashCode(this.value);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final NamedValue<?> other = (NamedValue<?>) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.value, other.value)) {
            return false;
        }
        return true;
    }
}
