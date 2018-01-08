/*
 * Copyright 2015-2018 52°North Initiative for Geospatial Open Source
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
import org.n52.shetland.w3c.xlink.AttributeSimpleAttrs;
import org.n52.shetland.w3c.xlink.SimpleAttrs;

public class Hierarchy
        extends AbstractGML
        implements AttributeSimpleAttrs {

    private SimpleAttrs simpleAttrs;

    /**
     * 0..1
     */
    private Time linkingTime;

    /**
     * 0..1
     */
    private AbstractMonitoringObject broader;

    /**
     * 0..1
     */
    private AbstractMonitoringObject narrower;

    @Override
    public void setSimpleAttrs(SimpleAttrs simpleAttrs) {
        this.simpleAttrs = simpleAttrs;
    }

    @Override
    public SimpleAttrs getSimpleAttrs() {
        return simpleAttrs;
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
     * @return the broader
     */
    public AbstractMonitoringObject getBroader() {
        return broader;
    }

    /**
     * @param broader
     *            the broader to set
     */
    public void setBroader(AbstractMonitoringObject broader) {
        this.broader = broader;
    }

    /**
     * @return the narrower
     */
    public AbstractMonitoringObject getNarrower() {
        return narrower;
    }

    /**
     * @param narrower
     *            the narrower to set
     */
    public void setNarrower(AbstractMonitoringObject narrower) {
        this.narrower = narrower;
    }

}