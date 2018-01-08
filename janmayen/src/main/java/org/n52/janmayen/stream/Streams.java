/*
 * Copyright 2015-2018 52°North Initiative for Geospatial Open Source
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
     * Returns a sequential reverse-ordered {@code IntStream} from {@code from} (inclusive) to {@code to} (inclusive) by
     * an incremental step of {@code 1}.
     *
     * @param from the inclusive lower bound
     * @param to   the inclusive upper bound
     *
     * @return the reverse range
     *
     * @see IntStream#range(int, int)
     */
    public static IntStream reverseRangeClosed(int from, int to) {
        return IntStream.rangeClosed(from, to).map(i -> to - i + from - 1);
    }

    /**
     * Returns a sequential reverse-ordered {@code IntStream} from {@code from} (inclusive) to {@code to} (exclusive) by
     * an incremental step of {@code 1}.
     *
     * @param from the inclusive lower bound
     * @param to   the exclusive upper bound
     *
     * @return the reverse range
     *
     * @see IntStream#range(int, int)
     */
    public static IntStream reverseRange(int from, int to) {
        return IntStream.range(from, to).map(i -> to - i + from - 1);
    }

    /**
     * Returns a reversed stream of the array values. Note that the index boundaries are given using the original order.
     *
     * @param <T>            the element type of the stream
     * @param array          the array
     * @param startInclusive the lowest index (i.e. the last element to return)
     * @param endInclusive   index immediately past the highest index (i.e. the index after the first returned value
     *
     * @return the reversed stream
     */
    public static <T> Stream<T> reverseStream(T[] array, int startInclusive, int endInclusive) {
        return reverseRange(startInclusive, endInclusive).mapToObj(i -> array[i]);
    }

    /**
     * Returns a reversed stream of the array values. Note that the index boundaries are given using the original order.
     *
     * @param array          the array
     * @param startInclusive the lowest index (i.e. the last element to return)
     * @param endInclusive   index immediately past the highest index (i.e. the index after the first returned value
     *
     * @return the reversed stream
     */
    public static LongStream reverseStream(long[] array, int startInclusive, int endInclusive) {
        return reverseRange(startInclusive, endInclusive).mapToLong(i -> array[i]);
    }

    /**
     * Returns a reversed stream of the array values. Note that the index boundaries are given using the original order.
     *
     * @param array          the array
     * @param startInclusive the lowest index (i.e. the last element to return)
     * @param endInclusive   index immediately past the highest index (i.e. the index after the first returned value
     *
     * @return the reversed stream
     */
    public static IntStream reverseStream(int[] array, int startInclusive, int endInclusive) {
        return reverseRange(startInclusive, endInclusive).map(i -> array[i]);
    }

    /**
     * Returns a reversed stream of the array values. Note that the index boundaries are given using the original order.
     *
     * @param array          the array
     * @param startInclusive the lowest index (i.e. the last element to return)
     * @param endInclusive   index immediately past the highest index (i.e. the index after the first returned value
     *
     * @return the reversed stream
     */
    public static DoubleStream reverseStream(double[] array, int startInclusive, int endInclusive) {
        return reverseRange(startInclusive, endInclusive).mapToDouble(i -> array[i]);
    }

    /**
     * Returns a reversed stream of the array values.
     *
     * @param <T>   the element type of the stream
     * @param array the array
     *
     * @return the reversed stream
     */
    public static <T> Stream<T> reverseStream(T[] array) {
        return reverseStream(array, 0, array.length);
    }

    /**
     * Returns a reversed stream of the array values.
     *
     * @param array the array
     *
     * @return the reversed stream
     */
    public static LongStream reverseStream(long[] array) {
        return reverseStream(array, 0, array.length);
    }

    /**
     * Returns a reversed stream of the array values.
     *
     * @param array the array
     *
     * @return the reversed stream
     */
    public static IntStream reverseStream(int[] array) {
        return reverseStream(array, 0, array.length);
    }

    /**
     * Returns a reversed stream of the array values.
     *
     * @param array the array
     *
     * @return the reversed stream
     */
    public static DoubleStream reverseStream(double[] array) {
        return reverseStream(array, 0, array.length);
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
