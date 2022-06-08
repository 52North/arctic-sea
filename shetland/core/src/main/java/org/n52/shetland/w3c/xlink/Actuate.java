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
package org.n52.shetland.w3c.xlink;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public enum Actuate {
    ON_LOAD,
    ON_REQUEST,
    OTHER,
    NONE;

    @Override
    @JsonValue
    public String toString() {
        switch (this) {
            case NONE:
                return "none";
            case ON_LOAD:
                return "onLoad";
            case ON_REQUEST:
                return "onRequest";
            case OTHER:
                return "other";
            default:
                throw new AssertionError(name());
        }
    }

    @JsonCreator
    public static Actuate fromString(String str) {
        if (str == null || str.isEmpty()) {
            return null;
        }

        return Arrays.stream(values())
                .filter(actuate -> actuate.name().equalsIgnoreCase(str) || actuate.toString().equalsIgnoreCase(str))
                .findAny().orElse(null);
    }

}
