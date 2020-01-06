/*
 * Copyright 2015-2020 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.ogc.ows;

import java.util.Optional;

import javax.xml.namespace.QName;

import org.n52.shetland.ogc.ows.exception.OwsExceptionCode;
import org.n52.shetland.w3c.SchemaLocation;

/**
 * Constants for OWS.
 *
 * @since 1.0.0
 */
public interface OWSConstants {

    // fixme: add version...
    String NS_OWS = "http://www.opengis.net/ows/1.1";

    String NS_OWS_PREFIX = "ows";

    String SCHEMA_LOCATION_URL_OWS = "http://schemas.opengis.net/ows/1.1.0/owsAll.xsd";

    String SCHEMA_LOCATION_URL_OWS_EXCEPTIONREPORT = "http://schemas.opengis.net/ows/1.1.0/owsExceptionReport.xsd";

    SchemaLocation OWS_110_SCHEMA_LOCATION = new SchemaLocation(NS_OWS, SCHEMA_LOCATION_URL_OWS);

    SchemaLocation OWS_110_EXCEPTION_REPORT_SCHEMA_LOCATION =
            new SchemaLocation(NS_OWS_PREFIX, SCHEMA_LOCATION_URL_OWS_EXCEPTIONREPORT);

    String NS_OWS_100 = "http://www.opengis.net/ows/1.0";

    String SCHEMA_LOCATION_URL_OWS100 = "http://schemas.opengis.net/ows/1.0.0/owsAll.xsd";

    SchemaLocation OWS_100_SCHEMA_LOCATION = new SchemaLocation(NS_OWS_100, SCHEMA_LOCATION_URL_OWS100);

    // exception messages
    String SOAP_REASON_INVALID_PARAMETER_VALUE = "The request contained an invalid parameter value.";

    String SOAP_REASON_INVALID_UPDATE_SEQUENCES =
            "The value of the updateSequence parameter in the GetCapabilities operation request "
            + "was greater than the current value of the service metadata updateSequence number.";

    String SOAP_REASON_MISSING_PARAMETER_VALUE =
            "The request did not include a value for a required parameter and this server "
            + "does not declare a default value for it.";

    String SOAP_REASON_NO_APPLICABLE_CODE = "A server exception was encountered.";

    String SOAP_REASON_NO_DATA_AVAILABLE = "There are no data available.";

    String SOAP_REASON_OPERATION_NOT_SUPPORTED = "The requested operation is not supported by this server.";

    String SOAP_REASON_OPTION_NOT_SUPPORTED =
            "The request included/targeted an option that is not supported by this server.";

    String SOAP_REASON_REQUEST_EXTENSION_NOT_SUPPORTED =
            "The request included an extension that is not supported by this server.";

    String SOAP_REASON_VERSION_NEGOTIATION_FAILED =
            "The list of versions in the ‘AcceptVersions’ parameter value of the GetCapabilities operation "
            + "request did not include any version supported by this server.";

    String SOAP_REASON_UNKNOWN = SOAP_REASON_NO_APPLICABLE_CODE;

    String EN_EXCEPTION = "Exception";

    String EN_EXCEPTION_CODE = "exceptionCode";

    String EN_LOCATOR = "locator";

    String EN_EXCEPTION_TEXT = "ExceptionText";

    QName QN_EXCEPTION = new QName(NS_OWS, EN_EXCEPTION, NS_OWS_PREFIX);

    QName QN_EXCEPTION_TEXT = new QName(NS_OWS, EN_EXCEPTION_TEXT, NS_OWS_PREFIX);

    QName QN_NO_APPLICABLE_CODE = new QName(NS_OWS, OwsExceptionCode.NoApplicableCode.name(), NS_OWS_PREFIX);

    String VERSION = "1.1.0";

    /**
     * enum with parameter names for getCapabilities request
     */
    enum GetCapabilitiesParams {
        Sections,
        AcceptVersions,
        updateSequence,
        AcceptFormats,
        service,
        request,
        Section,
        CapabilitiesId,
        AcceptLanguages;

        public static final String DYNAMIC_CAPABILITIES_IDENTIFIER = "dynamic";

        /**
         * Check if the supplied string represents a constant of this
         * enumeration.
         *
         * @param string
         *            the string value
         *
         * @return wether or not the string represents an enum value
         */
        public static boolean contains(String string) {
            return Enums.contains(GetCapabilitiesParams.class, string);
        }

        /**
         * Get the corresponding enum constant for the supplied string
         * representation.
         *
         * @param string
         *            the string value
         *
         * @return the enum constant
         */
        public static Optional<GetCapabilitiesParams> from(String string) {
            return Enums.fromString(GetCapabilitiesParams.class, string);
        }

    }

    enum CapabilitiesSection {
        All,
        ServiceProvider,
        ServiceIdentification,
        OperationsMetadata,
        Languages,
        Contents;

        /**
         * Check if the supplied string represents a constant of this
         * enumeration.
         *
         * @param string
         *            the string value
         *
         * @return wether or not the string represents an enum value
         */
        public static boolean contains(String string) {
            return Enums.contains(CapabilitiesSection.class, string);
        }

        /**
         * Get the corresponding enum constant for the supplied string
         * representation.
         *
         * @param string
         *            the string value
         *
         * @return the enum constant
         */
        public static Optional<CapabilitiesSection> from(String string) {
            return Enums.fromString(CapabilitiesSection.class, string);
        }
    }

    /**
     * the names of the operations supported by all versions of the service
     * specification
     */
    enum Operations {
        GetCapabilities;
    }

    /** enum with names of get request parameters for all requests */
    enum RequestParams {
        request,
        service,
        version;

        /**
         * Check if the supplied string represents a constant of this
         * enumeration.
         *
         * @param string
         *            the string value
         *
         * @return wether or not the string represents an enum value
         */
        public static boolean contains(String string) {
            return Enums.contains(RequestParams.class, string);
        }

        /**
         * Get the corresponding enum constant for the supplied string
         * representation.
         *
         * @param string
         *            the string value
         *
         * @return the enum constant
         */
        public static Optional<RequestParams> from(String string) {
            return Enums.fromString(RequestParams.class, string);
        }
    }

    /** enum with names of get request parameters for all requests */
    enum AdditionalRequestParams {
        language,
        crs,
        returnHumanReadableIdentifier;

        /**
         * Check if the supplied string represents a constant of this
         * enumeration.
         *
         * @param string
         *            the string value
         *
         * @return wether or not the string represents an enum value
         */
        public static boolean contains(String string) {
            return Enums.contains(AdditionalRequestParams.class, string);
        }

        /**
         * Get the corresponding enum constant for the supplied string
         * representation.
         *
         * @param string
         *            the string value
         *
         * @return the enum constant
         */
        public static Optional<AdditionalRequestParams> from(String string) {
            return Enums.fromString(AdditionalRequestParams.class, string);
        }
    }

}
