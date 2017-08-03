/*
 * Copyright 2016-2017 52°North Initiative for Geospatial Open Source
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

import org.n52.shetland.ogc.gml.time.Time;
import org.n52.shetland.ogc.ows.service.OwsServiceRequest;
import org.n52.shetland.ogc.sos.SosConstants;

import com.google.common.base.Strings;

/**
 * SOS DescribeSensor request
 *
 * @since 1.0.0
 */
public class DescribeSensorRequest extends OwsServiceRequest {

    /**
     * Procedure identifier
     */
    private String procedure;

    /**
     * Output format
     */
    private String procedureDescriptionFormat;

    /**
     * Temporal filters
     */
    private Time validTime;

    public DescribeSensorRequest() {
        super(null, null, SosConstants.Operations.DescribeSensor.name());
    }

    public DescribeSensorRequest(String service, String version) {
        super(service, version, SosConstants.Operations.DescribeSensor.name());
    }

    public DescribeSensorRequest(String service, String version, String operationName) {
        super(service, version, operationName);
    }


    /**
     * Get output format
     *
     * @return output format
     */
    public String getProcedureDescriptionFormat() {
        return procedureDescriptionFormat;
    }

    /**
     * Set output format
     *
     * @param procedureDescriptionFormat
     *            output format
     */
    public void setProcedureDescriptionFormat(String procedureDescriptionFormat) {
        this.procedureDescriptionFormat = procedureDescriptionFormat;
    }

    public boolean isSetProcedureDescriptionFormat() {
        return !Strings.isNullOrEmpty(getProcedureDescriptionFormat());
    }

    /**
     * Get Procedure identifier
     *
     * @return Procedure identifier
     */
    public String getProcedure() {
        return procedure;
    }

    /**
     * Set Procedure identifier
     *
     * @param procedure
     *            Procedure identifier
     */
    public void setProcedure(String procedure) {
        this.procedure = procedure;
    }

    public boolean isSetProcedure() {
        return !Strings.isNullOrEmpty(getProcedure());
    }

    /**
     * Get valid time
     *
     * @return valid time
     */
    public Time getValidTime() {
        return validTime;
    }

    /**
     * Set valid time
     *
     * @param validTime
     *            valid time
     */
    public void setValidTime(Time validTime) {
        this.validTime = validTime;
    }

    public boolean isSetValidTime() {
        return getValidTime() != null;
    }

}
