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
package org.n52.shetland.w3c.wsdl;

import java.net.URI;

import javax.xml.namespace.QName;

import org.n52.janmayen.http.HTTPMethods;
import org.n52.janmayen.http.MediaTypes;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.sos.SosSoapConstants;

/**
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 7.6.0
 */
public interface WSDLConstants {

    String NS_HTTP = "http://schemas.xmlsoap.org/wsdl/http/";

    String NS_HTTP_PREFIX = "http";

    String NS_MIME = "http://schemas.xmlsoap.org/wsdl/mime/";

    String NS_MIME_PREFIX = "mime";

    String NS_SOAP = "http://schemas.xmlsoap.org/wsdl/soap/";

    String NS_SOAP_PREFIX = "soap";

    String NS_SOAP_12 = "http://schemas.xmlsoap.org/wsdl/soap12/";

    String NS_SOAP_12_PREFIX = "soap12";

    String NS_SOSW = "http://www.opengis.net/sos/2.0/wsdl";

    String NS_SOSW_PREFIX = "sosw";

    String NS_WSAM = "http://www.w3.org/2007/05/addressing/metadata";

    String NS_WSAM_PREFIX = "wsam";

    String NS_WSDL = "http://schemas.xmlsoap.org/wsdl/";

    String NS_WSDL_PREFIX = "wsdl";

    String NS_XSD = "http://www.w3.org/2001/XMLSchema";

    String NS_XSD_PREFIX = "xsd";

    String AN_WSAM_ACTION = "Action";

    String AN_XSD_ELEMENT_FORM_DEFAULT = "elementFormDefault";

    String AN_XSD_NAMESPACE = "namespace";

    String AN_XSD_SCHEMA_LOCATION = "schemaLocation";

    String AN_XSD_TARGET_NAMESPACE = "targetNamespace";

    String EN_OPERATION = "operation";

    String EN_BINDING = "binding";

    String EN_HTTP_ADDRESS = "address";

    String EN_HTTP_BINDING = EN_BINDING;

    String EN_HTTP_OPERATION = EN_OPERATION;

    String EN_HTTP_URL_ENCODED = "urlEncoded";

    String EN_MIME_CONTENT = "content";

    String EN_MIME_MIME_XML = "mimeXml";

    String EN_SOAP_ADDRESS = EN_HTTP_ADDRESS;

    String EN_SOAP_BINDING = EN_BINDING;

    String EN_SOAP_BODY = "body";

    String EN_SOAP_FAULT = "fault";

    String EN_SOAP_OPERATION = EN_OPERATION;

    String EN_SOSW_SOS_KVP_BINDING = "SosKvpBinding";

    String EN_SOSW_SOS_GET_PORT_TYPE = "SosGetPortType";

    String EN_SOSW_SOS_POST_PORT_TYPE = "SosPostPortType";

    String EN_SOSW_SOS_POX_BINDING = "SosPoxBinding";

    String EN_SOSW_SOS_SOAP_BINDING = "SosSoapBinding";

    String EN_XSD_INCLUDE = "include";

    String EN_XSD_SCHEMA = "schema";

    String MESSAGE_PART = EN_SOAP_BODY;

    String POX_CONTENT_TYPE = MediaTypes.TEXT_XML.toString();

    String KVP_HTTP_VERB = HTTPMethods.GET;

    String POX_HTTP_VERB = HTTPMethods.POST;

    String QUALIFIED_ELEMENT_FORM_DEFAULT = "qualified";

    String SOAP_BINDING_HTTP_TRANSPORT = "http://schemas.xmlsoap.org/soap/http";

    // String SOAP_12_BINDING_HTTP_TRANSPORT = "http://www.w3.org/2003/05/soap/bindings/HTTP";
    String SOAP_12_BINDING_HTTP_TRANSPORT = SOAP_BINDING_HTTP_TRANSPORT;

    String SOAP_DOCUMENT_STYLE = "document";

    URI OWS_EXCEPTION_ACTION = URI.create(SosSoapConstants.RESP_ACTION_OWS);

    URI SWES_EXCEPTION_ACTION = URI.create(SosSoapConstants.RESP_ACTION_SWES);

    QName QN_HTTP_ADDRESS = new QName(NS_HTTP, EN_HTTP_ADDRESS, NS_HTTP_PREFIX);

    QName QN_HTTP_BINDING = new QName(NS_HTTP, EN_HTTP_BINDING, NS_HTTP_PREFIX);

    QName QN_HTTP_OPERATION = new QName(NS_HTTP, EN_HTTP_OPERATION, NS_HTTP_PREFIX);

