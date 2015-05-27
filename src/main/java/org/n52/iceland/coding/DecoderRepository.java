package org.n52.iceland.coding;

import java.util.List;
import java.util.Set;

import org.n52.iceland.decode.Decoder;
import org.n52.iceland.decode.DecoderFactory;
import org.n52.iceland.decode.DecoderKey;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class DecoderRepository
        extends AbstractCodingRepository<DecoderKey, Decoder<?, ?>, DecoderFactory> {

    public DecoderRepository() {
        super(Decoder.class, DecoderFactory.class);
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
