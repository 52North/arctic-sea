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
package org.n52.svalbard.encode;

import java.util.Collections;
import java.util.Set;

import org.apache.xmlbeans.XmlObject;
import org.n52.janmayen.http.MediaType;
import org.n52.janmayen.http.MediaTypes;
import org.n52.shetland.ogc.ows.OWSConstants;
import org.n52.shetland.ogc.ows.exception.ExceptionCode;
import org.n52.shetland.ogc.ows.exception.OwsExceptionCode;
import org.n52.shetland.ogc.ows.service.OwsOperationKey;
import org.n52.shetland.ogc.ows.service.OwsServiceCommunicationObject;
import org.n52.shetland.ogc.sos.SosSoapConstants;
import org.n52.shetland.ogc.sos.exception.SosExceptionCode;
import org.n52.shetland.ogc.swes.exception.SwesExceptionCode;
import org.n52.shetland.w3c.soap.AbstractSoap;
import org.n52.shetland.w3c.soap.SoapResponse;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.exception.NoEncoderForKeyException;

import com.google.common.collect.ImmutableSet;

/**
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 * @since 1.0.0
 */
public abstract class AbstractSoapEncoder<T, S> extends AbstractXmlEncoder<T, S> {
    public static final String DEFAULT_FAULT_REASON = "A server exception was encountered.";

    public static final String MISSING_RESPONSE_DETAIL_TEXT = "Missing SOS response document!";

    public static final String MISSING_EXCEPTION_DETAIL_TEXT
            = "Error while creating SOAPFault element from OWSException! OWSException is missing!";

    private final Set<EncoderKey> encoderKey;

    public AbstractSoapEncoder(String namespace) {
        this.encoderKey = ImmutableSet.<EncoderKey>of(new XmlEncoderKey(namespace, SoapResponse.class));
    }

    @Override
    public Set<EncoderKey> getKeys() {
        return Collections.unmodifiableSet(encoderKey);
    }

    @Override
    public MediaType getContentType() {
        return MediaTypes.APPLICATION_SOAP_XML;
    }

    @Override
    public T encode(S response) throws EncodingException {
        return encode(response, null);
    }


    /**
     * Get the content for the SOAPBody as {@link XmlObject}
     *
     * @param response SOAP response
     *
     * @return SOAPBody content as {@link XmlObject}
     *
     * @throws EncodingException If no encoder is available, the object to encode is not supported or an error occurs
     *                           during the encoding
     */
    protected XmlObject getBodyContent(AbstractSoap<?> response) throws EncodingException {
        OperationResponseEncoderKey key = new OperationResponseEncoderKey(
                new OwsOperationKey(response.getBodyContent()), MediaTypes.APPLICATION_XML);
        Encoder<Object, OwsServiceCommunicationObject> encoder = getEncoder(key);
        if (encoder == null) {
            throw new NoEncoderForKeyException(key);
        }
        return (XmlObject) encoder.encode(response.getBodyContent());
    }

    /**
     * Get SOAP action URI depending on Exception code
     *
     * @param exceptionCode Exception code
     *
     * @return SOAP action URI
     */
    protected String getExceptionActionURI(ExceptionCode exceptionCode) {
        if (exceptionCode instanceof OwsExceptionCode) {
            return SosSoapConstants.RESP_ACTION_OWS;
        } else if (exceptionCode instanceof SwesExceptionCode) {
            return SosSoapConstants.RESP_ACTION_SWES;
        } else if (exceptionCode instanceof SosExceptionCode) {
            return SosSoapConstants.RESP_ACTION_SOS;
        } else {
            return SosSoapConstants.RESP_ACTION_OWS;
        }
    }

    /**
     * Get the reason for a SOAP fault from Exception code
     *
     * @param exceptionCode
     *            OWS exception code to get reason for.
     * @return Text for SOAP fault reason
     */
    protected String getSoapFaultReasonText(ExceptionCode exceptionCode) {
        if (exceptionCode != null && exceptionCode.getSoapFaultReason() != null) {
            return exceptionCode.getSoapFaultReason();
        } else {
            return OWSConstants.SOAP_REASON_UNKNOWN;
        }
    }

}
