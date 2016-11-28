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

import java.math.BigInteger;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collector.Characteristics;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.n52.janmayen.function.Functions;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class MoreCollectors {

    private MoreCollectors() {
    }

    public static <T, U, A, R> Collector<T, ?, R> flatMapping(Function<? super T, ? extends Stream<? extends U>> mapper,
                                                              Collector<? super U, A, R> downstream) {
        BiConsumer<A, ? super U> downstreamAccumulator = downstream.accumulator();
        return Collector.of(downstream.supplier(),
                            (r, t) -> mapper.apply(t).sequential().forEach(u -> downstreamAccumulator.accept(r, u)),
                            downstream.combiner(),
                            downstream.finisher(),
                            downstream.characteristics().stream().toArray(Characteristics[]::new));
    }

    public static <X, T> Collector<X, ?, Map<Chain<T>, BigInteger>> toCardinalities(
            Function<X, T> id, Predicate<X> hasSubElements, Function<X, Stream<X>> subElements) {
        return new CardinalityCalculator<>(id, hasSubElements, subElements).countCollector();

    }

    public static <X, T> Collector<X, ?, Map<Chain<T>, X>> toChain(
            Function<X, T> id, Predicate<X> hasSubElements, Function<X, Stream<X>> subElements) {
        return toChain(id, hasSubElements, subElements, Function.identity());
    }

    public static <X, T, U> Collector<X, ?, Map<Chain<T>, U>> toChain(
            Function<X, T> id, Predicate<X> hasSubElements, Function<X, Stream<X>> subElements, Function<X, U> finisher) {
        return new ChainCollector<>(id, hasSubElements, subElements).collector(finisher);
    }

    public static <X> Collector<X, ?, Set<X>> toDuplicateSet() {
        return toDuplicateSet(2);
    }

    public static <X> Collector<X, ?, Stream<X>> toDuplicateStream() {
        return toDuplicateStream(2);
    }

    public static <X> Collector<X, ?, Set<X>> toDuplicateSet(int min) {
        if (min < 2) {
            throw new IllegalArgumentException();
        }
        return Collector.of(HashMap::new,
                            (map, key) -> map.merge(key, 1, Integer::sum),
                            Functions.mergeToLeftMap(Integer::sum),
                            Functions.keySetWhereValues(v -> v >= min),
                            Characteristics.UNORDERED);
    }

    public static <X> Collector<X, ?, Stream<X>> toDuplicateStream(int min) {
        if (min < 2) {
            throw new IllegalArgumentException();
        }
        return Collector.of(HashMap::new,
                            (map, key) -> map.merge(key, 1, Integer::sum),
                            Functions.mergeToLeftMap(Integer::sum),
                            Functions.keyStreamWhereValues(v -> v >= min),
                            Characteristics.UNORDERED);
    }

    public static <T> Collector<T, ?, T> toSingleResult() {
        return toSingleResult(IllegalStateException::new);
    }

    public static <T> Collector<T, ?, T> toSingleResult(Supplier<? extends RuntimeException> exceptionSupplier) {
        Objects.requireNonNull(exceptionSupplier);
        return Collector.of(LinkedList<T>::new,
                            List<T>::add,
                            Functions.mergeLeft(List::addAll),
                            (list) -> {
                                if (list.size() != 1) {
                                    throw exceptionSupplier.get();
                                }
                                return list.get(0);
                            },
                            Characteristics.UNORDERED);
    }

    private static class ChainCollector<X, T> {
        private final Function<X, T> id;
        private final Predicate<X> hasSubElements;
        private final Function<X, Stream<X>> subElements;

        private ChainCollector(Function<X, T> id,
                               Predicate<X> hasSubElements,
                               Function<X, Stream<X>> subElements) {
            this.id = id;
            this.hasSubElements = hasSubElements;
            this.subElements = subElements;
        }

        protected Stream<Entry<Chain<T>, X>> toIdentifierChain(X x) {
            Chain<T> chain = new Chain<>(id.apply(x));
            Stream<Entry<Chain<T>, X>> elemStream = Stream.of(new SimpleEntry<>(chain, x));
            if (!hasSubElements.test(x)) {
                return elemStream;
            } else {
                return Stream.concat(elemStream, subElements.apply(x).flatMap(sub -> toIdentifierChain(chain, sub)));
            }
        }

        protected Stream<Entry<Chain<T>, X>> toIdentifierChain(Chain<T> parent, X x) {
            Chain<T> chain = parent.child(id.apply(x));
            Stream<Entry<Chain<T>, X>> elemStream = Stream.of(new SimpleEntry<>(chain, x));
            if (!hasSubElements.test(x)) {
                return elemStream;
            } else {
                return Stream.concat(elemStream, subElements.apply(x).flatMap(sub -> toIdentifierChain(chain, sub)));
            }
        }

        private <U> Collector<X, ?, Map<Chain<T>, U>> collector(Function<X, U> finisher) {
            Function<Entry<Chain<T>, X>, X> valueFunction = Entry::getValue;
            return MoreCollectors.flatMapping(this::toIdentifierChain, Collectors.toMap(Entry::getKey, valueFunction.andThen(finisher)));
        }
    }

    private static class CardinalityCalculator<X, T> extends ChainCollector<X, T> {
        private CardinalityCalculator(Function<X, T> id, Predicate<X> hasSubElements, Function<X, Stream<X>> subElements) {
            super(id, hasSubElements, subElements);
        }

        private Collector<X, ?, Map<Chain<T>, BigInteger>> countCollector() {

            return MoreCollectors.flatMapping(
                    this::toIdentifierChain,
                    Collectors.mapping(
                            Entry::getKey,
                            Collectors.groupingBy(
                                    Function.identity(),
                                    Collectors.mapping(
                                            Functions.constant(BigInteger.ONE),
                                            Collectors.reducing(BigInteger.ZERO, BigInteger::add)))));
        }

    }

}
