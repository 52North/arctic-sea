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

import org.n52.shetland.w3c.xlink.Reference;
import org.n52.svalbard.coding.json.AQDJSONConstants;
import org.n52.svalbard.encode.exception.EncodingException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class ReferenceJSONEncoder extends JSONEncoder<Reference> {

    public ReferenceJSONEncoder() {
        super(Reference.class);
    }

    @Override
    public JsonNode encodeJSON(Reference t)
            throws EncodingException {
        ObjectNode ref = nodeFactory().objectNode();
        if (t.getHref().isPresent()) {
            ref.put(AQDJSONConstants.HREF, t.getHref().get().toString());
        }
        if (t.getActuate().isPresent()) {
            ref.put(AQDJSONConstants.ACTUATE, t.getActuate().get().toString());
        }
        if (t.getArcrole().isPresent()) {
            ref.put(AQDJSONConstants.ARCROLE, t.getArcrole().get());
        }
        if (t.getRemoteSchema().isPresent()) {
            ref.put(AQDJSONConstants.REMOTE_SCHEMA, t.getRemoteSchema().get());
        }
        if (t.getRole().isPresent()) {
            ref.put(AQDJSONConstants.ROLE, t.getRole().get());
        }
        if (t.getShow().isPresent()) {
            ref.put(AQDJSONConstants.SHOW, t.getShow().get().toString());
        }
        if (t.getTitle().isPresent()) {
            ref.put(AQDJSONConstants.TITLE, t.getTitle().get());
        }
        if (t.getType().isPresent()) {
            ref.put(AQDJSONConstants.TYPE, t.getType().get().toString());
        }
        return ref;
    }

}
