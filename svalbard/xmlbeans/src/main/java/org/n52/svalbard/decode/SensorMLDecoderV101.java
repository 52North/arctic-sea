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
import java.util.stream.Collectors;

import javax.xml.namespace.QName;

import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlObject;
import org.locationtech.jts.geom.Point;
import org.n52.shetland.ogc.SupportedType;
import org.n52.shetland.ogc.gml.CodeType;
import org.n52.shetland.ogc.gml.time.Time;
import org.n52.shetland.ogc.sensorML.AbstractComponent;
import org.n52.shetland.ogc.sensorML.AbstractProcess;
import org.n52.shetland.ogc.sensorML.AbstractSensorML;
import org.n52.shetland.ogc.sensorML.ProcessMethod;
import org.n52.shetland.ogc.sensorML.ProcessModel;
import org.n52.shetland.ogc.sensorML.RulesDefinition;
import org.n52.shetland.ogc.sensorML.SensorML;
import org.n52.shetland.ogc.sensorML.SensorMLConstants;
import org.n52.shetland.ogc.sensorML.SmlContact;
import org.n52.shetland.ogc.sensorML.SmlContactList;
import org.n52.shetland.ogc.sensorML.SmlPerson;
import org.n52.shetland.ogc.sensorML.SmlReferencedContact;
import org.n52.shetland.ogc.sensorML.SmlResponsibleParty;
import org.n52.shetland.ogc.sensorML.System;
import org.n52.shetland.ogc.sensorML.elements.AbstractSmlDocumentation;
import org.n52.shetland.ogc.sensorML.elements.SmlCapabilities;
import org.n52.shetland.ogc.sensorML.elements.SmlCharacteristics;
import org.n52.shetland.ogc.sensorML.elements.SmlClassifier;
import org.n52.shetland.ogc.sensorML.elements.SmlComponent;
import org.n52.shetland.ogc.sensorML.elements.SmlIdentifier;
import org.n52.shetland.ogc.sensorML.elements.SmlIo;
import org.n52.shetland.ogc.sensorML.elements.SmlParameter;
import org.n52.shetland.ogc.sensorML.elements.SmlPosition;
import org.n52.shetland.ogc.sos.ProcedureDescriptionFormat;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.swe.DataRecord;
import org.n52.shetland.ogc.swe.SweAbstractDataComponent;
import org.n52.shetland.util.CollectionHelper;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.decode.exception.UnsupportedDecoderXmlInputException;
import org.n52.svalbard.util.CodingHelper;
import org.n52.svalbard.util.XmlHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import net.opengis.sensorML.x101.AbstractComponentType;
import net.opengis.sensorML.x101.AbstractDerivableComponentType;
import net.opengis.sensorML.x101.AbstractProcessType;
import net.opengis.sensorML.x101.AbstractPureProcessType;
import net.opengis.sensorML.x101.CapabilitiesDocument.Capabilities;
import net.opengis.sensorML.x101.CharacteristicsDocument.Characteristics;
import net.opengis.sensorML.x101.ClassificationDocument.Classification;
import net.opengis.sensorML.x101.ClassificationDocument.Classification.ClassifierList;
import net.opengis.sensorML.x101.ComponentType;
import net.opengis.sensorML.x101.ComponentsDocument.Components;
import net.opengis.sensorML.x101.ComponentsDocument.Components.ComponentList;
import net.opengis.sensorML.x101.ComponentsDocument.Components.ComponentList.Component;
import net.opengis.sensorML.x101.ContactDocument.Contact;
import net.opengis.sensorML.x101.ContactInfoDocument.ContactInfo;
import net.opengis.sensorML.x101.ContactInfoDocument.ContactInfo.Address;
import net.opengis.sensorML.x101.ContactInfoDocument.ContactInfo.Phone;
import net.opengis.sensorML.x101.ContactListDocument.ContactList;
import net.opengis.sensorML.x101.DocumentationDocument.Documentation;
import net.opengis.sensorML.x101.HistoryDocument.History;
import net.opengis.sensorML.x101.IdentificationDocument.Identification;
import net.opengis.sensorML.x101.IdentificationDocument.Identification.IdentifierList.Identifier;
import net.opengis.sensorML.x101.InputsDocument.Inputs;
import net.opengis.sensorML.x101.IoComponentPropertyType;
import net.opengis.sensorML.x101.KeywordsDocument.Keywords;
import net.opengis.sensorML.x101.MethodPropertyType;
import net.opengis.sensorML.x101.OnlineResourceDocument.OnlineResource;
import net.opengis.sensorML.x101.OutputsDocument.Outputs;
import net.opengis.sensorML.x101.ParametersDocument.Parameters;
import net.opengis.sensorML.x101.PersonDocument.Person;
import net.opengis.sensorML.x101.PositionDocument.Position;
import net.opengis.sensorML.x101.ProcessModelType;
import net.opengis.sensorML.x101.ResponsiblePartyDocument.ResponsibleParty;
import net.opengis.sensorML.x101.SensorMLDocument;
import net.opengis.sensorML.x101.SensorMLDocument.SensorML.Member;
import net.opengis.sensorML.x101.SmlLocation.SmlLocation2;
import net.opengis.sensorML.x101.SystemDocument;
import net.opengis.sensorML.x101.SystemType;
import net.opengis.sensorML.x101.ValidTimeDocument.ValidTime;
import net.opengis.swe.x101.DataComponentPropertyType;

