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
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;

import org.n52.janmayen.Producer;
import org.n52.shetland.ogc.gml.time.TimeInstant;
import org.n52.shetland.ogc.gml.time.TimePosition;
import org.n52.shetland.util.DateTimeHelper;
import org.n52.shetland.w3c.SchemaLocation;
import org.n52.shetland.w3c.W3CConstants;
import org.n52.svalbard.encode.Encoder;
import org.n52.svalbard.encode.EncoderKey;
import org.n52.svalbard.encode.EncoderRepository;
import org.n52.svalbard.encode.EncodingContext;
import org.n52.svalbard.encode.XmlEncoderKey;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.exception.NoEncoderForKeyException;
import org.n52.svalbard.util.N52XmlHelper;

import com.google.common.base.Strings;
import com.google.common.xml.XmlEscapers;

/**
 * Abstract XML stream writer.
 *
 * @author Christian Autermann
 *
 * @param <S> object to write
 */
public abstract class XmlStreamWriter<S> {

    protected static final String XML_FRAGMENT = "xml-fragment";
    private static final Pattern NEW_LINE_PATTERN = Pattern.compile(System.lineSeparator(), Pattern.LITERAL);
    private final XMLOutputFactory outputFactory;
    private final OutputStream outputStream;
    private final EncodingContext context;
    private final EncoderRepository encoderRepository;
    private final Producer<XmlOptions> xmlOptions;
    private final Map<String, String> prefixes = new HashMap<>();
    private final XMLStreamWriter writer;
    private final S element;
    private int indent;

    public XmlStreamWriter(OutputStream outputStream,
                           EncodingContext context,
                           EncoderRepository encoderRepository,
                           Producer<XmlOptions> xmlOptions,
                           S element) throws XMLStreamException {
        this.outputStream = Objects.requireNonNull(outputStream);
        this.encoderRepository = Objects.requireNonNull(encoderRepository);
        this.xmlOptions = Objects.requireNonNull(xmlOptions);
        this.context = Objects.requireNonNull(context);

        this.outputFactory = XMLOutputFactory.newInstance();
        this.outputFactory.setProperty("escapeCharacters", false);
        this.writer = this.outputFactory.createXMLStreamWriter(this.outputStream, getEncoding());
        this.element = element;
    }

    protected S getElement() {
        return element;
    }

    /**
     * Get the XML writer.
     *
     * @return the writer
     */
    protected XMLStreamWriter getXmlWriter() {
        return writer;
    }

    /**
     * Get the encoder repository.
     *
     * @return the encoder repository
     */
    protected EncoderRepository getEncoderRepository() {
        return encoderRepository;
    }

    /**
     * Get the XML options.
     *
     * @return the xml options
     */
    protected XmlOptions getXmlOptions() {
        return this.xmlOptions.get();
    }

    /**
     * Get the xml version.
     *
     * @return the xml version
     */
    protected String getXmlVersion() {
        return this.context.get(XmlWriterSettings.XML_VERSION, "1.0");
    }

    /**
     * Get the encoding.
     *
     * @return the encoding
     */
    protected String getEncoding() {
        return this.context.get(XmlWriterSettings.XML_ENCODING, StandardCharsets.UTF_8.name());
    }

    /**
     * Get the underlying output stream.
     *
     * @return the output stream
     */
    protected OutputStream getOutputStream() {
        return this.outputStream;
    }

    /**
     * Get the current indentation level.
     *
     * @return the indentation level
     */
    protected int getIndent() {
        return this.indent;
    }

    /**
     * Get the encoding context.
     *
     * @return the context
     */
    protected EncodingContext getContext() {
        return context;
    }

    /**
     * Write attribute to stream
     *
     * @param name  Attribute name
     * @param value Attribute value
     *
     * @throws XMLStreamException If an error occurs when writing to {@link OutputStream}
     */
    protected void attr(QName name, String value) throws XMLStreamException {
        getXmlWriter().writeAttribute(name.getPrefix(), name.getNamespaceURI(), name.getLocalPart(), value);
    }

    /**
     * Write attribute to stream
     *
     * @param name  Attribute name
     * @param value Attribute value
     *
     * @throws XMLStreamException If an error occurs when writing to {@link OutputStream}
     */
    protected void attr(String name, String value) throws XMLStreamException {
        getXmlWriter().writeAttribute(name, value);
    }

    /**
     * Write attribute to stream
     *
     * @param namespace Namespace of the attribute name
     * @param localName LocalName of the attribute name
     * @param value     Attribute value
     *
     * @throws XMLStreamException If an error occurs when writing to {@link OutputStream}
     */
    protected void attr(String namespace, String localName, String value) throws XMLStreamException {
        getXmlWriter().writeAttribute(W3CConstants.NS_XSI, W3CConstants.SCHEMA_LOCATION, value);
    }

    /**
     * Write namespace to stream
     *
     * @param prefix    Namespace prefix
     * @param namespace Namespace URI
     *
     * @throws XMLStreamException If an error occurs when writing to {@link OutputStream}
     */
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

