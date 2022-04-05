/*
 * Copyright 2015-2022 52°North Spatial Information Research GmbH
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

import static java.util.stream.Collectors.toSet;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.janmayen.Producer;
import org.n52.janmayen.similar.CompositeSimilar;
import org.n52.janmayen.similar.ProxySimilarityComparator;
import org.n52.janmayen.similar.Similar;
import org.n52.janmayen.stream.MoreCollectors;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Component repository for keys that can be similar and support for composite keys.
 *
 * @param <K> the key type
 * @param <C> the component type
 * @param <F> the factory type
 *
 * @author Christian Autermann
 */
public abstract class AbstractSimilarityKeyComponentRepository<
        K extends Similar<K>, C extends Component<K>, F extends ComponentFactory<K, C>>
        extends AbstractComponentRepository<K, C, F> {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractSimilarityKeyComponentRepository.class);
    private final Set<Producer<C>> components = Sets.newHashSet();
    private final Map<K, Set<Producer<C>>> componentsByKey = new HashMap<>();

    public Set<Producer<C>> getComponentProviders() {
        return Collections.unmodifiableSet(this.components);
    }

    public Map<K, Set<Producer<C>>> getComponentProvidersByKey() {
        return Collections.unmodifiableMap(componentsByKey);
    }

    protected void setProducers(Map<K, Set<Producer<C>>> implementations) {
        this.componentsByKey.clear();
        this.componentsByKey.putAll(implementations);
        this.components.clear();
        implementations.values().stream().forEach(this.components::addAll);
    }

    private Optional<C> choose(Set<C> matches, K key) {
        if (matches == null || matches.isEmpty()) {
            LOG.debug("No implementation for {}", key);
            return Optional.empty();
        } else if (matches.size() > 1) {
            C component = Collections.min(matches, new ComponentSimilarityComparator<>(key));
            LOG.debug("Requested ambiguous implementations for {}: Found {}; Choosing {}.", key,
                      Joiner.on(", ").join(matches), component);
            return Optional.of(component);
        } else {
            return Optional.of(matches.iterator().next());
        }
    }

    private Set<C> findComponentForSingleKey(K key) {
        return this.componentsByKey.computeIfAbsent(key, k1 -> findProducersForSingleKey(k1))
                .stream().map(Supplier::get).collect(toSet());
    }

    private Set<Producer<C>> findProducersForSingleKey(K key) {
        return this.components.stream().filter(supplier -> matches(key, supplier))
                .collect(MoreCollectors.toUnmodifiableSet());
    }

    private Set<C> findComponentsForCompositeKey(CompositeKey ck) {
        return this.componentsByKey.computeIfAbsent(ck.asKey(), k1 -> findProducersForCompositeKey(ck))
                .stream().map(Supplier::get).collect(toSet());
    }

    private Set<Producer<C>> findProducersForCompositeKey(CompositeKey ck) {
        return this.components.stream().filter(supplier -> matches(ck, supplier))
                .collect(MoreCollectors.toUnmodifiableSet());
    }

    private boolean matches(K key, Producer<C> supplier) {
        Set<K> keys = supplier.get().getKeys();
        return keys.stream().anyMatch(k -> k.getSimilarity(key) >= 0);
    }

    private boolean matches(CompositeKey key, Producer<C> supplier) {
        Set<K> keys = supplier.get().getKeys();
        return key.matches(keys);
    }

    private Optional<C> getComponentForSingleKey(K key) {
        return choose(findComponentForSingleKey(key), key);
    }

    private Optional<C> getComponentForCompositeKey(CompositeKey key) {
        return choose(findComponentsForCompositeKey(key), key.asKey());
    }

    protected Set<C> getComponents() {
        return this.components.stream().map(Supplier::get).collect(toSet());
    }

    @SafeVarargs
    @SuppressWarnings("varargs")
    protected final boolean hasComponent(K key, K... keys) {
        return tryGetComponent(key, keys).isPresent();
    }

    @SafeVarargs
    @SuppressWarnings("varargs")
    protected final C getComponent(K key, K... keys) {
        return tryGetComponent(key, keys).orElse(null);
    }

    @SafeVarargs
    @SuppressWarnings("varargs")
    protected final Optional<C> tryGetComponent(K key, K... keys) {
        if (keys.length == 0) {
            return getComponentForSingleKey(key);
        } else {
            return getComponentForCompositeKey(createCompositeKey(asList(key, keys)));
        }

    }

    protected abstract CompositeKey createCompositeKey(List<K> keys);

    private List<K> asList(K key, K[] keys) {
        return ImmutableList.<K>builder().add(key).add(keys).build();
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
