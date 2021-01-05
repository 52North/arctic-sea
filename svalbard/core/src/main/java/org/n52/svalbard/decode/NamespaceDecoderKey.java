/*
 * Copyright 2015-2021 52°North Initiative for Geospatial Open Source
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
package org.n52.svalbard.decode;

import java.util.Objects;

/**
 * Abstract {@link DecoderKey} class for namespace decoder.
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 *
 * @since 1.0.0
 */
public abstract class NamespaceDecoderKey<T> implements DecoderKey {
    private final String namespace;

    public NamespaceDecoderKey(String namespace, T type) {
        this.namespace = namespace;
        setType(type);
    }

    /**
     * Set the type.
     *
     * @param type
     *            the type
     */
    protected abstract void setType(T type);

    /**
     * Get the type.
     *
     * @return the type
     */
    public abstract T getType();

    /**
     * Get {@link String} representation of the type.
     *
     * @return {@link String} representation of the type.
     */
    protected abstract String getTypeName();

    /**
     * Check for similarity
     *
     * @param key
     *            {@link DecoderKey} to check
     * @param type
     *            Type to check
     *
     * @return 0 for equality, -1 for non equality
     */
    protected abstract int getSimilarity(DecoderKey key, T type);

    public String getNamespace() {
        return namespace;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNamespace(), getType());
    }

    @Override
    public String toString() {
        return String.format("%s[namespace=%s, type=%s]", getClass().getName(), getNamespace(), getTypeName());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && getClass() == obj.getClass()) {
            final NamespaceDecoderKey<?> o = (NamespaceDecoderKey<?>) obj;
            return Objects.equals(getType(), o.getType()) && Objects.equals(getNamespace(), o.getNamespace());
        }
        return false;
    }
}
