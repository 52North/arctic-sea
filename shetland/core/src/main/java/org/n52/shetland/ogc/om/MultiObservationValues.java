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
package org.n52.shetland.ogc.om;

import org.n52.shetland.ogc.gml.time.Time;
import org.n52.shetland.ogc.om.values.MultiValue;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Class representing a multi value observation value
 *
 * @since 1.0.0
 *
 * @param <T>
 *            value type
 */
public class MultiObservationValues<
        T> extends AbstractObservationValue<MultiValue<T>> {
    /**
     * Mesurement values
     */
    private MultiValue<T> values;

    /**
     * Phenomenon time
     */
    private Time phenomenonTime;

    @Override
    @SuppressFBWarnings({ "EI_EXPOSE_REP" })
    public Time getPhenomenonTime() {
        if (phenomenonTime == null && getValue() != null) {
            phenomenonTime = getValue().getPhenomenonTime();
        }
        return phenomenonTime;
    }

    @Override
    @SuppressFBWarnings({ "EI_EXPOSE_REP" })
    public MultiValue<T> getValue() {
        return values;
    }

    @Override
    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public void setValue(MultiValue<T> value) {
        this.values = value;
    }

    @Override
    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public void setPhenomenonTime(Time phenomenonTime) {
        this.phenomenonTime = phenomenonTime;
    }

    @Override
    public boolean isSetValue() {
        return getValue() != null && getValue().isSetValue();
    }

}
