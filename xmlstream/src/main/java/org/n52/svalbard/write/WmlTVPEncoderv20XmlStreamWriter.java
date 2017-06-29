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

import java.util.List;
import java.util.Optional;

import javax.xml.stream.XMLStreamException;

import org.apache.commons.lang.StringEscapeUtils;

import org.n52.shetland.ogc.gml.GmlConstants;
import org.n52.shetland.ogc.om.MultiObservationValues;
import org.n52.shetland.ogc.om.OmConstants;
import org.n52.shetland.ogc.om.OmObservation;
import org.n52.shetland.ogc.om.SingleObservationValue;
import org.n52.shetland.ogc.om.StreamingValue;
import org.n52.shetland.ogc.om.TimeValuePair;
import org.n52.shetland.ogc.om.series.wml.WaterMLConstants;
import org.n52.shetland.ogc.om.values.CountValue;
import org.n52.shetland.ogc.om.values.QuantityValue;
import org.n52.shetland.ogc.om.values.TVPValue;
import org.n52.shetland.ogc.om.values.TextValue;
import org.n52.shetland.ogc.om.values.Value;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.shetland.util.DateTimeFormatException;
import org.n52.shetland.w3c.W3CConstants;
import org.n52.svalbard.encode.EncodingValues;
import org.n52.svalbard.encode.exception.EncodingException;

import com.google.common.base.Strings;

/**
 * Implementation of {@link AbstractOmV20XmlStreamWriter} to write WaterML 2.0
 * encoded {@link OmObservation}s to stream
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 4.1.0
 *
 */
public class WmlTVPEncoderv20XmlStreamWriter extends AbstractOmV20XmlStreamWriter {

    /**
     * constructor
     */
    public WmlTVPEncoderv20XmlStreamWriter() {
        super();
    }

    /**
     * constructor
     *
     * @param observation
     *            {@link OmObservation} to write to stream
     */
    public WmlTVPEncoderv20XmlStreamWriter(OmObservation observation) {
        super(observation);
    }

    @Override
    protected void writeResult(OmObservation observation, EncodingValues encodingValues)
            throws XMLStreamException, EncodingException {
        start(OmConstants.QN_OM_20_RESULT);
        namespace(WaterMLConstants.NS_WML_20_PREFIX, WaterMLConstants.NS_WML_20);
        writeNewLine();
        start(WaterMLConstants.QN_MEASUREMENT_TIMESERIES);
        attr(GmlConstants.QN_ID_32, "timeseries." + observation.getObservationID());
        writeNewLine();
        writeMeasurementTimeseriesMetadata(observation.getPhenomenonTime().getGmlId());
        writeNewLine();
        if (observation.getValue() instanceof SingleObservationValue) {
            SingleObservationValue<?> observationValue = (SingleObservationValue<?>) observation.getValue();
            writeDefaultPointMetadata(observationValue.getValue().getUnit());
            writeNewLine();
            String time = getTimeString(observationValue.getPhenomenonTime());
            writePoint(time, getValue(observation.getValue().getValue()));
            writeNewLine();
            close();
        } else if (observation.getValue() instanceof MultiObservationValues) {
            MultiObservationValues<?> observationValue = (MultiObservationValues<?>) observation.getValue();
            writeDefaultPointMetadata(observationValue.getValue().getUnit());
            writeNewLine();
            TVPValue tvpValue = (TVPValue) observationValue.getValue();
            List<TimeValuePair> timeValuePairs = tvpValue.getValue();
            for (TimeValuePair timeValuePair : timeValuePairs) {
                writePoint(getTimeString(timeValuePair.getTime()), getValue(timeValuePair.getValue()));
                writeNewLine();
            }
            close();
        } else if (observation.getValue() instanceof StreamingValue) {
            StreamingValue<?> observationValue = (StreamingValue) observation.getValue();
            writeDefaultPointMetadata(observationValue.getUnit());
            writeNewLine();
            try {
                while (observationValue.hasNext()) {
                    TimeValuePair timeValuePair = observationValue.nextValue();
                    writePoint(getTimeString(timeValuePair.getTime()), getValue(timeValuePair.getValue()));
                    writeNewLine();
                }
            } catch (DateTimeFormatException | OwsExceptionReport e) {
                throw new EncodingException(e);
            }
            close();
        } else {
            super.writeResult(observation, encodingValues);
        }
    }

