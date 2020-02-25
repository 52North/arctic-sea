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

import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("VisibilityModifier")
public abstract class STAEntityDefinition implements StaConstants {

    public static String[] ALLCOLLECTIONS = new String[] {
            DATASTREAMS,
            OBSERVATIONS,
            THINGS,
            LOCATIONS,
            HISTORICAL_LOCATIONS,
            SENSORS,
            OBSERVED_PROPERTIES,
            FEATURES_OF_INTEREST
    };

    // Entity Property Names
    public static String PROP_ID = "id";
    public static String PROP_SELF_LINK = "selfLink";
    public static String PROP_DEFINITION = "definition";
    public static String PROP_DESCRIPTION = "description";
    public static String PROP_ENCODINGTYPE = "encodingType";
    public static String PROP_FEATURE = "feature";
    public static String PROP_LOCATION = "location";
    public static String PROP_NAME = "name";
    public static String PROP_OBSERVATION_TYPE = "observationType";
    public static String PROP_OBSERVED_AREA = "observedArea";
    public static String PROP_PARAMETERS = "parameters";
    public static String PROP_PHENOMENON_TIME = "phenomenonTime";
    public static String PROP_PROPERTIES = "properties";
    public static String PROP_RESULT = "result";
    public static String PROP_RESULT_QUALITY = "resultQuality";
    public static String PROP_RESULT_TIME = "resultTime";
    public static String PROP_TIME = "time";
    public static String PROP_UOM = "unitOfMeasurement";
    public static String PROP_VALID_TIME = "validTime";
    public static String PROP_METADATA = "metadata";
    public static String PROP_SYMBOL = "symbol";

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
}
