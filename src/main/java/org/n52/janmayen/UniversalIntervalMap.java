package org.n52.janmayen;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * {@link IntervalMap} implementation that returns the same value for each key.
 *
 * @param <K> the key type
 * @param <V> the value type
 *
 * @author Christian Autermann
 */
public class UniversalIntervalMap<K, V> implements IntervalMap<K, V> {
    private final V value;

    private UniversalIntervalMap(V value) {
        this.value = Objects.requireNonNull(value);
    }

    @Override
    public Optional<V> get(K lower, K upper) {
        return Optional.of(this.value);
    }

    @Override
    public Set<V> search(K lower, K upper) {
        return Collections.singleton(this.value);
    }

    /**
     * Creates a new {@link UniversalIntervalMap}.
     *
     * @param <K>   the key type
     * @param <V>   the value type
     * @param value the value
     *
     * @return the {@link IntervalMap}
     */
    public static <K, V> IntervalMap<K, V> of(V value) {
        return new UniversalIntervalMap<>(value);
    }
}
