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
package org.n52.shetland.ogc.sos.delobs;

import static java.util.Collections.singleton;
import static java.util.Collections.unmodifiableSet;

import java.util.Set;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk
 *         J&uuml;rrens</a>
 *
 * @since 1.0.0
 */
public interface DeleteObservationConstants {

    String NS_SOSDO_1_0 = "http://www.opengis.net/sosdo/1.0";

    String NS_SOSDO_1_0_PREFIX = "sosdo";

    String PARAMETER_NAME = "observation";

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

    String CONFORMANCE_CLASS = "http://www.opengis.net/extension/SOSDO/1.0/observationDeletion";

    Set<String> CONFORMANCE_CLASSES = unmodifiableSet(singleton(CONFORMANCE_CLASS));

    String NS_SOSDO_1_0_SCHEMA_LOCATION =
            "https://raw.githubusercontent.com/52North/SOS/master/extensions/do/xml/src/main/xsd/sosdo.xsd";

}
