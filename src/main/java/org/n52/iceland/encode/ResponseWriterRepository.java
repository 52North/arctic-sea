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
package org.n52.iceland.encode;


import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;

import org.n52.iceland.util.ClassHelper;
import org.n52.iceland.util.CollectionHelper;
import org.n52.iceland.util.Comparables;
import org.n52.iceland.util.Producer;
import org.n52.iceland.component.AbstractUniqueKeyComponentRepository;

import com.google.common.collect.Sets;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <c.autermann@52north.org>
 * @author CarstenHollmann <c.hollmann@52north.org>
 *
 * @since 4.0.0
 */
public class ResponseWriterRepository extends AbstractUniqueKeyComponentRepository<ResponseWriterKey, ResponseWriter<?>, ResponseWriterFactory> {

    private static ResponseWriterRepository instance;

    private final Map<ResponseWriterKey, Producer<ResponseWriter<?>>> writersByClass = CollectionHelper.synchronizedMap();


    public ResponseWriterRepository() {
        super(ResponseWriter.class, ResponseWriterFactory.class);
        ResponseWriterRepository.instance = this;
    }

    public static ResponseWriterRepository getInstance() {
        return ResponseWriterRepository.instance;
    }

    @Override
    protected void processImplementations(Map<ResponseWriterKey, Producer<ResponseWriter<?>>> implementations) {
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

    private ResponseWriterKey chooseWriter(Set<Class<?>> compatible, Class<?> clazz) {
        if (compatible.isEmpty()) {
            return null;
        }
        Comparator<Class<?>> comparator = new ClassSimilarityComparator(clazz);
        return new ResponseWriterKey(Collections.min(compatible, comparator));
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
