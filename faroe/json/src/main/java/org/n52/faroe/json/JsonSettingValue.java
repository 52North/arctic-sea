/*
 * Copyright 2015-2017 52°North Initiative for Geospatial Open Source
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
package org.n52.faroe.json;

import java.util.Objects;

import org.n52.faroe.SettingType;
import org.n52.faroe.SettingValue;

import com.google.common.base.MoreObjects;


/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class JsonSettingValue<T> implements SettingValue<T> {

    private final SettingType type;
    private String key;
    private T value;

    public JsonSettingValue(SettingType type, String key, T value) {
        this.type = type;
        this.key = key;
        this.value = value;
    }

    public JsonSettingValue(SettingType type, String key) {
        this(type, key, null);
    }

    public JsonSettingValue(SettingType type) {
        this(type, null, null);
    }

    @Override
    public String getKey() {
        return this.key;
    }

    @Override
    public T getValue() {
        return this.value;
    }

    @Override
    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public SettingType getType() {
        return this.type;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SettingValue<?>)) {
            return false;
        }

        SettingValue<?> that = (SettingValue<?>) obj;
        return Objects.equals(this.getType(), that.getType()) &&
               Objects.equals(this.getKey(), that.getKey()) &&
               Objects.equals(this.getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getType(), getKey(), getValue());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("type", getType())
                .add("key", getKey())
                .add("value", getValue())
                .toString();
    }
}
