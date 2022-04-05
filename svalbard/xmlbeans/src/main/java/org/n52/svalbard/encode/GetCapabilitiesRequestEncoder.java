/*
 * Copyright 2015-2022 52°North Spatial Information Research GmbH
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
import org.n52.shetland.ogc.ows.OwsAcceptVersions;
import org.n52.shetland.ogc.ows.OwsSections;
import org.n52.shetland.ogc.ows.service.GetCapabilitiesRequest;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.w3c.SchemaLocation;
import org.n52.svalbard.encode.exception.EncodingException;

import com.google.common.collect.Sets;

import net.opengis.sos.x20.GetCapabilitiesDocument;
import net.opengis.sos.x20.GetCapabilitiesType;

/**
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 5.0.0
 *
 */
public class GetCapabilitiesRequestEncoder extends AbstractSosRequestEncoder<GetCapabilitiesRequest> {

    public GetCapabilitiesRequestEncoder() {
        super(SosConstants.Operations.GetCapabilities.name(), GetCapabilitiesRequest.class);
    }

    @Override
    protected Set<SchemaLocation> getConcreteSchemaLocations() {
        return Sets.newHashSet(Sos2Constants.SOS_GET_CAPABILITIES_SCHEMA_LOCATION);
    }

    @Override
    protected XmlObject create(GetCapabilitiesRequest request) throws EncodingException {
        GetCapabilitiesDocument doc = GetCapabilitiesDocument.Factory.newInstance(getXmlOptions());
        GetCapabilitiesType gct = doc.addNewGetCapabilities2();
        addService(gct, request);
        addAcceptVersion(gct, request);
        addSections(gct, request);
        return doc;
    }

    private void addService(GetCapabilitiesType gct, GetCapabilitiesRequest request) {
        if (request.isSetService()) {
            gct.setService(request.getService());
        } else {
            gct.setService(SosConstants.SOS);
        }
    }

    private void addAcceptVersion(GetCapabilitiesType gct, GetCapabilitiesRequest request) throws EncodingException {
        if (request.isSetAcceptVersions()) {
            gct.addNewAcceptVersions()
                    .set(encodeOws(new OwsAcceptVersions().setAcceptVersions(request.getAcceptVersions())));
        } else if (request.isSetVersion()) {
            gct.addNewAcceptVersions().addVersion(request.getVersion());
        } else {
            gct.addNewAcceptVersions().addVersion(Sos2Constants.SERVICEVERSION);
        }
    }

    private void addSections(GetCapabilitiesType gct, GetCapabilitiesRequest request) throws EncodingException {
        if (request.isSetSections()) {
            gct.addNewSections().set(encodeOws(new OwsSections().setSections(request.getSections())));
        }
    }

}
