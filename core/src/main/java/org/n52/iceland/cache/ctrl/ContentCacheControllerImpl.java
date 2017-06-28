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
package org.n52.iceland.cache.ctrl;

import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.inject.Inject;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.iceland.cache.ContentCachePersistenceStrategy;
import org.n52.iceland.cache.ContentCacheUpdate;
import org.n52.iceland.cache.WritableContentCache;
import org.n52.janmayen.lifecycle.Constructable;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;

public class ContentCacheControllerImpl extends AbstractSchedulingContentCacheController implements Constructable {
    private static final Logger LOGGER = LoggerFactory.getLogger(ContentCacheControllerImpl.class);

    private static final AtomicInteger COMPLETE_UPDATE_COUNT = new AtomicInteger(0);
    private static final AtomicInteger PARTIAL_UPDATE_COUNT = new AtomicInteger(0);

    private volatile WritableContentCache cache;
    private final ReentrantLock lock = new ReentrantLock();

    private CompleteUpdate current;
    private CompleteUpdate next;

    private ContentCachePersistenceStrategy persistenceStrategy;
    private ContentCacheFactory cacheFactory;
    private CompleteCacheUpdateFactory completeCacheUpdateFactory;

    @Inject
    public void setCacheFactory(ContentCacheFactory cacheFactory) {
        this.cacheFactory = cacheFactory;
    }

    @Inject
    public void setPersistenceStrategy(ContentCachePersistenceStrategy persistenceStrategy) {
        this.persistenceStrategy = persistenceStrategy;
    }

    @Inject
    public void setCompleteCacheUpdateFactory(CompleteCacheUpdateFactory factory) {
        this.completeCacheUpdateFactory = factory;
    }

    @Override
    public void init() {
        loadOrCreateCache();
    }

    private void loadOrCreateCache() {
        Optional<WritableContentCache> optionalCache = persistenceStrategy.load();
        if (optionalCache.isPresent()) {
            setCache(optionalCache.get());
        } else {
            // cache file doesn't exist, try to load cache from datasource
            setCache(this.cacheFactory.get());
            try {
                update();
            } catch (OwsExceptionReport e) {
                LOGGER.warn("Couldn't load cache from datasource, maybe the datasource isn't configured yet?", e);
            }
        }
        setInitialized(true);
    }

    @Override
    public WritableContentCache getCache() {
        return this.cache;
    }

    protected void setCache(WritableContentCache wcc) {
        this.cache = wcc;
    }

    @Override
    public void destroy() {
        super.destroy();
        lock();
        try {
            persistenceStrategy.persistOnShutdown(getCache());
        } finally {
            unlock();
        }
    }

    @Override
    public void update(ContentCacheUpdate update) throws OwsExceptionReport {
        if (update != null) {
            if (update.isCompleteUpdate()) {
                executeComplete(new CompleteUpdate(update));
            } else {
                executePartial(new PartialUpdate(update));
            }
            cache.setLastUpdateTime(DateTime.now());
        } else {
            throw new IllegalArgumentException("update may not be null");
        }
    }

    private void runCurrent() throws OwsExceptionReport {
        LOGGER.trace("Starting update {}", this.current);
        this.current.execute();
        LOGGER.trace("Finished update {}", this.current);
        lock();
        try {
            persistenceStrategy.persistOnCompleteUpdate(getCache());
            CompleteUpdate u = this.current;
            this.current = null;
            u.signalWaiting();
        } finally {
            unlock();
        }
    }

    private void executePartial(PartialUpdate update) throws OwsExceptionReport {
        update.execute(getCache());
        lock();
        try {
            if (this.current != null) {
                this.current.addUpdate(update);
            } else {
                persistenceStrategy.persistOnPartialUpdate(getCache());
            }
        } finally {
            unlock();
        }
    }

    private void executeComplete(CompleteUpdate update) throws OwsExceptionReport {
        boolean isCurrent = false;
        boolean isNext = false;
        CompleteUpdate waitFor = null;
        lock();
        try {
            if (current == null || current.isFinished()) {
                current = update;
                isCurrent = true;
            } else if (current.isNotYetStarted()) {
                waitFor = current;
            } else if (next == null || next.isFinished()) {
                next = update;
                waitFor = current;
                isNext = true;
            } else {
                waitFor = next;
            }
        } finally {
            unlock();
        }

        if (isCurrent) {
            runCurrent();
        } else if (isNext) {
            waitFor(waitFor, update);
            lock();
            try {
                current = next;
                next = null;
            } finally {
                unlock();
            }
            runCurrent();
        } else {
            waitFor(waitFor, update);
        }
    }

    private void waitFor(CompleteUpdate running, CompleteUpdate update) throws OwsExceptionReport {
        if (running != null) {
            LOGGER.trace("{} waiting for {}", update, running);
            running.waitForCompletion();
            LOGGER.trace("{} stopped waiting for {}", update, running);
        }
    }

