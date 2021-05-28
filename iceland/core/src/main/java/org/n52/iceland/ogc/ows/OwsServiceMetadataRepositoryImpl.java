/*
 * Copyright (C) 2015-2021 52°North Spatial Information Research GmbH
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
package org.n52.iceland.ogc.ows;

import static java.util.stream.Collectors.toSet;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import javax.inject.Inject;

import org.n52.faroe.SettingsService;
import org.n52.iceland.service.operator.ServiceOperatorRepository;
import org.n52.iceland.util.LocalizedProducer;
import org.n52.shetland.ogc.ows.OwsServiceIdentification;
import org.n52.shetland.ogc.ows.OwsServiceProvider;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class OwsServiceMetadataRepositoryImpl implements OwsServiceMetadataRepository {

    private final Map<String, LocalizedProducer<OwsServiceProvider>> serviceProviders;
    private final Map<String, LocalizedProducer<OwsServiceIdentification>> serviceIdentifications;

    private SettingsService settingsService;
    private ServiceOperatorRepository serviceOperatorRepository;

    public OwsServiceMetadataRepositoryImpl() {
        this.serviceProviders = Collections.synchronizedMap(new HashMap<>());
        this.serviceIdentifications = Collections.synchronizedMap(new HashMap<>());
    }

    @Inject
    public void setServiceOperatorRepository(ServiceOperatorRepository serviceOperatorRepository) {
        this.serviceOperatorRepository = serviceOperatorRepository;
    }

    @Inject
    public void setSettingsService(SettingsService settingsService) {
        this.settingsService = settingsService;
    }

    @Override
    public LocalizedProducer<OwsServiceIdentification> getServiceIdentificationFactory(String service) {
        return this.serviceIdentifications.computeIfAbsent(service, this::createServiceIdentificationFactory);
    }

    @Override
    public LocalizedProducer<OwsServiceProvider> getServiceProviderFactory(String service) {
        return this.serviceProviders.computeIfAbsent(service, this::createServiceProviderFactory);
    }

    private LocalizedProducer<OwsServiceProvider> createServiceProviderFactory(String service) {
        OwsServiceProviderFactory factory = new OwsServiceProviderFactory();
        this.settingsService.configure(factory);
        return factory;
    }

    private LocalizedProducer<OwsServiceIdentification> createServiceIdentificationFactory(String service) {
        OwsServiceIdentificationFactory factory = new OwsServiceIdentificationFactory(service,
                                                                                      this.serviceOperatorRepository);
        this.settingsService.configure(factory);
        return factory;
    }

    @Override
    public Set<Locale> getAvailableLocales() {
        Set<Locale> forProviders;
        Set<Locale> forIdentifications;

        synchronized (this.serviceProviders) {
            forProviders = this.serviceProviders.values().stream()
                    .map(LocalizedProducer::getAvailableLocales)
                    .flatMap(Set::stream).collect(toSet());
        }
        synchronized (this.serviceIdentifications) {
            forIdentifications = this.serviceIdentifications.values().stream()
                    .map(LocalizedProducer::getAvailableLocales)
                    .flatMap(Set::stream).collect(toSet());
        }

        return Stream.of(forProviders, forIdentifications)
                .flatMap(Set::stream).collect(toSet());
    }
}
