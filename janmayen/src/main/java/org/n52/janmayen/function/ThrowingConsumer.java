/*
 * Copyright (C) 2015-2022 52°North Spatial Information Research GmbH
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
 * Represents an operation that accepts a single input argument and returns no result. Unlike most other functional
 * interfaces, {@code Consumer} is expected to operate via side-effects.
 *
 * @param <A> the type of the input to the operation
 * @param <X> the type of the exception the operation might throw
 *
 * @author Christian Autermann
 *
 */
@FunctionalInterface
public interface ThrowingConsumer<A, X extends Exception> {
    /**
     * Performs this operation on the given argument.
     *
     * @param t the input argument
     *
     * @throws X if an error occurs
     */
    void accept(A t)
            throws X;

    /**
     * Returns a composed {@code Consumer} that performs, in sequence, this operation followed by the {@code after}
     * operation. If performing either operation throws an exception, it is relayed to the caller of the composed
     * operation. If performing this operation throws an exception, the {@code after} operation will not be performed.
     * <p>
     *
     * @param after the operation to perform after this operation
     *
     * @return a composed {@code Consumer} that performs in sequence this operation followed by the {@code after}
     *         operation
     *
     * @throws NullPointerException if {@code after} is null
     */
    default ThrowingConsumer<A, X> andThen(ThrowingConsumer<? super A, ? extends X> after) {
        Objects.requireNonNull(after);
        return (A t) -> {
            accept(t);
            after.accept(t);
        };
    }

    /**
     * Create a {@link ThrowingRunnable} by currying the parameter.
     *
     * @param t the constant parameter
     *
     * @return the runnable
     */
    default ThrowingRunnable<X> curry(A t) {
        return () -> accept(t);
    }

    /**
     * Change the type of the parameter using the supplied mapper
     *
     * @param <T> the new input type of the operation
     * @param fun the mapper
     *
     * @return the consumer
     */
    default <T> ThrowingConsumer<T, X> map(ThrowingFunction<? super T, ? extends A, X> fun) {
        return t -> accept(fun.apply(t));
    }

}
