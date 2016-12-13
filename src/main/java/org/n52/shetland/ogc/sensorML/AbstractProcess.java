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
import org.n52.shetland.ogc.gml.time.Time;
import org.n52.shetland.ogc.gml.time.TimePeriod;
import org.n52.shetland.ogc.sensorML.elements.SmlIo;

/**
 * @since 4.0.0
 *
 */
public class AbstractProcess extends AbstractSensorML {

    private List<SmlIo> inputs = new ArrayList<>(0);
    private List<SmlIo> outputs = new ArrayList<>(0);
    private List<String> parameters = new ArrayList<>(0);
    private List<Time> validTime = new ArrayList<>(0);


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

    public List<SmlIo> getInputs() {
        return inputs;
    }

    public AbstractProcess setInputs(final List<SmlIo> inputs) {
        this.inputs = inputs;
        return this;
    }

    public List<SmlIo> getOutputs() {
        return outputs;
    }

    public AbstractProcess setOutputs(final List<SmlIo> outputs) {
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

    public boolean isSetInputs() {
        return inputs != null && !inputs.isEmpty();
    }

    public boolean isSetOutputs() {
        return outputs != null && !outputs.isEmpty();
    }

    public boolean isSetParameters() {
        return parameters != null && !parameters.isEmpty();
    }

    public List<Time> getValidTime() {
        return validTime;
    }

    public Time getMergedValidTime() {
        TimePeriod tp = new TimePeriod();
        for (Time time : getValidTime()) {
            tp.extendToContain(time);
        }
        return tp;
    }

    public AbstractProcess setValidTime(Time validTime) {
        this.validTime.clear();
        if (validTime != null) {
            this.validTime.add(validTime);
        }
        return this;
    }

    public AbstractProcess setValidTime(List<Time> validTime) {
        this.validTime.clear();
        if (validTime != null) {
            this.validTime.addAll(validTime);
        }
        return this;
    }

    public boolean isSetValidTime() {
        return this.validTime != null && !this.validTime.isEmpty();
    }

    @Override
    public AbstractProcess addName(final CodeType name) {
        super.addName(name);
        return this;
    }

    public void copyTo(AbstractProcess copyOf) {
        super.copyTo(copyOf);
        copyOf.setInputs(getInputs());
        copyOf.setOutputs(getOutputs());
        copyOf.setParameters(getParameters());
    }


}
