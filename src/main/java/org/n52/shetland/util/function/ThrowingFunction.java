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

import java.util.Objects;
import java.util.function.Function;

/**
 * Represents a function that accepts one argument and produces a result.
 *
 * <p>
 * This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #apply(Object)}.
 *
 * @param <T> the type of the input to the function
 * @param <R> the type of the result of the function
 * @param <X> the type of the exception that might be thrown
 *
 * @author Christian Autermann
 */
@FunctionalInterface
public interface ThrowingFunction<T, R, X extends Throwable> {

    /**
     * Applies this function to the given argument.
     *
     * @param t the function argument
     *
     * @return the function result
     *
     * @throws X if the operation fails
     */
    R apply(T t) throws X;

    /**
     * Returns a composed function that first applies the {@code before}
     * function to its input, and then applies this function to the result.
     * If evaluation of either function throws an exception, it is relayed to
     * the caller of the composed function.
     *
     * @param <V>    the type of input to the {@code before} function, and to the
     *               composed function
     * @param before the function to apply before this function is applied
     *
     * @return a composed function that first applies the {@code before}
     *         function and then applies this function
     *
     * @throws NullPointerException if before is null
     *
     * @see #andThen(Function)
     */
    default <V> ThrowingFunction<V, R, X> compose(Function<? super V, ? extends T> before) {
        Objects.requireNonNull(before);
        return (V v) -> apply(before.apply(v));
    }

    /**
     * Returns a composed function that first applies this function to
     * its input, and then applies the {@code after} function to the result.
     * If evaluation of either function throws an exception, it is relayed to
     * the caller of the composed function.
     *
     * @param <V>   the type of output of the {@code after} function, and of the
     *              composed function
     * @param after the function to apply after this function is applied
     *
     * @return a composed function that first applies this function and then
     *         applies the {@code after} function
     *
     * @throws NullPointerException if after is null
     *
     * @see #compose(Function)
     */
    default <V> ThrowingFunction<T, V, X> andThen(Function<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (T t) -> after.apply(apply(t));
    }

    /**
     * Returns a composed function that first applies the {@code before}
     * function to its input, and then applies this function to the result.
     * If evaluation of either function throws an exception, it is relayed to
     * the caller of the composed function.
     *
     * @param <V>    the type of input to the {@code before} function, and to the
     *               composed function
     * @param before the function to apply before this function is applied
     *
     * @return a composed function that first applies the {@code before}
     *         function and then applies this function
     *
     * @throws NullPointerException if before is null
     *
     * @see #andThen(Function)
     */
    default <V> ThrowingFunction<V, R, X> compose(ThrowingFunction<? super V, ? extends T, ? extends X> before) {
        Objects.requireNonNull(before);
        return (V v) -> apply(before.apply(v));
    }

    /**
     * Returns a composed function that first applies this function to
     * its input, and then applies the {@code after} function to the result.
     * If evaluation of either function throws an exception, it is relayed to
     * the caller of the composed function.
     *
     * @param <V>   the type of output of the {@code after} function, and of the
     *              composed function
     * @param after the function to apply after this function is applied
     *
     * @return a composed function that first applies this function and then
     *         applies the {@code after} function
     *
     * @throws NullPointerException if after is null
     *
     * @see #compose(Function)
     */
    default <V> ThrowingFunction<T, V, X> andThen(ThrowingFunction<? super R, ? extends V, ? extends X> after) {
        Objects.requireNonNull(after);
        return (T t) -> after.apply(apply(t));
    }

}
