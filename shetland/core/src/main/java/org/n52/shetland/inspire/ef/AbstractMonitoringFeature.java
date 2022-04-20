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

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import org.n52.shetland.inspire.base.Identifier;
import org.n52.shetland.ogc.gml.ReferenceType;
import org.n52.shetland.ogc.om.OmObservation;
import org.n52.shetland.util.CollectionHelper;
import org.n52.shetland.w3c.xlink.Referenceable;

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
    private Set<Referenceable<EnvironmentalMonitoringActivity>> involvedIn = Sets.newHashSet();

    public AbstractMonitoringFeature(Identifier inspireId, ReferenceType mediaMonitored) {
        super(inspireId, mediaMonitored);
    }

    public AbstractMonitoringFeature(Identifier inspireId, Collection<ReferenceType> mediaMonitored) {
        super(inspireId, mediaMonitored);
    }

    /**
     * @return the reportedTo
     */
    public Set<ReportToLegalAct> getReportedTo() {
        return Collections.unmodifiableSet(reportedTo);
    }

    /**
     * @param reportedTo
     *            the reportedTo to set
     * @return
     */
    public AbstractMonitoringFeature setReportedTo(Collection<ReportToLegalAct> reportedTo) {
        this.reportedTo.clear();
        if (reportedTo != null) {
            this.reportedTo.addAll(reportedTo);
        }
        return this;
    }

    public boolean isSetReportedTo() {
        return CollectionHelper.isNotEmpty(getReportedTo());
    }

    /**
     * @return the hasObservation
     */
    public Set<Referenceable<OmObservation>> getHasObservation() {
        return Collections.unmodifiableSet(hasObservation);
    }

    /**
     * @param hasObservationa
     *            the hasObservation to set
     * @return
     */
    public AbstractMonitoringFeature setHasObservation(Collection<Referenceable<OmObservation>> hasObservation) {
        this.hasObservation.clear();
        if (hasObservation != null) {
            this.hasObservation.addAll(hasObservation);
        }
        return this;
    }

    /**
     * @param hasObservation
     *            the hasObservation to add
     */
    public void addHasObservation(OmObservation hasObservation) {
        this.hasObservation.add(Referenceable.of(hasObservation));
    }

    /**
     * @param hasObservation
     *            the hasObservation to add
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
    public Set<Referenceable<EnvironmentalMonitoringActivity>> getInvolvedIn() {
        return Collections.unmodifiableSet(involvedIn);
    }

    /**
     * @param involvedIn
     *            the involvedIn to set
     * @return
     */
    public AbstractMonitoringFeature setInvolvedIn(
            Collection<Referenceable<EnvironmentalMonitoringActivity>> involvedIn) {
        this.involvedIn.clear();
        if (involvedIn != null) {
            this.involvedIn.addAll(involvedIn);
        }
        return this;
    }

    public boolean isSetInvolvedIn() {
        return CollectionHelper.isNotEmpty(getInvolvedIn());
    }

}
