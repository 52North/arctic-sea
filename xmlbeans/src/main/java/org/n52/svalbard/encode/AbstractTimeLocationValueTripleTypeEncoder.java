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

import java.util.Map;

import org.apache.xmlbeans.XmlObject;
import org.joda.time.DateTime;
import org.n52.shetland.inspire.base.InspireBaseConstants;
import org.n52.shetland.inspire.omor.InspireOMORConstants;
import org.n52.shetland.inspire.omso.InspireOMSOConstants;
import org.n52.shetland.ogc.gml.GmlConstants;
import org.n52.shetland.ogc.gml.time.Time;
import org.n52.shetland.ogc.gml.time.TimeInstant;
import org.n52.shetland.ogc.gml.time.TimePeriod;
import org.n52.shetland.ogc.om.TimeLocationValueTriple;
import org.n52.shetland.ogc.om.values.CategoryValue;
import org.n52.shetland.ogc.om.values.CountValue;
import org.n52.shetland.ogc.om.values.QuantityValue;
import org.n52.shetland.ogc.swe.SweConstants;
import org.n52.shetland.ogc.swe.simpleType.SweCategory;
import org.n52.shetland.util.DateTimeFormatException;
import org.n52.shetland.util.DateTimeHelper;
import org.n52.svalbard.encode.exception.EncodingException;

import eu.europa.ec.inspire.schemas.omso.x30.CategoricalTimeLocationValueTripleType;
import eu.europa.ec.inspire.schemas.omso.x30.MeasurementTimeLocationValueTripleType;
import eu.europa.ec.inspire.schemas.omso.x30.TimeLocationValueTripleType;
import net.opengis.waterml.x20.TimeValuePairType;

/**
 * Abstract {@link Encoder} for {@link TimeLocationValueTriple}
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 * @param <T> Target
 */
