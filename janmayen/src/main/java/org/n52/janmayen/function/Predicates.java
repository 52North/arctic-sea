/*
 * Copyright 2016-2018 52°North Initiative for Geospatial Open Source
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
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import javax.annotation.Nonnull;

/**
 * Utility functions for {@link Predicate}.
 *
 * @author Christian Autermann
 */
public final class Predicates {

    private Predicates() {
    }

    /**
     * Negates the predicate.
     *
     * @param <T>       the type of the input to the predicate
     * @param predicate the predicate to negate
     *
     * @return the negated predicate
     */
    public static <T> Predicate<T> not(Predicate<T> predicate) {
        return predicate.negate();
    }

    /**
     * Creates a predicate.
     *
     * @param <T>       the type of the input to the predicate
     * @param predicate the predicate
     *
     * @return the predicate
     */
    public static <T> Predicate<T> of(Predicate<T> predicate) {
        return predicate;
    }

    /**
     * Creates a predicate that is always {@code false}.
     *
     * @param <T> the type of the input to the predicate
     *
     * @return the predicate
     */
    public static <T> Predicate<T> alwaysFalse() {
        return constant(false);
    }

    /**
     * Creates a predicate that is always {@code true}.
     *
     * @param <T> the type of the input to the predicate
     *
     * @return the predicate
     */
    public static <T> Predicate<T> alwaysTrue() {
        return constant(true);
    }

    /**
     * Creates a predicate that returns a constant value.
     *
     * @param <T>   the type of the input to the predicate
     * @param value the constant value
     *
     * @return the predicate
     */
    public static <T> Predicate<T> constant(boolean value) {
        return t -> value;
    }

    /**
     * Creates a {@link Predicate} that checks if it's input is an instance of {@code clazz}.
     *
     * @param <T>   The input type.
     * @param <U>   The type of which the input has to be an instance of.
     * @param clazz The class of which the input has to be an instance of.
     *
     * @return The predicate.
     */
    public static <T, U extends T> Predicate<T> instanceOf(@Nonnull Class<? extends U> clazz) {
        Objects.requireNonNull(clazz);
        return x -> clazz.isAssignableFrom(x.getClass());
    }

    /**
     * Curries the first parameter of the {@code BiPredicate} and creates a {@code Predicate}.
     *
     * @param <T>       the first parameter type
     * @param <U>       the second parameter type
     * @param predicate the predicate
     * @param t         the curried parameter
     *
     * @return the curried predicate
     */
    public static <T, U> Predicate<U> curryFirst(@Nonnull BiPredicate<T, U> predicate, T t) {
        Objects.requireNonNull(predicate);
        return (u) -> predicate.test(t, u);
    }

    /**
     * Curries the second parameter of the {@code BiPredicate} and creates a {@code Predicate}.
     *
     * @param <T>       the first parameter type
     * @param <U>       the second parameter type
     * @param predicate the predicate
     * @param u         the curried parameter
     *
     * @return the curried predicate
     */
    public static <T, U> Predicate<T> currySecond(@Nonnull BiPredicate<T, U> predicate, U u) {
        Objects.requireNonNull(predicate);
        return (t) -> predicate.test(t, u);
    }
}
