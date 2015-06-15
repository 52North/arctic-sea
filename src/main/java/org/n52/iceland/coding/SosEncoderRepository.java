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

import org.n52.iceland.coding.encode.Encoder;
import org.n52.iceland.coding.encode.ObservationEncoder;
import org.n52.iceland.util.Producer;
import org.n52.iceland.util.Producers;

import com.google.common.collect.Sets;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class SosEncoderRepository extends EncoderRepository {
    //TODO move to SOS
    private final Set<Producer<ObservationEncoder<?, ?>>> observationEncoders
            = Sets.newHashSet();

    @Override
    public void init() {
        super.init();
        this.observationEncoders.clear();
        for (Producer<Encoder<?, ?>> producer : getComponentProviders()) {
            Encoder<?, ?> encoder = producer.get();
            if (encoder instanceof ObservationEncoder) {
                this.observationEncoders.add(asObservationEncoderProducer(producer));
            }
        }
    }

    public Set<ObservationEncoder<?, ?>> getObservationEncoders() {
        return Producers.produce(this.observationEncoders);
    }

    @SuppressWarnings("unchecked")
    private static Producer<ObservationEncoder<?, ?>> asObservationEncoderProducer(
            Producer<? extends Encoder<?, ?>> producer) {
        return (Producer<ObservationEncoder<?, ?>>) producer;
    }

}
