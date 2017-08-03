/*
 * Copyright 2017 52°North Initiative for Geospatial Open Source
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

import java.util.Optional;

/**
 *
 * Interface for setting definitions that can be used within the Service. Defined settings will be presented in the
 * administrator and installer view.
 *
 * @see SettingDefinitionGroup
 * @see SettingsService
 * @see org.n52.faroe.settings.FileSettingDefinition
 * @see org.n52.faroe.settings.BooleanSettingDefinition
 * @see org.n52.faroe.settings.IntegerSettingDefinition
 * @see org.n52.faroe.settings.NumericSettingDefinition
 * @see org.n52.faroe.settings.StringSettingDefinition
 * @see org.n52.faroe.settings.UriSettingDefinition
 * @param <T> The type of the value
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 * @since 1.0.0
 */
public interface SettingDefinition<T> extends Ordered {
    /**
     * @return the unique key of this definition
     */
    String getKey();

    /**
     * @return the title of this definition
     */
    String getTitle();

    /**
     * @return the description of this definition
     */
    String getDescription();

    /**
     * @return wether this setting is optional or required.
     */
    boolean isOptional();

    /**
     * @return the default value (or null if there is none)
     */
    T getDefaultValue();

    default Optional<T> getOptionalDefaultValue() {
        if (hasDefaultValue()) {
            return Optional.of(getDefaultValue());
        } else {
            return Optional.empty();
        }
    }

    /**
     * @return the group of this definition
     */
    SettingDefinitionGroup getGroup();

    default SettingDefinitionGroup getGroup(SettingDefinitionGroup defaultGroup) {
        return hasGroup() ? getGroup() : defaultGroup;
    }

    /**
     * @return if this definition has a non empty title
     */
    boolean hasTitle();

    /**
     * @return if this definition has a non empty description
     */
    boolean hasDescription();

    /**
     * @return if this definition has a default value
     */
    boolean hasDefaultValue();

    /**
     * @return if this definition has a group
     */
    boolean hasGroup();

    /**
     * Sets the unique identifier of this setting definition, which can be referenced by configurable classes.
     *
     * @param key the <b>unique</b> key
     */
    SettingDefinition<?> setKey(String key);

    /**
     * Sets the title of this setting definition, which will be presented to the user.
     *
     * @param title the title
     */
    SettingDefinition<?> setTitle(String title);

    /**
     * Sets the description of this setting definition, which should further describe the purpose of this setting. Can
     * contain XHTML markup.
     *
     * @param description the description
     */
    SettingDefinition<?> setDescription(String description);

    /**
     * Sets whether this setting is optional or can be null. By default all settings are required.
     *
     * @param optional if this setting is optional
     *
     */
    SettingDefinition<?> setOptional(boolean optional);

    /**
     * Sets the default value of this setting. All required settings should have a default setting to allow a smoother
     * integration of new settings in old configurations.
     *
     * @param defaultValue the default value
     */
    SettingDefinition<?> setDefaultValue(T defaultValue);

    /**
     * Sets the group of this definition. If no group is set, the setting will be moved to a default group.
     *
     * @param group the group
     */
    SettingDefinition<?> setGroup(SettingDefinitionGroup group);

    /**
     * @return the type of the value of this definition
     */
    SettingType getType();
}
