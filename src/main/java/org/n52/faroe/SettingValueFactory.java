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

import java.io.File;
import java.net.URI;

import org.joda.time.DateTime;

import org.n52.faroe.settings.BooleanSettingDefinition;
import org.n52.faroe.settings.ChoiceSettingDefinition;
import org.n52.faroe.settings.DateTimeSettingDefinition;
import org.n52.faroe.settings.FileSettingDefinition;
import org.n52.faroe.settings.IntegerSettingDefinition;
import org.n52.faroe.settings.MultilingualStringSettingDefinition;
import org.n52.faroe.settings.NumericSettingDefinition;
import org.n52.faroe.settings.StringSettingDefinition;
import org.n52.faroe.settings.UriSettingDefinition;
import org.n52.janmayen.i18n.MultilingualString;

/**
 * Factory to construct implementation specific {@link SettingValue}s.
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 * @since 1.0.0
 */
public interface SettingValueFactory {

    /**
     * Constructs a new {@code Boolean} setting value from the supplied definition and string value.
     *
     * @param setting     the setting definition
     * @param stringValue the value as string
     *
     * @return the implementation specific {@code SettingValue}
     */
    SettingValue<Boolean> newBooleanSettingValue(BooleanSettingDefinition setting, String stringValue);

    /**
     * Constructs a new {@code Integer} setting value from the supplied definition and string value.
     *
     * @param setting     the setting definition
     * @param stringValue the value as string
     *
     * @return the implementation specific {@code SettingValue}
     */
    SettingValue<Integer> newIntegerSettingValue(IntegerSettingDefinition setting, String stringValue);

    /**
     * Constructs a new {@code String} setting value from the supplied definition and string value.
     *
     * @param setting     the setting definition
     * @param stringValue the value as string
     *
     * @return the implementation specific {@code SettingValue}
     */
    SettingValue<String> newStringSettingValue(StringSettingDefinition setting, String stringValue);

    /**
     * Constructs a new {@code File} setting value from the supplied definition and string value.
     *
     * @param setting     the setting definition
     * @param stringValue the value as string
     *
     * @return the implementation specific {@code SettingValue}
     */
    SettingValue<File> newFileSettingValue(FileSettingDefinition setting, String stringValue);

    /**
     * Constructs a new {@code URI} setting value from the supplied definition and string value.
     *
     * @param setting     the setting definition
     * @param stringValue the value as string
     *
     * @return the implementation specific {@code SettingValue}
     */
    SettingValue<URI> newUriSettingValue(UriSettingDefinition setting, String stringValue);

    /**
     * Constructs a new {@code Double} setting value from the supplied definition and string value.
     *
     * @param setting     the setting definition
     * @param stringValue the value as string
     *
     * @return the implementation specific {@code SettingValue}
     */
    SettingValue<Double> newNumericSettingValue(NumericSettingDefinition setting, String stringValue);

    /**
     * Constructs a new {@code TimeInstant} setting value from the supplied definition and string value.
     *
     * @param setting     the setting definition
     * @param stringValue the value as string
     *
     * @return the implementation specific {@code SettingValue}
     */
    SettingValue<DateTime> newDateTimeSettingValue(DateTimeSettingDefinition setting, String stringValue);

    /**
     * Constructs a new {@code MultilingualString} setting value from the supplied definition and string value.
     *
     * @param setting     the setting definition
     * @param stringValue the value as string
     *
     * @return the implementation specific {@code SettingValue}
     */
    SettingValue<MultilingualString> newMultiLingualStringValue(MultilingualStringSettingDefinition setting,
                                                                String stringValue);

    /**
     * Constructs a new {@code String} setting value from the supplied definition and string value.
     *
     * @param setting     the setting definition
     * @param stringValue the value as string
     *
     * @return the implementation specific {@code SettingValue}
     */
    SettingValue<String> newChoiceSettingValue(ChoiceSettingDefinition setting, String stringValue);

    /**
     * Constructs a new generic setting value from the supplied definition and string value.
     *
     * @param setting     the setting definition
     * @param stringValue the value as string
     *
     * @return the implementation specific {@code SettingValue}
     */
    SettingValue<?> newSettingValue(SettingDefinition<?> setting, String stringValue);
}
