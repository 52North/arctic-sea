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
package org.n52.shetland.aqd;

import org.n52.shetland.aqd.AqdConstants.AssessmentType;
import org.n52.shetland.inspire.ef.EfEnvironmentalMonitoringFacility;

/**
 * Class represents an AQD_SamplingPoint
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public class AqdSamplingPoint extends EfEnvironmentalMonitoringFacility {

    private AssessmentType assessmentType;

    private AqdStation station;

    private AqdNetwork network;

    public AqdSamplingPoint() {
        setDefaultElementEncoding(AqdConstants.NS_AQD);
    }

    /**
     * @return the assessmentType
     */
    public AssessmentType getAssessmentType() {
        return assessmentType;
    }

    /**
     * @param assessmentType the assessmentType to set
     */
    public void setAssessmentType(AssessmentType assessmentType) {
        this.assessmentType = assessmentType;
    }

    /**
     * @return the station
     */
    public AqdStation getStation() {
        return station;
    }

    /**
     * @param station the station to set
     */
    public void setStation(AqdStation station) {
        this.station = station;
    }

    /**
     * @return <code>true</code>, if {@link AqdStation} is not null
     */
    public boolean isSetStation() {
        return getStation() != null;
    }

    /**
     * @return the network
     */
    public AqdNetwork getNetwork() {
        return network;
    }

    /**
     * @param network the network to set
     */
    public void setNetwork(AqdNetwork network) {
        this.network = network;
    }

    /**
     * @return <code>true</code>, if {@link AqdNetwork} is not null
     */
    public boolean isSetNetwork() {
        return getNetwork() != null;
    }

    public boolean hasAssessmentType() {
        return getAssessmentType() != null;
    }

}
