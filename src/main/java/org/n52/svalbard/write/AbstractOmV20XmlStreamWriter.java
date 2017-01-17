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
import java.util.Locale;

import javax.inject.Inject;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.joda.time.DateTime;
import org.n52.faroe.ConfigurationError;
import org.n52.janmayen.function.Functions;
import org.n52.shetland.ogc.gml.CodeWithAuthority;
import org.n52.shetland.ogc.gml.GmlConstants;
import org.n52.shetland.ogc.gml.time.Time;
import org.n52.shetland.ogc.gml.time.TimeInstant;
import org.n52.shetland.ogc.gml.time.TimePeriod;
import org.n52.shetland.ogc.om.AbstractObservationValue;
import org.n52.shetland.ogc.om.NamedValue;
import org.n52.shetland.ogc.om.OmConstants;
import org.n52.shetland.ogc.om.OmObservation;
import org.n52.shetland.util.DateTimeFormatException;
import org.n52.shetland.util.DateTimeHelper;
import org.n52.shetland.util.JavaHelper;
import org.n52.shetland.w3c.W3CConstants;
import org.n52.svalbard.SosHelperValues;
import org.n52.svalbard.encode.AbstractOmEncoderv20;
import org.n52.svalbard.encode.Encoder;
import org.n52.svalbard.encode.EncoderRepository;
import org.n52.svalbard.encode.EncodingContext;
import org.n52.svalbard.encode.EncodingValues;
import org.n52.svalbard.encode.ObservationEncoder;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.exception.UnsupportedEncoderInputException;
import org.n52.svalbard.util.CodingHelper;
import org.n52.svalbard.util.GmlHelper;
import org.n52.svalbard.util.XmlOptionsHelper;

import com.google.common.base.Strings;

import net.opengis.om.x20.OMObservationType;

/**
 * Abstract implementation of {@link XmlStreamWriter} for writing
 * {@link OmObservation}s to stream
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 4.1.0
 *
 */
public abstract class AbstractOmV20XmlStreamWriter extends XmlStreamWriter<OmObservation> {

    private OmObservation observation;

    private EncoderRepository encoderRepository;

    /**
     * @param encoderRepository
     *            the encoderRepository to set
     */
    @Inject
    public void setEncoderRepository(EncoderRepository encoderRepository) {
        this.encoderRepository = encoderRepository;
    }

    /**
     * constructor
     */
    public AbstractOmV20XmlStreamWriter() {
    }

    /**
     * constructor
     *
     * @param observation
     *            {@link OmObservation} to write to stream
     */
    public AbstractOmV20XmlStreamWriter(OmObservation observation) {
        setOmObservation(observation);
    }

    @Override
    public void write(OutputStream out) throws XMLStreamException, EncodingException {
        write(getOmObservation(), out);
    }

    @Override
    public void write(OutputStream out, EncodingValues encodingValues) throws XMLStreamException, EncodingException {
        write(getOmObservation(), out, encodingValues);
    }

    @Override
    public void write(OmObservation response, OutputStream out) throws XMLStreamException, EncodingException {
        write(response, out, new EncodingValues());
    }

    @Override
    public void write(OmObservation observation, OutputStream out, EncodingValues encodingValues)
            throws XMLStreamException, EncodingException {
        setOmObservation(observation);
        init(out, encodingValues);
        start(encodingValues.isEmbedded());
        writeOmObservationDoc(encodingValues);
        end();
        finish();
    }

    /**
     * Write {@link OmObservation} XML encoded to stream
     *
     * @param encodingValues
     *            {@link EncodingValues} contains additional information for the
     *            encoding
     * @throws XMLStreamException
     *             If an error occurs when writing to stream
     * @throws EncodingException
     *             If an error occurs when creating elements to be written If an
     *             error occurs when creating elements to be written
     */
    protected void writeOmObservationDoc(EncodingValues encodingValues) throws XMLStreamException, EncodingException {
        start(OmConstants.QN_OM_20_OBSERVATION);
        namespace(W3CConstants.NS_XLINK_PREFIX, W3CConstants.NS_XLINK);
        namespace(OmConstants.NS_OM_PREFIX, OmConstants.NS_OM_2);
        namespace(GmlConstants.NS_GML_PREFIX, GmlConstants.NS_GML_32);
        String observationID = addGmlId(observation);
        writeNewLine();
        if (observation.isSetIdentifier()) {
            writeIdentifier(observation.getIdentifierCodeWithAuthority());
            writeNewLine();
        }
        if (observation.isSetDescription()) {
            writeDescription(observation.getDescription());
            writeNewLine();
        }
        if (observation.getObservationConstellation().isSetObservationType()) {
            writeObservationType(observation.getObservationConstellation().getObservationType());
            writeNewLine();
        }
        Time phenomenonTime = observation.getPhenomenonTime();
        if (phenomenonTime.getGmlId() == null) {
            phenomenonTime.setGmlId(OmConstants.PHENOMENON_TIME_NAME + "_" + observationID);
        }
        writePhenomenonTime(phenomenonTime);
        writeNewLine();
        writeResultTime();
        writeNewLine();
        if (observation.isSetValidTime()) {
            writeValidTime(observation.getValidTime());
            writeNewLine();
        }
        writeProcedure(encodingValues);
        writeNewLine();
        if (observation.isSetParameter()) {
            writeParameter(encodingValues);
        }
        writeObservableProperty();
        writeNewLine();
        writeFeatureOfIntererst(encodingValues);
        writeNewLine();
        writeResult(observation, encodingValues);
        writeNewLine();
        indent--;
        end(OmConstants.QN_OM_20_OBSERVATION);
        indent++;
    }

