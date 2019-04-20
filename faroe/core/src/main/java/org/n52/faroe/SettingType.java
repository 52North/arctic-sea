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

import org.joda.time.DateTime;

import org.n52.janmayen.i18n.LocalizedString;

/**
 * Enum to describe the type of a {@code SettingDefinition} and {@code SettingValue}.
 *
 * @see SettingDefinition
 * @see SettingValue
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 * @since 1.0.0
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
     * Type for {@link DateTime}.
     */
    TIMEINSTANT,
    /**
     * Type for {@link LocalizedString}.
     */
    MULTILINGUAL_STRING,
    /**
     * Type for a selection.
     */
    CHOICE,
    /**
     * Type for a pair
     */
    PAIR;

    public static SettingType fromString(String type) {
        switch (type) {
            case JSONSettingConstants.INTEGER_TYPE:
                return SettingType.INTEGER;
            case JSONSettingConstants.NUMBER_TYPE:
                return SettingType.NUMERIC;
            case JSONSettingConstants.BOOLEAN_TYPE:
                return SettingType.BOOLEAN;
            case JSONSettingConstants.TIME_INSTANT_TYPE:
                return SettingType.TIMEINSTANT;
            case JSONSettingConstants.FILE_TYPE:
                return SettingType.FILE;
            case JSONSettingConstants.STRING_TYPE:
                return SettingType.STRING;
            case JSONSettingConstants.URI_TYPE:
                return SettingType.URI;
            case JSONSettingConstants.MULTILINGUAL_TYPE:
                return SettingType.MULTILINGUAL_STRING;
            case JSONSettingConstants.CHOICE_TYPE:
                return SettingType.CHOICE;
            case JSONSettingConstants.PAIR_TYPE:
                return SettingType.PAIR;
            default:
                throw unknownType(type);
        }
    }

    public static String toString(SettingType type) {
        switch (type) {
            case INTEGER:
                return JSONSettingConstants.INTEGER_TYPE;
            case NUMERIC:
                return JSONSettingConstants.NUMBER_TYPE;
            case BOOLEAN:
                return JSONSettingConstants.BOOLEAN_TYPE;
            case TIMEINSTANT:
                return JSONSettingConstants.TIME_INSTANT_TYPE;
            case FILE:
                return JSONSettingConstants.FILE_TYPE;
            case STRING:
                return JSONSettingConstants.STRING_TYPE;
            case URI:
                return JSONSettingConstants.URI_TYPE;
            case MULTILINGUAL_STRING:
                return JSONSettingConstants.MULTILINGUAL_TYPE;
            case CHOICE:
                return JSONSettingConstants.CHOICE_TYPE;
            case PAIR:
                return JSONSettingConstants.PAIR_TYPE;
            default:
                throw unknownType(type);
        }
    }

    @Override
    public String toString() {
        return toString(this);
    }

    private static IllegalArgumentException unknownType(Object type) {
        return new IllegalArgumentException(String.format("Unknown Type %s", type));
    }

}
