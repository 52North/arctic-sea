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
package org.n52.svalbard.write;

import java.io.OutputStream;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;
import javax.xml.stream.XMLStreamException;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.xmlbeans.XmlObject;
import org.n52.shetland.ogc.gml.CodeType;
import org.n52.shetland.ogc.gml.GmlConstants;
import org.n52.shetland.ogc.gml.ReferenceType;
import org.n52.shetland.ogc.om.MultiObservationValues;
import org.n52.shetland.ogc.om.ObservationValue;
import org.n52.shetland.ogc.om.OmConstants;
import org.n52.shetland.ogc.om.OmObservableProperty;
import org.n52.shetland.ogc.om.OmObservation;
import org.n52.shetland.ogc.om.SingleObservationValue;
import org.n52.shetland.ogc.om.StreamingValue;
import org.n52.shetland.ogc.om.TimeValuePair;
import org.n52.shetland.ogc.om.series.MeasurementTimeseriesMetadata;
import org.n52.shetland.ogc.om.series.tsml.TimeseriesMLConstants;
import org.n52.shetland.ogc.om.series.tsml.TimeseriesMLConstants.InterpolationType;
import org.n52.shetland.ogc.om.values.CountValue;
import org.n52.shetland.ogc.om.values.ProfileValue;
import org.n52.shetland.ogc.om.values.QuantityValue;
import org.n52.shetland.ogc.om.values.SweDataArrayValue;
import org.n52.shetland.ogc.om.values.TVPValue;
import org.n52.shetland.ogc.om.values.TextValue;
import org.n52.shetland.ogc.om.values.Value;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.shetland.ogc.swe.SweConstants;
import org.n52.shetland.ogc.swe.simpleType.SweQuality;
import org.n52.shetland.ogc.swe.simpleType.SweQualityHolder;
import org.n52.shetland.util.DateTimeFormatException;
import org.n52.shetland.w3c.W3CConstants;
import org.n52.svalbard.encode.EncodingContext;
import org.n52.svalbard.encode.WmlTmlHelper;
import org.n52.svalbard.encode.XmlBeansEncodingFlags;
import org.n52.svalbard.encode.exception.EncodingException;

import com.google.common.base.Strings;

/**
 * TODO(specki): update javadoc Implementation of
 * {@link AbstractOmV20XmlStreamWriter} to write WaterML 2.0 encoded
 * {@link OmObservation}s to stream
 *
 * @since 1.0.0
 *
 */
public class TsmlTVPEncoderv10XmlStreamWriter extends AbstractOmV20XmlStreamWriter implements WmlTmlHelper {
    public TsmlTVPEncoderv10XmlStreamWriter(EncodingContext context, OutputStream outputStream, OmObservation element)
            throws XMLStreamException {
        super(context, outputStream, element);
    }

    @Override
    protected void writeResult() throws XMLStreamException, EncodingException {
        start(OmConstants.QN_OM_20_RESULT);
        namespace(TimeseriesMLConstants.NS_TSML_10_PREFIX, TimeseriesMLConstants.NS_TSML_10);
        start(TimeseriesMLConstants.QN_MEASUREMENT_TIMESERIES);
        OmObservation observation = getElement();
        attr(GmlConstants.QN_ID_32, "timeseries." + observation.getObservationID());
        writeMeasurementTimeseriesMLMetadata(observation);
        if (observation.getValue() instanceof SingleObservationValue) {
            SingleObservationValue<?> observationValue = (SingleObservationValue<?>) observation.getValue();
            writeDefaultPointMetadata(observationValue, observationValue.getValue()
                    .getUnit());
            if (checkSweDataArray(observationValue.getValue())) {
                SweDataArrayValue sweDataArrayValue = (SweDataArrayValue) observationValue.getValue();
                for (List<String> list : sweDataArrayValue.getValue()
                        .getValues()) {
                    for (int i = 0; i < list.size(); i = i + 2) {
                        writePoint(list.get(i), list.get(i + 1));
                        close();
                    }
                }
            } else {
                String time = getTimeString(observationValue.getPhenomenonTime());
                writePoint(time, observation.getValue()
                        .getValue());
                close();
            }
        } else if (observation.getValue() instanceof MultiObservationValues) {
            // XML streaming to client
            MultiObservationValues<?> observationValue = (MultiObservationValues<?>) observation.getValue();
            writeDefaultPointMetadata(observationValue, observationValue.getValue()
                    .getUnit());
            TVPValue tvpValue = (TVPValue) observationValue.getValue();
            List<TimeValuePair> timeValuePairs = tvpValue.getValue();
            for (TimeValuePair timeValuePair : timeValuePairs) {
                if (timeValuePair != null) {
                    writePoint(getTimeString(timeValuePair.getTime()), timeValuePair.getValue());
                }
            }
            close();
        } else if (observation.getValue() instanceof StreamingValue) {
            // Database streaming + XML streaming to client
            StreamingValue<?> observationValue = (StreamingValue<?>) observation.getValue();
            if (observationValue.isSetUnit()) {
                writeDefaultPointMetadata(observationValue, observationValue.getUnit());
            } else if (observation.getObservationConstellation()
                    .getObservableProperty() instanceof OmObservableProperty
                    && ((OmObservableProperty) observation.getObservationConstellation()
                            .getObservableProperty()).isSetUnit()) {
                writeDefaultPointMetadata(observationValue,
                        ((OmObservableProperty) observation.getObservationConstellation()
                                .getObservableProperty()).getUnit());
            } else {
                writeDefaultPointMetadata(observationValue, null);
            }
            try {
                while (observationValue.hasNext()) {
                    TimeValuePair timeValuePair = observationValue.nextValue();
                    if (timeValuePair != null) {
                        writePoint(getTimeString(timeValuePair.getTime()), timeValuePair.getValue());
                    }
                }
            } catch (DateTimeFormatException | OwsExceptionReport e) {
                throw new EncodingException(e);
            }
            close();
        } else {
            super.writeResult();
        }
    }

