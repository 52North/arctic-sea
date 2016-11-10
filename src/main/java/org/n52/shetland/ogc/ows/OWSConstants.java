/*
 * Copyright 2016 52°North Initiative for Geospatial Open Source
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

import org.n52.shetland.ogc.ows.exception.InvalidParameterValueException;
import org.n52.shetland.ogc.ows.exception.OwsExceptionCode;
import org.n52.shetland.w3c.SchemaLocation;

/**
 * Constants for OWS.
 *
 * @since 1.0.0
 */
public interface OWSConstants {

    //fixme: add version...
    String NS_OWS = "http://www.opengis.net/ows/1.1";

    String NS_OWS_PREFIX = "ows";

    String SCHEMA_LOCATION_URL_OWS = "http://schemas.opengis.net/ows/1.1.0/owsAll.xsd";

    String SCHEMA_LOCATION_URL_OWS_EXCEPTIONREPORT = "http://schemas.opengis.net/ows/1.1.0/owsExceptionReport.xsd";

    SchemaLocation OWS_110_SCHEMA_LOCATION = new SchemaLocation(NS_OWS, SCHEMA_LOCATION_URL_OWS);

    SchemaLocation OWS_110_EXCEPTION_REPORT_SCHEMA_LOCATION = new SchemaLocation(NS_OWS_PREFIX,
            SCHEMA_LOCATION_URL_OWS_EXCEPTIONREPORT);

    String NS_OWS_100 = "http://www.opengis.net/ows/1.0";

    String SCHEMA_LOCATION_URL_OWS100 = "http://schemas.opengis.net/ows/1.0.0/owsAll.xsd";

    SchemaLocation OWS_100_SCHEMA_LOCATION = new SchemaLocation(NS_OWS_100, SCHEMA_LOCATION_URL_OWS100);

    // exception messages
    String SOAP_REASON_INVALID_PARAMETER_VALUE = "The request contained an invalid parameter value.";

    String SOAP_REASON_INVALID_UPDATE_SEQUENCES
            = "The value of the updateSequence parameter in the GetCapabilities operation request was greater than the current value of the service metadata updateSequence number.";

    String SOAP_REASON_MISSING_PARAMETER_VALUE
            = "The request did not include a value for a required parameter and this server does not declare a default value for it.";

    String SOAP_REASON_NO_APPLICABLE_CODE = "A server exception was encountered.";

    String SOAP_REASON_NO_DATA_AVAILABLE = "There are no data available.";

    String SOAP_REASON_OPERATION_NOT_SUPPORTED = "The requested operation is not supported by this server.";

    String SOAP_REASON_OPTION_NOT_SUPPORTED =
            "The request included/targeted an option that is not supported by this server.";

    String SOAP_REASON_REQUEST_EXTENSION_NOT_SUPPORTED =
            "The request included an extension that is not supported by this server.";

    String SOAP_REASON_VERSION_NEGOTIATION_FAILED =
            "The list of versions in the ‘AcceptVersions’ parameter value of the GetCapabilities operation request did not include any version supported by this server.";

    String SOAP_REASON_RESPONSE_EXCEEDS_SIZE_LIMIT =
            "The requested result set exceeds the response size limit of this service and thus cannot be delivered.";

    @Deprecated // SOS-specific
    String SOAP_REASON_INVALID_PROPERTY_OFFERING_COMBINATION =
            "Observations for the requested combination of observedProperty and offering do not use SWE Common encoded results.";

    String SOAP_REASON_UNKNOWN = "A server exception was encountered.";

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
    }

    enum CapabilitiesSection {
        All,
        ServiceProvider,
        ServiceIdentification,
        OperationsMetadata,
        Languages,
        Contents;

        public static Optional<CapabilitiesSection> fromString(String string) {
            for (CapabilitiesSection section : values()) {
                if (section.toString().equalsIgnoreCase(string)) {
                    return Optional.of(section);
                }
            }
            return Optional.empty();
        }
    }

    /**
     * the names of the operations supported by all versions of the service
     * specification
     */
    enum Operations {
        GetCapabilities;
    }

    /**
     * Enumeration for related feature role
     *
     */
    @Deprecated //SOS-specific
    enum RelatedFeatureRole {
        featureOfInterestID, relatedFeatureID
    }

    /** enum with names of get request parameters for all requests */
    enum RequestParams {
        request, service, version;

        /**
         * method checks whether the string parameter is contained in this
         * enumeration
         *
         * @param s
         *            the name which should be checked
         * @return true if the name is contained in the enumeration
         */
        public static boolean contains(String s) {
            for (Enum<?> p : values()) {
                if (p.name().equals(s)) {
                    return true;
                }
            }
            return false;
        }
    }

    /** enum with names of get request parameters for all requests */
    enum AdditionalRequestParams {
        language, crs, returnHumanReadableIdentifier;

        /**
         * method checks whether the string parameter is contained in this
         * enumeration
         *
         * @param s
         *            the name which should be checked
         * @return true if the name is contained in the enumeration
         */
        public static boolean contains(String s) {
            for (Enum<?> p : values()) {
                if (p.name().equals(s)) {
                    return true;
                }
            }
            return false;
        }
    }



}
