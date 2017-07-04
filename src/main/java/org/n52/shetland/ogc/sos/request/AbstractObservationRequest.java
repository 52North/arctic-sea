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

import org.n52.shetland.ogc.ows.service.OwsServiceRequest;
import org.n52.shetland.ogc.ows.service.ResponseFormat;

import com.google.common.base.Strings;

/**
 * SOS AbstractObservation request
 *
 * @since 4.0.0
 */
public abstract class AbstractObservationRequest extends OwsServiceRequest
        implements ResponseFormat, SrsNameRequest {
    /**
     * SRS name
     */
    private String srsName;

    /**
     * Response format
     */
    private String responseFormat;

    /**
     * Result model
     */
    private String resultModel;

    /**
     * Response mode
     */
    private String responseMode;

    private boolean checkForDuplicity = false;

    public AbstractObservationRequest() {
    }

    public AbstractObservationRequest(String service, String version) {
        super(service, version);
    }

    public AbstractObservationRequest(String service, String version, String operationName) {
        super(service, version, operationName);
    }

    /**
     * Get response format
     *
     * @return response format
     */
    @Override
    public String getResponseFormat() {
        return responseFormat;
    }

    /**
     * Set response format
     *
     * @param responseFormat
     *                       response format
     */
    @Override
    public void setResponseFormat(String responseFormat) {
        this.responseFormat = responseFormat;
    }

    @Override
    public boolean isSetResponseFormat() {
        return !Strings.isNullOrEmpty(getResponseFormat());
    }

    /**
     * Get response mode
     *
     * @return response mode
     */
    public String getResponseMode() {
        return responseMode;
    }

    /**
     * Set response mode
     *
     * @param responseMode
     *                     response mode
     */
    public void setResponseMode(String responseMode) {
        this.responseMode = responseMode;
    }

    public boolean isSetResponseMode() {
        return !Strings.isNullOrEmpty(getResponseMode());
    }

    /**
     * Get result model
     *
     * @return result model
     */
    public String getResultModel() {
        return resultModel;
    }

    /**
     * Set result model
     *
     * @param resultModel
     *                    result model
     */
    public void setResultModel(String resultModel) {
        this.resultModel = resultModel;
    }

    public boolean isSetResultModel() {
        return !Strings.isNullOrEmpty(getResultModel());
    }

    @Override
    public String getSrsName() {
        return srsName;
    }

    @Override
    public void setSrsName(String srsName) {
        this.srsName = srsName;
    }

    @Override
    public boolean isSetSrsName() {
        return !Strings.isNullOrEmpty(getSrsName());
    }

    /**
     * @return the checkForDuplicity
     */
    public boolean isCheckForDuplicity() {
        return checkForDuplicity;
    }

    /**
     * @param checkForDuplicity the checkForDuplicity to set
     */
    public void setCheckForDuplicity(boolean checkForDuplicity) {
        this.checkForDuplicity = checkForDuplicity;
    }


    public void copyOf(AbstractObservationRequest res) {
        res.setResponseFormat(this.responseFormat);
        res.setResponseMode(this.responseMode);
        res.setResultModel(this.resultModel);
        res.setSrsName(this.srsName);
        res.setCheckForDuplicity(this.isCheckForDuplicity());
    }

}
