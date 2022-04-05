/*
 * Copyright 2015-${currentYear} 52°North Spatial Information Research GmbH
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
package org.n52.iceland.config.json;

import java.util.HashSet;
import java.util.Set;

import org.n52.faroe.json.AbstractJsonDao;
import org.n52.iceland.config.AdminUserDao;
import org.n52.iceland.config.AdministratorUser;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * TODO JavaDoc
 * @author Christian Autermann
 */
public class JsonAdminUserDao extends AbstractJsonDao implements AdminUserDao {

    @Override
    public AdministratorUser createAdminUser(String username, String password) {
        JsonAdministratorUser user;
        configuration().writeLock().lock();
        try {
            user = new JsonAdministratorUser(username, password);
            saveAdminUser(user);
        } finally {
            configuration().writeLock().unlock();
        }
        configuration().scheduleWrite();
        return user;
    }

    @Override
    public void deleteAdminUser(String username) {
        configuration().writeLock().lock();
        try {
            getConfiguration().with(JsonConstants.USERS).remove(username);
            configuration().scheduleWrite();
        } finally {
            configuration().writeLock().unlock();
        }
    }

    @Override
    public AdministratorUser getAdminUser(String username) {
        configuration().readLock().lock();
        try {
            String password = getConfiguration().path(JsonConstants.USERS).path(username).asText(null);
            return password == null ? null : new JsonAdministratorUser(username, password);
        } finally {
            configuration().readLock().unlock();
        }
    }


    @Override
    public Set<AdministratorUser> getAdminUsers() {
        Set<AdministratorUser> users;
        configuration().readLock().lock();
        try {
            JsonNode node = getConfiguration().path(JsonConstants.USERS);
            users = new HashSet<>(node.size());
            node.fieldNames()
                    .forEachRemaining(name -> users.add(new JsonAdministratorUser(name, node.path(name).asText(null))));
            return users;
        } finally {
            configuration().readLock().unlock();
        }

    }

    @Override
    public void saveAdminUser(AdministratorUser user) {
        configuration().writeLock().lock();
        try {
            getConfiguration().with(JsonConstants.USERS).put(user.getUsername(), user.getPassword());
            configuration().writeNow();
        } finally {
            configuration().writeLock().unlock();
        }

    }

    @Override
    public void deleteAll() {
        this.configuration().delete();
    }


}
