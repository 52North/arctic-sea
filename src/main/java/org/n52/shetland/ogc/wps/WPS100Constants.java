/*
 * Copyright 2016 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.ogc.wps;

import org.n52.shetland.w3c.SchemaLocation;

/**
 * WpsConstants holds all important and often used constants (e.g. name of the
 * getCapabilities operation) that are specific to OGC WPS 1.0.0
 *
 * @since 1.0.0
 */
public interface WPS100Constants extends WPSConstants {

    String NS_WPS = "http://www.opengis.net/wps/1.0.0";

    /** Constant for the schema repository of the WPS */
    String SCHEMA_LOCATION_WPS = "http://schemas.opengis.net/wps/1.0.0/wpsAll.xsd";

    String SCHEMA_LOCATION_URL_WPS1_GET_CAPBABILITIES = "http://schemas.opengis.net/wps/1.0.0/wpsAll.xsd";

    SchemaLocation WPS1_SCHEMA_LOCATION = new SchemaLocation(NS_WPS, SCHEMA_LOCATION_WPS);

    //TODO: there are different schemas for getcapabilities request and response
    SchemaLocation GET_CAPABILITIES_WPS1_SCHEMA_LOCATION = new SchemaLocation(NS_WPS, SCHEMA_LOCATION_URL_WPS1_GET_CAPBABILITIES);

    /** Constant for actual implementing version */
    String SERVICEVERSION = "1.0.0";
}
