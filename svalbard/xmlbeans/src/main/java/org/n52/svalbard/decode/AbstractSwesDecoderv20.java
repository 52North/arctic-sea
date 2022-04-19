/*
 * Copyright (C) 2015-2022 52°North Spatial Information Research GmbH
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

import net.opengis.swes.x20.ExtensibleRequestType;

import org.apache.xmlbeans.XmlObject;
import org.n52.shetland.ogc.ows.extension.Extension;
import org.n52.shetland.ogc.ows.extension.Extensions;
import org.n52.shetland.ogc.swe.SweAbstractDataComponent;
import org.n52.shetland.ogc.swes.SwesExtension;
import org.n52.shetland.util.CollectionHelper;
import org.n52.svalbard.decode.exception.DecodingException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public abstract class AbstractSwesDecoderv20<S>
        extends AbstractXmlDecoder<XmlObject, S> {

    protected Extensions parseExtensibleRequest(ExtensibleRequestType extensibleRequestType)
            throws DecodingException {
        return parseExtensibleRequestExtension(extensibleRequestType.getExtensionArray());
    }

    protected Extensions parseExtensibleRequestExtension(XmlObject[] extensionArray)
            throws DecodingException {
        if (CollectionHelper.isNotNullOrEmpty(extensionArray)) {
            final Extensions extensions = new Extensions();
            for (XmlObject xbExtension : extensionArray) {
                Object obj = decodeXmlElement(xbExtension);
                if (obj instanceof Extension<?>) {
                    extensions.addExtension((Extension<?>) obj);
                } else if (obj instanceof SweAbstractDataComponent) {
                    SwesExtension<SweAbstractDataComponent> swesExtension =
                            new SwesExtension<SweAbstractDataComponent>();
                    swesExtension.setIdentifier(((SweAbstractDataComponent) obj).getIdentifier());
                    swesExtension.setDefinition(((SweAbstractDataComponent) obj).getDefinition());
                    swesExtension.setValue((SweAbstractDataComponent) obj);
                    extensions.addExtension(swesExtension);
                }
            }
            return extensions;
        }
        return null;
    }

    /**
     * Check if the namespace of the procedure description element is equal to
     * the procedure description format of the request.
     *
     * @param procedureDescriptionFormat
     *            the procedure description format of the request
     * @param namespace
     *            the namespace of the procedure description element
     *
     * @throws DecodingException
     *             If the {@code procedureDescriptionFormat} and
     *             {@code namespace} are not equal
     */
    protected void checkFormatWithNamespace(String procedureDescriptionFormat, String namespace)
            throws DecodingException {
        if (!procedureDescriptionFormat.equals(namespace) && !procedureDescriptionFormat.contains(namespace)) {
            throw new DecodingException(
                    "The procedure description namespace '%s' does not match the procedureDescriptionFormat '%s'",
                    namespace, procedureDescriptionFormat);
        }
    }

    protected Node getNodeFromNodeList(final NodeList nodeList) {
        if (nodeList != null && nodeList.getLength() > 0) {
            for (int i = 0; i < nodeList.getLength(); i++) {
                if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    return nodeList.item(i);
                }
            }
        }
        return null;
    }
}
