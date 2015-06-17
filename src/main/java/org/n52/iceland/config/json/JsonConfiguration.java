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
package org.n52.iceland.config.json;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.iceland.exception.ConfigurationError;
import org.n52.iceland.lifecycle.Constructable;
import org.n52.iceland.lifecycle.Destroyable;
import org.n52.iceland.service.ConfigLocationProvider;
import org.n52.iceland.util.JSONUtils;
import org.n52.iceland.util.Producer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * TODO JavaDoc
 * @author Christian Autermann
 */
public class JsonConfiguration implements Constructable, Destroyable, Producer<ObjectNode> {

    private static final Logger LOG = LoggerFactory.getLogger(JsonConfiguration.class);

    public static final String DEFAULT_FILE_NAME = "configuration.json";
    public static final int DEFAULT_WRITE_TIMEOUT = 1000;

    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final JsonNodeFactory nodeFactory = JSONUtils.nodeFactory();
    private String fileName = DEFAULT_FILE_NAME;
    private int writeTimeout = DEFAULT_WRITE_TIMEOUT;
    private ObjectNode configuration;
    private File file;
    private ConfigLocationProvider configLocationProvider;
    private Debouncer debouncer;

    @Override
    public void init() {
        this.debouncer = new Debouncer(this.writeTimeout, this::persist);
        File directory = new File(this.configLocationProvider.get());
        this.file = new File(directory, this.fileName);
        this.configuration = readConfiguration(this.file)
                .orElseGet(nodeFactory::objectNode);
    }

    @Override
    public void destroy() {
        this.debouncer.destroy();
    }

    public void delete() {
        writeLock().lock();
        try {
            if (this.file.exists() && this.file.isFile()) {
                this.file.delete();
            }
        } finally {
            writeLock().unlock();
        }
    }

    @Override
    public ObjectNode get() {
        return this.configuration;
    }

    public void setWriteTimeout(int writeTimeout) {
        this.writeTimeout = writeTimeout;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Inject
    public void setConfigLocationProvider(ConfigLocationProvider configLocationProvider) {
        this.configLocationProvider = configLocationProvider;
    }

    public Lock readLock() {
        return this.lock.readLock();
    }

    public Lock writeLock() {
        return this.lock.writeLock();
    }

    public void scheduleWrite() {
        LOG.debug("Scheduling write");
        this.debouncer.call();
    }

    private void persist() {
        readLock().lock();
        try {
            LOG.debug("Writing configuration file");
            try (FileOutputStream fos = new FileOutputStream(this.file)) {
                JSONUtils.print(fos, this.configuration);
            }
        } catch(IOException e) {
            throw new ConfigurationError("Could not persist configuration", e);
        } finally {
            readLock().unlock();
        }
    }

    private Optional<ObjectNode> readConfiguration(File file) {
        if (!file.exists()) {
            return Optional.empty();
        }
        if (!file.isFile()) {
            throw new ConfigurationError(file.getAbsolutePath() +
                                             " is not a file");
        }
        if (!file.canRead()) {
            throw new ConfigurationError(file.getAbsolutePath() +
                                             " is not a readable file");
        }
        try {
            JsonNode node = JSONUtils.loadFile(file);
            if (!node.isObject()) {
                throw new ConfigurationError(file.getAbsolutePath() +
                                                 " does not contain a JSON object");
            }
            return Optional.of((ObjectNode) node);
        } catch (IOException ex) {
            throw new ConfigurationError("Could not read " + file
                    .getAbsolutePath(), ex);
        }
    }
}
