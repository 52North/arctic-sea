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
package org.n52.janmayen;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class Optionals {

    private Optionals() {
    }

    public static boolean any(Optional<?>... optionals) {
        return any(Arrays.asList(optionals));
    }

    public static boolean any(Iterable<Optional<?>> optionals) {
        return Streams.stream(optionals).anyMatch(o -> o.isPresent());
    }

    public static <U> Comparator<Optional<U>> nullsLast(Comparator<? super U> comparator) {
        return Comparator.comparing(Optionals::orElseNull, Comparator.nullsLast(comparator));
    }

    public static <U extends Comparable<? super U>> Comparator<Optional<U>> nullsLast() {
        return nullsLast(Comparator.<U>naturalOrder());
    }

    public static <U> Comparator<Optional<U>> nullsFirst(Comparator<? super U> comparator) {
        return Comparator.comparing(Optionals::orElseNull, Comparator.nullsFirst(comparator));
    }

    public static <U extends Comparable<? super U>> Comparator<Optional<U>> nullsFirst() {
        return nullsFirst(Comparator.<U>naturalOrder());
    }

    public static <U> U orElseNull(Optional<U> optional) {
        return optional.orElse(null);
    }

    public static <U> Optional<U> ifPresentOrElse(Optional<U> optional, Consumer<U> consumer, Runnable ifNotPresent) {
        if (optional.isPresent()) {
            consumer.accept(optional.get());
        } else {
            ifNotPresent.run();
        }
        return optional;
    }

    public static <T, U> Optional<U> mapOrElse(Optional<T> optional, Function<? super T, ? extends U> mapper, Runnable ifNotPresent) {
        if (!optional.isPresent()) {
            ifNotPresent.run();
            return Optional.empty();
        } else {
            return Optional.ofNullable(mapper.apply(optional.get()));
        }
    }

    public static <U> Optional<U> or(Optional<U> a, Supplier<Optional<U>> b) {
        if (a.isPresent()) {
            return a;
        } else {
            return b.get();
        }
    }

    public static <U> Optional<U> or(Optional<U> a, Optional<U> b) {
        return or(a, () -> b);
    }
}
