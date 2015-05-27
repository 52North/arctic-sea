package org.n52.iceland.util.activation;

/**
 *
 * @param <K>
 *
 * @author Christian Autermann
 */
public interface ActivationManager<K>
        extends ActivationListenable<K>,
                ActivationProvider<K> {

    void activate(K key);

    void deactivate(K key);
}
