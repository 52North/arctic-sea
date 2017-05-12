/*
 * Copyright 2016-2017 52°North Initiative for Geospatial Open Source
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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Utility functions for {@link Function}.
 *
 * @author Christian Autermann
 */
public final class Functions {
    private Functions() {
    }

    /**
     * Creates a {@link Predicate} that checks if it's input is an instance of {@code clazz}.
     *
     * @param <T>   The input type.
     * @param <U>   The type of which the input has to be an instance of.
     * @param clazz The class of which the input has to be an instance of.
     *
     * @return The predicate.
     *
     * @deprecated use {@link Predicates#instanceOf(java.lang.Class)}
     */
    @Deprecated
    public static <T, U extends T> Predicate<T> instanceOf(@Nonnull Class<? extends U> clazz) {
        return Predicates.instanceOf(clazz);
    }

    /**
     * Casts it's input argument to any type.
     *
     * @param <T> The input type.
     * @param <U> The output type.
     * @param t   The object to be casted.
     *
     * @return The casted object.
     *
     * @throws ClassCastException if the object can not be casted.
     */
    @SuppressWarnings("unchecked")
    public static <T, U extends T> U cast(T t) throws ClassCastException {
        return (U) t;
    }

    /**
     * Returns a {@link Function} that casts it's input argument to any type.
     *
     * @param <T>   The input type.
     * @param <U>   The output type.
     * @param clazz The class the input should be casted to.
     *
     * @return The casting function.
     *
     * @see #cast(java.lang.Object)
     *
     */
    public static <T, U extends T> Function<T, U> cast(@Nonnull Class<? extends U> clazz) {
        Objects.requireNonNull(clazz);
        return Functions::cast;
    }

    /**
     * Returns a {@link Function} that casts it'S input argument to the specified type if it's the right type. The
     * returned {@link Optional} either contains the casted input arguemnt or is empty.
     *
     * @param <T>   The input type.
     * @param <U>   The output type.
     * @param clazz The class to
     *
     * @return An {@link Optional} containing the input
     */
    public static <T, U extends T> Function<T, Optional<U>> castIfInstanceOf(@Nonnull Class<? extends U> clazz) {
        Predicate<Object> filter = Predicates.instanceOf(clazz);
        Function<Object, U> mapper = cast(clazz);
        return t -> Optional.ofNullable(t).filter(filter).map(mapper);
    }

    /**
     * Create a function for the the supplier, ignoring any supplied input.
     *
     * @param <T>      the type of the input to the function
     * @param <U>      the type of the result to the function
     * @param supplier the supplier
     *
     * @return the function wrapping the supplier
     */
    public static <T, U> Function<T, U> forSupplier(@Nonnull Supplier<U> supplier) {
        Objects.requireNonNull(supplier);
        return t -> supplier.get();
    }

    /**
     * Creates a function returning the same result for each invocation.
     *
     * @param <T> the type of the input to the function
     * @param <U> the type of the result to the function
     * @param u   the constant result
     *
     * @return the function
     */
    public static <T, U> Function<T, U> constant(@Nullable U u) {
        return t -> u;
    }

    /**
     * Curries the parameter of the {@code Function} and creates a {@code Consumer}.
     *
     * @param <T>      the parameter type
     * @param <R>      the return type
     * @param function the function
     * @param t        the curried parameter
     *
     * @return the curried function
     */
    public static <T, R> Supplier<R> curry(@Nonnull Function<? super T, ? extends R> function, T t) {
        Objects.requireNonNull(function);
        return () -> function.apply(t);
    }

    /**
     * Curries the first parameter of the {@code BiFunction} and creates a {@code Function}.
     *
     * @param <T1>       the first parameter type
     * @param <T2>       the second parameter type
     * @param <R>        the return type
     * @param bifunction the function
     * @param t1         the curried parameter
     *
     * @return the curried function
     */
    public static <T1, T2, R> Function<T2, R> curryFirst(@Nonnull BiFunction<T1, T2, R> bifunction, T1 t1) {
        Objects.requireNonNull(bifunction);
        return t2 -> bifunction.apply(t1, t2);
    }

    /**
     * Curries the second parameter of the {@code BiFunction} and creates a {@code Function}.
     *
     * @param <T1>       the first parameter type
     * @param <T2>       the second parameter type
     * @param <R>        the return type
     * @param bifunction the function
     * @param t2         the curried parameter
     *
     * @return the curried function
     */
    public static <T1, T2, R> Function<T1, R> currySecond(@Nonnull BiFunction<T1, T2, R> bifunction, T2 t2) {
        Objects.requireNonNull(bifunction);
        return t1 -> bifunction.apply(t1, t2);
    }

    /**
     * Reverses the parameter order of the BiConsumer
     *
     * @param <A>      the first parameter type
     * @param <B>      the second parameter type
     * @param consumer the consumer
     *
     * @return the consumer with switched parameters
     *
     * @deprecated use {@link Consumers#reverse(java.util.function.BiConsumer)}
     */
    @Deprecated
    public static <A, B> BiConsumer<B, A> reverse(@Nonnull BiConsumer<A, B> consumer) {
        return Consumers.reverse(consumer);
    }

    public static <T> BinaryOperator<T> mergeLeft(@Nonnull BiConsumer<T, T> merger) {
        Objects.requireNonNull(merger);
        return (a, b) -> {
            merger.accept(a, b);
            return a;
        };
    }

    public static <T> BinaryOperator<T> mergeRight(@Nonnull BiConsumer<T, T> merger) {
        Objects.requireNonNull(merger);
        return (a, b) -> {
            merger.accept(a, b);
            return b;
        };
    }

    public static <K, V> BinaryOperator<Map<K, V>> mergeToLeftMap(@Nonnull BinaryOperator<V> valueMerger) {
        return mergeLeft(mapMerger(valueMerger));
    }

    public static <K, V> BinaryOperator<Map<K, V>> mergeToRightMap(@Nonnull BinaryOperator<V> valueMerger) {
        return mergeRight(mapMerger(valueMerger));
    }

    private static <K, V> BiConsumer<Map<K, V>, Map<K, V>> mapMerger(@Nonnull BinaryOperator<V> valueMerger) {
        Objects.requireNonNull(valueMerger);
        BiFunction<? super V, ? super V, ? extends V> m = (a, b) -> valueMerger.apply(a, b);
        return (a, b) -> b.forEach((key, value) -> a.merge(key, value, m));
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

    /**
     * Wrapps a {@link Consumer} into a {@link Function} that returns it's input argument.
     *
     * @param <T>    The consumed element's type.
     * @param action The consumer.
     *
     * @return A function that applies the consumer and returns it's input.
     */
    public static <T> Function<T, T> mutate(@Nonnull Consumer<? super T> action) {
        Objects.requireNonNull(action);
        return (T t) -> {
            action.accept(t);
            return t;
        };
    }

    /**
     * Wraps an {@link ThrowingFunction} into an {@link Function} that throws an {@link Error} if the function throws an
     * {@link Exception}.
     *
     * @param <S> the function's input type.
     * @param <T> the function's output type.
     * @param <X> the type of the the exception that might be thrown.
     * @param fun the function.
     *
     * @return the wrapped function.
     */
    public static <S, T, X extends Exception> Function<S, T> errorWrapper(@Nonnull ThrowingFunction<S, T, X> fun) {
        Objects.requireNonNull(fun);
        return s -> {
            try {
                return fun.apply(s);
            } catch (Exception ex) {
                throw new Error(ex);
            }
        };
    }

}
