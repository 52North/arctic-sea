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

public abstract class AbstractJSONDecoder<T>
        extends JSONDecoder<T> {

    public AbstractJSONDecoder(Class<T> type) {
        super(type);
    }

    protected Nillable<String> parseNillableString(JsonNode node) {
        return parseNillable(node).transform(JsonNode::textValue);
    }

    protected Nillable<PT_FreeText> parseNillablePTFreeText(JsonNode node) {
        return parseNillable(node).transform(this::parsePTFreeText);
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
        return parseNillable(node).transform(this::parseReference);
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
        return parseReferenceable(node).transform(node1 -> {
            try {
                return parseTime(node1);
            } catch (DateTimeParseException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    protected Nillable<CodeType> parseNillableCodeType(JsonNode node) {
        return parseNillable(node).transform(this::parseCodeType);
    }

    protected <T> Nillable<T> decodeJsonToNillable(JsonNode node, final Class<T> type)
            throws DecodingException {
        ThrowableFunction<JsonNode, T> fun = new ThrowableFunction<JsonNode, T>() {

            @Override
            protected T applyThrowable(JsonNode input)
                    throws DecodingException {
                return decodeJsonToObject(input, type);
            }
        };

        Nillable<T> result = parseNillable(node).transform(fun);

        if (fun.hasErrors()) {
            fun.propagateIfPossible(DecodingException.class);
        }
        return result;
    }

    protected <T> Referenceable<T> decodeJsonToReferencable(JsonNode node, final Class<T> type)
            throws DecodingException {
        ThrowableFunction<JsonNode, T> fun = new ThrowableFunction<JsonNode, T>() {

            @Override
            protected T applyThrowable(JsonNode input)
                    throws DecodingException {
                return decodeJsonToObject(input, type);
            }
        };

        Referenceable<T> result = parseReferenceable(node).transform(fun);

        if (fun.hasErrors()) {
            fun.propagateIfPossible(DecodingException.class);
        }
        return result;
    }
}
