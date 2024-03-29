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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.ParseException;
import org.n52.janmayen.NcName;
import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.gml.AbstractGeometry;
import org.n52.shetland.ogc.gml.CodeWithAuthority;
import org.n52.shetland.ogc.gml.GmlConstants;
import org.n52.shetland.ogc.gml.GmlMeasureType;
import org.n52.shetland.ogc.gml.time.IndeterminateValue;
import org.n52.shetland.ogc.gml.time.TimeInstant;
import org.n52.shetland.ogc.gml.time.TimePeriod;
import org.n52.shetland.ogc.om.features.FeatureCollection;
import org.n52.shetland.ogc.om.features.samplingFeatures.AbstractSamplingFeature;
import org.n52.shetland.ogc.om.features.samplingFeatures.SamplingFeature;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.util.CRSHelper;
import org.n52.shetland.util.CollectionHelper;
import org.n52.shetland.util.DateTimeHelper;
import org.n52.shetland.util.DateTimeParseException;
import org.n52.shetland.util.JTSHelper;
import org.n52.shetland.util.ReferencedEnvelope;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.decode.exception.UnsupportedDecoderInputException;
import org.n52.svalbard.decode.exception.UnsupportedDecoderXmlInputException;
import org.n52.svalbard.util.CodingHelper;
import org.n52.svalbard.util.XmlHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;

import net.opengis.gml.x32.AbstractCurveType;
import net.opengis.gml.x32.AbstractGeometryType;
import net.opengis.gml.x32.AbstractRingPropertyType;
import net.opengis.gml.x32.AbstractRingType;
import net.opengis.gml.x32.AbstractSurfaceType;
import net.opengis.gml.x32.CodeType;
import net.opengis.gml.x32.CodeWithAuthorityType;
import net.opengis.gml.x32.CompositeSurfaceType;
import net.opengis.gml.x32.CoordinatesType;
import net.opengis.gml.x32.CurvePropertyType;
import net.opengis.gml.x32.DirectPositionListType;
import net.opengis.gml.x32.DirectPositionType;
import net.opengis.gml.x32.EnvelopeDocument;
import net.opengis.gml.x32.EnvelopeType;
import net.opengis.gml.x32.FeatureCollectionDocument;
import net.opengis.gml.x32.FeatureCollectionType;
import net.opengis.gml.x32.FeaturePropertyType;
import net.opengis.gml.x32.GeometryPropertyType;
import net.opengis.gml.x32.LineStringDocument;
import net.opengis.gml.x32.LineStringType;
import net.opengis.gml.x32.LinearRingType;
import net.opengis.gml.x32.MeasureType;
import net.opengis.gml.x32.MultiCurveDocument;
import net.opengis.gml.x32.MultiCurveType;
import net.opengis.gml.x32.PointDocument;
import net.opengis.gml.x32.PointType;
import net.opengis.gml.x32.PolygonDocument;
import net.opengis.gml.x32.PolygonType;
import net.opengis.gml.x32.ReferenceType;
import net.opengis.gml.x32.SurfacePropertyType;
import net.opengis.gml.x32.TimeInstantDocument;
import net.opengis.gml.x32.TimeInstantType;
import net.opengis.gml.x32.TimePeriodDocument;
import net.opengis.gml.x32.TimePeriodType;
import net.opengis.gml.x32.TimePositionType;
import net.opengis.gml.x32.VerticalDatumPropertyType;

/**
 * @since 1.0.0
 *
 */
