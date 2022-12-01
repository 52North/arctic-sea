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
package org.n52.svalbard.encode;

import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Set;

import javax.xml.stream.XMLStreamException;

import org.apache.xmlbeans.XmlObject;
import org.n52.janmayen.http.MediaType;
import org.n52.janmayen.http.MediaTypes;
import org.n52.shetland.rdf.RDF;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.util.CodingHelper;
import org.n52.svalbard.write.RdfStreamWriter;

public class RdfEncoder implements StreamingEncoder<XmlObject, RDF> {

    @Override
    public Set<EncoderKey> getKeys() {
        return CodingHelper.encoderKeysForElements(org.apache.jena.vocabulary.RDF.uri, RDF.class);
    }

    @Override
    public XmlObject encode(RDF rdf) throws EncodingException {
        return encode(rdf, EncodingContext.empty());
    }

    @Override
    public XmlObject encode(RDF rdf, EncodingContext additionalValues) throws EncodingException {
        try (PipedInputStream pis = new PipedInputStream(); PipedOutputStream pos = new PipedOutputStream()) {
            pis.connect(pos);
            encode(rdf, pos);
            return XmlObject.Factory.parse(pis);
        } catch (Exception e) {
            throw new EncodingException("Error while encoding element!", e);
        }
    }

    @Override
    public void encode(RDF rdf, OutputStream outputStream, EncodingContext context)
            throws EncodingException {
        try {
            new RdfStreamWriter(context, outputStream, rdf).write();
        } catch (XMLStreamException xmlse) {
            throw new EncodingException("Error while writing element to stream!", xmlse);
        }
    }

    @Override
    public MediaType getContentType() {
        return MediaTypes.TEXT_XML;
    }

}
