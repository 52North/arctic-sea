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

/**
 * This interface defines constants used for JSON settings configuration.
 * The constants represent keys used in JSON objects to describe various settings
 * and their attributes within the application.
 * 
 * <p>These constants are used to ensure consistency and avoid magic strings
 * throughout the codebase when dealing with JSON settings.</p>
 * 
 * @author Christian Autermann
 */
public interface JSONSettingConstants {

    /**
     * Key for the value associated with a setting.
     */
    String VALUE_KEY = "value";

    /**
     * Key for the sections in a settings configuration.
     */
    String SECTIONS_KEY = "sections";

    /**
     * Key for the title of a setting.
     */
    String TITLE_KEY = "title";

    /**
     * Key for the description of a setting.
     */
    String DESCRIPTION_KEY = "description";

    /**
     * Key for the settings section in a JSON configuration.
     */
    String SETTINGS_KEY = "settings";

    /**
     * Key for the type of a setting.
     */
    String TYPE_KEY = "type";

    /**
     * Key for the default value of a setting.
     */
    String DEFAULT_KEY = "default";

    /**
     * Key indicating whether a setting is required.
     */
    String REQUIRED_KEY = "required";

    /**
     * Key for the minimum exclusive value of a setting.
     */
    String MINIMUM_EXCLUSIVE_KEY = "minimumExclusive";

    /**
     * Key for the maximum value of a setting.
     */
    String MAXIMUM_KEY = "maximum";

    /**
     * Key for the maximum exclusive value of a setting.
     */
    String MAXIMUM_EXCLUSIVE_KEY = "maximumExclusive";

    /**
     * Key for the minimum value of a setting.
     */
    String MINIMUM_KEY = "minimum";

    /**
     * Key for the options available for a setting.
     */
    String OPTIONS_KEY = "options";

    /**
     * Constant representing the boolean type for a setting.
     */
    String BOOLEAN_TYPE = "boolean";

    /**
     * Constant representing the integer type for a setting.
     */
    String INTEGER_TYPE = "integer";

    /**
     * Constant representing the number type for a setting.
     */
    String NUMBER_TYPE = "number";

    /**
     * Constant representing the string type for a setting.
     */
    String STRING_TYPE = "string";

    /**
     * Constant representing the choice type for a setting.
     */
    String CHOICE_TYPE = "choice";

    /**
     * Constant representing the multilingual type for a setting.
     */
    String MULTILINGUAL_TYPE = "multilingual";

    /**
     * Constant representing the time instant type for a setting.
     */
    String TIME_INSTANT_TYPE = "timeInstant";

    /**
     * Constant representing the file type for a setting.
     */
    String FILE_TYPE = "file";

    /**
     * Constant representing the URI type for a setting.
     */
    String URI_TYPE = "uri";
}
