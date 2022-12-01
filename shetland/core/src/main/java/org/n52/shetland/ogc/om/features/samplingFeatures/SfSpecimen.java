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
package org.n52.shetland.ogc.om.features.samplingFeatures;

import java.util.List;

import org.n52.shetland.ogc.gml.CodeWithAuthority;
import org.n52.shetland.ogc.gml.ReferenceType;
import org.n52.shetland.ogc.gml.time.Time;
import org.n52.shetland.ogc.om.features.SfConstants;
import org.n52.shetland.ogc.om.values.QuantityValue;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.shetland.w3c.xlink.Referenceable;

import com.google.common.collect.Lists;
import org.locationtech.jts.geom.Geometry;

public class SfSpecimen
        extends SamplingFeature {

    /*
     * 1..1
     */
    private ReferenceType materialClass;
    /*
     * 1..1
     */
    private Time samplingTime;
    /*
     * 0..1
     */
    private Referenceable<SfProcess> samplingMethod;
    /*
     * 0..1 samplingLocation use geometry
     */
    /*
     * 0..*
     */
    private List<PreparationStep> processingDetails = Lists.newArrayList();
    /*
     * 0..1
     */
    private QuantityValue size;
    /*
     * 0..1
     */
    private Referenceable<SpecLocation> currentLocation;
    /*
     * 0..1
     */
    private ReferenceType specimenType;

    public SfSpecimen(CodeWithAuthority featureIdentifier) {
        this(featureIdentifier, null);
    }

    public SfSpecimen(final CodeWithAuthority featureIdentifier, final String gmlId) {
        super(featureIdentifier, gmlId);
        setDefaultElementEncoding(SfConstants.NS_SPEC);
    }

    @Override
    public String getFeatureType() {
        return SfConstants.SAMPLING_FEAT_TYPE_SF_SPECIMEN;
    }

    /**
     * @return the materialClass
     */
    public ReferenceType getMaterialClass() {
        return materialClass;
    }

    /**
     * @param materialClass
     *            the materialClass to set
     */
    public void setMaterialClass(ReferenceType materialClass) {
        this.materialClass = materialClass;
    }

    /**
     * @return the samplingTime
     */
    public Time getSamplingTime() {
        return samplingTime;
    }

    /**
     * @param samplingTime
     *            the samplingTime to set
     */
    public void setSamplingTime(Time samplingTime) {
        this.samplingTime = samplingTime;
    }

    /**
     * @return the samplingMethod
     */
    public Referenceable<SfProcess> getSamplingMethod() {
        return samplingMethod;
    }

    /**
     * @param samplingMethod
     *            the samplingMethod to set
     */
    public void setSamplingMethod(SfProcess samplingMethod) {
        this.samplingMethod = Referenceable.of(samplingMethod);
    }

    /**
     * @param samplingMethod
     *            the samplingMethod to set
     */
    public void setSamplingMethod(Referenceable<SfProcess> samplingMethod) {
        this.samplingMethod = samplingMethod;
    }

    public boolean isSetSamplingMethod() {
        return getSamplingMethod() != null && !getSamplingMethod().isAbsent();
    }

    /**
     * @return the samplingLocation
     */
    public Geometry getSamplingLocation() {
        return getGeometry();
    }

    /**
     * @param samplingLocation
     *            the samplingLocation to set
     * @throws InvalidSridException
     *             If the SRID is invalid
     */
    public void setSamplingLocation(Geometry samplingLocation) throws InvalidSridException {
        setGeometry(samplingLocation);
    }

    public boolean isSetSamplingLocation() {
        return super.isSetGeometry();
    }

    /**
     * @return the processingDetails
     */
    public List<PreparationStep> getProcessingDetails() {
        return processingDetails;
    }

    /**
     * @param processingDetails
     *            the processingDetails to set
     */
    public void setProcessingDetails(List<PreparationStep> processingDetails) {
        this.processingDetails.clear();
        this.processingDetails.addAll(processingDetails);
    }

    public void addProcessingDetails(PreparationStep processingDetails) {
        this.processingDetails.add(processingDetails);
    }

    public void addProcessingDetails(List<PreparationStep> processingDetails) {
        this.processingDetails.addAll(processingDetails);
    }

    public boolean isSetProcessingDetails() {
        return !getProcessingDetails().isEmpty();
    }

    /**
     * @return the size
     */
    public QuantityValue getSize() {
        return size;
    }

    /**
     * @param size
     *            the size to set
     */
    public void setSize(QuantityValue size) {
        this.size = size;
    }

    public boolean isSetSize() {
        return getSize() != null;
    }

    /**
     * @return the currentLocation
     */
    public Referenceable<SpecLocation> getCurrentLocation() {
        return currentLocation;
    }

    /**
     * @param currentLocation
     *            the currentLocation to set
     */
    public void setCurrentLocation(Referenceable<SpecLocation> currentLocation) {
        this.currentLocation = currentLocation;
    }

    public boolean isSetCurrentLocation() {
        return getCurrentLocation() != null && !getCurrentLocation().isAbsent();
    }

    /**
     * @return the specimenType
     */
    public ReferenceType getSpecimenType() {
        return specimenType;
    }

    /**
     * @param specimenType
     *            the specimenType to set
     */
    public void setSpecimenType(ReferenceType specimenType) {
        this.specimenType = specimenType;
    }

    public boolean isSetSpecimenType() {
        return getSpecimenType() != null;
    }

    @Override
    public <X> X accept(FeatureOfInterestVisitor<X> visitor) throws OwsExceptionReport {
        return visitor.visit(this);
    }
}
