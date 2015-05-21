package org.n52.iceland.util.lifecycle;

import javax.annotation.PostConstruct;

/**
 * @author Christian Autermann <c.autermann@52north.org>
 * @since 5.0.0
 */
public interface Constructable {
    @PostConstruct
    void init();
}
