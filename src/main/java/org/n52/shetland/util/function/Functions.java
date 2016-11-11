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
package org.n52.shetland.util.function;

import static java.util.stream.Collectors.toSet;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class Functions {
    private Functions() {
    }

    public static <X, T> Function<? super X, T> constant(T t) {
        return (x) -> t;
    }

    public static <T, R> Supplier<R> bind(Function<? super T, ? extends R> function, T t) {
        return () -> function.apply(t);
    }

    public static <T1, T2, R> Function<T2, R> bind1(BiFunction<T1, T2, R> bifunction, T1 t1) {
        return (t2) -> bifunction.apply(t1, t2);
    }

    public static <T1, T2, R> Function<T1, R> bind2(BiFunction<T1, T2, R> bifunction, T2 t2) {
        return (t1) -> bifunction.apply(t1, t2);
    }

    public static <A, B> BiConsumer<B, A> reverse(BiConsumer<A, B> consumer) {
        return (a, b) -> consumer.accept(b, a);
    }

    public static <K, V> BinaryOperator<Map<K, V>> mergeToRightMap(BiFunction<? super V, ? super V, ? extends V> valueMerger) {
        BinaryOperator<Map<K, V>> mergeToLeftMap = mergeToLeftMap(valueMerger);
        return (a, b) -> mergeToLeftMap.apply(b, a);
    }

    public static <K, V> BinaryOperator<Map<K, V>> mergeToLeftMap(BiFunction<? super V, ? super V, ? extends V> valueMerger) {
        Objects.requireNonNull(valueMerger);
        return (a, b) -> {
            b.forEach((key, value) -> a.merge(key, value, valueMerger));
            return b;
        };
    }

    public static <K, V> Function<Map<K, V>, Set<K>> keySetWhereValues(Predicate<? super V> predicate) {
        Objects.requireNonNull(predicate);
        return map -> map.entrySet().stream()
                .filter(e -> predicate.test(e.getValue()))
                .map(Entry::getKey).collect(toSet());
    }

    public static <K, V> Function<Map<K, V>, Stream<K>> keyStreamWhereValues(Predicate<? super V> predicate) {
        Objects.requireNonNull(predicate);
        return map -> map.entrySet().stream()
                .filter(e -> predicate.test(e.getValue()))
                .map(Entry::getKey);
    }

}
