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
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public interface JSONSettingConstants {

    String VALUE_KEY = "value";
    String SECTIONS_KEY = "sections";
    String TITLE_KEY = "title";
    String DESCRIPTION_KEY = "description";
    String SETTINGS_KEY = "settings";
    String TYPE_KEY = "type";
    String DEFAULT_KEY = "default";
    String REQUIRED_KEY = "required";
    String MINIMUM_EXCLUSIVE_KEY = "minimumExclusive";
    String MAXIMUM_KEY = "maximum";
    String MAXIMUM_EXCLUSIVE_KEY = "maximumExclusive";
    String MINIMUM_KEY = "minimum";
    String OPTIONS_KEY = "options";

    String BOOLEAN_TYPE = "boolean";
    String INTEGER_TYPE = "integer";
    String NUMBER_TYPE = "number";
    String STRING_TYPE = "string";
    String CHOICE_TYPE = "choice";
    String MULTILINGUAL_TYPE = "multilingual";
    String TIME_INSTANT_TYPE = "timeInstant";
    String FILE_TYPE = "file";
    String URI_TYPE = "uri";

    String SETTING_DEFINITIONS = "settingDefinitions";
}
