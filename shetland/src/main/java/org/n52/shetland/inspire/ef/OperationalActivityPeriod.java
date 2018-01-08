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

import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.gml.time.Time;
import org.n52.shetland.w3c.xlink.AttributeSimpleAttrs;
import org.n52.shetland.w3c.xlink.SimpleAttrs;

public class OperationalActivityPeriod extends AbstractFeature implements AttributeSimpleAttrs {

    private SimpleAttrs simpleAttrs;

    /**
     * 1..1
     */
    private Time activityTime;

    public OperationalActivityPeriod(Time activityTime) {
        super("");
        this.activityTime = activityTime;
    }

    /**
     * @return the activityTime
     */
    public Time getActivityTime() {
        return activityTime;
    }

    @Override
    public void setSimpleAttrs(SimpleAttrs simpleAttrs) {
        this.simpleAttrs = simpleAttrs;
    }

    @Override
    public SimpleAttrs getSimpleAttrs() {
        return simpleAttrs;
    }

}
