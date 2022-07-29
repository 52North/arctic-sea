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
package org.n52.bjornoya.schedule;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import javax.inject.Inject;

import org.n52.faroe.annotation.Configurable;
import org.n52.janmayen.lifecycle.Constructable;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

@Configurable
public class JobHandler implements Constructable, CronExpressionValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobHandler.class);
    private Scheduler scheduler;
    private Set<String> jobs = new HashSet<>();
    private Set<ScheduledJob> scheduledJobs = new TreeSet<>();
    private DefaultJobConfiguration defaultJobConfiguration;

    @Inject
    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Inject
    public void setDefaultJobConfiguration(DefaultJobConfiguration defaultJobConfiguration) {
        this.defaultJobConfiguration = defaultJobConfiguration.setJobHandler(this);
    }

    @Inject
    public void setScheduledJobs(Optional<Collection<ScheduledJob>> scheduledJobs) {
        this.scheduledJobs.clear();
        if (scheduledJobs.isPresent()) {
            this.scheduledJobs.addAll(scheduledJobs.get());
        }
    }

    public void addScheduledJobs(Collection<ScheduledJob> scheduledJobs) {
        if (scheduledJobs != null) {
            this.scheduledJobs.addAll(scheduledJobs);
        }
        reschedule();
    }

    public void addScheduledJob(ScheduledJob scheduledJob) {
        if (scheduledJob != null) {
            this.scheduledJobs.add(scheduledJob);
        }
        reschedule();
    }

    public List<ScheduledJob> getScheduledJobs() {
        return new LinkedList<>(scheduledJobs);
    }

    public void reschedule() {
        reschedule(defaultJobConfiguration.getDefaultJobNames());
    }

    public void reschedule(Collection<String> defaultJobNames) {
        for (ScheduledJob job : getScheduledJobs()) {
            if (jobs.contains(job.getJobName())) {
                if (job instanceof FullHarvesterJob && defaultJobNames.contains(job.getJobName())) {
                    job.setCronExpression(defaultJobConfiguration.getFullCronExpression());
                } else if (job instanceof TemporalHarvesterJob && defaultJobNames.contains(job.getJobName())) {
                    job.setCronExpression(defaultJobConfiguration.getTemporalCronExpression());
                }
                if (job.isModified()) {
                    try {
                        scheduler.updateJob(job);
                    } catch (SchedulerException e) {
                        LOGGER.error("Error while updating a job!", e);
                    }
                }
            } else {
                scheduler.scheduleJob(job);
            }
            jobs.add(job.getJobName());
            job.setModified(false);
        }
    }

    @Override
    public void init() {
        reschedule();
    }
}
