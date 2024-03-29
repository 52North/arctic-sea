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
package org.n52.svalbard.decode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.xmlbeans.XmlObject;
import org.isotc211.x2005.gmd.CIResponsiblePartyPropertyType;
import org.n52.shetland.ogc.OGCConstants;
import org.n52.shetland.ogc.SupportedType;
import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.gml.CodeWithAuthority;
import org.n52.shetland.ogc.gml.ReferenceType;
import org.n52.shetland.ogc.sensorML.AbstractProcess;
import org.n52.shetland.ogc.sensorML.AbstractSensorML;
import org.n52.shetland.ogc.sensorML.SensorML20Constants;
import org.n52.shetland.ogc.sensorML.SensorMLConstants;
import org.n52.shetland.ogc.sensorML.SmlContact;
import org.n52.shetland.ogc.sensorML.Term;
import org.n52.shetland.ogc.sensorML.elements.SmlCapabilities;
import org.n52.shetland.ogc.sensorML.elements.SmlCapability;
import org.n52.shetland.ogc.sensorML.elements.SmlCharacteristic;
import org.n52.shetland.ogc.sensorML.elements.SmlCharacteristics;
import org.n52.shetland.ogc.sensorML.elements.SmlClassifier;
import org.n52.shetland.ogc.sensorML.elements.SmlComponent;
import org.n52.shetland.ogc.sensorML.elements.SmlConnection;
import org.n52.shetland.ogc.sensorML.elements.SmlIdentifier;
import org.n52.shetland.ogc.sensorML.elements.SmlIo;
import org.n52.shetland.ogc.sensorML.elements.SmlLink;
import org.n52.shetland.ogc.sensorML.elements.SmlParameter;
import org.n52.shetland.ogc.sensorML.elements.SmlPosition;
import org.n52.shetland.ogc.sensorML.v20.AbstractPhysicalProcess;
import org.n52.shetland.ogc.sensorML.v20.AbstractProcessV20;
import org.n52.shetland.ogc.sensorML.v20.AggregateProcess;
import org.n52.shetland.ogc.sensorML.v20.DescribedObject;
import org.n52.shetland.ogc.sensorML.v20.PhysicalComponent;
import org.n52.shetland.ogc.sensorML.v20.PhysicalSystem;
import org.n52.shetland.ogc.sensorML.v20.SimpleProcess;
import org.n52.shetland.ogc.sensorML.v20.SmlDataInterface;
import org.n52.shetland.ogc.sensorML.v20.SmlFeatureOfInterest;
import org.n52.shetland.ogc.sos.ProcedureDescriptionFormat;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.swe.SweAbstractDataComponent;
import org.n52.shetland.ogc.swe.SweDataRecord;
import org.n52.shetland.ogc.swe.SweDataStream;
import org.n52.shetland.ogc.swe.SweVector;
import org.n52.shetland.ogc.swe.simpleType.SweObservableProperty;
import org.n52.shetland.util.CollectionHelper;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.decode.exception.UnsupportedDecoderXmlInputException;
import org.n52.svalbard.util.CodingHelper;
import org.n52.svalbard.util.XmlHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import net.opengis.gml.x32.FeaturePropertyType;
import net.opengis.sensorml.x20.AbstractPhysicalProcessType;
import net.opengis.sensorml.x20.AbstractProcessDocument;
import net.opengis.sensorml.x20.AbstractProcessType;
import net.opengis.sensorml.x20.AbstractProcessType.FeaturesOfInterest;
import net.opengis.sensorml.x20.AbstractProcessType.Inputs;
import net.opengis.sensorml.x20.AbstractProcessType.Outputs;
import net.opengis.sensorml.x20.AbstractProcessType.Parameters;
import net.opengis.sensorml.x20.AggregateProcessDocument;
import net.opengis.sensorml.x20.AggregateProcessPropertyType;
import net.opengis.sensorml.x20.AggregateProcessType;
import net.opengis.sensorml.x20.CapabilityListType;
import net.opengis.sensorml.x20.CapabilityListType.Capability;
import net.opengis.sensorml.x20.CharacteristicListPropertyType;
import net.opengis.sensorml.x20.CharacteristicListType;
import net.opengis.sensorml.x20.CharacteristicListType.Characteristic;
import net.opengis.sensorml.x20.ClassifierListPropertyType;
import net.opengis.sensorml.x20.ClassifierListType;
import net.opengis.sensorml.x20.ClassifierListType.Classifier;
import net.opengis.sensorml.x20.ComponentListPropertyType;
import net.opengis.sensorml.x20.ComponentListType;
import net.opengis.sensorml.x20.ComponentListType.Component;
import net.opengis.sensorml.x20.ConnectionListPropertyType;
import net.opengis.sensorml.x20.ConnectionListType.Connection;
import net.opengis.sensorml.x20.ContactListPropertyType;
import net.opengis.sensorml.x20.DataComponentOrObservablePropertyType;
import net.opengis.sensorml.x20.DataInterfaceType;
import net.opengis.sensorml.x20.DescribedObjectDocument;
import net.opengis.sensorml.x20.DescribedObjectType;
import net.opengis.sensorml.x20.DescribedObjectType.Capabilities;
import net.opengis.sensorml.x20.IdentifierListPropertyType;
import net.opengis.sensorml.x20.IdentifierListType.Identifier;
import net.opengis.sensorml.x20.InputListType.Input;
import net.opengis.sensorml.x20.KeywordListPropertyType;
import net.opengis.sensorml.x20.LinkType;
import net.opengis.sensorml.x20.ObservablePropertyType;
import net.opengis.sensorml.x20.OutputListType.Output;
import net.opengis.sensorml.x20.ParameterListType.Parameter;
import net.opengis.sensorml.x20.PhysicalComponentDocument;
import net.opengis.sensorml.x20.PhysicalComponentPropertyType;
import net.opengis.sensorml.x20.PhysicalComponentType;
import net.opengis.sensorml.x20.PhysicalSystemDocument;
import net.opengis.sensorml.x20.PhysicalSystemPropertyType;
import net.opengis.sensorml.x20.PhysicalSystemType;
import net.opengis.sensorml.x20.PositionUnionPropertyType;
import net.opengis.sensorml.x20.SimpleProcessDocument;
import net.opengis.sensorml.x20.SimpleProcessPropertyType;
import net.opengis.sensorml.x20.SimpleProcessType;
import net.opengis.sensorml.x20.TermType;

