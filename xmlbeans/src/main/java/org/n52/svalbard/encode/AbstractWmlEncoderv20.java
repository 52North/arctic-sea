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

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.apache.xmlbeans.GDuration;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.isotc211.x2005.gmd.CIResponsiblePartyPropertyType;
import org.isotc211.x2005.gmd.CIResponsiblePartyType;
import org.joda.time.DateTime;
import org.n52.janmayen.http.MediaType;
import org.n52.shetland.iso.gmd.CiResponsibleParty;
import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.gml.CodeType;
import org.n52.shetland.ogc.gml.CodeWithAuthority;
import org.n52.shetland.ogc.gml.GmlConstants;
import org.n52.shetland.ogc.gml.ReferenceType;
import org.n52.shetland.ogc.gml.VerticalDatum;
import org.n52.shetland.ogc.gml.time.Time;
import org.n52.shetland.ogc.gml.time.TimeInstant;
import org.n52.shetland.ogc.gml.time.TimePeriod;
import org.n52.shetland.ogc.om.NamedValue;
import org.n52.shetland.ogc.om.ObservationStream;
import org.n52.shetland.ogc.om.OmObservation;
import org.n52.shetland.ogc.om.features.FeatureCollection;
import org.n52.shetland.ogc.om.features.samplingFeatures.AbstractSamplingFeature;
import org.n52.shetland.ogc.om.series.wml.ObservationProcess;
import org.n52.shetland.ogc.om.series.wml.WaterMLConstants;
import org.n52.shetland.ogc.om.series.wml.WmlMonitoringPoint;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.sos.response.GetObservationResponse;
import org.n52.shetland.util.CollectionHelper;
import org.n52.shetland.util.DateTimeFormatException;
import org.n52.shetland.util.DateTimeHelper;
import org.n52.shetland.util.JavaHelper;
import org.n52.shetland.w3c.Nillable;
import org.n52.shetland.w3c.xlink.Reference;
import org.n52.shetland.w3c.xlink.Referenceable;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.exception.UnsupportedEncoderInputException;
import org.n52.svalbard.util.CodingHelper;
import org.n52.svalbard.util.XmlHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3.x1999.xlink.ActuateType;
import org.w3.x1999.xlink.ShowType;
import org.w3.x1999.xlink.TypeType;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.vividsolutions.jts.geom.Geometry;

import net.opengis.gml.x32.VerticalDatumPropertyType;
import net.opengis.gml.x32.VerticalDatumType;
import net.opengis.om.x20.OMObservationDocument;
import net.opengis.om.x20.OMObservationType;
import net.opengis.samplingSpatial.x20.ShapeType;
import net.opengis.waterml.x20.CollectionDocument;
import net.opengis.waterml.x20.CollectionType;
import net.opengis.waterml.x20.MonitoringPointDocument;
import net.opengis.waterml.x20.MonitoringPointType;
import net.opengis.waterml.x20.ObservationProcessDocument;
import net.opengis.waterml.x20.ObservationProcessType;

/**
 * Abstract encoder class for WaterML 2.0
 *
 * @since 1.0.0
 */