public abstract class AbstractTimeLocationValueTripleTypeEncoder<T>
        extends AbstractXmlEncoder<T, TimeLocationValueTriple> {

    private static final String MISSING = "missing";

    @Override
    public void addNamespacePrefixToMap(Map<String, String> nameSpacePrefixMap) {
        super.addNamespacePrefixToMap(nameSpacePrefixMap);
        nameSpacePrefixMap.put(InspireBaseConstants.NS_BASE, InspireBaseConstants.NS_BASE_PREFIX);
        nameSpacePrefixMap.put(InspireOMORConstants.NS_OMOR_30, InspireOMORConstants.NS_OMOR_PREFIX);
        nameSpacePrefixMap.put(InspireOMSOConstants.NS_OMSO_30, InspireOMSOConstants.NS_OMSO_PREFIX);
    }

    /**
     * Encode {@link TimeLocationValueTriple} to
     * {@link TimeLocationValueTripleType}
     *
     * @param timeLocationValueTriple
     *            The {@link TimeLocationValueTriple} to encode
     * @return The encoded {@link TimeLocationValueTriple}
     * @throws EncodingException
     *             If an error occurs
     */
    protected TimeValuePairType encodeTimeLocationValueTriple(TimeLocationValueTriple timeLocationValueTriple)
            throws EncodingException {
        if (timeLocationValueTriple.getValue() instanceof QuantityValue
                || timeLocationValueTriple.getValue() instanceof CountValue) {
            return createMeasurementTimeLocationValueTripleType(timeLocationValueTriple);
        } else if (timeLocationValueTriple.getValue() instanceof CategoryValue) {
            return createCategoricalTimeLocationValueTripleType(timeLocationValueTriple);
        }
        return null;
    }

    /**
     * Create a {@link MeasurementTimeLocationValueTripleType} from
     * {@link TimeLocationValueTriple}
     *
     * @param timeLocationValueTriple
     *            The {@link TimeLocationValueTriple} to encode
     * @return The encoded {@link TimeLocationValueTriple}
     * @throws EncodingException
     *             If an error occurs
     */
    private TimeValuePairType createMeasurementTimeLocationValueTripleType(
            TimeLocationValueTriple timeLocationValueTriple) throws EncodingException {
        MeasurementTimeLocationValueTripleType mtlvtt = MeasurementTimeLocationValueTripleType.Factory.newInstance();
        mtlvtt.addNewTime().setStringValue(getTimeString(timeLocationValueTriple.getTime()));
        mtlvtt.addNewLocation().addNewPoint().set(encodeGML(timeLocationValueTriple.getLocation()));
        String value = null;
        if (timeLocationValueTriple.getValue() instanceof QuantityValue) {
            QuantityValue quantityValue = (QuantityValue) timeLocationValueTriple.getValue();
            if (!quantityValue.getValue().equals(Double.NaN)) {
                value = Double.toString(quantityValue.getValue().doubleValue());
            }
        } else if (timeLocationValueTriple.getValue() instanceof CountValue) {
            CountValue countValue = (CountValue) timeLocationValueTriple.getValue();
            if (countValue.getValue() != null) {
                value = Integer.toString(countValue.getValue().intValue());
            }
        }
        if (value != null && !value.isEmpty()) {
            mtlvtt.addNewValue().setStringValue(value);
        } else {
            mtlvtt.addNewValue().setNil();
            mtlvtt.addNewMetadata().addNewTVPMeasurementMetadata().addNewNilReason().setNilReason(MISSING);
        }
        return mtlvtt;
    }

    /**
     * Create a {@link CategoricalTimeLocationValueTripleType} from
     * {@link TimeLocationValueTriple}
     *
     * @param timeLocationValueTriple
     *            The {@link TimeLocationValueTriple} to encode
     * @return The encoded {@link TimeLocationValueTriple}
     * @throws EncodingException
     *             If an error occurs
     */
    private TimeValuePairType createCategoricalTimeLocationValueTripleType(
            TimeLocationValueTriple timeLocationValueTriple) throws EncodingException {
        CategoricalTimeLocationValueTripleType ctlvtt = CategoricalTimeLocationValueTripleType.Factory.newInstance();
        ctlvtt.addNewTime().setStringValue(getTimeString(timeLocationValueTriple.getTime()));
        ctlvtt.addNewLocation().addNewPoint().set(encodeGML(timeLocationValueTriple.getLocation()));
        if (timeLocationValueTriple.getValue() instanceof CategoryValue) {
            CategoryValue categoryValue = (CategoryValue) timeLocationValueTriple.getValue();
            if (categoryValue.isSetValue()) {
                ctlvtt.addNewValue().addNewCategory().set(encodeSweCommon(convertToSweCategory(categoryValue)));
            } else {
                ctlvtt.addNewValue().setNil();
                ctlvtt.addNewMetadata().addNewTVPMetadata().addNewNilReason().setNilReason(MISSING);
            }
        }

        return ctlvtt;
    }

    /**
     * Convert {@link CategoryValue} to {@link SweCategory}
     *
     * @param categoryValue
     *            The {@link CategoryValue} to convert
     * @return Converted {@link CategoryValue}
     */
    private SweCategory convertToSweCategory(CategoryValue categoryValue) {
        SweCategory sweCategory = new SweCategory();
        sweCategory.setValue(categoryValue.getValue());
        sweCategory.setCodeSpace(categoryValue.getUnit());
        return sweCategory;
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

    protected XmlObject encodeGML(Object o) throws EncodingException {
        return encodeObjectToXml(GmlConstants.NS_GML_32, o);
    }

    protected XmlObject encodeGML(Object o, EncodingContext ec) throws EncodingException {
        return encodeObjectToXml(GmlConstants.NS_GML_32, o, ec);
    }

    protected XmlObject encodeSweCommon(Object o) throws EncodingException {
        return encodeObjectToXml(SweConstants.NS_SWE_20, o);
    }

    protected XmlObject encodeSweCommon(Object o, EncodingContext ec)
            throws EncodingException {
        return encodeObjectToXml(SweConstants.NS_SWE_20, o, ec);
    }
}
