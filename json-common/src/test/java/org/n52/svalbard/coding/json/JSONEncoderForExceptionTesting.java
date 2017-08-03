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
package org.n52.svalbard.coding.json;

import org.n52.svalbard.encode.EncoderKey;
import org.n52.svalbard.encode.json.JSONEncoder;
import org.n52.svalbard.encode.json.JSONEncodingException;

import com.fasterxml.jackson.databind.JsonNode;

public class JSONEncoderForExceptionTesting extends JSONEncoder<String> {

    public JSONEncoderForExceptionTesting(Class<String> type, EncoderKey[] additionalKeys) {
        super(type, additionalKeys);
    }

    public JSONEncoderForExceptionTesting(Class<String> clazz) {
        super(clazz);
    }

    @Override
    public JsonNode encodeJSON(String t) throws JSONEncodingException {
        throw new JSONEncodingException("message");
    }

}
