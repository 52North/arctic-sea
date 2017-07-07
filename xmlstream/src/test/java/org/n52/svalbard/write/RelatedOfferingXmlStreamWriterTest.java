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

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.xml.stream.XMLStreamException;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.junit.Test;
import org.n52.shetland.ogc.sos.ro.RelatedOfferings;
import org.n52.svalbard.encode.EncoderFlags;
import org.n52.svalbard.encode.EncoderRepository;
import org.n52.svalbard.encode.EncodingContext;
import org.n52.svalbard.encode.exception.EncodingException;

public class RelatedOfferingXmlStreamWriterTest {

    @Test
    public void should_encode_relatedOfferings()
            throws XMLStreamException, EncodingException, XmlException, IOException {
        RelatedOfferings ro = new RelatedOfferings();
        ro.addValue("role_1", "offering_1");
        ro.addValue("role_2", "offering_2");
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            new RelatedOfferingXmlStreamWriter(
                    EncodingContext.of(EncoderFlags.ENCODER_REPOSITORY, new EncoderRepository()), out, ro).write();
            XmlObject.Factory.parse(new String(out.toByteArray()));
        }
    }

}
