/*
 * Copyright 2016 52°North Initiative for Geospatial Open Source
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
package org.n52.iceland.ogc.ows;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.google.common.base.Strings;

/**
 * TODO JavaDoc
 * @author Christian Autermann
 */
public class OwsCapabilities {
    private final String service;
    private final String version;
    private final Optional<String> updateSequence;
    private final Optional<OwsServiceIdentification> serviceIdentification;
    private final Optional<OwsServiceProvider> serviceProvider;
    private final Optional<OwsOperationsMetadata> operationsMetadata;
    private final Optional<SortedSet<String>> languages;

    public OwsCapabilities(String service,
                           String version,
                           String updateSequence,
                           OwsServiceIdentification serviceIdentification,
                           OwsServiceProvider serviceProvider,
                           OwsOperationsMetadata operationsMetadata,
                           Set<String> languages) {
        this.service = Objects.requireNonNull(Strings.emptyToNull(service));
        this.version = Objects.requireNonNull(Strings.emptyToNull(version));
        this.updateSequence = Optional.ofNullable(Strings.emptyToNull(updateSequence));
        this.serviceIdentification = Optional.ofNullable(serviceIdentification);
        this.serviceProvider = Optional.ofNullable(serviceProvider);
        this.operationsMetadata = Optional.ofNullable(operationsMetadata);
        this.languages = Optional.ofNullable(languages).map(TreeSet::new);
    }

    public OwsCapabilities(OwsCapabilities other) {
        this(other.getService(),
             other.getVersion(),
             other.getUpdateSequence().orElse(null),
             other.getServiceIdentification().orElse(null),
             other.getServiceProvider().orElse(null),
             other.getOperationsMetadata().orElse(null),
             other.getLanguages().orElse(null));
    }

    public String getVersion() {
        return version;
    }

    public Optional<String> getUpdateSequence() {
        return updateSequence;
    }

    public Optional<OwsServiceIdentification> getServiceIdentification() {
        return serviceIdentification;
    }

    public Optional<OwsServiceProvider> getServiceProvider() {
        return serviceProvider;
    }

    public Optional<OwsOperationsMetadata> getOperationsMetadata() {
        return operationsMetadata;
    }

    public Optional<SortedSet<String>> getLanguages() {
        return this.languages;
    }

    public String getService() {
        return service;
    }
}
