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

import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.iceland.util.Producer;

import com.google.common.collect.Maps;
import com.google.common.collect.SetMultimap;

public abstract class AbstractUniqueKeyComponentRepository<K, C extends Component<K>, F extends ComponentFactory<K, C>>
        extends AbstractComponentRepository<K, C, F> {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractUniqueKeyComponentRepository.class);

    public AbstractUniqueKeyComponentRepository(Class<?> componentClass,
                                                Class<?> factoryClass) {
        super(componentClass, factoryClass);
    }

    @Override
    protected void processImplementations(SetMultimap<K, Producer<C>> implementations) {

        Map<K, Producer<C>> uniqueKeyImplementations = Maps.newHashMap();
        for (Entry<K, Producer<C>> entry : implementations.entries()) {
            K key = entry.getKey();
            Producer<C> value = entry.getValue();
            Producer<C> old = uniqueKeyImplementations.put(key, value);
            if (old != null) {
                LOG.warn("Duplicate component for key {}: {} vs. {}", key, old, value);
            }
        }
        processImplementations(uniqueKeyImplementations);
    }

    protected abstract void processImplementations(Map<K, Producer<C>> implementations);

}
