package org.n52.iceland.util.activation;

/**
 *
 * @param <K>
 *
 * @author Christian Autermann
 */
public interface ActivationProvider<K> {

    boolean isActive(K key);
}
