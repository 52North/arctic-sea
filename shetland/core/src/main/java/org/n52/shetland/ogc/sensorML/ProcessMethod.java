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
package org.n52.shetland.ogc.sensorML;

import org.n52.shetland.ogc.gml.AbstractReferenceType;
import org.n52.shetland.ogc.sensorML.v20.AbstractAlgorithm;

import com.google.common.base.Strings;

/**
 * Implementation for sml:ProcessMethod
 *
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk
 *         J&uuml;rrens</a>
 *
 * @since 1.0.0
 */
public class ProcessMethod extends AbstractReferenceType {

    private RulesDefinition rulesDefinition;

    private AbstractAlgorithm algorithm;

    public ProcessMethod(final String href) {
        if (Strings.isNullOrEmpty(href)) {
            throw new IllegalArgumentException("attribute 'xlink:href' is MANDATORY");
        }
        setHref(href);
    }

    public ProcessMethod(final RulesDefinition rulesDefinition) {
        if (rulesDefinition == null) {
            throw new IllegalArgumentException("parameter 'rulesDefinition' is MANDATORY");
        }
        this.rulesDefinition = rulesDefinition;
    }

    /**
     * Text and/or language defining rules for process profile (e.g. inputs,
     * outputs, parameters, and metadata) (Source: SensorML 1.0.1)
     *
     * @return SOS rules definition
     */
    public RulesDefinition getRulesDefinition() {
        return rulesDefinition;
    }

    public boolean isSetRulesDefinition() {
        return getRulesDefinition() != null;
    }

    public AbstractAlgorithm getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(AbstractAlgorithm algorithm) {
        this.algorithm = algorithm;
    }
}
