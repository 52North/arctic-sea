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

import java.util.Map;
import java.util.Set;

/**
 * @author <a href="mailto:j.speckamp@52north.org">Jan Speckamp</a>
 */
public class CitSciExtensionEntityDefinition extends STAEntityDefinition {

    public static String[] ALLCOLLECTIONS = new String[] {
            OBSERVATION_GROUPS,
            OBSERVATION_RELATIONS,
            PARTIES,
            PROJECTS,
            LICENSES,
    };

    protected CitSciExtensionEntityDefinition(Set<String> navPropOptional,
                                              Set<String> navPropMandatory,
                                              Set<String> entityPropOptional,
                                              Set<String> entityPropMandatory) {
        super(navPropOptional, navPropMandatory, entityPropOptional, entityPropMandatory);
    }

    // Map from EntityName to Definition
    public static Map<String, STAEntityDefinition> definitions = createStaMap();

    private static Map<String, STAEntityDefinition> createStaMap() {
        Map<String, STAEntityDefinition> coreMap = createMap();

        ObservationGroupEntityDefinition ogED = new ObservationGroupEntityDefinition();
        coreMap.put(OBSERVATION_GROUP, ogED);
        coreMap.put(OBSERVATION_GROUPS, ogED);

        ObservationRelationEntityDefinition orED = new ObservationRelationEntityDefinition();
        coreMap.put(OBSERVATION_RELATION, orED);
        coreMap.put(OBSERVATION_RELATIONS, orED);

        ProjectEntityDefinition proED = new ProjectEntityDefinition();
        coreMap.put(PROJECT, proED);
        coreMap.put(PROJECTS, proED);

        PartyEntityDefinition parED = new PartyEntityDefinition();
        coreMap.put(PARTY, parED);
        coreMap.put(PARTIES, parED);

        LicenseEntityDefinition lED = new LicenseEntityDefinition();
        coreMap.put(LICENSE, lED);
        coreMap.put(LICENSES, lED);

        return coreMap;
    }
}
