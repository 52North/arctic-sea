/*
 * Copyright (C) 2015-2022 52°North Spatial Information Research GmbH
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

public interface Extension<T> {

    String getNamespace();

    Extension<T> setNamespace(String namespace);

    default boolean isSetNamespace() {
        return getNamespace() != null && !getNamespace().isEmpty();
    }

    String getIdentifier();

    Extension<T> setIdentifier(String identifier);

    default boolean isSetIdentifier() {
        return getIdentifier() != null && !getIdentifier().isEmpty();
    }

    String getDefinition();

    Extension<T> setDefinition(String definition);

    default boolean isSetDefinition() {
        return getDefinition() != null && !getDefinition().isEmpty();
    }

    T getValue();

    Extension<T> setValue(T value);
}
