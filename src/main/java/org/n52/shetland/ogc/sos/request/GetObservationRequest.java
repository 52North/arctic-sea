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

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.n52.janmayen.function.Functions;
import org.n52.janmayen.function.Predicates;
import org.n52.shetland.ogc.filter.Filter;
import org.n52.shetland.ogc.filter.SpatialFilter;
import org.n52.shetland.ogc.filter.TemporalFilter;
import org.n52.shetland.ogc.gml.time.IndeterminateValue;
import org.n52.shetland.ogc.gml.time.TimeInstant;
import org.n52.shetland.ogc.ows.extension.Extension;
import org.n52.shetland.ogc.ows.extension.Extensions;
import org.n52.shetland.ogc.sos.ExtendedIndeterminateTime;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.swes.SwesExtensions;
import org.n52.shetland.util.CollectionHelper;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * SOS GetObservation request.
 *
 * @since 1.0.0
 */
public class GetObservationRequest
        extends AbstractObservationRequest
        implements SpatialFeatureQueryRequest {

    /**
     * Request as String.
     */
    private String requestString;

    /**
     * Offerings list.
     */
    private List<String> offerings = Lists.newLinkedList();

    /**
     * Temporal filters list.
     */
    private List<TemporalFilter> temporalFilters = Lists.newLinkedList();

    /**
     * Procedures list.
     */
    private List<String> procedures = Lists.newLinkedList();

    /**
     * ObservedProperties list.
     */
    private List<String> observedProperties = Lists.newLinkedList();

    /**
     * FOI identifiers list.
     */
    private List<String> featureIdentifiers = Lists.newLinkedList();

    /**
     * Spatial filters list.
     */
    private SpatialFilter spatialFilter;
    @SuppressWarnings("rawtypes")
    private Filter resultFilter;
    private Map<String, String> namespaces = Maps.newHashMap();
    private boolean mergeObservationValues;

    public GetObservationRequest() {
        super(null, null, SosConstants.Operations.GetObservation.name());
    }

    public GetObservationRequest(String service, String version) {
        super(service, version, SosConstants.Operations.GetObservation.name());
    }

    public GetObservationRequest(String service, String version, String operationName) {
        super(service, version, operationName);
    }

    /**
     * Get temporal filters.
     *
     * @return temporal filters
     */
    public List<TemporalFilter> getTemporalFilters() {
        return temporalFilters;
    }

    /**
     * Set temporal filters.
     *
     * @param temporalFilters
     *            temporal filters
     */
    public void setTemporalFilters(List<TemporalFilter> temporalFilters) {
        this.temporalFilters = temporalFilters;
    }

    /**
     * Get FOI identifiers.
     *
     * @return FOI identifiers
     */
    @Override
    public List<String> getFeatureIdentifiers() {
        return featureIdentifiers;
    }

    /**
     * Set FOI identifiers.
     *
     * @param featureIdentifiers
     *            FOI identifiers
     */
    @Override
    public void setFeatureIdentifiers(List<String> featureIdentifiers) {
        this.featureIdentifiers = featureIdentifiers;
    }

    /**
     * Get observableProperties.
     *
     * @return observableProperties
     */
    public List<String> getObservedProperties() {
        return observedProperties;
    }

    /**
     * Set observedProperties.
     *
     * @param observedProperties
     *            observedProperties
     */
    public void setObservedProperties(List<String> observedProperties) {
        this.observedProperties = observedProperties;
    }

    /**
     * Get offerings.
     *
     * @return offerings
     */
    public List<String> getOfferings() {
        return offerings;
    }

    /**
     * Set offerings.
     *
     * @param offerings
     *            offerings
     */
    public void setOfferings(List<String> offerings) {
        this.offerings = offerings;
    }

    /**
     * Get procedures.
     *
     * @return procedures
     */
    public List<String> getProcedures() {
        return procedures;
    }

    /**
     * Set procedures.
     *
     * @param procedures
     *            procedures
     */
    public void setProcedures(List<String> procedures) {
        this.procedures = procedures;
    }

    /**
     * Get result filter(s).
     *
     * @return result filter(s)
     */
    @SuppressWarnings("rawtypes")
    public Filter getResultFilter() {
        return resultFilter;
    }

    /**
     * Add result filter(s).
     *
     * @param resultFilter
     *            result filter(s)
     */
    @SuppressWarnings("rawtypes")
    public void setResultFilter(Filter resultFilter) {
        this.resultFilter = resultFilter;
    }

    /**
     * Check if a result filter is set.
     *
     * @return <code>true</code>, if a result filter is set
     */
    public boolean isSetResultFilter() {
        return getResultFilter() != null;
    }

    /**
     *
     * Get request as String.
     *
     * @return request as String
     */
    public String getRequestString() {
        return requestString;
    }

    /**
     * Set request as String.
     *
     * @param requestString
     *            request as String
     */
    public void setRequestString(String requestString) {
        this.requestString = requestString;
    }

    /**
     * Get spatial filter.
     *
     * @return spatial filter
     */
    @Override
    public SpatialFilter getSpatialFilter() {
        return spatialFilter;
    }

    /**
     * Set spatial filter.
     *
     * @param resultSpatialFilter
     *            spatial filter
     */
    @Override
    public void setSpatialFilter(SpatialFilter resultSpatialFilter) {
        this.spatialFilter = resultSpatialFilter;
    }

    /**
     * Create a copy of this request with defined observableProperties.
     *
     * @param obsProps
     *            defined observableProperties
     *
     * @return SOS GetObservation request copy
     */
    public GetObservationRequest copyOf(List<String> obsProps) {
        GetObservationRequest res = new GetObservationRequest();
        super.copyOf(res);
        res.setTemporalFilters(this.temporalFilters);
        res.setObservedProperties(obsProps);
        res.setOfferings(this.offerings);
        res.setProcedures(this.procedures);
        res.setResponseFormat(getResponseFormat());
        res.setResponseMode(getResponseMode());
        res.setSpatialFilter(this.spatialFilter);
        res.setResultModel(getResultModel());
        res.setFeatureIdentifiers(this.featureIdentifiers);
        res.setService(this.getService());
        res.setRequestString(this.requestString);
        return res;

    }

    public void setNamespaces(Map<String, String> namespaces) {
        this.namespaces = namespaces;
    }

    public Map<String, String> getNamespaces() {
        return namespaces;
    }

    public boolean isSetOffering() {
        return offerings != null && !offerings.isEmpty();
    }

    public boolean isSetObservableProperty() {
        return observedProperties != null && !observedProperties.isEmpty();
    }

    public boolean isSetProcedure() {
        return procedures != null && !procedures.isEmpty();
    }

    public boolean isSetTemporalFilter() {
        return temporalFilters != null && !temporalFilters.isEmpty();
    }

    public boolean hasFirstLatestTemporalFilter() {
        return temporalFilters.stream().map(TemporalFilter::getTime).filter(Predicates.instanceOf(TimeInstant.class))
                .map(Functions.cast(TimeInstant.class)).map(TimeInstant::getIndeterminateValue)
                .anyMatch(this::isFirstLatest);
    }

    public List<IndeterminateValue> getFirstLatestTemporalFilter() {
        return temporalFilters.stream().map(TemporalFilter::getTime).filter(Predicates.instanceOf(TimeInstant.class))
                .map(Functions.cast(TimeInstant.class)).map(TimeInstant::getIndeterminateValue)
                .filter(Objects::nonNull).filter(this::isFirstLatest).collect(toList());
    }

    public List<TemporalFilter> getNotFirstLatestTemporalFilter() {
        return temporalFilters.stream().map(temporalFilter -> {
            if (temporalFilter.getTime() instanceof TimeInstant) {
                if (!isFirstLatest(((TimeInstant) temporalFilter.getTime()).getIndeterminateValue())) {
                    return temporalFilter;
                } else {
                    return null;
                }
            } else {
                return temporalFilter;
            }
        }).collect(toList());
    }

    public boolean hasTemporalFilters() {
        return temporalFilters != null && !temporalFilters.isEmpty();
    }

    public boolean isEmpty() {
        return !isSetOffering() && !isSetObservableProperty() && !isSetProcedure() && !isSetFeatureOfInterest()
                && !isSetTemporalFilter() && !isSetSpatialFilter();
    }

    public boolean isSetRequestString() {
        return !Strings.isNullOrEmpty(getRequestString());
    }

    public boolean isSetNamespaces() {
        return CollectionHelper.isNotEmpty(getNamespaces());
    }

    public void setMergeObservationValues(boolean mergeObservationValues) {
        this.mergeObservationValues = mergeObservationValues;
    }

    public boolean isSetMergeObservationValues() {
        return mergeObservationValues;
    }

    /**
     * Check if the {@link SwesExtensions} contains {@link Filter}
     *
     * @return <code>true</code>, if the {@link SwesExtensions} contains
     *         {@link Filter}
     */
    public boolean isSetFesFilterExtension() {
        return getExtensions().stream().anyMatch(this::isFesFilterExtension);
    }

    /**
     * Get all {@link SwesExtensions} with {@link Filter}
     *
     * @return All {@link SwesExtensions} with {@link Filter}
     */
    public Set<Extension<?>> getFesFilterExtensions() {
        return Optional.ofNullable(getExtensions()).map(Extensions::stream).orElseGet(Stream::empty)
                .filter(this::isFesFilterExtension).collect(toSet());
    }

    private boolean isFesFilterExtension(Extension<?> extension) {
        return extension.getValue() instanceof Filter<?>;
    }

    private boolean isFirstLatest(IndeterminateValue v) {
        return v != null && (v.equals(ExtendedIndeterminateTime.FIRST) || v.equals(ExtendedIndeterminateTime.LATEST));
    }
}
