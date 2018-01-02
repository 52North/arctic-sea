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

import java.util.List;

import org.n52.shetland.ogc.ows.service.OwsServiceResponse;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.sos.SosProcedureDescription;
import org.n52.shetland.util.CollectionHelper;

/**
 * @since 1.0.0
 *
 */
public class DescribeSensorResponse extends OwsServiceResponse {

    private String outputFormat;

    private List<SosProcedureDescription<?>> procedureDescriptions;

    public DescribeSensorResponse() {
        super(null, null, SosConstants.Operations.DescribeSensor.name());
    }

    public DescribeSensorResponse(String service, String version) {
        super(service, version, SosConstants.Operations.DescribeSensor.name());
    }

    public DescribeSensorResponse(String service, String version, String operationName) {
        super(service, version, operationName);
    }

    public String getOutputFormat() {
        return outputFormat;
    }

    public void setOutputFormat(String outputFormat) {
        this.outputFormat = outputFormat;
    }

    public void setSensorDescriptions(List<SosProcedureDescription<?>> procedureDescriptions) {
        if (isSetProcedureDescriptions()) {
            this.procedureDescriptions = CollectionHelper
                    .conjunctCollections(getProcedureDescriptions(), procedureDescriptions);
        } else {
            this.procedureDescriptions = procedureDescriptions;
        }
    }

    public boolean isSetProcedureDescriptions() {
        return CollectionHelper.isNotEmpty(getProcedureDescriptions());
    }

    public List<SosProcedureDescription<?>> getProcedureDescriptions() {
        return this.procedureDescriptions;
    }

    public void addSensorDescription(SosProcedureDescription<?> procedureDescription) {
        if (isSetProcedureDescriptions()) {
            getProcedureDescriptions().add(procedureDescription);
        } else {
            this.procedureDescriptions = CollectionHelper.list(procedureDescription);
        }
    }
}
