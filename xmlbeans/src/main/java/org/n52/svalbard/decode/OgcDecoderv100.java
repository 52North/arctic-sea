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

import java.util.Collections;
import java.util.Set;

import net.opengis.ogc.BBOXType;
import net.opengis.ogc.BinarySpatialOpType;
import net.opengis.ogc.BinaryTemporalOpType;
import net.opengis.ogc.PropertyNameDocument;
import net.opengis.ogc.PropertyNameType;
import net.opengis.ogc.SpatialOperatorType;
import net.opengis.ogc.TemporalOperatorType;
import net.opengis.ogc.impl.BBOXTypeImpl;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.NodeList;

import org.n52.shetland.ogc.OGCConstants;
import org.n52.shetland.ogc.filter.FilterConstants;
import org.n52.shetland.ogc.filter.FilterConstants.TimeOperator;
import org.n52.shetland.ogc.filter.SpatialFilter;
import org.n52.shetland.ogc.filter.TemporalFilter;
import org.n52.shetland.ogc.gml.GmlConstants;
import org.n52.shetland.ogc.gml.time.Time;
import org.n52.shetland.ogc.gml.time.TimeInstant;
import org.n52.shetland.ogc.gml.time.TimePeriod;
import org.n52.shetland.ogc.sos.Sos1Constants;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.decode.exception.UnsupportedDecoderXmlInputException;
import org.n52.svalbard.util.CodingHelper;
import org.n52.svalbard.util.XmlHelper;

import com.google.common.base.Joiner;
import com.vividsolutions.jts.geom.Geometry;

/**
 * @since 1.0.0
 *
 */
public class OgcDecoderv100 extends AbstractXmlDecoder<XmlObject, Object> {

    private static final Logger LOGGER = LoggerFactory.getLogger(OgcDecoderv100.class);

    private static final Set<DecoderKey> DECODER_KEYS = CodingHelper.decoderKeysForElements(
            OGCConstants.NS_OGC,
            SpatialOperatorType.class,
            TemporalOperatorType.class,
            BinarySpatialOpType.class,
            BinaryTemporalOpType.class,
            BBOXType.class,
            PropertyNameDocument.class);

    public OgcDecoderv100() {
        LOGGER.debug("Decoder for the following keys initialized successfully: {}!",
                     Joiner.on(", ").join(DECODER_KEYS));
    }

    @Override
    public Set<DecoderKey> getKeys() {
        return Collections.unmodifiableSet(DECODER_KEYS);
    }

    @Override
    public Object decode(XmlObject xmlObject) throws DecodingException {
        // validate document

        // FIXME Validation currently fails against abstract types
        // XmlHelper.validateDocument(xmlObject);
        if (xmlObject instanceof BinaryTemporalOpType) {
            return parseTemporalOperatorType((BinaryTemporalOpType) xmlObject);
        }
        if (xmlObject instanceof TemporalOperatorType) {
            throw unsupportedTemporalFilterOperand();
        }
        // add propertyNameDoc here
        if (xmlObject instanceof PropertyNameDocument) {
            PropertyNameDocument xbPropertyNameDoc = (PropertyNameDocument) xmlObject;
            return xbPropertyNameDoc.getPropertyName();
        }
        // add BBOXType here
        if (xmlObject instanceof BinarySpatialOpType) {
            return parseSpatialOperatorType((BinarySpatialOpType) xmlObject);
        }
        if (xmlObject instanceof BBOXType) {
            return parseBBOXFilterType((BBOXTypeImpl) xmlObject);
        }
        if (xmlObject instanceof BBOXTypeImpl) {
            return parseBBOXFilterType((BBOXTypeImpl) xmlObject);
        } else {
            throw new UnsupportedDecoderXmlInputException(this, xmlObject);
        }
        // TODO more spatial filters (contains, intersects, overlaps Point
        // Linestring Polygon, not supported by this SOS yet
        // return error message
    }

