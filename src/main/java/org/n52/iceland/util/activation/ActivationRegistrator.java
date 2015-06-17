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
package org.n52.iceland.util.activation;

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

    private final Map<ActivationListenable, ActivationListener> listeners;
    private final Map<ActivationSink, ActivationInitializer> initializers;

    public ActivationRegistrator() {
        this.listeners = Maps.newHashMap();
        this.initializers = Maps.newHashMap();
    }

    public void setListeners(Map<ActivationListenable, ActivationListener> listeners) {
        if (listeners != null) {
            this.listeners.putAll(listeners);
        }
    }

    public void setInitializers(Map<ActivationSink, ActivationInitializer> initializers) {
        if (initializers != null) {
            this.initializers.putAll(initializers);
        }
    }

    @Override
    public void init() {
        initializeManagers();
        registerListeners();
    }

    @SuppressWarnings("unchecked")
    private void initializeManagers() {
        for (Entry<ActivationSink, ActivationInitializer> entry : this.initializers.entrySet()) {
            entry.getValue().initialize(entry.getKey());
        }
    }

    @SuppressWarnings("unchecked")
    private void registerListeners() {
        for (Entry<ActivationListenable, ActivationListener> mapping : this.listeners.entrySet()) {
            mapping.getKey().registerListener(mapping.getValue());
        }
    }



}
