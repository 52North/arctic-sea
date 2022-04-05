/*
 * Copyright 2015-${currentYear} 52°North Spatial Information Research GmbH
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
    private OffsetDateTime expirationDate;
    private OffsetDateTime estimatedCompletion;
    private OffsetDateTime nextPoll;
    private Short percentCompleted;

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
        return Optional.ofNullable(estimatedCompletion);
    }

    public void setEstimatedCompletion(OffsetDateTime estimatedCompletion) {
        this.estimatedCompletion = estimatedCompletion;
    }

    public Optional<OffsetDateTime> getNextPoll() {
        return Optional.ofNullable(nextPoll);
    }

    public void setNextPoll(OffsetDateTime nextPoll) {
        this.nextPoll = nextPoll;
    }

    public Optional<Short> getPercentCompleted() {
        return Optional.ofNullable(percentCompleted);
    }

    public void setPercentCompleted(Short percentCompleted) {
        if (percentCompleted != null && (percentCompleted < 0 || percentCompleted > 100)) {
            throw new IllegalArgumentException();
        }
        this.percentCompleted = percentCompleted;
    }

    public Optional<OffsetDateTime> getExpirationDate() {
        return Optional.ofNullable(expirationDate);
    }

    public void setExpirationDate(OffsetDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }
}
