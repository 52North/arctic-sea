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

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import net.opengis.om.x20.NamedValueDocument;
import net.opengis.om.x20.NamedValuePropertyType;
import net.opengis.om.x20.NamedValueType;
import net.opengis.om.x20.OMObservationDocument;
import net.opengis.om.x20.OMObservationPropertyType;
import net.opengis.om.x20.OMObservationType;
import net.opengis.om.x20.OMProcessPropertyType;
import net.opengis.om.x20.ObservationContextPropertyType;
import net.opengis.om.x20.ObservationContextType;
import net.opengis.om.x20.TimeObjectPropertyType;

import org.apache.xmlbeans.XmlBoolean;
import org.apache.xmlbeans.XmlInteger;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlString;
import org.isotc211.x2005.gmd.AbstractDQElementDocument;
import org.isotc211.x2005.gmd.DQElementPropertyType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.gml.AbstractMetaData;
import org.n52.shetland.ogc.gml.CodeType;
import org.n52.shetland.ogc.gml.GmlConstants;
import org.n52.shetland.ogc.gml.time.Time;
import org.n52.shetland.ogc.gml.time.TimeInstant;
import org.n52.shetland.ogc.gml.time.TimePeriod;
import org.n52.shetland.ogc.om.AbstractPhenomenon;
import org.n52.shetland.ogc.om.NamedValue;
import org.n52.shetland.ogc.om.ObservationValue;
import org.n52.shetland.ogc.om.OmConstants;
import org.n52.shetland.ogc.om.OmObservation;
import org.n52.shetland.ogc.om.OmObservationContext;
import org.n52.shetland.ogc.om.SingleObservationValue;
import org.n52.shetland.ogc.om.quality.OmResultQuality;
import org.n52.shetland.ogc.om.values.BooleanValue;
import org.n52.shetland.ogc.om.values.CategoryValue;
import org.n52.shetland.ogc.om.values.ComplexValue;
import org.n52.shetland.ogc.om.values.CountValue;
import org.n52.shetland.ogc.om.values.CvDiscretePointCoverage;
import org.n52.shetland.ogc.om.values.GeometryValue;
import org.n52.shetland.ogc.om.values.HrefAttributeValue;
import org.n52.shetland.ogc.om.values.MultiPointCoverage;
import org.n52.shetland.ogc.om.values.NilTemplateValue;
import org.n52.shetland.ogc.om.values.ProfileValue;
import org.n52.shetland.ogc.om.values.QuantityRangeValue;
import org.n52.shetland.ogc.om.values.QuantityValue;
import org.n52.shetland.ogc.om.values.RectifiedGridCoverage;
import org.n52.shetland.ogc.om.values.ReferenceValue;
import org.n52.shetland.ogc.om.values.SweDataArrayValue;
import org.n52.shetland.ogc.om.values.TLVTValue;
import org.n52.shetland.ogc.om.values.TVPValue;
import org.n52.shetland.ogc.om.values.TextValue;
import org.n52.shetland.ogc.om.values.TimeRangeValue;
import org.n52.shetland.ogc.om.values.UnknownValue;
import org.n52.shetland.ogc.om.values.Value;
import org.n52.shetland.ogc.om.values.XmlValue;
import org.n52.shetland.ogc.om.values.visitor.ValueVisitor;
import org.n52.shetland.ogc.sos.SosProcedureDescription;
import org.n52.shetland.ogc.sos.SosProcedureDescriptionUnknownType;
import org.n52.shetland.ogc.swe.SweConstants;
import org.n52.shetland.util.JavaHelper;
import org.n52.shetland.w3c.W3CConstants;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.exception.UnsupportedEncoderInputException;
import org.n52.svalbard.util.GmlHelper;
import org.n52.svalbard.util.XmlHelper;

import com.google.common.base.Strings;

