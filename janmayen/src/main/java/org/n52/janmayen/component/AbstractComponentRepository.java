/*
 * Copyright 2015-2022 52°North Initiative for Geospatial Open Source
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

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.Collectors.mapping;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.janmayen.Producer;

import com.google.common.base.MoreObjects;
import com.google.common.base.MoreObjects.ToStringHelper;

/**
 * Abstract class to encapsulate the loading of implementations that are registered with the ServiceLoader interface.
 *
 * @param <K> the component key type
 * @param <C> the component type
 * @param <F> the component factory type
 *
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 * @since 1.0.0
 */
public abstract class AbstractComponentRepository<K, C extends Component<K>, F extends ComponentFactory<K, C>> {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractComponentRepository.class);

    /**
     * Create a multi valued map with {@code Producer}s for the supplied {@code components} and {@code factories}.
     *
     * @param components the component instances
     * @param factories  the component factories
     *
     * @return the producers
     */
    protected Map<K, Set<Producer<C>>> getProviders(Optional<? extends Collection<? extends C>> components,
                                                    Optional<? extends Collection<? extends F>> factories) {
        Collection<? extends C> c = components.isPresent() ? components.get() : Collections.emptyList();
        Collection<? extends F> f = factories.isPresent() ? factories.get() : Collections.emptyList();
        return getProviders(c, f);
    }

    /**
     * Create a multi valued map with {@code Producer}s for the supplied {@code components} and {@code factories}.
     *
     * @param components the component instances (may be {@code null} or empty)
     * @param factories  the component factories (may be {@code null} or empty)
     *
     * @return the producers
     */
    protected Map<K, Set<Producer<C>>> getProviders(Collection<? extends C> components,
                                                    Collection<? extends F> factories) {
        return createProviders(factories, components)
                .collect(groupingBy(KeyedProducer::getKey, mapping(x -> (Producer<C>) x, toSet())));
    }

    private Stream<KeyedProducer<K, C>> createProviders(Collection<? extends F> factories,
                                                        Collection<? extends C> components) {
        return Stream.concat(createProviders(factories, FactoryProducer::new),
                             createProviders(components, InstanceProducer::new));
    }

    private <T extends Keyed<? extends K>> Stream<? extends KeyedProducer<K, C>>
            createProviders(Collection<? extends T> objects,
                            BiFunction<? super K, ? super T, ? extends KeyedProducer<K, C>> creator) {
        Objects.requireNonNull(creator);
        Function<T, Stream<KeyedProducer<K, C>>> mapper = (T t) -> {
            return t.getKeys().stream().map(key -> creator.apply(key, t));
        };
        return Optional.ofNullable(objects)
                .map(Collection::stream)
                .orElseGet(Stream::empty)
                .peek(t -> LOG.trace("Creating provider for {}", t))
                .flatMap(mapper);
    }

    /**
     * Create a map with {@code Producer}s for the supplied {@code components} and {@code factories}. Components or
     * factories with the same keys are discarded.
     *
     * @param components the component instances
     * @param factories  the component factories
     *
     * @return the producers
     */
    protected Map<K, Producer<C>> getUniqueProviders(Optional<? extends Collection<? extends C>> components,
                                                     Optional<? extends Collection<? extends F>> factories) {
        Collection<? extends C> c = components.isPresent() ? components.get() : Collections.emptyList();
        Collection<? extends F> f = factories.isPresent() ? factories.get() : Collections.emptyList();
        return getUniqueProviders(c, f);
    }

    /**
     * Create a map with {@code Producer}s for the supplied {@code components} and {@code factories}. Components or
     * factories with the same keys are discarded.
     *
     * @param components the component instances (may be {@code null} or empty)
     * @param factories  the component factories (may be {@code null} or empty)
     *
     * @return the producers
     */
    protected Map<K, Producer<C>> getUniqueProviders(Collection<? extends C> components,
                                                     Collection<? extends F> factories) {

        Map<K, Producer<C>> uniqueKeyImplementations = new HashMap<>();
        createProviders(factories, components).forEach(provider -> {
            Supplier<C> old = uniqueKeyImplementations.put(provider.getKey(), provider);
            if (old != null) {
                LOG.warn("Duplicate component for key {}: {} vs. {}", provider.getKey(), old, provider);
            }
        });
        return uniqueKeyImplementations;
    }

    /**
     * Abstract class that holds associate a key with the producer.
     *
     * @param <K> the key type
     * @param <C> the component type
     */
    private abstract static class KeyedProducer<K, C extends Component<K>> implements Producer<C> {
        private final K key;

        /**
         * Creates a new {@code KeyedSupplier} for the key.
         *
         * @param key the key
         */
        KeyedProducer(K key) {
            this.key = key;
        }

        /**
         * Gets the key of this provider.
         *
         * @return the key
         */
        public K getKey() {
            return this.key;
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.key);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof KeyedProducer) {
                KeyedProducer<?, ?> that = (KeyedProducer) obj;
                return Objects.equals(this.key, that.getKey());
            }
            return false;
        }

        @Override
        public String toString() {
            return toStringBuilder().toString();
        }

        /**
         * Creates a {@code ToStringHelper} filled with the attributes of this class.
         *
         * @return the {code ToStringHelper}
         */
        protected ToStringHelper toStringBuilder() {
            return MoreObjects.toStringHelper(this).add("key", this.key);
        }
    }

    /**
     * Provider for {@code Component} instances.
     *
     * @param <K> the component key type
     * @param <C> the component type
     */
    private static class InstanceProducer<K, C extends Component<K>> extends KeyedProducer<K, C> {

        private final C component;

        /**
         * Creates a new {@code InstanceProducer}.
         *
         * @param key      the key
         * @param instance the {@code Component}
         */
        InstanceProducer(K key, C instance) {
            super(key);
            this.component = instance;
        }

        /**
         * Gets the {@code Component}.
         *
         * @return the {@code Component}
         */
        @Override
        public C get() {
            return this.component;
        }

        /**
         * Gets the {@code Component}.
         *
         * @return the {@code Component}
         */
        public C getComponent() {
            return this.component;
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), this.component);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof InstanceProducer) {
                InstanceProducer<?, ?> that = (InstanceProducer) obj;
                return super.equals(that) && Objects.equals(this.component, that.getComponent());
            }
            return false;
        }

        @Override
        public String toString() {
            return toStringBuilder().toString();
        }

        @Override
        protected ToStringHelper toStringBuilder() {
            return super.toStringBuilder().add("component", this.component);
        }
    }

    /**
     * Provider for {@code Component} produced by a {@code ComponentFactory}
     *
     * @param <K> the component key type
     * @param <C> the component type
     * @param <F> the component factory type
     */
    private static class FactoryProducer<K, C extends Component<K>, F extends ComponentFactory<K, C>>
            extends KeyedProducer<K, C> {
        private final F factory;

        /**
         * Creates a new {@code FactoryProducer}.
         *
         * @param key     the {@code Component} key
         * @param factory the {@code ComponentFactory}
         */
        FactoryProducer(K key, F factory) {
            super(key);
            this.factory = factory;

        }

        /**
         * Gets or creates the {@code Component}.
         *
         * @return the {@code Component}
         */
        @Override
        public C get() {
            return this.factory.create(getKey());
        }

        /**
         * Gets the {@code ComponentFactory} of this {@code FactoryProvider}.
         *
         * @return the {@code ComponentFactory}
         */
        public F getFactory() {
            return factory;
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), this.factory);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof FactoryProducer) {
                FactoryProducer<?, ?, ?> that = (FactoryProducer) obj;
                return super.equals(that) &&
                       Objects.equals(this.factory, that.getFactory());
            }
            return false;
        }

        @Override
        public String toString() {
            return toStringBuilder()
                    .toString();
        }

        @Override
        protected ToStringHelper toStringBuilder() {
            return super.toStringBuilder()
                    .add("factory", this.factory);
        }

    }
}
