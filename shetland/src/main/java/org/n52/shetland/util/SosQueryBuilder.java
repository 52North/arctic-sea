/*
 * Copyright 2015-2017 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

import org.n52.shetland.ogc.ows.OWSConstants;
import org.n52.shetland.ogc.sos.Sos1Constants;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;

public class SosQueryBuilder extends OwsQueryBuilder {

    public SosQueryBuilder(URL url) {
        super(url);
    }

    public SosQueryBuilder(URL url, Charset charset) {
        super(url, charset);
    }

    public SosQueryBuilder(String url) throws MalformedURLException {
        super(url);
    }

    public SosQueryBuilder addProcedure(String procedure) {
        add(SosConstants.DescribeSensorParams.procedure, procedure);
        return this;
    }

    public SosQueryBuilder addOutputFormat(String outputFormat) {
        add(Sos1Constants.DescribeSensorParams.outputFormat, outputFormat);
        return this;
    }

    public SosQueryBuilder addProcedureDescriptionFormat(String procedureDescriptionFormat) {
        add(Sos2Constants.DescribeSensorParams.procedureDescriptionFormat, procedureDescriptionFormat);
        return this;
    }

    public SosQueryBuilder addFeatureOfInterestId(String foi) {
        add(Sos1Constants.GetFeatureOfInterestParams.featureOfInterestID, foi);
        return this;
    }

    public SosQueryBuilder addFeatureOfInterest(String foi) {
        add(Sos2Constants.GetFeatureOfInterestParams.featureOfInterest, foi);
        return this;
    }

    public SosQueryBuilder addService() {
        addService(SosConstants.SOS);
        return this;
    }

    public SosQueryBuilder addDescribeSensorRequest() {
        addRequest(SosConstants.Operations.DescribeSensor);
        return this;
    }

    public SosQueryBuilder addGetCapabilitiesRequest() {
        addRequest(SosConstants.Operations.GetCapabilities);
        return this;
    }

    public SosQueryBuilder addGetFeatureOfInterestRequest() {
        addRequest(SosConstants.Operations.GetFeatureOfInterest);
        return this;
    }

    public SosQueryBuilder addGetObservationRequest() {
        addRequest(SosConstants.Operations.GetObservation);
        return this;
    }

    public SosQueryBuilder addGetObservationByIdRequest() {
        addRequest(SosConstants.Operations.GetObservationById);
        return this;
    }

    public SosQueryBuilder addGetResultRequest() {
        addRequest(SosConstants.Operations.GetResult);
        return this;
    }

    public SosQueryBuilder addDescribeObservationTypeRequest() {
        addRequest(Sos1Constants.Operations.DescribeObservationType);
        return this;
    }

    public SosQueryBuilder addDescribeFeatureTypeRequest() {
        addRequest(Sos1Constants.Operations.DescribeFeatureType);
        return this;
    }

    public SosQueryBuilder addDescribeResultModelRequest() {
        addRequest(Sos1Constants.Operations.DescribeResultModel);
        return this;
    }

    public SosQueryBuilder addGetFeatureOfInterestTimeRequest() {
        addRequest(Sos1Constants.Operations.GetFeatureOfInterestTime);
        return this;
    }

    public SosQueryBuilder addGetResultTemplateRequest() {
        addRequest(Sos2Constants.Operations.GetResultTemplate);
        return this;
    }

    public SosQueryBuilder addLanguage(String language) {
        add(OWSConstants.AdditionalRequestParams.language, language);
        return this;
    }

    public SosQueryBuilder addCRS(String crs) {
        add(OWSConstants.AdditionalRequestParams.crs, crs);
        return this;
    }

    public SosQueryBuilder addOffering(String offering) {
        add(SosConstants.GetObservationParams.offering, offering);
        return this;
    }
}
