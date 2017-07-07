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
package org.n52.svalbard.read;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.n52.svalbard.decode.exception.DecodingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.collect.Iterables;

public abstract class XmlReader<T> {
    private static final Logger LOG = LoggerFactory.getLogger(XmlReader.class);
    private final XMLInputFactory inputFactory = XMLInputFactory.newInstance();
    private XMLStreamReader reader;
    private QName root;
    private int rootCount;

    public T read(InputStream in)
            throws XMLStreamException, DecodingException {
        return XmlReader.this.read(this.inputFactory.createXMLStreamReader(in));
    }

    private T read(XmlReader<?> reader)
            throws XMLStreamException, DecodingException {
        return read(reader.reader);
    }

    private T read(XMLStreamReader reader)
            throws XMLStreamException, DecodingException {
        this.reader = reader;
        this.root = toNextBeginTag();
        String cName = this.getClass().getSimpleName();
        this.rootCount = 0;
        if (root != null && !isEndTag()) {
            LOG.trace("{}: root: <{}:{}>", cName, root.getPrefix(),
                      root.getLocalPart());

            begin();

            //begin may proceed to the end of the element...
            if (isEndTag() && tagName().equals(root)) {
                return finish();
            }

            QName current;
            while ((current = toNextTag()) != null) {
                if (isStartTag()) {
                    LOG.trace("{}: begin: <{}:{}>", cName,
                              current.getPrefix(), current.getLocalPart());
                    if (current.equals(root)) {
                        ++rootCount;
                    }
                    read(current);
                } else if (isEndTag()) {
                    LOG.trace("{}: end: <{}:{}>", cName, current.getPrefix(),
                              current.getLocalPart());
                    if (current.equals(root)) {
                        if (rootCount == 0) {
                            break;
                        } else {
                            --rootCount;
                        }
                    }
                }
            }
        }
        return finish();
    }

    protected void read(QName name)
            throws XMLStreamException, DecodingException {
        ignore();
    }

    @SuppressWarnings("hiding")
    protected <T> T delegate(XmlReader<? extends T> reader)
            throws XMLStreamException, DecodingException {
        return reader.read(this);
    }

    private boolean isStartTag() {
        return this.reader.isStartElement();
    }

    private boolean isEndTag() {
        return this.reader.isEndElement();
    }

    private boolean hasNext()
            throws XMLStreamException {
        return this.reader.hasNext();
    }

    private void next()
            throws XMLStreamException {
        this.reader.next();
    }

    private QName toNextTag()
            throws XMLStreamException {
        if (hasNext()) {
            next();
            return toTag();
        } else {
            return null;
        }
    }

    private QName toNextBeginTag()
            throws XMLStreamException {
        while (!isStartTag() && hasNext()) {
            toNextTag();
        }
        return isStartTag() ? tagName() : null;
    }

    private QName toTag()
            throws XMLStreamException {
        while (!isStartTag() && !isEndTag() && hasNext()) {
            next();
        }
        return isStartTag() || isEndTag() ? tagName() : null;
    }

    protected Map<QName, String> attr() {
        int l = this.reader.getAttributeCount();
        Map<QName, String> attr = new HashMap<>(l);
        for (int i = 0; i < l; ++i) {
            if (this.reader.isAttributeSpecified(i)) {
                attr.put(this.reader.getAttributeName(i),
                         this.reader.getAttributeValue(i));
            }
        }
        return attr;
    }

    protected Iterable<Optional<String>> attr(Iterable<QName> names) {
        return Iterables.transform(names, XmlReader.this::attr);
    }

    protected Optional<String> attr(QName qn) {
        return Optional.fromNullable(attr().get(qn));
    }

    protected Optional<String> attr(String name) {
        return attr(new QName(name));
    }

    protected QName tagName() {
        return this.reader.getName();
    }

    protected String chars()
            throws XMLStreamException {
        return this.reader.getElementText();
    }

    protected void begin()
            throws XMLStreamException, DecodingException {
        /* no op */
    }

    protected abstract T finish()
            throws DecodingException;

    protected void ignore() {
        QName name = this.reader.getName();
        String cName = getClass().getSimpleName();
        LOG.warn("{}: ignoring element {}:{}", cName,
                 name.getPrefix(), name.getLocalPart());
    }

}
