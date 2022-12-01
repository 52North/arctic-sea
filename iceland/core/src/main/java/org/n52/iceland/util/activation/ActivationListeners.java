/*
 * Copyright (C) 2015-2022 52°North Spatial Information Research GmbH
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
package org.n52.iceland.util.activation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class ActivationListeners<K> implements ActivationManager<K> {
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Map<K, Boolean> actives;
    private final List<ActivationListener<K>> listeners;
    private final boolean stateForMissingKey;

    public ActivationListeners(boolean stateForMissingKey) {
        this.listeners = new ArrayList<>();
        this.actives = Collections.synchronizedMap(new HashMap<>());
        this.stateForMissingKey = stateForMissingKey;
    }

    public Set<K> getKeys() {
        synchronized (this.actives) {
            // contructor iterates over actives...
            return new HashSet<>(this.actives.keySet());
        }
    }

    @Override
    public boolean isActive(K key) {
        Boolean active = this.actives.get(key);
        if (active == null) {
            if (key instanceof DefaultActive) {
                return ((DefaultActive) key).isDefaultActive();
            }
            return this.stateForMissingKey;
        }
        return active;
    }

    private boolean setState(K key, boolean value) {
        Boolean old = this.actives.put(key, value);
        return old == null ? this.stateForMissingKey != value : old != value;
    }

    @Override
    public void setActive(K key, boolean value) {
        if (value) {
            activate(key);
        } else {
            deactivate(key);
        }
    }

    @Override
    public void activate(K key) {
        setState(key, true);
        this.lock.readLock().lock();
        try {
            this.listeners.forEach(l -> l.activated(key));
        } finally {
            this.lock.readLock().unlock();
        }
    }

    @Override
    public void deactivate(K key) {
        if (setState(key, false)) {
            this.lock.readLock().lock();
            try {
                this.listeners.forEach(l -> l.deactivated(key));
            } finally {
                this.lock.readLock().unlock();
            }
        }
    }

    @Override
    public void registerListener(ActivationListener<K> listener) {
        this.lock.writeLock().lock();
        try {
            this.listeners.add(listener);
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    @Override
    public void deregisterListener(ActivationListener<K> listener) {
        this.lock.writeLock().lock();
        try {
            this.listeners.remove(listener);
        } finally {
            this.lock.writeLock().unlock();
        }
    }
}
