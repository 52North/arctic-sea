/*
 * Copyright 2016 52°North Initiative for Geospatial Open Source
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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;

import org.n52.shetland.ogc.ows.exception.NoApplicableCodeException;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * Helper class for String objects. Contains methods to join Strings, convert
 * streams to Strings or to check for null and emptiness.
 *
 * @since 1.0.0
 *
 */
public class StringHelper {
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

    public static String convertStreamToString(InputStream is, String charset) throws OwsExceptionReport {
        try {
            Scanner scanner;
            if (isNotEmpty(charset)) {
                scanner = new Scanner(is, charset);
            } else {
                scanner = new Scanner(is);
            }
            scanner.useDelimiter("\\A");
            if (scanner.hasNext()) {
                return scanner.next();
            }
        } catch (NoSuchElementException nsee) {
            throw new NoApplicableCodeException().causedBy(nsee).withMessage(
                    "Error while reading content of HTTP request: %s", nsee.getMessage());
        }
        return "";
    }

    public static String convertStreamToString(InputStream is) throws OwsExceptionReport {
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
        ArrayList<String> stringList = Lists.newArrayList();
        if (StringHelper.isNotEmpty(string)) {
            for (String s : string.split(separator)) {
                if (s != null && !s.trim().isEmpty()) {
                    stringList.add(s.trim());
                }
            }
        }
        return stringList;
    }

    public static Set<String> splitToSet(String stringToSplit, String separator) {
        return Sets.newTreeSet(splitToList(stringToSplit, separator));
    }

    public static Set<String> splitToSet(String stringToSplit) {
        return splitToSet(stringToSplit, ",");
    }

    public static String[] splitToArray(String stringToSplit, String separator) {
        List<String> splitToList = splitToList(stringToSplit, separator);
        return splitToList.toArray(new String[splitToList.size()]);
    }

    public static String[] splitToArray(String stringToSplit) {
        return splitToArray(stringToSplit, ",");
    }

    public static long getCharacterCountIgnoreCase(String s, char character) {
        return getCharacterCount(s.toUpperCase(Locale.ROOT),
                                 Character.toUpperCase(character));
    }

    public static long getCharacterCount(String s, char character) {
        return s.chars().filter(c -> c == character).count();
    }
}
