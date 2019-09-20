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
package org.n52.shetland.ogc.om.series;

/**
 * This class implements the OGC WaterML 2.0 and OGC TimeseriesML 1.0 element
 * <code>MeasurementTimeseries &gt; defaultPointMetadata</code>. See
 * <code>/req/xsd-timeseries-tvp/defaultPointMetadata</code>.
 *
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 * @since 1.0.0
 * @see <a href="http://www.opengeospatial.org/standards/waterml">http://www.opengeospatial.org/standards/waterml</a>
 * @see <a href="http://www.opengeospatial.org/standards/tsml">http://www.opengeospatial.org/standards/tsml</a>
 */
public class DefaultPointMetadata {

    protected AbstractDefaultTVPMeasurementMetadata defaultTVPMeasurementMetadata;

    public boolean isSetDefaultTVPMeasurementMetadata() {
        return defaultTVPMeasurementMetadata != null;
    }

    public AbstractDefaultTVPMeasurementMetadata getDefaultTVPMeasurementMetadata() {
        return defaultTVPMeasurementMetadata;
    }

    public DefaultPointMetadata setDefaultTVPMeasurementMetadata(
            AbstractDefaultTVPMeasurementMetadata defaultTVPMeasurementMetadata) {
        this.defaultTVPMeasurementMetadata = defaultTVPMeasurementMetadata;
        return this;
    }
}
