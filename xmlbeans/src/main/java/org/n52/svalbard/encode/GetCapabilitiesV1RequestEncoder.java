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

import java.util.Set;

import net.opengis.sos.x10.GetCapabilitiesDocument;

import org.apache.xmlbeans.XmlObject;

import org.n52.shetland.ogc.ows.OwsAcceptVersions;
import org.n52.shetland.ogc.ows.OwsSections;
import org.n52.shetland.ogc.ows.service.GetCapabilitiesRequest;
import org.n52.shetland.ogc.sos.Sos1Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.w3c.SchemaLocation;
import org.n52.svalbard.encode.exception.EncodingException;

import com.google.common.collect.Sets;

/**
 * @author <a href="mailto:j.schulte@52north.org">Jan Schulte</a>
 */
public class GetCapabilitiesV1RequestEncoder extends AbstractSosV1RequestEncoder<GetCapabilitiesRequest> {

    public GetCapabilitiesV1RequestEncoder() {
        super(SosConstants.Operations.GetCapabilities.name(), GetCapabilitiesRequest.class);
    }

    @Override
    protected Set<SchemaLocation> getConcreteSchemaLocations() {
        return Sets.newHashSet(Sos1Constants.GET_CAPABILITIES_SOS1_SCHEMA_LOCATION);
    }

    @Override
    protected XmlObject create(GetCapabilitiesRequest request) throws EncodingException {
        GetCapabilitiesDocument doc = GetCapabilitiesDocument.Factory.newInstance(getXmlOptions());
        GetCapabilitiesDocument.GetCapabilities gc = doc.addNewGetCapabilities();
        addService(gc, request);
        addAcceptVersions(gc, request);
        addSections(gc, request);
        return doc;
    }

    private void addService(GetCapabilitiesDocument.GetCapabilities gc, GetCapabilitiesRequest request) {
        if (request.isSetService()) {
            gc.setService(request.getService());
        } else {
            gc.setService(SosConstants.SOS);
        }
    }

    private void addAcceptVersions(GetCapabilitiesDocument.GetCapabilities gc, GetCapabilitiesRequest request)
            throws EncodingException {
        if (request.isSetAcceptVersions()) {
            gc.addNewAcceptVersions().set(encodeOws(new OwsAcceptVersions()
                    .setAcceptVersions(request.getAcceptVersions())));
        } else if (request.isSetVersion()) {
            gc.addNewAcceptVersions().addVersion(request.getVersion());
        } else {
            gc.addNewAcceptVersions().addVersion(Sos1Constants.SERVICEVERSION);
        }
    }

    private void addSections(GetCapabilitiesDocument.GetCapabilities gc, GetCapabilitiesRequest request)
            throws EncodingException {
        if (request.isSetSections()) {
            gc.addNewSections().set(encodeOws(new OwsSections().setSections(request.getSections())));
        }
    }

}
