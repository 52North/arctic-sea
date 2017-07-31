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
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.joda.time.DateTime;
import org.n52.janmayen.exception.CompositeException;
import org.n52.janmayen.stream.Streams;
import org.n52.shetland.ogc.OGCConstants;
import org.n52.shetland.ogc.gml.CodeType;
import org.n52.shetland.ogc.gml.CodeWithAuthority;
import org.n52.shetland.ogc.gml.time.IndeterminateValue;
import org.n52.shetland.ogc.gml.time.Time;
import org.n52.shetland.ogc.gml.time.TimeInstant;
import org.n52.shetland.ogc.gml.time.TimePeriod;
import org.n52.shetland.util.DateTimeHelper;
import org.n52.shetland.util.DateTimeParseException;
import org.n52.svalbard.coding.json.JSONConstants;
import org.n52.svalbard.decode.AbstractDelegatingDecoder;
import org.n52.svalbard.decode.Decoder;
import org.n52.svalbard.decode.DecoderKey;
import org.n52.svalbard.decode.JsonDecoderKey;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.decode.exception.NoDecoderForKeyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

/**
 * TODO JavaDoc
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
public abstract class JSONDecoder<T>
        extends AbstractDelegatingDecoder<T, JsonNode> {
    private static final Logger LOGGER = LoggerFactory.getLogger(JSONDecoder.class);
    private final Set<DecoderKey> decoderKeys;

    public JSONDecoder(Class<T> type) {
        this(Collections.<DecoderKey> singleton(new JsonDecoderKey(type)));
    }

    public JSONDecoder(Set<DecoderKey> keys) {
        this.decoderKeys = keys;
    }

    private <T> Decoder<T, JsonNode> getDecoder(Class<T> type)
            throws DecodingException {
        JsonDecoderKey key = new JsonDecoderKey(type);
        Decoder<T, JsonNode> decoder = getDecoder(key);
        if (decoder == null) {
            throw new NoDecoderForKeyException(key);
        }
        return decoder;
    }

    protected <T> T decodeJsonToObject(JsonNode json, Class<T> type)
            throws DecodingException {
        if (json == null || json.isNull() || json.isMissingNode()) {
            return null;
        }
        return getDecoder(type).decode(json);
    }

    protected <T> List<T> decodeJsonToObjectList(JsonNode node, Class<T> type)
            throws DecodingException {
        Decoder<T, JsonNode> decoder = getDecoder(type);
        if (node.isArray()) {
            CompositeException exceptions = new CompositeException();
            List<T> list =
                    Streams.stream(node).filter(JsonNode::isObject).map(exceptions.wrapFunction(decoder::decode))
                            .filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
            exceptions.throwIfNotEmpty(DecodingException::new);
            return list;
        } else if (node.isObject()) {
            return Collections.singletonList(decoder.decode(node));
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public Set<DecoderKey> getKeys() {
        return Collections.unmodifiableSet(decoderKeys);
    }

    @Override
    public T decode(JsonNode objectToDecode)
            throws DecodingException {
        return decodeJSON(objectToDecode, true);
    }

    protected TimeInstant parseTimeInstant(JsonNode node)
            throws DateTimeParseException {
        if (node.isTextual()) {
            return new TimeInstant(parseDateTime(node.textValue()));
        } else if (node.path(JSONConstants.INDETERMINATE_VALUE).isTextual()) {
            return new TimeInstant(new IndeterminateValue(node.path(JSONConstants.INDETERMINATE_VALUE).textValue()));
        }
        return null;
    }

    protected TimePeriod parseTimePeriod(JsonNode node)
            throws DateTimeParseException {
        if (node.isArray()) {
            ArrayNode array = (ArrayNode) node;
            String startTime = array.get(0).textValue();
            String endTime = array.get(1).textValue();
            DateTime start = parseDateTime(startTime);
            DateTime end = parseDateTime(endTime);
            return new TimePeriod(start, end);
        } else {
            return null;
        }
    }

    protected DateTime parseDateTime(String time)
            throws DateTimeParseException {
        return DateTimeHelper.parseIsoString2DateTime(time);
    }

    protected Time parseTime(JsonNode node)
            throws DateTimeParseException {
        if (node.isArray()) {
            return parseTimePeriod(node);
        } else if (node.isTextual()) {
            return parseTimeInstant(node);
        } else {
            return null;
        }
    }

    protected CodeWithAuthority parseCodeWithAuthority(JsonNode node) {
        if (node.isObject()) {
            String value = node.path(JSONConstants.VALUE).textValue();
            String codespace = node.path(JSONConstants.CODESPACE).textValue();
            if (codespace == null || codespace.isEmpty()) {
                codespace = OGCConstants.UNKNOWN;
            }
            return new CodeWithAuthority(value, codespace);
        } else if (node.isTextual()) {
            return new CodeWithAuthority(node.textValue(), OGCConstants.UNKNOWN);
        } else {
            return null;
        }
    }

    protected CodeType parseCodeType(JsonNode node) {
        try {
            if (node.isObject()) {
                String value = node.path(JSONConstants.VALUE).textValue();
                String codespace = node.path(JSONConstants.CODESPACE).textValue();
                if (codespace == null || codespace.isEmpty()) {
                    codespace = OGCConstants.UNKNOWN;
                }
                return (CodeType) new CodeType(value).setCodeSpace(new URI(codespace));
            } else if (node.isTextual()) {

                return (CodeType) new CodeType(node.textValue()).setCodeSpace(new URI(OGCConstants.UNKNOWN));
            }
        } catch (URISyntaxException e) {
            LOGGER.error("Error while creating URI!", e);
        }
        return null;
    }

    public abstract T decodeJSON(JsonNode node, boolean validate)
            throws DecodingException;
}
