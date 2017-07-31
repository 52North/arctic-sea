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

import org.n52.shetland.inspire.GeographicalName;
import org.n52.shetland.inspire.Pronunciation;
import org.n52.shetland.inspire.Spelling;
import org.n52.svalbard.coding.json.AQDJSONConstants;
import org.n52.svalbard.decode.exception.DecodingException;

import com.fasterxml.jackson.databind.JsonNode;

public class GeographicalNameJSONDecoder
        extends AbstractJSONDecoder<GeographicalName> {

    public GeographicalNameJSONDecoder() {
        super(GeographicalName.class);
    }

    @Override
    public GeographicalName decodeJSON(JsonNode node, boolean validate)
            throws DecodingException {
        GeographicalName geographicalName = new GeographicalName();
        geographicalName.setGrammaticalGender(parseNillableCodeType(node.path(AQDJSONConstants.GRAMMATICAL_GENDER)));
        geographicalName.setGrammaticalNumber(parseNillableCodeType(node.path(AQDJSONConstants.GRAMMATICAL_NUMBER)));
        geographicalName.setLanguage(parseNillableString(node.path(AQDJSONConstants.LANGUAGE)));
        geographicalName.setNameStatus(parseNillableCodeType(node.path(AQDJSONConstants.NAME_STATUS)));
        geographicalName.setNativeness(parseNillableCodeType(node.path(AQDJSONConstants.NATIVENESS)));
        geographicalName.setSourceOfName(parseNillableString(node.path(AQDJSONConstants.SOURCE_OF_NAME)));
        geographicalName.setPronunciation(
                decodeJsonToNillable(node.path(AQDJSONConstants.PRONUNCIATION), Pronunciation.class));
        for (JsonNode n : node.path(AQDJSONConstants.SPELLING)) {
            geographicalName.addSpelling(decodeJsonToObject(n, Spelling.class));
        }
        return geographicalName;
    }
}
