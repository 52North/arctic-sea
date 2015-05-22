/*
 * Copyright 2015 52°North Initiative for Geospatial Open Source
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
 * TODO JavaDoc
 *
 * @param <K> the component key
 * @param <C> the component type
 *
 * @author Christian Autermann
 */
public abstract class SingleTypeComponentFactory<K, C extends Component<K>>
        implements ComponentFactory<K, C> {

    private final K key;

    public SingleTypeComponentFactory(K key) {
        this.key = key;
    }

    @Override
    public C create(K key) {
        if (has(key)) {
            return create();
        }
        throw new IllegalArgumentException("Key " + key + " not supported");
    }

    @Override
    public Set<C> createAll() {
        return Collections.singleton(create());
    }

    @Override
    public Set<K> getKeys() {
        return Collections.singleton(this.key);
    }

    @Override
    public boolean has(K key) {
        return key != null && key.equals(this.key);
    }

    protected abstract C create();
}
