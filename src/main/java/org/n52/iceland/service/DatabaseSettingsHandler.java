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
package org.n52.iceland.service;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.inject.Inject;
import javax.servlet.ServletContext;

import org.n52.iceland.exception.ConfigurationException;
import org.n52.iceland.lifecycle.Constructable;
import org.n52.iceland.util.ServletContextPropertyFileHandler;

/**
 * @since 4.0.0
 *
 */
public class DatabaseSettingsHandler implements Constructable {

    public static final String INIT_PARAM_DATA_SOURCE_CONFIG_LOCATION
            = "datasourceConfigLocation";

    @Deprecated
    private static DatabaseSettingsHandler instance;
    private ServletContextPropertyFileHandler handler;

    @Inject
    public void setServletContext(ServletContext ctx) {
        String name = ctx
                .getInitParameter(INIT_PARAM_DATA_SOURCE_CONFIG_LOCATION);
        this.handler = new ServletContextPropertyFileHandler(ctx, name);
    }

    @Override
    public void init() {
        DatabaseSettingsHandler.instance = this;
    }

    public boolean delete() {
        return this.handler.delete();
    }

    public void delete(String key) {
        this.handler.delete(key);
    }

    public boolean exists() {
        return this.handler.exists();
    }

    public String get(String m)
            throws ConfigurationException {
        return this.handler.get(m);
    }

    public Properties getAll()
            throws ConfigurationException {
        return this.handler.getAll();
    }

    public File getFile(boolean create)
            throws IOException {
        return this.handler.getFile(create);
    }

    public String getPath() {
        return this.handler.getPath();
    }

    public void save(String m, String value)
            throws ConfigurationException {
        this.handler.save(m, value);
    }

    public void saveAll(Properties properties)
            throws ConfigurationException {
        this.handler.saveAll(properties);
    }

    @Deprecated
    public static DatabaseSettingsHandler getInstance() {
        return instance;
    }

    @Deprecated
    public static DatabaseSettingsHandler getInstance(ServletContext ctx) {
        return instance;
    }

}