/**
 * @since 1.0.0
 *
 */
public class SensorMLDecoderV101 extends AbstractSensorMLDecoder {

    private static final Logger LOGGER = LoggerFactory.getLogger(SensorMLDecoderV101.class);

    private static final Set<DecoderKey> DECODER_KEYS = CodingHelper.decoderKeysForElements(
            SensorMLConstants.NS_SML,
            SensorMLDocument.class,
            SystemDocument.class,
            SystemType.class,
            ProcessModelType.class);

    private static final Set<String> REMOVABLE_CAPABILITIES_NAMES = Sets
            .newHashSet(SensorMLConstants.ELEMENT_NAME_PARENT_PROCEDURES, SensorMLConstants.ELEMENT_NAME_OFFERINGS);

    private static final Set<String> REMOVABLE_COMPONENTS_ROLES = Collections
            .singleton(SensorMLConstants.ELEMENT_NAME_CHILD_PROCEDURES);

    //CHECKSTYLE:OFF
    private static final Map<String, ImmutableMap<String, Set<String>>> SUPPORTED_TRANSACTIONAL_PROCEDURE_DESCRIPTION_FORMATS
            = ImmutableMap.of(SosConstants.SOS, ImmutableMap.<String, Set<String>>builder()
                              .put(Sos2Constants.SERVICEVERSION, ImmutableSet.of(SensorMLConstants.SENSORML_OUTPUT_FORMAT_URL))
                              .build());
    //CHECKSTYLE:ON

    private static final ImmutableSet<SupportedType> SUPPORTED_TYPES = ImmutableSet.<SupportedType>builder()
            .add(new ProcedureDescriptionFormat(SensorMLConstants.SENSORML_OUTPUT_FORMAT_URL)).build();

    public SensorMLDecoderV101() {
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
        if (SUPPORTED_TRANSACTIONAL_PROCEDURE_DESCRIPTION_FORMATS.containsKey(service) &&
                 SUPPORTED_TRANSACTIONAL_PROCEDURE_DESCRIPTION_FORMATS.get(service).containsKey(version)) {
            return SUPPORTED_TRANSACTIONAL_PROCEDURE_DESCRIPTION_FORMATS.get(service).get(version);
        }
        return Collections.emptySet();
    }

    @Override
    public AbstractSensorML decode(XmlObject element) throws DecodingException {
        // validate document
        XmlHelper.validateDocument(element);
        if (element instanceof SensorMLDocument) {
            return parseSensorML((SensorMLDocument) element);
        } else if (element instanceof SystemDocument) {
            return parseSystem(((SystemDocument) element).getSystem());
        } else if (element instanceof SystemType) {
            return parseSystem((SystemType) element);
        } else if (element instanceof ProcessModelType) {
            return parseProcessModel((ProcessModelType) element);
        } else {
            throw new UnsupportedDecoderXmlInputException(this, element);
        }
    }

    @SuppressFBWarnings("BC_VACUOUS_INSTANCEOF")
    private SensorML parseSensorML(final SensorMLDocument xbSensorML) throws DecodingException {
        final SensorML sensorML = new SensorML();
        // get member process
        for (final Member xbMember : xbSensorML.getSensorML().getMemberArray()) {
            if (xbMember.getProcess() != null) {
                if (xbMember.getProcess() instanceof AbstractProcessType) {
                    final AbstractProcessType xbAbstractProcess = xbMember.getProcess();
                    AbstractProcess abstractProcess = null;
                    if (xbAbstractProcess.schemaType() == SystemType.type) {
                        abstractProcess = parseSystem((SystemType) xbAbstractProcess);
                    } else if (xbAbstractProcess.schemaType() == ProcessModelType.type) {
                        abstractProcess = parseProcessModel((ProcessModelType) xbAbstractProcess);
                    } else if (xbAbstractProcess.schemaType() == ComponentType.type) {
                        abstractProcess = parseComponent((ComponentType) xbAbstractProcess);
                    } else {
                        throw unsupportedMemberProcess(xbMember);
                    }
                    sensorML.addMember(abstractProcess);
                } else {
                    throw unsupportedMemberProcess(xbMember);
                }
            } else {
                throw new DecodingException(XmlHelper.getLocalName(xbMember),
                                            "The process of a member of the SensorML Document is null (%s)!", xbMember
                                                    .getProcess());
            }
        }
        sensorML.setXml(xbSensorML.xmlText(getXmlOptions()));
        return sensorML;
    }

