/*
 * Copyright 2015-2016 52°North Initiative for Geospatial Open Source
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

import java.util.Set;

import org.n52.iceland.component.Component;
import org.n52.iceland.exception.ows.OwsExceptionReport;
import org.n52.iceland.request.GetCapabilitiesRequest;

/**
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 *
 * @since 1.0.0
 *
 */
public interface OwsExtendedCapabilitiesProvider extends Component<OwsExtendedCapabilitiesProviderKey> {

    /**
     * Get the {@link OwsExtendedCapabilities} for the
     * {@link GetCapabilitiesRequest}
     *
     * @param request
     *            Capabilities request
     * @return Resulting {@link OwsExtendedCapabilities}
     * @throws OwsExceptionReport
     *             If an error occurs when creating extended capabilities
     */
    OwsExtendedCapabilities getOwsExtendedCapabilities(GetCapabilitiesRequest request) throws OwsExceptionReport;

    boolean hasExtendedCapabilitiesFor(GetCapabilitiesRequest request);

    @Deprecated
    default Set<OwsExtendedCapabilitiesProviderKey> getExtendedCapabilitiesKeyType() {
        return getKeys();
    }

}
