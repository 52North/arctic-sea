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
package org.n52.shetland.ogc.sos.gda;

import java.util.LinkedList;
import java.util.List;

import org.n52.shetland.ogc.ows.service.OwsServiceRequest;
import org.n52.shetland.ogc.ows.service.ResponseFormat;
import org.n52.shetland.util.CollectionHelper;

import com.google.common.base.Strings;

/**
 * A request to obtain the {@code DataAvailabilites} of the SOS.
 *
 * @author Christian Autermann
 *
 * @since 4.0.0
 */
public class GetDataAvailabilityRequest extends OwsServiceRequest implements ResponseFormat {

    private List<String> procedures = new LinkedList<>();
    private List<String> observedProperties = new LinkedList<>();
    private List<String> featuresOfInterest = new LinkedList<>();
    private List<String> offerings = new LinkedList<>();
    private String namspace = GetDataAvailabilityConstants.NS_GDA;
    private String responseFormat;

    public GetDataAvailabilityRequest() {
        super(null, null, GetDataAvailabilityConstants.OPERATION_NAME);
    }

    public GetDataAvailabilityRequest(String service, String version) {
        super(service, version, GetDataAvailabilityConstants.OPERATION_NAME);
    }

    public GetDataAvailabilityRequest(String service, String version, String operationName) {
        super(service, version, operationName);
    }

    /**
     * @return the requested {@code procedures}.
     */
    public List<String> getProcedures() {
        return procedures;
    }

    /**
     * @return the requested {@code observedProperties}.
     */
    public List<String> getObservedProperties() {
        return observedProperties;
    }

    /**
     * @return the requested {@code featuresOfInterest}.
     */
    public List<String> getFeaturesOfInterest() {
        return featuresOfInterest;
    }

    /**
     * @return the requested {@code offerings}.
     */
    public List<String> getOfferings() {
        return offerings;
    }

    /**
     * Add a {@code procedure} to the request.
     *
     * @param procedure
     *                  the {@code procedure}
     */
    public void addProcedure(String procedure) {
        this.procedures.add(procedure);
    }

    /**
     * Add a {@code observedProperty} to the request.
     *
     * @param observedProperty
     *                         the {@code observedProperty}
     */
    public void addObservedProperty(String observedProperty) {
        this.observedProperties.add(observedProperty);
    }

    /**
     * Add a {@code featureOfInterest} to the request.
     *
     * @param featureOfInterest
     *                          the {@code featureOfInterest}
     */
    public void addFeatureOfInterest(String featureOfInterest) {
        this.featuresOfInterest.add(featureOfInterest);
    }

    public void setFeatureOfInterest(List<String> featuresOfInterest) {
        this.featuresOfInterest = featuresOfInterest;
    }

    /**
     * Add a {@code offering} to the request.
     *
     * @param offering
     *                 the {@code offering}
     */
    public void addOffering(String offering) {
        this.offerings.add(offering);
    }

    public void setOffering(List<String> offerings) {
        this.offerings = offerings;
    }

    public boolean isSetProcedures() {
        return CollectionHelper.isNotEmpty(getProcedures());
    }

    public void setProcedure(List<String> procedures) {
        this.procedures = procedures;
    }

    public boolean isSetProcedure() {
        return CollectionHelper.isNotEmpty(getProcedures());
    }

    public void setProcedures(List<String> procedures) {
        this.procedures = procedures;
    }

    public boolean isSetObservedProperties() {
        return CollectionHelper.isNotEmpty(getObservedProperties());
    }

    public void setObservedProperty(List<String> observedProperties) {
        this.observedProperties = observedProperties;
    }

    public boolean isSetFeaturesOfInterest() {
        return CollectionHelper.isNotEmpty(getFeaturesOfInterest());
    }

    public boolean isSetOfferings() {
        return CollectionHelper.isNotEmpty(getOfferings());
    }

    @Override
    public String getResponseFormat() {
        return responseFormat;
    }

    @Override
    public void setResponseFormat(String responseFormat) {
        this.responseFormat = responseFormat;
    }

    @Override
    public boolean isSetResponseFormat() {
        return !Strings.isNullOrEmpty(getResponseFormat());
    }

    public GetDataAvailabilityRequest setNamespace(String namspace) {
        if (!Strings.isNullOrEmpty(namspace)) {
            this.namspace = namspace;
        }
        return this;
    }

    public String getNamespace() {
        return this.namspace;
    }

}
