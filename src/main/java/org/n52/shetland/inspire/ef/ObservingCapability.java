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
package org.n52.shetland.inspire.ef;

import java.net.URI;

import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.gml.ReferenceType;
import org.n52.shetland.ogc.gml.time.Time;
import org.n52.shetland.w3c.xlink.SimpleAttrs;

public class ObservingCapability
        extends SimpleAttrs {

    /**
     * 1..1
     */
    private Time observingTime;

    /**
     * 1..1
     */
    private ReferenceType processType;

    /**
     * 1..1
     */
    private ReferenceType resultNature;

    /**
     * 0..1
     */
    private URI onlineResource;

    /**
     * 1..1
     */
    private AbstractFeature procedure;

    /**
     * 0..1
     */
    private AbstractFeature featureOfInterest;

    /**
     * 1..1
     */
    private ReferenceType observedProperty;

    public ObservingCapability(String href) {
        setHref(href);
    }

    public ObservingCapability(
            Time observingTime, ReferenceType processType, ReferenceType resultNature, AbstractFeature procedure,
            ReferenceType observedProperty) {
        this.observingTime = observingTime;
        this.processType = processType;
        this.resultNature = resultNature;
        this.procedure = procedure;
        this.observedProperty = observedProperty;
    }

    /**
     * @return the observingTime
     */
    public Time getObservingTime() {
        return observingTime;
    }

    /**
     * @param observingTime
     *            the observingTime to set
     */
    public void setObservingTime(Time observingTime) {
        this.observingTime = observingTime;
    }

    /**
     * @return the processType
     */
    public ReferenceType getProcessType() {
        return processType;
    }

    /**
     * @param processType
     *            the processType to set
     */
    public void setProcessType(ReferenceType processType) {
        this.processType = processType;
    }

    /**
     * @return the resultNature
     */
    public ReferenceType getResultNature() {
        return resultNature;
    }

    /**
     * @param resultNature
     *            the resultNature to set
     */
    public void setResultNature(ReferenceType resultNature) {
        this.resultNature = resultNature;
    }

    /**
     * @return the onlineResource
     */
    public URI getOnlineResource() {
        return onlineResource;
    }

    /**
     * @param onlineResource
     *            the onlineResource to set
     */
    public void setOnlineResource(URI onlineResource) {
        this.onlineResource = onlineResource;
    }

    /**
     * @return the procedure
     */
    public AbstractFeature getProcedure() {
        return procedure;
    }

    /**
     * @param procedure
     *            the procedure to set
     */
    public void setProcedure(AbstractFeature procedure) {
        this.procedure = procedure;
    }

    /**
     * @return the featureOfInterest
     */
    public AbstractFeature getFeatureOfInterest() {
        return featureOfInterest;
    }

    /**
     * @param featureOfInterest
     *            the featureOfInterest to set
     */
    public void setFeatureOfInterest(AbstractFeature featureOfInterest) {
        this.featureOfInterest = featureOfInterest;
    }

    /**
     * @return the observedProperty
     */
    public ReferenceType getObservedProperty() {
        return observedProperty;
    }

    /**
     * @param observedProperty
     *            the observedProperty to set
     */
    public void setObservedProperty(ReferenceType observedProperty) {
        this.observedProperty = observedProperty;
    }
}
