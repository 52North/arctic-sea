/*
 * Copyright 2015-2021 52°North Initiative for Geospatial Open Source
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

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.n52.shetland.ogc.filter.ComparisonFilter;
import org.n52.shetland.ogc.filter.Filter;
import org.n52.shetland.ogc.filter.SpatialFilter;
import org.n52.shetland.ogc.ows.service.OwsServiceRequest;
import org.n52.shetland.ogc.ows.service.ResponseFormat;
import org.n52.shetland.ogc.sos.ResultFilter;
import org.n52.shetland.ogc.sos.ResultFilterConstants;
import org.n52.shetland.ogc.sos.SosSpatialFilter;
import org.n52.shetland.ogc.sos.SosSpatialFilterConstants;
import org.n52.shetland.ogc.sos.request.SpatialFeatureQueryRequest;
import org.n52.shetland.util.CollectionHelper;

import com.google.common.base.Strings;

/**
 * A request to obtain the {@code DataAvailabilites} of the SOS.
 *
 * @author Christian Autermann
 *
 * @since 1.0.0
 */
public class GetDataAvailabilityRequest extends OwsServiceRequest
        implements ResponseFormat, SpatialFeatureQueryRequest {

    private List<String> procedures = new LinkedList<>();
    private final List<String> observedProperties = new LinkedList<>();
    private final List<String> featuresOfInterest = new LinkedList<>();
    private final List<String> offerings = new LinkedList<>();
    private String namespace = GetDataAvailabilityConstants.NS_GDA_20;
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
     * @param procedure the {@code procedure}
     */
    public void addProcedure(String procedure) {
        this.procedures.add(procedure);
    }

    /**
     * Add a {@code observedProperty} to the request.
     *
     * @param observedProperty the {@code observedProperty}
     */
    public void addObservedProperty(String observedProperty) {
        this.observedProperties.add(observedProperty);
    }

    /**
     * Add a {@code featureOfInterest} to the request.
     *
     * @param featureOfInterest the {@code featureOfInterest}
     */
    public void addFeatureOfInterest(String featureOfInterest) {
        this.featuresOfInterest.add(featureOfInterest);
    }

    public void setFeatureOfInterest(Collection<String> featuresOfInterest) {
        this.featuresOfInterest.clear();
        this.featuresOfInterest.addAll(featuresOfInterest);
    }

    /**
     * Add a {@code offering} to the request.
     *
     * @param offering the {@code offering}
     */
    public void addOffering(String offering) {
        this.offerings.add(offering);
    }

    public void setOfferings(Collection<String> offerings) {
        this.offerings.clear();
        this.offerings.addAll(offerings);
    }

    public boolean isSetProcedures() {
        return CollectionHelper.isNotEmpty(getProcedures());
    }

    public void setProcedure(List<String> procedures) {
        this.procedures.clear();
        this.procedures.addAll(procedures);
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
        this.observedProperties.clear();
        this.observedProperties.addAll(observedProperties);
    }

    public boolean isSetFeaturesOfInterest() {
        return CollectionHelper.isNotEmpty(getFeaturesOfInterest());
    }

    public boolean isSetOfferings() {
        return CollectionHelper.isNotEmpty(getOfferings());
    }

    @Override
    public String getResponseFormat() {
        return Strings.isNullOrEmpty(responseFormat) ? getNamespace() : responseFormat;
    }

    @Override
    public void setResponseFormat(String responseFormat) {
        this.responseFormat = responseFormat;
    }

    public String getNamespace() {
        return this.namespace;
    }

    public GetDataAvailabilityRequest setNamespace(String namespace) {
        this.namespace = namespace;
        return this;
    }

    public boolean hasResultFilter() {
        return hasExtension(ResultFilterConstants.RESULT_FILTER) &&
               getExtension(ResultFilterConstants.RESULT_FILTER).isPresent() &&
               getExtension(ResultFilterConstants.RESULT_FILTER).get() instanceof ResultFilter;
    }

    public Filter<?> getResultFilter() {
        if (hasResultFilter()) {
            return ((ResultFilter) getExtension(ResultFilterConstants.RESULT_FILTER).get()).getValue();
        }
        return null;
    }

    public GetDataAvailabilityRequest setResultFilter(ComparisonFilter filter) {
        addExtension(new ResultFilter(filter));
        return this;
    }

    public boolean hasSpatialFilter() {
        return hasExtension(SosSpatialFilterConstants.SPATIAL_FILTER) &&
               getExtension(SosSpatialFilterConstants.SPATIAL_FILTER).isPresent() &&
               getExtension(SosSpatialFilterConstants.SPATIAL_FILTER).get() instanceof SosSpatialFilter;
    }

    @Override
    public SpatialFilter getSpatialFilter() {
        if (!hasSpatialFilter()) {
            return null;
        }
        return ((SosSpatialFilter) getExtension(SosSpatialFilterConstants.SPATIAL_FILTER).get()).getValue();
    }

    @Override
    public void setSpatialFilter(SpatialFilter filter) {
        addExtension(new SosSpatialFilter(filter));
    }

    @Override
    public List<String> getFeatureIdentifiers() {
        return getFeaturesOfInterest();
    }

    @Override
    public void setFeatureIdentifiers(List<String> featureIdentifiers) {
        setFeatureOfInterest(featureIdentifiers);
    }
}
