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
package org.n52.iceland.util;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * @author Christian Autermann <c.autermann@52north.org>
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk
 *         J&uuml;rrens</a>
 *
 * @since 4.0.0
 */
public final class CollectionHelper {
    private CollectionHelper() {
    }

    /**
     * @param entries
     *            the <i>final</i> set of entries to add to the newly created
     *            <i>unmodifiable</i> map
     * @return an <i>unmodifiable</i> map with all given entries
     */
    @SafeVarargs
    public static <K, V> Map<K, V> map(Entry<K, V>... entries) {
        return Collections.unmodifiableMap(Arrays.stream(entries)
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue)));
    }

    /**
     * @return an <b>UNMODIFIABLE</b> Set&lt;T&gt;
     */
    @SafeVarargs
    public static <T> Set<T> set(T... elements) {
        return Collections.unmodifiableSet(new HashSet<>(Arrays.asList(elements)));
    }

    /**
     * @return an <b>UNMODIFIABLE</b> List&lt;T&gt;
     */
    @SafeVarargs
    public static <T> List<T> list(T... elements) {
        return Collections.unmodifiableList(Arrays.asList(elements));
    }

    @SafeVarargs
    public static <T> Set<T> union(Set<T>... elements) {
        return Arrays.stream(elements).flatMap(Set::stream).collect(toSet());
    }

    public static <T> Set<T> union(Iterable<Set<T>> elements) {
        return StreamSupport.stream(elements.spliterator(), true)
                .flatMap(Set::stream).collect(toSet());
    }

    /**
     * @param <T> the key type
     * @param s the set
     * @return an <b>UNMODIFIABLE</b> Set&lt;T&gt;
     */
    public static <T> Set<T> unmodifiableSet(Set<? extends T> s) {
        return Optional.ofNullable(s)
                .map(Collections::unmodifiableSet)
                .orElseGet(Collections::emptySet);
    }

    /**
     * @param <K> the key type
     * @param <V> the value type
     * @param m the map
     * @return an <b>UNMODIFIABLE</b> Map&lt;K, V&gt;
     */
    public static <K, V> Map<K, V> unmodifiableMap(Map<? extends K, ? extends V> m) {
        return Optional.ofNullable(m)
                .map(Collections::unmodifiableMap)
                .orElseGet(Collections::emptyMap);
    }

    /**
     * @param c the collection
     * @return an <b>UNMODIFIABLE</b> Collection&lt;T&gt;
     */
    public static <T> Collection<T> unmodifiableCollection(
            Collection<? extends T> c) {
        return Optional.ofNullable(c)
                .map(Collections::unmodifiableCollection)
                .orElseGet(Collections::emptyList);
    }

    /**
     * @param <T> the element type
     * @param l the list
     * @return an <b>UNMODIFIABLE</b> List&lt;T&gt;
     */
    public static <T> List<T> unmodifiableList(List<? extends T> l) {
        return Optional.ofNullable(l)
                .map(Collections::unmodifiableList)
                .orElseGet(Collections::emptyList);
    }

    public static <T> List<T> conjunctCollections(Collection<T> list1, Collection<T> list2) {
        Set<T> set = new HashSet<>(list2);
        return list1.stream().filter(set::contains).collect(toList());
    }

    public static <T> Set<T> conjunctCollectionsToSet(Collection<T> list1, Collection<T> list2) {
        Set<T> set = new HashSet<>(list2);
        return list1.stream().filter(set::contains).collect(toSet());
    }

    public static <K, V> Map<K, V> synchronizedInitialSizeMapWithLoadFactor1(int capacity) {
        return CollectionHelper.synchronizedMap(capacity, 1.0F);
    }

    public static <K, V> Map<K, V> synchronizedMap() {
        return Collections.synchronizedMap(Maps.<K, V> newHashMap());
    }

    public static <K, V> Map<K, V> synchronizedMap(int initialCapacity) {
        return Collections.synchronizedMap(new HashMap<K, V>(initialCapacity));
    }

    public static <K, V> Map<K, V> synchronizedMap(int initialCapacity, float loadFactor) {
        return Collections.synchronizedMap(new HashMap<K, V>(initialCapacity, loadFactor));
    }

    /**
     * Constructs a new synchronized {@code Set} based on a {@link HashSet}.
     *
     * @return a synchronized Set
     */
    public static <T> Set<T> synchronizedSet() {
        return Collections.synchronizedSet(Sets.<T> newHashSet());
    }

    /**
     * Constructs a new synchronized {@code Set} based on a {@link HashSet} with
     * the specified {@code initialCapacity}.
     *
     * @param initialCapacity
     *            the initial capacity of the set
     *
     * @return a synchronized Set
     */
    public static <T> Set<T> synchronizedSet(int initialCapacity) {
        return Collections.synchronizedSet(new HashSet<T>(initialCapacity));
    }

    /**
     * Constructs a new synchronized {@code List} based on a {@link LinkedList}.
     *
     * @return a synchronized List
     */
    public static <E> List<E> synchronizedList() {
        return Collections.synchronizedList(Lists.<E> newLinkedList());
    }

    /**
     * Constructs a new synchronized {@code List} based on a {@link ArrayList}
     * with the specified {@code initialCapacity}.
     *
     * @param initialCapacity
     *            the initial capacity of the array list
     *
     * @return a synchronized List
     */
    public static <E> List<E> synchronizedList(int initialCapacity) {
        return Collections.synchronizedList(Lists.<E> newArrayListWithCapacity(initialCapacity));
    }

    /**
     * @param collectionOfCollection
     *            a Collection&lt;Collection&lt;T>>
     *
     * @return a Set&lt;T> containing all values of all Collections&lt;T>
     *         without any duplicates
     */
    public static <T> Set<T> unionOfListOfLists(Collection<? extends Collection<? extends T>> collectionOfCollection) {
        return Optional.ofNullable(collectionOfCollection)
                .map(Collection::stream)
                .orElseGet(Stream::empty)
                .filter(Objects::nonNull)
                .flatMap(x -> x.stream())
                .filter(Objects::nonNull)
                .collect(toSet());
    }

    /**
     * Check if collection is not null and not empty
     *
     * @param collection
     *            Collection to check
     *
     * @return empty or not
     */
    public static <T> boolean isNotEmpty(Collection<T> collection) {
        return !isEmptyOrNull(collection);
    }

    public static <T> boolean isEmptyOrNull(Collection<T> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * Check if collection is not <tt>null</tt> and empty
     *
     * @param collection
     *            Collection to check
     *
     * @return <tt>true</tt>, if collection is not null and empty, else
     *         <tt>false</tt>
     */
    public static <T> boolean isEmpty(Collection<T> collection) {
        return collection != null && collection.isEmpty();
    }

    /**
     * Check if collection is not null and not empty
     *
     *
     * @param map
     *            Map to check
     *
     * @return <tt>false</tt>, if map is <tt>null</tt> or empty, else
     *         <tt>true</tt>.
     */
    public static <K, V> boolean isNotEmpty(Map<K, V> map) {
        return map != null && !map.isEmpty();
    }

    /**
     * Check if map is not <tt>null</tt> and empty
     *
     * @param map
     *            map to check
     *
     * @return <tt>true</tt>, if map is not null and empty, else <tt>false</tt>
     */
    public static <K, V> boolean isEmpty(Map<K, V> map) {
        return map != null && map.isEmpty();
    }

    /**
     * Reverses a map (switches key and value types).
     *
     * @param <K>
     *            the key type
     * @param <V>
     *            the value type
     * @param map
     *            the map
     *
     * @return the reversed map
     */
    public static <K, V> Map<V, K> reverse(Map<K, V> map) {
        return map.entrySet().stream().collect(entryToMap());
    }

    /**
     * Examine a collection and determines if it is null, empty, or contains
     * only null values
     *
     * @param collection
     *            Collection to examine
     * @return whether the collection is null, empty, or contains only nulls
     */
    public static boolean nullEmptyOrContainsOnlyNulls(final Collection<? extends Object> collection) {
        return Optional.ofNullable(collection)
                .map(Collection::stream)
                .orElseGet(Stream::empty)
                .allMatch(Objects::isNull);
    }

    /**
     * Check if array is not null and not empty
     *
     * @param array
     *            Array to check
     * @return <code>true</code>, if array is not null and not empty
     */
    public static boolean isNotNullOrEmpty(Object[] array) {
        return array != null && array.length > 0;
    }

    /**
     * Check if array is not null or not empty
     *
     * @param array
     *            Array to check
     * @return <code>true</code>, if array is null or empty
     */
    public static boolean isNullOrEmpty(Object[] array) {
        return !isNotNullOrEmpty(array);
    }

    public static String collectionToString(Collection<?> collection) {
        return collection.stream().map(String::valueOf)
                .collect(joining(",", "(", ")"));
    }

    /**
     * Add a value to a map collection, initializing the key's collection if needed
     *
     * @param key Key whose value collection should be added to
     * @param valueToAdd Vale to add to the key's collection
     * @param map Map holding collections
     */
    public static <K, V> void addToCollectionMap(K key, V valueToAdd, Map<K, Collection<V>> map) {
        if (key != null && valueToAdd != null && map != null)  {
            map.computeIfAbsent(key, k -> Lists.newArrayList()).add(valueToAdd);
        }
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        return map.entrySet().stream()
                .sorted(comparing(Map.Entry::getValue))
                .collect(toLinkedHashMap());
    }

    private static <A, B> BiConsumer<B, A> reverse(BiConsumer<A, B> consumer) {
        return (a, b) -> consumer.accept(b, a);
    }

    private static <K,V> Collector<Entry<K, V>, ?, Map<V, K>> entryToMap() {
        return Collectors.toMap(Entry::getValue, Entry::getKey);
    }

    private static <K,V> Collector<Entry<K, V>, ?, LinkedHashMap<K, V>> toLinkedHashMap() {
        return toMap(Entry::getKey, Entry::getValue, throwingMerger(), LinkedHashMap::new);
    }

    private static <T> BinaryOperator<T> throwingMerger() {
        return (u,v) -> { throw new IllegalStateException(String.format("Duplicate key %s", u)); };
    }
}
