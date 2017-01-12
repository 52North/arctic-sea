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
package org.n52.faroe;

import java.util.Map;
import java.util.Set;

import org.n52.faroe.annotation.Configurable;
import org.n52.faroe.annotation.Setting;

/**
 * @author Christian Autermann
 */
public interface SettingsService {

    /**
     * Changes a setting. The change is propagated to all Objects that are configured. If the change fails for one of
     * these objects, the setting is reverted to the old value of the setting for all objects.
     *
     * @param newValue the new value of the setting
     *
     * @throws ConfigurationError if there is a problem changing the setting.
     */
    void changeSetting(SettingValue<?> newValue)
            throws ConfigurationError;

    /**
     * Configure {@code o} with the required settings. All changes to a setting required by the object will be applied.
     *
     * @param object the object to configure
     *
     * @throws ConfigurationError if there is a problem configuring the object
     * @see Configurable
     * @see Setting
     */
    void configure(Object object)
            throws ConfigurationError;

    /**
     * Deletes all settings and users.
     */
    void deleteAll();

    /**
     * Deletes the setting defined by {@code setting}.
     *
     * @param setting the definition
     *
     * @throws ConfigurationError if there is a problem deleting the setting
     */
    void deleteSetting(SettingDefinition<?, ?> setting)
            throws ConfigurationError;

    /**
     * Get the definition that is defined with the specified key.
     *
     * @param key the key
     *
     * @return the definition or {@code null} if there is no definition for the key
     */
    SettingDefinition<?, ?> getDefinitionByKey(String key);

    /**
     * @return the keys for all definitions
     */
    Set<String> getKeys();

    /**
     * Gets the value of the setting defined by {@code key}.
     *
     * @param <T> the type of the setting and value
     * @param key the definition of the setting
     *
     * @return the value of the setting
     */
    @SuppressWarnings(value = "unchecked")
    <T> SettingValue<T> getSetting(SettingDefinition<?, T> key);

    /**
     * Gets the value of the setting defined by {@code key}.
     *
     * @param <T> the type of the setting and value
     * @param key the id of the setting
     *
     * @return the value of the setting
     */
    @SuppressWarnings(value = "unchecked")
    <T> SettingValue<T> getSetting(String key);

    /**
     * Gets all {@code SettingDefinition}s known by this class.
     *
     * @return the definitions
     */
    Set<SettingDefinition<?, ?>> getSettingDefinitions();

    /**
     * @return the {@link SettingValueFactory} to produce values
     */
    SettingValueFactory getSettingFactory();

    /**
     * Gets all values for all definitions. If there is no value for a definition {@code null} is added to the map.
     *
     * @return all values by definition
     */
    Map<SettingDefinition<?, ?>, SettingValue<?>> getSettings();

    /**
     * Gets all values for all definitions and udpates (changes or configures) all configured objets.
     */
    void reconfigure();

}
