package org.n52.iceland.util.activation;

import java.util.Set;

/**
 *
 * @author Christian Autermann
 */
public interface ActivationSource<K>
        extends ActivationProvider<K> {
    Set<K> getKeys();

}
