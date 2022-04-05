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
package org.n52.shetland.inspire.omor;

import org.n52.shetland.w3c.SchemaLocation;

public interface InspireOMORConstants {

    String NS_OMOR_30 = "http://inspire.ec.europa.eu/schemas/omor/3.0";

    String NS_OMOR_PREFIX = "omor";

    String SCHEMA_LOCATION_URL_OMOR = "http://inspire.ec.europa.eu/schemas/omor/3.0/ObservationReferences.xsd";

    SchemaLocation OMOR_SCHEMA_LOCATION = new SchemaLocation(NS_OMOR_30, SCHEMA_LOCATION_URL_OMOR);
}
