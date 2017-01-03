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
package org.n52.janmayen.component;

import java.util.Set;

/**
 * Generic interface for repositories containing {@link Component}s.
 *
 * @param <K> the component key type
 * @param <C> the component type
 *
 * @since 1.0.0
 *
 * @author Christian Autermann
 */
public interface ComponentRepository<K, C extends Component<K>> {

    /**
     * Get all {@link Component}s of this repository.
     *
     * @return the components
     */
    Set<C> getAll();

    /**
     * Gets the {@link Component} associated with {@code key} if it exists.
     *
     * @param key the key
     *
     * @return the component or {@code null}
     */
    C get(K key);
}
