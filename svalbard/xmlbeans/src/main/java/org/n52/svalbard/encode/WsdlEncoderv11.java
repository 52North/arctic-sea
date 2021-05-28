/*
 * Copyright (C) 2015-2021 52°North Spatial Information Research GmbH
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Set;

import javax.xml.stream.XMLStreamException;

import org.apache.xmlbeans.XmlObject;
import org.n52.janmayen.http.MediaType;
import org.n52.janmayen.http.MediaTypes;
import org.n52.shetland.w3c.wsdl.Definitions;
import org.n52.shetland.w3c.wsdl.WSDLConstants;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.util.CodingHelper;
import org.n52.svalbard.write.Wsdlv11XmlStreamWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;

public class WsdlEncoderv11 extends AbstractXmlEncoder<XmlObject, Definitions>
        implements StreamingEncoder<XmlObject, Definitions> {

    private static final Logger LOGGER = LoggerFactory.getLogger(WsdlEncoderv11.class);

    private static final Set<EncoderKey> ENCODER_KEYS =
            CodingHelper.encoderKeysForElements(WSDLConstants.NS_WSDL, Definitions.class);

    public WsdlEncoderv11() {
        LOGGER.debug("Encoder for the following keys initialized successfully: {}!", Joiner.on(", ")
                .join(ENCODER_KEYS));
    }

    @Override
    public Set<EncoderKey> getKeys() {
        return Collections.unmodifiableSet(ENCODER_KEYS);
    }

    @Override
    public MediaType getContentType() {
        return MediaTypes.APPLICATION_XML;
    }

    @Override
    public XmlObject encode(Definitions element, EncodingContext ctx) throws EncodingException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            encode(element, baos, ctx);
            ByteArrayInputStream in = new ByteArrayInputStream(baos.toByteArray());
            return XmlObject.Factory.parse(in);
        } catch (Exception e) {
            throw new EncodingException(e);
        }
    }

    @Override
    public void encode(Definitions element, OutputStream outputStream, EncodingContext ctx)
            throws EncodingException {
        try {
            if (!ctx.has(EncoderFlags.ENCODER_REPOSITORY)) {
                new Wsdlv11XmlStreamWriter(ctx.with(EncoderFlags.ENCODER_REPOSITORY, getEncoderRepository())
                        .with(StreamingEncoderFlags.EMBEDDED, false), outputStream, element).write();
            } else {
                new Wsdlv11XmlStreamWriter(ctx.with(StreamingEncoderFlags.EMBEDDED, false), outputStream, element)
                        .write();
            }
        } catch (XMLStreamException xmlse) {
            throw new EncodingException("Error while writing element to stream!", xmlse);
        }

    }

}
