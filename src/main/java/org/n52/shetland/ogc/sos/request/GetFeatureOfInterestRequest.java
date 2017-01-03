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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.n52.shetland.ogc.filter.SpatialFilter;
import org.n52.shetland.ogc.filter.TemporalFilter;
import org.n52.shetland.ogc.ows.service.OwsServiceRequest;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.util.CollectionHelper;

/**
 * Sos GetFeatureOfInterst request
 *
 * @since 4.0.0
 */
public class GetFeatureOfInterestRequest extends OwsServiceRequest {

    /**
     * FOI identifiers list
     */
    private List<String> featureIdentifiers = new ArrayList<>();

    /**
     * FOI observedProperties list
     */
    private List<String> observedProperties;

    /**
     * FOI procedures list
     */
    private List<String> procedures;

    /**
     * FOI spatial filters list
     */
    private List<SpatialFilter> spatialFilters;

    /**
     * FOI temporal filters list
     */
    private List<TemporalFilter> temporalFilters;

    private Map<String, String> namespaces;

    public GetFeatureOfInterestRequest() {
        super(null, null, SosConstants.Operations.GetFeatureOfInterest.name());
    }

    public GetFeatureOfInterestRequest(String service, String version) {
        super(service, version, SosConstants.Operations.GetFeatureOfInterest.name());
    }

    public GetFeatureOfInterestRequest(String service, String version, String operationName) {
        super(service, version, operationName);
    }

    /**
     * Get temporal filters
     *
     * @return temporal filters
     */
    public List<TemporalFilter> getTemporalFilters() {
        return temporalFilters;
    }

    /**
     * Set temporal filters
     *
     * @param temporalFilters
     *                        temporal filters
     */
    public void setTemporalFilters(List<TemporalFilter> temporalFilters) {
        this.temporalFilters = temporalFilters;
    }

    /**
     * Get FOI identifiers
     *
     * @return FOI identifiers
     */
    public List<String> getFeatureIdentifiers() {
        return featureIdentifiers;
    }

    /**
     * Set FOI identifiers
     *
     * @param featureIDs
     *                   FOI identifiers
     */
    public void setFeatureIdentifiers(Collection<String> featureIDs) {
        this.featureIdentifiers.clear();
        if (featureIDs != null) {
            this.featureIdentifiers.addAll(featureIDs);
        }
    }

    /**
     * Get FOI observedProperties
     *
     * @return FOI observedProperties
     */
    public List<String> getObservedProperties() {
        return observedProperties;
    }

    /**
     * Set FOI observedProperties
     *
     * @param observedProperties
     *                           FOI observedProperties
     */
    public void setObservedProperties(List<String> observedProperties) {
        this.observedProperties = observedProperties;
    }

    /**
     * Get FOI procedures
     *
     * @return FOI procedures
     */
    public List<String> getProcedures() {
        return procedures;
    }

    /**
     * Set FOI procedures
     *
     * @param procedures
     *                   FOI procedures
     */
    public void setProcedures(List<String> procedures) {
        this.procedures = procedures;
    }

    /**
     * Get spatial filters
     *
     * @return spatial filters
     */
    public List<SpatialFilter> getSpatialFilters() {
        return spatialFilters;
    }

    /**
     * Set spatial filters
     *
     * @param spatialFilters
     *                       spatial filters
     */
    public void setSpatialFilters(List<SpatialFilter> spatialFilters) {
        this.spatialFilters = spatialFilters;
    }

    public void setNamespaces(Map<String, String> namespaces) {
        this.namespaces = namespaces;
    }

    public Map<String, String> getNamespaces() {
        return namespaces;
    }

    public boolean isSetFeatureOfInterestIdentifiers() {
        return CollectionHelper.isNotEmpty(getFeatureIdentifiers());
    }

    public boolean isSetTemporalFilters() {
        return CollectionHelper.isNotEmpty(getTemporalFilters());
    }

    public boolean isSetSpatialFilters() {
        return CollectionHelper.isNotEmpty(getSpatialFilters());
    }

    public boolean isSetObservableProperties() {
        return CollectionHelper.isNotEmpty(getObservedProperties());
    }

    public boolean isSetProcedures() {
        return CollectionHelper.isNotEmpty(getProcedures());
    }

    public boolean isSetNamespaces() {
        return CollectionHelper.isNotEmpty(namespaces);
    }

    public boolean containsOnlyFeatureParameter() {
        return !isSetObservableProperties() && !isSetProcedures() && !isSetTemporalFilters();
    }

    public boolean hasNoParameter() {
        return !isSetObservableProperties() && !isSetProcedures() && !isSetTemporalFilters() &&
               !isSetFeatureOfInterestIdentifiers() && !isSetSpatialFilters();
    }

    public boolean hasParameter() {
        return !hasNoParameter();
    }
}
