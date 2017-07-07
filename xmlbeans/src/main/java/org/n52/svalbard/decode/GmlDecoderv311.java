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
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Set;

import org.apache.xmlbeans.XmlObject;
import org.joda.time.DateTime;

import org.n52.shetland.ogc.gml.GmlConstants;
import org.n52.shetland.ogc.gml.time.IndeterminateValue;
import org.n52.shetland.ogc.gml.time.TimeInstant;
import org.n52.shetland.ogc.gml.time.TimePeriod;
import org.n52.shetland.util.CRSHelper;
import org.n52.shetland.util.DateTimeHelper;
import org.n52.shetland.util.DateTimeParseException;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.decode.exception.UnsupportedDecoderXmlInputException;
import org.n52.svalbard.util.CodingHelper;
import org.n52.shetland.util.JTSHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;
import com.vividsolutions.jts.geom.Geometry;

import net.opengis.gml.CodeType;
import net.opengis.gml.CoordinatesType;
import net.opengis.gml.DirectPositionType;
import net.opengis.gml.EnvelopeDocument;
import net.opengis.gml.EnvelopeType;
import net.opengis.gml.PointType;
import net.opengis.gml.TimeInstantDocument;
import net.opengis.gml.TimeInstantType;
import net.opengis.gml.TimePeriodDocument;
import net.opengis.gml.TimePeriodType;
import net.opengis.gml.TimePositionType;

import com.vividsolutions.jts.io.ParseException;

/**
 * @since 1.0.0
 *
 */
public class GmlDecoderv311 implements Decoder<Object, XmlObject> {
    private static final Logger LOGGER = LoggerFactory.getLogger(GmlDecoderv311.class);

    private static final Set<DecoderKey> DECODER_KEYS = CodingHelper.decoderKeysForElements(GmlConstants.NS_GML,
            EnvelopeDocument.class, TimeInstantType.class, TimePeriodType.class, TimeInstantDocument.class,
            TimePeriodDocument.class, CodeType.class, PointType.class);

    private static final String CS = ",";

    private static final String DECIMAL = ".";

    private static final String TS = " ";

    public GmlDecoderv311() {
        LOGGER.debug("Decoder for the following keys initialized successfully: {}!",
                Joiner.on(", ").join(DECODER_KEYS));
    }

    @Override
    public Set<DecoderKey> getKeys() {
        return Collections.unmodifiableSet(DECODER_KEYS);
    }

    @Override
    public Object decode(XmlObject xmlObject) throws DecodingException {
        if (xmlObject instanceof EnvelopeDocument) {
            return getGeometry4BBOX((EnvelopeDocument) xmlObject);
        } else if (xmlObject instanceof TimeInstantType) {
            return parseTimeInstant((TimeInstantType) xmlObject);
        } else if (xmlObject instanceof TimePeriodType) {
            return parseTimePeriod((TimePeriodType) xmlObject);
        } else if (xmlObject instanceof TimeInstantDocument) {
            return parseTimeInstant(((TimeInstantDocument) xmlObject).getTimeInstant());
        } else if (xmlObject instanceof TimePeriodDocument) {
            return parseTimePeriod(((TimePeriodDocument) xmlObject).getTimePeriod());
        } else if (xmlObject instanceof CodeType) {
            return parseCodeType((CodeType) xmlObject);
        } else if (xmlObject instanceof PointType) {
            return parsePointType((PointType) xmlObject);
        } else {
            throw new UnsupportedDecoderXmlInputException(this, xmlObject);
        }
    }

    private Geometry getGeometry4BBOX(EnvelopeDocument xbBbox) throws DecodingException {
        EnvelopeType xbEnvelope = xbBbox.getEnvelope();
        // parse srid; if not set, throw exception!
        int srid = CRSHelper.parseSrsName(xbEnvelope.getSrsName());
        String lower = xbEnvelope.getLowerCorner().getStringValue();
        String upper = xbEnvelope.getUpperCorner().getStringValue();
        String geomWKT = String.format("MULTIPOINT(%s, %s)", lower, upper);
        try {
            return JTSHelper.createGeometryFromWKT(geomWKT, srid).getEnvelope();
        } catch (ParseException ex) {
            throw new DecodingException(ex);
        }
    }

    private Object parseTimePeriod(TimePeriodType xbTimePeriod) throws DecodingException {
        // begin position
        TimePositionType xbBeginTPT = xbTimePeriod.getBeginPosition();
        TimeInstant begin = null;
        if (xbBeginTPT != null) {
            begin = parseTimePosition(xbBeginTPT);
        } else {
            throw new DecodingException(
                    "gml:TimePeriod must contain gml:beginPosition Element with valid ISO:8601 String!");
        }

        // end position
        TimePositionType xbEndTPT = xbTimePeriod.getEndPosition();
        TimeInstant end = null;
        if (xbEndTPT != null) {
            end = parseTimePosition(xbEndTPT);
        } else {
            throw new DecodingException(
                    "gml:TimePeriod must contain gml:endPosition Element with valid ISO:8601 String!");
        }
        TimePeriod timePeriod = new TimePeriod(begin, end);
        timePeriod.setGmlId(xbTimePeriod.getId());
        return timePeriod;
    }

