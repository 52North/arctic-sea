/*
 * Copyright 2015-2020 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.ogc.om.series;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.gml.CodeWithAuthority;
import org.n52.shetland.ogc.gml.ReferenceType;
import org.n52.shetland.ogc.om.NamedValue;

/**
 *
 */
public abstract class AbstractObservationProcess extends AbstractFeature {

    /*
     * Multiplicity: 1 A defintion of the type of process used in the
     * observation. This may be a Sensor, ManualMethod, Algorithm or Simulation
     * (including models).
     */
    private ReferenceType processType;

    /*
     * Multiplicity: 0..1 A reference to the original source of the data. For
     * example, if this is a post-processed time series (and processType is
     * algorithm), this link would specify the original process that generated
     * the data, e.g. the sensor. This allows the origin of the data to be
     * maintained regardless of the processing that has occured to it.
     */
    private ReferenceType originatingProcess;

    /*
     * Multiplicity: 0..1 If the process involves temporal aggregation of a
     * result set, the time duration over which data has been aggregated should
     * be expressed here. E.g. hourly, daily aggregates.
     */
    private String aggregationDuration;

    /*
     * Multiplicity: 0..1 Reference to an external process definition
     */
    private ReferenceType processReference;

    /*
     * Multiplicity: 0..1 Specifies the datum that is used as the zero point for
     * level measurements. This can be process-specific as opposed the gauge at
     * the actual monitoring point.
     */
    private ReferenceType verticalDatum;

    /*
     * Multiplicity: 0..* A list of the inputs used in the process. This may be
     * a list of references to the data sets used (e.g. model input series) or a
     * input array to an algorithm.
     */
    private final List<ReferenceType> inputs = new ArrayList<>(0);

    /*
     * Multiplicity: 0..* Comments specific to the process from the operator or
     * system performing the process.
     */
    private List<String> comments = new ArrayList<>(0);

    /*
     * Multiplicity: 0..* A defintion of the type of process used in the
     * observation. This may be a Sensor, ManualMethod, Algorithm or Simulation
     * (including models).
     */
    private final List<NamedValue<?>> parameters = new ArrayList<>(0);

    public AbstractObservationProcess() {
        super("");
    }

    public AbstractObservationProcess(String identifier) {
        super(identifier);
    }

    public AbstractObservationProcess(CodeWithAuthority featureIdentifier) {
        super(featureIdentifier);
    }

    public AbstractObservationProcess(CodeWithAuthority featureIdentifier, String gmlId) {
        super(featureIdentifier, gmlId);
    }

    @Override
    public AbstractObservationProcess setIdentifier(final String procedureIdentifier) {
        super.setIdentifier(procedureIdentifier);
        return this;
    }

    public ReferenceType getProcessType() {
        return processType;
    }

    public AbstractObservationProcess setProcessType(final ReferenceType processType) {
        this.processType = processType;
        return this;
    }

    public ReferenceType getOriginatingProcess() {
        return originatingProcess;
    }

    public AbstractObservationProcess setOriginatingProcess(final ReferenceType originatingProcess) {
        this.originatingProcess = originatingProcess;
        return this;
    }

    public String getAggregationDuration() {
        return aggregationDuration;
    }

    public AbstractObservationProcess setAggregationDuration(final String aggregationDuration) {
        this.aggregationDuration = aggregationDuration;
        return this;
    }

    public ReferenceType getProcessReference() {
        return processReference;
    }

    public AbstractObservationProcess setProcessReference(final ReferenceType processReference) {
        this.processReference = processReference;
        return this;
    }

    public ReferenceType getVerticalDatum() {
        return verticalDatum;
    }

    public AbstractObservationProcess setVerticalDatum(final ReferenceType verticalDatum) {
        this.verticalDatum = verticalDatum;
        return this;
    }

    public List<ReferenceType> getInputs() {
        return inputs;
    }

    public AbstractObservationProcess setInputs(final List<ReferenceType> input) {
        inputs.addAll(input);
        return this;
    }

    public AbstractObservationProcess addInputs(final ReferenceType input) {
        inputs.add(input);
        return this;
    }

    public List<String> getComments() {
        return comments;
    }

    public AbstractObservationProcess setComments(final List<String> comments) {
        this.comments = comments;
        return this;
    }

    public AbstractObservationProcess addComment(final String comment) {
        comments.add(comment);
        return this;
    }

    public List<NamedValue<?>> getParameters() {
        return parameters;
    }

    public AbstractObservationProcess setParameters(final Collection<NamedValue<?>> parameters) {
        this.parameters.addAll(parameters);
        return this;
    }

    public AbstractObservationProcess addParameter(final NamedValue<?> parameter) {
        parameters.add(parameter);
        return this;
    }

    public boolean isSetProcessType() {
        return processType != null;
    }

    public boolean isSetOriginatingProcess() {
        return originatingProcess != null && originatingProcess.hasValues();
    }

    public boolean isSetProcessReference() {
        return processReference != null && processReference.hasValues();
    }

    public boolean isSetAggregationDuration() {
        return aggregationDuration != null && !aggregationDuration.isEmpty();
    }

    public boolean isSetVerticalDatum() {
        return verticalDatum != null && verticalDatum.hasValues();
    }

    public boolean isSetInputs() {
        return !inputs.isEmpty();
    }

    public boolean isSetComments() {
        return comments != null && !comments.isEmpty();
    }

    public boolean isSetParameters() {
        return !parameters.isEmpty();
    }

}
