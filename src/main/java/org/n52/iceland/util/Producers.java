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
package org.n52.iceland.util;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Function;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class Producers {

    private Producers() {
    }

    public static <K, T> Map<K, T> produce(Map<K, Producer<T>> map) {
        return Maps.transformValues(map, Producers.<T>producingFunction());
    }

    public static <T> Collection<T> produce(Collection<Producer<T>> list) {
        return Collections2.transform(list, Producers.<T>producingFunction());
    }

    public static <T> List<T> produce(List<Producer<T>> list) {
        return Lists.transform(list, Producers.<T>producingFunction());
    }

    public static <T> Set<T> produce(Set<Producer<T>> set) {
        Set<T> result = Sets.newHashSetWithExpectedSize(set.size());
        for (Producer<T> producer : set) {
            result.add(producer.get());
        }
        return result;
    }

    public static <T> Producer<T> forInstance(T instance) {
        return new InstanceProducer<>(instance);
    }

    @SuppressWarnings("unchecked")
    public static <T> ProducingFunction<T> producingFunction() {
        return (ProducingFunction<T>) ProducingFunction.getInstance();
    }

    private static class ProducingFunction<T> implements
            Function<Producer<T>, T> {
        private static final ProducingFunction<?> instance
                = new ProducingFunction<>();

        @Override
        public T apply(Producer<T> t) {
            return t.get();
        }

        public static ProducingFunction<?> getInstance() {
            return instance;
        }

    }

    private static class InstanceProducer<T> implements Producer<T> {
        private final T instance;

        InstanceProducer(T instance) {
            this.instance = instance;
        }

        @Override
        public T get() {
            return this.instance;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("instance", this.instance)
                    .toString();
        }

        public static <T> Producer<T> of(T instance) {
            return new InstanceProducer<>(instance);
        }
    }

}
