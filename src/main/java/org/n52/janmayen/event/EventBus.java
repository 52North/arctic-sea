/*
 * Copyright 2016-2017 52°North Initiative for Geospatial Open Source
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
package org.n52.janmayen.event;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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

import org.n52.janmayen.Classes;
import org.n52.janmayen.GroupedAndNamedThreadFactory;
import org.n52.janmayen.function.Functions;
import org.n52.janmayen.lifecycle.Constructable;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * The {@link EventListener} are registered to the {@link EventBus} which delegates the fired {@link Event} to the
 * {@link EventListener}.
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
@SuppressFBWarnings("ST_WRITE_TO_STATIC_FROM_INSTANCE_METHOD")
public class EventBus implements Constructable {
    private static final Logger LOG = LoggerFactory.getLogger(EventBus.class);
    @Deprecated
    private static EventBus instance;
    private static final int THREAD_POOL_SIZE = 3;
    private static final String THREAD_GROUP_NAME = "EventBus-Worker";

    private final ClassCache classCache;
    private final ReadWriteLock lock;
    private final ThreadFactory threadFactory;
    private final Executor executor;
    private final Map<Class<? extends Event>, Set<EventListener>> listeners;
    private final Queue<HandlerExecution> queue;
    private boolean async;

    public EventBus() {
        this.classCache = new ClassCache();
        this.lock = new ReentrantReadWriteLock();
        this.threadFactory = new GroupedAndNamedThreadFactory(THREAD_GROUP_NAME);
        this.executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE, threadFactory);
        this.listeners = new HashMap<>();
        this.queue = new ConcurrentLinkedQueue<>();
    }

    public void setAsync(boolean async) {
        this.async = async;
    }

    @Override
    public void init() {
        EventBus.instance = this;
    }

    private boolean checkEvent(Event event) {
        if (event == null) {
            LOG.warn("Submitted event is null!");
            return false;
        }
        return true;
    }

    private boolean checkListener(EventListener listener) {
        if (listener == null) {
            LOG.warn("Tried to unregister ServiceEventListener null");
            return false;
        }
        if (listener.getTypes() == null || listener.getTypes().isEmpty()) {
            LOG.warn("Listener {} has no EventTypes: {}", listener, listener.getTypes());
            return false;
        }
        return true;
    }

    private Set<EventListener> getListenersForEvent(final Event event) {
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
     * Submit the fired {@link Event} to the registered {@link EventListener} and initiate the handling of the
     * {@link Event}
     *
     * @param event Submitted {@link Event}
     */
    public void submit(Event event) {
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
     * Register a new {@link EventListener} to the {@link EventBus}.
     *
     * @param listener {@link EventListener} to register
     */
    public void register(EventListener listener) {
        if (!checkListener(listener)) {
            return;
        }
        lock.writeLock().lock();
        try {
            listener.getTypes().stream()
                    .peek(type -> LOG.debug("Subscibing Listener {} to EventType {}", listener, type))
                    .map(type -> listeners.computeIfAbsent(type, Functions.forSupplier(HashSet::new)))
                    .forEach(set -> set.add(listener));
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Unregister a new {@link EventListener} to the {@link EventBus}.
     *
     * @param listener {@link EventListener} to unregister
     */
    public void unregister(EventListener listener) {
        if (!checkListener(listener)) {
            return;
        }
        lock.writeLock().lock();
        try {
            listener.getTypes().forEach(eventType -> unregister(listener, eventType));
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void unregister(EventListener listener, Class<? extends Event> eventType) {
        Set<EventListener> listenersForKey = listeners.get(eventType);
        if (listenersForKey.contains(listener)) {
            LOG.debug("Unsubscibing Listener {} from EventType {}", listener, eventType);
            listenersForKey.remove(listener);
        } else {
            LOG.warn("Listener {} was not registered for SosEvent Type {}", listener, eventType);
        }
    }

    @Deprecated
    public static EventBus getInstance() {
        return EventBus.instance;
    }

    @Deprecated
    public static void fire(Event event) {
        getInstance().submit(event);
    }

    private static class ClassCache {
        private final ReadWriteLock lock = new ReentrantReadWriteLock();

        private final Map<Class<? extends Event>, Set<Class<? extends Event>>> cache = new HashMap<>(15);

        public Set<Class<? extends Event>> getClasses(Class<? extends Event> eventClass) {
            lock.readLock().lock();
            try {
                Set<Class<? extends Event>> r = cache.get(eventClass);
                if (r != null) {
                    return r;
                }
            } finally {
                lock.readLock().unlock();
            }
            lock.writeLock().lock();
            try {
                Set<Class<? extends Event>> r = cache.get(eventClass);
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

        private Set<Class<? extends Event>> flatten(Class<? extends Event> eventClass) {
            return Classes.flattenPartialHierachy(Event.class, eventClass);
        }
    }

    private static class HandlerExecution implements Runnable {
        private final Event event;

        private final EventListener listener;

        HandlerExecution(Event event, EventListener listener) {
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
