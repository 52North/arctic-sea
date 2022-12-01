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
package org.n52.shetland.ogc.om.series;

import java.util.ArrayList;
import java.util.List;

import org.n52.shetland.iso.gmd.CiResponsibleParty;
import org.n52.shetland.ogc.gml.CodeWithAuthority;
import org.n52.shetland.ogc.gml.ReferenceType;
import org.n52.shetland.ogc.gml.VerticalDatum;
import org.n52.shetland.ogc.om.features.samplingFeatures.AbstractSamplingFeature;
import org.n52.shetland.w3c.xlink.Referenceable;

public abstract class AbstractMonitoringFeature extends AbstractSamplingFeature {

    /* 0..* */
    private List<Referenceable<CiResponsibleParty>> relatedParty = new ArrayList<>();
    /* 0..* */
    private List<ReferenceType> monitoringType = new ArrayList<>();
    /* 0..* */
    private List<ReferenceType> descriptionReference = new ArrayList<>();
    /* 0..* */
    private List<Referenceable<VerticalDatum>> verticalDatum = new ArrayList<>();

    public AbstractMonitoringFeature(CodeWithAuthority featureIdentifier) {
        this(featureIdentifier, null);
    }

    public AbstractMonitoringFeature(CodeWithAuthority identifier, String gmlId) {
        super(identifier, gmlId);
    }

    /**
     * @return the relatedParty
     */
    public List<Referenceable<CiResponsibleParty>> getRelatedParty() {
        return relatedParty;
    }

    /**
     * @param relatedParty the relatedParty to set
     */
    public AbstractMonitoringFeature setRelatedParty(List<Referenceable<CiResponsibleParty>> relatedParty) {
        this.relatedParty.clear();
        if (relatedParty != null) {
            this.relatedParty.addAll(relatedParty);
        }
        return this;
    }

    /**
     * @param relatedParty the relatedParty to add
     */
    public AbstractMonitoringFeature addRelatedParty(List<Referenceable<CiResponsibleParty>> relatedParty) {
        if (relatedParty != null) {
            this.relatedParty.addAll(relatedParty);
        }
        return this;
    }

    /**
     * @param relatedParty the relatedParty to add
     */
    public AbstractMonitoringFeature addRelatedParty(Referenceable<CiResponsibleParty> relatedParty) {
        if (relatedParty != null) {
            this.relatedParty.add(relatedParty);
        }
        return this;
    }

    public boolean hasRelatedParty() {
        return getRelatedParty() != null && !getRelatedParty().isEmpty();
    }

    /**
     * @return the monitoringType
     */
    public List<ReferenceType> getMonitoringType() {
        return monitoringType;
    }

    /**
     * @param monitoringType the monitoringType to set
     */
    public AbstractMonitoringFeature setMonitoringType(List<ReferenceType> monitoringType) {
        this.monitoringType.clear();
        if (monitoringType != null) {
            this.monitoringType.addAll(monitoringType);
        }
        return this;
    }

    /**
     * @param monitoringType the monitoringType to add
     */
    public AbstractMonitoringFeature addMonitoringType(List<ReferenceType> monitoringType) {
        if (monitoringType != null) {
            this.monitoringType.addAll(monitoringType);
        }
        return this;
    }

    /**
     * @param monitoringType the monitoringType to add
     */
    public AbstractMonitoringFeature addMonitoringType(ReferenceType monitoringType) {
        if (monitoringType != null) {
            this.monitoringType.add(monitoringType);
        }
        return this;
    }

    public boolean hasMonitoringType() {
        return getMonitoringType() != null && !getMonitoringType().isEmpty();
    }

    /**
     * @return the descriptionReference
     */
    public List<ReferenceType> getDescriptionReference() {
        return descriptionReference;
    }

    /**
     * @param descriptionReference the descriptionReference to set
     */
    public AbstractMonitoringFeature setDescriptionReference(List<ReferenceType> descriptionReference) {
        this.descriptionReference.clear();
        if (descriptionReference != null) {
            this.descriptionReference.addAll(descriptionReference);
        }
        return this;
    }

    /**
     * @param descriptionReference the descriptionReference to add
     */
    public AbstractMonitoringFeature addDescriptionReference(List<ReferenceType> descriptionReference) {
        this.descriptionReference.clear();
        if (descriptionReference != null) {
            this.descriptionReference.addAll(descriptionReference);
        }
        return this;
    }

    /**
     * @param descriptionReference the descriptionReference to add
     */
    public AbstractMonitoringFeature addDescriptionReference(ReferenceType descriptionReference) {
        if (descriptionReference != null) {
            this.descriptionReference.add(descriptionReference);
        }
        return this;
    }

    public boolean hasDescriptionReference() {
        return getDescriptionReference() != null && !getDescriptionReference().isEmpty();
    }

    /**
     * @return the verticalDatum
     */
    public List<Referenceable<VerticalDatum>> getVerticalDatum() {
        return verticalDatum;
    }

    /**
     * @param verticalDatum the verticalDatum to set
     */
    public AbstractMonitoringFeature setVerticalDatum(List<Referenceable<VerticalDatum>> verticalDatum) {
        this.verticalDatum.clear();
        if (verticalDatum != null) {
            this.verticalDatum.addAll(verticalDatum);
        }
        return this;
    }

    /**
     * @param verticalDatum the verticalDatum to add
     */
    public AbstractMonitoringFeature addVerticalDatum(List<Referenceable<VerticalDatum>> verticalDatum) {
        if (verticalDatum != null) {
            this.verticalDatum.addAll(verticalDatum);
        }
        return this;
    }

    /**
     * @param verticalDatum the verticalDatum to add
     */
    public AbstractMonitoringFeature addVerticalDatum(Referenceable<VerticalDatum> verticalDatum) {
        if (verticalDatum != null) {
            this.verticalDatum.add(verticalDatum);
        }
        return this;
    }

    public boolean hasVerticalDatum() {
        return getVerticalDatum() != null && !getVerticalDatum().isEmpty();
    }

}
