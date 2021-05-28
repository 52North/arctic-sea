/*
 * Copyright (C) 2015-2021 52°North Spatial Information Research GmbH
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
package org.n52.shetland.ogc.sensorML.v20;

import org.n52.shetland.ogc.gml.ReferenceType;
import org.n52.shetland.ogc.sensorML.AbstractProcess;
import org.n52.shetland.ogc.sensorML.SensorML20Constants;

/**
 * Class that represents SensorML 2.0 AbstractProcess
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public class AbstractProcessV20 extends AbstractProcess {

    private AbstractSettings configuration;
    private SmlFeatureOfInterest featureOfInterest;
    private AbstractModes modes;
    private String definition;

    private ReferenceType typeOf;

    public AbstractProcessV20 setSmlFeatureOfInterest(SmlFeatureOfInterest featureOfInterest) {
        this.featureOfInterest = featureOfInterest;
        return this;
    }

    public SmlFeatureOfInterest getSmlFeatureOfInterest() {
        return featureOfInterest;
    }

    public boolean isSetSmlFeatureOfInterest() {
        return featureOfInterest != null && featureOfInterest.isSetFeatures();
    }

    /**
     * @return the configuration
     */
    public AbstractSettings getConfiguration() {
        return configuration;
    }

    /**
     * @param configuration the configuration to set
     */
    public void setConfiguration(AbstractSettings configuration) {
        this.configuration = configuration;
    }

    /**
     * @return the modes
     */
    public AbstractModes getModes() {
        return modes;
    }

    /**
     * @param modes the modes to set
     */
    public void setModes(AbstractModes modes) {
        this.modes = modes;
    }

    /**
     * @return the definition
     */
    public String getDefinition() {
        return definition;
    }

    /**
     * @param definition the definition to set
     */
    public void setDefinition(String definition) {
        this.definition = definition;
    }

    /**
     * @return the typeOf
     */
    public ReferenceType getTypeOf() {
        return typeOf;
    }

    /**
     * @param typeOf the typeOf to set
     */
    public void setTypeOf(ReferenceType typeOf) {
        this.typeOf = typeOf;
    }

    /**
     * @return <code>true</code>, if typeOf is not null
     */
    public boolean isSetTypeOf() {
        return getTypeOf() != null;
    }

    @Override
    public String getDefaultElementEncoding() {
        return SensorML20Constants.NS_SML_20;
    }
}
