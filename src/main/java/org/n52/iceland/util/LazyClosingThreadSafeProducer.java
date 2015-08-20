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
package org.n52.iceland.util;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.n52.iceland.exception.ConfigurationError;
import org.n52.iceland.lifecycle.Destroyable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**

 @author Daniel Nüst <d.nuest@52north.org>
 */
public abstract class LazyClosingThreadSafeProducer<T> implements Producer<T>, Destroyable {

    private static final Logger log = LoggerFactory.getLogger(LocalizedLazyThreadSafeProducer.class);

    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    private T nullType = null;

    protected void setRecreate() {
        this.lock.writeLock().lock();
        try {
            log.trace("Removing internal object to recreate it. Old object: {}", this.nullType);
            if (this.nullType != null) {
                log.trace("Closing {}", this.nullType);
                close(nullType);
            }

            this.nullType = null;
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    @Override
    public T get() throws ConfigurationError {
        this.lock.readLock().lock();
        try {
            if (this.nullType != null) {
                return this.nullType;
            }
        } finally {
            this.lock.readLock().unlock();
        }

        // default value is null, create it
        this.lock.writeLock().lock();
        try {
            // check if someone was faster
            if (this.nullType == null) {
                // create it
                this.nullType = create();
                log.trace("Created a new object: {}", this.nullType);
            }
            // downgrade to read lock
            this.lock.readLock().lock();
        } finally {
            this.lock.writeLock().unlock();
        }

        try {
            return this.nullType;
        } finally {
            this.lock.readLock().unlock();
        }
    }

    @Override
    public void destroy() {
        if (nullType != null) {
            close(nullType);
        }
    }

    protected abstract T create()
            throws ConfigurationError;

    protected abstract void close(T nullType);
}
