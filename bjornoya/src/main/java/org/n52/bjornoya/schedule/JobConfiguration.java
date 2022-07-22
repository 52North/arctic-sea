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

public class JobConfiguration {

    private String cronExpression;
    private boolean enabled;
    private boolean triggerAtStartup;
    private String name = "default";

    public String getCronExpression() {
        return cronExpression;
    }

    public JobConfiguration setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
        return this;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public JobConfiguration setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public boolean isTriggerAtStartup() {
        return triggerAtStartup;
    }

    public JobConfiguration setTriggerAtStartup(boolean triggerAtStartup) {
        this.triggerAtStartup = triggerAtStartup;
        return this;
    }

    public String getName() {
        return name;
    }

    public JobConfiguration setName(String name) {
        this.name = name;
        return this;
    }

}