    /**
     * Write {@link CodeWithAuthority} as gml:identifier to stream
     *
     * @param identifier
     *            {@link CodeWithAuthority} to write
     * @throws EncodingException
     *             If an error occurs when creating elements to be written
     * @throws XMLStreamException
     *             If an error occurs when writing to stream
     */
    protected void writeIdentifier(CodeWithAuthority identifier) throws EncodingException, XMLStreamException {
        Encoder<?, CodeWithAuthority> encoder =
                encoderRepository.getEncoder(CodingHelper.getEncoderKey(GmlConstants.NS_GML_32, identifier));
        if (encoder != null) {
            writeXmlObject((XmlObject) encoder.encode(identifier), GmlConstants.QN_IDENTIFIER_32);
        } else {
            throw new EncodingException("Error while encoding geometry value, needed encoder is missing!");
        }
    }

    /**
     * Write description as gml:descritpion to stream
     *
     * @param description
     *            Description to write
     * @throws XMLStreamException
     *             If an error occurs when writing to stream
     */
    protected void writeDescription(String description) throws XMLStreamException {
        start(GmlConstants.QN_DESCRIPTION_32);
        chars(description);
        endInline(GmlConstants.QN_DESCRIPTION_32);
    }

    /**
     * Write observation typ as om:type to stream
     *
     * @param observationType
     *            Observation type to write
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
     * @throws EncodingException
     *             If an error occurs when creating elements to be written
     * @throws XMLStreamException
     *             If an error occurs when writing to stream
     */
    protected void writePhenomenonTime(Time time) throws EncodingException, XMLStreamException {
        start(OmConstants.QN_OM_20_PHENOMENON_TIME);
        writeNewLine();
        writeTimeContent(time);
        writeNewLine();
        indent--;
        end(OmConstants.QN_OM_20_PHENOMENON_TIME);
        indent++;
    }

    protected void writeValidTime(TimePeriod validTime) throws EncodingException, XMLStreamException {
        start(OmConstants.QN_OM_20_VALID_TIME);
        writeNewLine();
        writeTimeContent(validTime);
        writeNewLine();
        indent--;
        end(OmConstants.QN_OM_20_VALID_TIME);
        indent++;
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
        TimeInstant resultTime = observation.getResultTime();
        Time phenomenonTime = observation.getPhenomenonTime();
        // get result time from SOS result time representation
        if (observation.getResultTime() != null) {
            if (resultTime.equals(phenomenonTime)) {
                empty(OmConstants.QN_OM_20_RESULT_TIME);
                addXlinkHrefAttr("#".concat(phenomenonTime.getGmlId()));
            } else {
                addResultTime(resultTime);
            }
        }
        // if result time is not set, get result time from phenomenon time
        // representation
        else {
            if (phenomenonTime instanceof TimeInstant) {
                empty(OmConstants.QN_OM_20_RESULT_TIME);
                addXlinkHrefAttr("#".concat(phenomenonTime.getGmlId()));
            } else if (phenomenonTime instanceof TimePeriod) {
                TimeInstant rsTime = new TimeInstant(((TimePeriod) observation.getPhenomenonTime()).getEnd());
                addResultTime(rsTime);
            }
        }
    }

