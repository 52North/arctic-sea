/*
 * Copyright 2016-2018 52°North Initiative for Geospatial Open Source
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

import org.n52.shetland.ogc.gml.ReferenceType;
import org.n52.shetland.ogc.om.AbstractObservationValue;
import org.n52.shetland.ogc.om.ObservationValue;
import org.n52.shetland.ogc.om.OmObservation;
import org.n52.shetland.ogc.om.PointValuePair;
import org.n52.shetland.ogc.om.SingleObservationValue;
import org.n52.shetland.ogc.om.StreamingValue;
import org.n52.shetland.ogc.om.features.samplingFeatures.AbstractSamplingFeature;
import org.n52.shetland.ogc.om.features.samplingFeatures.InvalidSridException;
import org.n52.shetland.ogc.om.values.CvDiscretePointCoverage;
import org.n52.shetland.ogc.om.values.GeometryValue;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;

public class PointObservation
        extends AbstractInspireObservation {

    /**
     * constructor
     */
    public PointObservation() {
        super();
    }

    /**
     * constructor
     *
     * @param observation
     *            {@link OmObservation} to convert
     */
    public PointObservation(OmObservation observation) {
        super(observation);
        getObservationConstellation().setObservationType(InspireOMSOConstants.OBS_TYPE_POINT_OBSERVATION);
        if (!checkForFeatureGeometry(observation) && observation.isSetSpatialFilteringProfileParameter()) {
            try {
                ((AbstractSamplingFeature) getObservationConstellation().getFeatureOfInterest())
                        .setGeometry(getGeometryFromSamplingGeometry(observation));
            } catch (InvalidSridException e) {
                // TODO
            }
        }
    }

    @Override
    public OmObservation cloneTemplate() {
        if (getObservationConstellation().getFeatureOfInterest() instanceof AbstractSamplingFeature) {
            ((AbstractSamplingFeature) getObservationConstellation().getFeatureOfInterest()).setEncode(true);
        }
        return cloneTemplate(new PointObservation());
    }

    @Override
    public void setValue(ObservationValue<?> value) {
        if (value instanceof StreamingValue<?>) {
            super.setValue(value);
        } else if (value.getValue() instanceof CvDiscretePointCoverage) {
            super.setValue(value);
        } else {
            CvDiscretePointCoverage cvDiscretePointCoverage = new CvDiscretePointCoverage(getObservationID());
            cvDiscretePointCoverage
                    .setRangeType(new ReferenceType(getObservationConstellation().getObservablePropertyIdentifier()));
            cvDiscretePointCoverage.setUnit(((AbstractObservationValue<?>) value).getUnit());
            Geometry geometry = null;
            String domainExtent = "";
            if (isSetSpatialFilteringProfileParameter()
                    && getSpatialFilteringProfileParameter().getValue() instanceof GeometryValue) {
                GeometryValue geometryValue = (GeometryValue) getSpatialFilteringProfileParameter().getValue();
                geometry = getSpatialFilteringProfileParameter().getValue().getValue();
                domainExtent = geometryValue.getGmlId();
            } else if (checkForFeatureGeometry(this)) {
                geometry = getGeometryFromFeature(this);
                domainExtent = getObservationConstellation().getFeatureOfInterest().getGmlId();
            }
            if (geometry != null) {
                cvDiscretePointCoverage.setDomainExtent("#" + geometry.getGeometryType() + "_" + domainExtent);
                Point point = null;
                if (geometry instanceof Point) {
                    point = (Point) geometry;
                } else {
                    point = geometry.getCentroid();
                }
                cvDiscretePointCoverage.setValue(new PointValuePair(point, value.getValue()));
            }
            super.setValue(new SingleObservationValue<>(value.getPhenomenonTime(), cvDiscretePointCoverage));
        }
    }

}
