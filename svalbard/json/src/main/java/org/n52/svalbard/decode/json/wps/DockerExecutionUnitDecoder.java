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
package org.n52.svalbard.decode.json.wps;

import com.fasterxml.jackson.databind.JsonNode;
import org.n52.janmayen.stream.Streams;
import org.n52.shetland.ogc.wps.ap.DockerExecutionUnit;
import org.n52.svalbard.coding.json.JSONConstants;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.decode.json.JSONDecoder;

import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import static java.util.stream.Collectors.toMap;

public class DockerExecutionUnitDecoder extends JSONDecoder<DockerExecutionUnit> {

    public DockerExecutionUnitDecoder() {
        super(DockerExecutionUnit.class);
    }

    @Override
    public DockerExecutionUnit decodeJSON(JsonNode node, boolean validate) throws DecodingException {
        if (node == null || node.isNull() || node.isMissingNode()) {
            return null;
        }
        DockerExecutionUnit unit = new DockerExecutionUnit();
        JsonNode unitNode = node.path(JSONConstants.UNIT);
        unit.setImage(unitNode.path(JSONConstants.IMAGE).asText());
        unit.setEnvironment(decodeEnvironment(unitNode.path(JSONConstants.ENVIRONMENT)));
        return unit;
    }

    private Map<String, String> decodeEnvironment(JsonNode node) {
        if (node.isArray()) {
            return Streams.stream(node).map(JsonNode::asText).filter(Objects::nonNull).map(s -> s.split("=", 1))
                          .collect(toMap(s -> s[0], s -> s.length > 1 ? s[1] : ""));
        }
        if (node.isObject()) {
            return Streams.stream(node.fields())
                          .collect(toMap(Entry::getKey, e -> e.getValue().asText()));
        }
        return Collections.emptyMap();
    }
}
