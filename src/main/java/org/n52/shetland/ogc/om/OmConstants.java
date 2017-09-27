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
package org.n52.shetland.ogc.om;

import java.util.Set;

import javax.xml.namespace.QName;

import org.n52.janmayen.http.MediaType;
import org.n52.shetland.ogc.OGCConstants;
import org.n52.shetland.w3c.SchemaLocation;

import com.google.common.collect.ImmutableSet;

/**
 * Class contains element names and namespaces used to encode the O&M responses.
 *
 * Interface contains element names and namespaces used to encode the OGC O&M
 * responses.
 *
 * @since 1.0.0
 */
public interface OmConstants {
    String PARAMETER_NOT_SET = "PARAMETER_NOT_SET";

    // //////////////////////////////
    // namespaces and schema locations
    String NS_OM = "http://www.opengis.net/om/1.0";

    String NS_OM_2 = "http://www.opengis.net/om/2.0";

    String NS_OM_PREFIX = "om";

    String NS_GMD = "http://www.isotc211.org/2005/gmd";

    String NS_GMD_PREFIX = "gmd";

    String SCHEMA_LOCATION_URL_OM = "http://schemas.opengis.net/om/1.0.0/om.xsd";

    String SCHEMA_LOCATION_URL_OM_CONSTRAINT =
            "http://schemas.opengis.net/om/1.0.0/extensions/observationSpecialization_constraint.xsd";

    String SCHEMA_LOCATION_URL_OM_20 = "http://schemas.opengis.net/om/2.0/observation.xsd";

    String SCHEMA_LOCATION_URL_OM_20_OM_OBSERVATION = SCHEMA_LOCATION_URL_OM_20 + "#OM_Observation";

    SchemaLocation OM_100_SCHEMA_LOCATION = new SchemaLocation(NS_OM, SCHEMA_LOCATION_URL_OM);

    SchemaLocation OM_20_SCHEMA_LOCATION = new SchemaLocation(NS_OM_2, SCHEMA_LOCATION_URL_OM_20);

    // //////////////////////////////////////////////////////////////////////
    // other
    String AN_ID = "id";

    String TEXT = "text";

    String XML = "xml";

    String SUBTYPE = "subtype";

    MediaType CONTENT_TYPE_OM = new MediaType(TEXT, XML, SUBTYPE, "om/1.0.0");

    MediaType CONTENT_TYPE_OM_2 = new MediaType(TEXT, XML, SUBTYPE, "om/2.0.0");

    String RESPONSE_FORMAT_OM = "http://www.opengis.net/om/1.0.0";

    String RESPONSE_FORMAT_OM_2 = NS_OM_2;

    // ///////////////////////////////////////////////////////////////////
    // names of elements in O&M documents
    String EN_ASCII_BLOCK = "AsciiBlock";

    String EN_ABSTRACT_DATA_GROUP = "_DataGroup";

    String EN_ABSTRACT_DATA_QUALITY = "AbstractDQ_Element";

    String EN_BOUNDED_BY = "boundedBy";

    String EN_CATEGORY_OBSERVATION = "CategoryObservation";

    String EN_COUNT_OBSERVATION = "CountObservation";

    String EN_TEXT_OBSERVATION = "TextObservation";

    String EN_REFERENCE_OBSERVATION = "ReferenceObservation";

    String EN_TRUTH_OBSERVATION = "TruthObservation";

    String EN_GEOMETRY_OBSERVATION = "GeometryObservation";

    String EN_COMPLEX_OBSERVATION = "ComplexObservation";

    String EN_COMMON_OBSERVATION = "CommonObservation";

    String EN_COMPOSITE_PHENOMENON = "CompositePhenomenon";

    String EN_DATA_GROUP = "DataGroup";

    String EN_DQ_QUAN_ATTR_ACC = "DQ_QuantitativeAttributeAccuracy";

    String EN_DQ_NON_QUAN_ATTR_ACC = "DQ_NonQuantitativeAttributeAccuracy";

    String EN_DQ_COMPL_COMM = "DQ_CompletenessCommission";

    String EN_DQ_COMPL_OM = "DQ_CompletenessOmission";

    String EN_FEATURE = "Feature";

    String EN_FEATURE_COLLECTION = "FeatureCollection";

    String EN_GEOREF_FEATURE = "GeoReferenceableFeature";

    String EN_MEMBER = "member";

    String EN_MEASUREMENT = "Measurement";

    String EN_OBSERVED_PROPERTY = "observedProperty";

    String EN_OBSERVATION_COLLECTION = "ObservationCollection";

    String EN_OBSERVATION = "Observation";

    String EN_OM_OBSERVATION = "OM_Observation";

    String EN_TYPE = "type";

    String EN_PHENOMENON = "Phenomenon";

    String EN_COMPOSITE_SURFACE = "CompositeSurface";

    String EN_RESULT = "result";

    String EN_WV_STATION = "WVStation";

    String EN_TEMPORAL_OPS = "temporalOps";

    String EN_PROCEDURE = "procedure";

    String EN_PHENOMENON_TIME = "phenomenonTime";

