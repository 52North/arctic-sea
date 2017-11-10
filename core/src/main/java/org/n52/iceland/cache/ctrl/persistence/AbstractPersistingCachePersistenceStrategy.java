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
package org.n52.iceland.cache.ctrl.persistence;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Optional;

import javax.inject.Inject;

import org.n52.faroe.annotation.Configurable;
import org.n52.faroe.annotation.Setting;
import org.n52.iceland.cache.ContentCache;
import org.n52.iceland.cache.ContentCachePersistenceStrategy;
import org.n52.iceland.cache.WritableContentCache;
import org.n52.janmayen.ConfigLocationProvider;
import org.n52.janmayen.lifecycle.Constructable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configurable
public abstract class AbstractPersistingCachePersistenceStrategy
        implements ContentCachePersistenceStrategy, Constructable {
    public static final String CACHE_FILE_FOLDER = "service.cacheFileFolder";
    private static final Logger LOGGER = LoggerFactory
            .getLogger(AbstractPersistingCachePersistenceStrategy.class);
    private static final String CACHE_FILE = "cache.tmp";
    private String cacheFile;
    private ConfigLocationProvider configLocationProvider;
    private File cacheFileFolder;

    @Inject
    public void setConfigLocationProvider(
            ConfigLocationProvider configLocationProvider) {
        this.configLocationProvider = configLocationProvider;
    }

    @Override
    public void init() {
        this.cacheFile = new File(getBasePath(), CACHE_FILE)
                .getAbsolutePath();
    }

    protected File getCacheFile() {
        return new File(this.cacheFile);
    }

    @Override
    public Optional<WritableContentCache> load() {
        File file = getCacheFile();
        if (file.exists() && file.canRead()) {
            LOGGER.debug("Reading cache from temp file '{}'", file.getAbsolutePath());

            try (FileInputStream fis = new FileInputStream(file);
                 ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(fis))) {
                return Optional.of((WritableContentCache) ois.readObject());
            } catch (IOException | ClassNotFoundException ex) {
                logErrorReading(file, ex);
            }
            if (!file.delete()) {
                logErrorDeleting(file);
            }
        } else {
            LOGGER.debug("No cache temp file found at '{}'", file.getAbsolutePath());
        }
        return Optional.empty();
    }

    protected void persistCache(ContentCache cache) {
        File file = getCacheFile();
        if (!file.exists() || file.delete()) {
            if (cache == null) {
                return;
            }

            try {
                if (!file.createNewFile()) {
                    logErroWriting(file);
                    return;
                }
            } catch (IOException ex) {
                logErrorSerializing(file, ex);
            }

            try (FileOutputStream fos = new FileOutputStream(file);
                 ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(fos))) {
                LOGGER.debug("Serializing cache to {}", file.getAbsolutePath());
                oos.writeObject(cache);
            } catch (IOException t) {
                logErrorSerializing(file, t);
            }
        }
    }

    protected String getBasePath() {
        return isSetCacheFileFolder() ? getCacheFileFolder().getAbsolutePath() : configLocationProvider.get();
    }

    @Override
    public void remove() {
        File f = getCacheFile();
        if (f != null && f.exists()) {
            if (!f.delete()) {
                logErrorDeleting(f);
            }
        }
    }

    /**
     * @return the cacheFileFolder
     */
    protected File getCacheFileFolder() {
        return cacheFileFolder;
    }

    protected boolean isSetCacheFileFolder() {
        return cacheFileFolder != null && !cacheFileFolder.exists();
    }

    /**
     * @param cacheFileFolder the cacheFileFolder to set
     */
    @Setting(CACHE_FILE_FOLDER)
    public void setCacheFileFolder(File cacheFileFolder) {
        this.cacheFileFolder = cacheFileFolder;
    }

    private void logErrorSerializing(File file, IOException ex) {
        LOGGER.error(String.format("Error serializing cache to '%s'", file.getAbsolutePath()), ex);
    }

    private void logErrorDeleting(File f) {
        LOGGER.error("Error deleting cache file '{}'", f.getAbsolutePath());
    }

    private void logErroWriting(File file) {
        LOGGER.error("Can not create writable file {}", file.getAbsolutePath());
    }

    private void logErrorReading(File file, Exception ex) {
        LOGGER.error(String.format("Error reading cache file '%s'", file.getAbsolutePath()), ex);
    }
}
