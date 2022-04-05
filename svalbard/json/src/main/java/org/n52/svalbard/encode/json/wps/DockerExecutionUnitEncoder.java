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
package org.n52.svalbard.encode.json.wps;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.n52.shetland.ogc.wps.ap.DockerExecutionUnit;
import org.n52.svalbard.coding.json.JSONConstants;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.json.JSONEncoder;

import java.util.Map;
import java.util.Map.Entry;

public class DockerExecutionUnitEncoder extends JSONEncoder<DockerExecutionUnit> {

    public DockerExecutionUnitEncoder() {
        super(DockerExecutionUnit.class);
    }

    @Override
    public JsonNode encodeJSON(DockerExecutionUnit unit) throws EncodingException {
        if (unit == null) {
            return nodeFactory().nullNode();
        }
        ObjectNode root = nodeFactory().objectNode();
        ObjectNode unitNode = root.putObject(JSONConstants.UNIT);
        unitNode.put(JSONConstants.TYPE, unit.getType());
        unitNode.set(JSONConstants.IMAGE, encodeAsString(unit.getImage()));
        Map<String, String> environment = unit.getEnvironment();
        if (environment != null && !environment.isEmpty()) {
            ObjectNode environmentNode = unitNode.putObject(JSONConstants.ENVIRONMENT);
            for (Entry<String, String> entry : environment.entrySet()) {
                environmentNode.set(entry.getKey(), encodeAsString(entry.getValue()));
            }
        }
        return root;
    }
}
