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

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Optional;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class StatusInfo {

    private JobId jobId;
    private JobStatus status;
    private Optional<OffsetDateTime> expirationDate = Optional.empty();
    private Optional<OffsetDateTime> estimatedCompletion = Optional.empty();
    private Optional<OffsetDateTime> nextPoll = Optional.empty();
    private Optional<Short> percentCompleted = Optional.empty();

    public JobId getJobId() {
        return jobId;
    }

    public void setJobId(JobId jobId) {
        this.jobId = Objects.requireNonNull(jobId);
    }

    public JobStatus getStatus() {
        return status;
    }

    public void setStatus(JobStatus status) {
        this.status = Objects.requireNonNull(status);
    }

    public Optional<OffsetDateTime> getEstimatedCompletion() {
        return estimatedCompletion;
    }

    public void setEstimatedCompletion(
            OffsetDateTime estimatedCompletion) {
        this.estimatedCompletion = Optional.ofNullable(estimatedCompletion);
    }

    public Optional<OffsetDateTime> getNextPoll() {
        return nextPoll;
    }

    public void setNextPoll(OffsetDateTime nextPoll) {
        this.nextPoll = Optional.ofNullable(nextPoll);
    }

    public Optional<Short> getPercentCompleted() {
        return percentCompleted;
    }

    public void setPercentCompleted(Short percentCompleted) {
        if (percentCompleted != null &&
            (percentCompleted < 0 || percentCompleted > 100)) {
            throw new IllegalArgumentException();
        }
        this.percentCompleted = Optional.ofNullable(percentCompleted);
    }

    public Optional<OffsetDateTime> getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(OffsetDateTime expirationDate) {
        this.expirationDate = Optional.ofNullable(expirationDate);
    }
}
