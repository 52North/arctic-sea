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
package org.n52.svalbard.encode;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.apache.xmlbeans.XmlBoolean;
import org.apache.xmlbeans.XmlInteger;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlString;
import org.joda.time.DateTime;
import org.n52.janmayen.http.MediaType;
import org.n52.shetland.ogc.SupportedType;
import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.gml.GenericMetaData;
import org.n52.shetland.ogc.gml.GmlConstants;
import org.n52.shetland.ogc.gml.time.Time;
import org.n52.shetland.ogc.gml.time.TimeInstant;
import org.n52.shetland.ogc.gml.time.TimePeriod;
import org.n52.shetland.ogc.om.MultiObservationValues;
import org.n52.shetland.ogc.om.ObservationStream;
import org.n52.shetland.ogc.om.OmCompositePhenomenon;
import org.n52.shetland.ogc.om.OmConstants;
import org.n52.shetland.ogc.om.OmObservableProperty;
import org.n52.shetland.ogc.om.OmObservation;
import org.n52.shetland.ogc.om.SingleObservationValue;
import org.n52.shetland.ogc.om.StreamingValue;
import org.n52.shetland.ogc.om.features.samplingFeatures.AbstractSamplingFeature;
import org.n52.shetland.ogc.om.values.BooleanValue;
import org.n52.shetland.ogc.om.values.CategoryValue;
import org.n52.shetland.ogc.om.values.CountValue;
import org.n52.shetland.ogc.om.values.GeometryValue;
import org.n52.shetland.ogc.om.values.QuantityValue;
import org.n52.shetland.ogc.om.values.TextValue;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.shetland.ogc.ows.extension.Extension;
import org.n52.shetland.ogc.ows.extension.Extensions;
import org.n52.shetland.ogc.sos.Sos1Constants;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.sos.response.AbstractObservationResponse;
import org.n52.shetland.ogc.sos.response.GetObservationByIdResponse;
import org.n52.shetland.ogc.sos.response.GetObservationResponse;
import org.n52.shetland.ogc.swe.SweAbstractDataComponent;
import org.n52.shetland.ogc.swe.SweConstants;
import org.n52.shetland.ogc.swe.SweDataArray;
import org.n52.shetland.util.CollectionHelper;
import org.n52.shetland.util.OMHelper;
import org.n52.shetland.util.ReferencedEnvelope;
import org.n52.shetland.w3c.SchemaLocation;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.exception.UnsupportedEncoderInputException;
import org.n52.svalbard.util.CodingHelper;
import org.n52.svalbard.util.GmlHelper;
import org.n52.svalbard.util.N52XmlHelper;
import org.n52.svalbard.util.SweHelper;
import org.n52.svalbard.util.XmlHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import net.opengis.om.x10.CategoryObservationDocument;
import net.opengis.om.x10.CategoryObservationType;
import net.opengis.om.x10.CountObservationDocument;
import net.opengis.om.x10.CountObservationType;
import net.opengis.om.x10.GeometryObservationDocument;
import net.opengis.om.x10.GeometryObservationType;
import net.opengis.om.x10.MeasurementDocument;
import net.opengis.om.x10.MeasurementType;
import net.opengis.om.x10.ObservationCollectionDocument;
import net.opengis.om.x10.ObservationCollectionType;
import net.opengis.om.x10.ObservationDocument;
import net.opengis.om.x10.ObservationPropertyType;
import net.opengis.om.x10.ObservationType;
import net.opengis.om.x10.TruthObservationDocument;
import net.opengis.om.x10.TruthObservationType;

/**
 * @since 1.0.0
 *
 */
