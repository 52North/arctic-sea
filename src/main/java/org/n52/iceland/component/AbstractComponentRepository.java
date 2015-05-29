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


import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.Set;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;

import org.n52.iceland.config.SettingsManager;
import org.n52.iceland.exception.ConfigurationException;
import org.n52.iceland.lifecycle.Constructable;
import org.n52.iceland.util.Producer;
import org.n52.iceland.util.Producers;

import com.google.common.base.MoreObjects;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Sets;

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
public abstract class AbstractComponentRepository<K, C extends Component<K>, F extends ComponentFactory<K, C>>
        implements Constructable {
    private static final Logger LOG = LoggerFactory
            .getLogger(AbstractComponentRepository.class);

    private final Class<?> componentClass;
    private final Class<?> factoryClass;
    private CompositeLoaderStrategy loader;
    private ApplicationContext applicationContext;

    @Inject
    private Collection<C> components;

    @Inject
    private Collection<F> factories;

    protected AbstractComponentRepository(Class<?> componentClass,
                                          Class<?> factoryClass) {
        this.componentClass = componentClass;
        this.factoryClass = factoryClass;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void init() {
        this.loader = new CompositeLoaderStrategy(
                this.factoryClass, this.componentClass,
                createSpringLoader(), createServiceLoader());

        LOG.debug("Loading Implementations for {}", componentClass);
        load();
        LOG.debug("Implementations for {} loaded succesfull!", this.componentClass);
    }

    private ServiceLoaderComponentLoaderStrategy createServiceLoader() {
        return new ServiceLoaderComponentLoaderStrategy(this.factoryClass,
                                                        this.componentClass,
                                                        this.applicationContext);
    }

    private SpringComponentLoaderStrategy createSpringLoader() {
        return new SpringComponentLoaderStrategy(this.factoryClass,
                                                 this.componentClass);
    }

    public void update() {
        LOG.debug("Reloading Implementations for {}", this.componentClass);
        load();
        LOG.debug("Implementations for {} reloaded succesfull!", this.componentClass);
    }

    protected void load() {
        SetMultimap<K, Producer<C>> providers = HashMultimap.create();
        for (F factory : findComponentFactories()) {
            for (K key : factory.getKeys()) {
                Producer<C> provider = new FactoryProvider(factory, key);
                providers.put(key, provider);
            }
        }
        for (C component : findComponents()) {
            Producer<C> provider = Producers.forInstance(component);
            for (K key : component.getKeys()) {
                providers.put(key, provider);
            }
        }
        processImplementations(providers);
    }

    protected Set<C> findComponents() {
        return this.loader.findComponents();
    }

    protected Set<F> findComponentFactories() {
        return this.loader.findComponentFactories();
    }

    @Deprecated
    protected Map<K, C> produce(Map<K, Producer<C>> map) {
        return Producers.produce(map);
    }

    @Deprecated
    protected List<C> produce(List<Producer<C>> list) {
        return Producers.produce(list);
    }

    protected abstract void processImplementations(SetMultimap<K, Producer<C>> implementations);

    private class CompositeLoaderStrategy extends ComponentLoaderStrategy {
        private final Iterable<ComponentLoaderStrategy> strategies;

        CompositeLoaderStrategy(Class<?> factoryClass,
                                Class<?> componentClass,
                                ComponentLoaderStrategy... strategies) {
            this(factoryClass, componentClass,
                 Arrays.asList(strategies));
        }

        CompositeLoaderStrategy(Class<?> factoryClass,
                                Class<?> componentClass,
                                Iterable<ComponentLoaderStrategy> strategies) {
            super(factoryClass, componentClass);
            this.strategies = strategies;
        }

        @Override
        public Set<C> findComponents() {
            Set<C> set = Sets.newHashSet();
            for (ComponentLoaderStrategy strategy : strategies) {
                set.addAll(strategy.findComponents());
            }
            return set;
        }

        @Override
        public Set<F> findComponentFactories() {
            Set<F> set = Sets.newHashSet();
            for (ComponentLoaderStrategy strategy : strategies) {
                set.addAll(strategy.findComponentFactories());
            }
            return set;
        }
    }

    /**
     * Strategy to load implementations from the supplied
     * {@link ApplicationContext}. Instances are <strong>not</strong>
     * configured by any {@link SettingsManager}. We assume the
     * component/factory will do it them self.
     */
    private class SpringComponentLoaderStrategy extends ComponentLoaderStrategy {

        SpringComponentLoaderStrategy(Class<?> factoryClass,
                                      Class<?> componentClass) {
            super(factoryClass, componentClass);
        }

        @Override
        @SuppressWarnings("unchecked")
        public Set<C> findComponents() {
            return new HashSet<>(AbstractComponentRepository.this.components);
        }

        @Override
        @SuppressWarnings("unchecked")
        public Set<F> findComponentFactories() {
            return new HashSet<>(AbstractComponentRepository.this.factories);
        }

    }

    /**
     * Strategy to load implementations using {@link ServiceLoader}.
     * The instantiated instances are configured using the supplied
     * {@link SettingsManager}.
     */
    private class ServiceLoaderComponentLoaderStrategy
            extends ConfiguringComponentLoaderStrategy {

        ServiceLoaderComponentLoaderStrategy(Class<?> factoryClass,
                                             Class<?> componentClass,
                                             ApplicationContext ctx) {
            super(factoryClass, componentClass, ctx);
        }

        @Override
        @SuppressWarnings("unchecked")
        public Set<C> findComponents() {
            Set<C> set = Sets.newHashSet();
            for (Object t : ServiceLoader.load(getComponentClass())) {
                C c = (C) t;
                configure(c);
                set.add(c);
            }
            return set;
        }

        @Override
        @SuppressWarnings("unchecked")
        public Set<F> findComponentFactories() {
            Set<F> set = Sets.newHashSet();
            for (Object t : ServiceLoader.load(getFactoryClass())) {
                F f = (F) t;
                configure(f);
                set.add(f);
            }
            return set;
        }
    }

    private abstract class ConfiguringComponentLoaderStrategy
            extends ComponentLoaderStrategy {
        private final SettingsManager settingsManager;
        private final AutowireCapableBeanFactory autowireCapableBeanFactory;

        ConfiguringComponentLoaderStrategy(Class<?> factoryClass,
                                           Class<?> componentClass,
                                           ApplicationContext ctx) {
            super(factoryClass, componentClass);
            this.settingsManager = ctx.getBean(SettingsManager.class);
            this.autowireCapableBeanFactory = ctx.getAutowireCapableBeanFactory();
        }

        protected <T> void configure(T t)
                throws ConfigurationException, BeansException {
            //FIXME this may cause a double configuration
            getSettingsManager().configure(t);
            getAutowireCapableBeanFactory().autowireBeanProperties(
                    t, AutowireCapableBeanFactory.AUTOWIRE_NO, true);
        }

        public AutowireCapableBeanFactory getAutowireCapableBeanFactory() {
            return autowireCapableBeanFactory;
        }

        public SettingsManager getSettingsManager() {
            return settingsManager;
        }
    }

    private abstract class ComponentLoaderStrategy {

        private final Class<?> factoryClass;
        private final Class<?> componentClass;

        ComponentLoaderStrategy(Class<?> factoryClass,
                                Class<?> componentClass) {
            this.factoryClass = Objects.requireNonNull(factoryClass);
            this.componentClass = Objects.requireNonNull(componentClass);
        }

        protected Class<?> getComponentClass() {
            return this.componentClass;
        }

        protected Class<?> getFactoryClass() {
            return this.factoryClass;
        }

        public abstract Set<C> findComponents();

        public abstract Set<F> findComponentFactories();

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
