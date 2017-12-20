/*
 * Copyright 2015-2017 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.ogc.sos.gda;

import javax.xml.namespace.QName;

import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.om.OmConstants;
import org.n52.shetland.w3c.SchemaLocation;
import org.n52.shetland.w3c.W3CConstants;

/**
 * Constants for the GetDataAvailability SOS operation.
 *
 * @author Christian Autermann
 *
 * @since 1.0.0
 */
public interface GetDataAvailabilityConstants {

    String NS_GDA_PREFIX = "gda";

    String EN_GET_DATA_AVAILABILITY_MEMBER = "dataAvailabilityMember";

    String EN_GET_DATA_AVAILABILITY = "GetDataAvailability";

    String EN_GET_DATA_AVAILABILITY_RESPONSE = "GetDataAvailabilityResponse";

    String EN_EXTENSION = "extension";

    String EN_COUNT = "count";

    String FORMAT_DESCRIPTOR = "formatDescriptor";

    String PROCEDURE_FORMAT_DESCRIPTOR = "procedureDescriptionFormatDescriptor";

    String OBSERVATION_FORMAT_DESCRIPTOR = "observationFormatDescriptor";

    String PROCEDURE_DESCRIPTION_FORMAT = "procedureDescriptionFormat";

    String RESPONSE_FORMAT = "responseFormat";

    String OBSERVATION_TYPE = "observationType";

    String DECLARE_NAMESPACE = "declare namespace %s='%s';";

    /*
     * GDA v10
     */
    String NS_GDA = "http://www.opengis.net/sosgda/1.0";

    String XPATH_PREFIXES_GDA = String.format(DECLARE_NAMESPACE, NS_GDA_PREFIX, NS_GDA);

    String SCHEMA_LOCATION_URL_GET_DATA_AVAILABILITY = "http://waterml2.org/schemas/gda/1.0/gda.xsd";

    SchemaLocation GET_DATA_AVAILABILITY_SCHEMA_LOCATION =
            new SchemaLocation(NS_GDA, SCHEMA_LOCATION_URL_GET_DATA_AVAILABILITY);

    /**
     * The {@code QName} for {@code gda:dataAvailabilityMember}.
     */
    QName GDA_DATA_AVAILABILITY_MEMBER = new QName(GetDataAvailabilityConstants.NS_GDA,
            EN_GET_DATA_AVAILABILITY_MEMBER, GetDataAvailabilityConstants.NS_GDA_PREFIX);

    /**
     * The {@code QName} for {@code gda:GetDataAvailabilityResponse}.
     */
    QName GDA_GET_DATA_AVAILABILITY_RESPONSE = new QName(GetDataAvailabilityConstants.NS_GDA,
            EN_GET_DATA_AVAILABILITY_RESPONSE, GetDataAvailabilityConstants.NS_GDA_PREFIX);

    /**
     * The {@code QName} for {@code gda:GetDataAvailability}.
     */
    QName GDA_GET_DATA_AVAILABILITY = new QName(GetDataAvailabilityConstants.NS_GDA, EN_GET_DATA_AVAILABILITY,
            GetDataAvailabilityConstants.NS_GDA_PREFIX);

    QName GDA_EXTENSION =
            new QName(GetDataAvailabilityConstants.NS_GDA, EN_EXTENSION, GetDataAvailabilityConstants.NS_GDA_PREFIX);

    /**
     * The {@code QName} for {@code gda:featureOfInterest}.
     */
    QName GDA_FEATURE_OF_INTEREST = new QName(GetDataAvailabilityConstants.NS_GDA, OmConstants.EN_FEATURE_OF_INTEREST,
            GetDataAvailabilityConstants.NS_GDA_PREFIX);

    /**
     * The {@code QName} for {@code gda:observedProperty}.
     */
    QName GDA_OBSERVED_PROPERTY = new QName(GetDataAvailabilityConstants.NS_GDA, OmConstants.EN_OBSERVED_PROPERTY,
            GetDataAvailabilityConstants.NS_GDA_PREFIX);

