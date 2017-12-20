/*
 * Copyright 2015-2017 52°North Initiative for Geospatial Open Source
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

import java.util.Objects;
import java.util.function.Function;

/**
 *
 *
 */
/**
 * Represents a function that accepts two arguments and produces a result. This is the two-arity specialization of
 * {@link Function}.
 *
 * @param <T> the type of the first argument to the function
 * @param <U> the type of the second argument to the function
 * @param <R> the type of the result of the function
 * @param <X> the type of the exception that might be thrown
 *
 * @author Christian Autermann
 */
@FunctionalInterface
public interface ThrowingBiFunction<T, U, R, X extends Exception> {

    /**
     * Applies this function to the given arguments.
     *
     * @param t the first function argument
     * @param u the second function argument
     *
     * @return the function result
     *
     * @throws X if the function invocation fails
     */
    R apply(T t, U u) throws X;

    /**
     * Returns a composed function that first applies this function to its input, and then applies the {@code after}
     * function to the result. If evaluation of either function throws an exception, it is relayed to the caller of the
     * composed function.
     *
     * @param <V>   the type of output of the {@code after} function, and of the composed function
     * @param after the function to apply after this function is applied
     *
     * @return a composed function that first applies this function and then applies the {@code after} function
     *
     * @throws NullPointerException if after is null
     */
    default <V> ThrowingBiFunction<T, U, V, X> andThen(ThrowingFunction<? super R, ? extends V, ? extends X> after) {
        Objects.requireNonNull(after);
        return (T t, U u) -> after.apply(apply(t, u));
    }

    /**
     * Get a {@link ThrowingFunction} by currying the first parameter.
     *
     * @param t the constant parameter
     *
     * @return the function
     */
    default ThrowingFunction<U, R, X> curryFirst(T t) {
        return u -> apply(t, u);
    }

    /**
     * Get a {@link ThrowingFunction} by currying the second parameter.
     *
     * @param u the constant parameter
     *
     * @return the function
     */
    default ThrowingFunction<T, R, X> currySecond(U u) {
        return t -> apply(t, u);
    }
}