    @Override
    protected void writeAddtitionalNamespaces() throws XMLStreamException {
        namespace(TimeseriesMLConstants.NS_TSML_10_PREFIX, TimeseriesMLConstants.NS_TSML_10);
    }

    @Override
    protected void checkAndWriteIdentifier() throws EncodingException, XMLStreamException {
        if (getElement().getObservationConstellation()
                .isSetIdentifier()) {
            writeIdentifier(getElement().getObservationConstellation()
                    .getIdentifierCodeWithAuthority());
        } else {
            super.checkAndWriteIdentifier();
        }
    }

    @Override
    protected void checkAndWriteName() throws EncodingException, XMLStreamException {
        if (getElement().getObservationConstellation()
                .isSetName()) {
            for (CodeType name : getElement().getObservationConstellation()
                    .getName()) {
                writeName(name);
            }
        } else {
            super.checkAndWriteName();
        }
    }

    @Override
    protected void checkAndWriteDescription() throws XMLStreamException {
        if (getElement().getObservationConstellation()
                .isSetDescription()) {
            writeDescription(getElement().getObservationConstellation()
                    .getDescription());
        } else {
            super.checkAndWriteDescription();
        }
    }

    /**
     * Close written wml:MeasurementTimeseriesML and om:result tags
     *
     * @throws XMLStreamException
     *             If an error occurs when writing to stream
     */
    private void close() throws XMLStreamException {
        end(TimeseriesMLConstants.QN_MEASUREMENT_TIMESERIES);
        end(OmConstants.QN_OM_20_RESULT);
    }

    /**
     * Write timeseries metadata to stream
     *
     * @param o
     *            the observation
     *
     * @throws XMLStreamException
     *             If an error occurs when writing to stream
     */
    private void writeMeasurementTimeseriesMLMetadata(OmObservation o) throws XMLStreamException {
        start(TimeseriesMLConstants.QN_METADATA);
        start(TimeseriesMLConstants.QN_MEASUREMENT_TIMESERIES_METADATA);
        empty(TimeseriesMLConstants.QN_TEMPORAL_EXTENT);
        addXlinkHrefAttr("#" + o.getPhenomenonTime()
                .getGmlId());
        if (o.isSetValue() && o.getValue()
                .isSetMetadata()
                && o.getValue()
                        .getMetadata()
                        .isSetTimeseriesMetadata()
                && o.getValue()
                        .getMetadata()
                        .getTimeseriesmetadata() instanceof MeasurementTimeseriesMetadata) {
            start(TimeseriesMLConstants.QN_CUMULATIVE);
            chars(Boolean.toString(((MeasurementTimeseriesMetadata) o.getValue()
                    .getMetadata()
                    .getTimeseriesmetadata()).isCumulative()));
            endInline(TimeseriesMLConstants.QN_CUMULATIVE);
        }
        end(TimeseriesMLConstants.QN_MEASUREMENT_TIMESERIES_METADATA);
        end(TimeseriesMLConstants.QN_METADATA);
    }

    /**
     * Write wml:defaultPointMetadata to stream
     *
     * @param value
     *            the observation value
     * @param unit
     *            the unit
     *
     * @throws XMLStreamException
     *             If an error occurs when writing to stream
     */
    private void writeDefaultPointMetadata(@Nullable ObservationValue<?> value, @Nullable String unit)
            throws XMLStreamException {
        start(TimeseriesMLConstants.QN_DEFAULT_POINT_METADATA);
        start(TimeseriesMLConstants.QN_DEFAULT_TVP_MEASUREMENT_METADATA);
        writeUOM(unit);
        writeInterpolationType(value);
        writeAggregationDuration(value);
        end(TimeseriesMLConstants.QN_DEFAULT_TVP_MEASUREMENT_METADATA);
        end(TimeseriesMLConstants.QN_DEFAULT_POINT_METADATA);
    }

