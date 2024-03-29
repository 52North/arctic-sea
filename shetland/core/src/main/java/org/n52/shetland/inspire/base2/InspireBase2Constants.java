/*
 * Copyright (C) 2015-2022 52°North Spatial Information Research GmbH
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
package org.n52.shetland.inspire.base2;

import org.n52.shetland.w3c.SchemaLocation;

public interface InspireBase2Constants {

    String NS_BASE2 = "http://inspire.ec.europa.eu/schemas/base2/2.0";

    String NS_BASE2_PREFIX = "base2";

    String SCHEMA_LOCATION_URL_BASE2 = "http://inspire.ec.europa.eu/schemas/base2/2.0/BaseTypes2.xsd";

    SchemaLocation BASE2_20_SCHEMA_LOCATION = new SchemaLocation(NS_BASE2, SCHEMA_LOCATION_URL_BASE2);
}
