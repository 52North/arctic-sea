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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.n52.janmayen.NcName;
import org.n52.janmayen.http.MediaType;
import org.n52.shetland.iso.gmd.GmdConstants;
import org.n52.shetland.ogc.OGCConstants;
import org.n52.shetland.ogc.SupportedType;
import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.gml.CodeType;
import org.n52.shetland.ogc.gml.GmlConstants;
import org.n52.shetland.ogc.gml.time.Time;
import org.n52.shetland.ogc.gml.time.TimeInstant;
import org.n52.shetland.ogc.gml.time.TimePeriod;
import org.n52.shetland.ogc.sensorML.AbstractProcess;
import org.n52.shetland.ogc.sensorML.AbstractSensorML;
import org.n52.shetland.ogc.sensorML.HasComponents;
import org.n52.shetland.ogc.sensorML.HasProcessMethod;
import org.n52.shetland.ogc.sensorML.SensorML20Constants;
import org.n52.shetland.ogc.sensorML.SensorMLConstants;
import org.n52.shetland.ogc.sensorML.SmlContact;
import org.n52.shetland.ogc.sensorML.SmlResponsibleParty;
import org.n52.shetland.ogc.sensorML.Term;
import org.n52.shetland.ogc.sensorML.elements.AbstractSmlDocumentation;
import org.n52.shetland.ogc.sensorML.elements.SmlCapabilities;
import org.n52.shetland.ogc.sensorML.elements.SmlCapability;
import org.n52.shetland.ogc.sensorML.elements.SmlCharacteristic;
import org.n52.shetland.ogc.sensorML.elements.SmlCharacteristics;
import org.n52.shetland.ogc.sensorML.elements.SmlClassifier;
import org.n52.shetland.ogc.sensorML.elements.SmlComponent;
import org.n52.shetland.ogc.sensorML.elements.SmlConnection;
import org.n52.shetland.ogc.sensorML.elements.SmlDocumentation;
import org.n52.shetland.ogc.sensorML.elements.SmlDocumentationList;
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
import org.n52.shetland.ogc.sos.Sos1Constants;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.swe.SweAbstractDataComponent;
import org.n52.shetland.ogc.swe.SweConstants;
import org.n52.shetland.ogc.swe.SweDataArray;
import org.n52.shetland.ogc.swe.SweDataRecord;
import org.n52.shetland.ogc.swe.SweVector;
import org.n52.shetland.ogc.swe.simpleType.SweObservableProperty;
import org.n52.shetland.util.CollectionHelper;
import org.n52.shetland.util.IdGenerator;
import org.n52.shetland.w3c.SchemaLocation;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.exception.UnsupportedEncoderInputException;
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

import net.opengis.sensorml.x20.AbstractPhysicalProcessType;
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
import net.opengis.sensorml.x20.CharacteristicListType;
import net.opengis.sensorml.x20.CharacteristicListType.Characteristic;
import net.opengis.sensorml.x20.ClassifierListPropertyType;
import net.opengis.sensorml.x20.ClassifierListType;
import net.opengis.sensorml.x20.ComponentListPropertyType;
import net.opengis.sensorml.x20.ComponentListType;
import net.opengis.sensorml.x20.ComponentListType.Component;
import net.opengis.sensorml.x20.ConnectionListPropertyType;
import net.opengis.sensorml.x20.ConnectionListType;
import net.opengis.sensorml.x20.ContactListType;
import net.opengis.sensorml.x20.DataComponentOrObservablePropertyType;
import net.opengis.sensorml.x20.DescribedObjectType;
import net.opengis.sensorml.x20.DescribedObjectType.Capabilities;
import net.opengis.sensorml.x20.DescribedObjectType.Characteristics;
import net.opengis.sensorml.x20.DocumentListPropertyType;
import net.opengis.sensorml.x20.DocumentListType;
import net.opengis.sensorml.x20.FeatureListType;
import net.opengis.sensorml.x20.IdentifierListPropertyType;
import net.opengis.sensorml.x20.IdentifierListType;
import net.opengis.sensorml.x20.InputListType;
import net.opengis.sensorml.x20.InputListType.Input;
import net.opengis.sensorml.x20.LinkType;
import net.opengis.sensorml.x20.ObservablePropertyType;
import net.opengis.sensorml.x20.OutputListType;
import net.opengis.sensorml.x20.OutputListType.Output;
import net.opengis.sensorml.x20.ParameterListType;
import net.opengis.sensorml.x20.ParameterListType.Parameter;
import net.opengis.sensorml.x20.PhysicalComponentDocument;
import net.opengis.sensorml.x20.PhysicalComponentPropertyType;
import net.opengis.sensorml.x20.PhysicalComponentType;
import net.opengis.sensorml.x20.PhysicalSystemDocument;
import net.opengis.sensorml.x20.PhysicalSystemPropertyType;
import net.opengis.sensorml.x20.PhysicalSystemType;
import net.opengis.sensorml.x20.PositionUnionPropertyType;
import net.opengis.sensorml.x20.ProcessMethodType;
import net.opengis.sensorml.x20.SimpleProcessDocument;
import net.opengis.sensorml.x20.SimpleProcessPropertyType;
import net.opengis.sensorml.x20.SimpleProcessType;
import net.opengis.sensorml.x20.TermType;

