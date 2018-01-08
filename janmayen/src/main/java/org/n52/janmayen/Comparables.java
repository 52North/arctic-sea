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
package org.n52.janmayen;

import java.util.Comparator;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.xml.namespace.QName;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;

public final class Comparables {
    public static final int LESS = -1;
    public static final int EQUAL = 0;
    public static final int GREATER = 1;

    private static final Comparator<QName> QNAME_COMPARATOR
            = Comparator.nullsLast(Comparator
                    .comparing(QName::getPrefix, Comparator.nullsLast(String::compareTo))
                    .thenComparing(QName::getLocalPart, Comparator.nullsLast(String::compareTo)));

    private Comparables() {
    }

    public static int compare(int x, int y) {
        return Integer.compare(x, y);
    }

    public static int compare(byte x, byte y) {
        return Byte.compare(x, y);
    }

    public static int compare(short x, short y) {
        return Short.compare(x, y);
    }

    public static int compare(char x, char y) {
        return Character.compare(x, y);
    }

    public static int compare(long x, long y) {
        return Long.compare(x, y);
    }

    public static int compare(boolean x, boolean y) {
        return Boolean.compare(x, y);
    }

    public static int compare(float a, float b) {
        return Float.compare(a, b);
    }

    public static int compare(double a, double b) {
        return Double.compare(a, b);
    }

    public static <T extends Comparable<T>> int compare(T a, T b) {
        return (a == b) ? EQUAL : a == null ? LESS : b == null ? GREATER : a.compareTo(b);
    }

    public static <T> Comparator<T> allowNull(Comparator<T> delegate) {
        return Comparator.nullsFirst(delegate);
    }

    @Deprecated
    public static ComparisonChain chain(Object o) {
        Objects.requireNonNull(o);
        return ComparisonChain.start();
    }

    public static <T> Ordering<T> inheritance() {
        return InheritanceComparator.instance();
    }

    public static Ordering<String> version() {
        return VersionComparator.instance();
    }

    public static Comparator<QName> qname() {
        return QNAME_COMPARATOR;
    }

    private static final class VersionComparator extends Ordering<String> {
        private static final VersionComparator INSTANCE = new VersionComparator();
        private static final Pattern DELIMITER = Pattern.compile("[._-]");
        private static final Pattern EOF = Pattern.compile("\\z");

        private VersionComparator() {
        }

        @Override
        public int compare(String a, String b) {
            if (a == null) {
                return b == null ? EQUAL : LESS;
            } else if (b == null) {
                return GREATER;
            }
            Scanner as = new Scanner(a).useDelimiter(DELIMITER);
            Scanner bs = new Scanner(b).useDelimiter(DELIMITER);
            while (as.hasNextInt() && bs.hasNextInt()) {
                int c = Comparables.compare(as.nextInt(), bs.nextInt());
                if (c != EQUAL) {
                    return c;
                }
            }
            if (as.hasNextInt()) {
                return GREATER;
            } else if (bs.hasNextInt()) {
                return LESS;
            } else {
                boolean na = as.useDelimiter(EOF).hasNext();
                boolean nb = bs.useDelimiter(EOF).hasNext();
                if (na && nb) {
                    return as.next().compareTo(bs.next());
                } else if (na) {
                    return LESS;
                } else if (nb) {
                    return GREATER;
                } else {
                    return EQUAL;
                }
            }
        }

        public static VersionComparator instance() {
            return INSTANCE;
        }
    }

    private static final class InheritanceComparator<T> extends Ordering<T> {
        private static final InheritanceComparator<Object> INSTANCE
                = new InheritanceComparator<>();

        private InheritanceComparator() {
        }

        @Override
        public int compare(T a, T b) {
            if (a == null) {
                return b == null ? EQUAL : LESS;
            } else if (b == null) {
                return GREATER;
            }
            Class<?> c1 = a.getClass();
            Class<?> c2 = b.getClass();
            if (c1.isAssignableFrom(c2)) {
                return GREATER;
            } else if (c2.isAssignableFrom(c1)) {
                return LESS;
            } else {
                return EQUAL;
            }
        }

        @SuppressWarnings("unchecked")
        public static <T> InheritanceComparator<T> instance() {
            return (InheritanceComparator<T>) INSTANCE;
        }
    }
}
