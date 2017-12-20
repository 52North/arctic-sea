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

import org.n52.janmayen.event.Event;

/**
 * This event is fired if the {@link SettingValue} of a {@link SettingDefinition} has been changed or a
 * {@link SettingDefinition} was deleted.
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 * @param <T> the settings type
 *
 * @since 1.0.0
 */
public class SettingsChangeEvent<T> implements Event {

    private final SettingDefinition<T> setting;
    private final SettingValue<T> oldValue;
    private final SettingValue<T> newValue;

    public SettingsChangeEvent(SettingDefinition<T> setting, SettingValue<T> oldValue, SettingValue<T> newValue) {
        this.setting = setting;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public SettingDefinition<T> getSetting() {
        return setting;
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
        return String.format("SettingsChangeEvent[setting=%s, oldValue=%s, newValue=%s",
                             getSetting(), getOldValue(), getNewValue());
    }
}
