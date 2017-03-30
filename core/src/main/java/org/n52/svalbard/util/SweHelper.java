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
package org.n52.svalbard.util;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.faroe.ConfigurationError;
import org.n52.faroe.Validation;
import org.n52.faroe.annotation.Configurable;
import org.n52.faroe.annotation.Setting;
import org.n52.shetland.ogc.gml.time.Time;
import org.n52.shetland.ogc.gml.time.TimePeriod;
import org.n52.shetland.ogc.om.AbstractObservationValue;
import org.n52.shetland.ogc.om.MultiObservationValues;
import org.n52.shetland.ogc.om.OmConstants;
import org.n52.shetland.ogc.om.OmObservation;
import org.n52.shetland.ogc.om.SingleObservationValue;
import org.n52.shetland.ogc.om.TimeValuePair;
import org.n52.shetland.ogc.om.values.BooleanValue;
import org.n52.shetland.ogc.om.values.CategoryValue;
import org.n52.shetland.ogc.om.values.ComplexValue;
import org.n52.shetland.ogc.om.values.CountValue;
import org.n52.shetland.ogc.om.values.GeometryValue;
import org.n52.shetland.ogc.om.values.HrefAttributeValue;
import org.n52.shetland.ogc.om.values.NilTemplateValue;
import org.n52.shetland.ogc.om.values.QuantityValue;
import org.n52.shetland.ogc.om.values.ReferenceValue;
import org.n52.shetland.ogc.om.values.SweDataArrayValue;
import org.n52.shetland.ogc.om.values.TVPValue;
import org.n52.shetland.ogc.om.values.TextValue;
import org.n52.shetland.ogc.om.values.UnknownValue;
import org.n52.shetland.ogc.om.values.Value;
import org.n52.shetland.ogc.om.values.visitor.ValueVisitor;
import org.n52.shetland.ogc.swe.SweAbstractDataComponent;
import org.n52.shetland.ogc.swe.SweDataArray;
import org.n52.shetland.ogc.swe.SweDataRecord;
import org.n52.shetland.ogc.swe.SweField;
import org.n52.shetland.ogc.swe.encoding.SweAbstractEncoding;
import org.n52.shetland.ogc.swe.encoding.SweTextEncoding;
import org.n52.shetland.ogc.swe.simpleType.SweAbstractUomType;
import org.n52.shetland.ogc.swe.simpleType.SweBoolean;
import org.n52.shetland.ogc.swe.simpleType.SweCategory;
import org.n52.shetland.ogc.swe.simpleType.SweCount;
import org.n52.shetland.ogc.swe.simpleType.SweObservableProperty;
import org.n52.shetland.ogc.swe.simpleType.SweQuantity;
import org.n52.shetland.ogc.swe.simpleType.SweText;
import org.n52.shetland.ogc.swe.simpleType.SweTime;
import org.n52.shetland.ogc.swe.simpleType.SweTimeRange;
import org.n52.shetland.util.DateTimeHelper;
import org.n52.shetland.util.JavaHelper;
import org.n52.svalbard.CodingSettings;
import org.n52.svalbard.encode.exception.EncodingException;

import com.google.common.base.Strings;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * SWE helper class.
 *
 * @since 4.0.0
 *
 */
@Configurable
public final class SweHelper {

    private final Logger LOGGER = LoggerFactory.getLogger(SweHelper.class);

    private String tokenSeparator;

    private String tupleSeparator;

    private String decimalSeparator;

    @Setting(CodingSettings.TOKEN_SEPARATOR)
    public void setTokenSeparator(final String separator) throws ConfigurationError {
        Validation.notNullOrEmpty("Token separator", separator);
        tokenSeparator = separator;
    }

    @Setting(CodingSettings.TUPLE_SEPARATOR)
    public void setTupleSeparator(final String separator) throws ConfigurationError {
        Validation.notNullOrEmpty("Tuple separator", separator);
        tupleSeparator = separator;
    }

    @Setting(CodingSettings.DECIMAL_SEPARATOR)
    public void setDecimalSeparator(final String separator) throws ConfigurationError {
        Validation.notNullOrEmpty("Decimal separator", separator);
        decimalSeparator = separator;
    }

