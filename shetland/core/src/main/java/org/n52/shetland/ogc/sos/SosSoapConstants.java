/*
 * Copyright (C) 2015-2021 52°North Spatial Information Research GmbH
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
package org.n52.shetland.ogc.sos;

/**
 * Constants for SOAP messages
 *
 * @since 1.0.0
 */
public interface SosSoapConstants {

    // SOS core
    String REQ_ACTION_GET_CAPABILITIES = "http://www.opengis.net/def/serviceOperation/sos/core/2.0/GetCapabilities";

    String RESP_ACTION_GET_CAPABILITIES =
            "http://www.opengis.net/def/serviceOperation/sos/core/2.0/GetCapabilitiesResponse";

    String REQ_ACTION_DESCRIBE_SENSOR = "http://www.opengis.net/swes/2.0/DescribeSensor";

    String RESP_ACTION_DESCRIBE_SENSOR = "http://www.opengis.net/swes/2.0/DescribeSensorResponse";

    String REQ_ACTION_GET_OBSERVATION = "http://www.opengis.net/def/serviceOperation/sos/core/2.0/GetObservation";

    String RESP_ACTION_GET_OBSERVATION =
            "http://www.opengis.net/def/serviceOperation/sos/core/2.0/GetObservationResponse";

    // SOS transactional
    String REQ_ACTION_INSERT_OBSERVATION =
            "http://www.opengis.net/def/serviceOperation/sos/obsInsertion/2.0/InsertObservation";

    String RESP_ACTION_INSERT_OBSERVATION =
            "http://www.opengis.net/def/serviceOperation/sos/obsInsertion/2.0/InsertObservationResponse";

    String REQ_ACTION_UPDATE_SENSOR_DESCRIPTION = "http://www.opengis.net/swes/2.0/UpdateSensorDescription";

    String RESP_ACTION_UPDATE_SENSOR_DESCRIPTION = "http://www.opengis.net/swes/2.0/UpdateSensorDescriptionResponse";

    String REQ_ACTION_INSERT_SENSOR = "http://www.opengis.net/swes/2.0/InsertSensor";

    String RESP_ACTION_INSERT_SENSOR = "http://www.opengis.net/swes/2.0/InsertSensorResponse";

    String REQ_ACTION_DELETE_SENSOR = "http://www.opengis.net/swes/2.0/DeleteSensor";

    String RESP_ACTION_DELETE_SENSOR = "http://www.opengis.net/swes/2.0/DeleteSensorResponse";

    // SOS enhanced
    String REQ_ACTION_GET_FEATURE_OF_INTEREST =
            "http://www.opengis.net/def/serviceOperation/sos/foiRetrieval/2.0/GetFeatureOfInterest";

    String RESP_ACTION_GET_FEATURE_OF_INTEREST =
            "http://www.opengis.net/def/serviceOperation/sos/foiRetrieval/2.0/GetFeatureOfInterestResponse";

    String REQ_ACTION_GET_OBSERVATION_BY_ID =
            "http://www.opengis.net/def/serviceOperation/sos/obsByIdRetrieval/2.0/GetObservationById";

    String RESP_ACTION_GET_OBSERVATION_BY_ID =
            "http://www.opengis.net/def/serviceOperation/sos/obsByIdRetrieval/2.0/GetObservationByIdResponse";

    // SOS result handling
    String REQ_ACTION_GET_RESULT_TEMPLATE =
            "http://www.opengis.net/def/serviceOperation/sos/resultRetrieval/2.0/GetResultTemplate";

    String RESP_ACTION_GET_RESULT_TEMPLATE =
            "http://www.opengis.net/def/serviceOperation/sos/resultRetrieval/2.0/GetResultTemplateResponse";

    String REQ_ACTION_INSERT_RESULT_TEMPLATE =
            "http://www.opengis.net/def/serviceOperation/sos/resultInsertion/2.0/InsertResultTemplate";

    String RESP_ACTION_INSERT_RESULT_TEMPLATE =
            "http://www.opengis.net/def/serviceOperation/sos/resultInsertion/2.0/InsertResultTemplateResponse";

    String REQ_ACTION_GET_RESULT = "http://www.opengis.net/def/serviceOperation/sos/resultRetrieval/2.0/GetResult";

    String RESP_ACTION_GET_RESULT =
            "http://www.opengis.net/def/serviceOperation/sos/resultRetrieval/2.0/GetResultResponse";

    String REQ_ACTION_INSERT_RESULT =
            "http://www.opengis.net/def/serviceOperation/sos/resultInsertion/2.0/InsertResult";

    String RESP_ACTION_INSERT_RESULT =
            "http://www.opengis.net/def/serviceOperation/sos/resultInsertion/2.0/InsertResultResponse";

    // GetDataAVailability

    String REQ_ACTION_GET_DATA_AVAILABILITY =
            "http://www.opengis.net/def/serviceOperation/sos/daRetrieval/2.0/GetDataAvailability";

    String RESP_ACTION_GET_DATA_AVAILABILITY  =
            "http://www.opengis.net/def/serviceOperation/sos/daRetrieval/2.0/GetDataAvailabilityResponse";

    // Exceptions

    /**
     * SWES exception response action URI
     */
    String RESP_ACTION_SWES = "http://www.opengis.net/swes/2.0/Exception";

    /**
     * SOS exception response action URI
     */
    String RESP_ACTION_SOS = "http://www.opengis.net/def/serviceOperation/sos/core/2.0/Exception";

    /**
     * OWS exception response action URI
     */
    String RESP_ACTION_OWS = "http://www.opengis.net/ows/1.1/Exception";

    /**
     * SOAP exception response action URI
     */
    String RESP_ACTION_SOAP = "http://www.w3.org/2005/08/addressing/soap/fault";

    /**
     * WSA exception response action URI
     */
    String RESP_ACTION_WSA = "http://www.w3.org/2005/08/addressing/fault";

    /**
     * WSN exception response action URI
     */
    String RESP_ACTION_WSN = "http://docs.oasis-open.org/wsn/fault";

}