    /**
     * The {@code QName} for {@code gda:phenomenonTime}.
     */
    QName GDA_PHENOMENON_TIME = new QName(GetDataAvailabilityConstants.NS_GDA, OmConstants.EN_PHENOMENON_TIME,
            GetDataAvailabilityConstants.NS_GDA_PREFIX);

    /**
     * The {@code QName} for {@code gda:procedure}.
     */
    QName GDA_PROCEDURE = new QName(GetDataAvailabilityConstants.NS_GDA, OmConstants.EN_PROCEDURE,
            GetDataAvailabilityConstants.NS_GDA_PREFIX);

    /**
     * The {@code QName} for {@code gda:procedure}.
     */
    QName GDA_COUNT =
            new QName(GetDataAvailabilityConstants.NS_GDA, EN_COUNT, GetDataAvailabilityConstants.NS_GDA_PREFIX);

    /*
     * GDA v20
     */
    String NS_GDA_20 = "http://www.opengis.net/sosgda/2.0";

    String XPATH_PREFIXES_GDA_20 = String.format(DECLARE_NAMESPACE, NS_GDA_PREFIX, NS_GDA_20);

    String SCHEMA_LOCATION_URL_GET_DATA_AVAILABILITY_20 = "http://waterml2.org/schemas/gda/2.0/gda.xsd";

    SchemaLocation GET_DATA_AVAILABILITY_20_SCHEMA_LOCATION =
            new SchemaLocation(NS_GDA_20, SCHEMA_LOCATION_URL_GET_DATA_AVAILABILITY_20);

    /**
     * The {@code QName} for {@code gda:dataAvailabilityMember}.
     */
    QName GDA_DATA_AVAILABILITY_20_MEMBER = new QName(GetDataAvailabilityConstants.NS_GDA_20,
            EN_GET_DATA_AVAILABILITY_MEMBER, GetDataAvailabilityConstants.NS_GDA_PREFIX);

    /**
     * The {@code QName} for {@code gda:GetDataAvailabilityResponse}.
     */
    QName GDA_GET_DATA_AVAILABILITY_20_RESPONSE = new QName(GetDataAvailabilityConstants.NS_GDA_20,
            EN_GET_DATA_AVAILABILITY_RESPONSE, GetDataAvailabilityConstants.NS_GDA_PREFIX);

    /**
     * The {@code QName} for {@code gda:GetDataAvailability}.
     */
    QName GDA_GET_DATA_AVAILABILITY_20 = new QName(GetDataAvailabilityConstants.NS_GDA_20, EN_GET_DATA_AVAILABILITY,
            GetDataAvailabilityConstants.NS_GDA_PREFIX);

    QName GDA_20_EXTENSION =
            new QName(GetDataAvailabilityConstants.NS_GDA_20, EN_EXTENSION, GetDataAvailabilityConstants.NS_GDA_PREFIX);

    /**
     * The {@code QName} for {@code gda:featureOfInterest}.
     */
    QName GDA_20_FEATURE_OF_INTEREST = new QName(GetDataAvailabilityConstants.NS_GDA_20,
            OmConstants.EN_FEATURE_OF_INTEREST, GetDataAvailabilityConstants.NS_GDA_PREFIX);

    /**
     * The {@code QName} for {@code gda:observedProperty}.
     */
    QName GDA_20_OBSERVED_PROPERTY = new QName(GetDataAvailabilityConstants.NS_GDA_20,
            OmConstants.EN_OBSERVED_PROPERTY, GetDataAvailabilityConstants.NS_GDA_PREFIX);

    /**
     * The {@code QName} for {@code gda:phenomenonTime}.
     */
    QName GDA_20PHENOMENON_TIME = new QName(GetDataAvailabilityConstants.NS_GDA_20, OmConstants.EN_PHENOMENON_TIME,
            GetDataAvailabilityConstants.NS_GDA_PREFIX);

