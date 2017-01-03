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
package org.n52.janmayen.function;

import java.util.Objects;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 * @param <A> the type of the first argument to the operation
 * @param <B> the type of the second argument to the operation
 * @param <C> the type of the third argument to the operation
 * @param <X> the type of the thrown exception
 */
@FunctionalInterface
public interface ThrowingTriConsumer<A, B, C, X extends Exception> {
    void accept(A a, B b, C c) throws X;

    /**
     * Returns a composed {@code ThrowingTriConsumer} that performs, in sequence, this
     * operation followed by the {@code after} operation. If performing either
     * operation throws an exception, it is relayed to the caller of the
     * composed operation. If performing this operation throws an exception,
     * the {@code after} operation will not be performed.
     *
     * @param after the operation to perform after this operation
     *
     * @return a composed {@code ThrowingTriConsumer} that performs in sequence this
     *         operation followed by the {@code after} operation
     *
     * @throws NullPointerException if {@code after} is null
     */
    default ThrowingTriConsumer<A, B, C, X> andThen(
            ThrowingTriConsumer<? super A, ? super B, ? super C, ? extends X> after) {
        Objects.requireNonNull(after);

        return (a, b, c) -> {
            accept(a, b, c);
            after.accept(a, b, c);
        };
    }

    default ThrowingBiConsumer<B, C, X> curryFirst(A a) {
        return (b, c) -> accept(a, b, c);
    }

    default ThrowingBiConsumer<A, C, X> currySecond(B b) {
        return (a, c) -> accept(a, b, c);
    }

    default ThrowingBiConsumer<A, B, X> curryThird(C c) {
        return (a, b) -> accept(a, b, c);
    }

    default <T> ThrowingTriConsumer<T, B, C, X> mapFirst(ThrowingFunction<? super T, ? extends A, X> fun) {
        return (t, b, c) -> accept(fun.apply(t), b, c);
    }

    default <T> ThrowingTriConsumer<A, T, C, X> mapSecond(ThrowingFunction<? super T, ? extends B, X> fun) {
        return (a, t, c) -> accept(a, fun.apply(t), c);
    }

    default <T> ThrowingTriConsumer<A, B, T, X> mapThird(ThrowingFunction<? super T, ? extends C, X> fun) {
        return (a, b, t) -> accept(a, b, fun.apply(t));
    }
}
