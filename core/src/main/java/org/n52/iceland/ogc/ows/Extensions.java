/*
 * Copyright 2015-2016 52°North Initiative for Geospatial Open Source
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
package org.n52.iceland.ogc.ows;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.n52.iceland.util.StringHelper;

public class Extensions {

    private final Set<Extension<?>> extensions = new HashSet<>();

    public Extensions() {

    }

    /**
     * @param extensionName
     *
     * @return <b><tt>true</tt></b>, only if the extension with the definition
     *         <tt>extensionName</tt> is holding a {@link Boolean} and is set to
     *         <tt>true</tt>.
     */
    @SuppressWarnings("rawtypes")
    public boolean isBooleanExtensionSet(final String extensionName) {
        for (final Extension<?> extension : getExtensions()) {
            if (isExtensionNameEquals(extensionName, (Extension<?>)extension)) {
                final Object value = extension.getValue();
                if (value instanceof Boolean) {
                    return (Boolean)value;
                } else if (value instanceof Value && ((Value)value).getValue() instanceof Boolean) {
                    return (Boolean) ((Value)value).getValue();
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
       return getExtensions().addAll(extensions);
    }

    public boolean addExtension(Extension<?> extensions) {
        return getExtensions().add(extensions);
    }

    public Set<Extension<?>> getExtensions() {
        return extensions;
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
    public Extension<?> getExtension(Enum identifier) {
        return getExtension(identifier.name());
    }

    public Extension<?> getExtension(String identifier) {
        return this.extensions.stream()
                .filter(e -> isExtensionNameEquals(identifier, e))
                .findFirst().orElse(null);
    }

    public boolean isEmpty() {
        return getExtensions().isEmpty();
    }

    @Override
    public String toString() {
        return String.format("Extensions [extensions=%s]", getExtensions());
    }

    protected boolean isExtensionNameEquals(final String extensionName, final Extension<?> extension) {
        return checkExtensionDefinition(extensionName, extension)
                || checkExtensionIdentifier(extensionName, extension)
                || checkExtensionValue(extensionName, extension);
    }

    private boolean checkExtensionValue(String extensionName, Extension<?> extension) {
            return (extension.isSetDefinition() && extension.getDefinition()
                    .equalsIgnoreCase(extensionName))
                    || (extension.isSetIdentifier() && extension.getIdentifier()
                            .equalsIgnoreCase(extensionName));
    }

    private boolean checkExtensionIdentifier(String extensionName, Extension<?> extension) {
        if (StringHelper.isNotEmpty(extensionName)) {
            return extension.isSetIdentifier() && extension.getIdentifier().equalsIgnoreCase(extensionName);
        }
        return false;
    }

    private boolean checkExtensionDefinition(String extensionName, Extension<?> extension) {
        if (extensionName != null && extension != null) {
            return extension.isSetDefinition() && extension.getDefinition().equalsIgnoreCase(extensionName);
        }
        return false;
    }

}