    /**
     * Create {@link SweDataArray} from {@link OmObservation}
     *
     * @param sosObservation The {@link OmObservation} to create {@link SweDataArray} from
     *
     * @return Created {@link SweDataArray}
     *
     * @throws EncodingException If the service does not support the {@link SweDataArray} creation from value of
     *                           {@link OmObservation}
     */
    public SweDataArray createSosSweDataArray(OmObservation sosObservation) throws EncodingException {
        String observablePropertyIdentifier = sosObservation.getObservationConstellation().getObservableProperty()
                .getIdentifier();
        SweDataArrayValue dataArrayValue = new SweDataArrayValue();
        SweDataArray dataArray = new SweDataArray();
        dataArray.setEncoding(createTextEncoding(sosObservation));
        dataArrayValue.setValue(dataArray);
        if (sosObservation.getValue() instanceof SingleObservationValue) {
            SingleObservationValue<?> singleValue = (SingleObservationValue<?>) sosObservation.getValue();
            if (singleValue.getValue() instanceof SweDataArrayValue) {
                return (SweDataArray) singleValue.getValue().getValue();
            } else {
                dataArray.setElementType(createElementType(singleValue, observablePropertyIdentifier));
                dataArrayValue.addBlock(createBlock(dataArray.getElementType(), sosObservation.getPhenomenonTime(),
                                                    observablePropertyIdentifier, singleValue.getValue()));
            }
        } else if (sosObservation.getValue() instanceof MultiObservationValues) {
            MultiObservationValues<?> multiValue = (MultiObservationValues<?>) sosObservation.getValue();
            if (multiValue.getValue() instanceof SweDataArrayValue) {
                return ((SweDataArrayValue) multiValue.getValue()).getValue();
            } else if (multiValue.getValue() instanceof TVPValue) {
                TVPValue tvpValues = (TVPValue) multiValue.getValue();
                for (TimeValuePair timeValuePair : tvpValues.getValue()) {
                    if (!dataArray.isSetElementTyp()) {
                        dataArray.setElementType(createElementType(timeValuePair, observablePropertyIdentifier));
                    }
                    List<String> newBlock = createBlock(dataArray.getElementType(), timeValuePair.getTime(),
                                                        observablePropertyIdentifier, timeValuePair.getValue());
                    dataArrayValue.addBlock(newBlock);
                }
            }
        }
        return dataArray;
    }

    /**
     * Create {@link SweDataArray} from {@link AbstractObservationValue}
     *
     * @param observationValue The {@link AbstractObservationValue} to create {@link SweDataArray} from
     *
     * @return Created {@link SweDataArray}
     *
     * @throws EncodingException If the service does not support the {@link SweDataArray} creation from
     *                           {@link AbstractObservationValue}
     */
    public SweDataArray createSosSweDataArray(AbstractObservationValue<?> observationValue) throws EncodingException {
        String observablePropertyIdentifier = observationValue.getObservableProperty();
        SweDataArrayValue dataArrayValue = new SweDataArrayValue();
        SweDataArray dataArray = new SweDataArray();
        dataArray.setEncoding(createTextEncoding(observationValue));
        dataArrayValue.setValue(dataArray);
        if (observationValue instanceof SingleObservationValue) {
            SingleObservationValue<?> singleValue = (SingleObservationValue<?>) observationValue;
            if (singleValue.getValue() instanceof SweDataArrayValue) {
                return (SweDataArray) singleValue.getValue().getValue();
            } else {
                dataArray.setElementType(createElementType(singleValue, observablePropertyIdentifier));
                dataArrayValue.addBlock(createBlock(dataArray.getElementType(), observationValue.getPhenomenonTime(),
                                                    observablePropertyIdentifier, singleValue.getValue()));
            }
        } else if (observationValue instanceof MultiObservationValues) {
            MultiObservationValues<?> multiValue = (MultiObservationValues<?>) observationValue;
            if (multiValue.getValue() instanceof SweDataArrayValue) {
                return ((SweDataArrayValue) multiValue.getValue()).getValue();
            } else if (multiValue.getValue() instanceof TVPValue) {
                TVPValue tvpValues = (TVPValue) multiValue.getValue();
                for (TimeValuePair timeValuePair : tvpValues.getValue()) {
                    if (!dataArray.isSetElementTyp()) {
                        dataArray.setElementType(createElementType(timeValuePair, observablePropertyIdentifier));
                    }
                    List<String> newBlock = createBlock(dataArray.getElementType(), timeValuePair.getTime(),
                                                        observablePropertyIdentifier, timeValuePair.getValue());
                    dataArrayValue.addBlock(newBlock);
                }
            }
        }
        return dataArray;
    }

