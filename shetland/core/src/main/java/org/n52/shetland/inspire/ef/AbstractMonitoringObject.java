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

import org.locationtech.jts.geom.Geometry;
import org.n52.shetland.inspire.base.Identifier;
import org.n52.shetland.inspire.base2.LegislationCitation;
import org.n52.shetland.inspire.base2.RelatedParty;
import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.gml.AbstractGeometry;
import org.n52.shetland.ogc.gml.ReferenceType;
import org.n52.shetland.util.CollectionHelper;
import org.n52.shetland.w3c.xlink.Referenceable;

import com.google.common.base.Strings;
import com.google.common.collect.Sets;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public abstract class AbstractMonitoringObject extends AbstractFeature {

    /**
     * 1..1 inspireId, super.identifier
     */
    /**
     * 0..* name, super.name
     */

    /**
     * 0..1
     */
    private String additionalDescription;

    /**
     * 1..*
     */
    private Set<ReferenceType> mediaMonitored = Sets.newHashSet();

    /**
     * 0..*
     */
    private Set<Referenceable<LegislationCitation>> legalBackground = Sets.newHashSet();

    /**
     * 0..1
     */
    private RelatedParty responsibleParty;

    /**
     * 0..1
     */
    private AbstractGeometry geometry;

    /**
     * 0..*
     */
    private Set<String> onlineResource = Sets.newHashSet();

    /**
     * 0..*
     */
    private Set<ReferenceType> purpose = Sets.newHashSet();

    /**
     * 0..*
     */
    private Set<Referenceable<ObservingCapability>> observingCapability = Sets.newHashSet();

    /**
     * 0..1
     */
    private Referenceable<Hierarchy> broader;

    /**
     * 0..*
     */
    private Set<Referenceable<Hierarchy>> narrower = Sets.newHashSet();

    /**
     * 0..*
     */
    private Set<Referenceable<AbstractMonitoringObject>> supersedes = Sets.newHashSet();

    /**
     * 0..*
     */
    private Set<Referenceable<AbstractMonitoringObject>> supersededBy = Sets.newHashSet();

    public AbstractMonitoringObject(Identifier inspireId, ReferenceType mediaMonitored) {
        this(inspireId, Sets.newHashSet(mediaMonitored));
    }

    public AbstractMonitoringObject(Identifier inspireId, Collection<ReferenceType> mediaMonitored) {
        super(inspireId);
        if (mediaMonitored != null) {
            this.mediaMonitored.addAll(mediaMonitored);
        }
    }

    public Identifier getInspireId() {
        return (Identifier) getIdentifierCodeWithAuthority();
    }

    /**
     * @return the additionalDescription
     */
    public String getAdditionalDescription() {
        return additionalDescription;
    }

    /**
     * @param additionalDescription
     *            the additionalDescription to set
     */
    public void setAdditionalDescription(String additionalDescription) {
        this.additionalDescription = additionalDescription;
    }

    public boolean isSetAdditionalDescription() {
        return !Strings.isNullOrEmpty(getAdditionalDescription());
    }

    /**
     * @return the mediaMonitored
     */
    public Set<ReferenceType> getMediaMonitored() {
        return Collections.unmodifiableSet(mediaMonitored);
    }

    /**
     * @param mediaMonitored
     *            the mediaMonitored to add
     * @return this
     */
    public AbstractMonitoringObject addMediaMonitored(Collection<ReferenceType> mediaMonitored) {
        this.mediaMonitored.clear();
        if (mediaMonitored != null) {
            this.mediaMonitored.addAll(mediaMonitored);
        }
        return this;
    }

    /**
     * @param mediaMonitored
     *            the mediaMonitored to add
     * @return this
     */
    public AbstractMonitoringObject addMediaMonitored(ReferenceType mediaMonitored) {
        if (mediaMonitored != null) {
            this.mediaMonitored.add(mediaMonitored);
        }
        return this;
    }

    /**
     * @return the legalBackground
     */
    public Set<Referenceable<LegislationCitation>> getLegalBackground() {
        return Collections.unmodifiableSet(legalBackground);
    }

    /**
     * @param legalBackground
     *            the legalBackground to set
     * @return this
     */
    public AbstractMonitoringObject setLegalBackground(
            Collection<Referenceable<LegislationCitation>> legalBackground) {
        this.legalBackground.clear();
        if (legalBackground != null) {
            this.legalBackground.addAll(legalBackground);
        }
        return this;
    }

    public boolean isSetLegalBackground() {
        return CollectionHelper.isNotEmpty(getLegalBackground());
    }

    /**
     * @return the responsibleParty
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP" })
    public RelatedParty getResponsibleParty() {
        return responsibleParty;
    }

    /**
     * @param responsibleParty
     *            the responsibleParty to set
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public void setResponsibleParty(RelatedParty responsibleParty) {
        this.responsibleParty = responsibleParty;
    }

    public boolean isSetResponsibleParty() {
        return getResponsibleParty() != null;
    }

    /**
     * @return the geometry
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP" })
    public AbstractGeometry getGeometry() {
        return geometry;
    }

    /**
     * @param geometry
     *            the geometry to set
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public void setGeometry(AbstractGeometry geometry) {
        this.geometry = geometry;
    }

    /**
     * @param geometry
     *            the geometry to set
     */
    public void setGeometry(Geometry geometry) {
        this.geometry = new AbstractGeometry().setGeometry(geometry);
    }

    public boolean isSetGeometry() {
        return getGeometry() != null && getGeometry().isSetGeometry();
    }

    /**
     * @return the onlineResource
     */
    public Set<String> getOnlineResource() {
        return Collections.unmodifiableSet(onlineResource);
    }

    /**
     * @param onlineResource
     *            the onlineResource to set
     * @return this
     */
    public AbstractMonitoringObject setOnlineResource(Collection<String> onlineResource) {
        this.onlineResource.clear();
        if (onlineResource != null) {
            this.onlineResource.addAll(onlineResource);
        }
        return this;
    }

    public boolean isSetOnlineResources() {
        return CollectionHelper.isNotEmpty(getOnlineResource());
    }

    /**
     * @return the purpose
     */
    public Set<ReferenceType> getPurpose() {
        return Collections.unmodifiableSet(purpose);
    }

    /**
     * @param purpose
     *            the purpose to set
     * @return this
     */
    public AbstractMonitoringObject setPurpose(Collection<ReferenceType> purpose) {
        this.purpose.clear();
        if (purpose != null) {
            this.purpose.addAll(purpose);
        }
        return this;
    }

    public boolean isSetPurpose() {
        return CollectionHelper.isNotEmpty(getPurpose());
    }

    /**
     * @return the observingCapability
     */
    public Set<Referenceable<ObservingCapability>> getObservingCapability() {
        return Collections.unmodifiableSet(observingCapability);
    }

    /**
     * @param observingCapability
     *            the observingCapability to set
     * @return this
     */
    public AbstractMonitoringObject setObservingCapability(
            Collection<Referenceable<ObservingCapability>> observingCapability) {
        this.observingCapability.clear();
        if (observingCapability != null) {
            this.observingCapability.addAll(observingCapability);
        }
        return this;
    }

    /**
     * @param observingCapability
     *            the observingCapability to add
     * @return this
     */
    public AbstractMonitoringObject addObservingCapability(Referenceable<ObservingCapability> observingCapability) {
        if (observingCapability != null) {
            this.observingCapability.add(observingCapability);
        }
        return this;
    }

    /**
     * @param observingCapability
     *            the observingCapability to add
     */
    public void addObservingCapability(ObservingCapability observingCapability) {
        addObservingCapability(Referenceable.of(observingCapability));
    }

    public boolean isSetObservingCapability() {
        return CollectionHelper.isNotEmpty(getObservingCapability());
    }

    /**
     * @return the broader
     */
    public Referenceable<Hierarchy> getBroader() {
        return broader;
    }

    /**
     * @param broader
     *            the broader to set
     */
    public void setBroader(Referenceable<Hierarchy> broader) {
        this.broader = broader;
    }

    public void setBroader(Hierarchy broader) {
        this.broader = Referenceable.of(broader);
    }

    public boolean isSetBroader() {
        return getBroader() != null;
    }

    /**
     * @return the narrower
     */
    public Set<Referenceable<Hierarchy>> getNarrower() {
        return Collections.unmodifiableSet(narrower);
    }

    /**
     * @param narrower
     *            the narrower to set
     * @return this
     */
    public AbstractMonitoringObject setNarrower(Collection<Referenceable<Hierarchy>> narrower) {
        this.narrower.clear();
        if (narrower != null) {
            this.narrower.addAll(narrower);
        }
        return this;
    }

    public boolean isSetNarrower() {
        return CollectionHelper.isNotEmpty(getObservingCapability());
    }

    /**
     * @return the supersedes
     */
    public Set<Referenceable<AbstractMonitoringObject>> getSupersedes() {
        return Collections.unmodifiableSet(supersedes);
    }

    /**
     * @param supersedes
     *            the supersedes to set
     * @return this
     */
    public AbstractMonitoringObject setSupersedes(Collection<Referenceable<AbstractMonitoringObject>> supersedes) {
        this.supersedes.clear();
        if (supersedes != null) {
            this.supersedes.addAll(supersedes);
        }
        return this;
    }

    public boolean isSetSupersedes() {
        return CollectionHelper.isNotEmpty(getSupersedes());
    }

    /**
     * @return the supersededBy
     */
    public Set<Referenceable<AbstractMonitoringObject>> getSupersededBy() {
        return Collections.unmodifiableSet(supersededBy);
    }

    /**
     * @param supersededBy
     *            the supersededBy to set
     * @return this
     */
    public AbstractMonitoringObject setSupersededBy(Collection<Referenceable<AbstractMonitoringObject>> supersededBy) {
        this.supersededBy.clear();
        if (supersededBy != null) {
            this.supersededBy.addAll(supersededBy);
        }
        return this;
    }

    public boolean isSetSupersededBy() {
        return CollectionHelper.isNotEmpty(getSupersededBy());
    }

}
