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
package org.n52.svalbard.write;

import java.io.OutputStream;
import java.util.Set;

import javax.xml.stream.XMLStreamException;

import org.apache.xmlbeans.XmlObject;
import org.n52.janmayen.http.MediaTypes;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.shetland.ogc.ows.service.OwsOperationKey;
import org.n52.shetland.ogc.ows.service.OwsServiceResponse;
import org.n52.shetland.w3c.SchemaLocation;
import org.n52.shetland.w3c.W3CConstants;
import org.n52.shetland.w3c.soap.SoapConstants;
import org.n52.shetland.w3c.soap.SoapFault;
import org.n52.shetland.w3c.soap.SoapResponse;
import org.n52.svalbard.encode.Encoder;
import org.n52.svalbard.encode.EncodingContext;
import org.n52.svalbard.encode.OperationResponseEncoderKey;
import org.n52.svalbard.encode.SchemaAwareEncoder;
import org.n52.svalbard.encode.StreamingEncoder;
import org.n52.svalbard.encode.StreamingEncoderFlags;
import org.n52.svalbard.encode.XmlBeansEncodingFlags;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.exception.NoEncoderForKeyException;

import com.google.common.collect.Sets;

/**
 * {@link XmlStreamWriter} implementation for SOAP 1.2
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public class Soap12XmlStreamWriter extends XmlStreamWriter<SoapResponse> {
    public Soap12XmlStreamWriter(EncodingContext context, OutputStream outputStream, SoapResponse element)
            throws XMLStreamException {
        super(context, outputStream, element);
    }

    @Override
    public void write() throws EncodingException, XMLStreamException {
        start();
        writeSoapEnvelope();
        end();
        finish();
    }

    /**
     * Write the SOAP 1.2. envelope element
     *
     * @throws XMLStreamException If an error occurs when writing to {@link OutputStream} If an error occurs when
     *                            writing to {@link OutputStream}
     * @throws EncodingException  If an encoding error occurs
     */
    protected void writeSoapEnvelope() throws XMLStreamException, EncodingException {
        start(SoapConstants.SOAP_12_ENVELOPE);
        namespace(W3CConstants.NS_XLINK_PREFIX, W3CConstants.NS_XLINK);
        namespace(SoapConstants.NS_SOAP_PREFIX, SoapConstants.NS_SOAP_12);
        schemaLocation(getSchemaLocation());
        // writeSoapHeader()
        writeSoapBody();
        end(SoapConstants.SOAP_12_ENVELOPE);

    }

    protected Set<SchemaLocation> getSchemaLocation()
            throws EncodingException, XMLStreamException {
        SoapResponse response = getElement();
        Set<SchemaLocation> schemaLocations = Sets.newHashSet();
        schemaLocations.add(SoapConstants.SOAP_12_SCHEMA_LOCATION);
        if (response.isSetBodyContent()) {
            Encoder<Object, OwsServiceResponse> encoder = getEncoder(response.getBodyContent());
            if (encoder != null && encoder instanceof SchemaAwareEncoder) {
                schemaLocations.addAll(((SchemaAwareEncoder<?, ?>) encoder).getSchemaLocations());
            }
        }
        return schemaLocations;
    }

    /**
     * Write the SOAP 1.2 body element
     *
     * @throws XMLStreamException If an error occurs when writing to {@link OutputStream}
     * @throws EncodingException  If an encoding error occurs
     */
    protected void writeSoapBody() throws XMLStreamException, EncodingException {
        start(SoapConstants.SOAP_12_BODY);
        SoapResponse response = getElement();
        if (response != null) {
            if (response.isSetSoapFault()) {
                writeSoapFault(response.getSoapFault());
            } else if (response.hasException()) {
                writeSoapFaultFromException(response.getException());
            } else if (response.isSetBodyContent()) {
                writeBodyContent(response.getBodyContent());
            }
        }
        end(SoapConstants.SOAP_12_BODY);
    }

    /**
     * Encode and write the {@link OwsServiceResponse} to stream
     *
     * @param bodyResponse The service internal response to encode and write
     *
     * @throws XMLStreamException If an error occurs when writing to {@link OutputStream}
     * @throws EncodingException  If an encoding error occurs
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected void writeBodyContent(OwsServiceResponse bodyResponse) throws XMLStreamException, EncodingException {
        Encoder<Object, OwsServiceResponse> encoder = getEncoder(
                new OperationResponseEncoderKey(
                        new OwsOperationKey(bodyResponse),
                        MediaTypes.APPLICATION_XML));
        if (encoder instanceof StreamingEncoder<?, ?>) {
            EncodingContext ctx = getContext()
                    .with(XmlBeansEncodingFlags.DOCUMENT)
                    .without(XmlBeansEncodingFlags.PROPERTY_TYPE)
                    .without(XmlBeansEncodingFlags.TYPE)
                    .with(StreamingEncoderFlags.EMBEDDED, true);
            ((StreamingEncoder) encoder).encode(bodyResponse, getOutputStream(), ctx);
        } else {
            String soapBodyContent = ((XmlObject) encoder.encode(bodyResponse)).xmlText(getXmlOptions());
            rawText(stripXmlDeclaration(soapBodyContent));
        }
    }

    /**
     * Encode and write SOAP 1.2 fault element to SOAP 1.2 body element
     *
     * @param fault Service internal SOAP fault representation
     *
     * @throws EncodingException  If an encoding error occurs
     * @throws XMLStreamException If an error occurs when writing to {@link OutputStream}
     */
    protected void writeSoapFault(SoapFault fault) throws EncodingException, XMLStreamException {
        Encoder<XmlObject, SoapFault> encoder = getEncoder(SoapConstants.NS_SOAP_12, fault);
        String soapFault = encoder.encode(fault).xmlText(getXmlOptions());
        rawText(stripXmlDeclaration(soapFault));
    }

    /**
     * Encode and write {@link OwsExceptionReport} element to SOAP 1.2 body element
     *
     * @param exception Service internal {@link OwsExceptionReport}
     *
     * @throws EncodingException  If an encoding error occurs
     * @throws XMLStreamException If an error occurs when writing to {@link OutputStream}
     */
    protected void writeSoapFaultFromException(OwsExceptionReport exception)
            throws EncodingException, XMLStreamException {
        Encoder<XmlObject, OwsExceptionReport> encoder = getEncoder(SoapConstants.NS_SOAP_12, exception);
        String soapFault = encoder.encode(exception).xmlText(getXmlOptions());
        rawText(stripXmlDeclaration(soapFault));
    }

    protected Encoder<Object, OwsServiceResponse> getEncoder(OwsServiceResponse abstractServiceResponse)
            throws NoEncoderForKeyException {
        return getEncoder(new OperationResponseEncoderKey(new OwsOperationKey(abstractServiceResponse),
                                                          MediaTypes.APPLICATION_XML));
    }

    private String stripXmlDeclaration(String soapFault) {
        if (soapFault.startsWith("<?xml")) {
            return soapFault.substring(soapFault.indexOf('>'));
        } else {
            return soapFault;
        }
    }

}
