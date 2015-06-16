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
package org.n52.iceland.event;

import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.iceland.lifecycle.Constructable;
import org.n52.iceland.util.ClassHelper;
import org.n52.iceland.util.GroupedAndNamedThreadFactory;
import org.n52.iceland.util.collections.MultiMaps;
import org.n52.iceland.util.collections.SetMultiMap;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;

/**
 * The {@link ServiceEventListener} are registered to the
 * {@link ServiceEventBus} which delegates the fired {@link ServiceEvent} to the
 * {@link ServiceEventListener}.
 *
 * @author Christian Autermann <c.autermann@52north.org>
 *
 * @since 1.0.0
 */
public class ServiceEventBus implements Constructable {
    private static final Logger LOG = LoggerFactory.getLogger(ServiceEventBus.class);
    @Deprecated
    private static ServiceEventBus instance;
    private static final int THREAD_POOL_SIZE = 3;
    private static final String THREAD_GROUP_NAME = "ServiceEventBus-Worker";

    private final ClassCache classCache;
    private final ReadWriteLock lock;
    private final ThreadFactory threadFactory;
    private final Executor executor;
    private final SetMultimap<Class<? extends ServiceEvent>, ServiceEventListener> listeners;
    private final Queue<HandlerExecution> queue;
    private boolean async = false;

    public ServiceEventBus() {
        this.classCache = new ClassCache();
        this.lock = new ReentrantReadWriteLock();
        this.threadFactory = new GroupedAndNamedThreadFactory(THREAD_GROUP_NAME);
        this.executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE, threadFactory);
        this.listeners = HashMultimap.create();
        this.queue = new ConcurrentLinkedQueue<>();
    }

    public void setAsync(boolean async) {
        this.async = async;
    }

    @Override
    public void init() {
        ServiceEventBus.instance = this;
    }

    private boolean checkEvent(ServiceEvent event) {
        if (event == null) {
            LOG.warn("Submitted event is null!");
            return false;
        }
        return true;
    }

    private boolean checkListener(ServiceEventListener listener) {
        if (listener == null) {
            LOG.warn("Tried to unregister SosEventListener null");
            return false;
        }
        if (listener.getTypes() == null || listener.getTypes().isEmpty()) {
            LOG.warn("Listener {} has no EventTypes", listener);
            return false;
        }
        return true;
    }

    private Set<ServiceEventListener> getListenersForEvent(final ServiceEvent event) {
        lock.readLock().lock();
        try {

            return classCache.getClasses(event.getClass()).stream()
                    .map(listeners::get).filter(Objects::nonNull)
                    .flatMap(Set::stream).collect(Collectors.toSet());
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Submit the fired {@link ServiceEvent} to the registered
     * {@link ServiceEventListener} and initiate the handling of the
     * {@link ServiceEvent}
     *
     * @param event
     *              Submitted {@link ServiceEvent}
     */
    public void submit(ServiceEvent event) {
        if (!checkEvent(event)) {
            return;
        }
        lock.readLock().lock();
        try {
            getListenersForEvent(event).stream()
                    .peek(listener -> LOG.debug("Queueing Event {} for Listener {}", event, listener))
                    .map(listener -> new HandlerExecution(event, listener))
                    .forEach(queue::offer);
        } finally {
            lock.readLock().unlock();
        }
        HandlerExecution r;
        while ((r = queue.poll()) != null) {
            if (async) {
                executor.execute(r);
            } else {
                r.run();
            }
        }
    }

    /**
     * Register a new {@link ServiceEventListener} to the
     * {@link ServiceEventBus}.
     *
     * @param listener
     *            {@link ServiceEventListener} to register
     */
    public void register(ServiceEventListener listener) {
        if (!checkListener(listener)) {
            return;
        }
        lock.writeLock().lock();
        try {
            listener.getTypes().stream()
                    .peek(type -> LOG.debug("Subscibing Listener {} to EventType {}", listener, type))
                    .forEach(type -> listeners.put(type, listener));
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Unregister a new {@link ServiceEventListener} to the
     * {@link ServiceEventBus}.
     *
     * @param listener
     *            {@link ServiceEventListener} to unregister
     */
    public void unregister(ServiceEventListener listener) {
        if (!checkListener(listener)) {
            return;
        }
        lock.writeLock().lock();
        try {
            for (Class<? extends ServiceEvent> eventType : listener.getTypes()) {
                Set<ServiceEventListener> listenersForKey = listeners.get(eventType);
                if (listenersForKey.contains(listener)) {
                    LOG.debug("Unsubscibing Listener {} from EventType {}", listener, eventType);
                    listenersForKey.remove(listener);
                } else {
                    LOG.warn("Listener {} was not registered for SosEvent Type {}", listener, eventType);
                }
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Deprecated
    public static ServiceEventBus getInstance() {
        return ServiceEventBus.instance;
    }

    @Deprecated
    public static void fire(ServiceEvent event) {
        getInstance().submit(event);
    }

    private class ClassCache {
        private final ReadWriteLock lock = new ReentrantReadWriteLock();

        private final SetMultiMap<Class<? extends ServiceEvent>, Class<? extends ServiceEvent>> cache = MultiMaps
                .newSetMultiMap();

        public Set<Class<? extends ServiceEvent>> getClasses(Class<? extends ServiceEvent> eventClass) {
            lock.readLock().lock();
            try {
                Set<Class<? extends ServiceEvent>> r = cache.get(eventClass);
                if (r != null) {
                    return r;
                }
            } finally {
                lock.readLock().unlock();
            }
            lock.writeLock().lock();
            try {
                Set<Class<? extends ServiceEvent>> r = cache.get(eventClass);
                if (r != null) {
                    return r;
                }
                r = flatten(eventClass);
                cache.put(eventClass, r);
                return r;
            } finally {
                lock.writeLock().unlock();
            }
        }

        private Set<Class<? extends ServiceEvent>> flatten(Class<? extends ServiceEvent> eventClass) {
            return ClassHelper.flattenPartialHierachy(ServiceEvent.class, eventClass);
        }
    }

    private class HandlerExecution implements Runnable {
        private final ServiceEvent event;

        private final ServiceEventListener listener;

        HandlerExecution(ServiceEvent event, ServiceEventListener listener) {
            this.event = event;
            this.listener = listener;
        }

        @Override
        public void run() {
            try {
                LOG.debug("Submitting Event {} to Listener {}", event, listener);
                listener.handle(event);
            } catch (final Throwable t) {
                LOG.error(String.format("Error handling event %s by handler %s", event, listener), t);
            }
        }
    }
}
