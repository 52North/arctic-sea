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

import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.xmlbeans.XmlObject;
import org.joda.time.DateTime;
import org.n52.shetland.ogc.sensorML.elements.SmlPosition;
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
import org.n52.shetland.ogc.swe.simpleType.SweQualityHolder;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;

import net.opengis.swe.x101.AbstractDataArrayType.ElementCount;
import net.opengis.swe.x101.AbstractDataComponentType;
import net.opengis.swe.x101.AbstractDataRecordDocument;
import net.opengis.swe.x101.AllowedTimesDocument.AllowedTimes;
import net.opengis.swe.x101.AllowedTimesPropertyType;
import net.opengis.swe.x101.AllowedTokensDocument.AllowedTokens;
import net.opengis.swe.x101.AllowedTokensPropertyType;
import net.opengis.swe.x101.AllowedValuesDocument.AllowedValues;
import net.opengis.swe.x101.AllowedValuesPropertyType;
import net.opengis.swe.x101.AnyScalarPropertyType;
import net.opengis.swe.x101.BlockEncodingPropertyType;
import net.opengis.swe.x101.BooleanDocument;
import net.opengis.swe.x101.CategoryDocument;
import net.opengis.swe.x101.CategoryDocument.Category;
import net.opengis.swe.x101.CountDocument;
import net.opengis.swe.x101.CountDocument.Count;
import net.opengis.swe.x101.CountRangeDocument;
import net.opengis.swe.x101.CountRangeDocument.CountRange;
import net.opengis.swe.x101.DataArrayDocument;
import net.opengis.swe.x101.DataArrayType;
import net.opengis.swe.x101.DataComponentPropertyType;
import net.opengis.swe.x101.DataRecordPropertyType;
import net.opengis.swe.x101.DataRecordType;
import net.opengis.swe.x101.EnvelopeType;
import net.opengis.swe.x101.ObservablePropertyDocument;
import net.opengis.swe.x101.ObservablePropertyDocument.ObservableProperty;
import net.opengis.swe.x101.PositionType;
import net.opengis.swe.x101.QualityPropertyType;
import net.opengis.swe.x101.QuantityDocument;
import net.opengis.swe.x101.QuantityDocument.Quantity;
import net.opengis.swe.x101.QuantityRangeDocument;
import net.opengis.swe.x101.QuantityRangeDocument.QuantityRange;
import net.opengis.swe.x101.SimpleDataRecordType;
import net.opengis.swe.x101.TextBlockDocument.TextBlock;
import net.opengis.swe.x101.TextDocument;
import net.opengis.swe.x101.TextDocument.Text;
import net.opengis.swe.x101.TimeDocument;
import net.opengis.swe.x101.TimeDocument.Time;
import net.opengis.swe.x101.TimeRangeDocument;
import net.opengis.swe.x101.TimeRangeDocument.TimeRange;
import net.opengis.swe.x101.VectorPropertyType;
import net.opengis.swe.x101.VectorType;
import net.opengis.swe.x101.VectorType.Coordinate;

/**
 * @since 1.0.0
 *
 */
