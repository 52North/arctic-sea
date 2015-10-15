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

import java.util.Set;

import javax.inject.Inject;

import org.n52.iceland.coding.decode.Decoder;
import org.n52.iceland.coding.decode.DecoderRepository;
import org.n52.iceland.coding.encode.Encoder;
import org.n52.iceland.coding.encode.EncoderRepository;
import org.n52.iceland.lifecycle.Constructable;
import org.n52.iceland.service.ServiceConstants.AbstractSupportedStringType;
import org.n52.iceland.service.ServiceConstants.FeatureType;
import org.n52.iceland.service.ServiceConstants.ObservationType;
import org.n52.iceland.service.ServiceConstants.SupportedType;
import org.n52.iceland.util.activation.Activatable;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Sets;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class SupportedTypeRepository implements Constructable {
    @Deprecated
    private static SupportedTypeRepository instance;
    private final Set<Activatable<SupportedType>> supportedTypes = Sets.newHashSet();
    private final SetMultimap<Class<? extends SupportedType>, Set<Activatable<SupportedType>>> typeMap = HashMultimap.create();
    private final LoadingCache<Class<? extends SupportedType>, Set<Activatable<SupportedType>>> cache;

    private EncoderRepository encoderRepository;
    private DecoderRepository decoderRepository;

    public SupportedTypeRepository() {
        this.cache = CacheBuilder.newBuilder().build(new CacheLoaderImpl());
    }

    @Inject
    public void setEncoderRepository(EncoderRepository encoderRepository) {
        this.encoderRepository = encoderRepository;
    }

    @Inject
    public void setDecoderRepository(DecoderRepository decoderRepository) {
        this.decoderRepository = decoderRepository;
    }

    @Override
    public void init() {
        SupportedTypeRepository.instance = this;
        this.supportedTypes.clear();

        for (Decoder<?, ?> decoder : this.decoderRepository.getDecoders()) {
            Set<SupportedType> set = decoder.getSupportedTypes();
            if (set != null) {
                this.supportedTypes.addAll(Activatable.from(set));
            }
        }

        for (Encoder<?, ?> encoder : this.encoderRepository.getEncoders()) {
            Set<SupportedType> set = encoder.getSupportedTypes();
            if (set != null) {
                this.supportedTypes.addAll(Activatable.from(set));
            }
        }
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
        return getSupportedTypeAsString((Set<AbstractSupportedStringType>)
                typesFor(FeatureType.class));
    }

    @SuppressWarnings("unchecked")
    public Set<ObservationType> getObservationTypes() {
        return (Set<ObservationType>) typesFor(ObservationType.class);
    }
    @SuppressWarnings("unchecked")
    public Set<String> getObservationTypesAsString() {
        return getSupportedTypeAsString((Set<AbstractSupportedStringType>)
                typesFor(ObservationType.class));
    }

    private Set<String> getSupportedTypeAsString(
            Set<? extends AbstractSupportedStringType> types) {
        Set<String> strings = Sets.newHashSetWithExpectedSize(types.size());
        for (AbstractSupportedStringType type : types) {
            strings.add(type.getValue());
        }
        return strings;
    }

    @Deprecated
    public static SupportedTypeRepository getInstance() {
        return instance;
    }

    private class CacheLoaderImpl extends CacheLoader<Class<? extends SupportedType>, Set<Activatable<SupportedType>>> {
        @Override
        public Set<Activatable<SupportedType>> load( Class<? extends SupportedType> key){
            Set<Activatable<SupportedType>> set = Sets.newHashSet();
            for (Activatable<SupportedType> activatable : supportedTypes) {
                if (activatable.getInternal().getClass().isAssignableFrom(key)) {
                    set.add(activatable);
                }
            }
            return set;
        }
    }
}
