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
package org.n52.svalbard.decode.json;

import java.net.URI;
import java.util.Iterator;

import org.n52.janmayen.function.ThrowableFunction;
import org.n52.shetland.iso.gmd.LocalisedCharacterString;
import org.n52.shetland.iso.gmd.PT_FreeText;
import org.n52.shetland.ogc.gml.CodeType;
import org.n52.shetland.ogc.gml.time.Time;
import org.n52.shetland.util.DateTimeParseException;
import org.n52.shetland.w3c.Nillable;
import org.n52.shetland.w3c.xlink.Reference;
import org.n52.shetland.w3c.xlink.Referenceable;
import org.n52.svalbard.coding.json.AQDJSONConstants;
import org.n52.svalbard.decode.exception.DecodingException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

public abstract class AbstractJSONDecoder<T> extends JSONDecoder<T> {

    public AbstractJSONDecoder(Class<T> type) {
        super(type);
    }

    protected Nillable<String> parseNillableString(JsonNode node) {
        return parseNillable(node).map(JsonNode::textValue);
    }

    protected Nillable<PT_FreeText> parseNillablePTFreeText(JsonNode node) {
        return parseNillable(node).map(this::parsePTFreeText);
    }

    protected Nillable<JsonNode> parseNillable(JsonNode node) {
        if (node.isMissingNode() || node.isNull()) {
            return Nillable.absent();
        } else if (node.isObject() && node.path(AQDJSONConstants.NIL).asBoolean()) {
            return Nillable.nil(node.path(AQDJSONConstants.REASON).textValue());
        }
        return Nillable.of(node);
    }

    protected Nillable<Reference> parseNillableReference(JsonNode node) {
        return parseNillable(node).map(this::parseReference);
    }

    protected Referenceable<JsonNode> parseReferenceable(JsonNode node) {
        Nillable<JsonNode> nillable = parseNillable(node);

        if (nillable.isAbsent() || nillable.isNil()) {
            return Referenceable.of(nillable);
        }

        if (node.has(AQDJSONConstants.HREF)) {
            return Referenceable.of(parseReference(node));
        }

        return Referenceable.of(node);
    }

    protected Reference parseReference(JsonNode node) {
        Reference ref = new Reference();
        ref.setHref(URI.create(node.path(AQDJSONConstants.HREF).textValue()));
        ref.setActuate(node.path(AQDJSONConstants.ACTUATE).textValue());
        ref.setArcrole(node.path(AQDJSONConstants.ARCROLE).textValue());
        ref.setRemoteSchema(node.path(AQDJSONConstants.REMOTE_SCHEMA).textValue());
        ref.setRole(node.path(AQDJSONConstants.ROLE).textValue());
        ref.setShow(node.path(AQDJSONConstants.SHOW).textValue());
        ref.setTitle(node.path(AQDJSONConstants.TITLE).textValue());
        ref.setType(node.path(AQDJSONConstants.TYPE).textValue());
        return ref;
    }

    protected PT_FreeText parsePTFreeText(JsonNode node) {
        PT_FreeText ptFreeText = new PT_FreeText();
        ptFreeText.addTextGroup(new LocalisedCharacterString(node.textValue()));
        return ptFreeText;
    }

    protected Referenceable<Time> parseReferenceableTime(JsonNode node) {
        return parseReferenceable(node).map(node1 -> {
            try {
                return parseTime(node1);
            } catch (DateTimeParseException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    protected Nillable<CodeType> parseNillableCodeType(JsonNode node) {
        return parseNillable(node).map(this::parseCodeType);
    }

    protected PT_FreeText parseFreeText(JsonNode n) {
        LocalisedCharacterString localisedCharacterString = new LocalisedCharacterString("");
        if (n.isArray()) {
            ArrayNode arrayNode = (ArrayNode) n;
            Iterator<JsonNode> it = arrayNode.iterator();
            while (it.hasNext()) {
                final JsonNode next = it.next();
                checkAndAddTextAndLanguage(next, localisedCharacterString);
            }
        } else if (n.isTextual()) {
            localisedCharacterString.setValue(n.asText());
        } else if (n.isObject()) {
            checkAndAddTextAndLanguage(n, localisedCharacterString);
        }
        return new PT_FreeText().addTextGroup(localisedCharacterString);
    }

    private void checkAndAddTextAndLanguage(JsonNode n, LocalisedCharacterString localisedCharacterString) {
        if (n.has(AQDJSONConstants.TEXT)) {
            localisedCharacterString.setValue(n.get(AQDJSONConstants.TEXT).asText());
        }
        if (n.has(AQDJSONConstants.LANGUAGE) && !n.get(AQDJSONConstants.LANGUAGE).isNull()) {
            localisedCharacterString.setLocale(n.get(AQDJSONConstants.LANGUAGE).asText());
        }
    }

    protected <T> Nillable<T> decodeJsonToNillable(JsonNode node, final Class<T> type) throws DecodingException {
        ThrowableFunction<JsonNode, T> fun = new ThrowableFunction<JsonNode, T>() {

            @Override
            protected T applyThrowable(JsonNode input) throws DecodingException {
                return decodeJsonToObject(input, type);
            }
        };

        Nillable<T> result = parseNillable(node).map(fun);

        if (fun.hasErrors()) {
            fun.propagateIfPossible(DecodingException.class);
        }
        return result;
    }

    protected <T> Referenceable<T> decodeJsonToReferencable(JsonNode node, final Class<T> type)
            throws DecodingException {
        ThrowableFunction<JsonNode, T> fun = new ThrowableFunction<JsonNode, T>() {

            @Override
            protected T applyThrowable(JsonNode input) throws DecodingException {
                return decodeJsonToObject(input, type);
            }
        };

        Referenceable<T> result = parseReferenceable(node).map(fun);

        if (fun.hasErrors()) {
            fun.propagateIfPossible(DecodingException.class);
        }
        return result;
    }
}
