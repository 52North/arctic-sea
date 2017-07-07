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
package org.n52.svalbard.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPConstants;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlCursor.TokenType;
import org.apache.xmlbeans.XmlObject;

import org.n52.shetland.ogc.OGCConstants;
import org.n52.shetland.ogc.gml.GmlConstants;
import org.n52.shetland.ogc.om.OmConstants;
import org.n52.shetland.ogc.om.features.SfConstants;
import org.n52.shetland.ogc.ows.OWSConstants;
import org.n52.shetland.ogc.sensorML.SensorMLConstants;
import org.n52.shetland.ogc.sos.Sos1Constants;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.swe.SweConstants;
import org.n52.shetland.ogc.swes.SwesConstants;
import org.n52.shetland.w3c.SchemaLocation;
import org.n52.shetland.w3c.W3CConstants;

import com.google.common.collect.Sets;

/**
 * Utility class for 52N
 *
 * @since 1.0.0
 *
 */
public final class N52XmlHelper {
    public static final SchemaLocation SCHEMA_LOCATION_OM_100 =
            new SchemaLocation(OmConstants.NS_OM, OmConstants.SCHEMA_LOCATION_URL_OM_CONSTRAINT);

    public static final SchemaLocation SCHEMA_LOCATION_SOAP_12 =
            new SchemaLocation(SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE, SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE);

    private N52XmlHelper() {
    }

    /**
     * Sets the schema location to a XmlObject
     *
     * @param document
     *            XML document
     * @param schemaLocations
     *            schema location
     */
    public static void setSchemaLocationToDocument(XmlObject document, String schemaLocations) {
        XmlCursor cursor = document.newCursor();
        if (cursor.toFirstChild()) {
            cursor.setAttributeText(getSchemaLocationQName(), schemaLocations);
        }
        cursor.dispose();
    }

    /**
     * Sets the schema locations to a XmlObject
     *
     * @param document
     *            XML document
     * @param schemaLocations
     *            List of schema locations
     */
    public static void setSchemaLocationsToDocument(XmlObject document, Collection<SchemaLocation> schemaLocations) {
        setSchemaLocationToDocument(document, mergeSchemaLocationsToString(schemaLocations));
    }

    public static String mergeSchemaLocationsToString(Iterable<SchemaLocation> schemaLocations) {
        if (schemaLocations != null) {
            Iterator<SchemaLocation> it = schemaLocations.iterator();
            if (it.hasNext()) {
                StringBuilder builder = new StringBuilder();
                builder.append(it.next().getSchemaLocationString());
                while (it.hasNext()) {
                    builder.append(" ").append(it.next().getSchemaLocationString());
                }
                return builder.toString();
            }
        }
        return "";
    }

    public static Set<String> getNamespaces(XmlObject xmlObject) {
        Set<String> namespaces = Sets.newHashSet();
        XmlCursor newCursor = xmlObject.newCursor();
        while (newCursor.hasNextToken()) {
            TokenType evt = newCursor.toNextToken();
            if (evt == TokenType.START) {
                QName qn = newCursor.getName();
                if (qn != null) {
                    namespaces.add(qn.getNamespaceURI());
                }
            }
        }
        return namespaces;
    }

    /**
     * W3C XSI schema location
     *
     * @return QName of schema location
     */
    public static QName getSchemaLocationQName() {
        return W3CConstants.QN_SCHEMA_LOCATION;
    }

    /**
     * W3C XSI schema location with prefix
     *
     * @return QName of schema location
     */
    public static QName getSchemaLocationQNameWithPrefix() {
        return W3CConstants.QN_SCHEMA_LOCATION_PREFIXED;
    }

    /**
     * SOS 1.0.0 schema location
     *
     * @return QName of schema location
     */
    public static SchemaLocation getSchemaLocationForSOS100() {
        return Sos1Constants.SOS1_SCHEMA_LOCATION;
    }

