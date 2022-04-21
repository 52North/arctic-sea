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

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.n52.janmayen.function.Functions;
import org.n52.janmayen.function.Predicates;
import org.n52.shetland.ogc.filter.ComparisonFilter;
import org.n52.shetland.ogc.filter.Filter;
import org.n52.shetland.ogc.filter.SpatialFilter;
import org.n52.shetland.ogc.filter.TemporalFilter;
import org.n52.shetland.ogc.gml.time.IndeterminateValue;
import org.n52.shetland.ogc.gml.time.TimeInstant;
import org.n52.shetland.ogc.ows.extension.Extension;
import org.n52.shetland.ogc.ows.extension.Extensions;
import org.n52.shetland.ogc.sos.ExtendedIndeterminateTime;
import org.n52.shetland.ogc.sos.ResultFilter;
import org.n52.shetland.ogc.sos.ResultFilterConstants;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.sos.SosSpatialFilter;
import org.n52.shetland.ogc.sos.SosSpatialFilterConstants;
import org.n52.shetland.util.CollectionHelper;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * SOS GetObservation request.
 *
 * @since 1.0.0
 */
public class GetObservationRequest extends AbstractObservationRequest implements SpatialFeatureQueryRequest {

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
        return Collections.unmodifiableList(temporalFilters);
    }

    /**
     * Set temporal filters.
     *
     * @param temporalFilters
     *            temporal filters
     */
    public GetObservationRequest setTemporalFilters(List<TemporalFilter> temporalFilters) {
        this.temporalFilters.clear();
        if (temporalFilters != null) {
            this.temporalFilters.addAll(temporalFilters);
        }
        return this;
    }

    public GetObservationRequest addTemporalFilter(TemporalFilter filter) {
        this.temporalFilters.add(filter);
        return this;
    }

    /**
     * Get FOI identifiers.
     *
     * @return FOI identifiers
     */
    @Override
    public List<String> getFeatureIdentifiers() {
        return Collections.unmodifiableList(featureIdentifiers);
    }

    /**
     * Set FOI identifiers.
     *
     * @param featureIdentifiers
     *            FOI identifiers
     */
    @Override
    public void setFeatureIdentifiers(List<String> featureIdentifiers) {
        this.featureIdentifiers.clear();
        if (featureIdentifiers != null) {
            this.featureIdentifiers.addAll(featureIdentifiers);
        }
    }

    public GetObservationRequest addFeatureIdentifier(String featureIdentifier) {
        this.featureIdentifiers.add(featureIdentifier);
        return this;
    }

    /**
     * Get observableProperties.
     *
     * @return observableProperties
     */
    public List<String> getObservedProperties() {
        return Collections.unmodifiableList(observedProperties);
    }

    /**
     * Set observedProperties.
     *
     * @param observedProperties
     *            observedProperties
     */
    public GetObservationRequest setObservedProperties(List<String> observedProperties) {
        this.observedProperties.clear();
        if (observedProperties != null) {
            this.observedProperties.addAll(observedProperties);
        }
        return this;
    }

    public GetObservationRequest addObservedProperty(String observedProperty) {
        this.observedProperties.add(observedProperty);
        return this;
    }

    /**
     * Get offerings.
     *
     * @return offerings
     */
    public List<String> getOfferings() {
        return Collections.unmodifiableList(offerings);
    }

    /**
     * Set offerings.
     *
     * @param offerings
     *            offerings
     */
    public GetObservationRequest setOfferings(List<String> offerings) {
        this.offerings.clear();
        if (offerings != null) {
            this.offerings.addAll(offerings);
        }
        return this;
    }

    public GetObservationRequest addOffering(String offering) {
        this.offerings.add(offering);
        return this;
    }

    /**
     * Get procedures.
     *
     * @return procedures
     */
    public List<String> getProcedures() {
        return Collections.unmodifiableList(procedures);
    }

    /**
     * Set procedures.
     *
     * @param procedures
     *            procedures
     */
    public GetObservationRequest setProcedures(List<String> procedures) {
        this.procedures.clear();
        if (procedures != null) {
            this.procedures.addAll(procedures);
        }
        return this;
    }

    public GetObservationRequest addProcedure(String procedure) {
        this.procedures.add(procedure);
        return this;
    }

    /**
     * Get result filter(s).
     *
     * @return result filter(s)
     */
    @SuppressWarnings("rawtypes")
    public Filter getResultFilter() {
        if (getExtension(ResultFilterConstants.RESULT_FILTER).isPresent()) {
            return ((ResultFilter) getExtension(ResultFilterConstants.RESULT_FILTER).get()).getValue();
        }
        return resultFilter;
    }

    public GetObservationRequest setResultFilter(ComparisonFilter filter) {
        this.resultFilter = filter;
        addExtension(new ResultFilter(filter));
        return this;
    }

    /**
     * Add result filter(s).
     *
     * @param resultFilter
     *            result filter(s)
     */
    @SuppressWarnings("rawtypes")
    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public GetObservationRequest setResultFilter(Filter resultFilter) {
        this.resultFilter = resultFilter;
        if (resultFilter instanceof ComparisonFilter) {
            addExtension(new ResultFilter((ComparisonFilter) resultFilter));
        }
        return this;
    }

    /**
     * Check if a result filter is set.
     *
     * @return <code>true</code>, if a result filter is set
     */
    public boolean isSetResultFilter() {
        return hasResultFilter();
    }

    public boolean hasResultFilter() {
        return resultFilter != null || hasExtension(ResultFilterConstants.RESULT_FILTER);
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
    public GetObservationRequest setRequestString(String requestString) {
        this.requestString = requestString;
        return this;
    }

    /**
     * Get spatial filter.
     *
     * @return spatial filter
     */
    @Override
    @SuppressFBWarnings({ "EI_EXPOSE_REP" })
    public SpatialFilter getSpatialFilter() {
        if (hasExtension(SosSpatialFilterConstants.SPATIAL_FILTER)) {
            return ((SosSpatialFilter) getExtension(SosSpatialFilterConstants.SPATIAL_FILTER).get()).getValue();
        }
        return spatialFilter;
    }

    /**
     * Set spatial filter.
     *
     * @param resultSpatialFilter
     *            spatial filter
     */
    @Override
    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
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

    public GetObservationRequest setNamespaces(Map<String, String> namespaces) {
        this.namespaces.clear();
        if (namespaces != null) {
            this.namespaces.putAll(namespaces);
        }
        return this;
    }

    public Map<String, String> getNamespaces() {
        return Collections.unmodifiableMap(namespaces);
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
        }).filter(Objects::nonNull).collect(toList());
    }

    public boolean hasTemporalFilters() {
        return temporalFilters != null && !temporalFilters.isEmpty();
    }

    public boolean isEmpty() {
        return !isSetOffering() && !isSetObservableProperty() && !isSetProcedure() && !isSetFeatureOfInterest()
                && !isSetTemporalFilter() && !isSetSpatialFilter();
    }

    @Override
    public boolean hasSpatialFilteringProfileSpatialFilter() {
        return isSetSpatialFilter() && (getSpatialFilter().getValueReference()
                .equals(Sos2Constants.VALUE_REFERENCE_SPATIAL_FILTERING_PROFILE)
                || hasExtension(SosSpatialFilterConstants.SPATIAL_FILTER)
                        && ((SosSpatialFilter) getExtension(SosSpatialFilterConstants.SPATIAL_FILTER).get()).getValue()
                                .getValueReference().equals(Sos2Constants.VALUE_REFERENCE_SPATIAL_FILTERING_PROFILE));
    }

    public boolean isSetRequestString() {
        return !Strings.isNullOrEmpty(getRequestString());
    }

    public boolean isSetNamespaces() {
        return CollectionHelper.isNotEmpty(getNamespaces());
    }

    public GetObservationRequest setMergeObservationValues(boolean mergeObservationValues) {
        this.mergeObservationValues = mergeObservationValues;
        return this;
    }

    public boolean isSetMergeObservationValues() {
        return mergeObservationValues;
    }

    /**
     * Check if the {@link Extensions} contains {@link Filter}
     *
     * @return <code>true</code>, if the {@link Extensions} contains {@link Filter}
     */
    public boolean isSetFesFilterExtension() {
        return getExtensions().stream().anyMatch(this::isFesFilterExtension);
    }

    /**
     * Get all {@link Extensions} with {@link Filter}
     *
     * @return All {@link Extensions} with {@link Filter}
     */
    public Set<Extension<?>> getFesFilterExtensions() {
        return Optional.ofNullable(getExtensions()).map(Extensions::stream).orElseGet(Stream::empty)
                .filter(this::isFesFilterExtension).collect(toSet());
    }

    private boolean isFesFilterExtension(Extension<?> extension) {
        return !(extension instanceof ResultFilter || extension instanceof SpatialFilter
                || extension instanceof SosSpatialFilter) && extension.getValue() instanceof Filter<?>;
    }

    private boolean isFirstLatest(IndeterminateValue v) {
        return v != null && (v.equals(ExtendedIndeterminateTime.FIRST) || v.equals(ExtendedIndeterminateTime.LATEST));
    }
}
