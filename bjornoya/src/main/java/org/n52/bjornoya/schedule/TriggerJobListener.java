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

import java.util.LinkedHashSet;
import java.util.Set;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.quartz.Trigger;
import org.quartz.Trigger.CompletedExecutionInstruction;
import org.quartz.TriggerListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TriggerJobListener implements JobListener, TriggerListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(TriggerJobListener.class);

    private Set<String> fullHarvestingJobs = new LinkedHashSet<>();

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        if (checkForFullHarvesterJob(context)) {
            fullHarvestingJobs.add(getGroup(context));
        }
    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {
        LOGGER.debug("The job '{}' was vetoed!", getJobName(context));
    }

    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        if (checkForFullHarvesterJob(context)) {
            fullHarvestingJobs.remove(getGroup(context));
        }
    }

    @Override
    public void triggerFired(Trigger trigger, JobExecutionContext context) {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {
        return checkForTemporalHarvesterJob(context) && fullHarvestingJobs.contains(getGroup(context));
    }

    @Override
    public void triggerMisfired(Trigger trigger) {
        // TODO Auto-generated method stub
    }

    @Override
    public void triggerComplete(Trigger trigger, JobExecutionContext context,
            CompletedExecutionInstruction triggerInstructionCode) {
        // TODO Auto-generated method stub
    }

    private String getGroup(JobExecutionContext context) {
        return context.getJobDetail()
                .getKey()
                .getGroup();
    }

    private String getJobName(JobExecutionContext context) {
        return context.getJobDetail()
                .getKey()
                .getName();
    }

    private boolean checkForFullHarvesterJob(JobExecutionContext context) {
        return context.getJobInstance() instanceof FullHarvesterJob;
    }

    private boolean checkForTemporalHarvesterJob(JobExecutionContext context) {
        return context.getJobInstance() instanceof TemporalHarvesterJob;
    }

}
