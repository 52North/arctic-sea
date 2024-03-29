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
package org.n52.svalbard.encode;

import java.util.Set;

import org.apache.xmlbeans.XmlObject;
import org.n52.shetland.ogc.filter.FilterConstants;
import org.n52.shetland.ogc.gml.GmlConstants;
import org.n52.shetland.ogc.ows.OWSConstants;
import org.n52.shetland.ogc.ows.extension.Extension;
import org.n52.shetland.ogc.ows.extension.Extensions;
import org.n52.shetland.ogc.ows.service.OwsServiceResponse;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.swe.SweAbstractDataComponent;
import org.n52.shetland.ogc.swe.SweConstants;
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
public abstract class AbstractSosResponseEncoder<T extends OwsServiceResponse> extends AbstractResponseEncoder<T> {

    // private ProfileHandler profileHandler;

    public AbstractSosResponseEncoder(String operation, Class<T> responseType) {
        super(SosConstants.SOS, Sos2Constants.SERVICEVERSION, operation, Sos2Constants.NS_SOS_20,
                SosConstants.NS_SOS_PREFIX, responseType);
    }

    // @Inject
    // public void setProfileHandler(ProfileHandler profileHandler) {
    // this.profileHandler = profileHandler;
    // }

    @Override
    public Set<SchemaLocation> getSchemaLocations() {
        return Sets.newHashSet(Sos2Constants.SOS_SCHEMA_LOCATION);
    }

    // protected Profile getActiveProfile() {
    // return this.profileHandler.getActiveProfile();
    // }

    protected XmlObject encodeGml(Object o) throws EncodingException {
        return encodeObjectToXml(GmlConstants.NS_GML_32, o);
    }

    protected XmlObject encodeGml(EncodingContext helperValues, Object o) throws EncodingException {
        return encodeObjectToXml(GmlConstants.NS_GML_32, o, helperValues);
    }

    protected XmlObject encodeOws(Object o) throws EncodingException {
        return encodeObjectToXml(OWSConstants.NS_OWS, o);
    }

    protected XmlObject encodeOws(EncodingContext helperValues, Object o) throws EncodingException {
        return encodeObjectToXml(OWSConstants.NS_OWS, o, helperValues);
    }

    protected XmlObject encodeFes(Object o) throws EncodingException {
        return encodeObjectToXml(FilterConstants.NS_FES_2, o);
    }

    protected XmlObject encodeFes(EncodingContext helperValues, Object o) throws EncodingException {
        return encodeObjectToXml(FilterConstants.NS_FES_2, o, helperValues);
    }

    protected XmlObject encodeSwe(Object o) throws EncodingException {
        return encodeObjectToXml(SweConstants.NS_SWE_20, o);
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
