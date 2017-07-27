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
package org.n52.svalbard.encode;

import java.io.OutputStream;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.xmlbeans.XmlObject;
import org.n52.shetland.inspire.omso.InspireOMSOConstants;
import org.n52.shetland.inspire.omso.TrajectoryObservation;
import org.n52.shetland.ogc.om.AbstractObservationValue;
import org.n52.shetland.ogc.om.MultiObservationValues;
import org.n52.shetland.ogc.om.ObservationValue;
import org.n52.shetland.ogc.om.OmObservation;
import org.n52.shetland.ogc.om.SingleObservationValue;
import org.n52.shetland.ogc.om.TimeLocationValueTriple;
import org.n52.shetland.ogc.om.values.CategoryValue;
import org.n52.shetland.ogc.om.values.CountValue;
import org.n52.shetland.ogc.om.values.QuantityValue;
import org.n52.shetland.ogc.om.values.TLVTValue;
import org.n52.shetland.util.JavaHelper;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.util.CodingHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;

import eu.europa.ec.inspire.schemas.omso.x30.TrajectoryObservationType;
import net.opengis.om.x20.OMObservationType;
import net.opengis.waterml.x20.CategoricalTimeseriesDocument;
import net.opengis.waterml.x20.CategoricalTimeseriesType;
import net.opengis.waterml.x20.DefaultCategoricalTVPMetadataType;
import net.opengis.waterml.x20.DefaultTVPCategoricalMetadataDocument;
import net.opengis.waterml.x20.DefaultTVPMeasurementMetadataDocument;
import net.opengis.waterml.x20.MeasurementTimeseriesDocument;
import net.opengis.waterml.x20.MeasurementTimeseriesType;
import net.opengis.waterml.x20.TVPDefaultMetadataPropertyType;
import net.opengis.waterml.x20.TVPMeasurementMetadataType;

