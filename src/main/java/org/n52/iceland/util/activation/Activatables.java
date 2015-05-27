package org.n52.iceland.util.activation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class Activatables {

    private  Activatables() {
    }

    public static <K, V> Map<K, V> filter(Map<K, Activatable<V>> map) {
        if (map == null) {
            return Maps.newHashMap();
        }
        Map<K, V> filtered = Maps.newHashMapWithExpectedSize(map.size());
        for (K k : map.keySet()) {
            if (map.get(k) != null && map.get(k).get() != null) {
                filtered.put(k, map.get(k).get());
            }
        }
        return filtered;
    }

    public static <E> Set<E> filter(Set<Activatable<E>> set) {
        if (set == null) {
            return Sets.newHashSet();
        }
        Set<E> filtered = new HashSet<>(set.size());
        for (Activatable<E> a : set) {
            if (a.isActive()) {
                filtered.add(a.get());
            }
        }
        return filtered;
    }

    public static <E> Set<E> unfiltered(Set<Activatable<E>> set) {
        if (set == null) {
            return Sets.newHashSet();
        }
        Set<E> unfiltered = new HashSet<>(set.size());
        for (Activatable<E> a : set) {
            unfiltered.add(a.getInternal());
        }
        return unfiltered;
    }

    public static <K, V> Map<K, V> unfiltered(Map<K, Activatable<V>> map) {
        if (map == null) {
            return Maps.newHashMap();
        }
        Map<K, V> filtered = new HashMap<>(map.size());
        for (K k : map.keySet()) {
            if (map.get(k) != null) {
                filtered.put(k, map.get(k).getInternal());
            }
        }
        return filtered;
    }

    public static <E> Set<Activatable<E>> from(Set<E> set) {
        Set<Activatable<E>> a = new HashSet<>(set.size());
        for (E t : set) {
            a.add(from(t));
        }
        return a;
    }

    public static <T> Activatable<T> from(T t) {
        return from(t, true);
    }

    public static <T> Activatable<T> from(T t, boolean active) {
        return new Activatable<>(t, active);
    }

}
