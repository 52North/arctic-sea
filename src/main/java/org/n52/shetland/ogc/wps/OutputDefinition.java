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
package org.n52.shetland.ogc.wps;

import static java.util.stream.Collectors.toMap;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import org.n52.shetland.ogc.ows.OwsCode;

import com.google.common.base.MoreObjects;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class OutputDefinition {

    private DataTransmissionMode dataTransmissionMode;
    private OwsCode id;
    private Format format;
    private List<OutputDefinition> outputs;

    public OutputDefinition(OwsCode id, Format format,
                            List<OutputDefinition> outputs) {
        this(id, format, null, outputs);
    }

    public OutputDefinition(OwsCode id, Format format) {
        this(id, format, null, null);
    }

    public OutputDefinition(OwsCode id) {
        this(id, null, null, null);
    }

    public OutputDefinition() {
        this(null, null, null, null);
    }

    public OutputDefinition(OwsCode id, Format format,
                            DataTransmissionMode dataTransmissionMode) {
        this(id, format, dataTransmissionMode, null);
    }

    public OutputDefinition(OwsCode id, Format format,
                            DataTransmissionMode dataTransmissionMode,
                            List<OutputDefinition> outputs) {
        this.dataTransmissionMode = dataTransmissionMode == null ? DataTransmissionMode.VALUE : dataTransmissionMode;
        this.id = id;
        this.format = format == null ? Format.empty() : format;
        this.outputs = outputs == null ? new LinkedList<>() : outputs;
    }

    public DataTransmissionMode getDataTransmissionMode() {
        return dataTransmissionMode;
    }

    public void setDataTransmissionMode(
            DataTransmissionMode dataTransmissionMode) {
        this.dataTransmissionMode = dataTransmissionMode;
    }

    public OwsCode getId() {
        return id;
    }

    public void setId(OwsCode identifier) {
        this.id = identifier;
    }

    public Format getFormat() {
        return format;
    }

    public void setFormat(Format format) {
        this.format = format;
    }

    public List<OutputDefinition> getOutputs() {
        return Collections.unmodifiableList(outputs);
    }

    public void setOutputs(List<OutputDefinition> outputs) {
        this.outputs = outputs == null ? Collections.emptyList() : outputs;
    }

    public Map<OwsCode, OutputDefinition> getOutputsById() {
        return getOutputsById(getOutputs());
    }

    public boolean hasOutputs() {
        return !this.outputs.isEmpty();
    }

    @Override
    public int hashCode() {
        return Objects
                .hash(this.dataTransmissionMode, this.id, this.format, this.outputs);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final OutputDefinition other = (OutputDefinition) obj;
        return this.getDataTransmissionMode() == other.getDataTransmissionMode() &&
               Objects.equals(this.getId(), other.getId()) &&
               Objects.equals(this.getFormat(), other.getFormat()) &&
               Objects.equals(this.getOutputs(), other.getOutputs());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("id", this.id)
                .add("format", this.format)
                .add("dataTransmissionMode", this.dataTransmissionMode)
                .add("outputs", this.outputs)
                .toString();
    }

    public static Map<OwsCode, OutputDefinition> getOutputsById(List<OutputDefinition> outputs) {
        return outputs.stream().collect(toMap(OutputDefinition::getId, Function.identity()));
    }

}
