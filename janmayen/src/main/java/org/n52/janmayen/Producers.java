/*
 * Copyright 2015-${currentYear} 52°North Spatial Information Research GmbH
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

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.base.Function;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Maps;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public final class Producers {

    private Producers() {
    }

    public static <T> T produce(Producer<T> producer) {
        return producer == null ? null : producer.get();
    }

    public static <K, T> Map<K, T> produce(Map<K, Producer<T>> map) {
        return Maps.transformValues(map, Producer::get);
    }

    public static <T> Collection<T> produce(Collection<Producer<T>> list) {
        return list.stream().map(Producers::produce).collect(Collectors.toList());
    }

    public static <T> List<T> produce(List<Producer<T>> list) {
        return list.stream().map(Producers::produce).collect(Collectors.toList());
    }

    public static <T> Set<T> produce(Set<Producer<T>> set) {
        return set.stream().map(Producers::produce).collect(Collectors.toSet());
    }

    public static <T> Producer<T> forInstance(T instance) {
        return new InstanceProducer<>(instance);
    }

    private static class ProducingFunction<T> implements
            Function<Producer<T>, T> {
        private static final ProducingFunction<?> INSTANCE
                = new ProducingFunction<>();

        @Override
        public T apply(Producer<T> t) {
            return produce(t);
        }

        public static ProducingFunction<?> getInstance() {
            return INSTANCE;
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
