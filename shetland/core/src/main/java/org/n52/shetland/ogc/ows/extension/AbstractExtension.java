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
package org.n52.shetland.ogc.ows.extension;

public abstract class AbstractExtension<T>
        implements Extension<T> {

    private String namespace;
    private String identifier;
    private String definition;

    @Override
    public Extension<T> setNamespace(String namespace) {
        this.namespace = namespace;
        return this;
    }

    @Override
    public String getNamespace() {
        return namespace;
    }

    @Override
    public Extension<T> setIdentifier(String identifier) {
        this.identifier = identifier;
        return this;
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public Extension<T> setDefinition(String definition) {
        this.definition = definition;
        return this;
    }

    @Override
    public String getDefinition() {
        return definition;
    }
}
