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
package org.n52.shetland.ogc.om.series.wml;

/**
 * This class implements the <a href="https://portal.opengeospatial.org/files/?artifact_id=48531">OGC WaterML 2.0</a>
 * element <code>metadata</code>. See <code>/req/uml-timeseries-observation/metadata</code>.
 *
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 * @since 1.0.0
 * @see http://www.opengeospatial.org/standards/waterml
 */
public class Metadata {

    private TimeseriesMetadata timeseriesmetadata;

    public boolean isSetTimeseriesMetadata() {
        return timeseriesmetadata != null;
    }

    public TimeseriesMetadata getTimeseriesmetadata() {
        return timeseriesmetadata;
    }

    public Metadata setTimeseriesmetadata(TimeseriesMetadata timeseriesMetadata) {
        this.timeseriesmetadata = timeseriesMetadata;
        return this;
    }
}
