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
package org.n52.svalbard.encode.json.base;

import org.n52.svalbard.encode.json.JSONEncoder;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class FloatJSONEncoder extends JSONEncoder<Float> {

    public FloatJSONEncoder() {
        super(Float.class);
    }

    @Override
    public JsonNode encodeJSON(Float t) {
        return nodeFactory().numberNode(t);
    }

}
