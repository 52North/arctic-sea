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

import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.gml.time.Time;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public class OperationalActivityPeriod extends AbstractFeature {

    /**
     * 1..1
     */
    private Time activityTime;

    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public OperationalActivityPeriod(Time activityTime) {
        super("");
        this.activityTime = activityTime;
    }

    /**
     * @return the activityTime
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP" })
    public Time getActivityTime() {
        return activityTime;
    }
}
