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

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.reducing;
import static java.util.stream.Collectors.toMap;

import java.math.BigInteger;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collector.Characteristics;
import java.util.stream.Stream;

import org.n52.janmayen.Chain;
import org.n52.janmayen.exception.CompositeException;
import org.n52.janmayen.function.Functions;
import org.n52.janmayen.function.ThrowingConsumer;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public final class MoreCollectors {

    private MoreCollectors() {
    }

    public static <T, A, R> Collector<T, A, R> filtering(Predicate<T> filter, Collector<T, A, R> downstream) {
        Objects.requireNonNull(filter);
        BiConsumer<A, T> downstreamAccumulator = downstream.accumulator();
        BiConsumer<A, T> accumulator = (a, t) -> {
            if (filter.test(t)) {
                downstreamAccumulator.accept(a, t);
            }
        };
        return Collector.of(downstream.supplier(), accumulator, downstream.combiner(), downstream.finisher(),
                            getCharacteristics(downstream));

    }

    public static <T, U, A, R> Collector<T, ?, R> flatMapping(Function<? super T, ? extends Stream<? extends U>> mapper,
                                                              Collector<? super U, A, R> downstream) {
        Objects.requireNonNull(mapper);
        BiConsumer<A, ? super U> downstreamAccumulator = downstream.accumulator();
        BiConsumer<A, T> accumulator = (r, t)
                -> mapper.apply(t).sequential().forEach(u -> downstreamAccumulator.accept(r, u));
        return Collector.of(downstream.supplier(), accumulator, downstream.combiner(), downstream.finisher(),
                            getCharacteristics(downstream));
    }

    public static <X, T> Collector<X, ?, Map<Chain<T>, BigInteger>> toCardinalities(
            Function<X, T> id, Predicate<X> hasSubElements, Function<X, Stream<X>> subElements) {
        return new CardinalityCalculator<>(id, hasSubElements, subElements).countCollector();
    }

    public static <X, T> Collector<X, ?, Map<Chain<T>, X>> toChain(Function<X, T> id,
                                                                   Predicate<X> hasSubElements,
                                                                   Function<X, Stream<X>> subElements) {
        return toChain(id, hasSubElements, subElements, Function.identity());
    }

    public static <X, T, U> Collector<X, ?, Map<Chain<T>, U>> toChain(Function<X, T> id, Predicate<X> hasSubElements,
                                                                      Function<X, Stream<X>> subElements,
                                                                      Function<X, U> finisher) {
        return new ChainCollector<>(id, hasSubElements, subElements).collector(finisher);
    }

    public static <X> Collector<X, ?, Set<X>> toDuplicateSet() {
        return toDuplicateSet(2);
    }

    public static <X> Collector<X, ?, Set<X>> toDuplicateSet(int min) {
        if (min < 2) {
            throw new IllegalArgumentException();
        }
        Supplier<Map<X, Integer>> supplier = HashMap::new;
        BiConsumer<Map<X, Integer>, X> accumulator = (map, key) -> map.merge(key, 1, Integer::sum);
        BinaryOperator<Map<X, Integer>> combiner = Functions.mergeToLeftMap(Integer::sum);
        Function<Map<X, Integer>, Set<X>> finisher = Functions.keySetWhereValues(v -> v >= min);
        return Collector.of(supplier, accumulator, combiner, finisher, Characteristics.UNORDERED);
    }

    public static <X> Collector<X, ?, Stream<X>> toDuplicateStream() {
        return toDuplicateStream(2);
    }

    public static <X> Collector<X, ?, Stream<X>> toDuplicateStream(int min) {
        if (min < 2) {
            throw new IllegalArgumentException();
        }
        Supplier<Map<X, Integer>> supplier = HashMap::new;
        BiConsumer<Map<X, Integer>, X> accumulator = (map, key) -> map.merge(key, 1, Integer::sum);
        BinaryOperator<Map<X, Integer>> combiner = Functions.mergeToLeftMap(Integer::sum);
        Function<Map<X, Integer>, Stream<X>> finisher = Functions.keyStreamWhereValues(v -> v >= min);
        return Collector.of(supplier, accumulator, combiner, finisher, Characteristics.UNORDERED);
    }

    public static <T> Collector<T, ?, T> toSingleResult() {
        return toSingleResult(IllegalStateException::new);
    }

    public static <T> Collector<T, ?, T> toSingleResult(Supplier<? extends RuntimeException> exceptionSupplier) {
        Objects.requireNonNull(exceptionSupplier);
        Supplier<List<T>> supplier = LinkedList<T>::new;
        BiConsumer<List<T>, T> accumulator = List<T>::add;
        BinaryOperator<List<T>> combiner = Functions.mergeLeft(List::addAll);
        Function<List<T>, T> finisher = list -> {
            if (list.size() != 1) {
                throw exceptionSupplier.get();
            }
            return list.get(0);
        };
        return Collector.of(supplier, accumulator, combiner, finisher, Characteristics.UNORDERED);
    }

    public static <K, V> Collector<Entry<K, V>, ?, Map<V, K>> toValueMap() {
        return toMap(Entry::getValue, Entry::getKey);
    }

    public static <K, V> Collector<Entry<K, V>, ?, LinkedHashMap<K, V>> toLinkedHashMap() {
        return toMap(Entry::getKey, Entry::getValue, Streams.throwingMerger(), LinkedHashMap::new);
    }

    @Deprecated
    public static <T, A, R> Collector<T, ?, R> collector(Supplier<A> supplier,
                                                         BiConsumer<A, T> accumulator,
                                                         BinaryOperator<A> combiner,
                                                         Function<A, R> finisher) {
        return Collector.of(supplier, accumulator, combiner, finisher);
    }

    public static <T, R> Collector<T, ?, R> collector(Supplier<R> supplier,
                                                      BiConsumer<R, T> accumulator,
                                                      BiConsumer<R, R> combiner) {
        BinaryOperator<R> binaryOperator = Functions.mergeLeft(combiner);
        return Collector.of(supplier, accumulator, binaryOperator);
    }

    @Deprecated
    public static <T, R> Collector<T, ?, R> collector(Supplier<R> supplier,
                                                      BiConsumer<R, T> accumulator,
                                                      BinaryOperator<R> combiner) {
        return Collector.of(supplier, accumulator, combiner);
    }

    public static <T, A, R> Collector<T, ?, R> collector(Supplier<A> supplier,
                                                         BiConsumer<A, T> accumulator,
                                                         BiConsumer<A, A> combiner,
                                                         Function<A, R> finisher) {
        BinaryOperator<A> binaryOperator = Functions.mergeLeft(combiner);
        return Collector.of(supplier, accumulator, binaryOperator, finisher);
    }

    public static <T, E extends Exception> Collector<T, ?, CompositeException> toCompositeException(
            ThrowingConsumer<? super T, E> fun) {
        Supplier<CompositeException> supplier = CompositeException::new;
        return toCompositeException(supplier, fun);
    }

    public static <T, E extends Exception, X extends CompositeException> Collector<T, ?, X> toCompositeException(
            Supplier<X> supplier, ThrowingConsumer<? super T, E> fun) {
        BiConsumer<X, T> accumulator = (composite, t) -> {
            try {
                fun.accept(t);
            } catch (Exception e) {
                composite.add(e);
            }
        };
        BiConsumer<X, X> combiner = CompositeException::add;
        return collector(supplier, accumulator, combiner);
    }

    private static <T, A, R> Characteristics[] getCharacteristics(Collector<T, A, R> downstream) {
        return downstream.characteristics().stream().toArray(Characteristics[]::new);
    }

    /**
     *
     * @param <X> The object type
     * @param <T> The id type
     */
    private static class ChainCollector<X, T> {
        private final Function<X, T> getIdentifier;
        private final Predicate<X> hasChildren;
        private final Function<X, Stream<X>> getChildren;

        /**
         * Creates a new {@code ChainCollector}.
         *
         * @param getIdentifier the id function
         * @param hasChildren   a predicate to check if x has children elements
         * @param getChildren   a function to get the children
         */
        protected ChainCollector(Function<X, T> getIdentifier,
                                 Predicate<X> hasChildren,
                                 Function<X, Stream<X>> getChildren) {
            this.getIdentifier = Objects.requireNonNull(getIdentifier);
            this.hasChildren = Objects.requireNonNull(hasChildren);
            this.getChildren = Objects.requireNonNull(getChildren);
        }

        protected Stream<Entry<Chain<T>, X>> toIdentifierChain(X x) {
            Chain<T> chain = new Chain<>(getIdentifier.apply(x));
            Stream<Entry<Chain<T>, X>> stream = Stream
                    .of(new SimpleEntry<>(chain, x));
            if (!hasChildren.test(x)) {
                return stream;
            } else {
                Stream<X> children = getChildren.apply(x);
                Function<X, Stream<Entry<Chain<T>, X>>> mapper
                        = sub -> toIdentifierChain(chain, sub);
                return Stream.concat(stream, children.flatMap(mapper));
            }
        }

        protected Stream<Entry<Chain<T>, X>> toIdentifierChain(Chain<T> parent,
                                                               X x) {
            T identifier = getIdentifier.apply(x);
            Chain<T> chain = parent.child(identifier);
            Entry<Chain<T>, X> entry = new SimpleEntry<>(chain, x);
            Stream<Entry<Chain<T>, X>> stream = Stream.of(entry);
            if (!hasChildren.test(x)) {
                return stream;
            } else {
                Stream<X> children = getChildren.apply(x);
                Function<X, Stream<Entry<Chain<T>, X>>> mapper
                        = sub -> toIdentifierChain(chain, sub);
                return Stream.concat(stream, children.flatMap(mapper));
            }
        }

        protected <U> Collector<X, ?, Map<Chain<T>, U>> collector(Function<X, U> finisher) {
            Function<Entry<Chain<T>, X>, X> valueFunction = Entry::getValue;
            Function<Entry<Chain<T>, X>, U> finish = valueFunction.andThen(finisher);
            Function<X, Stream<Entry<Chain<T>, X>>> toIdentifierChain = this::toIdentifierChain;
            return flatMapping(toIdentifierChain, toMap(Entry::getKey, finish));
        }
    }

    private static final class CardinalityCalculator<X, T> extends ChainCollector<X, T> {

        private CardinalityCalculator(Function<X, T> getIdentifier,
                                      Predicate<X> hasChildren,
                                      Function<X, Stream<X>> getChildren) {
            super(getIdentifier, hasChildren, getChildren);
        }

        private Collector<X, ?, Map<Chain<T>, BigInteger>> countCollector() {
            Function<Chain<T>, BigInteger> counter = Functions.constant(BigInteger.ONE);
            Function<Chain<T>, Chain<T>> identity = Function.identity();
            BinaryOperator<BigInteger> operator = BigInteger::add;
            Function<Entry<Chain<T>, X>, Chain<T>> getKey = Entry::getKey;

            Collector<BigInteger, ?, BigInteger> reducer = reducing(BigInteger.ZERO, operator);
            Collector<Chain<T>, ?, BigInteger> mapper = mapping(counter, reducer);
            Collector<Chain<T>, ?, Map<Chain<T>, BigInteger>> grouper = groupingBy(identity, mapper);
            Collector<Entry<Chain<T>, X>, ?, Map<Chain<T>, BigInteger>> mapper2 = mapping(getKey, grouper);
            Function<X, Stream<? extends Entry<Chain<T>, X>>> getIdentifierChain = this::toIdentifierChain;
            return flatMapping(getIdentifierChain, mapper2);
        }

    }

}
