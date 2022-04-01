/*
 * Copyright 2015-2022 52°North Initiative for Geospatial Open Source
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
package org.n52.janmayen.component;

import static java.util.stream.Collectors.toSet;

import java.util.Set;

/**
 * Interface for {@link Component} factories.
 *
 * @param <C> the component type
 * @param <K> the key type
 *
 * @since 1.0.0
 *
 * @author Christian Autermann
 */
public interface ComponentFactory<K, C extends Component<K>> extends Keyed<K> {

    /**
     * Checks if this factory supports the supplied {@code key}.
     *
     * @param key the key
     *
     * @return if this factory supports the key.
     */
    default boolean has(K key) {
        return getKeys().contains(key);
    }

    /**
     * Creates all {@link Component}s supported by this factory.
     *
     * @return the components
     */
    default Set<C> createAll() {
        return getKeys().stream().map(this::create).collect(toSet());
    }

    /**
     * Get all keys that are supported by this factory.
     *
     * @return the keys
     */
    @Override
    Set<K> getKeys();

    /**
     * Creates the {@link Component} associated with the supplied {@code key}.
     * Whether this method will always return the same instance or a fresh
     * instance for each call is implementation dependent.
     *
     * @param key the key
     *
     * @return the {@code Component}
     */
    C create(K key);
}
