/*
 * Copyright 2016-2017 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.ogc.sos.request;

import java.util.List;

import org.n52.shetland.ogc.sos.SosConstants;

/**
 * SOS GetObservationById request
 *
 * @since 1.0.0
 */
public class GetObservationByIdRequest extends AbstractObservationRequest {

    /**
     * Observation identifier
     */
    private List<String> observationIdentifier;

    public GetObservationByIdRequest() {
        super(null, null, SosConstants.Operations.GetObservationById.name());
    }

    public GetObservationByIdRequest(String service, String version) {
        super(service, version, SosConstants.Operations.GetObservationById.name());
    }

    public GetObservationByIdRequest(String service, String version, String operationName) {
        super(service, version, operationName);
    }

    /**
     * Get observation identifier
     *
     * @return observation identifier
     */
    public List<String> getObservationIdentifier() {
        return observationIdentifier;
    }

    public boolean isSetObservationIdentifier() {
        return observationIdentifier != null && !observationIdentifier.isEmpty();
    }

    /**
     * Set observation identifier
     *
     * @param observationIdentifier
     *                              observation identifier
     */
    public void setObservationIdentifier(List<String> observationIdentifier) {
        this.observationIdentifier = observationIdentifier;
    }

}
