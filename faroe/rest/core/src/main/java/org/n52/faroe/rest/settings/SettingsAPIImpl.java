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
package org.n52.faroe.rest.settings;

import org.joda.time.DateTime;
import org.n52.faroe.SettingDefinition;
import org.n52.faroe.SettingDefinitionGroup;
import org.n52.faroe.SettingType;
import org.n52.faroe.SettingValue;
import org.n52.faroe.SettingsDefinitionDao;
import org.n52.faroe.SettingsService;
import org.n52.faroe.settings.ChoiceSettingDefinition;
import org.n52.janmayen.i18n.MultilingualString;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.File;
import java.io.Serializable;
import java.net.URI;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SettingsAPIImpl implements InitializingBean, SettingsAPI {

    @Inject
    private SettingsService service;

    @Inject
    private SettingsDefinitionDao dao;

    public void afterPropertiesSet() {
        service.addSettings(dao.getAllSettings());
    }

    @Override
    public Collection<SettingDefinition<?>> getSettingDefinitions() {
        return service.getSettingDefinitions();

    }

    @Override
    public Collection<SettingValue<?>> getSettingValues() {
        return service.getSettingDefinitions().stream().map(definition -> {
            SettingValue<?> setting = service.getSetting(definition.getKey());
            if (setting == null) {
                if (definition.hasDefaultValue()) {
                    return createDefaultSettingValue(definition);
                } else {
                    return new NullSetting(definition);
                }
            }
            return setting;
        }).collect(Collectors.toList());
    }

    private SettingValue<? extends Serializable> createDefaultSettingValue(SettingDefinition<?> definition) {
        switch (definition.getType()) {
            case BOOLEAN:
                return service.getSettingFactory()
                              .newBooleanSettingValue(definition.getKey(), (Boolean) definition.getDefaultValue());
            case FILE:
                return service.getSettingFactory()
                              .newFileSettingValue(definition.getKey(), (File) definition.getDefaultValue());
            case INTEGER:
                return service.getSettingFactory()
                              .newIntegerSettingValue(definition.getKey(), (Integer) definition.getDefaultValue());
            case NUMERIC:
                return service.getSettingFactory()
                              .newNumericSettingValue(definition.getKey(), (Double) definition.getDefaultValue());
            case STRING:
                return service.getSettingFactory()
                              .newStringSettingValue(definition.getKey(), (String) definition.getDefaultValue());
            case URI:
                return service.getSettingFactory()
                              .newUriSettingValue(definition.getKey(), (URI) definition.getDefaultValue());
            case TIMEINSTANT:
                return service.getSettingFactory()
                              .newDateTimeSettingValue(definition.getKey(), (DateTime) definition.getDefaultValue());
            case MULTILINGUAL_STRING:
                return service.getSettingFactory()
                              .newMultiLingualStringSettingValue(definition.getKey(), (MultilingualString) definition.getDefaultValue());
            case CHOICE:
                return service.getSettingFactory()
                              .newChoiceSettingValue(definition.getKey(), (String) definition.getDefaultValue());
            default:
                throw new IllegalArgumentException(String.format("Type %s not supported", definition.getType()));
        }
    }

    @Override
    public Collection<SettingDefinition<?>> getSettingsByTitle(String groupTitle) {
        return service.getSettingDefinitions().stream()
                      .filter(definition -> definition.getGroup().getTitle().equalsIgnoreCase(groupTitle))
                      .collect(Collectors.toList());
    }

    @Override
    public Set<String> getGroups() {
        return service.getSettingDefinitions().stream().map(SettingDefinition::getGroup)
                      .map(SettingDefinitionGroup::getTitle).collect(Collectors.toSet());
    }

    @SuppressWarnings("unchecked")
    @Override
    public void addSettingDefinitions(Collection<SettingDefinition<?>> group) {
        if (group.stream().anyMatch(settingDefinition -> service.getKeys().contains(settingDefinition.getKey()))) {
            throw new IllegalArgumentException("definition already exists");
        }

        dao.saveSettings(group);
        service.addSettings(group);
    }

    @Override
    public void updateSettingValue(SettingValue<?> group) {
        service.changeSetting(group);
    }

    @Override
    public void deleteSettingValue(String setting) {
        service.deleteSetting(setting);
    }

    @Override
    public Collection<SettingValue<?>> getSettingValuesByGroup(String title) {
        return service.getSettingDefinitions().stream()
                      .filter(definition -> definition.getGroup().getTitle().equalsIgnoreCase(title))
                      .map(definition -> service.getSetting(definition))
                      .collect(Collectors.toList());
    }

    private static class NullSetting implements SettingValue<Object> {
        private final SettingDefinition<?> definition;

        public NullSetting(SettingDefinition<?> definition) {this.definition = definition;}

        @Override
        public String getKey() {
            return definition.getKey();
        }

        @Override
        public Object getValue() {
            return null;
        }

        @Override
        public void setKey(String key) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setValue(Object value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public SettingType getType() {
            return definition.getType();
        }

    }
}
