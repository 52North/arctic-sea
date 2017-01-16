/*
 * Copyright 2015-2017 52°North Initiative for Geospatial Open Source
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
package org.n52.iceland.binding.kvp;


import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.n52.janmayen.exception.CompositeException;
import org.n52.janmayen.function.ThrowingBiConsumer;
import org.n52.janmayen.function.ThrowingTriConsumer;
import org.n52.janmayen.http.MediaType;
import org.n52.janmayen.http.MediaTypes;
import org.n52.shetland.ogc.ows.OWSConstants;
import org.n52.shetland.ogc.ows.service.OwsServiceRequest;
import org.n52.shetland.util.StringHelper;
import org.n52.svalbard.decode.Decoder;
import org.n52.svalbard.decode.DecoderKey;
import org.n52.svalbard.decode.OperationDecoderKey;
import org.n52.svalbard.decode.exception.DecodingException;

import com.google.common.base.Strings;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 * @param <R> the request type
 */
public abstract class AbstractKvpDecoder<R extends OwsServiceRequest> implements Decoder<R, Map<String,String>> {

    private final Set<DecoderKey> keys;
    private final Supplier<? extends R> supplier;

    public AbstractKvpDecoder(Supplier<? extends R> supplier, String service, String version, String operation) {
        this(supplier, new OperationDecoderKey(service, version, operation, MediaTypes.APPLICATION_KVP));
    }

    public AbstractKvpDecoder(Supplier<? extends R> supplier, String service, String version, Enum<?> operation) {
        this(supplier, service, version, operation.name());
    }

    public AbstractKvpDecoder(Supplier<? extends R> supplier, DecoderKey... keys) {
        this(supplier, Arrays.asList(keys));
    }

    public AbstractKvpDecoder(Supplier<? extends R> supplier, Collection<? extends DecoderKey> keys) {
        this.supplier = Objects.requireNonNull(supplier);
        this.keys = new HashSet<>(keys);
    }

    @Override
    public Set<DecoderKey> getKeys() {
        return Collections.unmodifiableSet(this.keys);
    }

    @Override
    public R decode(Map<String,String> parameters) throws DecodingException {

        CompositeException exceptions = new CompositeException();
        R request = this.supplier.get();

        parameters.forEach(exceptions.wrap(getDecoder(new Builder<R>()
                        .add(this::getCommonRequestParameterDefinitions)
                        .add(this::getRequestParameterDefinitions).build()).curryFirst(request)));

        if (exceptions.hasExceptions()) {
            throw new DecodingException(exceptions);
        }

        return request;
    }

    @SuppressWarnings("rawtypes")
    protected void getCommonRequestParameterDefinitions(Builder<R> builder) {
        builder.add(OWSConstants.RequestParams.service, OwsServiceRequest::setService);
        builder.add(OWSConstants.RequestParams.version, OwsServiceRequest::setVersion);
        builder.add(OWSConstants.RequestParams.request, OwsServiceRequest::setOperationName);

    }

    protected List<String> decodeList(String value) {
        return value == null ? null : StringHelper.splitToList(value, ",");
    }

    protected ThrowingBiConsumer<R, String, DecodingException> decodeList(ThrowingBiConsumer<? super R, ? super List<String>, DecodingException> delegate) {
        return (request, value) -> delegate.accept(request, decodeList(value));
    }

    protected <T> ThrowingBiConsumer<R, T, DecodingException> asList(ThrowingBiConsumer<? super R, ? super List<T>, DecodingException> delegate) {
        return (request, value) -> delegate.accept(request, Collections.singletonList(value));
    }

    protected ThrowingTriConsumer<R, String, String, DecodingException> decodeList(ThrowingTriConsumer<? super R, ? super String, ? super List<String>, DecodingException> delegate) {
        return (request, name, value) -> delegate.accept(request, name, decodeList(value));
    }

    protected ThrowingBiConsumer<R, String, DecodingException> normalizeMediaType(ThrowingBiConsumer<? super R, ? super String, DecodingException> delegate) {
        return (request, value) -> delegate.accept(request, MediaType.normalizeString(value));
    }

    protected abstract void getRequestParameterDefinitions(Builder<R> builder);

    private ThrowingTriConsumer<R, String, String, DecodingException> getDecoder(
            Map<String, ThrowingBiConsumer<? super R, String, DecodingException>> parsers) {
        return (request, name, value) -> {
            if (parsers.containsKey(name)) {
                parsers.get(name).accept(request, value);
            } else {
                throw new DecodingException(name, "The parameter '%s' is not supported by this service!", name);
            }
        };
    }

    protected static class Builder<R extends OwsServiceRequest> {
        private final Map<String, ThrowingBiConsumer<? super R, String, DecodingException>> parsers
                = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

        private Builder() {
        }

        @SuppressWarnings("unchecked")
        public Builder<R> add(String name, ThrowingBiConsumer<? super R, String, DecodingException> parser) {
            Objects.requireNonNull(Strings.emptyToNull(name));
            Objects.requireNonNull(parser);
            this.parsers.merge(name, parser, (f1, f2) -> ((ThrowingBiConsumer<R, String, DecodingException>) f1)
                    .andThen(f2));
            return this;
        }

        public Builder<R> add(Enum<?> name, ThrowingBiConsumer<? super R, String, DecodingException> parser) {
            return add(name.name(), parser);
        }

        public Builder<R> add(String name, ThrowingTriConsumer<? super R, String, String, DecodingException> parser) {
            return add(name, parser.currySecond(name));
        }

        public Builder<R> add(Enum<?> name, ThrowingTriConsumer<? super R, String, String, DecodingException> parser) {
            return add(name.name(), parser);
        }

        public Builder<R> add(Consumer<Builder<R>> consumer) {
            consumer.accept(this);
            return this;
        }

        public Map<String, ThrowingBiConsumer<? super R, String, DecodingException>> build() {
            return Collections.unmodifiableMap(this.parsers);
        }
    }
}
