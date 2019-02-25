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
package org.n52.janmayen.i18n;

import static java.util.stream.Collectors.toMap;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Nullable;

import org.n52.janmayen.function.Functions;
import org.n52.janmayen.function.Predicates;

public final class LocaleHelper {
    private static final Map<String, Locale> CACHE = Collections.synchronizedMap(new HashMap<>());
    private static final Map<String, String> ISO_COUNTRY_ALPHA_3_TO_ALPHA_2 = Arrays
            .stream(Locale.getISOCountries()).map(Functions.curryFirst(Locale::new, "")).distinct()
            .collect(toMap(Locale::getISO3Country, Locale::getCountry));
    private static final Map<String, String> ISO_LANGUAGE_ALPHA_3_TO_ALPHA_2 = Arrays
            .stream(Locale.getISOLanguages()).map(Locale::new).distinct()
            .collect(toMap(Locale::getISO3Language, Locale::getLanguage));

    private LocaleHelper() {
    }

    public static Locale decode(String locale) {
        if (locale == null) {
            throw new NullPointerException();
        }
        if (locale.trim().isEmpty()) {
            throw new IllegalArgumentException();
        }
        return decode(locale, null);
    }

    public static Locale decode(String locale, @Nullable Locale defaultLocale) {
        return Optional.ofNullable(locale)
                .map(String::trim)
                .filter(Predicates.not(String::isEmpty))
                .map(l -> CACHE.computeIfAbsent(l, LocaleHelper::decode1))
                .orElse(defaultLocale);
    }

    public static String encode(Locale input) {
        if (input == null) {
            return null;
        }
        String country = input.getISO3Country();
        String language = input.getISO3Language();
        StringBuilder sb = new StringBuilder(language);
        if (!country.isEmpty()) {
            sb.append("-").append(country);
        }
        return sb.toString();
    }

    private static Locale decode1(String locale) {
        String[] tokens = locale.split("[-_# ]");
        if (tokens.length > 3) {
            throw new IllegalArgumentException("Unparsable language parameter: " + locale);
        }
        String language = tokens.length > 0 ? checkForIsoB(tokens[0].toLowerCase()) : "";
        String country = tokens.length > 1 ? tokens[1].toUpperCase() : "";
        String variant = tokens.length > 2 ? tokens[2] : "";
        country = ISO_COUNTRY_ALPHA_3_TO_ALPHA_2.getOrDefault(country, country);

        language = ISO_LANGUAGE_ALPHA_3_TO_ALPHA_2.getOrDefault(language, language);
        return new Locale(language, country, variant);
    }

    private static String checkForIsoB(String language) {
        try {
            return ISO6392B.fromValue(language).getIso();
        } catch (IllegalArgumentException e) {
            return language;
        }
    }

    public enum ISO6392B {
        ALB("alb","sqi"),
        ARM("arm","hye"),
        BAS("baq","eus"),
        BUR("bur","mya"),
        CHI("chi","zho"),
        CRO("scr","hrv"),
        CZE("cze","ces"),
        DUT("dut","nld"),
        FRE("fre","fra"),
        GEO("geo","kat"),
        GER("ger","deu"),
        GRE("gre","ell"),
        ICE("ice","isl"),
        MAC("mac","mkd"),
        MAL("may","msa"),
        MAO("mao","mri"),
        PER("per","fas"),
        SER("scc","srp"),
        SLO("slo","slk"),
        TIB("tib","bod"),
        WEL("wel","cym");

        private final String iso;

        private final String isoBib;

        ISO6392B(String isoBib, String iso) {
            this.iso = iso;
            this.isoBib = isoBib;
        }

        public String getIso() {
            return iso;
        }

        public String getIsoBib() {
            return isoBib;
        }

        public static ISO6392B fromValue(String v) {
            for (ISO6392B c : ISO6392B.values()) {
                if (c.getIso().equalsIgnoreCase(v) || c.getIsoBib().equalsIgnoreCase(v)) {
                    return c;
                }
            }
            throw new IllegalArgumentException(v);
        }

        public static ISO6392B fromValue(ISO6392B v) {
            for (ISO6392B c : ISO6392B.values()) {
                if (c.getIso().equalsIgnoreCase(v.getIso()) || c.getIsoBib().equalsIgnoreCase(v.getIsoBib())) {
                    return c;
                }
            }
            throw new IllegalArgumentException(v.getIso() + " or " + v.getIsoBib());
        }

        public static ISO6392B fromValue(Locale v) {
            for (ISO6392B c : ISO6392B.values()) {
                if (c.getIso().equalsIgnoreCase(v.getISO3Country()) || c.getIsoBib().equalsIgnoreCase(v.getISO3Country())) {
                    return c;
                }
            }
            throw new IllegalArgumentException(v.getISO3Country());
        }
    }
}
