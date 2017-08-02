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
package org.n52.svalbard.encode;

import org.apache.xmlbeans.XmlObject;
import org.n52.shetland.ogc.swe.SweAbstractDataComponent;
import org.n52.shetland.ogc.swes.SwesExtension;
import org.n52.shetland.ogc.swes.SwesExtensions;
import org.n52.shetland.util.CollectionHelper;
import org.n52.svalbard.decode.exception.DecodingException;

import net.opengis.swes.x20.ExtensibleResponseType;

/**
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 5.0.0
 *
 */
public interface ExtensibleResponseDecoder {

    default SwesExtensions parseExtensibleResponse(ExtensibleResponseType ert)
            throws DecodingException {
        return parseExtensibleResponseExtension(ert.getExtensionArray());
    }

    default SwesExtensions parseExtensibleResponseExtension(XmlObject[] extensionArray)
            throws DecodingException {
        if (CollectionHelper.isNotNullOrEmpty(extensionArray)) {
            SwesExtensions extensions = new SwesExtensions();
            for (XmlObject xbSwesExtension : extensionArray) {

                Object obj = decodeXmlElement(xbSwesExtension);
                if (obj instanceof SwesExtension<?>) {
                    extensions.addExtension((SwesExtension<?>) obj);
                } else if (obj instanceof SweAbstractDataComponent) {
                    extensions.addExtension(new SwesExtension<>().setValue((SweAbstractDataComponent) obj));
                }
            }
            return extensions;
        }
        return null;
    }

    <T> T decodeXmlElement(XmlObject obj)
            throws DecodingException;

}
