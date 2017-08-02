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

import java.util.Collections;
import java.util.Set;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.impl.values.XmlAnyTypeImpl;

import org.n52.shetland.ogc.swe.SweAbstractDataComponent;
import org.n52.shetland.ogc.swes.SwesConstants;
import org.n52.shetland.ogc.swes.SwesExtension;
import org.n52.shetland.util.CollectionHelper;
import org.n52.shetland.w3c.W3CConstants;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.decode.exception.UnsupportedDecoderInputException;
import org.n52.svalbard.decode.exception.UnsupportedDecoderXmlInputException;
import org.n52.svalbard.util.CodingHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

import com.google.common.base.Joiner;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk
 *         J&uuml;rrens</a>
 *
 * @since 1.0.0
 */
public class SwesExtensionDecoderv20
        extends AbstractXmlDecoder<XmlObject, SwesExtension<?>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SwesDecoderv20.class);

    private static final Set<DecoderKey> DECODER_KEYS =
            CollectionHelper.union(CodingHelper.decoderKeysForElements(W3CConstants.NS_XS, XmlAnyTypeImpl.class),
                    CodingHelper.decoderKeysForElements(SwesConstants.NS_SWES_20, XmlAnyTypeImpl.class));

    public SwesExtensionDecoderv20() {
        LOGGER.debug("Decoder for the following keys initialized successfully: {}!",
                Joiner.on(", ").join(DECODER_KEYS));
    }

    @Override
    public Set<DecoderKey> getKeys() {
        return Collections.unmodifiableSet(DECODER_KEYS);
    }

    @Override
    public SwesExtension<?> decode(XmlObject xmlObject)
            throws DecodingException, UnsupportedDecoderInputException {

        if (isSwesExtension(xmlObject)) {
            XmlObject[] children = xmlObject.selectPath("./*");
            if (children.length == 1) {
                Object xmlObj = decodeXmlElement(children[0]);
                if (xmlObj instanceof SweAbstractDataComponent) {
                    SwesExtension<SweAbstractDataComponent> extension = new SwesExtension<>();
                    extension.setValue((SweAbstractDataComponent) xmlObj);
                    return extension;
                }
            }
        }
        throw new UnsupportedDecoderXmlInputException(this, xmlObject);
    }

    private boolean isSwesExtension(XmlObject xmlObject) {
        Node node = xmlObject.getDomNode();
        return node.getNamespaceURI().equalsIgnoreCase(SwesConstants.NS_SWES_20)
                && node.getLocalName().equalsIgnoreCase(SwesConstants.EN_EXTENSION);
    }

}
