/*
 * Copyright 2015-${currentYear} 52°North Spatial Information Research GmbH
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
package org.n52.iceland.service.operator;

import org.n52.janmayen.component.Component;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.shetland.ogc.ows.service.OwsServiceKey;
import org.n52.shetland.ogc.ows.service.OwsServiceRequest;
import org.n52.shetland.ogc.ows.service.OwsServiceResponse;

/**
 * Interface for the request listeners of a service, e.g. SOS 2.0 or SOS 1.0.0.
 *
 * @since 1.0.0
 */
public interface ServiceOperator extends Component<OwsServiceKey> {

    /**
     * method handles the incoming operation request and returns a matching
     * response or an ServiceExceptionReport if the service was not able to build a
     * response
     *
     * @param request the operation request
     *
     * @return Returns the response of the request (e.g. CapabilitiesResponse
     *
     * @throws OwsExceptionReport If an error occurred or the requested
     *                            operation is not supported
     */
    OwsServiceResponse receiveRequest(OwsServiceRequest request)
            throws OwsExceptionReport;
}
