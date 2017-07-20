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
package org.n52.janmayen;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * {@link IntervalMap} implementation that returns the same value for each key.
 *
 * @param <K> the key type
 * @param <V> the value type
 *
 * @author Christian Autermann
 */
public final class UniversalIntervalMap<K, V> implements IntervalMap<K, V> {
    private final V value;

    private UniversalIntervalMap(V value) {
        this.value = Objects.requireNonNull(value);
    }

    @Override
    public Optional<V> get(K lower, K upper) {
        return Optional.of(this.value);
    }

    @Override
    public Set<V> search(K lower, K upper) {
        return Collections.singleton(this.value);
    }

    /**
     * Creates a new {@link UniversalIntervalMap}.
     *
     * @param <K>   the key type
     * @param <V>   the value type
     * @param value the value
     *
     * @return the {@link IntervalMap}
     */
    public static <K, V> IntervalMap<K, V> of(V value) {
        return new UniversalIntervalMap<>(value);
    }
}
