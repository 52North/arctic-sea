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
package org.n52.faroe.json;

import java.util.HashSet;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;

import org.n52.faroe.ConfigurationError;
import org.n52.faroe.JSONSettingConstants;
import org.n52.faroe.SettingType;
import org.n52.faroe.SettingValue;
import org.n52.faroe.SettingValueFactory;
import org.n52.faroe.SettingsDao;
import org.n52.janmayen.i18n.LocaleHelper;
import org.n52.janmayen.i18n.MultilingualString;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 *
 * @author Christian Autermann, Daniel Nüst
 */
public class JsonSettingsDao extends AbstractJsonDao implements SettingsDao {

    private JsonSettingsEncoder settingsEncoder;

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
            JsonNode node = getConfiguration().path(JSONSettingConstants.SETTINGS_KEY);
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
            JsonNode node = getConfiguration().path(JSONSettingConstants.SETTINGS_KEY).path(key);
            if (!node.isObject()) {
                return null;
            }
            return createSettingValue(key, node);
        } finally {
            readLock().unlock();
        }
    }

    @Override
    public void deleteSettingValue(String key) {
        writeLock().lock();
        try {
            getConfiguration().with(JSONSettingConstants.SETTINGS_KEY).remove(key);
        } finally {
            writeLock().unlock();
        }
        configuration().scheduleWrite();
    }

    @Override
    public void saveSettingValue(SettingValue<?> value) {
        writeLock().lock();
        try {
            ObjectNode settings = getConfiguration().with(JSONSettingConstants.SETTINGS_KEY);
            JsonNode node = settings.path(value.getKey());
            ObjectNode settingNode = (ObjectNode) Optional.ofNullable(node.isObject() ? node : null)
                    .orElseGet(() -> settings.putObject(value.getKey()));
            settingNode.put(JSONSettingConstants.TYPE_KEY, value.getType().toString());
            settingNode.set(JSONSettingConstants.VALUE_KEY, this.settingsEncoder.encodeValue(value));
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
        SettingType type = SettingType.fromString(node.path(JSONSettingConstants.TYPE_KEY).asText(null));
        Object value = decodeValue(type, node.path(JSONSettingConstants.VALUE_KEY));
        return new JsonSettingValue<>(type, key, value);
    }

    protected Object decodeValue(SettingType type, JsonNode node) {
        switch (type) {
            case INTEGER:
                if (!node.canConvertToInt()) {
                    numberDecodeError(type, node);
                }
                return node.intValue();
            case NUMERIC:
                if (!node.isNumber()) {
                    numberDecodeError(type, node);
                }
                return node.doubleValue();
            case BOOLEAN:
                return node.booleanValue();
            case TIMEINSTANT:
                return SettingValueFactory.decodeDateTimeValue(node.textValue());
            case FILE:
                return SettingValueFactory.decodeFileValue(node.textValue());
            case STRING:
                return node.textValue();
            case URI:
                return SettingValueFactory.decodeUriValue(node.textValue());
            case MULTILINGUAL_STRING:
                return decodeMultilingualString(node);
            case CHOICE:
                return node.textValue();
            default:
                throw new ConfigurationError(String.format("Unknown Type %s", type));
        }
    }

    protected MultilingualString decodeMultilingualString(JsonNode json) {
        MultilingualString mls = new MultilingualString();
        json.fields().forEachRemaining(e -> {
            Locale locale = LocaleHelper.decode(e.getKey());
            mls.addLocalization(locale, e.getValue().asText());
        });
        return mls;
    }

    private void numberDecodeError(SettingType type, JsonNode node) {
        throw new ConfigurationError(String.format("Cannot decode setting to %s type: node type = %s, value = >%s<",
                                                   type, node.getNodeType(), node.toString()));
    }

}
