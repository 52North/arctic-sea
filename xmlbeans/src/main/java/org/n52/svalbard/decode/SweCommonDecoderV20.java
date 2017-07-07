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
package org.n52.svalbard.decode;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.joda.time.DateTime;
import org.n52.shetland.ogc.OGCConstants;
import org.n52.shetland.ogc.UoM;
import org.n52.shetland.ogc.swe.RangeValue;
import org.n52.shetland.ogc.swe.SweAbstractDataComponent;
import org.n52.shetland.ogc.swe.SweConstants;
import org.n52.shetland.ogc.swe.SweCoordinate;
import org.n52.shetland.ogc.swe.SweDataArray;
import org.n52.shetland.ogc.swe.SweDataRecord;
import org.n52.shetland.ogc.swe.SweField;
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
import org.n52.shetland.ogc.swe.simpleType.SweQuality;
import org.n52.shetland.ogc.swe.simpleType.SweQuantity;
import org.n52.shetland.ogc.swe.simpleType.SweQuantityRange;
import org.n52.shetland.ogc.swe.simpleType.SweText;
import org.n52.shetland.ogc.swe.simpleType.SweTime;
import org.n52.shetland.ogc.swe.simpleType.SweTimeRange;
import org.n52.shetland.util.CollectionHelper;
import org.n52.shetland.util.DateTimeHelper;
import org.n52.shetland.util.DateTimeParseException;
import org.n52.shetland.w3c.xlink.Reference;
import org.n52.shetland.w3c.xlink.Referenceable;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.decode.exception.NotYetSupportedDecodingException;
import org.n52.svalbard.decode.exception.UnsupportedDecoderInputException;
import org.n52.svalbard.decode.exception.UnsupportedDecoderXmlInputException;
import org.n52.svalbard.util.CodingHelper;
import org.n52.svalbard.util.XmlHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

import net.opengis.swe.x20.AbstractDataComponentDocument;
import net.opengis.swe.x20.AbstractDataComponentType;
import net.opengis.swe.x20.AbstractEncodingType;
import net.opengis.swe.x20.AllowedTimesPropertyType;
import net.opengis.swe.x20.AllowedTimesType;
import net.opengis.swe.x20.AllowedTokensPropertyType;
import net.opengis.swe.x20.AllowedTokensType;
import net.opengis.swe.x20.AllowedValuesPropertyType;
import net.opengis.swe.x20.AllowedValuesType;
import net.opengis.swe.x20.AnyScalarPropertyType;
import net.opengis.swe.x20.BooleanPropertyType;
import net.opengis.swe.x20.BooleanType;
import net.opengis.swe.x20.CategoryPropertyType;
import net.opengis.swe.x20.CategoryType;
import net.opengis.swe.x20.CountPropertyType;
import net.opengis.swe.x20.CountRangeType;
import net.opengis.swe.x20.CountType;
import net.opengis.swe.x20.DataArrayDocument;
import net.opengis.swe.x20.DataArrayPropertyType;
import net.opengis.swe.x20.DataArrayType;
import net.opengis.swe.x20.DataRecordDocument;
import net.opengis.swe.x20.DataRecordPropertyType;
import net.opengis.swe.x20.DataRecordType;
import net.opengis.swe.x20.DataRecordType.Field;
import net.opengis.swe.x20.EncodedValuesPropertyType;
import net.opengis.swe.x20.QualityPropertyType;
import net.opengis.swe.x20.QuantityPropertyType;
import net.opengis.swe.x20.QuantityRangeType;
import net.opengis.swe.x20.QuantityType;
import net.opengis.swe.x20.TextEncodingDocument;
import net.opengis.swe.x20.TextEncodingType;
import net.opengis.swe.x20.TextPropertyType;
import net.opengis.swe.x20.TextType;
import net.opengis.swe.x20.TimeRangeType;
import net.opengis.swe.x20.TimeType;
import net.opengis.swe.x20.UnitReference;
import net.opengis.swe.x20.VectorType;
import net.opengis.swe.x20.VectorType.Coordinate;

/**
 * @since 1.0.0
 *
 */