    private void parseAbstractProcess(final AbstractProcessType xbAbstractProcess,
                                      final AbstractProcess abstractProcess) throws DecodingException {
        if (xbAbstractProcess.getId() != null) {
            abstractProcess.setGmlId(xbAbstractProcess.getId());
        }
        if (xbAbstractProcess.getIdentificationArray() != null) {
            parseIdentifications(abstractProcess, xbAbstractProcess.getIdentificationArray());
        }
        if (xbAbstractProcess.getClassificationArray() != null) {
            abstractProcess.setClassifications(parseClassification(xbAbstractProcess.getClassificationArray()));
        }
        if (xbAbstractProcess.getCharacteristicsArray() != null) {
            abstractProcess.setCharacteristics(parseCharacteristics(xbAbstractProcess.getCharacteristicsArray()));
        }
        if (xbAbstractProcess.getCapabilitiesArray() != null) {
            parseCapabilities(abstractProcess, xbAbstractProcess.getCapabilitiesArray());
            checkCapabilitiesForRemoval(xbAbstractProcess.getCapabilitiesArray())
                    .forEach(xbAbstractProcess::removeCapabilities);
        }
        if (xbAbstractProcess.isSetDescription()) {
            abstractProcess.addDescription(xbAbstractProcess.getDescription().getStringValue());
        }
        if (xbAbstractProcess.isSetValidTime()) {
            abstractProcess.setValidTime(parseValidTime(xbAbstractProcess.getValidTime()));
        }
        if (xbAbstractProcess.getContactArray() != null) {
            abstractProcess.setContact(parseContact(xbAbstractProcess.getContactArray()));
        }
        if (xbAbstractProcess.getDocumentationArray() != null) {
            abstractProcess.setDocumentation(parseDocumentation(xbAbstractProcess.getDocumentationArray()));
        }
        if (xbAbstractProcess.getHistoryArray() != null) {
            abstractProcess.setHistory(parseHistory(xbAbstractProcess.getHistoryArray()));
        }
        if (xbAbstractProcess.getKeywordsArray() != null) {
            abstractProcess.setKeywords(parseKeywords(xbAbstractProcess.getKeywordsArray()));
        }
        if (xbAbstractProcess.getNameArray() != null) {
            final int length = xbAbstractProcess.getNameArray().length;
            for (int i = 0; i < length; i++) {
                final Object decodedElement = decodeXmlElement(xbAbstractProcess.getNameArray(i));
                if (decodedElement instanceof CodeType) {
                    abstractProcess.addName((CodeType) decodedElement);
                }
            }
        }
    }

    private void parseAbstractDerivableComponent(final AbstractDerivableComponentType xbAbstractDerivableComponent,
                                                 final AbstractComponent abstractComponent) throws DecodingException {
        if (xbAbstractDerivableComponent.isSetPosition()) {
            abstractComponent.setPosition(parsePosition(xbAbstractDerivableComponent.getPosition()));
        }
        if (xbAbstractDerivableComponent.isSetSmlLocation()) {
            abstractComponent.setLocation(parseLocation(xbAbstractDerivableComponent.getSmlLocation()));
        }
        // TODO ...
    }

    private void parseAbstractComponent(final AbstractComponentType xbAbstractComponent,
                                        final AbstractProcess abstractProcess) throws DecodingException {
        if (xbAbstractComponent.isSetInputs()) {
            abstractProcess.setInputs(parseInputs(xbAbstractComponent.getInputs()));
        }
        if (xbAbstractComponent.isSetOutputs()) {
            abstractProcess.setOutputs(parseOutputs(xbAbstractComponent.getOutputs()));
        }
        if (xbAbstractComponent.isSetParameters()) {
            abstractProcess.setParameters(parseParameters(xbAbstractComponent.getParameters()));
        }
    }

    private void parseAbstractPureProcess(final AbstractPureProcessType xbAbstractPureProcess,
                                          final ProcessModel processModel) throws DecodingException {
        if (xbAbstractPureProcess.isSetInputs()) {
            processModel.setInputs(parseInputs(xbAbstractPureProcess.getInputs()));
        }
        if (xbAbstractPureProcess.isSetOutputs()) {
            processModel.setOutputs(parseOutputs(xbAbstractPureProcess.getOutputs()));
        }
        if (xbAbstractPureProcess.isSetParameters()) {
            processModel.setParameters(parseParameters(xbAbstractPureProcess.getParameters()));
        }

    }

    private System parseSystem(final SystemType xbSystemType) throws DecodingException {
        return parseSystem(xbSystemType, new System());
    }

    private System parseSystem(final SystemType xbSystemType, final System system) throws DecodingException {
        parseAbstractProcess(xbSystemType, system);
        parseAbstractComponent(xbSystemType, system);
        parseAbstractDerivableComponent(xbSystemType, system);
        if (xbSystemType.isSetComponents() && xbSystemType.getComponents().isSetComponentList()) {
            system.addComponents(parseComponents(xbSystemType.getComponents()));
            ComponentList componentList = xbSystemType.getComponents().getComponentList();
            checkComponentsForRemoval(componentList).forEach(componentList::removeComponent);
            checkAndRemoveEmptyComponents(xbSystemType);
        }
        final String xmlDescription = addSensorMLWrapperForXmlDescription(xbSystemType);
        system.setXml(xmlDescription);
        return system;
    }

    private AbstractProcess parseComponent(final ComponentType componentType) throws DecodingException {
        final org.n52.shetland.ogc.sensorML.Component component = new org.n52.shetland.ogc.sensorML.Component();
        parseAbstractProcess(componentType, component);
        parseAbstractDerivableComponent(componentType, component);
        parseAbstractComponent(componentType, component);
        if (componentType.isSetPosition()) {
            component.setPosition(parsePosition(componentType.getPosition()));
        }
        component.setXml(addSensorMLWrapperForXmlDescription(componentType));
        return component;
    }

