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
package org.n52.iceland.coding.encode;


import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import org.n52.iceland.component.AbstractComponentRepository;
import org.n52.iceland.lifecycle.Constructable;
import org.n52.iceland.util.ClassHelper;
import org.n52.iceland.util.CollectionHelper;
import org.n52.iceland.util.Comparables;
import org.n52.iceland.util.Producer;

import com.google.common.collect.Sets;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <c.autermann@52north.org>
 * @author CarstenHollmann <c.hollmann@52north.org>
 *
 * @since 4.0.0
 */
public class ResponseWriterRepository extends AbstractComponentRepository<ResponseWriterKey, ResponseWriter<?>, ResponseWriterFactory> implements Constructable {

    private static ResponseWriterRepository instance;

    private final Map<ResponseWriterKey, Producer<ResponseWriter<?>>> writersByClass = CollectionHelper.synchronizedMap();

    @Autowired(required = false)
    private Collection<ResponseWriter<?>> components;

    @Autowired(required = false)
    private Collection<ResponseWriterFactory> componentFactories;

    @Override
    public void init() {
        ResponseWriterRepository.instance = this;
        Map<ResponseWriterKey, Producer<ResponseWriter<?>>> implementations
                = getUniqueProviders(this.components, this.componentFactories);
        this.writersByClass.clear();
        this.writersByClass.putAll(implementations);
    }

    @SuppressWarnings("unchecked")
    public <T> ResponseWriter<T> getWriter(final Class<? extends T> clazz) {
        ResponseWriterKey key = new ResponseWriterKey(clazz);
        if (!writersByClass.containsKey(key)) {
            Set<Class<?>> compatible = Sets.newHashSet();
            for (ResponseWriterKey c : writersByClass.keySet()) {
                if (ClassHelper.getSimiliarity(c.getType(), clazz) >= 0) {
                    compatible.add(c.getType());
                }
            }
            writersByClass.put(key, writersByClass.get(chooseWriter(compatible, clazz)));
        }
        return (ResponseWriter<T>) writersByClass.get(key).get();
    }

	private  ResponseWriterKey chooseWriter(Set<Class<?>> compatible, Class<?> clazz) {
        if (compatible.isEmpty()) {
            return null;
        }
        Comparator<Class<?>> comparator = new ClassSimilarityComparator(clazz);
        return new ResponseWriterKey(Collections.min(compatible, comparator));
    }

    @Deprecated
    public static ResponseWriterRepository getInstance() {
        return ResponseWriterRepository.instance;
    }

    private class ClassSimilarityComparator implements Comparator<Class<?>> {
        private final Class<?> reference;

        ClassSimilarityComparator(Class<?> reference) {
            this.reference = reference;
        }

        @Override
        public int compare(Class<?> c1, Class<?> c2) {
            return Comparables.compare(ClassHelper.getSimiliarity(c1, reference),
                                       ClassHelper.getSimiliarity(c2, reference));
        }
    }
}
