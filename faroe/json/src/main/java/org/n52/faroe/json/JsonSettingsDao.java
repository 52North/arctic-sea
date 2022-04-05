/*
 * Copyright 2015-${currentYear} 52°North Spatial Information Research GmbH
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

import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;

import org.n52.faroe.JSONSettingConstants;
import org.n52.faroe.SettingValue;
import org.n52.faroe.SettingsDao;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 *
 * @author Christian Autermann, Daniel Nüst
 */
public class JsonSettingsDao extends AbstractJsonDao implements SettingsDao {

    private JsonSettingsEncoder settingsEncoder;
    private JsonSettingsDecoder settingsDecoder;

    @Inject
    public void setSettingsEncoder(JsonSettingsEncoder settingsEncoder) {
        this.settingsEncoder = settingsEncoder;
    }

    protected JsonSettingsEncoder getSettingsEncoder() {
        return this.settingsEncoder;
    }

    @Inject
    public void setSettingsDecoder(Optional<JsonSettingsDecoder> settingsDecoder) {
        this.settingsDecoder = settingsDecoder.isPresent() ? settingsDecoder.get() : new JsonSettingsDecoder();
    }

    protected JsonSettingsDecoder getSettingsDecoder() {
        return this.settingsDecoder;
    }

    @Override
    public Set<SettingValue<?>> getSettingValues() {
        readLock().lock();
        try {
            JsonNode node = getConfiguration().path(JSONSettingConstants.SETTINGS_KEY);
            return getSettingsDecoder().decode(node);
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
            return getSettingsDecoder().decode(key, node);
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
            settingNode.set(JSONSettingConstants.VALUE_KEY, getSettingsEncoder().encodeValue(value));
        } finally {
            writeLock().unlock();
        }
        configuration().scheduleWrite();
    }

    @Override
    public void deleteAll() {
        this.configuration().delete();
    }
}
