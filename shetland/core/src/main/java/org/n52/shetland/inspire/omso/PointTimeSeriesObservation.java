/*
 * Copyright 2015-${currentYear} 52°North Spatial Information Research GmbH
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

import org.n52.shetland.ogc.om.NamedValue;
import org.n52.shetland.ogc.om.OmObservation;
import org.n52.shetland.ogc.om.features.samplingFeatures.AbstractSamplingFeature;
import org.n52.shetland.ogc.om.features.samplingFeatures.InvalidSridException;

import com.google.common.collect.Sets;

public class PointTimeSeriesObservation
        extends AbstractInspireObservation {

    /**
     * constructor
     */
    public PointTimeSeriesObservation() {
        super();
    }

    /**
     * constructor
     *
     * @param observation
     *            {@link OmObservation} to convert
     */
    public PointTimeSeriesObservation(OmObservation observation) {
        super(observation);
        if (!checkForFeatureGeometry(observation) && observation.isSetSpatialFilteringProfileParameter()) {
            try {
                ((AbstractSamplingFeature) getObservationConstellation().getFeatureOfInterest())
                        .setGeometry(getGeometryFromSamplingGeometry(observation));
            } catch (InvalidSridException e) {
                // TODO
            }
        }
        observation.setParameter(Sets.<NamedValue<?>> newHashSet());
        getObservationConstellation().setObservationType(InspireOMSOConstants.OBS_TYPE_POINT_TIME_SERIES_OBSERVATION);
    }

    @Override
    public OmObservation cloneTemplate() {
        if (getObservationConstellation().getFeatureOfInterest() instanceof AbstractSamplingFeature) {
            ((AbstractSamplingFeature) getObservationConstellation().getFeatureOfInterest()).setEncode(true);
        }
        return cloneTemplate(new PointTimeSeriesObservation());
    }
}
