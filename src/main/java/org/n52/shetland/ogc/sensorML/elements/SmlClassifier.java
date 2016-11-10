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
package org.n52.shetland.ogc.sensorML.elements;

import org.n52.shetland.ogc.sensorML.Term;

/**
 * SOS internal representation of SensorML classifier
 *
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk
 *         J&uuml;rrens</a>
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 *
 * @since 4.0.0
 */
public class SmlClassifier extends Term {

    public static final String PROCEDURE_TYPE = "procedureType";

    public static final String INTENDED_APPLICATION = "intendedApplication";

    /**
     * constructor
     */
    public SmlClassifier() {

    }

    /**
     * constructor
     *
     * @param name
     *            Classifier name
     * @param definition
     *            Classifier definition (OPTIONAL)
     * @param codeSpace
     *            Classifier codeSpace (OPTIONAL)
     * @param value
     *            Classifier value
     */
    public SmlClassifier(final String name, final String definition, final String codeSpace,
            final String value) {
        super();
        setName(name);
        setCodeSpace(codeSpace);
        setDefinition(definition);
        setValue(value);

    }
}
