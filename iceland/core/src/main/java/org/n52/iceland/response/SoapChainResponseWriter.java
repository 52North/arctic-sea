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
package org.n52.iceland.response;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Set;

import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.n52.iceland.coding.encode.AbstractResponseWriter;
import org.n52.iceland.coding.encode.ResponseProxy;
import org.n52.iceland.coding.encode.ResponseWriterKey;
import org.n52.janmayen.Producer;
import org.n52.shetland.w3c.soap.SoapChain;
import org.n52.shetland.w3c.soap.SoapResponse;
import org.n52.svalbard.encode.Encoder;
import org.n52.svalbard.encode.EncoderKey;
import org.n52.svalbard.encode.EncoderRepository;
import org.n52.svalbard.encode.StreamingEncoder;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.exception.NoEncoderForKeyException;
import org.n52.svalbard.util.CodingHelper;


/**
 * Streaming SOAP response writer implementation.
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 2.0.0
 *
 */
public class SoapChainResponseWriter extends AbstractResponseWriter<SoapChain> {
    public static final ResponseWriterKey KEY = new ResponseWriterKey(SoapChain.class);

    private final Producer<XmlOptions> xmlOptions;
    private final boolean forceStreamingEncoding;

    public SoapChainResponseWriter(EncoderRepository encoderRepository,
                                   Producer<XmlOptions> xmlOptions,
                                   boolean forceStreamingEncoding) {
        super(encoderRepository);
        this.xmlOptions = xmlOptions;
        this.forceStreamingEncoding = forceStreamingEncoding;
    }


    @Override
    public Set<ResponseWriterKey> getKeys() {
        return Collections.singleton(KEY);
    }

    @Override
    public void write(SoapChain chain, OutputStream out, ResponseProxy responseProxy) throws IOException {
        try {
            write(chain, out);
        } catch (EncodingException ex) {
            throw new IOException(ex);
        }
    }

    private void write(SoapChain chain, OutputStream out) throws EncodingException, IOException {
        String namespace = chain.getSoapResponse().getSoapNamespace();
        EncoderKey key = CodingHelper.getEncoderKey(namespace, chain.getSoapResponse());
        Encoder<?, SoapResponse> encoder = getEncoder(key);
        if (encoder == null) {
            throw new NoEncoderForKeyException(key);
        }
        write(encoder, chain, out);
    }

    private void write(Encoder<?, SoapResponse> encoder, SoapChain chain, OutputStream out)
            throws IOException, EncodingException {
        if (this.forceStreamingEncoding && encoder instanceof StreamingEncoder) {
            ((StreamingEncoder<?, ? super SoapResponse>) encoder)
                    .encode(chain.getSoapResponse(), out);
        } else {
            try {
                Object object = encoder.encode(chain.getSoapResponse());
                if (object instanceof SOAPMessage) {
                    ((SOAPMessage) object).writeTo(out);
                } else if (object instanceof XmlObject) {
                    ((XmlObject) object).save(out, this.xmlOptions.get());
                }
            } catch (SOAPException ex) {
                throw new IOException(ex);
            }
        }
    }

    @Override
    public boolean supportsGZip(SoapChain t) {
        return false;
    }

}
