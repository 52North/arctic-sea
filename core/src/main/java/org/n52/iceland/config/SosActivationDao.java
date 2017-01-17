package org.n52.iceland.config;

import java.util.Set;

import org.n52.iceland.ogc.sos.SosOfferingExtensionKey;

/**
 * TODO JavaDoc
 * @author Christian Autermann
 */
public interface SosActivationDao extends ActivationDao {

/**
     * Checks if the offering extension is active.
     *
     * @param key
     *            the offering extension key
     *
     * @return if the offering extension is active
     */
    boolean isOfferingExtensionActive(SosOfferingExtensionKey key);

    void setOfferingExtensionStatus(SosOfferingExtensionKey key, boolean active);

    Set<SosOfferingExtensionKey> getOfferingExtensionKeys();
}
