/*
 * Copyright 2015-2017 52°North Initiative for Geospatial Open Source
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
package org.n52.iceland.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.faroe.ConfigurationError;

/**
 * @since 1.0.0
 *
 */
public class AbstractPropertyFileHandler {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractPropertyFileHandler.class);
    private static final String ERROR_READING_MESSAGE = "Error reading properties";
    private static final String ERROR_WRITING_MESSAGE = "Error writing properties";
    private final File propertiesFile;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private Properties cache;

    protected AbstractPropertyFileHandler(String name) {
        this.propertiesFile = new File(name);
    }

    public File getFile(boolean create) throws IOException {
        if (propertiesFile.exists() || (create && propertiesFile.createNewFile())) {
            return propertiesFile;
        }
        return null;
    }

    private Properties load() throws IOException {
        if (this.cache == null) {
            File f = getFile(false);
            if (f == null) {
                return new Properties();
            }
            try (InputStream is = new FileInputStream(getFile(true))) {
                Properties p = new Properties();
                p.load(is);
                cache = p;
            }
        }
        return cache;
    }

    private void save(Properties p) throws IOException {
        try (OutputStream os = new FileOutputStream(getFile(true))) {
            p.store(os, null);
            this.cache = p;
        }
    }

    public void save(String m, String value) throws ConfigurationError {
        lock.writeLock().lock();
        try {
            Properties p = load();
            p.setProperty(m, value);
            save(p);
        } catch (IOException e) {
            throw new ConfigurationError(ERROR_WRITING_MESSAGE, e);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public String get(String m) throws ConfigurationError {
        lock.readLock().lock();
        try {
            return load().getProperty(m);
        } catch (IOException e) {
            throw new ConfigurationError(ERROR_READING_MESSAGE, e);
        } finally {
            lock.readLock().unlock();
        }
    }

    public void delete(String m) throws ConfigurationError {
        lock.writeLock().lock();
        try {
            Properties p = load();
            p.remove(m);
            save(p);
        } catch (IOException e) {
            throw new ConfigurationError(ERROR_WRITING_MESSAGE, e);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public boolean delete() {
        try {
            cache = null;
            LOG.debug("Removing properties file: {}.", getFile(false));
            return exists() ? getFile(false).delete() : true;
        } catch (IOException ex) {
            return false;
        }
    }

    public void saveAll(Properties properties) throws ConfigurationError {
        lock.writeLock().lock();
        try {
            Properties p = load();
            properties.stringPropertyNames().forEach(key -> p.setProperty(key, properties.getProperty(key)));
            save(p);
        } catch (IOException e) {
            throw new ConfigurationError(ERROR_WRITING_MESSAGE, e);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public Properties getAll() throws ConfigurationError {
        lock.readLock().lock();
        try {
            return copyOf(load());
        } catch (IOException e) {
            throw new ConfigurationError(ERROR_READING_MESSAGE, e);
        } finally {
            lock.readLock().unlock();
        }
    }

    public String getPath() {
        return this.propertiesFile.getAbsolutePath();
    }

    public boolean exists() {
        try {
            return getFile(false) != null;
        } catch (IOException ex) {
            /* won't be thrown */
            throw new RuntimeException(ex);
        }
    }

    private static Properties copyOf(Properties p) {
        Properties np = new Properties();
        p.stringPropertyNames().forEach(s -> np.put(s, p.get(s)));
        return np;
    }
}
