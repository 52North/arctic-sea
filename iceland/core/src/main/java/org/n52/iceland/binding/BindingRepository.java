/*
 * Copyright 2015-2019 52°North Initiative for Geospatial Open Source
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

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

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
    @Deprecated
    private static BindingRepository instance;

    private final ActivationListeners<BindingKey> activation = new ActivationListeners<>(true);

    @Deprecated
    private final Map<PathBindingKey, Producer<Binding>> byPath = Maps.newHashMap();
    private final Map<MediaTypeBindingKey, Producer<Binding>> byMediaType = Maps.newHashMap();
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
        setStaticInstance();
        Map<BindingKey, Producer<Binding>> implementations
                = getUniqueProviders(this.components, this.componentFactories);
        this.bindings.clear();
        this.byMediaType.clear();
        this.byPath.clear();
        for (Entry<BindingKey, Producer<Binding>> entry : implementations.entrySet()) {
            BindingKey key = entry.getKey();
            Producer<Binding> binding = entry.getValue();
            this.bindings.put(key, binding);
            if (key instanceof MediaTypeBindingKey) {
                byMediaType.put((MediaTypeBindingKey) key, binding);
            } else if (key instanceof PathBindingKey) {
                byPath.put((PathBindingKey) key, binding);
            }
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

    @Deprecated
    public Map<String, Binding> getBindingsByPath() {
        Map<String, Binding> map = new HashMap<>(this.byPath.size());
        for (Entry<PathBindingKey, Producer<Binding>> entry : this.byPath.entrySet()) {
            PathBindingKey key = entry.getKey();
            Producer<Binding> producer = entry.getValue();
            if (isActive(key)) {
                map.put(key.getPath(), producer.get());
            }
        }
        return map;
    }

    public Map<MediaType, Binding> getBindingsByMediaType() {
        Map<MediaType, Binding> map = new HashMap<>(this.byMediaType.size());
        for (Entry<MediaTypeBindingKey, Producer<Binding>> entry : this.byMediaType.entrySet()) {
            MediaTypeBindingKey key = entry.getKey();
            Producer<Binding> producer = entry.getValue();
            if (isActive(key)) {
                map.put(key.getMediaType(), producer.get());
            }
        }
        return map;
    }

    @Deprecated
    public Map<String, Binding> getAllBindingsByPath() {
        Map<String, Binding> map = new HashMap<>(this.byPath.size());
        for (Entry<PathBindingKey, Producer<Binding>> entry : this.byPath.entrySet()) {
            PathBindingKey key = entry.getKey();
            Producer<Binding> producer = entry.getValue();
            map.put(key.getPath(), producer.get());
        }
        return map;
    }

    public Map<MediaType, Binding> getAllBindingsByMediaType() {
        Map<MediaType, Binding> map = new HashMap<>(this.byMediaType.size());
        for (Entry<MediaTypeBindingKey, Producer<Binding>> entry : this.byMediaType.entrySet()) {
            MediaTypeBindingKey key = entry.getKey();
            Producer<Binding> producer = entry.getValue();
            map.put(key.getMediaType(), producer.get());
        }
        return map;
    }

    @Deprecated
    @SuppressFBWarnings("ST_WRITE_TO_STATIC_FROM_INSTANCE_METHOD")
    private void setStaticInstance() {
        BindingRepository.instance = this;
    }

    @Deprecated
    public static BindingRepository getInstance() {
        return BindingRepository.instance;
    }
}
