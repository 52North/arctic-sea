/*
 * Copyright 2015-2022 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.ogc.om;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import javax.annotation.CheckReturnValue;

import org.n52.janmayen.ThrowingIterator;
import org.n52.janmayen.function.ThrowingConsumer;
import org.n52.janmayen.function.ThrowingFunction;
import org.n52.janmayen.function.ThrowingUnaryOperator;
import org.n52.janmayen.stream.Streams;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public interface ObservationStream
        extends ThrowingIterator<OmObservation, OwsExceptionReport>, AutoCloseable {
    @Override
    default void close() {
    }

    default <T extends Collection<OmObservation>> T collect(Supplier<T> supplier) throws OwsExceptionReport {
        T collection = supplier.get();
        forEachRemaining(collection::add);
        return collection;
    }

    /**
     * Gets the next value that is in the stream.
     *
     * @return the first observation of the stream
     *
     * @throws OwsExceptionReport
     *             if an error occurs during observation retrieval
     */
    default Optional<OmObservation> findFirst() throws OwsExceptionReport {
        if (hasNext()) {
            return Optional.of(next());
        } else {
            return Optional.empty();
        }
    }

    /**
     * Creates a new stream of this observation stream. Note that consuming the
     * stream will drain the iterator and all thrown exceptions will be wrapped
     * in {@link RuntimeException}s.
     *
     * @return the stream
     */
    default Stream<OmObservation> toStream() {
        return Streams.stream(new Iterator<OmObservation>() {
            @Override
            public boolean hasNext() {
                try {
                    return ObservationStream.this.hasNext();
                } catch (OwsExceptionReport ex) {
                    throw new RuntimeException(ex);
                }

            }

            @Override
            public OmObservation next() {
                try {
                    return ObservationStream.this.next();
                } catch (OwsExceptionReport ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    /**
     * As this stream is always serial this is the same as {@link #findFirst() }
     * .
     *
     * @return the first observation of the stream
     *
     * @throws OwsExceptionReport
     *             if an error occurs during observation retrieval
     */
    default Optional<OmObservation> findAny() throws OwsExceptionReport {
        return findFirst();
    }

    /**
     *
     * Creates a new observation stream from an consumer that consumes every
     * observation before it is emitted by the stream.
     *
     * @param consumer
     *            the consumer
     *
     * @return the new stream
     */
    @CheckReturnValue
    default ObservationStream modify(ThrowingConsumer<OmObservation, OwsExceptionReport> consumer) {
        Objects.requireNonNull(consumer);
        return map(o -> {
            consumer.accept(o);
            return o;
        });
    }

    /**
     * Creates a new observation stream from a mapping function that produces a
     * stream for every observation in this stream.
     *
     * @param mapper
     *            the mapping function
     *
     * @return the new stream
     */
    @CheckReturnValue
    default ObservationStream flatMap(ThrowingFunction<OmObservation, ObservationStream, OwsExceptionReport> mapper) {
        Objects.requireNonNull(mapper);
        ObservationStream delegate = this;
        return new AbstractObservationStream() {
            private ObservationStream current;

            @Override
            protected OmObservation computeNext() throws OwsExceptionReport {
                if (this.current == null) {
                    if (!delegate.hasNext()) {
                        return endOfData();
                    } else {
                        this.current = mapper.apply(delegate.next());
                    }
                }
                if (!current.hasNext()) {
                    current.close();
                    return endOfData();
                }
                return current.next();
            }

            @Override
            public void close() {
                try {
                    if (current != null) {
                        current.close();
                    }
                } finally {
                    delegate.close();
                }
            }
        };
    }

    /**
     * Produces a observation new stream from a mapping function that produces a
     * new observation for every observation in this stream.
     *
     * @param operator
     *            the mapping function
     *
     * @return the new stream
     */
    @CheckReturnValue
    default ObservationStream map(ThrowingUnaryOperator<OmObservation, OwsExceptionReport> operator) {
        Objects.requireNonNull(operator);
        ObservationStream delegate = this;
        return new AbstractObservationStream() {
            @Override
            protected OmObservation computeNext() throws OwsExceptionReport {
                if (!delegate.hasNext()) {
                    return endOfData();
                }
                return operator.apply(delegate.next());
            }

            @Override
            public void close() {
                delegate.close();
            }
        };
    }

    /**
     * Creates a new observation stream that filters the observatons from the
     * original stream using the supplied predicate.
     *
     * @param predicate
     *            the filter
     *
     * @return the new stream
     */
    @CheckReturnValue
    default ObservationStream filter(Predicate<OmObservation> predicate) {
        Objects.requireNonNull(predicate);
        ObservationStream delegate = this;
        return new AbstractObservationStream() {
            @Override
            protected OmObservation computeNext() throws OwsExceptionReport {
                while (delegate.hasNext()) {
                    OmObservation next = delegate.next();
                    if (predicate.test(next)) {
                        return next;
                    }
                }
                return endOfData();
            }

            @Override
            public void close() {
                delegate.close();
            }
        };
    }

    /**
     * Creates a new stream out of this stream in which observations with the
     * same observation constellation are merged. Be aware that this method will
     * consume this stream completely.
     *
     * @return the new observation stream
     *
     * @throws OwsExceptionReport
     *             if an error occurs during observation retrieval
     *
     * @see OmObservation#checkForMerge(org.n52.shetland.ogc.om.OmObservation)
     * @see OmObservation#mergeWithObservation(org.n52.shetland.ogc.om.OmObservation)
     */
    @CheckReturnValue
    default ObservationStream merge() throws OwsExceptionReport {
        return merge(ObservationMergeIndicator.sameObservationConstellation());
    }

    /**
     * Creates a new stream out of this stream in which observations with the
     * same observation constellation are merged. Be aware that this method will
     * consume this stream completely.
     *
     * @param indicator
     *
     * @return the new observation stream
     *
     * @throws OwsExceptionReport
     *             if an error occurs during observation retrieval
     *
     * @see OmObservation#checkForMerge(org.n52.shetland.ogc.om.OmObservation)
     * @see OmObservation#mergeWithObservation(org.n52.shetland.ogc.om.OmObservation)
     */
    @CheckReturnValue
    default ObservationStream merge(ObservationMergeIndicator indicator) throws OwsExceptionReport {
        List<OmObservation> mergedObservations = new LinkedList<>();
        int obsIdCounter = 1;
        try {
            while (hasNext()) {
                OmObservation observation = next();
                Optional<OmObservation> merge = mergedObservations.stream().filter(Objects::nonNull)
                        .filter(o -> o.checkForMerge(observation, indicator)).findAny();
                if (merge.isPresent()) {
                    merge.get().mergeWithObservation(observation);
                } else {
                    if (!observation.isSetGmlID()) {
                        observation.setObservationID(Integer.toString(obsIdCounter++));
                    }
                    mergedObservations.add(observation);
                }
            }
        } finally {
            close();
        }
        return of(mergedObservations);
    }

    /**
     * Creates an empty observation stream.
     *
     * @return the stream
     */
    static ObservationStream empty() {
        return new ObservationStream() {
            @Override
            public OmObservation next() {
                throw new NoSuchElementException();
            }

            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public ObservationStream map(ThrowingUnaryOperator<OmObservation, OwsExceptionReport> operator) {
                return this;
            }

            @Override
            public ObservationStream flatMap(
                    ThrowingFunction<OmObservation, ObservationStream, OwsExceptionReport> mapper) {
                return this;
            }

            @Override
            public ObservationStream filter(Predicate<OmObservation> predicate) {
                return this;
            }

            @Override
            public ObservationStream merge() {
                return this;
            }

            @Override
            public ObservationStream merge(ObservationMergeIndicator indicator) {
                return this;
            }

            @Override
            public ObservationStream modify(ThrowingConsumer<OmObservation, OwsExceptionReport> consumer) {
                return this;
            }

        };
    }

    /**
     * Creates a new observation stream from the supplied iterator.
     *
     * @param observations
     *            the observations
     *
     * @return the stream
     */
    static ObservationStream of(Iterator<OmObservation> observations) {
        Objects.requireNonNull(observations);
        return new ObservationStream() {

            @Override
            public OmObservation next() {
                return observations.next();
            }

            @Override
            public boolean hasNext() {
                return observations.hasNext();
            }
        };
    }

    /**
     * Creates a new observation stream from the supplied iterable.
     *
     * @param observations
     *            the observations
     *
     * @return the stream
     */
    static ObservationStream of(Iterable<OmObservation> observations) {
        return of(observations.iterator());
    }

    /**
     * Creates a new observation stream from the supplied observation.
     *
     * @param observation
     *            the observation
     *
     * @return the stream
     */
    static ObservationStream of(OmObservation observation) {
        Objects.requireNonNull(observation);
        return new ObservationStream() {

            private boolean done;

            @Override
            public OmObservation next() {
                if (done) {
                    throw new NoSuchElementException();
                }
                done = true;
                return observation;
            }

            @Override
            public boolean hasNext() {
                return !done;
            }

            @Override
            public ObservationStream merge() throws OwsExceptionReport {
                return this;
            }

            @Override
            public ObservationStream merge(ObservationMergeIndicator indicator) throws OwsExceptionReport {
                return this;
            }
        };
    }

}
