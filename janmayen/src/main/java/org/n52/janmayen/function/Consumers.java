/*
 * Copyright 2015-2017 52°North Initiative for Geospatial Open Source
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
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import javax.annotation.Nonnull;

/**
 * Utility functions for {@link Consumer}.
 *
 * @author Christian Autermann
 */
public final class Consumers {

    private Consumers() {
    }

    /**
     * Curries the second parameter of the {@code BiConsumer} and creates a {@code Consumer}.
     *
     * @param <T>      the first parameter type
     * @param <U>      the second parameter type
     * @param consumer the consumer
     * @param t        the curried parameter
     *
     * @return the curried consumer
     */
    public static <T, U> Consumer<U> curryFirst(@Nonnull BiConsumer<T, U> consumer, T t) {
        Objects.requireNonNull(consumer);
        return (u) -> consumer.accept(t, u);
    }

    /**
     * Curries the second parameter of the {@code BiConsumer} and creates a {@code Consumer}.
     *
     * @param <T>      the first parameter type
     * @param <U>      the second parameter type
     * @param consumer the consumer
     * @param u        the curried parameter
     *
     * @return the curried consumer
     */
    public static <T, U> Consumer<T> currySecond(@Nonnull BiConsumer<T, U> consumer, U u) {
        Objects.requireNonNull(consumer);
        return (t) -> consumer.accept(t, u);
    }

    /**
     * Reverses the parameter order of the BiConsumer
     *
     * @param <A>      the first parameter type
     * @param <B>      the second parameter type
     * @param consumer the consumer
     *
     * @return the consumer with switched parameters
     */
    public static <A, B> BiConsumer<B, A> reverse(@Nonnull BiConsumer<A, B> consumer) {
        Objects.requireNonNull(consumer);
        return (a, b) -> consumer.accept(b, a);
    }

}
