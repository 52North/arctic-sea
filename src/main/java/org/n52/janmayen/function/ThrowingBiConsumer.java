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
import java.util.function.Consumer;

/**
 * Represents an operation that accepts two input arguments and returns no result. This is the two-arity specialization
 * of {@link Consumer}. Unlike most other functional interfaces, {@code BiConsumer} is expected to operate via
 * side-effects.
 *
 * @param <A> the type of the first argument to the operation
 * @param <B> the type of the second argument to the operation
 * @param <X> the type of the exception that might be thrown
 *
 * @author Christian Autermann
 */
@FunctionalInterface
public interface ThrowingBiConsumer<A, B, X extends Exception> {

    /**
     * Performs this operation on the given arguments.
     *
     * @param t the first input argument
     * @param u the second input argument
     *
     * @throws X if the operation fails
     */
    void accept(A t, B u) throws X;

    /**
     * Returns a composed {@code BiConsumer} that performs, in sequence, this operation followed by the {@code after}
     * operation. If performing either operation throws an exception, it is relayed to the caller of the composed
     * operation. If performing this operation throws an exception, the {@code after} operation will not be performed.
     *
     * @param after the operation to perform after this operation
     *
     * @return a composed {@code BiConsumer} that performs in sequence this operation followed by the {@code after}
     *         operation
     *
     * @throws NullPointerException if {@code after} is null
     */
    default ThrowingBiConsumer<A, B, X> andThen(ThrowingBiConsumer<? super A, ? super B, ? extends X> after) {
        Objects.requireNonNull(after);
        return (l, r) -> {
            accept(l, r);
            after.accept(l, r);
        };
    }

    /**
     * Get a {@link ThrowingConsumer} by currying the first parameter.
     *
     * @param t the constant parameter
     *
     * @return the consumer
     */
    default ThrowingConsumer<B, X> curryFirst(A t) {
        return u -> accept(t, u);
    }

    /**
     * Get {@link ThrowingConsumer} by currying the second parameter.
     *
     * @param u the constant parameter
     *
     * @return the consumer
     */
    default ThrowingConsumer<A, X> currySecond(B u) {
        return t -> accept(t, u);
    }

    /**
     * Change the first parameter using the supplied mapper.
     *
     * @param <T> the new type of the first parameter
     * @param fun the mapper
     *
     * @return the consumer
     */
    default <T> ThrowingBiConsumer<T, B, X> mapFirst(ThrowingFunction<? super T, ? extends A, X> fun) {
        return (t, b) -> accept(fun.apply(t), b);
    }

    /**
     * Change the second parameter using the supplied mapper.
     *
     * @param <T> the new type of the second parameter
     * @param fun the mapper
     *
     * @return the consumer
     */
    default <T> ThrowingBiConsumer<A, T, X> mapSecond(ThrowingFunction<? super T, ? extends B, X> fun) {
        return (a, t) -> accept(a, fun.apply(t));
    }

}
