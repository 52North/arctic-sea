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
package org.n52.janmayen.i18n;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import org.n52.janmayen.function.Predicates;

import com.neovisionaries.i18n.LanguageAlpha3Code;

public final class LocaleHelper {
    private static final Map<String, Locale> CACHE = Collections.synchronizedMap(new HashMap<>());

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
        String country = input.getCountry();
        String language = input.getLanguage();
        StringBuilder sb = new StringBuilder(language);
        if (!country.isEmpty()) {
            sb.append("-").append(country);
        }
        return sb.toString();
    }

    public static Set<Locale> getEquivalents(Locale locale) {
        Set<Locale> locales = new LinkedHashSet<>();
        LanguageAlpha3Code byCode = LanguageAlpha3Code.getByCode(locale.getLanguage());
        if (byCode != null) {
            locales.add(byCode.getAlpha2().toLocale());
            locales.add(new Locale(byCode.getAlpha3B().name()));
            locales.add(new Locale(byCode.getAlpha3T().name()));
        }
        return locales;
    }

    public static Set<String> getEquivalents(String locale) {
        return getEquivalents(decode(locale)).stream().map(l -> encode(l)).collect(Collectors.toSet());
    }

    private static Locale decode1(String locale) {
        String strippedQualifiers = stripQualityFactorWeights(locale);
        String firstLanguage = getFirstLanguageCode(strippedQualifiers);
        String[] tokens = firstLanguage.split("[-_# ]");
        if (tokens.length > 3) {
            throw new IllegalArgumentException("Unparsable language parameter: " + locale);
        }
        String language = tokens.length > 0 ? tokens[0].toLowerCase() : "";
        String country = tokens.length > 1 ? tokens[1].toUpperCase() : "";
        String variant = tokens.length > 2 ? tokens[2] : "";
        return new Locale(language, country, variant);
    }

    /**
     * Strips quality factors weights after semicolon, e.g. {@code de-DE,de;q=0.9}.
     *
     * @param locale the locale potentially containing quality factor weights
     * @return the locale without quality factor weights
     */
    private static String stripQualityFactorWeights(String locale) {
        int semicolonIndex = locale.indexOf(";");
        boolean hasQualityFactorWeights = semicolonIndex != -1;
        return hasQualityFactorWeights
                ? locale.substring(0, semicolonIndex)
                : locale;
    }

    private static String getFirstLanguageCode(String locale) {
        return locale.split(",")[0];
    }
}
