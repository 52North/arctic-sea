/*
 * Copyright (C) 2015-2021 52°North Spatial Information Research GmbH
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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;

import org.n52.faroe.ConfigurationError;
import org.n52.faroe.Validation;
import org.n52.faroe.annotation.Configurable;
import org.n52.faroe.annotation.Setting;
import org.n52.janmayen.lifecycle.Constructable;
import org.quartz.CronExpression;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

@Configurable
public class JobFactory implements Constructable {

    public static final String FULL_HARVEST_UPDATE = "harvest.full";
    public static final String TEMPORAL_HARVEST_UPDATE = "harvest.temporal";
    private static final String DOLLAR_BRACE = "${";
    private static final String BRACE = "}";
    private static final String COLON = ":";
    private static final String DEFAULT_FULL = "0 0 03 * * ?";
    private static final String DEFAULT_TEMPORAL = "0 0/5 * * * ?";
    private static final String FULL_HARVEST_UPDATE_VALUE =
            DOLLAR_BRACE + FULL_HARVEST_UPDATE + COLON + DEFAULT_FULL + BRACE;
    private static final String TEMPORAL_HARVEST_UPDATE_VALUE =
            DOLLAR_BRACE + TEMPORAL_HARVEST_UPDATE + COLON + DEFAULT_TEMPORAL + BRACE;
    private static final Logger LOGGER = LoggerFactory.getLogger(JobFactory.class);
    private String cronFullExpression = DEFAULT_FULL;
    private String cronTemporalExpression = DEFAULT_TEMPORAL;
    private Scheduler scheduler;
    private Set<String> jobs = new HashSet<>();
    private List<ScheduledJob> scheduledJobs = new ArrayList<>();
    private boolean initialized;

    @Inject
    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Inject
    public void setScheduledJobs(Optional<List<ScheduledJob>> scheduledJobs) {
        this.scheduledJobs.clear();
        if (scheduledJobs.isPresent()) {
            this.scheduledJobs.addAll(scheduledJobs.get());
        }
    }

    public List<ScheduledJob> getScheduledJobs() {
        return scheduledJobs;
    }

    /**
     * @return the updateDefinition
     */
    public String getFullCronExpression() {
        return cronFullExpression;
    }

    /**
     * @param updateDefinition
     *            the updateDefinition to set
     */
    @Setting(FULL_HARVEST_UPDATE)
    @Value(FULL_HARVEST_UPDATE_VALUE)
    public void setFullCronExpression(String cronExpression) {
        Validation.notNullOrEmpty("Cron expression for full update!", cronExpression);
        validate(cronExpression);
        if (this.cronFullExpression == null) {
            this.cronFullExpression = cronExpression;
            reschedule();
        } else if (!this.cronFullExpression.equalsIgnoreCase(cronExpression)) {
            this.cronFullExpression = cronExpression;
            reschedule();
        }
    }

    /**
     * @return the updateDefinition
     */
    public String getTemporalCronExpression() {
        return cronTemporalExpression;
    }

    /**
     * @param updateDefinition
     *            the updateDefinition to set
     */
    @Setting(TEMPORAL_HARVEST_UPDATE)
    @Value(TEMPORAL_HARVEST_UPDATE_VALUE)
    public void setTemporalCronExpression(String cronExpression) {
        Validation.notNullOrEmpty("Cron expression for temporal update!", cronExpression);
        validate(cronExpression);
        if (this.cronTemporalExpression == null) {
            this.cronTemporalExpression = cronExpression;
            reschedule();
        } else if (!this.cronTemporalExpression.equalsIgnoreCase(cronExpression)) {
            this.cronTemporalExpression = cronExpression;
            reschedule();
        }
    }

    private void reschedule() {
        reschedule(true);
    }

    private void reschedule(boolean update) {
        if ((!initialized && !update) || (initialized && update)) {
            for (ScheduledJob job : getScheduledJobs()) {
                if (jobs.contains(job.getJobName())) {
                    boolean updateJob = false;
                    if (job instanceof FullHarvesterJob) {
                        updateJob = checkCronExpression(job, getFullCronExpression());
                    } else if (job instanceof TemporalHarvesterJob) {
                        updateJob = checkCronExpression(job, getTemporalCronExpression());
                    }
                    if (updateJob) {
                        try {
                            scheduler.updateJob(job);
                        } catch (SchedulerException e) {
                            LOGGER.error("Error while updating a job!", e);
                        }
                    }
                } else {
                    if (job instanceof FullHarvesterJob) {
                        job.setCronExpression(getFullCronExpression());
                    } else if (job instanceof TemporalHarvesterJob) {
                        job.setCronExpression(getTemporalCronExpression());
                    }
                    scheduler.scheduleJob(job);
                }
                jobs.add(job.getJobName());
            }
        }
    }

    private boolean checkCronExpression(ScheduledJob job, String cronExpression) {
        if (job.getCronExpression() == null || (job.getCronExpression() != null && !job.getCronExpression()
                .isEmpty() && !job.getCronExpression()
                        .equals(cronExpression))) {
            job.setCronExpression(cronExpression);
            return true;
        }
        return false;
    }

    private void validate(String cronExpression) {
        try {
            CronExpression.validateExpression(cronExpression);
        } catch (ParseException e) {
            throw new ConfigurationError(String.format(
                    "%s is invalid! Please check http://www.quartz-scheduler.org/documentation/quartz-2.3.0/tutorials"
                    + "/tutorial-lesson-06.html",
                    cronExpression));
        }
    }

    @Override
    public void init() {
        reschedule(false);
        this.initialized = true;
    }
}