    private Object parseTimeInstant(TimeInstantType xbTimeIntant) throws DecodingException {
        TimeInstant ti = parseTimePosition(xbTimeIntant.getTimePosition());
        ti.setGmlId(xbTimeIntant.getId());
        return ti;
    }

    private TimeInstant parseTimePosition(TimePositionType xbTimePosition) throws DecodingException {
        TimeInstant ti = new TimeInstant();
        String timeString = xbTimePosition.getStringValue();
        if (timeString != null && !timeString.isEmpty()) {
            try {
                // TODO better differnetiate between ISO8601 and an
                // indeterminate value
                DateTime dateTime = DateTimeHelper.parseIsoString2DateTime(timeString);
                ti.setValue(dateTime);
                ti.setRequestedTimeLength(DateTimeHelper.getTimeLengthBeforeTimeZone(timeString));
            } catch (DateTimeParseException ex) {
                ti.setIndeterminateValue(new IndeterminateValue(timeString));
            }
        }
        if (xbTimePosition.getIndeterminatePosition() != null) {
            ti.setIndeterminateValue(new IndeterminateValue(xbTimePosition.getIndeterminatePosition().toString()));
        }

        return ti;
    }

    private org.n52.shetland.ogc.gml.CodeType parseCodeType(CodeType element) throws DecodingException {
        org.n52.shetland.ogc.gml.CodeType codeType = new org.n52.shetland.ogc.gml.CodeType(element.getStringValue());
        if (element.isSetCodeSpace()) {
            try {
                codeType.setCodeSpace(new URI(element.getCodeSpace()));
            } catch (URISyntaxException e) {
                throw new DecodingException(e, "Error while creating URI from '{}'", element.getCodeSpace());
            }
        }
        return codeType;
    }

    private Object parsePointType(PointType xbPointType) throws DecodingException {
        String geomWKT = null;
        int srid = -1;
        if (xbPointType.getSrsName() != null) {
            srid = CRSHelper.parseSrsName(xbPointType.getSrsName());
        }

        if (xbPointType.getPos() != null) {
            DirectPositionType xbPos = xbPointType.getPos();
            if (srid == -1 && xbPos.getSrsName() != null) {
                srid = CRSHelper.parseSrsName(xbPos.getSrsName());
            }
            String directPosition = getString4Pos(xbPos);
            geomWKT = JTSHelper.createWKTPointFromCoordinateString(directPosition);
        } else if (xbPointType.getCoordinates() != null) {
            CoordinatesType xbCoords = xbPointType.getCoordinates();
            String directPosition = getString4Coordinates(xbCoords);
            geomWKT = JTSHelper.createWKTPointFromCoordinateString(directPosition);
        } else {
            throw new DecodingException(
                    "For geometry type 'gml:Point' only elements 'gml:pos' and 'gml:coordinates' are allowed");
        }

        checkSrid(srid);
        if (srid == -1) {
            throw new DecodingException("No SrsName ist specified for geometry!");
        }

        try {
            return JTSHelper.createGeometryFromWKT(geomWKT, srid);
        } catch (ParseException ex) {
            throw new DecodingException(ex);
        }
    }

    /**
     * parses XmlBeans DirectPosition to a String with coordinates for WKT.
     *
     * @param xbPos
     *            XmlBeans generated DirectPosition.
     * @return Returns String with coordinates for WKT.
     */
    private String getString4Pos(DirectPositionType xbPos) {
        return xbPos.getStringValue();
    }

    /**
     * parses XmlBeans Coordinates to a String with coordinates for WKT.
     * Replaces cs, decimal and ts if different from default.
     *
     * @param xbCoordinates
     *            XmlBeans generated Coordinates.
     * @return Returns String with coordinates for WKT.
     */
    private String getString4Coordinates(CoordinatesType xbCoordinates) {
        String coordinateString = xbCoordinates.getStringValue();

        // replace cs, decimal and ts if different from default.
        if (!xbCoordinates.getCs().equals(CS)) {
            coordinateString = coordinateString.replace(xbCoordinates.getCs(), CS);
        }
        if (!xbCoordinates.getDecimal().equals(DECIMAL)) {
            coordinateString = coordinateString.replace(xbCoordinates.getDecimal(), DECIMAL);
        }
        if (!xbCoordinates.getTs().equals(TS)) {
            coordinateString = coordinateString.replace(xbCoordinates.getTs(), TS);
        }

        return coordinateString;
    }

    private void checkSrid(int srid) throws DecodingException {
        if (srid == 0 || srid == -1) {
            throw new DecodingException("No SrsName is specified for geometry!");
        }
    }
}
