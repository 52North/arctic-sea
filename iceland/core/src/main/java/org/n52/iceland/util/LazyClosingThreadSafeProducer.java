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
package org.n52.iceland.util;


import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.faroe.ConfigurationError;
import org.n52.janmayen.Producer;
import org.n52.janmayen.lifecycle.Destroyable;

/**

 @author <a href="mailto:d.nuest@52north.org">Daniel Nüst</a>
 */
public abstract class LazyClosingThreadSafeProducer<T> implements Producer<T>, Destroyable {

    private static final Logger LOG = LoggerFactory.getLogger(LocalizedLazyThreadSafeProducer.class);
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private T t;

    protected void setRecreate() {
        this.lock.writeLock().lock();
        try {
            LOG.trace("Removing internal object to recreate it. Old object: {}", this.t);
            if (this.t != null) {
                LOG.trace("Closing {}", this.t);
                close(t);
            }

            this.t = null;
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    @Override
    public T get() throws ConfigurationError {
        this.lock.readLock().lock();
        try {
            if (this.t != null) {
                return this.t;
            }
        } finally {
            this.lock.readLock().unlock();
        }

        // default value is null, create it
        this.lock.writeLock().lock();
        try {
            // check if someone was faster
            if (this.t == null) {
                // create it
                this.t = create();
                LOG.trace("Created a new object: {}", this.t);
            }
            // downgrade to read lock
            this.lock.readLock().lock();
        } finally {
            this.lock.writeLock().unlock();
        }

        try {
            return this.t;
        } finally {
            this.lock.readLock().unlock();
        }
    }

    @Override
    public void destroy() {
        if (t != null) {
            close(t);
        }
    }

    protected abstract T create()
            throws ConfigurationError;

    protected abstract void close(T object);
}
