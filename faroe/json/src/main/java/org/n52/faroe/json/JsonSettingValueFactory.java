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
package org.n52.faroe.json;

import java.io.File;
import java.net.URI;

import org.joda.time.DateTime;

import org.n52.faroe.SettingType;
import org.n52.faroe.SettingValue;
import org.n52.faroe.SettingValueFactory;
import org.n52.janmayen.i18n.MultilingualString;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class JsonSettingValueFactory implements SettingValueFactory {
    @Override
    public SettingValue<Boolean> newBooleanSettingValue(String key, Boolean value) {
        return new JsonSettingValue<>(SettingType.BOOLEAN, key, value);
    }

    @Override
    public SettingValue<Integer> newIntegerSettingValue(String key, Integer value) {
        return new JsonSettingValue<>(SettingType.INTEGER, key, value);
    }

    @Override
    public SettingValue<String> newStringSettingValue(String key, String value) {
        return new JsonSettingValue<>(SettingType.STRING, key, value);
    }

    @Override
    public SettingValue<String> newChoiceSettingValue(String key, String value) {
        return new JsonSettingValue<>(SettingType.CHOICE, key, value);
    }

    @Override
    public SettingValue<File> newFileSettingValue(String key, File value) {
        return new JsonSettingValue<>(SettingType.FILE, key, value);
    }

    @Override
    public SettingValue<URI> newUriSettingValue(String key, URI value) {
        return new JsonSettingValue<>(SettingType.URI, key, value);
    }

    @Override
    public SettingValue<Double> newNumericSettingValue(String key, Double value) {
        return new JsonSettingValue<>(SettingType.NUMERIC, key, value);
    }

    @Override
    public SettingValue<DateTime> newDateTimeSettingValue(String key, DateTime value) {
        return new JsonSettingValue<>(SettingType.TIMEINSTANT, key, value);
    }

    @Override
    public SettingValue<MultilingualString> newMultiLingualStringSettingValue(String key, MultilingualString value) {
        return new JsonSettingValue<>(SettingType.MULTILINGUAL_STRING, key, value);
    }
}
