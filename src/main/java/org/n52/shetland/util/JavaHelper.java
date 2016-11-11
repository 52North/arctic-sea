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

import static java.util.stream.Collectors.toSet;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Set;

import org.joda.time.DateTime;


/**
 * Helper class for Java objects.
 *
 * @since 1.0.0
 *
 */
public final class JavaHelper {

    /**
     * Message digest for generating single identifier
     */
    private static final MessageDigest messageDigest;

    /**
     * Instantiation of the message digest
     */
    static {
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
        } catch (final NoSuchAlgorithmException nsae) {
            throw new Error("Error while getting SHA-256 messagedigest!", nsae);
        }
    }

    private JavaHelper() {
    }

    /**
     * Generates a sensor id from description and current time as long.
     *
     * @param message
     *                sensor description
     *
     * @return generated sensor id as hex SHA-256.
     */
    public static String generateID(String message) {
        final long autoGeneratredID = new DateTime().getMillis();
        final String concate = message + Long.toString(autoGeneratredID);
        return bytesToHex(messageDigest.digest(concate.getBytes()));
    }

    /**
     * Transforms byte to hex representation
     *
     * @param bytes
     *          bytes
     *
     * @return hex
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder buffer = new StringBuilder(2 * bytes.length);
        for (int i = 0; i < bytes.length; ++i) {
            buffer.append(Character.forDigit((bytes[i] >> 4) & 0xF, 16));
            buffer.append(Character.forDigit((bytes[i] & 0xF), 16));
        }
        return buffer.toString();
    }

    @Deprecated
    public static void appendTextToStringBuilderWithLineBreak(StringBuilder stringBuilder, String message) {
        if (stringBuilder != null && StringHelper.isNotEmpty(message)) {
            stringBuilder.append(message);
            stringBuilder.append("\n");
        }
    }

    /**
     * return Object value as String
     *
     * @param object
     *               to get as String
     *
     * @return String value
     */
    public static String asString(final Object object) {
        if (object instanceof String) {
            return (String) object;
        } else if (object instanceof BigDecimal) {
            return ((BigDecimal) object).toPlainString();
        } else if (object instanceof Double) {
            return ((Double) object).toString();
        } else if (object instanceof Integer) {
            return ((Integer) object).toString();
        }
        // TODO why not object.toString()?
        return "";
    }

    /**
     * return Object value as Double
     *
     * @param object
     *               to get as Double
     *
     * @return Double value
     */
    public static Double asDouble(final Object object) {
        if (object instanceof String) {
            return Double.parseDouble((String) object);
        } else if (object instanceof Number) {
            return ((Number) object).doubleValue();
        }
        return Double.NaN;
    }

    /**
     * return Object value as Integer
     *
     * @param object
     *               to get as Integer
     *
     * @return Integer value
     */
    public static Integer asInteger(Object object) {
        if (object == null) {
            return null;
        } else if (object instanceof Integer) {
            return (Integer) object;
        } else if (object instanceof String) {
            return Integer.valueOf((String) object);
        }
        return null;
    }

    /**
     * return Object value as Boolean
     *
     * @param object
     *               to get as Boolean
     *
     * @return Boolean value
     */
    public static Boolean asBoolean(Object object) {
        if (object == null) {
            return null;
        } else if (object instanceof Boolean) {
            return (Boolean) object;
        } else if (object instanceof String) {
            return Boolean.valueOf((String) object);
        }
        return null;
    }

    public static Set<Integer> getIntegerSetFromString(String s) {
        return StringHelper.splitToStream(s, ",").map(Integer::parseInt).collect(toSet());
    }
}
