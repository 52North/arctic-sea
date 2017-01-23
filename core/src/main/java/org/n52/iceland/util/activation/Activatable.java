/*
 * Copyright 2015-2017 52°North Initiative for Geospatial Open Source
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
package org.n52.iceland.util.activation;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import com.google.common.base.MoreObjects;

/**
 * @param <T>
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 * @since 1.0.0
 *
 */
public class Activatable<T> {
    private T object;

    private boolean active;

    public Activatable(T object) {
        this(object, true);
    }

    public Activatable(T object, boolean active) {
        this.object = object;
        this.active = active;
    }

    /**
     * @return isActive() ? getInternal() : null
     */
    public T get() {
        return isActive() ? getInternal() : null;
    }

    public Optional<T> getOptional() {
        return Optional.ofNullable(get());
    }

    public T getInternal() {
        return object;
    }

    public boolean isActive() {
        return active;
    }

    public Activatable<T> setActive(boolean active) {
        this.active = active;
        return this;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getInternal(), isActive());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Activatable) {
            Activatable<?> a = (Activatable<?>) obj;
            return Objects.equals(isActive(), a.isActive()) && Objects.equals(getInternal(), a.getInternal());
        }
        return false;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(getClass()).add("object", getInternal()).add("active", isActive()).toString();
    }

    public static <K, V> Map<K, V> filter(Map<K, Activatable<V>> map) {
        return Activatables.filter(map);
    }

    public static <E> Set<E> filter(Set<Activatable<E>> set) {
        return Activatables.filter(set);
    }

    public static <E> Set<E> unfiltered(Set<Activatable<E>> set) {
        return Activatables.unfiltered(set);
    }

    public static <K, V> Map<K, V> unfiltered(Map<K, Activatable<V>> map) {
        return Activatables.unfiltered(map);
    }

    public static <E> Set<Activatable<E>> from(Set<E> set) {
        return Activatables.from(set);
    }

    public static <T> Activatable<T> from(T t) {
        return Activatables.from(t);
    }

    public static <T> Activatable<T> from(T t, boolean active) {
        return Activatables.from(t, active);
    }
}
