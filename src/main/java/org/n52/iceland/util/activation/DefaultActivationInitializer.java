package org.n52.iceland.util.activation;

public class DefaultActivationInitializer<K> implements ActivationInitializer<K> {

    private ActivationProvider<K> provider;

    public DefaultActivationInitializer(ActivationProvider<K> provider) {
        this.provider = provider;
    }

    public DefaultActivationInitializer() {
        this(null);
    }

    public void setProvider(ActivationProvider<K> provider) {
        this.provider = provider;
    }

    @Override
    public void initialize(ActivationManager<K> manager) {
        for (K key : this.provider.getAllKeys()) {
            if (this.provider.isActive(key)) {
                manager.activate(key);
            } else {
                manager.deactivate(key);
            }
        }
    }

}
