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

import static java.util.stream.Collectors.joining;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;

import net.opengis.gml.StringOrRefType;
import net.opengis.swe.x101.AbstractDataComponentType;
import net.opengis.swe.x101.AbstractEncodingType;
import net.opengis.swe.x101.AllowedTimesDocument.AllowedTimes;
import net.opengis.swe.x101.AllowedTimesPropertyType;
import net.opengis.swe.x101.AllowedTokensDocument.AllowedTokens;
import net.opengis.swe.x101.AllowedTokensPropertyType;
import net.opengis.swe.x101.AllowedValuesDocument.AllowedValues;
import net.opengis.swe.x101.AllowedValuesPropertyType;
import net.opengis.swe.x101.AnyScalarPropertyType;
import net.opengis.swe.x101.BlockEncodingPropertyType;
import net.opengis.swe.x101.CategoryDocument.Category;
import net.opengis.swe.x101.CountDocument.Count;
import net.opengis.swe.x101.CountRangeDocument.CountRange;
import net.opengis.swe.x101.DataArrayDocument;
import net.opengis.swe.x101.DataArrayType;
import net.opengis.swe.x101.DataComponentPropertyType;
import net.opengis.swe.x101.DataRecordDocument;
import net.opengis.swe.x101.DataRecordType;
import net.opengis.swe.x101.EnvelopeType;
import net.opengis.swe.x101.ObservablePropertyDocument.ObservableProperty;
import net.opengis.swe.x101.QualityPropertyType;
import net.opengis.swe.x101.QuantityDocument.Quantity;
import net.opengis.swe.x101.QuantityRangeDocument.QuantityRange;
import net.opengis.swe.x101.SimpleDataRecordType;
import net.opengis.swe.x101.TextBlockDocument.TextBlock;
import net.opengis.swe.x101.TextDocument.Text;
import net.opengis.swe.x101.TimeDocument.Time;
import net.opengis.swe.x101.TimeGeometricPrimitivePropertyType;
import net.opengis.swe.x101.TimeRangeDocument.TimeRange;
import net.opengis.swe.x101.UomPropertyType;
import net.opengis.swe.x101.VectorPropertyType;
import net.opengis.swe.x101.VectorType;
import net.opengis.swe.x101.VectorType.Coordinate;

import org.apache.xmlbeans.GDateBuilder;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlDateTime;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlString;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3.x1999.xlink.ActuateType;
import org.w3.x1999.xlink.ShowType;
import org.w3.x1999.xlink.TypeType;

import org.n52.shetland.ogc.UoM;
import org.n52.shetland.ogc.gml.GmlConstants;
import org.n52.shetland.ogc.gml.time.TimePeriod;
import org.n52.shetland.ogc.swe.RangeValue;
import org.n52.shetland.ogc.swe.SweAbstractDataComponent;
import org.n52.shetland.ogc.swe.SweConstants;
import org.n52.shetland.ogc.swe.SweCoordinate;
import org.n52.shetland.ogc.swe.SweDataArray;
import org.n52.shetland.ogc.swe.SweDataRecord;
import org.n52.shetland.ogc.swe.SweEnvelope;
import org.n52.shetland.ogc.swe.SweField;
import org.n52.shetland.ogc.swe.SweSimpleDataRecord;
import org.n52.shetland.ogc.swe.SweVector;
import org.n52.shetland.ogc.swe.encoding.SweAbstractEncoding;
import org.n52.shetland.ogc.swe.encoding.SweTextEncoding;
import org.n52.shetland.ogc.swe.simpleType.SweAbstractSimpleType;
import org.n52.shetland.ogc.swe.simpleType.SweAllowedTimes;
import org.n52.shetland.ogc.swe.simpleType.SweAllowedTokens;
import org.n52.shetland.ogc.swe.simpleType.SweAllowedValues;
import org.n52.shetland.ogc.swe.simpleType.SweBoolean;
import org.n52.shetland.ogc.swe.simpleType.SweCategory;
import org.n52.shetland.ogc.swe.simpleType.SweCount;
import org.n52.shetland.ogc.swe.simpleType.SweCountRange;
import org.n52.shetland.ogc.swe.simpleType.SweObservableProperty;
import org.n52.shetland.ogc.swe.simpleType.SweQuality;
import org.n52.shetland.ogc.swe.simpleType.SweQuantity;
import org.n52.shetland.ogc.swe.simpleType.SweQuantityRange;
import org.n52.shetland.ogc.swe.simpleType.SweText;
import org.n52.shetland.ogc.swe.simpleType.SweTime;
import org.n52.shetland.ogc.swe.simpleType.SweTimeRange;
import org.n52.shetland.util.DateTimeHelper;
import org.n52.shetland.w3c.Nillable;
import org.n52.shetland.w3c.SchemaLocation;
import org.n52.shetland.w3c.xlink.Reference;
import org.n52.shetland.w3c.xlink.Referenceable;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.exception.NotYetSupportedEncodingException;
import org.n52.svalbard.encode.exception.UnsupportedEncoderInputException;
import org.n52.svalbard.util.CodingHelper;
import org.n52.svalbard.util.XmlHelper;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * Encoder class for SWE Common 1.0.1
 *
 * @since 1.0.0
 */