public class SweCommonDecoderV20
        extends AbstractXmlDecoder<Object, Object> {
    private static final Logger LOGGER = LoggerFactory.getLogger(SweCommonDecoderV20.class);

    private static final Set<DecoderKey> DECODER_KEYS = CodingHelper.decoderKeysForElements(SweConstants.NS_SWE_20,
            AbstractDataComponentDocument.class, AbstractDataComponentType.class, AnyScalarPropertyType[].class,
            BooleanPropertyType.class, BooleanType.class, Coordinate[].class, CategoryPropertyType.class,
            CategoryType.class, CountPropertyType.class, CountType.class, DataArrayDocument.class,
            DataArrayPropertyType.class, DataArrayType.class, DataRecordDocument.class, DataRecordPropertyType.class,
            DataRecordType.class, QuantityPropertyType.class, QuantityType.class, TextEncodingDocument.class,
            TextEncodingType.class, TextPropertyType.class, TextType.class);

    public SweCommonDecoderV20() {
        LOGGER.debug("Decoder for the following keys initialized successfully: {}!",
                Joiner.on(", ").join(DECODER_KEYS));
    }

    @Override
    public Set<DecoderKey> getKeys() {
        return Collections.unmodifiableSet(DECODER_KEYS);
    }

    @Override
    public Object decode(Object element) throws DecodingException {
        if (element instanceof DataArrayPropertyType) {
            final DataArrayPropertyType dataArrayPropertyType = (DataArrayPropertyType) element;
            return parseAbstractDataComponent(dataArrayPropertyType.getDataArray1());
        } else if (element instanceof DataRecordPropertyType) {
            final DataRecordPropertyType dataRecordPropertyType = (DataRecordPropertyType) element;
            return parseAbstractDataComponent(dataRecordPropertyType.getDataRecord());
        } else if (element instanceof AbstractDataComponentDocument) {
            return parseAbstractDataComponentDocument((AbstractDataComponentDocument) element);
        } else if (element instanceof AbstractDataComponentType) {
            return parseAbstractDataComponent((AbstractDataComponentType) element);
        } else if (element instanceof Coordinate[]) {
            return parseCoordinates((Coordinate[]) element);
        } else if (element instanceof AnyScalarPropertyType[]) {
            return parseAnyScalarPropertyTypeArray((AnyScalarPropertyType[]) element);
        } else if (element instanceof TextEncodingDocument) {
            final TextEncodingDocument textEncodingDoc = (TextEncodingDocument) element;
            final SweTextEncoding sosTextEncoding = parseTextEncoding(textEncodingDoc.getTextEncoding());
            sosTextEncoding.setXml(textEncodingDoc.xmlText(getXmlOptions()));
            return sosTextEncoding;
        } else if (element instanceof TextEncodingType) {
            TextEncodingDocument textEncodingDoc = TextEncodingDocument.Factory.newInstance(getXmlOptions());
            TextEncodingType textEncoding = (TextEncodingType) element;
            textEncodingDoc.setTextEncoding(textEncoding);
            SweTextEncoding sosTextEncoding = parseTextEncoding(textEncoding);
            sosTextEncoding.setXml(textEncodingDoc.xmlText(getXmlOptions()));
            return sosTextEncoding;
        } else if (element instanceof TextPropertyType) {
            return parseAbstractDataComponent(((TextPropertyType) element).getText());
        } else if (element instanceof CountPropertyType) {
            return parseAbstractDataComponent(((CountPropertyType) element).getCount());
        } else if (element instanceof BooleanPropertyType) {
            return parseAbstractDataComponent(((BooleanPropertyType) element).getBoolean());
        } else if (element instanceof CategoryPropertyType) {
            return parseAbstractDataComponent(((CategoryPropertyType) element).getCategory());
        } else if (element instanceof QuantityPropertyType) {
            return parseAbstractDataComponent(((QuantityPropertyType) element).getQuantity());
        } else if (element instanceof XmlObject) {
            throw new UnsupportedDecoderXmlInputException(this, (XmlObject) element);
        } else {
            throw new UnsupportedDecoderInputException(this, element);
        }
    }

    private SweAbstractDataComponent parseAbstractDataComponent(AbstractDataComponentType abstractDataComponent)
            throws DecodingException {
        SweAbstractDataComponent sosAbstractDataComponent = null;
        if (abstractDataComponent instanceof BooleanType) {
            sosAbstractDataComponent = parseBoolean((BooleanType) abstractDataComponent);
        } else if (abstractDataComponent instanceof CategoryType) {
            sosAbstractDataComponent = parseCategory((CategoryType) abstractDataComponent);
        } else if (abstractDataComponent instanceof CountRangeType) {
            sosAbstractDataComponent = parseCountRange((CountRangeType) abstractDataComponent);
        } else if (abstractDataComponent instanceof CountType) {
            sosAbstractDataComponent = parseCount((CountType) abstractDataComponent);
        } else if (abstractDataComponent instanceof QuantityType) {
            sosAbstractDataComponent = parseQuantity((QuantityType) abstractDataComponent);
        } else if (abstractDataComponent instanceof QuantityRangeType) {
            sosAbstractDataComponent = parseQuantityRange((QuantityRangeType) abstractDataComponent);
        } else if (abstractDataComponent instanceof TextType) {
            sosAbstractDataComponent = parseText((TextType) abstractDataComponent);
        } else if (abstractDataComponent instanceof TimeType) {
            sosAbstractDataComponent = parseTime((TimeType) abstractDataComponent);
        } else if (abstractDataComponent instanceof TimeRangeType) {
            sosAbstractDataComponent = parseTimeRange((TimeRangeType) abstractDataComponent);
        } else if (abstractDataComponent instanceof VectorType) {
            sosAbstractDataComponent = parseVector((VectorType) abstractDataComponent);
        } else if (abstractDataComponent instanceof DataRecordType) {
            SweDataRecord sosDataRecord = parseDataRecord((DataRecordType) abstractDataComponent);
            DataRecordDocument dataRecordDoc = DataRecordDocument.Factory.newInstance(getXmlOptions());
            dataRecordDoc.setDataRecord((DataRecordType) abstractDataComponent);
            sosDataRecord.setXml(dataRecordDoc.xmlText(getXmlOptions()));
            sosAbstractDataComponent = sosDataRecord;
        } else if (abstractDataComponent instanceof DataArrayType) {
            SweDataArray sosDataArray = parseDataArray((DataArrayType) abstractDataComponent);
            DataArrayDocument dataArrayDoc = DataArrayDocument.Factory.newInstance(getXmlOptions());
            dataArrayDoc.setDataArray1((DataArrayType) abstractDataComponent);
            sosDataArray.setXml(dataArrayDoc.xmlText(getXmlOptions()));
            sosAbstractDataComponent = sosDataArray;
        } else {
            throw new UnsupportedDecoderXmlInputException(this, abstractDataComponent);
        }
        if (sosAbstractDataComponent != null) {
            if (abstractDataComponent.isSetDefinition()) {
                sosAbstractDataComponent.setDefinition(abstractDataComponent.getDefinition());
            }
            if (abstractDataComponent.isSetDescription()) {
                sosAbstractDataComponent.setDescription(abstractDataComponent.getDescription());
            }
            if (abstractDataComponent.isSetIdentifier()) {
                sosAbstractDataComponent.setIdentifier(abstractDataComponent.getIdentifier());
            }
            if (abstractDataComponent.isSetLabel()) {
                sosAbstractDataComponent.setLabel(abstractDataComponent.getLabel());
            }
        }
        return sosAbstractDataComponent;
    }

    private Object parseAbstractDataComponentDocument(final AbstractDataComponentDocument abstractDataComponentDoc)
            throws DecodingException {
        SweAbstractDataComponent sosAbstractDataComponent =
                parseAbstractDataComponent(abstractDataComponentDoc.getAbstractDataComponent());
        sosAbstractDataComponent.setXml(abstractDataComponentDoc.xmlText(getXmlOptions()));
        return sosAbstractDataComponent;
    }

    private SweDataArray parseDataArray(DataArrayType xbDataArray) throws DecodingException {
        SweDataArray sosSweDataArray = new SweDataArray();

        CountPropertyType elementCount = xbDataArray.getElementCount();
        if (elementCount != null) {
            sosSweDataArray.setElementCount(parseElementCount(elementCount));
        }

        // parse data record to elementType
        DataArrayType.ElementType xbElementType = xbDataArray.getElementType();
        if (xbElementType != null && xbElementType.getAbstractDataComponent() != null) {
            sosSweDataArray.setElementType(parseAbstractDataComponent(xbElementType.getAbstractDataComponent()));
        }
        if (xbDataArray.isSetEncoding()) {
            sosSweDataArray.setEncoding(parseEncoding(xbDataArray.getEncoding().getAbstractEncoding()));
        }

        // parse values
        if (xbDataArray.isSetValues()) {
            sosSweDataArray.setValues(parseValues(sosSweDataArray.getElementCount(), sosSweDataArray.getElementType(),
                    sosSweDataArray.getEncoding(), xbDataArray.getValues()));
        }
        // set XML
        DataArrayDocument dataArrayDoc = DataArrayDocument.Factory.newInstance(getXmlOptions());
        dataArrayDoc.setDataArray1(xbDataArray);
        sosSweDataArray.setXml(dataArrayDoc.xmlText(getXmlOptions()));
        return sosSweDataArray;
    }

    private List<List<String>> parseValues(final SweCount elementCount, final SweAbstractDataComponent elementType,
            final SweAbstractEncoding encoding, final EncodedValuesPropertyType encodedValuesPropertyType)
            throws DecodingException {
        if (checkParameterTypes(elementType, encoding)) {
            // Get swe values String via cursor as String
            String values;
            // TODO replace XmlCursor
            /*
             * if (encodedValuesPropertyType.schemaType() == XmlString.type) {
             * XmlString xbString
             */
            // @see SosDecoderv20#parseResultValues
            XmlCursor xbCursor = encodedValuesPropertyType.newCursor();
            xbCursor.toFirstContentToken();
            if (xbCursor.isText()) {
                values = xbCursor.getTextValue().trim();
                xbCursor.dispose();
                if (values != null && !values.isEmpty()) {
                    SweTextEncoding textEncoding = (SweTextEncoding) encoding;

                    String[] blocks = values.split(textEncoding.getBlockSeparator());
                    List<List<String>> resultValues = new ArrayList<>(blocks.length);
                    for (String block : blocks) {
                        String[] tokens = block.split(textEncoding.getTokenSeparator());
                        List<String> tokenList = Arrays.asList(tokens);
                        resultValues.add(tokenList);
                    }
                    return resultValues;
                }
            }
        }
        assert false;
        return null;
    }

    private boolean checkParameterTypes(SweAbstractDataComponent elementType, SweAbstractEncoding encoding)
            throws DecodingException {
        if (!(encoding instanceof SweTextEncoding)) {
            throw new NotYetSupportedDecodingException(SweConstants.EN_ENCODING_TYPE, encoding);
        }
        if (!(elementType instanceof SweDataRecord)) {
            throw new NotYetSupportedDecodingException(SweConstants.EN_ENCODING_TYPE, elementType);
        }
        return true;
    }

    private SweAbstractEncoding parseEncoding(AbstractEncodingType abstractEncodingType) throws DecodingException {
        if (abstractEncodingType instanceof TextEncodingType) {
            return parseTextEncoding((TextEncodingType) abstractEncodingType);
        }
        throw new NotYetSupportedDecodingException(SweConstants.EN_ENCODING_TYPE, abstractEncodingType,
                TextEncodingType.type.getName());
    }

    private SweDataRecord parseDataRecord(DataRecordType dataRecord) throws DecodingException {
        SweDataRecord sosSweDataRecord = new SweDataRecord();
        for (final Field field : dataRecord.getFieldArray()) {
            sosSweDataRecord.addField(
                    new SweField(field.getName(), parseAbstractDataComponent(field.getAbstractDataComponent())));
        }
        return sosSweDataRecord;
    }

    private SweBoolean parseBoolean(BooleanType xbBoolean) throws DecodingException {
        SweBoolean sosBoolean = new SweBoolean();
        if (xbBoolean.isSetValue()) {
            sosBoolean.setValue(xbBoolean.getValue());
        }
        if (xbBoolean.getQualityArray() != null) {
            sosBoolean.setQuality(parseQuality(xbBoolean.getQualityArray()));
        }
        return sosBoolean;
    }

    private SweCategory parseCategory(CategoryType xbCategory) throws DecodingException {
        SweCategory sosSweCategory = new SweCategory();
        if (xbCategory.isSetCodeSpace() && xbCategory.getCodeSpace().isSetHref()) {
            sosSweCategory.setCodeSpace(xbCategory.getCodeSpace().getHref());
        }
        if (xbCategory.isSetValue()) {
            sosSweCategory.setValue(xbCategory.getValue());
        }
        if (xbCategory.isSetConstraint()) {
            sosSweCategory.setConstraint(parseConstraint(xbCategory.getConstraint()));
        }
        if (xbCategory.getQualityArray() != null) {
            sosSweCategory.setQuality(parseQuality(xbCategory.getQualityArray()));
        }
        return sosSweCategory;
    }

    private SweCount parseCount(CountType count) throws DecodingException {
        final SweCount sosCount = new SweCount();
        if (count.getQualityArray() != null) {
            sosCount.setQuality(parseQuality(count.getQualityArray()));
        }
        if (count.isSetValue()) {
            sosCount.setValue(count.getValue().intValue());
        }
        if (count.isSetConstraint()) {
            sosCount.setConstraint(parseConstraint(count.getConstraint()));
        }
        return sosCount;
    }

    private SweCountRange parseCountRange(CountRangeType countRange) throws DecodingException {
        throw new UnsupportedDecoderInputException(this, countRange);
    }

    private SweQuantity parseQuantity(final QuantityType xbQuantity) throws DecodingException {
        final SweQuantity sosQuantity = new SweQuantity();
        if (xbQuantity.isSetAxisID()) {
            sosQuantity.setAxisID(xbQuantity.getAxisID());
        }
        if (xbQuantity.getQualityArray() != null) {
            sosQuantity.setQuality(parseQuality(xbQuantity.getQualityArray()));
        }

        if (xbQuantity.getUom() != null) {
            sosQuantity.setUom(parseUnitOfReference(xbQuantity.getUom()));
        }

        if (xbQuantity.isSetValue()) {
            sosQuantity.setValue(xbQuantity.getValue());
        }
        if (xbQuantity.isSetConstraint()) {
            sosQuantity.setConstraint(parseConstraint(xbQuantity.getConstraint()));
        }
        return sosQuantity;
    }

    private SweQuantityRange parseQuantityRange(final QuantityRangeType quantityRange) throws DecodingException {
        SweQuantityRange sweQuantityRange = new SweQuantityRange();
        if (quantityRange.isSetDefinition()) {
            sweQuantityRange.setDefinition(quantityRange.getDefinition());
        }
        if (quantityRange.isSetLabel()) {
            sweQuantityRange.setLabel(quantityRange.getLabel());
        }
        if (!quantityRange.getUom().isNil() && quantityRange.getUom().isSetCode()) {
            sweQuantityRange.setUom(parseUnitOfReference(quantityRange.getUom()));
        }
        if (quantityRange.getValue() != null) {
            sweQuantityRange.setValue(parseRangeValue(quantityRange.getValue()));
        }
        if (quantityRange.isSetConstraint()) {
            sweQuantityRange.setConstraint(parseConstraint(quantityRange.getConstraint()));
        }
        if (quantityRange.getQualityArray() != null) {
            sweQuantityRange.setQuality(parseQuality(quantityRange.getQualityArray()));
        }
        return sweQuantityRange;
    }

    private UoM parseUnitOfReference(UnitReference ur) {
        UoM uom = null;
        if (ur.isSetCode()) {
            uom = new UoM(ur.getCode());
        } else if (ur.isSetHref()) {
            uom = new UoM(ur.getHref());
        } else {
            uom = new UoM(OGCConstants.UNKNOWN);
        }
        if (ur.isSetHref()) {
            uom.setLink(ur.getHref());
        }
        if (ur.isSetTitle()) {
            uom.setName(ur.getTitle());
        }
        return uom;
    }

    private RangeValue<Double> parseRangeValue(List<?> value) throws DecodingException {
        if (value == null || value.isEmpty() || value.size() != 2) {
            throw new DecodingException("?:QuantityRange/?:value",
                    "The 'swe:value' element of an 'swe:QuantityRange' is not set correctly");
        }
        return new RangeValue<>(Double.parseDouble(value.get(0).toString()),
                Double.parseDouble(value.get(1).toString()));
    }

    private SweText parseText(final TextType xbText) {
        final SweText sosText = new SweText();
        if (xbText.isSetValue()) {
            sosText.setValue(xbText.getValue());
        }
        if (xbText.isSetConstraint()) {
            sosText.setConstraint(parseConstraint(xbText.getConstraint()));
        }
        return sosText;
    }

    private SweTime parseTime(TimeType xbTime) throws DecodingException {
        final SweTime sosTime = new SweTime();
        if (xbTime.isSetValue()) {
            sosTime.setValue(DateTimeHelper.parseIsoString2DateTime(xbTime.getValue().toString()));
        }
        if (xbTime.getUom() != null) {
            sosTime.setUom(parseUnitOfReference(xbTime.getUom()));
        }
        if (xbTime.isSetConstraint()) {
            sosTime.setConstraint(parseConstraint(xbTime.getConstraint()));
        }
        if (xbTime.getQualityArray() != null) {
            sosTime.setQuality(parseQuality(xbTime.getQualityArray()));
        }
        return sosTime;
    }

    private SweTimeRange parseTimeRange(TimeRangeType xbTime) throws DecodingException {
        SweTimeRange sosTimeRange = new SweTimeRange();
        if (xbTime.isSetValue()) {
            List<?> value = xbTime.getValue();
            if (value != null && !value.isEmpty()) {
                RangeValue<DateTime> range = new RangeValue<>();
                Iterator<?> iter = value.iterator();
                if (iter.hasNext()) {
                    range.setRangeStart(DateTimeHelper.parseIsoString2DateTime(iter.next().toString()));
                }
                if (iter.hasNext()) {
                    range.setRangeEnd(DateTimeHelper.parseIsoString2DateTime(iter.next().toString()));
                }
                sosTimeRange.setValue(range);
            }
        }
        if (xbTime.getUom() != null) {
            sosTimeRange.setUom(xbTime.getUom().getHref());
        }
        if (xbTime.isSetConstraint()) {
            sosTimeRange.setConstraint(parseConstraint(xbTime.getConstraint()));
        }
        if (xbTime.getQualityArray() != null) {
            sosTimeRange.setQuality(parseQuality(xbTime.getQualityArray()));
        }
        return sosTimeRange;
    }

    private Referenceable<SweAllowedValues> parseConstraint(AllowedValuesPropertyType avpt) {
        if (avpt.isSetAllowedValues()) {
            return Referenceable.of(parseAllowedValues(avpt.getAllowedValues()));
        } else {
            Reference ref = new Reference();
            if (avpt.isSetHref()) {
                ref.setHref(URI.create(avpt.getHref()));
            }
            if (avpt.isSetTitle()) {
                ref.setTitle(avpt.getTitle());
            }
            if (avpt.isSetActuate()) {
                ref.setActuate(avpt.getActuate().toString());
            }
            if (avpt.isSetArcrole()) {
                ref.setArcrole(avpt.getArcrole());
            }
            if (avpt.isSetRole()) {
                ref.setRole(avpt.getRole());
            }
            if (avpt.isSetShow()) {
                ref.setShow(avpt.getShow().toString());
            }
            if (avpt.isSetType()) {
                ref.setType(avpt.getType().toString());
            }
            return Referenceable.of(ref);
        }
    }

    private Referenceable<SweAllowedTokens> parseConstraint(AllowedTokensPropertyType atpt) {
        if (atpt.isSetAllowedTokens()) {
            return Referenceable.of(parseAllowedTokens(atpt.getAllowedTokens()));
        } else {
            Reference ref = new Reference();
            if (atpt.isSetHref()) {
                ref.setHref(URI.create(atpt.getHref()));
            }
            if (atpt.isSetTitle()) {
                ref.setTitle(atpt.getTitle());
            }
            if (atpt.isSetActuate()) {
                ref.setActuate(atpt.getActuate().toString());
            }
            if (atpt.isSetArcrole()) {
                ref.setArcrole(atpt.getArcrole());
            }
            if (atpt.isSetRole()) {
                ref.setRole(atpt.getRole());
            }
            if (atpt.isSetShow()) {
                ref.setShow(atpt.getShow().toString());
            }
            if (atpt.isSetType()) {
                ref.setType(atpt.getType().toString());
            }
            return Referenceable.of(ref);
        }
    }

    private Referenceable<SweAllowedTimes> parseConstraint(AllowedTimesPropertyType atpt)
            throws DateTimeParseException {
        if (atpt.isSetAllowedTimes()) {
            return Referenceable.of(parseAllowedTimes(atpt.getAllowedTimes()));
        } else {
            Reference ref = new Reference();
            if (atpt.isSetHref()) {
                ref.setHref(URI.create(atpt.getHref()));
            }
            if (atpt.isSetTitle()) {
                ref.setTitle(atpt.getTitle());
            }
            if (atpt.isSetActuate()) {
                ref.setActuate(atpt.getActuate().toString());
            }
            if (atpt.isSetArcrole()) {
                ref.setArcrole(atpt.getArcrole());
            }
            if (atpt.isSetRole()) {
                ref.setRole(atpt.getRole());
            }
            if (atpt.isSetShow()) {
                ref.setShow(atpt.getShow().toString());
            }
            if (atpt.isSetType()) {
                ref.setType(atpt.getType().toString());
            }
            return Referenceable.of(ref);
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private SweAllowedValues parseAllowedValues(AllowedValuesType avt) {
        SweAllowedValues allowedValues = new SweAllowedValues();
        if (avt.isSetId()) {
            allowedValues.setGmlId(avt.getId());
        }
        if (avt.getValueArray() != null && avt.getValueArray().length > 0) {
            for (double value : avt.getValueArray()) {
                allowedValues.addValue(value);
            }
        }
        if (CollectionHelper.isNotNullOrEmpty(avt.getIntervalArray())) {
            for (List interval : avt.getIntervalArray()) {
                RangeValue<Double> rangeValue = new RangeValue<Double>();
                Iterator<Double> iterator = interval.iterator();
                if (iterator.hasNext()) {
                    rangeValue.setRangeStart(iterator.next());
                }
                if (iterator.hasNext()) {
                    rangeValue.setRangeEnd(iterator.next());
                }
                allowedValues.addInterval(rangeValue);
            }
        }
        if (avt.isSetSignificantFigures()) {
            allowedValues.setSignificantFigures(avt.getSignificantFigures());
        }
        return allowedValues;
    }

    private SweAllowedTokens parseAllowedTokens(AllowedTokensType att) {
        SweAllowedTokens allowedTokens = new SweAllowedTokens();
        if (att.isSetId()) {
            allowedTokens.setGmlId(att.getId());
        }
        if (CollectionHelper.isNotNullOrEmpty(att.getValueArray())) {
            allowedTokens.setValue(Arrays.asList(att.getValueArray()));
        }
        if (att.isSetPattern()) {
            allowedTokens.setPattern(att.getPattern());
        }
        return allowedTokens;
    }

    @SuppressWarnings("rawtypes")
    private SweAllowedTimes parseAllowedTimes(AllowedTimesType att) throws DateTimeParseException {
        SweAllowedTimes allowedTimes = new SweAllowedTimes();
        if (att.isSetId()) {
            allowedTimes.setGmlId(att.getId());
        }
        if (CollectionHelper.isNotNullOrEmpty(att.getValueArray())) {
            for (Object value : att.getValueArray()) {
                allowedTimes.addValue(DateTimeHelper.parseIsoString2DateTime(value.toString()));
            }
        }
        if (CollectionHelper.isNotNullOrEmpty(att.getIntervalArray())) {
            for (List interval : att.getIntervalArray()) {
                RangeValue<DateTime> rangeValue = new RangeValue<DateTime>();
                Iterator iterator = interval.iterator();
                if (iterator.hasNext()) {
                    rangeValue.setRangeStart(DateTimeHelper.parseIsoString2DateTime(iterator.next().toString()));
                }
                if (iterator.hasNext()) {
                    rangeValue.setRangeEnd(DateTimeHelper.parseIsoString2DateTime(iterator.next().toString()));
                }
                allowedTimes.addInterval(rangeValue);
            }
        }
        if (att.isSetSignificantFigures()) {
            allowedTimes.setSignificantFigures(att.getSignificantFigures());
        }
        return allowedTimes;
    }

    private Collection<SweQuality> parseQuality(QualityPropertyType... qualityArray) throws DecodingException {
        if (qualityArray != null && qualityArray.length > 0) {
            final ArrayList<SweQuality> sosQualities = Lists.newArrayListWithCapacity(qualityArray.length);
            for (final QualityPropertyType quality : qualityArray) {
                if (quality.isSetQuantity()) {
                    sosQualities.add((SweQuality) parseQuantity(quality.getQuantity()));
                } else if (quality.isSetQuantityRange()) {
                    sosQualities.add((SweQuality) parseQuantityRange(quality.getQuantityRange()));
                } else if (quality.isSetCategory()) {
                    sosQualities.add((SweQuality) parseCategory(quality.getCategory()));
                } else if (quality.isSetText()) {
                    sosQualities.add((SweQuality) parseText(quality.getText()));
                }
            }
            return sosQualities;
        }
        return Collections.emptyList();
    }

    private SweAbstractDataComponent parseVector(VectorType vector) throws DecodingException {
        final SweVector sweVector = new SweVector();
        if (vector.isSetLocalFrame()) {
            sweVector.setLocalFrame(vector.getLocalFrame());
        }
        sweVector.setReferenceFrame(vector.getReferenceFrame());
        sweVector.setCoordinates(parseCoordinates(vector.getCoordinateArray()));
        return sweVector;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private List<SweCoordinate<?>> parseCoordinates(Coordinate[] coordinateArray) throws DecodingException {
        List<SweCoordinate<?>> sosCoordinates = new ArrayList<>(coordinateArray.length);
        for (final Coordinate xbCoordinate : coordinateArray) {
            // validate document
            XmlHelper.validateDocument(xbCoordinate);
            if (xbCoordinate.isSetQuantity()) {
                sosCoordinates.add(new SweCoordinate(xbCoordinate.getName(),
                        (SweAbstractSimpleType) parseAbstractDataComponent(xbCoordinate.getQuantity())));
            } else {
                throw new DecodingException(SweConstants.EN_POSITION,
                        "Error when parsing the Coordinates of Position: It must be of type Quantity!");
            }
        }
        return sosCoordinates;
    }

    private List<SweField> parseAnyScalarPropertyTypeArray(final AnyScalarPropertyType[] fieldArray)
            throws DecodingException {
        final List<SweField> sosFields = new ArrayList<>(fieldArray.length);
        for (final AnyScalarPropertyType xbField : fieldArray) {
            // validate document
            XmlHelper.validateDocument(xbField);
            /*
             * if (xbField.isSetBoolean()) { sosFields.add(new
             * SosSweField(xbField.getName(),
             * parseAbstractDataComponent(xbField.getBoolean()))); } else if
             * (xbField.isSetCategory()) { sosFields.add(new
             * SosSweField(xbField.getName(),
             * parseAbstractDataComponent(xbField.getCategory()))); } else if
             * (xbField.isSetCount()) { sosFields.add(new
             * SosSweField(xbField.getName(),
             * parseAbstractDataComponent(xbField.getCount()))); } else if
             * (xbField.isSetQuantity()) { sosFields.add(new
             * SosSweField(xbField.getName(),
             * parseAbstractDataComponent(xbField.getQuantity()))); } else if
             * (xbField.isSetText()) { sosFields.add(new
             * SosSweField(xbField.getName(),
             * parseAbstractDataComponent(xbField.getText()))); } else if
             * (xbField.isSetTime()) { sosFields.add(new
             * SosSweField(xbField.getName(),
             * parseAbstractDataComponent(xbField.getTime()))); }
             */

        }
        return sosFields;
    }

    private SweTextEncoding parseTextEncoding(final TextEncodingType textEncoding) {
        final SweTextEncoding sosTextEncoding = new SweTextEncoding();
        sosTextEncoding.setBlockSeparator(textEncoding.getBlockSeparator());
        sosTextEncoding.setTokenSeparator(textEncoding.getTokenSeparator());
        if (textEncoding.isSetDecimalSeparator()) {
            sosTextEncoding.setDecimalSeparator(textEncoding.getDecimalSeparator());
        }
        if (textEncoding.isSetCollapseWhiteSpaces()) {
            sosTextEncoding.setCollapseWhiteSpaces(textEncoding.getCollapseWhiteSpaces());
        }
        return sosTextEncoding;
    }

    private SweCount parseElementCount(final CountPropertyType elementCount) throws DecodingException {
        if (elementCount.isSetCount()) {
            return (SweCount) parseAbstractDataComponent(elementCount.getCount());
        }
        return null;
    }
}
