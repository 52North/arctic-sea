package org.n52.iceland.util.activation;

/**
 * @param <K>
 *
 * @author Christian Autermann
 */
public interface ActivationListenable<K> {
    void registerListener(ActivationListener<K> listener);

    void deregisterListener(ActivationListener<K> listener);
}
