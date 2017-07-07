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
package org.n52.svalbard.decode;


import javax.inject.Inject;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.w3c.dom.Node;

import org.n52.janmayen.Producer;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.decode.exception.NoDecoderForKeyException;
import org.n52.svalbard.decode.exception.XmlDecodingException;

/**
 * TODO JavaDoc
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
public abstract class AbstractXmlDecoder<T, S> extends AbstractDelegatingDecoder<S, T> {

    private Producer<XmlOptions> xmlOptions;

    public XmlOptions getXmlOptions() {
        return xmlOptions.get();
    }

    @Inject
    public void setXmlOptions(Producer<XmlOptions> xmlOptions) {
        this.xmlOptions = xmlOptions;
    }

    public <T> T decodeXmlElement(XmlObject x) throws DecodingException {
        return decodeXmlObject(x);
    }

    public DecoderKey getDecoderKey(XmlObject doc) {

        Node domNode = doc.getDomNode();
        String namespaceURI = domNode.getNamespaceURI();
        if (namespaceURI == null && domNode.getFirstChild() != null) {
            namespaceURI = domNode.getFirstChild().getNamespaceURI();
        }
        /*
         * if document starts with a comment, get next sibling (and ignore
         * initial comment)
         */
        if (namespaceURI == null &&
            domNode.getFirstChild() != null &&
            domNode.getFirstChild().getNextSibling() != null) {
            namespaceURI = domNode.getFirstChild().getNextSibling().getNamespaceURI();
        }

        return new XmlNamespaceDecoderKey(namespaceURI, doc.getClass());
    }

    public <T> T decodeXmlObject(XmlObject xbObject) throws DecodingException {
        DecoderKey key = getDecoderKey(xbObject);
        Decoder<T, XmlObject> decoder = getDecoderRepository().getDecoder(key);
        if (decoder == null) {
            DecoderKey schemaTypeKey = new XmlNamespaceDecoderKey(xbObject.schemaType().getName().getNamespaceURI(),
                                                                  xbObject.getClass());
            decoder = getDecoderRepository().getDecoder(schemaTypeKey);
        }
        if (decoder == null) {
            throw new NoDecoderForKeyException(key);
        }
        return decoder.decode(xbObject);
    }

    public Object decodeXmlObject(String xmlString) throws DecodingException {
        try {
            return decodeXmlObject(XmlObject.Factory.parse(xmlString));
        } catch (final XmlException e) {
            throw new XmlDecodingException("XML string", xmlString, e);
        }
    }

}
