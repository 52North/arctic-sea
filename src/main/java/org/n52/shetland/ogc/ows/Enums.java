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
package org.n52.shetland.ogc.ows;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class Enums {

    private Enums() {
    }

    private static final Map<Class<? extends Enum<?>>, Map<String, ? extends Enum<?>>> NAMES = new HashMap<>();

    private static Map<String, ? extends Enum<?>> getNamesForEnum(Class<? extends Enum<?>> enumClass) {
        return Arrays.stream(enumClass.getEnumConstants()).collect(Collectors.toMap(e -> e.toString()
                .toLowerCase(Locale.ROOT), Function.identity()));
    }

    static <E extends Enum<E>> boolean contains(Class<? extends E> enumClass, String string) {
        return fromString(enumClass, string).isPresent();
    }

    @SuppressWarnings(value = "unchecked")
    static <E extends Enum<E>> Optional<E> fromString(Class<? extends E> enumClass, String string) {
        return Optional.ofNullable((E) NAMES.computeIfAbsent(enumClass, Enums::getNamesForEnum).get(string
                .toLowerCase(Locale.ROOT)));
    }

}