    String EN_RESULT_TIME = "resultTime";

    String EN_FEATURE_OF_INTEREST = "featureOfInterest";

    String EN_PROCESS = "Process";

    String EN_PARAMETER = "parameter";

    String EN_NAMED_VALUE = "NamedValue";

    String EN_VALID_TIME = "validTime";

    // /////////////////////////////////////////////////////////////////////////////////
    // other constants
    String PHEN_SAMPLING_TIME = "http://www.opengis.net/def/property/OGC/0/SamplingTime";

    String PHENOMENON_TIME = "http://www.opengis.net/def/property/OGC/0/PhenomenonTime";

    String RESULT_TIME = "http://www.opengis.net/def/property/OGC/0/ResultTime";

    String PHENOMENON_TIME_NAME = EN_PHENOMENON_TIME;

    String VALID_TIME_NAME = EN_VALID_TIME;

    String SAMPLING_TIME_NAME = "samplingTime";

    String PHEN_UOM_ISO8601 = "http://www.opengis.net/def/uom/ISO-8601/0/Gregorian";

    String PHEN_FEATURE_OF_INTEREST = "http://www.opengis.net/def/property/OGC/0/FeatureOfInterest";

    String ATTR_SRS_NAME = "srsName";

    String PARAM_NAME_SAMPLING_GEOMETRY = "http://www.opengis.net/def/param-name/OGC-OM/2.0/samplingGeometry";

    String PARAMETER_NAME_DEPTH_URL = "http://www.opengis.net/def/param-name/OGC-OM/2.0/depth";

    String PARAMETER_NAME_HEIGHT_URL = "http://www.opengis.net/def/param-name/OGC-OM/2.0/height";

    String PARAMETER_NAME_DEPTH = "depth";

    String PARAMETER_NAME_HEIGHT = "height";

    String PARAMETER_NAME_ELEVATION = "elevation";

    String PARAMETER_NAME_FROM = "from";

    String PARAMETER_NAME_TO = "to";

    String PARAMETER_NAME_FROM_DEPTH = "fromDepth";

    String PARAMETER_NAME_TO_DEPTH = "toDepth";

    String PARAMETER_NAME_FROM_HEIGHT = "fromHeight";

    String PARAMETER_NAME_TO_HEIGHT = "toHeight";

    String PARAMETER_NAME_FROM_ELEVATION = "fromEelvation";

    String PARAMETER_NAME_TO_ELEVATION = "toElevation";

    String PARAMETER = EN_PARAMETER;

    String OM_PARAMETER = "om:" + PARAMETER;

    // observation types
    String OBS_TYPE_MEASUREMENT = "http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_Measurement";

    String OBS_TYPE_CATEGORY_OBSERVATION =
            "http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_CategoryObservation";

    String OBS_TYPE_COMPLEX_OBSERVATION =
            "http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_ComplexObservation";

    String OBS_TYPE_COUNT_OBSERVATION = "http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_CountObservation";

    String OBS_TYPE_GEOMETRY_OBSERVATION =
            "http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_GeometryObservation";

    String OBS_TYPE_TRUTH_OBSERVATION = "http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_TruthObservation";

    String OBS_TYPE_OBSERVATION = "http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_Observation";

    // coverage observation types
    String OBS_TYPE_DISCRETE_COVERAGE_OBSERVATION =
            "http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_DiscreteCoverageObservation";

    String OBS_TYPE_POINT_COVERAGE_OBSERVATION =
            "http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_PointCoverageObservation";

    String OBS_TYPE_TIME_SERIES_OBSERVATION =
            "http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_TimeSeriesObservation";

    // no Definition in O&M and not in Lightweight Profile
    String OBS_TYPE_TEXT_OBSERVATION = "http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_TextObservation";

    String OBS_TYPE_PROFILE_OBSERVATION =
            "http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_ProfileObservation";

    String OBS_TYPE_UNKNOWN = OGCConstants.UNKNOWN;

    String OBS_TYPE_SWE_ARRAY_OBSERVATION =
            "http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_SWEArrayObservation";

    String OBS_TYPE_REFERENCE_OBSERVATION =
            "http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_ReferenceObservation";

    String OBS_RESULT_TYPE_OBSERVATION = "http://www.opengis.net/sensorML/2.0/DataArray";

    String SAMPLING_FEAT_TYPE_UNKNOWN = "http://www.opengis.net/def/samplingFeatureType/unknown";

    // ////////////////////////////////////////////////////////
    // resultModel constants; not possible to use enum because of
    QName RESULT_MODEL_MEASUREMENT = new QName(NS_OM, EN_MEASUREMENT, NS_OM_PREFIX);

    QName RESULT_MODEL_GEOMETRY_OBSERVATION = new QName(NS_OM, EN_GEOMETRY_OBSERVATION, NS_OM_PREFIX);

    QName RESULT_MODEL_CATEGORY_OBSERVATION = new QName(NS_OM, EN_CATEGORY_OBSERVATION, NS_OM_PREFIX);

    QName RESULT_MODEL_OBSERVATION = new QName(NS_OM, EN_OBSERVATION, NS_OM_PREFIX);

