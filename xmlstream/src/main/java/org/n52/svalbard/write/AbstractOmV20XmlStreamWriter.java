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
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import net.opengis.om.x20.OMObservationType;

import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.joda.time.DateTime;

import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.gml.AbstractMetaData;
import org.n52.shetland.ogc.gml.CodeType;
import org.n52.shetland.ogc.gml.CodeWithAuthority;
import org.n52.shetland.ogc.gml.GmlConstants;
import org.n52.shetland.ogc.gml.time.Time;
import org.n52.shetland.ogc.gml.time.TimeInstant;
import org.n52.shetland.ogc.gml.time.TimePeriod;
import org.n52.shetland.ogc.om.AbstractObservationValue;
import org.n52.shetland.ogc.om.NamedValue;
import org.n52.shetland.ogc.om.OmConstants;
import org.n52.shetland.ogc.om.OmObservation;
import org.n52.shetland.ogc.sos.gda.GetDataAvailabilityConstants;
import org.n52.shetland.util.DateTimeFormatException;
import org.n52.shetland.util.DateTimeHelper;
import org.n52.shetland.util.JavaHelper;
import org.n52.shetland.w3c.W3CConstants;
import org.n52.svalbard.encode.Encoder;
import org.n52.svalbard.encode.EncodingContext;
import org.n52.svalbard.encode.ObservationEncoder;
import org.n52.svalbard.encode.XmlBeansEncodingFlags;
import org.n52.svalbard.encode.XmlEncoderFlags;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.exception.UnsupportedEncoderInputException;
import org.n52.svalbard.util.GmlHelper;

import com.google.common.base.Strings;

