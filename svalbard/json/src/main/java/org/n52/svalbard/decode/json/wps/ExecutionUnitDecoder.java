/*
 * Copyright 2015-2019 52°North Initiative for Geospatial Open Source
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
package org.n52.svalbard.decode.json.wps;

import com.fasterxml.jackson.databind.JsonNode;
import org.n52.shetland.ogc.wps.ap.DockerExecutionUnit;
import org.n52.shetland.ogc.wps.ap.ExecutionUnit;
import org.n52.svalbard.coding.json.JSONConstants;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.decode.json.JSONDecoder;

public class ExecutionUnitDecoder extends JSONDecoder<ExecutionUnit> {

    public ExecutionUnitDecoder() {
        super(ExecutionUnit.class);
    }

    @Override
    public ExecutionUnit decodeJSON(JsonNode node, boolean validate) throws DecodingException {
        String type = node.path(JSONConstants.TYPE).asText();
        if (type == null) {
            throw new DecodingException("missing execution unit type");
        }

        switch (type) {
            case DockerExecutionUnit.TYPE:
                return decodeJsonToObject(node, DockerExecutionUnit.class);
            default:
                throw new DecodingException("unsupported execution unit type: " + type);
        }
    }

}
