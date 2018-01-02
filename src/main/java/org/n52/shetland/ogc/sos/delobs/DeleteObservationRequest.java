/*
 * Copyright 2016-2018 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.ogc.sos.delobs;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.n52.shetland.ogc.filter.TemporalFilter;
import org.n52.shetland.ogc.ows.service.OwsServiceRequest;
import org.n52.shetland.ogc.ows.service.ResponseFormat;
import org.n52.shetland.util.CollectionHelper;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk
 *         J&uuml;rrens</a>
 *
 * @since 1.0.0
 */
public class DeleteObservationRequest extends OwsServiceRequest implements ResponseFormat {

    private final String operationName = DeleteObservationConstants.Operations.DeleteObservation.name();

    private Set<String> observationIdentifiers = new HashSet<>();

    private Set<String> procedures = new HashSet<>();

    private Set<String> observedProperties = new HashSet<>();

    private Set<String> features = new HashSet<>();

    private Set<String> offerings = new HashSet<>();

    private Set<TemporalFilter> temporalFilters = new HashSet<>();

    private String responseFormat;

    public DeleteObservationRequest() {
    }

    public DeleteObservationRequest(String responseFormat) {
        this.responseFormat = responseFormat;
    }

    @Override
    public String getOperationName() {
        return operationName;
    }

    public Set<String> getObservationIdentifiers() {
        return observationIdentifiers;
    }

    public void setObservationIdentifiers(Collection<String> observationIdentifier) {
        this.observationIdentifiers.clear();
        if (observationIdentifier != null) {
            this.observationIdentifiers.addAll(observationIdentifier);
        }
    }

    public void addObservationIdentifier(String observationIdentifier) {
        this.observationIdentifiers.add(observationIdentifier);
    }

    public boolean isSetObservationIdentifiers() {
        return CollectionHelper.isNotEmpty(getObservationIdentifiers());
    }

    /**
     * @return the procedures
     */
    public Set<String> getProcedures() {
        return procedures;
    }

    /**
     * @param procedures
     *            the procedures to set
     */
    public void setProcedures(Collection<String> procedures) {
        this.procedures.clear();
        if (procedures != null) {
            this.procedures.addAll(procedures);
        }
    }

    public void addProcedure(String procedure) {
        this.procedures.add(procedure);
    }

    public boolean isSetprocedures() {
        return CollectionHelper.isNotEmpty(getProcedures());
    }

    /**
     * @return the observedProperties
     */
    public Set<String> getObservedProperties() {
        return observedProperties;
    }

    /**
     * @param observedProperties
     *            the observedProperties to set
     */
    public void setObservedProperties(Collection<String> observedProperties) {
        this.observedProperties.clear();
        if (observedProperties != null) {
            this.observedProperties.addAll(observedProperties);
        }
    }

    public void addObservedProperty(String observedProperty) {
        this.observedProperties.add(observedProperty);
    }

    public boolean isSetObservedProperty() {
        return CollectionHelper.isNotEmpty(getObservedProperties());
    }

    /**
     * @return the featureOfInterest
     */
    public Set<String> getFeatureIdentifiers() {
        return features;
    }

    /**
     * @param featureOfInterest
     *            the featureOfInterest to set
     */
    public void setFeatureIdentifiers(Collection<String> featureOfInterest) {
        this.features.clear();
        if (featureOfInterest != null) {
            this.features.addAll(featureOfInterest);
        }
    }

    public void addFeatureIdentifier(String featureOfInterest) {
        this.features.add(featureOfInterest);
    }

    public boolean isSetFeatureIdentifiers() {
        return CollectionHelper.isNotEmpty(getFeatureIdentifiers());
    }

    /**
     * @return the offerings
     */
    public Set<String> getOfferings() {
        return offerings;
    }

    /**
     * @param offerings
     *            the offerings to set
     */
    public void setOfferings(Collection<String> offerings) {
        this.offerings.clear();
        if (offerings != null) {
            this.offerings.addAll(offerings);
        }
    }

    public void addOffering(String offering) {
        this.offerings.add(offering);
    }

    public boolean isSetOfferings() {
        return CollectionHelper.isNotEmpty(getOfferings());
    }

    /**
     * @return the temporalFilters
     */
    public Set<TemporalFilter> getTemporalFilters() {
        return temporalFilters;
    }

    /**
     * @param temporalFilters
     *            the temporalFilters to set
     */
    public void setTemporalFilters(Collection<TemporalFilter> temporalFilters) {
        this.temporalFilters.clear();
        if (temporalFilters != null) {
            this.temporalFilters.addAll(temporalFilters);
        }
    }

    public void addTemporalFilter(TemporalFilter temporalFilter) {
        this.temporalFilters.add(temporalFilter);
    }

    public boolean isSetTemporalFilters() {
        return CollectionHelper.isNotEmpty(getTemporalFilters());
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
    public String toString() {
        return String.format(
                "DeleteObservationRequest [service=%s, version=%s, observationIdentifier=%s, operationName=%s]",
                getService(), getVersion(), getObservationIdentifiers(), getOperationName());
    }
}
