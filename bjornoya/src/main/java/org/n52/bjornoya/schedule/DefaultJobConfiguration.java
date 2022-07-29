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
import java.util.LinkedHashSet;
import java.util.Set;

import org.n52.bjornoya.schedule.JobConfiguration.JobType;
import org.n52.faroe.Validation;
import org.n52.faroe.annotation.Configurable;
import org.n52.faroe.annotation.Setting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

@Configurable
@SuppressFBWarnings({ "EI_EXPOSE_REP", "EI_EXPOSE_REP2" })
public class DefaultJobConfiguration implements CronExpressionValidator {

    public static final String FULL_HARVEST_UPDATE = "harvest.full";
    public static final String TEMPORAL_HARVEST_UPDATE = "harvest.temporal";
    public static final String DEFUALT_FULL_HARVEST_JOB_NAME = "Default full harvest job";
    public static final String DEFUALT_TEMPORAL_HARVEST_JOB_NAME = "Default temporal harvest job";
    private static final String DOLLAR_BRACE = "${";
    private static final String BRACE = "}";
    private static final String COLON = ":";
    private static final String DEFAULT_FULL = "0 0 03 * * ?";
    private static final String DEFAULT_TEMPORAL = "0 0/5 * * * ?";
    private static final String FULL_HARVEST_UPDATE_VALUE =
            DOLLAR_BRACE + FULL_HARVEST_UPDATE + COLON + DEFAULT_FULL + BRACE;
    private static final String TEMPORAL_HARVEST_UPDATE_VALUE =
            DOLLAR_BRACE + TEMPORAL_HARVEST_UPDATE + COLON + DEFAULT_TEMPORAL + BRACE;
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultJobConfiguration.class);
    private String cronFullExpression = DEFAULT_FULL;
    private String cronTemporalExpression = DEFAULT_TEMPORAL;
    private JobHandler jobHandler;
    private Set<String> defaultJobNames = new LinkedHashSet<>();

    /**
     * @return the cronFullExpression
     */
    public String getFullCronExpression() {
        return cronFullExpression;
    }

    /**
     * @param cronFullExpression
     *            the cronFullExpression to set
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
     * @return the cronTemporalExpression
     */
    public String getTemporalCronExpression() {
        return cronTemporalExpression;
    }

    /**
     * @param cronTemporalExpression
     *            the cronTemporalExpression to set
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

    public DefaultJobConfiguration setJobHandler(JobHandler jobHandler) {
        this.jobHandler = jobHandler;
        return this;
    }

    private void reschedule() {
        if (jobHandler != null) {
            jobHandler.reschedule();
        }
    }

    public JobConfiguration getFullJobConfiguration() {
        return getFullJobConfiguration(DEFUALT_FULL_HARVEST_JOB_NAME);
    }

    public JobConfiguration getFullJobConfiguration(String name) {
        addDefaultJobName(name);
        return new JobConfiguration().setEnabled(true).setTriggerAtStartup(true)
                .setCronExpression(getFullCronExpression()).setJobType(JobType.full).setName(name);
    }

    public JobConfiguration getTemporalJobConfiguration() {
        return getTemporalJobConfiguration(DEFUALT_TEMPORAL_HARVEST_JOB_NAME);
    }

    public JobConfiguration getTemporalJobConfiguration(String name) {
        addDefaultJobName(name);
        return new JobConfiguration().setEnabled(true).setTriggerAtStartup(true)
                .setCronExpression(getTemporalCronExpression()).setJobType(JobType.temporal).setName(name);
    }

    public DefaultJobConfiguration addDefaultJobNames(Collection<String> names) {
        if (names != null) {
            names.forEach(this::addDefaultJobName);
        }
        return this;
    }

    public DefaultJobConfiguration addDefaultJobName(String name) {
        if (name != null && !name.isEmpty()) {
            this.defaultJobNames.add(name);
        }
        return this;
    }

    public DefaultJobConfiguration removeDefaultJobName(String name) {
        if (name != null && !name.isEmpty()) {
            this.defaultJobNames.remove(name);
        }
        return this;
    }

    public Collection<String> getDefaultJobNames() {
        return defaultJobNames;
    }

}