/**
 * Abstract implementation of {@link XmlStreamWriter} for writing
 * {@link OmObservation}s to stream
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public abstract class AbstractOmV20XmlStreamWriter
        extends XmlStreamWriter<OmObservation> {
    public AbstractOmV20XmlStreamWriter(EncodingContext context, OutputStream outputStream, OmObservation element)
            throws XMLStreamException {
        super(context, outputStream, element);
    }

    protected abstract void writeAddtitionalNamespaces() throws XMLStreamException;

    @Override
    public void write() throws XMLStreamException, EncodingException {
        start();
        writeObservation(getElement());
        end();
        finish();
    }

    /**
     * Write {@link OmObservation} XML encoded to stream
     *
     * @param observation the observation
     *
     * @throws XMLStreamException If an error occurs when writing to stream
     * @throws EncodingException  If an error occurs when creating elements to be written If an error occurs when
     *                            creating elements to be written
     */
    protected void writeObservation(OmObservation observation) throws XMLStreamException, EncodingException {
        start(getDocumentName());
        namespace(W3CConstants.NS_XLINK_PREFIX, W3CConstants.NS_XLINK);
        namespace(W3CConstants.NS_XSI_PREFIX, W3CConstants.NS_XSI);
        namespace(OmConstants.NS_OM_PREFIX, OmConstants.NS_OM_2);
        namespace(GmlConstants.NS_GML_PREFIX, GmlConstants.NS_GML_32);

        writeAddtitionalNamespaces();
        String observationID = addGmlId(observation);
        checkAndWriteIdentifier();
        checkAndWriteName();
        checkAndWriteDescription();
        if (observation.getObservationConstellation().isSetObservationType()) {
            writeObservationType(observation.getObservationConstellation().getObservationType());
        }
        if (observation.isSetMetaDataProperty()) {
            writeMetaDataProperty(observation.getMetaDataProperty());
        }
        Time phenomenonTime = observation.getPhenomenonTime();
        if (phenomenonTime.getGmlId() == null) {
            phenomenonTime.setGmlId(OmConstants.PHENOMENON_TIME_NAME + "_" + observationID);
        }
        writePhenomenonTime(phenomenonTime);
        writeResultTime();
        if (observation.isSetValidTime()) {
            writeValidTime(observation.getValidTime());
        }
        writeProcedure();
        if (observation.isSetParameter()) {
            writeParameter();
        }
        writeObservableProperty();
        writeFeatureOfIntererst();
        writeResult();
        end(getDocumentName());
    }

    protected void checkAndWriteIdentifier() throws XMLStreamException, EncodingException {
        if (getElement().isSetIdentifier()) {
            writeIdentifier(getElement().getIdentifierCodeWithAuthority());
        }
    }

    protected void checkAndWriteName() throws XMLStreamException, EncodingException {
        if (getElement().isSetName()) {
            for (CodeType name : getElement().getName()) {
                writeName(name);
            }
        }
    }

    protected void checkAndWriteDescription() throws XMLStreamException {
        if (getElement().isSetDescription()) {
            writeDescription(getElement().getDescription());
        }
    }

    /**
     * Write {@link CodeWithAuthority} as gml:identifier to stream
     *
     * @param identifier
     *            {@link CodeWithAuthority} to write
     *
     * @throws EncodingException
     *             If an error occurs when creating elements to be written
     * @throws XMLStreamException
     *             If an error occurs when writing to stream
     */
    protected void writeIdentifier(CodeWithAuthority identifier) throws EncodingException, XMLStreamException {
        Encoder<?, CodeWithAuthority> encoder = getEncoder(GmlConstants.NS_GML_32, identifier);
        writeXmlObject((XmlObject) encoder.encode(identifier), GmlConstants.QN_IDENTIFIER_32);
    }

    protected void writeName(CodeType name) throws XMLStreamException, EncodingException {
        Encoder<?, CodeType> encoder = getEncoder(GmlConstants.NS_GML_32, name);
        if (encoder != null) {
            writeXmlObject((XmlObject) encoder.encode(name), GmlConstants.QN_NAME_32);
        } else {
            throw new EncodingException("Error while encoding name value, needed encoder is missing!");
        }
    }

    /**
     * Write description as gml:descritpion to stream
     *
     * @param description
     *            Description to write
     *
     * @throws XMLStreamException
     *             If an error occurs when writing to stream
     */
    protected void writeDescription(String description) throws XMLStreamException {
        start(GmlConstants.QN_DESCRIPTION_32);
        chars(description);
        endInline(GmlConstants.QN_DESCRIPTION_32);
    }

    /**
     * Write extension information
     *
     * @param map
     *            Map with values
     * @param element
     *            element name
     * @throws XMLStreamException
     *             If an error occurs when writing to stream
     * @throws EncodingException
     *             If an error occurs when creating elements to be written
     */
    protected void writeMetadata(Map<String, NamedValue<?>> map, QName element)
            throws XMLStreamException, EncodingException {
        for (Entry<String, NamedValue<?>> entry : map.entrySet()) {
            Object o = getEncoder(OmConstants.NS_OM_2, entry.getValue()).encode(entry.getValue(),
                    EncodingContext.of(XmlBeansEncodingFlags.DOCUMENT));
            if (o != null && o instanceof XmlObject) {
                start(GetDataAvailabilityConstants.GDA_EXTENSION);
                attr("name", entry.getKey());
                rawText(((XmlObject) o).xmlText(getXmlOptions()));
                end(GetDataAvailabilityConstants.GDA_EXTENSION);
            }
        }
    }

    protected void writeMetaDataProperty(List<AbstractMetaData> metaDataProperty)
            throws XMLStreamException, EncodingException {
        for (AbstractMetaData abstractMetaData : metaDataProperty) {
            Object o = getEncoder(GmlConstants.NS_GML_32, abstractMetaData).encode(abstractMetaData,
                    EncodingContext.of(XmlBeansEncodingFlags.DOCUMENT));
            if (o != null && o instanceof XmlObject) {
                start(GmlConstants.QN_OM_20_META_DATA_PROPERTY_32);
                rawText(((XmlObject) o).xmlText(getXmlOptions()));
                end(GmlConstants.QN_OM_20_META_DATA_PROPERTY_32);
            }
        }
    }

    /**
     * Write observation typ as om:type to stream
     *
     * @param observationType
     *            Observation type to write
     *
     * @throws XMLStreamException
     *             If an error occurs when writing to stream
     */
    protected void writeObservationType(String observationType) throws XMLStreamException {
        empty(OmConstants.QN_OM_20_OBSERVATION_TYPE);
        addXlinkHrefAttr(observationType);
    }

    /**
     * Write {@link Time} as om:phenomenonTime to stream
     *
     * @param time
     *            {@link Time} to write as om:phenomenonTime to stream
     *
     * @throws EncodingException
     *             If an error occurs when creating elements to be written
     * @throws XMLStreamException
     *             If an error occurs when writing to stream
     */
    protected void writePhenomenonTime(Time time) throws EncodingException, XMLStreamException {
        start(OmConstants.QN_OM_20_PHENOMENON_TIME);
        writeTimeContent(time);
        end(OmConstants.QN_OM_20_PHENOMENON_TIME);
    }

    protected void writeValidTime(TimePeriod validTime) throws EncodingException, XMLStreamException {
        start(OmConstants.QN_OM_20_VALID_TIME);
        writeTimeContent(validTime);
        end(OmConstants.QN_OM_20_VALID_TIME);
    }

    /**
     * Write om:resultTime to stream
     *
     * @throws XMLStreamException
     *             If an error occurs when writing to stream
     * @throws EncodingException
     *             If an error occurs when creating elements to be written
     */
    protected void writeResultTime() throws XMLStreamException, EncodingException {
        OmObservation observation = getElement();
        TimeInstant resultTime = observation.getResultTime();
        Time phenomenonTime = observation.getPhenomenonTime();
        // get result time from SOS result time
        if (observation.getResultTime() != null) {
            if (resultTime.equals(phenomenonTime)) {
                empty(OmConstants.QN_OM_20_RESULT_TIME);
                addXlinkHrefAttr("#".concat(phenomenonTime.getGmlId()));
            } else {
                addResultTime(resultTime);
            }
        } else if (phenomenonTime instanceof TimeInstant) {
            // if result time is not set, get result time from phenomenon time
            empty(OmConstants.QN_OM_20_RESULT_TIME);
            addXlinkHrefAttr("#".concat(phenomenonTime.getGmlId()));
        } else if (phenomenonTime instanceof TimePeriod) {
            TimeInstant rsTime = new TimeInstant(((TimePeriod) observation.getPhenomenonTime()).getEnd());
            addResultTime(rsTime);
        }
    }

    /**
     * Write om:procedure encoded or as xlink:href to stream
     *
     * @throws XMLStreamException
     *             If an error occurs when writing to stream
     * @throws UnsupportedEncoderInputException
     *             If the procedure could not be encoded
     * @throws EncodingException
     *             If an error occurs when creating elements to be written
     */
    protected void writeProcedure() throws XMLStreamException, EncodingException {
        empty(OmConstants.QN_OM_20_PROCEDURE);
        OmObservation observation = getElement();
        addXlinkHrefAttr(observation.getObservationConstellation().getProcedure().getIdentifier());
        if (observation.getObservationConstellation().getProcedure().isSetName()
                && observation.getObservationConstellation().getProcedure().getFirstName().isSetValue()) {
            addXlinkTitleAttr(observation.getObservationConstellation().getProcedure().getFirstName().getValue());
        }
    }

    /**
     * Write om:parameter to stream
     *
     * @throws XMLStreamException
     *             If an error occurs when writing to stream
     * @throws EncodingException
     *             If an error occurs when creating elements to be written
     */
    protected void writeParameter() throws XMLStreamException, EncodingException {
        Optional<ObservationEncoder<XmlObject, Object>> encoder = this.<XmlObject, Object> getEncoder()
                .filter(e -> e instanceof ObservationEncoder).map(e -> (ObservationEncoder<XmlObject, Object>) e);

        if (encoder.isPresent()) {
            ObservationEncoder<XmlObject, Object> e = encoder.get();
            for (NamedValue<?> namedValue : getElement().getParameter()) {
                start(OmConstants.QN_OM_20_PARAMETER);
                writeXmlObject(e.encode(namedValue), OmConstants.QN_OM_20_NAMED_VALUE);
                end(OmConstants.QN_OM_20_PARAMETER);
            }
        }
    }

    /**
     * Write om:observedProperty to stream
     *
     * @throws XMLStreamException
     *             If an error occurs when writing to stream
     */
    protected void writeObservableProperty() throws XMLStreamException {
        empty(OmConstants.QN_OM_20_OBSERVED_PROPERTY);
        OmObservation observation = getElement();
        addXlinkHrefAttr(observation.getObservationConstellation().getObservableProperty().getIdentifier());
        if (observation.getObservationConstellation().getObservableProperty().isSetName()
                && observation.getObservationConstellation().getObservableProperty().getFirstName().isSetValue()) {
            addXlinkTitleAttr(
                    observation.getObservationConstellation().getObservableProperty().getFirstName().getValue());
        }
    }

    /**
     * Write om:featureOfInterest encoded or as xlink:href to stream
     *
     * @throws XMLStreamException
     *             If an error occurs when writing to stream
     * @throws EncodingException
     *             If an error occurs when creating elements to be written
     */
    protected void writeFeatureOfIntererst() throws XMLStreamException, EncodingException {

        Optional<String> namespace = getDefaultFeatureEncodingNamespace();
        AbstractFeature foi = getElement().getObservationConstellation().getFeatureOfInterest();

        if (namespace.isPresent()) {
            EncodingContext codingContext = EncodingContext.of(XmlEncoderFlags.ENCODE_NAMESPACE, namespace.get());
            Encoder<XmlObject, AbstractFeature> encoder = getEncoder(GmlConstants.NS_GML_32, foi);
            writeXmlObject(encoder.encode(foi, codingContext), OmConstants.QN_OM_20_FEATURE_OF_INTEREST);
        } else {
            empty(OmConstants.QN_OM_20_FEATURE_OF_INTEREST);
            addXlinkHrefAttr(foi.getIdentifier());
            if (foi.isSetName() && foi.getFirstName().isSetValue()) {
                addXlinkTitleAttr(foi.getFirstName().getValue());
            }
        }
    }

    protected abstract Optional<String> getDefaultFeatureEncodingNamespace();

    /**
     * write om:result to stream
     *
     * @throws XMLStreamException
     *             If an error occurs when writing to stream
     * @throws EncodingException
     *             If an error occurs when creating elements to be written
     */
    protected void writeResult() throws XMLStreamException, EncodingException {
        OmObservation observation = getElement();
        if (observation.getValue() instanceof AbstractObservationValue<?>) {
            ((AbstractObservationValue<?>) observation.getValue()).setValuesForResultEncoding(observation);
        }
        XmlObject createResult = (XmlObject) getEncoder(getEncodeNamespace().orElse(OmConstants.NS_OM_2),
                                                        observation.getValue()).encode(observation.getValue());
        if (createResult != null) {
            if (createResult.xmlText().contains(XML_FRAGMENT)) {
                XmlObject set =
                        OMObservationType.Factory.newInstance(getXmlOptions()).addNewResult().set(createResult);
                writeXmlObject(set, OmConstants.QN_OM_20_RESULT);
            } else {
                if (checkResult(createResult)) {
                    QName name = createResult.schemaType().getName();
                    String prefix = name.getPrefix();
                    if (Strings.isNullOrEmpty(prefix)) {
                        XmlCursor newCursor = createResult.newCursor();
                        prefix = newCursor.prefixForNamespace(name.getNamespaceURI());
                        newCursor.setAttributeText(W3CConstants.QN_XSI_TYPE, prefix + ":" + name.getLocalPart());
                        newCursor.dispose();
                    }
                    writeXmlObject(createResult, OmConstants.QN_OM_20_RESULT);
                } else {
                    start(OmConstants.QN_OM_20_RESULT);
                    writeXmlObject(createResult, OmConstants.QN_OM_20_RESULT);
                    end(OmConstants.QN_OM_20_RESULT);
                }
            }
        } else {
            empty(OmConstants.QN_OM_20_RESULT);
        }
    }

    /**
     * Get additional values map with document helper value
     *
     * @return The encoding context
     */
    protected EncodingContext getDocumentAdditionalHelperValues() {
        return EncodingContext.of(XmlBeansEncodingFlags.DOCUMENT);
    }

    /**
     * Parses the ITime object to a time representation as String
     *
     * @param time
     *            SOS ITime object
     *
     * @return Time as String
     *
     * @throws DateTimeFormatException
     *             If a formatting error occurs
     */
    protected String getTimeString(Time time) throws DateTimeFormatException {
        DateTime dateTime = getTime(time);
        return DateTimeHelper.formatDateTime2String(dateTime, time.getTimeFormat());
    }

    protected QName getDocumentName() {
        return OmConstants.QN_OM_20_OBSERVATION;
    }

    /**
     * Check the encoded om:result content for ...PropertyType
     *
     * @param result
     *            Encoded om:result content to check
     *
     * @return <code>true</code>, if content contains ...PropertyType
     */
    private boolean checkResult(XmlObject result) {
        if (result.schemaType() != null) {
            SchemaType schemaType = result.schemaType();
            if (schemaType.getName() != null) {
                QName name = schemaType.getName();
                if (name.getLocalPart() != null
                        && name.getLocalPart().toLowerCase(Locale.ROOT).contains("propertytype")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Add gml:id to om:OM_Observation element
     *
     * @param observation
     *            {@link OmObservation} with the GML id
     *
     * @return observation id
     *
     * @throws XMLStreamException
     *             If an error occurs when writing to stream
     */
    private String addGmlId(OmObservation observation) throws XMLStreamException {
        String observationID = JavaHelper.generateID(Double.toString(System.currentTimeMillis() * Math.random()));
        if (observation.isSetObservationID()) {
            observationID = observation.getObservationID();
        } else {
            observation.setObservationID(observationID);
        }
        attr(GmlConstants.QN_ID_32, "o_" + observationID);
        return observationID;
    }

    /**
     * Write encoded time object to the stream.
     *
     * @param time
     *            {@link Time} to encode and write
     *
     * @throws EncodingException
     *             If an error occurs when creating elements to be written
     * @throws XMLStreamException
     *             If an error occurs when writing to stream
     */
    private void writeTimeContent(Time time) throws EncodingException, XMLStreamException {
        XmlObject xmlObject =
                (XmlObject) getEncoder(GmlConstants.NS_GML_32, time).encode(time, getDocumentAdditionalHelperValues());
        writeXmlObject(xmlObject, GmlHelper.getGml321QnameForITime(time));
    }

    /**
     * Write encoded om:resultTime to stream
     *
     * @param time
     *            {@link Time} to encode and write
     *
     * @throws EncodingException
     *             If an error occurs when creating elements to be written
     * @throws XMLStreamException
     *             If an error occurs when writing to stream
     */
    private void addResultTime(TimeInstant time) throws EncodingException, XMLStreamException {
        start(OmConstants.QN_OM_20_RESULT_TIME);
        XmlObject xmlObject =
                (XmlObject) getEncoder(GmlConstants.NS_GML_32, time).encode(time, getDocumentAdditionalHelperValues());
        writeXmlObject(xmlObject, GmlConstants.QN_TIME_INSTANT_32);
        end(OmConstants.QN_OM_20_RESULT_TIME);
    }

    /**
     * Get the time representation from ITime object
     *
     * @param time
     *            ITime object
     *
     * @return Time as DateTime
     */
    private DateTime getTime(Time time) {
        if (time instanceof TimeInstant) {
            return ((TimeInstant) time).getValue();
        } else if (time instanceof TimePeriod) {
            TimePeriod timePeriod = (TimePeriod) time;
            if (timePeriod.getEnd() != null) {
                return timePeriod.getEnd();
            } else {
                return timePeriod.getStart();
            }
        }
        return new DateTime().minusYears(1000);
    }
}
