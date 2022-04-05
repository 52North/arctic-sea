/*
 * Copyright 2015-${currentYear} 52°North Spatial Information Research GmbH
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
import java.util.stream.Stream;

import org.n52.janmayen.function.Suppliers;
import org.n52.janmayen.stream.Streams;

/**
 * Utility functions for {@link Optional}.
 *
 * @author Christian Autermann
 */
public final class Optionals {

    private Optionals() {
    }

    /**
     * Checks if any of the optionals is present.
     *
     * @param optionals the optionals
     *
     * @return if at least one is present
     */
    public static boolean any(Optional<?>... optionals) {
        return any(Arrays.stream(optionals));
    }

    /**
     * Checks if any of the optionals is present.
     *
     * @param optionals the optionals
     *
     * @return if at least one is present
     */
    public static boolean any(Iterable<Optional<?>> optionals) {
        return any(Streams.stream(optionals));
    }

    /**
     * Checks if any of the optionals is present.
     *
     * @param optionals the optionals
     *
     * @return if at least one is present
     */
    public static boolean any(Stream<Optional<?>> optionals) {
        return optionals.anyMatch(Optional::isPresent);
    }

    /**
     * Creates a comparator wrapping {@code comparator} to sort by the optional's value, {@code null}s last.
     *
     * @param <U>        the optional's type
     * @param comparator the comparator
     *
     * @return the new comparator
     */
    public static <U> Comparator<Optional<U>> nullsLast(Comparator<? super U> comparator) {
        return Comparator.comparing(Optionals::orElseNull, Comparator.nullsLast(comparator));
    }

    /**
     * Creates a comparator to sort by the optional's value, {@code null}s last.
     *
     * @param <U> the optional's type
     *
     * @return the new comparator
     */
    public static <U extends Comparable<? super U>> Comparator<Optional<U>> nullsLast() {
        return nullsLast(Comparator.<U>naturalOrder());
    }

    /**
     * Creates a comparator wrapping {@code comparator} to sort by the optional's value, {@code null}s first.
     *
     * @param <U>        the optional's type
     * @param comparator the comparator
     *
     * @return the new comparator
     */
    public static <U> Comparator<Optional<U>> nullsFirst(Comparator<? super U> comparator) {
        return Comparator.comparing(Optionals::orElseNull, Comparator.nullsFirst(comparator));
    }

    /**
     * Creates a comparator to sort by the optional's value, {@code null}s first.
     *
     * @param <U> the optional's type
     *
     * @return the new comparator
     */
    public static <U extends Comparable<? super U>> Comparator<Optional<U>> nullsFirst() {
        return nullsFirst(Comparator.<U>naturalOrder());
    }

    /**
     * Gets the value of {@code optional} if it is present, {@code null} otherwise.
     *
     * @param <U>      the optional's type
     * @param optional the optional
     *
     * @return the optional's value or {@code null}
     */
    public static <U> U orElseNull(Optional<U> optional) {
        return optional.orElse(null);
    }

    /**
     * Feeds {code optional}'s value to {@code consumer} if it is present or else runs {@code ifNotPresent}.
     *
     * @param <U>          the optional's type
     * @param optional     the optional
     * @param consumer     the consumer
     * @param ifNotPresent the runnable
     *
     * @return the optional
     */
    public static <U> Optional<U> ifPresentOrElse(Optional<U> optional, Consumer<U> consumer, Runnable ifNotPresent) {
        if (optional.isPresent()) {
            consumer.accept(optional.get());
        } else {
            ifNotPresent.run();
        }
        return optional;
    }

    /**
     * Maps the {@code optional} using {@code mapper} if it is present or runs {@code ifNotPresent}.
     *
     * @param <T>          the resulting optional's type
     * @param <U>          the optional's type
     * @param optional     the optinal
     * @param mapper       the mapper
     * @param ifNotPresent the runnable
     *
     * @return the new optional
     */
    public static <U, T> Optional<U> mapOrElse(Optional<T> optional,
                                               Function<? super T, ? extends U> mapper,
                                               Runnable ifNotPresent) {
        if (!optional.isPresent()) {
            ifNotPresent.run();
            return Optional.empty();
        } else {
            return Optional.ofNullable(mapper.apply(optional.get()));
        }
    }

    /**
     * Gets the value of {@code a} if it is present or else the value of {@code b}.
     *
     * @param <U> the optional's type
     * @param a   the first optional
     * @param b   a supplier for the second optional
     *
     * @return the value
     */
    public static <U> Optional<U> or(Optional<U> a, Supplier<Optional<U>> b) {
        return a.isPresent() ? a : b.get();
    }

    /**
     * Gets the value of {@code a} if it is present or else the value of {@code b}.
     *
     * @param <U> the optional's type
     * @param a   the first optional
     * @param b   a the second optional
     *
     * @return the value
     */
    public static <U> Optional<U> or(Optional<U> a, Optional<U> b) {
        return or(a, () -> b);
    }

    /**
     * Gets the first value of the first optional that is present.
     *
     * @param <U>       the optional's type
     * @param optionals the optionals
     *
     * @return the first present value
     */
    @SafeVarargs
    @SuppressWarnings("varargs")
    public static <U> Optional<U> or(Optional<U>... optionals) {
        return or(Arrays.stream(optionals).map(Suppliers::constant));
    }

    /**
     * Gets the first value of the first optional that is present.
     *
     * @param <U>       the optional's type
     * @param optionals the optionals
     *
     * @return the first present value
     */
    @SafeVarargs
    @SuppressWarnings("varargs")
    public static <U> Optional<U> or(Supplier<Optional<U>>... optionals) {
        return or(Arrays.stream(optionals));
    }

    /**
     * Gets the first value of the first optional that is present.
     *
     * @param <U>       the optional's type
     * @param optionals the optionals
     *
     * @return the first present value
     */
    public static <U> Optional<U> or(Stream<Supplier<Optional<U>>> optionals) {
        return optionals.map(Supplier::get).filter(Optional::isPresent).map(Optionals::orElseNull).findFirst();
    }

}
