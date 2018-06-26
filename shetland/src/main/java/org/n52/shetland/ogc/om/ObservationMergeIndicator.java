/*
 * Copyright 2015-2018 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.ogc.om;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

/**
 * An indicator for which parameters of two observations have to be equal to be able to merge them.
 *
 * @author Christian Autermann
 * @author Carsten Hollmann
 */
public class ObservationMergeIndicator {
    private final Set<Param> parameters;

    /**
     * Creates a new {@code ObservationMergeIndicator} that requires no parameters to be equal.
     */
    public ObservationMergeIndicator() {
        this(EnumSet.noneOf(Param.class));
    }

    /**
     * Creates a new {@code ObservationMergeIndicator} that requires the supplied parameters to be equal.
     *
     * @param parameters the parameters
     */
    private ObservationMergeIndicator(Set<Param> parameters) {
        this.parameters = Objects.requireNonNull(parameters);
    }

    /**
     * Sets that the parameter idoes not need to be equal to merge two observations.
     *
     * @param param the parameter
     *
     * @return {@code this}
     */
    private ObservationMergeIndicator without(Param param) {
        this.parameters.remove(param);
        return this;
    }

    /**
     * Sets that the parameter should be equal to merge two observations.
     *
     * @param param the parameter
     *
     * @return {@code this}
     */
    private ObservationMergeIndicator with(Param param) {
        this.parameters.add(param);
        return this;
    }

    /**
     * Checks if all of the parameters should be equal to merge two observations.
     *
     * @param param the parameters
     *
     * @return if all should be equal
     */
    private boolean is(Param... param) {
        return Arrays.stream(param).allMatch(this.parameters::contains);
    }

    /**
     * Sets that the procedure should be equal to merge two observations.
     *
     * @return {@code this}
     */
    public ObservationMergeIndicator withProcedure() {
        return with(Param.PROCEDURE);
    }

    /**
     * Sets that the observable property should be equal to merge two observations.
     *
     * @return {@code this}
     */
    public ObservationMergeIndicator withObservableProperty() {
        return with(Param.OBSERVABLE_PROPERTY);
    }

    /**
     * Sets that the feature of interest should be equal to merge two observations.
     *
     * @return {@code this}
     */
    public ObservationMergeIndicator withFeatureOfInterest() {
        return with(Param.FEATURE_OF_INTEREST);
    }

    /**
     * Sets that the offering should be equal to merge two observations.
     *
     * @return {@code this}
     */
    public ObservationMergeIndicator withOffering() {
        return with(Param.OFFERING);
    }

    /**
     * Sets that the phenomenon time should be equal to merge two observations.
     *
     * @return {@code this}
     */
    public ObservationMergeIndicator withPhenomenonTime() {
        return with(Param.PHENOMENON_TIME);
    }

    /**
     * Sets that the result time should be equal to merge two observations.
     *
     * @return {@code this}
     */
    public ObservationMergeIndicator withResultTime() {
        return with(Param.RESULT_TIME);
    }

    /**
     * Sets that the sampling geometry should be equal to merge two observations.
     *
     * @return {@code this}
     */
    public ObservationMergeIndicator withSamplingGeometry() {
        return with(Param.SAMPLING_GEOMETRY);
    }

    /**
     * Sets that the observation type should be checked to merge two observations.
     *
     * @return {@code this}
     */
    public ObservationMergeIndicator withObservationType() {
        return with(Param.OBSERVATION_TYPE);
    }

    /**
     * Sets that the procedure does not need to be equal to merge two observations.
     *
     * @return {@code this}
     */
    public ObservationMergeIndicator withoutProcedure() {
        return without(Param.PROCEDURE);
    }

    /**
     * Sets that the observable property does not need to be equal to merge two observations.
     *
     * @return {@code this}
     */
    public ObservationMergeIndicator withoutObservableProperty() {
        return without(Param.OBSERVABLE_PROPERTY);
    }

    /**
     * Sets that the feature of interest does not need to be equal to merge two observations.
     *
     * @return {@code this}
     */
    public ObservationMergeIndicator withoutFeatureOfInterest() {
        return without(Param.FEATURE_OF_INTEREST);
    }

    /**
     * Sets that the offering does not need to be equal to merge two observations.
     *
     * @return {@code this}
     */
    public ObservationMergeIndicator withoutOffering() {
        return without(Param.OFFERING);
    }

    /**
     * Sets that the phenomenon time does not need to be equal to merge two observations.
     *
     * @return {@code this}
     */
    public ObservationMergeIndicator withoutPhenomenonTime() {
        return without(Param.PHENOMENON_TIME);
    }

    /**
     * Sets that the result time does not need to be equal to merge two observations.
     *
     * @return {@code this}
     */
    public ObservationMergeIndicator withoutResultTime() {
        return without(Param.RESULT_TIME);
    }

    /**
     * Sets that the sampling geometry does not need to be equal to merge two observations.
     *
     * @return {@code this}
     */
    public ObservationMergeIndicator withoutSamplingGeometry() {
        return without(Param.SAMPLING_GEOMETRY);
    }

    /**
     * Sets that the observation type does not need to be checked to merge two observations.
     *
     * @return {@code this}
     */
    public ObservationMergeIndicator withoutObservationType() {
        return without(Param.OBSERVATION_TYPE);
    }

    /**
     * Checks if the procedure should be equal to merge two observations.
     *
     * @return if the procedure should be equal
     */
    public boolean isProcedure() {
        return is(Param.PROCEDURE);
    }

