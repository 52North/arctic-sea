/*
 * Copyright 2015 52°North Initiative for Geospatial Open Source
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
package org.n52.iceland.ds;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <c.autermann@52north.org>
 *
 * @since 4.0.0
 */
public interface FeatureQuerySettingsProvider {

    String DATASOURCE_NORTHING_FIRST = "misc.datasourceNorthingFirst";

    String EPSG_CODES_WITH_NORTHING_FIRST = "misc.switchCoordinatesForEpsgCodes";

    String STORAGE_EPSG = "service.defaultEpsg";

    String STORAGE_3D_EPSG = "service.default3DEpsg";

    String DEFAULT_RESPONSE_EPSG = "service.defaultResponseEpsg";

    String DEFAULT_RESPONSE_3D_EPSG = "service.defaultRespopnse3DEpsg";

    String SPATIAL_DATASOURCE = "service.SpatialDatasource";

    String SUPPORTED_CRS_KEY = "service.supportedCrs";

    String AUTHORITY = "service.crsAuthority";

}
