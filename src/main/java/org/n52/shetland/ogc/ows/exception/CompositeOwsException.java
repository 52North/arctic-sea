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
package org.n52.shetland.ogc.ows.exception;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;

import org.n52.janmayen.function.ThrowingCallable;
import org.n52.janmayen.function.ThrowingConsumer;
import org.n52.janmayen.function.ThrowingRunnable;

/**
 * Composite {@link OwsExceptionReport} which can contain several
 * {@link OwsExceptionReport}s which were thrown
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
public class CompositeOwsException extends OwsExceptionReport {
    private static final long serialVersionUID = -4876354677532448922L;

    private final List<CodedException> exceptions = new LinkedList<>();

    public CompositeOwsException(OwsExceptionReport... exceptions) {
        add(exceptions);
    }

    public CompositeOwsException(Collection<? extends OwsExceptionReport> exceptions) {
        add(exceptions);
    }

    public CompositeOwsException() {
    }

    public final CompositeOwsException addAllOf(CompositeOwsException exceptions) {
        return this.add(exceptions.getExceptions());
    }

    public final CompositeOwsException add(Collection<? extends OwsExceptionReport> exceptions) {
        if (exceptions != null) {
            exceptions.stream()
                    .map(OwsExceptionReport::getExceptions)
                    .forEach(this.exceptions::addAll);

            if (getCause() == null && !this.exceptions.isEmpty()) {
                initCause(this.exceptions.get(0));
            }
        }
        return this;
    }

    public final CompositeOwsException add(OwsExceptionReport... exceptions) {
        return add(Arrays.asList(exceptions));
    }

    @Override
    public List<? extends CodedException> getExceptions() {
        return Collections.unmodifiableList(this.exceptions);
    }

    public void throwIfNotEmpty() throws CompositeOwsException {
        if (hasExceptions()) {
            throw this;
        }
    }

    public int size() {
        return this.exceptions.size();
    }

    public boolean isEmpty() {
        return this.exceptions.isEmpty();
    }

    public boolean hasExceptions() {
        return !isEmpty();
    }

    public void wrap(ThrowingRunnable<OwsExceptionReport> runnable) {
        try {
            runnable.run();
        } catch (OwsExceptionReport e) {
            add(e);
        }
    }

    public <T> Optional<T> wrap(ThrowingCallable<T, OwsExceptionReport> runnable) {
        try {
            return Optional.ofNullable(runnable.call());
        } catch (OwsExceptionReport e) {
            add(e);
            return Optional.empty();
        }
    }

    public static <T> Collector<? super T, ?, CompositeOwsException> toCompositeException(
            ThrowingConsumer<? super T, ? extends OwsExceptionReport> fun) {
        return new ExceptionCollector<>(fun);
    }

    public static <T> void check(Stream<? extends T> stream,
            ThrowingConsumer<? super T, ? extends OwsExceptionReport> consumer)
            throws OwsExceptionReport {
        CompositeOwsException exceptions = new CompositeOwsException();
        stream.forEach(t -> exceptions.wrap(() -> consumer.accept(t)));
        exceptions.throwIfNotEmpty();
    }

    private static class ExceptionCollector<T> implements Collector<T, CompositeOwsException, CompositeOwsException> {
        private final ThrowingConsumer<? super T, ? extends OwsExceptionReport> fun;

        ExceptionCollector(ThrowingConsumer<? super T, ? extends OwsExceptionReport> fun) {
            this.fun = fun;
        }

        @Override
        public Supplier<CompositeOwsException> supplier() {
            return CompositeOwsException::new;
        }

        @Override
        public BiConsumer<CompositeOwsException, T> accumulator() {
            return (composite, t) -> {
                try {
                    fun.accept(t);
                } catch (OwsExceptionReport e) {
                    composite.add(e);
                }
            };
        }

        @Override
        public BinaryOperator<CompositeOwsException> combiner() {
            return CompositeOwsException::addAllOf;
        }

        @Override
        public Function<CompositeOwsException, CompositeOwsException> finisher() {
            return Function.identity();
        }

        @Override
        public Set<Characteristics> characteristics() {
            return Collections.singleton(Characteristics.IDENTITY_FINISH);
        }
    }

}
