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
package org.n52.svalbard.encode;

import java.util.Map;
import java.util.Set;

import org.n52.shetland.ogc.SupportedType;
import org.n52.shetland.ogc.ows.service.OwsServiceKey;
import org.n52.svalbard.ConformanceClass;

/**
 * @since 1.0.0
 *
 * @param <T> the resulting type, the "Target"
 * @param <S> the input type, the "Source"
 */
public interface ObservationEncoder<S, T> extends ConformanceClass, Encoder<S, T> {

    /**
     * Indicator whether the ObservationEncoder of type or subtype Observation&Measurement 2.0
     *
     * @return Of type or not
     */
    boolean isObservationAndMeasurmentV20Type();

    /**
     * Indicator whether the single observations with the same procedure, observableProperty and featureOfInterest
     * should be merged to one observation.
     *
     * @return Merge or not
     */
    boolean shouldObservationsWithSameXBeMerged();

    boolean supportsResultStreamingForMergedValues();

    /**
     * Get the supported response formats for this {@linkplain ObservationEncoder} and the specified service and
     * version.
     *
     * @param service the service
     * @param version the version
     *
     * @return the response formats
     */
    Set<String> getSupportedResponseFormats(String service, String version);

    default Set<String> getSupportedResponseFormats(OwsServiceKey key) {
        return getSupportedResponseFormats(key.getService(), key.getVersion());
    }

    Map<String, Set<SupportedType>> getSupportedResponseFormatObservationTypes();

    /**
     * Indicator whether the procedure is to be encoded
     *
     * @return Indicator
     */
    default String getProcedureEncodingNamspace() {
        return "";
    }

}
