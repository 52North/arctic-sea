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
package org.n52.shetland.inspire.ef;

import java.net.URI;

import org.n52.shetland.inspire.base2.LegislationCitation;
import org.n52.shetland.ogc.gml.time.TimeInstant;

public class ReportToLegalAct {

    /**
     * 1..1
     */
    private LegislationCitation legalAct;

    /**
     * 1..1
     */
    private TimeInstant reportDate;

    /**
     * 0..1
     */
    private URI reportedEnvelope;

    /**
     * 1..1
     */
    private boolean observationRequired;

    /**
     * 1..1
     */
    private boolean observingCapabilityRequired;

    /**
     * 0..1
     */
    private String description;

    /**
     * @return the legalAct
     */
    public LegislationCitation getLegalAct() {
        return legalAct;
    }

    /**
     * @param legalAct the legalAct to set
     */
    public void setLegalAct(LegislationCitation legalAct) {
        this.legalAct = legalAct;
    }

    /**
     * @return the reportDate
     */
    public TimeInstant getReportDate() {
        return reportDate;
    }

    /**
     * @param reportDate the reportDate to set
     */
    public void setReportDate(TimeInstant reportDate) {
        this.reportDate = reportDate;
    }

    /**
     * @return the reportedEnvelope
     */
    public URI getReportedEnvelope() {
        return reportedEnvelope;
    }

    /**
     * @param reportedEnvelope the reportedEnvelope to set
     */
    public void setReportedEnvelope(URI reportedEnvelope) {
        this.reportedEnvelope = reportedEnvelope;
    }

    /**
     * @return the observationRequired
     */
    public boolean isObservationRequired() {
        return observationRequired;
    }

    /**
     * @param observationRequired the observationRequired to set
     */
    public void setObservationRequired(boolean observationRequired) {
        this.observationRequired = observationRequired;
    }

    /**
     * @return the observingCapabilityRequired
     */
    public boolean isObservingCapabilityRequired() {
        return observingCapabilityRequired;
    }

    /**
     * @param observingCapabilityRequired the observingCapabilityRequired to set
     */
    public void setObservingCapabilityRequired(boolean observingCapabilityRequired) {
        this.observingCapabilityRequired = observingCapabilityRequired;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
