/*
 * Copyright 2015-2019 52°North Initiative for Geospatial Open Source
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
import org.n52.shetland.inspire.base2.LegislationCitation;
import org.n52.shetland.inspire.base2.RelatedParty;
import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.gml.AbstractGeometry;
import org.n52.shetland.ogc.gml.ReferenceType;
import org.n52.shetland.util.CollectionHelper;
import org.n52.shetland.w3c.xlink.AttributeSimpleAttrs;
import org.n52.shetland.w3c.xlink.SimpleAttrs;

import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import org.locationtech.jts.geom.Geometry;

public abstract class AbstractMonitoringObject
        extends AbstractFeature
        implements AttributeSimpleAttrs {

    private SimpleAttrs simpleAttrs;

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
    private Set<LegislationCitation> legalBackground = Sets.newHashSet();

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
    private Set<ObservingCapability> observingCapability = Sets.newHashSet();

    /**
     * 0..1
     */
    private Hierarchy broader;

    /**
     * 0..*
     */
    private Set<Hierarchy> narrower = Sets.newHashSet();

    /**
     * 0..*
     */
    private Set<AbstractMonitoringObject> supersedes = Sets.newHashSet();

    /**
     * 0..*
     */
    private Set<AbstractMonitoringObject> supersededBy = Sets.newHashSet();

    public AbstractMonitoringObject(SimpleAttrs simpleAttrs) {
        super("");
        this.simpleAttrs = simpleAttrs;
    }

    public AbstractMonitoringObject(Identifier inspireId, ReferenceType mediaMonitored) {
        this(inspireId, Sets.newHashSet(mediaMonitored));
    }

    public AbstractMonitoringObject(Identifier inspireId, Set<ReferenceType> mediaMonitored) {
        super(inspireId);
        this.mediaMonitored.addAll(mediaMonitored);
    }

    @Override
    public void setSimpleAttrs(SimpleAttrs simpleAttrs) {
        this.simpleAttrs = simpleAttrs;
    }

    @Override
    public SimpleAttrs getSimpleAttrs() {
        return simpleAttrs;
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
        return mediaMonitored;
    }

    /**
     * @param mediaMonitored
     *            the mediaMonitored to add
     */
    public void addMediaMonitored(Set<ReferenceType> mediaMonitored) {
        this.mediaMonitored.addAll(mediaMonitored);
    }

    /**
     * @param mediaMonitored
     *            the mediaMonitored to add
     */
    public void addMediaMonitored(ReferenceType mediaMonitored) {
        this.mediaMonitored.add(mediaMonitored);
    }

    /**
     * @return the legalBackground
     */
    public Set<LegislationCitation> getLegalBackground() {
        return legalBackground;
    }

    /**
     * @param legalBackground
     *            the legalBackground to set
     */
    public void setLegalBackground(Set<LegislationCitation> legalBackground) {
        this.legalBackground.clear();
        this.legalBackground = legalBackground;
    }

    public boolean isSetLegalBackground() {
        return CollectionHelper.isNotEmpty(getLegalBackground());
    }

    /**
     * @return the responsibleParty
     */
    public RelatedParty getResponsibleParty() {
        return responsibleParty;
    }

    /**
     * @param responsibleParty
     *            the responsibleParty to set
     */
    public void setResponsibleParty(RelatedParty responsibleParty) {
        this.responsibleParty = responsibleParty;
    }

    public boolean isSetResponsibleParty() {
        return getResponsibleParty() != null;
    }

    /**
     * @return the geometry
     */
    public AbstractGeometry getGeometry() {
        return geometry;
    }

    /**
     * @param geometry
     *            the geometry to set
     */
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
        return onlineResource;
    }

    /**
     * @param onlineResource
     *            the onlineResource to set
     */
    public void setOnlineResource(Set<String> onlineResource) {
        this.onlineResource.clear();
        this.onlineResource = onlineResource;
    }

    public boolean isSetOnlineResources() {
        return CollectionHelper.isNotEmpty(getOnlineResource());
    }

    /**
     * @return the purpose
     */
    public Set<ReferenceType> getPurpose() {
        return purpose;
    }

    /**
     * @param purpose
     *            the purpose to set
     */
    public void setPurpose(Set<ReferenceType> purpose) {
        this.purpose.clear();
        this.purpose = purpose;
    }

    public boolean isSetPurpose() {
        return CollectionHelper.isNotEmpty(getPurpose());
    }

    /**
     * @return the observingCapability
     */
    public Set<ObservingCapability> getObservingCapability() {
        return observingCapability;
    }

    /**
     * @param observingCapability
     *            the observingCapability to set
     */
    public void setObservingCapability(Set<ObservingCapability> observingCapability) {
        this.observingCapability.clear();
        this.observingCapability = observingCapability;
    }

    /**
     * @param observingCapability
     *            the observingCapability to add
     */
    public void addObservingCapability(ObservingCapability observingCapability) {
        this.observingCapability.add(observingCapability);
    }

    public boolean isSetObservingCapability() {
        return CollectionHelper.isNotEmpty(getObservingCapability());
    }

    /**
     * @return the broader
     */
    public Hierarchy getBroader() {
        return broader;
    }

    /**
     * @param broader
     *            the broader to set
     */
    public void setBroader(Hierarchy broader) {
        this.broader = broader;
    }

    public boolean isSetBroader() {
        return getBroader() != null;
    }

    /**
     * @return the narrower
     */
    public Set<Hierarchy> getNarrower() {
        return narrower;
    }

    /**
     * @param narrower
     *            the narrower to set
     */
    public void setNarrower(Set<Hierarchy> narrower) {
        this.narrower.clear();
        this.narrower = narrower;
    }

    public boolean isSetNarrower() {
        return CollectionHelper.isNotEmpty(getObservingCapability());
    }

    /**
     * @return the supersedes
     */
    public Set<AbstractMonitoringObject> getSupersedes() {
        return supersedes;
    }

    /**
     * @param supersedes
     *            the supersedes to set
     */
    public void setSupersedes(Set<AbstractMonitoringObject> supersedes) {
        this.supersedes.clear();
        this.supersedes = supersedes;
    }

    public boolean isSetSupersedes() {
        return CollectionHelper.isNotEmpty(getSupersedes());
    }

    /**
     * @return the supersededBy
     */
    public Set<AbstractMonitoringObject> getSupersededBy() {
        return supersededBy;
    }

    /**
     * @param supersededBy
     *            the supersededBy to set
     */
    public void setSupersededBy(Set<AbstractMonitoringObject> supersededBy) {
        this.supersededBy.clear();
        this.supersededBy = supersededBy;
    }

    public boolean isSetSupersededBy() {
        return CollectionHelper.isNotEmpty(getSupersededBy());
    }

}
