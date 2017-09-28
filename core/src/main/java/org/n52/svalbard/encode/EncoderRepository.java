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
package org.n52.svalbard.encode;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;

import org.n52.janmayen.component.AbstractSimilarityKeyComponentRepository;
import org.n52.janmayen.lifecycle.Constructable;

import com.google.common.annotations.VisibleForTesting;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class EncoderRepository
        extends AbstractSimilarityKeyComponentRepository<EncoderKey, Encoder<?, ?>, EncoderFactory>
        implements Constructable {

    @Inject
    private Optional<Collection<Encoder<?, ?>>> encoders = Optional.of(Collections.emptyList());

    @Inject
    private Optional<Collection<EncoderFactory>> encoderFactories = Optional.of(Collections.emptyList());

    @Override
    public void init() {
        setProducers(getProviders(encoders, encoderFactories));
    }

    public Set<Encoder<?, ?>> getEncoders() {
        return getComponents();
    }

    @VisibleForTesting
    void setEncoders(Collection<Encoder<?, ?>> encoders) {
        this.encoders = Optional.of(encoders);
    }

    public boolean hasEncoder(EncoderKey key, EncoderKey... keys) {
        return hasComponent(key, keys);
    }

    public <F, T> Encoder<F, T> getEncoder(EncoderKey key, EncoderKey... keys) {
        return this.<F, T>tryGetEncoder(key, keys).orElse(null);
    }

    @SuppressWarnings("unchecked")
    public <F, T> Optional<Encoder<F, T>> tryGetEncoder(EncoderKey key, EncoderKey... keys) {
        return tryGetComponent(key, keys).map(e -> (Encoder<F, T>) e);
    }

    @Override
    protected CompositeKey createCompositeKey(List<EncoderKey> keys) {
        return new CompositeEncoderKey(keys);
    }

    @VisibleForTesting
    void setEncoderFactories(Collection<EncoderFactory> encoderFactories) {
        this.encoderFactories = Optional.of(encoderFactories);
    }

    private class CompositeEncoderKey extends CompositeKey implements EncoderKey {
        CompositeEncoderKey(Iterable<EncoderKey> keys) {
            super(keys);
        }

        @Override
        public EncoderKey asKey() {
            return this;
        }
    }
}
