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
package org.n52.svalbard.encode.json;

import org.n52.janmayen.Json;
import org.n52.janmayen.http.MediaTypes;
import org.n52.shetland.ogc.ows.service.OwsServiceResponse;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.svalbard.coding.json.JSONConstants;
import org.n52.svalbard.encode.OperationResponseEncoderKey;
import org.n52.svalbard.encode.exception.EncodingException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * TODO JavaDoc
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 * @param <T> the response type
 *
 * @since 1.0.0
 */
public abstract class AbstractSosResponseEncoder<T extends OwsServiceResponse> extends JSONEncoder<T> {
    public AbstractSosResponseEncoder(Class<T> type, String operation) {
        super(type, new OperationResponseEncoderKey(SosConstants.SOS, Sos2Constants.SERVICEVERSION, operation,
                                                    MediaTypes.APPLICATION_JSON));
    }

    public AbstractSosResponseEncoder(Class<T> type, Enum<?> operation) {
        this(type, operation.name());
    }

    @Override
    public JsonNode encodeJSON(T t) throws EncodingException {
        ObjectNode n = Json.nodeFactory().objectNode();
        n.put(JSONConstants.REQUEST, t.getOperationName());
        n.put(JSONConstants.VERSION, t.getVersion());
        n.put(JSONConstants.SERVICE, t.getService());
        encodeResponse(n, t);
        return n;
    }

    protected abstract void encodeResponse(ObjectNode json, T t) throws EncodingException;
}
