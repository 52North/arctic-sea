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
package org.n52.janmayen;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility functions for {@link Iterator}.
 *
 * @author Christian Autermann
 */
public final class MoreIterators {

    private MoreIterators() {
    }

    /**
     * Create a {@code Iterator} that is splitting the input using {@code regex}.
     *
     * @param regex the regex
     * @param input the input string
     *
     * @return the iterator
     */
    public static Iterator<String> split(String regex, CharSequence input) {
        return split(Pattern.compile(regex), input);
    }

    /**
     * Create a {@code Iterator} that is splitting the input using {@code pattern}.
     *
     * @param pattern the pattern
     * @param input   the input string
     *
     * @return the iterator
     *
     * @see Pattern#splitAsStream(java.lang.CharSequence)
     */
    public static Iterator<String> split(Pattern pattern, CharSequence input) {
        // copyied form Pattern.splitAsStream()
        return new Iterator<String>() {
            private final Matcher matcher = pattern.matcher(input);
            private int current;
            private String nextElement;
            private int emptyElementCount;

            @Override
            public String next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                if (emptyElementCount == 0) {
                    String n = nextElement;
                    nextElement = null;
                    return n;
                } else {
                    emptyElementCount--;
                    return "";
                }
            }

            @Override
            public boolean hasNext() {
                if (nextElement != null || emptyElementCount > 0) {
                    return true;
                }
                if (current == input.length()) {
                    return false;
                }
                while (matcher.find()) {
                    nextElement = input.subSequence(current, matcher.start()).toString();
                    current = matcher.end();
                    if (!nextElement.isEmpty()) {
                        return true;
                    } else if (current > 0) {
                        emptyElementCount++;
                    }
                }

                nextElement = input.subSequence(current, input.length()).toString();
                current = input.length();
                if (!nextElement.isEmpty()) {
                    return true;
                } else {
                    emptyElementCount = 0;
                    nextElement = null;
                    return false;
                }
            }
        };
    }

}
