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
package org.n52.shetland.ogc.sensorML.elements;

import org.n52.shetland.ogc.gml.AbstractReferenceType;
import org.n52.shetland.ogc.sensorML.AbstractSensorML;

/**
 * SOS internal representation of SensorML component
 *
 * @since 4.0.0
 */
public class SmlComponent extends AbstractReferenceType {

    private String name;

    private AbstractSensorML process;

    public SmlComponent() {

    }

    /**
     * constructor
     *
     * @param name
     *            Component identifier
     */
    public SmlComponent(String name) {
        super();
        this.name = name;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            The name
     */
    public void setName(String name) {
        this.name = name;
    }

    public void setProcess(AbstractSensorML process) {
        this.process = process;
    }

    public AbstractSensorML getProcess() {
        return process;
    }

    public boolean isSetProcess() {
        return process != null;
    }

    public boolean isSetName() {
        return name != null && !name.isEmpty();
    }

    public boolean isReferencedExternally() {
        return isSetTitle() || isSetHref();
    }

}
