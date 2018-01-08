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
package org.n52.iceland.statistics.api.mappings;

import org.n52.iceland.statistics.api.parameters.AbstractEsParameter;
import org.n52.iceland.statistics.api.parameters.Description;
import org.n52.iceland.statistics.api.parameters.Description.InformationOrigin;
import org.n52.iceland.statistics.api.parameters.Description.Operation;
import org.n52.iceland.statistics.api.parameters.ElasticsearchTypeRegistry;
import org.n52.iceland.statistics.api.parameters.ObjectEsParameterFactory;
import org.n52.iceland.statistics.api.parameters.SingleEsParameter;

public class ServiceEventDataMapping {
    public static final AbstractEsParameter UNHANDLED_SERVICEEVENT_TYPE = new SingleEsParameter(
            "unhandled-serviceevent-type",
            new Description(InformationOrigin.Computed, Operation.Default,
                            "If no processing handler is defined this field " +
                            "stores the Java class full name of the event"),
            ElasticsearchTypeRegistry.IPV4_FIELD);

    public static final AbstractEsParameter UUID_FIELD = new SingleEsParameter(
            "instance-uuid",
            new Description(InformationOrigin.Computed, Operation.Default,
                            "Unique ID of the instance who stored the event"),
            ElasticsearchTypeRegistry.STRING_FIELD);

    public static final AbstractEsParameter TIMESTAMP_FIELD = new SingleEsParameter(
            "@timestamp",
            new Description(InformationOrigin.Computed, Operation.Default,
                            "UTC timestamp of the event insertion"),
            ElasticsearchTypeRegistry.DATE_FIELD);

    // --------------- OutgoingResponseEvent --------------//
    public static final AbstractEsParameter ORE_EXEC_TIME = new SingleEsParameter(
            "outre-exec-time-ms",
            new Description(InformationOrigin.OutgoingResponseEvent, Operation.Default,
                            "The execution time of processing the request-response"),
            ElasticsearchTypeRegistry.INTEGER_FIELD);

    public static final AbstractEsParameter ORE_COUNT = new SingleEsParameter(
            "outre-count",
            new Description(InformationOrigin.OutgoingResponseEvent, Operation.Default,
                            "An incremental number since the start of the webapplication." +
                            " This field indicates the serial number of the request"),
            ElasticsearchTypeRegistry.LONG_FIELD);

    public static final AbstractEsParameter ORE_BYTES_WRITTEN = ObjectEsParameterFactory
            .bytesWritten("outre-bytes-written",
                          new Description(InformationOrigin.OutgoingResponseEvent, Operation.Default,
                                          "Size of the response document"));

    // --------------- Iceland Exception --------------//
    public static final AbstractEsParameter EX_STATUS = new SingleEsParameter(
            "exception-status",
            new Description(InformationOrigin.ExceptionEvent, Operation.Default,
                            "HTTP status of the exception if any"),
            ElasticsearchTypeRegistry.STRING_FIELD);

    public static final AbstractEsParameter EX_VERSION = new SingleEsParameter(
            "exception-version",
            new Description(InformationOrigin.ExceptionEvent, Operation.Default,
                            "Version of the exception"),
            ElasticsearchTypeRegistry.STRING_FIELD);

    public static final AbstractEsParameter EX_MESSAGE = new SingleEsParameter(
            "exception-message",
            new Description(InformationOrigin.ExceptionEvent, Operation.Default, "Text of the exception"),
            ElasticsearchTypeRegistry.STRING_FIELD);

    public static final AbstractEsParameter EX_CLASSTYPE = new SingleEsParameter(
            "exception-classtype",
            new Description(InformationOrigin.ExceptionEvent, Operation.Default,
                            "The Java class type of the exceptions by simple name"),
            ElasticsearchTypeRegistry.STRING_FIELD);

