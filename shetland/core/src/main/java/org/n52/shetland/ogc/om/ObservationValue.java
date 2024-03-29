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
import org.n52.shetland.ogc.om.series.DefaultPointMetadata;
import org.n52.shetland.ogc.om.series.Metadata;
import org.n52.shetland.ogc.om.values.Value;

/**
 * Interface for observation values.
 *
 * @since 1.0.0
 *
 * @param <T>
 *            observation value type
 */
public interface ObservationValue<
        T extends Value<?>> {

    /**
     * Get phenomenon or sampling time of the observation
     *
     * @return Phenomenon or sampling time of the observation
     */
    Time getPhenomenonTime();

    /**
     * Set phenomenon or sampling time of the observation
     *
     * @param phenomenonTime
     *            Phenomenon or sampling time of the observation
     */
    void setPhenomenonTime(Time phenomenonTime);

    /**
     * Get observation value
     *
     * @return Observation value
     */
    T getValue();

    /**
     * Set observation value
     *
     * @param value
     *            Observation value
     */
    void setValue(T value);

    boolean isSetValue();

    boolean isSetDefaultPointMetadata();

    void setDefaultPointMetadata(DefaultPointMetadata defaultPointMetadata);

    DefaultPointMetadata getDefaultPointMetadata();

    boolean isSetMetadata();

    void setMetadata(Metadata metadata);

    Metadata getMetadata();

}
