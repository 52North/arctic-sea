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

import java.net.URI;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collector;

import javax.inject.Inject;

import org.n52.janmayen.Json;
import org.n52.janmayen.exception.CompositeException;
import org.n52.janmayen.function.ThrowingFunction;
import org.n52.janmayen.http.MediaType;
import org.n52.janmayen.http.MediaTypes;
import org.n52.shetland.ogc.gml.CodeType;
import org.n52.shetland.ogc.gml.CodeWithAuthority;
import org.n52.shetland.ogc.ows.OwsCode;
import org.n52.svalbard.coding.json.JSONConstants;
import org.n52.svalbard.encode.Encoder;
import org.n52.svalbard.encode.EncoderKey;
import org.n52.svalbard.encode.EncoderRepository;
import org.n52.svalbard.encode.EncodingContext;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.exception.NoEncoderForKeyException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;

/**
 * TODO JavaDoc
 *
 * @param <T>
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
public abstract class JSONEncoder<T>
        implements Encoder<JsonNode, T> {
    public static final String CONTENT_TYPE = "application/json";

    private final Set<EncoderKey> encoderKeys;
    private EncoderRepository encoderRepository;

    public JSONEncoder(Class<? super T> type, EncoderKey... additionalKeys) {
        Builder<EncoderKey> set = ImmutableSet.builder();
        set.add(new JSONEncoderKey(type));
        set.addAll(Arrays.asList(additionalKeys));
        this.encoderKeys = set.build();
    }

    @Inject
    public void setEncoderRepository(EncoderRepository encoderRepository) {
        this.encoderRepository = Objects.requireNonNull(encoderRepository);
    }

    @Override
    public Set<EncoderKey> getKeys() {
        return Collections.unmodifiableSet(encoderKeys);
    }

    @Override
    public MediaType getContentType() {
        return MediaTypes.APPLICATION_JSON;
    }

    @Override
    public JsonNode encode(T objectToEncode, EncodingContext v)
            throws EncodingException {
        return encode(objectToEncode);
    }

    @Override
    public JsonNode encode(T objectToEncode)
            throws EncodingException {
        if (objectToEncode == null) {
            return nodeFactory().nullNode();
        }
        return encodeJSON(objectToEncode);
    }

    protected <T> void encode(ArrayNode json, T obj, Function<T, JsonNode> encoder) {
        json.add(encoder.apply(obj));
    }

    protected <T> void encode(ObjectNode json, String name, T obj, Function<T, JsonNode> encoder) {
        json.set(name, encoder.apply(obj));
    }

    public abstract JsonNode encodeJSON(T t)
            throws EncodingException;

    protected JsonNode encodeObjectToJson(Object o)
            throws EncodingException {
        if (o == null) {
            return nodeFactory().nullNode();
        }
        JSONEncoderKey key = new JSONEncoderKey(o.getClass());
        Encoder<JsonNode, Object> encoder = this.encoderRepository.getEncoder(key);
        return Optional.ofNullable(encoder).orElseThrow(() -> new NoEncoderForKeyException(key)).encode(o);
    }

    protected JsonNodeFactory nodeFactory() {
        return Json.nodeFactory();
    }

    protected <T> void encodeOptional(ObjectNode json, String name, Optional<T> obj, Function<T, JsonNode> encoder) {
        obj.map(encoder).ifPresent(node -> json.set(name, node));
    }

    protected <T> void encodeOptional(ArrayNode json, Optional<T> obj, Function<T, JsonNode> encoder) {
        obj.map(encoder).ifPresent(json::add);
    }

    protected <T, X extends Exception> void encodeOptionalChecked(ObjectNode json, String name, Optional<T> obj,
            ThrowingFunction<T, JsonNode, X> encoder)
            throws X {
        if (obj.isPresent()) {
            Optional.ofNullable(encoder.apply(obj.get())).ifPresent(node -> json.set(name, node));
        }
    }

    protected <T, X extends Exception> void encodeOptionalChecked(ArrayNode json, Optional<T> obj,
            ThrowingFunction<T, JsonNode, X> encoder)
            throws X {
        if (obj.isPresent()) {
            Optional.ofNullable(encoder.apply(obj.get())).ifPresent(json::add);
        }
    }

    protected <T> void encodeList(ObjectNode json, String name, Collection<T> collection,
            Function<T, JsonNode> encoder) {
        if (!collection.isEmpty()) {
            json.set(name, collection.stream().map(encoder).collect(toJsonArray()));
        }
    }

    protected <T, X extends Exception> void encodeListChecked(ObjectNode json, String name, Collection<T> collection,
            ThrowingFunction<T, JsonNode, X> encoder)
            throws CompositeException {
        if (!collection.isEmpty()) {
            CompositeException exceptions = new CompositeException();
            json.set(name, collection.stream().map(exceptions.wrapFunction(encoder))
                    .map(o -> o.orElseGet(nodeFactory()::nullNode)).collect(toJsonArray()));
            if (!exceptions.isEmpty()) {
                throw exceptions;
            }
        }
    }

    protected <T, X extends Exception> void encodeChecked(ObjectNode json, String name, T obj,
            ThrowingFunction<T, JsonNode, X> encoder)
            throws X {
        json.set(name, encoder.apply(obj));
    }

    protected <T, X extends Exception> void encodeChecked(ArrayNode json, T obj,
            ThrowingFunction<T, JsonNode, X> encoder)
            throws X {
        json.add(encoder.apply(obj));
    }

    protected <T> JsonNode encodeAsString(T t) {
        return (t == null) ? nodeFactory().nullNode() : nodeFactory().textNode(t.toString());
    }

    protected JsonNode encodeURI(URI uri) {
        return encodeAsString(uri);
    }

    protected JsonNode encodeCodeType(CodeType codeType) {
        return encodeCodeType(Optional.ofNullable(codeType.getCodeSpace()).map(URI::toString), codeType.getValue());
    }

    protected JsonNode encodeCodeType(Optional<String> codeSpace, String value) {
        if (codeSpace.isPresent()) {
            return nodeFactory().objectNode().put(JSONConstants.CODESPACE, codeSpace.get()).put(JSONConstants.VALUE,
                    value);
        } else {
            return nodeFactory().textNode(value);
        }
    }

    protected JsonNode encodeCodeWithAuthority(CodeWithAuthority cwa) {
        return encodeCodeType(Optional.ofNullable(Strings.emptyToNull(cwa.getCodeSpace())), cwa.getValue());
    }

    protected JsonNode encodeOwsCode(OwsCode codeType) {
        return encodeCodeType(codeType.getCodeSpace().map(URI::toString), codeType.getValue());
    }

    protected Collector<JsonNode, ?, ArrayNode> toJsonArray() {
        return Collector.of(nodeFactory()::arrayNode, ArrayNode::add, (BinaryOperator<ArrayNode>) ArrayNode::addAll);
    }

    protected <T> Collector<T, ?, ObjectNode> toJsonObject(
            Collector<T, ?, ? extends Map<String, ? extends JsonNode>> mapper) {
        @SuppressWarnings("unchecked")
        Collector<T, Object, Map<String, ? extends JsonNode>> m =
                (Collector<T, Object, Map<String, ? extends JsonNode>>) mapper;
        Function<Map<String, ? extends JsonNode>, ObjectNode> finisher = map -> {
            ObjectNode node = nodeFactory().objectNode();
            node.setAll(map);
            return node;
        };
        return Collector.of(m.supplier(), m.accumulator(), m.combiner(), m.finisher().andThen(finisher));
    }
}
