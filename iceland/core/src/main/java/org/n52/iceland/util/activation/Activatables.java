/*
 * Copyright (C) 2015-2022 52°North Spatial Information Research GmbH
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
package org.n52.iceland.util.activation;

import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import com.google.common.base.Predicates;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public final class Activatables {

    private Activatables() {
    }

    public static <K, V> Map<K, V> filter(Map<K, Activatable<V>> map) {
        return Optional.ofNullable(map)
                .orElseGet(HashMap::new)
                .entrySet().stream()
                .filter(e -> e.getValue() != null)
                .filter(e -> e.getValue().isActive())
                .collect(toMap(Entry::getKey, e -> e.getValue().get()));
    }

    public static <E> Set<E> filter(Set<Activatable<E>> set) {
        return Optional.ofNullable(set)
                .orElseGet(HashSet::new)
                .stream()
                .filter(Activatable::isActive)
                .map(Activatable::get)
                .collect(toSet());
    }

    public static <E> Set<E> unfiltered(Set<Activatable<E>> set) {
        return Optional.ofNullable(set)
                .orElseGet(HashSet::new).stream()
                .map(Activatable::getInternal)
                .collect(toSet());
    }

    public static <K, V> Map<K, V> unfiltered(Map<K, Activatable<V>> map) {
        return Optional.ofNullable(map)
                .orElseGet(HashMap::new)
                .entrySet().stream()
                .filter(e -> e.getValue() != null)
                .collect(toMap(Entry::getKey, e -> e.getValue().getInternal()));
    }

    public static <E> Set<Activatable<E>> from(Set<E> set) {
        return set.stream().map(Activatables::from).collect(toSet());
    }

    public static <T> Activatable<T> from(T t) {
        return from(t, true);
    }

    public static <T> Activatable<T> from(T t, boolean active) {
        return new Activatable<>(t, active);
    }

    public static <K, T> Set<K> activatedKeys(Map<K, T> map, ActivationProvider<? super K> provider) {
        return Sets.filter(map.keySet(), provider::isActive);
    }

    public static <K, T> Set<T> activatedSet(Map<K, T> map, ActivationProvider<? super K> provider) {
        return new HashSet<>(activatedMap(map, provider).values());
    }

    public static <K, T> Map<K, T> activatedMap(Map<K, T> map, ActivationProvider<? super K> provider) {
        return Maps.filterKeys(map, provider::isActive);
    }

    public static <K, T> Set<K> deactivatedKeys(Map<K, T> map, ActivationProvider<? super K> provider) {
        return Sets.filter(map.keySet(), Predicates.not(provider::isActive));
    }

    public static <K, T> Set<T> deactivatedSet(Map<K, T> map, ActivationProvider<? super K> provider) {
        return new HashSet<>(deactivatedMap(map, provider).values());
    }

    public static <K, T> Map<K, T> deactivatedMap(Map<K, T> map, ActivationProvider<? super K> provider) {
        return Maps.filterKeys(map, Predicates.not(provider::isActive));
    }

}
