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
package org.n52.shetland.ogc.om.series;

/**
 * This class implements the OGC TimeseriesML 1.0 and OGC WaterML 2.0 element
 * <code>MeasurementTimeseries &gt; defaultPointMetadata &gt; DefaultTVPMeasurementMetadata</code>.
 *
 * See <code>/req/xsd-measurement-timeseries-tvp/</code>.
 *
 * @see <a href="http://www.opengeospatial.org/standards/tsml">OGC TSML</a>
 * @see <a href="http://www.opengeospatial.org/standards/waterml">OGC WaterML</a>
 */
public abstract class AbstractDefaultTVPMeasurementMetadata<
        T extends AbstractDefaultTVPMeasurementMetadata<?>> {

    private String aggregationDuration;

    public String getAggregationDuration() {
        return aggregationDuration;
    }

    public T setAggregationDuration(String aggregationDuration) {
        this.aggregationDuration = aggregationDuration;
        return (T) this;
    }

    public boolean isSetAggregationDuration() {
        return getAggregationDuration() != null && !getAggregationDuration().isEmpty();
    }

    public boolean isSetInterpolationType() {
        return getInterpolationtype() != null;
    }

    public abstract AbstractInterpolationType getInterpolationtype();

    public abstract T setInterpolationtype(AbstractInterpolationType interpolationtype);

}
