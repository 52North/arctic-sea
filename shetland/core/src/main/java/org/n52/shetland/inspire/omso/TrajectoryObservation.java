/*
 * Copyright 2015-2022 52°North Initiative for Geospatial Open Source
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

import org.apache.commons.lang.ArrayUtils;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.gml.CodeWithAuthority;
import org.n52.shetland.ogc.om.AbstractObservationValue;
import org.n52.shetland.ogc.om.MultiObservationValues;
import org.n52.shetland.ogc.om.ObservationValue;
import org.n52.shetland.ogc.om.OmObservation;
import org.n52.shetland.ogc.om.SingleObservationValue;
import org.n52.shetland.ogc.om.StreamingValue;
import org.n52.shetland.ogc.om.TimeLocationValueTriple;
import org.n52.shetland.ogc.om.features.SfConstants;
import org.n52.shetland.ogc.om.features.samplingFeatures.AbstractSamplingFeature;
import org.n52.shetland.ogc.om.features.samplingFeatures.InvalidSridException;
import org.n52.shetland.ogc.om.features.samplingFeatures.SamplingFeature;
import org.n52.shetland.ogc.om.values.TLVTValue;
import org.n52.shetland.ogc.om.values.TVPValue;
import org.n52.shetland.util.IdGenerator;

import com.google.common.collect.Lists;

public class TrajectoryObservation
        extends AbstractInspireObservation {

    /**
     * constructor
     */
    public TrajectoryObservation() {
        super();
    }

    /**
     * constructor
     *
     * @param observation
     *            {@link OmObservation} to convert
     */
    public TrajectoryObservation(OmObservation observation) {
        super(observation);
        getObservationConstellation().setObservationType(InspireOMSOConstants.OBS_TYPE_TRAJECTORY_OBSERVATION);
        SamplingFeature sf = new SamplingFeature(
                getObservationConstellation().getFeatureOfInterest().getIdentifierCodeWithAuthority());
        sf.setFeatureType(SfConstants.SAMPLING_FEAT_TYPE_SF_SAMPLING_CURVE);
        getObservationConstellation().setFeatureOfInterest(sf);
        if (isSetSpatialFilteringProfileParameter()) {
            removeSpatialFilteringProfileParameter();
        }
        if (!isSetObservationID()) {
            setObservationID(IdGenerator.generate(toString()));
        }
    }

    @Override
    public OmObservation cloneTemplate() {
        SamplingFeature sf = new SamplingFeature(new CodeWithAuthority(""));
        sf.setFeatureType(SfConstants.SAMPLING_FEAT_TYPE_SF_SAMPLING_CURVE);
        getObservationConstellation().setFeatureOfInterest(sf);
        if (isSetSpatialFilteringProfileParameter()) {
            removeSpatialFilteringProfileParameter();
        }
        return cloneTemplate(new TrajectoryObservation());
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void setValue(ObservationValue<?> value) {
        if (value instanceof StreamingValue || value.getValue() instanceof TLVTValue) {
            super.setValue(value);
        } else {
            Geometry geometry = null;
            if (isSetSpatialFilteringProfileParameter()) {
                geometry = getSpatialFilteringProfileParameter().getValue().getValue();
            } else {
                if (getObservationConstellation().getFeatureOfInterest() instanceof AbstractSamplingFeature
                        && ((AbstractSamplingFeature) getObservationConstellation().getFeatureOfInterest())
                                .isSetGeometry()) {
                    geometry = ((AbstractSamplingFeature) getObservationConstellation().getFeatureOfInterest())
                            .getGeometry();
                }
            }
            TLVTValue tlvpValue = convertSingleValueToMultiValue((SingleObservationValue<?>) value, geometry);
            if (!tlvpValue.isSetUnit() && ((AbstractObservationValue<?>) value).isSetUnit()) {
                tlvpValue.setUnit(((AbstractObservationValue<?>) value).getUnit());
            }
            final MultiObservationValues<List<TimeLocationValueTriple>> multiValue =
                    new MultiObservationValues<List<TimeLocationValueTriple>>();
            multiValue.setValue(tlvpValue);
            if (!multiValue.isSetObservationID()) {
                if (value instanceof AbstractObservationValue
                        && ((AbstractObservationValue) value).isSetObservationID()) {
                    multiValue.setObservationID(((AbstractObservationValue) value).getObservationID());
                } else if (isSetObservationID()) {
                    multiValue.setObservationID(getObservationID());
                }
            }
            super.setValue(multiValue);
        }
    }

    @Override
    public void mergeWithObservation(OmObservation observation) {
        if (observation instanceof TrajectoryObservation) {
            mergeValues(observation.getValue());
        } else {
            super.mergeWithObservation(observation);
        }
    }

    protected boolean mergeValues(ObservationValue<?> observationValue) {
        if (observationValue.getValue() instanceof TLVTValue) {
            TLVTValue tlvtValue = (TLVTValue) observationValue.getValue();
            List<TimeLocationValueTriple> valuesToMerge = tlvtValue.getValue();
            // List<TimeLocationValueTriple> valuesToMerge =
            // (List<TimeLocationValueTriple>)((TLVTValue)observationValue.getValue()).getValue();
            ((TLVTValue) getValue().getValue()).addValues(valuesToMerge);
            checkForFeature(valuesToMerge);
            return true;

        } else {
            return super.mergeValues(observationValue);
        }
    }

    /**
     * Create geometry for featureOfInterest from
     * {@link TimeLocationValueTriple}s
     *
     * @param values
     *            The {@link TimeLocationValueTriple}s to check for
     *            featureOfInterest
     */
    private void checkForFeature(List<TimeLocationValueTriple> values) {
        AbstractFeature featureOfInterest = getObservationConstellation().getFeatureOfInterest();
        if (featureOfInterest instanceof AbstractSamplingFeature) {
            AbstractSamplingFeature sf = (AbstractSamplingFeature) featureOfInterest;
            Coordinate[] coords = getCoordinates(values);
            int srid = 0;
            if (sf.isSetGeometry()) {
                srid = sf.getGeometry().getSRID();
                coords = (Coordinate[]) ArrayUtils.addAll(sf.getGeometry().getCoordinates(), coords);
            } else {
                TimeLocationValueTriple next = values.iterator().next();
                if (next.isSetLocation()) {
                    srid = next.getLocation().getSRID();
                }
            }
            try {
                if (coords.length == 1) {
                    Point point = new GeometryFactory().createPoint(coords[0]);
                    point.setSRID(srid);
                    sf.setGeometry(point);
                } else if (coords.length > 1) {
                    LineString lineString = new GeometryFactory().createLineString(coords);
                    lineString.setSRID(srid);
                    sf.setGeometry(lineString);
                }
            } catch (InvalidSridException e) {
                // TODO
            }
        }
    }

    /**
     * Get {@link Coordinate}s from the {@link TimeLocationValueTriple}s
     *
     * @param values
     *            The {@link TimeLocationValueTriple}s to get {@link Coordinate}
     *            s from
     * @return The coordinates
     */
    private Coordinate[] getCoordinates(List<TimeLocationValueTriple> values) {
        List<Coordinate> coords = Lists.newArrayList();
        for (TimeLocationValueTriple timeLocationValueTriple : values) {
            if (timeLocationValueTriple.isSetLocation()) {
                coords.add(timeLocationValueTriple.getLocation().getCoordinate());
            }
        }
        return coords.toArray(new Coordinate[0]);
    }

    /**
     * Convert {@link SingleObservationValue} to {@link TVPValue}
     *
     * @param singleValue
     *            Single observation value
     * @return Converted TVPValue value
     */
    private TLVTValue convertSingleValueToMultiValue(final SingleObservationValue<?> singleValue, Geometry geom) {
        final TLVTValue tlvpValue = new TLVTValue();
        tlvpValue.setUnit(singleValue.getValue().getUnit());
        final TimeLocationValueTriple timeLocationValueTriple =
                new TimeLocationValueTriple(singleValue.getPhenomenonTime(), singleValue.getValue(), geom);
        tlvpValue.addValue(timeLocationValueTriple);
        return tlvpValue;
    }
}
