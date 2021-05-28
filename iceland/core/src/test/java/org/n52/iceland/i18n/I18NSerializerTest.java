/*
 * Copyright (C) 2015-2021 52°North Spatial Information Research GmbH
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
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Locale;

import org.junit.jupiter.api.Test;

import org.n52.janmayen.i18n.MultilingualString;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class I18NSerializerTest {

    @Test
    public void testSingle() {
        test(new MultilingualString().addLocalization(Locale.ENGLISH, "text"));
        test(new MultilingualString()
                .addLocalization(Locale.ENGLISH, "text!;\")=?§"));
    }

    @Test
    public void testMultiple() {
        test(new MultilingualString()
                .addLocalization(Locale.ENGLISH, "text!;\")=?§")
                .addLocalization(Locale.CANADA_FRENCH, "text!;\")=?§")
                .addLocalization(Locale.TRADITIONAL_CHINESE, "text!;\")=?§")
                .addLocalization(Locale.GERMANY, "text!;\")=?§")
                .addLocalization(Locale.KOREAN, "text!;\")=?§"));
    }

    private void test(MultilingualString string) {
        assertThat(string, is(notNullValue()));
        String encoded = new I18NSerializer().encode(string);
        //System.out.println(encoded);
        assertThat(encoded.isEmpty(), is(string.isEmpty()));
        MultilingualString decoded = new I18NSerializer().decode(encoded);
        assertThat(decoded, is(equalTo(string)));
    }

}
