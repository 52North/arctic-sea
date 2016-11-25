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
package org.n52.janmayen.function;

import static java.util.stream.Collectors.toSet;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
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

    public static <T, U extends T> Predicate<T> instanceOf(Class<? extends U> clazz) {
        Objects.requireNonNull(clazz);
        return x -> clazz.isAssignableFrom(x.getClass());
    }

    @SuppressWarnings("unchecked")
    public static <T, U extends T> Function<T, U> cast() {
        return (t) -> (U) t;
    }

    public static <T, U extends T> Function<T, U> cast(Class<? extends U> clazz) {
        return cast();
    }

    public static <T, U extends T> Function<T, Optional<U>> castIfInstanceOf(Class<? extends U> clazz) {
        Objects.requireNonNull(clazz);
        return t -> Optional.ofNullable(t).filter(instanceOf(clazz)).map(cast(clazz));
    }

    public static <T, U> Function<T, U> forSupplier(Supplier<U> supplier) {
        Objects.requireNonNull(supplier);
        return t -> supplier.get();
    }

    public static <X, T> Function<? super X, T> constant(T t) {
        return x -> t;
    }

    public static <T, R> Supplier<R> curry(Function<? super T, ? extends R> function, T t) {
        Objects.requireNonNull(function);
        return () -> function.apply(t);
    }

    public static <T1, T2, R> Function<T2, R> curryFirst(BiFunction<T1, T2, R> bifunction, T1 t1) {
        Objects.requireNonNull(bifunction);
        return t2 -> bifunction.apply(t1, t2);
    }

    public static <T1, T2, R> Function<T1, R> currySecond(BiFunction<T1, T2, R> bifunction, T2 t2) {
        Objects.requireNonNull(bifunction);
        return t1 -> bifunction.apply(t1, t2);
    }

    public static <A, B> BiConsumer<B, A> reverse(BiConsumer<A, B> consumer) {
        Objects.requireNonNull(consumer);
        return (a, b) -> consumer.accept(b, a);
    }

    public static <T> BinaryOperator<T> mergeLeft(BiConsumer<T,T> merger) {
        Objects.requireNonNull(merger);
        return (a,b) -> {
            merger.accept(a, b);
            return a;
        };
    }

    public static <T> BinaryOperator<T> mergeRight(BiConsumer<T,T> merger) {
        Objects.requireNonNull(merger);
        return (a,b) -> {
            merger.accept(a, b);
            return b;
        };
    }

    public static <K, V> BinaryOperator<Map<K, V>> mergeToLeftMap(
            BiFunction<? super V, ? super V, ? extends V> valueMerger) {
        Objects.requireNonNull(valueMerger);
        return mergeLeft((a, b) -> b.forEach((key, value) -> a.merge(key, value, valueMerger)));
    }

    public static <K, V> BinaryOperator<Map<K, V>> mergeToRightMap(
            BiFunction<? super V, ? super V, ? extends V> valueMerger) {
        return mergeRight((a, b) -> b.forEach((key, value) -> a.merge(key, value, valueMerger)));
    }

    public static <K, V> Function<Map<K, V>, Set<K>> keySetWhereValues(Predicate<? super V> predicate) {
        return Functions.<K, V>keyStreamWhereValues(predicate).andThen(s -> s.collect(toSet()));
    }

    public static <K, V> Function<Map<K, V>, Stream<K>> keyStreamWhereValues(Predicate<? super V> predicate) {
        Objects.requireNonNull(predicate);
        return map -> map.entrySet().stream()
                .filter(e -> predicate.test(e.getValue()))
                .map(Entry::getKey);
    }

    public static <T> Function<T, T> mutate(Consumer<? super T> action) {
        Objects.requireNonNull(action);
        return (T t) -> {
            action.accept(t);
            return t;
        };
    }

}
