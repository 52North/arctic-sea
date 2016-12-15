/*
 * Copyright 2016 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.ogc.ows.extension;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import com.google.common.base.Strings;

public class Extensions {

    private final Set<Extension<?>> extensions = new HashSet<>();
    /**
     * @param extensionName
     *
     * @return <b><tt>true</tt></b>, only if the extension with the definition
     * <tt>extensionName</tt> is holding a {@link Boolean} and is set to
     * <tt>true</tt>.
     */
    @SuppressWarnings("rawtypes")
    public boolean isBooleanExtensionSet(final String extensionName) {
        for (final Extension<?> extension : getExtensions()) {
            if (isExtensionNameEquals(extensionName, (Extension<?>) extension)) {
                final Object value = extension.getValue();
                if (value instanceof Boolean) {
                    return (Boolean) value;
                } else if (value instanceof Value && ((Value) value).getValue() instanceof Boolean) {
                    return (Boolean) ((Value) value).getValue();
                }
                return false;
            }
        }
        return false;
    }

    public boolean addExtension(Extensions extensions) {
        return addExtension(extensions.getExtensions());
    }

    public boolean addExtension(Collection<Extension<?>> extensions) {
        return extensions.stream()
                .map(this::addExtension)
                .reduce(false, (ret, changed) -> ret || changed);
    }

    public boolean addExtension(Extension<?> extensions) {
        return this.extensions.add(Objects.requireNonNull(extensions));
    }

    public Set<Extension<?>> getExtensions() {
        return Collections.unmodifiableSet(this.extensions);
    }

    public Stream<Extension<?>> stream() {
        return this.extensions.stream();
    }

    @SuppressWarnings("rawtypes")
    public boolean containsExtension(Enum identifier) {
        return containsExtension(identifier.name());
    }

    public boolean containsExtension(String identifier) {
        return this.extensions.stream()
                .filter(e -> isExtensionNameEquals(identifier, e))
                .findAny().isPresent();
    }

    @SuppressWarnings("rawtypes")
    public Optional<Extension<?>> getExtension(Enum identifier) {
        return getExtension(identifier.name());
    }

    public Optional<Extension<?>> getExtension(String identifier) {
        return this.extensions.stream()
                .filter(e -> isExtensionNameEquals(identifier, e))
                .findFirst();
    }

    public boolean isEmpty() {
        return getExtensions().isEmpty();
    }

    @Override
    public String toString() {
        return String.format("Extensions [extensions=%s]", getExtensions());
    }

    protected boolean isExtensionNameEquals(String extensionName, Extension<?> extension) {
        return checkExtensionDefinition(extensionName, extension) ||
               checkExtensionIdentifier(extensionName, extension) ||
               checkExtensionValue(extensionName, extension);
    }

    private boolean checkExtensionValue(String extensionName, Extension<?> extension) {
        return (extension.isSetDefinition() && extension.getDefinition().equalsIgnoreCase(extensionName)) ||
               (extension.isSetIdentifier() && extension.getIdentifier().equalsIgnoreCase(extensionName));
    }

    private boolean checkExtensionIdentifier(String extensionName, Extension<?> extension) {
        return Strings.emptyToNull(extensionName) != null && extension.isSetIdentifier() && extension.getIdentifier().equalsIgnoreCase(extensionName);
    }

    private boolean checkExtensionDefinition(String extensionName, Extension<?> extension) {
        return extensionName != null && extension != null && extension.isSetDefinition() && extension.getDefinition().equalsIgnoreCase(extensionName);
    }

}
