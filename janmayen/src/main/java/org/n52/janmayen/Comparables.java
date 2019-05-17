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
package org.n52.janmayen;

import java.util.Comparator;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.xml.namespace.QName;

import com.google.common.collect.Ordering;

public final class Comparables {
    public static final int LESS = -1;
    public static final int EQUAL = 0;
    public static final int GREATER = 1;

    private static final Comparator<QName> QNAME_COMPARATOR
            = Comparator.nullsLast(Comparator
                    .comparing(QName::getPrefix, Comparator.nullsLast(String::compareTo))
                    .thenComparing(QName::getLocalPart, Comparator.nullsLast(String::compareTo)));

    /**
     * Private utility class constructor.
     */
    private Comparables() {
    }

    /**
     * Compares two {@code int} values numerically.
     *
     * @param x The first {@code int} to compare
     * @param y The second {@code int} to compare
     *
     * @return the value {@code 0} if {@code x == y}; a value less than {@code 0} if {@code x < y}; and a value greater
     *         than {@code 0} if {@code x > y}
     *
     * @see Integer#compare(int, int)
     */
    public static int compare(int x, int y) {
        return Integer.compare(x, y);
    }

    /**
     * Compares two {@code byte} values numerically.
     *
     * @param x The first {@code byte} to compare
     * @param y The second {@code byte} to compare
     *
     * @return the value {@code 0} if {@code x == y}; a value less than {@code 0} if {@code x < y}; and a value greater
     *         than {@code 0} if {@code x > y}
     *
     * @see Byte#compare(byte, byte)
     */
    public static int compare(byte x, byte y) {
        return Byte.compare(x, y);
    }

    /**
     * Compares two {@code short} values numerically.
     *
     * @param x The first {@code short} to compare
     * @param y The second {@code short} to compare
     *
     * @return the value {@code 0} if {@code x == y}; a value less than {@code 0} if {@code x < y}; and a value greater
     *         than {@code 0} if {@code x > y}
     *
     * @see Short#compare(short, short)
     */
    public static int compare(short x, short y) {
        return Short.compare(x, y);
    }

    /**
     * Compares two {@code char} values numerically.
     *
     * @param x The first {@code char} to compare
     * @param y The second {@code char} to compare
     *
     * @return the value {@code 0} if {@code x == y}; a value less than {@code 0} if {@code x < y}; and a value greater
     *         than {@code 0} if {@code x > y}
     *
     * @see Character#compare(char, char)
     */
    public static int compare(char x, char y) {
        return Character.compare(x, y);
    }

    /**
     * Compares two {@code long} values numerically.
     *
     * @param x The first {@code long} to compare
     * @param y The second {@code long} to compare
     *
     * @return the value {@code 0} if {@code x == y}; a value less than {@code 0} if {@code x < y}; and a value greater
     *         than {@code 0} if {@code x > y}
     *
     * @see Long#compare(long, long)
     */
    public static int compare(long x, long y) {
        return Long.compare(x, y);
    }

    /**
     * Compares two {@code boolean} values.
     *
     * @param x The first {@code boolean} to compare
     * @param y The second {@code boolean} to compare
     *
     * @return the value {@code 0} if {@code x == y}; a value less than {@code 0} if {@code !x && y}; and a value
     *         greater than {@code 0} if {@code x && !y}
     *
     * @see Boolean#compare(boolean, boolean)
     */
    public static int compare(boolean x, boolean y) {
        return Boolean.compare(x, y);
    }

    /**
     * Compares two {@code float} values numerically.
     *
     * @param x The first {@code float} to compare
     * @param y The second {@code float} to compare
     *
     * @return the value {@code 0} if {@code x == y}; a value less than {@code 0} if {@code x < y}; and a value greater
     *         than {@code 0} if {@code x > y}
     *
     * @see Float#compare(float, float)
     */
    public static int compare(float x, float y) {
        return Float.compare(x, y);
    }

    /**
     * Compares two {@code double} values numerically.
     *
     * @param x The first {@code double} to compare
     * @param y The second {@code double} to compare
     *
     * @return the value {@code 0} if {@code x == y}; a value less than {@code 0} if {@code x < y}; and a value greater
     *         than {@code 0} if {@code x > y}
     *
     * @see Double#compare(double, double)
     */
    public static int compare(double x, double y) {
        return Double.compare(x, y);
    }

    /**
     * Compares two values {@code x} and {@code y}. {@code null} values are considered less than all other values.
     *
     * @param <T> the comparable type
     * @param x   The first value to compare
     * @param y   The second value to compare
     *
     * @return the value {@code 0} if {@code x == y}; a value less than {@code 0} if {@code x < y}; and a value greater
     *         than {@code 0} if {@code x > y}
     *
     * @see Double#compare(double, double)
     */
    public static <T extends Comparable<T>> int compare(T x, T y) {
        return (x == y) ? EQUAL : x == null ? LESS : y == null ? GREATER : x.compareTo(y);
    }

    /**
     * Compares two {@code byte} values numerically, but handle the {@code byte} value as an unsigned byte.
     *
     * @param x The first {@code byte} to compare
     * @param y The second {@code byte} to compare
     *
     * @return the value {@code 0} if {@code x == y}; a value less than {@code 0} if {@code x < y}; and a value greater
     *         than {@code 0} if {@code x > y}
     *
     * @see Byte#compare(byte, byte)
     */
    public static int compareUnsignedByte(byte x, byte y) {
        return (x & 0xff) - (y & 0xff);
    }

    public static <T> Comparator<T> allowNull(Comparator<T> delegate) {
        return Comparator.nullsFirst(delegate);
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
