/*
 * Copyright 2015-2018 52°North Initiative for Geospatial Open Source
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

import static java.util.stream.Collectors.toSet;

import java.util.Objects;
import java.util.Set;

import org.n52.iceland.util.activation.Activatable;
import org.n52.shetland.ogc.AbstractSupportedStringType;
import org.n52.shetland.ogc.SupportedType;
import org.n52.shetland.ogc.om.ObservationType;
import org.n52.shetland.ogc.sos.FeatureType;
import org.n52.svalbard.decode.Decoder;
import org.n52.svalbard.decode.DecoderRepository;
import org.n52.svalbard.encode.Encoder;
import org.n52.svalbard.encode.EncoderRepository;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Sets;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class SupportedTypeRepository {
    @Deprecated
    private static SupportedTypeRepository instance;
    private final Set<Activatable<SupportedType>> supportedTypes = Sets.newHashSet();
    private final LoadingCache<Class<? extends SupportedType>, Set<Activatable<SupportedType>>> cache;

    private EncoderRepository encoderRepository;
    private DecoderRepository decoderRepository;

    public SupportedTypeRepository() {
        this.cache = CacheBuilder.newBuilder().build(new CacheLoaderImpl());
    }

    public void init(DecoderRepository decoderRepository, EncoderRepository encoderRepository) {
        this.decoderRepository = decoderRepository;
        this.encoderRepository = encoderRepository;
        SupportedTypeRepository.instance = this;
        this.supportedTypes.clear();

        this.decoderRepository.getDecoders().stream()
                .map(Decoder::getSupportedTypes)
                .filter(Objects::nonNull)
                .map(Activatable::from)
                .forEachOrdered(this.supportedTypes::addAll);

        this.encoderRepository.getEncoders().stream()
                .map(Encoder::getSupportedTypes)
                .filter(Objects::nonNull)
                .map(Activatable::from)
                .forEachOrdered(this.supportedTypes::addAll);
    }

    private Set<? extends SupportedType> typesFor(Class<? extends SupportedType> key) {
        return Activatable.filter(this.cache.getUnchecked(key));
    }

    @SuppressWarnings("unchecked")
    public Set<ObservationType> getFeatureOfInterestTypes() {
        return (Set<ObservationType>) typesFor(FeatureType.class);
    }

    @SuppressWarnings("unchecked")
    public Set<String> getFeatureOfInterestTypesAsString() {
        return getSupportedTypeAsString((Set<AbstractSupportedStringType>) typesFor(FeatureType.class));
    }

    @SuppressWarnings("unchecked")
    public Set<ObservationType> getObservationTypes() {
        return (Set<ObservationType>) typesFor(ObservationType.class);
    }

    @SuppressWarnings("unchecked")
    public Set<String> getObservationTypesAsString() {
        return getSupportedTypeAsString((Set<AbstractSupportedStringType>) typesFor(ObservationType.class));
    }

    private Set<String> getSupportedTypeAsString(Set<? extends AbstractSupportedStringType> types) {
        return types.stream().map(AbstractSupportedStringType::getValue).collect(toSet());
    }

    @Deprecated
    public static SupportedTypeRepository getInstance() {
        return instance;
    }

    private class CacheLoaderImpl extends CacheLoader<Class<? extends SupportedType>, Set<Activatable<SupportedType>>> {
        @Override
        public Set<Activatable<SupportedType>> load(Class<? extends SupportedType> key) {
            return supportedTypes.stream()
                    .filter(activatable -> activatable.getInternal().getClass().isAssignableFrom(key))
                    .collect(toSet());
        }
    }
}
