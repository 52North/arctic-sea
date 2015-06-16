/*
 * Copyright 2015 52°North Initiative for Geospatial Open Source
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
package org.n52.iceland.coding.decode;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.n52.iceland.coding.AbstractCodingRepository;

import org.springframework.beans.factory.annotation.Autowired;

import org.n52.iceland.coding.decode.Decoder;
import org.n52.iceland.coding.decode.DecoderKey;
import org.n52.iceland.decode.DecoderFactory;
import org.n52.iceland.lifecycle.Constructable;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class DecoderRepository
        extends AbstractCodingRepository<DecoderKey, Decoder<?, ?>, DecoderFactory> implements Constructable {

    @Autowired(required = false)
    private Collection<Decoder<?, ?>> decoders;
    @Autowired(required = false)
    private Collection<DecoderFactory> decoderFactories;

    @Override
    public void init() {
        setProducers(getProviders(decoders, decoderFactories));
    }

    public Set<Decoder<?, ?>> getDecoders() {
        return getComponents();
    }

    public boolean hasDecoder(DecoderKey key, DecoderKey... keys) {
        return hasComponent(key, keys);
    }

    @SuppressWarnings("unchecked")
    public <F, T> Decoder<F, T> getDecoder(DecoderKey key, DecoderKey... keys) {
        return (Decoder<F, T>) getComponent(key, keys);
    }

    @Override
    protected CompositeKey createCompositeKey(List<DecoderKey> keys) {
        return new CompositeDecoderKey(keys);
    }

    private class CompositeDecoderKey extends CompositeKey implements DecoderKey {
        CompositeDecoderKey(Iterable<DecoderKey> keys) {
            super(keys);
        }

        @Override
        public DecoderKey asKey() {
            return this;
        }
    }
}
