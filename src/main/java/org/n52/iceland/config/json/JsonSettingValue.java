/*
 * Copyright 2015 52°North Initiative for Geospatial Open Source
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
package org.n52.iceland.config.json;

import org.n52.iceland.config.SettingType;
import org.n52.iceland.config.SettingValue;

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
    public SettingValue<T> setKey(String key) {
        this.key = key;
        return this;
    }

    @Override
    public SettingValue<T> setValue(T value) {
        this.value = value;
        return this;
    }

    @Override
    public SettingType getType() {
        return this.type;
    }

}