    /**
     * The {@code QName} for {@code gda:procedure}.
     */
    QName GDA_20_PROCEDURE = new QName(GetDataAvailabilityConstants.NS_GDA_20, OmConstants.EN_PROCEDURE,
            GetDataAvailabilityConstants.NS_GDA_PREFIX);

    /**
     * The {@code QName} for {@code gda:procedure}.
     */
    QName GDA_20_COUNT =
            new QName(GetDataAvailabilityConstants.NS_GDA_20, EN_COUNT, GetDataAvailabilityConstants.NS_GDA_PREFIX);

    /**
     * The {@code QName} for {@code offering}.
     */
    QName GDA_20_OFFERING =
            new QName(GetDataAvailabilityConstants.NS_GDA_20, "offering", GetDataAvailabilityConstants.NS_GDA_PREFIX);

    /**
     * The {@code QName} for {@code gda:formatDescriptor}.
     */
    QName GDA_20_FORMAT_DESCRIPTOR = new QName(GetDataAvailabilityConstants.NS_GDA_20, FORMAT_DESCRIPTOR,
            GetDataAvailabilityConstants.NS_GDA_PREFIX);

    /**
     * The {@code QName} for {@code gda:procedureFormatDescriptor}.
     */
    QName GDA_20_PROCEDURE_FORMAT_DESCRIPTOR = new QName(GetDataAvailabilityConstants.NS_GDA_20,
            PROCEDURE_FORMAT_DESCRIPTOR, GetDataAvailabilityConstants.NS_GDA_PREFIX);

    /**
     * The {@code QName} for {@code gda:observatinFormatDescriptor}.
     */
    QName GDA_20_OBSERVATION_FORMAT_DESCRIPTOR = new QName(GetDataAvailabilityConstants.NS_GDA_20,
            OBSERVATION_FORMAT_DESCRIPTOR, GetDataAvailabilityConstants.NS_GDA_PREFIX);

    /**
     * The {@code QName} for {@code gda:procedureDescriptionFormat}.
     */
    QName GDA_20_PROCEDURE_DESCRIPTION_FORMAT = new QName(GetDataAvailabilityConstants.NS_GDA_20,
            PROCEDURE_DESCRIPTION_FORMAT, GetDataAvailabilityConstants.NS_GDA_PREFIX);

    /**
     * The {@code QName} for {@code gda:responseFormat}.
     */
    QName GDA_20_RESPONSE_FORMAT = new QName(GetDataAvailabilityConstants.NS_GDA_20, RESPONSE_FORMAT,
            GetDataAvailabilityConstants.NS_GDA_PREFIX);

    /**
     * The {@code QName} for {@code gda:observationType}.
     */
    QName GDA_20_OBSERVATION_TYPE = new QName(GetDataAvailabilityConstants.NS_GDA_20, OBSERVATION_TYPE,
            GetDataAvailabilityConstants.NS_GDA_PREFIX);

    String AN_VERSION = "version";

    String AN_SERVICE = "service";

    String DATA_AVAILABILITY = "dataAvailability";

    /* TODO is this one right? */
    String CONFORMANCE_CLASS = "http://www.opengis.net/spec/SOS/2.0/conf/daRetrieval";

    /**
     * The operation name.
     */
    String OPERATION_NAME = EN_GET_DATA_AVAILABILITY;

    /**
     * The {@code QName} for {@code sos:dataAvailabilityMember}.
     */
    QName SOS_DATA_AVAILABILITY_MEMBER =
            new QName(Sos2Constants.NS_SOS_20, EN_GET_DATA_AVAILABILITY_MEMBER, SosConstants.NS_SOS_PREFIX);

    /**
     * The {@code QName} for {@code sos:GetDataAvailabilityResponse}.
     */
    QName SOS_GET_DATA_AVAILABILITY_RESPONSE =
            new QName(Sos2Constants.NS_SOS_20, EN_GET_DATA_AVAILABILITY_RESPONSE, SosConstants.NS_SOS_PREFIX);

