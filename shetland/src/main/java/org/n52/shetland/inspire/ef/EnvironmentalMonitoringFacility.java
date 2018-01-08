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
package org.n52.shetland.inspire.ef;

import java.util.Set;

import org.n52.shetland.inspire.base.Identifier;
import org.n52.shetland.ogc.gml.ReferenceType;
import org.n52.shetland.util.CollectionHelper;
import org.n52.shetland.util.JavaHelper;
import org.n52.shetland.w3c.xlink.SimpleAttrs;

import com.google.common.collect.Sets;
import org.locationtech.jts.geom.Point;

public class EnvironmentalMonitoringFacility
        extends AbstractMonitoringFeature {

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
    private Set<OperationalActivityPeriod> operationalActivityPeriod = Sets.newHashSet();

    /**
     * 0..*
     */
    private Set<AnyDomainLink> relatedTo = Sets.newHashSet();

    /**
     * 0..*
     */
    private Set<NetworkFacility> belongsTo = Sets.newHashSet();

    private boolean wasEncoded;

    public EnvironmentalMonitoringFacility(SimpleAttrs simpleAttrs) {
        super(simpleAttrs);
    }

    public EnvironmentalMonitoringFacility(Identifier inspireId, ReferenceType mediaMonitored) {
        super(inspireId, mediaMonitored);
    }

    public EnvironmentalMonitoringFacility(Identifier inspireId, Set<ReferenceType> mediaMonitored) {
        super(inspireId, mediaMonitored);
    }

    public EnvironmentalMonitoringFacility(
            Identifier inspireId, ReferenceType mediaMonitored, ReferenceType measurementRegime, boolean mobile,
            OperationalActivityPeriod operationalActivityPeriod) {
        super(inspireId, mediaMonitored);
        this.measurementRegime = measurementRegime;
        this.mobile = mobile;
        this.operationalActivityPeriod.add(operationalActivityPeriod);
        setDefaultElementEncoding(InspireEfConstants.NS_EF);
    }

    public EnvironmentalMonitoringFacility(
            Identifier inspireId, Set<ReferenceType> mediaMonitored, ReferenceType measurementRegime, boolean mobile,
            Set<OperationalActivityPeriod> operationalActivityPeriod) {
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
            builder.append(JavaHelper.generateID(getIdentifierCodeWithAuthority().getValue()));
            setGmlId(builder.toString());
        }
        return super.getGmlId();
    }

    /**
     * @return the representativePoint
     */
    public Point getRepresentativePoint() {
        return representativePoint;
    }

    /**
     * @param representativePoint
     *            the representativePoint to set
     */
    public void setRepresentativePoint(Point representativePoint) {
        this.representativePoint = representativePoint;
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
     */
    public void setMeasurementRegime(ReferenceType measurementRegime) {
        this.measurementRegime = measurementRegime;
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
        return resultAcquisitionSource;
    }

    /**
     * @param resultAcquisitionSource
     *            the resultAcquisitionSource to set
     */
    public void setResultAcquisitionSource(Set<ReferenceType> resultAcquisitionSource) {
        this.resultAcquisitionSource.clear();
        this.resultAcquisitionSource = resultAcquisitionSource;
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
    public void setSpecialisedEMFType(ReferenceType specialisedEMFType) {
        this.specialisedEMFType = specialisedEMFType;
    }

    public boolean isSetSpecialisedEMFType() {
        return getSpecialisedEMFType() != null;
    }

    /**
     * @return the operationalActivityPeriod
     */
    public Set<OperationalActivityPeriod> getOperationalActivityPeriod() {
        return operationalActivityPeriod;
    }

    /**
     * @param operationalActivityPeriod
     *            the operationalActivityPeriod to set
     */
    public void setOperationalActivityPeriod(Set<OperationalActivityPeriod> operationalActivityPeriod) {
        this.operationalActivityPeriod = operationalActivityPeriod;
    }

    public boolean isSetOperationalActivityPeriod() {
        return CollectionHelper.isNotEmpty(getOperationalActivityPeriod());
    }

    /**
     * @return the relatedTo
     */
    public Set<AnyDomainLink> getRelatedTo() {
        return relatedTo;
    }

    /**
     * @param relatedTo
     *            the relatedTo to set
     */
    public void setRelatedTo(Set<AnyDomainLink> relatedTo) {
        this.relatedTo.clear();
        this.relatedTo = relatedTo;
    }

    public boolean isSetRelatedTo() {
        return CollectionHelper.isNotEmpty(getRelatedTo());
    }

    /**
     * @return the belongsTo
     */
    public Set<NetworkFacility> getBelongsTo() {
        return belongsTo;
    }

    /**
     * @param belongsTo
     *            the belongsTo to set
     */
    public void setBelongsTo(Set<NetworkFacility> belongsTo) {
        this.belongsTo.clear();
        this.belongsTo = belongsTo;
    }

    public boolean isSetBelongsTo() {
        return CollectionHelper.isNotEmpty(getBelongsTo());
    }

    public void wasEncoded() {
        this.wasEncoded = true;
    }

}
