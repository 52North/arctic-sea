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

import org.n52.shetland.inspire.GeographicalName;
import org.n52.svalbard.coding.json.AQDJSONConstants;
import org.n52.svalbard.encode.exception.EncodingException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class GeographicNameJSONEncoder extends JSONEncoder<GeographicalName> {

    public GeographicNameJSONEncoder() {
        super(GeographicalName.class);
    }

    @Override
    public JsonNode encodeJSON(GeographicalName t)
            throws EncodingException {
        ObjectNode j = nodeFactory().objectNode();
        j.set(AQDJSONConstants.GRAMMATICAL_GENDER, encodeObjectToJson(t.getGrammaticalGender()));
        j.set(AQDJSONConstants.GRAMMATICAL_NUMBER, encodeObjectToJson(t.getGrammaticalNumber()));
        j.set(AQDJSONConstants.LANGUAGE, encodeObjectToJson(t.getLanguage()));
        j.set(AQDJSONConstants.NAME_STATUS, encodeObjectToJson(t.getNameStatus()));
        j.set(AQDJSONConstants.NATIVENESS, encodeObjectToJson(t.getNativeness()));
        j.set(AQDJSONConstants.PRONUNCIATION, encodeObjectToJson(t.getPronunciation()));
        j.set(AQDJSONConstants.SOURCE_OF_NAME, encodeObjectToJson(t.getSourceOfName()));
        j.set(AQDJSONConstants.SPELLING, encodeObjectToJson(t.getSpelling()));
        return j;
    }

}
