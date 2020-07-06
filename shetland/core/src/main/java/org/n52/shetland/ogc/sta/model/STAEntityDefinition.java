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
package org.n52.shetland.ogc.sta.model;

import org.n52.shetland.ogc.sta.StaConstants;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("VisibilityModifier")
public abstract class STAEntityDefinition implements StaConstants {

    public static String[] ALLCOLLECTIONS = new String[]{DATASTREAMS,
                                                         OBSERVATIONS,
                                                         THINGS,
                                                         LOCATIONS,
                                                         HISTORICAL_LOCATIONS,
                                                         SENSORS,
                                                         OBSERVED_PROPERTIES,
                                                         FEATURES_OF_INTEREST};

    // Map from EntityName to Definition
    public static Map<String, STAEntityDefinition> definitions = createMap();

    private final Set<String> navPropsOptional;
    private final Set<String> navPropsMandatory;
    private final Set<String> entityPropsOptional;
    private final Set<String> entityPropsMandatory;

    protected STAEntityDefinition(Set<String> navPropOptional,
                                  Set<String> navPropMandatory,
                                  Set<String> entityPropOptional,
                                  Set<String> entityPropMandatory) {
        this.navPropsOptional = navPropOptional;
        this.navPropsMandatory = navPropMandatory;
        this.entityPropsOptional = entityPropOptional;
        this.entityPropsMandatory = entityPropMandatory;
    }

    public Set<String> getNavPropsOptional() {
        return navPropsOptional;
    }

    public Set<String> getNavPropsMandatory() {
        return navPropsMandatory;
    }

    public Set<String> getEntityPropsOptional() {
        return entityPropsOptional;
    }

    public Set<String> getEntityPropsMandatory() {
        return entityPropsMandatory;
    }

    static Set<String> combineSets(Set<String>... sets) {
        HashSet<String> result = new HashSet<>();
        for (Set<String> set : sets) {
            result.addAll(set);
        }
        return result;
    }

    private static Map<String, STAEntityDefinition> createMap() {
        HashMap<String, STAEntityDefinition> map = new HashMap<>();
        DatastreamEntityDefinition dsED = new DatastreamEntityDefinition();
        FeatureOfInterestEntityDefinition foiED = new FeatureOfInterestEntityDefinition();
        HistoricalLocationEntityDefinition hlED = new HistoricalLocationEntityDefinition();
        LocationEntityDefinition lED = new LocationEntityDefinition();
        ObservationEntityDefinition oED = new ObservationEntityDefinition();
        ObservedPropertyEntityDefinition opED = new ObservedPropertyEntityDefinition();
        SensorEntityDefinition sED = new SensorEntityDefinition();
        ThingEntityDefinition tED = new ThingEntityDefinition();
        map.put(DATASTREAM, dsED);
        map.put(DATASTREAMS, dsED);
        map.put(FEATURE_OF_INTEREST, foiED);
        map.put(FEATURES_OF_INTEREST, foiED);
        map.put(HISTORICAL_LOCATION, hlED);
        map.put(HISTORICAL_LOCATIONS, hlED);
        map.put(LOCATION, lED);
        map.put(LOCATIONS, lED);
        map.put(OBSERVATION, oED);
        map.put(OBSERVATIONS, oED);
        map.put(OBSERVED_PROPERTY, opED);
        map.put(OBSERVED_PROPERTIES, opED);
        map.put(SENSOR, sED);
        map.put(SENSORS, sED);
        map.put(THING, tED);
        map.put(THINGS, tED);
        return map;
    }
}
