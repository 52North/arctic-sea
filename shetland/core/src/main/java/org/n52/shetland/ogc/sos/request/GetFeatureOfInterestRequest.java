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

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.n52.shetland.ogc.filter.SpatialFilter;
import org.n52.shetland.ogc.filter.TemporalFilter;
import org.n52.shetland.ogc.ows.service.OwsServiceRequest;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.util.CollectionHelper;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Sos GetFeatureOfInterst request
 *
 * @since 1.0.0
 */
public class GetFeatureOfInterestRequest extends OwsServiceRequest {

    /**
     * FOI identifiers list
     */
    private List<String> featureIdentifiers = new LinkedList<>();

    /**
     * FOI observedProperties list
     */
    private List<String> observedProperties = new LinkedList<>();

    /**
     * FOI procedures list
     */
    private List<String> procedures = new LinkedList<>();

    /**
     * FOI spatial filters list
     */
    private List<SpatialFilter> spatialFilters = new LinkedList<>();

    /**
     * FOI temporal filters list
     */
    private List<TemporalFilter> temporalFilters = new LinkedList<>();

    private Map<String, String> namespaces = new LinkedHashMap<>();

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
        return Collections.unmodifiableList(temporalFilters);
    }

    /**
     * Set temporal filters
     *
     * @param temporalFilters
     *                        temporal filters
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public GetFeatureOfInterestRequest setTemporalFilters(List<TemporalFilter> temporalFilters) {
        this.temporalFilters.clear();
        if (temporalFilters != null) {
            this.temporalFilters.addAll(temporalFilters);
        }
        return this;
    }

    /**
     * Get FOI identifiers
     *
     * @return FOI identifiers
     */
    public List<String> getFeatureIdentifiers() {
        return Collections.unmodifiableList(featureIdentifiers);
    }

    /**
     * Set FOI identifiers
     *
     * @param featureIDs
     *                   FOI identifiers
     */
    public GetFeatureOfInterestRequest setFeatureIdentifiers(Collection<String> featureIDs) {
        this.featureIdentifiers.clear();
        if (featureIDs != null) {
            this.featureIdentifiers.addAll(featureIDs);
        }
        return this;
    }

    /**
     * Get FOI observedProperties
     *
     * @return FOI observedProperties
     */
    public List<String> getObservedProperties() {
        return Collections.unmodifiableList(observedProperties);
    }

    /**
     * Set FOI observedProperties
     *
     * @param observedProperties
     *                           FOI observedProperties
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public GetFeatureOfInterestRequest setObservedProperties(List<String> observedProperties) {
        this.observedProperties.clear();
        if (observedProperties != null) {
            this.observedProperties.addAll(observedProperties);
        }
        return this;
    }

    /**
     * Get FOI procedures
     *
     * @return FOI procedures
     */
    public List<String> getProcedures() {
        return Collections.unmodifiableList(procedures);
    }

    /**
     * Set FOI procedures
     *
     * @param procedures
     *                   FOI procedures
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public GetFeatureOfInterestRequest setProcedures(List<String> procedures) {
        this.procedures.clear();
        if (procedures != null) {
            this.procedures.addAll(procedures);
        }
        return this;
    }

    /**
     * Get spatial filters
     *
     * @return spatial filters
     */
    public List<SpatialFilter> getSpatialFilters() {
        return Collections.unmodifiableList(spatialFilters);
    }

    /**
     * Set spatial filters
     *
     * @param spatialFilters
     *                       spatial filters
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public GetFeatureOfInterestRequest setSpatialFilters(List<SpatialFilter> spatialFilters) {
        this.spatialFilters.clear();
        if (spatialFilters != null) {
            this.spatialFilters.addAll(spatialFilters);
        }
        return this;
    }

    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public GetFeatureOfInterestRequest setNamespaces(Map<String, String> namespaces) {
        this.namespaces.clear();
        if (namespaces != null) {
            this.namespaces.putAll(namespaces);
        }
        return this;
    }

    public Map<String, String> getNamespaces() {
        return Collections.unmodifiableMap(namespaces);
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
