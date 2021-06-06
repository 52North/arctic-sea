/*
 * Copyright 2015-2021 52°North Initiative for Geospatial Open Source
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
import java.util.function.Predicate;

/**
 * Represents a predicate (boolean-valued function) of two arguments. This is the two-arity specialization of
 * {@link Predicate}.
 *
 * @param <T> the type of the first argument to the predicate
 * @param <U> the type of the second argument the predicate
 * @param <X> the type of the exception that might be thrown
 *
 * @author Christian Autermann
 */
@FunctionalInterface
public interface ThrowingBiPredicate<T, U, X extends Exception> {

    /**
     * Evaluates this predicate on the given arguments.
     *
     * @param t the first input argument
     * @param u the second input argument
     *
     * @return {@code true} if the input arguments match the predicate, otherwise {@code false}
     *
     * @throws X if the operation fails
     */
    boolean test(T t, U u) throws X;

    /**
     * Returns a composed predicate that represents a short-circuiting logical AND of this predicate and another. When
     * evaluating the composed predicate, if this predicate is {@code false}, then the {@code other} predicate is not
     * evaluated.
     *
     * <p>
     * Any exceptions thrown during evaluation of either predicate are relayed to the caller; if evaluation of this
     * predicate throws an exception, the {@code other} predicate will not be evaluated.
     *
     * @param other a predicate that will be logically-ANDed with this predicate
     *
     * @return a composed predicate that represents the short-circuiting logical AND of this predicate and the
     *         {@code other} predicate
     *
     * @throws NullPointerException if other is null
     */
    default ThrowingBiPredicate<T, U, X> and(ThrowingBiPredicate<? super T, ? super U, ? extends X> other) {
        Objects.requireNonNull(other);
        return (T t, U u) -> test(t, u) && other.test(t, u);
    }

    /**
     * Returns a predicate that represents the logical negation of this predicate.
     *
     * @return a predicate that represents the logical negation of this predicate
     */
    default ThrowingBiPredicate<T, U, X> negate() {
        return (T t, U u) -> !test(t, u);
    }

    /**
     * Returns a composed predicate that represents a short-circuiting logical OR of this predicate and another. When
     * evaluating the composed predicate, if this predicate is {@code true}, then the {@code other} predicate is not
     * evaluated.
     *
     * <p>
     * Any exceptions thrown during evaluation of either predicate are relayed to the caller; if evaluation of this
     * predicate throws an exception, the {@code other} predicate will not be evaluated.
     *
     * @param other a predicate that will be logically-ORed with this predicate
     *
     * @return a composed predicate that represents the short-circuiting logical OR of this predicate and the
     *         {@code other} predicate
     *
     * @throws NullPointerException if other is null
     */
    default ThrowingBiPredicate<T, U, X> or(ThrowingBiPredicate<? super T, ? super U, ? extends X> other) {
        Objects.requireNonNull(other);
        return (T t, U u) -> test(t, u) || other.test(t, u);
    }

    /**
     * Create a {@link ThrowingPredicate} by currying the first parameter
     *
     * @param t the constant parameter
     *
     * @return the predicate
     */
    default ThrowingPredicate<U, X> curryFirst(T t) {
        return u -> test(t, u);
    }

    /**
     * Create a {@link ThrowingPredicate} by currying the second parameter
     *
     * @param u the constant parameter
     *
     * @return the predicate
     */
    default ThrowingPredicate<T, X> currySecond(U u) {
        return t -> test(t, u);
    }
}
