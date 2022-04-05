/*
 * Copyright 2015-2022 52°North Spatial Information Research GmbH
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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public enum Show {
    NEW,
    REPLACE,
    EMBED,
    OTHER,
    NONE;

    @Override
    @JsonValue
    public String toString() {
        return this.name().toLowerCase();
    }

    @JsonCreator
    public static Show fromString(String str) {
        if (str == null || str.isEmpty()) {
            return null;
        }

        return Arrays.stream(values())
                .filter(show -> show.name().equalsIgnoreCase(str))
                .findAny().orElse(null);
    }

}