    /**
     * Write start element to stream
     *
     * @param name Element name
     *
     * @throws XMLStreamException If an error occurs when writing to {@link OutputStream}
     */
    protected void start(QName name) throws XMLStreamException {
        writeIndent(indent++);
        getXmlWriter().writeStartElement(name.getPrefix(), name.getLocalPart(), name.getNamespaceURI());
    }

    /**
     * Write document start to stream with or without
     *
     * @throws XMLStreamException If an error occurs when writing to {@link OutputStream}
     */
    protected void start() throws XMLStreamException {
        if (!isEmbedded()) {
            getXmlWriter().writeStartDocument(getEncoding(), getXmlVersion());
        }
    }

    /**
     * Write an empty element to stream
     *
     * @param name Element name
     *
     * @throws XMLStreamException If an error occurs when writing to {@link OutputStream}
     */
    protected void empty(QName name) throws XMLStreamException {
        writeIndent(indent);
        getXmlWriter().writeEmptyElement(name.getPrefix(), name.getLocalPart(), name.getNamespaceURI());
    }

    /**
     * Write characters to stream
     *
     * @param chars Characters to write
     *
     * @throws XMLStreamException If an error occurs when writing to {@link OutputStream}
     */
    protected void chars(String chars) throws XMLStreamException {
        chars(chars, true);
    }

    /**
     * Write characters to stream
     *
     * @param chars  Characters to write
     * @param escape if the chars should be XML escaped
     *
     * @throws XMLStreamException If an error occurs when writing to {@link OutputStream}
     */
    protected void chars(String chars, boolean escape) throws XMLStreamException {
        getXmlWriter().writeCharacters(escape ? XmlEscapers.xmlContentEscaper().escape(chars) : chars);
    }

    /**
     * Write the end element to new line
     *
     * @param name Element name
     *
     * @throws XMLStreamException If an error occurs when writing to {@link OutputStream}
     */
    protected void end(QName name) throws XMLStreamException {
        writeIndent(--indent);
        getXmlWriter().writeEndElement();
        flush();
    }

    /**
     * Write the document end to stream
     *
     * @throws XMLStreamException If an error occurs when writing to {@link OutputStream}
     */
    protected void end() throws XMLStreamException {
        getXmlWriter().writeEndDocument();
        flush();
    }

    /**
     * Write end element to the same line
     *
     * @param name Element name
     *
     * @throws XMLStreamException If an error occurs when writing to {@link OutputStream}
     */
    protected void endInline(QName name) throws XMLStreamException {
        --indent;
        getXmlWriter().writeEndElement();
        flush();
    }

    /**
     * Finish the stream writing, flush and close
     *
     * @throws XMLStreamException If an error occurs when writing to {@link OutputStream}
     */
    protected void finish() throws XMLStreamException {
        flush();
        getXmlWriter().close();
    }

    /**
     * Flush written elements
     *
     * @throws XMLStreamException If an error occurs when writing to {@link OutputStream}
     */
    protected void flush() throws XMLStreamException {
        getXmlWriter().flush();
    }

    /**
     * Encode and write element to the {@link OutputStream}
     *
     * @throws XMLStreamException If an error occurs when writing to {@link OutputStream}
     * @throws EncodingException  If an encoding error occurs
     */
    public abstract void write() throws XMLStreamException, EncodingException;

    /**
     * Write raw text to stream an adds current indent before writing the text (and before each subsequent line).
     *
     * @param text Text to write to stream
     *
     * @throws XMLStreamException If an error occurs when writing to {@link OutputStream}
     */
    protected void rawText(String text) throws XMLStreamException {
        Iterator<String> iter = split(NEW_LINE_PATTERN, text);
        while (iter.hasNext()) {
            writeIndent(getIndent());
            chars(iter.next(), false);
        }
    }

    /**
     * Write text to stream an adds current indent before writing the text (and before each subsequent line).
     *
     * @param text Text to write to stream
     *
     * @throws XMLStreamException If an error occurs when writing to {@link OutputStream}
     */
    protected void text(String text) throws XMLStreamException {
        Iterator<String> iter = split(NEW_LINE_PATTERN, text);
        while (iter.hasNext()) {
            writeIndent(getIndent());
            chars(iter.next(), true);
        }
    }

    /**
     * Create the replacement from {@link QName}
     *
     * @param qname {@link QName} to create replacement from
     *
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
     * @throws XMLStreamException If an error occurs when writing to {@link OutputStream}
     * @deprecated do not use it... it just flushes the output stream, else it would generate an super redundant new
     * line
     */
    @Deprecated
    protected void writeNewLine() throws XMLStreamException {
        // chars(System.lineSeperator());
        flush();
    }