public class OmEncoderv100
        extends
        AbstractXmlEncoder<XmlObject, Object>
        implements
        ObservationEncoder<XmlObject, Object> {

    private static final Logger LOGGER = LoggerFactory.getLogger(OmEncoderv100.class);

    private static final Set<SupportedType> SUPPORTED_TYPES = ImmutableSet.<SupportedType> builder()
            .add(OmConstants.OBS_TYPE_SWE_ARRAY_OBSERVATION_TYPE)
            // .add(OmConstants.OBS_TYPE_COMPLEX_OBSERVATION_TYPE)
            // .add(OmConstants.OBS_TYPE_GEOMETRY_OBSERVATION_TYPE)
            .add(OmConstants.OBS_TYPE_CATEGORY_OBSERVATION_TYPE).add(OmConstants.OBS_TYPE_COUNT_OBSERVATION_TYPE)
            .add(OmConstants.OBS_TYPE_MEASUREMENT_TYPE).add(OmConstants.OBS_TYPE_TEXT_OBSERVATION_TYPE)
            .add(OmConstants.OBS_TYPE_TRUTH_OBSERVATION_TYPE).build();

    // TODO: change to correct conformance class
    private static final Set<String> CONFORMANCE_CLASSES =
            ImmutableSet.of("http://www.opengis.net/spec/OMXML/1.0/conf/measurement",
                    "http://www.opengis.net/spec/OMXML/1.0/conf/categoryObservation",
                    "http://www.opengis.net/spec/OMXML/1.0/conf/countObservation",
                    "http://www.opengis.net/spec/OMXML/1.0/conf/truthObservation",
                    "http://www.opengis.net/spec/OMXML/1.0/conf/geometryObservation",
                    "http://www.opengis.net/spec/OMXML/1.0/conf/textObservation");

    private static final Map<String, Map<String, Set<String>>> SUPPORTED_RESPONSE_FORMATS =
            Collections.singletonMap(SosConstants.SOS, Collections.singletonMap(Sos1Constants.SERVICEVERSION,
                    (Set<String>) ImmutableSet.of(OmConstants.CONTENT_TYPE_OM.toString())));

    private static final Set<EncoderKey> ENCODER_KEYS = CollectionHelper.union(
            CodingHelper.encoderKeysForElements(OmConstants.NS_OM, OmObservation.class, GetObservationResponse.class,
                    GetObservationByIdResponse.class),
            CodingHelper.encoderKeysForElements(OmConstants.CONTENT_TYPE_OM.toString(), OmObservation.class,
                    GetObservationResponse.class, GetObservationByIdResponse.class));
    private static final String RESULT_TIME_ID_PREFIX = "resultTime_";
    private static final String OBSERVATION_ID_PREFIX = "o_";
    private SweHelper sweHelper;

    public OmEncoderv100() {
        LOGGER.debug("Encoder for the following keys initialized successfully: {}!",
                Joiner.on(", ").join(ENCODER_KEYS));
    }

    @Inject
    @SuppressFBWarnings({"EI_EXPOSE_REP2"})
    public void setSweHelper(SweHelper sweHelper) {
        this.sweHelper = sweHelper;
    }

    @Override
    public Set<EncoderKey> getKeys() {
        return Collections.unmodifiableSet(ENCODER_KEYS);
    }

    @Override
    public Set<SupportedType> getSupportedTypes() {
        return Collections.unmodifiableSet(SUPPORTED_TYPES);
    }

    @Override
    public Map<String, Set<SupportedType>> getSupportedResponseFormatObservationTypes() {
        return Collections.singletonMap(OmConstants.CONTENT_TYPE_OM.toString(), getSupportedTypes());
    }

    @Override
    public Set<String> getConformanceClasses(String service, String version) {
        if (SosConstants.SOS.equals(service) && Sos2Constants.SERVICEVERSION.equals(version)) {
            return Collections.unmodifiableSet(CONFORMANCE_CLASSES);
        }
        return Collections.emptySet();
    }

    @Override
    public void addNamespacePrefixToMap(Map<String, String> nameSpacePrefixMap) {
        nameSpacePrefixMap.put(OmConstants.NS_OM, OmConstants.NS_OM_PREFIX);
    }

    @Override
    public boolean isObservationAndMeasurmentV20Type() {
        return false;
    }

    @Override
    public Set<String> getSupportedResponseFormats(String service, String version) {
        if (SUPPORTED_RESPONSE_FORMATS.get(service) != null
                && SUPPORTED_RESPONSE_FORMATS.get(service).get(version) != null) {
            return SUPPORTED_RESPONSE_FORMATS.get(service).get(version);
        }
        return Collections.emptySet();
    }

    @Override
    public boolean shouldObservationsWithSameXBeMerged() {
        return true;
    }

    @Override
    public boolean supportsResultStreamingForMergedValues() {
        return false;
    }

    @Override
    public MediaType getContentType() {
        return OmConstants.CONTENT_TYPE_OM;
    }

    @Override
    public Set<SchemaLocation> getSchemaLocations() {
        return Sets.newHashSet(OmConstants.OM_100_SCHEMA_LOCATION);
    }

    @Override
    public XmlObject encode(Object element, EncodingContext additionalValues)
            throws EncodingException {
        XmlObject encodedObject = null;
        if (element instanceof OmObservation) {
            encodedObject = createObservation((OmObservation) element, additionalValues);
        } else if (element instanceof GetObservationResponse) {
            GetObservationResponse response = (GetObservationResponse) element;
            encodedObject =
                    createObservationCollection(response);
        } else if (element instanceof GetObservationByIdResponse) {
            GetObservationByIdResponse response = (GetObservationByIdResponse) element;
            encodedObject =
                    createObservationCollection(response);
        } else {
            throw new UnsupportedEncoderInputException(this, element);
        }
        XmlHelper.validateDocument(encodedObject, EncodingException::new);
        return encodedObject;
    }

    private XmlObject createObservation(OmObservation sosObservation, EncodingContext additionalValues)
            throws EncodingException {
        String observationType = checkObservationType(sosObservation);
        if (null != observationType) {
            switch (observationType) {
                case OmConstants.OBS_TYPE_MEASUREMENT:
                    return createMeasurement(sosObservation, additionalValues);
                case OmConstants.OBS_TYPE_CATEGORY_OBSERVATION:
                    return createCategoryObservation(sosObservation, additionalValues);
                case OmConstants.OBS_TYPE_COUNT_OBSERVATION:
                    return createCountObservation(sosObservation, additionalValues);
                case OmConstants.OBS_TYPE_TRUTH_OBSERVATION:
                    return createTruthObservation(sosObservation, additionalValues);
                case OmConstants.OBS_TYPE_GEOMETRY_OBSERVATION:
                    return createGeometryObservation(sosObservation, additionalValues);
                default:
                    return createOmObservation(sosObservation, additionalValues);
            }
        } else {
            return createOmObservation(sosObservation, additionalValues);
        }
    }

    private String checkObservationType(OmObservation sosObservation)
            throws EncodingException {
        if (sosObservation.isSetResultType()) {
            return sosObservation.getResultType();
        } else if (sosObservation.getValue() instanceof SingleObservationValue) {
            SingleObservationValue<?> observationValue = (SingleObservationValue<?>) sosObservation.getValue();
            return OMHelper.getObservationTypeFor(observationValue.getValue());
        }
        return OmConstants.OBS_TYPE_OBSERVATION;
    }

    private XmlObject createObservationCollection(AbstractObservationResponse response)
            throws EncodingException {
        ObservationStream sosObservationCollectionIterable = response.getObservationCollection();
        String resultModel =  response.getResultModel();
        ObservationCollectionDocument xbObservationCollectionDoc =
                ObservationCollectionDocument.Factory.newInstance(getXmlOptions());
        ObservationCollectionType xbObservationCollection = xbObservationCollectionDoc.addNewObservationCollection();
        xbObservationCollection.setId(SosConstants.OBS_COL_ID_PREFIX + new DateTime().getMillis());
        if (response.hasExtensions()) {
            createMetadataProperty(xbObservationCollection, response.getExtensions());
        }
        if (sosObservationCollectionIterable != null) {
            List<OmObservation> sosObservationCollection = new LinkedList<>();
            try {
                sosObservationCollectionIterable.forEachRemaining(sosObservationCollection::add);
                ReferencedEnvelope sosEnvelope = getEnvelope(sosObservationCollection);
                if (sosEnvelope.isSetEnvelope()) {
                    Encoder<XmlObject, ReferencedEnvelope> envEncoder = getEncoder(GmlConstants.NS_GML, sosEnvelope);
                    xbObservationCollection.addNewBoundedBy().addNewEnvelope().set(envEncoder.encode(sosEnvelope));
                }
                for (OmObservation sosObservation : sosObservationCollection) {
                    String observationType = checkObservationType(sosObservation);
                    if (Strings.isNullOrEmpty(resultModel)
                            || !Strings.isNullOrEmpty(resultModel) && observationType.equals(resultModel)) {
                        if (sosObservation.getValue() instanceof StreamingValue) {
                            StreamingValue<?> streamingValue = (StreamingValue<?>) sosObservation.getValue();
                            while (streamingValue.hasNext()) {
                                xbObservationCollection.addNewMember()
                                        .set(createObservation(streamingValue.next(), null));
                            }

                        } else {
                            xbObservationCollection.addNewMember().set(createObservation(sosObservation, null));
                        }
                    } else {
                        throw new EncodingException(
                                "The requested resultModel '%s' is invalid for the resulting observations!",
                                OMHelper.getEncodedResultModelFor(resultModel));
                    }
                }
            } catch (OwsExceptionReport owse) {
                throw new EncodingException(owse);
            }
        } else {
            ObservationPropertyType xbObservation = xbObservationCollection.addNewMember();
            xbObservation.setHref(GmlConstants.NIL_INAPPLICABLE);
        }
        XmlHelper.makeGmlIdsUnique(xbObservationCollectionDoc.getDomNode());
        N52XmlHelper.setSchemaLocationsToDocument(xbObservationCollectionDoc,
                Sets.newHashSet(N52XmlHelper.getSchemaLocationForSOS100(), N52XmlHelper.getSchemaLocationForOM100(),
                        N52XmlHelper.getSchemaLocationForSA100()));
        return xbObservationCollectionDoc;
    }

    private ReferencedEnvelope getEnvelope(List<OmObservation> sosObservationCollection) {
        ReferencedEnvelope sosEnvelope = new ReferencedEnvelope();
        sosObservationCollection.stream()
                .map(o -> (AbstractSamplingFeature) o.getObservationConstellation().getFeatureOfInterest())
                .forEach(f -> {
                    if (f.isSetGeometry()) {
                        sosEnvelope.setSrid(f.getGeometry().getSRID());
                        sosEnvelope.expandToInclude(f.getGeometry().getEnvelopeInternal());
                    }
                });
        return sosEnvelope;
    }

    private XmlObject createMeasurement(OmObservation sosObservation, EncodingContext additionalValues)
            throws EncodingException {
        MeasurementDocument xbMeasurementDoc = MeasurementDocument.Factory.newInstance(getXmlOptions());
        MeasurementType xbObs = xbMeasurementDoc.addNewMeasurement();
        addValuesToObservation(xbObs, sosObservation, additionalValues);
        addSingleObservationToResult(xbObs.addNewResult(), sosObservation);
        return xbMeasurementDoc;
    }

    private XmlObject createCategoryObservation(OmObservation sosObservation, EncodingContext additionalValues)
            throws EncodingException {
        CategoryObservationDocument xbCategoryObservationDoc =
                CategoryObservationDocument.Factory.newInstance(getXmlOptions());
        CategoryObservationType xbObs = xbCategoryObservationDoc.addNewCategoryObservation();
        addValuesToObservation(xbObs, sosObservation, additionalValues);
        addSingleObservationToResult(xbObs.addNewResult(), sosObservation);
        return xbCategoryObservationDoc;
    }

    private XmlObject createCountObservation(OmObservation sosObservation, EncodingContext additionalValues)
            throws EncodingException {
        CountObservationDocument xbCountObservationDoc = CountObservationDocument.Factory.newInstance(getXmlOptions());
        CountObservationType xbObs = xbCountObservationDoc.addNewCountObservation();
        addValuesToObservation(xbObs, sosObservation, additionalValues);
        addSingleObservationToResult(xbObs.addNewResult(), sosObservation);
        return xbCountObservationDoc;
    }

    private XmlObject createTruthObservation(OmObservation sosObservation, EncodingContext additionalValues)
            throws EncodingException {
        TruthObservationDocument xbTruthObservationDoc = TruthObservationDocument.Factory.newInstance(getXmlOptions());
        TruthObservationType xbObs = xbTruthObservationDoc.addNewTruthObservation();
        addValuesToObservation(xbObs, sosObservation, additionalValues);
        addSingleObservationToResult(xbObs.addNewResult(), sosObservation);
        return xbTruthObservationDoc;
    }

    private XmlObject createGeometryObservation(OmObservation sosObservation, EncodingContext additionalValues)
            throws EncodingException {
        GeometryObservationDocument xbGeometryObservationDoc =
                GeometryObservationDocument.Factory.newInstance(getXmlOptions());
        GeometryObservationType xbObs = xbGeometryObservationDoc.addNewGeometryObservation();
        addValuesToObservation(xbObs, sosObservation, additionalValues);
        addSingleObservationToResult(xbObs.addNewResult(), sosObservation);
        return xbGeometryObservationDoc;
    }

    private XmlObject createOmObservation(OmObservation sosObservation, EncodingContext additionalValues)
            throws EncodingException {
        ObservationDocument xbObservationDoc = ObservationDocument.Factory.newInstance(getXmlOptions());
        ObservationType xbObs = xbObservationDoc.addNewObservation();
        List<OmObservableProperty> phenComponents = addValuesToObservation(xbObs, sosObservation, additionalValues);
        addResultToObservation(xbObs.addNewResult(), sosObservation, phenComponents);
        return xbObservationDoc;
    }

    private List<OmObservableProperty> addValuesToObservation(ObservationType xbObs, OmObservation sosObservation,
            EncodingContext additionalValues)
            throws EncodingException {
        xbObs.setId(OBSERVATION_ID_PREFIX + Long.toString(System.currentTimeMillis()));
        if (!sosObservation.isSetObservationID()) {
            sosObservation.setObservationID(xbObs.getId().replace(OBSERVATION_ID_PREFIX, ""));
        }
        String observationID = sosObservation.getObservationID();
        // set samplingTime
        Time samplingTime = sosObservation.getPhenomenonTime();
        if (samplingTime.getGmlId() == null) {
            samplingTime.setGmlId(OmConstants.PHENOMENON_TIME_NAME + "_" + observationID);
        }
        addSamplingTime(xbObs, samplingTime);
        // set resultTime
        addResultTime(xbObs, sosObservation);

        // set procedure
        xbObs.addNewProcedure().setHref(sosObservation.getObservationConstellation().getProcedure().getIdentifier());
        // set observedProperty (phenomenon)
        List<OmObservableProperty> phenComponents = null;
        if (sosObservation.getObservationConstellation().getObservableProperty() instanceof OmObservableProperty) {
            xbObs.addNewObservedProperty()
                    .setHref(sosObservation.getObservationConstellation().getObservableProperty().getIdentifier());
            phenComponents = new ArrayList<>(1);
            phenComponents
                    .add((OmObservableProperty) sosObservation.getObservationConstellation().getObservableProperty());
        } else if (sosObservation.getObservationConstellation()
                .getObservableProperty() instanceof OmCompositePhenomenon) {
            OmCompositePhenomenon compPhen =
                    (OmCompositePhenomenon) sosObservation.getObservationConstellation().getObservableProperty();
            xbObs.addNewObservedProperty().setHref(compPhen.getIdentifier());
            phenComponents = compPhen.getPhenomenonComponents();
        }
        // set feature
        addFeatureOfInterest(xbObs, sosObservation.getObservationConstellation().getFeatureOfInterest());
        return phenComponents;
    }

    private void addSamplingTime(ObservationType xbObservation, Time iTime)
            throws EncodingException {
        XmlObject xmlObject = encodeObjectToXml(GmlConstants.NS_GML, iTime);
        XmlObject substitution = xbObservation.addNewSamplingTime().addNewTimeObject()
                .substitute(GmlHelper.getGml311QnameForITime(iTime), xmlObject.schemaType());
        substitution.set(xmlObject);
    }

    private void addResultTime(ObservationType xbObs, OmObservation sosObservation)
            throws EncodingException {
        Time phenomenonTime = sosObservation.getPhenomenonTime();
        if (sosObservation.isSetResultTime()) {
            if (sosObservation.getResultTime().equals(phenomenonTime)) {
                xbObs.addNewResultTime().setHref("#".concat(phenomenonTime.getGmlId()));
            } else {
                TimeInstant resultTime = sosObservation.getResultTime();
                if (!resultTime.isSetGmlId()) {
                    resultTime.setGmlId(RESULT_TIME_ID_PREFIX.concat(sosObservation.getObservationID()));
                }
                addResultTime(xbObs, resultTime);
            }
        } else {
            if (phenomenonTime instanceof TimeInstant) {
                xbObs.addNewResultTime().setHref("#".concat(phenomenonTime.getGmlId()));
            } else if (phenomenonTime instanceof TimePeriod) {
                TimeInstant resultTime = new TimeInstant(((TimePeriod) sosObservation.getPhenomenonTime()).getEnd());
                resultTime.setGmlId(RESULT_TIME_ID_PREFIX + sosObservation.getObservationID());
                addResultTime(xbObs, resultTime);
            }
        }
    }

    private void addResultTime(ObservationType xbObs, TimeInstant iTime)
            throws EncodingException {
        XmlObject xmlObject = encodeObjectToXml(GmlConstants.NS_GML, iTime);
        XmlObject substitution = xbObs.addNewResultTime().addNewTimeObject()
                .substitute(GmlHelper.getGml311QnameForITime(iTime), xmlObject.schemaType());
        substitution.set(xmlObject);
    }

    private void addResultToObservation(XmlObject xbResult, OmObservation sosObservation,
            List<OmObservableProperty> phenComponents)
            throws EncodingException {
        // TODO if OM_SWEArrayObservation and get ResultEncoding and
        // ResultStructure exists,
        if (sosObservation.getValue() instanceof SingleObservationValue) {
            addSingleObservationToResult(xbResult, sosObservation);
        } else if (sosObservation.getValue() instanceof MultiObservationValues) {
            addMultiObservationValueToResult(xbResult, sosObservation);
        }
    }

    // FIXME String.equals(QName) !?
    private void addSingleObservationToResult(XmlObject xbResult, OmObservation sosObservation)
            throws EncodingException {
        String observationType = sosObservation.getObservationConstellation().getObservationType();
        SingleObservationValue<?> observationValue = (SingleObservationValue<?>) sosObservation.getValue();
        if (observationValue.getValue() instanceof QuantityValue) {
            QuantityValue quantityValue = (QuantityValue) observationValue.getValue();
            xbResult.set(encodeObjectToXml(GmlConstants.NS_GML, quantityValue));
        } else if (observationValue.getValue() instanceof CountValue) {
            CountValue countValue = (CountValue) observationValue.getValue();
            XmlInteger xbInteger = XmlInteger.Factory.newInstance(getXmlOptions());
            if (countValue.getValue() != null && countValue.getValue() != Integer.MIN_VALUE) {
                xbInteger.setBigIntegerValue(new BigInteger(countValue.getValue().toString()));
            } else {
                xbInteger.setNil();
            }
            xbResult.set(xbInteger);
        } else if (observationValue.getValue() instanceof TextValue) {
            TextValue textValue = (TextValue) observationValue.getValue();
            XmlString xbString = XmlString.Factory.newInstance(getXmlOptions());
            if (textValue.getValue() != null && !textValue.getValue().isEmpty()) {
                xbString.setStringValue(textValue.getValue());
            } else {
                xbString.setNil();
            }
            xbResult.set(xbString);
        } else if (observationValue.getValue() instanceof BooleanValue) {
            BooleanValue booleanValue = (BooleanValue) observationValue.getValue();
            XmlBoolean xbBoolean = XmlBoolean.Factory.newInstance(getXmlOptions());
            if (booleanValue.getValue() != null) {
                xbBoolean.setBooleanValue(booleanValue.getValue());
            } else {
                xbBoolean.setNil();
            }
            xbResult.set(xbBoolean);
        } else if (observationValue.getValue() instanceof CategoryValue) {
            CategoryValue categoryValue = (CategoryValue) observationValue.getValue();
            if (categoryValue.getValue() != null && !categoryValue.getValue().isEmpty()) {
                xbResult.set(encodeObjectToXml(GmlConstants.NS_GML, categoryValue, EncodingContext.of(
                        XmlBeansEncodingFlags.GMLID, SosConstants.OBS_ID_PREFIX + sosObservation.getObservationID())));
            } else {
                xbResult.setNil();
            }
        } else if (observationValue.getValue() instanceof GeometryValue) {
            GeometryValue geometryValue = (GeometryValue) observationValue.getValue();
            if (geometryValue.getValue() != null) {
                xbResult.set(encodeObjectToXml(GmlConstants.NS_GML, geometryValue.getValue(), EncodingContext.of(
                        XmlBeansEncodingFlags.GMLID, SosConstants.OBS_ID_PREFIX + sosObservation.getObservationID())));
            } else {
                xbResult.setNil();
            }
        } else if (OmConstants.OBS_TYPE_SWE_ARRAY_OBSERVATION.equals(observationType)
                || OmConstants.RESULT_MODEL_OBSERVATION.getLocalPart().equals(observationType)) {
            SweDataArray dataArray = sweHelper.createSosSweDataArray(sosObservation);
            xbResult.set(encodeObjectToXml(SweConstants.NS_SWE_101, dataArray,
                    EncodingContext.of(XmlBeansEncodingFlags.FOR_OBSERVATION)));
        }
    }

    private void addMultiObservationValueToResult(XmlObject xbResult, OmObservation sosObservation)
            throws EncodingException {
        SweDataArray dataArray = sweHelper.createSosSweDataArray(sosObservation);
        xbResult.set(encodeObjectToXml(SweConstants.NS_SWE_101, dataArray,
                EncodingContext.of(XmlBeansEncodingFlags.FOR_OBSERVATION)));
    }

    /**
     * Encodes a SosAbstractFeature to an SpatialSamplingFeature under
     * consideration of duplicated SpatialSamplingFeature in the XML document.
     *
     * @param observation
     *            XmlObject O&M observation
     * @param feature
     *            SOS observation
     *
     * @throws EncodingException
     *             if encoding of the feature fails
     */
    private void addFeatureOfInterest(ObservationType observation, AbstractFeature feature)
            throws EncodingException {
        EncodingContext ctx = EncodingContext.of(XmlBeansEncodingFlags.ENCODE, feature.getDefaultElementEncoding());
        XmlObject encodeObjectToXml = encodeObjectToXml(GmlConstants.NS_GML, feature, ctx);
        observation.addNewFeatureOfInterest().set(encodeObjectToXml);
    }

    private void createMetadataProperty(ObservationCollectionType xbObservationCollection, Extensions extensions)
            throws EncodingException {
        for (Extension<?> extension : extensions.getExtensions()) {
            if (extension.getValue() instanceof SweAbstractDataComponent) {
                xbObservationCollection.addNewMetaDataProperty()
                        .set(encodeGML311(new GenericMetaData(extension.getValue())));
            }
        }
    }

    protected XmlObject encodeGML311(Object o)
            throws EncodingException {
        return encodeObjectToXml(GmlConstants.NS_GML, o);
    }

    protected XmlObject encodeGML311(Object o, EncodingContext context)
            throws EncodingException {
        return encodeObjectToXml(GmlConstants.NS_GML, o, context);
    }
}
