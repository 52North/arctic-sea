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
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;

import org.n52.shetland.ogc.gml.time.TimeInstant;
import org.n52.shetland.ogc.gml.time.TimePosition;
import org.n52.shetland.util.DateTimeHelper;
import org.n52.shetland.w3c.SchemaLocation;
import org.n52.shetland.w3c.W3CConstants;
import org.n52.svalbard.encode.Encoder;
import org.n52.svalbard.encode.EncoderFlags;
import org.n52.svalbard.encode.EncoderKey;
import org.n52.svalbard.encode.EncoderRepository;
import org.n52.svalbard.encode.EncodingContext;
import org.n52.svalbard.encode.StreamingEncoderFlags;
import org.n52.svalbard.encode.XmlEncoderFlags;
import org.n52.svalbard.encode.XmlEncoderKey;
import org.n52.svalbard.encode.XmlStreamEncoderFlags;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.exception.NoEncoderForKeyException;
import org.n52.svalbard.util.N52XmlHelper;
import org.n52.svalbard.write.util.ExtendedXMLStreamWriter;
import org.n52.svalbard.write.util.IndentingXMLStreamWriter;

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
    private static final String OUTPUT_PROPERTY_ESCAPE_CHARACTERS = "escapeCharacters";
    private static final int INDENTATION = 2;
    private EncodingContext context;
    private final S element;
    private final EncoderRepository encoderRepository;
    private final OutputStream outputStream;
    private final Supplier<XmlOptions> xmlOptions;
    private final ExtendedXMLStreamWriter writer;
    private final String xmlVersion;
    private final String xmlEncoding;
    private final boolean close;
    private final boolean embedded;

    public XmlStreamWriter(EncodingContext context, OutputStream outputStream, S element) throws XMLStreamException {
        this.context = Objects.requireNonNull(context);
        this.outputStream = Objects.requireNonNull(outputStream);
        this.element = element;
        this.encoderRepository = context.require(EncoderFlags.ENCODER_REPOSITORY);
        this.xmlOptions = context.get(XmlEncoderFlags.XML_OPTIONS, XmlOptions::new);
        this.xmlVersion = context.get(XmlEncoderFlags.XML_VERSION, "1.0");
        this.xmlEncoding = context.get(EncoderFlags.ENCODING, StandardCharsets.UTF_8.name());
        this.embedded = context.getBoolean(StreamingEncoderFlags.EMBEDDED);

        if (context.has(XmlStreamEncoderFlags.XML_WRITER)) {
            this.writer = context.require(XmlStreamEncoderFlags.XML_WRITER);
            this.close = false;
        } else {
            XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
            if (outputFactory.isPropertySupported(OUTPUT_PROPERTY_ESCAPE_CHARACTERS)) {
                outputFactory.setProperty(OUTPUT_PROPERTY_ESCAPE_CHARACTERS, false);
            }
            this.writer = new IndentingXMLStreamWriter(outputFactory
                    .createXMLStreamWriter(this.outputStream, this.xmlEncoding), INDENTATION);
            this.context = this.context.with(XmlStreamEncoderFlags.XML_WRITER, this.writer);
            this.close = true;
        }
    }

    protected S getElement() {
        return element;
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
     * Get the underlying output stream. This will flush this writer.
     *
     * @return the output stream
     *
     * @throws javax.xml.stream.XMLStreamException if the flush fails
     */
    protected OutputStream getOutputStream() throws XMLStreamException {
        // flush the writer before returning the stream
        // to be sure nobody interfers with our stuff
        flush();
        return this.outputStream;
    }

    /**
     * Get the encoding context.
     *
     * @return the context
     */
    protected EncodingContext getContext() {
        return this.context;
    }

    /**
     * Write attribute to stream.
     *
     * @param name  Attribute name
     * @param value Attribute value
     *
     * @throws XMLStreamException If an error occurs when writing to {@link OutputStream}
     */
    protected void attr(QName name, String value) throws XMLStreamException {
        this.writer.writeAttribute(name.getPrefix(), name.getNamespaceURI(), name.getLocalPart(), value);
    }

    /**
     * Write attribute to stream.
     *
     * @param name  Attribute name
     * @param value Attribute value
     *
     * @throws XMLStreamException If an error occurs when writing to {@link OutputStream}
     */
    protected void attr(String name, String value) throws XMLStreamException {
        this.writer.writeAttribute(name, value);
    }

    /**
     * Write attribute to stream.
     *
     * @param namespace namespace of the attribute name
     * @param localName LocalName of the attribute name
     * @param value     Attribute value
     *
     * @throws XMLStreamException If an error occurs when writing to {@link OutputStream}
     */
    protected void attr(String namespace, String localName, String value) throws XMLStreamException {
        this.writer.writeAttribute(namespace, localName, value);
    }

    /**
     * Write namespace to stream.
     *
     * @param prefix    Namespace prefix
     * @param namespace Namespace URI
     *
     * @throws XMLStreamException If an error occurs when writing to {@link OutputStream}
     */
    protected void namespace(String prefix, String namespace) throws XMLStreamException {
        String ns = this.writer.getNamespaceContext().getNamespaceURI(prefix);
        if (ns == null || ns.isEmpty()) {
            this.writer.writeNamespace(prefix, namespace);
        } else if (!ns.equals(namespace)) {
            throw prefixAlreadyBound(prefix, ns);
        }
    }

    /**
     * Write start element to stream.
     *
     * @param name Element name
     *
     * @throws XMLStreamException If an error occurs when writing to {@link OutputStream}
     */
    protected void start(QName name) throws XMLStreamException {
        String prefix = name.getPrefix();
        String ns = this.writer.getNamespaceContext().getNamespaceURI(prefix);
        boolean alreadySet = ns != null && !ns.isEmpty();
        if (alreadySet && !ns.equals(name.getNamespaceURI())) {
            throw prefixAlreadyBound(prefix, ns);
        }
        this.writer.writeStartElement(prefix, name.getLocalPart(), name.getNamespaceURI());
        if (!alreadySet) {
            this.writer.writeNamespace(prefix, name.getNamespaceURI());
        }
    }

    /**
     * Write document start to stream with or without.
     *
     * @throws XMLStreamException If an error occurs when writing to {@link OutputStream}
     */
    protected void start() throws XMLStreamException {
        if (!this.embedded) {
            this.writer.writeStartDocument(this.context.getEncoding(), this.xmlVersion);
        }
    }

    /**
     * Write an empty element to stream.
     *
     * @param name Element name
     *
     * @throws XMLStreamException If an error occurs when writing to {@link OutputStream}
     */
    protected void empty(QName name) throws XMLStreamException {
        this.writer.writeEmptyElement(name.getPrefix(), name.getLocalPart(), name.getNamespaceURI());
    }

    /**
     * Write characters to stream.
     *
     * @param chars Characters to write
     *
     * @throws XMLStreamException If an error occurs when writing to {@link OutputStream}
     */
    protected void chars(String chars) throws XMLStreamException {
        chars(chars, true);
    }

    /**
     * Write characters to stream.
     *
     * @param chars  Characters to write
     * @param escape if the chars should be XML escaped
     *
     * @throws XMLStreamException If an error occurs when writing to {@link OutputStream}
     */
    protected void chars(String chars, boolean escape) throws XMLStreamException {
        this.writer.writeCharacters(escape ? XmlEscapers.xmlContentEscaper().escape(chars) : chars);
    }

    /**
     * Write the end element to new line.
     *
     * @param name Element name
     *
     * @throws XMLStreamException If an error occurs when writing to {@link OutputStream}
     */
    protected void end(QName name) throws XMLStreamException {
        this.writer.writeEndElement();
    }

    /**
     * Write the document end to stream.
     *
     * @throws XMLStreamException If an error occurs when writing to {@link OutputStream}
     */
    protected void end() throws XMLStreamException {
        if (!this.embedded) {
            this.writer.writeEndDocument();
        }
    }

    /**
     * Write end element to the same line.
     *
     * @param name Element name
     *
     * @throws XMLStreamException If an error occurs when writing to {@link OutputStream}
     */
    protected void endInline(QName name) throws XMLStreamException {
        end(name);
    }

    /**
     * Finish the stream writing, flush and close.
     *
     * @throws XMLStreamException If an error occurs when writing to {@link OutputStream}
     */
    protected void finish() throws XMLStreamException {
        flush();
        if (this.close) {
            this.writer.close();
        }
    }

    /**
     * Flush written elements.
     *
     * @throws XMLStreamException If an error occurs when writing to {@link OutputStream}
     */
    protected void flush() throws XMLStreamException {
        this.writer.flush();
    }

    /**
     * Encode and write element to the {@link OutputStream}.
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
        this.writer.writeXML(text);
    }

    /**
     * Create the replacement from {@link QName}.
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
     * Write {@link XmlObject} to stream and replace xml-fragment with {@link QName}.
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
     * Write {@link XmlObject} to stream and replace xml-fragment with {@link QName}.
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
     * Write {@link SchemaLocation}s as xsi:schemaLocations attribute to stream.
     *
     * @param schemaLocations {@link SchemaLocation}s to write
     *
     * @throws XMLStreamException If an error occurs when writing to {@link OutputStream}
     */
    protected void schemaLocation(Set<SchemaLocation> schemaLocations) throws XMLStreamException {
        String merged = N52XmlHelper.mergeSchemaLocationsToString(schemaLocations);
        if (!Strings.isNullOrEmpty(merged)) {
            namespace(W3CConstants.NS_XSI_PREFIX, W3CConstants.NS_XSI);
            attr(W3CConstants.QN_SCHEMA_LOCATION_PREFIXED, merged);
        }
    }

    /**
     * Write {@link TimeInstant} to stream.
     *
     * @param time {@link TimeInstant} to write to stream
     *
     * @throws XMLStreamException If an error occurs when writing to {@link OutputStream}
     */
    protected void time(TimeInstant time) throws XMLStreamException {
        time(time.getTimePosition());
    }

    /**
     * Write {@link TimePosition} as ISO 8601 to stream.
     *
     * @param time {@link TimePosition} to write as ISO 8601 to stream
     *
     * @throws XMLStreamException If an error occurs when writing to {@link OutputStream}
     */
    protected void time(TimePosition time) throws XMLStreamException {
        chars(DateTimeHelper.formatDateTime2IsoString(time.getTime()));
    }

    protected void addXlinkHrefAttr(String value) throws XMLStreamException {
        attr(W3CConstants.QN_XLINK_HREF, value);
    }

    protected void addXlinkTitleAttr(String value) throws XMLStreamException {
        attr(W3CConstants.QN_XLINK_TITLE, value);
    }

    /**
     * Try to get encoder for {@link EncoderKey}.
     *
     * @param <T> the resulting type, the "Target"
     * @param <S> the input type, the "Source"
     * @param key Encoder key to get encoder for
     *
     * @return Matching encoder
     */
    protected <T, S> Optional<Encoder<T, S>> tryGetEncoder(EncoderKey key) {
        return this.encoderRepository.<T, S>tryGetEncoder(key);
    }

    /**
     * Get encoder for {@link EncoderKey}.
     *
     * @param <T> the resulting type, the "Target"
     * @param <S> the input type, the "Source"
     * @param key Encoder key to get encoder for
     *
     * @return Matching encoder
     *
     * @throws NoEncoderForKeyException If no matching encoder was found
     */
    @SuppressWarnings("hiding")
    protected <T, S> Encoder<T, S> getEncoder(EncoderKey key) throws NoEncoderForKeyException {
        return this.encoderRepository.<T, S>tryGetEncoder(key)
                .orElseThrow(() -> new NoEncoderForKeyException(key));
    }

    @SuppressWarnings("hiding")
    protected <T, S> Encoder<T, S> getEncoder(String namespace, Object o) throws NoEncoderForKeyException {
        return getEncoder(new XmlEncoderKey(namespace, o.getClass()));
    }

    @SuppressWarnings("hiding")
    protected <T, S> Optional<Encoder<T, S>> getEncoder() {
        return getContext().get(StreamingEncoderFlags.ENCODER);
    }

    protected boolean isAddSchemaLocation() {
        return getContext().getBoolean(XmlEncoderFlags.ADD_SCHEMA_LOCATION);
    }

    protected Optional<String> getEncodeNamespace() {
        return getContext().get(XmlEncoderFlags.ENCODE_NAMESPACE);
    }

    private static XMLStreamException prefixAlreadyBound(String prefix, String ns) {
        return new XMLStreamException("Prefix <" + prefix + "> is already bound to <" + ns + ">");
    }

}
