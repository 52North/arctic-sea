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
import java.util.LinkedHashSet;

import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.om.AbstractPhenomenon;
import org.n52.shetland.ogc.ows.service.OwsServiceRequest;
import org.n52.shetland.ogc.sos.Sos1Constants;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * SOS RegisterSensor request
 *
 * @since 1.0.0
 */
public class SosRegisterSensorRequest extends OwsServiceRequest {

    /**
     * SOS Sensor system
     */
    // private SensorSystem system;

    /**
     * observableProperties collection
     */
    private Collection<AbstractPhenomenon> observableProperties = new LinkedHashSet<>();

    /**
     * featureOfInterest collection
     */
    private Collection<AbstractFeature> featuresOfInterest = new LinkedHashSet<>();

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
    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public SosRegisterSensorRequest(Collection<AbstractPhenomenon> observableProperties, String sensorDescription,
            Collection<AbstractFeature> featuresOfInterest) {
        super(null, null, Sos1Constants.Operations.RegisterSensor.name());
        // this.system = system;
        this.observableProperties.addAll(observableProperties);
        this.sensorDescription = sensorDescription;
        this.featuresOfInterest.addAll(featuresOfInterest);
    }

    /**
     * Get observableProperties
     *
     * @return observableProperties
     */
    public Collection<AbstractPhenomenon> getObservableProperties() {
        return Collections.unmodifiableCollection(observableProperties);
    }

    /**
     * Set observableProperties
     *
     * @param observableProperties
     *            observableProperties
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public void setObservableProperties(Collection<AbstractPhenomenon> observableProperties) {
        this.observableProperties.clear();
        if (observableProperties != null) {
            this.observableProperties.addAll(observableProperties);
        }
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

    /**
     * Get featuresOfInterst
     *
     * @return featuresOfInterst
     */
    public Collection<AbstractFeature> getFeaturesOfInterest() {
        return Collections.unmodifiableCollection(featuresOfInterest);
    }

    /**
     * Set featuresOfInterst
     *
     * @param featuresOfInterest
     *            featuresOfInterst
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public void setFeaturesOfInterest(Collection<AbstractFeature> featuresOfInterest) {
        this.featuresOfInterest.clear();
        if (featuresOfInterest != null) {
            this.featuresOfInterest.addAll(featuresOfInterest);
        }
    }

}
