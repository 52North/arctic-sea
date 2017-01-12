/*
 * Copyright 2016-2017 52°North Initiative for Geospatial Open Source
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

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import javax.annotation.Nullable;

public final class LocaleHelper {
    private static final Map<String, Locale> CACHE = new HashMap<>();

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
        if (locale == null || locale.isEmpty()) {
            return defaultLocale;
        }
        return CACHE.computeIfAbsent(
                locale, l -> {
                    StringTokenizer tokenizer = new StringTokenizer(l, "-_ #");
                    int length = tokenizer.countTokens();
                    String[] tokens = new String[length];
                    for (int i = 0; i < length; ++i) {
                        tokens[i] = tokenizer.nextToken();
                    }
                    String language = length > 0 ? tokens[0] : "";
                    String country = length > 1 ? tokens[1] : "";
                    String variant = length > 2 ? tokens[2] : "";
                    return new Locale(language, country, variant);
                });
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
}
