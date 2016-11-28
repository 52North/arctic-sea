/*
 * Copyright 2016 52°North Initiative for Geospatial Open Source
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
package org.n52.janmayen.similar;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.function.Function;

import org.n52.janmayen.component.Keyed;

/**
 * TODO JavaDoc
 *
 * @param <T>
 *            the type to compare
 * @param <K>
 *            the similarity type of {@code T}
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
public abstract class ProxySimilarityComparator<T, K extends Similar<K>>
        implements Comparator<T> {

    private final Comparator<T> comparator;

    public ProxySimilarityComparator(K ref) {
        this.comparator = createComparator(ref);
    }

    private Comparator<T> createComparator(K ref) {
        Comparator<K> keyComparator = new SimilarityComparator<>(ref);
        return Comparator.comparing(min(this::getSimilars, keyComparator), keyComparator)
                .thenComparing(Object::getClass, classComparator());
    }

    @Override
    public int compare(T o1, T o2) {
        return this.comparator.compare(o1, o2);
    }

    protected abstract Collection<K> getSimilars(T t);

    private Function<T, K> min(Function<T, Collection<K>> func, Comparator<K> k) {
        return (T x) -> Collections.min(func.apply(x), k);
    }

    private static Comparator<Class<?>> classComparator() {
        return (a, b) -> a.isAssignableFrom(b) ? 1 : b .isAssignableFrom(a) ? -1 : 0;
    }

    public static <C extends Keyed<K>, K extends Similar<K>> Comparator<C> create(K ref) {
        return new ProxySimilarityComparator<C, K>(ref) {
            @Override protected Collection<K> getSimilars(C t) {
                return t.getKeys();
            }
        };
    }

    public static <T, K extends Similar<K>> Comparator<T> create(
            K ref, Function<T, Collection<K>> similars) {
        return new ProxySimilarityComparator<T, K>(ref) {
            @Override protected Collection<K> getSimilars(T t) {
                return similars.apply(t);
            }
        };
    }
}
