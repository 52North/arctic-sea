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
package org.n52.iceland.ogc.ows.extension;

import org.n52.janmayen.component.Component;
import org.n52.shetland.ogc.ows.OwsOperationMetadataExtension;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.shetland.ogc.ows.service.GetCapabilitiesRequest;

/**
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 *
 * @since 1.0.0
 *
 */
public interface OwsOperationMetadataExtensionProvider extends Component<OwsOperationMetadataExtensionProviderKey> {

    /**
     * Get the {@link OwsOperationMetadataExtension} for the
     * {@link GetCapabilitiesRequest}
     *
     * @param request
     *            Capabilities request
     * @return Resulting {@link OwsOperationMetadataExtension}
     * @throws OwsExceptionReport
     *             If an error occurs when creating extended capabilities
     */
    OwsOperationMetadataExtension getOwsExtendedCapabilities(GetCapabilitiesRequest request) throws OwsExceptionReport;

    boolean hasExtendedCapabilitiesFor(GetCapabilitiesRequest request);

}
