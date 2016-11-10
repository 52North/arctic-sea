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
package org.n52.shetland.ogc.sensorML;

import javax.xml.namespace.QName;

import org.n52.shetland.util.http.MediaType;
import org.n52.shetland.w3c.SchemaLocation;

/**
 * Constants class for SensorML
 *
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk
 *         J&uuml;rrens</a>
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 * @author ShaneStClair
 *
 * @since 4.0.0
 */
public interface SensorMLConstants {

    // namespaces and schema locations
    String NS_SML = "http://www.opengis.net/sensorML/1.0.1";

    String NS_SML_PREFIX = "sml";

    String SCHEMA_LOCATION_URL_SML_101 = "http://schemas.opengis.net/sensorML/1.0.1/sensorML.xsd";

    SchemaLocation SML_101_SCHEMA_LOCATION = new SchemaLocation(NS_SML, SCHEMA_LOCATION_URL_SML_101);

    String SENSORML_OUTPUT_FORMAT_URL = NS_SML;

    MediaType SENSORML_CONTENT_TYPE = new MediaType("text", "xml", "subtype", "sensorML/1.0.1");

    String SENSORML_OUTPUT_FORMAT_MIME_TYPE = SENSORML_CONTENT_TYPE.toString();

    String EN_SYSTEM = "System";

    String EN_PROCESS_MODEL = "ProcessModel";

    String EN_COMPONENT = "Component";

    String EN_ABSTRACT_PROCESS = "AbstractProcess";

    QName SYSTEM_QNAME = new QName(NS_SML, EN_SYSTEM, NS_SML_PREFIX);

    QName PROCESS_MODEL_QNAME = new QName(NS_SML, EN_PROCESS_MODEL, NS_SML_PREFIX);

    QName COMPONENT_QNAME = new QName(NS_SML, EN_COMPONENT, NS_SML_PREFIX);

    QName ABSTRACT_PROCESS_QNAME = new QName(NS_SML, EN_ABSTRACT_PROCESS, NS_SML_PREFIX);

    String VERSION_V101 = "1.0.1";

    /**
     * Name of a SensorML element describing the offerings a procedure/sensor is
     * related to or should be inserted into
     */
    String ELEMENT_NAME_OFFERINGS = "offerings";

    // FIXME use a proper URI/URN for this, e.g. from settings
    String OFFERING_FIELD_DEFINITION = "http://www.opengis.net/def/offering/identifier";

    /**
     * name of System capabilities containing parent procedures
     */
    String ELEMENT_NAME_PARENT_PROCEDURES = "parentProcedures";

    // FIXME use a proper URI/URN for this, e.g. from settings
    String PARENT_PROCEDURE_FIELD_DEFINITION = "http://www.opengis.net/def/procedure/identifier";

    String PARENT_PROCEDURE_FIELD_NAME = "parentProcedureID";

    /**
     * name of System components containing child procedures
     */
    String ELEMENT_NAME_CHILD_PROCEDURES = "childProcedure";

    /**
     * name of System capabilities containing featureOfInterest
     */
    String ELEMENT_NAME_FEATURES_OF_INTEREST = "featuresOfInterest";

    // FIXME use a proper URI/URN for this, e.g. from settings
    String FEATURE_OF_INTEREST_FIELD_DEFINITION = "http://www.opengis.net/def/featureOfInterest/identifier";

    String FEATURE_OF_INTEREST_FIELD_NAME = "featureOfInterestID";

    String ELEMENT_NAME_SHORT_NAME = "shortName";

    String ELEMENT_NAME_LONG_NAME = "longName";

    String ELEMENT_NAME_OBSERVED_BBOX = "observedBBOX";

    String OBSERVED_BBOX_DEFINITION_URN = "urn:ogc:def:property:OGC:1.0:observedBBOX";

    String DEFAULT_FIELD_NAME = "field_";
}
