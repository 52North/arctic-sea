/*
 * Copyright (C) 2015-2022 52°North Spatial Information Research GmbH
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
package org.n52.shetland.ogc.sos.response;

import java.util.Collections;
import java.util.List;

import org.n52.shetland.ogc.om.OmObservation;
import org.n52.shetland.ogc.ows.service.OwsServiceResponse;
import org.n52.shetland.ogc.sos.Sos2Constants;

import com.google.common.collect.Lists;

/**
 * @since 1.0.0
 *
 */
public class InsertResultResponse extends OwsServiceResponse {
    private List<OmObservation> observations = Lists.newArrayList();

    public InsertResultResponse() {
        super(null, null, Sos2Constants.Operations.InsertResult.name());
    }

    public InsertResultResponse(String service, String version) {
        super(service, version, Sos2Constants.Operations.InsertResult.name());
    }

    public InsertResultResponse(String service, String version, String operationName) {
        super(service, version, operationName);
    }

    public List<OmObservation> getObservations() {
        return Collections.unmodifiableList(observations);
    }

    public void setObservation(OmObservation observation) {
        if (observation != null) {
            this.observations.add(observation);
        }
    }

    public void setObservations(List<OmObservation> observations) {
        this.observations.clear();
        if (observations != null) {
            this.observations.addAll(observations);
        }
    }

}
