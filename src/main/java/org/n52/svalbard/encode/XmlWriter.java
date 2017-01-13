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
package org.n52.svalbard.encode;

import java.io.OutputStream;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.xmlbeans.XmlObject;

import org.n52.shetland.ogc.gml.time.TimeInstant;
import org.n52.shetland.ogc.gml.time.TimePosition;
import org.n52.shetland.util.DateTimeHelper;
import org.n52.shetland.w3c.SchemaLocation;
import org.n52.shetland.w3c.W3CConstants;
import org.n52.svalbard.EncodingValues;
import org.n52.svalbard.N52XmlHelper;
import org.n52.svalbard.XmlOptionsHelper;
import org.n52.svalbard.encode.exception.EncodingException;

import com.google.common.base.StandardSystemProperty;
import com.google.common.base.Strings;

/**
 * Abstract XML writer class
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 4.0.2
 *
 * @param <T>
 *            concrete writer, e.g, {@link XMLStreamWriter} or
 *            {@link XMLEventWriter}
 * @param <S>
 *            object to write
 */
public abstract class XmlWriter<T, S> {

    protected static final String XML_VERSION = "1.0";
    protected static final String ENCODING = "UTF-8";
    protected static final String XML_FRAGMENT = "xml-fragment";

    protected int indent = 0;
    protected OutputStream out;

    private final XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();

    /**
     * Encode and write element to the {@link OutputStream}
     *
     * @param out
     *            OutputStream to write the encoded element
     * @throws XMLStreamException
     *             If an error occurs when writing to {@link OutputStream}
     * @throws EncodingException
     *             If an encoding error occurs
     */
    public abstract void write(OutputStream out) throws XMLStreamException, EncodingException;

    /**
     * Encode and write element to the {@link OutputStream}
     *
     * @param out
     *            OutputStream to write the encoded element
     * @param encodingValues
     *            {@link EncodingValues} with additional information
     * @throws XMLStreamException
     *             If an error occurs when writing to {@link OutputStream}
     * @throws EncodingException
     *             If an encoding error occurs
     */
    public abstract void write(OutputStream out, EncodingValues encodingValues) throws XMLStreamException,
            EncodingException;

    /**
     * Encode and write the elementToStream to the {@link OutputStream}
     *
     * @param elementToStream
     *            Element to encode and write to stream
     * @param out
     *            OutputStream to write the encoded element
     * @throws XMLStreamException
     *             If an error occurs when writing to {@link OutputStream}
     * @throws EncodingException
     *             If an encoding error occurs
     */
    public abstract void write(S elementToStream, OutputStream out) throws XMLStreamException, EncodingException;

    /**
     * Encode and write the elementToStream to the {@link OutputStream}
     *
     * @param elementToStream
     *            Element to encode and write to stream
     * @param out
     *            OutputStream to write the encoded element
     * @param encodingValues
     *            {@link EncodingValues} with additional information
     * @throws XMLStreamException
     *             If an error occurs when writing to {@link OutputStream}
     * @throws EncodingException
     *             If an encoding error occurs
     */
    public abstract void write(S elementToStream, OutputStream out, EncodingValues encodingValues)
            throws XMLStreamException, EncodingException;

    /**
     * Initialize this XML stream writer
     *
     * @param out
     *            OutputStream to write the encoded element
     * @param encoding
     *            Encoding, e.g. UTF-8
     * @param encodingValues
     *            {@link EncodingValues} with additional information
     * @throws XMLStreamException
     *             If an error occurs when initializing the writer
     */
    protected abstract void init(OutputStream out, String encoding, EncodingValues encodingValues)
            throws XMLStreamException;

    /**
     * Get the XML writer
     *
     * @return the writer
     */
    protected abstract T getXmlWriter();

    /**
     * Write attribute to stream
     *
     * @param name
     *            Attribute name
     * @param value
     *            Attribute value
     * @throws XMLStreamException
     *             If an error occurs when writing to {@link OutputStream}
     */
    protected abstract void attr(QName name, String value) throws XMLStreamException;

    /**
     * Write attribute to stream
     *
     * @param name
     *            Attribute name
     * @param value
     *            Attribute value
     * @throws XMLStreamException
     *             If an error occurs when writing to {@link OutputStream}
     */
    protected abstract void attr(String name, String value) throws XMLStreamException;

