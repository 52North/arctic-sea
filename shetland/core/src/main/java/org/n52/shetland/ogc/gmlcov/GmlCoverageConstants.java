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
package org.n52.shetland.ogc.gmlcov;

import org.n52.shetland.w3c.SchemaLocation;

/**
 * Interface for GML coverage constants
 * @since 1.0.0
 *
 */
public interface GmlCoverageConstants {

    String NS_GML_COV = "http://www.opengis.net/gmlcov/1.0";

    String NS_GML_COV_PREFIX = "gmlcov";

    String SCHEMA_LOCATION_URL_GML_COVERAGE_10 = "http://schemas.opengis.net/gmlcov/1.0/gmlcovAll.xsd";

    SchemaLocation GML_COVERAGE_10_SCHEMA_LOCATION = new SchemaLocation(NS_GML_COV,
            SCHEMA_LOCATION_URL_GML_COVERAGE_10);

}
