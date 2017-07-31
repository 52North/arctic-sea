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

import org.n52.janmayen.http.MediaTypes;
import org.n52.shetland.ogc.ows.service.OwsServiceRequest;
import org.n52.shetland.ogc.sos.BatchConstants;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.sos.request.BatchRequest;
import org.n52.svalbard.coding.json.JSONConstants;
import org.n52.svalbard.coding.json.SchemaConstants;
import org.n52.svalbard.decode.Decoder;
import org.n52.svalbard.decode.OperationDecoderKey;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.decode.exception.NoDecoderForKeyException;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * TODO JavaDoc
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
public class BatchRequestDecoder extends AbstractSosRequestDecoder<BatchRequest> {
    public BatchRequestDecoder() {
        super(BatchRequest.class, SosConstants.SOS, Sos2Constants.SERVICEVERSION, BatchConstants.OPERATION_NAME);
    }

    @Override
    protected String getSchemaURI() {
        return SchemaConstants.Request.BULK_REQUEST;
    }

    @Override
    protected BatchRequest decodeRequest(JsonNode node) throws DecodingException {
        BatchRequest request = new BatchRequest();
        if (node.path(JSONConstants.STOP_AT_FAILURE).isBoolean()) {
            request.setStopAtFailure(node.path(JSONConstants.STOP_AT_FAILURE).booleanValue());
        }
        for (JsonNode n : node.path(JSONConstants.REQUESTS)) {
            request.add(getDecoder(n).decode(n));
        }
        return request;
    }

    private Decoder<OwsServiceRequest, JsonNode> getDecoder(JsonNode n) throws DecodingException {
        OperationDecoderKey k =
                new OperationDecoderKey(n.path(JSONConstants.SERVICE).textValue(), n.path(JSONConstants.VERSION)
                        .textValue(), n.path(JSONConstants.REQUEST).textValue(), MediaTypes.APPLICATION_JSON);
        Decoder<OwsServiceRequest, JsonNode> decoder = getDecoder(k);
        if (decoder == null) {
            // TODO other exception?
            throw new NoDecoderForKeyException(k);
        }
        return decoder;
    }
}
