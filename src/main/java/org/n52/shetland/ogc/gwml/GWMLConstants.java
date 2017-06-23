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
package org.n52.shetland.ogc.gwml;

import org.n52.janmayen.http.MediaType;
import org.n52.shetland.w3c.SchemaLocation;

public interface GWMLConstants {

    String NS_GWML_22 = "http://www.opengis.net/gwml/2.2";

    String NS_GWML_WELL_22 = "http://www.opengis.net/gwml-well/2.2";

    String NS_GWML_2_PREFIX = "gwml2";

    String NS_GWML_WELL_2_PREFIX = "gwml2w";

    String SCHEMA_LOCATION_URL_GWML_22 = "http://schemas.opengis.net/gwml/2.2/gwml2.xsd";

    String SCHEMA_LOCATION_URL_GWML_WELL_22 = "http://schemas.opengis.net/gwml/2.2/gwml2-well.xsd";

    SchemaLocation GWML_22_SCHEMA_LOCATION = new SchemaLocation(NS_GWML_22, SCHEMA_LOCATION_URL_GWML_22);

    SchemaLocation GWML_WELL_22_SCHEMA_LOCATION = new SchemaLocation(NS_GWML_WELL_22, SCHEMA_LOCATION_URL_GWML_WELL_22);

    String OBS_TYPE_GEOLOGY_LOG = "http://www.opengis.net/def/observationType/OGC-GWML/2.2/GW_GeologyLog";

    String OBS_TYPE_GEOLOGY_LOG_COVERAGE = "http://www.opengis.net/def/observationType/OGC-GWML/2.2/GW_GeologyLogCoverage";

    MediaType CONTENT_TYPE_GWML_22 = new MediaType("text", "xml", "subtype", "gwml/2.2");

    String PARAM_FROM_DEPTH = "fromDepth";

    String PARAM_TO_DEPTH = "toDepth";
}
