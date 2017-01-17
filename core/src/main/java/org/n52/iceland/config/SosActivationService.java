package org.n52.iceland.config;

import java.util.Set;

import javax.inject.Inject;

import org.n52.iceland.ogc.sos.SosOfferingExtensionKey;
import org.n52.iceland.util.activation.ActivationInitializer;
import org.n52.iceland.util.activation.ActivationSource;
import org.n52.iceland.util.activation.DefaultActivationInitializer;
import org.n52.iceland.util.activation.FunctionalActivationListener;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class SosActivationService extends ActivationService {

    private SosActivationDao activationDao;

    @Override
    public SosActivationDao getActivationDao() {
        return activationDao;
    }

    @Inject
    public void setActivationDao(SosActivationDao activationDao) {
        this.activationDao = activationDao;
    }

    /**
     * Checks if the offering extension is active.
     *
     * @param key the offering extension key
     *
     * @return if the offering extension is active
     */
    public boolean isOfferingExtensionActive(SosOfferingExtensionKey key) {
        return getActivationDao().isOfferingExtensionActive(key);
    }

    public FunctionalActivationListener<SosOfferingExtensionKey> getOfferingExtensionListener() {
        return getActivationDao()::setOfferingExtensionStatus;
    }

    public ActivationSource<SosOfferingExtensionKey> getOfferingExtensionSource() {
        return ActivationSource.create(this::isOfferingExtensionActive,
                                       this::getOfferingExtensionKeys);
    }

    protected Set<SosOfferingExtensionKey> getOfferingExtensionKeys() {
        return getActivationDao().getOfferingExtensionKeys();
    }

    public ActivationInitializer<SosOfferingExtensionKey> getOfferingExtensionInitializer() {
        return new DefaultActivationInitializer<>(getOfferingExtensionSource());
    }
}