    // --------------- CodedException --------------//
    public static final AbstractEsParameter CEX_LOCATOR = new SingleEsParameter(
            "codedexception-locator",
            new Description(InformationOrigin.ExceptionEvent, Operation.Default, "CodedException locator"),
            ElasticsearchTypeRegistry.STRING_FIELD);
    public static final AbstractEsParameter CEX_SOAP_FAULT = new SingleEsParameter(
            "codedexception-soapfault",
            new Description(InformationOrigin.ExceptionEvent, Operation.Default, "CodedException SOAP fault message"),
            ElasticsearchTypeRegistry.STRING_FIELD);

    // --------------- OwsExceptionReport --------------//
    public static final AbstractEsParameter OWSEX_NAMESPACE = new SingleEsParameter(
            "owsexception-namespace",
            new Description(InformationOrigin.ExceptionEvent, Operation.Default, "OWSException namespace"),
            ElasticsearchTypeRegistry.STRING_FIELD);

    // ---------------- DEFAULT VALUES SERVICE REQUESTs--------------//
    public static final AbstractEsParameter SR_VERSION_FIELD = new SingleEsParameter(
            "sr-version",
            new Description(InformationOrigin.RequestEvent, Operation.Default, "Version of the deployment"),
            ElasticsearchTypeRegistry.STRING_FIELD);

    public static final AbstractEsParameter SR_SERVICE_FIELD = new SingleEsParameter(
            "sr-service",
            new Description(InformationOrigin.RequestEvent, Operation.Default,
                            "Name of deployment. E.g: SOS"),
            ElasticsearchTypeRegistry.STRING_FIELD);

    public static final AbstractEsParameter SR_LANGUAGE_FIELD = new SingleEsParameter(
            "sr-language",
            new Description(InformationOrigin.RequestEvent, Operation.Default,
                            "Language of the request. Read from the extension fields"),
            ElasticsearchTypeRegistry.STRING_FIELD);

    public static final AbstractEsParameter SR_OPERATION_NAME_FIELD = new SingleEsParameter(
            "sr-operation-name",
            new Description(InformationOrigin.RequestEvent, Operation.Default,
                            "Name of the requested operation. E.g: GetCapabilities"),
            ElasticsearchTypeRegistry.STRING_FIELD);

    public static final AbstractEsParameter SR_IP_ADDRESS_FIELD = new SingleEsParameter(
            "sr-source-ip-address",
            new Description(InformationOrigin.Computed, Operation.Default,
                            "Source IP address of the client proxies are stripped away"),
            ElasticsearchTypeRegistry.STRING_FIELD);

    public static final AbstractEsParameter SR_CONTENT_TYPE = new SingleEsParameter(
            "sr-content-type",
            new Description(InformationOrigin.Computed, Operation.Default,
                            "Content type of the request"),
            ElasticsearchTypeRegistry.STRING_FIELD);

    public static final AbstractEsParameter SR_ACCEPT_TYPES = new SingleEsParameter(
            "sr-accept-types",
            new Description(InformationOrigin.Computed, Operation.Default, "Accept type of the request"),
            ElasticsearchTypeRegistry.STRING_FIELD);

    public static final AbstractEsParameter SR_GEO_LOC_FIELD = ObjectEsParameterFactory
            .geoLocation("sr-source-geolocation",
                         new Description(InformationOrigin.Computed, Operation.Default,
                                         "Based on the IP address if this feature is enabled " +
                                         "the latitude and longitude coordinates are computed"));

    public static final AbstractEsParameter SR_PROXIED_REQUEST_FIELD = new SingleEsParameter(
            "sr-proxied-request",
            new Description(InformationOrigin.Computed, Operation.Default,
                            "Is the request came through a proxy or proxies"),
            ElasticsearchTypeRegistry.BOOLEAN_FIELD);

    public static final AbstractEsParameter SR_EXTENSIONS = ObjectEsParameterFactory
            .extension("sr-extensions", new Description(InformationOrigin.Computed, Operation.Default, "Extensions"));

    // --------------- DEFAULT RESPONSE EVENTS --------------//
    public static final AbstractEsParameter SRESP_CONTENT_TYPE = new SingleEsParameter(
            "sresp-content-type",
            new Description(InformationOrigin.ResponseEvent, Operation.Default, "Response content type"),
            ElasticsearchTypeRegistry.STRING_FIELD);

}