public abstract class AbstractOmEncoderv20
        extends AbstractXmlEncoder<XmlObject, Object>
        implements ObservationEncoder<XmlObject, Object>, StreamingEncoder<XmlObject, Object> {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractOmEncoderv20.class);

    private static final String OBSERVATION_ID_PREFIX = "o_";

    /**
     * Method to create the om:result element content
     *
     * @param sosObservation
     *            SosObservation to be encoded
     * @return XML encoded result object, e.g a gml:MeasureType
     * @throws EncodingException
     *             if an error occurs
     */
    protected abstract XmlObject createResult(OmObservation sosObservation) throws EncodingException;

    protected abstract XmlObject encodeResult(ObservationValue<?> observationValue) throws EncodingException;

    /**
     * Method to add the observation type to the om:Observation. Subclasses
     * should have mappings to set the correct type, e.g. O&M .../Measurement ==
     * .../MeasurementTimeseriesTVPObservation in WaterML 2.0
     *
     * @param xbObservation
     *            XmlBeans object of observation
     * @param observationType
     *            Observation type
     */
    protected abstract void addObservationType(OMObservationType xbObservation, String observationType);

    /**
     * Get the default encoding Namespace for FeatureOfInterest
     *
     * @return Encoding namespace
     */
    public abstract String getDefaultFeatureEncodingNamespace();

    /**
     * Get the default encoding Namespace for Procedures
     *
     * @return Encoding namespace
     */
    protected abstract String getDefaultProcedureEncodingNamspace();

    /**
     * Indicator whether the procedure is to be encoded
     *
     * @return Indicator
     */
    protected abstract boolean convertEncodedProcedure();

    protected abstract OMObservationType createOmObservationType();

    protected abstract void addAddtitionalInformation(OMObservationType omot, OmObservation observation)
            throws EncodingException;

    @Override
    public XmlObject encode(Object element, EncodingContext additionalValues) throws EncodingException {
        XmlObject encodedObject = null;
        if (element instanceof OmObservation) {
            encodedObject = encodeOmObservation((OmObservation) element, additionalValues);
        } else if (element instanceof NamedValue) {
            NamedValueType nvt = createNamedValue((NamedValue<?>) element);
            if (additionalValues.has(XmlBeansEncodingFlags.DOCUMENT)) {
                NamedValueDocument nvd = NamedValueDocument.Factory.newInstance();
                nvd.setNamedValue(nvt);
                encodedObject = nvd;
            } else if (additionalValues.has(XmlBeansEncodingFlags.PROPERTY_TYPE)) {
                NamedValuePropertyType nvpt = NamedValuePropertyType.Factory.newInstance();
                nvpt.setNamedValue(nvt);
                encodedObject = nvpt;
            } else {
                encodedObject = nvt;
            }
        } else if (element instanceof AbstractFeature) {
            encodedObject = encodeFeatureOfInterest((AbstractFeature) element);
        } else if (element instanceof SosProcedureDescription) {
            encodedObject = encodeProcedureDescription((SosProcedureDescription<?>) element);
        } else {
            throw new UnsupportedEncoderInputException(this, element);
        }
        // LOGGER.debug("Encoded object {} is valid: {}",
        // encodedObject.schemaType().toString(),
        // XmlHelper.validateDocument(encodedObject));
        return encodedObject;
    }

    @Override
    public void encode(Object objectToEncode, OutputStream outputStream, EncodingContext ctx) throws EncodingException {
        try {
            XmlOptions xmlOptions = getXmlOptions();
            if (ctx.has(StreamingEncoderFlags.EMBEDDED)) {
                xmlOptions.setSaveNoXmlDecl();
            }
            encode(objectToEncode, ctx.with(StreamingEncoderFlags.ENCODER, this))
                    .save(outputStream, xmlOptions);
        } catch (IOException ioe) {
            throw new EncodingException("Error while writing element to stream!", ioe);
        } finally {
            if (ctx.has(StreamingEncoderFlags.EMBEDDED)) {
                getXmlOptions().remove(XmlOptions.SAVE_NO_XML_DECL);
            }
        }
    }

    @Override
    public void addNamespacePrefixToMap(Map<String, String> nameSpacePrefixMap) {
        nameSpacePrefixMap.put(OmConstants.NS_OM_2, OmConstants.NS_OM_PREFIX);
    }

    /**
     * Method to create an O&M 2.0 observation XmlBeans object
     *
     * @param sosObservation
     *            SosObservation to be encoded
     * @param context
     *            Additional values which are used during the encoding
     * @return XmlBeans representation of O&M 2.0 observation
     * @throws EncodingException
     *             If an error occurs
     */
    protected XmlObject encodeOmObservation(OmObservation sosObservation, EncodingContext context)
            throws EncodingException {
        OMObservationType xbObservation = createOmObservationType();

        if (!sosObservation.isSetObservationID()) {
            sosObservation.setObservationID(
                    JavaHelper.generateID(Double.toString(System.currentTimeMillis() * Math.random())));
        }
        String observationID = sosObservation.getObservationID();
        if (!sosObservation.isSetGmlID()) {
            sosObservation.setGmlId(OBSERVATION_ID_PREFIX + observationID);
        }
        // set a unique gml:id
        xbObservation.setId(generateObservationGMLId());
        if (!sosObservation.isSetObservationID()) {
            sosObservation.setObservationID(xbObservation.getId().replace(OBSERVATION_ID_PREFIX, ""));
        }

        setObservationIdentifier(sosObservation, xbObservation);
        setObservationName(sosObservation, xbObservation);
        setDescription(sosObservation, xbObservation);
        setMetaDataProperty(sosObservation, xbObservation);
        setObservationType(sosObservation, xbObservation);
        setRelatedObservations(sosObservation, xbObservation);
        setPhenomenonTime(sosObservation, xbObservation);
        setResultTime(sosObservation, xbObservation);
        setValidTime(sosObservation, xbObservation);
        setProcedure(sosObservation, xbObservation);
        setParameter(sosObservation, xbObservation);
        setObservableProperty(sosObservation, xbObservation);
        setFeatureOfInterest(sosObservation, xbObservation);
        setResultQualities(xbObservation, sosObservation);
        setResult(sosObservation, xbObservation);
        addAddtitionalInformation(xbObservation, sosObservation);

        if (context.has(XmlBeansEncodingFlags.PROPERTY_TYPE)) {
            return createObservationPropertyType(xbObservation);
        } else if (context.has(XmlBeansEncodingFlags.DOCUMENT)) {
            return createObservationDocument(xbObservation);
        } else {
            return xbObservation;
        }
    }

    private XmlObject createObservationDocument(OMObservationType xbObservation) {
        OMObservationDocument doc = createObservationDocument();
        doc.setOMObservation(xbObservation);
        return doc;
    }

    private OMObservationDocument createObservationDocument() {
        return OMObservationDocument.Factory.newInstance(getXmlOptions());
    }

    private XmlObject createObservationPropertyType(OMObservationType obs) {
        OMObservationPropertyType opt = createObservationPropertyType();
        opt.setOMObservation(obs);
        return opt;
    }

    private OMObservationPropertyType createObservationPropertyType() {
        return OMObservationPropertyType.Factory.newInstance(getXmlOptions());
    }

    private void setDescription(OmObservation observation, OMObservationType xb) {
        // set observation description
        if (observation.isSetDescription()) {
            xb.addNewDescription().setStringValue(observation.getDescription());
        }
    }

    private void setResult(OmObservation observation, OMObservationType xb) throws EncodingException {
        XmlObject result = createResult(observation);
        if (result != null) {
            xb.addNewResult().set(result);
        } else {
            xb.addNewResult();
        }
    }

    private void setFeatureOfInterest(OmObservation observation, OMObservationType xb) throws EncodingException {
        AbstractFeature foi = observation.getObservationConstellation().getFeatureOfInterest();
        XmlObject xbFoi = encodeFeatureOfInterest(foi);
        xb.addNewFeatureOfInterest().set(xbFoi);
    }

    private void setObservationIdentifier(OmObservation observation, OMObservationType xb) throws EncodingException {
        // set observation identifier if available
        if (observation.isSetIdentifier()) {
            XmlObject xbId = encodeGML(observation.getIdentifierCodeWithAuthority());
            xb.addNewIdentifier().set(xbId);
        }
    }

    private void setObservationName(OmObservation observation, OMObservationType xb) throws EncodingException {
        // set observation identifier if available
        if (observation.isSetIdentifier()) {
            for (CodeType name : observation.getName()) {
                XmlObject xbId = encodeGML(name);
                xb.addNewName().set(xbId);
            }
        }
    }

    private void setMetaDataProperty(OmObservation sosObservation, OMObservationType xbObservation)
            throws EncodingException {
        if (sosObservation.isSetMetaDataProperty()) {
            for (AbstractMetaData abstractMetaData : sosObservation.getMetaDataProperty()) {
                XmlObject encodeObject = encodeGML(abstractMetaData);
                XmlObject substituteElement = XmlHelper.substituteElement(
                        xbObservation.addNewMetaDataProperty().addNewAbstractMetaData(), encodeObject);
                substituteElement.set(encodeObject);
            }
        }
    }

    protected void setObservationType(OmObservation observation, OMObservationType xb) {
        // add observationType if set
        addObservationType(xb, observation.getObservationConstellation().getObservationType());
    }

    private void setRelatedObservations(OmObservation sosObservation, OMObservationType omot)
            throws EncodingException {
        if (sosObservation.isSetRelatedObservations()) {
            for (OmObservationContext observationContext : sosObservation.getRelatedObservations()) {
                addRelatedObservation(omot.addNewRelatedObservation(), observationContext);
            }
        }
    }

    private void addRelatedObservation(ObservationContextPropertyType ocpt, OmObservationContext observationContext)
            throws EncodingException {
        ObservationContextType oct = ocpt.addNewObservationContext();
        oct.addNewRole().set(encodeGML(observationContext.getRole()));
        oct.addNewRelatedObservation().set(encodeGML(observationContext.getRelatedObservation()));
    }

    private void setPhenomenonTime(OmObservation observation, OMObservationType xb) throws EncodingException {
        // set validTime
        Time phenomenonTime = observation.getPhenomenonTime();
        if (phenomenonTime.getGmlId() == null) {
            phenomenonTime.setGmlId(OmConstants.PHENOMENON_TIME_NAME + "_" + observation.getObservationID());
        }
        addPhenomenonTime(xb.addNewPhenomenonTime(), phenomenonTime);
    }

    private void setResultTime(OmObservation observation, OMObservationType xb) throws EncodingException {
        // set resultTime
        addResultTime(xb, observation);
    }

    private void setProcedure(OmObservation observation, OMObservationType xb) throws EncodingException {
        // set procedure
        addProcedure(xb.addNewProcedure(), observation.getObservationConstellation().getProcedure());
    }

    private void setParameter(OmObservation observation, OMObservationType xb) throws EncodingException {
        // set parameter
        if (observation.isSetParameter()) {
            addParameter(xb, observation.getParameter());
        }
    }

    private void setObservableProperty(OmObservation observation, OMObservationType xb) {
        // set observedProperty (phenomenon)
        AbstractPhenomenon observableProperty = observation.getObservationConstellation().getObservableProperty();
        xb.addNewObservedProperty().setHref(observableProperty.getIdentifier());
        if (observableProperty.isSetName()) {
            xb.getObservedProperty().setTitle(observableProperty.getFirstName().getValue());
        }
        if (observableProperty.isSetName() && observableProperty.getFirstName().isSetValue()) {
            xb.getObservedProperty().setTitle(observableProperty.getFirstName().getValue());
        }

    }

    private XmlObject encodeProcedureDescription(SosProcedureDescription<?> procedureDescription)
            throws EncodingException {
        OMProcessPropertyType procedure = OMProcessPropertyType.Factory.newInstance();
        addProcedure(procedure, procedureDescription);
        return procedure;
    }

    /**
     * Method that adds the procedure as reference or as encoded object to the
     * XML observation object
     *
     * @param procedure
     *            XML process type
     * @param procedureDescription
     *            SosProcedureDescription to be encoded
     * @throws EncodingException
     *             If an error occurs
     */
    private void addProcedure(OMProcessPropertyType procedure, AbstractFeature procedureDescription)
            throws EncodingException {
        if (!(procedureDescription instanceof SosProcedureDescriptionUnknownType)) {
            XmlObject encodedProcedure;
            if (procedureDescription instanceof SosProcedureDescription<?>) {
                encodedProcedure = encodeObjectToXml(procedureDescription.getDefaultElementEncoding(),
                        ((SosProcedureDescription<?>) procedureDescription).getProcedureDescription());
            } else {
                encodedProcedure =
                        encodeObjectToXml(procedureDescription.getDefaultElementEncoding(), procedureDescription);
            }
            // encode procedure or add reference

            if (encodedProcedure != null) {
                procedure.set(encodedProcedure);
            } else {
                procedure.setHref(procedureDescription.getIdentifier());
            }
        } else {
            procedure.setHref(procedureDescription.getIdentifier());
        }
        // set name as xlink:title
        if (procedure.isSetHref() && procedureDescription.isSetName()
                && procedureDescription.getFirstName().isSetValue()) {
            procedure.setTitle(procedureDescription.getFirstName().getValue());
        }
    }

    /**
     * Method to add the phenomenon time to the XML observation object
     *
     * @param timeObjectPropertyType
     *            XML time object from XML observation object
     * @param time
     *            SOS phenomenon time representation
     * @throws EncodingException
     *             If an error occurs
     */
    private void addPhenomenonTime(TimeObjectPropertyType timeObjectPropertyType, Time time) throws EncodingException {
        XmlObject xmlObject = encodeGML(time);
        XmlObject substitution = timeObjectPropertyType.addNewAbstractTimeObject()
                .substitute(GmlHelper.getGml321QnameForITime(time), xmlObject.schemaType());
        substitution.set(xmlObject);
    }

    /**
     * Method to add the result time to the XML observation object
     *
     * @param xbObs
     *            XML observation object
     * @param sosObservation
     *            SOS observation object
     * @throws EncodingException
     *             If an error occurs.
     */
    private void addResultTime(OMObservationType xbObs, OmObservation sosObservation) throws EncodingException {
        TimeInstant resultTime = sosObservation.getResultTime();
        Time phenomenonTime = sosObservation.getPhenomenonTime();
        // get result time from SOS result time representation
        if (sosObservation.getResultTime() != null) {
            if (resultTime.equals(phenomenonTime)) {
                xbObs.addNewResultTime().setHref("#" + phenomenonTime.getGmlId());
            } else {
                addResultTime(xbObs, resultTime);
            }
        } else {
            // if result time is not set, get result time from phenomenon time
            // representation
            if (phenomenonTime instanceof TimeInstant) {
                xbObs.addNewResultTime().setHref("#" + phenomenonTime.getGmlId());
            } else if (phenomenonTime instanceof TimePeriod) {
                TimeInstant rsTime = new TimeInstant(((TimePeriod) sosObservation.getPhenomenonTime()).getEnd());
                addResultTime(xbObs, rsTime);
            }
        }
    }

    /**
     * Method to add the result time to the XML observation object
     *
     * @param xbObs
     *            XML observation object
     * @param time
     *            SOS result time representation
     * @throws EncodingException
     *             If an error occurs.
     */
    private void addResultTime(OMObservationType xbObs, TimeInstant time) throws EncodingException {
        XmlObject xmlObject = encodeGML(time);
        xbObs.addNewResultTime().addNewTimeInstant().set(xmlObject);
        XmlObject substitution = xbObs.getResultTime().getTimeInstant()
                .substitute(GmlHelper.getGml321QnameForITime(time), xmlObject.schemaType());
        substitution.set(xmlObject);
    }

    private void setValidTime(OmObservation observation, OMObservationType xb) throws EncodingException {
        Time validTime = observation.getValidTime();
        if (validTime == null) {
            return;
        }
        if (validTime.getGmlId() == null) {
            validTime.setGmlId(OmConstants.VALID_TIME_NAME + "_" + observation.getObservationID());
        }
        xb.addNewValidTime().addNewTimePeriod().set(encodeGML(validTime));
    }

    private void addParameter(OMObservationType xbObservation, Collection<NamedValue<?>> parameter)
            throws EncodingException {
        for (NamedValue<?> namedValue : parameter) {
            xbObservation.addNewParameter().setNamedValue(createNamedValue(namedValue));
        }
    }

    /**
     * Method to add the featureOfInterest to the XML observation object
     *
     * @param feature
     *            SOS feature representation
     * @return Encoded featureOfInterest
     * @throws EncodingException
     *             If an error occurs.
     */
    private XmlObject encodeFeatureOfInterest(AbstractFeature feature) throws EncodingException {
        String namespace = null;
        if (!Strings.isNullOrEmpty(getDefaultFeatureEncodingNamespace())) {
            namespace = getDefaultFeatureEncodingNamespace();
        } else {
            namespace = feature.getDefaultElementEncoding();
        }
        return encodeGML(feature, EncodingContext.empty().with(XmlEncoderFlags.ENCODE_NAMESPACE, namespace));
    }

    /**
     * Method to encode a SOS NamedValue to an XmlBeans representation
     *
     * @param sosNamedValue
     *            SOS NamedValue
     * @return XmlBeans object
     * @throws EncodingException
     *             If an error occurs.
     */
    protected NamedValueType createNamedValue(NamedValue<?> sosNamedValue) throws EncodingException {
        // encode value (any)
        XmlObject namedValuePropertyValue = getNamedValueValue(sosNamedValue.getValue());
        if (namedValuePropertyValue != null) {
            NamedValueType xbNamedValue = NamedValueType.Factory.newInstance(getXmlOptions());
            // encode gml:ReferenceType
            XmlObject encodeObjectToXml = encodeGML(sosNamedValue.getName());
            xbNamedValue.addNewName().set(encodeObjectToXml);
            // set value (any)
            xbNamedValue.setValue(namedValuePropertyValue);
            return xbNamedValue;
        }
        return null;
    }

    /**
     * Get the XmlBeans object for SOS value
     *
     * @param value
     *            SOS value object
     * @return XmlBeans object
     * @throws EncodingException
     *             If an error occurs.
     */
    private XmlObject getNamedValueValue(Value<?> value) throws EncodingException {
        if (value.isSetValue()) {
            return value.accept(new NamedValueValueEncoder());
        }
        return null;
    }

    private void setResultQualities(OMObservationType xbObservation, OmObservation sosObservation)
            throws EncodingException {
        if (sosObservation.isSetResultQuality()) {
            encodeResultQualities(xbObservation, sosObservation.getResultQuality());
        } else if (sosObservation.getValue() instanceof SingleObservationValue) {
            encodeResultQualities(xbObservation,
                    ((SingleObservationValue<?>) sosObservation.getValue()).getQualityList());
        }
    }

    private void encodeResultQualities(OMObservationType xbObservation, Set<OmResultQuality> resultQuality)
            throws EncodingException {
        for (OmResultQuality quality : resultQuality) {
            AbstractDQElementDocument encodedQuality = (AbstractDQElementDocument) encodeObjectToXml(null, quality,
                    EncodingContext.of(XmlBeansEncodingFlags.DOCUMENT));
            DQElementPropertyType addNewResultQuality = xbObservation.addNewResultQuality();
            addNewResultQuality.setAbstractDQElement(encodedQuality.getAbstractDQElement());
            XmlHelper.substituteElement(addNewResultQuality.getAbstractDQElement(),
                    encodedQuality.getAbstractDQElement());
        }
    }

    protected XmlObject encodeXLINK(Object o) throws EncodingException {
        return encodeObjectToXml(W3CConstants.NS_XLINK, o);
    }

    protected XmlObject encodeXLINK(Object o, EncodingContext context) throws EncodingException {
        return encodeObjectToXml(W3CConstants.NS_XLINK, o, context);
    }

    protected XmlObject encodeGML(Object o) throws EncodingException {
        return encodeObjectToXml(GmlConstants.NS_GML_32, o);
    }

    protected XmlObject encodeGML(Object o, EncodingContext context) throws EncodingException {
        return encodeObjectToXml(GmlConstants.NS_GML_32, o, context);
    }

    protected XmlObject encodeSweCommon(Object o) throws EncodingException {
        return encodeObjectToXml(SweConstants.NS_SWE_20, o);
    }

    protected XmlObject encodeSweCommon(Object o, EncodingContext context) throws EncodingException {
        return encodeObjectToXml(SweConstants.NS_SWE_20, o, context);
    }

    private static String generateObservationGMLId() {
        return OBSERVATION_ID_PREFIX
                + JavaHelper.generateID(Double.toString(System.currentTimeMillis() * Math.random()));
    }

    private class NamedValueValueEncoder
            implements ValueVisitor<XmlObject, EncodingException> {

        @Override
        public XmlObject visit(BooleanValue value) {
            XmlBoolean xbBoolean = XmlBoolean.Factory.newInstance();
            xbBoolean.setBooleanValue(value.getValue());
            return xbBoolean;
        }

        @Override
        public XmlObject visit(CategoryValue value) throws EncodingException {
            return encodeGML(value, createHelperValues(value));
        }

        @Override
        public XmlObject visit(ComplexValue value) {
            return defaultValue(value);
        }

        @Override
        public XmlObject visit(CountValue value) {
            XmlInteger xmlInteger = XmlInteger.Factory.newInstance();
            xmlInteger.setStringValue(value.getValue().toString());
            return xmlInteger;
        }

        @Override
        public XmlObject visit(GeometryValue value) throws EncodingException {
            return encodeGML(value, createHelperValues(value));
        }

        @Override
        public XmlObject visit(HrefAttributeValue value) throws EncodingException {
            return encodeXLINK(value.getValue(), createHelperValues(value));
        }

        @Override
        public XmlObject visit(NilTemplateValue value) {
            return defaultValue(value);
        }

        @Override
        public XmlObject visit(QuantityValue value) throws EncodingException {
            return encodeGML(value, createHelperValues(value));
        }

        @Override
        public XmlObject visit(QuantityRangeValue value) throws EncodingException {
            return defaultValue(value);
        }

        @Override
        public XmlObject visit(ReferenceValue value) throws EncodingException {
            return encodeGML(value.getValue(), createHelperValues(value));
        }

        @Override
        public XmlObject visit(SweDataArrayValue value) {
            return defaultValue(value);
        }

        @Override
        public XmlObject visit(TVPValue value) {
            return defaultValue(value);
        }

        @Override
        public XmlObject visit(TextValue value) {
            XmlString xmlString = XmlString.Factory.newInstance();
            xmlString.setStringValue(value.getValue());
            return xmlString;
        }

        @Override
        public XmlObject visit(UnknownValue value) {
            return defaultValue(value);
        }

        @Override
        public XmlObject visit(TLVTValue value) throws EncodingException {
            return defaultValue(value);
        }

        @Override
        public XmlObject visit(CvDiscretePointCoverage value) throws EncodingException {
            return defaultValue(value);
        }

        @Override
        public XmlObject visit(MultiPointCoverage value) throws EncodingException {
            return defaultValue(value);
        }

        @Override
        public XmlObject visit(RectifiedGridCoverage value) throws EncodingException {
            return defaultValue(value);
        }

        @Override
        public XmlObject visit(ProfileValue value) throws EncodingException {
            return defaultValue(value);
        }

        @Override
        public XmlObject visit(TimeRangeValue value) throws EncodingException {
            return encodeObjectToXml(SweConstants.NS_SWE_20, value,
                                     EncodingContext.of(XmlBeansEncodingFlags.PROPERTY_TYPE));
        }

        @Override
        public XmlObject visit(XmlValue<?> value) throws EncodingException {
            if (value.getValue() instanceof XmlObject) {
                return (XmlObject) value.getValue();
            }
            return defaultValue(value);
        }

        private EncodingContext createHelperValues(Value<?> value) {
            return EncodingContext.of(XmlBeansEncodingFlags.PROPERTY_TYPE).with(XmlBeansEncodingFlags.GMLID,
                    JavaHelper.generateID(value.toString()));
        }

        private XmlObject defaultValue(Value<?> value) {
            LOG.warn("Can not encode named value value {}", value);
            return null;
        }
    }
}
