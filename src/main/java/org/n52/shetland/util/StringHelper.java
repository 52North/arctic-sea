/*
 * Copyright 2016-2018 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.util;

import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;

import com.google.common.base.Strings;
import com.google.common.io.CharStreams;

/**
 * Helper class for String objects. Contains methods to join Strings, convert
 * streams to Strings or to check for null and emptiness.
 *
 * @since 1.0.0
 *
 */
public final class StringHelper {
    private StringHelper() {
    }

    /**
     * @param toNormalize
     *                    the string to normalize
     *
     * @return a normalized String for use in a file path, i.e. all
     *         [\,/,:,*,?,",<,>,;] characters are replaced by '_'.
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

    /**
     * Check if string is not null and not empty
     *
     * @param string
     *               string to check
     *
     * @return empty or not
     *
     * @deprecated use {@link Strings#isNullOrEmpty(java.lang.String) }
     */
    @Deprecated
    public static boolean isNotEmpty(String string) {
        return !Strings.isNullOrEmpty(string);
    }

    public static String convertStreamToString(InputStream is, String charset) throws IOException {
        try (InputStreamReader reader = new InputStreamReader(is, Strings.emptyToNull(charset))) {
            return CharStreams.toString(reader);
        } catch (IOException ex) {
            throw new IOException("Error while reading content of HTTP request", ex);
        }
    }

    public static String convertStreamToString(InputStream is) throws IOException {
        return convertStreamToString(is, null);
    }

    @Deprecated
    public static boolean checkIfCharacterOccursXTimesIgnoreCase(String toCheck, char character, int count) {
        return getCharacterCountIgnoreCase(toCheck, character) >= count;
    }

    @Deprecated
    public static boolean checkIfCharacterOccursXTimes(String toCheck, char character, int count) {
        return getCharacterCount(toCheck, character) >= count;
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
