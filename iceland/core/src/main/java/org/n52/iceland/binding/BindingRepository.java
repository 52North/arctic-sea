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
package org.n52.iceland.binding;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;

import org.n52.iceland.util.activation.Activatables;
import org.n52.iceland.util.activation.ActivationListener;
import org.n52.iceland.util.activation.ActivationListeners;
import org.n52.iceland.util.activation.ActivationManager;
import org.n52.iceland.util.activation.ActivationSource;
import org.n52.janmayen.Producer;
import org.n52.janmayen.Producers;
import org.n52.janmayen.component.AbstractComponentRepository;
import org.n52.janmayen.http.MediaType;
import org.n52.janmayen.lifecycle.Constructable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

/**
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
public class BindingRepository extends AbstractComponentRepository<BindingKey, Binding, BindingFactory>
        implements ActivationManager<BindingKey>,
                   ActivationSource<BindingKey>,
                   Constructable {
    private static final Logger LOG = LoggerFactory.getLogger(BindingRepository.class);
    private final ActivationListeners<BindingKey> activation = new ActivationListeners<>(true);

    private final Map<BindingKey, Producer<Binding>> bindings = Maps.newHashMap();

    private Optional<Collection<Binding>> components;
    private Optional<Collection<BindingFactory>> componentFactories;

    @Inject
    public void setComponentFactories(Optional<Collection<BindingFactory>> componentFactories) {
        this.componentFactories = componentFactories;
    }

    @Inject
    public void setComponents(Optional<Collection<Binding>> components) {
        this.components = components;
    }

    @Override
    public void registerListener(ActivationListener<BindingKey> listener) {
        this.activation.registerListener(listener);
    }

    @Override
    public void deregisterListener(ActivationListener<BindingKey> listener) {
        this.activation.deregisterListener(listener);
    }

    @Override
    public boolean isActive(BindingKey key) {
        return this.activation.isActive(key);
    }

    public boolean isActive(String urlPattern) {
        return isActive(new PathBindingKey(urlPattern));
    }

    public boolean isActive(MediaType mediaType) {
        return isActive(new MediaTypeBindingKey(mediaType));
    }

    @Override
    public void activate(BindingKey key) {
        this.activation.activate(key);
    }

    @Override
    public void deactivate(BindingKey key) {
        this.activation.deactivate(key);
    }

    @Override
    public Set<BindingKey> getKeys() {
        return Collections.unmodifiableSet(this.bindings.keySet());
    }

    @Override
    public void setActive(BindingKey bk, boolean active) {
        this.activation.setActive(bk, active);
    }

    @Override
    public void init() {
        Map<BindingKey, Producer<Binding>> implementations
                = getUniqueProviders(this.components, this.componentFactories);
        this.bindings.clear();
        for (Entry<BindingKey, Producer<Binding>> entry : implementations.entrySet()) {
            BindingKey key = entry.getKey();
            Producer<Binding> binding = entry.getValue();
            this.bindings.put(key, binding);
        }
        if (this.bindings.isEmpty()) {
            final StringBuilder exceptionText = new StringBuilder();
            exceptionText.append("No Binding implementation could be loaded! ");
            exceptionText.append("If the service is not used as a webapp, this has no effect! ");
            exceptionText.append("Please add a Binding implementation!");
            LOG.warn(exceptionText.toString());
        }
    }

    public Binding getBinding(String urlPattern) {
        return getBinding(new PathBindingKey(urlPattern));
    }

    public Binding getBinding(MediaType mediaType) {
        return getBinding(new MediaTypeBindingKey(mediaType));
    }

    public Binding getBinding(BindingKey key) {
        Producer<Binding> binding = this.bindings.get(key);
        return binding == null ? null : binding.get();
    }

    public boolean isBindingSupported(String urlPattern) {
        return isActive(new PathBindingKey(urlPattern));
    }

    public boolean isBindingSupported(MediaType mediaType) {
        return isActive(new MediaTypeBindingKey(mediaType));
    }

    public boolean isBindingSupported(BindingKey key) {
        return isActive(key);
    }

    public Map<BindingKey, Binding> getBindings() {
        Map<BindingKey, Producer<Binding>> actives
                = Activatables.activatedMap(this.bindings,
                                            this.activation);
        return Producers.produce(actives);
    }

    public Map<MediaType, Binding> getBindingsByMediaType() {
        Map<MediaType, Binding> map = new HashMap<>(this.bindings.size());
        for (Entry<BindingKey, Producer<Binding>> entry : this.bindings.entrySet()) {
            if (entry.getKey() instanceof MediaTypeBindingKey) {
                MediaTypeBindingKey key = (MediaTypeBindingKey) entry.getKey();
                Producer<Binding> producer = entry.getValue();
                if (isActive(key)) {
                    map.put(key.getMediaType(), producer.get());
                }
            }
        }
        return map;
    }

    public Map<MediaType, Binding> getAllBindingsByMediaType() {
        Map<MediaType, Binding> map = new HashMap<>(this.bindings.size());
        for (Entry<BindingKey, Producer<Binding>> entry : this.bindings.entrySet()) {
            if (entry.getKey() instanceof MediaTypeBindingKey) {
                MediaTypeBindingKey key = (MediaTypeBindingKey) entry.getKey();
                Producer<Binding> producer = entry.getValue();
                map.put(key.getMediaType(), producer.get());
            }
        }
        return map;
    }

}