    /**
     * Checks if the observable property should be equal to merge two observations.
     *
     * @return if the observable property should be equal
     */
    public boolean isObservableProperty() {
        return is(Param.OBSERVABLE_PROPERTY);
    }

    /**
     * Checks if the feature of interest should be equal to merge two observations.
     *
     * @return if the feature of interest should be equal
     */
    public boolean isFeatureOfInterest() {
        return is(Param.FEATURE_OF_INTEREST);
    }

    /**
     * Checks if the offering should be equal to merge two observations.
     *
     * @return if the offering should be equal
     */
    public boolean isOfferings() {
        return is(Param.OFFERING);
    }

    /**
     * Checks if the phenomenon time should be equal to merge two observations.
     *
     * @return if the phenomenon time should be equal
     */
    public boolean isPhenomenonTime() {
        return is(Param.PHENOMENON_TIME);
    }

    /**
     * Checks if the result time should be equal to merge two observations.
     *
     * @return if the result time should be equal
     */
    public boolean isSetResultTime() {
        return is(Param.RESULT_TIME);
    }

    /**
     * Checks if the sampling geometry should be equal to merge two observations.
     *
     * @return if the sampling geometry should be equal
     */
    public boolean isSamplingGeometry() {
        return is(Param.SAMPLING_GEOMETRY);
    }

    /**
     * Checks if the observation type should be checked to merge two observations.
     *
     * @return if the observation type should be checked
     */
    public boolean isObservationType() {
        return is(Param.OBSERVATION_TYPE);
    }

    /**
     * Checks if the observation constellation (procedure, observable property, feature of interest and offering) should
     * be equal to merge two observations.
     *
     * @return if the observation constellation should be equal
     */
    public boolean isSameObservationConstellation() {
        return is(Param.PROCEDURE, Param.OBSERVABLE_PROPERTY, Param.FEATURE_OF_INTEREST, Param.OFFERING);
    }

    /**
     * Sets whether the procedure should be to merge two observations.
     *
     * @param procedure if the procedure should be equal
     *
     * @return {@code this}
     */
    public ObservationMergeIndicator setProcedure(boolean procedure) {
        return procedure ? withProcedure() : withoutProcedure();
    }

    /**
     * Sets whether the observable property should be to merge two observations.
     *
     * @param observableProperty if the observable property should be equal
     *
     * @return {@code this}
     */
    public ObservationMergeIndicator setObservableProperty(boolean observableProperty) {
        return observableProperty ? withObservableProperty() : withoutObservableProperty();
    }

    /**
     * Sets whether the feature of interest should be to merge two observations.
     *
     * @param featureOfInterest if the feature of interest should be equal
     *
     * @return {@code this}
     */
    public ObservationMergeIndicator setFeatureOfInterest(boolean featureOfInterest) {
        return featureOfInterest ? withFeatureOfInterest() : withoutFeatureOfInterest();
    }

    /**
     * Sets whether the offering should be to merge two observations.
     *
     * @param offerings if the offerings should be equal
     *
     * @return {@code this}
     */
    public ObservationMergeIndicator setOfferings(boolean offerings) {
        return offerings ? withOffering() : withoutOffering();
    }

    /**
     * Sets whether the phenomenon time should be to merge two observations.
     *
     * @param phenomenonTime if the phenomenon time should be equal
     *
     * @return {@code this}
     */
    public ObservationMergeIndicator setPhenomenonTime(boolean phenomenonTime) {
        return phenomenonTime ? withPhenomenonTime() : withoutPhenomenonTime();
    }

    /**
     * Sets whether the result time should be to merge two observations.
     *
     * @param resultTime if the result time should be equal
     *
     * @return {@code this}
     */
    public ObservationMergeIndicator setResultTime(boolean resultTime) {
        return resultTime ? withResultTime() : withoutResultTime();
    }

    /**
     * Sets whether the sampling geometry should be to merge two observations.
     *
     * @param samplingGeometry if the sampling geometry should be equal
     *
     * @return {@code this}
     */
    public ObservationMergeIndicator setSamplingGeometry(boolean samplingGeometry) {
        return samplingGeometry ? withSamplingGeometry() : withoutSamplingGeometry();
    }

    /**
     * Sets whether the observation type should be checked to merge two observations.
     *
     * @param observationType if the observation type should be checked
     *
     * @return {@code this}
     */
    public ObservationMergeIndicator setObservationType(boolean observationType) {
        return observationType ? withObservationType() : withoutObservationType();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.parameters);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ObservationMergeIndicator other = (ObservationMergeIndicator) obj;
        return Objects.equals(this.parameters, other.parameters);
    }

    /**
     * Gets an indicator that requires the observation constellations (procedure, observable property, feature of
     * interest and offering) to be the same.
     *
     * @return the indicator
     */
    public static ObservationMergeIndicator sameObservationConstellation() {
        return new ObservationMergeIndicator(EnumSet.of(Param.PROCEDURE, Param.OBSERVABLE_PROPERTY,
                Param.FEATURE_OF_INTEREST, Param.OFFERING, Param.OBSERVATION_TYPE));
    }

    /**
     * Enum for possible parameters.
     */
    private enum Param {
        PROCEDURE,
        OBSERVABLE_PROPERTY,
        FEATURE_OF_INTEREST,
        OFFERING,
        PHENOMENON_TIME,
        RESULT_TIME,
        SAMPLING_GEOMETRY,
        OBSERVATION_TYPE
    }

}
