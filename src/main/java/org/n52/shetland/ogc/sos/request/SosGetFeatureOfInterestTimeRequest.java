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

import org.n52.shetland.ogc.ows.service.OwsServiceRequest;
import org.n52.shetland.ogc.sos.Sos1Constants;

/**
 * SOS GetFeatureOfInterestTime request
 *
 * @since 1.0.0
 */
public class SosGetFeatureOfInterestTimeRequest
        extends OwsServiceRequest {

    /**
     * FOI identifier
     */
    private String featureIdentifier;

    public SosGetFeatureOfInterestTimeRequest() {
        super(null, null, Sos1Constants.Operations.GetFeatureOfInterestTime.name());
    }

    public SosGetFeatureOfInterestTimeRequest(String service, String version) {
        super(service, version, Sos1Constants.Operations.GetFeatureOfInterestTime.name());
    }

    public SosGetFeatureOfInterestTimeRequest(String service, String version, String operationName) {
        super(service, version, operationName);
    }

    /**
     * Get FOI identifier
     *
     * @return FOI identifier
     */
    public String getFeatureIdentifier() {
        return featureIdentifier;
    }

    /**
     * Set FOI identifier
     *
     * @param featureIdentifier
     *            FOI identifier
     */
    public void setFeatureIdentifier(String featureIdentifier) {
        this.featureIdentifier = featureIdentifier;
    }
}