    QName RESULT_MODEL_COUNT_OBSERVATION = new QName(NS_OM, EN_COUNT_OBSERVATION, NS_OM_PREFIX);

    QName RESULT_MODEL_COMPLEX_OBSERVATION = new QName(NS_OM, EN_COMPLEX_OBSERVATION, NS_OM_PREFIX);

    QName RESULT_MODEL_TRUTH_OBSERVATION = new QName(NS_OM, EN_TRUTH_OBSERVATION, NS_OM_PREFIX);

    QName RESULT_MODEL_TEXT_OBSERVATION = new QName(NS_OM, EN_TEXT_OBSERVATION, NS_OM_PREFIX);

    QName RESULT_MODEL_REFERENCE_OBSERVATION = new QName(NS_OM, EN_REFERENCE_OBSERVATION, NS_OM_PREFIX);

    /**
     * Array of constants for result models.
     */
    Set<QName> RESULT_MODELS = ImmutableSet.of(RESULT_MODEL_OBSERVATION, RESULT_MODEL_MEASUREMENT,
            RESULT_MODEL_CATEGORY_OBSERVATION, RESULT_MODEL_GEOMETRY_OBSERVATION);

    String VALUE_REF_OM_OBSERVATION = NS_OM_PREFIX + ":observation";

    QName QN_OM_20_OBSERVATION = new QName(NS_OM_2, EN_OM_OBSERVATION, NS_OM_PREFIX);

    QName QN_OM_20_OBSERVATION_TYPE = new QName(NS_OM_2, EN_TYPE, NS_OM_PREFIX);

    QName QN_OM_20_PHENOMENON_TIME = new QName(NS_OM_2, EN_PHENOMENON_TIME, NS_OM_PREFIX);

    QName QN_OM_20_VALID_TIME = new QName(NS_OM_2, EN_VALID_TIME, NS_OM_PREFIX);

    QName QN_OM_20_RESULT_TIME = new QName(NS_OM_2, EN_RESULT_TIME, NS_OM_PREFIX);

    QName QN_OM_20_PROCEDURE = new QName(NS_OM_2, EN_PROCEDURE, NS_OM_PREFIX);

    QName QN_OM_20_OBSERVED_PROPERTY = new QName(NS_OM_2, EN_OBSERVED_PROPERTY, NS_OM_PREFIX);

    QName QN_OM_20_FEATURE_OF_INTEREST = new QName(NS_OM_2, EN_FEATURE_OF_INTEREST, NS_OM_PREFIX);

    QName QN_OM_20_RESULT = new QName(NS_OM_2, EN_RESULT, NS_OM_PREFIX);

    QName QN_OM_20_PARAMETER = new QName(NS_OM_2, EN_PARAMETER, NS_OM_PREFIX);

    QName QN_OM_20_NAMED_VALUE = new QName(NS_OM_2, EN_NAMED_VALUE, NS_OM_PREFIX);

    ObservationType OBS_TYPE_CATEGORY_OBSERVATION_TYPE = new ObservationType(OBS_TYPE_CATEGORY_OBSERVATION);
    ObservationType OBS_TYPE_COMPLEX_OBSERVATION_TYPE = new ObservationType(OBS_TYPE_COMPLEX_OBSERVATION);
    ObservationType OBS_TYPE_COUNT_OBSERVATION_TYPE = new ObservationType(OBS_TYPE_COUNT_OBSERVATION);
    ObservationType OBS_TYPE_GEOMETRY_OBSERVATION_TYPE = new ObservationType(OBS_TYPE_GEOMETRY_OBSERVATION);
    ObservationType OBS_TYPE_TEXT_OBSERVATION_TYPE = new ObservationType(OBS_TYPE_TEXT_OBSERVATION);
    ObservationType OBS_TYPE_SWE_ARRAY_OBSERVATION_TYPE = new ObservationType(OBS_TYPE_SWE_ARRAY_OBSERVATION);
    ObservationType OBS_TYPE_TRUTH_OBSERVATION_TYPE = new ObservationType(OBS_TYPE_TRUTH_OBSERVATION);
    ObservationType OBS_TYPE_MEASUREMENT_TYPE = new ObservationType(OBS_TYPE_MEASUREMENT);
    ObservationType OBS_TYPE_REFERENCE_OBSERVATION_TYPE = new ObservationType(OBS_TYPE_REFERENCE_OBSERVATION);

    Set<String> OBSERVATION_TYPES =
            ImmutableSet.of(OBS_TYPE_MEASUREMENT,
                    OBS_TYPE_CATEGORY_OBSERVATION,
                    OBS_TYPE_COMPLEX_OBSERVATION,
                    OBS_TYPE_COUNT_OBSERVATION,
                    OBS_TYPE_GEOMETRY_OBSERVATION,
                    OBS_TYPE_TEXT_OBSERVATION,
                    OBS_TYPE_TRUTH_OBSERVATION,
                    OBS_TYPE_SWE_ARRAY_OBSERVATION,
                    OBS_TYPE_PROFILE_OBSERVATION,
                    OBS_TYPE_REFERENCE_OBSERVATION);
}
