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

import net.opengis.swes.x20.ExtensibleRequestType;

import org.apache.xmlbeans.XmlObject;

import org.n52.shetland.ogc.ows.extension.Extension;
import org.n52.shetland.ogc.ows.service.OwsServiceRequest;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.svalbard.encode.exception.EncodingException;

/**
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 5.0.0
 *
 */
public interface ExtensibleRequestEncoder {

    default void addService(ExtensibleRequestType ert, OwsServiceRequest request) {
        if (request.isSetService()) {
            ert.setService(request.getService());
        } else {
            ert.setService(SosConstants.SOS);
        }
    }

    default void addVersion(ExtensibleRequestType ert, OwsServiceRequest request) {
        if (request.isSetVersion()) {
            ert.setVersion(request.getVersion());
        } else {
            ert.setVersion(Sos2Constants.SERVICEVERSION);
        }
    }

    default void addExtension(ExtensibleRequestType ert, OwsServiceRequest request) throws EncodingException {
        for (Extension<?> extension : request.getExtensions().getExtensions()) {
            ert.addNewExtension().set(encodeObjectToXml(extension.getNamespace(), extension));
        }
    }

    XmlObject encodeObjectToXml(String namespace, Object object) throws EncodingException;

}