    private ProcessModel parseProcessModel(final ProcessModelType xbProcessModel) throws DecodingException {
        final ProcessModel processModel = new ProcessModel();
        parseAbstractProcess(xbProcessModel, processModel);
        parseAbstractPureProcess(xbProcessModel, processModel);
        if (xbProcessModel.getMethod() != null) {
            processModel.setMethod(parseProcessMethod(xbProcessModel.getMethod()));
        }
        processModel.setXml(addSensorMLWrapperForXmlDescription(xbProcessModel));
        return processModel;
    }

    private ProcessMethod parseProcessMethod(final MethodPropertyType method) throws DecodingException {
        if (method.isSetHref()) {
            final ProcessMethod processMethod = new ProcessMethod(method.getHref());
            if (method.isSetTitle()) {
                processMethod.setTitle(method.getTitle());
            }
            if (method.isSetRole()) {
                processMethod.setRole(method.getRole());
            }
            return processMethod;
        } else if (method.isSetProcessMethod()) {
            final ProcessMethod processMethod = new ProcessMethod(parseRulesDefinition(method.getProcessMethod()
                    .getRules().getRulesDefinition()));
            // TODO implement parsing of sml:ProcessMethod
            return processMethod;
        }
        throw new DecodingException(
                "method", "The sml:method should contain a xlink:href attribut or a sml:ProcessMethod element!");
    }

    private RulesDefinition parseRulesDefinition(
            final net.opengis.sensorML.x101.ProcessMethodType.Rules.RulesDefinition xbRulesDefinition) {
        final RulesDefinition rulesDefinition = new RulesDefinition();
        if (xbRulesDefinition.isSetDescription()) {
            rulesDefinition.setDescription(xbRulesDefinition.getDescription().getStringValue());
        }
        // TODO add other options if required
        return rulesDefinition;
    }

    /**
     * Parses the identifications and sets the AbstractProcess' identifiers
     *
     * @param abstractProcess The AbstractProcess to which identifiers are added
     * @param identificationArray XML identification
     */
    private void parseIdentifications(final AbstractProcess abstractProcess,
                                      final Identification[] identificationArray) {
        for (final Identification xbIdentification : identificationArray) {
            if (xbIdentification.getIdentifierList() != null) {
                for (final Identifier xbIdentifier : xbIdentification.getIdentifierList().getIdentifierArray()) {
                    if (xbIdentifier.getName() != null && xbIdentifier.getTerm() != null) {
                        final SmlIdentifier identifier = new SmlIdentifier(xbIdentifier.getName(),
                                                                           xbIdentifier.getTerm().getDefinition(),
                                                                           xbIdentifier.getTerm().getValue());
                        abstractProcess.addIdentifier(identifier);
                        if (isIdentificationProcedureIdentifier(identifier)) {
                            abstractProcess.setIdentifier(identifier.getValue());
                        }
                    }
                }
            }
        }
    }

    /**
     * Parses the classification
     *
     * @param classificationArray XML classification
     *
     * @return SOS classification
     */
    private List<SmlClassifier> parseClassification(Classification[] classificationArray) {
        return Arrays.stream(classificationArray).map(Classification::getClassifierList)
                .map(ClassifierList::getClassifierArray).flatMap(Arrays::stream)
                .map(c -> {
                    net.opengis.sensorML.x101.TermDocument.Term term = c.getTerm();
                    String definition = term.isSetDefinition() ? term.getDefinition() : null;
                    String codespace = term.isSetCodeSpace() ? term.getCodeSpace().getHref() : null;
                    String value = term.getValue();
                    return new SmlClassifier(c.getName(), definition, codespace, value);
                }).collect(Collectors.toList());
    }

    /**
     * Parses the characteristics
     *
     * @param characteristicsArray XML characteristics
     *
     * @return SOS characteristics
     *
     *
     * @throws DecodingException if an error occurs
     */
    private List<SmlCharacteristics> parseCharacteristics(final Characteristics[] characteristicsArray)
            throws DecodingException {
        final List<SmlCharacteristics> sosCharacteristicsList = new ArrayList<>(characteristicsArray.length);
        for (final Characteristics xbCharacteristics : characteristicsArray) {
            final SmlCharacteristics sosCharacteristics = new SmlCharacteristics();
            if (xbCharacteristics.isSetName()) {
                sosCharacteristics.setName(xbCharacteristics.getName());
            }
            if (xbCharacteristics.isSetAbstractDataRecord()) {
                final Object decodedObject = decodeXmlElement(xbCharacteristics.getAbstractDataRecord());
                if (decodedObject instanceof DataRecord) {
                    sosCharacteristics.setDataRecord((DataRecord) decodedObject);
                } else {
                    throw new DecodingException(XmlHelper.getLocalName(xbCharacteristics),
                                                "Error while parsing the characteristics of the SensorML (the " +
                                                "characteristics' data record is not of type DataRecordPropertyType)!");
                }
            } else if (xbCharacteristics.isSetHref()) {
                sosCharacteristics.setHref(xbCharacteristics.getHref());
                if (xbCharacteristics.isSetTitle()) {
                    sosCharacteristics.setTitle(xbCharacteristics.getTitle());
                }
            }
            if (sosCharacteristics.isSetName()) {
                sosCharacteristicsList.add(sosCharacteristics);
            }
        }

        return sosCharacteristicsList;
    }

