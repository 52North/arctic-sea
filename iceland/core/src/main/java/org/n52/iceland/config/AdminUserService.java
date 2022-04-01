/*
 * Copyright 2015-2022 52°North Initiative for Geospatial Open Source
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

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public interface AdminUserService {

    /**
     * Creates a new {@code AdministratorUser}. This method will fail if the username is already used by another user.
     *
     * @param username the proposed username
     * @param password the proposed (hashed) password
     *
     * @return the user
     */
    AdministratorUser createAdminUser(String username, String password);

    /**
     * Deletes the user with the specified username.
     *
     * @param username the username
     */
    void deleteAdminUser(String username);

    /**
     * Deletes the user previously returned by {@link #getAdminUser(java.lang.String)} or {@link #getAdminUsers()}.
     *
     * @param user the user
     */
    void deleteAdminUser(AdministratorUser user);

    /**
     * Gets the administrator user with the specified user name.
     *
     * @param username the username
     *
     * @return the administrator user or {@code null} if no user with the specified name exists
     */
    AdministratorUser getAdminUser(String username);

    /**
     * Gets all registered administrator users.
     *
     * @return the users
     */
    Set<AdministratorUser> getAdminUsers();

    /**
     * Checks if a administrator user exists.
     *
     * @return {@code true} if there is a admin user, otherwise {@code false}.
     */
    boolean hasAdminUser();

    /**
     * Saves a user previously returned by {@link #getAdminUser(java.lang.String)} or {@link #getAdminUsers()}.
     *
     * @param user the user to change
     */
    void saveAdminUser(AdministratorUser user);

    /**
     * Deletes all users.
     */
    void deleteAll();
}
