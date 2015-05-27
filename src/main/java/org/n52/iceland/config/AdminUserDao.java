package org.n52.iceland.config;

import java.util.Set;

import org.n52.iceland.ds.ConnectionProviderException;

/**
 *
 * @author Christian Autermann
 */
public interface AdminUserDao {

    AdministratorUser createAdminUser(String username, String password)
            throws ConnectionProviderException;

    void deleteAdminUser(String username)
            throws ConnectionProviderException;

    AdministratorUser getAdminUser(String username)
            throws ConnectionProviderException;

    Set<AdministratorUser> getAdminUsers()
            throws ConnectionProviderException;

    void saveAdminUser(AdministratorUser user)
            throws ConnectionProviderException;

    void deleteAll()
            throws ConnectionProviderException;
}