    /**
     * parses a single temporal filter of the requests and returns SOS temporal filter
     *
     * @param xbBinaryTemporalOp XmlObject representing the temporal filter
     *
     * @return Returns SOS representation of temporal filter
     *
     *
     * @throws DecodingException if parsing of the element failed
     */
    private Object parseTemporalOperatorType(BinaryTemporalOpType xbBinaryTemporalOp) throws DecodingException {

        TemporalFilter temporalFilter = new TemporalFilter();
        // FIXME local workaround against SOSHelper check value reference
        String valueRef = "phenomenonTime";
        try {

            NodeList nodes = xbBinaryTemporalOp.getDomNode().getChildNodes();
            for (int i = 0; i < nodes.getLength(); i++) {

                if (nodes.item(i).getNamespaceURI() != null &&
                         !nodes.item(i).getLocalName().equals(FilterConstants.EN_VALUE_REFERENCE)) {
                    // GML decoder will return TimeInstant or TimePriod
                    Object timeObject = decodeXmlElement(XmlObject.Factory.parse(nodes.item(i)));

                    if (timeObject instanceof PropertyNameType) {
                        PropertyNameType propType = (PropertyNameType) timeObject;

                        // TODO here apply logic for ogc property
                        // om:samplingTime etc
                        // valueRef = propType.getDomNode().getNodeValue();
                    }

                    if (timeObject instanceof Time) {
                        TimeOperator operator;
                        Time time = (Time) timeObject;
                        String localName = XmlHelper.getLocalName(xbBinaryTemporalOp);
                        // change to SOS 1.0. TMDuring kind of
                        if (localName.equals(TimeOperator.TM_During.name()) && time instanceof TimePeriod) {
                            operator = TimeOperator.TM_During;
                        } else if (localName.equals(TimeOperator.TM_Equals.name()) && time instanceof TimeInstant) {
                            operator = TimeOperator.TM_Equals;
                        } else if (localName.equals(TimeOperator.TM_After.name()) && time instanceof TimeInstant) {
                            operator = TimeOperator.TM_After;
                        } else if (localName.equals(TimeOperator.TM_Before.name()) && time instanceof TimeInstant) {
                            operator = TimeOperator.TM_Before;
                        } else {
                            throw unsupportedTemporalFilterOperand();
                        }
                        temporalFilter.setOperator(operator);
                        temporalFilter.setTime(time);
                        // actually it should be eg om:samplingTime
                        temporalFilter.setValueReference(valueRef);
                        break;
                    }
                }
            }

        } catch (XmlException xmle) {
            throw new DecodingException("Error while parsing temporal filter!", xmle);
        }
        return temporalFilter;

    }

    /**
     * Parses the spatial filter of a request.
     *
     * @param xbBBOX XmlBean representing the feature of interest parameter of the request
     *
     * @return Returns SpatialFilter created from the passed foi request parameter
     *
     *
     * @throws DecodingException * if creation of the SpatialFilter failed
     */
    private SpatialFilter parseBBOXFilterType(BBOXTypeImpl xbBBOX) throws DecodingException {

        SpatialFilter spatialFilter = new SpatialFilter();
        // FIXME local workaround for SOSHelper check value reference
        String valueRef = "om:featureOfInterest/sams:SF_SpatialSamplingFeature/sams:shape";
        try {

            spatialFilter.setOperator(FilterConstants.SpatialOperator.BBOX);
            XmlCursor geometryCursor = xbBBOX.newCursor();
            if (geometryCursor.toChild(GmlConstants.QN_ENVELOPE)) {
                Object sosGeometry = decodeXmlElement(XmlObject.Factory.parse(geometryCursor.getDomNode()));

                // if (sosGeometry instanceof PropertyNameType) {
                // PropertyNameType propType = (PropertyNameType) sosGeometry;
                // TODO here apply logic for ogc property
                // urn:ogc:data:location etc
                // valueRef = propType.getDomNode().getNodeValue();
                // }
                if (sosGeometry instanceof Geometry) {
                    spatialFilter.setGeometry((Geometry) sosGeometry);
                    spatialFilter.setValueReference(valueRef);
                }

            } else {
                throw unsupportedSpatialFilterOperand();
            }
            geometryCursor.dispose();

        } catch (XmlException xmle) {
            throw errorParsingSpatialFilter(xmle);
        }
        return spatialFilter;
    }

    private Object parseSpatialOperatorType(BinarySpatialOpType xbSpatialOpsType) throws DecodingException {
        SpatialFilter spatialFilter = new SpatialFilter();
        try {
            if (xbSpatialOpsType instanceof BBOXTypeImpl) {
                spatialFilter.setOperator(FilterConstants.SpatialOperator.BBOX);
                BBOXTypeImpl xbBBOX = (BBOXTypeImpl) xbSpatialOpsType;
                spatialFilter.setOperator(FilterConstants.SpatialOperator.BBOX);
                XmlCursor geometryCursor = xbBBOX.newCursor();
                if (geometryCursor.toChild(GmlConstants.QN_ENVELOPE)) {
                    Object sosGeometry = decodeXmlElement(XmlObject.Factory.parse(geometryCursor.getDomNode()));
                    if (sosGeometry instanceof Geometry) {
                        spatialFilter.setGeometry((Geometry) sosGeometry);
                    }

                } else {
                    throw unsupportedSpatialFilter();
                }
                geometryCursor.dispose();
            } else {
                throw unsupportedSpatialFilter();
            }
        } catch (XmlException xmle) {
            throw errorParsingSpatialFilter(xmle);
        }
        return spatialFilter;
    }

    private static DecodingException unsupportedSpatialFilterOperand() {
        return new DecodingException("The requested spatial filter operand is not supported by this SOS!");
    }

    private static DecodingException unsupportedSpatialFilter() {
        return new DecodingException("The requested spatial filter is not supported by this SOS!");
    }

    private static DecodingException errorParsingSpatialFilter(XmlException xmle) {
        return new DecodingException("Error while parsing spatial filter!", xmle);
    }

    private static DecodingException unsupportedTemporalFilterOperand() {
        return new DecodingException(Sos1Constants.GetObservationParams.eventTime,
                                     "The requested temporal filter operand is not supported by this SOS!");
    }
}
