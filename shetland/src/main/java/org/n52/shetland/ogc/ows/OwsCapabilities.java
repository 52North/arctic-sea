/*
 * Copyright 2015-2017 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.ogc.ows;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;

import org.n52.shetland.util.CollectionHelper;

import com.google.common.base.Strings;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class OwsCapabilities {
    private Optional<String> service;
    private String version;
    private Optional<String> updateSequence;
    private Optional<OwsServiceIdentification> serviceIdentification;
    private Optional<OwsServiceProvider> serviceProvider;
    private Optional<OwsOperationsMetadata> operationsMetadata;
    private Optional<SortedSet<String>> languages;
    private SortedSet<OwsCapabilitiesExtension> extensions;

    public OwsCapabilities(String service,
                           String version,
                           String updateSequence,
                           OwsServiceIdentification serviceIdentification,
                           OwsServiceProvider serviceProvider,
                           OwsOperationsMetadata operationsMetadata,
                           Collection<String> languages,
                           Collection<OwsCapabilitiesExtension> extensions) {
        this.service = Optional.ofNullable(Strings.emptyToNull(service));
        this.version = Objects.requireNonNull(Strings.emptyToNull(version));
        this.updateSequence = Optional.ofNullable(Strings.emptyToNull(updateSequence));
        this.serviceIdentification = Optional.ofNullable(serviceIdentification);
        this.serviceProvider = Optional.ofNullable(serviceProvider);
        this.operationsMetadata = Optional.ofNullable(operationsMetadata);
        this.languages = Optional.ofNullable(languages).map(TreeSet<String>::new);
        this.extensions =
                Optional.ofNullable(extensions).map(TreeSet<OwsCapabilitiesExtension>::new).orElseGet(TreeSet::new);
    }

    public OwsCapabilities(OwsCapabilities other) {
        this(other.getService().orElse(null),
             other.getVersion(),
             other.getUpdateSequence().orElse(null),
             other.getServiceIdentification().orElse(null),
             other.getServiceProvider().orElse(null),
             other.getOperationsMetadata().orElse(null),
             other.getLanguages().orElse(null),
             other.getExtensions());
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = Objects.requireNonNull(Strings.emptyToNull(version));
    }

    public Optional<String> getService() {
        return service;
    }

    public void setService(String service) {
        this.service = Optional.ofNullable(Strings.emptyToNull(service));
    }

    public Optional<String> getUpdateSequence() {
        return updateSequence;
    }

    public void setUpdateSequence(String updateSequence) {
        this.updateSequence = Optional.ofNullable(Strings.emptyToNull(updateSequence));
    }

    public Optional<OwsServiceIdentification> getServiceIdentification() {
        return serviceIdentification;
    }

    public void setServiceIdentification(OwsServiceIdentification serviceIdentification) {
        this.serviceIdentification = Optional.ofNullable(serviceIdentification);
    }

    public Optional<OwsServiceProvider> getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(OwsServiceProvider serviceProvider) {
        this.serviceProvider = Optional.ofNullable(serviceProvider);
    }

    public Optional<OwsOperationsMetadata> getOperationsMetadata() {
        return operationsMetadata;
    }

    public void setOperationsMetadata(OwsOperationsMetadata operationsMetadata) {
        this.operationsMetadata = Optional.ofNullable(operationsMetadata);
    }

    public Optional<SortedSet<String>> getLanguages() {
        return this.languages;
    }

    public void setLanguages(SortedSet<String> languages) {
        this.languages = Optional.ofNullable(languages).map(TreeSet<String>::new);
    }

    public SortedSet<OwsCapabilitiesExtension> getExtensions() {
        return Collections.unmodifiableSortedSet(extensions);
    }

    public void setExtensions(Collection<OwsCapabilitiesExtension> extensions) {
        this.extensions = CollectionHelper.newSortedSet(extensions);
    }

}
