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
package org.n52.shetland.ogc.sta.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("VisibilityModifier")
public class ObservationEntityDefinition extends STAEntityDefinition {

    public static final String ENTITY_NAME = OBSERVATION;

    public static final String ENTITY_SET_NAME = OBSERVATIONS;

    private static final String[] NAV_PROPS_MANDATORY = new String[] {
            DATASTREAM,
            FEATURE_OF_INTEREST
    };

    private static final String[] NAV_PROPS_OPTIONAL = new String[] {
            SUBJECTS,
            OBJECTS,
            GROUPS
    };

    private static final String[] ENTITY_PROPS_MANDATORY = new String[] {
            PROP_ID,
            PROP_PHENOMENON_TIME,
            PROP_RESULT,
            PROP_RESULT_TIME
    };

    private static final String[] ENTITY_PROPS_OPTIONAL = new String[] {
            PROP_RESULT_QUALITY,
            PROP_VALID_TIME,
            PROP_PARAMETERS
    };

    public static final Set<String> NAVIGATION_PROPERTIES_OPTIONAL =
            Collections.unmodifiableSet(new HashSet<>(Arrays.asList(NAV_PROPS_OPTIONAL)));

    public static final Set<String> NAVIGATION_PROPERTIES_MANDATORY =
            Collections.unmodifiableSet(new HashSet<>(Arrays.asList(NAV_PROPS_MANDATORY)));

    public static final Set<String> NAVIGATION_PROPERTIES =
            Collections.unmodifiableSet(STAEntityDefinition.combineSets(NAVIGATION_PROPERTIES_OPTIONAL,
                                                                        NAVIGATION_PROPERTIES_MANDATORY));

    public static final Set<String> ENTITY_PROPERTIES_OPTIONAL =
            Collections.unmodifiableSet(new HashSet<>(Arrays.asList(ENTITY_PROPS_OPTIONAL)));

    public static final Set<String> ENTITY_PROPERTIES_MANDATORY =
            Collections.unmodifiableSet(new HashSet<>(Arrays.asList(ENTITY_PROPS_MANDATORY)));

    public static final Set<String> ENTITY_PROPERTIES =
            Collections.unmodifiableSet(STAEntityDefinition.combineSets(ENTITY_PROPERTIES_OPTIONAL,
                                                                        ENTITY_PROPERTIES_MANDATORY));

    public ObservationEntityDefinition() {
        super(NAVIGATION_PROPERTIES_OPTIONAL,
              NAVIGATION_PROPERTIES_MANDATORY,
              ENTITY_PROPERTIES_OPTIONAL,
              ENTITY_PROPERTIES_MANDATORY);
    }
}
