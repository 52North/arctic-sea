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
package org.n52.shetland.ogc.ows;

import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.base.MoreObjects;

import java.util.Comparator;
import java.util.Objects;

/**
 * A single value, encoded as a string. This type can be used for one value, for a spacing between allowed values, or
 * for the default value of a parameter.
 *
 * @author Christian Autermann
 */
public class OwsValue implements OwsValueRestriction {
    public static final Comparator<OwsValue> COMPARATOR = Comparator.comparing(OwsValue::getValue);
    private final String value;

    public OwsValue(String value) {
        this.value = Objects.requireNonNull(value);
    }

    @JsonValue
    public String getValue() {
        return this.value;
    }

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj.getClass() == getClass()) {
            OwsValue that = (OwsValue) obj;
            return Objects.equals(this.value, that.getValue());
        }
        return false;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).addValue(this.value).toString();
    }

    @Override
    public OwsValue asValue() {
        return this;
    }

    @Override
    public boolean isValue() {
        return true;
    }

    public static OwsValue of(String value) {
        return new OwsValue(value);
    }
}