    /**
     * Write UOM attribute to stream
     *
     * @param code
     *            UOM code
     *
     * @throws XMLStreamException
     *             If an error occurs when writing to stream
     */
    private void writeUOM(@Nullable String code) throws XMLStreamException {
        if (code != null && !code.isEmpty()) {
            empty(TimeseriesMLConstants.UOM);
            attr("code", code);
        }
    }

    /**
     * Write wml:interpolationType to stream
     *
     * @param value
     *            the value
     *
     * @throws XMLStreamException
     *             If an error occurs when writing to stream
     */
    private void writeInterpolationType(@Nullable ObservationValue<?> value) throws XMLStreamException {
        empty(TimeseriesMLConstants.QN_INTERPOLATION_TYPE);
        if (value != null && value.isSetMetadata() && value.getDefaultPointMetadata()
                .isSetDefaultTVPMeasurementMetadata() && value.getDefaultPointMetadata()
                        .getDefaultTVPMeasurementMetadata()
                        .isSetInterpolationType()) {
            InterpolationType interpolationtype = (InterpolationType) value.getDefaultPointMetadata()
                    .getDefaultTVPMeasurementMetadata()
                    .getInterpolationtype();
            addXlinkHrefAttr(interpolationtype.getIdentifier());
            addXlinkTitleAttr(interpolationtype.getTitle());
        } else {
            addXlinkHrefAttr("http://www.opengis.net/def/timeseries/InterpolationCode/continuous");
            addXlinkTitleAttr("Instantaneous");
        }
    }

    private void writeAggregationDuration(ObservationValue<?> value) throws XMLStreamException {
        if (value != null && value.isSetMetadata() && value.getDefaultPointMetadata()
                .isSetDefaultTVPMeasurementMetadata() && value.getDefaultPointMetadata()
                        .getDefaultTVPMeasurementMetadata()
                        .isSetAggregationDuration()) {
            start(TimeseriesMLConstants.QN_AGGREGATION_DURATION);
            chars(value.getDefaultPointMetadata()
                    .getDefaultTVPMeasurementMetadata()
                    .getAggregationDuration());
            end(TimeseriesMLConstants.QN_AGGREGATION_DURATION);
        }
    }

    private void writePoint(String time, Value<?> value) throws XMLStreamException, EncodingException {
        if (value != null) {
            if (value instanceof QuantityValue) {
                QuantityValue quantityValue = (QuantityValue) value;
                writePoint(time, quantityValue);
            } else if (value instanceof ProfileValue) {
                ProfileValue gwglcValue = (ProfileValue) value;
                if (gwglcValue.isSetValue()) {
                    writePoint(time, gwglcValue.getValue()
                            .iterator()
                            .next()
                            .getSimpleValue());
                }
            } else if (value instanceof CountValue) {
                CountValue countValue = (CountValue) value;
                writePoint(time, countValue);
            } else if (value instanceof TextValue) {
                TextValue textValue = (TextValue) value;
                String nonXmlEscapedText = textValue.getValue();
                writePoint(time, StringEscapeUtils.escapeXml(nonXmlEscapedText));
            }
        } else {
            writePoint(time, "");
        }
    }

    private void writePoint(String time, QuantityValue value) throws XMLStreamException, EncodingException {
        if (value.isSetValue()) {
            writePoint(time, value.getValue()
                    .toPlainString());
        } else {
            if (value.isSetQuality()) {
                writePointWithQuality(time, value.getQuality());
            } else {
                writePoint(time, "");
            }
        }
    }

    private void writePoint(String time, CountValue value) throws XMLStreamException {
        writePoint(time, value.isSetValue() ? Integer.toString(value.getValue()) : "");
    }

    /**
     * Write wml:point to stream
     *
     * @param time
     *            time as {@link String}
     * @param value
     *            value as {@link String}
     *
     * @throws XMLStreamException
     *             If an error occurs when writing to stream
     */
    private void writePoint(String time, String value) throws XMLStreamException {
        if (!Strings.isNullOrEmpty(time)) {
            start(TimeseriesMLConstants.QN_POINT);
            writeMeasurementTVP(time, value);
            end(TimeseriesMLConstants.QN_POINT);
        }
    }

