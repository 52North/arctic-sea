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
package org.n52.shetland.ogc.sensorML;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.n52.shetland.ogc.gml.CodeType;
import org.n52.shetland.util.CollectionHelper;
import org.n52.shetland.ogc.sensorML.elements.SmlComponent;
import org.n52.shetland.ogc.sensorML.elements.SmlIo;

/**
 * @since 4.0.0
 *
 */
public class AbstractProcess extends AbstractSensorML {

    private static final long serialVersionUID = 90768395878987095L;

    private List<SmlIo<?>> inputs = new ArrayList<>(0);
    private List<SmlIo<?>> outputs = new ArrayList<>(0);
    private List<String> parameters = new ArrayList<>(0);


    public AbstractProcess setDescriptions(List<String> descriptions) {
        if (descriptions != null) {
            if (descriptions.size() == 1) {
                setDescription(descriptions.iterator().next());
            } else {
                setDescription(Arrays.toString(descriptions.toArray(new String[descriptions.size()])));
            }
        }
        return this;
    }

    public AbstractProcess addDescription(final String description) {
        if (isSetDescription()) {
            setDescription(new StringBuilder(getDescription()).append(", ")
                    .append(description).toString());
        } else {
            setDescription(description);
        }

        return this;
    }

    public List<CodeType> getNames() {
        return getName();
    }

    public AbstractProcess setNames(final List<CodeType> names) {
        setName(names);
        return this;
    }

    public List<SmlIo<?>> getInputs() {
        return inputs;
    }

    public AbstractProcess setInputs(final List<SmlIo<?>> inputs) {
        this.inputs = inputs;
        return this;
    }

    public List<SmlIo<?>> getOutputs() {
        return outputs;
    }

    public AbstractProcess setOutputs(final List<SmlIo<?>> outputs) {
        this.outputs = outputs;
        return this;
    }

    public List<String> getParameters() {
        return parameters;
    }

    public AbstractProcess setParameters(final List<String> parameters) {
        this.parameters = parameters;
        return this;
    }

    public boolean isSetName() {
        return CollectionHelper.isNotEmpty(getName());
    }

    public boolean isSetInputs() {
        return inputs != null && !inputs.isEmpty();
    }

    public boolean isSetOutputs() {
        return outputs != null && !outputs.isEmpty();
    }

    public boolean isSetParameters() {
        return parameters != null && !parameters.isEmpty();
    }

    public AbstractProcess addName(final CodeType name) {
        super.addName(name);
        return this;
    }

    protected void checkAndSetChildProcedures(final List<SmlComponent> components) {
        if (components != null) {
            for (final SmlComponent component : components) {
                checkAndSetChildProcedures(component);
            }
        }
    }

    protected void checkAndSetChildProcedures(final SmlComponent component) {
        if (component != null && component.isSetName()
                && component.getName().contains(SensorMLConstants.ELEMENT_NAME_CHILD_PROCEDURES)) {
            addChildProcedure(component.getProcess());
        }
    }

    public void copyTo(AbstractProcess copyOf) {
        super.copyTo(copyOf);
        copyOf.setInputs(getInputs());
        copyOf.setOutputs(getOutputs());
        copyOf.setParameters(getParameters());
    }


}
