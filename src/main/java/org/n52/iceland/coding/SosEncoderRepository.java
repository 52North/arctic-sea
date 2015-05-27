package org.n52.iceland.coding;

import java.util.Set;

import org.n52.iceland.encode.Encoder;
import org.n52.iceland.encode.EncoderKey;
import org.n52.iceland.encode.ObservationEncoder;
import org.n52.iceland.util.Producer;
import org.n52.iceland.util.Producers;

import com.google.common.collect.SetMultimap;
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
    protected void processImplementations(
            SetMultimap<EncoderKey, Producer<Encoder<?, ?>>> implementations) {
        super.processImplementations(implementations);

        this.observationEncoders.clear();
        for (Producer<Encoder<?, ?>> producer : implementations.values()) {
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
