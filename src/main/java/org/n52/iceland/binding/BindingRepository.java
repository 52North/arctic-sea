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
package org.n52.iceland.binding;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.iceland.config.SettingsManager;
import org.n52.iceland.ds.ConnectionProviderException;
import org.n52.iceland.exception.ConfigurationException;
import org.n52.iceland.util.Activatable;
import org.n52.iceland.util.Producer;
import org.n52.iceland.util.http.MediaType;
import org.n52.iceland.component.AbstractComponentRepository;
import org.n52.iceland.component.AbstractUniqueKeyComponentRepository;

import com.google.common.collect.Maps;
import com.google.common.collect.SetMultimap;

/**
 * @author Christian Autermann <c.autermann@52north.org>
 *
 * @since 4.0.0
 */
public class BindingRepository extends AbstractUniqueKeyComponentRepository<BindingKey, Binding, BindingFactory> {
    private static final Logger LOG = LoggerFactory.getLogger(BindingRepository.class);
    private static BindingRepository instance;

    @Deprecated
    public static BindingRepository getInstance() {
        return BindingRepository.instance;
    }

    private final Map<PathBindingKey, Activatable<Producer<Binding>>> byPath = Maps.newHashMap();
    private final Map<MediaTypeBindingKey, Activatable<Producer<Binding>>> byMediaType = Maps.newHashMap();
    private final Map<BindingKey, Activatable<Producer<Binding>>> bindings = Maps.newHashMap();

    @Inject
    private SettingsManager settingsManager;

    public BindingRepository() {
        super(Binding.class, BindingFactory.class);
        BindingRepository.instance = this;
    }

    @Override
    protected void processImplementations(Map<BindingKey, Producer<Binding>> bindings) {
        this.bindings.clear();
        this.byMediaType.clear();
        this.byPath.clear();
        for (Entry<BindingKey, Producer<Binding>> entry : bindings.entrySet()) {
            try {
                BindingKey key = entry.getKey();
                Producer<Binding> binding = entry.getValue();
                boolean isActive = this.settingsManager.isActive(key);
                Activatable<Producer<Binding>> activatable = Activatable.from(binding, isActive);
                this.bindings.put(key, activatable);
                if (key instanceof MediaTypeBindingKey) {
                    byMediaType.put((MediaTypeBindingKey) key, activatable);
                } else if (key instanceof PathBindingKey) {
                    byPath.put((PathBindingKey) key, activatable);
                }
            } catch (ConnectionProviderException ex) {
                throw new ConfigurationException("Could not check status of Binding", ex);
            }
        }
        if (this.bindings.isEmpty()) {
            final StringBuilder exceptionText = new StringBuilder();
            exceptionText.append("No Binding implementation could be loaded! ");
            exceptionText.append("If the SOS is not used as webapp, this has no effect! ");
            exceptionText.append("Else add a Binding implementation!");
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
        Activatable<Producer<Binding>> binding = this.bindings.get(key);
        return binding == null ? null : binding.get().get();
    }

    public boolean isBindingSupported(String urlPattern) {
        return isBindingSupported(new PathBindingKey(urlPattern));
    }

    public boolean isBindingSupported(MediaType mediaType) {
        return isBindingSupported(new MediaTypeBindingKey(mediaType));
    }

    public boolean isBindingSupported(BindingKey key) {
        return this.bindings.containsKey(key) &&
               this.bindings.get(key).isActive();
    }

    public Map<BindingKey, Binding> getBindings() {
        return produce(Activatable.filter(this.bindings));
    }

    public Map<String, Binding> getBindingsByPath() {
        return getByPath(Activatable.filter(this.bindings));
    }

    public Map<MediaType, Binding> getBindingsByMediaType() {
        return getByMediaType(Activatable.filter(this.bindings));
    }

    public Map<String, Binding> getAllBindingsByPath() {
        return getByPath(Activatable.unfiltered(this.bindings));
    }

    public Map<MediaType, Binding> getAllBindingsByMediaType() {
        return getByMediaType(Activatable.unfiltered(this.bindings));
    }

    private Map<MediaType, Binding> getByMediaType(Map<BindingKey, Producer<Binding>> b) {
        Map<MediaType, Binding> result = new HashMap<>(b.size()/2);
        for (BindingKey key : b.keySet()) {
            if (key instanceof MediaTypeBindingKey) {
                MediaTypeBindingKey mediaTypeBindingKey = (MediaTypeBindingKey) key;
                result.put(mediaTypeBindingKey.getMediaType(), b.get(key).get());
            }
        }
        return result;
    }

    private Map<String, Binding> getByPath(Map<BindingKey, Producer<Binding>> b) {
        Map<String, Binding> result = new HashMap<>(b.size()/2);
        for (BindingKey key : b.keySet()) {
            if (key instanceof PathBindingKey) {
                PathBindingKey pathBindingKey = (PathBindingKey) key;
                result.put(pathBindingKey.getPath(), b.get(key).get());
            }
        }
        return result;
    }

    public void setActive(final BindingKey bk, final boolean active) {
        if (this.bindings.containsKey(bk)) {
            this.bindings.get(bk).setActive(active);
        }
    }
}