public class SweCommonDecoderV101
        extends AbstractXmlDecoder<Object, Object> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SweCommonDecoderV101.class);

    private static final Set<DecoderKey> DECODER_KEYS = CodingHelper.decoderKeysForElements(SweConstants.NS_SWE_101,
            net.opengis.swe.x101.DataArrayDocument.class, net.opengis.swe.x101.DataArrayType.class,
            net.opengis.swe.x101.AbstractDataComponentType.class, net.opengis.swe.x101.BooleanDocument.class,
            net.opengis.swe.x101.BooleanDocument.Boolean.class, net.opengis.swe.x101.CategoryDocument.Category.class,
            net.opengis.swe.x101.CategoryDocument.class, net.opengis.swe.x101.CountDocument.Count.class,
            net.opengis.swe.x101.CountDocument.class, net.opengis.swe.x101.CountRangeDocument.CountRange.class,
            net.opengis.swe.x101.CountRangeDocument.class,
            net.opengis.swe.x101.ObservablePropertyDocument.ObservableProperty.class,
            net.opengis.swe.x101.ObservablePropertyDocument.class,
            net.opengis.swe.x101.QuantityDocument.Quantity.class, net.opengis.swe.x101.QuantityDocument.class,
            net.opengis.swe.x101.QuantityRangeDocument.QuantityRange.class,
            net.opengis.swe.x101.QuantityRangeDocument.class, net.opengis.swe.x101.TextDocument.Text.class,
            net.opengis.swe.x101.TextDocument.class, net.opengis.swe.x101.TimeDocument.Time.class,
            net.opengis.swe.x101.TimeDocument.class, net.opengis.swe.x101.TimeRangeDocument.TimeRange.class,
            net.opengis.swe.x101.TimeRangeDocument.class, net.opengis.swe.x101.DataComponentPropertyType[].class,
            net.opengis.swe.x101.PositionType.class, net.opengis.swe.x101.VectorType.Coordinate[].class,
            net.opengis.swe.x101.AnyScalarPropertyType[].class, net.opengis.swe.x101.AbstractDataRecordDocument.class,
            net.opengis.swe.x101.AbstractDataRecordType.class);

    public SweCommonDecoderV101() {
        LOGGER.debug("Decoder for the following keys initialized successfully: {}!",
                Joiner.on(", ").join(DECODER_KEYS));
    }

    @Override
    public Set<DecoderKey> getKeys() {
        return Collections.unmodifiableSet(DECODER_KEYS);
    }

    @Override
    public Object decode(Object element) throws DecodingException {
        if (element instanceof DataArrayDocument) {
            return parseAbstractDataComponentType(((DataArrayDocument) element).getDataArray1());
        } else if (element instanceof AbstractDataComponentType) {
            return parseAbstractDataComponentType((AbstractDataComponentType) element);
        } else if (element instanceof BooleanDocument) {
            return parseAbstractDataComponentType(((BooleanDocument) element).getBoolean());
        } else if (element instanceof CategoryDocument) {
            return parseAbstractDataComponentType(((CategoryDocument) element).getCategory());
        } else if (element instanceof CountDocument) {
            return parseAbstractDataComponentType(((CountDocument) element).getCount());
        } else if (element instanceof CountRangeDocument) {
            return parseAbstractDataComponentType(((CountRangeDocument) element).getCountRange());
        } else if (element instanceof ObservablePropertyDocument) {
            return parseAbstractDataComponentType(((ObservablePropertyDocument) element).getObservableProperty());
        } else if (element instanceof QuantityDocument) {
            return parseAbstractDataComponentType(((QuantityDocument) element).getQuantity());
        } else if (element instanceof QuantityRangeDocument) {
            return parseAbstractDataComponentType(((QuantityRangeDocument) element).getQuantityRange());
        } else if (element instanceof TextDocument) {
            return parseAbstractDataComponentType(((TextDocument) element).getText());
        } else if (element instanceof TimeDocument) {
            return parseAbstractDataComponentType(((TimeDocument) element).getTime());
        } else if (element instanceof TimeRangeDocument) {
            return parseAbstractDataComponentType(((TimeRangeDocument) element).getTimeRange());
        } else if (element instanceof DataComponentPropertyType[]) {
            return parseDataComponentPropertyArray((DataComponentPropertyType[]) element);
        } else if (element instanceof Coordinate[]) {
            return parseCoordinates((Coordinate[]) element);
        } else if (element instanceof AnyScalarPropertyType[]) {
            return parseAnyScalarPropertyArray((AnyScalarPropertyType[]) element);
        } else if (element instanceof AbstractDataRecordDocument) {
            return parseAbstractDataComponentType(((AbstractDataRecordDocument) element).getAbstractDataRecord());
        } else if (element instanceof XmlObject) {
            throw new UnsupportedDecoderXmlInputException(this, (XmlObject) element);
        } else {
            throw new UnsupportedDecoderInputException(this, element);
        }
    }

    private SweAbstractDataComponent parseAbstractDataComponentType(
            final AbstractDataComponentType abstractDataComponent) throws DecodingException {
        SweAbstractDataComponent component = null;
        if (abstractDataComponent instanceof net.opengis.swe.x101.BooleanDocument.Boolean) {
            component = parseBoolean((net.opengis.swe.x101.BooleanDocument.Boolean) abstractDataComponent);
        } else if (abstractDataComponent instanceof Category) {
            component = parseCategory((Category) abstractDataComponent);
        } else if (abstractDataComponent instanceof Count) {
            component = parseCount((Count) abstractDataComponent);
        } else if (abstractDataComponent instanceof CountRange) {
            component = parseCountRange((CountRange) abstractDataComponent);
        } else if (abstractDataComponent instanceof ObservableProperty) {
            component = parseObservableProperty((ObservableProperty) abstractDataComponent);
        } else if (abstractDataComponent instanceof Quantity) {
            component = parseQuantity((Quantity) abstractDataComponent);
        } else if (abstractDataComponent instanceof QuantityRange) {
            component = parseQuantityRange((QuantityRange) abstractDataComponent);
        } else if (abstractDataComponent instanceof Text) {
            component = parseText((Text) abstractDataComponent);
        } else if (abstractDataComponent instanceof Time) {
            component = parseTime((Time) abstractDataComponent);
        } else if (abstractDataComponent instanceof TimeRange) {
            component = parseTimeRange((TimeRange) abstractDataComponent);
        } else if (abstractDataComponent instanceof PositionType) {
            component = parsePosition((PositionType) abstractDataComponent);
        } else if (abstractDataComponent instanceof DataRecordPropertyType) {
            component = parseDataRecordProperty((DataRecordPropertyType) abstractDataComponent);
        } else if (abstractDataComponent instanceof SimpleDataRecordType) {
            component = parseSimpleDataRecord((SimpleDataRecordType) abstractDataComponent);
        } else if (abstractDataComponent instanceof DataArrayType) {
            component = parseSweDataArrayType((DataArrayType) abstractDataComponent);
        } else if (abstractDataComponent instanceof DataRecordType) {
            component = parseDataRecord((DataRecordType) abstractDataComponent);
        } else if (abstractDataComponent instanceof EnvelopeType) {
            component = parseEnvelope((EnvelopeType) abstractDataComponent);
        } else {
            throw new UnsupportedDecoderXmlInputException(this, abstractDataComponent);
        }
        if (abstractDataComponent.isSetDefinition()) {
            component.setDefinition(abstractDataComponent.getDefinition());
        }
        if (abstractDataComponent.isSetDescription()) {
            component.setDescription(abstractDataComponent.getDescription().getStringValue());
        }
        return component;
    }

    // private SosSweAbstractDataComponent
    // parseAbstractDataRecord(AbstractDataRecordType abstractDataRecord) throws
    // DecodingException {
    // if (abstractDataRecord instanceof DataRecordPropertyType) {
    // return parseDataRecordProperty((DataRecordPropertyType)
    // abstractDataRecord);
    // } else if (abstractDataRecord instanceof SimpleDataRecordType) {
    // return parseSimpleDataRecord((SimpleDataRecordType) abstractDataRecord);
    // }
    // return null;
    // }
    private SweDataRecord parseDataRecordProperty(final DataRecordPropertyType dataRecordProperty)
            throws DecodingException {
        final DataRecordType dataRecord = dataRecordProperty.getDataRecord();
        return parseDataRecord(dataRecord);
    }

    private SweDataRecord parseDataRecord(final DataRecordType dataRecord) throws DecodingException {
        final SweDataRecord sosDataRecord = new SweDataRecord();
        if (dataRecord.getFieldArray() != null) {
            sosDataRecord.setFields(parseDataComponentPropertyArray(dataRecord.getFieldArray()));
        }
        return sosDataRecord;
    }

    private SweAbstractDataComponent parseEnvelope(EnvelopeType envelopeType) throws DecodingException {

        String referenceFrame = null;
        SweVector lowerCorner = null;
        SweVector upperCorner = null;
        SweTimeRange time = null;

        if (envelopeType.isSetReferenceFrame()) {
            referenceFrame = envelopeType.getReferenceFrame();
        }
        if (envelopeType.getLowerCorner() != null) {
            lowerCorner = parseVectorProperty(envelopeType.getLowerCorner());
        }
        if (envelopeType.getUpperCorner() != null) {
            upperCorner = parseVectorProperty(envelopeType.getUpperCorner());
        }
        if (envelopeType.isSetTime()) {
            time = parseTimeRange(envelopeType.getTime().getTimeRange());
        }

        // FIXME get the northing first value for the reference frame
        boolean northingFirst = false;

        return new SweEnvelope(referenceFrame, upperCorner, lowerCorner, time, northingFirst);
    }

    private SweVector parseVectorProperty(VectorPropertyType vectorPropertyType) throws DecodingException {
        return parseVector(vectorPropertyType.getVector());
    }

    private SweVector parseVector(VectorType vectorType) throws DecodingException {
        return new SweVector(parseCoordinates(vectorType.getCoordinateArray()));
    }

    private SweSimpleDataRecord parseSimpleDataRecord(SimpleDataRecordType simpleDataRecord) throws DecodingException {
        SweSimpleDataRecord sosSimpleDataRecord = new SweSimpleDataRecord();
        if (simpleDataRecord.getFieldArray() != null) {
            sosSimpleDataRecord.setFields(parseAnyScalarPropertyArray(simpleDataRecord.getFieldArray()));
        }
        return sosSimpleDataRecord;
    }

    private SweDataArray parseSweDataArrayType(DataArrayType xbDataArray) throws DecodingException {
        if (!xbDataArray.getElementType().isSetAbstractDataRecord()) {
            throw new DecodingException("The swe:DataArray contains a not yet supported elementType element. "
                    + "Currently only 'swe:DataRecord' is supported as elementType element.");
        }
        final SweDataArray dataArray = new SweDataArray();
        if (xbDataArray.getElementCount() != null) {
            dataArray.setElementCount(parseElementCount(xbDataArray.getElementCount()));
        }
        // parse data record to elementType
        DataComponentPropertyType elementType = xbDataArray.getElementType();
        if (elementType != null) {
            dataArray.setElementType(parseDataComponentProperty(elementType));
        }
        if (xbDataArray.isSetEncoding()) {
            dataArray.setEncoding(parseEncoding(xbDataArray.getEncoding()));
        }

        // parse values
        // if (xbDataArray.isSetValues()) {
        // // TODO implement full support
        // // dataArray.setValues(parseValues(dataArray.getElementCount(),
        // // dataArray.getElementType(),
        // // dataArray.getEncoding(), xbDataArray.getValues()));
        // }
        DataArrayDocument xbDataArrayDoc = DataArrayDocument.Factory.newInstance(getXmlOptions());
        xbDataArrayDoc.setDataArray1(xbDataArray);
        dataArray.setXml(xbDataArrayDoc.xmlText(getXmlOptions()));
        return dataArray;
    }

    private List<SweField> parseDataComponentPropertyArray(DataComponentPropertyType[] fieldArray)
            throws DecodingException {
        List<SweField> sosFields = new ArrayList<>(fieldArray.length);
        for (DataComponentPropertyType xbField : fieldArray) {
            sosFields.add(new SweField(xbField.getName(), parseDataComponentProperty(xbField)));
        }
        return sosFields;
    }

    private SweAbstractSimpleType<Boolean> parseBoolean(net.opengis.swe.x101.BooleanDocument.Boolean xbBoolean)
            throws DecodingException {
        SweBoolean sosBoolean = new SweBoolean();
        if (xbBoolean.isSetDefinition()) {
            sosBoolean.setDefinition(xbBoolean.getDefinition());
        }
        if (xbBoolean.isSetDescription()) {
            sosBoolean.setDescription(xbBoolean.getDescription().getStringValue());
        }
        if (xbBoolean.isSetValue()) {
            sosBoolean.setValue(xbBoolean.getValue());
        }
        if (xbBoolean.isSetQuality()) {
            sosBoolean.setQuality(parseQuality(xbBoolean.getQuality()));
        }
        return sosBoolean;
    }

    private SweCategory parseCategory(Category category) throws DecodingException {
        SweCategory sosCategory = new SweCategory();
        if (category.isSetValue()) {
            sosCategory.setValue(category.getValue());
        }
        if (category.isSetCodeSpace()) {
            sosCategory.setCodeSpace(category.getCodeSpace().getHref());
        }
        if (category.isSetConstraint()) {
            sosCategory.setConstraint(parseConstraint(category.getConstraint()));
        }
        if (category.isSetQuality()) {
            sosCategory.setQuality(parseQuality(category.getQuality()));
        }
        return sosCategory;
    }

    private SweCount parseCount(Count xbCount) throws DecodingException {
        SweCount sosCount = new SweCount();
        if (xbCount.getQualityArray() != null) {
            sosCount.setQuality(parseQuality(xbCount.getQualityArray()));
        }
        if (xbCount.isSetValue()) {
            sosCount.setValue(xbCount.getValue().intValue());
        }
        if (xbCount.isSetConstraint()) {
            sosCount.setConstraint(parseConstraint(xbCount.getConstraint()));
        }
        return sosCount;
    }

    private SweCountRange parseCountRange(CountRange xbCountRange) throws DecodingException {
        SweCountRange sosCountRange = new SweCountRange();

        // if (xbCountRange.isSetAxisID()) {
        // // TODO axisID
        // }
        if (xbCountRange.getQualityArray() != null) {
            sosCountRange.setQuality(parseQuality(xbCountRange.getQualityArray()));
        }
        // if (xbCountRange.isSetReferenceFrame()) {
        // // TODO reference frame
        // }
        if (xbCountRange.isSetDefinition()) {
            sosCountRange.setDefinition(xbCountRange.getDefinition());
        }
        if (xbCountRange.isSetDescription()) {
            sosCountRange.setDescription(xbCountRange.getDescription().getStringValue());
        }
        if (xbCountRange.isSetValue()) {
            List<?> value = xbCountRange.getValue();
            Integer rangeStart = Integer.parseInt(value.get(0).toString());
            Integer rangeEnd = Integer.parseInt(value.get(1).toString());
            sosCountRange.setValue(new RangeValue<>(rangeStart, rangeEnd));
        }
        return sosCountRange;
    }

    private SweObservableProperty parseObservableProperty(ObservableProperty observableProperty) {
        return new SweObservableProperty();
    }

    private SweQuantity parseQuantity(Quantity xbQuantity) throws DecodingException {
        SweQuantity sosQuantity = new SweQuantity();
        if (xbQuantity.isSetAxisID()) {
            sosQuantity.setAxisID(xbQuantity.getAxisID());
        }
        if (xbQuantity.getQualityArray() != null) {
            sosQuantity.setQuality(parseQuality(xbQuantity.getQualityArray()));
        }
        if (xbQuantity.isSetUom() && xbQuantity.getUom().isSetCode()) {
            sosQuantity.setUom(xbQuantity.getUom().getCode());
        }
        if (xbQuantity.isSetConstraint()) {
            sosQuantity.setConstraint(parseConstraint(xbQuantity.getConstraint()));
        }
        if (xbQuantity.isSetValue()) {
            sosQuantity.setValue(xbQuantity.getValue());
        }
        return sosQuantity;
    }

    private SweQuantityRange parseQuantityRange(QuantityRange xbQuantityRange) throws DecodingException {
        SweQuantityRange sosQuantityRange = new SweQuantityRange();
        if (xbQuantityRange.isSetAxisID()) {
            sosQuantityRange.setAxisID(xbQuantityRange.getAxisID());
        }
        if (xbQuantityRange.isSetDefinition()) {
            sosQuantityRange.setDefinition(xbQuantityRange.getDefinition());
        }
        if (xbQuantityRange.isSetDescription()) {
            sosQuantityRange.setDescription(xbQuantityRange.getDescription().getStringValue());
        }
        if (xbQuantityRange.isSetUom() && xbQuantityRange.getUom().isSetCode()) {
            sosQuantityRange.setUom(xbQuantityRange.getUom().getCode());
        }
        if (xbQuantityRange.isSetConstraint()) {
            sosQuantityRange.setConstraint(parseConstraint(xbQuantityRange.getConstraint()));
        }
        if (xbQuantityRange.isSetValue()) {
            try {
                List<?> value = xbQuantityRange.getValue();
                if (value != null && value.size() == 2 && value.get(0) != null && value.get(1) != null) {
                    BigDecimal rangeStart = new BigDecimal(value.get(0).toString());
                    BigDecimal rangeEnd = new BigDecimal(value.get(1).toString());
                    sosQuantityRange.setValue(new RangeValue<>(rangeStart, rangeEnd));
                }
            } catch (final NumberFormatException | IndexOutOfBoundsException nfe) {
                throw createParsingException(nfe);
            }
        }
        if (xbQuantityRange.isSetConstraint()) {
            LOGGER.error("Decoding of swe:QuantityRange/swe:constraint is not implemented");
        }
        if (xbQuantityRange.getQualityArray() != null && xbQuantityRange.getQualityArray().length > 0) {
            LOGGER.error("Decoding of swe:QuantityRange/swe:quality is not implemented");
        }
        return sosQuantityRange;
    }

    private DecodingException createParsingException(final Exception e) {
        return new DecodingException(e, "QuantityRange",
                "Error when parsing 'swe:QuantityRange/swe:value': It must be of type 'double double!");
    }

    private SweText parseText(Text xbText) {
        final SweText sosText = new SweText();
        if (xbText.isSetValue()) {
            sosText.setValue(xbText.getValue());
        }
        return sosText;
    }

    private SweTime parseTime(Time time) throws DecodingException {
        SweTime sosTime = new SweTime();
        if (time.isSetConstraint()) {
            sosTime.setConstraint(parseConstraint(time.getConstraint()));
        }
        if (time.isSetValue()) {
            sosTime.setValue(DateTimeHelper.parseIsoString2DateTime(time.getValue().toString()));
        }
        if (time.getUom() != null) {
            sosTime.setUom(time.getUom().getHref());
        }
        return sosTime;
    }

    private SweTimeRange parseTimeRange(TimeRange timeRange) throws DecodingException {
        SweTimeRange sosTimeRange = new SweTimeRange();
        if (timeRange.isSetValue()) {
            RangeValue<DateTime> range = new RangeValue<>();
            Iterator<?> iter = timeRange.getValue().iterator();

            if (iter.hasNext()) {
                range.setRangeStart(DateTimeHelper.parseIsoString2DateTime(iter.next().toString()));

                while (iter.hasNext()) {
                    range.setRangeEnd(DateTimeHelper.parseIsoString2DateTime(iter.next().toString()));
                }
            }
            sosTimeRange.setValue(range);
        }
        if (timeRange.getUom() != null) {
            sosTimeRange.setUom(timeRange.getUom().getHref());
        }
        if (timeRange.isSetConstraint()) {
            sosTimeRange.setConstraint(parseConstraint(timeRange.getConstraint()));
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

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private SweAllowedValues parseAllowedValues(AllowedValues avt) {
        SweAllowedValues allowedValues = new SweAllowedValues();
        if (avt.isSetId()) {
            allowedValues.setGmlId(avt.getId());
        }
        if (CollectionHelper.isNotNullOrEmpty(avt.getValueListArray())) {
            for (List list : avt.getValueListArray()) {
                if (CollectionHelper.isNotEmpty(list)) {
                    for (Object value : list) {
                        allowedValues.addValue(Double.parseDouble(value.toString()));
                    }
                }
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
        return allowedValues;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private SweAllowedTokens parseAllowedTokens(AllowedTokens att) {
        SweAllowedTokens allowedTokens = new SweAllowedTokens();
        if (att.isSetId()) {
            allowedTokens.setGmlId(att.getId());
        }
        if (CollectionHelper.isNotNullOrEmpty(att.getValueListArray())) {
            for (List list : att.getValueListArray()) {
                if (CollectionHelper.isNotEmpty(list)) {
                    allowedTokens.setValue(list);
                }
            }
        }
        return allowedTokens;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private SweAllowedTimes parseAllowedTimes(AllowedTimes att) throws DateTimeParseException {
        SweAllowedTimes allowedTimes = new SweAllowedTimes();
        if (att.isSetId()) {
            allowedTimes.setGmlId(att.getId());
        }
        if (CollectionHelper.isNotNullOrEmpty(att.getValueListArray())) {
            for (List list : att.getValueListArray()) {
                if (CollectionHelper.isNotEmpty(list)) {
                    for (Object value : list) {
                        allowedTimes.addValue(DateTimeHelper.parseIsoString2DateTime(value.toString()));
                    }
                }
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
        return allowedTimes;
    }

    private SweQualityHolder parseQuality(QualityPropertyType... qualityArray) throws DecodingException {
        if (qualityArray != null && qualityArray.length > 0) {
            SweQualityHolder sweQualityHolder = new SweQualityHolder();
            ArrayList<SweQuality> sosQualities = new ArrayList<>(qualityArray.length);
            for (QualityPropertyType quality : qualityArray) {
                parseQualityPropertyType(quality).ifPresent(sosQualities::add);
            }
            sweQualityHolder.setQuality(sosQualities);
            return sweQualityHolder;
        }
        return null;
    }

    private SmlPosition parsePosition(PositionType position) throws DecodingException {
        final SmlPosition sosSMLPosition = new SmlPosition();
        if (position.isSetReferenceFrame()) {
            sosSMLPosition.setReferenceFrame(position.getReferenceFrame());
        }
        if (position.isSetLocation() && position.getLocation().isSetVector()) {
            if (position.getLocation().getVector().isSetReferenceFrame()) {
                sosSMLPosition.setReferenceFrame(position.getLocation().getVector().getReferenceFrame());
            }
            sosSMLPosition.setPosition(parseCoordinates(position.getLocation().getVector().getCoordinateArray()));
        }
        return sosSMLPosition;
    }

    private List<SweCoordinate<?>> parseCoordinates(Coordinate[] coordinateArray) throws DecodingException {
        List<SweCoordinate<?>> sosCoordinates = new ArrayList<>(coordinateArray.length);
        for (Coordinate xbCoordinate : coordinateArray) {
            if (xbCoordinate.isSetQuantity()) {
                sosCoordinates
                        .add(new SweCoordinate<>(xbCoordinate.getName(), parseQuantity(xbCoordinate.getQuantity())));
            } else {
                throw new DecodingException("Position",
                        "Error when parsing the Coordinates of Position: It must be of type Quantity!");
            }
        }
        return sosCoordinates;
    }

    private List<SweField> parseAnyScalarPropertyArray(AnyScalarPropertyType[] fieldArray) throws DecodingException {
        List<SweField> sosFields = new ArrayList<>(fieldArray.length);
        for (AnyScalarPropertyType xbField : fieldArray) {
            sosFields.add(new SweField(xbField.getName(), parseAnyScalarProperty(xbField)));
        }
        return sosFields;
    }

    private SweCount parseElementCount(ElementCount elementCount) throws DecodingException {
        if (elementCount.isSetCount()) {
            return parseCount(elementCount.getCount());
        }
        return null;
    }

    private SweAbstractEncoding parseEncoding(BlockEncodingPropertyType abstractEncodingType)
            throws DecodingException {
        if (abstractEncodingType.isSetTextBlock()) {
            return parseTextEncoding(abstractEncodingType.getTextBlock());
        }
        throw new NotYetSupportedDecodingException(SweConstants.EN_ENCODING_TYPE, abstractEncodingType,
                TextBlock.type.getName());
    }

    private SweTextEncoding parseTextEncoding(TextBlock textEncoding) {
        final SweTextEncoding sosTextEncoding = new SweTextEncoding();
        sosTextEncoding.setBlockSeparator(textEncoding.getBlockSeparator());
        sosTextEncoding.setTokenSeparator(textEncoding.getTokenSeparator());
        sosTextEncoding.setDecimalSeparator(textEncoding.getDecimalSeparator());
        return sosTextEncoding;
    }

    private SweAbstractDataComponent parseAnyScalarProperty(AnyScalarPropertyType xbField) throws DecodingException {
        if (xbField.isSetBoolean()) {
            return parseAbstractDataComponentType(xbField.getBoolean());
        } else if (xbField.isSetCategory()) {
            return parseAbstractDataComponentType(xbField.getCategory());
        } else if (xbField.isSetCount()) {
            return parseAbstractDataComponentType(xbField.getCount());
        } else if (xbField.isSetQuantity()) {
            return parseAbstractDataComponentType(xbField.getQuantity());
        } else if (xbField.isSetText()) {
            return parseAbstractDataComponentType(xbField.getText());
        } else if (xbField.isSetTime()) {
            return parseAbstractDataComponentType(xbField.getTime());
        } else {
            throw new UnsupportedDecoderXmlInputException(this, xbField);
        }

    }

    private SweAbstractDataComponent parseDataComponentProperty(DataComponentPropertyType xbField)
            throws DecodingException {
        if (xbField.isSetBoolean()) {
            return parseAbstractDataComponentType(xbField.getBoolean());
        } else if (xbField.isSetCategory()) {
            return parseAbstractDataComponentType(xbField.getCategory());
        } else if (xbField.isSetCount()) {
            return parseAbstractDataComponentType(xbField.getCount());
        } else if (xbField.isSetCountRange()) {
            return parseAbstractDataComponentType(xbField.getCountRange());
        } else if (xbField.isSetQuantity()) {
            return parseAbstractDataComponentType(xbField.getQuantity());
        } else if (xbField.isSetQuantityRange()) {
            return parseAbstractDataComponentType(xbField.getQuantityRange());
        } else if (xbField.isSetText()) {
            return parseAbstractDataComponentType(xbField.getText());
        } else if (xbField.isSetTime()) {
            return parseAbstractDataComponentType(xbField.getTime());
        } else if (xbField.isSetTimeRange()) {
            return parseAbstractDataComponentType(xbField.getTimeRange());
        } else if (xbField.isSetAbstractDataRecord()) {
            return parseAbstractDataComponentType(xbField.getAbstractDataRecord());
        } else if (xbField.isSetAbstractDataArray1()) {
            return parseAbstractDataComponentType(xbField.getAbstractDataArray1());
        } else {
            throw new UnsupportedDecoderXmlInputException(this, xbField);
        }
    }

    private Optional<SweQuality> parseQualityPropertyType(QualityPropertyType quality) throws DecodingException {
        if (quality.isSetQuantity()) {
            return Optional.of(parseQuantity(quality.getQuantity()));
        } else if (quality.isSetQuantityRange()) {
            return Optional.of(parseQuantityRange(quality.getQuantityRange()));
        } else if (quality.isSetCategory()) {
            return Optional.of(parseCategory(quality.getCategory()));
        } else if (quality.isSetText()) {
            return Optional.of(parseText(quality.getText()));
        }
        return Optional.empty();
    }
}