    /**
     * Parses the capabilities, processing and removing special insertion metadata
     *
     * @param abstractProcess The AbstractProcess to which capabilities and insertion metadata are added
     * @param capabilitiesArray XML capabilities
     *
     * @throws DecodingException if an error occurs
     */
    private void parseCapabilities(final AbstractProcess abstractProcess, final Capabilities[] capabilitiesArray)
            throws DecodingException {
        for (final Capabilities xbcaps : capabilitiesArray) {
            final SmlCapabilities caps = new SmlCapabilities();
            if (xbcaps.isSetName()) {
                caps.setName(xbcaps.getName());
            }
            if (xbcaps.isSetAbstractDataRecord()) {
                final Object o = decodeXmlElement(xbcaps.getAbstractDataRecord());
                if (o instanceof DataRecord) {
                    final DataRecord record = (DataRecord) o;
                    caps.setDataRecord(record).setName(xbcaps.getName());
                } else {
                    throw new DecodingException(XmlHelper.getLocalName(xbcaps),
                                                "Error while parsing the capabilities of the SensorML (the " +
                                                "capabilities data record is not of type DataRecordPropertyType)!");
                }
            } else if (xbcaps.isSetHref()) {
                caps.setHref(xbcaps.getHref());
                if (xbcaps.isSetTitle()) {
                    caps.setTitle(xbcaps.getTitle());
                }
            }
            if (caps.isSetName()) {
                abstractProcess.addCapabilities(caps);
            }
        }
    }

    /**
     * Parses the position
     *
     * @param position XML position
     *
     * @return SOS position
     *
     *
     * @throws DecodingException if an error occurs
     */
    private SmlPosition parsePosition(Position position) throws DecodingException {
        if (!position.isSetPosition()) {
            throw new DecodingException(XmlHelper.getLocalName(position),
                                        "Error while parsing the position of the SensorML (the position is not set)!");
        }
        SmlPosition sosSMLPosition = (SmlPosition) decodeXmlElement(position.getPosition());
        if (position.getName() != null) {
            sosSMLPosition.setName(position.getName());
        }
        return sosSMLPosition;
    }

    /**
     * Parses the location
     *
     * @param location XML location
     *
     * @return SOS location
     *
     *
     * @throws DecodingException if an error occurs
     */
    private org.n52.shetland.ogc.sensorML.elements.SmlLocation parseLocation(final SmlLocation2 location)
            throws DecodingException {
        if (!location.isSetPoint()) {
            throw new DecodingException(XmlHelper.getLocalName(location),
                                        "Error while parsing the sml:location of the SensorML " +
                                        "(point is not set, only point is supported)!");
        }
        org.n52.shetland.ogc.sensorML.elements.SmlLocation sosSmlLocation = null;
        final Object point = decodeXmlElement(location.getPoint());
        if (point instanceof Point) {
            sosSmlLocation = new org.n52.shetland.ogc.sensorML.elements.SmlLocation((Point) point);
        }
        return sosSmlLocation;
    }

    private Time parseValidTime(final ValidTime validTime) {
        // TODO Auto-generated method stub
        return null;
    }

    private List<SmlParameter> parseParameters(final Parameters parameters) throws DecodingException {
        if (CollectionHelper.isNotNullOrEmpty(parameters.getParameterList().getParameterArray())) {
            final List<SmlParameter> sweComponents
                    = new ArrayList<>(parameters.getParameterList().getParameterArray().length);
            for (final DataComponentPropertyType sweComponent : parameters.getParameterList().getParameterArray()) {
                sweComponents.add(parseDataComponentPropertyType(sweComponent));
            }
            return sweComponents;
        }
        return Collections.emptyList();
    }

    private List<SmlContact> parseContact(final Contact[] contactArray) {
        List<SmlContact> smlContacts = Lists.newArrayList();
        if (contactArray != null && contactArray.length > 0) {
            for (Contact contact : contactArray) {
                if (contact.isSetContactList()) {
                    smlContacts.add(parseContactListMembers(contact.getContactList()));
                } else if (contact.isSetPerson()) {
                    smlContacts.add(parsePerson(contact.getPerson()));
                } else if (contact.isSetResponsibleParty()) {
                    smlContacts.add(parseResponsibleParty(contact.getResponsibleParty()));
                } else if (contact.isSetHref()) {
                    SmlReferencedContact thisSmlContact = new SmlReferencedContact();
                    thisSmlContact.setHref(contact.getHref());
                    if (contact.isSetTitle()) {
                        thisSmlContact.setTitle(contact.getTitle());
                    }
                    if (contact.getRole() != null) {
                        thisSmlContact.setRole(contact.getRole());
                    }
                    smlContacts.add(thisSmlContact);
                }
            }
        }
        return smlContacts;
    }

    private SmlContact parseContactListMembers(final ContactList contactList) {
        SmlContactList smlContactList = new SmlContactList();
        if (contactList.getMemberArray() != null && contactList.getMemberArray().length > 0) {
            for (ContactList.Member member : contactList.getMemberArray()) {
                SmlContact thisSmlContact = null;
                if (member.getPerson() != null) {
                    thisSmlContact = parsePerson(member.getPerson());
                } else if (member.getResponsibleParty() != null) {
                    thisSmlContact = parseResponsibleParty(member.getResponsibleParty());
                } else if (member.isSetHref()) {
                    thisSmlContact = new SmlReferencedContact();
                    thisSmlContact.setHref(member.getHref());
                    if (member.isSetTitle()) {
                        thisSmlContact.setTitle(member.getTitle());
                    }
                }
                if (thisSmlContact != null) {
                    if (member.getRole() != null) {
                        thisSmlContact.setRole(member.getRole());
                    }
                    smlContactList.addMember(thisSmlContact);
                }
            }
        }
        return smlContactList;
    }

