/*
 * Copyright 2015-2021 52°North Initiative for Geospatial Open Source
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
package org.n52.iceland.util;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class XmlFactories {
    private static final String XML_VERSION = "1.0";
    private static final String INDENT_AMOUNT = "{http://xml.apache.org/xslt}indent-amount";

    private final TransformerFactory transformerFactory;
    private final XMLEventFactory eventFactory;
    private final XMLOutputFactory outputFactory;
    private final XMLInputFactory inputFactory;

    private final Charset DOCUMENT_ENCODING = StandardCharsets.UTF_8;

    public XmlFactories() {
        this.inputFactory = createInputFactory();
        this.outputFactory = createOutputFactory();
        this.eventFactory = createEventFactory();
        this.transformerFactory = createTransformerFactory();
    }

    /**
     * @return the event factory
     */
    public XMLEventFactory eventFactory() {
        return eventFactory;
    }

    /**
     * @return the output factory
     */
    public XMLOutputFactory outputFactory() {
        return outputFactory;
    }

    /**
     * @return the input factory
     */
    public XMLInputFactory inputFactory() {
        return inputFactory;
    }

    /**
     * @return the document encoding
     */
    public Charset documentEncoding() {
        return DOCUMENT_ENCODING;
    }

    /**
     * @return the document version
     */
    public String documentVersion() {
        return XML_VERSION;
    }

    /**
     * @return the transformer factory
     */
    public TransformerFactory transformerFactory() {
        return transformerFactory;
    }

    private TransformerFactory createTransformerFactory() {
        TransformerFactory factory = TransformerFactory.newInstance();
        //factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "false");
        //factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "false");
        return factory;
    }

    private XMLInputFactory createInputFactory() {
        XMLInputFactory factory = XMLInputFactory.newFactory();
        return factory;
    }

    private XMLOutputFactory createOutputFactory() {
        XMLOutputFactory factory = XMLOutputFactory.newFactory();

        factory.setProperty("escapeCharacters", false);

        return factory;
    }

    private XMLEventFactory createEventFactory() {
        XMLEventFactory factory = XMLEventFactory.newFactory();
        return factory;
    }

    public Transformer createIndentingTransformer()
            throws TransformerException {
        Transformer transformer = transformerFactory().newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.VERSION, XML_VERSION);
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

        try {
            transformer.setOutputProperty(INDENT_AMOUNT, "2");
        } catch (IllegalArgumentException e) {
            // ignore
        }

        return transformer;
    }

    public XMLStreamException unexpectedTag(StartElement element) {
        String message = String.format("unexpected tag: %s", element.getName());
        return new XMLStreamException(message, element.getLocation());
    }

    public XMLStreamException eof() {
        return new XMLStreamException("premature end of file");
    }

}
