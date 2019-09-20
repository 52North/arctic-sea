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
package org.n52.shetland.ogc.sos.delobs;

import java.util.Collections;
import java.util.Set;

import com.google.common.collect.Sets;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk
 *         J&uuml;rrens</a>
 *
 * @since 1.0.0
 */
public interface DeleteObservationConstants {

    String NS_SOSDO_1_0 = "http://www.opengis.net/sosdo/1.0";

    String NS_SOSDO_PREFIX = "sosdo";

    String NS_SOSDO_2_0 = "http://www.opengis.net/sosdo/2.0";

    String PARAM_OBSERVATION = "observation";

    String PARAM_PROCEDURE = "procedure";

    String PARAM_OBSERVED_PROPERTY = "observedProperty";

    String PARAM_FEATURE_OF_INTEREST = "featureOfInterest";

    String PARAM_OFFERING = "offering";

    String PARAM_TEMPORAL_FILTER = "temporalFilter";

    String CONFORMANCE_CLASS_10 = "http://www.opengis.net/extension/SOSDO/1.0/observationDeletion";

    String CONFORMANCE_CLASS_20 = "http://www.opengis.net/extension/SOSDO/2.0/observationDeletion";

    Set<String> CONFORMANCE_CLASSES =
            Collections.unmodifiableSet(Sets.newHashSet(CONFORMANCE_CLASS_10, CONFORMANCE_CLASS_20));

    String NS_SOSDO_1_0_SCHEMA_LOCATION = "http://52north.org/schema/sosdo/1.0/sosdo.xsd";

    String NS_SOSDO_2_0_SCHEMA_LOCATION = "http://52north.org/schema/sosdo/2.0/sosdo.xsd";

    enum Operations {
        DeleteObservation;

        public static boolean contains(final String s) {
            for (final Enum<?> p : values()) {
                if (p.name().equals(s)) {
                    return true;
                }
            }
            return false;
        }
    }

}
