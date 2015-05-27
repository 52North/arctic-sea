package org.n52.iceland.coding;

import java.util.List;
import java.util.Set;

import org.n52.iceland.encode.Encoder;
import org.n52.iceland.encode.EncoderFactory;
import org.n52.iceland.encode.EncoderKey;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class EncoderRepository extends AbstractCodingRepository<EncoderKey, Encoder<?, ?>, EncoderFactory> {

    public EncoderRepository() {
        super(Encoder.class, EncoderFactory.class);
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
