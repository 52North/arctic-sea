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

import java.util.Date;

import org.joda.time.DateTime;
import org.n52.janmayen.lifecycle.Destroyable;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public class Scheduler implements Destroyable {

    private static final Logger LOGGER = LoggerFactory.getLogger(Scheduler.class);

    private int startupDelayInSeconds = 5;

    private org.quartz.Scheduler scheduler;

    private boolean enabled = true;

    public void init() {
        if (!enabled) {
            LOGGER.debug("Job schedular disabled. No jobs will be triggered. "
                    + "This is also true for particularly enabled jobs.");
            return;
        }
        try {
            TriggerJobListener triggerJobListener = new TriggerJobListener();
            scheduler.getListenerManager().addJobListener(triggerJobListener);
            scheduler.getListenerManager().addTriggerListener(triggerJobListener);
            scheduler.startDelayed(startupDelayInSeconds);
            LOGGER.debug("Scheduler will start jobs in {}s ...", startupDelayInSeconds);
        } catch (SchedulerException e) {
            LOGGER.error("Could not start scheduler.", e);
        }
    }

    public void updateJob(ScheduledJob taskToSchedule) throws SchedulerException {
        JobDetail details = taskToSchedule.createJobDetails();
        Trigger trigger = taskToSchedule.createTrigger(details.getKey());
        Date nextExecution = scheduler.rescheduleJob(trigger.getKey(), trigger);
        LOGGER.debug("Rescheduled job '{}' will be executed at '{}'!", details.getKey(), new DateTime(nextExecution));
    }

    public void scheduleJob(ScheduledJob taskToSchedule) {
        try {
            JobDetail details = taskToSchedule.createJobDetails();
            Trigger trigger = taskToSchedule.createTrigger(details.getKey());
            Date nextExecution = scheduler.scheduleJob(details, trigger);
            LOGGER.debug("Schedule job '{}' will be executed at '{}'!", details.getKey(), new DateTime(nextExecution));
            if (taskToSchedule.isTriggerAtStartup() || taskToSchedule instanceof FullHarvesterJob) {
                LOGGER.debug("Schedule job '{}' to run once at startup.", details.getKey());
                Trigger onceAtStartup = TriggerBuilder.newTrigger().withIdentity(details.getKey() + "_onceAtStartup")
                        .forJob(details.getKey()).build();
                Date startupExecution = scheduler.scheduleJob(onceAtStartup);
                LOGGER.debug("Schedule job '{}' will be executed on startup at '{}'!", details.getKey(),
                        new DateTime(startupExecution));
            }
        } catch (SchedulerException e) {
            LOGGER.warn("Could not schdule Job '{}'.", taskToSchedule.getJobName(), e);
        }
    }

    /**
     * Shuts down the task scheduler without waiting tasks to be finished.
     */
    public void shutdown() {
        try {
            scheduler.shutdown(false);
            LOGGER.debug("Shutdown scheduler");
        } catch (SchedulerException e) {
            LOGGER.error("Could not scheduler.", e);
        }
    }

    @Override
    public void destroy() {
        shutdown();
    }

    public int getStartupDelayInSeconds() {
        return startupDelayInSeconds;
    }

    public void setStartupDelayInSeconds(int startupDelayInSeconds) {
        this.startupDelayInSeconds = startupDelayInSeconds;
    }

    @SuppressFBWarnings("EI_EXPOSE_REP")
    public org.quartz.Scheduler getScheduler() {
        return scheduler;
    }

    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public void setScheduler(org.quartz.Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

}