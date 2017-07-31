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

import org.n52.shetland.inspire.Spelling;
import org.n52.svalbard.coding.json.AQDJSONConstants;
import org.n52.svalbard.decode.exception.DecodingException;

import com.fasterxml.jackson.databind.JsonNode;

public class SpellingJSONDecoder extends AbstractJSONDecoder<Spelling> {
    public SpellingJSONDecoder() {
        super(Spelling.class);
    }

    @Override
    public Spelling decodeJSON(JsonNode node, boolean validate)
            throws DecodingException {
        Spelling spelling = new Spelling();
        spelling.setScript(parseNillableString(node
                .path(AQDJSONConstants.SCRIPT)));
        spelling.setText(node.path(AQDJSONConstants.TEXT).textValue());
        spelling.setTransliterationScheme(parseNillableString(node
                .path(AQDJSONConstants.TRANSLITERATION_SCHEME)));
        return spelling;
    }

}
