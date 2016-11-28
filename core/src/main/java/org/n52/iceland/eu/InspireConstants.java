/*
 * Copyright 2015-2016 52°North Initiative for Geospatial Open Source
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
package org.n52.iceland.eu;

import org.n52.shetland.w3c.SchemaLocation;

/**
 *
 * @author <a href="mailto:d.nuest@52north.org">Daniel Nüst</a>
 */
public interface InspireConstants {

    String INSPIRE_DS_10_NS = "http://inspire.ec.europa.eu/schemas/inspire_ds/1.0";

    String INSPIRE_COMMON_10_NS = "inspire.ec.europa.eu/schemas/common/1.0";

    String INSPIRE_DS_10_PREFIX = "inspire_ds";

    String INSPIRE_COMMON_10_PREFIX = "inspire_c";

    String INSPIRE_DS_10_SCHEMA_LOCATION_URL = "http://inspire.ec.europa.eu/schemas/inspire_ds/1.0/inspire_ds.xsd";

    String INSPIRE_COMMON_10_SCHEMA_LOCATION_URL = "http://inspire.ec.europa.eu/schemas/common/1.0/common.xsd";

    SchemaLocation INSPIRE_DS_10_SCHEMA_LOCATION = new SchemaLocation(INSPIRE_DS_10_NS, INSPIRE_DS_10_SCHEMA_LOCATION_URL);

    SchemaLocation INSPIRE_COMMON_10_SCHEMA_LOCATION = new SchemaLocation(INSPIRE_COMMON_10_NS, INSPIRE_COMMON_10_SCHEMA_LOCATION_URL);
}
