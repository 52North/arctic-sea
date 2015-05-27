package org.n52.iceland.util.activation;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.n52.iceland.lifecycle.Constructable;

import com.google.common.collect.Maps;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
@SuppressWarnings("rawtypes")
public class ActivationRegistrator implements Constructable {

    private final Map<ActivationListenable, List<ActivationListener>> listeners;
    private final Map<ActivationManager, ActivationInitializer> initializers;

    public ActivationRegistrator() {
        this.listeners = Maps.newHashMap();
        this.initializers = Maps.newHashMap();
    }

    public void setListeners(
            Map<ActivationListenable, List<ActivationListener>> listeners) {
        if (listeners != null) {
            this.listeners.putAll(listeners);
        }
    }

    public void setInitializers(
            Map<ActivationManager, ActivationInitializer> initializers) {
        if (initializers != null) {
            this.initializers.putAll(initializers);
        }

    }

    @Override
    public void init() {
        initializeManagers();
        registerListeners();
    }

    private void initializeManagers() {
        for (Entry<ActivationManager, ActivationInitializer> entry
                     : this.initializers.entrySet()) {
            ActivationManager manager = entry.getKey();
            ActivationInitializer initializer = entry.getValue();
            initializer.initialize(manager);
        }
    }

    @SuppressWarnings("unchecked")
    private void registerListeners() {
        for (Entry<ActivationListenable, List<ActivationListener>> mapping
                     : this.listeners.entrySet()) {
            ActivationListenable listenable = mapping.getKey();
            List<ActivationListener> listeners = mapping.getValue();
            for (ActivationListener listener : listeners) {
                listenable.registerListener(listener);
            }
        }
    }



}
