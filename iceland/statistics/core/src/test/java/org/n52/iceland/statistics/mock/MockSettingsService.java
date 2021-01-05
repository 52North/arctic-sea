/*
 * Copyright 2015-2021 52°North Initiative for Geospatial Open Source
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
package org.n52.iceland.statistics.mock;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.n52.faroe.ConfigurationError;
import org.n52.faroe.SettingDefinition;
import org.n52.faroe.SettingValue;
import org.n52.faroe.SettingValueFactory;
import org.n52.faroe.SettingsService;

public class MockSettingsService implements SettingsService {

    @Override
    public void changeSetting(SettingValue<?> newValue) throws ConfigurationError {
    }

    @Override
    public void configure(Object object) throws ConfigurationError {
    }

    @Override
    public void deleteAll() {
    }

    @Override
    public void deleteSetting(SettingDefinition<?> setting) throws ConfigurationError {
    }

    @Override
    public SettingDefinition<?> getDefinitionByKey(String key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<String> getKeys() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> SettingValue<T> getSetting(SettingDefinition<T> key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> SettingValue<T> getSetting(String key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<SettingDefinition<?>> getSettingDefinitions() {
        throw new UnsupportedOperationException();
    }

    @Override
    public SettingValueFactory getSettingFactory() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<SettingDefinition<?>, SettingValue<?>> getSettings() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void reconfigure() {
    }

    @Override
    public void configureOnce(Object object) throws ConfigurationError {
    }

    @Override
    public void addSetting(SettingDefinition<?> def) {
        // TODO Auto-generated method stub

    }

    @Override
    public void addSettings(Collection<SettingDefinition<?>> defs) {
        // TODO Auto-generated method stub

    }

}
