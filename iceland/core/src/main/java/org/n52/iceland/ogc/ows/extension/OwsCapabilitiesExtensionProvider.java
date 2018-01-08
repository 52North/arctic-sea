/*
 * Copyright 2015-2018 52°North Initiative for Geospatial Open Source
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
package org.n52.iceland.ogc.ows.extension;

import org.n52.janmayen.component.Component;
import org.n52.shetland.ogc.ows.OwsCapabilitiesExtension;

/**
 * Interface for OwsCapabilitiesExtensionProvider. Implementations of this interface are loaded by the
 * {@link OwsCapabilitiesExtensionRepository}.
 *
 * @since 1.0.0
 *
 */
public interface OwsCapabilitiesExtensionProvider extends Component<OwsCapabilitiesExtensionKey> {

    /**
     * Get the {@link OwsCapabilitiesExtension} the provider provides.
     *
     * @return provided CapabilitiesExtension
     */
    OwsCapabilitiesExtension getExtension();

    /**
     * Does this {@link OwsCapabilitiesExtension} related to a specific service operation
     *
     * @return <code>true</code>, if service relates to a specific service operation
     */
    boolean hasRelatedOperation();

    /**
     * Get the specific service operation name this {@link OwsCapabilitiesExtension} relates to.
     *
     * @return Related service operation name
     */
    String getRelatedOperation();
}
