/*
 * Copyright 2015-2019 52°North Initiative for Geospatial Open Source
 * Software GmbH
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

public class NetworkFacility
        extends AbstractGML {

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

    public NetworkFacility(
            Time linkingTime, EnvironmentalMonitoringNetwork belongsTo, EnvironmentalMonitoringFacility contains) {
        this.linkingTime = linkingTime;
        this.belongsTo = belongsTo;
        this.contains = contains;
    }

    /**
     * @return the linkingTime
     */
    public Time getLinkingTime() {
        return linkingTime;
    }

    /**
     * @param linkingTime
     *            the linkingTime to set
     */
    public void setLinkingTime(Time linkingTime) {
        this.linkingTime = linkingTime;
    }

    /**
     * @return the belongsTo
     */
    public EnvironmentalMonitoringNetwork getBelongsTo() {
        return belongsTo;
    }

    /**
     * @param belongsTo
     *            the belongsTo to set
     */
    public void setBelongsTo(EnvironmentalMonitoringNetwork belongsTo) {
        this.belongsTo = belongsTo;
    }

    /**
     * @return the contains
     */
    public EnvironmentalMonitoringFacility getContains() {
        return contains;
    }

    /**
     * @param contains
     *            the contains to set
     */
    public void setContains(EnvironmentalMonitoringFacility contains) {
        this.contains = contains;
    }
}
