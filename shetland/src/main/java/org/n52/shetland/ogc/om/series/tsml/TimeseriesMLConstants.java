/*
 * Copyright 2015-2018 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.ogc.om.series.tsml;

import javax.xml.namespace.QName;

import org.n52.janmayen.http.MediaType;
import org.n52.shetland.ogc.om.series.AbstractInterpolationType;
import org.n52.shetland.w3c.SchemaLocation;

/**
 * @see http://www.opengeospatial.org/standards/tsml
 */
public interface TimeseriesMLConstants {
    String NS_TSML_10 = "http://www.opengis.net/tsml/1.0";

    String NS_TSML_10_PREFIX = "tsml";

    String NS_TSML_10_PROCEDURE_ENCODING = "http://www.opengis.net/def/timeseries/observationProcess";

    String PROCESS_TYPE_SIMULATION = "http://www.opengis.net/def/timeseries/ProcessTypeCode/Simulation";

    String PROCESS_TYPE_MANUAL_METHOD = "http://www.opengis.net/def/timeseries/ProcessTypeCode/ManualMethod";

    String PROCESS_TYPE_SENSOR = "http://www.opengis.net/def/timeseries/ProcessTypeCode/Sensor";

    String PROCESS_TYPE_ALGORITHM = "http://www.opengis.net/def/timeseries/ProcessTypeCode/Algorithm ";

    String PROCESS_TYPE_UNKNOWN = "http://www.opengis.net/def/timeseries/ProcessTypeCode/Unknown ";

    String OBSERVATION_TYPE_MEASURMENT_TVP =
            "http://www.opengis.net/def/observationType/timeseriesML/1.0/MeasurementTimeseriesTVPObservation";

    String OBSERVATION_TYPE_CATEGORICAL_TVP =
            "http://www.opengis.net/def/observationType/timeseriesML/1.0/CategoricalTimeseriesTVPObservation";

    String OBSERVATION_TYPE_MEASURMENT_TDR =
            "http://www.opengis.net/def/observationType/timeseriesML/1.0/MeasurementTimeseriesDomainRangeObservation";

    String OBSERVATION_TYPE_CATEGORICAL_TDR =
            "http://www.opengis.net/def/observationType/timeseriesML/1.0/CategoricalTimeseriesDomainRangeObservation";

    String TEXT = "text";

    String XML = "xml";

    String SUBTYPE = "subtype";

    MediaType TSML_CONTENT_TYPE = new MediaType(TEXT, XML, SUBTYPE, "timeseriesml/1.0");

    String SCHEMA_LOCATION_URL_TSML_10 = "http://schemas.opengis.net/tsml/1.0/timeseriesML.xsd";

    String SCHEMA_LOCATION_URL_TSML_10_DR = "http://schemas.opengis.net/tsml/1.0/timeseriesDR.xsd";

    String SCHEMA_LOCATION_URL_TSML_10_TS = "http://schemas.opengis.net/tsml/1.0/timeseriesTVP.xsd";

    String SCHEMA_LOCATION_URL_TSML_10_MF = "http://schemas.opengis.net/tsml/1.0/monitoringFeature.xsd";

    SchemaLocation TSML_10_SCHEMA_LOCATION = new SchemaLocation(NS_TSML_10, SCHEMA_LOCATION_URL_TSML_10);

    SchemaLocation TSML_10_DR_SCHEMA_LOCATION = new SchemaLocation(NS_TSML_10, SCHEMA_LOCATION_URL_TSML_10_DR);

    SchemaLocation TSML_10_TS_SCHEMA_LOCATION = new SchemaLocation(NS_TSML_10, SCHEMA_LOCATION_URL_TSML_10_TS);

    SchemaLocation TSML_10_MF_SCHEMA_LOCATION = new SchemaLocation(NS_TSML_10, SCHEMA_LOCATION_URL_TSML_10_MF);

    String EN_POINT = "point";

    String EN_MEASUREMENT_TVP = "MeasurementTVP";

    String EN_TIME = "time";

    String EN_VALUE = "value";

    String EN_METADATA = "metadata";

    String EN_TVP_MEASUREMENT_METADATA = "TVPMeasurementMetadata";

    String EN_NIL_REASON = "nilReason";

    String EN_MEASUREMENT_TIMESERIES = "MeasurementTimeseries";

    String EN_TIMESERIES_METADATA = "TimeseriesMetadata";

    String EN_MEASUREMENT_TIMESERIES_METADATA = "MeasurementTimeseriesMetadata";

    String EN_TEMPORAL_EXTENT = "temporalExtent";

    String EN_DEFAULT_POINT_METADATA = "defaultPointMetadata";

    String EN_DEFAULT_TVP_MEASUREMENT_METADATA = "DefaultTVPMeasurementMetadata";

    String EN_INTERPOLATION_TYPE = "interpolationType";

    String EN_CUMULATIVE = "cumulative";

    String EN_UOM = "uom";

    QName QN_POINT = new QName(NS_TSML_10, EN_POINT, NS_TSML_10_PREFIX);

