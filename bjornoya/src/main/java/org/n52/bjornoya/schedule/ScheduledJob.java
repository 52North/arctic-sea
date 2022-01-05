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

import org.joda.time.DateTime;
import org.quartz.CronScheduleBuilder;
import org.quartz.DateBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.scheduling.quartz.QuartzJobBean;

public abstract class ScheduledJob extends QuartzJobBean {
    private boolean enabled = true;

    private String jobName;

    private String triggerName;

    private String jobDescription;

    private String cronExpression;

    private boolean triggerAtStartup;

    private DateTime startUpDelay;

    private boolean modified;

    // XXX job details create a job instance! snake biting tail
    public abstract JobDetail createJobDetails();

    public String getJobName() {
        return jobName == null || jobName.isEmpty()
                ? getClass().getSimpleName()
                : jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getTriggerName() {
        return triggerName == null || triggerName.isEmpty()
                ? "trigger_" + getJobName()
                : triggerName;
    }

    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpresssion) {
        this.cronExpression = cronExpresssion;
    }

    public boolean isTriggerAtStartup() {
        return triggerAtStartup || isStartUpDelay();
    }

    public void setTriggerAtStartup(boolean triggerAtStartup) {
        this.triggerAtStartup = triggerAtStartup;
    }

    public boolean isStartUpDelay() {
        return getStartUpDelay() != null && getStartUpDelay().isAfter(DateTime.now());
    }

    public Trigger createTrigger(JobKey jobKey) {
        TriggerBuilder<Trigger> tb = TriggerBuilder.newTrigger()
                .forJob(jobKey)
                .withIdentity(getTriggerName());
        if (getCronExpression() != null) {
            tb.withSchedule(CronScheduleBuilder.cronSchedule(getCronExpression()));
        }

        if (isTriggerAtStartup()) {
            tb.startAt(isStartUpDelay() ? getStartUpDelay().toDate()
                    : DateBuilder.futureDate(5, DateBuilder.IntervalUnit.SECOND));
        }
        return tb.build();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isModified() {
        return modified;
    }

    public void setModified(boolean modified) {
        this.modified = modified;
    }

    public DateTime getStartUpDelay() {
        return startUpDelay;
    }

    public void setStartUpDelay(DateTime startUpDelay) {
        this.startUpDelay = startUpDelay;
    }
}
