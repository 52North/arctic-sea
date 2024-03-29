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
package org.n52.shetland.inspire.ef;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import org.n52.shetland.inspire.base.Identifier;
import org.n52.shetland.ogc.gml.ReferenceType;
import org.n52.shetland.util.CollectionHelper;

import com.google.common.collect.Sets;

public class EnvironmentalMonitoringProgramme extends AbstractMonitoringObject {

    /**
     * 0..1
     */
    private Set<EnvironmentalMonitoringActivity> triggers = Sets.newHashSet();

    public EnvironmentalMonitoringProgramme(Identifier inspireId, ReferenceType mediaMonitored) {
        super(inspireId, mediaMonitored);
    }

    public EnvironmentalMonitoringProgramme(Identifier inspireId, Collection<ReferenceType> mediaMonitored) {
        super(inspireId, mediaMonitored);
    }

    /**
     * @return the triggers
     */
    public Set<EnvironmentalMonitoringActivity> getTriggers() {
        return Collections.unmodifiableSet(triggers);
    }

    /**
     * @param triggers
     *            the triggers to set
     * @return this
     */
    public EnvironmentalMonitoringProgramme setTriggers(Collection<EnvironmentalMonitoringActivity> triggers) {
        this.triggers.clear();
        if (triggers != null) {
            this.triggers.addAll(triggers);
        }
        return this;
    }

    public boolean isSetTriggers() {
        return CollectionHelper.isNotEmpty(getTriggers());
    }

}
