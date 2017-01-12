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
package org.n52.shetland.ogc.sos.response;

import java.util.List;

import org.n52.shetland.ogc.om.OmObservation;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.shetland.ogc.sos.SosConstants;


import com.google.common.collect.Lists;

/**
 * @since 4.0.0
 *
 */
public class GetObservationResponse extends AbstractObservationResponse implements StreamingDataResponse {
    public GetObservationResponse() {
        super(null, null, SosConstants.Operations.GetObservation.name());
    }

    public GetObservationResponse(String service, String version) {
        super(service, version, SosConstants.Operations.GetObservation.name());
    }

    public GetObservationResponse(String service, String version, String operationName) {
        super(service, version, operationName);
    }

    /*
     * TODO uncomment when WaterML support is activated public
     * Collection<SosObservation> mergeObservations(boolean
     * mergeObservationValuesWithSameParameters) { Collection<SosObservation>
     * combinedObsCol = new ArrayList<SosObservation>(); int obsIdCounter = 1;
     * for (SosObservation sosObservation : observationCollection) { if
     * (combinedObsCol.isEmpty()) {
     * sosObservation.setObservationID(Integer.toString(obsIdCounter++));
     * combinedObsCol.add(sosObservation); } else { boolean combined = false;
     * for (SosObservation combinedSosObs : combinedObsCol) { if
     * (mergeObservationValuesWithSameParameters) { if
     * (combinedSosObs.getObservationConstellation().equals(
     * sosObservation.getObservationConstellation())) {
     * combinedSosObs.mergeWithObservation(sosObservation, false); combined =
     * true; break; } } else { if
     * (combinedSosObs.getObservationConstellation().equalsExcludingObsProp(
     * sosObservation.getObservationConstellation())) {
     * combinedSosObs.mergeWithObservation(sosObservation, true); combined =
     * true; break; } } } if (!combined) { combinedObsCol.add(sosObservation); }
     * } } return combinedObsCol; }
     */
    @Override
    public boolean hasStreamingData() {
        OmObservation observation = getFirstObservation();
        return observation != null && observation.getValue() instanceof AbstractStreaming;
    }

    @Override
    public void mergeStreamingData() throws OwsExceptionReport {
        List<OmObservation> observations = Lists.newArrayList();
        if (hasStreamingData()) {
            for (OmObservation observation : getObservationCollection()) {
                AbstractStreaming values = (AbstractStreaming) observation.getValue();
                if (values.hasNextValue()) {
                    if (isSetMergeObservation()) {
                        observations.addAll(values.mergeObservation());
                    } else {
                        observations.addAll(values.getObservation());
                    }
                }
            }
        }
        setObservationCollection(observations);
    }

}
