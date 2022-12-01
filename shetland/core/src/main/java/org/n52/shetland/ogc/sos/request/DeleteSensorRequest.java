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

import org.n52.shetland.ogc.ows.service.OwsServiceRequest;
import org.n52.shetland.ogc.sos.Sos2Constants;

/**
 * @since 1.0.0
 *
 */
public class DeleteSensorRequest
        extends OwsServiceRequest {

    private String procedureIdentifier;

    public DeleteSensorRequest() {
        super(null, null, Sos2Constants.Operations.DeleteSensor.name());
    }

    public DeleteSensorRequest(String service, String version) {
        super(service, version, Sos2Constants.Operations.DeleteSensor.name());
    }

    public DeleteSensorRequest(String service, String version, String operationName) {
        super(service, version, operationName);
    }

    /**
     * @param procedureIdentifier
     *            the procedureIdentifier to set
     */
    public void setProcedureIdentifier(String procedureIdentifier) {
        this.procedureIdentifier = procedureIdentifier;
    }

    /**
     * @return the procedureIdentifier
     */
    public String getProcedureIdentifier() {
        return procedureIdentifier;
    }

}
