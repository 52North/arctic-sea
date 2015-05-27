package org.n52.iceland.config;

import java.util.Set;

import org.n52.iceland.binding.BindingKey;
import org.n52.iceland.ds.ConnectionProviderException;
import org.n52.iceland.ogc.ows.OwsExtendedCapabilitiesProviderKey;
import org.n52.iceland.request.operator.RequestOperatorKey;

/**
 *
 * @author Christian Autermann
 */
public interface ActivationDao {

    /**
     * Returns if a operation is active and should be offered by this SOS.
     *
     * @param key
     *            the key identifying the operation
     *
     * @return {@code true} if the operation is active in this SOS
     *
     * @throws ConnectionProviderException
     */
    public abstract boolean isRequestOperatorActive(RequestOperatorKey key)
            throws ConnectionProviderException;

    /**
     * Sets the status of an operation.
     *
     * @param key
     *               the key identifying the operation
     * @param active
     *               whether the operation is active or not
     *
     * @throws ConnectionProviderException
     * @see #setActive(RequestOperatorKey, boolean)
     */
    void setOperationStatus(RequestOperatorKey key, boolean active)
            throws ConnectionProviderException;

    Set<RequestOperatorKey> getRequestOperatorKeys()
            throws ConnectionProviderException;

    /**
     * Checks if the binding is active.
     *
     * @param key
     *            the binding
     *
     * @return if the binding is active
     *
     * @throws ConnectionProviderException
     */
    public abstract boolean isBindingActive(BindingKey key)
            throws ConnectionProviderException;

    /**
     * Sets the status of a binding.
     *
     * @param key
     *               the binding
     * @param active
     *               the status
     *
     * @throws ConnectionProviderException
     * @see #setActive(org.n52.iceland.binding.BindingKey, boolean)
     */
    void setBindingStatus(BindingKey key, boolean active)
            throws ConnectionProviderException;

    Set<BindingKey> getBindingKeys()
            throws ConnectionProviderException;

    /**
     * Checks if the extended capabilities is active.
     *
     * @param key
     *            the extended capabilities key
     *
     * @return if the extended capabilities is active
     *
     * @throws ConnectionProviderException
     */
    public abstract boolean isOwsExtendedCapabilitiesProviderActive(
            OwsExtendedCapabilitiesProviderKey key)
            throws ConnectionProviderException;

    void setOwsExtendedCapabilitiesStatus(OwsExtendedCapabilitiesProviderKey key,
                                          boolean active)
            throws ConnectionProviderException;

    Set<OwsExtendedCapabilitiesProviderKey> getOwsExtendedCapabilitiesProviderKeys()
            throws ConnectionProviderException;

}