    private void lock() {
        lock.lock();
    }

    private void unlock() {
        lock.unlock();
    }

    @Override
    public boolean isUpdateInProgress() {
        return current != null;
    }

    @Override
    public void update() throws OwsExceptionReport {
        update(this.completeCacheUpdateFactory.get());
    }

    @Override
    public ContentCachePersistenceStrategy getContentCachePersistenceStrategy() {
        return this.persistenceStrategy;
    }

    private enum State {
        WAITING,
        RUNNING,
        APPLYING_UPDATES,
        FINISHED,
        FAILED
    }

    private abstract class Update {
        private final ContentCacheUpdate update;

        Update(ContentCacheUpdate update) {
            this.update = update;
        }

        ContentCacheUpdate getUpdate() {
            return update;
        }
    }

    private class PartialUpdate extends Update {
        private final int nr = PARTIAL_UPDATE_COUNT.getAndIncrement();

        PartialUpdate(ContentCacheUpdate update) {
            super(update);
        }

        synchronized void execute(WritableContentCache newCache) throws OwsExceptionReport {
            LOGGER.trace("Starting partial update {}", getUpdate());
            getUpdate().reset();
            getUpdate().setCache(newCache);
            getUpdate().execute();
            LOGGER.trace("Finished partial update {}", getUpdate());
            if (getUpdate().failed()) {
                LOGGER.warn("Partial update failed!", getUpdate().getFailureCause());
                throw getUpdate().getFailureCause();
            }
        }

        @Override
        public String toString() {
            return String.format("PartialUpdate[#%d]", nr);
        }
    }

    private class CompleteUpdate extends Update {
        private final ConcurrentLinkedQueue<PartialUpdate> updates
                = new ConcurrentLinkedQueue<>();

        private final Lock lock = new ReentrantLock();
        private final Condition finished = lock.newCondition();
        private State state = State.WAITING;
        private final int nr = COMPLETE_UPDATE_COUNT.getAndIncrement();

        CompleteUpdate(ContentCacheUpdate update) {
            super(update);
        }

        void addUpdate(PartialUpdate update) {
            updates.offer(update);
        }

        State getState() {
            lock();
            try {
                return state;
            } finally {
                unlock();
            }
        }

        void setState(State state) {
            ContentCacheControllerImpl.this.lock();
            try {
                lock();
                try {
                    LOGGER.debug("State change: {} -> {}", this.state, state);
                    this.state = state;
                } finally {
                    unlock();
                }
            } finally {
                ContentCacheControllerImpl.this.unlock();
            }
        }

        boolean isFinished() {
            lock();
            try {
                return getState() == State.FINISHED || getState() == State.FAILED;
            } finally {
                unlock();
            }
        }

        boolean isNotYetStarted() {
            lock();
            try {
                return getState() == State.WAITING;
            } finally {
                unlock();
            }
        }

        void execute() throws OwsExceptionReport {
            setCache(execute(getCache()));
        }

        WritableContentCache execute(WritableContentCache newCache) throws OwsExceptionReport {
            if (isFinished()) {
                throw new IllegalStateException("already finished");
            }
            setState(State.RUNNING);
            getUpdate().setCache(newCache);
            LOGGER.trace("Starting complete update {}", getUpdate());
            getUpdate().execute();
            LOGGER.trace("Finished complete update {}", getUpdate());
            lock();
            try {
                if (getUpdate().failed()) {
                    setState(State.FAILED);
                    LOGGER.warn("Complete update failed!", getUpdate().getFailureCause());
                    throw getUpdate().getFailureCause();
                } else {
                    setState(State.APPLYING_UPDATES);
                    PartialUpdate pu;
                    WritableContentCache cc = getUpdate().getCache();
                    while ((pu = updates.poll()) != null) {
                        pu.execute(cc);
                    }
                    setState(State.FINISHED);
                    return cc;
                }
            } finally {
                unlock();
            }
        }

        void waitForCompletion() throws OwsExceptionReport {
            lock();
            try {
                while (!isFinished()) {
                    try {
                        finished.await();
                    } catch (InterruptedException ex) {
                        LOGGER.warn("Interrupted while waiting for cache update completion", ex);
                    }
                }
                if (getState() == State.FAILED) {
                    throw getUpdate().getFailureCause();
                }
            } finally {
                unlock();
            }
        }

        void signalWaiting() {
            lock();
            try {
                finished.signalAll();
            } finally {
                unlock();
            }
        }

        @Override
        public String toString() {
            return String.format("CompleteUpdate[#%d]", nr);
        }

        protected void unlock() {
            lock.unlock();
        }

        protected void lock() {
            lock.lock();
        }
    }
}
