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

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.faroe.ConfigurationError;
import org.n52.faroe.annotation.Configurable;
import org.n52.faroe.annotation.Setting;
import org.n52.iceland.cache.ContentCache;
import org.n52.janmayen.GroupedAndNamedThreadFactory;

/**
 * @author Christian Autermann
 */
@Configurable
public class AsyncCachePersistenceStrategy
        extends AbstractPersistingCachePersistenceStrategy {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncCachePersistenceStrategy.class);

    private static final TimeUnit WRITE_DELAY_UNITS = TimeUnit.SECONDS;
    private long writeDelay = 30;
    private final ScheduledExecutorService executor = Executors
            .newSingleThreadScheduledExecutor(new GroupedAndNamedThreadFactory("cache-persister"));
    private final AtomicReference<ContentCache> cacheReference = new AtomicReference<>();
    private Updater updater;

    @Override
    public void init() {
        super.init();
        this.updater = new Updater();
        this.executor.schedule(updater, writeDelay, WRITE_DELAY_UNITS);
    }

    @Setting(value = AsyncCachePersistenceStrategySettings.CACHE_PERSISTENCE_DELAY, required = false)
    public void setDelay(int delay) {
        if (delay <= 1) {
            throw new ConfigurationError("The write delay has be greater than 1 second.");
        }
        this.writeDelay = delay;
    }

    @Override
    public void persistOnPartialUpdate(ContentCache cache) {
        this.cacheReference.set(cache);
    }

    @Override
    public void persistOnCompleteUpdate(ContentCache cache) {
        this.cacheReference.set(cache);
    }

    @Override
    public void persistOnShutdown(ContentCache cache) {
        updater.setReschedule(false);
        this.executor.shutdown();
        try {
            this.executor.awaitTermination(writeDelay, WRITE_DELAY_UNITS);
        } catch (InterruptedException ie) {
            LOGGER.debug("Executor awaitTermination() was interrupted!", ie);
        }
        this.cacheReference.set(null);
        persistCache(cache);
    }

    private class Updater implements Runnable {

        private boolean reschedule = true;

        /**
         * @return the reschedule
         */
        public boolean isReschedule() {
            return reschedule;
        }

        /**
         * @param reschedule the reschedule to set
         */
        public void setReschedule(boolean reschedule) {
            this.reschedule = reschedule;
        }

        @Override
        public void run() {
            ContentCache cache = cacheReference.getAndSet(null);
            if (cache != null) {
                persistCache(cache);
            }
            if (isReschedule()) {
                executor.schedule(this, writeDelay, WRITE_DELAY_UNITS);
            }
        }
    }

}
