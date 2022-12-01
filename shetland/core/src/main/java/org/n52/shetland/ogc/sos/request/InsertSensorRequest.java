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
package org.n52.shetland.ogc.sos.request;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.n52.shetland.ogc.ows.service.OwsServiceRequest;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosInsertionMetadata;
import org.n52.shetland.ogc.sos.SosOffering;
import org.n52.shetland.ogc.sos.SosProcedureDescription;
import org.n52.shetland.ogc.swes.SwesFeatureRelationship;
import org.n52.shetland.util.CollectionHelper;

import com.google.common.base.Strings;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * @since 1.0.0
 *
 */
public class InsertSensorRequest extends OwsServiceRequest {

    private static final String SENSOR_TYPE_FLAG = "isType";
    private String procedureDescriptionFormat;

    /**
     * observableProperty parameter
     */
    private List<String> observableProperty = new LinkedList<>();
    private List<SwesFeatureRelationship> relatedFeatures = new LinkedList<>();

    /**
     * SOS SensorML description
     */
    private SosProcedureDescription<?> procedureDescription;
    private String assignedProcedureIdentifier;
    private List<SosOffering> assignedOfferings = new LinkedList<>();

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

    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public InsertSensorRequest setProcedureDescriptionFormat(String procedureDescriptionFormat) {
        this.procedureDescriptionFormat = procedureDescriptionFormat;
        return this;
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
        return Collections.unmodifiableList(observableProperty);
    }

    /**
     * Set the observableProperty contained in request.
     *
     * @param observableProperty
     *            the observableProperty to set
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public InsertSensorRequest setObservableProperty(Collection<String> observableProperty) {
        this.observableProperty.clear();
        if (observableProperty != null) {
            this.observableProperty.addAll(observableProperty);
        }
        return this;
    }

    public boolean isSetObservableProperty() {
        return CollectionHelper.isNotEmpty(getObservableProperty());
    }

    /**
     * Get the sensor description contained in request.
     *
     * @return the sosSensorML
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP" })
    public SosProcedureDescription<?> getProcedureDescription() {
        return procedureDescription;
    }

    /**
     * Set the sensor description contained in request.
     *
     * @param procedureDescription
     *            the procedureDescription to set
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public InsertSensorRequest setProcedureDescription(SosProcedureDescription<?> procedureDescription) {
        this.procedureDescription = procedureDescription;
        return this;
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
     *            the metadata to set
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public InsertSensorRequest setMetadata(SosInsertionMetadata metadata) {
        this.metadata = metadata;
        return this;
    }

    public boolean isSetMetadata() {
        return getMetadata() != null;
    }

    public List<SwesFeatureRelationship> getRelatedFeatures() {
        return Collections.unmodifiableList(relatedFeatures);
    }

    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public InsertSensorRequest setRelatedFeature(Collection<SwesFeatureRelationship> relatedFeatures) {
        this.relatedFeatures.clear();
        if (relatedFeatures != null) {
            this.relatedFeatures.addAll(relatedFeatures);
        }
        return this;
    }

    public boolean isSetRelatedFeatures() {
        return CollectionHelper.isNotEmpty(getRelatedFeatures());
    }

    public String getAssignedProcedureIdentifier() {
        return assignedProcedureIdentifier;
    }

    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public InsertSensorRequest setAssignedProcedureIdentifier(String assignedProcedureID) {
        this.assignedProcedureIdentifier = assignedProcedureID;
        return this;
    }

    public boolean isSetAssignedProcedureIdentifier() {
        return !Strings.isNullOrEmpty(getAssignedProcedureIdentifier());
    }

    public List<SosOffering> getAssignedOfferings() {
        return Collections.unmodifiableList(assignedOfferings);
    }

    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public InsertSensorRequest setAssignedOfferings(Collection<SosOffering> assignedOfferings) {
        this.assignedOfferings.clear();
        if (assignedOfferings != null) {
            this.assignedOfferings.addAll(assignedOfferings);
        }
        return this;
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
        return getBooleanExtension(SENSOR_TYPE_FLAG);
    }
}
