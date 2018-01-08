/*
 * Copyright 2015-2018 52°North Initiative for Geospatial Open Source
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
package org.n52.iceland.i18n;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.util.Locale;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import org.n52.iceland.i18n.I18NSerializer;
import org.n52.janmayen.i18n.MultilingualString;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class I18NSerializerTest {

    @Rule
    public final ErrorCollector errors = new ErrorCollector();
    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Test@Ignore
    public void testSingle() {
        test(new MultilingualString().addLocalization(Locale.ENGLISH, "text"));
        test(new MultilingualString()
                .addLocalization(Locale.ENGLISH, "text!;\")=?§"));
    }

    @Test@Ignore
    public void testMultiple() {
        test(new MultilingualString()
                .addLocalization(Locale.ENGLISH, "text!;\")=?§")
                .addLocalization(Locale.CANADA_FRENCH, "text!;\")=?§")
                .addLocalization(Locale.TRADITIONAL_CHINESE, "text!;\")=?§")
                .addLocalization(Locale.GERMANY, "text!;\")=?§")
                .addLocalization(Locale.KOREAN, "text!;\")=?§"));
    }

    private void test(MultilingualString string) {
        errors.checkThat(string, is(notNullValue()));
        String encoded = new I18NSerializer().encode(string);
        System.out.println(encoded);
        errors.checkThat(encoded.isEmpty(), is(string.isEmpty()));
        MultilingualString decoded = new I18NSerializer().decode(encoded);
        errors.checkThat(decoded, is(equalTo(string)));
    }

}
