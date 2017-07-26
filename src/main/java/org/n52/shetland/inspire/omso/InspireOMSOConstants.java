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
package org.n52.shetland.inspire.omso;

import javax.xml.namespace.QName;

import org.n52.shetland.w3c.SchemaLocation;

/**
 * INSPIRES OM Specialised Observation constants
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public interface InspireOMSOConstants {

    String NS_OMSO_30 = "http://inspire.ec.europa.eu/schemas/omso/3.0";

    String NS_OMSO_PREFIX = "omso";

    String SCHEMA_LOCATION_URL_OMSO = "http://inspire.ec.europa.eu/schemas/omso/3.0/SpecialisedObservations.xsd";

    SchemaLocation OMSO_SCHEMA_LOCATION = new SchemaLocation(NS_OMSO_30, SCHEMA_LOCATION_URL_OMSO);

    // observation types
    String OBS_TYPE_POINT_OBSERVATION = "http://inspire.ec.europa.eu/featureconcept/PointObservation";

    String OBS_TYPE_POINT_TIME_SERIES_OBSERVATION =
            "http://inspire.ec.europa.eu/featureconcept/PointTimeSeriesObservation";

    String OBS_TYPE_MULTI_POINT_OBSERVATION = "http://inspire.ec.europa.eu/featureconcept/MultiPointObservation";

    String OBS_TYPE_PROFILE_OBSERVATION = "http://inspire.ec.europa.eu/featureconcept/ProfileObservation";

    String OBS_TYPE_TRAJECTORY_OBSERVATION = "http://inspire.ec.europa.eu/featureconcept/TrajectoryObservation";

    String EN_OMSO_POINT_TIMESERIES_OBSERVATION = "PointTimeSeriesObservation";

    QName QN_POINT_TIMESERES_OBSERVATION = new QName(NS_OMSO_30, EN_OMSO_POINT_TIMESERIES_OBSERVATION, NS_OMSO_PREFIX);;

}
