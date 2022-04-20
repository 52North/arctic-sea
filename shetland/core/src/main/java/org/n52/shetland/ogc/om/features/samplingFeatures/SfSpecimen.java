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

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.n52.shetland.ogc.gml.CodeWithAuthority;
import org.n52.shetland.ogc.gml.ReferenceType;
import org.n52.shetland.ogc.gml.time.Time;
import org.n52.shetland.ogc.om.features.SfConstants;
import org.n52.shetland.ogc.om.values.QuantityValue;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.shetland.w3c.xlink.Referenceable;

import com.google.common.collect.Lists;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import org.locationtech.jts.geom.Geometry;

public class SfSpecimen extends SamplingFeature {

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
    public SfSpecimen setMaterialClass(ReferenceType materialClass) {
        this.materialClass = materialClass;
        return this;
    }

    /**
     * @return the samplingTime
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP" })
    public Time getSamplingTime() {
        return samplingTime;
    }

    /**
     * @param samplingTime
     *            the samplingTime to set
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public SfSpecimen setSamplingTime(Time samplingTime) {
        this.samplingTime = samplingTime;
        return this;
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
    public SfSpecimen setSamplingMethod(SfProcess samplingMethod) {
        this.samplingMethod = Referenceable.of(samplingMethod);
        return this;
    }

    /**
     * @param samplingMethod
     *            the samplingMethod to set
     */
    public SfSpecimen setSamplingMethod(Referenceable<SfProcess> samplingMethod) {
        this.samplingMethod = samplingMethod;
        return this;
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
    public SfSpecimen setSamplingLocation(Geometry samplingLocation) throws InvalidSridException {
        setGeometry(samplingLocation);
        return this;
    }

    public boolean isSetSamplingLocation() {
        return super.isSetGeometry();
    }

    /**
     * @return the processingDetails
     */
    public List<PreparationStep> getProcessingDetails() {
        return Collections.unmodifiableList(processingDetails);
    }

    /**
     * @param processingDetails
     *            the processingDetails to set
     * @return
     */
    public SfSpecimen setProcessingDetails(Collection<PreparationStep> processingDetails) {
        this.processingDetails.clear();
        if (processingDetails != null) {
            this.processingDetails.addAll(processingDetails);
        }
        return this;
    }

    public SfSpecimen addProcessingDetails(PreparationStep processingDetails) {
        if (processingDetails != null) {
            this.processingDetails.add(processingDetails);
        }
        return this;
    }

    public SfSpecimen addProcessingDetails(Collection<PreparationStep> processingDetails) {
        if (processingDetails != null) {
            this.processingDetails.addAll(processingDetails);
        }
        return this;
    }

    public boolean isSetProcessingDetails() {
        return !getProcessingDetails().isEmpty();
    }

    /**
     * @return the size
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP" })
    public QuantityValue getSize() {
        return size;
    }

    /**
     * @param size
     *            the size to set
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public SfSpecimen setSize(QuantityValue size) {
        this.size = size;
        return this;
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
    public SfSpecimen setCurrentLocation(Referenceable<SpecLocation> currentLocation) {
        this.currentLocation = currentLocation;
        return this;
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
     * @return
     */
    public SfSpecimen setSpecimenType(ReferenceType specimenType) {
        this.specimenType = specimenType;
        return this;
    }

    public boolean isSetSpecimenType() {
        return getSpecimenType() != null;
    }

    @Override
    public <X> X accept(FeatureOfInterestVisitor<X> visitor) throws OwsExceptionReport {
        return visitor.visit(this);
    }
}
