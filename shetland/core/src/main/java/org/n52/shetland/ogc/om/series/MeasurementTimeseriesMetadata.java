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
 * This class implements the OGC WaterML 2.0 and TimeseriesML 1.0 element <code>metadata</code>. See
 * <code>/req/uml-timeseries-observation/metadata</code>.
 *
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk
 *         J&uuml;rrens</a>
 * @see <a href="http://www.opengeospatial.org/standards/waterml">http://www.opengeospatial.org/standards/waterml</a>
 * @see <a href="http://www.opengeospatial.org/standards/tsml">http://www.opengeospatial.org/standards/tsml</a>
 * @since 1.0.0
 */
public class MeasurementTimeseriesMetadata
        extends TimeseriesMetadata {

    private boolean cumulative;

    /**
     * "A series that is defined as cumulative is one where the values indicate
     * a sequentially increasing series; i.e. each value is added to the last so
     * the value represents the total of a value since accumulation began."
     * (Source: OGC#10-126r3)
     */
    public boolean isCumulative() {
        return cumulative;
    }

    public MeasurementTimeseriesMetadata setCumulative(boolean cumulative) {
        this.cumulative = cumulative;
        return this;
    }

}
