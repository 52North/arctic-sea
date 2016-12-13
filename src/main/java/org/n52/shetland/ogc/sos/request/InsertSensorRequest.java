/*
 * Copyright 2016 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.ogc.sos.request;

import java.util.Collection;
import java.util.List;

import org.n52.shetland.ogc.ows.service.OwsServiceRequest;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosInsertionMetadata;
import org.n52.shetland.ogc.sos.SosOffering;
import org.n52.shetland.ogc.sos.SosProcedureDescription;
import org.n52.shetland.ogc.swes.SwesFeatureRelationship;
import org.n52.shetland.util.CollectionHelper;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

/**
 * @since 4.0.0
 *
 */
public class InsertSensorRequest extends OwsServiceRequest {

    private static final String SENSOR_TYPE_FLAG = "isType";
    private String procedureDescriptionFormat;

    /**
     * observableProperty parameter
     */
    private List<String> observableProperty;
    private List<SwesFeatureRelationship> relatedFeatures;

    /**
     * SOS SensorML description
     */
    private SosProcedureDescription<?> procedureDescription;
    private String assignedProcedureIdentifier;
    private List<SosOffering> assignedOfferings = Lists.newLinkedList();

    /**
     * metadata parameter
     */
    private SosInsertionMetadata metadata;

    /**
     * default constructor
     */
    public InsertSensorRequest() {
        super(null, null, Sos2Constants.Operations.InsertSensor.name());
    }

    public InsertSensorRequest(String service, String version) {
        super(service, version, Sos2Constants.Operations.InsertSensor.name());
    }

    public InsertSensorRequest(String service, String version, String operationName) {
        super(service, version, operationName);
    }

    public String getProcedureDescriptionFormat() {
        return procedureDescriptionFormat;
    }

    public void setProcedureDescriptionFormat(String procedureDescriptionFormat) {
        this.procedureDescriptionFormat = procedureDescriptionFormat;
    }

    public boolean isSetProcedureDescriptionFormat() {
        return !Strings.isNullOrEmpty(getProcedureDescriptionFormat());
    }

    /**
     * Get the observableProperty contained in request.
     *
     * @return the observableProperty
     */
    public List<String> getObservableProperty() {
        return observableProperty;
    }

    /**
     * Set the observableProperty contained in request.
     *
     * @param observableProperty
     *                           the observableProperty to set
     */
    public void setObservableProperty(List<String> observableProperty) {
        this.observableProperty = observableProperty;
    }

    public boolean isSetObservableProperty() {
        return CollectionHelper.isNotEmpty(getObservableProperty());
    }

    /**
     * Get the sensor description contained in request.
     *
     * @return the sosSensorML
     */
    public SosProcedureDescription<?> getProcedureDescription() {
        return procedureDescription;
    }

    /**
     * Set the sensor description contained in request.
     *
     * @param procedureDescription
     *                             the procedureDescription to set
     */
    public void setProcedureDescription(SosProcedureDescription<?> procedureDescription) {
        this.procedureDescription = procedureDescription;
    }

    public boolean isSetProcedureDescription() {
        return procedureDescription != null;
    }

    /**
     * Get the metadata contained in request.
     *
     * @return the metadata
     */
    public SosInsertionMetadata getMetadata() {
        return metadata;
    }

    /**
     * Set the metadata contained in request.
     *
     * @param metadata
     *                 the metadata to set
     */
    public void setMetadata(SosInsertionMetadata metadata) {
        this.metadata = metadata;
    }

    public boolean isSetMetadata() {
        return getMetadata() != null;
    }

    public List<SwesFeatureRelationship> getRelatedFeatures() {
        return relatedFeatures;
    }

    public void setRelatedFeature(List<SwesFeatureRelationship> relatedFeatures) {
        this.relatedFeatures = relatedFeatures;
    }

    public boolean isSetRelatedFeatures() {
        return CollectionHelper.isNotEmpty(getRelatedFeatures());
    }

    public String getAssignedProcedureIdentifier() {
        return assignedProcedureIdentifier;
    }

    public void setAssignedProcedureIdentifier(String assignedProcedureID) {
        this.assignedProcedureIdentifier = assignedProcedureID;
    }

    public boolean isSetAssignedProcedureIdentifier() {
        return !Strings.isNullOrEmpty(getAssignedProcedureIdentifier());
    }

    public List<SosOffering> getAssignedOfferings() {
        return assignedOfferings;
    }

    public void setAssignedOfferings(Collection<SosOffering> assignedOfferings) {
        this.assignedOfferings.addAll(assignedOfferings);
    }

    public SosOffering getFirstAssignedOffering() {
        if (isSetAssignedOfferings()) {
            return assignedOfferings.get(0);
        }
        return null;
    }

    public boolean isSetAssignedOfferings() {
        return CollectionHelper.isNotEmpty(getAssignedOfferings());
    }

    /**
     * @return <code>true</code>, if the sensor type flag is set
     */
    public boolean isType() {
        if (hasExtension(SENSOR_TYPE_FLAG)) {
            return getExtensions().isBooleanExtensionSet(SENSOR_TYPE_FLAG);
        }
        return false;
    }
}
