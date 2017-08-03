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

import org.n52.janmayen.http.MediaType;
import org.n52.janmayen.http.MediaTypes;
import org.n52.shetland.ogc.ows.service.GetCapabilitiesRequest;
import org.n52.shetland.ogc.sos.Sos1Constants;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.sos.SosConstants.Operations;
import org.n52.svalbard.coding.json.JSONConstants;
import org.n52.svalbard.coding.json.SchemaConstants;
import org.n52.svalbard.decode.DecoderKey;
import org.n52.svalbard.decode.JsonDecoderKey;
import org.n52.svalbard.decode.OperationDecoderKey;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Sets;

/**
 * TODO JavaDoc
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
public class GetCapabilitiesRequestDecoder extends AbstractSosRequestDecoder<GetCapabilitiesRequest> {
    private static final Operations OP = SosConstants.Operations.GetCapabilities;

    private static final MediaType MT = MediaTypes.APPLICATION_JSON;

    private static final String V2 = Sos2Constants.SERVICEVERSION;

    private static final String V1 = Sos1Constants.SERVICEVERSION;

    private static final String SOS = SosConstants.SOS;

    public GetCapabilitiesRequestDecoder() {
        super(Sets.<DecoderKey> newHashSet(new JsonDecoderKey(GetCapabilitiesRequest.class), new OperationDecoderKey(
                SOS, null, OP, MT), new OperationDecoderKey(SOS, V2, OP, MT),
                new OperationDecoderKey(null, V2, OP, MT), new OperationDecoderKey(null, null, OP, MT),
                new OperationDecoderKey(SOS, V1, OP, MT), new OperationDecoderKey(null, V1, OP, MT)));
    }

    @Override
    protected String getSchemaURI() {
        return SchemaConstants.Request.GET_CAPABILITIES;
    }

    @Override
    protected GetCapabilitiesRequest decodeRequest(JsonNode node) {
        GetCapabilitiesRequest req = new GetCapabilitiesRequest(SosConstants.SOS);
        req.setAcceptFormats(parseStringOrStringList(node.path(JSONConstants.ACCEPT_FORMATS)));
        req.setAcceptVersions(parseStringOrStringList(node.path(JSONConstants.ACCEPT_VERSIONS)));
        req.setSections(parseStringOrStringList(node.path(JSONConstants.SECTIONS)));
        req.setUpdateSequence(node.path(JSONConstants.UPDATE_SEQUENCE).textValue());
        return req;
    }

}
