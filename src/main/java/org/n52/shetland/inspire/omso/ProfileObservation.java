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

import java.util.List;

import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.om.ObservationValue;
import org.n52.shetland.ogc.om.OmObservation;
import org.n52.shetland.ogc.om.SingleObservationValue;
import org.n52.shetland.ogc.om.StreamingValue;
import org.n52.shetland.ogc.om.features.SfConstants;
import org.n52.shetland.ogc.om.features.samplingFeatures.AbstractSamplingFeature;
import org.n52.shetland.ogc.om.features.samplingFeatures.InvalidSridException;
import org.n52.shetland.ogc.om.values.ProfileLevel;
import org.n52.shetland.ogc.om.values.ProfileValue;
import org.n52.shetland.ogc.om.values.RectifiedGridCoverage;
import org.n52.shetland.ogc.om.values.ReferencableGridCoverage;
import org.n52.shetland.util.CollectionHelper;

import com.google.common.collect.Lists;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;

public class ProfileObservation
        extends AbstractInspireObservation {

    /**
     * constructor
     */
    public ProfileObservation() {
        super();
    }

    /**
     * constructor
     *
     * @param observation
     *            {@link OmObservation} to convert
     */
    public ProfileObservation(OmObservation observation) {
        super(observation);
        getObservationConstellation().setObservationType(InspireOMSOConstants.OBS_TYPE_PROFILE_OBSERVATION);
    }

    @Override
    public OmObservation cloneTemplate() {
        if (getObservationConstellation().getFeatureOfInterest() instanceof AbstractSamplingFeature) {
            ((AbstractSamplingFeature) getObservationConstellation().getFeatureOfInterest()).setEncode(true);
        }
        return cloneTemplate(new ProfileObservation());
    }

    @Override
    public void setValue(ObservationValue<?> value) {
        if (value instanceof StreamingValue<?>) {
            super.setValue(value);
        } else if (value.getValue() instanceof RectifiedGridCoverage
                || value.getValue() instanceof ReferencableGridCoverage) {
            super.setValue(value);
        } else if (value.getValue() instanceof ProfileValue) {
            ProfileValue profile = (ProfileValue) value.getValue();
            RectifiedGridCoverage rectifiedGridCoverage = new RectifiedGridCoverage(getObservationID());
            rectifiedGridCoverage.setUnit(value.getValue().getUnit());
            List<Coordinate> coordinates = Lists.newArrayList();
            int srid = 0;
            for (ProfileLevel level : profile.getValue()) {
                rectifiedGridCoverage.addValue(level.getLevelStart().getValue(), level.getSimpleValue());
                if (level.isSetLocation()) {
                    Coordinate coordinate = level.getLocation().getCoordinate();
                    coordinate.z = level.getLevelStart().getValue();
                    coordinates.add(coordinate);
                    if (srid == 0) {
                        srid = level.getLocation().getSRID();
                    }
                }
            }
            if (CollectionHelper.isNotEmpty(coordinates)) {
                setFeatureGeometry(coordinates, srid);
            }
            super.setValue(new SingleObservationValue<>(value.getPhenomenonTime(), rectifiedGridCoverage));
        } else {
            double heightDepth = 0;
            if (isSetHeightDepthParameter()) {
                heightDepth = getHeightDepthParameter().getValue().getValue();
                removeParameter(getHeightDepthParameter());
            }
            RectifiedGridCoverage rectifiedGridCoverage = new RectifiedGridCoverage(getObservationID());
            rectifiedGridCoverage.setUnit(value.getValue().getUnit());
            rectifiedGridCoverage.addValue(heightDepth, value.getValue());
            super.setValue(new SingleObservationValue<>(value.getPhenomenonTime(), rectifiedGridCoverage));
        }
    }

    private void setFeatureGeometry(List<Coordinate> coordinates, int srid) {
        AbstractFeature featureOfInterest = getObservationConstellation().getFeatureOfInterest();
        if (featureOfInterest instanceof AbstractSamplingFeature) {
            AbstractSamplingFeature sf = (AbstractSamplingFeature) featureOfInterest;
            Coordinate[] coords = coordinates.toArray(new Coordinate[0]);
            try {
                LineString lineString = new GeometryFactory().createLineString(coords);
                lineString.setSRID(srid);
                sf.setGeometry(lineString);
                sf.setFeatureType(SfConstants.SAMPLING_FEAT_TYPE_SF_SAMPLING_CURVE);
            } catch (InvalidSridException e) {
                // TODO
            }
        }
    }

    protected boolean mergeValues(ObservationValue<?> observationValue) {
        if (observationValue.getValue() instanceof RectifiedGridCoverage) {
            ((RectifiedGridCoverage) getValue().getValue())
                    .addValue(((RectifiedGridCoverage) observationValue.getValue()).getValue());
            // } else if (observationValue.getValue() instanceof
            // ReverencableGridCoverage) {
            // ((ReverencableGridCoverage)getValue()).addValue(((ReverencableGridCoverage)observationValue).getValue());

            // if (getObservationConstellation().getFeatureOfInterest()
            // instanceof AbstractSamplingFeature) {
            // if (((AbstractSamplingFeature)
            // getObservationConstellation().getFeatureOfInterest()).isSetGeometry())
            // {
            // // TODO check for SamplingCurve and Depht/Height
            // }
            // }
            return true;
        }
        return false;
    }
}
