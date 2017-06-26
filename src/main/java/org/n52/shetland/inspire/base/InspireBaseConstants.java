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
package org.n52.shetland.inspire.base;

import org.n52.shetland.w3c.SchemaLocation;

public interface InspireBaseConstants {

    String NS_BASE = "http://inspire.ec.europa.eu/schemas/base/3.3";

    String NS_BASE_PREFIX = "base";

    String SCHEMA_LOCATION_URL_BASE = "http://inspire.ec.europa.eu/schemas/base/3.3/BaseTypes.xsd";

    SchemaLocation BASE_33_SCHEMA_LOCATION = new SchemaLocation(NS_BASE, SCHEMA_LOCATION_URL_BASE);
}
