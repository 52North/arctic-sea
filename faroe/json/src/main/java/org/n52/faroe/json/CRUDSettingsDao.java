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
package org.n52.faroe.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import org.n52.faroe.JSONSettingConstants;
import org.n52.faroe.SettingDefinition;
import org.n52.faroe.SettingsDefinitionDao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CRUDSettingsDao extends JsonSettingsDao implements SettingsDefinitionDao {

    private final ObjectMapper objectMapper = createObjectMapper();

    @Override
    public Collection<SettingDefinition<?>> getAllSettings() {
        readLock().lock();

        try {
            ObjectNode settings = getConfiguration().with(JSONSettingConstants.SETTING_DEFINITIONS);
            List<SettingDefinition<?>> list = new ArrayList<>(settings.size());
            for (JsonNode setting : settings) {
                list.add(objectMapper.treeToValue(setting, SettingDefinition.class));
            }
            return list;

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } finally {
            readLock().unlock();
        }
    }

    @Override
    public void saveSettings(Collection<SettingDefinition<?>> settings) {
        settings.forEach(this::saveSetting);
    }

    private void saveSetting(SettingDefinition<?> setting) {
        writeLock().lock();
        try {
            ObjectNode settings = getConfiguration().with(JSONSettingConstants.SETTING_DEFINITIONS);
            settings.set(setting.getKey(), objectMapper.valueToTree(setting));
        } finally {
            writeLock().unlock();
        }
        configuration().scheduleWrite();
    }

    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new Jdk8Module());
        return mapper;
    }
}
