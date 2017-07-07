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
package org.n52.svalbard.write;

import java.io.OutputStream;
import java.util.List;
import java.util.Optional;

import javax.xml.stream.XMLStreamException;

import org.apache.commons.lang.StringEscapeUtils;

import org.n52.shetland.ogc.gml.CodeType;
import org.n52.shetland.ogc.gml.GmlConstants;
import org.n52.shetland.ogc.om.MultiObservationValues;
import org.n52.shetland.ogc.om.ObservationValue;
import org.n52.shetland.ogc.om.OmConstants;
import org.n52.shetland.ogc.om.OmObservableProperty;
import org.n52.shetland.ogc.om.OmObservation;
import org.n52.shetland.ogc.om.SingleObservationValue;
import org.n52.shetland.ogc.om.StreamingValue;
import org.n52.shetland.ogc.om.TimeValuePair;
import org.n52.shetland.ogc.om.series.wml.MeasurementTimeseriesMetadata;
import org.n52.shetland.ogc.om.series.wml.WaterMLConstants;
import org.n52.shetland.ogc.om.series.wml.WaterMLConstants.InterpolationType;
import org.n52.shetland.ogc.om.values.CountValue;
import org.n52.shetland.ogc.om.values.ProfileValue;
import org.n52.shetland.ogc.om.values.QuantityValue;
import org.n52.shetland.ogc.om.values.TVPValue;
import org.n52.shetland.ogc.om.values.TextValue;
import org.n52.shetland.ogc.om.values.Value;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.shetland.util.DateTimeFormatException;
import org.n52.shetland.w3c.W3CConstants;
import org.n52.svalbard.encode.EncodingContext;
import org.n52.svalbard.encode.exception.EncodingException;

import com.google.common.base.Strings;

