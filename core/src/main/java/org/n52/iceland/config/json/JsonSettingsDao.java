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


import java.util.HashSet;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;

import org.n52.iceland.config.SettingType;
import org.n52.iceland.config.SettingValue;
import org.n52.iceland.config.SettingsDao;
import org.n52.iceland.i18n.LocaleHelper;
import org.n52.iceland.i18n.MultilingualString;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 *
 * @author Christian Autermann
 */
public class JsonSettingsDao extends AbstractJsonDao
        implements SettingsDao {
    private JsonSettingsEncoder settingsEncoder;
    private JsonSettingValueFactory settingValueFactory;

    @Inject
    public void setSettingValueFactory(JsonSettingValueFactory settingValueFactory) {
        this.settingValueFactory = settingValueFactory;
    }

    @Inject
    public void setSettingsEncoder(JsonSettingsEncoder settingsEncoder) {
        this.settingsEncoder = settingsEncoder;
    }

    protected JsonSettingsEncoder getSettingsEncoder() {
        return this.settingsEncoder;
    }

    @Override
    public Set<SettingValue<?>> getSettingValues() {
        readLock().lock();
        try {
            JsonNode node = getConfiguration().path(JsonConstants.SETTINGS);
            Set<SettingValue<?>> values = new HashSet<>(node.size());
            node.fieldNames().forEachRemaining(key -> values.add(createSettingValue(key, node.path(key))));
            return values;
        } finally {
            readLock().unlock();
        }
    }

    @Override
    public SettingValue<?> getSettingValue(String key) {
        readLock().lock();
        try {
            JsonNode node = getConfiguration().path(JsonConstants.SETTINGS).path(key);
            if (!node.isObject()) { return null; }
            return createSettingValue(key, node);
        } finally {
            readLock().unlock();
        }
    }

    @Override
    public void deleteSettingValue(String key) {
        writeLock().lock();
        try {
            getConfiguration().with(JsonConstants.SETTINGS).remove(key);
        } finally {
            writeLock().unlock();
        }
        configuration().scheduleWrite();
    }

    @Override
    public void saveSettingValue(SettingValue<?> value) {
        writeLock().lock();
        try {
            ObjectNode settings = getConfiguration().with(JsonConstants.SETTINGS);
            JsonNode node = settings.path(value.getKey());
            ObjectNode settingNode = (ObjectNode) Optional.ofNullable(node.isObject() ? node : null)
                    .orElseGet(()-> settings.putObject(value.getKey()));
            settingNode.put(JsonConstants.TYPE, value.getType().toString());
            settingNode.set(JsonConstants.VALUE, this.settingsEncoder.encodeValue(value));
        } finally {
            writeLock().unlock();
        }
        configuration().scheduleWrite();
    }

    @Override
    public void deleteAll() {
        this.configuration().delete();
    }

    protected SettingValue<?> createSettingValue(String key, JsonNode node) {
        SettingType type = SettingType.fromString(node.path(JsonConstants.TYPE).asText(null));
        Object value = decodeValue(type, node.path(JsonConstants.VALUE));
        return new JsonSettingValue<>(type).setKey(key).setValue(value);
    }

    protected Object decodeValue(SettingType type, JsonNode node) {
        switch (type) {
            case INTEGER:
                return node.intValue();
            case NUMERIC:
                return node.doubleValue();
            case BOOLEAN:
                return node.booleanValue();
            case TIMEINSTANT:
                return this.settingValueFactory.parseTimeInstant(node.textValue());
            case FILE:
                return this.settingValueFactory.parseFile(node.textValue());
            case STRING:
                return node.textValue();
            case URI:
                return this.settingValueFactory.parseUri(node.textValue());
            case MULTILINGUAL_STRING:
                return decodeMultilingualString(node);
            case CHOICE:
                return node.textValue();
            default:
                throw new IllegalArgumentException(String.format("Unknown Type %s", type));
        }
    }

    protected MultilingualString decodeMultilingualString(JsonNode json) {
        MultilingualString mls = new MultilingualString();
        json.fields().forEachRemaining(e -> {
            Locale locale = LocaleHelper.fromString(e.getKey());
            mls.addLocalization(locale, e.getValue().asText());
        });
        return mls;
    }

}
