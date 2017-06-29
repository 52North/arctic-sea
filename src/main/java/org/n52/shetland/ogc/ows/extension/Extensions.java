/*
 * Copyright 2016-2017 52°North Initiative for Geospatial Open Source
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
import java.util.function.Function;
import java.util.stream.Stream;

import com.google.common.base.Strings;

public class Extensions {

    private final Set<Extension<?>> extensions = new HashSet<>();

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
                .filter(e -> checkExtensionName(identifier, e))
                .findAny().isPresent();
    }

    @SuppressWarnings("rawtypes")
    public Optional<Extension<?>> getExtension(Enum identifier) {
        return getExtension(identifier.name());
    }

    public Optional<Extension<?>> getExtension(String identifier) {
        return this.extensions.stream()
                .filter(e -> checkExtensionName(identifier, e))
                .findFirst();
    }

    public boolean isEmpty() {
        return getExtensions().isEmpty();
    }

    @Override
    public String toString() {
        return String.format("Extensions [extensions=%s]", getExtensions());
    }

    public boolean getBooleanExtension(String identifier) {
        return getBooleanExtension(identifier, false);
    }

    public boolean getBooleanExtension(Enum<?> identifier) {
        return getBooleanExtension(identifier.name(), false);
    }

    public boolean getBooleanExtension(Enum<?> identifier, boolean defaultValue) {
        return getBooleanExtension(identifier.name(), defaultValue);
    }

    public boolean getBooleanExtension(String identifier, boolean defaultValue) {
        return getExtension(identifier).map(e -> e.getValue()).map(value -> {
            if (value instanceof Boolean) {
                return (Boolean) value;
            } else if (value instanceof Value && ((Value) value).getValue() instanceof Boolean) {
                return (Boolean) ((Value) value).getValue();
            }
            return false;
        }).orElse(defaultValue);
    }

    private boolean check(String name, Extension<?> extension, Function<Extension<?>, String> extractor) {
        if (Strings.emptyToNull(name) == null || extension == null) {
            return false;
        }
        return Optional.ofNullable(extractor.apply(extension)).map(s -> s.equalsIgnoreCase(name)).orElseGet(() -> false);
    }

    private boolean checkExtensionName(String extensionName, Extension<?> extension) {
        return checkExtensionIdentifier(extensionName, extension) || checkExtensionDefinition(extensionName, extension);
    }

    private boolean checkExtensionIdentifier(String extensionName, Extension<?> extension) {
        return check(extensionName, extension, Extension::getIdentifier);
    }

    private boolean checkExtensionDefinition(String extensionName, Extension<?> extension) {
        return check(extensionName, extension, Extension::getDefinition);
    }

}
