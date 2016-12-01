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

import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.n52.shetland.ogc.wps.description.ProcessDescription;
import org.n52.shetland.util.CollectionHelper;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class ProcessOffering implements Comparable<ProcessOffering> {
    private static final String DEFAULT_PROCESS_MODEL = "native";
    private final ProcessDescription processDescription;
    private final Set<JobControlOption> jobControlOptions;
    private final Set<DataTransmissionMode> outputTransmissionModes;
    private final String processModel;

    public ProcessOffering(ProcessDescription processDescription) {
        this(processDescription, JobControlOption.defaultOptions(), EnumSet
             .allOf(DataTransmissionMode.class), DEFAULT_PROCESS_MODEL);
    }

    public ProcessOffering(ProcessDescription processDescription,
                           Collection<JobControlOption> jobControlOptions) {
        this(processDescription, jobControlOptions, EnumSet
             .allOf(DataTransmissionMode.class), DEFAULT_PROCESS_MODEL);
    }

    public ProcessOffering(ProcessDescription processDescription,
                           Collection<JobControlOption> jobControlOptions,
                           Collection<DataTransmissionMode> outputTransmissionModes) {
        this(processDescription, jobControlOptions, outputTransmissionModes, DEFAULT_PROCESS_MODEL);
    }

    public ProcessOffering(ProcessDescription processDescription,
                           Collection<JobControlOption> jobControlOptions,
                           Collection<DataTransmissionMode> outputTransmissionModes,
                           String processModel) {
        this.processDescription = Objects.requireNonNull(processDescription);
        this.jobControlOptions = CollectionHelper
                .newSortedSet(jobControlOptions);
        this.outputTransmissionModes = CollectionHelper
                .newSortedSet(outputTransmissionModes);
        this.processModel = Optional.ofNullable(processModel)
                .orElse(DEFAULT_PROCESS_MODEL);
    }

    public Optional<String> getProcessVersion() {
        return Optional.ofNullable(this.processDescription.getVersion());
    }

    public String getProcessModel() {
        return processModel;
    }

    public ProcessDescription getProcessDescription() {
        return processDescription;
    }

    public Collection<JobControlOption> getJobControlOptions() {
        return Collections.unmodifiableCollection(jobControlOptions);
    }

    public Collection<DataTransmissionMode> getOutputTransmissionModes() {
        return Collections.unmodifiableCollection(outputTransmissionModes);
    }

    @Override
    public int compareTo(ProcessOffering o) {
        return getProcessDescription().compareTo(o.getProcessDescription());
    }
}