    /**
     * Close written wml:MeasurementTimeseries and om:result tags
     *
     * @throws XMLStreamException
     *             If an error occurs when writing to stream
     */
    private void close() throws XMLStreamException {
        indent--;
        end(WaterMLConstants.QN_MEASUREMENT_TIMESERIES);
        writeNewLine();
        end(OmConstants.QN_OM_20_RESULT);
        indent++;
    }

    /**
     * Write timeseries metadata to stream
     *
     * @param id
     *            Observation id
     * @throws XMLStreamException
     *             If an error occurs when writing to stream
     */
    private void writeMeasurementTimeseriesMetadata(String id) throws XMLStreamException {
        start(WaterMLConstants.QN_METADATA);
        writeNewLine();
        start(WaterMLConstants.QN_TIMESERIES_METADATA);
        writeNewLine();
        empty(WaterMLConstants.QN_TEMPORAL_EXTENT);
        addXlinkHrefAttr("#" + id);
        writeNewLine();
        indent--;
        end(WaterMLConstants.QN_TIMESERIES_METADATA);
        writeNewLine();
        end(WaterMLConstants.QN_METADATA);
        indent++;
    }

    /**
     * Write wml:defaultPointMetadata to stream
     *
     * @param unit
     * @throws XMLStreamException
     *             If an error occurs when writing to stream
     */
    private void writeDefaultPointMetadata(String unit) throws XMLStreamException {
        start(WaterMLConstants.QN_DEFAULT_POINT_METADATA);
        writeNewLine();
        start(WaterMLConstants.QN_DEFAULT_TVP_MEASUREMENT_METADATA);
        writeNewLine();
        writeUOM(unit);
        writeNewLine();
        writeInterpolationType();
        writeNewLine();
        indent--;
        end(WaterMLConstants.QN_DEFAULT_TVP_MEASUREMENT_METADATA);
        writeNewLine();
        end(WaterMLConstants.QN_DEFAULT_POINT_METADATA);
        indent++;
    }

    /**
     * Write UOM attribute to stream
     *
     * @param code
     *            UOM code
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
    private void writeInterpolationType() throws XMLStreamException {
        empty(WaterMLConstants.QN_INTERPOLATION_TYPE);
        addXlinkHrefAttr("http://www.opengis.net/def/timeseriesType/WaterML/2.0/continuous");
        addXlinkTitleAttr("Instantaneous");
    }

    /**
     * Get the {@link String} representation of {@link Value}
     *
     * @param value
     *            {@link Value} to get {@link String} representation from
     * @return {@link String} representation of {@link Value}
     */
    private String getValue(Value<?> value) {
        if (value.isSetValue()) {
            if (value instanceof QuantityValue) {
                QuantityValue quantityValue = (QuantityValue) value;
                return Double.toString(quantityValue.getValue().doubleValue());
            } else if (value instanceof CountValue) {
                CountValue countValue = (CountValue) value;
                return Integer.toString(countValue.getValue().intValue());
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
     * @throws XMLStreamException
     *             If an error occurs when writing to stream
     */
    private void writePoint(String time, String value) throws XMLStreamException {
        if (!Strings.isNullOrEmpty(time)) {
            start(WaterMLConstants.QN_POINT);
            writeNewLine();
            writeMeasurementTVP(time, value);
            writeNewLine();
            indent--;
            end(WaterMLConstants.QN_POINT);
            indent++;
        }
    }

    /**
     * Write wml:MeasurementTVP to stream
     *
     * @param time
     *            time as {@link String}
     * @param value
     *            value as {@link String}
     * @throws XMLStreamException
     *             If an error occurs when writing to stream
     */
    private void writeMeasurementTVP(String time, String value) throws XMLStreamException {
        start(WaterMLConstants.QN_MEASUREMENT_TVP);
        writeNewLine();
        writeTime(time);
        writeNewLine();
        writeValue(value);
        writeNewLine();
        indent--;
        end(WaterMLConstants.QN_MEASUREMENT_TVP);
        indent++;
    }

    /**
     * Write wml:time to stream
     *
     * @param time
     *            time to write
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
