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
package org.n52.shetland.ogc.sensorML;

/**
 * Implementation for sml:RulesDefinition
 *
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk
 *         J&uuml;rrens</a>
 *
 * @since 1.0.0
 */
public class RulesDefinition {

    private String description;

    public RulesDefinition() {
    }

    public RulesDefinition(final String description) {
        this.description = description;
    }

    /**
     * @return <tt>true</tt>, if description is set and not empty.
     */
    public boolean isSetDescription() {
        return description != null && !description.isEmpty();
    }

    /**
     *
     * @param description
     *            a {@link String} holding the
     *            "[t]extual description of the i/o structure" (Source: SensorML
     *            1.0.1).
     * @return the {@link RulesDefinition} object instance
     */
    public RulesDefinition setDescription(final String description) {
        this.description = description;
        return this;
    }

    /**
     *
     * @return Textual description of the i/o structure (Source: SensorML 1.0.1)
     */
    public String getDescription() {
        return description;
    }

}
