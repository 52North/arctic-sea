package org.n52.janmayen;

import java.util.Optional;
import java.util.Set;

/**
 * A map that uses intervals (specified as an upper and lower bound) as keys.
 *
 * @author Christian Autermann
 * @param <K> the key type
 * @param <V> the value type
 */
public interface IntervalMap<K, V> {

    /**
     * Get the value of the first interval matching the supplied key.
     *
     * @param key the key
     *
     * @return the value
     */
    default Optional<V> get(K key) {
        return get(key, key);
    }

    /**
     * Get the first value falling into the interval.
     *
     * @param lower the lower bound
     * @param upper the upper bound
     *
     * @return the value
     */
    Optional<V> get(K lower, K upper);

    /**
     * Get the first value for the specified key or the supplied default.
     *
     * @param key   the key
     * @param value the default value
     *
     * @return the value
     */
    default V getOrDefault(K key, V value) {
        return get(key).orElse(value);
    }

    /**
     * Get the first value falling into the interval or the supplied default.
     *
     * @param lower the lower bound
     * @param upper the upper bound
     * @param value the default value
     *
     * @return the value
     */
    default V getOrDefault(K lower, K upper, V value) {
        return get(lower, upper).orElse(value);
    }

    /**
     * Get the values for all intervals matching the supplied key.
     *
     * @param key the key
     *
     * @return the values
     */
    default Set<V> search(K key) {
        return search(key, key);
    }

    /**
     * Get all values falling into the specified interval.
     *
     * @param lower the upper bound
     * @param upper the lower bound
     *
     * @return the values
     */
    Set<V> search(K lower, K upper);

}
