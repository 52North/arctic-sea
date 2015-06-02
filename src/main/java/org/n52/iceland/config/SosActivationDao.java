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

import org.n52.iceland.ds.ConnectionProviderException;
import org.n52.iceland.encode.ProcedureDescriptionFormatKey;
import org.n52.iceland.encode.ResponseFormatKey;
import org.n52.iceland.ogc.swes.OfferingExtensionKey;

/**
 *
 * @author Christian Autermann
 */
public interface SosActivationDao {

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
    boolean isResponseFormatActive(ResponseFormatKey key)
            throws ConnectionProviderException;

    /**
     * Sets the status of a response format for the specified service and
     * version.
     *
     * @param key
     *               the service/version/responseFormat combination
     * @param active
     *               the status
     *
     * @throws ConnectionProviderException
     * @see #setActive(ResponseFormatKey, boolean)
     */
    void setResponseFormatStatus(ResponseFormatKey key, boolean active)
            throws ConnectionProviderException;

    Set<ResponseFormatKey> getResponseFormatKeys()
            throws ConnectionProviderException;

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
    boolean isProcedureDescriptionFormatActive(ProcedureDescriptionFormatKey key)
            throws ConnectionProviderException;

    /**
     * Sets the status of a response format for the specified service and
     * version.
     *
     * @param key
     *               the service/version/responseFormat combination
     * @param active
     *               the status
     *
     * @throws ConnectionProviderException
     * @see #setActive(ProcedureDescriptionFormatKey, boolean)
     */
    void setProcedureDescriptionFormatStatus(ProcedureDescriptionFormatKey key,
                                             boolean active)
            throws ConnectionProviderException;

    Set<ProcedureDescriptionFormatKey> getProcedureDescriptionFormatKeys()
            throws ConnectionProviderException;

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
    boolean isOfferingExtensionActive(OfferingExtensionKey key)
            throws ConnectionProviderException;

    void setOfferingExtensionStatus(OfferingExtensionKey key, boolean active)
            throws ConnectionProviderException;

    Set<OfferingExtensionKey> getOfferingExtensionKeys()
            throws ConnectionProviderException;
}
