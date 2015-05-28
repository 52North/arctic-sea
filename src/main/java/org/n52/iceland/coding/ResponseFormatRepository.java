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
package org.n52.iceland.coding;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.n52.iceland.config.SettingsManager;
import org.n52.iceland.encode.Encoder;
import org.n52.iceland.encode.ObservationEncoder;
import org.n52.iceland.encode.ResponseFormatKey;
import org.n52.iceland.lifecycle.Constructable;
import org.n52.iceland.service.operator.ServiceOperatorKey;
import org.n52.iceland.service.operator.ServiceOperatorRepository;
import org.n52.iceland.util.activation.ActivationListener;
import org.n52.iceland.util.activation.ActivationListeners;
import org.n52.iceland.util.activation.ActivationManager;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class ResponseFormatRepository implements ActivationManager<ResponseFormatKey>, Constructable {
    @Deprecated
    private static ResponseFormatRepository instance;
    private final Map<String, Map<String, Set<String>>> responseFormats = Maps.newHashMap();
    private final ActivationListeners<ResponseFormatKey> activation = new ActivationListeners<>(true);

    private ServiceOperatorRepository serviceOperatorRepository;
    private EncoderRepository encoderRepository;

    @Inject
    public void setEncoderRepository(EncoderRepository encoderRepository) {
        this.encoderRepository = encoderRepository;
    }

    @Inject
    public void setServiceOperatorRepository(ServiceOperatorRepository serviceOperatorRepository) {
        this.serviceOperatorRepository = serviceOperatorRepository;
    }

    @Override
    public void init() {
        ResponseFormatRepository.instance = this;
        generateResponseFormatMaps();
    }

    private void generateResponseFormatMaps() {
        this.responseFormats.clear();
        Set<ServiceOperatorKey> serviceOperatorKeyTypes
                = getServiceOperatorKeys();

        for (Encoder<?, ?> encoder : this.encoderRepository.getEncoders()) {
            if (encoder instanceof ObservationEncoder) {
                ObservationEncoder<?, ?> observationEncoder = (ObservationEncoder<?, ?>) encoder;
                for (ServiceOperatorKey key : serviceOperatorKeyTypes) {
                    Set<String> responseFormats = observationEncoder.getSupportedResponseFormats(key.getService(), key.getVersion());
                    if (responseFormats != null) {
                        for (String responseFormat : responseFormats) {
                            addResponseFormat(new ResponseFormatKey(key, responseFormat));
                        }
                    }
                }
            }
        }
    }

    protected void addResponseFormat(ResponseFormatKey key) {
        Map<String, Set<String>> byService = this.responseFormats.get(key.getService());
        if (byService == null) {
            this.responseFormats.put(key.getService(), byService = Maps.newHashMap());
        }
        Set<String> byVersion = byService.get(key.getVersion());
        if (byVersion == null) {
            byService.put(key.getVersion(), byVersion = Sets.newHashSet());
        }
        byVersion.add(key.getResponseFormat());
    }

    public Set<String> getSupportedResponseFormats(ServiceOperatorKey sokt) {
        return getSupportedResponseFormats(sokt.getService(), sokt.getVersion());
    }

    public Set<String> getSupportedResponseFormats(String service,
                                                   String version) {
        Map<String, Set<String>> byService = responseFormats.get(service);
        if (byService == null) {
            return Collections.emptySet();
        }
        Set<String> responseFormats = byService.get(version);
        if (responseFormats == null) {
            return Collections.emptySet();
        }

        ServiceOperatorKey sokt = new ServiceOperatorKey(service, version);
        Set<String> result = Sets.newHashSet();
        for (String responseFormat : responseFormats) {
            ResponseFormatKey rfkt = new ResponseFormatKey(sokt, responseFormat);
            if (isActive(rfkt)) {
                result.add(responseFormat);
            }
        }
        return result;
    }

    public Set<String> getAllSupportedResponseFormats(String service,
                                                      String version) {
        Map<String, Set<String>> byService = this.responseFormats.get(service);
        if (byService == null) {
            return Collections.emptySet();
        }
        Set<String> rfs = byService.get(version);
        if (rfs == null) {
            return Collections.emptySet();
        }
        return Collections.unmodifiableSet(rfs);
    }

    public Map<ServiceOperatorKey, Set<String>> getAllSupportedResponseFormats() {
        Map<ServiceOperatorKey, Set<String>> map = Maps.newHashMap();
        for (ServiceOperatorKey sokt : getServiceOperatorKeys()) {
            map.put(sokt, getAllSupportedResponseFormats(sokt));
        }
        return map;
    }

    private Set<ServiceOperatorKey> getServiceOperatorKeys() {
        return this.serviceOperatorRepository
                .getServiceOperatorKeys();
    }

    public Set<String> getAllSupportedResponseFormats(ServiceOperatorKey sokt) {
        return getAllSupportedResponseFormats(sokt.getService(),
                                              sokt.getVersion());
    }

     public Map<ServiceOperatorKey, Set<String>> getSupportedResponseFormats() {
        Map<ServiceOperatorKey, Set<String>> map = Maps.newHashMap();
        for (ServiceOperatorKey sokt : getServiceOperatorKeys()) {
            map.put(sokt, getSupportedResponseFormats(sokt));
        }
        return map;
    }

    public void setActive(ResponseFormatKey rfkt, boolean active) {
        this.activation.setActive(rfkt, active);
    }

    @Override
    public void activate(ResponseFormatKey key) {
        this.activation.activate(key);
    }

    @Override
    public void deactivate(ResponseFormatKey key) {
        this.activation.deactivate(key);
    }

    @Override
    public void registerListener(ActivationListener<ResponseFormatKey> listener) {
        this.activation.registerListener(listener);
    }

    @Override
    public void deregisterListener(ActivationListener<ResponseFormatKey> listener) {
        this.activation.deregisterListener(listener);
    }

    @Override
    public boolean isActive(ResponseFormatKey key) {
        return this.activation.isActive(key);
    }

    @Deprecated
    public static ResponseFormatRepository getInstance() {
        return instance;
    }

}