    QName QN_HTTP_URL_ENCODED = new QName(NS_HTTP, EN_HTTP_URL_ENCODED, NS_HTTP_PREFIX);

    QName QN_MIME_CONTENT = new QName(NS_MIME, EN_MIME_CONTENT, NS_MIME_PREFIX);

    QName QN_MIME_MIME_XML = new QName(NS_MIME, EN_MIME_MIME_XML, NS_MIME_PREFIX);

    QName QN_SOAP_ADDRESS = new QName(NS_SOAP, EN_SOAP_ADDRESS, NS_SOAP_PREFIX);

    QName QN_SOAP_BINDING = new QName(NS_SOAP, EN_SOAP_BINDING, NS_SOAP_PREFIX);

    QName QN_SOAP_BODY = new QName(NS_SOAP, EN_SOAP_BODY, NS_SOAP_PREFIX);

    QName QN_SOAP_FAULT = new QName(NS_SOAP, EN_SOAP_FAULT, NS_SOAP_PREFIX);

    QName QN_SOAP_12_ADDRESS = new QName(NS_SOAP_12, EN_SOAP_ADDRESS, NS_SOAP_12_PREFIX);

    QName QN_SOAP_12_BINDING = new QName(NS_SOAP_12, EN_SOAP_BINDING, NS_SOAP_12_PREFIX);

    QName QN_SOAP_12_BODY = new QName(NS_SOAP_12, EN_SOAP_BODY, NS_SOAP_12_PREFIX);

    QName QN_SOAP_12_FAULT = new QName(NS_SOAP_12, EN_SOAP_FAULT, NS_SOAP_12_PREFIX);

    QName QN_SOAP_OPERATION = new QName(NS_SOAP_12, EN_SOAP_OPERATION, NS_SOAP_12_PREFIX);

    QName QN_SOSW_KVP_BINDING = new QName(NS_SOSW, EN_SOSW_SOS_KVP_BINDING, NS_SOSW_PREFIX);

    QName QN_SOSW_GET_PORT_TYPE = new QName(NS_SOSW, EN_SOSW_SOS_GET_PORT_TYPE, NS_SOSW_PREFIX);

    QName QN_SOSW_POST_PORT_TYPE = new QName(NS_SOSW, EN_SOSW_SOS_POST_PORT_TYPE, NS_SOSW_PREFIX);

    QName QN_SOSW_POX_BINDING = new QName(NS_SOSW, EN_SOSW_SOS_POX_BINDING, NS_SOSW_PREFIX);

    QName QN_SOSW_SERVICE = new QName(NS_SOSW, SosConstants.SOS);

    QName QN_SOSW_SOAP_BINDING = new QName(NS_SOSW, EN_SOSW_SOS_SOAP_BINDING, NS_SOSW_PREFIX);

    QName QN_WSAM_ACTION = new QName(NS_WSAM, AN_WSAM_ACTION, NS_WSAM_PREFIX);

    QName QN_XSD_SCHEMA = new QName(NS_XSD, EN_XSD_SCHEMA, NS_XSD_PREFIX);

    QName QN_XSD_INCLUDE = new QName(NS_XSD, EN_XSD_INCLUDE, NS_XSD_PREFIX);

    interface WSDLElements {

        String EN_WSDL_DEFINITIONS = "definitions";

        String EN_WSDL_TYPES = "types";

        String EN_WSDL_MESSAGE = "message";

        String EN_WSDL_PART = "part";

        String EN_WSDL_PORT = "port";

        String EN_WSDL_PORT_TYPE = "portType";

        String EN_WSDL_BINDING = EN_BINDING;

        String EN_WSDL_OPERATION = EN_OPERATION;

        String EN_WSDL_INPUT = "input";

        String EN_WSDL_OUTPUT = "output";

        String EN_WSDL_FAULT = EN_SOAP_FAULT;

        String EN_WSDL_SERVICE = "service";
    }

    interface WSDLQNames extends WSDLElements {

        QName QN_WSDL_DEFINITIONS = new QName(NS_WSDL, EN_WSDL_DEFINITIONS, NS_WSDL_PREFIX);

        QName QN_WSDL_TYPES = new QName(NS_WSDL, EN_WSDL_TYPES, NS_WSDL_PREFIX);

        QName QN_WSDL_MESSAGE = new QName(NS_WSDL, EN_WSDL_MESSAGE, NS_WSDL_PREFIX);

        QName QN_WSDL_PART = new QName(NS_WSDL, EN_WSDL_PART, NS_WSDL_PREFIX);

        QName QN_WSDL_PORT = new QName(NS_WSDL, EN_WSDL_PORT, NS_WSDL_PREFIX);

