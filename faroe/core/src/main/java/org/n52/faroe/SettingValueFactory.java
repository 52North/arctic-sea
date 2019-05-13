/*
 * Copyright 2015-2019 52°North Initiative for Geospatial Open Source
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
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.Locale;

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
import org.n52.janmayen.Json;
import org.n52.janmayen.Times;
import org.n52.janmayen.i18n.LocaleHelper;
import org.n52.janmayen.i18n.MultilingualString;

import com.fasterxml.jackson.databind.JsonNode;

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
    default SettingValue<Boolean> newBooleanSettingValue(BooleanSettingDefinition setting, String stringValue) {
        return newBooleanSettingValue(setting.getKey(), stringValue);
    }

    /**
     * Constructs a new {@code Boolean} setting value from the supplied key and string value.
     *
     * @param key         the setting key
     * @param stringValue the value as string
     *
     * @return the implementation specific {@code SettingValue}
     */
    default SettingValue<Boolean> newBooleanSettingValue(String key, String stringValue) {
        Boolean value;
        if (nullOrEmpty(stringValue)) {
            return newBooleanSettingValue(key, Boolean.FALSE);
        }
        String lc = stringValue.trim().toLowerCase(Locale.ROOT);
        switch (lc) {
            case "true":
            case "yes":
            case "on":
            case "1":
                return newBooleanSettingValue(key, Boolean.TRUE);
            case "false":
            case "no":
            case "off":
            case "0":
                return newBooleanSettingValue(key, Boolean.FALSE);
            default:
                throw new ConfigurationError(String.format("'%s' is not a valid boolean value", stringValue));
        }

    }

    /**
     * Constructs a new {@code Boolean} setting value from the supplied key and value.
     *
     * @param key   the setting key
     * @param value the value
     *
     * @return the implementation specific {@code SettingValue}
     */
    SettingValue<Boolean> newBooleanSettingValue(String key, Boolean value);

    /**
     * Constructs a new {@code Integer} setting value from the supplied definition and string value.
     *
     * @param setting     the setting definition
     * @param stringValue the value as string
     *
     * @return the implementation specific {@code SettingValue}
     */
    default SettingValue<Integer> newIntegerSettingValue(IntegerSettingDefinition setting, String stringValue) {
        return newIntegerSettingValue(setting.getKey(), stringValue);
    }

    /**
     * Constructs a new {@code Integer} setting value from the supplied key and string value.
     *
     * @param key         the setting key
     * @param stringValue the value as string
     *
     * @return the implementation specific {@code SettingValue}
     */
    default SettingValue<Integer> newIntegerSettingValue(String key, String stringValue) {
        return newIntegerSettingValue(key, decodeIntegerValue(stringValue));
    }

    /**
     * Constructs a new {@code Integer} setting value from the supplied key and value.
     *
     * @param key   the setting key
     * @param value the value
     *
     * @return the implementation specific {@code SettingValue}
     */
    SettingValue<Integer> newIntegerSettingValue(String key, Integer value);

    /**
     * Constructs a new {@code String} setting value from the supplied definition and string value.
     *
     * @param setting     the setting definition
     * @param stringValue the value as string
     *
     * @return the implementation specific {@code SettingValue}
     */
    default SettingValue<String> newStringSettingValue(StringSettingDefinition setting, String stringValue) {
        return newStringSettingValue(setting.getKey(), stringValue);
    }

    /**
     * Constructs a new {@code String} setting value from the supplied key and string value.
     *
     * @param key         the setting key
     * @param stringValue the value as string
     *
     * @return the implementation specific {@code SettingValue}
     */
    SettingValue<String> newStringSettingValue(String key, String stringValue);

    /**
     * Constructs a new {@code File} setting value from the supplied definition and string value.
     *
     * @param setting     the setting definition
     * @param stringValue the value as string
     *
     * @return the implementation specific {@code SettingValue}
     */
    default SettingValue<File> newFileSettingValue(FileSettingDefinition setting, String stringValue) {
        return newFileSettingValue(setting.getKey(), stringValue);
    }

    /**
     * Constructs a new {@code File} setting value from the supplied key and string value.
     *
     * @param key         the setting key
     * @param stringValue the value as string
     *
     * @return the implementation specific {@code SettingValue}
     */
    default SettingValue<File> newFileSettingValue(String key, String stringValue) {
        return newFileSettingValue(key, decodeFileValue(stringValue));
    }

    /**
     * Constructs a new {@code File} setting value from the supplied key and value.
     *
     * @param key   the setting key
     * @param value the value
     *
     * @return the implementation specific {@code SettingValue}
     */
    SettingValue<File> newFileSettingValue(String key, File value);

    /**
     * Constructs a new {@code URI} setting value from the supplied definition and string value.
     *
     * @param setting     the setting definition
     * @param stringValue the value as string
     *
     * @return the implementation specific {@code SettingValue}
     */
    default SettingValue<URI> newUriSettingValue(UriSettingDefinition setting, String stringValue) {
        return newUriSettingValue(setting.getKey(), stringValue);
    }

    /**
     * Constructs a new {@code URI} setting value from the supplied key and string value.
     *
     * @param key         the setting key
     * @param stringValue the value as string
     *
     * @return the implementation specific {@code SettingValue}
     */
    default SettingValue<URI> newUriSettingValue(String key, String stringValue) {
        return newUriSettingValue(key, decodeUriValue(stringValue));
    }

    /**
     * Constructs a new {@code URI} setting value from the supplied key and value.
     *
     * @param key   the setting key
     * @param value the value
     *
     * @return the implementation specific {@code SettingValue}
     */
    SettingValue<URI> newUriSettingValue(String key, URI value);

    /**
     * Constructs a new {@code Double} setting value from the supplied definition and string value.
     *
     * @param setting     the setting definition
     * @param stringValue the value as string
     *
     * @return the implementation specific {@code SettingValue}
     */
    default SettingValue<Double> newNumericSettingValue(NumericSettingDefinition setting, String stringValue) {
        return newNumericSettingValue(setting.getKey(), stringValue);
    }

    /**
     * Constructs a new {@code Double} setting value from the supplied key and string value.
     *
     * @param key         the setting key
     * @param stringValue the value as string
     *
     * @return the implementation specific {@code SettingValue}
     */
    default SettingValue<Double> newNumericSettingValue(String key, String stringValue) {
        return newNumericSettingValue(key, decodeNumericValue(stringValue));
    }

    /**
     * Constructs a new {@code Double} setting value from the supplied key and value.
     *
     * @param key   the setting key
     * @param value the value
     *
     * @return the implementation specific {@code SettingValue}
     */
    SettingValue<Double> newNumericSettingValue(String key, Double value);

    /**
     * Constructs a new {@code TimeInstant} setting value from the supplied definition and string value.
     *
     * @param setting     the setting definition
     * @param stringValue the value as string
     *
     * @return the implementation specific {@code SettingValue}
     */
    default SettingValue<DateTime> newDateTimeSettingValue(DateTimeSettingDefinition setting, String stringValue) {
        return newDateTimeSettingValue(setting.getKey(), stringValue);
    }

    /**
     * Constructs a new {@code TimeInstant} setting value from the supplied key and string value.
     *
     * @param key         the setting key
     * @param stringValue the value as string
     *
     * @return the implementation specific {@code SettingValue}
     */
    default SettingValue<DateTime> newDateTimeSettingValue(String key, String stringValue) {
        return newDateTimeSettingValue(key, decodeDateTimeValue(stringValue));
    }

    /**
     * Constructs a new {@code TimeInstant} setting value from the supplied key and value.
     *
     * @param key   the setting key
     * @param value the value
     *
     * @return the implementation specific {@code SettingValue}
     */
    SettingValue<DateTime> newDateTimeSettingValue(String key, DateTime value);

    /**
     * Constructs a new {@code MultilingualString} setting value from the supplied definition and string value.
     *
     * @param setting     the setting definition
     * @param stringValue the value as string
     *
     * @return the implementation specific {@code SettingValue}
     */
    default SettingValue<MultilingualString> newMultiLingualStringSettingValue(
            MultilingualStringSettingDefinition setting, String stringValue) {
        return newMultiLingualStringSettingValue(setting.getKey(), stringValue);
    }

    /**
     * Constructs a new {@code MultilingualString} setting value from the supplied key and string value.
     *
     * @param key         the setting key
     * @param stringValue the value as string
     *
     * @return the implementation specific {@code SettingValue}
     */
    default SettingValue<MultilingualString> newMultiLingualStringSettingValue(String key, String stringValue) {
        return newMultiLingualStringSettingValue(key, decodeMultiLingualStringValue(stringValue));
    }

    /**
     * Constructs a new {@code MultilingualString} setting value from the supplied key and value.
     *
     * @param key   the setting key
     * @param value the value
     *
     * @return the implementation specific {@code SettingValue}
     */
    SettingValue<MultilingualString> newMultiLingualStringSettingValue(String key, MultilingualString value);

    /**
     * Constructs a new {@code String} setting value from the supplied definition and string value.
     *
     * @param setting     the setting definition
     * @param stringValue the value as string
     *
     * @return the implementation specific {@code SettingValue}
     */
    default SettingValue<String> newChoiceSettingValue(ChoiceSettingDefinition setting, String stringValue) {
        if (!setting.hasOption(stringValue)) {
            if (setting.hasDefaultValue()) {
                return newChoiceSettingValue(setting.getKey(), setting.getDefaultValue());
            }
            throw new ConfigurationError("Invalid choice value");
        }
        return newChoiceSettingValue(setting.getKey(), stringValue);
    }

    /**
     * Constructs a new {@code String} setting value from the supplied definition and string value.
     *
     * @param key         the setting key
     * @param stringValue the value
     *
     * @return the implementation specific {@code SettingValue}
     */
    SettingValue<String> newChoiceSettingValue(String key, String stringValue);

    /**
     * Constructs a new generic setting value from the supplied definition and string value.
     *
     * @param setting the setting definition
     * @param value   the value as string
     *
     * @return the implementation specific {@code SettingValue}
     */
    default SettingValue<?> newSettingValue(SettingDefinition<?> setting, String value) {
        String key = setting.getKey();
        switch (setting.getType()) {
            case BOOLEAN:
                return newBooleanSettingValue(key, value);
            case FILE:
                return newFileSettingValue(key, value);
            case INTEGER:
                return newIntegerSettingValue(key, value);
            case NUMERIC:
                return newNumericSettingValue(key, value);
            case STRING:
                return newStringSettingValue(key, value);
            case URI:
                return newUriSettingValue(key, value);
            case TIMEINSTANT:
                return newDateTimeSettingValue(key, value);
            case MULTILINGUAL_STRING:
                return newMultiLingualStringSettingValue(key, value);
            case CHOICE:
                return newChoiceSettingValue((ChoiceSettingDefinition) setting, value);
            default:
                throw new IllegalArgumentException(String.format("Type %s not supported", setting.getType()));
        }
    }

    static Integer decodeIntegerValue(String stringValue) throws NumberFormatException {
        return nullOrEmpty(stringValue) ? null : Integer.valueOf(stringValue, 10);
    }

    static File decodeFileValue(String stringValue) {
        return nullOrEmpty(stringValue) ? null : new File(stringValue);
    }

    static URI decodeUriValue(String stringValue) throws ConfigurationError {
        URI uri;
        if (nullOrEmpty(stringValue)) {
            uri = null;
        } else {
            try {
                uri = new URI(stringValue);
            } catch (URISyntaxException e) {
                throw new ConfigurationError(e);
            }
        }
        return uri;
    }

    static Double decodeNumericValue(String stringValue) throws NumberFormatException {
        return nullOrEmpty(stringValue) ? null : Double.parseDouble(stringValue);
    }

    static DateTime decodeDateTimeValue(String stringValue) {
        return nullOrEmpty(stringValue) ? null : Times.decodeDateTime(stringValue);
    }

    static MultilingualString decodeMultiLingualStringValue(String stringValue) {
        MultilingualString ms = new MultilingualString();
        if (!nullOrEmpty(stringValue)) {
            JsonNode json = Json.loadString(stringValue);
            Iterator<String> it = json.fieldNames();
            while (it.hasNext()) {
                String lang = it.next();
                String value = json.path(lang).asText();
                ms.addLocalization(LocaleHelper.decode(lang), value);
            }
        }
        return ms;
    }

    /**
     * @param stringValue
     *
     * @return <code>stringValue == null || stringValue.trim().isEmpty()</code>
     */
    static boolean nullOrEmpty(String stringValue) {
        return stringValue == null || stringValue.trim().isEmpty();
    }
}
