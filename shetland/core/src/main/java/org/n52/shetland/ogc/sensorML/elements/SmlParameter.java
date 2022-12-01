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
package org.n52.shetland.ogc.sensorML.elements;

import org.n52.shetland.ogc.gml.AbstractReferenceType;
import org.n52.shetland.ogc.swe.SweAbstractDataComponent;

/**
 * @author <a href="mailto:m.radtke@52north.org">Maurin Radtke</a>
 */
public class SmlParameter extends AbstractReferenceType {

    private String name;
    private SweAbstractDataComponent parameter;

    public SmlParameter() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SweAbstractDataComponent getParameter() {
        return parameter;
    }

    public void setParameter(SweAbstractDataComponent parameters) {
        parameter = parameters;
    }
}