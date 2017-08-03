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
package org.n52.svalbard.decode.json;

import org.n52.shetland.ogc.om.OmObservation;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.sos.request.InsertObservationRequest;
import org.n52.svalbard.coding.json.JSONConstants;
import org.n52.svalbard.coding.json.SchemaConstants;
import org.n52.svalbard.decode.exception.DecodingException;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * TODO JavaDoc
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
public class InsertObservationRequestDecoder
        extends AbstractSosRequestDecoder<InsertObservationRequest> {
    public InsertObservationRequestDecoder() {
        super(InsertObservationRequest.class, SosConstants.SOS, Sos2Constants.SERVICEVERSION,
                SosConstants.Operations.InsertObservation);
    }

    @Override
    public String getSchemaURI() {
        return SchemaConstants.Request.INSERT_OBSERVATION;
    }

    @Override
    public InsertObservationRequest decodeRequest(JsonNode node)
            throws DecodingException {
        InsertObservationRequest r = new InsertObservationRequest();
        r.setObservation(decodeJsonToObjectList(node.path(JSONConstants.OBSERVATION), OmObservation.class));
        r.setOfferings(parseStringOrStringList(node.path(JSONConstants.OFFERING)));
        return r;
    }
}
