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

import java.util.Collections;
import java.util.Set;

import net.opengis.sos.x20.GetObservationByIdDocument;
import net.opengis.sos.x20.GetObservationByIdType;

import org.apache.xmlbeans.XmlObject;

import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.sos.request.GetObservationByIdRequest;
import org.n52.shetland.w3c.SchemaLocation;
import org.n52.svalbard.encode.exception.EncodingException;

/**
 * @author <a href="mailto:j.schulte@52north.org">Jan Schulte</a>
 */
public class GetObservationByIdRequestEncoder extends AbstractSosRequestEncoder<GetObservationByIdRequest>
        implements ExtensibleRequestEncoder {

    public GetObservationByIdRequestEncoder() {
        super(SosConstants.Operations.GetObservationById.name(), GetObservationByIdRequest.class);
    }

    @Override
    protected Set<SchemaLocation> getConcreteSchemaLocations() {
        return Collections.singleton(Sos2Constants.SOS_GET_OBSERVATION_BY_ID_SCHEMA_LOCATION);
    }

    @Override
    protected XmlObject create(GetObservationByIdRequest request) throws EncodingException {
        GetObservationByIdDocument doc = GetObservationByIdDocument.Factory.newInstance(getXmlOptions());
        GetObservationByIdType gobit = doc.addNewGetObservationById();
        addService(gobit, request);
        addVersion(gobit, request);
        addExtension(gobit, request);
        addObservationIds(gobit, request);
        return doc;
    }

    private void addObservationIds(GetObservationByIdType gobit, GetObservationByIdRequest request) {
        if (request.isSetObservationIdentifier()) {
            request.getObservationIdentifier().forEach(gobit::addObservation);
        }
    }

}
