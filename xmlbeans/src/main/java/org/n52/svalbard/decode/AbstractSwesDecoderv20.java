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

import net.opengis.swes.x20.ExtensibleRequestType;

import org.apache.xmlbeans.XmlObject;

import org.n52.shetland.ogc.swe.SweAbstractDataComponent;
import org.n52.shetland.ogc.swes.SwesExtension;
import org.n52.shetland.ogc.swes.SwesExtensions;
import org.n52.shetland.util.CollectionHelper;
import org.n52.svalbard.decode.exception.DecodingException;

public abstract class AbstractSwesDecoderv20<S>
        extends AbstractXmlDecoder<XmlObject, S> {

    protected SwesExtensions parseExtensibleRequest(ExtensibleRequestType extensibleRequestType)
            throws DecodingException {
        return parseExtensibleRequestExtension(extensibleRequestType.getExtensionArray());
    }

    protected SwesExtensions parseExtensibleRequestExtension(XmlObject[] extensionArray)
            throws DecodingException {
        if (CollectionHelper.isNotNullOrEmpty(extensionArray)) {
            final SwesExtensions extensions = new SwesExtensions();
            for (XmlObject xbSwesExtension : extensionArray) {

                Object obj = decodeXmlElement(xbSwesExtension);
                if (obj instanceof SwesExtension<?>) {
                    extensions.addExtension((SwesExtension<?>) obj);
                } else if (obj instanceof SweAbstractDataComponent) {
                    SwesExtension<SweAbstractDataComponent> swesExtension =
                            new SwesExtension<SweAbstractDataComponent>();
                    swesExtension.setIdentifier(((SweAbstractDataComponent) obj).getIdentifier());
                    swesExtension.setDefinition(((SweAbstractDataComponent) obj).getDefinition());
                    extensions.addExtension(swesExtension);
                }
            }
            return extensions;
        }
        return null;
    }
}