    /**
     * Write {@link XmlObject} to stream and replace xml-fragment with {@link QName}
     *
     * @param xmlObject {@link XmlObject} to write
     * @param qname     Replacement for xml-fragment
     *
     * @throws XMLStreamException If an error occurs when writing to {@link OutputStream}
     */
    protected void writeXmlObject(XmlObject xmlObject, QName qname) throws XMLStreamException {
        if (xmlObject != null) {
            String s = xmlObject.xmlText(getXmlOptions());
            rawText(s.replaceAll(XML_FRAGMENT, getReplacement(qname)));
        }
    }

    /**
     * Write {@link XmlObject} to stream and replace xml-fragment with {@link QName}
     *
     * @param xmlObject {@link XmlObject} to write
     *
     * @throws XMLStreamException If an error occurs when writing to {@link OutputStream}
     */
    protected void writeXmlObject(XmlObject xmlObject) throws XMLStreamException {
        if (xmlObject != null) {
            rawText(xmlObject.xmlText(getXmlOptions()));
        }
    }

    /**
     * Write {@link SchemaLocation}s as xsi:schemaLocations attribute to stream
     *
     * @param schemaLocations {@link SchemaLocation}s to write
     *
     * @throws XMLStreamException If an error occurs when writing to {@link OutputStream}
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
     * @param level current level
     *
     * @throws XMLStreamException If an error occurs when writing to {@link OutputStream}
     */
    protected void writeIndent(int level) throws XMLStreamException {
        chars(System.lineSeparator());
        for (int i = 0; i < level; i++) {
            chars("  ");
        }
    }

    /**
     * Write {@link TimeInstant} to stream
     *
     * @param time {@link TimeInstant} to write to stream
     *
     * @throws XMLStreamException If an error occurs when writing to {@link OutputStream}
     */
    protected void time(TimeInstant time) throws XMLStreamException {
        time(time.getTimePosition());
    }

    /**
     * Write {@link TimePosition} as ISO 8601 to stream
     *
     * @param time {@link TimePosition} to write as ISO 8601 to stream
     *
     * @throws XMLStreamException If an error occurs when writing to {@link OutputStream}
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
        return this.outputFactory;
    }

    protected void addXlinkHrefAttr(String value) throws XMLStreamException {
        attr(W3CConstants.QN_XLINK_HREF, value);
    }

    protected void addXlinkTitleAttr(String value) throws XMLStreamException {
        attr(W3CConstants.QN_XLINK_TITLE, value);
    }

    private static Iterator<String> split(Pattern pattern, CharSequence input) {
        // copyied form Pattern.splitAsStream()
        return new Iterator<String>() {
            private final Matcher matcher = pattern.matcher(input);
            private int current;
            private String nextElement;
            private int emptyElementCount;

            @Override
            public String next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }

                if (emptyElementCount == 0) {
                    String n = nextElement;
                    nextElement = null;
                    return n;
                } else {
                    emptyElementCount--;
                    return "";
                }
            }

            @Override
            public boolean hasNext() {
                if (nextElement != null || emptyElementCount > 0) {
                    return true;
                }
                if (current == input.length()) {
                    return false;
                }
                while (matcher.find()) {
                    nextElement = input.subSequence(current, matcher.start()).toString();
                    current = matcher.end();
                    if (!nextElement.isEmpty()) {
                        return true;
                    } else if (current > 0) {
                        emptyElementCount++;
                    }
                }

                nextElement = input.subSequence(current, input.length()).toString();
                current = input.length();
                if (!nextElement.isEmpty()) {
                    return true;
                } else {
                    emptyElementCount = 0;
                    nextElement = null;
                    return false;
                }
            }
        };
    }

    /**
     * Get encoder for {@link EncoderKey}
     *
     * @param key Encoder key to get encoder for
     *
     * @return Matching encoder
     *
     * @throws NoEncoderForKeyException If no matching encoder was found
     */
    protected <T, S> Encoder<T, S> getEncoder(EncoderKey key) throws NoEncoderForKeyException {
        Encoder<T, S> encoder = this.encoderRepository.getEncoder(key);
        if (encoder == null) {
            throw new NoEncoderForKeyException(key);
        }
        return encoder;
    }

    protected <T, S> Encoder<T, S> getEncoder(String namespace, Object o) throws NoEncoderForKeyException {
        return getEncoder(new XmlEncoderKey(namespace, o.getClass()));
    }

    protected <T, S> Optional<Encoder<T, S>> getEncoder() {
        return Optional.ofNullable(this.context.get(XmlWriterSettings.ENCODER));
    }

    protected boolean isEmbedded() {
        return this.context.getBoolean(XmlWriterSettings.EMBEDDED);
    }

    protected boolean isAddSchemaLocation() {
        return this.context.getBoolean(XmlWriterSettings.ADD_SCHEMA_LOCATION);
    }

    protected Optional<String> getEncodeNamespace() {
        return Optional.ofNullable(this.context.getString(XmlWriterSettings.ENCODE_NAMESPACE));
    }

    public enum XmlWriterSettings {
        XML_VERSION,
        XML_ENCODING,
        INDENT,
        EMBEDDED,
        ENCODER,
        ENCODE_NAMESPACE,
        ADD_SCHEMA_LOCATION
    }
}
