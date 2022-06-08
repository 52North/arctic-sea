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

import org.n52.shetland.ogc.gml.AbstractGML;
import org.n52.shetland.ogc.gml.time.Time;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public class NetworkFacility extends AbstractGML {

    /**
     * 1..1
     */
    private Time linkingTime;

    /**
     * 1..1
     */
    private EnvironmentalMonitoringNetwork belongsTo;

    /**
     * 1..1
     */
    private EnvironmentalMonitoringFacility contains;

    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public NetworkFacility(Time linkingTime, EnvironmentalMonitoringNetwork belongsTo,
            EnvironmentalMonitoringFacility contains) {
        this.linkingTime = linkingTime;
        this.belongsTo = belongsTo;
        this.contains = contains;
    }

    /**
     * @return the linkingTime
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP" })
    public Time getLinkingTime() {
        return linkingTime;
    }

    /**
     * @param linkingTime
     *            the linkingTime to set
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public void setLinkingTime(Time linkingTime) {
        this.linkingTime = linkingTime;
    }

    /**
     * @return the belongsTo
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP" })
    public EnvironmentalMonitoringNetwork getBelongsTo() {
        return belongsTo;
    }

    /**
     * @param belongsTo
     *            the belongsTo to set
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public void setBelongsTo(EnvironmentalMonitoringNetwork belongsTo) {
        this.belongsTo = belongsTo;
    }

    /**
     * @return the contains
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP" })
    public EnvironmentalMonitoringFacility getContains() {
        return contains;
    }

    /**
     * @param contains
     *            the contains to set
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public void setContains(EnvironmentalMonitoringFacility contains) {
        this.contains = contains;
    }
}