/**
 * {@link Encoder} implementation for {@link TrajectoryObservation} to
 * {@link TrajectoryObservationType}
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public class TrajectoryObservationTypeEncoder
        extends AbstractOmInspireEncoder {

    private static final Logger LOGGER = LoggerFactory.getLogger(TrajectoryObservationTypeEncoder.class);
    private static final String TIMESERIES_PREFIX = "timeseries.";
    private static final Set<EncoderKey> ENCODER_KEYS =
            CodingHelper.encoderKeysForElements(InspireOMSOConstants.NS_OMSO_30, TrajectoryObservation.class);

    public TrajectoryObservationTypeEncoder() {
        LOGGER.debug("Encoder for the following keys initialized successfully: {}!",
                Joiner.on(", ").join(ENCODER_KEYS));
    }

    @Override
    public Set<EncoderKey> getKeys() {
        return Collections.unmodifiableSet(ENCODER_KEYS);
    }

    @Override
    public XmlObject encode(Object element, EncodingContext ec) throws EncodingException {
        return super.encode(element, ec);
    }

    @Override
    public void encode(Object objectToEncode, OutputStream outputStream, EncodingContext context)
            throws EncodingException {
        super.encode(objectToEncode, outputStream, context);
    }

    @Override
    protected XmlObject createResult(OmObservation sosObservation) throws EncodingException {
        return encodeResult(sosObservation.getValue());
    }

    @Override
    protected XmlObject encodeResult(ObservationValue<?> observationValue) throws EncodingException {
        if (observationValue instanceof SingleObservationValue
                && observationValue.getValue() instanceof TimeLocationValueTriple) {
            if (observationValue.getValue().getValue() instanceof QuantityValue
                    || observationValue.getValue().getValue() instanceof CountValue) {
                return createMeasurementTimeseries((AbstractObservationValue<?>) observationValue);
            } else if (observationValue.getValue().getValue() instanceof CategoryValue) {
                return createCategoricalTimeseries((AbstractObservationValue<?>) observationValue);
            }
        } else if (observationValue instanceof MultiObservationValues) {
            if (observationValue.getValue() instanceof TLVTValue) {
                TimeLocationValueTriple value = ((TLVTValue) observationValue.getValue())
                        .getValue().iterator().next();
                if (value.getValue() instanceof QuantityValue || value.getValue() instanceof CountValue) {
                    return createMeasurementTimeseries((AbstractObservationValue<?>) observationValue);
                } else if (value.getValue() instanceof CategoryValue) {
                    return createCategoricalTimeseries((AbstractObservationValue<?>) observationValue);
                }
            }
        }
        return null;
    }

    @Override
    protected String getObservationType() {
        return InspireOMSOConstants.OBS_TYPE_TRAJECTORY_OBSERVATION;
    }

    protected OMObservationType createOmObservationType() {
        return TrajectoryObservationType.Factory.newInstance(getXmlOptions());
    }

    /**
     * Encode {@link AbstractObservationValue} to
     * {@link MeasurementTimeseriesDocument}
     *
     * @param observationValue
     *            The {@link AbstractObservationValue} to encode
     * @return The encoded {@link AbstractObservationValue}
     * @throws EncodingException
     *             If an error occurs
     */
    private XmlObject createMeasurementTimeseries(AbstractObservationValue<?> observationValue)
            throws EncodingException {
        MeasurementTimeseriesDocument measurementTimeseriesDoc = MeasurementTimeseriesDocument.Factory.newInstance();
        MeasurementTimeseriesType measurementTimeseries = measurementTimeseriesDoc.addNewMeasurementTimeseries();
        if (!observationValue.isSetObservationID()) {
            observationValue.setObservationID(JavaHelper.generateID(observationValue.toString()));
        }
        measurementTimeseries.setId(TIMESERIES_PREFIX + observationValue.getObservationID());
        measurementTimeseries.addNewMetadata().addNewTimeseriesMetadata().addNewTemporalExtent()
                .setHref("#" + observationValue.getPhenomenonTime().getGmlId());

        TVPDefaultMetadataPropertyType xbMetaComponent = measurementTimeseries.addNewDefaultPointMetadata();

        DefaultTVPMeasurementMetadataDocument xbDefMeasureMetaComponent =
                DefaultTVPMeasurementMetadataDocument.Factory.newInstance();
        TVPMeasurementMetadataType defaultTVPMeasurementMetadata =
                xbDefMeasureMetaComponent.addNewDefaultTVPMeasurementMetadata();
        defaultTVPMeasurementMetadata.addNewInterpolationType()
                .setHref("http://www.opengis.net/def/timeseriesType/WaterML/2.0/continuous");

        xbDefMeasureMetaComponent.getDefaultTVPMeasurementMetadata().getInterpolationType().setTitle("Instantaneous");
        String unit = null;
        if (observationValue instanceof SingleObservationValue) {
            SingleObservationValue<?> singleObservationValue = (SingleObservationValue<?>) observationValue;
            unit = singleObservationValue.getValue().getUnit();
            if (observationValue.getValue() instanceof TimeLocationValueTriple) {

                measurementTimeseries.addNewPoint().addNewMeasurementTVP()
                        .set(encodeTLVT((TimeLocationValueTriple) observationValue.getValue()));
            }
        } else if (observationValue instanceof MultiObservationValues) {
            MultiObservationValues<?> multiObservationValue = (MultiObservationValues<?>) observationValue;
            if (multiObservationValue.getValue() instanceof TLVTValue) {
                TLVTValue tlvtValue = (TLVTValue) multiObservationValue.getValue();
                List<TimeLocationValueTriple> timeLocationValueTriples = tlvtValue.getValue();
                unit = tlvtValue.getUnit();
                int counter = 0;
                for (TimeLocationValueTriple timeLocationValueTriple : timeLocationValueTriples) {
                    timeLocationValueTriple.getLocation()
                            .setUserData(getUserObject(observationValue.getObservationID(), counter));
                    measurementTimeseries.addNewPoint().addNewMeasurementTVP()
                            .set(encodeTLVT(timeLocationValueTriple));
                    counter++;
                }
            }
        }
        if (unit != null && !unit.isEmpty()) {
            defaultTVPMeasurementMetadata.addNewUom().setCode(unit);
        }

        xbMetaComponent.set(xbDefMeasureMetaComponent);
        return measurementTimeseriesDoc;
    }

    /**
     * Encode {@link TimeLocationValueTriple}
     *
     * @param value
     *            The {@link TimeLocationValueTriple} to encode
     * @return Encoded {@link TimeLocationValueTriple}
     * @throws EncodingException
     *             If an error occurs
     */
    private XmlObject encodeTLVT(TimeLocationValueTriple value) throws EncodingException {
        return encodeInspireOMSO(value);
    }

    /**
     * Encode {@link AbstractObservationValue} to
     * {@link CategoricalTimeseriesDocument}
     *
     * @param observationValue
     *            The {@link AbstractObservationValue} to encode
     * @return The encoded {@link AbstractObservationValue}
     * @throws EncodingException
     *             If an error occurs
     */
    private XmlObject createCategoricalTimeseries(AbstractObservationValue<?> observationValue)
            throws EncodingException {
        CategoricalTimeseriesDocument categoricalTimeseriesDoc = CategoricalTimeseriesDocument.Factory.newInstance();
        CategoricalTimeseriesType categoricalTimeseries = categoricalTimeseriesDoc.addNewCategoricalTimeseries();
        categoricalTimeseries.setId(TIMESERIES_PREFIX + observationValue.getObservationID());
        categoricalTimeseries.addNewMetadata().addNewTimeseriesMetadata().addNewTemporalExtent()
                .setHref("#" + observationValue.getPhenomenonTime().getGmlId());

        TVPDefaultMetadataPropertyType xbMetaComponent = categoricalTimeseries.addNewDefaultPointMetadata();

        DefaultTVPCategoricalMetadataDocument xbDefCateMetaComponent =
                DefaultTVPCategoricalMetadataDocument.Factory.newInstance();
        DefaultCategoricalTVPMetadataType defaultTVPCateMetadata =
                xbDefCateMetaComponent.addNewDefaultTVPCategoricalMetadata();
        String unit = null;
        if (observationValue instanceof SingleObservationValue) {
            SingleObservationValue<?> singleObservationValue = (SingleObservationValue<?>) observationValue;
            unit = singleObservationValue.getValue().getUnit();
            if (observationValue.getValue() instanceof TimeLocationValueTriple) {
                categoricalTimeseries.addNewPoint().addNewCategoricalTVP()
                        .set(encodeTLVT((TimeLocationValueTriple) observationValue.getValue()));
            }
        } else if (observationValue instanceof MultiObservationValues) {
            MultiObservationValues<?> multiObservationValue = (MultiObservationValues<?>) observationValue;
            if (multiObservationValue.getValue() instanceof TLVTValue) {
                TLVTValue tlvtValue = (TLVTValue) multiObservationValue.getValue();
                List<TimeLocationValueTriple> timeLocationValueTriples = tlvtValue.getValue();
                unit = tlvtValue.getUnit();
                int counter = 0;
                for (TimeLocationValueTriple timeLocationValueTriple : timeLocationValueTriples) {
                    timeLocationValueTriple.getLocation()
                            .setUserData(getUserObject(observationValue.getObservationID(), counter));
                    categoricalTimeseries.addNewPoint().addNewCategoricalTVP()
                            .set(encodeTLVT(timeLocationValueTriple));
                    counter++;
                }
            }
        }
        if (unit != null && !unit.isEmpty()) {
            defaultTVPCateMetadata.setCodeSpace(unit);
        }

        xbMetaComponent.set(xbDefCateMetaComponent);
        return categoricalTimeseriesDoc;
    }

    /**
     * @param observationID
     *            Observation id
     * @param counter
     *            Observation counter
     * @return {@link EncodingContext}
     */
    private EncodingContext getUserObject(String observationID, int counter) {
        return EncodingContext.of(XmlBeansEncodingFlags.GMLID, observationID + "_" + counter);
    }

    protected XmlObject encodeInspireOMSO(Object o) throws EncodingException {
        return encodeObjectToXml(InspireOMSOConstants.NS_OMSO_30, o);
    }

    protected XmlObject encodeInspireOMSO(Object o, EncodingContext ec) throws EncodingException {
        return encodeObjectToXml(InspireOMSOConstants.NS_OMSO_30, o, ec);
    }

}