    /**
     * Write attribute to stream
     *
     * @param namespace
     *            Namespace of the attribute name
     * @param localName
     *            LocalName of the attribute name
     * @param value
     *            Attribute value
     * @throws XMLStreamException
     *             If an error occurs when writing to {@link OutputStream}
     */
    protected abstract void attr(String namespace, String localName, String value) throws XMLStreamException;

    /**
     * Write namespace to stream
     *
     * @param prefix
     *            Namespace prefix
     * @param namespace
     *            Namespace URI
     * @throws XMLStreamException
     *             If an error occurs when writing to {@link OutputStream}
     */
    protected abstract void namespace(String prefix, String namespace) throws XMLStreamException;

    /**
     * Write start element to stream
     *
     * @param name
     *            Element name
     * @throws XMLStreamException
     *             If an error occurs when writing to {@link OutputStream}
     */
    protected abstract void start(QName name) throws XMLStreamException;

    /**
     * Write document start to stream with or without
     *
     * @param embedded
     *            if <code>false</code>, XML version and encoding written to
     *            stream
     * @throws XMLStreamException
     *             If an error occurs when writing to {@link OutputStream}
     */
    protected abstract void start(boolean embedded) throws XMLStreamException;

    /**
     * Write an empty element to stream
     *
     * @param name
     *            Element name
     * @throws XMLStreamException
     *             If an error occurs when writing to {@link OutputStream}
     */
    protected abstract void empty(QName name) throws XMLStreamException;

    /**
     * Write characters to stream
     *
     * @param chars
     *            Characters to write
     * @throws XMLStreamException
     *             If an error occurs when writing to {@link OutputStream}
     */
    protected abstract void chars(String chars) throws XMLStreamException;

    /**
     * Write characters to stream
     *
     * @param chars
     *            Characters to write
     * @param escape if the chars should be XML escaped
     * @throws XMLStreamException
     *             If an error occurs when writing to {@link OutputStream}
     */
    protected abstract void chars(String chars, boolean escape) throws XMLStreamException;

    /**
     * Write the end element to new line
     *
     * @param name
     *            Element name
     * @throws XMLStreamException
     *             If an error occurs when writing to {@link OutputStream}
     */
    protected abstract void end(QName name) throws XMLStreamException;

    /**
     * Write end element to the same line
     *
     * @param name
     *            Element name
     * @throws XMLStreamException
     *             If an error occurs when writing to {@link OutputStream}
     */
    protected abstract void endInline(QName name) throws XMLStreamException;

    /**
     * Write the document end to stream
     *
     * @throws XMLStreamException
     *             If an error occurs when writing to {@link OutputStream}
     */
    protected abstract void end() throws XMLStreamException;

    /**
     * Finish the stream writing, flush and close
     *
     * @throws XMLStreamException
     *             If an error occurs when writing to {@link OutputStream}
     */
    protected abstract void finish() throws XMLStreamException;

    /**
     * Flush written elements
     *
     * @throws XMLStreamException
     *             If an error occurs when writing to {@link OutputStream}
     */
    protected abstract void flush() throws XMLStreamException;

    /**
     * Write raw text to stream an adds current indent before writing the text.
     *
     * @param text
     *            Text to write to stream
     * @throws XMLStreamException
     *             If an error occurs when writing to {@link OutputStream}
     */
    protected void rawText(String text) throws XMLStreamException {
        writeIndent(indent);
        chars(text, false);
    }

    /**
     * Create the replacement from {@link QName}
     *
     * @param qname
     *            {@link QName} to create replacement from
     * @return Created replacement
     */
    protected String getReplacement(QName qname) {
        StringBuilder builder = new StringBuilder();
        if (!Strings.isNullOrEmpty(qname.getPrefix())) {
            builder.append(qname.getPrefix());
            builder.append(':');
        }
        builder.append(qname.getLocalPart());
        return builder.toString();
    }

    /**
     * Write new line to stream
     *
     * @throws XMLStreamException
     *             If an error occurs when writing to {@link OutputStream}
     */
    protected void writeNewLine() throws XMLStreamException {
        chars(StandardSystemProperty.LINE_SEPARATOR.value());
        flush();
    }

