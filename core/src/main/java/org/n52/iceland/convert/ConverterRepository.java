/*
 * Copyright 2015-2017 52°North Initiative for Geospatial Open Source
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
package org.n52.iceland.convert;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import org.n52.janmayen.Producer;
import org.n52.janmayen.component.AbstractComponentRepository;
import org.n52.janmayen.lifecycle.Constructable;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Sets;

/**
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
@SuppressWarnings("rawtypes")
public class ConverterRepository extends AbstractComponentRepository<ConverterKey, Converter<?, ?>, ConverterFactory> implements Constructable {
    @Deprecated
    private static ConverterRepository instance;

    private Collection<Converter<?, ?>> components;
    private Collection<ConverterFactory> componentFactories;

    private final Map<ConverterKey, Producer<Converter<?, ?>>> converter
            = new HashMap<>(0);

    @Autowired(required = false)
    public void setComponentFactories(Collection<ConverterFactory> componentFactories) {
        this.componentFactories = componentFactories;
    }

    @Autowired(required = false)
    public void setComponents(Collection<Converter<?, ?>> components) {
        this.components = components;
    }

    @Override
    public void init() {
        ConverterRepository.instance = this;
        // TODO check for encoder/decoder used by converter
        Map<ConverterKey, Producer<Converter<?, ?>>> implementations
                = getUniqueProviders(this.components, this.componentFactories);
        this.converter.clear();
        this.converter.putAll(implementations);
    }


    public <T, F> Converter<T, F> getConverter(final String fromNamespace, final String toNamespace) {
        return getConverter(new ConverterKey(fromNamespace, toNamespace));
    }

    @SuppressWarnings("unchecked")
    public <T, F> Converter<T, F> getConverter(final ConverterKey key) {
        Supplier<Converter<?, ?>> producer = converter.get(key);
        if (producer == null) {
            return null;
        }
        return (Converter<T, F>) producer.get();
    }

    /**
     * Get all namespaces for which a converter is available to convert from
     * requested format to default format
     *
     * @param toNamespace
     *                    Requested format
     *
     * @return Swt with all possible formats
     */
    public Set<String> getFromNamespaceConverterTo(final String toNamespace) {
        final Set<String> fromNamespaces = Sets.newHashSet();
        for (final ConverterKey converterKey : converter.keySet()) {
            if (toNamespace.equals(converterKey.getToNamespace())) {
                fromNamespaces.add(converterKey.getFromNamespace());
            }
        }
        return fromNamespaces;
    }

    /**
     * Checks if a converter is available to convert the stored object from the
     * default format to the requested format
     *
     * @param fromNamespace
     *                      Default format
     * @param toNamespace
     *                      Requested fromat
     *
     * @return If a converter is available
     */
    public boolean hasConverter(final String fromNamespace, final String toNamespace) {
        return hasConverter(new ConverterKey(fromNamespace, toNamespace));
    }

    public boolean hasConverter(ConverterKey key) {
        return this.converter.containsKey(key);
    }

    @Deprecated
    public static ConverterRepository getInstance() {
        return ConverterRepository.instance;
    }
}
