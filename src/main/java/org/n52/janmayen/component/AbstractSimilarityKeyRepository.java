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
package org.n52.janmayen.component;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toSet;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import javax.inject.Provider;

import org.n52.janmayen.similar.ProxySimilarityComparator;
import org.n52.janmayen.similar.Similar;

import com.google.common.collect.Maps;

/**
 * @author Christian Autermann
 * @param <K> the key type
 * @param <C> the component type
 */
// TODO move to iceland
public abstract class AbstractSimilarityKeyRepository<K extends Similar<K>, C extends Component<K>> {

    private Set<Provider<C>> components = Collections.emptySet();
    private Map<K, Set<Provider<C>>> componentsByKey = new HashMap<>(0);

    protected void setProducers(Collection<Provider<C>> providers) {
        this.components = new HashSet<>(providers);
        this.componentsByKey = providers.stream()
                .flatMap(p -> keys(p).map(k -> Maps.immutableEntry(k, p)))
                .collect(groupingBy(Entry::getKey, HashMap::new, mapping(Entry::getValue, toSet())));
    }

    protected Set<K> keys() {
        return Collections.unmodifiableSet(componentsByKey.keySet());
    }

    protected Optional<C> get(K k) {
        return this.componentsByKey.computeIfAbsent(k, this::findProviders).stream()
                .map(Provider::get).min(ProxySimilarityComparator.create(k));
    }

    private Set<Provider<C>> findProviders(K key) {
        return this.components.stream()
                .filter(p -> keys(p).anyMatch(k -> k.getSimilarity(key) >= 0))
                .collect(toSet());
    }

    private static <K extends Similar<K>, C extends Component<K>> Stream<K> keys(Provider<C> p) {
        return p.get().getKeys().stream();
    }

}
