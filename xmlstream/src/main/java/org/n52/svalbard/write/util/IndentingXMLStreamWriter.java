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
package org.n52.svalbard.write.util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Stack;
import java.util.regex.Pattern;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class IndentingXMLStreamWriter extends DelegatingXMLStreamWriter implements ExtendedXMLStreamWriter {
    private static final Pattern NEW_LINE_PATTERN = Pattern.compile("\\r?\\n");
    private static final String NEW_LINE = "\n";
    private final Stack<State> stateStack = new Stack<>();
    private final String indent;
    private State state = State.SEEN_NOTHING;
    private int depth ;

    public IndentingXMLStreamWriter(XMLStreamWriter writer, String indent) {
        super(writer);
        this.indent = indent;
    }

    public IndentingXMLStreamWriter(XMLStreamWriter writer, int indent) {
        this(writer, repeat(" ", indent));
    }

    private void onStartElement() throws XMLStreamException {
        this.stateStack.push(State.SEEN_ELEMENT);
        this.state = State.SEEN_NOTHING;
        if (depth > 0) {
            writeNewLine();
        }
        writeIndent();
        this.depth++;
    }

    private void onEndElement() throws XMLStreamException {
        this.depth--;
        if (this.state == State.SEEN_ELEMENT) {
            writeNewLine();
            writeIndent();
        }
        this.state = stateStack.pop();
    }

    private void onEmptyElement() throws XMLStreamException {
        this.state = State.SEEN_ELEMENT;
        if (this.depth > 0) {
            writeNewLine();
        }
        writeIndent();
    }

    private void onData() {
        this.state = State.SEEN_DATA;
    }

    private void writeNewLine() throws XMLStreamException {
        super.writeCharacters(NEW_LINE);
    }

    private void writeIndent() throws XMLStreamException {
        for (int i = 0; i < this.depth; i++) {
            super.writeCharacters(indent);
        }
    }

    @Override
    public void writeStartDocument() throws XMLStreamException {
        super.writeStartDocument();
        writeNewLine();
    }

    @Override
    public void writeStartDocument(String version) throws XMLStreamException {
        super.writeStartDocument(version);
        writeNewLine();
    }

    @Override
    public void writeStartDocument(String encoding, String version) throws XMLStreamException {
        super.writeStartDocument(encoding, version);
        writeNewLine();
    }

    @Override
    public void writeStartElement(String localName) throws XMLStreamException {
        onStartElement();
        super.writeStartElement(localName);
    }

    @Override
    public void writeStartElement(String namespaceURI, String localName) throws XMLStreamException {
        onStartElement();
        super.writeStartElement(namespaceURI, localName);
    }

    @Override
    public void writeStartElement(String prefix, String localName, String namespaceURI) throws XMLStreamException {
        onStartElement();
        super.writeStartElement(prefix, localName, namespaceURI);
    }

    @Override
    public void writeEmptyElement(String namespaceURI, String localName) throws XMLStreamException {
        onEmptyElement();
        super.writeEmptyElement(namespaceURI, localName);
    }

    @Override
    public void writeEmptyElement(String prefix, String localName, String namespaceURI) throws XMLStreamException {
        onEmptyElement();
        super.writeEmptyElement(prefix, localName, namespaceURI);
    }

    @Override
    public void writeEmptyElement(String localName) throws XMLStreamException {
        onEmptyElement();
        super.writeEmptyElement(localName);
    }

    @Override
    public void writeEndElement() throws XMLStreamException {
        onEndElement();
        super.writeEndElement();
    }

    @Override
    public void writeXML(String text) throws XMLStreamException {
        String[] lines = NEW_LINE_PATTERN.split(text.trim());
        Iterator<String> iter = Arrays.asList(lines).iterator();
        while (iter.hasNext()) {
            writeNewLine();
            writeIndent();
            super.writeCharacters(iter.next());
        }
        this.state = State.SEEN_ELEMENT;
    }

    @Override
    public void writeCharacters(String text) throws XMLStreamException {
        onData();
        String[] lines = NEW_LINE_PATTERN.split(text.trim());
        if (lines.length <= 1) {
            super.writeCharacters(text.trim());
        } else {
            Iterator<String> iter = Arrays.asList(lines).iterator();
            while (iter.hasNext()) {
                writeNewLine();
                writeIndent();
                super.writeCharacters(iter.next());
            }
            state = State.SEEN_ELEMENT;
        }
    }

    @Override
    public void writeCharacters(char[] text, int start, int len) throws XMLStreamException {
        writeCharacters(new String(text, start, len));
    }

    @Override
    public void writeCData(String data) throws XMLStreamException {
        onData();
        super.writeCData(data);
    }

    private static String repeat(String s, int times) {
        StringBuilder builder = new StringBuilder(s.length() * times);
        for (int i = 0; i < times; ++i) {
            builder.append(s);
        }
        return builder.toString();
    }

    private enum State {
        SEEN_NOTHING,
        SEEN_ELEMENT,
        SEEN_DATA,
    }

}
