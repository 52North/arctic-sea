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

import com.google.common.base.Strings;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * @since 1.0.0
 *
 */
public class GetResultRequest extends OwsServiceRequest implements SpatialFeatureQueryRequest {

    /**
     * Identifier for the observation template
     */
    private String observationTemplateIdentifier;

    private String offering;

    private String observedProperty;

    private List<String> featureIdentifiers = new LinkedList<>();

    private List<TemporalFilter> temporalFilter = new LinkedList<>();

    private SpatialFilter spatialFilter;

    private Map<String, String> namespaces = new LinkedHashMap<>();

    public GetResultRequest() {
        super(null, null, SosConstants.Operations.GetResult.name());
    }

    public GetResultRequest(String service, String version) {
        super(service, version, SosConstants.Operations.GetResult.name());
    }

    public GetResultRequest(String service, String version, String operationName) {
        super(service, version, operationName);
    }

    /**
     * Get observation template identifier
     *
     * @return observation template identifier
     */
    public String getObservationTemplateIdentifier() {
        return observationTemplateIdentifier;
    }

    /**
     * Set observation template identifier
     *
     * @param observationTemplateIdentifier
     *            observation template identifier
     * @return this
     */
    public GetResultRequest setObservationTemplateIdentifier(String observationTemplateIdentifier) {
        this.observationTemplateIdentifier = observationTemplateIdentifier;
        return this;
    }

    public boolean isSetObservationTemplateIdentifier() {
        return !Strings.isNullOrEmpty(getObservationTemplateIdentifier());
    }

    public String getOffering() {
        return offering;
    }

    public GetResultRequest setOffering(String offering) {
        this.offering = offering;
        return this;
    }

    public boolean isSetOffering() {
        return !Strings.isNullOrEmpty(getOffering());
    }

    public String getObservedProperty() {
        return observedProperty;
    }

    public GetResultRequest setObservedProperty(String observedProperty) {
        this.observedProperty = observedProperty;
        return this;
    }

    public boolean isSetObservedProperty() {
        return !Strings.isNullOrEmpty(getObservedProperty());
    }

    /**
     * Get FOI identifiers
     *
     * @return FOI identifiers
     */
    @Override
    public List<String> getFeatureIdentifiers() {
        return Collections.unmodifiableList(featureIdentifiers);
    }

    /**
     * Set FOI identifiers
     *
     * @param featureIdentifiers
     *            FOI identifiers
     * @return this
     */
    @Override
    public void setFeatureIdentifiers(List<String> featureIdentifiers) {
        this.featureIdentifiers.clear();
        if (featureIdentifiers != null) {
            this.featureIdentifiers.addAll(featureIdentifiers);
        }
    }

    public List<TemporalFilter> getTemporalFilter() {
        return Collections.unmodifiableList(temporalFilter);
    }

    public GetResultRequest setTemporalFilter(Collection<TemporalFilter> temporalFilters) {
        this.temporalFilter.clear();
        if (temporalFilters != null) {
            this.temporalFilter.addAll(temporalFilters);
        }
        return this;
    }

    public boolean hasTemporalFilter() {
        return CollectionHelper.isNotEmpty(getTemporalFilter());
    }

    @Override
    @SuppressFBWarnings({ "EI_EXPOSE_REP" })
    public SpatialFilter getSpatialFilter() {
        return spatialFilter;
    }

    @Override
    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public void setSpatialFilter(SpatialFilter spatialFilter) {
        this.spatialFilter = spatialFilter;
    }

    public Map<String, String> getNamespaces() {
        return Collections.unmodifiableMap(namespaces);
    }

    public GetResultRequest setNamespaces(Map<String, String> namespaces) {
        this.namespaces.clear();
        if (namespaces != null) {
            this.namespaces.putAll(namespaces);
        }
        return this;
    }

    public boolean isSetNamespaces() {
        return CollectionHelper.isNotEmpty(getNamespaces());
    }
}
