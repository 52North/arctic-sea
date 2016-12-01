/*
 * Copyright 2016 52°North Initiative for Geospatial Open Source
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

import java.util.LinkedList;
import java.util.List;

import org.n52.shetland.ogc.om.OmObservation;
import org.n52.shetland.ogc.ows.service.OwsServiceRequest;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.util.CollectionHelper;

import com.google.common.base.Strings;

import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;

/**
 * SOS InsertObservation request
 *
 * @since 4.0.0
 */
public class InsertObservationRequest extends OwsServiceRequest {

    /**
     * Assigned sensor id
     */
    private String assignedSensorId;

    private List<String> offerings;

    /**
     * SOS observation collection with observations to insert
     */
    private List<OmObservation> observations;

    public InsertObservationRequest() {
        super(null, null, SosConstants.Operations.InsertObservation.name());
    }

    public InsertObservationRequest(String service, String version) {
        super(service, version, SosConstants.Operations.InsertObservation.name());
    }

    public InsertObservationRequest(String service, String version, String operationName) {
        super(service, version, operationName);
    }

    /**
     * Get assigned sensor id
     *
     * @return assigned sensor id
     */
    public String getAssignedSensorId() {
        return assignedSensorId;
    }

    /**
     * Set assigned sensor id
     *
     * @param assignedSensorId
     *                         assigned sensor id
     */
    public void setAssignedSensorId(String assignedSensorId) {
        this.assignedSensorId = assignedSensorId;
    }

    public boolean isSetAssignedSensorId() {
        return !Strings.isNullOrEmpty(getAssignedSensorId());
    }

    /**
     * Get observations to insert
     *
     * @return observations to insert
     */
    public List<OmObservation> getObservations() {
        return observations;
    }

    /**
     * Set observations to insert
     *
     * @param observation
     *                    observations to insert
     */
    public void setObservation(List<OmObservation> observation) {
        this.observations = observation;
    }

    public void addObservation(OmObservation observation) {
        if (observations == null) {
            observations = new LinkedList<OmObservation>();
        }
        observations.add(observation);
    }

    public boolean isSetObservation() {
        return CollectionHelper.isNotEmpty(getObservations());
    }

    public void setOfferings(List<String> offerings) {
        this.offerings = offerings;
    }

    public List<String> getOfferings() {
        return offerings;
    }

    public boolean isSetOfferings() {
        return CollectionHelper.isNotEmpty(getOfferings());
    }

    public boolean isSetExtensionSplitDataArrayIntoObservations() {
        return isSetExtensions() && getExtensions()
               .isBooleanExtensionSet(Sos2Constants.Extensions.SplitDataArrayIntoObservations.name());
    }
}
