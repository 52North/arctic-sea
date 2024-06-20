/*
 * Copyright 2015-2020 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.ogc.swe;

/**
 * @author <a href="mailto:j.speckamp@52north.org">Jan Speckamp</a>
 */
public class AbstractSWEIdentifiable {

    /**
     * optional: swe:description[0..1]
     */
    private String description;

    /**
     * optional: swe:label [0..1]
     */
    private String label;
    /**
     * optional: swe:identifier [0..1]
     */
    private String identifier;

    public boolean isSetDescription() {
        return description != null && !description.isEmpty();
    }

    public String getDescription() {
        return description;
    }

    public AbstractSWEIdentifiable setDescription(final String description) {
        this.description = description;
        return this;
    }

    public boolean isSetLabel() {
        return label != null && !label.isEmpty();
    }

    public String getLabel() {
        if (label != null && !label.isEmpty()) {
            return label;
        }
        return null;
    }

    public AbstractSWEIdentifiable setLabel(final String label) {
        this.label = label;
        return this;
    }

    public boolean isSetIdentifier() {
        return identifier != null && !identifier.isEmpty();
    }

    public String getIdentifier() {
        return identifier;
    }

    public AbstractSWEIdentifiable setIdentifier(final String identifier) {
        this.identifier = identifier;
        return this;
    }
}
