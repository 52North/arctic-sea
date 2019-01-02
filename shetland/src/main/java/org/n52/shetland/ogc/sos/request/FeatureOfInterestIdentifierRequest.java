/*
 * Copyright 2015-2019 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.ogc.sos.request;

import java.util.List;

/**
 * Interface to define methods for requests that supports featureOfInterest identifiers
 *
 * @since 1.0.0
 *
 */
public interface FeatureOfInterestIdentifierRequest {

    /**
     * Get FeatureOfInterest identifiers
     *
     * @return FeatureOfInterest identifiers
     */
    List<String> getFeatureIdentifiers();

    /**
     * Set FeatureOfInterest identifiers
     *
     * @param featureIdentifiers FeatureOfInterest identifiers
     */
    void setFeatureIdentifiers(List<String> featureIdentifiers);

    /**
     * Check if request contains FeatureOfInterest identifiers
     *
     * @return True if request contains FeatureOfInterest identifiers
     */
    default boolean isSetFeatureOfInterest() {
        return getFeatureIdentifiers() != null && !getFeatureIdentifiers().isEmpty();
    }

}
