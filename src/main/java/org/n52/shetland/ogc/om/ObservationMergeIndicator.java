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
package org.n52.shetland.ogc.om;

public class ObservationMergeIndicator {

    private boolean procedure = false;

    private boolean observableProperty = false;

    private boolean featureOfInterest = false;

    private boolean offerings = false;

    private boolean phenomenonTime = false;

    private boolean resultTime = false;

    private boolean samplingGeometry = false;

    public ObservationMergeIndicator() {
    }

    public static ObservationMergeIndicator defaultObservationMergerIndicator() {
        return new ObservationMergeIndicator().setFeatureOfInterest(true).setObservableProperty(true)
                .setProcedure(true).setOfferings(true);
    }

    /**
     * @return the procedure
     */
    public boolean isProcedure() {
        return procedure;
    }

    /**
     * @param procedure
     *            the procedure to set
     */
    public ObservationMergeIndicator setProcedure(boolean procedure) {
        this.procedure = procedure;
        return this;
    }

    /**
     * @return the observableProperty
     */
    public boolean isObservableProperty() {
        return observableProperty;
    }

    /**
     * @param observableProperty
     *            the observableProperty to set
     */
    public ObservationMergeIndicator setObservableProperty(boolean observableProperty) {
        this.observableProperty = observableProperty;
        return this;
    }

    /**
     * @return the featureOfInterest
     */
    public boolean isFeatureOfInterest() {
        return featureOfInterest;
    }

    /**
     * @param featureOfInterest
     *            the featureOfInterest to set
     */
    public ObservationMergeIndicator setFeatureOfInterest(boolean featureOfInterest) {
        this.featureOfInterest = featureOfInterest;
        return this;
    }

    /**
     * @return the offerings
     */
    public boolean isOfferings() {
        return offerings;
    }

    /**
     * @param offerings
     *            the offerings to set
     */
    public ObservationMergeIndicator setOfferings(boolean offerings) {
        this.offerings = offerings;
        return this;
    }

    public boolean sameObservationConstellation() {
        return isProcedure() && isObservableProperty() && isFeatureOfInterest() && isOfferings();
    }

    /**
     * @return the phenomenonTime
     */
    public boolean isPhenomenonTime() {
        return phenomenonTime;
    }

    /**
     * @param phenomenonTime
     *            the phenomenonTime to set
     */
    public ObservationMergeIndicator setPhenomenonTime(boolean phenomenonTime) {
        this.phenomenonTime = phenomenonTime;
        return this;
    }

    /**
     * @return the resultTime
     */
    public boolean isSetResultTime() {
        return resultTime;
    }

    /**
     * @param resultTime
     *            the resultTime to set
     */
    public ObservationMergeIndicator setResultTime(boolean resultTime) {
        this.resultTime = resultTime;
        return this;
    }

    /**
     * @return the samplingGeometry
     */
    public boolean isSamplingGeometry() {
        return samplingGeometry;
    }

    /**
     * @param samplingGeometry
     *            the samplingGeometry to set
     */
    public ObservationMergeIndicator setSamplingGeometry(boolean samplingGeometry) {
        this.samplingGeometry = samplingGeometry;
        return this;
    }

}
