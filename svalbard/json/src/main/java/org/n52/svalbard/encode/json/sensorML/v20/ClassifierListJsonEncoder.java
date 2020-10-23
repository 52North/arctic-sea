/*
 * Copyright 2015-2020 52°North Initiative for Geospatial Open Source
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
package org.n52.svalbard.encode.json.sensorML.v20;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.n52.shetland.ogc.sensorML.elements.SmlClassifier;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.json.JSONEncoder;
import org.n52.svalbard.encode.json.ListEncoderKey;

import java.util.List;

/**
 * @author <a href="mailto:j.speckamp@52north.org">Jan Speckamp</a>
 */
public class ClassifierListJsonEncoder extends JSONEncoder<List<SmlClassifier>> implements SensorML20JsonEncoder {

    public ClassifierListJsonEncoder() {
        super(new ListEncoderKey(SmlClassifier.class));
    }

    @Override public JsonNode encodeJSON(List<SmlClassifier> smlClassifiers) throws EncodingException {
        ObjectNode cJson = jsonFactory.objectNode();
        cJson.put(TYPE, "ClassifierList");
        if (!smlClassifiers.isEmpty()) {
            cJson.put("classifier", encodeObjectsToJson(smlClassifiers));
        }
        return cJson;
    }
}
