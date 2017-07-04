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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.gml.time.Time;
import org.n52.shetland.ogc.gml.time.TimeInstant;
import org.n52.shetland.ogc.gml.time.TimePeriod;
import org.n52.shetland.ogc.om.OmObservation;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.shetland.ogc.ows.service.OwsServiceRequest;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.util.CollectionHelper;

import com.google.common.base.Strings;

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
        return getBooleanExtension(Sos2Constants.Extensions.SplitDataArrayIntoObservations);
    }

    /**
     * Checks if an observation contains referenced elements. Checked elements
     * are phenomenonTime, resultTime and featureOfInterest.
     *
     * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
     * @since 4.3.7
     *
     */
    private static class ReferenceChecker {
        final Map<String, Time> phenomenonTimes = new HashMap<String, Time>();

        final Map<String, TimeInstant> resultTimes = new HashMap<String, TimeInstant>();

        final Map<String, AbstractFeature> features = new HashMap<String, AbstractFeature>();

        /**
         * Check observations for references
         *
         * @param observations
         *            {@link OmObservation}s to check
         * @return Checked observations
         * @throws OwsExceptionReport
         *             If an error occurs
         */
        public List<OmObservation> checkObservationsForReferences(final List<OmObservation> observations)
                throws OwsExceptionReport {
            if (CollectionHelper.isNotEmpty(observations)) {
                for (OmObservation observation : observations) {
                    checkObservationForReferences(observation);
                }
            }
            return observations;
        }

        /**
         * Check observation for references
         *
         * @param observation
         *            {@link OmObservation} to check
         * @return Checked observation
         * @throws OwsExceptionReport
         *             If an error occurs
         */
        public OmObservation checkObservationForReferences(OmObservation observation) throws OwsExceptionReport {
            if (observation != null) {
                checkAndAddPhenomenonTime(observation.getPhenomenonTime(), phenomenonTimes);
                checkAndAddResultTime(observation.getResultTime(), resultTimes);
                checkAndAddFeatures(observation.getObservationConstellation().getFeatureOfInterest(), features);
                checkReferencedElements(observation, phenomenonTimes, resultTimes, features);
            }
            return observation;
        }

        private void checkAndAddPhenomenonTime(final Time phenomenonTime, final Map<String, Time> phenomenonTimes) {
            if (phenomenonTime != null && !phenomenonTime.isReferenced()) {
                phenomenonTimes.put(phenomenonTime.getGmlId(), phenomenonTime);
            }
        }

        private void checkAndAddResultTime(final TimeInstant resultTime, final Map<String, TimeInstant> resultTimes) {
            if (resultTime != null && !resultTime.isReferenced()) {
                resultTimes.put(resultTime.getGmlId(), resultTime);
            }
        }

        private void checkAndAddFeatures(final AbstractFeature featureOfInterest,
                final Map<String, AbstractFeature> features) {
            if (featureOfInterest != null && !featureOfInterest.isReferenced()) {
                features.put(featureOfInterest.getGmlId(), featureOfInterest);
            }
        }

        private void checkReferencedElements(final OmObservation observation, final Map<String, Time> phenomenonTimes,
                final Map<String, TimeInstant> resultTimes, final Map<String, AbstractFeature> features)
                throws OwsExceptionReport {
            // phenomenonTime
            final Time phenomenonTime = observation.getPhenomenonTime();
            if (phenomenonTime != null && phenomenonTime.isReferenced()) {
                observation.getValue().setPhenomenonTime(phenomenonTimes.get(phenomenonTime.getGmlId()));
            }
            // resultTime
            final TimeInstant resultTime = observation.getResultTime();
            if (resultTime != null && resultTime.isReferenced()) {
                if (resultTimes.containsKey(resultTime.getGmlId())) {
                    observation.setResultTime(resultTimes.get(resultTime.getGmlId()));
                } else if (phenomenonTimes.containsKey(resultTime.getGmlId())) {
                    final Time iTime = phenomenonTimes.get(resultTime.getGmlId());
                    if (iTime instanceof TimeInstant) {
                        observation.setResultTime((TimeInstant) iTime);
                    } else if (iTime instanceof TimePeriod) {
                        final TimePeriod timePeriod = (TimePeriod) iTime;
                        observation.setResultTime(new TimeInstant(timePeriod.getEnd()));
                    }
                }
            }
            // featureOfInterest
            final AbstractFeature featureOfInterest = observation.getObservationConstellation().getFeatureOfInterest();
            if (featureOfInterest != null && featureOfInterest.isReferenced()) {
                observation.getObservationConstellation()
                        .setFeatureOfInterest(features.get(featureOfInterest.getGmlId()));
            }
        }
    }
}
