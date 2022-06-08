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
package org.n52.shetland.ogc.sos;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * SOS internal representation of SOS insertion metadata
 *
 * @since 1.0.0
 */
public class SosInsertionMetadata {

    /**
     * list of valid feature types
     */
    private Set<String> featureOfInterestTypes = new LinkedHashSet<>();

    /**
     * list of valid observation types
     */
    private Set<String> observationTypes = new LinkedHashSet<>();

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
        return Collections.unmodifiableSet(featureOfInterestTypes);
    }

    /**
     * @param featureOfInterestTypes
     *            the featureOfInterestTypes to set
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public SosInsertionMetadata setFeatureOfInterestTypes(Collection<String> featureOfInterestTypes) {
        this.featureOfInterestTypes.clear();
        if (featureOfInterestTypes != null) {
            this.featureOfInterestTypes.addAll(featureOfInterestTypes);
        }
        return this;
    }

    /**
     * @return the observationTypes
     */
    public Set<String> getObservationTypes() {
        return Collections.unmodifiableSet(observationTypes);
    }

    /**
     * @param observationTypes
     *            the observationTypes to set
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public SosInsertionMetadata setObservationTypes(Collection<String> observationTypes) {
        this.observationTypes.clear();
        if (observationTypes != null) {
            this.observationTypes.addAll(observationTypes);
        }
        return this;
    }
}
