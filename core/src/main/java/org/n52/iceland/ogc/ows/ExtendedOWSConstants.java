/*
 * Copyright 2015-2016 52°North Initiative for Geospatial Open Source
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
package org.n52.iceland.ogc.ows;

import org.n52.iceland.exception.ows.InvalidParameterValueException;
import org.n52.iceland.w3c.SchemaLocation;

/**
 * Constants for OWS.
 *
 * @since 1.0.0
 */
public interface ExtendedOWSConstants extends OWSConstants  {

    SchemaLocation OWS_110_SCHEMA_LOCATION = new SchemaLocation(NS_OWS, SCHEMA_LOCATION_URL_OWS);

    SchemaLocation OWS_110_EXCEPTION_REPORT_SCHEMA_LOCATION = new SchemaLocation(NS_OWS_PREFIX,
            SCHEMA_LOCATION_URL_OWS_EXCEPTIONREPORT);

    SchemaLocation OWS_100_SCHEMA_LOCATION = new SchemaLocation(NS_OWS_100, SCHEMA_LOCATION_URL_OWS100);


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
        CapabilitiesId;

        public static final String DYNAMIC_CAPABILITIES_IDENTIFIER = "dynamic";
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

    @Deprecated //SOS-specific
    enum ExtendedIndeterminateTime {
        first, latest;

        private static final String GET_FIRST = "getFirst";

        public static boolean contains(final String timeString) {
            return timeString.equalsIgnoreCase(first.name()) || timeString.equalsIgnoreCase(latest.name()) || timeString.equalsIgnoreCase(GET_FIRST);
        }

        public static ExtendedIndeterminateTime getEnumForString(final String value) {
            for (ExtendedIndeterminateTime sit : values()) {
                if (sit.name().equalsIgnoreCase(value) || (GET_FIRST.equalsIgnoreCase(value) && sit.equals(first))) {
                    return sit;
                }
            }
            return null;
        }
    }


    /**
     * Interface to identify if the implemented class supportes
     * {@link Extensions}
     *
     * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
     * @since 1.0.0
     *
     * @param <T>
     */
    interface HasExtension<T> {
        /**
         * Get the {@link Extension}s
         *
         * @return {@link Extensions} with {@link Extension}s
         */
        Extensions getExtensions();

        /**
         * Set the {@link Extensions} object
         *
         * @param extensions
         *            the {@link Extensions} object to set
         * @return this
         */
        T setExtensions(final Extensions extensions);

        /**
         * Add a {@link Extensions} to this object
         *
         * @param extension
         *            the {@link Extensions} to add
         * @return this
         */
        T addExtensions(final Extensions extension);

        /**
         * Add a {@link Extension} to this object
         *
         * @param extension
         *            the {@link Extension} to add
         * @return this
         */
        @SuppressWarnings("rawtypes")
        T addExtension(final Extension extension);

        /**
         * Check if {@link Extension}s are set
         *
         * @return <code>true</code>, if {@link Extensions} is not null or
         *         empty
         */
        boolean isSetExtensions();

        /**
         * Check if {@link Extension} for identifier is set
         *
         * @param identifier
         *            Identifier to check
         * @return <code>true</code>, if {@link Extensions} is available for the
         *         identifier
         */
        boolean hasExtension(Enum<?> identifier);

        /**
         * Check if {@link Extension} for identifier is set
         *
         * @param identifier
         *            Identifier to check
         * @return <code>true</code>, if {@link Extensions} is available for the
         *         identifier
         */
        boolean hasExtension(String identifier);

        /**
         * Get {@link Extension} for identifier
         *
         * @param identifier
         *            Identifier to get {@link Extension} for
         * @return The requested {@link Extension}
         * @throws InvalidParameterValueException
         *             If an error occurs
         */
        Extension<?> getExtension(Enum<?> identifier) throws InvalidParameterValueException;

        /**
         * Get {@link Extension} for identifier
         *
         * @param identifier
         *            Identifier to get {@link Extension} for
         * @return The requested {@link Extension}
         * @throws InvalidParameterValueException
         *             If an error occurs
         */
        Extension<?> getExtension(String identifier) throws InvalidParameterValueException;
    }

}
