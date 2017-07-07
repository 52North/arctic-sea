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

import static org.hamcrest.Matchers.is;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;

import org.junit.Assert;
import org.junit.Test;

import org.n52.svalbard.write.util.IndentingXMLStreamWriter;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class IndentingXmlStreamWriterTest {

    @Test
    public void test() throws XMLStreamException, UnsupportedEncodingException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        writeXML(baos);
        Assert.assertThat(baos.toString("UTF-8"), is("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + System.lineSeparator() +
                                                     "<document attribute=\"attributeValue\">" + System.lineSeparator() +
                                                     "    <startElement attribute=\"attributeValue\"></startElement>" + System.lineSeparator() +
                                                     "    <emptyElement attribute=\"attributeValue\"/>" + System.lineSeparator() +
                                                     "    <startElement>non-line-breaking-text</startElement>" + System.lineSeparator() +
                                                     "    <startElement>non-line-breaking-text</startElement>" + System.lineSeparator() +
                                                     "    <startElement>non-line-breaking-text</startElement>" + System.lineSeparator() +
                                                     "    <startElement>non-line-breaking-text</startElement>" + System.lineSeparator() +
                                                     "    <startElement>" + System.lineSeparator() +
                                                     "        line-breaking-text" + System.lineSeparator() +
                                                     "        line-breaking-text" + System.lineSeparator() +
                                                     "        line-breaking-text" + System.lineSeparator() +
                                                     "        line-breaking-text" + System.lineSeparator() +
                                                     "    </startElement>" + System.lineSeparator() +
                                                     "    <startElement>" + System.lineSeparator() +
                                                     "        line-breaking-text" + System.lineSeparator() +
                                                     "        line-breaking-text" + System.lineSeparator() +
                                                     "        line-breaking-text" + System.lineSeparator() +
                                                     "        line-breaking-text" + System.lineSeparator() +
                                                     "    </startElement>" + System.lineSeparator() +
                                                     "    <emptyElement attribute=\"attributeValue\"/>" + System.lineSeparator() +
                                                     "    <some-externally-generated-xml/>" + System.lineSeparator() +
                                                     "    <some-externally-generated-xml>" + System.lineSeparator() +
                                                     "        <some-inner-tag/>" + System.lineSeparator() +
                                                     "    </some-externally-generated-xml>" + System.lineSeparator() +
                                                     "    <wrapper1>" + System.lineSeparator() +
                                                     "        <wrapper2>" + System.lineSeparator() +
                                                     "            <some-externally-generated-xml>" + System.lineSeparator() +
                                                     "                <some-inner-tag/>" + System.lineSeparator() +
                                                     "            </some-externally-generated-xml>" + System.lineSeparator() +
                                                     "        </wrapper2>" + System.lineSeparator() +
                                                     "    </wrapper1>" + System.lineSeparator() +
                                                     "</document>"));
    }

    private void writeXML(ByteArrayOutputStream baos) throws FactoryConfigurationError, XMLStreamException,
                                                             IllegalArgumentException {
        XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
        outputFactory.setProperty("escapeCharacters", false);
        IndentingXMLStreamWriter writer = new IndentingXMLStreamWriter(outputFactory
                .createXMLStreamWriter(baos, "UTF-8"), 4);

        writer.writeStartDocument("UTF-8", "1.0");

            writer.writeStartElement("document");
                writer.writeAttribute("attribute", "attributeValue");

                writer.writeStartElement("startElement");
                    writer.writeAttribute("attribute", "attributeValue");
                writer.writeEndElement();

                writer.writeEmptyElement("emptyElement");
                    writer.writeAttribute("attribute", "attributeValue");

                writer.writeStartElement("startElement");
                    writer.writeCharacters("non-line-breaking-text");
                writer.writeEndElement();

                writer.writeStartElement("startElement");
                    writer.writeCharacters("non-line-breaking-text" + System.lineSeparator() );
                writer.writeEndElement();

                writer.writeStartElement("startElement");
                    writer.writeCharacters("" + System.lineSeparator() + "non-line-breaking-text");
                writer.writeEndElement();

                writer.writeStartElement("startElement");
                    writer.writeCharacters("" + System.lineSeparator() + "non-line-breaking-text" + System.lineSeparator() );
                writer.writeEndElement();

                writer.writeStartElement("startElement");
                    writer.writeCharacters("line-breaking-text" + System.lineSeparator() + "line-breaking-text" + System.lineSeparator() + "line-breaking-text" + System.lineSeparator() + "line-breaking-text" + System.lineSeparator() );
                writer.writeEndElement();

                writer.writeStartElement("startElement");
                    writer.writeCharacters("line-breaking-text" + System.lineSeparator() + "line-breaking-text" + System.lineSeparator() );
                    writer.writeCharacters("line-breaking-text" + System.lineSeparator() + "line-breaking-text" + System.lineSeparator() );
                writer.writeEndElement();

                writer.writeEmptyElement("emptyElement");
                    writer.writeAttribute("attribute", "attributeValue");

                writer.writeXML("<some-externally-generated-xml/>");
                writer.writeXML("<some-externally-generated-xml>" + System.lineSeparator() + "    <some-inner-tag/>" + System.lineSeparator() + "</some-externally-generated-xml>");

                writer.writeStartElement("wrapper1");
                    writer.writeStartElement("wrapper2");
                        writer.writeXML("<some-externally-generated-xml>" + System.lineSeparator() + "    <some-inner-tag/>" + System.lineSeparator() + "</some-externally-generated-xml>");
                    writer.writeEndElement();
                writer.writeEndElement();

            writer.writeEndElement();

        writer.writeEndDocument();

        writer.flush();
        writer.close();
    }
}
