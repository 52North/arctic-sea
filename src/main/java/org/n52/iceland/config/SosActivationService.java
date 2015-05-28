/*
 * Copyright 2015 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.n52.iceland.config;

import java.util.Set;

import javax.inject.Inject;

import org.n52.iceland.config.ActivationService.AbstractActivationListener;
import org.n52.iceland.config.ActivationService.AbstractActivationSource;
import org.n52.iceland.ds.ConnectionProviderException;
import org.n52.iceland.encode.ProcedureDescriptionFormatKey;
import org.n52.iceland.encode.ResponseFormatKey;
import org.n52.iceland.ogc.swes.OfferingExtensionKey;
import org.n52.iceland.util.activation.ActivationInitializer;
import org.n52.iceland.util.activation.ActivationListener;
import org.n52.iceland.util.activation.ActivationSource;
import org.n52.iceland.util.activation.DefaultActivationInitializer;

public class SosActivationService
        extends ActivationService {

    private SosActivationDao sosActivationDao;

    @Inject
    public void setSosActivationDao(SosActivationDao dao) {
        this.sosActivationDao = dao;
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
        return sosActivationDao.isResponseFormatActive(key);
    }

    public ActivationListener<ResponseFormatKey> getResponseFormatListener() {
        return new AbstractActivationListener<ResponseFormatKey>() {
            @Override
            protected void setStatus(ResponseFormatKey key, boolean active)
                    throws ConnectionProviderException {
                sosActivationDao
                        .setResponseFormatStatus(key, active);
            }
        };
    }

    public ActivationSource<ResponseFormatKey> getResponseFormatSource() {
        return new AbstractActivationSource<ResponseFormatKey>() {
            @Override
            protected boolean check(ResponseFormatKey key)
                    throws ConnectionProviderException {
                return sosActivationDao
                        .isResponseFormatActive(key);
            }

            @Override
            protected Set<ResponseFormatKey> get()
                    throws ConnectionProviderException {
                return sosActivationDao.getResponseFormatKeys();
            }
        };
    }

    public ActivationInitializer<ResponseFormatKey> getResponseFormatInitializer() {
        return new DefaultActivationInitializer<>(getResponseFormatSource());
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
        return sosActivationDao
                .isProcedureDescriptionFormatActive(key);
    }

    public ActivationListener<ProcedureDescriptionFormatKey> getProcedureDescriptionFormatListener() {
        return new AbstractActivationListener<ProcedureDescriptionFormatKey>() {
            @Override
            protected void setStatus(ProcedureDescriptionFormatKey key,
                                     boolean active)
                    throws ConnectionProviderException {
                sosActivationDao
                        .setProcedureDescriptionFormatStatus(key, active);
            }
        };
    }

    public ActivationSource<ProcedureDescriptionFormatKey> getProcedureDescriptionFormatSource() {
        return new AbstractActivationSource<ProcedureDescriptionFormatKey>() {
            @Override
            protected boolean check(ProcedureDescriptionFormatKey key)
                    throws ConnectionProviderException {
                return sosActivationDao
                        .isProcedureDescriptionFormatActive(key);
            }

            @Override
            protected Set<ProcedureDescriptionFormatKey> get()
                    throws ConnectionProviderException {
                return sosActivationDao
                        .getProcedureDescriptionFormatKeys();
            }
        };
    }

    public ActivationInitializer<ProcedureDescriptionFormatKey> getProcedureDescriptionFormatInitializer() {
        return new DefaultActivationInitializer<>(getProcedureDescriptionFormatSource());
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
        return sosActivationDao.isOfferingExtensionActive(key);
    }

    public ActivationListener<OfferingExtensionKey> getOfferingExtensionListener() {
        return new AbstractActivationListener<OfferingExtensionKey>() {
            @Override
            protected void setStatus(OfferingExtensionKey key, boolean active)
                    throws ConnectionProviderException {
                sosActivationDao
                        .setOfferingExtensionStatus(key, active);
            }
        };
    }

    public ActivationSource<OfferingExtensionKey> getOfferingExtensionSource() {
        return new AbstractActivationSource<OfferingExtensionKey>() {
            @Override
            protected boolean check(OfferingExtensionKey key)
                    throws ConnectionProviderException {
                return sosActivationDao
                        .isOfferingExtensionActive(key);
            }

            @Override
            protected Set<OfferingExtensionKey> get()
                    throws ConnectionProviderException {
                return sosActivationDao
                        .getOfferingExtensionKeys();
            }
        };
    }


    public ActivationInitializer<OfferingExtensionKey> getOfferingExtensionInitializer() {
        return new DefaultActivationInitializer<>(getOfferingExtensionSource());
    }
}
