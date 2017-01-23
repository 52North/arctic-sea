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
package org.n52.janmayen.stream;

import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Utility functions for {@link Stream}.
 *
 * @author Christian Autermann
 */
public final class Streams {

    private Streams() {
    }

    /**
     * Factory function for creating a {@code Stream} from an {@code Enumeration}.
     *
     * @param <T>         the element type
     * @param enumeration the enumeration
     *
     * @return the stream
     */
    public static <T> Stream<T> stream(Enumeration<T> enumeration) {
        Objects.requireNonNull(enumeration);
        return stream(new Iterator<T>() {
            @Override
            public boolean hasNext() {
                return enumeration.hasMoreElements();
            }

            @Override
            public T next() {
                return enumeration.nextElement();
            }
        });
    }

    /**
     * Factory function for creating a {@code Stream} from an {@code Iterator}.
     *
     * @param <T>      the element type
     * @param iterator the iterator
     *
     * @return the stream
     */
    public static <T> Stream<T> stream(Iterator<T> iterator) {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED), false);
    }

    /**
     * Factory function for creating a {@code Stream} from an {@code Iterable}.
     *
     * @param <T>      the element type
     * @param iterable the iterable
     *
     * @return the stream
     */
    public static <T> Stream<T> stream(Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false);
    }

    /**
     * Factory function for creating a {@code Stream} from an array.
     *
     * @param <T>   the element type
     * @param array the array
     *
     * @return the stream
     */
    public static <T> Stream<T> stream(T[] array) {
        return Arrays.stream(array);
    }

    /**
     * Factory function for creating a {@code Stream} from an array.
     *
     * @param array the array
     *
     * @return the stream
     */
    public static IntStream stream(int[] array) {
        return Arrays.stream(array);
    }

    /**
     * Factory function for creating a {@code Stream} from an array.
     *
     * @param array the array
     *
     * @return the stream
     */
    public static LongStream stream(long[] array) {
        return Arrays.stream(array);
    }

    /**
     * Factory function for creating a {@code Stream} from an array.
     *
     * @param array the array
     *
     * @return the stream
     */
    public static DoubleStream stream(double[] array) {
        return Arrays.stream(array);
    }

    /**
     * Factory function for creating a {@code Stream} from a {@code Collection}.
     *
     * @param <T>        the element type
     * @param collection the collection
     *
     * @return the stream
     */
    public static <T> Stream<T> stream(Collection<T> collection) {
        return collection.stream();
    }

    /**
     * Creates a {@code BinaryOperator} that throws an exception if invoked.
     *
     * @param <T>               the operators input and result type
     * @param exceptionSupplier the exception factory
     *
     * @see Collectors#toMap(java.util.function.Function, java.util.function.Function,
     * java.util.function.BinaryOperator)
     * @see Collectors#toMap(java.util.function.Function, java.util.function.Function,
     * java.util.function.BinaryOperator, java.util.function.Supplier)
     * @see Collectors#toConcurrentMap(java.util.function.Function, java.util.function.Function,
     * java.util.function.BinaryOperator)
     * @see Collectors#toConcurrentMap(java.util.function.Function, java.util.function.Function,
     * java.util.function.BinaryOperator, java.util.function.Supplier)
     * @return the operator
     */
    public static <T> BinaryOperator<T> throwingMerger(Supplier<? extends RuntimeException> exceptionSupplier) {
        Objects.requireNonNull(exceptionSupplier);
        return throwingMerger((a, b) -> exceptionSupplier.get());
    }

    /**
     * Creates a {@code BinaryOperator} that throws an {@link IllegalStateException} if invoked.
     *
     * @param <T> the operators input and result type
     *
     * @see Collectors#toMap(java.util.function.Function, java.util.function.Function,
     * java.util.function.BinaryOperator)
     * @see Collectors#toMap(java.util.function.Function, java.util.function.Function,
     * java.util.function.BinaryOperator, java.util.function.Supplier)
     * @see Collectors#toConcurrentMap(java.util.function.Function, java.util.function.Function,
     * java.util.function.BinaryOperator)
     * @see Collectors#toConcurrentMap(java.util.function.Function, java.util.function.Function,
     * java.util.function.BinaryOperator, java.util.function.Supplier)
     * @return the operator
     */
    public static <T> BinaryOperator<T> throwingMerger() {
        return throwingMerger((a, b) -> new IllegalStateException(String.format("Duplicate key %s", a)));
    }

    /**
     * Creates a {@code BinaryOperator} that throws an exception if invoked.
     *
     * @param <T>               the operators input and result type
     * @param exceptionSupplier the exception factory
     *
     * @see Collectors#toMap(java.util.function.Function, java.util.function.Function,
     * java.util.function.BinaryOperator)
     * @see Collectors#toMap(java.util.function.Function, java.util.function.Function,
     * java.util.function.BinaryOperator, java.util.function.Supplier)
     * @see Collectors#toConcurrentMap(java.util.function.Function, java.util.function.Function,
     * java.util.function.BinaryOperator)
     * @see Collectors#toConcurrentMap(java.util.function.Function, java.util.function.Function,
     * java.util.function.BinaryOperator, java.util.function.Supplier)
     * @return the operator
     */
    public static <T> BinaryOperator<T> throwingMerger(BiFunction<T, T, ? extends RuntimeException> exceptionSupplier) {
        Objects.requireNonNull(exceptionSupplier);
        return (a, b) -> {
            throw exceptionSupplier.apply(a, b);
        };
    }

}