        QName QN_WSDL_PORT_TYPE = new QName(NS_WSDL, EN_WSDL_PORT_TYPE, NS_WSDL_PREFIX);

        QName QN_WSDL_BINDING = new QName(NS_WSDL, EN_WSDL_BINDING, NS_WSDL_PREFIX);

        QName QN_WSDL_OPERATION = new QName(NS_WSDL, EN_WSDL_OPERATION, NS_WSDL_PREFIX);

        QName QN_WSDL_INPUT = new QName(NS_WSDL, EN_WSDL_INPUT, NS_WSDL_PREFIX);

        QName QN_WSDL_OUTPUT = new QName(NS_WSDL, EN_WSDL_OUTPUT, NS_WSDL_PREFIX);

        QName QN_WSDL_FAULT = new QName(NS_WSDL, EN_WSDL_FAULT, NS_WSDL_PREFIX);

        QName QN_WSDL_SERVICE = new QName(NS_WSDL, EN_WSDL_SERVICE, NS_WSDL_PREFIX);

    }

    interface SoapResponseActionUris {
        URI DELETE_SENSOR = URI.create(SosSoapConstants.RESP_ACTION_DELETE_SENSOR);

        URI DESCRIBE_SENSOR = URI.create(SosSoapConstants.RESP_ACTION_DESCRIBE_SENSOR);

        URI GET_CAPABILITIES = URI.create(SosSoapConstants.RESP_ACTION_GET_CAPABILITIES);

        URI GET_FEATURE_OF_INTEREST = URI.create(SosSoapConstants.RESP_ACTION_GET_FEATURE_OF_INTEREST);

        URI GET_OBSERVATION = URI.create(SosSoapConstants.RESP_ACTION_GET_OBSERVATION);

        URI GET_OBSERVATION_BY_ID = URI.create(SosSoapConstants.RESP_ACTION_GET_OBSERVATION_BY_ID);

        URI GET_RESULT = URI.create(SosSoapConstants.RESP_ACTION_GET_RESULT);

        URI GET_RESULT_TEMPLATE = URI.create(SosSoapConstants.RESP_ACTION_GET_RESULT_TEMPLATE);

        URI INSERT_OBSERVATION = URI.create(SosSoapConstants.REQ_ACTION_INSERT_OBSERVATION);

        URI INSERT_RESULT = URI.create(SosSoapConstants.RESP_ACTION_INSERT_RESULT);

        URI INSERT_RESULT_TEMPLATE = URI.create(SosSoapConstants.RESP_ACTION_INSERT_RESULT_TEMPLATE);

        URI INSERT_SENSOR = URI.create(SosSoapConstants.RESP_ACTION_INSERT_SENSOR);

        URI UPDATE_SENSOR_DESCRIPTION = URI.create(SosSoapConstants.RESP_ACTION_UPDATE_SENSOR_DESCRIPTION);
    }

    interface SoapRequestActionUris {
        URI DELETE_SENSOR = URI.create(SosSoapConstants.REQ_ACTION_DELETE_SENSOR);

        URI DESCRIBE_SENSOR = URI.create(SosSoapConstants.REQ_ACTION_DESCRIBE_SENSOR);

        URI GET_CAPABILITIES = URI.create(SosSoapConstants.REQ_ACTION_GET_CAPABILITIES);

        URI GET_FEATURE_OF_INTEREST = URI.create(SosSoapConstants.REQ_ACTION_GET_FEATURE_OF_INTEREST);

        URI GET_OBSERVATION = URI.create(SosSoapConstants.REQ_ACTION_GET_OBSERVATION);

        URI GET_OBSERVATION_BY_ID = URI.create(SosSoapConstants.REQ_ACTION_GET_OBSERVATION_BY_ID);

        URI GET_RESULT = URI.create(SosSoapConstants.REQ_ACTION_GET_RESULT);

        URI GET_RESULT_TEMPLATE = URI.create(SosSoapConstants.REQ_ACTION_GET_RESULT_TEMPLATE);

        URI INSERT_OBSERVATION = URI.create(SosSoapConstants.REQ_ACTION_INSERT_OBSERVATION);

        URI INSERT_RESULT = URI.create(SosSoapConstants.REQ_ACTION_INSERT_RESULT);

        URI INSERT_RESULT_TEMPLATE = URI.create(SosSoapConstants.REQ_ACTION_INSERT_RESULT_TEMPLATE);

        URI INSERT_SENSOR = URI.create(SosSoapConstants.REQ_ACTION_INSERT_SENSOR);

        URI UPDATE_SENSOR_DESCRIPTION = URI.create(SosSoapConstants.REQ_ACTION_UPDATE_SENSOR_DESCRIPTION);
    }

}
