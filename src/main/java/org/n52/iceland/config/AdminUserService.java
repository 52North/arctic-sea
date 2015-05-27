package org.n52.iceland.config;

import java.util.Set;

import org.n52.iceland.ds.ConnectionProviderException;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public interface AdminUserService {

    /**
     * Creates a new {@code AdministratorUser}. This method will fail if the
     * username is already used by another user.
     * <p/>
     *
     * @param username
     *                 the proposed username
     * @param password
     *                 the proposed (hashed) password
     * <p/>
     * @return the created user
     * <p/>
     * @throws ConnectionProviderException
     */
    AdministratorUser createAdminUser(String username, String password)
            throws ConnectionProviderException;

    /**
     * Deletes the user with the specified username.
     * <p/>
     *
     * @param username
     *                 the username
     * <p/>
     * @throws ConnectionProviderException
     */
    void deleteAdminUser(String username)
            throws ConnectionProviderException;

    /**
     * Deletes the user previously returned by
     * {@link #getAdminUser(java.lang.String)} or {@link #getAdminUsers()}.
     * <p/>
     *
     * @param user
     *             <p/>
     * @throws ConnectionProviderException
     */
    void deleteAdminUser(AdministratorUser user)
            throws ConnectionProviderException;

    /**
     * Gets the administrator user with the specified user name.
     *
     * @param username
     *                 the username
     *
     * @return the administrator user or {@code null} if no user with the
     *         specified name exists
     *
     * @throws ConnectionProviderException
     */
    AdministratorUser getAdminUser(String username)
            throws ConnectionProviderException;

    /**
     * Gets all registered administrator users.
     *
     * @return the users
     *
     * @throws ConnectionProviderException
     */
    Set<AdministratorUser> getAdminUsers()
            throws ConnectionProviderException;

    /**
     * Checks if a administrator user exists.
     *
     * @return {@code true} if there is a admin user, otherwise {@code false}.
     *
     * @throws ConnectionProviderException
     */
    boolean hasAdminUser()
            throws ConnectionProviderException;

    /**
     * Saves a user previously returned by
     * {@link #getAdminUser(java.lang.String)} or {@link #getAdminUsers()}.
     * <p/>
     *
     * @param user
     *             the user to change
     * <p/>
     * @throws ConnectionProviderException
     */
    void saveAdminUser(AdministratorUser user)
            throws ConnectionProviderException;

    /**
     * Deletes all users.
     * <p/>
     *
     * @throws ConnectionProviderException
     */
    public abstract void deleteAll()
            throws ConnectionProviderException;
}
