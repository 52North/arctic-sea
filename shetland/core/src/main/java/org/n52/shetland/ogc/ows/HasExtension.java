/*
 * Copyright 2015-2022 52°North Spatial Information Research GmbH
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
package org.n52.shetland.ogc.ows;

import java.util.Optional;
import java.util.Set;

import org.n52.shetland.ogc.ows.extension.Extension;
import org.n52.shetland.ogc.ows.extension.Extensions;
import org.n52.shetland.ogc.swe.SweAbstractDataComponent;
import org.n52.shetland.ogc.swe.simpleType.SweBoolean;
import org.n52.shetland.ogc.swe.simpleType.SweText;
import org.n52.shetland.ogc.swes.SwesExtension;

/**
 * Interface to identify if the implemented class supportes {@link Extensions}
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 * @param <T> Extension type
 */
public interface HasExtension<T extends HasExtension<? extends T>> {
    /**
     * Get the {@link Extension}s
     *
     * @return {@link Extensions} with {@link Extension}s
     */
    Extensions getExtensions();

    /**
     * Set the {@link Extensions} object
     *
     * @param extensions the {@link Extensions} object to set
     *
     * @return this
     */
    T setExtensions(Extensions extensions);

    /**
     * Add a {@link Extensions} to this object
     *
     * @param extensions the {@link Extensions} to add
     *
     * @return this
     */
    @SuppressWarnings("unchecked")
    default T addExtensions(Extensions extensions) {
        getExtensions().addExtension(extensions.getExtensions());
        return (T) this;
    }

    /**
     * Add a {@link Extension} to this object
     *
     * @param extension the {@link Extension} to add
     *
     * @return this
     */
    @SuppressWarnings("unchecked")
    default T addExtension(Extension<?> extension) {
        getExtensions().addExtension(extension);
        return (T) this;
    }

    /**
     * Add a {@link Extension}s to this object
     *
     * @param extensions the {@link Extension}s to add
     *
     * @return this
     */
    @SuppressWarnings("unchecked")
    default T addExtension(Set<Extension<?>> extensions) {
        getExtensions().addExtension(extensions);
        return (T) this;
    }

    default boolean hasExtensions() {
        return getExtensions() != null && !getExtensions().isEmpty();
    }

    /**
     * Check if {@link Extension} for identifier is set
     *
     * @param identifier Identifier to check
     *
     * @return <code>true</code>, if {@link Extensions} is available for the
     *         identifier
     */
    default boolean hasExtension(Enum<?> identifier) {
        return getExtensions().containsExtension(identifier);
    }

    /**
     * Check if {@link Extension} for identifier is set
     *
     * @param identifier Identifier to check
     *
     * @return <code>true</code>, if {@link Extensions} is available for the
     *         identifier
     */
    default boolean hasExtension(String identifier) {
        return getExtensions().containsExtension(identifier);
    }

    /**
     * Get {@link Extension} for identifier
     *
     * @param identifier Identifier to get {@link Extension} for
     *
     * @return The requested {@link Extension}
     *
     */
    default Optional<Extension<?>> getExtension(Enum<?> identifier) {
        return getExtensions().getExtension(identifier);
    }

    /**
     * Get {@link Extension} for identifier
     *
     * @param identifier Identifier to get {@link Extension} for
     *
     * @return The requested {@link Extension}
     *
     */
    default Optional<Extension<?>> getExtension(String identifier) {
        return getExtensions().getExtension(identifier);
    }


    default int getExtensionCount(String identifier) {
        return getExtensions().countExtensions(identifier);
    }

    default boolean getBooleanExtension(String identifier) {
        return getExtensions().getBooleanExtension(identifier);
    }

    default boolean getBooleanExtension(Enum<?> identifier) {
        return getExtensions().getBooleanExtension(identifier);
    }

    default boolean getBooleanExtension(Enum<?> identifier, boolean defaultValue) {
        return getExtensions().getBooleanExtension(identifier, defaultValue);
    }

    default boolean getBooleanExtension(String identifier, boolean defaultValue) {
        return getExtensions().getBooleanExtension(identifier, defaultValue);
    }

    default <V extends SweAbstractDataComponent> void addSwesExtension(String name, V value) {
        SwesExtension<V> extension = new SwesExtension<V>();
        extension.setIdentifier(name);
        extension.setValue(value);
        addExtension(extension);
    }

    default void addSweTextExtension(String name, String value) {
        SweText sweText = new SweText();
        sweText.setValue(value);
        sweText.setIdentifier(name);
        addSwesExtension(name, sweText);
    }

    default void addSweBooleanExtension(String name, String value) {
        addSweBooleanExtension(name, Boolean.parseBoolean(value));
    }

    default void addSweBooleanExtension(String name, boolean value) {
        SweBoolean sweBoolean = new SweBoolean();
        sweBoolean.setValue(value);
        sweBoolean.setIdentifier(name);
        addSwesExtension(name, sweBoolean);
    }
}
