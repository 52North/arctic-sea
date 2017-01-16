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
package org.n52.svalbard;

import java.io.OutputStream;
import java.util.Set;

import javax.inject.Inject;
import javax.xml.stream.XMLStreamException;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.n52.janmayen.Producer;
import org.n52.janmayen.http.MediaTypes;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.shetland.ogc.ows.service.OwsServiceResponse;
import org.n52.shetland.w3c.SchemaLocation;
import org.n52.shetland.w3c.W3CConstants;
import org.n52.shetland.w3c.soap.SoapConstants;
import org.n52.shetland.w3c.soap.SoapFault;
import org.n52.shetland.w3c.soap.SoapResponse;
import org.n52.svalbard.encode.Encoder;
import org.n52.svalbard.encode.EncoderKey;
import org.n52.svalbard.encode.EncoderRepository;
import org.n52.svalbard.encode.OperationResponseEncoderKey;
import org.n52.svalbard.encode.SchemaAwareEncoder;
import org.n52.svalbard.encode.StreamingEncoder;
import org.n52.svalbard.encode.XmlStreamWriter;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.exception.NoEncoderForKeyException;

import com.google.common.collect.Sets;

/**
 * {@link XmlStreamWriter} implementation for SOAP 1.2
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 4.1.0
 *
 */
public class Soap12XmlStreamWriter extends XmlStreamWriter<SoapResponse> {

    private SoapResponse response;

    private EncoderRepository encoderRepository;

    private Producer<XmlOptions> xmlOptions;

    /**
     * constructor.
     */
    public Soap12XmlStreamWriter() {
        this(null);
    }

    /**
     * constructor
     *
     * @param response
     *            Service internal SOAP response to encode
     */
    public Soap12XmlStreamWriter(SoapResponse response) {
        this.response = response;
    }

    @Inject
    public void setEncoderRepository(EncoderRepository encoderRepository) {
        this.encoderRepository = encoderRepository;
    }

    @Inject
    public void setXmlOptions(Producer<XmlOptions> xmlOptions) {
        this.xmlOptions = xmlOptions;
    }

    @Override
    public void write(OutputStream out) throws EncodingException {
        write(getResponse(), out);
    }

    @Override
    public void write(OutputStream out, EncodingValues encodingValues) throws EncodingException {
        write(getResponse(), out, encodingValues);
    }

    @Override
    public void write(SoapResponse element, OutputStream out) throws EncodingException {
        write(element, out, new EncodingValues());
    }

    @Override
    public void write(SoapResponse element, OutputStream out, EncodingValues encodingValues) throws EncodingException {
        try {
            init(out);
            start(encodingValues.isEmbedded());
            writeSoapEnvelope(element);
            end();
            finish();
        } catch (XMLStreamException xmlse) {
            throw new EncodingException(xmlse);
        }
    }

    /**
     * Set the response element to encode and write to stream
     *
     * @param response
     *            Service internal response
     */
    public void setResponse(SoapResponse response) {
        this.response = response;
    }

    /**
     * Get the response element to encode and write to stream
     *
     * @return The response element to encode and write to stream
     */
    protected SoapResponse getResponse() {
        return response;
    }

    /**
     * Write the SOAP 1.2. envelope element
     *
     * @param response
     *            The response element to encode and write to stream
     * @throws XMLStreamException
     *             If an error occurs when writing to {@link OutputStream} If an
     *             error occurs when writing to {@link OutputStream}
     * @throws EncodingException
     *             If an encoding error occurs
     */
    protected void writeSoapEnvelope(SoapResponse response) throws XMLStreamException, EncodingException {
        start(SoapConstants.SOAP_12_ENVELOPE);
        namespace(W3CConstants.NS_XLINK_PREFIX, W3CConstants.NS_XLINK);
        namespace(SoapConstants.NS_SOAP_PREFIX, SoapConstants.NS_SOAP_12);
        schemaLocation(getSchemaLocation(response));
        writeNewLine();
        // writeSoapHeader()
        writeSoapBody(response);
        writeNewLine();
        end(SoapConstants.SOAP_12_ENVELOPE);

    }