    private SweAbstractDataComponent createElementType(TimeValuePair tvp, String name) throws EncodingException {
        SweDataRecord dataRecord = new SweDataRecord();
        dataRecord.addField(getPhenomenonTimeField(tvp.getTime()));
        dataRecord.addField(getFieldForValue(tvp.getValue(), name));
        return dataRecord;
    }

    private SweAbstractDataComponent createElementType(SingleObservationValue<?> sov, String name)
            throws EncodingException {
        SweDataRecord dataRecord = new SweDataRecord();
        dataRecord.addField(getPhenomenonTimeField(sov.getPhenomenonTime()));
        dataRecord.addField(getFieldForValue(sov.getValue(), name));
        return dataRecord;
    }

    private SweField getPhenomenonTimeField(Time sosTime) {
        SweAbstractUomType<?> time;
        if (sosTime instanceof TimePeriod) {
            time = new SweTimeRange();
        } else {
            time = new SweTime();
        }
        time.setDefinition(OmConstants.PHENOMENON_TIME);
        time.setUom(OmConstants.PHEN_UOM_ISO8601);
        return new SweField(OmConstants.PHENOMENON_TIME_NAME, time);
    }

    private SweField getFieldForValue(Value<?> iValue, String name) throws EncodingException {
        SweAbstractDataComponent value = getValue(iValue);
        value.setDefinition(name);
        return new SweField(name, value);
    }

    private SweAbstractDataComponent getValue(Value<?> iValue) throws EncodingException {

        return iValue.accept(new ValueVisitor<SweAbstractDataComponent, EncodingException>() {
            @Override
            public SweAbstractDataComponent visit(BooleanValue value) {
                return new SweBoolean();
            }

            @Override
            public SweAbstractDataComponent visit(CategoryValue value) {
                SweCategory sosSweCategory = new SweCategory();
                sosSweCategory.setCodeSpace(value.getUnit());
                return sosSweCategory;
            }

            @Override
            public SweAbstractDataComponent visit(NilTemplateValue value) {
                return new SweText();
            }

            @Override
            public SweAbstractDataComponent visit(QuantityValue value) {
                SweQuantity sosSweQuantity = new SweQuantity();
                sosSweQuantity.setUom(value.getUnit());
                return sosSweQuantity;
            }

            @Override
            public SweAbstractDataComponent visit(TextValue value) {
                return new SweText();
            }

            @Override
            public SweAbstractDataComponent visit(CountValue value) {
                return new SweCount();
            }

            @Override
            public SweAbstractDataComponent visit(ComplexValue value) throws EncodingException {
                throw new EncodingException("The merging of '%s' is not yet supported!",
                                            OmConstants.OBS_TYPE_COMPLEX_OBSERVATION);
            }

            @Override
            public SweAbstractDataComponent visit(GeometryValue value) throws EncodingException {
                throw notSupported();
            }

            @Override
            public SweAbstractDataComponent visit(HrefAttributeValue value) throws EncodingException {
                throw notSupported();
            }

            @Override
            public SweAbstractDataComponent visit(ReferenceValue value) throws EncodingException {
                throw notSupported();
            }

            @Override
            public SweAbstractDataComponent visit(SweDataArrayValue value) throws EncodingException {
                throw notSupported();
            }

            @Override
            public SweAbstractDataComponent visit(TVPValue value) throws EncodingException {
                throw notSupported();
            }

            @Override
            public SweAbstractDataComponent visit(UnknownValue value) throws EncodingException {
                throw notSupported();
            }

            private EncodingException notSupported() {
                return new EncodingException("The merging of value type '%s' is not yet supported!",
                                             iValue.getClass().getName());
            }
        });
    }

