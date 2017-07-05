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
package org.n52.shetland.ogc.sos.response;

import org.n52.shetland.ogc.om.ObservationMergeIndicator;
import org.n52.shetland.ogc.om.ObservationStream;
import org.n52.shetland.ogc.ows.service.OwsServiceResponse;
import org.n52.shetland.ogc.ows.service.ResponseFormat;

import com.google.common.base.Strings;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 *
 * @since 4.0.0
 */
public abstract class AbstractObservationResponse extends OwsServiceResponse implements ResponseFormat {
    private ObservationStream observationCollection;
    private String responseFormat;
    private String resultModel;
    private boolean mergeObservation = false;
    private ObservationMergeIndicator observationMergeIndicator;
    private GlobalObservationResponseValues globalValues;

    public AbstractObservationResponse() {
    }

    public AbstractObservationResponse(String service, String version) {
        super(service, version);
    }

    public AbstractObservationResponse(String service, String version, String operationName) {
        super(service, version, operationName);
    }

    public ObservationStream getObservationCollection() {
        return observationCollection;
    }

    public void setObservationCollection(final ObservationStream observationCollection) {
        this.observationCollection = observationCollection;
    }

    @Override
    public String getResponseFormat() {
        return responseFormat;
    }

    @Override
    public void setResponseFormat(final String responseFormat) {
        this.responseFormat = responseFormat;
    }

    @Override
    public boolean isSetResponseFormat() {
        return !Strings.isNullOrEmpty(getResponseFormat());
    }

    public void setResultModel(final String resultModel) {
        this.resultModel = resultModel;
    }

    public String getResultModel() {
        return resultModel;
    }

    public boolean isSetResultModel() {
        return resultModel != null;
    }

    public void setMergeObservations(boolean mergeObservation) {
        this.mergeObservation = mergeObservation;
    }

    public boolean isSetMergeObservation() {
        return mergeObservation;
    }

    public void setObservationMergeIndicator(ObservationMergeIndicator indicator) {
        this.observationMergeIndicator = indicator;
    }

    public ObservationMergeIndicator getObservationMergeIndicator() {
        if (this.observationMergeIndicator == null) {
            setObservationMergeIndicator(new ObservationMergeIndicator());
        }
        return this.observationMergeIndicator;
    }

    public AbstractObservationResponse setGlobalObservationValues(GlobalObservationResponseValues globalValues) {
        this.globalValues = globalValues;
        return this;
    }

    public GlobalObservationResponseValues getGlobalObservationValues() {
        return globalValues;
    }

    public boolean hasGlobalObservationValues() {
        return getGlobalObservationValues() != null && !getGlobalObservationValues().isEmpty();
    }

    @Override
    public void close() {
        this.observationCollection.close();
    }

}
