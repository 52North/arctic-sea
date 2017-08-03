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

import java.net.URI;

import org.n52.shetland.inspire.Pronunciation;
import org.n52.svalbard.coding.json.AQDJSONConstants;
import org.n52.svalbard.decode.exception.DecodingException;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Function;

public class PronunciationJSONDecoder extends AbstractJSONDecoder<Pronunciation> {

    private static final Function<String, URI> STRING_TO_URI = new Function<String, URI>() {

        @Override
        public URI apply(String input) {
            return URI.create(input);
        }
    };

    public PronunciationJSONDecoder() {
        super(Pronunciation.class);
    }

    @Override
    public Pronunciation decodeJSON(JsonNode node, boolean validate)
            throws DecodingException {
        Pronunciation pronunciation = new Pronunciation();
        pronunciation
                .setIPA(parseNillableString(node.path(AQDJSONConstants.IPA)));
        pronunciation.setSoundLink(parseNillableString(node
                .path(AQDJSONConstants.SOUND_LINK)).transform(stringToURI()));
        return pronunciation;
    }

    public static Function<String, URI> stringToURI() {
        return STRING_TO_URI;
    }

}
