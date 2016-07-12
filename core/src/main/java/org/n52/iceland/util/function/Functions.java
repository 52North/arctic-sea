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
package org.n52.iceland.util.function;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class Functions {
    private Functions() {
    }

    public static <X, T> Function<? super X, T> constant(T t) {
        return (x) -> t;
    }

    public static <T, R> Supplier<R> bind(Function<? super T, ? extends R> function, T t) {
        return () -> function.apply(t);
    }

    public static <T1, T2, R> Function<T2, R> bind1(BiFunction<T1, T2, R> bifunction, T1 t1) {
        return (t2) -> bifunction.apply(t1, t2);
    }

    public static <T1, T2, R> Function<T1, R> bind2(BiFunction<T1, T2, R> bifunction, T2 t2) {
        return (t1) -> bifunction.apply(t1, t2);
    }

}
