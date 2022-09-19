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
package org.n52.faroeREST.springrest.settings;

import org.n52.faroe.SettingDefinition;
import org.n52.faroe.SettingDefinitionGroup;
import org.n52.faroe.SettingValue;
import org.n52.faroe.SettingsDefinitionDao;
import org.n52.faroe.SettingsService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
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
        return service.getSettings().values();
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
}
