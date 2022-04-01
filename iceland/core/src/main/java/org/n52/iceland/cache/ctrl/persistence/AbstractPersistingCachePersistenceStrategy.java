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
package org.n52.iceland.cache.ctrl.persistence;

import org.n52.faroe.annotation.Configurable;
import org.n52.faroe.annotation.Setting;
import org.n52.iceland.cache.ContentCache;
import org.n52.iceland.cache.ContentCachePersistenceStrategy;
import org.n52.iceland.cache.WritableContentCache;
import org.n52.janmayen.ConfigLocationProvider;
import org.n52.janmayen.lifecycle.Constructable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Optional;

@Configurable
public abstract class AbstractPersistingCachePersistenceStrategy
        implements ContentCachePersistenceStrategy, Constructable {
    public static final String CACHE_FILE_FOLDER = "service.cacheFileFolder";
    private static final Logger LOGGER = LoggerFactory
                                                 .getLogger(AbstractPersistingCachePersistenceStrategy.class);
    private static final String CACHE_FILE = "cache.tmp";
    private static final String TMP_PATH = "tmp";
    private static final String WEB_INF_PATH = "WEB-INF";
    private Path cacheFile;
    private ConfigLocationProvider configLocationProvider;
    private Path cacheFileFolder;

    @Inject
    public void setConfigLocationProvider(
            ConfigLocationProvider configLocationProvider) {
        this.configLocationProvider = configLocationProvider;
    }

    @Override
    public void init() {
        this.cacheFile = getBasePath().resolve(WEB_INF_PATH).resolve(TMP_PATH).resolve(CACHE_FILE);
        try {
            Path parent = cacheFile.getParent();
            if (parent != null) {
                if (!Files.isSymbolicLink(parent)) {
                    Files.createDirectories(parent);
                }
            } else {
                throw new RuntimeException("Error while creating tmp file path.");
            }
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    public Path getCacheFile() {
        return this.cacheFile;
    }

    @Override
    public Optional<WritableContentCache> load() {
        Path file = getCacheFile();

        if (!Files.isReadable(file)) {
            LOGGER.debug("No cache temp file found at '{}'", file);
            return Optional.empty();
        }
        LOGGER.debug("Reading cache from temp file '{}'", file);
        try (ObjectInputStream ois = newObjectInputStream(file)) {
            return Optional.of((WritableContentCache) ois.readObject());
        } catch (IOException | ClassNotFoundException ex) {
            LOGGER.error(String.format("Error reading cache file '%s'", file), ex);
            return Optional.empty();
        }
    }

    protected void persistCache(ContentCache cache) {

        if (cache == null) {
            remove();
            return;
        }
        Path file = getCacheFile();

        try (ObjectOutputStream oos = newObjectOutputStream(file)) {
            LOGGER.debug("Serializing cache to {}", file);
            oos.writeObject(cache);
        } catch (IOException ex) {
            LOGGER.error(String.format("Error serializing cache to '%s'", file), ex);
        }
    }

    protected Path getBasePath() {
        return isSetCacheFileFolder()
               ? getCacheFileFolder().toAbsolutePath()
               : Paths.get(configLocationProvider.get());
    }

    @Override
    public void remove() {
        Path file = getCacheFile();
        if (file == null) {
            return;
        }
        try {
            Files.deleteIfExists(file);
        } catch (IOException ex) {
            LOGGER.error(String.format("Error deleting cache file '%s'", file), ex);
        }
    }

    /**
     * @return the cacheFileFolder
     */
    protected Path getCacheFileFolder() {
        return cacheFileFolder;
    }

    /**
     * @param cacheFileFolder the cacheFileFolder to set
     */
    @Setting(value = CACHE_FILE_FOLDER, required = false)
    public void setCacheFileFolder(File cacheFileFolder) {
        if (cacheFileFolder != null) {
            setCacheFileFolder(cacheFileFolder.toPath());
        }
    }

    /**
     * @param cacheFileFolder the cacheFileFolder to set
     */
    public void setCacheFileFolder(Path cacheFileFolder) {
        this.cacheFileFolder = cacheFileFolder;
    }

    protected boolean isSetCacheFileFolder() {
        return cacheFileFolder != null && Files.exists(cacheFileFolder);
    }

    private static ObjectInputStream newObjectInputStream(Path file) throws IOException {
        InputStream is = Files.newInputStream(file);
        BufferedInputStream bis = new BufferedInputStream(is);
        try {
            return new ObjectInputStream(bis);
        } catch (IOException ex) {
            try {
                is.close();
            } catch (IOException suppressed) {
                ex.addSuppressed(suppressed);
            }
            throw ex;
        }
    }

    private static ObjectOutputStream newObjectOutputStream(Path file) throws IOException {
        OutputStream os = Files.newOutputStream(file, StandardOpenOption.CREATE);
        BufferedOutputStream bos = new BufferedOutputStream(os);
        try {
            return new ObjectOutputStream(bos);
        } catch (IOException ex) {
            try {
                bos.close();
            } catch (IOException suppressed) {
                suppressed.addSuppressed(suppressed);
            }
            throw ex;
        }
    }
}