    /**
     * Write wml:MeasurementTVP to stream
     *
     * @param time
     *            time as {@link String}
     * @param value
     *            value as {@link String}
     *
     * @throws XMLStreamException
     *             If an error occurs when writing to stream
     */
    private void writeMeasurementTVP(String time, String value) throws XMLStreamException {
        start(TimeseriesMLConstants.QN_MEASUREMENT_TVP);
        writeTime(time);
        writeValue(value);
        end(TimeseriesMLConstants.QN_MEASUREMENT_TVP);
    }

    /**
     * Write wml:time to stream
     *
     * @param time
     *            time to write
     *
     * @throws XMLStreamException
     *             If an error occurs when writing to stream
     */
    private void writeTime(String time) throws XMLStreamException {
        start(TimeseriesMLConstants.QN_TIME);
        chars(time);
        endInline(TimeseriesMLConstants.QN_TIME);
    }

    /**
     * Write wml:value to stream
     *
     * @param value
     *            value to write
     *
     * @throws XMLStreamException
     *             If an error occurs when writing to stream
     */
    private void writeValue(String value) throws XMLStreamException {
        if (!Strings.isNullOrEmpty(value)) {
            start(TimeseriesMLConstants.QN_VALUE);
            chars(value);
            endInline(TimeseriesMLConstants.QN_VALUE);
        } else {
            writeEmptyValue();
            writeValueMetadataMissing();
        }
    }

    private void writePointWithQuality(String time, SweQualityHolder qualityHolder)
            throws XMLStreamException, EncodingException {
        start(TimeseriesMLConstants.QN_POINT);
        start(TimeseriesMLConstants.QN_MEASUREMENT_TVP);
        writeTime(time);
        writeEmptyValue();
        writeValueMetadata(qualityHolder);
        end(TimeseriesMLConstants.QN_MEASUREMENT_TVP);
        end(TimeseriesMLConstants.QN_POINT);
    }

    private void writeEmptyValue() throws XMLStreamException {
        empty(TimeseriesMLConstants.QN_VALUE);
        attr(W3CConstants.QN_XSI_NIL, "true");
    }

    /**
     * Write missing value metadata to stream
     *
     * @throws XMLStreamException
     *             If an error occurs when writing to stream
     */
    private void writeValueMetadata(SweQualityHolder qualityHolder) throws XMLStreamException, EncodingException {
        start(TimeseriesMLConstants.QN_METADATA);
        start(TimeseriesMLConstants.QN_TVP_MEASUREMENT_METADATA);
        writeQualifier(qualityHolder);
        writeCensoredReason(qualityHolder);
        endInline(TimeseriesMLConstants.QN_TVP_MEASUREMENT_METADATA);
        endInline(TimeseriesMLConstants.QN_METADATA);

    }

    private void writeQualifier(SweQualityHolder qualityHolder) throws EncodingException, XMLStreamException {
        if (qualityHolder.isSetQuality()) {
            for (SweQuality quality : qualityHolder.getQuality()) {
                XmlObject createdQuality = (XmlObject) getEncoder(SweConstants.NS_SWE_20, quality).encode(quality,
                        EncodingContext.of(XmlBeansEncodingFlags.DOCUMENT, true));
                if (createdQuality != null) {
                    start(TimeseriesMLConstants.QN_QUALIFIER);
                    writeXmlObject(createdQuality);
                    end(TimeseriesMLConstants.QN_QUALIFIER);
                }
            }
        }
    }

    private void writeCensoredReason(SweQualityHolder qualityHolder) throws XMLStreamException {
        if (qualityHolder.isSetReferences() && qualityHolder.getReferences()
                .containsKey(TimeseriesMLConstants.EN_CENSORED_REASON)) {
            ReferenceType reference = qualityHolder.getReferences()
                    .get(TimeseriesMLConstants.EN_CENSORED_REASON);
            empty(TimeseriesMLConstants.QN_CENSORED_REASON);
            if (reference.isSetHref()) {
                attr(W3CConstants.QN_XLINK_HREF, reference.getHref());
            }
            if (reference.isSetTitle()) {
                attr(W3CConstants.QN_XLINK_TITLE, reference.getTitle());
            }
        }
    }

    /**
     * Write missing value metadata to stream
     *
     * @throws XMLStreamException
     *             If an error occurs when writing to stream
     */
    private void writeValueMetadataMissing() throws XMLStreamException {
        start(TimeseriesMLConstants.QN_METADATA);
        start(TimeseriesMLConstants.QN_TVP_MEASUREMENT_METADATA);
        empty(TimeseriesMLConstants.QN_NIL_REASON);
        addXlinkHrefAttr("missing");
        endInline(TimeseriesMLConstants.QN_TVP_MEASUREMENT_METADATA);
        endInline(TimeseriesMLConstants.QN_METADATA);

    }

    @Override
    protected Optional<String> getDefaultFeatureEncodingNamespace() {
        return Optional.of(TimeseriesMLConstants.NS_TSML_10);
    }

}