    /**
     * The {@code QName} for {@code sos:GetDataAvailability}.
     */
    QName SOS_GET_DATA_AVAILABILITY =
            new QName(Sos2Constants.NS_SOS_20, EN_GET_DATA_AVAILABILITY, SosConstants.NS_SOS_PREFIX);

    /**
     * The {@code QName} for {@code version}.
     */
    QName VERSION = new QName(AN_VERSION);

    /**
     * The {@code QName} for {@code service}.
     */
    QName SERVICE = new QName(AN_SERVICE);

    /**
     * The {@code QName} for {@code sos:version}.
     */
    QName SOS_VERSION = new QName(Sos2Constants.NS_SOS_20, AN_VERSION, SosConstants.NS_SOS_PREFIX);

    /**
     * The {@code QName} for {@code sos:service}.
     */
    QName SOS_SERVICE = new QName(Sos2Constants.NS_SOS_20, AN_SERVICE, SosConstants.NS_SOS_PREFIX);

    /**
     * The {@code QName} for {@code xlink:href}.
     */
    QName XLINK_HREF = new QName(W3CConstants.NS_XLINK, W3CConstants.AN_HREF, W3CConstants.NS_XLINK_PREFIX);

    /**
     * The {@code QName} for {@code xlink:title}.
     */
    QName XLINK_TITLE =
            new QName(W3CConstants.NS_XLINK, W3CConstants.AN_TITLE, W3CConstants.NS_XLINK_PREFIX);

    /**
     * The {@code QName} for {@code om:featureOfInterest}.
     */
    QName OM_FEATURE_OF_INTEREST =
            new QName(OmConstants.NS_OM_2, OmConstants.EN_FEATURE_OF_INTEREST, OmConstants.NS_OM_PREFIX);

    /**
     * The {@code QName} for {@code om:observedProperty}.
     */
    QName OM_OBSERVED_PROPERTY =
            new QName(OmConstants.NS_OM_2, OmConstants.EN_OBSERVED_PROPERTY, OmConstants.NS_OM_PREFIX);

    /**
     * The {@code QName} for {@code om:phenomenonTime}.
     */
    QName OM_PHENOMENON_TIME =
            new QName(OmConstants.NS_OM_2, OmConstants.EN_PHENOMENON_TIME, OmConstants.NS_OM_PREFIX);

    /**
     * The {@code QName} for {@code om:procedure}.
     */
    QName OM_PROCEDURE = new QName(OmConstants.NS_OM_2, OmConstants.EN_PROCEDURE, OmConstants.NS_OM_PREFIX);

    /**
     * The {@code QName} for {@code sos:procedure}.
     */
    QName SOS_COUNT = new QName(Sos2Constants.NS_SOS_20, EN_COUNT, SosConstants.NS_SOS_PREFIX);

    /**
     * The available parameters of the operation.
     */
    enum GetDataAvailabilityParams {
        featureOfInterest, observedProperty, procedure, offering;
    }

    enum GetDataAvailabilityV20Params {
        responseFormat;
    }

    // WSDLOperation WSDL_OPERATION =
    // WSDLOperation
    // .newWSDLOperation()
    // .setName(OPERATION_NAME)
    // .setVersion(Sos2Constants.SERVICEVERSION)
    // .setRequest(SOS_GET_DATA_AVAILABILITY)
    // .setRequestAction(
    // URI.create("http://www.opengis.net/def/serviceOperation/sos/daRetrieval/2.0/GetDataAvailability"))
    // .setResponse(SOS_GET_DATA_AVAILABILITY_RESPONSE)
    // .setResponseAction(
    // URI.create("http://www.opengis.net/def/serviceOperation/sos/daRetrieval/2.0/GetDataAvailabilityResponse"))
    // .setFaults(WSDLFault.DEFAULT_FAULTS).build();
}
