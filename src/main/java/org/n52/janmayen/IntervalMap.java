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

import java.util.Optional;
import java.util.Set;

/**
 * A map that uses intervals (specified as an upper and lower bound) as keys.
 *
 * @author Christian Autermann
 * @param <K> the key type
 * @param <V> the value type
 */
public interface IntervalMap<K, V> {

    /**
     * Get the value of the first interval matching the supplied key.
     *
     * @param key the key
     *
     * @return the value
     */
    default Optional<V> get(K key) {
        return get(key, key);
    }

    /**
     * Get the first value falling into the interval.
     *
     * @param lower the lower bound
     * @param upper the upper bound
     *
     * @return the value
     */
    Optional<V> get(K lower, K upper);

    /**
     * Get the first value for the specified key or the supplied default.
     *
     * @param key   the key
     * @param value the default value
     *
     * @return the value
     */
    default V getOrDefault(K key, V value) {
        return get(key).orElse(value);
    }

    /**
     * Get the first value falling into the interval or the supplied default.
     *
     * @param lower the lower bound
     * @param upper the upper bound
     * @param value the default value
     *
     * @return the value
     */
    default V getOrDefault(K lower, K upper, V value) {
        return get(lower, upper).orElse(value);
    }

    /**
     * Get the values for all intervals matching the supplied key.
     *
     * @param key the key
     *
     * @return the values
     */
    default Set<V> search(K key) {
        return search(key, key);
    }

    /**
     * Get all values falling into the specified interval.
     *
     * @param lower the upper bound
     * @param upper the lower bound
     *
     * @return the values
     */
    Set<V> search(K lower, K upper);

}
