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
package org.n52.iceland.service;

/**
 * @author Christian Autermann <c.autermann@52north.org>
 *
 * @since 1.0.0
 */
public interface ServiceSettings {

    String SERVICE_URL = "service.serviceURL";

    @Deprecated
    //  String SUPPORTS_QUALITY = "service.supportsQuality";
    String SENSOR_DIRECTORY = "service.sensorDirectory";

    @Deprecated
    String USE_DEFAULT_PREFIXES = "service.useDefaultPrefixes";

    @Deprecated
    String ENCODE_FULL_CHILDREN_IN_DESCRIBE_SENSOR
            = "service.encodeFullChildrenInDescribeSensor";

    @Deprecated
    String MAX_GET_OBSERVATION_RESULTS = "service.maxGetObservationResults";

    @Deprecated
    String DEREGISTER_JDBC_DRIVER = "service.jdbc.deregister";

    @Deprecated
    String ADD_OUTPUTS_TO_SENSOR_ML = "service.addOutputsToSensorML";

    @Deprecated
    String STRICT_SPATIAL_FILTERING_PROFILE
            = "service.strictSpatialFilteringProfile";

    String VALIDATE_RESPONSE = "service.response.validate";

}
