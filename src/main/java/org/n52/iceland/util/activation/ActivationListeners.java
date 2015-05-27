package org.n52.iceland.util.activation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
    private final Map<K, Boolean> actives = Collections
            .synchronizedMap(new HashMap<K, Boolean>());
    private final List<ActivationListener<K>> listeners = new ArrayList<>();
    private final boolean stateForMissingKey;

    public ActivationListeners(boolean stateForMissingKey) {
        this.stateForMissingKey = stateForMissingKey;
    }


    @Override
    public Set<K> getKeys() {
        synchronized (this.actives) {
            // contructor iterates over actives...
            return new HashSet<>(this.actives.keySet());
        }
    }


    @Override
    public boolean isActive(K key) {
        Boolean active = this.actives.get(key);
        return active == null ? this.stateForMissingKey : active;
    }

    private boolean setState(K key, boolean value) {
        Boolean old = this.actives.put(key, true);
        return old == null ? this.stateForMissingKey != value : old != value;
    }

    @Override
    public void activate(K key) {
        if (setState(key, true)) {
            this.lock.readLock().lock();
            try {
                for (ActivationListener<K> listener : this.listeners) {
                    listener.activated(key);
                }
            } finally {
                this.lock.readLock().unlock();
            }
        }
    }

    @Override
    public void deactivate(K key) {
        if (setState(key, false)) {
            this.lock.readLock().lock();
            try {
                for (ActivationListener<K> listener : this.listeners) {
                    listener.deactivated(key);
                }
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