    /**
     * Write {@link XmlObject} to stream and replace xml-fragment with
     * {@link QName}
     *
     * @param xmlObject
     *            {@link XmlObject} to write
     * @param qname
     *            Replacement for xml-fragment
     * @throws XMLStreamException
     *             If an error occurs when writing to {@link OutputStream}
     */
    protected void writeXmlObject(XmlObject xmlObject, QName qname) throws XMLStreamException {
        if (xmlObject != null) {
            String s = xmlObject.xmlText(XmlOptionsHelper.getInstance().getXmlOptions());
            rawText(s.replaceAll(XML_FRAGMENT, getReplacement(qname)));
        }
    }

    /**
     * Write {@link SchemaLocation}s as xsi:schemaLocations attribute to stream
     *
     * @param schemaLocations
     *            {@link SchemaLocation}s to write
     * @throws XMLStreamException
     *             If an error occurs when writing to {@link OutputStream}
     */
    protected void schemaLocation(Set<SchemaLocation> schemaLocations) throws XMLStreamException {
        String merged = N52XmlHelper.mergeSchemaLocationsToString(schemaLocations);
        if (!Strings.isNullOrEmpty(merged)) {
            namespace(W3CConstants.NS_XSI_PREFIX, W3CConstants.NS_XSI);
            attr(W3CConstants.NS_XSI, W3CConstants.SCHEMA_LOCATION, merged);
        }
    }

    /**
     * Write indent to stream
     *
     * @param level
     *            current level
     * @throws XMLStreamException
     *             If an error occurs when writing to {@link OutputStream}
     */
    protected void writeIndent(int level) throws XMLStreamException {
        chars("\n");
        for (int i = 0; i < level; i++) {
            chars("  ");
        }
    }

    /**
     * Initialize this XML stream writer, calls
     * {@link #init(OutputStream, String)}
     *
     * @param out
     *            OutputStream to write the encoded element
     * @throws XMLStreamException
     *             If an error occurs when initializing the writer
     */
    protected void init(OutputStream out) throws XMLStreamException {
        init(out, ENCODING);
    }

    /**
     * Initialize this XML stream writer, calls
     * {@link #init(OutputStream, String, EncodingValues)}
     *
     * @param out
     *            OutputStream to write the encoded element
     * @param encodingValues
     *            {@link EncodingValues} with additional information
     * @throws XMLStreamException
     *             If an error occurs when initializing the writer
     */
    protected void init(OutputStream out, EncodingValues encodingValues) throws XMLStreamException {
        init(out, ENCODING, encodingValues);
    }

    /**
     * Initialize this XML stream writer, calls
     * {@link #init(OutputStream, String, EncodingValues)}
     *
     * @param out
     *            OutputStream to write the encoded element
     * @param encoding
     *            Encoding, e.g. UTF-8
     * @throws XMLStreamException
     *             If an error occurs when initializing the writer
     */
    protected void init(OutputStream out, String encoding) throws XMLStreamException {
        init(out, encoding, new EncodingValues());
    }

    /**
     * Get the {@link OutputStream}
     *
     * @return the {@link OutputStream}
     */
    protected OutputStream getOutputStream() {
        return out;
    }

    /**
     * Write {@link TimeInstant} to stream
     *
     * @param time
     *            {@link TimeInstant} to write to stream
     * @throws XMLStreamException
     *             If an error occurs when writing to {@link OutputStream}
     */
    protected void time(TimeInstant time) throws XMLStreamException {
        time(time.getTimePosition());
    }

    /**
     * Write {@link TimePosition} as ISO 8601 to stream
     *
     * @param time
     * @link TimePosition} to write as ISO 8601 to stream
     * @throws XMLStreamException
     *             If an error occurs when writing to {@link OutputStream}
     */
    protected void time(TimePosition time) throws XMLStreamException {
        chars(DateTimeHelper.formatDateTime2IsoString(time.getTime()));
    }

    /**
     * Get the {@link XMLOutputFactory}
     *
     * @return the {@link XMLOutputFactory}
     */
    protected XMLOutputFactory getXmlOutputFactory() {
        this.outputFactory.setProperty("escapeCharacters", false);
        return this.outputFactory;
    }

    protected void addXlinkHrefAttr(String value) throws XMLStreamException {
        attr(W3CConstants.QN_XLINK_HREF, value);
    }

    protected void addXlinkTitleAttr(String value) throws XMLStreamException {
        attr(W3CConstants.QN_XLINK_TITLE, value);
    }
}
