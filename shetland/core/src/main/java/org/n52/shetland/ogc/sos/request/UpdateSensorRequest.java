/*
 * Copyright (C) 2015-2021 52°North Spatial Information Research GmbH
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

import java.util.ArrayList;
import java.util.List;

import org.n52.shetland.ogc.ows.service.OwsServiceRequest;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosProcedureDescription;
import org.n52.shetland.util.CollectionHelper;

import com.google.common.base.Strings;

/**
 * class represents a updateSensor request
 *
 * @since 1.0.0
 */
public class UpdateSensorRequest extends OwsServiceRequest {
    private String procedureIdentifier;

    private String procedureDescriptionFormat;

    /**
     * SOS SensorML description
     */
    private List<SosProcedureDescription<?>> procedureDescriptions;

    /**
     * default constructor
     */
    public UpdateSensorRequest() {
        super(null, null, Sos2Constants.Operations.UpdateSensorDescription.name());
    }

    public UpdateSensorRequest(String service, String version) {
        super(service, version, Sos2Constants.Operations.UpdateSensorDescription.name());
    }

    public UpdateSensorRequest(String service, String version, String operationName) {
        super(service, version, operationName);
    }

    /**
     * @return the procedureIdentifier
     */
    public String getProcedureIdentifier() {
        return procedureIdentifier;
    }

    /**
     * @param procedureIdentifier
     *                            the procedureIdentifier to set
     */
    public void setProcedureIdentifier(String procedureIdentifier) {
        this.procedureIdentifier = procedureIdentifier;
    }

    public boolean isSetProcedureIdentifier() {
        return !Strings.isNullOrEmpty(getProcedureIdentifier());
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

    public List<SosProcedureDescription<?>> getProcedureDescriptions() {
        return procedureDescriptions;
    }

    public void setProcedureDescriptions(List<SosProcedureDescription<?>> procedureDescriptions) {
        this.procedureDescriptions = procedureDescriptions;
    }

    public void addProcedureDescriptionString(SosProcedureDescription<?> procedureDescription) {
        if (procedureDescriptions == null) {
            procedureDescriptions = new ArrayList<>();
        }
        procedureDescriptions.add(procedureDescription);
    }

    public boolean isSetProcedureDescriptions() {
        return CollectionHelper.isNotEmpty(getProcedureDescriptions());
    }

}
