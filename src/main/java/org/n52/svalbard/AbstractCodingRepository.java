/*
 * Copyright 2016-2017 52°North Initiative for Geospatial Open Source
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
package org.n52.svalbard;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.n52.janmayen.Producer;
import org.n52.janmayen.Producers;
import org.n52.janmayen.component.AbstractComponentRepository;
import org.n52.janmayen.component.Component;
import org.n52.janmayen.component.ComponentFactory;
import org.n52.janmayen.similar.CompositeSimilar;
import org.n52.janmayen.similar.ProxySimilarityComparator;
import org.n52.janmayen.similar.Similar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multimaps;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Sets;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 * @param <K>
 *            the key type
 * @param <C>
 *            the component type
 * @param <F>
 *            the factory type
 */
@SuppressWarnings("checkstyle:linelength")
public abstract class AbstractCodingRepository<K extends Similar<K>, C extends Component<K>, F extends ComponentFactory<K, C>>
        extends AbstractComponentRepository<K, C, F> {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractCodingRepository.class);

    private final Set<Producer<C>> components = Sets.newHashSet();

    private final SetMultimap<K, Producer<C>> componentsByKey = HashMultimap.create();

    public Set<Producer<C>> getComponentProviders() {
        return Collections.unmodifiableSet(this.components);
    }

    public SetMultimap<K, Producer<C>> getComponentProvidersByKey() {
        return Multimaps.unmodifiableSetMultimap(componentsByKey);
    }

    protected void setProducers(SetMultimap<K, Producer<C>> implementations) {
        this.componentsByKey.clear();
        this.componentsByKey.putAll(implementations);
        this.components.clear();
        this.components.addAll(implementations.values());
    }

    protected C choose(Set<C> matches, K key) {
        if (matches == null || matches.isEmpty()) {
            LOG.debug("No implementation for {}", key);
            return null;
        } else if (matches.size() > 1) {
            return chooseFrom(matches, key);
        } else {
            return Iterables.getFirst(matches, null);
        }
    }

    private C chooseFrom(Set<C> matches, K key) {
        ComponentSimilarityComparator<K, C> comparator = new ComponentSimilarityComparator<>(key);
        C component = Collections.min(matches, comparator);
        LOG.debug("Requested ambiguous implementations for {}: Found {}; Choosing {}.", key,
                Joiner.on(", ").join(matches), component);
        return component;
    }

    protected Set<C> findComponentForSingleKey(K key) {
        if (!this.componentsByKey.containsKey(key)) {
            Set<C> instances = Sets.newHashSet();
            for (Producer<C> producer : this.components) {
                C component = producer.get();
                for (K ckey : component.getKeys()) {
                    if (ckey.getSimilarity(key) >= 0) {
                        this.componentsByKey.put(key, producer);
                        instances.add(component);
                    }
                }
            }
            return instances;
        } else {
            return Producers.produce(componentsByKey.get(key));
        }
    }

    protected Set<C> findComponentsForCompositeKey(CompositeKey ck) {
        K key = ck.asKey();
        if (!this.componentsByKey.containsKey(key)) {
            Set<C> instances = Sets.newHashSet();
            for (Producer<C> producer : this.components) {
                C component = producer.get();
                if (ck.matches(component.getKeys())) {
                    this.componentsByKey.put(key, producer);
                    instances.add(component);
                }
            }
            LOG.debug("Found {} components for composite key: {}", instances.size(), Joiner.on(", ").join(instances));
            return instances;
        } else {
            return Producers.produce(componentsByKey.get(key));
        }
    }

    protected Set<C> getComponents() {
        return Producers.produce(this.components);
    }

    @SafeVarargs
    @SuppressWarnings("varargs")
    protected final boolean hasComponent(K key, K... keys) {
        return getComponent(key, keys) != null;
    }

    protected C getComponentForSingleKey(K key) {
        return choose(findComponentForSingleKey(key), key);
    }

    protected C getComponentForCompositeKey(CompositeKey key) {
        return choose(findComponentsForCompositeKey(key), key.asKey());
    }

    @SafeVarargs
    @SuppressWarnings("varargs")
    protected final C getComponent(K key, K... keys) {
        if (keys.length == 0) {
            return getComponentForSingleKey(key);
        } else {
            return getComponentForCompositeKey(createCompositeKey(asList(key, keys)));
        }
    }

    protected abstract CompositeKey createCompositeKey(List<K> keys);

    private List<K> asList(K key, K[] keys) {
        return ImmutableList.<K> builder().add(key).add(keys).build();
    }

    @SuppressFBWarnings("SE_COMPARATOR_SHOULD_BE_SERIALIZABLE")
    private static class ComponentSimilarityComparator<K extends Similar<K>, C extends Component<K>>
            extends ProxySimilarityComparator<C, K> {

        ComponentSimilarityComparator(K key) {
            super(key);
        }

        @Override
        protected Collection<K> getSimilars(C t) {
            return t.getKeys();
        }

    }

    protected abstract class CompositeKey extends CompositeSimilar<K> {

        protected CompositeKey(Iterable<K> keys) {
            super(keys);
        }

        public abstract K asKey();

    }

}