    /**
     * Create a TextEncoding object for token and tuple separators from SosObservation. If separators not set,
     * definitions from Configurator are used.
     *
     * @param sosObservation SosObservation with token and tuple separator
     *
     * @return TextEncoding
     */
    public SweAbstractEncoding createTextEncoding(OmObservation sosObservation) {
        String tupleSeparator = sosObservation.isSetTupleSeparator() ? sosObservation.getTupleSeparator()
                                : this.tupleSeparator;
        String tokenSeparator = sosObservation.isSetTokenSeparator() ? sosObservation.getTokenSeparator()
                                : this.tokenSeparator;
        String decimalSeparator = sosObservation.isSetDecimalSeparator() ? sosObservation.getDecimalSeparator()
                                  : this.decimalSeparator;
        return createTextEncoding(tupleSeparator, tokenSeparator, decimalSeparator);
    }

    /**
     * Create a TextEncoding object for token and tuple separators from SosObservation. If separators not set,
     * definitions from Configurator are used.
     *
     * @param observationValue AbstractObservationValue with token and tuple separator
     *
     * @return TextEncoding
     */
    private SweAbstractEncoding createTextEncoding(AbstractObservationValue<?> observationValue) {
        String tupleSeparator = observationValue.isSetTupleSeparator() ? observationValue.getTupleSeparator()
                                : this.tupleSeparator;
        String tokenSeparator = observationValue.isSetTokenSeparator() ? observationValue.getTokenSeparator()
                                : this.tokenSeparator;
        String decimalSeparator = observationValue.isSetDecimalSeparator() ? observationValue.getDecimalSeparator()
                                  : this.decimalSeparator;
        return createTextEncoding(tupleSeparator, tokenSeparator, decimalSeparator);
    }

    /**
     * Create a TextEncoding object for token and tuple separators.
     *
     * @param tupleSeparator Token separator
     * @param tokenSeparator Tuple separator
     * @param decimalSeparator Decimal separator
     *
     * @return TextEncoding
     */
    private SweAbstractEncoding createTextEncoding(String tupleSeparator, String tokenSeparator,
                                                   String decimalSeparator) {
        SweTextEncoding sosTextEncoding = new SweTextEncoding();
        sosTextEncoding.setBlockSeparator(tupleSeparator);
        sosTextEncoding.setTokenSeparator(tokenSeparator);
        if (!Strings.isNullOrEmpty(decimalSeparator)) {
            sosTextEncoding.setDecimalSeparator(decimalSeparator);
        }
        return sosTextEncoding;
    }

    @SuppressFBWarnings("BC_VACUOUS_INSTANCEOF")
    private List<String> createBlock(SweAbstractDataComponent elementType, Time phenomenonTime, String phenID,
                                     Value<?> value) {
        if (elementType instanceof SweDataRecord) {
            SweDataRecord elementTypeRecord = (SweDataRecord) elementType;
            List<String> block = new ArrayList<>(elementTypeRecord.getFields().size());
            for (SweField sweField : elementTypeRecord.getFields()) {
                if (!(value instanceof NilTemplateValue)) {
                    if (sweField.getElement() instanceof SweTime || sweField.getElement() instanceof SweTimeRange) {
                        block.add(DateTimeHelper.format(phenomenonTime));
                    } else if (sweField.getElement() instanceof SweAbstractDataComponent &&
                             sweField.getElement().getDefinition().equals(phenID)) {
                        block.add(value.getValue().toString());
                    } else if (sweField.getElement() instanceof SweObservableProperty) {
                        block.add(phenID);
                    }
                }
            }
            return block;
        }
        String exceptionMsg = String.format("Type of ElementType is not supported: %s",
                                            elementType != null ? elementType.getClass().getName() : "null");
        LOGGER.debug(exceptionMsg);
        throw new IllegalArgumentException(exceptionMsg);
    }

    /**
     * Create a {@link SweQuantity} from parameter
     *
     * @param value the {@link SweQuantity} value
     * @param axis the {@link SweQuantity} axis id
     * @param uom the {@link SweQuantity} unit of measure
     *
     * @return the {@link SweQuantity} from parameter
     */
    public SweQuantity createSweQuantity(Object value, String axis, String uom) {
        return new SweQuantity().setAxisID(axis).setUom(uom).setValue(JavaHelper.asDouble(value));
    }

}
