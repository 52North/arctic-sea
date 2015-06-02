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

import java.util.Locale;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.n52.iceland.exception.ConfigurationException;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.UncheckedExecutionException;

public abstract class LazyThreadSafeProducer<T> implements LocalizedProducer<T> {

    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final LoadingCache<Locale, T> cache = CacheBuilder.newBuilder()
            .build(new CacheLoader<Locale, T>() {
                @Override
                public T load(Locale key) {
                    return create(key);
                }
            });

    private T nullLocale = null;

    protected void setRecreate() {
        this.lock.writeLock().lock();
        try {
            this.nullLocale = null;
        } finally {
            this.lock.writeLock().unlock();
        }
        this.cache.invalidateAll();
    }

    @Override
    public T get() throws ConfigurationException {
        this.lock.readLock().lock();
        try {
            if (this.nullLocale != null) {
                return this.nullLocale;
            }
        } finally {
            this.lock.readLock().unlock();
        }

        // default value is null, create it
        this.lock.writeLock().lock();
        try {
            // check if someone was faster
            if (this.nullLocale == null) {
                // create it
                this.nullLocale = create(null);
            }
            // downgrade to read lock
            this.lock.readLock().lock();
        } finally {
            this.lock.writeLock().unlock();
        }

        try {
            return this.nullLocale;
        } finally {
            this.lock.readLock().unlock();
        }

    }

    @Override
    public T get(Locale language)
            throws ConfigurationException {
        if (language == null) {
            return get();
        } else {
            try {
                return this.cache.getUnchecked(language);
            } catch (UncheckedExecutionException ex) {
                if (ex.getCause() instanceof ConfigurationException) {
                    throw (ConfigurationException) ex.getCause();
                } else {
                    throw ex;
                }
            }
        }
    }

    protected abstract T create(Locale language)
            throws ConfigurationException;

}
