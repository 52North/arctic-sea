/*
 * Copyright 2016-2017 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.inspire.omso;

import org.n52.shetland.ogc.om.OmObservation;
import org.n52.shetland.ogc.om.features.samplingFeatures.AbstractSamplingFeature;
import org.n52.shetland.ogc.om.values.GeometryValue;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Abstract class for INSPIRE OM Specialised Observations
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public abstract class AbstractInspireObservation
        extends OmObservation {

    /**
     * constructor
     */
    public AbstractInspireObservation() {
    }

    /**
     * constructor
     *
     * @param observation
     *            {@link OmObservation} to convert
     */
    public AbstractInspireObservation(OmObservation observation) {
        this();
        observation.copyTo(this);
        if (getObservationConstellation().getFeatureOfInterest() instanceof AbstractSamplingFeature) {
            AbstractSamplingFeature sf =
                    (AbstractSamplingFeature) getObservationConstellation().getFeatureOfInterest();
            sf.setEncode(true);
        }
    }

    /**
     * Check if the {@link OmObservation} has a featureOfInterest with geometry
     * value
     *
     * @param observation
     *            {@link OmObservation} to check
     * @return <code>true</code>, if the {@link OmObservation} has a
     *         featureOfInterest with geometry value
     */
    protected boolean checkForFeatureGeometry(OmObservation observation) {
        if (observation.getObservationConstellation().isSetFeatureOfInterest() && observation
                .getObservationConstellation().getFeatureOfInterest() instanceof AbstractSamplingFeature) {
            return ((AbstractSamplingFeature) observation.getObservationConstellation().getFeatureOfInterest())
                    .isSetGeometry();
        }
        return false;
    }

    /**
     * Get the geometry value from the featureOfInterest of the
     * {@link OmObservation}
     *
     * @param observation
     *            The {@link OmObservation} to get the geometry from
     * @return The geometry
     */
    protected Geometry getGeometryFromFeature(OmObservation observation) {
        return ((AbstractSamplingFeature) observation.getObservationConstellation().getFeatureOfInterest())
                .getGeometry();
    }

    /**
     * Get the geometry value from the samplingGeometry (om:parameter) of the
     * {@link OmObservation}
     *
     * @param observation
     *            The {@link OmObservation} to get the geometry from
     * @return The geometry
     */
    protected Geometry getGeometryFromSamplingGeometry(OmObservation observation) {
        return ((GeometryValue) getSpatialFilteringProfileParameter().getValue()).getValue();
    }
}