public class SweCommonEncoderv101
        extends AbstractXmlEncoder<XmlObject, Object> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SweCommonEncoderv101.class);

    private static final String URN = "urn:";

    private static final String HTTP = "http://";

    private static final Set<EncoderKey> ENCODER_KEYS = CodingHelper.encoderKeysForElements(SweConstants.NS_SWE_101,
            SweBoolean.class, SweCategory.class, SweCount.class, SweObservableProperty.class, SweQuantity.class,
            SweQuantityRange.class, SweText.class, SweTime.class, SweTimeRange.class, SweEnvelope.class,
            SweCoordinate.class, SweDataArray.class, SweDataRecord.class, SweSimpleDataRecord.class, TimePeriod.class);

    public SweCommonEncoderv101() {
        LOGGER.debug("Encoder for the following keys initialized successfully: {}!",
                Joiner.on(", ").join(ENCODER_KEYS));
    }

    @Override
    public Set<EncoderKey> getKeys() {
        return Collections.unmodifiableSet(ENCODER_KEYS);
    }

    @Override
    public void addNamespacePrefixToMap(Map<String, String> nameSpacePrefixMap) {
        nameSpacePrefixMap.put(SweConstants.NS_SWE_101, SweConstants.NS_SWE_PREFIX);
    }

    @Override
    public Set<SchemaLocation> getSchemaLocations() {
        return Sets.newHashSet(SweConstants.SWE_101_SCHEMA_LOCATION);
    }

    @Override
    public XmlObject encode(Object element, EncodingContext context) throws EncodingException {
        XmlObject encodedObject = null;
        if (element instanceof SweAbstractSimpleType) {
            encodedObject = createSimpleType((SweAbstractSimpleType<?>) element, context);
            // }
            // if (element instanceof SweBoolean) {
            // encodedObject = createBoolean((SweBoolean) element);
            // } else if (element instanceof SweCategory) {
            // encodedObject = createCategory((SweCategory) element);
            // } else if (element instanceof SweCount) {
            // encodedObject = createCount((SweCount) element);
            // } else if (element instanceof SweObservableProperty) {
            // encodedObject = createObservableProperty((SweObservableProperty)
            // element);
            // } else if (element instanceof SweQuantity) {
            // encodedObject = createQuantity((SweQuantity) element);
            // } else if (element instanceof SweQuantityRange) {
            // encodedObject = createQuantityRange((SweQuantityRange) element);
            // } else if (element instanceof SweText) {
            // encodedObject = createText((SweText) element);
            // } else if (element instanceof SweTime) {
            // encodedObject = createTime((SweTime) element);
            // } else if (element instanceof SweTimeRange) {
            // encodedObject = createTimeRange((SweTimeRange) element);
        } else if (element instanceof SweCoordinate) {
            encodedObject = createCoordinate((SweCoordinate<?>) element);
        } else if (element instanceof SweDataArray) {
            encodedObject = createDataArray((SweDataArray) element);
        } else if (element instanceof SweDataRecord) {
            DataRecordType drt = createDataRecord((SweDataRecord) element);
            if (context.has(XmlBeansEncodingFlags.DOCUMENT)) {
                DataRecordDocument drd = DataRecordDocument.Factory.newInstance(getXmlOptions());
                drd.setDataRecord(drt);
                encodedObject = drd;
            } else {
                encodedObject = drt;
            }
        } else if (element instanceof SweEnvelope) {
            encodedObject = createEnvelope((SweEnvelope) element);
        } else if (element instanceof SweSimpleDataRecord) {
            encodedObject = createSimpleDataRecord((SweSimpleDataRecord) element);
        } else if (element instanceof TimePeriod) {
            encodedObject = createTimeGeometricPrimitivePropertyType((TimePeriod) element);
        } else {
            throw new UnsupportedEncoderInputException(this, element);
        }
        XmlHelper.validateDocument(encodedObject, EncodingException::new);
        return encodedObject;
    }

    private AbstractDataComponentType createSimpleType(SweAbstractSimpleType<?> sosSimpleType)
            throws EncodingException {
        return createSimpleType(sosSimpleType, null);
    }

    private AbstractDataComponentType createSimpleType(SweAbstractSimpleType<?> sosSimpleType,
            EncodingContext additionalValues) throws EncodingException {
        AbstractDataComponentType abstractDataComponentType = null;
        if (sosSimpleType instanceof SweBoolean) {
            abstractDataComponentType = createBoolean((SweBoolean) sosSimpleType);
        } else if (sosSimpleType instanceof SweCategory) {
            abstractDataComponentType = createCategory((SweCategory) sosSimpleType);
        } else if (sosSimpleType instanceof SweCount) {
            abstractDataComponentType = createCount((SweCount) sosSimpleType);
        } else if (sosSimpleType instanceof SweCountRange) {
            abstractDataComponentType = createCountRange((SweCountRange) sosSimpleType);
        } else if (sosSimpleType instanceof SweObservableProperty) {
            abstractDataComponentType = createObservableProperty((SweObservableProperty) sosSimpleType);
        } else if (sosSimpleType instanceof SweQuantity) {
            abstractDataComponentType = createQuantity((SweQuantity) sosSimpleType);
        } else if (sosSimpleType instanceof SweQuantityRange) {
            abstractDataComponentType = createQuantityRange((SweQuantityRange) sosSimpleType);
        } else if (sosSimpleType instanceof SweText) {
            abstractDataComponentType = createText((SweText) sosSimpleType);
        } else if (sosSimpleType instanceof SweTimeRange) {
            abstractDataComponentType = createTimeRange((SweTimeRange) sosSimpleType);
        } else if (sosSimpleType instanceof SweTime) {
            abstractDataComponentType = createTime((SweTime) sosSimpleType);
        } else {
            throw new NotYetSupportedEncodingException(SweAbstractSimpleType.class.getSimpleName(), sosSimpleType);
        }
        addAbstractDataComponentValues(abstractDataComponentType, sosSimpleType);
        return abstractDataComponentType;

    }

    private SimpleDataRecordType createSimpleDataRecord(SweSimpleDataRecord simpleDataRecord)
            throws EncodingException {
        SimpleDataRecordType xbSimpleDataRecord = SimpleDataRecordType.Factory.newInstance(getXmlOptions());
        if (simpleDataRecord.isSetDefinition()) {
            xbSimpleDataRecord.setDefinition(simpleDataRecord.getDefinition());
        }
        if (simpleDataRecord.isSetDescription()) {
            StringOrRefType xbSoR = StringOrRefType.Factory.newInstance();
            xbSoR.setStringValue(simpleDataRecord.getDefinition());
            xbSimpleDataRecord.setDescription(xbSoR);
        }
        if (simpleDataRecord.isSetFields()) {
            AnyScalarPropertyType[] xbFields = new AnyScalarPropertyType[simpleDataRecord.getFields().size()];
            int xbFieldIndex = 0;
            for (SweField sweField : simpleDataRecord.getFields()) {
                AnyScalarPropertyType xbField = createFieldForSimpleDataRecord(sweField);
                xbFields[xbFieldIndex] = xbField;
                xbFieldIndex++;
            }
            xbSimpleDataRecord.setFieldArray(xbFields);
        }
        return xbSimpleDataRecord;
    }

    private AnyScalarPropertyType createFieldForSimpleDataRecord(SweField sweField) throws EncodingException {
        SweAbstractDataComponent sosElement = sweField.getElement();
        AnyScalarPropertyType xbField = AnyScalarPropertyType.Factory.newInstance(getXmlOptions());
        if (sweField.isSetName()) {
            xbField.setName(sweField.getName().getValue());
        }
        AbstractDataComponentType xbDCD;
        if (sosElement instanceof SweBoolean) {
            xbDCD = xbField.addNewBoolean();
            xbDCD.set(createSimpleType((SweBoolean) sosElement));
        } else if (sosElement instanceof SweCategory) {
            xbDCD = xbField.addNewCategory();
            xbDCD.set(createSimpleType((SweCategory) sosElement));
        } else if (sosElement instanceof SweCount) {
            xbDCD = xbField.addNewCount();
            xbDCD.set(createSimpleType((SweCount) sosElement));
        } else if (sosElement instanceof SweQuantity) {
            xbDCD = xbField.addNewQuantity();
            xbDCD.set(createSimpleType((SweQuantity) sosElement));
        } else if (sosElement instanceof SweText) {
            xbDCD = xbField.addNewText();
            xbDCD.set(createSimpleType((SweText) sosElement));
        } else if (sosElement instanceof SweTime) {
            xbDCD = xbField.addNewTime();
            xbDCD.set(createSimpleType((SweTime) sosElement));
        } else {
            throw new EncodingException(
                    "The element type '%s' of the received %s is not supported by this encoder '%s'.",
                    new Object[] { sosElement != null ? sosElement.getClass().getName() : null,
                            sweField.getClass().getName(), getClass().getName() });
        }
        return xbField;
    }

    private DataComponentPropertyType createField(SweField sweField) throws EncodingException {
        SweAbstractDataComponent sosElement = sweField.getElement();
        DataComponentPropertyType xbField = DataComponentPropertyType.Factory.newInstance(getXmlOptions());
        if (sweField.isSetName()) {
            xbField.setName(sweField.getName().getValue());
        }
        if (sosElement instanceof SweBoolean) {
            xbField.addNewBoolean().set(createSimpleType((SweBoolean) sosElement));
        } else if (sosElement instanceof SweCategory) {
            xbField.addNewCategory().set(createSimpleType((SweCategory) sosElement));
        } else if (sosElement instanceof SweCount) {
            xbField.addNewCount().set(createSimpleType((SweCount) sosElement));
        } else if (sosElement instanceof SweCountRange) {
            xbField.addNewCount().set(createSimpleType((SweCountRange) sosElement));
        } else if (sosElement instanceof SweQuantity) {
            xbField.addNewQuantity().set(createSimpleType((SweQuantity) sosElement));
        } else if (sosElement instanceof SweQuantityRange) {
            xbField.addNewQuantity().set(createSimpleType((SweQuantityRange) sosElement));
        } else if (sosElement instanceof SweText) {
            xbField.addNewText().set(createSimpleType((SweText) sosElement));
        } else if (sosElement instanceof SweTimeRange) {
            xbField.addNewTimeRange().set(createSimpleType((SweTimeRange) sosElement));
        } else if (sosElement instanceof SweTime) {
            xbField.addNewTime().set(createSimpleType((SweTime) sosElement));
        } else if (sosElement instanceof SweEnvelope) {
            EnvelopeType xbEnvelope = (EnvelopeType) xbField.addNewAbstractDataRecord()
                    .substitute(SweConstants.QN_ENVELOPE_SWE_101, EnvelopeType.type);
            xbEnvelope.set(createEnvelope((SweEnvelope) sosElement));
        } else if (sosElement instanceof SweDataRecord) {
            DataRecordType xbEnvelope = (DataRecordType) xbField.addNewAbstractDataRecord()
                    .substitute(SweConstants.QN_DATA_RECORD_SWE_101, DataRecordType.type);
            xbEnvelope.set(createDataRecord((SweDataRecord) sosElement));
        } else if (sosElement instanceof SweDataArray) {
            DataArrayType xbEnvelope = (DataArrayType) xbField.addNewAbstractDataRecord()
                    .substitute(SweConstants.QN_DATA_RECORD_SWE_101, DataArrayType.type);
            xbEnvelope.set(createDataArray((SweDataArray) sosElement).getDataArray1());
        } else {
            throw new EncodingException(
                    "The element type '%s' of the received '%s' is not supported by this encoder '%s'.",
                    new Object[] { sosElement != null ? sosElement.getClass().getName() : null,
                            sweField.getClass().getName(), getClass().getName() });
        }
        return xbField;
    }

    private net.opengis.swe.x101.BooleanDocument.Boolean createBoolean(SweBoolean bool) throws EncodingException {
        net.opengis.swe.x101.BooleanDocument.Boolean xbBoolean =
                net.opengis.swe.x101.BooleanDocument.Boolean.Factory.newInstance(getXmlOptions());
        if (bool.isSetValue()) {
            xbBoolean.setValue(bool.getValue());
        }
        if (bool.isSetQuality()) {
            xbBoolean.setQuality(createQuality(bool.getQuality())[0]);
        }
        return xbBoolean;
    }

    private QualityPropertyType[] createQuality(Collection<SweQuality> quality) {
        if (!quality.isEmpty()) {
            ArrayList<QualityPropertyType> xbQualities = Lists.newArrayListWithCapacity(quality.size());
            for (SweQuality sweQuality : quality) {
                QualityPropertyType xbQuality = QualityPropertyType.Factory.newInstance();
                if (sweQuality instanceof SweText) {
                    xbQuality.addNewText().set(createText((SweText) sweQuality));
                } else if (sweQuality instanceof SweCategory) {
                    xbQuality.addNewCategory().set(createCategory((SweCategory) sweQuality));
                } else if (sweQuality instanceof SweQuantity) {
                    xbQuality.addNewQuantity().set(createQuantity((SweQuantity) sweQuality));
                } else if (sweQuality instanceof SweQuantityRange) {
                    xbQuality.addNewQuantityRange().set(createQuantityRange((SweQuantityRange) sweQuality));
                }
                xbQualities.add(xbQuality);
            }
            return xbQualities.toArray(new QualityPropertyType[xbQualities.size()]);
        }
        return new QualityPropertyType[] { QualityPropertyType.Factory.newInstance() };
    }

    private Category createCategory(SweCategory component) {
        Category xml = Category.Factory.newInstance(getXmlOptions());
        if (component.isSetValue()) {
            xml.setValue(component.getValue());
        }
        if (component.isSetCodeSpace()) {
            xml.addNewCodeSpace().setHref(component.getCodeSpace());
        }
        if (component.isSetQuality()) {
            xml.setQuality(createQuality(component.getQuality())[0]);
        }
        if (component.isSetContstraint()) {
            createConstraint(xml.getConstraint(), component.getConstraint());
        }
        return xml;
    }

    private Count createCount(SweCount component) throws EncodingException {
        Count xml = Count.Factory.newInstance(getXmlOptions());
        if (component.isSetValue()) {
            xml.setValue(new BigInteger(Integer.toString(component.getValue())));
        }
        if (component.isSetQuality()) {
            xml.setQualityArray(createQuality(component.getQuality()));
        }
        if (component.isSetContstraint()) {
            createConstraint(xml.getConstraint(), component.getConstraint());
        }
        return xml;
    }

    private CountRange createCountRange(SweCountRange component) throws EncodingException {
        CountRange xml = CountRange.Factory.newInstance(getXmlOptions());
        if (component.isSetValue()) {
            xml.setValue(component.getValue().getRangeAsList());
        }
        if (component.isSetQuality()) {
            xml.setQualityArray(createQuality(component.getQuality()));
        }
        if (component.isSetContstraint()) {
            createConstraint(xml.getConstraint(), component.getConstraint());
        }
        return xml;
    }

    private ObservableProperty createObservableProperty(SweObservableProperty observableProperty)
            throws EncodingException {
        ObservableProperty xbObservableProperty = ObservableProperty.Factory.newInstance(getXmlOptions());
        return xbObservableProperty;
    }

    /**
     * Adds values to SWE quantity
     *
     * @param component
     *            SOS internal representation
     *
     * @return the quantity
     */
    protected Quantity createQuantity(SweQuantity component) {
        Quantity xml = Quantity.Factory.newInstance(getXmlOptions());
        if (component.isSetAxisID()) {
            xml.setAxisID(component.getAxisID());
        }
        if (component.isSetValue()) {
            xml.setValue(component.getValue());
        }
        if (component.isSetUom()) {
            xml.addNewUom().set(createUom(component.getUomObject()));
        }
        if (component.isSetQuality()) {
            xml.setQualityArray(createQuality(component.getQuality()));
        }
        if (component.isSetContstraint()) {
            createConstraint(xml.getConstraint(), component.getConstraint());
        }
        return xml;
    }

    protected QuantityRange createQuantityRange(SweQuantityRange component) {
        QuantityRange xml = QuantityRange.Factory.newInstance(getXmlOptions());
        if (component.isSetAxisID()) {
            xml.setAxisID(component.getDescription());
        }
        if (component.isSetValue()) {
            xml.setValue(component.getValue().getRangeAsList());
        }
        if (component.isSetUom()) {
            xml.addNewUom().set(createUom(component.getUomObject()));
        }
        if (component.isSetQuality()) {
            xml.setQualityArray(createQuality(component.getQuality()));
        }
        if (component.isSetContstraint()) {
            createConstraint(xml.getConstraint(), component.getConstraint());
        }
        return xml;
    }

    /**
     * Adds values to SWE text
     *
     * @param text
     *            SOS internal representation
     *
     * @return the text
     */
    private Text createText(SweText component) {
        Text xml = Text.Factory.newInstance(getXmlOptions());
        if (component.isSetValue()) {
            xml.setValue(component.getValue());
        }
        return xml;
    }

    private Time createTime(SweTime component) throws EncodingException {
        Time xml = Time.Factory.newInstance(getXmlOptions());
        if (component.isSetValue()) {
            XmlDateTime xbDateTime = createDateTime(component.getValue());
            xml.setValue(xbDateTime);
        }
        if (component.isSetUom()) {
            if (component.getUom().startsWith(URN) || component.getUom().startsWith(HTTP)) {
                xml.addNewUom().setHref(component.getUom());
            } else {
                xml.addNewUom().setCode(component.getUom());
            }
        }
        if (component.isSetQuality()) {
            xml.setQuality(createQuality(component.getQuality())[0]);
        }
        if (component.isSetContstraint()) {
            createConstraint(xml.getConstraint(), component.getConstraint());
        }
        return xml;
    }

    private AllowedValuesPropertyType createConstraint(AllowedValuesPropertyType avpt,
            Referenceable<SweAllowedValues> constraint) {
        if (constraint.isInstance()) {
            createAllowedValues(avpt.addNewAllowedValues(), constraint.getInstance());
        } else if (constraint.isReference()) {
            Reference ref = constraint.getReference();
            if (ref.getHref().isPresent()) {
                avpt.setHref(ref.getHref().get().toString());
            }
            if (ref.getTitle().isPresent()) {
                avpt.setTitle(ref.getTitle().get());
            }
            if (ref.getActuate().isPresent()) {
                avpt.setActuate(ActuateType.Enum.forString(ref.getActuate().get()));
            }
            if (ref.getArcrole().isPresent()) {
                avpt.setArcrole(ref.getArcrole().get());
            }
            if (ref.getRole().isPresent()) {
                avpt.setRole(ref.getRole().get());
            }
            if (ref.getShow().isPresent()) {
                avpt.setShow(ShowType.Enum.forString(ref.getShow().get()));
            }
            if (ref.getType().isPresent()) {
                avpt.setType(TypeType.Enum.forString(ref.getType().get()));
            }
        }
        return avpt;
    }

    private AllowedTokensPropertyType createConstraint(AllowedTokensPropertyType atpt,
            Referenceable<SweAllowedTokens> constraint) {
        if (constraint.isInstance()) {
            createAllowedTokens(atpt.addNewAllowedTokens(), constraint.getInstance());
        } else if (constraint.isReference()) {
            Reference ref = constraint.getReference();
            if (ref.getHref().isPresent()) {
                atpt.setHref(ref.getHref().get().toString());
            }
            if (ref.getTitle().isPresent()) {
                atpt.setTitle(ref.getTitle().get());
            }
            if (ref.getActuate().isPresent()) {
                atpt.setActuate(ActuateType.Enum.forString(ref.getActuate().get()));
            }
            if (ref.getArcrole().isPresent()) {
                atpt.setArcrole(ref.getArcrole().get());
            }
            if (ref.getRole().isPresent()) {
                atpt.setRole(ref.getRole().get());
            }
            if (ref.getShow().isPresent()) {
                atpt.setShow(ShowType.Enum.forString(ref.getShow().get()));
            }
            if (ref.getType().isPresent()) {
                atpt.setType(TypeType.Enum.forString(ref.getType().get()));
            }
        }
        return atpt;
    }

    private AllowedTimesPropertyType createConstraint(AllowedTimesPropertyType atpt,
            Referenceable<SweAllowedTimes> constraint) {
        if (constraint.isInstance()) {
            createAllowedTimes(atpt.addNewAllowedTimes(), constraint.getInstance());
        } else if (constraint.isReference()) {
            Reference ref = constraint.getReference();
            if (ref.getHref().isPresent()) {
                atpt.setHref(ref.getHref().get().toString());
            }
            if (ref.getTitle().isPresent()) {
                atpt.setTitle(ref.getTitle().get());
            }
            if (ref.getActuate().isPresent()) {
                atpt.setActuate(ActuateType.Enum.forString(ref.getActuate().get()));
            }
            if (ref.getArcrole().isPresent()) {
                atpt.setArcrole(ref.getArcrole().get());
            }
            if (ref.getRole().isPresent()) {
                atpt.setRole(ref.getRole().get());
            }
            if (ref.getShow().isPresent()) {
                atpt.setShow(ShowType.Enum.forString(ref.getShow().get()));
            }
            if (ref.getType().isPresent()) {
                atpt.setType(TypeType.Enum.forString(ref.getType().get()));
            }
        }
        return atpt;
    }

    private AllowedValues createAllowedValues(AllowedValues avt, Nillable<SweAllowedValues> instance) {
        if (instance.isPresent()) {
            if (instance.get().isSetGmlID()) {
                avt.setId(instance.get().getGmlId());
            }
            if (instance.get().isSetValue()) {
                for (Double value : instance.get().getValue()) {
                    avt.addValueList(Lists.newArrayList(value));
                }
            }
            if (instance.get().isSetInterval()) {
                for (RangeValue<Double> interval : instance.get().getInterval()) {
                    avt.addInterval(interval.getRangeAsList());
                }
            }
        }
        return avt;
    }

    private AllowedTokens createAllowedTokens(AllowedTokens att, Nillable<SweAllowedTokens> instance) {
        if (instance.isPresent()) {
            if (instance.get().isSetGmlID()) {
                att.setId(instance.get().getGmlId());
            }
            if (instance.get().isSetValue()) {
                for (String value : instance.get().getValue()) {
                    att.addValueList(Lists.newArrayList(value));
                }
            }
        }
        return att;
    }

    private AllowedTimes createAllowedTimes(AllowedTimes att, Nillable<SweAllowedTimes> instance) {
        if (instance.isPresent()) {
            if (instance.get().isSetGmlID()) {
                att.setId(instance.get().getGmlId());
            }
            if (instance.get().isSetValue()) {
                for (DateTime value : instance.get().getValue()) {
                    att.addNewValueList()
                            .setListValue(Lists.newArrayList(DateTimeHelper.formatDateTime2IsoString(value)));
                }
            }
            if (instance.get().isSetInterval()) {
                for (RangeValue<DateTime> interval : instance.get().getInterval()) {
                    List<String> list = Lists.newArrayListWithCapacity(2);
                    list.add(DateTimeHelper.formatDateTime2IsoString(interval.getRangeStart()));
                    if (interval.isSetEndValue()) {
                        list.add(DateTimeHelper.formatDateTime2IsoString(interval.getRangeEnd()));
                    }
                    att.addInterval(list);
                }
            }
        }
        return att;
    }

    private XmlDateTime createDateTime(DateTime sosDateTime) {
        XmlDateTime xbDateTime = XmlDateTime.Factory.newInstance(getXmlOptions());

        // encode the DateTime in UTC
        GDateBuilder gdb = new GDateBuilder(sosDateTime.toDate());
        gdb.normalize();
        xbDateTime.setGDateValue(gdb.toGDate());

        return xbDateTime;
    }

    private EnvelopeType createEnvelope(SweEnvelope sosSweEnvelope) throws EncodingException {
        EnvelopeType envelopeType = EnvelopeType.Factory.newInstance(getXmlOptions());
        addAbstractDataComponentValues(envelopeType, sosSweEnvelope);
        if (sosSweEnvelope.isReferenceFrameSet()) {
            envelopeType.setReferenceFrame(sosSweEnvelope.getReferenceFrame());
        }
        if (sosSweEnvelope.isLowerCornerSet()) {
            envelopeType.setLowerCorner(createVectorProperty(sosSweEnvelope.getLowerCorner()));
        }
        if (sosSweEnvelope.isUpperCornerSet()) {
            envelopeType.setUpperCorner(createVectorProperty(sosSweEnvelope.getUpperCorner()));
        }
        if (sosSweEnvelope.isTimeSet()) {
            envelopeType.addNewTime().setTimeRange(createTimeRange(sosSweEnvelope.getTime()));
        }
        return envelopeType;
    }

    private VectorPropertyType createVectorProperty(SweVector sosSweVector) throws EncodingException {
        VectorPropertyType vectorPropertyType = VectorPropertyType.Factory.newInstance(getXmlOptions());
        vectorPropertyType.setVector(createVector(sosSweVector.getCoordinates()));
        return vectorPropertyType;
    }

    private VectorType createVector(List<? extends SweCoordinate<?>> coordinates) throws EncodingException {
        VectorType vectorType = VectorType.Factory.newInstance(getXmlOptions());
        vectorType.setCoordinateArray(createCoordinates(coordinates));
        return vectorType;
    }

    private TimeRange createTimeRange(SweTimeRange timeRange) throws EncodingException {
        TimeRange xbTimeRange = TimeRange.Factory.newInstance(getXmlOptions());
        addAbstractDataComponentValues(xbTimeRange, timeRange);
        if (timeRange.isSetValue()) {
            xbTimeRange.setValue(timeRange.getValue().getRangeAsStringList());
        }
        if (timeRange.isSetUom()) {
            xbTimeRange.addNewUom().setCode(timeRange.getUom());
        }
        if (timeRange.isSetQuality()) {
            xbTimeRange.setQuality(createQuality(timeRange.getQuality())[0]);
        }
        return xbTimeRange;
    }

    private void addAbstractDataComponentValues(AbstractDataComponentType xbComponent,
            SweAbstractDataComponent component) throws EncodingException {
        if (component.isSetDefinition()) {
            xbComponent.setDefinition(component.getDefinition());
        }
        if (component.isSetDescription()) {
            xbComponent.addNewDescription().setStringValue(component.getDescription());
        }
        if (component.isSetName()) {
            xbComponent.addNewName().set(encodeObjectToXml(GmlConstants.NS_GML, component.getName()));
        }
    }

    /**
     * Adds values to SWE coordinates
     *
     * @param coordinate
     *            SOS internal representation
     *
     * @return the coordinate
     */
    private Coordinate createCoordinate(SweCoordinate<?> coordinate) {
        Coordinate xbCoordinate = Coordinate.Factory.newInstance(getXmlOptions());
        xbCoordinate.setName(coordinate.getName());
        xbCoordinate.setQuantity(createQuantity((SweQuantity) coordinate.getValue()));
        return xbCoordinate;
    }

    /**
     * Adds values to SWE coordinates
     *
     * @param coordinates
     *            SOS internal representation
     *
     * @return the coordinates
     */
    private Coordinate[] createCoordinates(List<? extends SweCoordinate<?>> coordinates) {
        if (coordinates != null) {
            return coordinates.stream().map(this::createCoordinate).toArray(Coordinate[]::new);
        }
        return null;
    }

    // TODO check types for SWE101
    private DataRecordType createDataRecord(SweDataRecord sosDataRecord) throws EncodingException {

        List<SweField> sosFields = sosDataRecord.getFields();

        DataRecordType xbDataRecord = DataRecordType.Factory.newInstance(getXmlOptions());

        if (sosDataRecord.isSetDefinition()) {
            xbDataRecord.setDefinition(sosDataRecord.getDefinition());
        }

        if (sosDataRecord.isSetFields()) {
            DataComponentPropertyType[] xbFields = new DataComponentPropertyType[sosFields.size()];
            int xbFieldIndex = 0;
            for (SweField sosSweField : sosFields) {
                DataComponentPropertyType xbField = createField(sosSweField);
                xbFields[xbFieldIndex] = xbField;
                xbFieldIndex++;
            }
            xbDataRecord.setFieldArray(xbFields);
        }
        return xbDataRecord;
    }

    private DataArrayDocument createDataArray(SweDataArray sosDataArray) throws EncodingException {
        if (sosDataArray != null) {
            if (sosDataArray.isSetElementTyp()) {
                DataArrayDocument xbDataArrayDoc = DataArrayDocument.Factory.newInstance(getXmlOptions());
                DataArrayType xbDataArray = xbDataArrayDoc.addNewDataArray1();

                // set element count
                if (sosDataArray.isSetElementCount()) {
                    xbDataArray.addNewElementCount().addNewCount().set(createCount(sosDataArray.getElementCount()));
                }

                if (sosDataArray.isSetElementTyp()) {
                    DataComponentPropertyType xbElementType = xbDataArray.addNewElementType();
                    xbElementType.setName("Components");
                    // FIXME use visitor pattern
                    if (sosDataArray.getElementType() instanceof SweBoolean) {
                        xbElementType.addNewBoolean()
                                .set(createSimpleType((SweBoolean) sosDataArray.getElementType()));
                    } else if (sosDataArray.getElementType() instanceof SweCategory) {
                        xbElementType.addNewCategory()
                                .set(createSimpleType((SweCategory) sosDataArray.getElementType()));
                    } else if (sosDataArray.getElementType() instanceof SweCount) {
                        xbElementType.addNewCount().set(createSimpleType((SweCount) sosDataArray.getElementType()));
                    } else if (sosDataArray.getElementType() instanceof SweQuantity) {
                        xbElementType.addNewQuantity()
                                .set(createSimpleType((SweQuantity) sosDataArray.getElementType()));
                    } else if (sosDataArray.getElementType() instanceof SweText) {
                        xbElementType.addNewText().set(createSimpleType((SweText) sosDataArray.getElementType()));
                    } else if (sosDataArray.getElementType() instanceof SweTimeRange) {
                        xbElementType.addNewTimeRange()
                                .set(createSimpleType((SweTimeRange) sosDataArray.getElementType()));
                    } else if (sosDataArray.getElementType() instanceof SweTime) {
                        xbElementType.addNewTime().set(createSimpleType((SweTime) sosDataArray.getElementType()));
                    } else if (sosDataArray.getElementType() instanceof SweEnvelope) {
                        xbElementType.addNewAbstractDataRecord()
                                .set(createEnvelope((SweEnvelope) sosDataArray.getElementType()));
                        xbElementType.getAbstractDataRecord().substitute(SweConstants.QN_ENVELOPE_SWE_101,
                                EnvelopeType.type);
                    } else if (sosDataArray.getElementType() instanceof SweDataRecord) {
                        xbElementType.addNewAbstractDataRecord()
                                .set(createDataRecord((SweDataRecord) sosDataArray.getElementType()));
                        xbElementType.getAbstractDataRecord().substitute(SweConstants.QN_DATA_RECORD_SWE_101,
                                DataRecordType.type);
                    } else if (sosDataArray.getElementType() instanceof SweDataArray) {
                        xbElementType.addNewAbstractDataArray1()
                                .set(createDataArray((SweDataArray) sosDataArray.getElementType()).getDataArray1());
                        xbElementType.getAbstractDataArray1().substitute(SweConstants.QN_DATA_RECORD_SWE_101,
                                DataArrayType.type);
                    } else {
                        throw new UnsupportedEncoderInputException(this, sosDataArray.getElementType());
                    }
                }

                if (sosDataArray.isSetEncoding()) {

                    BlockEncodingPropertyType xbEncoding = xbDataArray.addNewEncoding();
                    xbEncoding.set(createBlockEncoding(sosDataArray.getEncoding()));
                    // xbDataArray.getEncoding().substitute(
                    // new QName(SWEConstants.NS_SWE_101,
                    // SWEConstants.EN_TEXT_ENCODING,
                    // SWEConstants.NS_SWE_PREFIX), TextBlock.type);
                }
                // if (absObs.getObservationTemplateIDs() == null
                // || (absObs.getObservationTemplateIDs() != null &&
                // absObs.getObservationTemplateIDs().isEmpty())) {
                // xbValues.newCursor().setTextValue(createResultString(phenComponents,
                // absObs));
                // }
                if (sosDataArray.isSetValues()) {
                    xbDataArray.addNewValues().set(createValues(sosDataArray.getValues(), sosDataArray.getEncoding()));
                }
                return xbDataArrayDoc;
            } else if (sosDataArray.isSetXml()) {
                try {
                    XmlObject xmlObject = XmlObject.Factory.parse(sosDataArray.getXml().trim());
                    if (xmlObject instanceof DataArrayDocument) {
                        return (DataArrayDocument) xmlObject;
                    } else {
                        DataArrayDocument xbDataArrayDoc = DataArrayDocument.Factory.newInstance(getXmlOptions());
                        xbDataArrayDoc.setDataArray1(DataArrayType.Factory.parse(sosDataArray.getXml().trim()));
                        return xbDataArrayDoc;
                    }
                } catch (XmlException e) {
                    throw new EncodingException("Error while encoding SweDataArray!", e);
                }
            }
        }
        return null;
    }

    private XmlString createValues(List<List<String>> values, SweAbstractEncoding encoding) {
        return createValues((SweTextEncoding) encoding, values);
    }

    private XmlString createValues(SweTextEncoding textEncoding, List<List<String>> values) {
        // TODO How to deal with the decimal separator - is it an issue here?
        // textEncoding.getDecimalSeparator();

        String tokenSeparator = textEncoding.getTokenSeparator();
        String blockSeparator = textEncoding.getBlockSeparator();

        String valueString =
                values.stream().map(block -> String.join(tokenSeparator, block)).collect(joining(blockSeparator));

        // create XB result object
        XmlString xbValueString = XmlString.Factory.newInstance();
        xbValueString.setStringValue(valueString);
        return xbValueString;
    }

    private BlockEncodingPropertyType createBlockEncoding(SweAbstractEncoding sosSweAbstractEncoding)
            throws EncodingException {

        try {
            if (sosSweAbstractEncoding instanceof SweTextEncoding) {
                return createTextEncoding((SweTextEncoding) sosSweAbstractEncoding);
            }
            if (sosSweAbstractEncoding.getXml() != null && !sosSweAbstractEncoding.getXml().isEmpty()) {
                XmlObject xmlObject = XmlObject.Factory.parse(sosSweAbstractEncoding.getXml());
                if (xmlObject instanceof AbstractEncodingType) {
                    return (BlockEncodingPropertyType) xmlObject;
                }
                throw new EncodingException("AbstractEncoding can not be encoded!");
            }

        } catch (XmlException e) {
            throw new EncodingException("Error while encoding AbstractEncoding!", e);
        }
        return null;
    }

    private BlockEncodingPropertyType createTextEncoding(SweTextEncoding sosTextEncoding) {
        BlockEncodingPropertyType xbTextEncodingType = BlockEncodingPropertyType.Factory.newInstance(getXmlOptions());
        TextBlock xbTextEncoding = xbTextEncodingType.addNewTextBlock();

        if (sosTextEncoding.getBlockSeparator() != null) {
            xbTextEncoding.setBlockSeparator(sosTextEncoding.getBlockSeparator());
        }
        // TODO check not used in SWE101
        // if (sosTextEncoding.isSetCollapseWhiteSpaces()) {
        // xbTextEncoding.setCollapseWhiteSpaces(sosTextEncoding.isCollapseWhiteSpaces());
        // }
        if (sosTextEncoding.getDecimalSeparator() != null) {
            xbTextEncoding.setDecimalSeparator(sosTextEncoding.getDecimalSeparator());
        }
        if (sosTextEncoding.getTokenSeparator() != null) {
            xbTextEncoding.setTokenSeparator(sosTextEncoding.getTokenSeparator());
        }
        // wont cast !!! net.opengis.swe.x101.impl.BlockEncodingPropertyTypeImpl
        // cannot be cast to net.opengis.swe.x101.AbstractEncodingType
        return xbTextEncodingType;
    }

    private XmlObject createTimeGeometricPrimitivePropertyType(TimePeriod timePeriod) throws EncodingException {
        TimeGeometricPrimitivePropertyType xbTimeGeometricPrimitiveProperty =
                TimeGeometricPrimitivePropertyType.Factory.newInstance(getXmlOptions());
        if (timePeriod.isSetStart() && timePeriod.isSetEnd()) {
            xbTimeGeometricPrimitiveProperty.addNewTimeGeometricPrimitive()
                    .set(encodeObjectToXml(GmlConstants.NS_GML, timePeriod));
        }
        // TODO check GML 311 rename nodename of geometric primitive to
        // gml:timePeriod
        XmlCursor timeCursor = xbTimeGeometricPrimitiveProperty.newCursor();
        boolean hasTimePrimitive =
                timeCursor.toChild(new QName(GmlConstants.NS_GML, GmlConstants.EN_ABSTRACT_TIME_GEOM_PRIM));
        if (hasTimePrimitive) {
            timeCursor.setName(new QName(GmlConstants.NS_GML, GmlConstants.EN_TIME_PERIOD));
        }
        timeCursor.dispose();
        return xbTimeGeometricPrimitiveProperty;
    }

    private UomPropertyType createUom(UoM uom) {
        UomPropertyType upt = UomPropertyType.Factory.newInstance(getXmlOptions());
        if (!uom.isSetLink() && (uom.getUom().startsWith(URN) || uom.getUom().startsWith(HTTP))) {
            upt.setHref(uom.getUom());
        } else {
            upt.setCode(uom.getUom());
        }
        if (uom.isSetName()) {
            upt.setTitle(uom.getName());
        }
        if (uom.isSetLink()) {
            upt.setHref(uom.getLink());
        }
        return upt;
    }
}
