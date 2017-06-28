/*
 * Copyright 2015-2017 52°North Initiative for Geospatial Open Source
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
package org.n52.iceland.util;

/**
 * Default constants interface with constants used by other constant interfaces
 * or classes
 *
 * @since 1.0.0
 * @deprecated use constants in less generic interfaces
 *
 */
@Deprecated
public interface Constants {

    int EPSG_WGS84_3D = 4979;

    int EPSG_WGS84 = 4326;

    String DEFAULT_ENCODING = "UTF-8";

    String CSV_BLOCK_SEPARATOR = ",";

    String CSV_TOKEN_SEPARATOR = "@@";

    String URN = "urn";

    String HTTP = "http";
}
