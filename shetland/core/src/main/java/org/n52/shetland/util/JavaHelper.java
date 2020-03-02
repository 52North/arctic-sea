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
package org.n52.shetland.util;

import static java.util.stream.Collectors.toSet;

import java.math.BigDecimal;
import java.util.Set;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Helper class for Java objects.
 *
 * @since 1.0.0
 *
 */
@SuppressFBWarnings("NP_BOOLEAN_RETURN_NULL")
public final class JavaHelper {

    private JavaHelper() {
    }

    /**
     * Generates a sensor id from description and current time as long.
     *
     * @param message
     *                sensor description
     *
     * @return generated sensor id as hex SHA-256.
     * @deprecated See {@link IdGenerator#generateID(String)}
     */
    @Deprecated
    public static String generateID(String message) {
        return IdGenerator.generate(message);
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
