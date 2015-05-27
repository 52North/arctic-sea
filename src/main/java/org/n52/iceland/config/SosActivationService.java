package org.n52.iceland.config;

import java.util.Set;

import org.n52.iceland.config.ActivationService.AbstractActivationListener;
import org.n52.iceland.config.ActivationService.AbstractActivationSource;
import org.n52.iceland.ds.ConnectionProviderException;
import org.n52.iceland.encode.ProcedureDescriptionFormatKey;
import org.n52.iceland.encode.ResponseFormatKey;
import org.n52.iceland.ogc.swes.OfferingExtensionKey;
import org.n52.iceland.util.activation.ActivationListener;
import org.n52.iceland.util.activation.ActivationSource;

public class SosActivationService
        extends ActivationService {

    private SosActivationDao sosPersistingActivationManagerDao;

    public void setSosPersistingActivationManagerDao(
            SosActivationDao dao) {
        this.sosPersistingActivationManagerDao = dao;
    }

    /**
     * Checks if the response format is active for the specified service and
     * version.
     *
     * @param key
     *            the service/version/responseFormat combination
     *
     * @return if the format is active
     *
     * @throws ConnectionProviderException
     */
    public boolean isResponseFormatActive(ResponseFormatKey key)
            throws ConnectionProviderException {
        return sosPersistingActivationManagerDao.isResponseFormatActive(key);
    }

    public ActivationListener<ResponseFormatKey> getResponseFormatActivationListener() {
        return new AbstractActivationListener<ResponseFormatKey>() {
            @Override
            protected void setStatus(ResponseFormatKey key, boolean active)
                    throws ConnectionProviderException {
                sosPersistingActivationManagerDao
                        .setResponseFormatStatus(key, active);
            }
        };
    }

    public ActivationSource<ResponseFormatKey> getResponseFormatActivationSource() {
        return new AbstractActivationSource<ResponseFormatKey>() {
            @Override
            protected boolean check(ResponseFormatKey key)
                    throws ConnectionProviderException {
                return sosPersistingActivationManagerDao
                        .isResponseFormatActive(key);
            }

            @Override
            protected Set<ResponseFormatKey> get()
                    throws ConnectionProviderException {
                return sosPersistingActivationManagerDao.getResponseFormatKeys();
            }
        };
    }

    /**
     * Checks if the procedure description format is active for the specified
     * service and version.
     *
     * @param key
     *            the service/version/procedure description combination
     *
     * @return if the format is active
     *
     * @throws ConnectionProviderException
     */
    public boolean isProcedureDescriptionFormatActive(
            ProcedureDescriptionFormatKey key)
            throws ConnectionProviderException {
        return sosPersistingActivationManagerDao
                .isProcedureDescriptionFormatActive(key);
    }

    public ActivationListener<ProcedureDescriptionFormatKey> getProcedureDescriptionFormatListener() {
        return new AbstractActivationListener<ProcedureDescriptionFormatKey>() {
            @Override
            protected void setStatus(ProcedureDescriptionFormatKey key,
                                     boolean active)
                    throws ConnectionProviderException {
                sosPersistingActivationManagerDao
                        .setProcedureDescriptionFormatStatus(key, active);
            }
        };
    }

    public ActivationSource<ProcedureDescriptionFormatKey> getProcedureDescriptionFormatSource() {
        return new AbstractActivationSource<ProcedureDescriptionFormatKey>() {
            @Override
            protected boolean check(ProcedureDescriptionFormatKey key)
                    throws ConnectionProviderException {
                return sosPersistingActivationManagerDao
                        .isProcedureDescriptionFormatActive(key);
            }

            @Override
            protected Set<ProcedureDescriptionFormatKey> get()
                    throws ConnectionProviderException {
                return sosPersistingActivationManagerDao
                        .getProcedureDescriptionFormatKeys();
            }
        };
    }

    /**
     * Checks if the offering extension is active.
     *
     * @param key
     *            the offering extension key
     *
     * @return if the offering extension is active
     *
     * @throws ConnectionProviderException
     */
    public boolean isOfferingExtensionActive(OfferingExtensionKey key)
            throws ConnectionProviderException {
        return sosPersistingActivationManagerDao.isOfferingExtensionActive(key);
    }

    public ActivationListener<OfferingExtensionKey> getOfferingExtensionActivationListener() {
        return new AbstractActivationListener<OfferingExtensionKey>() {
            @Override
            protected void setStatus(OfferingExtensionKey key, boolean active)
                    throws ConnectionProviderException {
                sosPersistingActivationManagerDao
                        .setOfferingExtensionStatus(key, active);
            }
        };
    }

    public ActivationSource<OfferingExtensionKey> getOfferingExtensionActivationSource() {
        return new AbstractActivationSource<OfferingExtensionKey>() {
            @Override
            protected boolean check(OfferingExtensionKey key)
                    throws ConnectionProviderException {
                return sosPersistingActivationManagerDao
                        .isOfferingExtensionActive(key);
            }

            @Override
            protected Set<OfferingExtensionKey> get()
                    throws ConnectionProviderException {
                return sosPersistingActivationManagerDao
                        .getOfferingExtensionKeys();
            }
        };
    }

}