public class GmlDecoderv321 extends AbstractGmlDecoderv321<XmlObject, Object> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GmlDecoderv321.class);

    private static final Set<DecoderKey> DECODER_KEYS = CollectionHelper.union(CodingHelper
            .decoderKeysForElements(GmlConstants.NS_GML_32,
                                    EnvelopeDocument.class,
                                    EnvelopeType.class,
                                    TimeInstantType.class,
                                    TimePeriodType.class,
                                    TimeInstantDocument.class,
                                    TimePeriodDocument.class,
                                    ReferenceType.class,
                                    MeasureType.class,
                                    PointType.class,
                                    PointDocument.class,
                                    LineStringType.class,
                                    LineStringDocument.class,
                                    MultiCurveDocument.class,
                                    MultiCurveType.class,
                                    PolygonType.class,
                                    PolygonDocument.class,
                                    CompositeSurfaceType.class,
                                    CodeWithAuthorityType.class,
                                    CodeType.class,
                                    FeaturePropertyType.class,
                                    GeometryPropertyType.class,
                                    VerticalDatumPropertyType.class,
                                    FeatureCollectionDocument.class,
                                    FeatureCollectionType.class
            ), CodingHelper.decoderKeysForElements(MeasureType.type.toString(), MeasureType.class));

    private static final String CS = ",";
    private static final String DECIMAL = ".";
    private static final String TS = " ";
    private static final int DEFAULT_SRID = 4326;

    public GmlDecoderv321() {
        LOGGER.debug("Decoder for the following keys initialized successfully: {}!",
                     Joiner.on(", ").join(DECODER_KEYS));
    }

    @Override
    public Set<DecoderKey> getKeys() {
        return Collections.unmodifiableSet(DECODER_KEYS);
    }

    @Override
    public Object decode(XmlObject xmlObject) throws DecodingException {
        if (xmlObject instanceof FeaturePropertyType) {
            return parseFeaturePropertyType((FeaturePropertyType) xmlObject);
        } else if (xmlObject instanceof EnvelopeDocument) {
            return parseEnvelope(((EnvelopeDocument) xmlObject).getEnvelope());
        } else if (xmlObject instanceof EnvelopeType) {
            return parseEnvelope((EnvelopeType) xmlObject);
        } else if (xmlObject instanceof TimeInstantType) {
            return parseTimeInstant((TimeInstantType) xmlObject);
        } else if (xmlObject instanceof TimePeriodType) {
            return parseTimePeriod((TimePeriodType) xmlObject);
        } else if (xmlObject instanceof TimeInstantDocument) {
            return parseTimeInstant(((TimeInstantDocument) xmlObject).getTimeInstant());
        } else if (xmlObject instanceof TimePeriodDocument) {
            return parseTimePeriod(((TimePeriodDocument) xmlObject).getTimePeriod());
        } else if (xmlObject instanceof ReferenceType) {
            return parseReferenceType((ReferenceType) xmlObject);
        } else if (xmlObject instanceof MeasureType) {
            return parseMeasureType((MeasureType) xmlObject);
        } else if (xmlObject instanceof PointType) {
            return parsePointType((PointType) xmlObject);
        } else if (xmlObject instanceof PointDocument) {
            return parsePointType(((PointDocument) xmlObject).getPoint());
        } else if (xmlObject instanceof LineStringType) {
            return parseLineStringType((LineStringType) xmlObject);
        } else if (xmlObject instanceof LineStringDocument) {
            return parseLineStringType(((LineStringDocument) xmlObject).getLineString());
        } else if (xmlObject instanceof PolygonType) {
            return parsePolygonType((PolygonType) xmlObject);
        } else if (xmlObject instanceof PolygonDocument) {
            return parsePolygonType(((PolygonDocument) xmlObject).getPolygon());
        } else if (xmlObject instanceof CompositeSurfaceType) {
            return parseCompositeSurfaceType((CompositeSurfaceType) xmlObject);
        } else if (xmlObject instanceof CodeWithAuthorityType) {
            return parseCodeWithAuthorityTye((CodeWithAuthorityType) xmlObject);
        } else if (xmlObject instanceof CodeType) {
            return parseCodeType((CodeType) xmlObject);
        } else if (xmlObject instanceof GeometryPropertyType) {
            return parseGeometryPropertyType((GeometryPropertyType) xmlObject);
        } else if (xmlObject instanceof VerticalDatumPropertyType) {
            return parseVerticalDatumPropertyType((VerticalDatumPropertyType) xmlObject);
        } else if (xmlObject instanceof FeatureCollectionDocument) {
            return parseFeatureCollectionDocument((FeatureCollectionDocument) xmlObject);
        } else if (xmlObject instanceof FeatureCollectionType) {
            return parseFeatureCollectionType((FeatureCollectionType) xmlObject);
        } else if (xmlObject instanceof MultiCurveDocument) {
            return parseMultiCurveDocument((MultiCurveDocument) xmlObject);
        } else if (xmlObject instanceof MultiCurveType) {
            return parseMultiCurveType((MultiCurveType) xmlObject);
        } else {
            throw new UnsupportedDecoderXmlInputException(this, xmlObject);
        }
    }

    private Object parseFeaturePropertyType(FeaturePropertyType featurePropertyType) throws DecodingException {
        AbstractSamplingFeature feature = null;
        // if xlink:href is set
        if (featurePropertyType.getHref() != null) {
            if (featurePropertyType.getHref().startsWith("#")) {
                feature = new SamplingFeature(null, featurePropertyType.getHref().replace("#", ""));
                feature.setGmlId(featurePropertyType.getHref());
            } else {
                feature = new SamplingFeature(new CodeWithAuthority(featurePropertyType.getHref()));
                if (featurePropertyType.getTitle() != null && !featurePropertyType.getTitle().isEmpty()) {
                    feature.addName(new org.n52.shetland.ogc.gml.CodeType(featurePropertyType.getTitle()));
                }
                feature.setGmlId("ssf_" + NcName.makeValid(featurePropertyType.getHref()));
            }
        } else {
            // if feature is encoded
            XmlObject abstractFeature = null;
            if (featurePropertyType.getAbstractFeature() != null) {
                abstractFeature = featurePropertyType.getAbstractFeature();
            } else if (featurePropertyType.getDomNode().hasChildNodes()) {
                try {
                    abstractFeature = XmlObject.Factory
                            .parse(XmlHelper.getNodeFromNodeList(featurePropertyType.getDomNode().getChildNodes()));
                } catch (XmlException xmle) {
                    throw new DecodingException("Error while parsing feature request!", xmle);
                }
            }
            if (abstractFeature != null) {
                Object decodedObject = decodeXmlObject(abstractFeature);
                if (decodedObject instanceof AbstractSamplingFeature) {
                    feature = (AbstractSamplingFeature) decodedObject;
                } else {
                    throw unsupportedFeaturePropertyType();
                }
            }
        }
        if (feature == null) {
            throw unsupportedFeaturePropertyType();
        }
        return feature;
    }

    private FeatureCollection parseFeatureCollectionDocument(FeatureCollectionDocument featureCollectionDocument)
            throws DecodingException {
        return parseFeatureCollectionType(featureCollectionDocument.getFeatureCollection());
    }

    private FeatureCollection parseFeatureCollectionType(FeatureCollectionType featureCollectionType)
            throws DecodingException {
        final FeatureCollection feaColl = new FeatureCollection();
        for (FeaturePropertyType feaPropType : featureCollectionType.getFeatureMemberArray()) {
            Object decoded = decodeXmlElement(feaPropType);
            feaColl.addMember((AbstractFeature) decoded);
        }
        return feaColl;
    }

    /**
     * parses the BBOX element of the featureOfInterest element contained in the GetObservation request and returns a
     * String representing the BOX in Well-Known-Text format
     *
     * @param envelopeType XmlBean representing the BBOX-element in the request
     *
     * @return Returns the BBOX as ReferencedEnvelope.
     *
     * @throws DecodingException * if parsing the BBOX element failed
     */
    private ReferencedEnvelope parseEnvelope(EnvelopeType envelopeType) throws DecodingException {
        int srid = CRSHelper.parseSrsName(envelopeType.getSrsName());
        String lowerCorner = envelopeType.getLowerCorner().getStringValue();
        String upperCorner = envelopeType.getUpperCorner().getStringValue();
        return new ReferencedEnvelope(JTSHelper.createEnvelopeFromLowerUpperCorner(lowerCorner, upperCorner), srid);
    }

    /**
     * parses TimeInstant
     *
     * @param xbTimeIntant XmlBean representation of TimeInstant
     *
     * @return Returns a TimeInstant created from the TimeInstantType
     *
     * @throws DecodingException if the time string is invalid
     */
    private Object parseTimeInstant(TimeInstantType xbTimeIntant) throws DecodingException {
        TimeInstant ti = parseTimePosition(xbTimeIntant.getTimePosition());
        ti.setGmlId(xbTimeIntant.getId());
        return ti;
    }

    /**
     * creates SOS representation of time period from XMLBeans representation of time period
     *
     * @param xbTimePeriod XMLBeans representation of time period
     *
     * @return Returns SOS representation of time period
     *
     *
     * @throws DecodingException if the time string is invalid
     */
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

    private TimeInstant parseTimePosition(TimePositionType xbTimePosition) throws DecodingException {
        TimeInstant ti = new TimeInstant();
        String timeString = xbTimePosition.getStringValue();
        if (timeString != null && !timeString.isEmpty()) {
            try {
                // TODO better differentiate between ISO8601 and indeterminate
                // value
                ti.setValue(DateTimeHelper.parseIsoString2DateTime(timeString));
                ti.setRequestedTimeLength(DateTimeHelper.getTimeLengthBeforeTimeZone(timeString));
            } catch (DateTimeParseException ex) {
                ti.setIndeterminateValue(new IndeterminateValue(timeString));
            }
        }

        if (xbTimePosition.isSetIndeterminatePosition()) {
            ti.setIndeterminateValue(new IndeterminateValue(xbTimePosition.getIndeterminatePosition().toString()));
        }

        return ti;
    }

    private GmlMeasureType parseMeasureType(MeasureType measureType) {
        GmlMeasureType sosMeasureType = new GmlMeasureType(measureType.getDoubleValue());
        sosMeasureType.setUnit(measureType.getUom());
        return sosMeasureType;
    }

    private AbstractGeometry parseGeometryPropertyType(GeometryPropertyType geometryPropertyType)
            throws DecodingException {
        return parseAbstractGeometryType(geometryPropertyType.getAbstractGeometry());
    }

    private AbstractGeometry parseAbstractGeometryType(AbstractGeometryType agt) throws DecodingException {
        AbstractGeometry abstractGeometry = new AbstractGeometry(agt.getId());
        parseAbstractGMLType(agt, abstractGeometry);
        abstractGeometry.setGeometry((Geometry) decode(agt));
        return abstractGeometry;
    }

    private Geometry parsePointType(PointType xbPointType) throws DecodingException {

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
            geomWKT = "POINT(" + directPosition + ")";
        } else if (xbPointType.getCoordinates() != null) {
            CoordinatesType xbCoords = xbPointType.getCoordinates();
            String directPosition = getString4Coordinates(xbCoords);
            geomWKT = "POINT" + directPosition;
        } else {
            throw new DecodingException("For geometry type 'gml:Point' only element " +
                                        "'gml:pos' and 'gml:coordinates' are allowed " +
                                        "in the feature of interest parameter!");
        }

        srid = setDefaultForUnsetSrid(srid);

        try {
            return JTSHelper.createGeometryFromWKT(geomWKT, srid);
        } catch (ParseException ex) {
            throw new DecodingException(ex);
        }
    }

    private Geometry parseLineStringType(LineStringType xbLineStringType) throws DecodingException {
        int srid = -1;
        if (xbLineStringType.getSrsName() != null) {
            srid = CRSHelper.parseSrsName(xbLineStringType.getSrsName());
        }

        DirectPositionType[] xbPositions = xbLineStringType.getPosArray();

        String geomWKT;
        if (xbPositions != null && xbPositions.length > 0) {
            if (srid == -1 && xbPositions[0].getSrsName() != null && !(xbPositions[0].getSrsName().isEmpty())) {
                srid = CRSHelper.parseSrsName(xbPositions[0].getSrsName());
            }
            geomWKT = "LINESTRING" + getString4PosArray(xbLineStringType.getPosArray(), false) + "";
        } else if (xbLineStringType.getPosList() != null) {
            StringBuilder builder = new StringBuilder();
            builder.append("LINESTRING(");
            DirectPositionListType posList = xbLineStringType.getPosList();
            int dim;
            if (posList.getSrsDimension() == null) {
                dim = 2;
            } else {
                dim = posList.getSrsDimension().intValue();
            }
            if (posList.getListValue().size() % dim != 0) {
                throw new DecodingException("posList does not contain a multiple of %d coordinates", dim);
            }

            @SuppressWarnings("unchecked")
            Iterator<Double> iterator = posList.getListValue().iterator();
            if (iterator.hasNext()) {
                builder.append(iterator.next());
                for (int i = 1; i < dim; ++i) {
                    builder.append(' ').append(iterator.next());
                }
                while (iterator.hasNext()) {
                    builder.append(", ");
                    builder.append(iterator.next());
                    for (int i = 1; i < dim; ++i) {
                        builder.append(' ').append(iterator.next());
                    }
                }
            }
            builder.append(")");
            geomWKT = builder.toString();
        } else {
            geomWKT = null;
        }
        srid = setDefaultForUnsetSrid(srid);

        if (geomWKT != null) {
            try {
                return JTSHelper.createGeometryFromWKT(geomWKT, srid);
            } catch (ParseException ex) {
                throw new DecodingException(ex);
            }
        } else {
            return JTSHelper.getGeometryFactoryForSRID(srid).createGeometryCollection(null);
        }
    }

    private Geometry parsePolygonType(PolygonType xbPolygonType) throws DecodingException {
        int srid = -1;
        if (xbPolygonType.getSrsName() != null) {
            srid = CRSHelper.parseSrsName(xbPolygonType.getSrsName());
        }
        String exteriorCoordString = null;
        StringBuilder geomWKT = new StringBuilder();
        StringBuilder interiorCoordString = new StringBuilder();

        AbstractRingPropertyType xbExterior = xbPolygonType.getExterior();

        if (xbExterior != null) {
            AbstractRingType xbExteriorRing = xbExterior.getAbstractRing();
            if (xbExteriorRing instanceof LinearRingType) {
                LinearRingType xbLinearRing = (LinearRingType) xbExteriorRing;
                exteriorCoordString = getCoordString4LinearRing(xbLinearRing);
            } else {
                throw new DecodingException(
                        "The Polygon must contain the following elements <gml:exterior><gml:LinearRing><gml:posList>!");
            }
        }

        AbstractRingPropertyType[] xbInterior = xbPolygonType.getInteriorArray();
        if (xbInterior != null && xbInterior.length != 0) {
            for (AbstractRingPropertyType xbInteriorRing : xbInterior) {
                if (xbInteriorRing.getAbstractRing() instanceof LinearRingType) {
                    interiorCoordString.append(", ")
                            .append(getCoordString4LinearRing((LinearRingType) xbInteriorRing.getAbstractRing()));
                }
            }
        }

        geomWKT.append("POLYGON(");
        geomWKT.append(exteriorCoordString);
        geomWKT.append(interiorCoordString);
        geomWKT.append(")");

        srid = setDefaultForUnsetSrid(srid);
        try {
            return JTSHelper.createGeometryFromWKT(geomWKT.toString(), srid);
        } catch (ParseException ex) {
            throw new DecodingException(ex);
        }
    }

    private Geometry parseMultiCurveDocument(MultiCurveDocument multiCurveDocument) throws DecodingException {
        return parseMultiCurveType(multiCurveDocument.getMultiCurve());
    }

    private Geometry parseMultiCurveType(MultiCurveType multiCurveType) throws DecodingException {
        List<Geometry> curves = new ArrayList<>(multiCurveType.getCurveMemberArray().length);
        for (CurvePropertyType curvePropertyType: multiCurveType.getCurveMemberArray()) {
            if (curvePropertyType.getAbstractCurve() != null) {
                curves.add(parseAbstractCurveType(curvePropertyType.getAbstractCurve()));
            }
        }
        return JTSHelper.getGeometryFactoryForSRID(getSRID(multiCurveType)).buildGeometry(curves);
    }

    private int getSRID(AbstractGeometryType abstractGeometryType) {
        String srsName = abstractGeometryType.getSrsName();
        int srid = CRSHelper.parseSrsName(srsName);
        return srid <= 0 ? DEFAULT_SRID : srid;
    }

    private Geometry parseAbstractCurveType(AbstractCurveType abstractCurveType) throws DecodingException {
        if (abstractCurveType instanceof LineStringType) {
            return parseLineStringType((LineStringType) abstractCurveType);
        } else {
            throw new UnsupportedDecoderInputException(this, abstractCurveType);
        }
    }


    private Geometry parseCompositeSurfaceType(CompositeSurfaceType xbCompositeSurface) throws DecodingException {
        SurfacePropertyType[] xbCurfaceProperties = xbCompositeSurface.getSurfaceMemberArray();
        int srid = -1;
        ArrayList<Polygon> polygons = new ArrayList<>(xbCurfaceProperties.length);
        if (xbCompositeSurface.getSrsName() != null) {
            srid = CRSHelper.parseSrsName(xbCompositeSurface.getSrsName());
        }
        for (SurfacePropertyType xbSurfaceProperty : xbCurfaceProperties) {
            AbstractSurfaceType xbAbstractSurface = xbSurfaceProperty.getAbstractSurface();
            if (srid == -1 && xbAbstractSurface.getSrsName() != null) {
                srid = CRSHelper.parseSrsName(xbAbstractSurface.getSrsName());
            }
            if (xbAbstractSurface instanceof PolygonType) {
                polygons.add((Polygon) parsePolygonType((PolygonType) xbAbstractSurface));
            } else {
                throw new DecodingException("The FeatureType %s is not supportted! Only PolygonType",
                                            xbAbstractSurface);
            }
        }
        if (polygons.isEmpty()) {
            throw new DecodingException("The FeatureType: %s does not contain any member!", xbCompositeSurface);
        }
        srid = setDefaultForUnsetSrid(srid);
        GeometryFactory factory = new GeometryFactory();
        Geometry geom = factory.createMultiPolygon(polygons.toArray(new Polygon[polygons.size()]));
        geom.setSRID(srid);
        return geom;
    }

    private org.n52.shetland.ogc.gml.ReferenceType parseVerticalDatumPropertyType(VerticalDatumPropertyType vdpt) {
        // TODO parse VerticalDatumType
        if (vdpt.isSetHref() && !vdpt.getHref().isEmpty()) {
            org.n52.shetland.ogc.gml.ReferenceType referenceType = new org.n52.shetland.ogc.gml.ReferenceType(vdpt
                    .getHref());
            if (vdpt.isSetTitle() && !vdpt.getTitle().isEmpty()) {
                referenceType.setTitle(vdpt.getTitle());
            }
            return referenceType;
        }
        return new org.n52.shetland.ogc.gml.ReferenceType("UNKNOWN");
    }

    /**
     * method parses the passed linearRing(generated thru XmlBEans) and returns a string containing the coordinate
     * values of the passed ring
     *
     * @param xbLinearRing linearRing(generated thru XmlBEans)
     *
     * @return Returns a string containing the coordinate values of the passed ring
     *
     *
     * @throws DecodingException * if parsing the linear Ring failed
     */
    private String getCoordString4LinearRing(LinearRingType xbLinearRing) throws DecodingException {

        String result = "";
        DirectPositionListType xbPosList = xbLinearRing.getPosList();
        CoordinatesType xbCoordinates = xbLinearRing.getCoordinates();
        DirectPositionType[] xbPosArray = xbLinearRing.getPosArray();
        if (xbPosList != null && !(xbPosList.getStringValue().isEmpty())) {
            result = getString4PosList(xbPosList);
        } else if (xbCoordinates != null && !(xbCoordinates.getStringValue().isEmpty())) {
            result = getString4Coordinates(xbCoordinates);
        } else if (xbPosArray != null && xbPosArray.length > 0) {
            result = getString4PosArray(xbPosArray, true);
        } else {
            throw new DecodingException("The Polygon must contain the following elements " +
                                        "<gml:exterior><gml:LinearRing><gml:posList>, " +
                                        "<gml:exterior><gml:LinearRing><gml:coordinates> " +
                                        "or <gml:exterior><gml:LinearRing><gml:pos>{<gml:pos>}!");
        }

        return result;
    }

    /**
     * parses XmlBeans DirectPosition to a String with coordinates for WKT.
     *
     * @param xbPos XmlBeans generated DirectPosition.
     *
     * @return Returns String with coordinates for WKT.
     */
    private String getString4Pos(DirectPositionType xbPos) {
        return xbPos.getStringValue();
    }

    /**
     * parses XmlBeans DirectPosition[] to a String with coordinates for WKT.
     *
     * @param xbPosArray XmlBeans generated DirectPosition[].
     *
     * @return Returns String with coordinates for WKT.
     */
    private String getString4PosArray(DirectPositionType[] xbPosArray, boolean polygon) {
        StringBuilder coordinateString = new StringBuilder();
        coordinateString.append("(");
        for (DirectPositionType directPositionType : xbPosArray) {
            coordinateString.append(directPositionType.getStringValue());
            coordinateString.append(", ");
        }
        if (polygon && !xbPosArray[0].getStringValue()
                .equalsIgnoreCase(xbPosArray[xbPosArray.length - 1].getStringValue())) {
            coordinateString.append(xbPosArray[0].getStringValue());
        } else {
            coordinateString.delete(coordinateString.length() - 2, coordinateString.length());
        }
        coordinateString.append(")");

        return coordinateString.toString();
    }

    /**
     * parses XmlBeans DirectPositionList to a String with coordinates for WKT.
     *
     * @param xbPosList XmlBeans generated DirectPositionList.
     *
     * @return Returns String with coordinates for WKT.
     *
     *
     * @throws DecodingException if the pos list contains an odd number of values
     */
    private String getString4PosList(DirectPositionListType xbPosList) throws DecodingException {
        StringBuilder coordinateString = new StringBuilder("(");
        List<?> values = xbPosList.getListValue();
        if ((values.size() % 2) != 0) {
            throw new DecodingException("The Polygons posList must contain pairs of coordinates!");
        } else {
            for (int i = 0; i < values.size(); i++) {
                coordinateString.append(values.get(i));
                if ((i % 2) != 0) {
                    coordinateString.append(", ");
                } else {
                    coordinateString.append(" ");
                }
            }
        }
        int length = coordinateString.length();
        coordinateString.delete(length - 2, length);
        coordinateString.append(")");

        return coordinateString.toString();
    }

    /**
     * parses XmlBeans Coordinates to a String with coordinates for WKT. Replaces cs, decimal and ts if different from
     * default.
     *
     * @param xbCoordinates XmlBeans generated Coordinates.
     *
     * @return Returns String with coordinates for WKT.
     */
    private String getString4Coordinates(CoordinatesType xbCoordinates) {
        String coordinateString = "(" + xbCoordinates.getStringValue() + ")";

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

    private int setDefaultForUnsetSrid(int srid) throws DecodingException {
        if (srid == 0 || srid == -1) {
            LOGGER.warn("No SrsName is specified for geometry, instead the default 4326 is taken!");
            return DEFAULT_SRID;
        } else {
            return srid;
        }
    }

    private static DecodingException unsupportedFeaturePropertyType() {
        return new DecodingException(Sos2Constants.InsertObservationParams.observation,
                                     "The requested featurePropertyType type is not supported by this service!");
    }
}
