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
package org.n52.faroe;

import org.n52.janmayen.event.Event;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * This event is fired if the {@link SettingValue} of a {@link SettingDefinition} has been changed or a
 * {@link SettingDefinition} was deleted.
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 * @param <T>
 *            the settings type
 *
 * @since 1.0.0
 */
@SuppressFBWarnings({ "EI_EXPOSE_REP", "EI_EXPOSE_REP2" })
public class SettingsChangeEvent<T> implements Event {

    private final SettingDefinition<T> setting;
    private final String settings;
    private final SettingValue<T> oldValue;
    private final SettingValue<T> newValue;

    public SettingsChangeEvent(SettingDefinition<T> setting, SettingValue<T> oldValue, SettingValue<T> newValue) {
        this.setting = setting;
        this.settings = null;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public SettingsChangeEvent(String settings, SettingValue<T> oldValue, SettingValue<T> newValue) {
        this.setting = null;
        this.settings = settings;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public SettingDefinition<T> getSetting() {
        return setting;
    }

    public String getSettings() {
        return settings;
    }

    public SettingValue<T> getOldValue() {
        return oldValue;
    }

    public SettingValue<T> getNewValue() {
        return newValue;
    }

    public boolean hasNewValue() {
        return getNewValue() != null;
    }

    public boolean hasOldValue() {
        return getOldValue() != null;
    }

    @Override
    public String toString() {
        return String.format("SettingsChangeEvent[setting=%s, settings=%s, oldValue=%s, newValue=%s", getSetting(),
                getSettings(), getOldValue(), getNewValue());
    }

}
