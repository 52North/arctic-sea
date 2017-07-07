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
package org.n52.svalbard.util;

import java.io.IOException;
import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Helper class for W3C
 *
 * @since 1.0.0
 *
 */
public final class W3cHelper {

    private W3cHelper() {
    }

    /**
     * Parses w3c.Node to String
     *
     * @param node
     *            Node to parse.
     *
     * @return Node as String.
     *
     * @throws IOException
     *             if an error occurs.
     */
    public static String nodeToXmlString(Node node) throws IOException {
        try (StringWriter sw = new StringWriter()) {
            Transformer t = TransformerFactory.newInstance().newTransformer();
            t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            t.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            t.transform(new DOMSource(node), new StreamResult(sw));
            return sw.toString();
        } catch (TransformerException te) {
            throw new IOException(te);
        }

    }

    /**
     * Get text content from element by namespace.
     *
     * @param element
     *            element
     * @param namespaceURI
     *            Namespace URI
     * @param localName
     *            local name
     *
     * @return Text content.
     */
    public static String getContentFromElement(Element element, String namespaceURI, String localName) {
        String elementContent = null;
        NodeList nodes = element.getElementsByTagNameNS(namespaceURI, localName);
        for (int i = 0; i < nodes.getLength(); i++) {
            elementContent = nodes.item(i).getTextContent();
        }
        return elementContent;
    }

}