    /**
     * Write om:procedure encoded or as xlink:href to stream
     *
     * @param encodingValues
     *            {@link EncodingValues} contains the required encoder
     * @throws XMLStreamException
     *             If an error occurs when writing to stream
     * @throws UnsupportedEncoderInputException
     *             If the procedure could not be encoded
     * @throws EncodingException
     *             If an error occurs when creating elements to be written
     */
    @SuppressWarnings("unchecked")
    protected void writeProcedure(EncodingValues encodingValues) throws XMLStreamException, EncodingException {
        // if (encodingValues.isSetEncoder() &&
        // checkEncodProcedureForEncoderKeys(encodingValues.getEncoder())) {
        // SosProcedureDescription procedureToEncode = observation
        // .getObservationConstellation().getProcedure();
        // // should the procedure be converted
        // if (procedureToEncode.getDescriptionFormat().equals(anObject)) {
        // Converter<SosProcedureDescription, SosProcedureDescription> converter
        // =
        // ConverterRepository.getInstance().getConverter(procedureDescription.getDescriptionFormat(),
        // getDefaultProcedureEncodingNamspace());
        // if (converter != null) {
        // try {
        // procedureToEncode = converter.convert(procedureDescription);
        // } catch (ConverterException e) {
        // throw new NoApplicableCodeException().causedBy(e).withMessage(
        // "Error while converting procedureDescription!");
        // }
        // } else {
        // throw new NoApplicableCodeException().withMessage("No converter (%s
        // -> %s) found!",
        // procedureDescription.getDescriptionFormat(),
        // getDefaultProcedureEncodingNamspace());
        // }
        // } else {
        // procedureToEncode = procedureDescription;
        // }
        // // encode procedure or add reference
        // XmlObject encodedProcedure =
        // CodingHelper.encodeObjectToXml(procedureToEncode.getDescriptionFormat(),
        // procedureToEncode);
        // if (encodedProcedure != null) {
        // writeXmlObject(encodedProcedure, OmConstants.QN_OM_20_PROCEDURE);
        // } else {
        // empty(OmConstants.QN_OM_20_PROCEDURE);
        // addXlinkHrefAttr(observation.getObservationConstellation().getProcedure().getIdentifier());
        // }
        // } else {
        empty(OmConstants.QN_OM_20_PROCEDURE);
        addXlinkHrefAttr(observation.getObservationConstellation().getProcedure().getIdentifier());
        if (observation.getObservationConstellation().getProcedure().isSetName()
                && observation.getObservationConstellation().getProcedure().getFirstName().isSetValue()) {
            addXlinkTitleAttr(observation.getObservationConstellation().getProcedure().getFirstName().getValue());
        }
        // }

        // if (encodingValues.isSetEncoder() && encodingValues.getEncoder()
        // instanceof ObservationEncoder) {
        // XmlObject xmlObject =
        // ((ObservationEncoder<XmlObject, Object>)
        // encodingValues.getEncoder()).encode(observation
        // .getObservationConstellation().getProcedure(), null);
        // writeXmlObject(xmlObject, OmConstants.QN_OM_20_PROCEDURE);
        // } else {
        // empty(OmConstants.QN_OM_20_PROCEDURE);
        // addXlinkHrefAttr(observation.getObservationConstellation().getProcedure().getIdentifier());
        // }
    }

