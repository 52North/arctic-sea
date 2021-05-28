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
package org.n52.shetland.ogc.sos.request;

import org.n52.shetland.ogc.ows.service.OwsServiceRequest;
import org.n52.shetland.ogc.sos.Sos1Constants;

/**
 * SOS DescribeObservationType request
 *
 * @since 1.0.0
 */
public class SosDescribeObservationTypeRequest
        extends OwsServiceRequest {
    public SosDescribeObservationTypeRequest() {
        super(null, null, Sos1Constants.Operations.DescribeObservationType.name());
    }

    public SosDescribeObservationTypeRequest(String service, String version) {
        super(service, version, Sos1Constants.Operations.DescribeObservationType.name());
    }

    public SosDescribeObservationTypeRequest(String service, String version, String operationName) {
        super(service, version, operationName);
    }

}
