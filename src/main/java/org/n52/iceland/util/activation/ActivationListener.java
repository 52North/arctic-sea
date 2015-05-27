package org.n52.iceland.util.activation;

/**
 * TODO JavaDoc
 *
 * @param <K>
 *
 * @author Christian Autermann
 */
public interface ActivationListener<K> {

    void activated(K key);

    void deactivated(K key);

}
