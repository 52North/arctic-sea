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
package org.n52.janmayen;

import static java.util.stream.Collectors.toMap;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collector.Characteristics;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.n52.janmayen.exception.CompositeException;
import org.n52.janmayen.function.ThrowingConsumer;

public class Streams {

    private Streams() {
    }

    public static <T> Stream<? extends T> stream(Enumeration<? extends T> enumeration) {
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

    public static <T> Stream<? extends T> stream(Iterator<? extends T> iterator) {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED), false);
    }

    public static <T> Stream<? extends T> stream(Iterable<? extends T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false);
    }

    public static <T> Stream<? extends T> parallelStream(Iterable<? extends T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), true);
    }

    public static <T> BinaryOperator<T> throwingMerger(Supplier<? extends RuntimeException> exceptionSupplier) {
        Objects.requireNonNull(exceptionSupplier);
        return throwingMerger((a, b) -> exceptionSupplier.get());
    }

    public static <T> BinaryOperator<T> throwingMerger(BiFunction<T, T, ? extends RuntimeException> exceptionSupplier) {
        Objects.requireNonNull(exceptionSupplier);
        return (a, b) -> {
            throw exceptionSupplier.apply(a, b);
        };
    }

    public static <T> BinaryOperator<T> throwingMerger() {
        return throwingMerger((a, b) -> new IllegalStateException(String.format("Duplicate key %s", a)));
    }

    public static <K, V> Collector<Entry<K, V>, ?, Map<V, K>> entryToMap() {
        return Collectors.toMap(Entry::getValue, Entry::getKey);
    }

    public static <K, V> Collector<Entry<K, V>, ?, LinkedHashMap<K, V>> toLinkedHashMap() {
        return toMap(Entry::getKey, Entry::getValue, Streams.throwingMerger(), LinkedHashMap::new);
    }

    public static <T, A, R> Collector<T, ?, R> collector(Supplier<A> supplier,
                                                         BiConsumer<A, T> accumulator,
                                                         BinaryOperator<A> combiner,
                                                         Function<A, R> finisher) {
        return new CollectorImpl<>(supplier,
                                   accumulator,
                                   combiner,
                                   finisher,
                                   EnumSet.noneOf(Characteristics.class));

    }

    public static <T, R> Collector<T, ?, R> collector(Supplier<R> supplier,
                                                      BiConsumer<R, T> accumulator,
                                                      BiConsumer<R, R> combiner) {
        return new CollectorImpl<>(supplier,
                                   accumulator,
                                   asCombiner(combiner),
                                   Function.identity(),
                                   EnumSet.of(Characteristics.IDENTITY_FINISH));
    }

    public static <T, R> Collector<T, ?, R> collector(Supplier<R> supplier,
                                                      BiConsumer<R, T> accumulator,
                                                      BinaryOperator<R> combiner) {
        return new CollectorImpl<>(supplier,
                                   accumulator,
                                   combiner,
                                   Function.identity(),
                                   EnumSet.of(Characteristics.IDENTITY_FINISH));
    }

    public static <T, A, R> Collector<T, ?, R> collector(Supplier<A> supplier,
                                                         BiConsumer<A, T> accumulator,
                                                         BiConsumer<A, A> combiner,
                                                         Function<A, R> finisher) {
        return new CollectorImpl<>(supplier,
                                   accumulator,
                                   asCombiner(combiner),
                                   finisher,
                                   EnumSet.noneOf(Characteristics.class));
    }

    public static <T, E extends Exception> Collector<T, ?, CompositeException> toCompositeException(
            ThrowingConsumer<? super T, E> fun) {
        return toCompositeException(CompositeException::new, fun);
    }

    public static <T, E extends Exception, X extends CompositeException> Collector<T, ?, X> toCompositeException(
            Supplier<X> supplier, ThrowingConsumer<? super T, E> fun) {
        BiConsumer<X, T> accumulator
                = (composite, t) -> {
                    try {
                        fun.accept(t);
                    } catch (Exception e) {
                        composite.add(e);
                    }
                };
        BiConsumer<X, X> combiner = CompositeException::add;
        return collector(supplier, accumulator, combiner);
    }

    private static <R> BinaryOperator<R> asCombiner(BiConsumer<R, R> combiner) {
        return (a, b) -> {
            combiner.accept(a, b);
            return a;
        };
    }

    private static class CollectorImpl<T, A, R> implements Collector<T, A, R> {
        private final Supplier<A> supplier;
        private final BiConsumer<A, T> accumulator;
        private final BinaryOperator<A> combiner;
        private final Function<A, R> finisher;
        private final Set<Characteristics> characteristics;

        CollectorImpl(Supplier<A> supplier,
                      BiConsumer<A, T> accumulator,
                      BinaryOperator<A> combiner,
                      Function<A, R> finisher,
                      Set<Characteristics> characteristics) {
            this.supplier = Objects.requireNonNull(supplier);
            this.accumulator = Objects.requireNonNull(accumulator);
            this.combiner = Objects.requireNonNull(combiner);
            this.finisher = Objects.requireNonNull(finisher);
            this.characteristics = Objects.requireNonNull(characteristics);
        }

        @Override
        public Supplier<A> supplier() {
            return this.supplier;
        }

        @Override
        public BiConsumer<A, T> accumulator() {
            return this.accumulator;
        }

        @Override
        public BinaryOperator<A> combiner() {
            return this.combiner;
        }

        @Override
        public Function<A, R> finisher() {
            return this.finisher;
        }

        @Override
        public Set<Characteristics> characteristics() {
            return Collections.unmodifiableSet(this.characteristics);
        }
    }

}
