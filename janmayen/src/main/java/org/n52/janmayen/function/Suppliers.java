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
package org.n52.janmayen.function;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Utility functions for {@link Supplier}.
 *
 * @author Christian Autermann
 */
public final class Suppliers {
    private Suppliers() {

    }

    /**
     * Returns a constant supplier.
     *
     * @param <T> the type of the supplied value
     * @param t   the constant
     *
     * @return the constant supplier
     */
    public static <T> Supplier<T> constant(T t) {
        return () -> t;
    }

    /**
     * Maps the output of an supplier using a function.
     *
     * @param <U>      The original suppliers return type
     * @param <V>      The new suppliers return type
     * @param supplier the original supplier
     * @param mapper   the mapper
     *
     * @return the new supplier
     */
    public static <U, V> Supplier<V> mapping(Function<? super U, ? extends V> mapper, Supplier<U> supplier) {
        return () -> mapper.apply(supplier.get());
    }

    /**
     * Creates a function ignoring it's parameter and just returning the supplier's value.
     *
     * @param <U>      the parameter type
     * @param <V>      the supplier's return type
     * @param supplier the supplier
     *
     * @return the function
     */
    public static <U, V> Function<U, V> asFunction(Supplier<V> supplier) {
        return (u) -> supplier.get();
    }
}
