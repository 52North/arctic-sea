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
package org.n52.iceland.util;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.n52.faroe.ConfigurationError;

/**
 * {@link PropertyFileHandler} that delegates to another handler.
 *
 * @author Christian Autermann
 */
public class DelegatingPropertyFileHandler implements PropertyFileHandler {

    private PropertyFileHandler delegate;

    /**
     * Get the delegate.
     *
     * @return the delegate
     */
    protected PropertyFileHandler getDelegate() {
        return delegate;
    }

    /**
     * Set the delegate.
     *
     * @param delegate the delegate
     */
    protected void setDelegate(PropertyFileHandler delegate) {
        this.delegate = delegate;
    }

    @Override
    public void delete(String m) throws ConfigurationError {
        this.delegate.delete(m);
    }

    @Override
    public boolean delete() {
        return this.delegate.delete();
    }

    @Override
    public boolean exists() {
        return this.delegate.exists();
    }

    @Override
    public String get(String m) throws ConfigurationError {
        return this.delegate.get(m);
    }

    @Override
    public Properties getAll() throws ConfigurationError {
        return this.delegate.getAll();
    }

    @Override
    public File getFile(boolean create) throws IOException {
        return this.delegate.getFile(create);
    }

    @Override
    public String getPath() {
        return this.delegate.getPath();
    }

    @Override
    public void save(String m, String value) throws ConfigurationError {
        this.delegate.save(m, value);
    }

    @Override
    public void saveAll(Properties properties) throws ConfigurationError {
        this.delegate.saveAll(properties);
    }

}