    /**
     * Write om:parameter to stream
     *
     * @param encodingValues
     *            {@link EncodingValues} contains the required encoder
     * @throws XMLStreamException
     *             If an error occurs when writing to stream
     * @throws EncodingException
     *             If an error occurs when creating elements to be written
     */
    @SuppressWarnings("unchecked")
    protected void writeParameter(EncodingValues encodingValues) throws XMLStreamException, EncodingException {
        if (encodingValues.isSetEncoder() && encodingValues.getEncoder() instanceof ObservationEncoder) {
            for (NamedValue<?> namedValue : observation.getParameter()) {
                start(OmConstants.QN_OM_20_PARAMETER);
                writeNewLine();
                XmlObject xmlObject =
                        ((ObservationEncoder<XmlObject, Object>) encodingValues.getEncoder()).encode(namedValue);
                writeXmlObject(xmlObject, OmConstants.QN_OM_20_NAMED_VALUE);
                writeNewLine();
                indent--;
                end(OmConstants.QN_OM_20_PARAMETER);
                writeNewLine();
                indent++;
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
     * @param encodingValues
     *            {@link EncodingValues} contains the required encoder
     * @throws XMLStreamException
     *             If an error occurs when writing to stream
     * @throws EncodingException
     *             If an error occurs when creating elements to be written
     */
    protected void writeFeatureOfIntererst(EncodingValues encodingValues)
            throws XMLStreamException, EncodingException {
        if (encodingValues.isSetEncoder() && encodingValues.getEncoder() instanceof AbstractOmEncoderv20) {
            AbstractOmEncoderv20 encoder = (AbstractOmEncoderv20) encodingValues.getEncoder();
            Object namespace = encoder.getDefaultFeatureEncodingNamespace();
            EncodingContext codingContext = EncodingContext.of(SosHelperValues.ENCODE_NAMESPACE, namespace);
            XmlObject xmlObject = (XmlObject) encoderRepository
                    .getEncoder(CodingHelper.getEncoderKey(GmlConstants.NS_GML_32,
                            observation.getObservationConstellation().getFeatureOfInterest()))
                    .encode(observation.getObservationConstellation().getFeatureOfInterest(), codingContext);
            writeXmlObject(xmlObject, OmConstants.QN_OM_20_FEATURE_OF_INTEREST);
        } else {
            empty(OmConstants.QN_OM_20_FEATURE_OF_INTEREST);
            addXlinkHrefAttr(observation.getObservationConstellation().getFeatureOfInterest().getIdentifier());
            if (observation.getObservationConstellation().getFeatureOfInterest().isSetName()
                    && observation.getObservationConstellation().getFeatureOfInterest().getFirstName().isSetValue()) {
                addXlinkTitleAttr(
                        observation.getObservationConstellation().getFeatureOfInterest().getFirstName().getValue());
            }
        }
    }

    /**
     * write om:result to stream
     *
     * @param observation
     *            {@link OmObservation} with the result to write
     * @param encodingValues
     *            {@link EncodingValues} contains the result element namespace
     * @throws XMLStreamException
     *             If an error occurs when writing to stream
     * @throws EncodingException
     *             If an error occurs when creating elements to be written
     */
    protected void writeResult(OmObservation observation, EncodingValues encodingValues)
            throws XMLStreamException, EncodingException {
        if (observation.getValue() instanceof AbstractObservationValue<?>) {
            ((AbstractObservationValue<?>) observation.getValue()).setValuesForResultEncoding(observation);
        }
        XmlObject createResult = (XmlObject) encoderRepository
                .getEncoder(CodingHelper.getEncoderKey(encodingValues.getEncodingNamespace(), observation.getValue()))
                .encode(observation.getValue());
        if (createResult != null) {
            if (createResult.xmlText().contains(XML_FRAGMENT)) {
                XmlObject set = OMObservationType.Factory.newInstance(XmlOptionsHelper.getInstance().getXmlOptions())
                        .addNewResult().set(createResult);
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
                    writeNewLine();
                    writeXmlObject(createResult, OmConstants.QN_OM_20_RESULT);
                    writeNewLine();
                    indent--;
                    end(OmConstants.QN_OM_20_RESULT);
                    indent++;
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
        return EncodingContext.of(SosHelperValues.DOCUMENT);
    }

    /**
     * Parses the ITime object to a time representation as String
     *
     * @param time
     *            SOS ITime object
     * @return Time as String
     * @throws DateTimeFormatException
     *             If a formatting error occurs
     */
    protected String getTimeString(Time time) throws DateTimeFormatException {
        DateTime dateTime = getTime(time);
        return DateTimeHelper.formatDateTime2String(dateTime, time.getTimeFormat());
    }

    /**
     * Check the encoded om:result content for ...PropertyType
     *
     * @param result
     *            Encoded om:result content to check
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
     * @return observation id
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
     * @throws EncodingException
     *             If an error occurs when creating elements to be written
     * @throws XMLStreamException
     *             If an error occurs when writing to stream
     */
    private void writeTimeContent(Time time) throws EncodingException, XMLStreamException {
        XmlObject xmlObject =
                (XmlObject) encoderRepository.getEncoder(CodingHelper.getEncoderKey(GmlConstants.NS_GML_32, time))
                        .encode(time, getDocumentAdditionalHelperValues());
        writeXmlObject(xmlObject, GmlHelper.getGml321QnameForITime(time));
    }

    /**
     * Write encoded om:resultTime to stream
     *
     * @param time
     *            {@link Time} to encode and write
     * @throws EncodingException
     *             If an error occurs when creating elements to be written
     * @throws XMLStreamException
     *             If an error occurs when writing to stream
     */
    private void addResultTime(TimeInstant time) throws EncodingException, XMLStreamException {
        start(OmConstants.QN_OM_20_RESULT_TIME);
        writeNewLine();
        XmlObject xmlObject =
                (XmlObject) encoderRepository.getEncoder(CodingHelper.getEncoderKey(GmlConstants.NS_GML_32, time))
                        .encode(time, getDocumentAdditionalHelperValues());
        writeXmlObject(xmlObject, GmlConstants.QN_TIME_INSTANT_32);
        writeNewLine();
        indent--;
        end(OmConstants.QN_OM_20_RESULT_TIME);
        indent++;
    }

    /**
     * Get the time representation from ITime object
     *
     * @param time
     *            ITime object
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

    /**
     * Set {@link OmObservation} which should be written
     *
     * @param observation
     *            the {@link OmObservation}
     */
    private void setOmObservation(OmObservation observation) {
        this.observation = observation;
    }

    /**
     * Get the {@link OmObservation} which should be written
     *
     * @return the {@link OmObservation}
     */
    private OmObservation getOmObservation() {
        return observation;
    }

}
