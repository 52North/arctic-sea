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
package org.n52.shetland.ogc.sos.ifoi;

import java.util.ArrayList;
import java.util.List;

import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.ows.service.OwsServiceRequest;

public class InsertFeatureOfInterestRequest extends OwsServiceRequest {

    private static String OPERATION_NAME = "InsertFeatureOfInterest";
    private List<AbstractFeature> abstractFeatures = new ArrayList<>();

    public InsertFeatureOfInterestRequest() {
        super(null, null, OPERATION_NAME);
    }

    public InsertFeatureOfInterestRequest(String service, String version) {
        super(service, version, OPERATION_NAME);
    }

    public InsertFeatureOfInterestRequest(String service, String version, String operationName) {
        super(service, version, operationName);
    }

    public List<AbstractFeature> getFeatureMembers() {
        return abstractFeatures;
    }

    public InsertFeatureOfInterestRequest setFeatureMembers(List<AbstractFeature> abstractFeatures) {
        this.abstractFeatures.clear();
        if (abstractFeatures != null) {
            this.abstractFeatures.addAll(abstractFeatures);
        }
        return this;
    }

    public InsertFeatureOfInterestRequest addFeatureMember(AbstractFeature abstractFeature) {
        if (abstractFeature != null) {
            this.abstractFeatures.add(abstractFeature);
        }
        return this;
    }

    public boolean hasFeatureMembers() {
        return !getFeatureMembers().isEmpty();
    }

    @Override
    public String getOperationName() {
        return OPERATION_NAME;
    }

}
