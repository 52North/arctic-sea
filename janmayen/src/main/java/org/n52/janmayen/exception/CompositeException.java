/*
 * Copyright (C) 2015-2021 52°North Spatial Information Research GmbH
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
package org.n52.janmayen.exception;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Spliterator;
import java.util.concurrent.Callable;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.n52.janmayen.function.ThrowingBiConsumer;
import org.n52.janmayen.function.ThrowingBiFunction;
import org.n52.janmayen.function.ThrowingBiPredicate;
import org.n52.janmayen.function.ThrowingCallable;
import org.n52.janmayen.function.ThrowingConsumer;
import org.n52.janmayen.function.ThrowingFunction;
import org.n52.janmayen.function.ThrowingPredicate;
import org.n52.janmayen.function.ThrowingRunnable;
import org.n52.janmayen.function.ThrowingSupplier;
import org.n52.janmayen.function.ThrowingTriConsumer;
import org.n52.janmayen.function.ThrowingUnaryOperator;
import org.n52.janmayen.function.TriConsumer;

import com.google.common.base.Throwables;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
@SuppressWarnings("unchecked")
public class CompositeException extends Exception implements Iterable<Throwable> {
    private static final long serialVersionUID = -5245180365129871997L;

    private final List<Throwable> exceptions = new LinkedList<>();

    public List<Throwable> getExceptions() {
        return Collections.unmodifiableList(this.exceptions);
    }

    public int size() {
        return this.exceptions.size();
    }

    public Stream<Throwable> stream() {
        return this.exceptions.stream();
    }

    private Stream<Throwable> stream(Throwable t) {
        if (t instanceof CompositeException) {
            return ((CompositeException) t).stream();
        } else {
            return Stream.of(t);
        }
    }

    public void add(Throwable throwable) {
        stream(throwable).forEach(t -> {
            this.exceptions.add(t);
            if (getCause() == null) {
                initCause(t);
            } else {
                addSuppressed(t);
            }
        });
    }

    public void add(Collection<? extends Throwable> throwables) {
        throwables.forEach(this::add);
    }

    public boolean isEmpty() {
        return this.exceptions.isEmpty();
    }

    public boolean hasExceptions() {
        return !isEmpty();
    }

    public void throwIfNotEmpty() throws CompositeException {
        if (hasExceptions()) {
            throw this;
        }
    }

    public <X extends Throwable> void throwIfNotEmpty(Function<? super CompositeException, X> factory) throws X {
        if (hasExceptions()) {
            throw factory.apply(this);
        }
    }

    public <X extends Exception> void throwCause(Class<? extends X> type) throws X {
        Throwable cause = getCause();
        if (cause != null) {
            Throwables.throwIfUnchecked(cause);
            Throwables.throwIfInstanceOf(cause, type);
        }
    }

    @Override
    public Iterator<Throwable> iterator() {
        return new Iterator<Throwable>() {
            private final Iterator<Throwable> iter = exceptions.iterator();

            @Override
            public boolean hasNext() {
                return iter.hasNext();
            }

            @Override
            public Throwable next() {
                return iter.next();
            }
        };
    }

    @Override
    public void forEach(Consumer<? super Throwable> action) {
        this.exceptions.forEach(action);
    }

    @Override
    public Spliterator<Throwable> spliterator() {
        return this.exceptions.spliterator();
    }

    public <X extends Exception> Runnable wrapRunnable(ThrowingRunnable<X> runnable) {
        return () -> run(runnable);
    }

    public <T> Supplier<Optional<T>> wrapCallable(Callable<T> callable) {
        return () -> call(callable);
    }

    public <T, X extends Exception> Supplier<Optional<T>> wrapCallable(ThrowingCallable<T, X> callable) {
        return () -> call(callable);
    }

    public <T, X extends Exception> Consumer<T> wrapConsumer(ThrowingConsumer<T, X> consumer) {
        return t -> accept(consumer, t);
    }

    public <T, U, X extends Exception> BiConsumer<T, U> wrapConsumer(ThrowingBiConsumer<T, U, X> consumer) {
        return (t, u) -> accept(consumer, t, u);
    }

    public <A, B, C, X extends Exception> TriConsumer<A, B, C> wrapConsumer(ThrowingTriConsumer<A, B, C, X> consumer) {
        return (a, b, c) -> accept(consumer, a, b, c);
    }

    public <T, R, X extends Exception> Function<T, Optional<R>> wrapFunction(ThrowingFunction<T, R, X> function) {
        return t -> apply(function, t);
    }

    public <T, U, R, X extends Exception> BiFunction<T, U, Optional<R>> wrapFunction(
            ThrowingBiFunction<T, U, R, X> function) {
        return (t, u) -> apply(function, t, u);
    }

    public <T, X extends Exception> Function<T, Optional<T>> wrapOperator(ThrowingUnaryOperator<T, X> operator) {
        return wrapFunction((ThrowingFunction<T, T, X>) operator);
    }

    public <T, X extends Exception> Supplier<Optional<T>> wrapSupplier(ThrowingSupplier<T, X> supplier) {
        return () -> get(supplier);
    }

    public <T, X extends Exception> Predicate<T> wrapPredicate(
            ThrowingPredicate<T, X> predicate, boolean defaultValue) {
        return t -> test(predicate, defaultValue, t);
    }

    public <T, U, X extends Exception> BiPredicate<T, U> wrapPredicate(
            ThrowingBiPredicate<T, U, X> predicate, boolean defaultValue) {
        return (t, u) -> test(predicate, defaultValue, t, u);
    }

    @SuppressWarnings("UseSpecificCatch")
    public <X extends Exception> void run(ThrowingRunnable<X> runnable) {
        wrapRunnable(runnable).run();
        try {
            runnable.run();
        } catch (Exception e) {
            add((X) e);
        }
    }

    public <T> Optional<T> call(Callable<T> callable) {
        try {
            T result = callable.call();
            return Optional.ofNullable(result);
        } catch (Exception e) {
            add(e);
            return Optional.empty();
        }
    }

    @SuppressWarnings("UseSpecificCatch")
    public <T, X extends Exception> Optional<T> call(ThrowingCallable<T, X> callable) {
        try {
            T result = callable.call();
            return Optional.ofNullable(result);
        } catch (Exception e) {
            add((X) e);
            return Optional.empty();
        }
    }

    @SuppressWarnings("UseSpecificCatch")
    public <T, X extends Exception> void accept(ThrowingConsumer<T, X> consumer, T t) {
        try {
            consumer.accept(t);
        } catch (Exception ex) {
            add((X) ex);
        }
    }

    @SuppressWarnings("UseSpecificCatch")
    public <T, U, X extends Exception> void accept(ThrowingBiConsumer<T, U, X> consumer, T t, U u) {
        try {
            consumer.accept(t, u);
        } catch (Exception ex) {
            add((X) ex);
        }
    }

    @SuppressWarnings("UseSpecificCatch")
    public <A, B, C, X extends Exception> void accept(ThrowingTriConsumer<A, B, C, X> consumer, A a, B b, C c) {
        try {
            consumer.accept(a, b, c);
        } catch (Exception ex) {
            add((X) ex);
        }
    }

    @SuppressWarnings("UseSpecificCatch")
    public <T, R, X extends Exception> Optional<R> apply(ThrowingFunction<T, R, X> function, T t) {
        try {
            return Optional.ofNullable(function.apply(t));
        } catch (Exception ex) {
            add((X) ex);
            return Optional.empty();
        }
    }

    @SuppressWarnings("UseSpecificCatch")
    public <T, U, R, X extends Exception> Optional<R> apply(ThrowingBiFunction<T, U, R, X> function, T t, U u) {
        try {
            return Optional.ofNullable(function.apply(t, u));
        } catch (Exception ex) {
            add((X) ex);
            return Optional.empty();
        }
    }

    @SuppressWarnings("UseSpecificCatch")
    public <T, X extends Exception> Optional<T> get(ThrowingSupplier<T, X> supplier) {
        try {
            return Optional.ofNullable(supplier.get());
        } catch (Exception ex) {
            add((X) ex);
            return Optional.empty();
        }
    }

    @SuppressWarnings("UseSpecificCatch")
    public <T, X extends Exception> boolean test(ThrowingPredicate<T, X> predicate, boolean defaultValue, T t) {
        try {
            return predicate.test(t);
        } catch (Exception ex) {
            add((X) ex);
            return defaultValue;
        }
    }

    @SuppressWarnings("UseSpecificCatch")
    public <T, U, X extends Exception> boolean test(ThrowingBiPredicate<T, U, X> predicate,
                                                    boolean defaultValue, T t, U u) {
        try {
            return predicate.test(t, u);
        } catch (Exception ex) {
            add((X) ex);
            return defaultValue;
        }
    }
}
