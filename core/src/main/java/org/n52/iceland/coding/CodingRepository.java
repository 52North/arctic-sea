/*
 * Copyright 2015-2016 52°North Initiative for Geospatial Open Source
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
package org.n52.iceland.coding;

import org.n52.iceland.coding.encode.SchemaRepository;
import org.n52.iceland.coding.encode.EncoderRepository;
import org.n52.iceland.coding.decode.DecoderRepository;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.n52.iceland.coding.decode.DecoderKey;
import org.n52.iceland.coding.encode.EncoderKey;
import org.n52.iceland.lifecycle.Constructable;
import org.n52.iceland.w3c.SchemaLocation;
import org.n52.iceland.coding.decode.ConformanceClassDecoder;
import org.n52.iceland.coding.decode.Decoder;
import org.n52.iceland.coding.encode.ConformanceClassEncoder;
import org.n52.iceland.coding.encode.Encoder;

/**
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 * @see EncoderRepository
 * @see DecoderRepository
 */
@Deprecated
public class CodingRepository implements Constructable {

    @Deprecated
    private static CodingRepository instance;
    private EncoderRepository encoderRepository;
    private DecoderRepository decoderRepository;
    private SchemaRepository schemaRepository;

    @Inject
    public void setEncoderRepository(EncoderRepository encoderRepository) {
        this.encoderRepository = encoderRepository;
    }

    @Inject
    public void setDecoderRepository(DecoderRepository decoderRepository) {
        this.decoderRepository = decoderRepository;
    }

    @Inject
    public void setSchemaRepository(SchemaRepository schemaRepository) {
        this.schemaRepository = schemaRepository;
    }

    @Override
    public void init() {
        CodingRepository.instance = this;
    }

    @Deprecated
    public Set<Decoder<?, ?>> getDecoders() {
        return this.decoderRepository.getDecoders();
    }

    @Deprecated
    public Set<Encoder<?, ?>> getEncoders() {
        return this.encoderRepository.getEncoders();
    }

    @Deprecated
    public Map<DecoderKey, Set<ConformanceClassDecoder<?, ?>>> getDecoderByKey() {
        return Collections.emptyMap();
    }

    @Deprecated
    public Map<EncoderKey, Set<ConformanceClassEncoder<?, ?>>> getEncoderByKey() {
        return Collections.emptyMap();
    }

    @Deprecated
    public boolean hasDecoder(DecoderKey key, DecoderKey... keys) {
        return this.decoderRepository.hasDecoder(key, keys);
    }

    @Deprecated
    public <F, T> Decoder<F, T> getDecoder(DecoderKey key, DecoderKey... keys) {
        return this.decoderRepository.getDecoder(key, keys);
    }

    @Deprecated
    public boolean hasEncoder(EncoderKey key, EncoderKey... keys) {
        return this.encoderRepository.hasEncoder(key, keys);
    }

    @Deprecated
    public <F, T> Encoder<F, T> getEncoder(EncoderKey key, EncoderKey... keys) {
        return this.encoderRepository.getEncoder(key, keys);
    }

    @Deprecated
    public Set<SchemaLocation> getSchemaLocation(String namespace) {
        return this.schemaRepository.getSchemaLocation(namespace);
    }

    @Deprecated
    public String getNamespaceFor(String prefix) {
        return this.schemaRepository.getNamespaceFor(prefix);
    }

    @Deprecated
    public String getPrefixFor(String namespace) {
        return this.schemaRepository.getPrefixFor(namespace);
    }

    @Deprecated
    public static CodingRepository getInstance() {
        return CodingRepository.instance;
    }
}
