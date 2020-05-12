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
package org.n52.shetland.w3c.soap;

import javax.xml.namespace.QName;

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

    String NS_SOAP_11 = "http://schemas.xmlsoap.org/soap/envelope";

    String NS_SOAP_12 = "http://www.w3.org/2003/05/soap-envelope";

    String SOAP_1_1_VERSION = "SOAP 1.1 Protocol";

    String SOAP_1_2_VERSION = "SOAP 1.2 Protocol";

    String EN_SOAP_ENVELOPE = "Envelope";

    String EN_SOAP_BODY = "Body";

    QName SOAP_11_ENVELOPE = new QName(NS_SOAP_11, EN_SOAP_ENVELOPE, NS_SOAP_PREFIX);

    QName SOAP_11_BODY = new QName(NS_SOAP_11, EN_SOAP_BODY, NS_SOAP_PREFIX);

    String SCHEMA_LOCATION_URL_SOPA_11 = NS_SOAP_11 + "/";

    SchemaLocation SOAP_11_SCHEMA_LOCATION = new SchemaLocation(NS_SOAP_11, SCHEMA_LOCATION_URL_SOPA_11);

    QName SOAP_12_ENVELOPE = new QName(NS_SOAP_12, EN_SOAP_ENVELOPE, NS_SOAP_PREFIX);

    QName SOAP_12_BODY = new QName(NS_SOAP_12, EN_SOAP_BODY, NS_SOAP_PREFIX);

    String SCHEMA_LOCATION_URL_SOPA_12 = NS_SOAP_12 + "/soap-envelope.xsd";

    SchemaLocation SOAP_12_SCHEMA_LOCATION = new SchemaLocation(NS_SOAP_12, SCHEMA_LOCATION_URL_SOPA_12);

    QName VERSIONMISMATCH_FAULT = new QName(NS_SOAP_12, "VersionMismatch", NS_SOAP_PREFIX);

    QName MUSTUNDERSTAND_FAULT = new QName(NS_SOAP_12, "MustUnderstand", NS_SOAP_PREFIX);

    QName DATAENCODINGUNKNOWN_FAULT = new QName(NS_SOAP_12, "DataEncodingUnknown", NS_SOAP_PREFIX);

    QName SENDER_FAULT = new QName(NS_SOAP_12, "Sender", NS_SOAP_PREFIX);

    QName RECEIVER_FAULT = new QName(NS_SOAP_12, "Receiver", NS_SOAP_PREFIX);
}
