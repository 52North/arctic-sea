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
package org.n52.shetland.inspire.ompr;

import org.n52.janmayen.http.MediaType;
import org.n52.shetland.w3c.SchemaLocation;

/**
 * INSPIRE OM Process constants
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public interface InspireOMPRConstants {

    String NS_OMPR_30 = "http://inspire.ec.europa.eu/schemas/ompr/3.0";

    String NS_OMPR_PREFIX = "ompr";

    String SCHEMA_LOCATION_URL_OMPR = "http://inspire.ec.europa.eu/schemas/ompr/3.0/Process.xsd";

    SchemaLocation OMPR_SCHEMA_LOCATION = new SchemaLocation(NS_OMPR_30, SCHEMA_LOCATION_URL_OMPR);

    String OMPR_30_OUTPUT_FORMAT_URL = NS_OMPR_30;

    MediaType OMPR_30_CONTENT_TYPE = new MediaType("text", "xml", "subtype", "ompr/3.0");

    String OMPR_30_OUTPUT_FORMAT_MIME_TYPE = OMPR_30_CONTENT_TYPE.toString();

    String FEATURE_CONCEPT_PROCESS = "http://inspire.ec.europa.eu/featureconcept/Process";
}
