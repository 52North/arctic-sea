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

import java.util.List;

import org.n52.shetland.ogc.gml.CodeWithAuthority;
import org.n52.shetland.ogc.om.AbstractObservationValue;
import org.n52.shetland.ogc.om.ObservationValue;
import org.n52.shetland.ogc.om.OmObservation;
import org.n52.shetland.ogc.om.PointValuePair;
import org.n52.shetland.ogc.om.SingleObservationValue;
import org.n52.shetland.ogc.om.features.SfConstants;
import org.n52.shetland.ogc.om.features.samplingFeatures.AbstractSamplingFeature;
import org.n52.shetland.ogc.om.features.samplingFeatures.InvalidSridException;
import org.n52.shetland.ogc.om.features.samplingFeatures.SamplingFeature;
import org.n52.shetland.ogc.om.values.MultiPointCoverage;
import org.n52.shetland.ogc.ows.exception.CodedException;
import org.n52.shetland.ogc.ows.exception.NoApplicableCodeException;
import org.n52.shetland.util.CollectionHelper;
import org.n52.shetland.util.JTSHelper;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

/**
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since
 *
 */
public class MultiPointObservation
        extends AbstractInspireObservation {

    /**
     * consturctor
     */
    public MultiPointObservation() {
        super();
    }

    /**
     * constructor
     *
     * @param observation
     *            {@link OmObservation} to convert
     * @throws CodedException If an error occurs
     */
    public MultiPointObservation(OmObservation observation) throws CodedException {
        super(observation);
        getObservationConstellation().setObservationType(InspireOMSOConstants.OBS_TYPE_MULTI_POINT_OBSERVATION);
        if (getValue().getValue() instanceof MultiPointCoverage) {
            SamplingFeature samplingFeature = new SamplingFeature(new CodeWithAuthority(""));
            samplingFeature.setFeatureType(SfConstants.SAMPLING_FEAT_TYPE_SF_SAMPLING_SURFACE);
            samplingFeature.setEncode(true);
            try {
                samplingFeature.setGeometry(getEnvelope(((MultiPointCoverage) getValue().getValue()).getValue()));
            } catch (InvalidSridException e) {
                throw new NoApplicableCodeException().causedBy(e);
            }
            getObservationConstellation().setFeatureOfInterest(samplingFeature);
        }
    }

    @Override
    public OmObservation cloneTemplate() {
        if (getObservationConstellation().getFeatureOfInterest() instanceof AbstractSamplingFeature) {
            ((AbstractSamplingFeature) getObservationConstellation().getFeatureOfInterest()).setEncode(true);
        }
        return cloneTemplate(new MultiPointObservation());
    }

    @Override
    public void setValue(ObservationValue<?> value) {
        if (value.getValue() instanceof MultiPointCoverage) {
            super.setValue(value);
        } else {
            MultiPointCoverage multiPointCoverage = new MultiPointCoverage(getObservationID());
            multiPointCoverage.setUnit(((AbstractObservationValue<?>) value).getUnit());
            multiPointCoverage.addValue(new PointValuePair(getPoint(), value.getValue()));
            super.setValue(new SingleObservationValue<>(value.getPhenomenonTime(), multiPointCoverage));
        }
    }

    protected boolean mergeValues(ObservationValue<?> observationValue) {
        if (observationValue.getValue() instanceof MultiPointCoverage) {
            List<PointValuePair> valuesToMerge = ((MultiPointCoverage) observationValue.getValue()).getValue();
            ((MultiPointCoverage) getValue().getValue()).addValues(valuesToMerge);
            if (getObservationConstellation().getFeatureOfInterest() instanceof AbstractSamplingFeature) {
                if (((AbstractSamplingFeature) getObservationConstellation().getFeatureOfInterest()).isSetGeometry()) {
                    try {
                        ((AbstractSamplingFeature) getObservationConstellation().getFeatureOfInterest())
                                .setGeometry(getEnvelope(((MultiPointCoverage) getValue().getValue()).getValue()));
                    } catch (InvalidSridException e) {
                        // TODO
                    }
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Get the point from samplingGeometry or featureOfInterest
     *
     * @return The {@link Point}
     */
    private Point getPoint() {
        Point point = null;
        if (isSetSpatialFilteringProfileParameter()) {
            Geometry geometry = getSpatialFilteringProfileParameter().getValue().getValue();
            point = geometry.getInteriorPoint();
            point.setSRID(geometry.getSRID());
        } else {
            if (getObservationConstellation().getFeatureOfInterest() instanceof AbstractSamplingFeature
                    && ((AbstractSamplingFeature) getObservationConstellation().getFeatureOfInterest())
                            .isSetGeometry()) {
                Geometry geometry =
                        ((AbstractSamplingFeature) getObservationConstellation().getFeatureOfInterest()).getGeometry();
                point = geometry.getInteriorPoint();
                point.setSRID(geometry.getSRID());
            }
        }
        return point;
    }

    /**
     * Get the envelope from {@link PointValuePair}s {@link List}
     *
     * @param pointValuePairs
     *            The {@link PointValuePair}s to get the envelope from
     * @return The envelope of the {@link PointValuePair}s
     */
    private Geometry getEnvelope(List<PointValuePair> pointValuePairs) {
        Envelope envelope = new Envelope();
        GeometryFactory factory = null;
        int srid = 4326;
        if (CollectionHelper.isNotEmpty(pointValuePairs)) {
            for (PointValuePair pointValuePair : pointValuePairs) {
                if (factory == null && pointValuePair.getPoint() != null) {
                    factory = pointValuePair.getPoint().getFactory();
                }
                if (pointValuePair.getPoint().getSRID() > 0) {
                    srid = pointValuePair.getPoint().getSRID();
                }
                envelope.expandToInclude(pointValuePair.getPoint().getEnvelopeInternal());
            }
        } else {
            if (isSetSpatialFilteringProfileParameter()) {
                Geometry geometry = getSpatialFilteringProfileParameter().getValue().getValue();
                if (geometry != null) {
                    if (factory == null) {
                        factory = geometry.getFactory();
                    }
                    if (geometry.getSRID() > 0) {
                        srid = geometry.getSRID();
                    }
                    envelope.expandToInclude(geometry.getEnvelopeInternal());
                }
            } else {
                if (getObservationConstellation().getFeatureOfInterest() instanceof AbstractSamplingFeature
                        && ((AbstractSamplingFeature) getObservationConstellation().getFeatureOfInterest())
                                .isSetGeometry()) {
                    Geometry geometry =
                            ((AbstractSamplingFeature) getObservationConstellation().getFeatureOfInterest())
                                    .getGeometry();
                    if (geometry != null) {
                        if (factory == null) {
                            factory = geometry.getFactory();
                        }
                        if (geometry.getSRID() > 0) {
                            srid = geometry.getSRID();
                        }
                        envelope.expandToInclude(geometry.getEnvelopeInternal());
                    }
                }
            }
        }
        if (factory == null) {
            factory = JTSHelper.getGeometryFactoryForSRID(srid);
        }
        Geometry geometry = factory.toGeometry(envelope);
        geometry.setSRID(srid);
        return geometry;
    }
}
