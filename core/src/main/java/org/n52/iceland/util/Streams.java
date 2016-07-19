/*
 * Copyright 2015-2016 52°North Initiative for Geospatial Open Source
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
package org.n52.iceland.util;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;

public class Streams {
    private Streams() {
    }

    public static <T> BinaryOperator<T> throwingMerger(Supplier<? extends RuntimeException> exceptionSupplier) {
        Objects.requireNonNull(exceptionSupplier);
        return throwingMerger((a, b) -> exceptionSupplier.get());
    }

    public static <T> BinaryOperator<T> throwingMerger(BiFunction<T, T, ? extends RuntimeException> exceptionSupplier) {
        Objects.requireNonNull(exceptionSupplier);
        return (a, b) -> {
            throw exceptionSupplier.apply(a, b);
        };
    }

    public static <T> BinaryOperator<T> throwingMerger() {
        return throwingMerger((a, b) -> new IllegalStateException(String.format("Duplicate key %s", a)));
    }

}
