/*
 * Copyright 2015-${currentYear} 52°North Spatial Information Research GmbH
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
package org.n52.shetland.ogc.sta;

public interface StaConstants {

    /**
     * Profiles/Extensions
     */
    String VANILLA = "vanilla";
    String STAPLUS = "staplus";
    String MULTIDATASTREAM = "multiDatastream";
    String UFZAGGREGATA = "ufzaggregata";

    /**
     * Core Entities + Collections
     */
    String THING = "Thing";

    String THINGS = "Things";

    String LOCATION = "Location";

    String LOCATIONS = "Locations";

    String DATASTREAM = "Datastream";

    String DATASTREAMS = "Datastreams";

    String HISTORICAL_LOCATION = "HistoricalLocation";

    String HISTORICAL_LOCATIONS = "HistoricalLocations";

    String SENSOR = "Sensor";

    String SENSORS = "Sensors";

    String OBSERVATION = "Observation";

    String OBSERVATIONS = "Observations";

    String OBSERVED_PROPERTY = "ObservedProperty";

    String OBSERVED_PROPERTIES = "ObservedProperties";

    String FEATURE_OF_INTEREST = "FeatureOfInterest";

    String FEATURES_OF_INTEREST = "FeaturesOfInterest";

    /**
     * CitSci Extension Entities + Collections
     */

    String GROUP = "Group";
    String GROUPS = "Groups";
    String RELATION = "Relation";
    String RELATIONS = "Relations";
    String PARTY = "Party";
    String PARTIES = "Parties";
    String LICENSE = "License";
    String LICENSES = "Licenses";
    String PROJECT = "Project";
    String PROJECTS = "Projects";
    String SUBJECT = "Subject";
    String SUBJECTS = "Subjects";
    String OBJECT = "Object";
    String OBJECTS = "Objects";

    String PARENT = "parent";
    String CHILDREN = "children";

    String AT = "@";
    String IOT_ID = "iot.id";
    String IOT_SELFLINK = "iot.selfLink";
    String AT_IOT_ID = AT + IOT_ID;
    String AT_IOT_SELFLINK = AT + IOT_SELFLINK;

    /**
     * Properties
     */
    String PROP_ID = "id";
    String PROP_SELF_LINK = "selfLink";
    String PROP_DEFINITION = "definition";
    String PROP_DESCRIPTION = "description";
    String PROP_ENCODINGTYPE = "encodingType";
    String PROP_FEATURE = "feature";
    String PROP_LOCATION = "location";
    String PROP_NAME = "name";
    String PROP_OBSERVATION_TYPE = "observationType";
    String PROP_OBSERVED_AREA = "observedArea";
    String PROP_PARAMETERS = "parameters";
    String PROP_PHENOMENON_TIME = "phenomenonTime";
    String PROP_PROPERTIES = "properties";
    String PROP_RESULT = "result";
    String PROP_RESULT_QUALITY = "resultQuality";
    String PROP_RESULT_TIME = "resultTime";
    String PROP_TIME = "time";
    String PROP_UOM = "unitOfMeasurement";
    String PROP_VALID_TIME = "validTime";
    String PROP_METADATA = "metadata";
    String PROP_SYMBOL = "symbol";
    String PROP_TYPE = "type";
    String PROP_DISPLAY_NAME = "displayName";
    String PROP_ROLE = "role";
    String PROP_RUNTIME = "runtime";
    String PROP_URL = "url";
    String PROP_LOGO = "logo";
    String PROP_CLASSIFICATION = "classification";
    String PROP_TERMS_OF_USE = "termsOfUse";
    String PROP_PRIVACY_POLICY = "privacyPolicy";
    String PROP_PURPOSE = "purpose";
    String PROP_START_TIME = "startTime";
    String PROP_END_TIME = "endTime";
    String PROP_CREATION_TIME = "creationTime";
    String PROP_EXTERNAL_OBJECT = "externalObject";
    String PROP_AUTH_ID = "authId";
    String PROP_PERSONAL_DATA = "personalData";
}
