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

import java.math.BigInteger;
import java.util.Map;

import org.apache.xmlbeans.XmlBoolean;
import org.apache.xmlbeans.XmlInteger;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlString;
import org.n52.shetland.ogc.cv.CvConstants;
import org.n52.shetland.ogc.gml.GmlConstants;
import org.n52.shetland.ogc.om.PointValuePair;
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
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.swe.SweConstants;
import org.n52.shetland.util.JavaHelper;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.exception.UnsupportedEncoderInputException;

import com.vividsolutions.jts.geom.Point;

import net.opengis.cv.x02.gml32.CVDiscretePointCoverageType;
import net.opengis.cv.x02.gml32.CVPointValuePairPropertyType;
import net.opengis.cv.x02.gml32.CVPointValuePairType;
import net.opengis.gml.x32.PointPropertyType;
import net.opengis.gml.x32.PointType;

/**
 * Abstract {@link Encoder} class for CV_DiscretePointCoverage
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public abstract class AbstractCVDiscretePointCoverageTypeEncoder<T>
        extends AbstractXmlEncoder<T, CvDiscretePointCoverage> {

    /**
     * Encode {@link CvDiscretePointCoverage} to
     * {@link CVDiscretePointCoverageType}
     *
     * @param cvDiscretePointCoverage
     *            The {@link CvDiscretePointCoverage} to encode
     * @return The encoded {@link CvDiscretePointCoverage}
     * @throws UnsupportedEncoderInputException
     *             If an element can not be encoded
     * @throws EncodingException
     *             If an error occurs
     */
    protected CVDiscretePointCoverageType encodeCVDiscretePointCoverage(
            CvDiscretePointCoverage cvDiscretePointCoverage) throws EncodingException {
        CVDiscretePointCoverageType cvdpct = CVDiscretePointCoverageType.Factory.newInstance(getXmlOptions());
        cvdpct.setId(cvDiscretePointCoverage.getGmlId());
        cvdpct.addNewDomainExtent().setHref(cvDiscretePointCoverage.getDomainExtent());
        cvdpct.addNewRangeType().setHref(cvDiscretePointCoverage.getRangeType().getHref());
        CVPointValuePairPropertyType cvpvppt = cvdpct.addNewElement();
        cvpvppt.setCVPointValuePair(encodePointValuePair(cvDiscretePointCoverage.getValue()));
        return cvdpct;
    }

    /**
     * Encode {@link PointValuePair} to {@link CVPointValuePairType}
     *
     * @param value
     *            The {@link PointValuePair} to encode
     * @return The encoded {@link PointValuePair}
     * @throws UnsupportedEncoderInputException
     *             If an element can not be encoded
     * @throws EncodingException
     *             If an error occurs
     */
    private CVPointValuePairType encodePointValuePair(PointValuePair value) throws EncodingException {
        CVPointValuePairType cvpvpt = CVPointValuePairType.Factory.newInstance(getXmlOptions());
        cvpvpt.setGeometry(encodeGeometry(value.getPoint(), JavaHelper.generateID(value.toString())));
        if (value.isSetValue()) {
            cvpvpt.setValue(encodeValue(value.getValue()));
        } else {
            cvpvpt.addNewValue();
        }
        return cvpvpt;
    }

    /**
     * Encode {@link Point} to {@link PointPropertyType}
     *
     * @param point
     *            The {@link Point} to encode
     * @param gmlId
     *            The gml id for the point
     * @return The encoded {@link Point}
     * @throws UnsupportedEncoderInputException
     *             If an element can not be encoded
     * @throws EncodingException
     *             If an error occurs
     */
    private PointPropertyType encodeGeometry(Point point, String gmlId) throws EncodingException {
        PointPropertyType ppt = PointPropertyType.Factory.newInstance(getXmlOptions());
        ppt.setPoint((PointType) encodeGML(point, EncodingContext.of(XmlBeansEncodingFlags.GMLID, gmlId)));
        return ppt;
    }

    /**
     * Encode {@link Value} to an {@link XmlObject}
     *
     * @param value
     *            The {@link Value} to encode
     * @return The encoded {@link Value}
     * @throws UnsupportedEncoderInputException
     *             If an element can not be encoded
     * @throws EncodingException
     *             If an error occurs
     */
    private XmlObject encodeValue(Value<?> value) throws EncodingException {
        return value.accept(new ResultValueVisitor());
    }

    @Override
    public void addNamespacePrefixToMap(Map<String, String> nameSpacePrefixMap) {
        super.addNamespacePrefixToMap(nameSpacePrefixMap);
        nameSpacePrefixMap.put(CvConstants.NS_CV, CvConstants.NS_CV_PREFIX);
    }

    protected XmlObject encodeGML(Object o, EncodingContext ec) throws EncodingException {
        return encodeObjectToXml(GmlConstants.NS_GML_32, o, ec);
    }

    protected XmlObject encodeGML(Object o) throws EncodingException {
        return encodeObjectToXml(GmlConstants.NS_GML_32, o);
    }

    protected XmlObject encodeSWE(Object o) throws EncodingException {
        return encodeObjectToXml(SweConstants.NS_SWE_20, o);
    }

    protected XmlObject encodeSWE(Object o, EncodingContext ec) throws EncodingException {
        return encodeObjectToXml(SweConstants.NS_SWE_20, o, ec);
    }

    protected XmlString createXmlString() {
        return XmlString.Factory.newInstance(getXmlOptions());
    }

    protected XmlInteger createXmlInteger() {
        return XmlInteger.Factory.newInstance(getXmlOptions());
    }

    protected XmlBoolean createXmlBoolean() {
        return XmlBoolean.Factory.newInstance(getXmlOptions());
    }

    /**
     * {@link ValueVisitor} implementation for the result
     *
     * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
     * @since 1.0.0
     *
     */
    private class ResultValueVisitor
            implements ValueVisitor<XmlObject, EncodingException> {

        ResultValueVisitor() {
        }

        @Override
        public XmlObject visit(BooleanValue value) throws EncodingException {
            XmlBoolean xbBoolean = createXmlBoolean();
            if (value.isSetValue()) {
                xbBoolean.setBooleanValue(value.getValue());
            } else {
                xbBoolean.setNil();
            }
            return xbBoolean;
        }

        @Override
        public XmlObject visit(CategoryValue value) throws EncodingException {
            if (value.isSetValue() && !value.getValue().isEmpty()) {
                return encodeGML(value);
            }
            return null;
        }

        @Override
        public XmlObject visit(ComplexValue value) throws EncodingException {

            if (value.isSetValue()) {
                return encodeSWE(value.getValue(), EncodingContext.of(XmlBeansEncodingFlags.FOR_OBSERVATION, true));
            }
            return null;
        }

        @Override
        public XmlObject visit(CountValue value) throws EncodingException {
            XmlInteger xbInteger = createXmlInteger();
            if (value.isSetValue() && value.getValue() != Integer.MIN_VALUE) {
                xbInteger.setBigIntegerValue(new BigInteger(value.getValue().toString()));
            } else {
                xbInteger.setNil();
            }
            return xbInteger;
        }

        @Override
        public XmlObject visit(GeometryValue value) throws EncodingException {
            if (value.isSetValue()) {
                EncodingContext ec = EncodingContext
                        .of(XmlBeansEncodingFlags.GMLID,
                                SosConstants.OBS_ID_PREFIX + JavaHelper.generateID(value.toString()))
                        .with(XmlBeansEncodingFlags.PROPERTY_TYPE, true);
                return encodeGML(value.getValue(), ec);
            } else {
                return null;
            }
        }

        @Override
        public XmlObject visit(HrefAttributeValue value) throws EncodingException {
            return null;
        }

        @Override
        public XmlObject visit(NilTemplateValue value) throws EncodingException {
            return null;
        }

        @Override
        public XmlObject visit(QuantityValue value) throws EncodingException {
            return encodeGML(value);
        }

        @Override
        public XmlObject visit(ReferenceValue value) throws EncodingException {
            return null;
        }

        @Override
        public XmlObject visit(SweDataArrayValue value) throws EncodingException {
            return encodeSWE(value.getValue(), EncodingContext.of(XmlBeansEncodingFlags.FOR_OBSERVATION, true));
        }

        @Override
        public XmlObject visit(TVPValue value) throws EncodingException {
            return null;
        }

        @Override
        public XmlObject visit(TLVTValue value) throws EncodingException {
            return null;
        }

        @Override
        public XmlObject visit(TextValue value) throws EncodingException {
            XmlString xbString = createXmlString();
            if (value.isSetValue()) {
                xbString.setStringValue(value.getValue());
            } else {
                xbString.setNil();
            }
            return xbString;
        }

        // @Override
        // public XmlObject visit(XmlValue value) throws EncodingException {
        // return value.getValue();
        // }

        @Override
        public XmlObject visit(UnknownValue value) throws EncodingException {
            return null;
        }

        @Override
        public XmlObject visit(CvDiscretePointCoverage value) throws EncodingException {
            return null;
        }

        @Override
        public XmlObject visit(MultiPointCoverage value) throws EncodingException {
            return null;
        }

        @Override
        public XmlObject visit(RectifiedGridCoverage value) throws EncodingException {
            return null;
        }

        @Override
        public XmlObject visit(ProfileValue value) throws EncodingException {
            return encodeObjectToXml(value.getDefaultElementEncoding(), value);
        }

        @Override
        public XmlObject visit(TimeRangeValue value) throws EncodingException {
            return null;
        }

        @Override
        public XmlObject visit(XmlValue<?> value) throws EncodingException {
            if (value.getValue() instanceof XmlObject) {
                return (XmlObject) value.getValue();
            }
            return null;
        }

        @Override
        public XmlObject visit(QuantityRangeValue value) throws EncodingException {
            return null;
        }
    }
}
