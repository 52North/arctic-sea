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
package org.n52.iceland.coding;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import org.n52.iceland.encode.Encoder;
import org.n52.iceland.encode.EncoderFactory;
import org.n52.iceland.encode.EncoderKey;
import org.n52.iceland.lifecycle.Constructable;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class EncoderRepository extends AbstractCodingRepository<EncoderKey, Encoder<?, ?>, EncoderFactory>
        implements Constructable {

    @Autowired(required = false)
    private Collection<Encoder<?, ?>> encoders;
    @Autowired(required = false)
    private Collection<EncoderFactory> encoderFactories;

    @Override
    public void init() {
        setProducers(getProviders(encoders, encoderFactories));
    }

    public Set<Encoder<?, ?>> getEncoders() {
        return getComponents();
    }

    public boolean hasEncoder(EncoderKey key, EncoderKey... keys) {
        return hasComponent(key, keys);
    }

    public <F, T> Encoder<F, T> getEncoder(EncoderKey key, EncoderKey... keys) {
        return (Encoder<F, T>) getComponent(key, keys);
    }

    @Override
    protected CompositeKey createCompositeKey(List<EncoderKey> keys) {
        return new CompositeEncoderKey(keys);
    }

    private class CompositeEncoderKey extends CompositeKey
            implements EncoderKey {
        CompositeEncoderKey(Iterable<EncoderKey> keys) {
            super(keys);
        }

        @Override
        public EncoderKey asKey() {
            return this;
        }
    }
}
