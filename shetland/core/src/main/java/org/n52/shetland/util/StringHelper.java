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
package org.n52.shetland.util;

import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import com.google.common.base.Strings;
import com.google.common.io.CharStreams;

/**
 * Helper class for String objects. Contains methods to join Strings, convert streams to Strings or to check for null
 * and emptiness.
 *
 * @since 1.0.0
 *
 */
public final class StringHelper {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    private StringHelper() {
    }

    /**
     * @param toNormalize the string to normalize
     *
     * @return a normalized String for use in a file path, i.e. all [\,/,:,*,?,",&lt;,&gt;,;] characters are replaced by
     *         '_'.
     */
    public static String normalize(String toNormalize) {
        // toNormalize = toNormalize.replaceAll("ä", "ae");
        // toNormalize = toNormalize.replaceAll("ö", "oe");
        // toNormalize = toNormalize.replaceAll("ü", "ue");
        // toNormalize = toNormalize.replaceAll("Ä", "AE");
        // toNormalize = toNormalize.replaceAll("Ö", "OE");
        // toNormalize = toNormalize.replaceAll("Ü", "UE");
        // toNormalize = toNormalize.replaceAll("ß", "ss");
        return toNormalize.replaceAll("[\\\\/:\\*?\"<>;,#%=@]", "_");
    }

    public static String convertStreamToString(InputStream is, @Nullable String charset) throws IOException {
        String cs = Optional.ofNullable(charset).map(Strings::emptyToNull).orElseGet(DEFAULT_CHARSET::name);
        try (InputStreamReader reader = new InputStreamReader(is, cs)) {
            return CharStreams.toString(reader);
        } catch (IOException ex) {
            throw new IOException("Error while reading content of HTTP request", ex);
        }
    }

    public static String convertStreamToString(InputStream is) throws IOException {
        return convertStreamToString(is, null);
    }

    public static List<String> splitToList(String string) {
        return splitToList(string, ",");
    }

    public static List<String> splitToList(String string, String separator) {
        return splitToStream(string, separator).collect(toList());
    }

    public static Set<String> splitToSet(String string, String separator) {
        return splitToStream(string, separator).collect(toCollection(TreeSet::new));
    }

    public static Set<String> splitToSet(String stringToSplit) {
        return splitToSet(stringToSplit, ",");
    }

    public static String[] splitToArray(String stringToSplit, String separator) {
        return splitToStream(stringToSplit, separator).toArray(length -> new String[length]);
    }

    public static String[] splitToArray(String stringToSplit) {
        return splitToArray(stringToSplit, ",");
    }

    public static int getCharacterCountIgnoreCase(String s, char character) {
        return getCharacterCount(s.toUpperCase(Locale.ROOT), Character.toUpperCase(character));
    }

    public static int getCharacterCount(String s, char character) {
        return (int) s.chars().filter(c -> c == character).count();
    }

    public static Stream<String> splitToStream(String string, String separator) {
        return Optional.ofNullable(string)
                .map(s -> s.split(separator))
                .map(Arrays::stream)
                .orElseGet(Stream::empty)
                .map(String::trim)
                .map(Strings::emptyToNull)
                .filter(Objects::nonNull);
    }
}
