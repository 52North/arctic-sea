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
package org.n52.faroe.json;

import java.io.File;
import java.net.URI;

import org.joda.time.DateTime;

import org.n52.faroe.AbstractSettingValueFactory;
import org.n52.faroe.SettingType;
import org.n52.faroe.SettingValue;
import org.n52.janmayen.i18n.MultilingualString;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class JsonSettingValueFactory extends AbstractSettingValueFactory {
    @Override
    protected SettingValue<Boolean> newBooleanSettingValue() {
        return new JsonSettingValue<>(SettingType.BOOLEAN);
    }

    @Override
    protected SettingValue<Integer> newIntegerSettingValue() {
        return new JsonSettingValue<>(SettingType.INTEGER);
    }

    @Override
    protected SettingValue<String> newStringSettingValue() {
        return new JsonSettingValue<>(SettingType.STRING);
    }

    @Override
    protected SettingValue<String> newChoiceSettingValue() {
        return new JsonSettingValue<>(SettingType.CHOICE);
    }

    @Override
    protected SettingValue<File> newFileSettingValue() {
        return new JsonSettingValue<>(SettingType.FILE);
    }

    @Override
    protected SettingValue<URI> newUriSettingValue() {
        return new JsonSettingValue<>(SettingType.URI);
    }

    @Override
    protected SettingValue<Double> newNumericSettingValue() {
        return new JsonSettingValue<>(SettingType.NUMERIC);
    }

    @Override
    protected SettingValue<DateTime> newDateTimeSettingValue() {
        return new JsonSettingValue<>(SettingType.TIMEINSTANT);
    }

    @Override
    protected SettingValue<MultilingualString> newMultiLingualStringSettingValue() {
        return new JsonSettingValue<>(SettingType.MULTILINGUAL_STRING);
    }
}
