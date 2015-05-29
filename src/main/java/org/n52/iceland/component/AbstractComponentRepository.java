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


import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.iceland.util.Producer;
import org.n52.iceland.util.Producers;

import com.google.common.base.MoreObjects;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.SetMultimap;

/**
 *
 * Abstract class to encapsulate the loading of implementations that are
 * registered with the ServiceLoader interface.
 *
 * @param <K> the component key type
 * @param <C> the component type
 * @param <F> the component factory type
 *
 *
 * @author Christian Autermann <c.autermann@52north.org>
 * @since 4.0.0
 */
public abstract class AbstractComponentRepository<K, C extends Component<K>, F extends ComponentFactory<K, C>> {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractComponentRepository.class);
    
    protected SetMultimap<K, Producer<C>> getProviders(Collection<C> components, Collection<F> factories) {
        SetMultimap<K, Producer<C>> providers = HashMultimap.create();
        for (F factory : factories) {
            for (K key : factory.getKeys()) {
                Producer<C> provider = new FactoryProvider(factory, key);
                providers.put(key, provider);
            }
        }
        for (C component : components) {
            Producer<C> provider = Producers.forInstance(component);
            for (K key : component.getKeys()) {
                providers.put(key, provider);
            }
        }
        return providers;
    }

    protected Map<K, Producer<C>> getUniqueProviders(Collection<C> components, Collection<F> factories) {
        Map<K, Producer<C>> uniqueKeyImplementations = Maps.newHashMap();
        for (Entry<K, Producer<C>> entry : getProviders(components, factories).entries()) {
            K key = entry.getKey();
            Producer<C> value = entry.getValue();
            Producer<C> old = uniqueKeyImplementations.put(key, value);
            if (old != null) {
                LOG.warn("Duplicate component for key {}: {} vs. {}", key, old, value);
            }
        }
        return uniqueKeyImplementations;
    }

    private class FactoryProvider implements Producer<C> {
        private final F factory;
        private final K key;

        FactoryProvider(F factory, K key) {
            this.factory = factory;
            this.key = key;
        }

        @Override
        public C get() {
            return this.factory.create(this.key);
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("factory", this.factory)
                    .add("key", this.key)
                    .toString();
        }

    }
}