    QName QN_MEASUREMENT_TVP = new QName(NS_TSML_10, EN_MEASUREMENT_TVP, NS_TSML_10_PREFIX);

    QName QN_TIME = new QName(NS_TSML_10, EN_TIME, NS_TSML_10_PREFIX);

    QName QN_VALUE = new QName(NS_TSML_10, EN_VALUE, NS_TSML_10_PREFIX);

    QName QN_METADATA = new QName(NS_TSML_10, EN_METADATA, NS_TSML_10_PREFIX);

    QName QN_TVP_MEASUREMENT_METADATA = new QName(NS_TSML_10, EN_TVP_MEASUREMENT_METADATA, NS_TSML_10_PREFIX);

    QName QN_NIL_REASON = new QName(NS_TSML_10, EN_NIL_REASON, NS_TSML_10_PREFIX);

    QName QN_MEASUREMENT_TIMESERIES = new QName(NS_TSML_10, EN_MEASUREMENT_TIMESERIES, NS_TSML_10_PREFIX);

    QName QN_TIMESERIES_METADATA = new QName(NS_TSML_10, EN_TIMESERIES_METADATA, NS_TSML_10_PREFIX);

    QName QN_MEASUREMENT_TIMESERIES_METADATA =
            new QName(NS_TSML_10, EN_MEASUREMENT_TIMESERIES_METADATA, NS_TSML_10_PREFIX);

    QName QN_TEMPORAL_EXTENT = new QName(NS_TSML_10, EN_TEMPORAL_EXTENT, NS_TSML_10_PREFIX);

    QName QN_DEFAULT_POINT_METADATA = new QName(NS_TSML_10, EN_DEFAULT_POINT_METADATA, NS_TSML_10_PREFIX);

    QName QN_DEFAULT_TVP_MEASUREMENT_METADATA =
            new QName(NS_TSML_10, EN_DEFAULT_TVP_MEASUREMENT_METADATA, NS_TSML_10_PREFIX);

    QName QN_INTERPOLATION_TYPE = new QName(NS_TSML_10, EN_INTERPOLATION_TYPE, NS_TSML_10_PREFIX);

    QName QN_CUMULATIVE = new QName(NS_TSML_10, EN_CUMULATIVE, NS_TSML_10_PREFIX);

    QName UOM = new QName(NS_TSML_10, EN_UOM, NS_TSML_10_PREFIX);

    /**
     * @see MeasurementTimeseriesMetadata#isCumulative()
     */
    String SERIES_METADATA_CUMULATIVE = NS_TSML_10 + "/cumulative";

    String INTERPOLATION_TYPE = "http://www.opengis.net/def/timeseries/InterpolationCode";

    /**
     * Hold allowed values for element <code>interpolationType</code>.
     */
    enum InterpolationType implements AbstractInterpolationType {

        /**
         * Continuous
         * http://www.opengis.net/def/timeseries/InterpolationCode/Continuous
         */
        Continuous,
        /**
         * Discontinuous
         * http://www.opengis.net/def/timeseries/InterpolationCode/Discontinuous
         */
        Discontinuous,
        /**
         * Instantaneous total
         * http://www.opengis.net/def/timeseries/InterpolationCode/InstantTotal
         */
        InstantTotal,
        /**
         * Average in preceding interval
         * http://www.opengis.net/def/timeseries/InterpolationCode/AveragePrec
         */
        AveragePrec,
        /**
         * Maximum in preceding interval
         * http://www.opengis.net/def/timeseries/InterpolationCode/MaxPrec
         */
        MaxPrec,
        /**
         * Minimum in preceding interval
         * http://www.opengis.net/def/timeseries/InterpolationCode/MinPrec
         */
        MinPrec,
        /**
         * Preceding total
         * http://www.opengis.net/def/timeseries/InterpolationCode/PrecTotal
         */
        TotalPrec,
        /**
         * Average in succeeding interval
         * http://www.opengis.net/def/timeseries/InterpolationCode/AverageSucc
         */
        AverageSucc,
        /**
         * Succeeding total
         * http://www.opengis.net/def/timeseries/InterpolationCode/TotalSucc
         */
        TotalSucc,
        /**
         * Minimum in succeeding interval
         * http://www.opengis.net/def/timeseries/InterpolationCode/MinSucc
         */
        MinSucc,
        /**
         * Maximum in succeeding interval
         * http://www.opengis.net/def/timeseries/InterpolationCode/MaxSucc
         */
        MaxSucc,
        /**
         * Constant in preceding interval
         * http://www.opengis.net/def/timeseries/InterpolationCode/ConstPrec
         */
        ConstPrec,
        /**
         * Constant in succeeding interval
         * http://www.opengis.net/def/timeseries/InterpolationCode/ConstSucc
         */
        ConstSucc;

        public String getIdentifier() {
            return INTERPOLATION_TYPE + "/" + this.toString();
        }

        public String getTitle() {
            return this.toString();
        }

        public static InterpolationType from(String v) {
            return valueOf(v.replace(INTERPOLATION_TYPE + "/", ""));
        }
    }
}
