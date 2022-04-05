/*
 * Copyright 2015-${currentYear} 52°North Spatial Information Research GmbH
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

import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.om.AbstractPhenomenon;
import org.n52.shetland.ogc.ows.service.OwsServiceRequest;
import org.n52.shetland.ogc.sos.Sos1Constants;

/**
 * SOS RegisterSensor request
 *
 * @since 1.0.0
 */
public class SosRegisterSensorRequest
        extends OwsServiceRequest {

    /**
     * SOS Sensor system
     */
    // private SensorSystem system;

    /**
     * observableProperties collection
     */
    private Collection<AbstractPhenomenon> observableProperties;

    /**
     * featureOfInterest collection
     */
    private Collection<AbstractFeature> featuresOfInterest;

    /**
     * Sensor description
     */
    private String sensorDescription;

    /**
     * constructor
     *
     * @param observableProperties
     *            Observable properties
     * @param sensorDescription
     *            Sensor description as String
     * @param featuresOfInterest
     *            FeatureOfInterest
     */
    public SosRegisterSensorRequest(
            Collection<AbstractPhenomenon> observableProperties, String sensorDescription,
            Collection<AbstractFeature> featuresOfInterest) {
        super(null, null, Sos1Constants.Operations.RegisterSensor.name());
        // this.system = system;
        this.observableProperties = observableProperties;
        this.sensorDescription = sensorDescription;
        this.featuresOfInterest = featuresOfInterest;
    }

    /**
     * Get observableProperties
     *
     * @return observableProperties
     */
    public Collection<AbstractPhenomenon> getObservableProperties() {
        return observableProperties;
    }

    /**
     * Set observableProperties
     *
     * @param observableProperties
     *            observableProperties
     */
    public void setObservableProperties(Collection<AbstractPhenomenon> observableProperties) {
        this.observableProperties = observableProperties;
    }

    /**
     * Get sensor description
     *
     * @return sensor description
     */
    public String getSensorDescription() {
        return sensorDescription;
    }

    /**
     * Set sensor description
     *
     * @param sensorDescription
     *            sensor description
     */
    public void setSensorDescription(String sensorDescription) {
        this.sensorDescription = sensorDescription;
    }

    // /**
    // * Get SOS sensor system
    // *
    // * @return SOS sensor system
    // */
    // public SensorSystem getSystem() {
    // return system;
    // }
    //
    // /**
    // * Set SOS sensor system
    // *
    // * @param system
    // * SOS sensor system
    // */
    // public void setSystem(SensorSystem system) {
    // this.system = system;
    // }

    /**
     * Get featuresOfInterst
     *
     * @return featuresOfInterst
     */
    public Collection<AbstractFeature> getFeaturesOfInterest() {
        return featuresOfInterest;
    }

    /**
     * Set featuresOfInterst
     *
     * @param featuresOfInterest
     *            featuresOfInterst
     */
    public void setFeaturesOfInterest(Collection<AbstractFeature> featuresOfInterest) {
        this.featuresOfInterest = featuresOfInterest;
    }

}