/**
 * {@link AbstractSensorMLDecoder} class to decode OGC SensorML 2.0
 *
 * @author Carsten Hollmann
 * @since 1.0.0
 *
 */
public class SensorMLDecoderV20
        extends AbstractSensorMLDecoder {

    private static final Logger LOGGER = LoggerFactory.getLogger(SensorMLDecoderV20.class);

    private static final Set<DecoderKey> DECODER_KEYS
            = CodingHelper.decoderKeysForElements(SensorML20Constants.NS_SML_20, DescribedObjectDocument.class,
                    SimpleProcessDocument.class, PhysicalComponentDocument.class, PhysicalSystemDocument.class,
                    AggregateProcessDocument.class, AbstractProcessDocument.class);

    private static final Set<String> REMOVABLE_CAPABILITIES_NAMES
            = Sets.newHashSet(SensorMLConstants.ELEMENT_NAME_OFFERINGS);

    private static final Set<String> REMOVABLE_COMPONENTS_ROLES
            = Collections.singleton(SensorMLConstants.ELEMENT_NAME_CHILD_PROCEDURES);

    private static final ImmutableSet<SupportedType> SUPPORTED_TYPES = ImmutableSet.<SupportedType>builder()
            .add(new ProcedureDescriptionFormat(SensorML20Constants.SENSORML_20_OUTPUT_FORMAT_URL)).build();

    // CHECKSTYLE:OFF
    private static final Map<String, ImmutableMap<String, Set<String>>> SUPPORTED_TRANSACTIONAL_PROCEDURE_DESCRIPTION_FORMATS
            = ImmutableMap.of(SosConstants.SOS, ImmutableMap.<String, Set<String>>builder()
                    .put(Sos2Constants.SERVICEVERSION, ImmutableSet.of(SensorMLConstants.SENSORML_OUTPUT_FORMAT_URL))
                    .build());
    // CHECKSTYLE:ON

    public SensorMLDecoderV20() {
        LOGGER.debug("Decoder for the following keys initialized successfully: {}!",
                Joiner.on(", ").join(DECODER_KEYS));
    }

    @Override
    public Set<DecoderKey> getKeys() {
        return Collections.unmodifiableSet(DECODER_KEYS);
    }

    @Override
    public Set<SupportedType> getSupportedTypes() {
        return Collections.unmodifiableSet(SUPPORTED_TYPES);
    }

    @Override
    public Set<String> getSupportedProcedureDescriptionFormats(final String service, final String version) {
        if (SUPPORTED_TRANSACTIONAL_PROCEDURE_DESCRIPTION_FORMATS.containsKey(service)
                && SUPPORTED_TRANSACTIONAL_PROCEDURE_DESCRIPTION_FORMATS.get(service).containsKey(version)) {
            return SUPPORTED_TRANSACTIONAL_PROCEDURE_DESCRIPTION_FORMATS.get(service).get(version);
        }
        return Collections.emptySet();
    }

    @Override
    public AbstractSensorML decode(XmlObject element) throws DecodingException {
        // validate document
        XmlHelper.validateDocument(element);
        return parse(element);
    }

    private AbstractSensorML parse(XmlObject element) throws DecodingException {
        AbstractSensorML sml = null;
        if (element instanceof PhysicalSystemDocument) {
            sml = parsePhysicalSystem(((PhysicalSystemDocument) element).getPhysicalSystem());
        } else if (element instanceof PhysicalSystemPropertyType) {
            sml = parsePhysicalSystem(((PhysicalSystemPropertyType) element).getPhysicalSystem());
        } else if (element instanceof PhysicalSystemType) {
            sml = parsePhysicalSystem((PhysicalSystemType) element);
        } else if (element instanceof PhysicalComponentDocument) {
            sml = parsePhysicalComponent(((PhysicalComponentDocument) element).getPhysicalComponent());
        } else if (element instanceof PhysicalComponentPropertyType) {
            sml = parsePhysicalComponent(((PhysicalComponentPropertyType) element).getPhysicalComponent());
        } else if (element instanceof PhysicalComponentType) {
            sml = parsePhysicalComponent((PhysicalComponentType) element);
        } else if (element instanceof SimpleProcessDocument) {
            sml = parseSimpleProcess(((SimpleProcessDocument) element).getSimpleProcess());
        } else if (element instanceof SimpleProcessPropertyType) {
            sml = parseSimpleProcess(((SimpleProcessPropertyType) element).getSimpleProcess());
        } else if (element instanceof SimpleProcessType) {
            sml = parseSimpleProcess((SimpleProcessType) element);
        } else if (element instanceof AggregateProcessDocument) {
            sml = parseAggregateProcess(((AggregateProcessDocument) element).getAggregateProcess());
        } else if (element instanceof AggregateProcessPropertyType) {
            sml = parseAggregateProcess(((AggregateProcessPropertyType) element).getAggregateProcess());
        } else if (element instanceof AggregateProcessType) {
            sml = parseAggregateProcess((AggregateProcessType) element);
        } else {
            throw new UnsupportedDecoderXmlInputException(this, element);
        }
        if (sml != null) {
            setXmlDescription(element, sml);
        }
        return sml;
    }

    private void setXmlDescription(XmlObject xml, AbstractSensorML sml) {
        XmlObject xmlToString = null;
        if (xml.schemaType() != null && xml.schemaType().isDocumentType()) {
            xmlToString = xml;
        } else {
            // check and create documents if necessary
            if (xml instanceof PhysicalSystemPropertyType) {
                PhysicalSystemDocument psd = PhysicalSystemDocument.Factory.newInstance(getXmlOptions());
                psd.setPhysicalSystem(((PhysicalSystemPropertyType) xml).getPhysicalSystem());
                xmlToString = psd;
            } else if (xml instanceof PhysicalSystemType) {
                PhysicalSystemDocument psd = PhysicalSystemDocument.Factory.newInstance(getXmlOptions());
                psd.setPhysicalSystem((PhysicalSystemType) xml);
                xmlToString = psd;
            } else if (xml instanceof PhysicalComponentPropertyType) {
                PhysicalComponentDocument pcd = PhysicalComponentDocument.Factory.newInstance(getXmlOptions());
                pcd.setPhysicalComponent(((PhysicalComponentPropertyType) xml).getPhysicalComponent());
                xmlToString = pcd;
            } else if (xml instanceof PhysicalComponentType) {
                PhysicalComponentDocument pcd = PhysicalComponentDocument.Factory.newInstance(getXmlOptions());
                pcd.setPhysicalComponent((PhysicalComponentType) xml);
                xmlToString = pcd;
            } else if (xml instanceof SimpleProcessPropertyType) {
                SimpleProcessDocument spd = SimpleProcessDocument.Factory.newInstance(getXmlOptions());
                spd.setSimpleProcess(((SimpleProcessPropertyType) xml).getSimpleProcess());
                xmlToString = spd;
            } else if (xml instanceof SimpleProcessType) {
                SimpleProcessDocument spd = SimpleProcessDocument.Factory.newInstance(getXmlOptions());
                spd.setSimpleProcess((SimpleProcessType) xml);
                xmlToString = spd;
            } else if (xml instanceof AggregateProcessPropertyType) {
                AggregateProcessDocument apd = AggregateProcessDocument.Factory.newInstance(getXmlOptions());
                apd.setAggregateProcess(((AggregateProcessPropertyType) xml).getAggregateProcess());
                xmlToString = apd;
            } else if (xml instanceof AggregateProcessType) {
                AggregateProcessDocument apd = AggregateProcessDocument.Factory.newInstance(getXmlOptions());
                apd.setAggregateProcess((AggregateProcessType) xml);
                xmlToString = apd;
            }
        }
        if (xmlToString != null) {
            sml.setXml(xmlToString.xmlText(getXmlOptions()));
        }
    }

    private DescribedObject parsePhysicalSystem(PhysicalSystemType describedObject) throws DecodingException {
        PhysicalSystem ps = new PhysicalSystem();
        parseAbstractPhysicalProcess(describedObject, ps);
        parseAbstractProcess(describedObject, ps);
        parseDescribedObject(describedObject, ps);
        if (describedObject.isSetComponents()) {
            ps.addComponents(parseComponents(describedObject.getComponents()));
            final List<Integer> compsToRemove
                    = checkComponentsForRemoval(describedObject.getComponents().getComponentList());
            compsToRemove.forEach(describedObject.getComponents().getComponentList()::removeComponent);
            if (removeEmptyComponents(describedObject.getComponents())) {
                describedObject.unsetComponents();
            }
        }
        return ps;
    }

    private DescribedObject parsePhysicalComponent(PhysicalComponentType describedObject) throws DecodingException {
        PhysicalComponent pc = new PhysicalComponent();
        parseAbstractPhysicalProcess(describedObject, pc);
        parseAbstractProcess(describedObject, pc);
        parseDescribedObject(describedObject, pc);
        return pc;
    }

    private DescribedObject parseSimpleProcess(SimpleProcessType describedObject) throws DecodingException {
        SimpleProcess sp = new SimpleProcess();
        parseAbstractProcess(describedObject, sp);
        parseDescribedObject(describedObject, sp);
        return sp;
    }

    private DescribedObject parseAggregateProcess(AggregateProcessType describedObject) throws DecodingException {
        AggregateProcess ap = new AggregateProcess();
        parseAbstractProcess(describedObject, ap);
        parseDescribedObject(describedObject, ap);
        if (describedObject.isSetComponents()) {
            ap.addComponents(parseComponents(describedObject.getComponents()));
            List<Integer> compsToRemove
                    = checkComponentsForRemoval(describedObject.getComponents().getComponentList());
            compsToRemove.forEach(describedObject.getComponents().getComponentList()::removeComponent);
            if (removeEmptyComponents(describedObject.getComponents())) {
                describedObject.unsetComponents();
            }
        }
        if (describedObject.isSetConnections()) {
            ap.setConnections(parseConnections(describedObject.getConnections()));
        }
        return ap;
    }

    private void parseDescribedObject(DescribedObjectType dot, DescribedObject describedObject)
            throws DecodingException {
        if (dot.getId() != null && !dot.getId().isEmpty()) {
            describedObject.setGmlId(dot.getId());
        }
        if (dot.isSetIdentifier()) {
            describedObject.setIdentifier((CodeWithAuthority) decodeXmlElement(dot.getIdentifier()));
            checkIdentifierCodeSpace(describedObject);
        }
        // if (CollectionHelper.isNotNullOrEmpty(dot.getExtensionArray())) {
        //
        // }
        if (CollectionHelper.isNotNullOrEmpty(dot.getKeywordsArray())) {
            parseKeywords(dot.getKeywordsArray());
        }
        if (CollectionHelper.isNotNullOrEmpty(dot.getIdentificationArray())) {
            parseIdentifications(describedObject, dot.getIdentificationArray());
        }
        if (CollectionHelper.isNotNullOrEmpty(dot.getClassificationArray())) {
            parseClassification(dot.getClassificationArray());
        }
        if (CollectionHelper.isNotNullOrEmpty(dot.getCharacteristicsArray())) {
            parseCharacteristics(dot.getCharacteristicsArray());
        }
        // if (CollectionHelper.isNotNullOrEmpty(dot.getValidTimeArray())) {
        // }
        // if
        // (CollectionHelper.isNotNullOrEmpty(dot.getSecurityConstraintsArray()))
        // {
        //
        // }
        // if
        // (CollectionHelper.isNotNullOrEmpty(dot.getLegalConstraintsArray())) {
        //
        // }
        // if (CollectionHelper.isNotNullOrEmpty(dot.getCharacteristicsArray()))
        // {
        //
        // }
        if (CollectionHelper.isNotNullOrEmpty(dot.getCapabilitiesArray())) {
            parseCapabilities(describedObject, dot.getCapabilitiesArray());
            checkCapabilitiesForRemoval(dot.getCapabilitiesArray()).forEach(dot::removeCapabilities);
        }
        if (CollectionHelper.isNotNullOrEmpty(dot.getContactsArray())) {
            parseContact(dot.getContactsArray());
        }
        // if (CollectionHelper.isNotNullOrEmpty(dot.getDocumentationArray())) {
        //
        // }
        // if (CollectionHelper.isNotNullOrEmpty(dot.getHistoryArray())) {
        //
        // }
        // if (dot.isSetLocation()) {
        //
        // }
    }

    private void parseAbstractProcess(AbstractProcessType apt, AbstractProcessV20 abstractProcess)
            throws DecodingException {
        if (apt.isSetTypeOf()) {
            Object decodedElement = decodeXmlElement(apt.getTypeOf());
            if (decodedElement instanceof ReferenceType) {
                abstractProcess.setTypeOf((ReferenceType) decodedElement);
            }
        }
        // if (apt.isSetConfiguration()) {
        //
        // }
        if (apt.isSetFeaturesOfInterest()) {
            parseFeatureOfInterest(apt.getFeaturesOfInterest(), abstractProcess);
        }
        if (apt.isSetInputs()) {
            abstractProcess.setInputs(parseInputs(apt.getInputs()));
        }
        if (apt.isSetOutputs()) {
            abstractProcess.setOutputs(parseOutputs(apt.getOutputs()));
        }
        if (apt.isSetParameters()) {
            abstractProcess.setParameters(parseParameters(apt.getParameters()));
        }
        // if (CollectionHelper.isNotNullOrEmpty(apt.getModesArray())) {
        //
        // }
    }

    private void parseAbstractPhysicalProcess(AbstractPhysicalProcessType appt,
            AbstractPhysicalProcess abstractPhysicalProcess) throws DecodingException {

        if (appt.isSetAttachedTo()) {
            Object decodeXmlElement = decodeXmlElement(appt.getAttachedTo());
            if (decodeXmlElement != null && decodeXmlElement instanceof ReferenceType) {
                abstractPhysicalProcess.setAttachedTo((ReferenceType) decodeXmlElement);
            }
        }
        // if
        // (CollectionHelper.isNotNullOrEmpty(appt.getLocalReferenceFrameArray()))
        // {
        //
        // }
        // if (CollectionHelper.isNotNullOrEmpty(appt.getLocalTimeFrameArray()))
        // {
        //
        // }
        if (CollectionHelper.isNotNullOrEmpty(appt.getPositionArray())) {
            for (PositionUnionPropertyType pupt : appt.getPositionArray()) {
                abstractPhysicalProcess.setPosition(parsePositionFrom(pupt));
                // TODO remove break if AbstractPhysicalProcess is extended
                break;
            }
        }
        // if (CollectionHelper.isNotNullOrEmpty(appt.getTimePositionArray())) {
        //
        // }
    }

    private List<String> parseKeywords(final KeywordListPropertyType[] keywordsArray) {
        final Set<String> keywords = Sets.newHashSet();
        if (keywordsArray != null && keywordsArray.length > 0) {
            for (final KeywordListPropertyType keyword : keywordsArray) {
                if (keyword.isSetKeywordList()) {
                    final String[] array = keyword.getKeywordList().getKeywordArray();
                    if (array != null && array.length > 0) {
                        keywords.addAll(Arrays.asList(array));
                    }
                }
            }
        }
        return Lists.newArrayList(keywords);
    }

    private void parseIdentifications(DescribedObject describedObject,
            IdentifierListPropertyType[] identificationArray) {
        for (final IdentifierListPropertyType ilpt : identificationArray) {
            if (ilpt.isSetIdentifierList()
                    && CollectionHelper.isNotNullOrEmpty(ilpt.getIdentifierList().getIdentifier2Array())) {
                for (final Identifier i : ilpt.getIdentifierList().getIdentifier2Array()) {
                    if (i.getTerm() != null) {
                        final SmlIdentifier identifier = new SmlIdentifier();
                        parseTerm(i.getTerm(), identifier);
                        describedObject.addIdentifier(identifier);
                        if (isIdentificationProcedureIdentifier(identifier)) {
                            describedObject.setIdentifier(identifier.getValue());
                        }
                    }
                }
            }
        }
    }

    /**
     * Parses the classification
     *
     * @param clpts XML classification
     * @return SOS classification
     */
    private List<SmlClassifier> parseClassification(final ClassifierListPropertyType[] clpts) {
        final List<SmlClassifier> sosClassifiers = new ArrayList<>(clpts.length);
        for (final ClassifierListPropertyType clpt : clpts) {
            if (clpt.isSetClassifierList()) {
                ClassifierListType clt = clpt.getClassifierList();
                if (CollectionHelper.isNotNullOrEmpty(clt.getClassifierArray())) {
                    for (final Classifier c : clt.getClassifierArray()) {
                        if (c.getTerm() != null) {
                            final SmlClassifier smlClassifier = new SmlClassifier();
                            parseTerm(c.getTerm(), smlClassifier);
                            sosClassifiers.add(smlClassifier);
                        }
                    }
                }
            }
        }
        return sosClassifiers;
    }

    private void parseTerm(TermType t, Term term) {
        term.setLabel(t.getLabel());
        term.setName(t.getLabel());
        if (t.isSetDefinition()) {
            term.setDefinition(t.getDefinition());
        }
        if (t.isSetCodeSpace()) {
            term.setCodeSpace(t.getCodeSpace().getHref());
        }
        term.setValue(t.getValue());
    }

    /**
     * Parses the characteristics
     *
     * @param clpts XML characteristics
     * @return SOS characteristics
     *
     *
     * @throws DecodingException * if an error occurs
     */
    private List<SmlCharacteristics> parseCharacteristics(final CharacteristicListPropertyType[] clpts)
            throws DecodingException {
        final List<SmlCharacteristics> sosCharacteristicsList = new ArrayList<>(clpts.length);
        for (final CharacteristicListPropertyType clpt : clpts) {
            final SmlCharacteristics sosCharacteristics = new SmlCharacteristics();
            if (clpt.isSetCharacteristicList()) {
                CharacteristicListType clt = clpt.getCharacteristicList();
                if (CollectionHelper.isNotNullOrEmpty(clt.getCharacteristicArray())) {
                    for (Characteristic c : clt.getCharacteristicArray()) {
                        final SmlCharacteristic characteristic = new SmlCharacteristic(c.getName());
                        if (c.isSetAbstractDataComponent()) {
                            final Object o = decodeXmlElement(c.getAbstractDataComponent());
                            if (o instanceof SweAbstractDataComponent) {
                                characteristic.setAbstractDataComponent((SweAbstractDataComponent) o);
                            } else {
                                throw new DecodingException(XmlHelper.getLocalName(clpt),
                                        "Error while parsing the characteristics of the SensorML "
                                        + "(the characteristics' data record is not of "
                                        + "type DataRecordPropertyType)!");
                            }
                        } else if (c.isSetHref()) {
                            characteristic.setHref(c.getHref());
                            if (c.isSetTitle()) {
                                characteristic.setTitle(c.getTitle());
                            }
                        }
                        sosCharacteristics.addCharacteristic(characteristic);
                    }
                }
            }
            sosCharacteristicsList.add(sosCharacteristics);
        }
        return sosCharacteristicsList;
    }

    /**
     * Parses the capabilities, processing and removing special insertion
     * metadata
     *
     * @param abstractProcess The AbstractProcess to which capabilities and
     * insertion metadata are added
     * @param capabilitiesArray XML capabilities
     *
     * @throws DecodingException * if an error occurs
     */
    private void parseCapabilities(final AbstractProcess abstractProcess, final Capabilities[] capabilitiesArray)
            throws DecodingException {
        for (final Capabilities cs : capabilitiesArray) {
            final SmlCapabilities capabilities = new SmlCapabilities(cs.getName());
            if (cs.isSetCapabilityList()) {
                CapabilityListType cl = cs.getCapabilityList();
                if (CollectionHelper.isNotNullOrEmpty(cl.getCapabilityArray())) {
                    for (Capability c : cl.getCapabilityArray()) {
                        final SmlCapability capability = new SmlCapability(c.getName());
                        if (c.isSetAbstractDataComponent()) {
                            final Object o = decodeXmlElement(c.getAbstractDataComponent());
                            if (o instanceof SweAbstractDataComponent) {
                                capability.setAbstractDataComponent((SweAbstractDataComponent) o);
                                capabilities.addCapability(capability);
                            } else {
                                throw new DecodingException(XmlHelper.getLocalName(cs),
                                        "Error while parsing the capabilities of "
                                        + "the SensorML (the capabilities data record "
                                        + "is not of type DataRecordPropertyType)!");
                            }
                        } else if (c.isSetHref()) {
                            capability.setHref(c.getHref());
                            if (c.isSetTitle()) {
                                capability.setTitle(c.getTitle());
                            }
                        }
                    }
                }
            }
            abstractProcess.addCapabilities(capabilities);
        }
    }

    private List<SmlContact> parseContact(final ContactListPropertyType[] clpts) throws DecodingException {
        List<SmlContact> smlContacts = Lists.newArrayList();
        for (ContactListPropertyType clpt : clpts) {
            if (clpt.isSetContactList()
                    && CollectionHelper.isNotNullOrEmpty(clpt.getContactList().getContactArray())) {
                for (CIResponsiblePartyPropertyType c : clpt.getContactList().getContactArray()) {
                    final Object o = decodeXmlElement(c);
                    if (o instanceof SmlContact) {
                        smlContacts.add((SmlContact) o);
                    } else {
                        throw new DecodingException(XmlHelper.getLocalName(c),
                                "Error while parsing the contacts of the SensorML!");
                    }
                }
            }
        }
        return smlContacts;
    }

    private List<SmlParameter> parseParameters(Parameters parameters)
            throws DecodingException {
        if (CollectionHelper.isNotNullOrEmpty(parameters.getParameterList().getParameterArray())) {
            final List<SmlParameter> sweComponents
                    = new ArrayList<>(parameters.getParameterList().getParameterArray().length);
            for (final Parameter sweComponent : parameters.getParameterList().getParameterArray()) {
                sweComponents.add(parseSweParameter(sweComponent));
            }
            return sweComponents;
        }
        return Collections.emptyList();
    }

    private List<SmlIo> parseInputs(Inputs inputs) throws DecodingException {
        if (CollectionHelper.isNotNullOrEmpty(inputs.getInputList().getInputArray())) {
            final List<SmlIo> sosInputs = new ArrayList<>(inputs.getInputList().getInputArray().length);
            for (final Input xbInput : inputs.getInputList().getInputArray()) {
                sosInputs.add(parseInput(xbInput));
            }
            return sosInputs;
        }
        return Collections.emptyList();
    }

    private List<SmlIo> parseOutputs(Outputs outputs) throws DecodingException {
        if (CollectionHelper.isNotNullOrEmpty(outputs.getOutputList().getOutputArray())) {
            final List<SmlIo> sosOutputs = new ArrayList<>(outputs.getOutputList().getOutputArray().length);
            for (final Output xbOutput : outputs.getOutputList().getOutputArray()) {
                sosOutputs.add(parseOutput(xbOutput));
            }
            return sosOutputs;
        }
        return Collections.emptyList();
    }

    private void parseFeatureOfInterest(FeaturesOfInterest featuresOfInterest, AbstractProcessV20 abstractProcess)
            throws DecodingException {
        if (CollectionHelper.isNotNullOrEmpty(featuresOfInterest.getFeatureList().getFeatureArray())) {
            SmlFeatureOfInterest smlFeatureOfInterest = new SmlFeatureOfInterest();
            for (FeaturePropertyType fpt : featuresOfInterest.getFeatureList().getFeatureArray()) {
                Object o = decodeXmlElement(fpt);
                if (o instanceof AbstractFeature) {
                    smlFeatureOfInterest.addFeatureOfInterest((AbstractFeature) o);
                }
            }
            abstractProcess.setSmlFeatureOfInterest(smlFeatureOfInterest);
        }
    }

    private SmlPosition parsePositionFrom(PositionUnionPropertyType pupt) throws DecodingException {
        SmlPosition position = new SmlPosition();
        if (pupt.isSetVector()) {
            Object decodeXmlElement = decodeXmlElement(pupt.getVector());
            if (decodeXmlElement != null && decodeXmlElement instanceof SweVector) {
                position.setVector((SweVector) decodeXmlElement);
            }
            // } else if (pupt.isSetAbstractProcess()) {
            // AbstractSensorML decode = decode(pupt.getAbstractProcess());
            // } else if (pupt.isSetDataArray1()) {
            // Object decodeXmlElement =
            // CodingHelper.decodeXmlElement(pupt.getDataArray1());
        } else if (pupt.isSetDataRecord()) {
            Object decodeXmlElement = decodeXmlElement(pupt.getDataRecord());
            if (decodeXmlElement != null && decodeXmlElement instanceof SweDataRecord) {
                position.setAbstractDataComponent((SweDataRecord) decodeXmlElement);
            }
            // } else if (pupt.isSetPoint()){
            // Object decodeXmlElement =
            // CodingHelper.decodeXmlElement(pupt.getPoint());
            // } else if (pupt.isSetText()) {
            // Object decodeXmlElement =
            // CodingHelper.decodeXmlElement(pupt.getText());
        } else {
            throw new UnsupportedDecoderXmlInputException(this, pupt);
        }
        return position;
    }

    private List<SmlComponent> parseComponents(ComponentListPropertyType components) throws DecodingException {
        final List<SmlComponent> sosSmlComponents = Lists.newLinkedList();
        if (components.isSetComponentList() && components.getComponentList().getComponentArray() != null) {
            for (final Component component : components.getComponentList().getComponentArray()) {
                if (component.isSetAbstractProcess() || component.isSetHref() || component.isSetTitle()) {
                    final SmlComponent sosSmlcomponent = new SmlComponent(component.getName());
                    AbstractSensorML abstractProcess;
                    if (component.isSetAbstractProcess()) {
                        abstractProcess = parse(component.getAbstractProcess());
                    } else {
                        if (component.isSetTitle()) {
                            sosSmlcomponent.setTitle(component.getTitle());
                        }
                        if (component.isSetHref()) {
                            sosSmlcomponent.setHref(component.getHref());
                        }
                        abstractProcess = new AbstractProcess();
                        if (sosSmlcomponent.isSetTitle()) {
                            abstractProcess.setIdentifier(sosSmlcomponent.getTitle());
                        } else if (!sosSmlcomponent.isSetTitle() && sosSmlcomponent.isSetHref()) {
                            abstractProcess.setIdentifier(sosSmlcomponent.getHref());
                        }
                    }
                    sosSmlcomponent.setProcess(abstractProcess);
                    sosSmlComponents.add(sosSmlcomponent);
                }
            }
        }
        return sosSmlComponents;
    }

    private SmlConnection parseConnections(ConnectionListPropertyType connections)
            throws DecodingException {
        SmlConnection sosSmlConnection = new SmlConnection();
        if (connections.isSetConnectionList() && connections.getConnectionList().getConnectionArray() != null) {
            for (final Connection connection : connections.getConnectionList().getConnectionArray()) {
                if (connection.getLink() != null) {
                    LinkType link = connection.getLink();
                    sosSmlConnection
                            .addConnection(new SmlLink(link.getDestination().getRef(), link.getSource().getRef()));
                }
            }
        }
        return sosSmlConnection;
    }

    private boolean checkIdentifierCodeSpace(AbstractProcessV20 ap) throws DecodingException {
        if (ap.getIdentifierCodeWithAuthority().isSetCodeSpace()
                && OGCConstants.UNIQUE_ID.equals(ap.getIdentifierCodeWithAuthority().getCodeSpace())) {
            return true;
        } else {
            throw new DecodingException("gml:identifier[@codesSpace]",
                    ap.getIdentifierCodeWithAuthority().getCodeSpace());
        }
    }

    private List<Integer> checkCapabilitiesForRemoval(final Capabilities[] capabilitiesArray) {
        final List<Integer> removeableCaps = new ArrayList<>(capabilitiesArray.length);
        for (int i = 0; i < capabilitiesArray.length; i++) {
            final String name = capabilitiesArray[i].getName();
            if (name != null && REMOVABLE_CAPABILITIES_NAMES.contains(name)) {
                removeableCaps.add(i);
            }
        }
        Collections.sort(removeableCaps);
        Collections.reverse(removeableCaps);
        return removeableCaps;
    }

    private List<Integer> checkComponentsForRemoval(ComponentListType componentList) {
        final List<Integer> removeableComponents = new ArrayList<>(0);
        if (componentList != null && componentList.getComponentArray() != null) {
            final Component[] componentArray = componentList.getComponentArray();
            for (int i = 0; i < componentArray.length; i++) {
                if (componentArray[i].getRole() != null
                        && REMOVABLE_COMPONENTS_ROLES.contains(componentArray[i].getRole())) {
                    removeableComponents.add(i);
                }
            }
        }
        return removeableComponents;
    }

    private boolean removeEmptyComponents(ComponentListPropertyType components) {
        boolean removeComponents = false;
        if (components != null) {
            if (components.getComponentList() == null) {
                removeComponents = true;
            } else if (components.getComponentList().getComponentArray() == null
                    || components.getComponentList().getComponentArray() != null
                    && components.getComponentList().getComponentArray().length == 0) {
                removeComponents = true;
            }
        }
        return removeComponents;
    }

    private SmlParameter parseSweParameter(Parameter xbParameter)
            throws DecodingException {
        SmlParameter param = new SmlParameter();
        String name = xbParameter.getName();
        if (xbParameter.isSetHref()) {
            param.setHref(xbParameter.getHref());
            if (xbParameter.isSetTitle()) {
                param.setTitle(xbParameter.getTitle());
            }
            return param;
        }
        Object parameter = decodeXmlElement(xbParameter.getAbstractDataComponent());
        if (parameter instanceof SweAbstractDataComponent) {
            if (xbParameter.getName() != null
                    && !xbParameter.getName().isEmpty()) {
                ((SweAbstractDataComponent) parameter).setName(name);
            }

            param.setName(name);
            param.setParameter((SweAbstractDataComponent) parameter);
            return param;
        } else {
            throw new DecodingException(XmlHelper.getLocalName(xbParameter),
                    "An 'SweAbstractDataComponent' is not supported");
        }
    }

    private SmlIo parseInput(Input xbInput) throws DecodingException {
        final SmlIo sosIo = new SmlIo();
        sosIo.setIoName(xbInput.getName());
        if (xbInput.isSetHref()) {
            parseReference(xbInput, sosIo);
        } else {
            sosIo.setIoValue(parseDataComponentOrObservablePropertyType(xbInput));
        }

        return sosIo;
    }

    private void parseReference(DataComponentOrObservablePropertyType adcpt, SmlIo sosIo) {
        sosIo.setHref(adcpt.getHref());
        if (adcpt.isSetTitle()) {
            sosIo.setTitle(adcpt.getTitle());
        }

    }

    private SmlIo parseOutput(Output xbOutput) throws DecodingException {
        final SmlIo sosIo = new SmlIo();
        sosIo.setIoName(xbOutput.getName());
        if (xbOutput.isSetHref()) {
            parseReference(xbOutput, sosIo);
        } else {
            sosIo.setIoValue(parseDataComponentOrObservablePropertyType(xbOutput));
        }
        return sosIo;
    }

    /**
     * Parses the components
     *
     * @param adcpt XML components
     * @return SOS component
     *
     *
     * @throws DecodingException * if an error occurs
     */
    private SweAbstractDataComponent parseDataComponentOrObservablePropertyType(
            final DataComponentOrObservablePropertyType adcpt) throws DecodingException {
        XmlObject toDecode = null;
        if (adcpt.isSetObservableProperty()) {
            return parseObservableProperty(adcpt.getObservableProperty());
        } else if (adcpt.isSetAbstractDataComponent()) {
            final Object decodedObject = decodeXmlElement(adcpt.getAbstractDataComponent());
            if (decodedObject instanceof SweAbstractDataComponent) {
                return (SweAbstractDataComponent) decodedObject;
            } else {
                throw new DecodingException(XmlHelper.getLocalName(adcpt),
                        "The 'DataComponentOrObservablePropertyType' with "
                        + "type '%s' as value for '%s' is not supported.",
                        XmlHelper.getLocalName(toDecode), XmlHelper.getLocalName(adcpt));
            }
        } else if (adcpt.isSetDataInterface()) {
            return parseDataInterfaceType(adcpt.getDataInterface());
        } else {
            throw new DecodingException(XmlHelper.getLocalName(adcpt),
                    "An 'DataComponentOrObservablePropertyType' is not supported");
        }
    }

    protected SmlDataInterface parseDataInterfaceType(DataInterfaceType xbDataInterface) throws DecodingException {
        SmlDataInterface dataInterface = new SmlDataInterface();
        // TODO implement- no funding at the moment available
        // When starting implementation: Do not forget to activate the already
        // available unit tests
        Object data = decodeXmlElement(xbDataInterface.getData());
        if (data instanceof SweDataStream) {
            dataInterface.setData((SweDataStream) data);
        }
        if (xbDataInterface.isSetInterfaceParameters()) {
            Object parameter = decodeXmlElement(xbDataInterface.getInterfaceParameters());
            if (parameter instanceof SweDataRecord) {
                dataInterface.setInputParameters((SweDataRecord) parameter);
            }
            // TODO throw exception if not instance of SweDataRecord
        }
        return dataInterface;
    }

    /**
     * Parse {@link ObservablePropertyType}
     *
     * @param opt Object to parse
     * @return Parsed {@link SweObservableProperty}
     */
    private SweObservableProperty parseObservableProperty(ObservablePropertyType opt) {
        SweObservableProperty observableProperty = new SweObservableProperty();
        observableProperty.setDefinition(opt.getDefinition());
        if (opt.isSetDescription()) {
            observableProperty.setDescription(opt.getDescription());
        }
        if (opt.isSetIdentifier()) {
            observableProperty.setIdentifier(opt.getIdentifier());
        }
        if (opt.isSetLabel()) {
            observableProperty.setLabel(opt.getLabel());
        }
        return observableProperty;
    }

}
