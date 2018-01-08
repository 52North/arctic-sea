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
package org.n52.shetland.ogc.sos;

import java.util.Collection;
import java.util.Set;

import com.google.common.collect.Sets;

/**
 * SOS internal representation of SOS insertion metadata
 *
 * @since 1.0.0
 */
public class SosInsertionMetadata {

    /**
     * list of valid feature types
     */
    private Set<String> featureOfInterestTypes;

    /**
     * list of valid observation types
     */
    private Set<String> observationTypes;

    /**
     * constructor
     */
    public SosInsertionMetadata() {
        super();
    }

    /**
     * @return the featureOfInterestTypes
     */
    public Set<String> getFeatureOfInterestTypes() {
        return featureOfInterestTypes;
    }

    /**
     * @param featureOfInterestTypes
     *            the featureOfInterestTypes to set
     */
    public SosInsertionMetadata setFeatureOfInterestTypes(Collection<String> featureOfInterestTypes) {
        this.featureOfInterestTypes = Sets.newHashSet(featureOfInterestTypes);
        return this;
    }

    /**
     * @return the observationTypes
     */
    public Set<String> getObservationTypes() {
        return observationTypes;
    }

    /**
     * @param observationTypes
     *            the observationTypes to set
     */
    public SosInsertionMetadata setObservationTypes(Collection<String> observationTypes) {
        this.observationTypes = Sets.newHashSet(observationTypes);
        return this;
    }
}
