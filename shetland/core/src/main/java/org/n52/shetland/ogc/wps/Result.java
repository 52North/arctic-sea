/*
 * Copyright 2015-2021 52°North Initiative for Geospatial Open Source
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

import org.n52.shetland.ogc.wps.data.ProcessData;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class Result {

    private OffsetDateTime expirationDate;
    private JobId jobId;
    private final List<ProcessData> outputs = new LinkedList<>();
    private ResponseMode responseMode = ResponseMode.DOCUMENT;

    public Result() {
    }

    public Optional<OffsetDateTime> getExpirationDate() {
        return Optional.ofNullable(expirationDate);
    }

    public void setExpirationDate(OffsetDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Optional<JobId> getJobId() {
        return Optional.ofNullable(jobId);
    }

    public void setJobId(JobId jobId) {
        this.jobId = jobId;
    }

    public List<ProcessData> getOutputs() {
        return Collections.unmodifiableList(outputs);
    }

    public void addOutput(ProcessData output) {
        this.outputs.add(Objects.requireNonNull(output));
    }

    public ResponseMode getResponseMode() {
        return responseMode;
    }

    public void setResponseMode(ResponseMode responseMode) {
        this.responseMode = Objects.requireNonNull(responseMode);
    }

}
