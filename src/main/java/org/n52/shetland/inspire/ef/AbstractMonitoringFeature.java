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
package org.n52.shetland.inspire.ef;

import java.util.Set;

import org.n52.shetland.inspire.base.Identifier;
import org.n52.shetland.ogc.gml.ReferenceType;
import org.n52.shetland.ogc.om.OmObservation;
import org.n52.shetland.util.CollectionHelper;
import org.n52.shetland.w3c.xlink.Referenceable;
import org.n52.shetland.w3c.xlink.SimpleAttrs;

import com.google.common.collect.Sets;

public abstract class AbstractMonitoringFeature extends AbstractMonitoringObject {

    /**
     * 0..*
     */
    private Set<ReportToLegalAct> reportedTo = Sets.newHashSet();

    /**
     * 0..*
     */
    private Set<Referenceable<OmObservation>> hasObservation = Sets.newHashSet();

    /**
     * 0..*
     */
    private Set<EnvironmentalMonitoringActivity> involvedIn = Sets.newHashSet();

    public AbstractMonitoringFeature(SimpleAttrs simpleAttrs) {
        super(simpleAttrs);
    }

    public AbstractMonitoringFeature(Identifier inspireId, ReferenceType mediaMonitored) {
        super(inspireId, mediaMonitored);
    }

    public AbstractMonitoringFeature(Identifier inspireId, Set<ReferenceType> mediaMonitored) {
        super(inspireId, mediaMonitored);
    }

    /**
     * @return the reportedTo
     */
    public Set<ReportToLegalAct> getReportedTo() {
        return reportedTo;
    }

    /**
     * @param reportedTo the reportedTo to set
     */
    public void setReportedTo(Set<ReportToLegalAct> reportedTo) {
        this.reportedTo.clear();
        this.reportedTo = reportedTo;
    }

    public boolean isSetReportedTo() {
        return CollectionHelper.isNotEmpty(getReportedTo());
    }

    /**
     * @return the hasObservation
     */
    public Set<Referenceable<OmObservation>> getHasObservation() {
        return hasObservation;
    }

    /**
     * @param hasObservation the hasObservation to set
     */
    public void setHasObservation(Set<Referenceable<OmObservation>> hasObservation) {
        this.hasObservation.clear();
        this.hasObservation = hasObservation;
    }

    /**
     * @param hasObservation the hasObservation to add
     */
    public void addHasObservation(OmObservation hasObservation) {
        this.hasObservation.add(Referenceable.of(hasObservation));
    }

    /**
     * @param hasObservation the hasObservation to add
     */
    public void addHasObservation(Referenceable<OmObservation> hasObservation) {
        this.hasObservation.add(hasObservation);
    }

    public boolean isSetHasObservation() {
        return CollectionHelper.isNotEmpty(getHasObservation());
    }

    /**
     * @return the involvedIn
     */
    public Set<EnvironmentalMonitoringActivity> getInvolvedIn() {
        return involvedIn;
    }

    /**
     * @param involvedIn the involvedIn to set
     */
    public void setInvolvedIn(Set<EnvironmentalMonitoringActivity> involvedIn) {
        this.involvedIn.clear();
        this.involvedIn = involvedIn;
    }

    public boolean isSetInvolvedIn() {
        return CollectionHelper.isNotEmpty(getInvolvedIn());
    }

}
