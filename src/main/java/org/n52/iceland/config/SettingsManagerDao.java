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
package org.n52.iceland.config;

import java.util.Set;

import org.n52.iceland.ds.ConnectionProviderException;

/**
 *
 * @author Christian Autermann
 */
public interface SettingsManagerDao {
    /**
     * @return all saved setting values
     *
     * @throws ConnectionProviderException
     */
    Set<SettingValue<?>> getSettingValues()
            throws ConnectionProviderException;

    /**
     * Returns the value of the specified setting or {@code null} if it does not
     * exist.
     * <p/>
     *
     * @param key
     *            the key
     * <p/>
     * @return the value
     *
     * @throws ConnectionProviderException
     */
    SettingValue<?> getSettingValue(String key)
            throws ConnectionProviderException;

    /**
     * Deletes the setting with the specified key.
     * <p/>
     *
     * @param key
     *            the key
     *
     * @throws ConnectionProviderException
     */
    void deleteSettingValue(String key)
            throws ConnectionProviderException;

    /**
     * Saves the setting value.
     * <p/>
     *
     * @param setting
     *                the value
     *
     * @throws ConnectionProviderException
     */
    void saveSettingValue(SettingValue<?> setting)
            throws ConnectionProviderException;

    void deleteAll()
            throws ConnectionProviderException;

}
