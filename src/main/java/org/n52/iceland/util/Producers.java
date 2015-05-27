package org.n52.iceland.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.base.Function;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
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
        Map<K, T> result = new HashMap<>(map.size());
        for (Entry<K, Producer<T>> entry : map.entrySet()) {
            result.put(entry.getKey(), entry.getValue().get());
        }
        return result;
    }

    public static <T> List<T> produce(Collection<Producer<T>> list) {
        List<T> result = Lists.newArrayListWithExpectedSize(list.size());
        for (Producer<T> producer : list) {
            result.add(producer.get());
        }
        return result;
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
