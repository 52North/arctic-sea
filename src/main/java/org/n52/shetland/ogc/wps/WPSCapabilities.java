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
package org.n52.shetland.ogc.wps;

import java.util.Collection;
import java.util.Optional;

import org.n52.shetland.ogc.ows.OwsCapabilities;
import org.n52.shetland.ogc.ows.OwsCapabilitiesExtension;
import org.n52.shetland.ogc.ows.OwsOperationsMetadata;
import org.n52.shetland.ogc.ows.OwsServiceIdentification;
import org.n52.shetland.ogc.ows.OwsServiceProvider;

public class WPSCapabilities extends OwsCapabilities {

    private final Optional<ProcessOfferings> processOfferings;

    public WPSCapabilities(String version, String updateSequence,
                           OwsServiceIdentification serviceIdentification,
                           OwsServiceProvider serviceProvider,
                           OwsOperationsMetadata operationsMetadata,
                           Collection<String> languages,
                           Collection<OwsCapabilitiesExtension> extension,
                           ProcessOfferings processOfferings) {
        this(WPSConstants.SERVICE, version, updateSequence, serviceIdentification, serviceProvider, operationsMetadata, languages, extension, processOfferings);
    }

    public WPSCapabilities(String service, String version, String updateSequence,
                           OwsServiceIdentification serviceIdentification,
                           OwsServiceProvider serviceProvider,
                           OwsOperationsMetadata operationsMetadata,
                           Collection<String> languages,
                           Collection<OwsCapabilitiesExtension> extension,
                           ProcessOfferings processOfferings) {
        super(service, version, updateSequence, serviceIdentification, serviceProvider, operationsMetadata, languages, extension);
        this.processOfferings = Optional.ofNullable(processOfferings);
    }

    public WPSCapabilities(OwsCapabilities other, ProcessOfferings processOfferings) {
        super(other);
        this.processOfferings = Optional.ofNullable(processOfferings);
    }

    public WPSCapabilities(WPSCapabilities other) {
        this(other, other.getProcessOfferings().orElse(null));
    }

    public Optional<ProcessOfferings> getProcessOfferings() {
        return processOfferings;
    }

}
