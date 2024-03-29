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
package org.n52.shetland.inspire;

import javax.xml.namespace.QName;

import org.n52.shetland.w3c.SchemaLocation;

/**
 * Constants interface for INSPIRE. Provides element names, prefixes, namespaces, {@link SchemaLocation},
 * {@link QName}
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public interface InspireConstants {

    String INSPIRE = "INSPIRE";

    String NS_INSPIRE_COMMON = "http://inspire.ec.europa.eu/schemas/common/1.0";

    String NS_INSPIRE_COMMON_PREFIX = "inspire_common";

    String SCHEMA_LOCATION_URL_INSPIRE_COMMON = "http://inspire.ec.europa.eu/schemas/common/1.0/common.xsd";

    SchemaLocation INSPIRE_COMMON_10_SCHEMA_LOCATION =
            new SchemaLocation(NS_INSPIRE_COMMON, SCHEMA_LOCATION_URL_INSPIRE_COMMON);

    String NS_INSPIRE_DLS = "http://inspire.ec.europa.eu/schemas/inspire_dls/1.0";

    String NS_INSPIRE_DLS_PREFIX = "inspire_dls";

    String SCHEMA_LOCATION_URL_INSPIRE_DLS = "http://inspire.ec.europa.eu/schemas/inspire_dls/1.0/inspire_dls.xsd";

    SchemaLocation INSPIRE_DLS_10_SCHEMA_LOCATION =
            new SchemaLocation(NS_INSPIRE_DLS, SCHEMA_LOCATION_URL_INSPIRE_DLS);

    /*
     * element/attribute names (EL_/AN_)
     */

    String EN_EXTENDED_CAPABILITIES = "ExtendedCapabilities";

    String EN_METADATA_URL = "MetadataUrl";

    String EN_URL = "URL";

    String EN_MEDIA_TYPE = "MediaType";

    String EN_SUPPORTED_LANGUAGES = "SupportedLanguages";

    String EN_SUPPORTED_LANGUAGE = "SupportedLanguage";

    String EN_DEFAULT_LANGUAGE = "DefaultLanguage";

    String EN_LANGUAGE = "Language";

    String EN_RESPONSE_LANGUAGE = "ResponseLanguage";

    String EN_SUPPORTED_CRS = "SupportedCRS";

    String EN_DEFAULT_CRS = "DefaultCRS";

    String EN_OTHER_CRS = "OtherCRS";

    String EN_CRS = "CRS";

    String EN_SPATIAL_DATASET_IDENTIFIER = "SpatialDataSetIdentifier";

    String EN_CODE = "Code";

    String EN_NAMESPACE = "Namespace";

    String EN_RESOURCE_TYPE = "ResourceType";

    String EN_RESOURCE_LOCATOR = "ResourceLocator";

    String EN_SPATIAL_DATA_SERVICE_TYPE = "SpatialDataServiceType";

    String EN_TEMPORAL_REFERENCE = "TemporalReference";

    String EN_CONFORMITY = "Conformity";

    String EN_SPECIFICATION = "Specification";

    String EN_DATE_OF_PUBLICATION = "DateOfPublication";

    String EN_DATE_OF_CREATION = "DateOfCreation";

    String EN_DATE_OF_LAST_REVISION = "DateOfLastRevision";

    String EN_DEGREE = "Degree";

    String EN_METADATA_POINT_OF_CONTACT = "MetadataPointOfContact";

    String EN_METADATA_DATE = "MetadataDate";

    String EN_MANDATORY_KEYWORD = "MandatoryKeyword";

    String EN_KEYWORD_VALUE = "KeywordValue";

    String EN_KEYWORD = "Keyword";

    String EN_ORIGINATING_CONTROLLED_VOCABULARY = "OriginatingControlledVocabulary";

    String EN_TITLE = "Title";

    String EN_URI = "URI";

    String EN_TEMPORAL_EXTENT = "TemporalExtent";

    String EN_INDIVIDUAL_DATE = "IndividualDate";

    String EN_INTERVAL_OF_DATES = "IntervalOfDates";

    String EN_STARTING_DATE = "StartingDate";

    String EN_END_DATE = "EndDate";

    String EN_ORGANISATION_NAME = "OrganisationName";

    String EN_EMAIL_ADDRESS = "EmailAddress";

    /*
     * QNames (QN_)
     */
    QName QN_EXTENDED_CAPABILITIES = new QName(NS_INSPIRE_DLS, EN_EXTENDED_CAPABILITIES, NS_INSPIRE_DLS_PREFIX);

    QName QN_METADATA_URL = new QName(NS_INSPIRE_COMMON, EN_METADATA_URL, NS_INSPIRE_COMMON_PREFIX);

    QName QN_URL = new QName(NS_INSPIRE_COMMON, EN_URL, NS_INSPIRE_COMMON_PREFIX);

    QName QN_MEDIA_TYPE = new QName(NS_INSPIRE_COMMON, EN_MEDIA_TYPE, NS_INSPIRE_COMMON_PREFIX);

    QName QN_SUPPORTED_LANGUAGES = new QName(NS_INSPIRE_COMMON, EN_SUPPORTED_LANGUAGES, NS_INSPIRE_COMMON_PREFIX);

    QName QN_SUPPORTED_LANGUAGE = new QName(NS_INSPIRE_COMMON, EN_SUPPORTED_LANGUAGE, NS_INSPIRE_COMMON_PREFIX);

    QName QN_DEFAULT_LANGUAGE = new QName(NS_INSPIRE_COMMON, EN_DEFAULT_LANGUAGE, NS_INSPIRE_COMMON_PREFIX);

    QName QN_LANGUAGE = new QName(NS_INSPIRE_COMMON, EN_LANGUAGE, NS_INSPIRE_COMMON_PREFIX);

    QName QN_RESPONSE_LANGUAGE = new QName(NS_INSPIRE_COMMON, EN_RESPONSE_LANGUAGE, NS_INSPIRE_COMMON_PREFIX);

    QName QN_SUPPORTED_CRS = new QName(NS_INSPIRE_DLS, EN_SUPPORTED_CRS, NS_INSPIRE_DLS_PREFIX);

    QName QN_OTHER_CRS = new QName(NS_INSPIRE_DLS, EN_OTHER_CRS, NS_INSPIRE_DLS_PREFIX);

    QName QN_DEFAULT_CRS = new QName(NS_INSPIRE_DLS, EN_DEFAULT_CRS, NS_INSPIRE_DLS_PREFIX);

    QName QN_CRS = new QName(NS_INSPIRE_DLS, EN_CRS, NS_INSPIRE_DLS_PREFIX);

    QName QN_SPATIAL_DATASET_IDENTIFIER =
            new QName(NS_INSPIRE_DLS, EN_SPATIAL_DATASET_IDENTIFIER, NS_INSPIRE_DLS_PREFIX);

    QName QN_CODE = new QName(NS_INSPIRE_COMMON, EN_CODE, NS_INSPIRE_COMMON_PREFIX);

    QName QN_NAMESPACE = new QName(NS_INSPIRE_COMMON, EN_NAMESPACE, NS_INSPIRE_COMMON_PREFIX);

    QName QN_RESOURCE_LOCATOR = new QName(NS_INSPIRE_COMMON, EN_RESOURCE_LOCATOR, NS_INSPIRE_COMMON_PREFIX);

    QName QN_RESOURCE_TYPE = new QName(NS_INSPIRE_COMMON, EN_RESOURCE_TYPE, NS_INSPIRE_COMMON_PREFIX);

    QName QN_SPATIAL_DATA_SERVICE_TYPE =
            new QName(NS_INSPIRE_COMMON, EN_SPATIAL_DATA_SERVICE_TYPE, NS_INSPIRE_COMMON_PREFIX);

    QName QN_TEMPORAL_REFERENCE = new QName(NS_INSPIRE_COMMON, EN_TEMPORAL_REFERENCE, NS_INSPIRE_COMMON_PREFIX);

    QName QN_CONFORMITY = new QName(NS_INSPIRE_COMMON, EN_CONFORMITY, NS_INSPIRE_COMMON_PREFIX);

    QName QN_SPECIFICATION = new QName(NS_INSPIRE_COMMON, EN_SPECIFICATION, NS_INSPIRE_COMMON_PREFIX);

    QName QN_DATE_OF_PUBLICATION = new QName(NS_INSPIRE_COMMON, EN_DATE_OF_PUBLICATION, NS_INSPIRE_COMMON_PREFIX);

    QName QN_DATE_OF_CREATION = new QName(NS_INSPIRE_COMMON, EN_DATE_OF_CREATION, NS_INSPIRE_COMMON_PREFIX);

    QName QN_DATE_OF_LAST_REVISION = new QName(NS_INSPIRE_COMMON, EN_DATE_OF_LAST_REVISION, NS_INSPIRE_COMMON_PREFIX);

    QName QN_DEGREE = new QName(NS_INSPIRE_COMMON, EN_DEGREE, NS_INSPIRE_COMMON_PREFIX);

    QName QN_METADATA_POINT_OF_CONTACT =
            new QName(NS_INSPIRE_COMMON, EN_METADATA_POINT_OF_CONTACT, NS_INSPIRE_COMMON_PREFIX);

    QName QN_METADATA_DATE = new QName(NS_INSPIRE_COMMON, EN_METADATA_DATE, NS_INSPIRE_COMMON_PREFIX);

    QName QN_MANDATORY_KEYWORD = new QName(NS_INSPIRE_COMMON, EN_MANDATORY_KEYWORD, NS_INSPIRE_COMMON_PREFIX);

    QName QN_KEYWORD_VALUE = new QName(NS_INSPIRE_COMMON, EN_KEYWORD_VALUE, NS_INSPIRE_COMMON_PREFIX);

    QName QN_KEYWORD = new QName(NS_INSPIRE_COMMON, EN_KEYWORD, NS_INSPIRE_COMMON_PREFIX);

    QName QN_ORIGINATING_CONTROLLED_VOCABULARY = new QName(NS_INSPIRE_COMMON, EN_KEYWORD, NS_INSPIRE_COMMON_PREFIX);

    QName QN_TITLE = new QName(NS_INSPIRE_COMMON, EN_TITLE, NS_INSPIRE_COMMON_PREFIX);

    QName QN_URI = new QName(NS_INSPIRE_COMMON, EN_URI, NS_INSPIRE_COMMON_PREFIX);

    QName QN_TEMPORAL_EXTENT = new QName(NS_INSPIRE_COMMON, EN_TEMPORAL_EXTENT, NS_INSPIRE_COMMON_PREFIX);

    QName QN_INDIVIDUAL_DATE = new QName(NS_INSPIRE_COMMON, EN_INDIVIDUAL_DATE, NS_INSPIRE_COMMON_PREFIX);

    QName QN_INTERVAL_OF_DATES = new QName(NS_INSPIRE_COMMON, EN_INTERVAL_OF_DATES, NS_INSPIRE_COMMON_PREFIX);

    QName QN_STARTING_DATE = new QName(NS_INSPIRE_COMMON, EN_STARTING_DATE, NS_INSPIRE_COMMON_PREFIX);

    QName QN_END_DATE = new QName(NS_INSPIRE_COMMON, EN_END_DATE, NS_INSPIRE_COMMON_PREFIX);

    QName QN_ORGANISATION_NAME = new QName(NS_INSPIRE_COMMON, EN_ORGANISATION_NAME, NS_INSPIRE_COMMON_PREFIX);

    QName QN_EMAIL_ADDRESS = new QName(NS_INSPIRE_COMMON, EN_EMAIL_ADDRESS, NS_INSPIRE_COMMON_PREFIX);

}