    private SmlPerson parsePerson(Person person) {
        SmlPerson smlPerson = new SmlPerson();
        if (person != null) {
            if (!Strings.isNullOrEmpty(person.getAffiliation())) {
                smlPerson.setAffiliation(person.getAffiliation());
            }
            if (!Strings.isNullOrEmpty(person.getEmail())) {
                smlPerson.setEmail(person.getEmail());
            }
            if (!Strings.isNullOrEmpty(person.getName())) {
                smlPerson.setName(person.getName());
            }
            if (!Strings.isNullOrEmpty(person.getPhoneNumber())) {
                smlPerson.setPhoneNumber(person.getPhoneNumber());
            }
            if (!Strings.isNullOrEmpty(person.getSurname())) {
                smlPerson.setSurname(person.getSurname());
            }
            if (!Strings.isNullOrEmpty(person.getUserID())) {
                smlPerson.setUserID(person.getUserID());
            }
        }
        return smlPerson;
    }

    private SmlResponsibleParty parseResponsibleParty(ResponsibleParty responsibleParty) {
        SmlResponsibleParty smlRespParty = new SmlResponsibleParty();
        if (responsibleParty != null) {
            if (!Strings.isNullOrEmpty(responsibleParty.getIndividualName())) {
                smlRespParty.setIndividualName(responsibleParty.getIndividualName());
            }
            if (!Strings.isNullOrEmpty(responsibleParty.getOrganizationName())) {
                smlRespParty.setOrganizationName(responsibleParty.getOrganizationName());
            }
            if (!Strings.isNullOrEmpty(responsibleParty.getPositionName())) {
                smlRespParty.setPositionName(responsibleParty.getPositionName());
            }
            if (responsibleParty.getContactInfo() != null) {
                ContactInfo contactInfo = responsibleParty.getContactInfo();
                if (contactInfo.getAddress() != null) {
                    Address address = contactInfo.getAddress();
                    if (!Strings.isNullOrEmpty(address.getAdministrativeArea())) {
                        smlRespParty.setAdministrativeArea(address.getAdministrativeArea());
                    }
                    if (!Strings.isNullOrEmpty(address.getCity())) {
                        smlRespParty.setCity(address.getCity());
                    }
                    if (!Strings.isNullOrEmpty(address.getCountry())) {
                        smlRespParty.setCountry(address.getCountry());
                    }
                    if (address.getDeliveryPointArray() != null && address.getDeliveryPointArray().length > 0) {
                        for (String deliveryPoint : address.getDeliveryPointArray()) {
                            smlRespParty.addDeliveryPoint(deliveryPoint);
                        }
                    }
                    if (!Strings.isNullOrEmpty(address.getElectronicMailAddress())) {
                        smlRespParty.setEmail(address.getElectronicMailAddress());
                    }
                    if (!Strings.isNullOrEmpty(address.getPostalCode())) {
                        smlRespParty.setPostalCode(address.getPostalCode());
                    }
                }
                if (!Strings.isNullOrEmpty(contactInfo.getContactInstructions())) {
                    smlRespParty.setContactInstructions(contactInfo.getContactInstructions());
                }
                if (!Strings.isNullOrEmpty(contactInfo.getHoursOfService())) {
                    smlRespParty.setHoursOfService(contactInfo.getHoursOfService());
                }
                if (contactInfo.getOnlineResourceArray() != null && contactInfo.getOnlineResourceArray().length > 0) {
                    for (OnlineResource onlineResource : contactInfo.getOnlineResourceArray()) {
                        if (!Strings.isNullOrEmpty(onlineResource.getHref())) {
                            smlRespParty.addOnlineResource(onlineResource.getHref());
                        }
                    }
                }
                if (contactInfo.getPhone() != null) {
                    Phone phone = contactInfo.getPhone();
                    if (phone.getVoiceArray() != null && phone.getVoiceArray().length > 0) {
                        for (String phoneVoice : phone.getVoiceArray()) {
                            smlRespParty.addPhoneVoice(phoneVoice);
                        }
                    }
                    if (phone.getFacsimileArray() != null && phone.getFacsimileArray().length > 0) {
                        for (String phoneFax : phone.getFacsimileArray()) {
                            smlRespParty.addPhoneFax(phoneFax);
                        }
                    }
                }
            }
        }
        return smlRespParty;
    }

    private List<AbstractSmlDocumentation> parseDocumentation(final Documentation[] documentationArray) {
        final List<AbstractSmlDocumentation> abstractDocumentation = new ArrayList<>(documentationArray.length);
        // TODO Auto-generated method stub
        return abstractDocumentation;
    }

