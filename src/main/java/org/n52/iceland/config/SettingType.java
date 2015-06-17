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

import java.io.File;
import java.net.URI;

import org.n52.iceland.config.json.JsonConstants;
import org.n52.iceland.i18n.LocalizedString;
import org.n52.iceland.ogc.gml.time.TimeInstant;

/**
 * Enum to describe the type of a {@code SettingDefinition} and
 * {@code SettingValue}.
 *
 * @see SettingDefinition
 * @see SettingValue
 * @author Christian Autermann <c.autermann@52north.org>
 * @since 4.0.0
 */
public enum SettingType {
    /**
     * Type for {@link Boolean} and {@code boolean}.
     */
    BOOLEAN,
    /**
     * Type for {@link Integer} and {@code int}.
     */
    INTEGER,
    /**
     * Type for {@link File}.
     */
    FILE,
    /**
     * Type for {@link Double} and {@code double}.
     */
    NUMERIC,
    /**
     * Type for {@link String}.
     */
    STRING,
    /**
     * Type for {@link URI}.
     */
    URI,
    /**
     * Type for {@link TimeInstant}.
     */
    TIMEINSTANT,
    /**
     * Type for {@link LocalizedString}.
     */
    MULTILINGUAL_STRING,
    /**
     * Type for a selection.
     */
    CHOICE;


     public static SettingType fromString(String type) {
        switch (type) {
            case JsonConstants.INTEGER_TYPE:
                return SettingType.INTEGER;
            case JsonConstants.NUMBER_TYPE:
                return SettingType.NUMERIC;
            case JsonConstants.BOOLEAN_TYPE:
                return SettingType.BOOLEAN;
            case JsonConstants.TIME_INSTANT_TYPE:
                return SettingType.TIMEINSTANT;
            case JsonConstants.FILE_TYPE:
                return SettingType.FILE;
            case JsonConstants.STRING_TYPE:
                return SettingType.STRING;
            case JsonConstants.URI_TYPE:
                return SettingType.URI;
            case JsonConstants.MULTILINGUAL_TYPE:
                return SettingType.MULTILINGUAL_STRING;
            case JsonConstants.CHOICE_TYPE:
                return SettingType.CHOICE;
            default:
                throw new IllegalArgumentException(String.format("Unknown Type %s", type));
        }
    }

    public static String toString(SettingType type) {
        switch (type) {
            case INTEGER:
                return JsonConstants.INTEGER_TYPE;
            case NUMERIC:
                return JsonConstants.NUMBER_TYPE;
            case BOOLEAN:
                return JsonConstants.BOOLEAN_TYPE;
            case TIMEINSTANT:
                return JsonConstants.TIME_INSTANT_TYPE;
            case FILE:
                return JsonConstants.FILE_TYPE;
            case STRING:
                return JsonConstants.STRING_TYPE;
            case URI:
                return JsonConstants.URI_TYPE;
            case MULTILINGUAL_STRING:
                return JsonConstants.MULTILINGUAL_TYPE;
            case CHOICE:
                return JsonConstants.CHOICE_TYPE;
            default:
                throw new IllegalArgumentException(String.format("Unknown Type %s", type));
        }
    }

    @Override
    public String toString() {
        return toString(this);
    }

}
