/*
 * Copyright 2017-2018 52°North Initiative for Geospatial Open Source
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

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.util.Locale;
import java.util.Optional;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import org.n52.janmayen.i18n.LocalizedString;
import org.n52.janmayen.i18n.MultilingualString;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class SettingValueFactoryTest {

    @Rule
    public final ErrorCollector errors = new ErrorCollector();

    @Test
    public void testMultilingualStringParsing() {
        String s = "{\"de\":\"Hallo\",\"en\":\"Hello\"}";

        MultilingualString ms = SettingValueFactory.decodeMultiLingualStringValue(s);

        errors.checkThat(ms, is(notNullValue()));
        errors.checkThat(ms.getLocalization(Locale.ENGLISH), is(Optional.of(new LocalizedString(Locale.ENGLISH, "Hello"))));
        errors.checkThat(ms.getLocalization(Locale.GERMAN), is(Optional.of(new LocalizedString(Locale.GERMAN, "Hallo"))));

    }
}
