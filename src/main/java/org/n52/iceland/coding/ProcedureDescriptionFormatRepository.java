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
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.inject.Inject;

import org.n52.iceland.coding.encode.Encoder;
import org.n52.iceland.coding.encode.ProcedureDescriptionFormatKey;
import org.n52.iceland.coding.encode.ProcedureEncoder;
import org.n52.iceland.lifecycle.Constructable;
import org.n52.iceland.service.operator.ServiceOperatorKey;
import org.n52.iceland.service.operator.ServiceOperatorRepository;
import org.n52.iceland.util.activation.ActivationListener;
import org.n52.iceland.util.activation.ActivationListeners;
import org.n52.iceland.util.activation.ActivationManager;
import org.n52.iceland.util.activation.ActivationSource;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class ProcedureDescriptionFormatRepository
        implements Constructable,
                   ActivationManager<ProcedureDescriptionFormatKey>,
                   ActivationSource<ProcedureDescriptionFormatKey> {

    @Deprecated
    private static ProcedureDescriptionFormatRepository instance;
    private final ActivationListeners<ProcedureDescriptionFormatKey> activation = new ActivationListeners<>(true);
    private final Map<String, Map<String, Set<String>>> procedureDescriptionFormats = Maps.newHashMap();
    private final Set<ProcedureDescriptionFormatKey> keys = new HashSet<>();
    private EncoderRepository encoderRepository;
    private ServiceOperatorRepository serviceOperatorRepository;

    @Override
    public void init() {
        ProcedureDescriptionFormatRepository.instance = this;

        Objects.requireNonNull(this.encoderRepository);
        Objects.requireNonNull(this.serviceOperatorRepository);

        generateProcedureDescriptionFormatMaps();
    }

    private void generateProcedureDescriptionFormatMaps() {
        this.procedureDescriptionFormats.clear();
        this.keys.clear();
        Set<ServiceOperatorKey> serviceOperatorKeyTypes
                = this.serviceOperatorRepository.getServiceOperatorKeys();
        for (Encoder<?, ?> encoder : this.encoderRepository.getEncoders()) {
            if (encoder instanceof ProcedureEncoder) {
                ProcedureEncoder<?, ?> procedureEncoder = (ProcedureEncoder<?, ?>) encoder;
                for (ServiceOperatorKey sokt : serviceOperatorKeyTypes) {
                    Set<String> formats = procedureEncoder.getSupportedProcedureDescriptionFormats(sokt.getService(), sokt.getVersion());
                    if (formats != null) {
                        for (String format : formats) {
                            addProcedureDescriptionFormat(new ProcedureDescriptionFormatKey(sokt, format));
                        }
                    }

                }
            }
        }
    }

    @Inject
    public void setEncoderRepository(EncoderRepository repository) {
        this.encoderRepository = repository;
    }

    @Inject
    public void setServiceOperatorRepository(ServiceOperatorRepository repository) {
        this.serviceOperatorRepository = repository;
    }

    protected void addProcedureDescriptionFormat(ProcedureDescriptionFormatKey key) {
        this.keys.add(key);
        Map<String, Set<String>> byService = this.procedureDescriptionFormats.get(key.getService());
        if (byService == null) {
            this.procedureDescriptionFormats.put(key.getService(), byService = Maps.newHashMap());
        }
        Set<String> byVersion = byService.get(key.getVersion());
        if (byVersion == null) {
            byService.put(key.getVersion(), byVersion = Sets.newHashSet());
        }
        byVersion.add(key.getProcedureDescriptionFormat());
    }

    public Map<ServiceOperatorKey, Set<String>> getSupportedProcedureDescriptionFormats() {
        Map<ServiceOperatorKey, Set<String>> map = Maps.newHashMap();
        for (ServiceOperatorKey sokt : this.serviceOperatorRepository.getServiceOperatorKeys()) {
            map.put(sokt, getSupportedProcedureDescriptionFormats(sokt));
        }
        return map;
    }

    public Set<String> getSupportedProcedureDescriptionFormats(ServiceOperatorKey sokt) {
        return getSupportedProcedureDescriptionFormats(sokt.getService(), sokt.getVersion());
    }

    public Set<String> getSupportedProcedureDescriptionFormats(String service, String version) {
        Map<String, Set<String>> byService = procedureDescriptionFormats.get(service);
        if (byService == null) {
            return Collections.emptySet();
        }
        Set<String> rfs = byService.get(version);
        if (rfs == null) {
            return Collections.emptySet();
        }

        ServiceOperatorKey sokt = new ServiceOperatorKey(service, version);
        Set<String> result = Sets.newHashSet();
        for (String a : rfs) {
            if (isActive(new ProcedureDescriptionFormatKey(sokt, a))) {
                result.add(a);
            }
        }
        return result;
    }

    public Map<ServiceOperatorKey, Set<String>> getAllProcedureDescriptionFormats() {
        Map<ServiceOperatorKey, Set<String>> map = Maps.newHashMap();
        for (ServiceOperatorKey sokt : this.serviceOperatorRepository.getServiceOperatorKeys()) {
            map.put(sokt, getAllSupportedProcedureDescriptionFormats(sokt));
        }
        return map;
    }

    public Set<String> getAllSupportedProcedureDescriptionFormats(String service, String version) {
        Map<String, Set<String>> byService = procedureDescriptionFormats.get(service);
        if (byService == null) {
            return Collections.emptySet();
        }
        Set<String> formats = byService.get(version);
        if (formats == null) {
            return Collections.emptySet();
        }
        return Collections.unmodifiableSet(formats);
    }

    public Set<String> getAllSupportedProcedureDescriptionFormats(ServiceOperatorKey sokt) {
        return getAllSupportedProcedureDescriptionFormats(sokt.getService(), sokt.getVersion());
    }

    @Override
    public void activate(ProcedureDescriptionFormatKey key) {
        this.activation.activate(key);
    }

    @Override
    public void deactivate(ProcedureDescriptionFormatKey key) {
        this.activation.deactivate(key);
    }

    @Override
    public boolean isActive(ProcedureDescriptionFormatKey key) {
        return this.activation.isActive(key);
    }

    @Override
    public void registerListener(ActivationListener<ProcedureDescriptionFormatKey> listener) {
        this.activation.registerListener(listener);
    }

    @Override
    public void deregisterListener(ActivationListener<ProcedureDescriptionFormatKey> listener) {
        this.activation.deregisterListener(listener);
    }

    @Override
    public void setActive(ProcedureDescriptionFormatKey key, boolean active) {
        this.activation.setActive(key, active);
    }

    @Override
    public Set<ProcedureDescriptionFormatKey> getKeys() {
        return Collections.unmodifiableSet(this.keys);
    }

    @Deprecated
    public static ProcedureDescriptionFormatRepository getInstance() {
        return instance;
    }
}