/**
 * {@link AbstractSensorMLEncoder} class to encode OGC SensorML 2.0
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public class SensorMLEncoderv20
        extends
        AbstractSensorMLEncoder {
    private static final Logger LOGGER = LoggerFactory.getLogger(SensorMLEncoderv20.class);

    private static final ImmutableSet<SupportedType> SUPPORTED_TYPES = ImmutableSet.<SupportedType> builder()
            .add(new ProcedureDescriptionFormat(SensorML20Constants.SENSORML_20_OUTPUT_FORMAT_URL))
            .add(new ProcedureDescriptionFormat(SensorML20Constants.SENSORML_20_CONTENT_TYPE.toString())).build();

    private static final Map<String, Map<String, Set<String>>> SUPPORTED_PROCEDURE_DESCRIPTION_FORMATS =
            ImmutableMap.of(SosConstants.SOS,
                    ImmutableMap.<String, Set<String>> builder()
                            .put(Sos2Constants.SERVICEVERSION,
                                    ImmutableSet.of(SensorML20Constants.SENSORML_20_OUTPUT_FORMAT_URL))
                            .put(Sos1Constants.SERVICEVERSION,
                                    ImmutableSet.of(SensorML20Constants.SENSORML_20_OUTPUT_FORMAT_MIME_TYPE))
                            .build());

    private static final Set<EncoderKey> ENCODER_KEYS = CollectionHelper.union(
            CodingHelper.encoderKeysForElements(SensorML20Constants.NS_SML_20, AbstractSensorML.class,
                    DescribedObject.class),
            CodingHelper.encoderKeysForElements(SensorML20Constants.SENSORML_20_CONTENT_TYPE.toString(),
                    AbstractSensorML.class, DescribedObject.class));

    public SensorMLEncoderv20() {
        LOGGER.debug("Encoder for the following keys initialized successfully: {}!",
                Joiner.on(", ").join(ENCODER_KEYS));
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
    public void addNamespacePrefixToMap(final Map<String, String> nameSpacePrefixMap) {
        nameSpacePrefixMap.put(SensorML20Constants.NS_SML_20, SensorML20Constants.NS_SML_PREFIX);
    }

    @Override
    public MediaType getContentType() {
        return SensorML20Constants.SENSORML_20_CONTENT_TYPE;
    }

    @Override
    public Set<SchemaLocation> getSchemaLocations() {
        return Sets.newHashSet(SensorML20Constants.SML_20_SCHEMA_LOCATION);
    }

    @Override
    public Set<String> getSupportedProcedureDescriptionFormats(final String service, final String version) {
        return SUPPORTED_PROCEDURE_DESCRIPTION_FORMATS.getOrDefault(service, Collections.emptyMap())
                .getOrDefault(version, Collections.emptySet());
    }

    @Override
    public XmlObject encode(Object objectToEncode, EncodingContext additionalValues)
            throws EncodingException {
        XmlObject encodedObject = null;
        try {
            if (objectToEncode instanceof AbstractSensorML) {
                AbstractSensorML description = (AbstractSensorML) objectToEncode;
                if (description.isSetXml()) {
                    encodedObject = XmlObject.Factory.parse(((AbstractSensorML) objectToEncode).getXml());
                    addValuesToXmlObject(encodedObject, (AbstractSensorML) objectToEncode);
                    encodedObject = checkForAdditionalValues(encodedObject, additionalValues);
                } else {
                    encodedObject = encodeDescription(description, additionalValues);
                }
            } else {
                throw new UnsupportedEncoderInputException(this, objectToEncode);
            }
        } catch (XmlException xmle) {
            throw new EncodingException(xmle);
        }
        // check if all gml:id are unique
        XmlHelper.makeGmlIdsUnique(encodedObject.getDomNode());
        XmlHelper.validateDocument(encodedObject, EncodingException::new);
        return encodedObject;
    }

    private XmlObject checkForAdditionalValues(XmlObject element, EncodingContext additionalValues) {
        boolean doc = additionalValues.has(XmlBeansEncodingFlags.DOCUMENT);
        boolean propertyType = additionalValues.has(XmlBeansEncodingFlags.PROPERTY_TYPE);
        boolean type = additionalValues.has(XmlBeansEncodingFlags.TYPE);
        if (element instanceof PhysicalSystemDocument) {
            if (propertyType) {
                PhysicalSystemPropertyType pspt = PhysicalSystemPropertyType.Factory.newInstance(getXmlOptions());
                pspt.setPhysicalSystem(((PhysicalSystemDocument) element).getPhysicalSystem());
                return pspt;
            } else if (type) {
                return ((PhysicalSystemDocument) element).getPhysicalSystem();
            }
        } else if (element instanceof PhysicalSystemPropertyType) {
            if (doc) {
                PhysicalSystemDocument psd = PhysicalSystemDocument.Factory.newInstance(getXmlOptions());
                psd.setPhysicalSystem(((PhysicalSystemPropertyType) element).getPhysicalSystem());
                return psd;
            } else if (type) {
                return ((PhysicalSystemPropertyType) element).getPhysicalSystem();
            }
        } else if (element instanceof PhysicalSystemType) {
            if (doc) {
                PhysicalSystemDocument psd = PhysicalSystemDocument.Factory.newInstance(getXmlOptions());
                psd.setPhysicalSystem((PhysicalSystemType) element);
                return psd;
            } else if (propertyType) {
                PhysicalSystemPropertyType pspt = PhysicalSystemPropertyType.Factory.newInstance(getXmlOptions());
                pspt.setPhysicalSystem((PhysicalSystemType) element);
                return pspt;
            }
        } else if (element instanceof PhysicalComponentDocument) {
            if (propertyType) {
                PhysicalComponentPropertyType pcpt =
                        PhysicalComponentPropertyType.Factory.newInstance(getXmlOptions());
                pcpt.setPhysicalComponent(((PhysicalComponentDocument) element).getPhysicalComponent());
            } else if (type) {
                return ((PhysicalComponentDocument) element).getPhysicalComponent();
            }
        } else if (element instanceof PhysicalComponentPropertyType) {
            if (doc) {
                PhysicalComponentDocument pcd = PhysicalComponentDocument.Factory.newInstance(getXmlOptions());
                pcd.setPhysicalComponent(((PhysicalComponentPropertyType) element).getPhysicalComponent());
                return pcd;
            } else if (type) {
                return ((PhysicalComponentPropertyType) element).getPhysicalComponent();
            }
        } else if (element instanceof PhysicalComponentType) {
            if (doc) {
                PhysicalComponentDocument pcd = PhysicalComponentDocument.Factory.newInstance(getXmlOptions());
                pcd.setPhysicalComponent((PhysicalComponentType) element);
                return pcd;
            } else if (propertyType) {
                PhysicalComponentPropertyType pcpt =
                        PhysicalComponentPropertyType.Factory.newInstance(getXmlOptions());
                pcpt.setPhysicalComponent((PhysicalComponentType) element);
                return pcpt;
            }
        } else if (element instanceof SimpleProcessDocument) {
            if (propertyType) {
                SimpleProcessPropertyType sppt = SimpleProcessPropertyType.Factory.newInstance(getXmlOptions());
                sppt.setSimpleProcess(((SimpleProcessDocument) element).getSimpleProcess());
            } else if (type) {
                return ((SimpleProcessDocument) element).getSimpleProcess();
            }
        } else if (element instanceof SimpleProcessPropertyType) {
            if (doc) {
                SimpleProcessDocument spd = SimpleProcessDocument.Factory.newInstance(getXmlOptions());
                spd.setSimpleProcess(((SimpleProcessPropertyType) element).getSimpleProcess());
                return spd;
            } else if (type) {
                return ((SimpleProcessPropertyType) element).getSimpleProcess();
            }
        } else if (element instanceof SimpleProcessType) {
            if (doc) {
                SimpleProcessDocument spd = SimpleProcessDocument.Factory.newInstance(getXmlOptions());
                spd.setSimpleProcess((SimpleProcessType) element);
                return spd;
            } else if (propertyType) {
                SimpleProcessPropertyType sppt = SimpleProcessPropertyType.Factory.newInstance(getXmlOptions());
                sppt.setSimpleProcess((SimpleProcessType) element);
                return sppt;
            }
        } else if (element instanceof AggregateProcessDocument) {
            if (propertyType) {
                AggregateProcessPropertyType appt = AggregateProcessPropertyType.Factory.newInstance(getXmlOptions());
                appt.setAggregateProcess(((AggregateProcessDocument) element).getAggregateProcess());
            } else if (type) {
                return ((AggregateProcessDocument) element).getAggregateProcess();
            }
        } else if (element instanceof AggregateProcessPropertyType) {
            if (doc) {
                AggregateProcessDocument apd = AggregateProcessDocument.Factory.newInstance(getXmlOptions());
                apd.setAggregateProcess(((AggregateProcessPropertyType) element).getAggregateProcess());
                return apd;
            } else if (type) {
                return ((AggregateProcessPropertyType) element).getAggregateProcess();
            }
        } else if (element instanceof AggregateProcessType) {
            if (doc) {
                AggregateProcessDocument apd = AggregateProcessDocument.Factory.newInstance(getXmlOptions());
                apd.setAggregateProcess((AggregateProcessType) element);
                return apd;
            } else if (propertyType) {
                AggregateProcessPropertyType appt = AggregateProcessPropertyType.Factory.newInstance(getXmlOptions());
                appt.setAggregateProcess((AggregateProcessType) element);
                return appt;
            }
        }
        return element;
    }

    private void addValuesToXmlObject(XmlObject element, AbstractSensorML description)
            throws EncodingException {
        if (element instanceof PhysicalSystemDocument) {
            addPhysicalSystemValues(((PhysicalSystemDocument) element).getPhysicalSystem(),
                    (PhysicalSystem) description);
        } else if (element instanceof PhysicalSystemPropertyType) {
            addPhysicalSystemValues(((PhysicalSystemPropertyType) element).getPhysicalSystem(),
                    (PhysicalSystem) description);
        } else if (element instanceof PhysicalComponentDocument && description instanceof PhysicalComponent) {
            addPhysicalComponentValues(((PhysicalComponentDocument) element).getPhysicalComponent(),
                    (PhysicalComponent) description);
        } else if (element instanceof PhysicalComponentPropertyType && description instanceof PhysicalComponent) {
            addPhysicalComponentValues(((PhysicalComponentPropertyType) element).getPhysicalComponent(),
                    (PhysicalComponent) description);
        } else if (element instanceof SimpleProcessDocument && description instanceof SimpleProcess) {
            addSimpleProcessValues(((SimpleProcessDocument) element).getSimpleProcess(), (SimpleProcess) description);
        } else if (element instanceof SimpleProcessPropertyType && description instanceof SimpleProcess) {
            addSimpleProcessValues(((SimpleProcessPropertyType) element).getSimpleProcess(),
                    (SimpleProcess) description);
        } else if (element instanceof AggregateProcessDocument && description instanceof AggregateProcess) {
            addAggregateProcessValues(((AggregateProcessDocument) element).getAggregateProcess(),
                    (AggregateProcess) description);
        } else if (element instanceof AggregateProcessPropertyType && description instanceof AggregateProcess) {
            addAggregateProcessValues(((AggregateProcessPropertyType) element).getAggregateProcess(),
                    (AggregateProcess) description);
        }
    }

    private XmlObject encodeDescription(AbstractSensorML description, EncodingContext additionalValues)
            throws EncodingException {
        XmlObject absProc = null;
        if (description instanceof AbstractPhysicalProcess) {
            absProc = encodeAbstractPhysicalProcess((AbstractPhysicalProcess) description, additionalValues);
        } else if (description instanceof SimpleProcess) {
            absProc = encodeSimpleProcess((SimpleProcess) description, additionalValues);
        } else if (description instanceof AggregateProcess) {
            absProc = encodeAggregateProcess((AggregateProcess) description, additionalValues);
        } else {
            throw new UnsupportedEncoderInputException(this, description);
        }
        return absProc;
    }

    private XmlObject encodeSimpleProcess(SimpleProcess abstractProcess, EncodingContext additionalValues)
            throws EncodingException {
        SimpleProcessPropertyType sppt = SimpleProcessPropertyType.Factory.newInstance(getXmlOptions());
        addSimpleProcessValues(sppt.addNewSimpleProcess(), abstractProcess);
        if (additionalValues.has(XmlBeansEncodingFlags.DOCUMENT)) {
            SimpleProcessDocument spd = SimpleProcessDocument.Factory.newInstance(getXmlOptions());
            spd.setSimpleProcess(sppt.getSimpleProcess());
        } else if (additionalValues.has(XmlBeansEncodingFlags.TYPE)) {
            return sppt.getSimpleProcess();
        }
        return sppt;
    }

    private void addSimpleProcessValues(SimpleProcessType spt, SimpleProcess abstractProcess)
            throws EncodingException {
        addAbstractProcessValues(spt, abstractProcess);
        addDescribedObjectValues(spt, abstractProcess);
        // set method
        if (abstractProcess.isSetMethod()) {
            spt.addNewMethod().setProcessMethod(createProcessMethod(abstractProcess));
        }
    }

    private XmlObject encodeAggregateProcess(AggregateProcess abstractProcess, EncodingContext additionalValues)
            throws EncodingException {
        AggregateProcessPropertyType appt = AggregateProcessPropertyType.Factory.newInstance(getXmlOptions());
        addAggregateProcessValues(appt.addNewAggregateProcess(), abstractProcess);
        if (additionalValues.has(XmlBeansEncodingFlags.DOCUMENT)) {
            AggregateProcessDocument apd = AggregateProcessDocument.Factory.newInstance(getXmlOptions());
            apd.setAbstractProcess(appt.getAggregateProcess());
        } else if (additionalValues.has(XmlBeansEncodingFlags.TYPE)) {
            return appt.getAggregateProcess();
        }
        return appt;
    }

    private void addAggregateProcessValues(AggregateProcessType apt, AggregateProcess abstractProcess)
            throws EncodingException {
        addAbstractProcessValues(apt, abstractProcess);
        addDescribedObjectValues(apt, abstractProcess);
        // set components
        if (abstractProcess.isSetComponents()) {
            List<SmlComponent> smlComponents = checkForComponents(abstractProcess);
            if (!smlComponents.isEmpty()) {
                ComponentListPropertyType clpt = createComponents(smlComponents);
                if (clpt != null && clpt.getComponentList() != null
                        && clpt.getComponentList().sizeOfComponentArray() > 0) {
                    apt.setComponents(clpt);
                }
            }
        }
        // set connections
        if (abstractProcess.isSetConnections() && !apt.isSetConnections()) {
            apt.setConnections(createConnections(abstractProcess.getConnections()));
        }
    }

    private XmlObject encodeAbstractPhysicalProcess(AbstractPhysicalProcess abstractPhysicalProcess,
            EncodingContext additionalValues)
            throws EncodingException {
        XmlObject absPhysObj = null;
        if (abstractPhysicalProcess instanceof PhysicalSystem) {
            absPhysObj = encodePhysicalSystem((PhysicalSystem) abstractPhysicalProcess, additionalValues);
        } else if (abstractPhysicalProcess instanceof PhysicalComponent) {
            absPhysObj = encodePhysicalComponent((PhysicalComponent) abstractPhysicalProcess, additionalValues);
        } else {
            throw new UnsupportedEncoderInputException(this, abstractPhysicalProcess);
        }
        return absPhysObj;
    }

    private XmlObject encodePhysicalComponent(PhysicalComponent abstractPhysicalProcess,
            EncodingContext additionalValues)
            throws EncodingException {
        PhysicalComponentPropertyType pcpt = PhysicalComponentPropertyType.Factory.newInstance(getXmlOptions());
        addPhysicalComponentValues(pcpt.addNewPhysicalComponent(), abstractPhysicalProcess);
        if (additionalValues.has(XmlBeansEncodingFlags.DOCUMENT)) {
            PhysicalComponentDocument pcd = PhysicalComponentDocument.Factory.newInstance(getXmlOptions());
            pcd.setPhysicalComponent(pcpt.getPhysicalComponent());
        } else if (additionalValues.has(XmlBeansEncodingFlags.TYPE)) {
            return pcpt.getPhysicalComponent();
        }
        return pcpt;
    }

    private void addPhysicalComponentValues(PhysicalComponentType pct, PhysicalComponent abstractPhysicalProcess)
            throws EncodingException {
        addAbstractProcessValues(pct, abstractPhysicalProcess);
        addDescribedObjectValues(pct, abstractPhysicalProcess);
        addAbstractPhysicalProcessValues(pct, abstractPhysicalProcess);
        // set method
        if (abstractPhysicalProcess.isSetMethod()) {
            pct.addNewMethod().setProcessMethod(createProcessMethod(abstractPhysicalProcess));
        }
    }

    private XmlObject encodePhysicalSystem(PhysicalSystem abstractPhysicalProcess, EncodingContext additionalValues)
            throws EncodingException {
        PhysicalSystemPropertyType pspt = PhysicalSystemPropertyType.Factory.newInstance(getXmlOptions());
        addPhysicalSystemValues(pspt.addNewPhysicalSystem(), abstractPhysicalProcess);
        if (additionalValues.has(XmlBeansEncodingFlags.DOCUMENT)) {
            PhysicalSystemDocument psd = PhysicalSystemDocument.Factory.newInstance(getXmlOptions());
            psd.setPhysicalSystem(pspt.getPhysicalSystem());
        } else if (additionalValues.has(XmlBeansEncodingFlags.TYPE)) {
            return pspt.getPhysicalSystem();
        }
        return pspt;
    }

    private void addPhysicalSystemValues(PhysicalSystemType pst, PhysicalSystem abstractPhysicalProcess)
            throws EncodingException {
        addAbstractProcessValues(pst, abstractPhysicalProcess);
        addDescribedObjectValues(pst, abstractPhysicalProcess);
        addAbstractPhysicalProcessValues(pst, abstractPhysicalProcess);
        // set components
        if (abstractPhysicalProcess.isSetComponents()) {
            List<SmlComponent> smlComponents = checkForComponents(abstractPhysicalProcess);
            if (!smlComponents.isEmpty()) {
                ComponentListPropertyType clpt = createComponents(smlComponents);
                if (clpt != null && clpt.getComponentList() != null
                        && clpt.getComponentList().sizeOfComponentArray() > 0) {
                    pst.setComponents(clpt);
                }
            }
        }
        // set connections
        if (abstractPhysicalProcess.isSetConnections() && !pst.isSetConnections()) {
            pst.setConnections(createConnections(abstractPhysicalProcess.getConnections()));
        }
    }

    private void addDescribedObjectValues(DescribedObjectType dot, DescribedObject describedObject)
            throws EncodingException {
        if (!describedObject.isSetGmlID()) {
            describedObject.setGmlId("do_" + IdGenerator.generate(describedObject.toString()));
        }
        if (dot.getId() == null || dot.getId().isEmpty()) {
            dot.setId(describedObject.getGmlId());
        }

        // update/set gml:identifier
        if (describedObject.isSetIdentifier()) {
            describedObject.getIdentifierCodeWithAuthority().setCodeSpace(OGCConstants.UNIQUE_ID);
            XmlObject encodeObjectToXml = encodeObjectToXmlGml32(describedObject.getIdentifierCodeWithAuthority());
            if (encodeObjectToXml != null) {
                if (dot.isSetIdentifier()) {
                    dot.getIdentifier().set(encodeObjectToXml);
                } else {
                    dot.addNewIdentifier().set(encodeObjectToXml);
                }
            }
        }

        // set capabilities
        if (describedObject.isSetCapabilities()) {
            final Capabilities[] existing = dot.getCapabilitiesArray();
            final Set<String> names = Sets.newHashSetWithExpectedSize(existing.length);
            for (final Capabilities element : existing) {
                if (element.getName() != null) {
                    names.add(element.getName());
                }
            }
            for (final SmlCapabilities sosCapability : describedObject.getCapabilities()) {
                // check for observedBBOX, currently not supported, how to model
                // in SML 2.0?
                // Update Discovery Profile
                if (!SensorMLConstants.ELEMENT_NAME_OBSERVED_BBOX.equals(sosCapability.getName())) {
                    final Capabilities c = createCapability(sosCapability);
                    // replace existing capability with the same name
                    if (c != null) {
                        if (names.contains(c.getName())) {
                            removeCapability(dot, c);
                        }
                        dot.addNewCapabilities().set(c);
                    }
                }
            }
        }
        // set description
        if (describedObject.isSetDescription() && !dot.isSetDescription()) {
            dot.addNewDescription().setStringValue(describedObject.getDescription());
        }
        // set names
        if (describedObject.isSetName() && CollectionHelper.isNullOrEmpty(dot.getNameArray())) {
            // TODO check if override existing names
            addNamesToAbstractProcess(dot, describedObject.getNames());
        }
        // set location
        // set extension
        // set keywords
        if (describedObject.isSetKeywords()) {
            if (CollectionHelper.isNullOrEmpty(dot.getKeywordsArray())) {
                final List<String> keywords = describedObject.getKeywords();
                // final int length = dot.getKeywordsArray().length;
                // for (int i = 0; i < length; ++i) {
                // dot.removeKeywords(i);
                // }
                dot.addNewKeywords().addNewKeywordList()
                        .setKeywordArray(keywords.toArray(new String[keywords.size()]));
                // TODO else
            }
        }
        // set identification
        if (describedObject.isSetIdentifications()) {
            // TODO check for merging identifications if exists
            dot.setIdentificationArray(createIdentification(describedObject.getIdentifications()));
        }
        // set classification
        if (describedObject.isSetClassifications()) {
            dot.setClassificationArray(createClassification(describedObject.getClassifications()));
        }
        // set validTime
        if (describedObject.isSetValidTime() && CollectionHelper.isNullOrEmpty(dot.getValidTimeArray())) {
            for (Time time : describedObject.getValidTime()) {
                final XmlObject xbtime = encodeObjectToXmlGml32(time);
                if (time instanceof TimeInstant) {
                    dot.addNewValidTime().addNewTimeInstant().set(xbtime);
                } else if (time instanceof TimePeriod) {
                    dot.addNewValidTime().addNewTimePeriod().set(xbtime);
                }
            }

            // } else {
            // TODO remove or
            // remove existing validTime element
            // final XmlCursor newCursor = dot.getValidTime().newCursor();
            // newCursor.removeXml();
            // newCursor.dispose();
        }
        // set securityConstraints
        // set legalConstraints
        // set characteristics
        if (describedObject.isSetCharacteristics()) {
            if (CollectionHelper.isNullOrEmpty(dot.getCharacteristicsArray())) {
                dot.setCharacteristicsArray(createCharacteristics(describedObject.getCharacteristics()));
            }
        }
        // set contacts if contacts aren't already present in the abstract
        // process
        // if (describedObject.isSetContact() &&
        // CollectionHelper.isNotNullOrEmpty(dot.getContactsArray())) {
        if (describedObject.isSetContact()) {
            if (CollectionHelper.isNullOrEmpty(dot.getContactsArray())) {
                ContactListType cl = ContactListType.Factory.newInstance();
                for (SmlContact contact : describedObject.getContact()) {
                    if (contact instanceof SmlResponsibleParty) {
                        if (contact.isSetHref()) {
                            XmlObject xml = encodeObjectToXml(GmdConstants.NS_GMD, (SmlResponsibleParty) contact,
                                    EncodingContext.of(XmlBeansEncodingFlags.PROPERTY_TYPE));
                            cl.addNewContact().set(xml);
                        } else {
                            XmlObject encodeObjectToXml = encodeObjectToXml(GmdConstants.NS_GMD, contact);
                            if (encodeObjectToXml != null) {
                                cl.addNewContact().addNewCIResponsibleParty().set(encodeObjectToXml);
                            }
                        }
                    }
                }
                if (CollectionHelper.isNotNullOrEmpty(cl.getContactArray())) {
                    dot.addNewContacts().setContactList(cl);
                }
            }
        }
        // set documentation
        if (describedObject.isSetDocumentation()) {
            if (CollectionHelper.isNullOrEmpty(dot.getDocumentationArray())) {
                dot.setDocumentationArray(createDocumentationArray(describedObject.getDocumentation()));
            }
        }
        // set history

    }

    private void addAbstractProcessValues(final AbstractProcessType apt, final AbstractProcessV20 abstractProcess)
            throws EncodingException {
        // TODO
        // set typeOf
        if (abstractProcess.isSetTypeOf()) {
            if (apt.isSetTypeOf()) {
                substitute(apt.getTypeOf(), encodeObjectToXmlGml32(abstractProcess.getTypeOf()));
            } else {
                substitute(apt.addNewTypeOf(), encodeObjectToXmlGml32(abstractProcess.getTypeOf()));
            }
        }
        // set configuration
        // set featureOfInterest
        if (abstractProcess.isSetSmlFeatureOfInterest() && abstractProcess.getSmlFeatureOfInterest().isSetFeatures()) {
            if (!apt.isSetFeaturesOfInterest()) {
                apt.setFeaturesOfInterest(createFeatureOfInterest(abstractProcess.getSmlFeatureOfInterest()));
            }
            addFeatures(apt.getFeaturesOfInterest().getFeatureList(), abstractProcess.getSmlFeatureOfInterest());
        }
        // set inputs
        if (abstractProcess.isSetInputs() && !apt.isSetInputs()) {
            apt.setInputs(createInputs(abstractProcess.getInputs()));
        }
        // set outputs
        if (abstractProcess.isSetOutputs() && !apt.isSetOutputs()) {
            apt.setOutputs(createOutputs(abstractProcess.getOutputs()));
        } else if (abstractProcess.isSetOutputs() && apt.isSetOutputs()) {
            Outputs createOutputs = createOutputs(abstractProcess.getOutputs());
            if (!createOutputs.xmlText().equals(apt.getOutputs().xmlText(getXmlOptions()))) {
                apt.setOutputs(createOutputs);
            }
        }
        // set parameters
        if (abstractProcess.isSetParameters() && !apt.isSetParameters()) {
            apt.setParameters(createParameters(abstractProcess.getParameters()));
        } else if (abstractProcess.isSetParameters() && apt.isSetParameters()) {
            Parameters createParameters = createParameters(abstractProcess.getParameters());
            if (!createParameters.xmlText().equals(apt.getParameters().xmlText(getXmlOptions()))) {
                apt.setParameters(createParameters);
            }
        }
        // set modes
    }

    private void addAbstractPhysicalProcessValues(AbstractPhysicalProcessType appt,
            AbstractPhysicalProcess absPhysicalProcess)
            throws EncodingException {
        // set attachedTo
        if (absPhysicalProcess.isSetAttachedTo()) {
            if (appt.isSetAttachedTo()) {
                substitute(appt.getAttachedTo(), encodeObjectToXmlGml32(absPhysicalProcess.getAttachedTo()));
            } else {
                substitute(appt.addNewAttachedTo(), encodeObjectToXmlGml32(absPhysicalProcess.getAttachedTo()));
            }
        }

        // set localReferenceFrame
        // set localTimeFrame
        // set position
        if (CollectionHelper.isNullOrEmpty(appt.getPositionArray()) && absPhysicalProcess.isSetPosition()) {
            createPosition(appt.addNewPosition(), absPhysicalProcess.getPosition());
        }
        // set timePosition
        // // set location
        // if (absPhysicalProcess.isSetLocation()) {
        // appt.setSmlLocation(createLocation(absPhysicalProcess.getLocation()));
        // }

    }

    private List<SmlComponent> checkForComponents(AbstractProcess abstractProcess)
            throws EncodingException {
        List<SmlComponent> smlComponents = Lists.newArrayList();
        if (abstractProcess instanceof HasComponents<?> && ((HasComponents<?>) abstractProcess).isSetComponents()) {
            smlComponents.addAll(((HasComponents<?>) abstractProcess).getComponents());
        }
        return smlComponents;
    }

    private void addNamesToAbstractProcess(DescribedObjectType dot, List<CodeType> names)
            throws EncodingException {
        for (CodeType codeType : names) {
            dot.addNewName().set(encodeObjectToXmlGml32(codeType));
        }
    }

    // private ContactList createContactList(final List<SmlContact> contacts) {
    // final ContactList xbContacts = ContactList.Factory.newInstance();
    // for (final SmlContact smlContact : contacts) {
    // if (smlContact instanceof SmlPerson) {
    // ContactList.Member member = xbContacts.addNewMember();
    // member.addNewPerson().set(createPerson((SmlPerson) smlContact));
    // if (!Strings.isNullOrEmpty(smlContact.getRole())) {
    // member.setRole(smlContact.getRole());
    // }
    // } else if (smlContact instanceof SmlResponsibleParty) {
    // ContactList.Member member = xbContacts.addNewMember();
    // member.addNewResponsibleParty().set(createResponsibleParty((SmlResponsibleParty)
    // smlContact));
    // if (!Strings.isNullOrEmpty(smlContact.getRole())) {
    // member.setRole(smlContact.getRole());
    // }
    // } else if (smlContact instanceof SmlContactList) {
    // SmlContactList contactList = (SmlContactList) smlContact;
    // ContactList innerContactList =
    // createContactList(contactList.getMembers());
    // int innerContactLength = innerContactList.getMemberArray().length;
    // for (int i = 0; i < innerContactLength; i++) {
    // xbContacts.addNewMember().set(innerContactList.getMemberArray(i));
    // }
    // }
    // }
    // return xbContacts;
    // }
    //
    // private XmlObject createResponsibleParty(final SmlResponsibleParty
    // smlRespParty) {
    // final ResponsibleParty xbRespParty =
    // ResponsibleParty.Factory.newInstance();
    // if (smlRespParty.isSetIndividualName()) {
    // xbRespParty.setIndividualName(smlRespParty.getIndividualName());
    // }
    // if (smlRespParty.isSetOrganizationName()) {
    // xbRespParty.setOrganizationName(smlRespParty.getOrganizationName());
    // }
    // if (smlRespParty.isSetPositionName()) {
    // xbRespParty.setPositionName(smlRespParty.getPositionName());
    // }
    // if (smlRespParty.isSetContactInfo()) {
    // xbRespParty.setContactInfo(createContactInfo(smlRespParty));
    // }
    // return xbRespParty;
    // }
    // private ContactInfo createContactInfo(final SmlResponsibleParty
    // smlRespParty) {
    // final ContactInfo xbContactInfo = ContactInfo.Factory.newInstance();
    // if (smlRespParty.isSetHoursOfService()) {
    // xbContactInfo.setHoursOfService(smlRespParty.getHoursOfService());
    // }
    // if (smlRespParty.isSetContactInstructions()) {
    // xbContactInfo.setContactInstructions(smlRespParty.getContactInstructions());
    // }
    // if (smlRespParty.isSetOnlineResources()) {
    // for (final String onlineResouce : smlRespParty.getOnlineResources()) {
    // xbContactInfo.addNewOnlineResource().setHref(onlineResouce);
    // }
    // }
    // if (smlRespParty.isSetPhone()) {
    // final Phone xbPhone = xbContactInfo.addNewPhone();
    // if (smlRespParty.isSetPhoneFax()) {
    // for (final String fax : smlRespParty.getPhoneFax()) {
    // xbPhone.addFacsimile(fax);
    // }
    // }
    // if (smlRespParty.isSetPhoneVoice()) {
    // for (final String voice : smlRespParty.getPhoneVoice()) {
    // xbPhone.addVoice(voice);
    // }
    // }
    // }
    // if (smlRespParty.isSetAddress()) {
    // final Address xbAddress = xbContactInfo.addNewAddress();
    // if (smlRespParty.isSetDeliveryPoint()) {
    // for (final String deliveryPoint : smlRespParty.getDeliveryPoint()) {
    // xbAddress.addDeliveryPoint(deliveryPoint);
    // }
    // }
    // if (smlRespParty.isSetCity()) {
    // xbAddress.setCity(smlRespParty.getCity());
    // }
    // if (smlRespParty.isSetAdministrativeArea()) {
    // xbAddress.setAdministrativeArea(smlRespParty.getAdministrativeArea());
    // }
    // if (smlRespParty.isSetPostalCode()) {
    // xbAddress.setPostalCode(smlRespParty.getPostalCode());
    // }
    // if (smlRespParty.isSetCountry()) {
    // xbAddress.setCountry(smlRespParty.getCountry());
    // }
    // if (smlRespParty.isSetEmail()) {
    // xbAddress.setElectronicMailAddress(smlRespParty.getEmail());
    // }
    // }
    // return xbContactInfo;
    // }
    //
    // private Person createPerson(final SmlPerson smlPerson) {
    // final Person xbPerson = Person.Factory.newInstance();
    // if (smlPerson.isSetAffiliation()) {
    // xbPerson.setAffiliation(smlPerson.getAffiliation());
    // }
    // if (smlPerson.isSetEmail()) {
    // xbPerson.setEmail(smlPerson.getEmail());
    // }
    // if (smlPerson.isSetName()) {
    // xbPerson.setName(smlPerson.getName());
    // }
    // if (smlPerson.isSetPhoneNumber()) {
    // xbPerson.setPhoneNumber(smlPerson.getPhoneNumber());
    // }
    // if (smlPerson.isSetSurname()) {
    // xbPerson.setSurname(smlPerson.getSurname());
    // }
    // if (smlPerson.isSetUserID()) {
    // xbPerson.setUserID(smlPerson.getUserID());
    // }
    // return xbPerson;
    // }
    //
    // private ContactList createContactList(final List<SmlContact> contacts) {
    // final ContactList xbContacts = ContactList.Factory.newInstance();
    // for (final SmlContact smlContact : contacts) {
    // if (smlContact instanceof SmlPerson) {
    // ContactList.Member member = xbContacts.addNewMember();
    // member.addNewPerson().set(createPerson((SmlPerson) smlContact));
    // if (!Strings.isNullOrEmpty(smlContact.getRole())) {
    // member.setRole(smlContact.getRole());
    // }
    // } else if (smlContact instanceof SmlResponsibleParty) {
    // ContactList.Member member = xbContacts.addNewMember();
    // member.addNewResponsibleParty().set(createResponsibleParty((SmlResponsibleParty)
    // smlContact));
    // if (!Strings.isNullOrEmpty(smlContact.getRole())) {
    // member.setRole(smlContact.getRole());
    // }
    // } else if (smlContact instanceof SmlContactList) {
    // SmlContactList contactList = (SmlContactList) smlContact;
    // ContactList innerContactList =
    // createContactList(contactList.getMembers());
    // int innerContactLength = innerContactList.getMemberArray().length;
    // for (int i = 0; i < innerContactLength; i++) {
    // xbContacts.addNewMember().set(innerContactList.getMemberArray(i));
    // }
    // }
    // }
    // return xbContacts;
    // }
    //
    // private XmlObject createResponsibleParty(final SmlResponsibleParty
    // smlRespParty) {
    // final ResponsibleParty xbRespParty =
    // ResponsibleParty.Factory.newInstance();
    // if (smlRespParty.isSetIndividualName()) {
    // xbRespParty.setIndividualName(smlRespParty.getIndividualName());
    // }
    // if (smlRespParty.isSetOrganizationName()) {
    // xbRespParty.setOrganizationName(smlRespParty.getOrganizationName());
    // }
    // if (smlRespParty.isSetPositionName()) {
    // xbRespParty.setPositionName(smlRespParty.getPositionName());
    // }
    // if (smlRespParty.isSetContactInfo()) {
    // xbRespParty.setContactInfo(createContactInfo(smlRespParty));
    // }
    // return xbRespParty;
    // }
    // private boolean isContactListSetAndContainingElements(final Contact
    // contact) {
    // return contact.getContactList() != null &&
    // contact.getContactList().getMemberArray() != null
    // && contact.getContactList().getMemberArray().length > 0;
    // }
    private void removeCapability(final DescribedObjectType dot, final Capabilities c) {
        // get current index of element with this name
        for (int i = 0; i < dot.getCapabilitiesArray().length; i++) {
            if (dot.getCapabilitiesArray(i).getName().equals(c.getName())) {
                dot.removeCapabilities(i);
                return;
            }
        }
    }

    private Capabilities createCapability(final SmlCapabilities capabilities)
            throws EncodingException {
        Capabilities xbCapabilities = null;
        if (capabilities.isSetAbstractDataComponents()) {
            xbCapabilities = Capabilities.Factory.newInstance(getXmlOptions());
            if (capabilities.isSetName()) {
                xbCapabilities.setName(capabilities.getName());
            }

            CapabilityListType capabilityList = xbCapabilities.addNewCapabilityList();
            if (capabilities.isSetCapabilities()) {
                for (SmlCapability capability : capabilities.getCapabilities()) {
                    XmlObject encodeObjectToXml = encodeObjectToXmlSwe20(capability.getAbstractDataComponent());
                    Capability c = capabilityList.addNewCapability();
                    if (capability.isSetName()) {
                        c.setName(NcName.makeValid(capability.getName()));
                    } else if (capability.getAbstractDataComponent().isSetName()) {
                        capability
                                .setName(NcName.makeValid(capability.getAbstractDataComponent().getName().getValue()));
                    } else {
                        capability.setName(NcName.makeValid(capability.getAbstractDataComponent().getDefinition()));
                    }
                    XmlObject substituteElement =
                            XmlHelper.substituteElement(c.addNewAbstractDataComponent(), encodeObjectToXml);
                    substituteElement.set(encodeObjectToXml);

                }
            } else if (capabilities.isSetAbstractDataComponents()) {
                for (SweAbstractDataComponent component : capabilities.getAbstractDataComponents()) {
                    XmlObject encodeObjectToXml = encodeObjectToXmlSwe20(component);
                    Capability capability = capabilityList.addNewCapability();
                    if (component.isSetName()) {
                        capability.setName(NcName.makeValid(component.getName().getValue()));
                    } else {
                        capability.setName(NcName.makeValid(component.getDefinition()));
                    }
                    XmlObject substituteElement =
                            XmlHelper.substituteElement(capability.addNewAbstractDataComponent(), encodeObjectToXml);
                    substituteElement.set(encodeObjectToXml);
                }
            }
        }
        return xbCapabilities;
    }

    private ProcessMethodType createProcessMethod(HasProcessMethod processMethod) {
        return null;
    }

    /**
     * Creates the valueentification section of the SensorML description.
     *
     * @param identifications
     *            SOS valueentifications
     *
     * @return XML Identification array
     */
    protected IdentifierListPropertyType[] createIdentification(final List<SmlIdentifier> identifications) {
        final IdentifierListPropertyType xbIdentification =
                IdentifierListPropertyType.Factory.newInstance(getXmlOptions());
        final IdentifierListType xbIdentifierList = xbIdentification.addNewIdentifierList();
        identifications.forEach(
            sosSMLIdentifier -> createTerm(xbIdentifierList.addNewIdentifier2().addNewTerm(), sosSMLIdentifier));
        return new IdentifierListPropertyType[] { xbIdentification };
    }

    /**
     * Creates the classification section of the SensorML description.
     *
     * @param classifications
     *            SOS classifications
     *
     * @return XML Classification array
     */
    private ClassifierListPropertyType[] createClassification(final List<SmlClassifier> classifications) {
        ClassifierListPropertyType xbClassification = ClassifierListPropertyType.Factory.newInstance(getXmlOptions());
        ClassifierListType xbClassifierList = xbClassification.addNewClassifierList();
        classifications
                .forEach(classifier -> createTerm(xbClassifierList.addNewClassifier().addNewTerm(), classifier));
        return new ClassifierListPropertyType[] { xbClassification };
    }

    private void createTerm(TermType t, Term term) {
        t.setLabel(term.getLabel());
        if (term.isSetDefinition()) {
            t.setDefinition(term.getDefinition());
        }
        if (term.isSetCodeSpace()) {
            t.addNewCodeSpace().setHref(term.getCodeSpace());
        }
        t.setValue(term.getValue());
    }

    /**
     * Creates the characteristics section of the SensorML description.
     *
     * @param smlCharacteristics
     *            SOS characteristics list
     *
     * @return XML Characteristics array
     *
     * @throws EncodingException
     *             If an error occurs
     */
    private Characteristics[] createCharacteristics(final List<SmlCharacteristics> smlCharacteristics)
            throws EncodingException {
        final List<Characteristics> characteristicsList = new ArrayList<>(smlCharacteristics.size());
        for (final SmlCharacteristics sosSMLCharacteristics : smlCharacteristics) {
            Characteristics xbCharacteristics = Characteristics.Factory.newInstance(getXmlOptions());
            if (sosSMLCharacteristics.isSetName()) {
                xbCharacteristics.setName(sosSMLCharacteristics.getName());
            } else {
                xbCharacteristics.setName("characteristics_" + smlCharacteristics.lastIndexOf(sosSMLCharacteristics));
            }
            CharacteristicListType characteristicList = xbCharacteristics.addNewCharacteristicList();
            if (sosSMLCharacteristics.isSetCharacteristics()) {
                for (SmlCharacteristic characteristic : sosSMLCharacteristics.getCharacteristic()) {
                    Characteristic c = characteristicList.addNewCharacteristic();
                    c.setName(NcName.makeValid(characteristic.getName()));
                    if (characteristic.isSetAbstractDataComponent()) {
                        XmlObject encodeObjectToXml =
                                encodeObjectToXml(SweConstants.NS_SWE_20, characteristic.getAbstractDataComponent());
                        XmlObject substituteElement =
                                XmlHelper.substituteElement(c.addNewAbstractDataComponent(), encodeObjectToXml);
                        substituteElement.set(encodeObjectToXml);
                    } else if (characteristic.isSetHref()) {
                        c.setHref(characteristic.getHref());
                        if (characteristic.isSetTitle()) {
                            c.setTitle(characteristic.getTitle());
                        }
                    }
                }
            }
            if (sosSMLCharacteristics.isSetAbstractDataComponents()) {
                if (sosSMLCharacteristics.isSetAbstractDataComponents()) {
                    for (SweAbstractDataComponent component : sosSMLCharacteristics.getAbstractDataComponents()) {
                        XmlObject encodeObjectToXml = encodeObjectToXml(SweConstants.NS_SWE_20, component);
                        Characteristic c = characteristicList.addNewCharacteristic();
                        c.setName(NcName.makeValid(component.getName().getValue()));
                        XmlObject substituteElement =
                                XmlHelper.substituteElement(c.addNewAbstractDataComponent(), encodeObjectToXml);
                        substituteElement.set(encodeObjectToXml);
                    }
                }

            }
            characteristicsList.add(xbCharacteristics);
        }
        return characteristicsList.toArray(new Characteristics[characteristicsList.size()]);
    }

    /**
     * Create XML Documentation array from SOS documentations
     *
     * @param sosDocumentation
     *            SOS documentation list
     *
     * @return XML Documentation array
     */
    protected DocumentListPropertyType[] createDocumentationArray(List<AbstractSmlDocumentation> sosDocumentation) {
        return sosDocumentation.stream().map(doc -> {
            DocumentListPropertyType documentation = DocumentListPropertyType.Factory.newInstance();
            if (doc instanceof SmlDocumentation) {
                documentation.setDocumentList(createDocument((SmlDocumentation) doc));
            } else if (doc instanceof SmlDocumentationList) {
                documentation.setDocumentList(createDocumentationList((SmlDocumentationList) doc));
            }
            return documentation;
        }).toArray(DocumentListPropertyType[]::new);
    }

    /**
     * Create a XML Documentation element from SOS documentation
     *
     * @param sosDocumentation
     *            SOS documentation
     *
     * @return XML Documentation element
     */
    private DocumentListType createDocument(final SmlDocumentation sosDocumentation) {
        final DocumentListType documentList = DocumentListType.Factory.newInstance();
        if (sosDocumentation.isSetDescription()) {
            documentList.setDescription(sosDocumentation.getDescription());
        }
        // TODO encode
        // documentList.addNewDocument().setCIOnlineResource(ciOnlineResource);
        return documentList;
    }

    /**
     * Create a XML DocuemntList from SOS documentList
     *
     * @param sosDocumentationList
     *            SOS documentList
     *
     * @return XML DocumentList element
     */
    private DocumentListType createDocumentationList(final SmlDocumentationList sosDocumentationList) {
        final DocumentListType documentList = DocumentListType.Factory.newInstance();
        if (sosDocumentationList.isSetDescription()) {
            documentList.setDescription(sosDocumentationList.getDescription());
        }
        // TODO sosDocumentationList.getMember()
        return documentList;
    }

    /**
     * Creates the position section of the SensorML description.
     *
     * @param pupt
     *            the xml type
     * @param position
     *            SOS position
     *
     * @throws EncodingException
     *             if an error occurs
     */
    private void createPosition(PositionUnionPropertyType pupt, final SmlPosition position)
            throws EncodingException {
        if (position.isSetVector()) {
            pupt.addNewVector().set(encodeObjectToXmlSwe20(position.getVector()));
        } else if (position.isSetAbstractDataComponent()) {
            SweAbstractDataComponent abstractDataComponent = position.getAbstractDataComponent();
            if (abstractDataComponent instanceof SweDataRecord) {
                pupt.addNewDataRecord().set(encodeObjectToXmlSwe20(abstractDataComponent));
            } else if (abstractDataComponent instanceof SweDataArray) {
                pupt.addNewDataArray1().set(encodeObjectToXmlSwe20(abstractDataComponent));
            }
        } else if (position.isSetPosition()) {
            SweVector vector = new SweVector(position.getPosition());
            vector.setReferenceFrame(position.isSetReferenceFrame() ? position.getReferenceFrame()
                    : OGCConstants.URL_DEF_CRS_EPSG + 4326);
            pupt.addNewVector().set(encodeObjectToXmlSwe20(vector));
        }
    }

    // /**
    // * Creates the location section of the SensorML description.
    // *
    // * @param dot
    // * the described object
    // * @param location
    // * SOS location representation.
    // *
    // * @throws EncodingException
    // * if an error occurs
    // */
    // private void createLocation(DescribedObjectType dot, SmlLocation
    // location) throws EncodingException {
    // dot.addNewLocation().addNewAbstractGeometry().set(encodeObjectToXmlGml32(location.getPoint()));
    // }

    /**
     * Creates the inputs section of the SensorML description.
     *
     * @param inputs
     *            SOS SWE representation.
     *
     * @return XML Inputs element
     *
     * @throws EncodingException
     *             if an error occurs
     */
    private Inputs createInputs(final List<SmlIo> inputs)
            throws EncodingException {
        final Inputs xbInputs = Inputs.Factory.newInstance(getXmlOptions());
        final InputListType xbInputList = xbInputs.addNewInputList();
        int counter = 1;
        for (final SmlIo sosSMLIo : inputs) {
            if (!sosSMLIo.isSetName()) {
                sosSMLIo.setIoName("input_" + counter++);
            } else {
                sosSMLIo.setIoName(NcName.makeValid(sosSMLIo.getIoName()));
            }
            addInput(xbInputList.addNewInput(), sosSMLIo);
        }
        return xbInputs;
    }

    /**
     * Creates the outputs section of the SensorML description.
     *
     * @param sosOutputs
     *            SOS SWE representation.
     *
     * @return XML Outputs element
     *
     * @throws org.n52.svalbard.encode.exception.EncodingException
     *             if the encoding fails
     */
    private Outputs createOutputs(final List<SmlIo> sosOutputs)
            throws EncodingException {
        final Outputs outputs = Outputs.Factory.newInstance(getXmlOptions());
        final OutputListType outputList = outputs.addNewOutputList();
        final Set<String> definitions = Sets.newHashSet();
        int counter = 1;
        final Set<String> outputNames = Sets.newHashSet();
        for (final SmlIo sosSMLIo : sosOutputs) {
            if (sosSMLIo.isSetValue() && !definitions.contains(sosSMLIo.getIoValue().getDefinition())) {
                if (!sosSMLIo.isSetName() || outputNames.contains(sosSMLIo.getIoName())) {
                    sosSMLIo.setIoName(getValidOutputName(counter++, outputNames));
                } else {
                    sosSMLIo.setIoName(NcName.makeValid(sosSMLIo.getIoName()));
                }
                outputNames.add(sosSMLIo.getIoName());
                addOutput(outputList.addNewOutput(), sosSMLIo);
                definitions.add(sosSMLIo.getIoValue().getDefinition());
            }
        }
        return outputs;
    }

    private Parameters createParameters(List<SmlParameter> smlParameters) throws EncodingException {
        final Parameters parameters = Parameters.Factory.newInstance(getXmlOptions());
        final ParameterListType parameterList = parameters.addNewParameterList();
        for (SmlParameter smlParameter : smlParameters) {
            Parameter param = parameterList.addNewParameter();
            param.setName(smlParameter.getName());
            if (smlParameter.isSetHref()) {
                param.setHref(smlParameter.getHref());
                if (smlParameter.isSetTitle()) {
                    param.setTitle(smlParameter.getTitle());
                }
            } else {
                XmlObject xmlObject = encodeObjectToXmlSwe20(smlParameter.getParameter());
                if (xmlObject != null) {
                    SweAbstractDataComponent parameter = smlParameter.getParameter();
                    if (parameter instanceof SmlDataInterface) {
                        substitute(param.addNewDataInterface(), xmlObject);
                    } else if (parameter instanceof SweObservableProperty) {
                        substitute(param.addNewObservableProperty(), xmlObject);
                    } else {
                        substitute(param.addNewAbstractDataComponent(), xmlObject);
                    }
                }
            }
        }
        return parameters;
    }

    private FeaturesOfInterest createFeatureOfInterest(SmlFeatureOfInterest feature) {
        if (feature.isSetFeaturesOfInterest()) {
            FeaturesOfInterest foi = FeaturesOfInterest.Factory.newInstance(getXmlOptions());
            FeatureListType featureList = foi.addNewFeatureList();
            if (feature.isSetDefinition()) {
                featureList.setDefinition(feature.getDefinition());
            }
            if (feature.isSetDescription()) {
                featureList.setDescription(feature.getDescription());
            }
            if (feature.isSetIdentifier()) {
                featureList.setIdentifier(feature.getIdentifier());
            }
            if (feature.isSetLabel()) {
                featureList.setLabel(feature.getLabel());
            }

            return foi;
        }
        return null;
    }

    private void addFeatures(FeatureListType featureList, SmlFeatureOfInterest feature)
            throws EncodingException {
        if (feature.isSetFeaturesOfInterestMap()) {
            for (int i = 0; i < featureList.sizeOfFeatureArray(); i++) {
                featureList.removeFeature(i);
            }
            for (AbstractFeature abstractFeature : feature.getFeaturesOfInterestMap().values()) {
                featureList.addNewFeature().set(encodeObjectToXmlGml32(abstractFeature));
            }
        }
    }

    /**
     * Creates the components section of the SensorML description.
     *
     * @param sosComponents
     *            SOS SWE representation.
     *
     * @return encoded sml:components
     *
     * @throws EncodingException
     *             if the process encoding fails
     */
    private ComponentListPropertyType createComponents(final List<SmlComponent> sosComponents)
            throws EncodingException {
        ComponentListPropertyType clpt = ComponentListPropertyType.Factory.newInstance(getXmlOptions());
        final ComponentListType clt = clpt.addNewComponentList();
        for (final SmlComponent sosSMLComponent : sosComponents) {
            final Component component = clt.addNewComponent();
            if (sosSMLComponent.isSetName()) {
                component.setName(sosSMLComponent.getName());
            }

            if (sosSMLComponent.isSetHref()) {
                component.setHref(sosSMLComponent.getHref());
                if (sosSMLComponent.isSetTitle()) {
                    component.setTitle(sosSMLComponent.getTitle());
                }
            } else if (sosSMLComponent.isSetProcess()) {
                XmlObject xmlObject =
                        encode(sosSMLComponent.getProcess(), EncodingContext.of(XmlBeansEncodingFlags.TYPE));
                // if
                // (sosSMLComponent.getProcess().getXml()
                // != null
                // &&
                // !sosSMLComponent.getProcess().getXml().isEmpty())
                // {
                // try {
                // xmlObject =
                // XmlObject.Factory.parse(sosSMLComponent.getProcess().getXml());
                //
                // } catch (final XmlException xmle) {
                // throw new
                // NoApplicableCodeException().causedBy(xmle).withMessage(
                // "Error while encoding SensorML child procedure description "
                // +
                // "from stored SensorML encoded sensor description with
                // XMLBeans");
                // }
                // } else {
                // xmlObject = encode(sosSMLComponent.getProcess());
                // }
                if (xmlObject != null) {
                    // AbstractProcessType xbProcess = null;
                    // if (xmlObject instanceof AbstractProcessType) {
                    // xbProcess = (AbstractProcessType) xmlObject;
                    // } else {
                    // throw new NoApplicableCodeException()
                    // .withMessage("The sensor type is not supported by this
                    // SOS");
                    // }
                    // TODO add feature/parentProcs/childProcs to component - is
                    // this already done?
                    // XmlObject substituteElement =
                    // XmlHelper.substituteElement(component.addNewAbstractProcess(),
                    // xmlObject);
                    // substituteElement.set(xmlObject);
                    substitute(component.addNewAbstractProcess(), xmlObject);
                }
            }
        }
        return clpt;
    }

    private ConnectionListPropertyType createConnections(SmlConnection connections) {
        ConnectionListPropertyType clpt = ConnectionListPropertyType.Factory.newInstance(getXmlOptions());
        if (!Strings.isNullOrEmpty(connections.getHref())) {
            clpt.setHref(connections.getHref());
            if (!Strings.isNullOrEmpty(connections.getTitle())) {
                clpt.setTitle(connections.getTitle());
            }
            if (!Strings.isNullOrEmpty(connections.getRole())) {
                clpt.setRole(connections.getRole());
            }
        } else {
            ConnectionListType clt = clpt.addNewConnectionList();
            for (SmlLink link : connections.getConnections()) {
                LinkType lt = clt.addNewConnection().addNewLink();
                lt.addNewDestination().setRef(link.getDestination());
                lt.addNewSource().setRef(link.getSource());
                if (!Strings.isNullOrEmpty(link.getId())) {
                    lt.setId(link.getId());
                }
            }
        }
        return clpt;
    }

    /**
     * Adds a SOS SWE simple type to a XML SML Input.
     *
     * @param input
     *            SML Input
     * @param sosSMLIO
     *            SOS SWE simple type.
     *
     * @throws EncodingException
     *             if the encoding fails
     */
    private void addInput(final Input input, final SmlIo sosSMLIO)
            throws EncodingException {
        input.setName(sosSMLIO.getIoName());
        addDataComponentOrObservablePropertyType(input, sosSMLIO);
        // if (sosSMLIO.isSetHref()) {
        // input.setHref(sosSMLIO.getHref());
        //
        // } else if (sosSMLIO.getIoValue() instanceof SweObservableProperty) {
        // addValueToObservableProperty(input.addNewObservableProperty(),
        // sosSMLIO.getIoValue());
        // } else {
        // final XmlObject encodeObjectToXml =
        // encodeObjectToXmlSwe20(sosSMLIO.getIoValue());
        // XmlObject substituteElement =
        // XmlHelper.substituteElement(input.addNewAbstractDataComponent(),
        // encodeObjectToXml);
        // substituteElement.set(encodeObjectToXml);
        // }
    }

    private void addDataComponentOrObservablePropertyType(DataComponentOrObservablePropertyType type, SmlIo sosSMLIO)
            throws EncodingException {
        if (sosSMLIO.isSetHref()) {
            type.setHref(sosSMLIO.getHref());
            if (sosSMLIO.isSetTitle()) {
                type.setTitle(sosSMLIO.getTitle());
            }
        } else if (sosSMLIO.getIoValue() instanceof SweObservableProperty) {
            addValueToObservableProperty(type.addNewObservableProperty(), sosSMLIO.getIoValue());
            // TODO } else if (sosSMLIO.getIoValue() instanceof
            // SmlDataInterface) {
        } else {
            final XmlObject encodeObjectToXml = encodeObjectToXmlSwe20(sosSMLIO.getIoValue());
            XmlObject substituteElement =
                    XmlHelper.substituteElement(type.addNewAbstractDataComponent(), encodeObjectToXml);
            substituteElement.set(encodeObjectToXml);
        }
    }

    private void addValueToObservableProperty(ObservablePropertyType opt,
            SweAbstractDataComponent observableProperty) {
        if (observableProperty.isSetDefinition()) {
            opt.setDefinition(observableProperty.getDefinition());
        }
        if (observableProperty.isSetDescription()) {
            opt.setDescription(observableProperty.getDescription());
        }
        if (observableProperty.isSetIdentifier()) {
            opt.setIdentifier(observableProperty.getIdentifier());
        }
        if (observableProperty.isSetLabel()) {
            opt.setLabel(observableProperty.getLabel());
        }
    }

    /**
     * Adds a SOS SWE simple type to a XML SML Output.
     *
     * @param output
     *            SML Output
     * @param sosSMLIO
     *            SOS SWE simple type.
     *
     * @throws org.n52.svalbard.encode.exception.EncodingException
     *             if the encoding fails
     */
    private void addOutput(final Output output, final SmlIo sosSMLIO)
            throws EncodingException {
        output.setName(sosSMLIO.getIoName());
        addDataComponentOrObservablePropertyType(output, sosSMLIO);
        // substitute(output.addNewAbstractDataComponent(),
        // encodeObjectToXmlSwe20(sosSMLIO.getIoValue()));
        // final XmlObject encodeObjectToXml =
        // encodeObjectToXmlSwe20(sosSMLIO.getIoValue());
        // XmlObject substituteElement =
        // XmlHelper.substituteElement(output.addNewAbstractDataComponent(),
        // encodeObjectToXml);
        // substituteElement.set(encodeObjectToXml);
    }

    private XmlObject encodeObjectToXmlGml32(Object o)
            throws EncodingException {
        return encodeObjectToXml(GmlConstants.NS_GML_32, o);
    }

    private XmlObject encodeObjectToXmlSwe20(Object o)
            throws EncodingException {
        return encodeObjectToXml(SweConstants.NS_SWE_20, o);
    }
}
