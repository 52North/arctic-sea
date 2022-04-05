/*
 * Copyright 2015-${currentYear} 52°North Spatial Information Research GmbH
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
package org.n52.iceland.coding;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.n52.janmayen.lifecycle.Constructable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author <a href="mailto:m.rieke@52north.org">Matthes Rieke</a>
 */
public class DocumentBuilderProvider implements Constructable {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentBuilderProvider.class);
    private DocumentBuilderFactory factory;

    @Override
    public void init() {
        this.factory = DocumentBuilderFactory.newInstance();
        try {
            // Xerces 1 - http://xerces.apache.org/xerces-j/features.html#external-general-entities
            // Xerces 2 - http://xerces.apache.org/xerces2-j/features.html#external-general-entities
            this.factory.setFeature("http://xml.org/sax/features/external-general-entities", false);

            // Xerces 2 only - http://xerces.apache.org/xerces-j/features.html#external-general-entities
            this.factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        } catch (ParserConfigurationException e) {
            LOG.warn("Could not set feature on document builder factory: " + e.getMessage(), e);
        }
    }

    public DocumentBuilder newDocumentBuilder()
            throws ParserConfigurationException {
        return this.factory.newDocumentBuilder();
    }

}
