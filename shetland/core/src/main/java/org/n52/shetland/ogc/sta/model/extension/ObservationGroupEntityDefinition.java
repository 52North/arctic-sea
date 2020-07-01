/*
 * Copyright 2015-2020 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.ogc.sta.model.extension;

import org.n52.shetland.ogc.sta.model.STAEntityDefinition;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author <a href="mailto:j.speckamp@52north.org">Jan Speckamp</a>
 */
public class ObservationGroupEntityDefinition extends STAEntityDefinition {

    public static final String ENTITY_NAME = OBSERVATION_GROUP;

    public static final String ENTITY_SET_NAME = OBSERVATION_GROUPS;

    private static final String[] NAV_PROPS_MANDATORY = new String[] {
            OBSERVATION_RELATIONS
    };

    private static final String[] NAV_PROPS_OPTIONAL = new String[] { };

    private static final String[] ENTITY_PROPS_MANDATORY = new String[] {
            PROP_NAME,
            PROP_DESCRIPTION
    };

    private static final String[] ENTITY_PROPS_OPTIONAL = new String[] {};

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

    public ObservationGroupEntityDefinition() {
        super(NAVIGATION_PROPERTIES_OPTIONAL,
              NAVIGATION_PROPERTIES_MANDATORY,
              ENTITY_PROPERTIES_OPTIONAL,
              ENTITY_PROPERTIES_MANDATORY);
    }
}