    /**
     * SOS 2.0 schema location
     *
     * @return QName of schema location
     */
    public static SchemaLocation getSchemaLocationForSOS200() {
        return Sos2Constants.SOS_SCHEMA_LOCATION;
    }

    /**
     * OM 1.0.0 schema location
     *
     * @return QName of schema location
     */
    public static SchemaLocation getSchemaLocationForOM100() {
        return SCHEMA_LOCATION_OM_100;
    }

    /**
     * OM 2.0 schema location
     *
     * @return QName of schema location
     */
    public static SchemaLocation getSchemaLocationForOM200() {
        return OmConstants.OM_20_SCHEMA_LOCATION;
    }

    /**
     * GML 3.1.1 schema location
     *
     * @return QName of schema location
     */
    public static SchemaLocation getSchemaLocationForGML311() {
        return GmlConstants.GML_311_SCHEMAL_LOCATION;
    }

    /**
     * GML 3.2.1 schema location
     *
     * @return QName of schema location
     */
    public static SchemaLocation getSchemaLocationForGML321() {
        return GmlConstants.GML_32_SCHEMAL_LOCATION;
    }

    /**
     * SOS OGC schema location
     *
     * @return QName of schema location
     */
    public static SchemaLocation getSchemaLocationForOGC() {
        return OGCConstants.OGC_SCHEMA_LOCATION;
    }

    /**
     * OWS 1.1.0 schema location
     *
     * @return QName of schema location
     */
    public static SchemaLocation getSchemaLocationForOWS110() {
        return OWSConstants.OWS_110_SCHEMA_LOCATION;
    }

    /**
     * OWS 1.1.0 schema location
     *
     * @return QName of schema location
     */
    public static SchemaLocation getSchemaLocationForOWS110Exception() {
        return OWSConstants.OWS_110_EXCEPTION_REPORT_SCHEMA_LOCATION;
    }

    /**
     * Sampling 1.0.0 schema location
     *
     * @return QName of schema location
     */
    public static SchemaLocation getSchemaLocationForSA100() {
        return SfConstants.SA_SCHEMA_LOCATION;
    }

    /**
     * Sampling 2.0 schema location
     *
     * @return QName of schema location
     */
    public static SchemaLocation getSchemaLocationForSF200() {
        return SfConstants.SF_SCHEMA_LOCATION;
    }

    /**
     * SamplingSpatial 2.0 schema location
     *
     * @return QName of schema location
     */
    public static SchemaLocation getSchemaLocationForSAMS200() {
        return SfConstants.SAMS_SCHEMA_LOCATION;
    }

    /**
     * SensorML 1.0.1 schema location
     *
     * @return QName of schema location
     */
    public static SchemaLocation getSchemaLocationForSML101() {
        return SensorMLConstants.SML_101_SCHEMA_LOCATION;
    }

    /**
     * SWECommon 1.0.1 schema location
     *
     * @return QName of schema location
     */
    public static SchemaLocation getSchemaLocationForSWE101() {
        return SweConstants.SWE_101_SCHEMA_LOCATION;
    }

    /**
     * SWECommon 2.0 schema location
     *
     * @return QName of schema location
     */
    public static SchemaLocation getSchemaLocationForSWE200() {
        return SweConstants.SWE_20_SCHEMA_LOCATION;
    }

    /**
     * SWECommon 2.0 schema location
     *
     * @return QName of schema location
     */
    public static SchemaLocation getSchemaLocationForSWES200() {
        return SwesConstants.SWES_20_SCHEMA_LOCATION;
    }

    /**
     * W3C XLINK schema location
     *
     * @return QName of schema location
     */
    public static SchemaLocation getSchemaLocationForXLINK() {
        return W3CConstants.XLINK_SCHEMA_LOCATION;
    }

    public static SchemaLocation getSchemaLocationForSOAP12() {
        return SCHEMA_LOCATION_SOAP_12;
    }

}
