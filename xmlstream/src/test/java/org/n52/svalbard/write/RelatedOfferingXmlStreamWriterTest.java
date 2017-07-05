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
package org.n52.svalbard.ro.encode.streaming;

import java.io.ByteArrayOutputStream;

import javax.xml.stream.XMLStreamException;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.junit.Test;
import org.n52.sos.ogc.ows.OwsExceptionReport;
import org.n52.sos.ogc.sos.RelatedOfferings;

public class RelatedOfferingXmlStreamWriterTest {

    private RelatedOfferingXmlStreamWriter writer = new RelatedOfferingXmlStreamWriter();

    @Test
    public void should_encode_relatedOfferings() throws XMLStreamException, OwsExceptionReport, XmlException {
        RelatedOfferings ro = new RelatedOfferings();
        ro.addValue("role_1", "offering_1");
        ro.addValue("role_2", "offering_2");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        writer.write(ro, out);
        XmlObject.Factory.parse(new String(out.toByteArray()));
    }

}
