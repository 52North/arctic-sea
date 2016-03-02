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
package org.n52.iceland.component;

import java.util.Collections;
import java.util.Set;

/**
 * Convenience interface for a {@link ComponentFactory} that supports only a
 * single key.
 *
 * @param <K> the component key
 * @param <C> the component type
 *
 * @since 1.0.0
 *
 * @author Christian Autermann
 */
public interface SingleTypeComponentFactory<K, C extends Component<K>>
        extends ComponentFactory<K, C> {

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException if {@code key} does not equal the
     *                                  {@linkplain #getKey() key of this factory}.
     */
    @Override
    default C create(K key) {
        if (!has(key)) {
            throw new IllegalArgumentException("Key " + key + " not supported");
        }
        return create();
    }

    /**
     * Creates a singleton set of the {@link Component} supported by this
     * factory.
     *
     * @return the component
     */
    @Override
    default Set<C> createAll() {
        return Collections.singleton(create());
    }

    /**
     * Creates a singleton set of the key supported by this factory.
     *
     * @return the key
     */
    @Override
    default Set<K> getKeys() {
        return Collections.singleton(getKey());
    }

    /**
     * Checks if the {@code key} is the key supported by this factory.
     *
     * @param key the key
     *
     * @return if the {@code key} is the key supported
     */
    @Override
    default boolean has(K key) {
        return key != null && key.equals(getKey());
    }

    /**
     * Gets the single key supported by this factory.
     *
     * @return the key
     */
    K getKey();

    /**
     * Creates the single {@link Component} supported by this factory. Whether
     * this method will always return the same instance or a fresh instance for
     * each call is implementation dependent.
     *
     * @return the component
     */
    C create();
}