    protected Set<SchemaLocation> getSchemaLocation(SoapResponse response)
            throws EncodingException, XMLStreamException {
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
     * @param response
     *            The response element to encode and write to stream
     * @throws XMLStreamException
     *             If an error occurs when writing to {@link OutputStream}
     * @throws EncodingException
     *             If an encoding error occurs
     */
    protected void writeSoapBody(SoapResponse response) throws XMLStreamException, EncodingException {
        int before = indent;
        start(SoapConstants.SOAP_12_BODY);
        writeNewLine();
        if (response != null) {
            if (response.isSetSoapFault()) {
                writeSoapFault(response.getSoapFault());
            } else if (response.hasException()) {
                writeSoapFaultFromException(response.getException());
            } else if (response.isSetBodyContent()) {
                writeBodyContent(response.getBodyContent());
            }
        }
        indent = before;
        writeNewLine();
        end(SoapConstants.SOAP_12_BODY);
    }

    /**
     * Encode and write the {@link OwsServiceResponse} to stream
     *
     * @param bodyResponse
     *            The service internal response to encode and write
     * @throws XMLStreamException
     *             If an error occurs when writing to {@link OutputStream}
     * @throws EncodingException
     *             If an encoding error occurs
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected void writeBodyContent(OwsServiceResponse bodyResponse) throws XMLStreamException, EncodingException {
        Encoder<Object, OwsServiceResponse> encoder = getEncoder(
                new OperationResponseEncoderKey(new OperationKey(bodyResponse), MediaTypes.APPLICATION_XML));
        if (encoder instanceof StreamingEncoder<?, ?>) {
            ((StreamingEncoder) encoder).encode(bodyResponse, getOutputStream(),
                    new EncodingValues().setAsDocument(true).setEmbedded(true).setIndent(indent));
        } else {
            String soapBodyContent = ((XmlObject) encoder.encode(bodyResponse)).xmlText(this.xmlOptions.get());
            if (soapBodyContent.startsWith("<?xml")) {
                soapBodyContent = soapBodyContent.substring(soapBodyContent.indexOf('>'));
            }
            rawText(soapBodyContent);
        }
    }

    /**
     * Encode and write SOAP 1.2 fault element to SOAP 1.2 body element
     *
     * @param fault
     *            Service internal SOAP fault representation
     * @throws EncodingException
     *             If an encoding error occurs
     * @throws XMLStreamException
     *             If an error occurs when writing to {@link OutputStream}
     */
    protected void writeSoapFault(SoapFault fault) throws EncodingException, XMLStreamException {
        Encoder<XmlObject, SoapFault> encoder = getEncoder(SoapConstants.NS_SOAP_12, fault);
        String soapFault = encoder.encode(fault).xmlText(this.xmlOptions.get());
        if (soapFault.startsWith("<?xml")) {
            soapFault = soapFault.substring(soapFault.indexOf('>'));
        }
        rawText(soapFault);
    }

    private <T, S> Encoder<T, S> getEncoder(String namespace, Object o) throws NoEncoderForKeyException {
        EncoderKey key = new XmlEncoderKey(namespace, o.getClass());
        Encoder<T, S> encoder = this.encoderRepository.getEncoder(key);
        if (encoder == null) {
            throw new NoEncoderForKeyException(key);
        }
        return encoder;
    }

    /**
     * Encode and write {@link OwsExceptionReport} element to SOAP 1.2 body
     * element
     *
     * @param exception
     *            Service internal {@link OwsExceptionReport}
     * @throws EncodingException
     *             If an encoding error occurs
     * @throws XMLStreamException
     *             If an error occurs when writing to {@link OutputStream}
     */
    protected void writeSoapFaultFromException(OwsExceptionReport exception)
            throws EncodingException, XMLStreamException {
        Encoder<XmlObject, OwsExceptionReport> encoder = getEncoder(SoapConstants.NS_SOAP_12, exception);
        String soapFault = encoder.encode(exception).xmlText(this.xmlOptions.get());
        if (soapFault.startsWith("<?xml")) {
            soapFault = soapFault.substring(soapFault.indexOf('>'));
        }
        rawText(soapFault);
    }

    protected Encoder<Object, OwsServiceResponse> getEncoder(OwsServiceResponse abstractServiceResponse)
            throws NoEncoderForKeyException {
        return getEncoder(new OperationResponseEncoderKey(new OperationKey(abstractServiceResponse),
                MediaTypes.APPLICATION_XML));
    }

    /**
     * Get encoder for {@link EncoderKey}
     *
     * @param key
     *            Encoder key to get encoder for
     * @return Matching encoder
     * @throws NoEncoderForKeyException
     *             If no matching encoder was found
     */
    protected Encoder<Object, OwsServiceResponse> getEncoder(EncoderKey key) throws NoEncoderForKeyException {
        Encoder<Object, OwsServiceResponse> encoder = this.encoderRepository.getEncoder(key);
        if (encoder == null) {
            throw new NoEncoderForKeyException(key);
        }
        return encoder;
    }
}
