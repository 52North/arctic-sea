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
package org.n52.shetland.w3c.soap;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPConstants;

import org.n52.shetland.w3c.SchemaLocation;

/**
 * Constants for SOAP messages
 *
 * @since 1.0.0
 */
public interface SoapConstants {
    /**
     * SOAP envelope locator
     */
    String LOCATOR_SOAP_ENVELOPE = "SOAPEnvelope";

    /**
     * SOAP header locator
     */
    String LOCATOR_SOAP_HEADER = "SOAPHeader";

    /**
     * SOAP body locator
     */
    String LOCATOR_SOAP_BODY = "SOAPBody";

    String NS_SOAP_PREFIX = "soap";

    String NS_SOAP_12 = SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE;

    String EN_SOAP_ENVELOPE = "Envelope";

    String EN_SOAP_BODY = "Body";

    QName SOAP_12_ENVELOPE = new QName(NS_SOAP_12, EN_SOAP_ENVELOPE, NS_SOAP_PREFIX);

    QName SOAP_12_BODY = new QName(NS_SOAP_12, EN_SOAP_BODY, NS_SOAP_PREFIX);

    String SCHEMA_LOCATION_URL_SOPA_12 = SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE + "/soap-envelope.xsd";

    SchemaLocation SOAP_12_SCHEMA_LOCATION = new SchemaLocation(NS_SOAP_12, SCHEMA_LOCATION_URL_SOPA_12);
}
