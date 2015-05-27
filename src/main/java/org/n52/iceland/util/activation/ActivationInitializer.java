package org.n52.iceland.util.activation;

/**
 *
 * @author Christian Autermann
 */
public interface ActivationInitializer<K> {

    void initialize(ActivationManager<K> manager);

}
