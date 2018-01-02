/*
 * Copyright 2016-2018 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.ogc.sos.response;

import org.n52.shetland.ogc.ows.service.OwsServiceResponse;
import org.n52.shetland.ogc.sos.Sos2Constants;

/**
 * @since 1.0.0
 *
 */
public class InsertSensorResponse extends OwsServiceResponse {

    private String assignedProcedure;
    private String assignedOffering;

    public InsertSensorResponse() {
        super(null, null, Sos2Constants.Operations.InsertSensor.name());
    }

    public InsertSensorResponse(String service, String version) {
        super(service, version, Sos2Constants.Operations.InsertSensor.name());
    }

    public InsertSensorResponse(String service, String version, String operationName) {
        super(service, version, operationName);
    }

    public void setAssignedProcedure(String assignedProcedure) {
        this.assignedProcedure = assignedProcedure;
    }

    public String getAssignedProcedure() {
        return assignedProcedure;
    }

    public void setAssignedOffering(String assignedOffering) {
        this.assignedOffering = assignedOffering;
    }

    public String getAssignedOffering() {
        return assignedOffering;
    }

}
