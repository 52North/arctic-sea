/*
 * Copyright 2015-2018 52°North Initiative for Geospatial Open Source
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

import java.util.Set;

import org.apache.xmlbeans.XmlObject;
import org.n52.shetland.ogc.ows.extension.Extension;
import org.n52.shetland.ogc.ows.extension.Extensions;
import org.n52.shetland.ogc.ows.service.OwsServiceResponse;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.swe.SweAbstractDataComponent;
import org.n52.shetland.ogc.swe.SweConstants;
import org.n52.shetland.ogc.swes.SwesConstants;
import org.n52.shetland.w3c.SchemaLocation;
import org.n52.svalbard.encode.exception.EncodingException;

import com.google.common.collect.Sets;

import net.opengis.swes.x20.ExtensibleResponseType;

/**
 * TODO JavaDoc
 *
 * @param <T>
 *            the response type
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
public abstract class AbstractSwesResponseEncoder<T extends OwsServiceResponse> extends AbstractResponseEncoder<T> {

    public AbstractSwesResponseEncoder(String operation, Class<T> responseType) {
        super(SosConstants.SOS, Sos2Constants.SERVICEVERSION, operation, SwesConstants.NS_SWES_20,
                SwesConstants.NS_SWES_PREFIX, responseType);
    }

    @Override
    public Set<SchemaLocation> getSchemaLocations() {
        return Sets.newHashSet(SwesConstants.SWES_20_SCHEMA_LOCATION);
    }

    protected XmlObject encodeSwe(EncodingContext helperValues, Object o) throws EncodingException {
        return encodeObjectToXml(SweConstants.NS_SWE_20, o, helperValues);
    }

    protected void createExtension(ExtensibleResponseType xbResponse, Extensions extensions) throws EncodingException {
        EncodingContext ctx = new EncodingContext().with(XmlBeansEncodingFlags.PROPERTY_TYPE, "true");
        for (Extension<?> extension : extensions.getExtensions()) {
            if (extension.getValue() instanceof SweAbstractDataComponent) {
                xbResponse.addNewExtension()
                        .set(encodeSwe(ctx, extension.getValue()));
            }
        }
    }
}
