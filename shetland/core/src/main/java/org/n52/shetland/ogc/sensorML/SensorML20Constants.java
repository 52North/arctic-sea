/*
 * Copyright 2015-2022 52°North Spatial Information Research GmbH
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
package org.n52.shetland.ogc.sensorML;

import org.n52.janmayen.http.MediaType;
import org.n52.shetland.w3c.SchemaLocation;

/**
 * OGC SensorML 2.0 constants
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public interface SensorML20Constants extends SensorMLConstants {

    String NS_SML_20 = "http://www.opengis.net/sensorml/2.0";

    String SCHEMA_LOCATION_URL_SML_20 = "http://schemas.opengis.net/sensorML/2.0/sensorML.xsd";

    SchemaLocation SML_20_SCHEMA_LOCATION = new SchemaLocation(NS_SML_20, SCHEMA_LOCATION_URL_SML_20);

    String SENSORML_20_OUTPUT_FORMAT_URL = NS_SML_20;

    MediaType SENSORML_20_CONTENT_TYPE = new MediaType("text", "xml", "subtype", "sensorml/2.0");

    String SENSORML_20_OUTPUT_FORMAT_MIME_TYPE = SENSORML_20_CONTENT_TYPE.toString();

}
