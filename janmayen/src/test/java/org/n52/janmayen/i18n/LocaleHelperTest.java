/*
 * Copyright 2015-2020 52°North Initiative for Geospatial Open Source
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
package org.n52.janmayen.i18n;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Locale;

import org.junit.jupiter.api.Test;
/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class LocaleHelperTest {

    @Test
    public void testSerialization() {
        String string = LocaleHelper.encode(Locale.GERMAN);
        assertThat(LocaleHelper.decode(string, null), is(Locale.GERMAN));
    }

    @Test
    public void test() {

        //IETF BCP 47
        // ISO 639 alpha-2 or alpha-3
        String string = LocaleHelper.encode(Locale.GERMAN);
        //System.out.println(Locale.GERMAN.toLanguageTag());
        assertThat(LocaleHelper.decode(string, null), is(Locale.GERMAN));
        assertThat(LocaleHelper.decode("de_DE", null), is(Locale.GERMANY));
        assertThat(LocaleHelper.decode("de_DE", null), is(Locale.GERMANY));
        assertThat(LocaleHelper.decode("de DE", null), is(Locale.GERMANY));
        assertThat(LocaleHelper.decode("de-DE", null), is(Locale.GERMANY));
        assertThat(LocaleHelper.decode("de-de", null), is(Locale.GERMANY));
        assertThat(LocaleHelper.decode("de-DE", null), is(Locale.GERMANY));
        assertThat(LocaleHelper.decode("deu", null), is(new Locale("deu")));
        assertThat(LocaleHelper.decode("ger", null), is(new Locale("ger")));
        assertThat(LocaleHelper.decode("eng", null), is(new Locale("eng")));
    }

    @Test
    public void parseFirstOfMultipleLanguages() {
        String multiLanguageString = "de-DE,en";
        Locale locale = LocaleHelper.decode(multiLanguageString);
        assertThat(locale, is(Locale.GERMANY));
    }

    @Test
    public void parseWithEmptyRelativeQualityFactors() {
        String headerWithQualityFactors = "de-DE;";
        Locale locale = LocaleHelper.decode(headerWithQualityFactors);
        assertThat(locale, is(Locale.GERMANY));
    }

    @Test
    public void parseWithSingleRelativeQualityFactors() {
        String headerWithQualityFactors = "de-DE;q=0.9";
        Locale locale = LocaleHelper.decode(headerWithQualityFactors);
        assertThat(locale, is(Locale.GERMANY));
    }

    @Test
    public void parseWithMultipleRelativeQualityFactors() {
        String headerWithQualityFactors = "de-DE,en;q=0.9,en-GB;q=0.8,en;q=0.7,en-US;q=0.6";
        Locale locale = LocaleHelper.decode(headerWithQualityFactors);
        assertThat(locale, is(Locale.GERMANY));
    }

}
