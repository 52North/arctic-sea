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

import org.n52.shetland.inspire.Spelling;
import org.n52.svalbard.coding.json.AQDJSONConstants;
import org.n52.svalbard.encode.exception.EncodingException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class SpellingJSONEncoder extends JSONEncoder<Spelling> {

    public SpellingJSONEncoder() {
        super(Spelling.class);
    }

    @Override
    public JsonNode encodeJSON(Spelling t)
            throws EncodingException {
        ObjectNode j = nodeFactory().objectNode();
        j.put(AQDJSONConstants.TEXT, t.getText());
        j.set(AQDJSONConstants.SCRIPT, encodeObjectToJson(t.getScript()));
        j.set(AQDJSONConstants.TRANSLITERATION_SCHEME, encodeObjectToJson(t.getTransliterationScheme()));
        return j;
    }

}
