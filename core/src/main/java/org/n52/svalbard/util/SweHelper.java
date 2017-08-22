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
import java.util.Collections;
import java.util.List;
import java.util.Set;

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
import org.n52.shetland.ogc.swe.CoordinateSettingsProvider;
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
import org.n52.shetland.util.CollectionHelper;
import org.n52.shetland.util.DateTimeHelper;
import org.n52.shetland.util.JavaHelper;
import org.n52.svalbard.CodingSettings;
import org.n52.svalbard.encode.exception.EncodingException;

import com.google.common.base.Strings;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * SWE helper class.
 *
 * @since 1.0.0
 *
 */
@Configurable
public final class SweHelper {

    private final Logger LOGGER = LoggerFactory.getLogger(SweHelper.class);

    private String tokenSeparator;

    private String tupleSeparator;

    private String decimalSeparator;

    private Set<String> northingNames = Collections.emptySet();

    private Set<String> eastingNames = Collections.emptySet();

    private Set<String> altitudeNames = Collections.emptySet();

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
     * @param sosObservation
     *            The {@link OmObservation} to create {@link SweDataArray} from
     *
     * @return Created {@link SweDataArray}
     *
     * @throws EncodingException
     *             If the service does not support the {@link SweDataArray}
     *             creation from value of {@link OmObservation}
     */
    public SweDataArray createSosSweDataArray(OmObservation sosObservation) throws EncodingException {
        String observablePropertyIdentifier =
                sosObservation.getObservationConstellation().getObservableProperty().getIdentifier();
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
                    if (timeValuePair != null && timeValuePair.getValue() != null
                            && timeValuePair.getValue().isSetValue()) {
                        if (!dataArray.isSetElementTyp()) {
                            dataArray.setElementType(createElementType(timeValuePair, observablePropertyIdentifier));
                        }
                        List<String> newBlock = createBlock(dataArray.getElementType(), timeValuePair.getTime(),
                                observablePropertyIdentifier, timeValuePair.getValue());
                        dataArrayValue.addBlock(newBlock);
                    }
                }
            }
        }
        return dataArray;
    }

    /**
     * Create {@link SweDataArray} from {@link AbstractObservationValue}
     *
     * @param observationValue
     *            The {@link AbstractObservationValue} to create
     *            {@link SweDataArray} from
     *
     * @return Created {@link SweDataArray}
     *
     * @throws EncodingException
     *             If the service does not support the {@link SweDataArray}
     *             creation from {@link AbstractObservationValue}
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
                    if (timeValuePair != null && timeValuePair.getValue() != null
                            && timeValuePair.getValue().isSetValue()) {
                        if (!dataArray.isSetElementTyp()) {
                            dataArray.setElementType(createElementType(timeValuePair, observablePropertyIdentifier));
                        }
                        List<String> newBlock = createBlock(dataArray.getElementType(), timeValuePair.getTime(),
                                observablePropertyIdentifier, timeValuePair.getValue());
                        dataArrayValue.addBlock(newBlock);
                    }
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
            public SweAbstractDataComponent visit(QuantityRangeValue value) throws EncodingException {
                throw notSupported();
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

            @Override
            public SweAbstractDataComponent visit(TLVTValue value) throws EncodingException {
                throw notSupported();
            }

            @Override
            public SweAbstractDataComponent visit(CvDiscretePointCoverage value) throws EncodingException {
                throw notSupported();
            }

            @Override
            public SweAbstractDataComponent visit(MultiPointCoverage value) throws EncodingException {
                throw notSupported();
            }

            @Override
            public SweAbstractDataComponent visit(RectifiedGridCoverage value) throws EncodingException {
                throw notSupported();
            }

            @Override
            public SweAbstractDataComponent visit(ProfileValue value) throws EncodingException {
                throw notSupported();
            }

            @Override
            public SweAbstractDataComponent visit(TimeRangeValue value) throws EncodingException {
                SweTimeRange sweTimeRange = new SweTimeRange();
                sweTimeRange.setUom(value.getUnit());
                return sweTimeRange;
            }

            @Override
            public SweAbstractDataComponent visit(XmlValue<?> value) throws EncodingException {
                throw notSupported();
            }

            private EncodingException notSupported() {
                return new EncodingException("The merging of value type '%s' is not yet supported!",
                        iValue.getClass().getName());
            }
        });
    }

    /**
     * Create a TextEncoding object for token and tuple separators from
     * SosObservation. If separators not set, definitions from Configurator are
     * used.
     *
     * @param o
     *            SosObservation with token and tuple separator
     *
     * @return TextEncoding
     */
    public SweAbstractEncoding createTextEncoding(OmObservation o) {
        String tuple = o.isSetTupleSeparator() ? o.getTupleSeparator() : this.tupleSeparator;
        String token = o.isSetTokenSeparator() ? o.getTokenSeparator() : this.tokenSeparator;
        String decimal = o.isSetDecimalSeparator() ? o.getDecimalSeparator() : this.decimalSeparator;
        return createTextEncoding(tuple, token, decimal);
    }

    /**
     * Create a TextEncoding object for token and tuple separators from
     * SosObservation. If separators not set, definitions from Configurator are
     * used.
     *
     * @param v
     *            AbstractObservationValue with token and tuple separator
     *
     * @return TextEncoding
     */
    private SweAbstractEncoding createTextEncoding(AbstractObservationValue<?> v) {
        String tuple = v.isSetTupleSeparator() ? v.getTupleSeparator() : this.tupleSeparator;
        String token = v.isSetTokenSeparator() ? v.getTokenSeparator() : this.tokenSeparator;
        String decimal = v.isSetDecimalSeparator() ? v.getDecimalSeparator() : this.decimalSeparator;
        return createTextEncoding(tuple, token, decimal);
    }

    /**
     * Create a TextEncoding object for token and tuple separators.
     *
     * @param tuple
     *            Token separator
     * @param token
     *            Tuple separator
     * @param decimal
     *            Decimal separator
     *
     * @return TextEncoding
     */
    private SweAbstractEncoding createTextEncoding(String tuple, String token, String decimal) {
        SweTextEncoding sosTextEncoding = new SweTextEncoding();
        sosTextEncoding.setBlockSeparator(tuple);
        sosTextEncoding.setTokenSeparator(token);
        if (!Strings.isNullOrEmpty(decimal)) {
            sosTextEncoding.setDecimalSeparator(decimal);
        }
        return sosTextEncoding;
    }

    @SuppressFBWarnings("BC_VACUOUS_INSTANCEOF")
    private List<String> createBlock(SweAbstractDataComponent elementType, Time phenomenonTime, String phenID,
            Value<?> value) {
        if (elementType instanceof SweDataRecord) {
            SweDataRecord elementTypeRecord = (SweDataRecord) elementType;
            List<String> block = new ArrayList<>(elementTypeRecord.getFields().size());
            if (!(value instanceof NilTemplateValue)) {
                elementTypeRecord.getFields().forEach(field -> {
                    if (field.getElement() instanceof SweTime || field.getElement() instanceof SweTimeRange) {
                        block.add(DateTimeHelper.format(phenomenonTime));
                    } else if (field.getElement() instanceof SweAbstractDataComponent
                            && field.getElement().getDefinition().equals(phenID)) {
                        block.add(value.getValue().toString());
                    } else if (field.getElement() instanceof SweObservableProperty) {
                        block.add(phenID);
                    }
                });
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
     * @param value
     *            the {@link SweQuantity} value
     * @param axis
     *            the {@link SweQuantity} axis id
     * @param uom
     *            the {@link SweQuantity} unit of measure
     *
     * @return the {@link SweQuantity} from parameter
     */
    public SweQuantity createSweQuantity(Object value, String axis, String uom) {
        return new SweQuantity().setAxisID(axis).setUom(uom).setValue(JavaHelper.asDouble(value));
    }

    /**
     * @return the northingNames
     */
    public Set<String> getNorthingNames() {
        return northingNames;
    }

    /**
     * @param northingNames
     *            the northingNames to set
     */
    @Setting(CoordinateSettingsProvider.NORTHING_COORDINATE_NAME)
    public void setNorthingNames(String northingNames) {
        if (!Strings.isNullOrEmpty(northingNames)) {
            this.northingNames = CollectionHelper.csvStringToSet(northingNames);
        }
    }

    /**
     * Check if northing names contains name
     *
     * @param names
     *            Names to check
     * @return <code>true</code>, if the name is defined.
     */
    public boolean hasNorthingName(String... names) {
        return check(getNorthingNames(), names);
    }

    /**
     * @return the eastingNames
     */
    public Set<String> getEastingNames() {
        return eastingNames;
    }

    /**
     * @param eastingNames
     *            the eastingNames to set
     */
    @Setting(CoordinateSettingsProvider.EASTING_COORDINATE_NAME)
    public void setEastingNames(String eastingNames) {
        if (!Strings.isNullOrEmpty(eastingNames)) {
            this.eastingNames = CollectionHelper.csvStringToSet(eastingNames);
        }
    }

    /**
     * Check if easting names contains name
     *
     * @param names
     *            Names to check
     * @return <code>true</code>, if the name is defined.
     */
    public boolean hasEastingName(String... names) {
        return check(getEastingNames(), names);
    }

    /**
     * @return the altitudeNames
     */
    public Set<String> getAltitudeNames() {
        return altitudeNames;
    }

    /**
     * @param altitudeNames
     *            the altitudeNames to set
     */
    @Setting(CoordinateSettingsProvider.ALTITUDE_COORDINATE_NAME)
    public void setAltitudeNames(String altitudeNames) {
        if (!Strings.isNullOrEmpty(altitudeNames)) {
            this.altitudeNames = CollectionHelper.csvStringToSet(altitudeNames);
        }
    }

    /**
     * Check if altitude names contains name
     *
     * @param names
     *            Names to check
     * @return <code>true</code>, if the name is defined.
     */
    public boolean hasAltitudeName(String... names) {
        return check(getAltitudeNames(), names);
    }

    private boolean check(Set<String> set, String... names) {
        for (String string : set) {
            for (String name : names) {
                if (string.equalsIgnoreCase(name)) {
                    return true;
                }
            }
        }
        return false;
    }
}
