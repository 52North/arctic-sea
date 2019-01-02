/*
 * Copyright 2015-2019 52°North Initiative for Geospatial Open Source
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

public class AdminUserServiceImpl implements AdminUserService {

    private AdminUserDao adminUserDao;

    @Inject
    public void setAdminUserDao(AdminUserDao adminUserDao) {
        this.adminUserDao = adminUserDao;
    }

    @Override
    public void deleteAdminUser(AdministratorUser user) {
        deleteAdminUser(user.getUsername());
    }

    @Override
    public void deleteAdminUser(String username) {
        this.adminUserDao.deleteAdminUser(username);
    }

    @Override
    public boolean hasAdminUser() {
        return !getAdminUsers().isEmpty();
    }

    @Override
    public AdministratorUser createAdminUser(String username, String password) {
        return this.adminUserDao.createAdminUser(username, password);
    }

    @Override
    public AdministratorUser getAdminUser(String username) {
        return this.adminUserDao.getAdminUser(username);
    }

    @Override
    public Set<AdministratorUser> getAdminUsers() {
        return this.adminUserDao.getAdminUsers();
    }

    @Override
    public void saveAdminUser(AdministratorUser user) {
        this.adminUserDao.saveAdminUser(user);
    }

    @Override
    public void deleteAll() {
        this.adminUserDao.deleteAll();
    }

}
