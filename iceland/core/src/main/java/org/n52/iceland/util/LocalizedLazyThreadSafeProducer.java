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

import java.util.Locale;

import org.n52.faroe.ConfigurationError;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.UncheckedExecutionException;

public abstract class LocalizedLazyThreadSafeProducer<T> extends LazyThreadSafeProducer<T>
        implements LocalizedProducer<T> {

    private final LoadingCache<Locale, T> cache = CacheBuilder.newBuilder().build(new CacheLoader<Locale, T>() {
        @Override
        public T load(Locale key) {
            return create(key);
        }
    });

    private T t;

    @Override
    protected void setRecreate() {
        super.setRecreate();
        this.cache.invalidateAll();
    }

    @Override
    public T get(Locale language)
            throws ConfigurationError {
        if (language == null) {
            return get();
        } else {
            try {
                return this.cache.getUnchecked(language);
            } catch (UncheckedExecutionException ex) {
                if (ex.getCause() instanceof ConfigurationError) {
                    throw (ConfigurationError) ex.getCause();
                } else {
                    throw ex;
                }
            }
        }
    }

    @Override
    protected T create() throws ConfigurationError {
        return create(null);
    }

    protected abstract T create(Locale language)
            throws ConfigurationError;

}
