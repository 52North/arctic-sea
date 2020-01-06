/*
 * Copyright 2015-2020 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.ogc.sos;


import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;

import org.n52.shetland.ogc.filter.FilterCapabilities;
import org.n52.shetland.ogc.ows.OwsCapabilities;
import org.n52.shetland.ogc.ows.OwsCapabilitiesExtension;
import org.n52.shetland.ogc.ows.OwsOperationsMetadata;
import org.n52.shetland.ogc.ows.OwsServiceIdentification;
import org.n52.shetland.ogc.ows.OwsServiceProvider;
import org.n52.shetland.util.CollectionHelper;

/**
 * Class which represents the Capabilities.
 *
 * @since 1.0.0
 *
 */
public class SosCapabilities extends OwsCapabilities {

    /**
     * Metadata for all supported filter
     */
    private Optional<FilterCapabilities> filterCapabilities;

    /**
     * All ObservationOfferings provided by this SOS.
     */
    private Optional<SortedSet<SosObservationOffering>> contents;

    public SosCapabilities(SosCapabilities owsCapabilities) {
        super(owsCapabilities);
        this.filterCapabilities = owsCapabilities.getFilterCapabilities();
        this.contents = owsCapabilities.getContents();
    }

    public SosCapabilities(OwsCapabilities owsCapabilities) {
        this(owsCapabilities, null, null);
    }

    public SosCapabilities(
            OwsCapabilities owsCapabilities, FilterCapabilities filterCapabilities,
            Collection<SosObservationOffering> contents) {
        super(owsCapabilities);
        this.filterCapabilities = Optional.ofNullable(filterCapabilities);
        this.contents = Optional.ofNullable(contents).map(CollectionHelper::newSortedSet);
    }

    public SosCapabilities(
            String service, String version, String updateSequence, OwsServiceIdentification serviceIdentification,
            OwsServiceProvider serviceProvider, OwsOperationsMetadata operationsMetadata, Set<String> languages,
            FilterCapabilities filterCapabilities, Collection<SosObservationOffering> contents,
            Collection<OwsCapabilitiesExtension> extensions) {
        super(SosConstants.SOS, version, updateSequence, serviceIdentification, serviceProvider, operationsMetadata,
                languages, extensions);
        this.filterCapabilities = Optional.ofNullable(filterCapabilities);
        this.contents = Optional.ofNullable(contents).map(CollectionHelper::newSortedSet);
    }

    /**
     * Get filter capabilities
     *
     * @return filter capabilities
     */
    public Optional<FilterCapabilities> getFilterCapabilities() {
        return filterCapabilities;
    }

    public void setFilterCapabilities(FilterCapabilities filterCapabilities) {
        this.filterCapabilities = Optional.ofNullable(filterCapabilities);
    }

    /**
     * Get contents data
     *
     * @return contents data
     */
    public Optional<SortedSet<SosObservationOffering>> getContents() {
        return this.contents.map(Collections::unmodifiableSortedSet);
    }

    public void setContents(Collection<SosObservationOffering> contents) {
        this.contents = Optional.ofNullable(contents).map(CollectionHelper::newSortedSet);
    }
}
