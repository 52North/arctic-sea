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

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.n52.janmayen.http.MediaTypes;
import org.n52.janmayen.stream.Streams;
import org.n52.shetland.ogc.filter.ComparisonFilter;
import org.n52.shetland.ogc.ows.extension.Extension;
import org.n52.shetland.ogc.ows.service.OwsServiceRequest;
import org.n52.shetland.ogc.swe.SweAbstractDataComponent;
import org.n52.shetland.ogc.swe.simpleType.SweBoolean;
import org.n52.shetland.ogc.swe.simpleType.SweText;
import org.n52.shetland.ogc.swes.SwesExtension;
import org.n52.shetland.ogc.swes.SwesExtensions;
import org.n52.svalbard.coding.json.JSONConstants;
import org.n52.svalbard.coding.json.JSONValidator;
import org.n52.svalbard.decode.DecoderKey;
import org.n52.svalbard.decode.JsonDecoderKey;
import org.n52.svalbard.decode.OperationDecoderKey;
import org.n52.svalbard.decode.exception.DecodingException;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * TODO JavaDoc
 *
 * @param <T>
 *            Class type
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
public abstract class AbstractSosRequestDecoder<T extends OwsServiceRequest>
        extends JSONDecoder<T> {

    public AbstractSosRequestDecoder(Class<T> type, String service, String version, Enum<?> operation) {
        this(type, service, version, operation.name());
    }

    public AbstractSosRequestDecoder(Class<T> type, String service, String version, String operation) {
        super(new HashSet<>(Arrays.asList(new JsonDecoderKey(type),
                new OperationDecoderKey(service, version, operation, MediaTypes.APPLICATION_JSON))));
    }

    public AbstractSosRequestDecoder(Set<DecoderKey> keys) {
        super(keys);
    }

    public AbstractSosRequestDecoder(Class<T> type, String service, Enum<?> operation) {
        this(type, service, null, operation.name());
    }

    public AbstractSosRequestDecoder(Class<T> type, String service, String operation) {
        this(type, service, null, operation);
    }

    @Override
    public T decodeJSON(JsonNode node, boolean validate)
            throws DecodingException {
        if (node == null || node.isNull() || node.isMissingNode()) {
            return null;
        }
        if (validate) {
            JSONValidator.getInstance().validateAndThrow(node, getSchemaURI());
        }
        T t = decodeRequest(node);
        t.setService(node.path(JSONConstants.SERVICE).textValue());
        t.setVersion(node.path(JSONConstants.VERSION).textValue());
        t.setExtensions(parseExtensions(node.path(JSONConstants.EXTENSIONS)));
        return t;

    }

    protected SwesExtensions parseExtensions(JsonNode node) {
        SwesExtensions extensions = new SwesExtensions();
        if (node.isArray()) {
            for (JsonNode n : node) {
                Extension<SweAbstractDataComponent> extension = parseExtension(n);
                if (extension != null) {
                    extensions.addExtension(extension);
                }
            }
        }
        return extensions;
    }

    protected Extension<SweAbstractDataComponent> parseExtension(JsonNode node) {
        if (node.isObject() && node.has(JSONConstants.DEFINITION) && node.has(JSONConstants.VALUE)) {
            if (node.path("value").isBoolean()) {
                return new SwesExtension<SweAbstractDataComponent>()
                        .setDefinition(node.path(JSONConstants.DEFINITION).asText())
                        .setValue(new SweBoolean().setValue(node.path(JSONConstants.VALUE).asBoolean()));
            } else if (node.path(JSONConstants.VALUE).isTextual()) {
                return new SwesExtension<SweAbstractDataComponent>()
                        .setDefinition(node.path(JSONConstants.DEFINITION).asText())
                        .setValue(new SweText().setValue(node.path(JSONConstants.VALUE).asText()));
            }
        }
        return null;
    }

    protected List<String> parseStringOrStringList(JsonNode node) {
        if (node.isArray()) {
            return Streams.stream(node).filter(JsonNode::isTextual).map(JsonNode::textValue)
                    .collect(Collectors.toList());
        } else if (node.isTextual()) {
            return Collections.singletonList(node.textValue());
        } else {
            return null;
        }
    }

    protected ComparisonFilter parseComparisonFilter(JsonNode node) {
        // TODO
        return null;
    }

    protected abstract String getSchemaURI();

    protected abstract T decodeRequest(JsonNode node)
            throws DecodingException;
}
