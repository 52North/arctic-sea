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
package org.n52.shetland.ogc.sensorML.elements;

import org.n52.shetland.ogc.sensorML.Term;

/**
 * SOS internal representation of SensorML identifier
 *
 * @since 1.0.0
 */
public class SmlIdentifier
        extends Term {

    /**
     * constructor
     */
    public SmlIdentifier() {

    }

    /**
     * constructor
     *
     * @param name
     *            Identifier name
     * @param definition
     *            Identifier definition
     * @param value
     *            Identifier value
     */
    public SmlIdentifier(final String name, final String definition, final String value) {
        super();
        setName(name);
        setLabel(name);
        setDefinition(definition);
        setValue(value);
    }
}
