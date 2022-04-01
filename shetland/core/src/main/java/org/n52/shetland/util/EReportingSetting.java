/*
 * Copyright 2015-2022 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.util;

/**
 * Setting definition provider for AQD e-Reporting definitions
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public interface EReportingSetting {

    String EREPORTING_GROUP_TITLE = "eReporting";

    String EREPORTING_NAMESPACE = "eReporting.namespace";

    String EREPORTING_OBSERVATION_PREFIX = "eReporting.observation.prefix";

    String EREPORTING_OFFERING_PREFIX_KEY = "eReporting.offering.prefix";

    String EREPORTING_PROCEDURE_PREFIX_KEY = "eReporting.procedure.prefix";

    String EREPORTING_FEATURE_OF_INTEREST_PREFIX_KEY = "eReporting.featureOfInterest.prefix";

    String EREPORTING_SAMPLING_POINT_PREFIX_KEY = "eReporting.samplingPoint.prefix";

    String EREPORTING_STATION_PREFIX_KEY = "eReporting.station.prefix";

    String EREPORTING_NETWORK_PREFIX_KEY = "eReporting.network.prefix";

    String EREPORTING_VALIDITY_FLAGS = "eReporting.flags.validity";

    String EREPORTING_VERIFICATION_FLAGS = "eReporting.flags.verification";

}
