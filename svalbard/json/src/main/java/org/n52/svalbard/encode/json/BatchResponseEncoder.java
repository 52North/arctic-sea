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
package org.n52.svalbard.encode.json;

import org.n52.shetland.ogc.sos.BatchConstants;
import org.n52.shetland.ogc.sos.response.BatchResponse;
import org.n52.shetland.ogc.sos.response.BatchResponse.ExceptionOrResponse;
import org.n52.svalbard.coding.json.JSONConstants;
import org.n52.svalbard.encode.exception.EncodingException;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * TODO JavaDoc
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
public class BatchResponseEncoder extends AbstractSosResponseEncoder<BatchResponse> {
    public BatchResponseEncoder() {
        super(BatchResponse.class, BatchConstants.OPERATION_NAME);
    }

    @Override
    protected void encodeResponse(ObjectNode json, BatchResponse response) throws EncodingException {
        ArrayNode responses = json.putArray(JSONConstants.RESPONSES);
        for (ExceptionOrResponse eor : response) {
            if (eor.isException()) {
                responses.add(encodeObjectToJson(eor.getException()));
            } else {
                responses.add(encodeObjectToJson(eor.getResponse()));
            }

        }
    }
}
