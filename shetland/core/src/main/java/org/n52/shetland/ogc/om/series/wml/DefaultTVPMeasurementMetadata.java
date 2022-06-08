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
package org.n52.shetland.ogc.om.series.wml;

import org.n52.shetland.ogc.om.series.AbstractDefaultTVPMeasurementMetadata;
import org.n52.shetland.ogc.om.series.AbstractInterpolationType;
import org.n52.shetland.ogc.om.series.wml.WaterMLConstants.InterpolationType;

/**
 * This class implements the OGC WaterML 2.0 element
 * <code>MeasurementTimeseries &gt; defaultPointMetadata &gt; DefaultTVPMeasurementMetadata</code>.
 *
 * See <code>/req/xsd-measurement-timeseries-tvp/interpolation-type</code>.
 *
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 * @since 1.0.0
 * @see <a href="http://www.opengeospatial.org/standards/waterml">OGC WaterML</a>
 */
public class DefaultTVPMeasurementMetadata
        extends AbstractDefaultTVPMeasurementMetadata<DefaultTVPMeasurementMetadata> {

    private InterpolationType interpolationtype;

    @Override
    public AbstractInterpolationType getInterpolationtype() {
        return interpolationtype;
    }

    @Override
    public DefaultTVPMeasurementMetadata setInterpolationtype(AbstractInterpolationType interpolationtype) {
        this.interpolationtype = (InterpolationType) interpolationtype;
        return this;
    }

}
