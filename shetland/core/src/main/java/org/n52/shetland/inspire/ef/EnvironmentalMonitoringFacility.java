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

import org.locationtech.jts.geom.Point;
import org.n52.shetland.inspire.base.Identifier;
import org.n52.shetland.ogc.gml.ReferenceType;
import org.n52.shetland.util.CollectionHelper;
import org.n52.shetland.util.IdGenerator;
import org.n52.shetland.w3c.xlink.Referenceable;

import com.google.common.collect.Sets;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public class EnvironmentalMonitoringFacility extends AbstractMonitoringFeature {

    /**
     * 0..1
     */
    private Point representativePoint;

    /**
     * 1..1, nillable
     */
    private ReferenceType measurementRegime;

    /**
     * 1..1, nillable
     */
    private Boolean mobile;

    /**
     * 0..*
     */
    private Set<ReferenceType> resultAcquisitionSource = Sets.newHashSet();

    /**
     * 0..1
     */
    private ReferenceType specialisedEMFType;

    /**
     * 1..*, nillable
     */
    private Set<Referenceable<OperationalActivityPeriod>> operationalActivityPeriod = Sets.newHashSet();

    /**
     * 0..*
     */
    private Set<Referenceable<AnyDomainLink>> relatedTo = Sets.newHashSet();

    /**
     * 0..*
     */
    private Set<Referenceable<NetworkFacility>> belongsTo = Sets.newHashSet();

    private boolean wasEncoded;

    public EnvironmentalMonitoringFacility(Identifier inspireId, ReferenceType mediaMonitored) {
        super(inspireId, mediaMonitored);
    }

    public EnvironmentalMonitoringFacility(Identifier inspireId, Set<ReferenceType> mediaMonitored) {
        super(inspireId, mediaMonitored);
    }

    public EnvironmentalMonitoringFacility(Identifier inspireId, ReferenceType mediaMonitored,
            ReferenceType measurementRegime, boolean mobile,
            Referenceable<OperationalActivityPeriod> operationalActivityPeriod) {
        super(inspireId, mediaMonitored);
        this.measurementRegime = measurementRegime;
        this.mobile = mobile;
        this.operationalActivityPeriod.add(operationalActivityPeriod);
        setDefaultElementEncoding(InspireEfConstants.NS_EF);
    }

    public EnvironmentalMonitoringFacility(Identifier inspireId, Set<ReferenceType> mediaMonitored,
            ReferenceType measurementRegime, boolean mobile,
            Set<Referenceable<OperationalActivityPeriod>> operationalActivityPeriod) {
        super(inspireId, mediaMonitored);
        this.measurementRegime = measurementRegime;
        this.mobile = mobile;
        this.operationalActivityPeriod.addAll(operationalActivityPeriod);
        setDefaultElementEncoding(InspireEfConstants.NS_EF);
    }

    @Override
    public boolean isSetGmlID() {
        return super.isSetGmlID() && wasEncoded;
    }

    @Override
    public String getGmlId() {
        if (!super.isSetGmlID()) {
            final StringBuilder builder = new StringBuilder();
            builder.append("emf");
            builder.append(IdGenerator.generate(getIdentifierCodeWithAuthority().getValue()));
            setGmlId(builder.toString());
        }
        return super.getGmlId();
    }

    /**
     * @return the representativePoint
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP" })
    public Point getRepresentativePoint() {
        return representativePoint;
    }

    /**
     * @param representativePoint
     *            the representativePoint to set
     * @return this
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public EnvironmentalMonitoringFacility setRepresentativePoint(Point representativePoint) {
        this.representativePoint = representativePoint;
        return this;
    }

    public boolean isSetRepresentativePoint() {
        return getRepresentativePoint() != null && !getRepresentativePoint().isEmpty();
    }

    /**
     * @return the measurementRegime
     */
    public ReferenceType getMeasurementRegime() {
        return measurementRegime;
    }

    /**
     * @param measurementRegime
     *            the measurementRegime to set
     * @return this
     */
    public EnvironmentalMonitoringFacility setMeasurementRegime(ReferenceType measurementRegime) {
        this.measurementRegime = measurementRegime;
        return this;
    }

    public boolean isSetMeasurementRegime() {
        return getMeasurementRegime() != null && getMeasurementRegime().isSetHref();
    }

    /**
     * @return the mobile
     */
    public boolean isMobile() {
        return mobile;
    }

    /**
     * @param mobile
     *            the mobile to set
     */
    public void setMobile(Boolean mobile) {
        this.mobile = mobile;
    }

    public boolean isSetMobile() {
        return mobile != null;
    }

    /**
     * @return the resultAcquisitionSource
     */
    public Set<ReferenceType> getResultAcquisitionSource() {
        return Collections.unmodifiableSet(resultAcquisitionSource);
    }

    /**
     * @param resultAcquisitionSource
     *            the resultAcquisitionSource to set
     */
    public EnvironmentalMonitoringFacility setResultAcquisitionSource(
            Collection<ReferenceType> resultAcquisitionSource) {
        this.resultAcquisitionSource.clear();
        if (resultAcquisitionSource != null) {
            this.resultAcquisitionSource.addAll(resultAcquisitionSource);
        }
        return this;
    }

    public boolean isSetResultAcquisitionSource() {
        return CollectionHelper.isNotEmpty(getResultAcquisitionSource());
    }

    /**
     * @return the specialisedEMFType
     */
    public ReferenceType getSpecialisedEMFType() {
        return specialisedEMFType;
    }

    /**
     * @param specialisedEMFType
     *            the specialisedEMFType to set
     */
    public EnvironmentalMonitoringFacility setSpecialisedEMFType(ReferenceType specialisedEMFType) {
        this.specialisedEMFType = specialisedEMFType;
        return this;
    }

    public boolean isSetSpecialisedEMFType() {
        return getSpecialisedEMFType() != null;
    }

    /**
     * @return the operationalActivityPeriod
     */
    public Set<Referenceable<OperationalActivityPeriod>> getOperationalActivityPeriod() {
        return Collections.unmodifiableSet(operationalActivityPeriod);
    }

    /**
     * @param operationalActivityPeriod
     *            the operationalActivityPeriod to set
     * @return this
     */
    public EnvironmentalMonitoringFacility setOperationalActivityPeriod(
            Collection<Referenceable<OperationalActivityPeriod>> operationalActivityPeriod) {
        this.operationalActivityPeriod.clear();
        if (operationalActivityPeriod != null) {
            this.operationalActivityPeriod.addAll(operationalActivityPeriod);
        }
        return this;
    }

    public boolean isSetOperationalActivityPeriod() {
        return CollectionHelper.isNotEmpty(getOperationalActivityPeriod());
    }

    /**
     * @return the relatedTo
     */
    public Set<Referenceable<AnyDomainLink>> getRelatedTo() {
        return Collections.unmodifiableSet(relatedTo);
    }

    /**
     * @param relatedTo
     *            the relatedTo to set
     * @return this
     */
    public EnvironmentalMonitoringFacility setRelatedTo(Collection<Referenceable<AnyDomainLink>> relatedTo) {
        this.relatedTo.clear();
        if (relatedTo != null) {
            this.relatedTo.addAll(relatedTo);
        }
        return this;
    }

    public boolean isSetRelatedTo() {
        return CollectionHelper.isNotEmpty(getRelatedTo());
    }

    /**
     * @return the belongsTo
     */
    public Set<Referenceable<NetworkFacility>> getBelongsTo() {
        return Collections.unmodifiableSet(belongsTo);
    }

    /**
     * @param belongsTo
     *            the belongsTo to set
     * @return this
     */
    public EnvironmentalMonitoringFacility setBelongsTo(Collection<Referenceable<NetworkFacility>> belongsTo) {
        this.belongsTo.clear();
        if (relatedTo != null) {
            this.belongsTo.addAll(belongsTo);
        }
        return this;
    }

    public boolean isSetBelongsTo() {
        return CollectionHelper.isNotEmpty(getBelongsTo());
    }

    public void wasEncoded() {
        this.wasEncoded = true;
    }

}