/**
 * Implementation of {@link AbstractOmV20XmlStreamWriter} to write WaterML 2.0
 * encoded {@link OmObservation}s to stream
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public class WmlTVPEncoderv20XmlStreamWriter
        extends AbstractOmV20XmlStreamWriter {
    public WmlTVPEncoderv20XmlStreamWriter(
            EncodingContext context,
            OutputStream outputStream,
            OmObservation element)
                    throws XMLStreamException {
        super(context, outputStream, element);
    }

    @Override
    protected void writeResult() throws XMLStreamException, EncodingException {
        start(OmConstants.QN_OM_20_RESULT);
        namespace(WaterMLConstants.NS_WML_20_PREFIX, WaterMLConstants.NS_WML_20);
        start(WaterMLConstants.QN_MEASUREMENT_TIMESERIES);
        OmObservation observation = getElement();
        attr(GmlConstants.QN_ID_32, "timeseries." + observation.getObservationID());
        writeMeasurementTimeseriesMetadata(observation);
        if (observation.getValue() instanceof SingleObservationValue) {
            SingleObservationValue<?> observationValue = (SingleObservationValue<?>) observation.getValue();
            writeDefaultPointMetadata(observationValue, observationValue.getValue().getUnit());
            String time = getTimeString(observationValue.getPhenomenonTime());
            writePoint(time, getValue(observation.getValue().getValue()));
            close();
        } else if (observation.getValue() instanceof MultiObservationValues) {
            // XML streaming to client
            MultiObservationValues<?> observationValue = (MultiObservationValues<?>) observation.getValue();
            writeDefaultPointMetadata(observationValue, observationValue.getValue().getUnit());
            TVPValue tvpValue = (TVPValue) observationValue.getValue();
            List<TimeValuePair> timeValuePairs = tvpValue.getValue();
            for (TimeValuePair timeValuePair : timeValuePairs) {
                if (timeValuePair != null) {
                    writePoint(getTimeString(timeValuePair.getTime()), getValue(timeValuePair.getValue()));
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
                    && ((OmObservableProperty) observation.getObservationConstellation().getObservableProperty())
                            .isSetUnit()) {
                writeDefaultPointMetadata(observationValue,
                        ((OmObservableProperty) observation.getObservationConstellation().getObservableProperty())
                                .getUnit());
            } else {
                writeDefaultPointMetadata(observationValue, null);
            }
            try {
                while (observationValue.hasNext()) {
                    TimeValuePair timeValuePair = observationValue.nextValue();
                    if (timeValuePair != null) {
                        writePoint(getTimeString(timeValuePair.getTime()), getValue(timeValuePair.getValue()));
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
        namespace(WaterMLConstants.NS_WML_20_PREFIX, WaterMLConstants.NS_WML_20);
    }

    @Override
    protected void checkAndWriteIdentifier() throws EncodingException, XMLStreamException {
        if (getElement().getObservationConstellation().isSetIdentifier()) {
            writeIdentifier(getElement().getObservationConstellation().getIdentifierCodeWithAuthority());
        } else {
            super.checkAndWriteIdentifier();
        }
    }

    @Override
    protected void checkAndWriteName() throws EncodingException, XMLStreamException {
        if (getElement().getObservationConstellation().isSetName()) {
            for (CodeType name : getElement().getObservationConstellation().getName()) {
                writeName(name);
            }
        } else {
            super.checkAndWriteName();
        }
    }

    @Override
    protected void checkAndWriteDescription() throws XMLStreamException {
        if (getElement().getObservationConstellation().isSetDescription()) {
            writeDescription(getElement().getObservationConstellation().getDescription());
        } else {
            super.checkAndWriteDescription();
        }
    }

    /**
     * Close written wml:MeasurementTimeseries and om:result tags
     *
     * @throws XMLStreamException
     *             If an error occurs when writing to stream
     */
    private void close() throws XMLStreamException {
        end(WaterMLConstants.QN_MEASUREMENT_TIMESERIES);
        end(OmConstants.QN_OM_20_RESULT);
    }

    /**
     * Write timeseries metadata to stream
     *
     * @param id
     *            Observation id
     *
     * @throws XMLStreamException
     *             If an error occurs when writing to stream
     */
    private void writeMeasurementTimeseriesMetadata(OmObservation o) throws XMLStreamException {
        start(WaterMLConstants.QN_METADATA);
        start(WaterMLConstants.QN_MEASUREMENT_TIMESERIES_METADATA);
        empty(WaterMLConstants.QN_TEMPORAL_EXTENT);
        addXlinkHrefAttr("#" + o.getPhenomenonTime().getGmlId());
        if (o.isSetValue() && o.getValue().isSetMetadata() && o.getValue().getMetadata().isSetTimeseriesMetadata()
                && o.getValue().getMetadata().getTimeseriesmetadata() instanceof MeasurementTimeseriesMetadata) {
            start(WaterMLConstants.QN_CUMULATIVE);
            chars(Boolean.toString(((MeasurementTimeseriesMetadata) o.getValue().getMetadata().getTimeseriesmetadata())
                    .isCumulative()));
            endInline(WaterMLConstants.QN_CUMULATIVE);
        }
        end(WaterMLConstants.QN_MEASUREMENT_TIMESERIES_METADATA);
        end(WaterMLConstants.QN_METADATA);
    }

    /**
     * Write wml:defaultPointMetadata to stream
     *
     * @param unit
     *            the unit
     *
     * @throws XMLStreamException
     *             If an error occurs when writing to stream
     */
    private void writeDefaultPointMetadata(ObservationValue<?> value, String unit) throws XMLStreamException {
        start(WaterMLConstants.QN_DEFAULT_POINT_METADATA);
        start(WaterMLConstants.QN_DEFAULT_TVP_MEASUREMENT_METADATA);
        writeUOM(unit);
        writeInterpolationType(value);
        end(WaterMLConstants.QN_DEFAULT_TVP_MEASUREMENT_METADATA);
        end(WaterMLConstants.QN_DEFAULT_POINT_METADATA);
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
    private void writeUOM(String code) throws XMLStreamException {
        if (!Strings.isNullOrEmpty(code)) {
            empty(WaterMLConstants.UOM);
            attr("code", code);
        }
    }

    /**
     * Write wml:interpolationType to stream
     *
     * @throws XMLStreamException
     *             If an error occurs when writing to stream
     */
    private void writeInterpolationType(ObservationValue<?> value) throws XMLStreamException {
        empty(WaterMLConstants.QN_INTERPOLATION_TYPE);
        if (value != null && value.isSetMetadata()
                && value.getDefaultPointMetadata().isSetDefaultTVPMeasurementMetadata()
                && value.getDefaultPointMetadata().getDefaultTVPMeasurementMetadata().isSetInterpolationType()) {
            InterpolationType interpolationtype =
                    value.getDefaultPointMetadata().getDefaultTVPMeasurementMetadata().getInterpolationtype();
            addXlinkHrefAttr(interpolationtype.getIdentifier());
            addXlinkTitleAttr(interpolationtype.getTitle());
        } else {
            addXlinkHrefAttr("http://www.opengis.net/def/timeseriesType/WaterML/2.0/continuous");
            addXlinkTitleAttr("Instantaneous");
        }
    }

    /**
     * Get the {@link String} representation of {@link Value}
     *
     * @param value
     *            {@link Value} to get {@link String} representation from
     *
     * @return {@link String} representation of {@link Value}
     */
    private String getValue(Value<?> value) {
        if (value != null && value.isSetValue()) {
            if (value instanceof QuantityValue) {
                QuantityValue quantityValue = (QuantityValue) value;
                return Double.toString(quantityValue.getValue());
            } else if (value instanceof ProfileValue) {
                ProfileValue gwglcValue = (ProfileValue) value;
                if (gwglcValue.isSetValue()) {
                    return getValue(gwglcValue.getValue().iterator().next().getSimpleValue());
                }
            } else if (value instanceof CountValue) {
                CountValue countValue = (CountValue) value;
                return Integer.toString(countValue.getValue());
            } else if (value instanceof TextValue) {
                TextValue textValue = (TextValue) value;
                String nonXmlEscapedText = textValue.getValue();
                return StringEscapeUtils.escapeXml(nonXmlEscapedText);
            }
        }
        return null;
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
            start(WaterMLConstants.QN_POINT);
            writeMeasurementTVP(time, value);
            end(WaterMLConstants.QN_POINT);
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
        start(WaterMLConstants.QN_MEASUREMENT_TVP);
        writeTime(time);
        writeValue(value);
        end(WaterMLConstants.QN_MEASUREMENT_TVP);
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
        start(WaterMLConstants.QN_TIME);
        chars(time);
        endInline(WaterMLConstants.QN_TIME);
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
            start(WaterMLConstants.QN_VALUE);
            chars(value);
            endInline(WaterMLConstants.QN_VALUE);
        } else {
            empty(WaterMLConstants.QN_VALUE);
            attr(W3CConstants.QN_XSI_NIL, "true");
            writeValueMetadata();
        }
    }

    /**
     * Write missing value metadata to stream
     *
     * @throws XMLStreamException
     *             If an error occurs when writing to stream
     */
    private void writeValueMetadata() throws XMLStreamException {
        start(WaterMLConstants.QN_METADATA);
        start(WaterMLConstants.QN_TVP_MEASUREMENT_METADATA);
        empty(WaterMLConstants.QN_NIL_REASON);
        addXlinkHrefAttr("missing");
        endInline(WaterMLConstants.QN_TVP_MEASUREMENT_METADATA);
        endInline(WaterMLConstants.QN_METADATA);

    }

    @Override
    protected Optional<String> getDefaultFeatureEncodingNamespace() {
        return Optional.of(WaterMLConstants.NS_WML_20);
    }

}