    private List<String> parseKeywords(final Keywords[] keywordsArray) {
        final Set<String> keywords = Sets.newHashSet();
        if (keywordsArray != null && keywordsArray.length > 0) {
            for (final Keywords keyword : keywordsArray) {
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

    private String parseHistory(final History[] historyArray) {
        // TODO Auto-generated method stub
        return "";
    }

    /**
     * Parses the inputs
     *
     * @param inputs XML inputs
     *
     * @return SOS inputs
     *
     *
     * @throws DecodingException if an error occurs
     */
    private List<SmlIo> parseInputs(final Inputs inputs) throws DecodingException {
        final List<SmlIo> sosInputs = new ArrayList<>(inputs.getInputList().getInputArray().length);
        for (final IoComponentPropertyType xbInput : inputs.getInputList().getInputArray()) {
            sosInputs.add(parseIoComponentPropertyType(xbInput));
        }
        return sosInputs;
    }

    /**
     * Parses the outputs
     *
     * @param outputs XML outputs
     *
     * @return SOS outputs
     *
     *
     * @throws DecodingException if an error occurs
     */
    private List<SmlIo> parseOutputs(final Outputs outputs) throws DecodingException {
        final List<SmlIo> sosOutputs = new ArrayList<>(outputs.getOutputList().getOutputArray().length);
        for (final IoComponentPropertyType xbOutput : outputs.getOutputList().getOutputArray()) {
            sosOutputs.add(parseIoComponentPropertyType(xbOutput));
        }
        return sosOutputs;
    }

    private List<SmlComponent> parseComponents(final Components components) throws DecodingException {
        final List<SmlComponent> sosSmlComponents = Lists.newLinkedList();
        if (components.isSetComponentList() && components.getComponentList().getComponentArray() != null) {
            for (final Component component : components.getComponentList().getComponentArray()) {
                if (component.isSetProcess() || component.isSetHref()) {
                    final SmlComponent sosSmlcomponent = new SmlComponent(component.getName());
                    AbstractProcess abstractProcess = null;
                    if (component.isSetProcess()) {
                        if (component.getProcess() instanceof SystemType) {
                            abstractProcess = new System();
                            parseSystem((SystemType) component.getProcess(), (System) abstractProcess);
                        } else {
                            abstractProcess = new AbstractProcess();
                            parseAbstractProcess(component.getProcess(), abstractProcess);
                        }
                    } else {
                        abstractProcess = new AbstractProcess();
                        abstractProcess.setIdentifier(component.getHref());
                    }
                    sosSmlcomponent.setProcess(abstractProcess);
                    sosSmlComponents.add(sosSmlcomponent);
                }
            }
        }
        return sosSmlComponents;
    }

    /**
     * Parses the components
     *
     * @param xbIoCompPropType XML components
     *
     * @return SOS components
     *
     *
     * @throws DecodingException if an error occurs
     */
    private SmlIo parseIoComponentPropertyType(final IoComponentPropertyType xbIoCompPropType)
            throws DecodingException {
        final SmlIo sosIo = new SmlIo();
        sosIo.setIoName(xbIoCompPropType.getName());
        XmlObject toDecode = null;
        if (xbIoCompPropType.isSetHref()) {
            sosIo.setHref(xbIoCompPropType.getHref());
            if (xbIoCompPropType.isSetTitle()) {
                sosIo.setTitle(xbIoCompPropType.getTitle());
            }
            return sosIo;
        }
        if (xbIoCompPropType.isSetBoolean()) {
            toDecode = xbIoCompPropType.getBoolean();
        } else if (xbIoCompPropType.isSetCategory()) {
            toDecode = xbIoCompPropType.getCategory();
        } else if (xbIoCompPropType.isSetCount()) {
            toDecode = xbIoCompPropType.getCount();
        } else if (xbIoCompPropType.isSetCountRange()) {
            toDecode = xbIoCompPropType.getCountRange();
        } else if (xbIoCompPropType.isSetObservableProperty()) {
            toDecode = xbIoCompPropType.getObservableProperty();
        } else if (xbIoCompPropType.isSetQuantity()) {
            toDecode = xbIoCompPropType.getQuantity();
        } else if (xbIoCompPropType.isSetQuantityRange()) {
            toDecode = xbIoCompPropType.getQuantityRange();
        } else if (xbIoCompPropType.isSetText()) {
            toDecode = xbIoCompPropType.getText();
        } else if (xbIoCompPropType.isSetTime()) {
            toDecode = xbIoCompPropType.getTime();
        } else if (xbIoCompPropType.isSetTimeRange()) {
            toDecode = xbIoCompPropType.getTimeRange();
        } else if (xbIoCompPropType.isSetAbstractDataArray1()) {
            toDecode = xbIoCompPropType.getAbstractDataArray1();
        } else if (xbIoCompPropType.isSetAbstractDataRecord()) {
            toDecode = xbIoCompPropType.getAbstractDataRecord();
        } else {
            throw new DecodingException(XmlHelper.getLocalName(xbIoCompPropType),
                                        "An 'IoComponentProperty' is not supported");
        }

        final Object decodedObject = decodeXmlElement(toDecode);
        if (decodedObject instanceof SweAbstractDataComponent) {
            sosIo.setIoValue((SweAbstractDataComponent) decodedObject);
        } else {
            throw new DecodingException(XmlHelper.getLocalName(xbIoCompPropType),
                                        "The 'IoComponentProperty' with type '%s' as value for '%s' is not supported.",
                                        XmlHelper.getLocalName(toDecode), XmlHelper.getLocalName(xbIoCompPropType));
        }
        return sosIo;
    }

    private SmlParameter parseDataComponentPropertyType(final DataComponentPropertyType xbDataComponentPropertyType)
            throws DecodingException {
        final SmlParameter smlParameter = new SmlParameter();
        smlParameter.setName(xbDataComponentPropertyType.getName());
        XmlObject toDecode = null;
        if (xbDataComponentPropertyType.isSetHref()) {
            smlParameter.setHref(xbDataComponentPropertyType.getHref());
            if (xbDataComponentPropertyType.isSetTitle()) {
                smlParameter.setTitle(xbDataComponentPropertyType.getTitle());
            }
            return smlParameter;
        }
        if (xbDataComponentPropertyType.isSetBoolean()) {
            toDecode = xbDataComponentPropertyType.getBoolean();
        } else if (xbDataComponentPropertyType.isSetCategory()) {
            toDecode = xbDataComponentPropertyType.getCategory();
        } else if (xbDataComponentPropertyType.isSetCount()) {
            toDecode = xbDataComponentPropertyType.getCount();
        } else if (xbDataComponentPropertyType.isSetCountRange()) {
            toDecode = xbDataComponentPropertyType.getCountRange();
        } else if (xbDataComponentPropertyType.isSetQuantity()) {
            toDecode = xbDataComponentPropertyType.getQuantity();
        } else if (xbDataComponentPropertyType.isSetQuantityRange()) {
            toDecode = xbDataComponentPropertyType.getQuantityRange();
        } else if (xbDataComponentPropertyType.isSetText()) {
            toDecode = xbDataComponentPropertyType.getText();
        } else if (xbDataComponentPropertyType.isSetTime()) {
            toDecode = xbDataComponentPropertyType.getTime();
        } else if (xbDataComponentPropertyType.isSetTimeRange()) {
            toDecode = xbDataComponentPropertyType.getTimeRange();
        } else if (xbDataComponentPropertyType.isSetAbstractDataArray1()) {
            toDecode = xbDataComponentPropertyType.getAbstractDataArray1();
        } else if (xbDataComponentPropertyType.isSetAbstractDataRecord()) {
            toDecode = xbDataComponentPropertyType.getAbstractDataRecord();
        } else {
            throw new DecodingException(XmlHelper.getLocalName(xbDataComponentPropertyType),
                                        "An 'DataComponentProperty' is not supported");
        }

        final Object decodedObject = decodeXmlElement(toDecode);
        if (decodedObject instanceof SweAbstractDataComponent) {
            smlParameter.setParameter((SweAbstractDataComponent) decodedObject);
        } else {
            throw new DecodingException(XmlHelper.getLocalName(xbDataComponentPropertyType),
                    "The 'DataComponentProperty' with type '%s' as value for '%s' is not supported.",
                    XmlHelper.getLocalName(toDecode), XmlHelper.getLocalName(xbDataComponentPropertyType));
        }
        return smlParameter;
    }

    private String addSensorMLWrapperForXmlDescription(final AbstractProcessType xbProcessType) {
        final SensorMLDocument xbSensorMLDoc = SensorMLDocument.Factory.newInstance(getXmlOptions());
        final net.opengis.sensorML.x101.SensorMLDocument.SensorML xbSensorML = xbSensorMLDoc.addNewSensorML();
        xbSensorML.setVersion(SensorMLConstants.VERSION_V101);
        final Member member = xbSensorML.addNewMember();
        final AbstractProcessType xbAbstractProcessType = (AbstractProcessType) member.addNewProcess()
                .substitute(getQnameForType(xbProcessType.schemaType()), xbProcessType.schemaType());
        xbAbstractProcessType.set(xbProcessType);
        return xbSensorMLDoc.xmlText(getXmlOptions());
    }

    private QName getQnameForType(final SchemaType type) {
        if (type == SystemType.type) {
            return SensorMLConstants.SYSTEM_QNAME;
        } else if (type == ProcessModelType.type) {
            return SensorMLConstants.PROCESS_MODEL_QNAME;
        } else if (type == ComponentType.type) {
            return SensorMLConstants.COMPONENT_QNAME;
        }
        return SensorMLConstants.ABSTRACT_PROCESS_QNAME;
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

    private List<Integer> checkComponentsForRemoval(final ComponentList componentList) {
        final List<Integer> removeableComponents = new ArrayList<>(0);
        if (componentList != null && componentList.getComponentArray() != null) {
            final Component[] componentArray = componentList.getComponentArray();
            for (int i = 0; i < componentArray.length; i++) {
                if (componentArray[i].getRole() != null &&
                         REMOVABLE_COMPONENTS_ROLES.contains(componentArray[i].getRole())) {
                    removeableComponents.add(i);
                }
            }
        }
        return removeableComponents;
    }

    private void checkAndRemoveEmptyComponents(final SystemType system) {
        boolean removeComponents = false;
        final Components components = system.getComponents();
        if (components != null) {
            if (components.getComponentList() == null) {
                removeComponents = true;
            } else if (components.getComponentList().getComponentArray() == null ||
                     components.getComponentList().getComponentArray() != null &&
                        components.getComponentList().getComponentArray().length == 0) {
                removeComponents = true;
            }
        }
        if (removeComponents) {
            system.unsetComponents();
        }
    }

    private static DecodingException unsupportedMemberProcess(final Member xbMember) {
        return new DecodingException(XmlHelper.getLocalName(xbMember),
                "The process of a member of the SensorML Document (%s) is not supported!",
                xbMember.getProcess().getDomNode().getNodeName());
    }

}
