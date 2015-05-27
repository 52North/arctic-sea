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
    public abstract boolean isResponseFormatActive(ResponseFormatKey key)
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
    public abstract boolean isProcedureDescriptionFormatActive(
            ProcedureDescriptionFormatKey key)
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
    public abstract boolean isOfferingExtensionActive(OfferingExtensionKey key)
            throws ConnectionProviderException;

    void setOfferingExtensionStatus(OfferingExtensionKey key, boolean active)
            throws ConnectionProviderException;

    Set<OfferingExtensionKey> getOfferingExtensionKeys()
            throws ConnectionProviderException;
}
