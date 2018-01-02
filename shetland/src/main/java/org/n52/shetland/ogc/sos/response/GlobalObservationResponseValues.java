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
package org.n52.shetland.ogc.sos.response;

import org.n52.shetland.ogc.gml.time.Time;
import org.n52.shetland.ogc.gml.time.TimeInstant;
import org.n52.shetland.ogc.gml.time.TimePeriod;

/**
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public class GlobalObservationResponseValues {

    private Time phenomenonTime;

    public GlobalObservationResponseValues addPhenomenonTime(Time phenomenonTime) {
        if (isSetPhenomenonTime()) {
            if (phenomenonTime instanceof TimeInstant) {
                this.phenomenonTime = new TimePeriod(this.phenomenonTime, this.phenomenonTime);
            }
            ((TimePeriod) this.phenomenonTime).extendToContain(phenomenonTime);
        } else {
            this.phenomenonTime = phenomenonTime;
        }
        return this;
    }

    public GlobalObservationResponseValues setPhenomenonTime(Time phenomenonTime) {
        this.phenomenonTime = phenomenonTime;
        return this;
    }

    public Time getPhenomenonTime() {
        return phenomenonTime;
    }

    public boolean isSetPhenomenonTime() {
        return getPhenomenonTime() != null && !getPhenomenonTime().isEmpty();
    }

    public boolean isEmpty() {
        return !isSetPhenomenonTime();
    }

}
