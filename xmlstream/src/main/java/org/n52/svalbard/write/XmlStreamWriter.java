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
import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.n52.shetland.w3c.W3CConstants;
import org.n52.svalbard.encode.EncodingValues;

import com.google.common.xml.XmlEscapers;

/**
 * Abstract {@link XmlWriter} class for {@link XMLStreamWriter}
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 4.0.2
 *
 */
public abstract class XmlStreamWriter<S> extends XmlWriter<XMLStreamWriter, S> {

    private final Map<String, String> prefixes = new HashMap<>();

    private XMLStreamWriter w;
    private int indent = 0;

    @Override
    protected void init(OutputStream out, String encoding, EncodingValues encodingValues) throws XMLStreamException {
        this.w = getXmlOutputFactory().createXMLStreamWriter(out, encoding);
        this.out = out;
        indent = encodingValues.getIndent();
    }

    @Override
    protected XMLStreamWriter getXmlWriter() {
        return w;
    }

    @Override
    protected void attr(QName name, String value) throws XMLStreamException {
        getXmlWriter().writeAttribute(name.getPrefix(), name.getNamespaceURI(), name.getLocalPart(), value);
    }

    @Override
    protected void attr(String name, String value) throws XMLStreamException {
        getXmlWriter().writeAttribute(name, value);
    }

    @Override
    protected void attr(String namespace, String localName, String value) throws XMLStreamException {
        getXmlWriter().writeAttribute(W3CConstants.NS_XSI, W3CConstants.SCHEMA_LOCATION, value);
    }

    @Override
    protected void namespace(String prefix, String namespace) throws XMLStreamException {
        String ns = prefixes.get(prefix);
        if (ns == null) {
            getXmlWriter().writeNamespace(prefix, namespace);
            prefixes.put(prefix, namespace);
        } else {
            if (!ns.equals(namespace)) {
                throw new XMLStreamException("Prefix <" + prefix + "> is already bound to <" + ns + ">");
            }
        }
    }

    @Override
    protected void start(QName name) throws XMLStreamException {
        writeIndent(indent++);
        getXmlWriter().writeStartElement(name.getPrefix(), name.getLocalPart(), name.getNamespaceURI());
    }

    @Override
    protected void start(boolean embedded) throws XMLStreamException {
        if (!embedded) {
            getXmlWriter().writeStartDocument(ENCODING, XML_VERSION);
        }
    }

    @Override
    protected void empty(QName name) throws XMLStreamException {
        writeIndent(indent);
        getXmlWriter().writeEmptyElement(name.getPrefix(), name.getLocalPart(), name.getNamespaceURI());
    }

    @Override
    protected void chars(String chars) throws XMLStreamException {
        chars(chars, true);
    }

    @Override
    protected void chars(String chars, boolean escape) throws XMLStreamException {
        getXmlWriter().writeCharacters(escape ? XmlEscapers.xmlContentEscaper().escape(chars) : chars);
    }

    @Override
    protected void end(QName name) throws XMLStreamException {
        writeIndent(--indent);
        getXmlWriter().writeEndElement();
        flush();
    }

    @Override
    protected void endInline(QName name) throws XMLStreamException {
        --indent;
        getXmlWriter().writeEndElement();
        flush();
    }

    @Override
    protected void end() throws XMLStreamException {
        getXmlWriter().writeEndDocument();
        flush();
    }

    @Override
    protected void finish() throws XMLStreamException {
        flush();
        getXmlWriter().close();
    }

    @Override
    protected void flush() throws XMLStreamException {
        getXmlWriter().flush();
    }

    @Override
    public int getIndent() {
        return this.indent;
    }

}
