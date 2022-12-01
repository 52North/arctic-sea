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
package org.n52.shetland.ogc.ows;

import static java.util.stream.Collectors.toMap;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Stream;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public final class Enums {

    private static final Map<Class<? extends Enum<?>>, Map<String, Enum<?>>> NAMES = new HashMap<>();

    private Enums() {
    }

    private static Map<String, Enum<?>> getNamesForEnum(Class<? extends Enum<?>> enumClass) {
        Function<Enum<?>, String> getName = e -> e.toString().toLowerCase(Locale.ROOT);
        Function<Enum<?>, Enum<?>> identity = Function.identity();
        Enum<?>[] enums = enumClass.getEnumConstants();
        Stream<Enum<?>> stream = Arrays.stream(enums);
        Collector<Enum<?>, ?, Map<String, Enum<?>>> collector = toMap(getName, identity);
        return stream.collect(collector);
    }

    static <
            E extends Enum<E>> boolean contains(Class<? extends E> enumClass, String string) {
        return fromString(enumClass, string).isPresent();
    }

    @SuppressWarnings(value = "unchecked")
    static <
            E extends Enum<E>> Optional<E> fromString(Class<? extends E> enumClass, String string) {
        return Optional.ofNullable(
                (E) NAMES.computeIfAbsent(enumClass, Enums::getNamesForEnum).get(string.toLowerCase(Locale.ROOT)));
    }

}
