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
package org.n52.faroe;

/**
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 * @since 1.0.0
 *
 */
public final class Validation {

    private Validation() {
    }

    public static <T> T notNull(String name, T val) throws ConfigurationError {
        if (val == null) {
            throw new ConfigurationError(String.format("%s can not be null!", name));
        }
        return val;
    }

    public static int greaterZero(String name, int i) throws ConfigurationError {
        if (i <= 0) {
            throw new ConfigurationError(String.format("%s can not be smaller or equal zero (was %d)!", name, i));
        }
        return i;
    }

    public static int greaterEqualZero(String name, int i) throws ConfigurationError {
        if (i < 0) {
            throw new ConfigurationError(String.format("%s can not be smaller than zero (was %d)!", name, i));
        }
        return i;
    }

    public static String notNullOrEmpty(String name, String val) throws ConfigurationError {
        notNull(name, val);
        if (val.isEmpty()) {
            throw new ConfigurationError(String.format("%s can not be empty!", name));
        }
        return val;
    }
}