public abstract class AbstractWmlEncoderv20
        extends AbstractOmEncoderv20
        implements ProcedureEncoder<XmlObject, Object> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractWmlEncoderv20.class);

    private static final Map<String, ImmutableMap<String, Set<String>>> SUPPORTED_PROCEDURE_DESCRIPTION_FORMATS =
            ImmutableMap.of(SosConstants.SOS, ImmutableMap.<String, Set<String>> builder()
                    .put(Sos2Constants.SERVICEVERSION, ImmutableSet.of(WaterMLConstants.NS_WML_20_PROCEDURE_ENCODING))
                    .build());

    private static final String PROCESS_ID_PREFIX = "process.";

    private static final String SF_ID_PREFIX = "sf_";

    private static final Set<EncoderKey> DEFAULT_ENCODER_KEYS = CollectionHelper
            .union(CodingHelper.encoderKeysForElements(WaterMLConstants.NS_WML_20, AbstractFeature.class), CodingHelper
                    .encoderKeysForElements(WaterMLConstants.NS_WML_20_PROCEDURE_ENCODING, ObservationProcess.class));

    protected static Set<EncoderKey> getDefaultEncoderKeys() {
        return Collections.unmodifiableSet(DEFAULT_ENCODER_KEYS);
    }

    @Override
    protected boolean convertEncodedProcedure() {
        return true;
    }

    @Override
    public boolean isObservationAndMeasurmentV20Type() {
        return true;
    }

    @Override
    public boolean shouldObservationsWithSameXBeMerged() {
        return true;
    }

    @Override
    public Set<String> getSupportedProcedureDescriptionFormats(String service, String version) {
        if (SUPPORTED_PROCEDURE_DESCRIPTION_FORMATS.containsKey(service)
                && SUPPORTED_PROCEDURE_DESCRIPTION_FORMATS.get(service).containsKey(version)) {
            return SUPPORTED_PROCEDURE_DESCRIPTION_FORMATS.get(service).get(version);
        }
        return Collections.emptySet();
    }

    @Override
    public MediaType getContentType() {
        return WaterMLConstants.WML_CONTENT_TYPE;
    }

    @Override
    public XmlObject encode(Object element, EncodingContext context) throws EncodingException {
        if (element instanceof ObservationProcess) {
            return createObservationProcess((ObservationProcess) element, context);
        } else if (element instanceof OmObservation) {
            return super.encode(element, context);
        } else if (element instanceof AbstractFeature) {
            return encodeAbstractFeature((AbstractFeature) element, context);
        } else {
            return super.encode(element, context);
        }
    }

    private XmlObject encodeAbstractFeature(AbstractFeature abstractFeature, EncodingContext context)
            throws UnsupportedEncoderInputException, EncodingException {
        if (abstractFeature instanceof OmObservation) {
            return super.encode(abstractFeature, context);
        } else {
            return createMonitoringPoint(abstractFeature);
        }
    }

    @Override
    public String getDefaultFeatureEncodingNamespace() {
        return WaterMLConstants.NS_WML_20;
    }

    @Override
    protected String getDefaultProcedureEncodingNamspace() {
        return WaterMLConstants.NS_WML_20_PROCEDURE_ENCODING;
    }

    @Override
    public void addNamespacePrefixToMap(Map<String, String> nameSpacePrefixMap) {
        super.addNamespacePrefixToMap(nameSpacePrefixMap);
        nameSpacePrefixMap.put(WaterMLConstants.NS_WML_20, WaterMLConstants.NS_WML_20_PREFIX);
    }

    @Override
    protected void addAddtitionalInformation(OMObservationType omot, OmObservation observation)
            throws EncodingException {
        // do nothing
    }

    /**
     * Encodes a SOS GetObservationResponse to a single WaterML 2.0 observation
     * or to a WaterML 1.0 ObservationCollection
     *
     * @param getObservationResonse
     *            SOS GetObservationResponse
     * @return Encoded response
     * @throws EncodingException
     *             If an error occurs
     */
    protected XmlObject createWmlGetObservationResponse(GetObservationResponse getObservationResonse)
            throws EncodingException {
        // TODO: set schemaLocation if final
        Map<CodeWithAuthority, String> gmlID4sfIdentifier = Maps.newHashMap();
        int sfIdCounter = 1;
        try {
            if (getObservationResonse.getObservationCollection() != null
                    && !getObservationResonse.getObservationCollection().hasNext()) {
                ObservationStream observations = getObservationResonse.getObservationCollection();
                OmObservation observation = observations.next();
                if (!observations.hasNext()) {
                    OMObservationDocument omObservationDoc =
                            OMObservationDocument.Factory.newInstance(getXmlOptions());
                    omObservationDoc.setOMObservation(encodeObservation(observation, gmlID4sfIdentifier, sfIdCounter));
                    sfIdCounter++;
                    return omObservationDoc;
                } else {
                    CollectionDocument xmlCollectionDoc = CollectionDocument.Factory.newInstance(getXmlOptions());
                    CollectionType wmlCollection = xmlCollectionDoc.addNewCollection();
                    wmlCollection.addNewObservationMember()
                            .setOMObservation(encodeObservation(observation, gmlID4sfIdentifier, sfIdCounter));
                    sfIdCounter++;
                    while (observations.hasNext()) {
                        wmlCollection.addNewObservationMember().setOMObservation(
                                encodeObservation(observations.next(), gmlID4sfIdentifier, sfIdCounter));
                        sfIdCounter++;
                    }
                    return xmlCollectionDoc;
                }

            } else {
                // TODO: HydrologieProfile-Exception
                throw new EncodingException("Combination does not exists!");
            }
        } catch (NoSuchElementException | OwsExceptionReport e) {
            throw new EncodingException(e);
        }
    }

    private OMObservationType encodeObservation(OmObservation observation,
            Map<CodeWithAuthority, String> gmlID4sfIdentifier, int sfIdCounter) throws EncodingException {
        EncodingContext foiContext;
        String gmlId;
        // FIXME CodeWithAuthority VS. String keys
        if (gmlID4sfIdentifier.containsKey(
                observation.getObservationConstellation().getFeatureOfInterest().getIdentifierCodeWithAuthority())) {
            gmlId = gmlID4sfIdentifier.get(
                    observation.getObservationConstellation().getFeatureOfInterest().getIdentifierCodeWithAuthority());
            foiContext = EncodingContext.of(XmlBeansEncodingFlags.EXIST_FOI_IN_DOC, true)
                    .with(XmlBeansEncodingFlags.GMLID, gmlId);
        } else {
            gmlId = SF_ID_PREFIX + sfIdCounter;
            gmlID4sfIdentifier.put(
                    observation.getObservationConstellation().getFeatureOfInterest().getIdentifierCodeWithAuthority(),
                    gmlId);
            foiContext = EncodingContext.of(XmlBeansEncodingFlags.EXIST_FOI_IN_DOC, Boolean.toString(false))
                    .with(XmlBeansEncodingFlags.GMLID, gmlId);
        }
        return (OMObservationType) encodeOmObservation(observation, foiContext);
    }

    /**
     * Creates a WaterML 2.0 MonitoringPoint XML object from SOS feature object
     *
     * @param absFeature
     *            SOS feature
     * @return WaterML 2.0 MonitoringPoint XML object
     * @throws EncodingException
     *             If an error occurs
     */
    protected XmlObject createMonitoringPoint(AbstractFeature absFeature) throws EncodingException {
        if (absFeature instanceof AbstractSamplingFeature) {
            AbstractSamplingFeature sampFeat = (AbstractSamplingFeature) absFeature;
            StringBuilder builder = new StringBuilder();
            builder.append("mp_");
            builder.append(JavaHelper.generateID(absFeature.getIdentifierCodeWithAuthority().getValue()));
            absFeature.setGmlId(builder.toString());

            MonitoringPointDocument monitoringPointDoc = MonitoringPointDocument.Factory.newInstance(getXmlOptions());
            if (sampFeat.isSetXml()) {
                try {
                    XmlObject feature = XmlObject.Factory.parse(sampFeat.getXml());
                    if (XmlHelper.getNamespace(feature).equals(WaterMLConstants.NS_WML_20)) {
                        if (feature instanceof MonitoringPointDocument) {
                            monitoringPointDoc = (MonitoringPointDocument) feature;
                        } else if (feature instanceof MonitoringPointType) {
                            monitoringPointDoc.setSFSpatialSamplingFeature((MonitoringPointType) feature);
                        }
                        XmlHelper.updateGmlIDs(monitoringPointDoc.getDomNode(), absFeature.getGmlId(), null);
                        return monitoringPointDoc;
                    }
                } catch (XmlException xmle) {
                    throw new EncodingException(
                            "Error while encoding GetFeatureOfInterest response, invalid samplingFeature description!",
                            xmle);
                }
            }
            MonitoringPointType mpt = monitoringPointDoc.addNewMonitoringPoint();
            // set gml:id
            mpt.setId(absFeature.getGmlId());

            if (sampFeat.isSetIdentifier()) {
                XmlObject xmlObject = encodeGML(sampFeat.getIdentifierCodeWithAuthority());
                if (xmlObject != null) {
                    mpt.addNewIdentifier().set(xmlObject);
                }
            }

            if (sampFeat.isSetName()) {
                for (CodeType sosName : sampFeat.getName()) {
                    mpt.addNewName().set(encodeGML(sosName));
                }
            }

            // set type
            // TODO: check if special definition
            // monitoringPoint.addNewType().setHref(sampFeat.getFeatureType());

            // set sampledFeatures
            // TODO: CHECK
            if (sampFeat.getSampledFeatures() != null && !sampFeat.getSampledFeatures().isEmpty()) {
                if (sampFeat.getSampledFeatures().size() == 1) {
                    XmlObject encodeObjectToXml =
                            encodeObjectToXml(GmlConstants.NS_GML_32, sampFeat.getSampledFeatures().get(0));
                    mpt.addNewSampledFeature().set(encodeObjectToXml);
                } else {
                    FeatureCollection featureCollection = new FeatureCollection();
                    featureCollection.setGmlId("sampledFeatures_" + absFeature.getGmlId());
                    for (AbstractFeature sampledFeature : sampFeat.getSampledFeatures()) {
                        featureCollection.addMember(sampledFeature);
                    }
                    XmlObject encodeObjectToXml = encodeGML(featureCollection);
                    mpt.addNewSampledFeature().set(encodeObjectToXml);
                }
            } else {
                mpt.addNewSampledFeature().setHref(GmlConstants.NIL_UNKNOWN);
            }

            if (sampFeat.isSetParameter()) {
                addParameter(mpt, sampFeat);
            }

            // set position
            ShapeType xbShape = mpt.addNewShape();
            Encoder<XmlObject, Geometry> encoder =
                    getEncoder(getEncoderKey(GmlConstants.NS_GML_32, sampFeat.getGeometry()));
            if (encoder != null) {
                XmlObject xmlObject = encoder.encode(sampFeat.getGeometry(),
                        new EncodingContext().with(XmlBeansEncodingFlags.GMLID, absFeature.getGmlId()));
                xbShape.addNewAbstractGeometry().set(xmlObject);
                XmlHelper.substituteElement(xbShape.getAbstractGeometry(), xmlObject);
            } else {
                throw new EncodingException("Error while encoding geometry for feature, needed encoder is missing!");
            }
            if (absFeature instanceof WmlMonitoringPoint) {
                addMonitoringPointValues(mpt, (WmlMonitoringPoint) absFeature);
            }
            sampFeat.wasEncoded();
            return monitoringPointDoc;
        }
        throw new UnsupportedEncoderInputException(this, absFeature);
    }

    /**
     * Creates an WaterML 2.0 ObservationProcess XML object from SOS
     * ObservationProcess object
     *
     * @param procedure
     *            SOS ObservationProcess
     * @param context
     *            Additional values
     * @return WaterML 2.0 ObservationProcess XML object
     * @throws EncodingException
     *             If an error occurs
     */
    protected ObservationProcessDocument createObservationProcess(ObservationProcess procedure,
            EncodingContext context) throws EncodingException {
        XmlObject encodedObject = null;
        try {
            if (procedure.isSetXml()) {
                encodedObject = XmlObject.Factory.parse(procedure.getXml());
                checkAndAddIdentifier(procedure, ((ObservationProcessDocument) encodedObject).getObservationProcess());
            } else {
                encodedObject = ObservationProcessDocument.Factory.newInstance();
                ObservationProcessType observationProcess =
                        ((ObservationProcessDocument) encodedObject).addNewObservationProcess();
                if (context.has(XmlBeansEncodingFlags.GMLID)) {
                    observationProcess.setId(PROCESS_ID_PREFIX + context.get(XmlBeansEncodingFlags.GMLID));
                } else {
                    observationProcess.setId(PROCESS_ID_PREFIX + JavaHelper.generateID(procedure.toString()));
                }
                if (procedure.isSetIdentifier()) {
                    observationProcess.addNewIdentifier()
                            .set(encodeGML(procedure.getIdentifierCodeWithAuthority()));
                }
                if (procedure.isSetName()) {
                    for (final CodeType sosName : procedure.getName()) {
                        observationProcess.addNewName().set(encodeGML(sosName));
                    }
                }
                addProcessType(observationProcess, procedure);
                addOriginatingProcess(observationProcess, procedure);
                addAggregatingDuration(observationProcess, procedure);
                addVerticalDatum(observationProcess, procedure);
                addComment(observationProcess, procedure);
                addProcessReference(observationProcess, procedure);
                addInput(observationProcess, procedure);
                addParameter(observationProcess, procedure);
            }
        } catch (final XmlException xmle) {
            throw new EncodingException(xmle);
        }
        try {
            LOGGER.debug("Encoded object {} is valid: {}", encodedObject.schemaType().toString(),
                    XmlHelper.validateDocument(encodedObject));
        } catch (DecodingException e) {
            throw new EncodingException(e);
        }
        return (ObservationProcessDocument) encodedObject;
    }

    private void checkAndAddIdentifier(ObservationProcess op, ObservationProcessType opt) throws EncodingException {
        if (op.isSetIdentifier() && !opt.isSetIdentifier()) {
            CodeWithAuthority codeWithAuthority = op.getIdentifierCodeWithAuthority();
            Encoder<?, CodeWithAuthority> encoder =
                    getEncoder(getEncoderKey(GmlConstants.NS_GML_32, codeWithAuthority));
            if (encoder != null) {
                XmlObject xmlObject = (XmlObject) encoder.encode(codeWithAuthority);
                opt.addNewIdentifier().set(xmlObject);
            } else {
                throw new EncodingException("Error while encoding geometry value, needed encoder is missing!");
            }
        }
    }

    /**
     * Adds processType value to WaterML 2.0 ObservationProcess XML object
     *
     * @param observationProcess
     *            WaterML 2.0 ObservationProcess XML object
     * @param procedure
     *            SOS ObservationProcess
     * @throws EncodingException
     *             If an error occurs
     */
    private void addProcessType(ObservationProcessType observationProcess, ObservationProcess procedure)
            throws EncodingException {
        if (procedure.isSetProcessType() && procedure.getProcessType().isSetHref()) {
            XmlObject referenceType = encodeReferenceType(procedure.getProcessType());
            if (referenceType != null) {
                observationProcess.addNewProcessType().set(referenceType);
            }
        } else {
            throw new EncodingException("Missing processType definition");
        }
    }

    /**
     * Adds OriginatingProcess value to WaterML 2.0 ObservationProcess XML
     * object
     *
     * @param observationProcess
     *            WaterML 2.0 ObservationProcess XML object
     * @param procedure
     *            SOS ObservationProcess
     * @throws EncodingException
     *             If an error occurs
     */
    private void addOriginatingProcess(ObservationProcessType observationProcess, ObservationProcess procedure)
            throws EncodingException {
        if (procedure.isSetOriginatingProcess()) {
            XmlObject referenceType = encodeReferenceType(procedure.getOriginatingProcess());
            if (referenceType != null) {
                observationProcess.addNewOriginatingProcess().set(referenceType);
            }
        }
    }

    /**
     * Adds AggregatingDuration value to WaterML 2.0 ObservationProcess XML
     * object
     *
     * @param observationProcess
     *            WaterML 2.0 ObservationProcess XML object
     * @param procedure
     *            SOS ObservationProcess
     */
    private void addAggregatingDuration(ObservationProcessType observationProcess, ObservationProcess procedure) {
        if (procedure.isSetAggregationDuration()) {
            observationProcess.setAggregationDuration(new GDuration(procedure.getAggregationDuration()));
        }
    }

    /**
     * Adds VerticalDatum value to WaterML 2.0 ObservationProcess XML object
     *
     * @param observationProcess
     *            WaterML 2.0 ObservationProcess XML object
     * @param procedure
     *            SOS ObservationProcess
     * @throws EncodingException
     *             If an error occurs
     */
    private void addVerticalDatum(ObservationProcessType observationProcess, ObservationProcess procedure)
            throws EncodingException {
        if (procedure.isSetVerticalDatum()) {
            XmlObject referenceType = encodeReferenceType(procedure.getVerticalDatum());
            if (referenceType != null) {
                observationProcess.addNewVerticalDatum().set(referenceType);
            }
        }
    }

    private void addVerticalDatum(MonitoringPointType mpt, List<Referenceable<VerticalDatum>> verticalDatums)
            throws EncodingException {
        for (Referenceable<VerticalDatum> verticalDatum : verticalDatums) {
            VerticalDatumPropertyType vdpt = mpt.addNewVerticalDatum();
            if (verticalDatum.isReference()) {
                Reference reference = verticalDatum.getReference();
                if (reference.getActuate().isPresent()) {
                    vdpt.setActuate(ActuateType.Enum.forString(reference.getActuate().get()));
                }
                if (reference.getArcrole().isPresent()) {
                    vdpt.setHref(reference.getArcrole().get());
                }
                if (reference.getHref().isPresent()) {
                    vdpt.setHref(reference.getHref().get().toString());
                }
                if (reference.getRole().isPresent()) {
                    vdpt.setRole(reference.getRole().get());
                }
                if (reference.getShow().isPresent()) {
                    vdpt.setShow(ShowType.Enum.forString(reference.getShow().get()));
                }
                if (reference.getTitle().isPresent()) {
                    vdpt.setTitle(reference.getTitle().get());
                }
                if (reference.getType().isPresent()) {
                    vdpt.setType(TypeType.Enum.forString(reference.getType().get()));
                }
            } else {
                if (verticalDatum.isInstance()) {
                    Nillable<VerticalDatum> nillable = verticalDatum.getInstance();
                    if (nillable.isPresent()) {
                        XmlObject xml = encodeGML(nillable.get());
                        if (xml != null && xml instanceof VerticalDatumType) {
                            vdpt.setVerticalDatum((VerticalDatumType) xml);
                        } else {
                            vdpt.setNil();
                            vdpt.setNilReason(Nillable.missing().get());
                        }
                    } else {
                        vdpt.setNil();
                        if (nillable.hasReason()) {
                            vdpt.setNilReason(nillable.getNilReason().get());
                        } else {
                            vdpt.setNilReason(Nillable.missing().get());
                        }
                    }
                }
            }
        }
    }

    /**
     * Adds Comment value to WaterML 2.0 ObservationProcess XML object
     *
     * @param observationProcess
     *            WaterML 2.0 ObservationProcess XML object
     * @param procedure
     *            SOS ObservationProcess
     */
    private void addComment(ObservationProcessType observationProcess, ObservationProcess procedure) {
        if (procedure.isSetComments()) {
            for (String comment : procedure.getComments()) {
                if (comment != null && !comment.isEmpty()) {
                    observationProcess.addComment(comment);
                }
            }
        }
    }

    /**
     * Adds ProcessReference value to WaterML 2.0 ObservationProcess XML object
     *
     * @param observationProcess
     *            WaterML 2.0 ObservationProcess XML object
     * @param procedure
     *            SOS ObservationProcess
     * @throws EncodingException
     *             If an error occurs
     */
    private void addProcessReference(ObservationProcessType observationProcess, ObservationProcess procedure)
            throws EncodingException {
        if (procedure.isSetProcessReference()) {
            XmlObject referenceType = encodeReferenceType(procedure.getProcessReference());
            if (referenceType != null) {
                observationProcess.addNewProcessReference().set(referenceType);
            }
        }
    }

    /**
     * Adds Input value to WaterML 2.0 ObservationProcess XML object
     *
     * @param observationProcess
     *            WaterML 2.0 ObservationProcess XML object
     * @param procedure
     *            SOS ObservationProcess
     * @throws EncodingException
     *             If an error occurs
     */
    private void addInput(ObservationProcessType observationProcess, ObservationProcess procedure)
            throws EncodingException {
        if (procedure.isSetInputs()) {
            for (ReferenceType sosReferenceType : procedure.getInputs()) {
                XmlObject referenceType = encodeReferenceType(sosReferenceType);
                if (referenceType != null) {
                    observationProcess.addNewInput().set(referenceType);
                }
            }
        }
    }

    /**
     * Adds Parameter value to WaterML 2.0 ObservationProcess XML object
     *
     * @param observationProcess
     *            WaterML 2.0 ObservationProcess XML object
     * @param procedure
     *            SOS ObservationProcess
     * @throws EncodingException
     *             If an error occurs
     */
    private void addParameter(ObservationProcessType observationProcess, ObservationProcess procedure)
            throws EncodingException {
        if (procedure.isSetParameters()) {
            List<NamedValue<?>> parameters = procedure.getParameters();
            for (NamedValue<?> sosNamedValue : parameters) {
                XmlObject namedValue = createNamedValue(sosNamedValue);
                if (namedValue != null) {
                    observationProcess.addNewParameter().addNewNamedValue().set(namedValue);
                }
            }
        }
    }

    /**
     * Adds parameter values to WaterML 2.0 XML MonitoringPoint object from
     * SosSamplingFeature
     *
     * @param monitoringPoint
     *            WaterML 2.0 XML MonitoringPoint object
     * @param sampFeat
     *            SosSamplingFeature
     * @throws EncodingException
     *             If an error occurs
     */
    private void addParameter(MonitoringPointType monitoringPoint, AbstractSamplingFeature sampFeat)
            throws EncodingException {
        for (NamedValue<?> namedValue : sampFeat.getParameters()) {
            XmlObject encodeObjectToXml = createNamedValue(namedValue);
            if (encodeObjectToXml != null) {
                monitoringPoint.addNewParameter().addNewNamedValue().set(encodeObjectToXml);
            }
        }
    }

    /**
     * Creates a XML ReferenceType object from SOS ReferenceType object
     *
     * @param sosReferenceType
     *            SOS ReferenceType object
     * @return XML ReferenceType object
     * @throws EncodingException
     *             If an error occurs
     */
    private XmlObject encodeReferenceType(ReferenceType sosReferenceType) throws EncodingException {
        Encoder<XmlObject, ReferenceType> encoder =
                getEncoder(getEncoderKey(GmlConstants.NS_GML_32, sosReferenceType));
        if (encoder != null) {
            return encoder.encode(sosReferenceType);
        } else {
            throw new EncodingException("Error while encoding referenceType, needed encoder is missing!");
        }

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

    private void addMonitoringPointValues(MonitoringPointType mpt, WmlMonitoringPoint monitoringPoint)
            throws EncodingException {
        if (monitoringPoint.hasRelatedParty()) {
            addRelatedParty(mpt, monitoringPoint.getRelatedParty());
        }
        if (monitoringPoint.hasMonitoringType()) {
            addMonitoringType(mpt, monitoringPoint.getMonitoringType());
        }
        if (monitoringPoint.hasDescriptionReference()) {
            addDescriptionReference(mpt, monitoringPoint.getDescriptionReference());
        }
        if (monitoringPoint.hasVerticalDatum()) {
            addVerticalDatum(mpt, monitoringPoint.getVerticalDatum());
        }
    }

    private void addRelatedParty(MonitoringPointType mpt, List<Referenceable<CiResponsibleParty>> relatedParties)
            throws EncodingException {
        for (Referenceable<CiResponsibleParty> relatedParty : relatedParties) {
            CIResponsiblePartyPropertyType citppt = mpt.addNewRelatedParty();
            if (relatedParty.isReference()) {
                Reference reference = relatedParty.getReference();
                if (reference.getActuate().isPresent()) {
                    citppt.setActuate(ActuateType.Enum.forString(reference.getActuate().get()));
                }
                if (reference.getArcrole().isPresent()) {
                    citppt.setHref(reference.getArcrole().get());
                }
                if (reference.getHref().isPresent()) {
                    citppt.setHref(reference.getHref().get().toString());
                }
                if (reference.getRole().isPresent()) {
                    citppt.setRole(reference.getRole().get());
                }
                if (reference.getShow().isPresent()) {
                    citppt.setShow(ShowType.Enum.forString(reference.getShow().get()));
                }
                if (reference.getTitle().isPresent()) {
                    citppt.setTitle(reference.getTitle().get());
                }
                if (reference.getType().isPresent()) {
                    citppt.setType(TypeType.Enum.forString(reference.getType().get()));
                }
            } else {
                if (relatedParty.isInstance()) {
                    Nillable<CiResponsibleParty> nillable = relatedParty.getInstance();
                    if (nillable.isPresent()) {
                        XmlObject xml = encodeObjectToXml(nillable.get().getDefaultElementEncoding(), nillable.get());
                        if (xml != null && xml instanceof CIResponsiblePartyType) {
                            citppt.setCIResponsibleParty((CIResponsiblePartyType) xml);
                        } else {
                            citppt.setNil();
                            citppt.setNilReason(Nillable.missing().get());
                        }
                    } else {
                        citppt.setNil();
                        if (nillable.hasReason()) {
                            citppt.setNilReason(nillable.getNilReason().get());
                        } else {
                            citppt.setNilReason(Nillable.missing().get());
                        }
                    }
                }
            }
        }
    }

    private void addMonitoringType(MonitoringPointType mpt, List<ReferenceType> monitoringType)
            throws EncodingException {
        for (ReferenceType referenceType : monitoringType) {
            mpt.addNewMonitoringType().set(encodeGML(referenceType));
        }
    }

    private void addDescriptionReference(MonitoringPointType mpt, List<ReferenceType> descriptionReference)
            throws EncodingException {
        for (ReferenceType referenceType : descriptionReference) {
            mpt.addNewDescriptionReference2().set(encodeGML(referenceType));
        }
    }
}
