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

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import net.opengis.gml.x32.AbstractCRSType;
import net.opengis.gml.x32.AbstractCoordinateSystemType;
import net.opengis.gml.x32.AbstractDatumType;
import net.opengis.gml.x32.AbstractGeometryType;
import net.opengis.gml.x32.AbstractRingPropertyType;
import net.opengis.gml.x32.AbstractRingType;
import net.opengis.gml.x32.AggregationType;
import net.opengis.gml.x32.CodeType;
import net.opengis.gml.x32.CodeWithAuthorityType;
import net.opengis.gml.x32.CoordinateSystemAxisDocument;
import net.opengis.gml.x32.CoordinateSystemAxisPropertyType;
import net.opengis.gml.x32.CoordinateSystemAxisType;
import net.opengis.gml.x32.CurvePropertyType;
import net.opengis.gml.x32.DefinitionType;
import net.opengis.gml.x32.DirectPositionListType;
import net.opengis.gml.x32.DirectPositionType;
import net.opengis.gml.x32.EnvelopeType;
import net.opengis.gml.x32.FeatureCollectionDocument;
import net.opengis.gml.x32.FeatureCollectionType;
import net.opengis.gml.x32.FeaturePropertyType;
import net.opengis.gml.x32.GenericMetaDataDocument;
import net.opengis.gml.x32.GenericMetaDataType;
import net.opengis.gml.x32.GeometryPropertyType;
import net.opengis.gml.x32.LineStringDocument;
import net.opengis.gml.x32.LineStringType;
import net.opengis.gml.x32.LinearRingType;
import net.opengis.gml.x32.MeasureType;
import net.opengis.gml.x32.MultiCurveDocument;
import net.opengis.gml.x32.MultiCurveType;
import net.opengis.gml.x32.MultiPointDocument;
import net.opengis.gml.x32.MultiPointType;
import net.opengis.gml.x32.PointDocument;
import net.opengis.gml.x32.PointType;
import net.opengis.gml.x32.PolygonDocument;
import net.opengis.gml.x32.PolygonType;
import net.opengis.gml.x32.ReferenceType;
import net.opengis.gml.x32.TimeIndeterminateValueType;
import net.opengis.gml.x32.TimeInstantDocument;
import net.opengis.gml.x32.TimeInstantPropertyType;
import net.opengis.gml.x32.TimeInstantType;
import net.opengis.gml.x32.TimePeriodDocument;
import net.opengis.gml.x32.TimePeriodPropertyType;
import net.opengis.gml.x32.TimePeriodType;
import net.opengis.gml.x32.TimePositionType;
import net.opengis.gml.x32.VerticalCRSPropertyType;
import net.opengis.gml.x32.VerticalCRSType;
import net.opengis.gml.x32.VerticalCSDocument;
import net.opengis.gml.x32.VerticalCSPropertyType;
import net.opengis.gml.x32.VerticalCSType;
import net.opengis.gml.x32.VerticalDatumDocument;
import net.opengis.gml.x32.VerticalDatumPropertyType;
import net.opengis.gml.x32.VerticalDatumType;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.isotc211.x2005.gmd.EXExtentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3.x1999.xlink.ActuateType;
import org.w3.x1999.xlink.ShowType;
import org.w3.x1999.xlink.TypeType;

import org.n52.faroe.annotation.Configurable;
import org.n52.faroe.annotation.Setting;
import org.n52.janmayen.function.Predicates;
import org.n52.shetland.ogc.HasDefaultEncoding;
import org.n52.shetland.ogc.OGCConstants;
import org.n52.shetland.ogc.gml.AbstractCRS;
import org.n52.shetland.ogc.gml.AbstractCoordinateSystem;
import org.n52.shetland.ogc.gml.AbstractDatum;
import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.gml.AbstractGeometry;
import org.n52.shetland.ogc.gml.CodeWithAuthority;
import org.n52.shetland.ogc.gml.CoordinateSystemAxis;
import org.n52.shetland.ogc.gml.Definition;
import org.n52.shetland.ogc.gml.DomainOfValidity;
import org.n52.shetland.ogc.gml.GenericMetaData;
import org.n52.shetland.ogc.gml.GmlConstants;
import org.n52.shetland.ogc.gml.VerticalCRS;
import org.n52.shetland.ogc.gml.VerticalCS;
import org.n52.shetland.ogc.gml.VerticalDatum;
import org.n52.shetland.ogc.gml.time.IndeterminateValue;
import org.n52.shetland.ogc.gml.time.Time;
import org.n52.shetland.ogc.gml.time.TimeInstant;
import org.n52.shetland.ogc.gml.time.TimePeriod;
import org.n52.shetland.ogc.gml.time.TimePosition;
import org.n52.shetland.ogc.om.features.FeatureCollection;
import org.n52.shetland.ogc.om.features.samplingFeatures.AbstractSamplingFeature;
import org.n52.shetland.ogc.om.features.samplingFeatures.SamplingFeature;
import org.n52.shetland.ogc.om.values.CategoryValue;
import org.n52.shetland.ogc.om.values.QuantityValue;
import org.n52.shetland.util.CRSHelper;
import org.n52.shetland.util.DateTimeFormatException;
import org.n52.shetland.util.DateTimeHelper;
import org.n52.shetland.util.EnvelopeOrGeometry;
import org.n52.shetland.util.JTSHelper;
import org.n52.shetland.util.JavaHelper;
import org.n52.shetland.util.MinMax;
import org.n52.shetland.util.OMHelper;
import org.n52.shetland.util.ReferencedEnvelope;
import org.n52.shetland.w3c.Nillable;
import org.n52.shetland.w3c.SchemaLocation;
import org.n52.shetland.w3c.xlink.Reference;
import org.n52.shetland.w3c.xlink.Referenceable;
import org.n52.svalbard.CodingSettings;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.exception.UnsupportedEncoderInputException;
import org.n52.svalbard.util.CodingHelper;
import org.n52.svalbard.util.XmlHelper;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.util.PolygonExtracter;

/**
 * @since 1.0.0
 *
 */
@Configurable
public class GmlEncoderv321
        extends AbstractGmlEncoderv321<XmlObject, Object> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GmlEncoderv321.class);

    private static final Set<EncoderKey> ENCODER_KEY_TYPES = CodingHelper.encoderKeysForElements(
            GmlConstants.NS_GML_32, org.n52.shetland.ogc.gml.time.Time.class,
            com.vividsolutions.jts.geom.Geometry.class, org.n52.shetland.ogc.om.values.CategoryValue.class,
            org.n52.shetland.ogc.gml.ReferenceType.class, org.n52.shetland.ogc.om.values.QuantityValue.class,
            org.n52.shetland.ogc.gml.CodeWithAuthority.class, org.n52.shetland.ogc.gml.CodeType.class,
            org.n52.shetland.ogc.om.features.samplingFeatures.SamplingFeature.class,
            org.n52.shetland.util.ReferencedEnvelope.class, org.n52.shetland.util.EnvelopeOrGeometry.class,
            org.n52.shetland.ogc.om.features.FeatureCollection.class, org.n52.shetland.ogc.gml.AbstractGeometry.class);

    private String srsNamePrefixURL = OGCConstants.URL_DEF_CRS_EPSG;
    private String srsNamePrefixURN = OGCConstants.URN_DEF_CRS_EPSG;

    public GmlEncoderv321() {
        LOGGER.debug("Encoder for the following keys initialized successfully: {}!",
                Joiner.on(", ").join(ENCODER_KEY_TYPES));
    }

    @Setting(CodingSettings.SRS_NAME_PREFIX_URL)
    public void setSrsNamePrefixURL(String prefix) {
        this.srsNamePrefixURL = CRSHelper.asHttpPrefix(prefix);
    }

    @Setting(CodingSettings.SRS_NAME_PREFIX_URN)
    public void setSrsNamePrefixURN(String prefix) {
        this.srsNamePrefixURN = CRSHelper.asUrnPrefix(prefix);
    }

    @Override
    public Set<EncoderKey> getKeys() {
        return Collections.unmodifiableSet(ENCODER_KEY_TYPES);
    }

    @Override
    public void addNamespacePrefixToMap(Map<String, String> nameSpacePrefixMap) {
        nameSpacePrefixMap.put(GmlConstants.NS_GML_32, GmlConstants.NS_GML_PREFIX);
    }

    @Override
    public Set<SchemaLocation> getSchemaLocations() {
        return Sets.newHashSet(GmlConstants.GML_32_SCHEMAL_LOCATION);
    }

    @Override
    public XmlObject encode(Object element, EncodingContext ctx) throws EncodingException {
        XmlObject encodedObject = null;
        if (element instanceof Time) {
            encodedObject = createTime((Time) element, ctx);
        } else if (element instanceof Geometry) {
            encodedObject = createPosition((Geometry) element, ctx);
        } else if (element instanceof CategoryValue) {
            encodedObject = createReferenceTypeForCategroyValue((CategoryValue) element);
        } else if (element instanceof org.n52.shetland.ogc.gml.ReferenceType) {
            encodedObject = createReferencType((org.n52.shetland.ogc.gml.ReferenceType) element);
        } else if (element instanceof CodeWithAuthority) {
            encodedObject = createCodeWithAuthorityType((CodeWithAuthority) element);
        } else if (element instanceof QuantityValue) {
            encodedObject = createMeasureType((QuantityValue) element);
        } else if (element instanceof org.n52.shetland.ogc.gml.CodeType) {
            encodedObject = createCodeType((org.n52.shetland.ogc.gml.CodeType) element);
        } else if (element instanceof AbstractFeature) {
            encodedObject = createFeaturePropertyType((AbstractFeature) element, ctx);
        } else if (element instanceof AbstractGeometry) {
            encodedObject = createGeomteryPropertyType((AbstractGeometry) element, ctx);
        } else if (element instanceof ReferencedEnvelope) {
            encodedObject = createEnvelope((ReferencedEnvelope) element);
        } else if (element instanceof EnvelopeOrGeometry) {
            if (((EnvelopeOrGeometry) element).isEnvelope()) {
                encodedObject = createEnvelope(((EnvelopeOrGeometry) element).getEnvelope().get());
            } else if (((EnvelopeOrGeometry) element).isGeometry()) {
                encodedObject = createPosition(((EnvelopeOrGeometry) element).getGeometry().get(), ctx);
            } else {
                throw new UnsupportedEncoderInputException(this, element);
            }
        } else if (element instanceof GenericMetaData) {
            encodedObject = createGenericMetaData((GenericMetaData) element, ctx);
        } else if (element instanceof VerticalDatum) {
            encodedObject = createVerticalDatum((VerticalDatum) element, ctx);
        } else if (element instanceof DomainOfValidity) {
            encodedObject = createDomainOfValidity((DomainOfValidity) element, ctx);
        } else if (element instanceof VerticalCRS) {
            encodedObject = createVerticalCRS((VerticalCRS) element, ctx);
        } else if (element instanceof VerticalCS) {
            encodedObject = createVerticalCS((VerticalCS) element, ctx);
        } else if (element instanceof CoordinateSystemAxis) {
            encodedObject = createCoordinateSystemAxis((CoordinateSystemAxis) element, ctx);
        } else {
            throw new UnsupportedEncoderInputException(this, element);
        }
        // LOGGER.debug("Encoded object {} is valid: {}",
        // encodedObject.schemaType().toString(),
        // XmlHelper.validateDocument(encodedObject));
        return encodedObject;
    }

    private XmlObject createFeaturePropertyType(AbstractFeature feature, EncodingContext ctx)
            throws EncodingException {
        if (feature instanceof FeatureCollection) {
            return createFeatureCollection((FeatureCollection) feature, ctx);
        } else if (feature instanceof SamplingFeature) {
            return createFeature(feature, ctx);
        } else if (feature.isSetDefaultElementEncoding()) {
            return encodeObjectToXml(feature.getDefaultElementEncoding(), feature);
        } else if (ctx.has(XmlEncoderFlags.ENCODE_NAMESPACE)
                && ctx.get(XmlEncoderFlags.ENCODE_NAMESPACE).isPresent()) {
            return encodeObjectToXml((String) ctx.get(XmlEncoderFlags.ENCODE_NAMESPACE).get(), feature, ctx);
        } else {
            throw new UnsupportedEncoderInputException(this, feature);
        }
    }

    private XmlObject createFeatureCollection(FeatureCollection element, EncodingContext ctx)
            throws EncodingException {
        FeatureCollectionDocument featureCollectionDoc =
                FeatureCollectionDocument.Factory.newInstance(getXmlOptions());
        FeatureCollectionType featureCollection = featureCollectionDoc.addNewFeatureCollection();
        featureCollection.setId(element.getGmlId());
        EncodingContext context =
                ctx.with(XmlBeansEncodingFlags.PROPERTY_TYPE).without(XmlBeansEncodingFlags.DOCUMENT);

        if (element.isSetMembers()) {
            for (AbstractFeature abstractFeature : element.getMembers().values()) {
                featureCollection.addNewFeatureMember().set(createFeaturePropertyType(abstractFeature, context));
            }
        }
        if (ctx.has(XmlBeansEncodingFlags.DOCUMENT)) {
            return featureCollectionDoc;
        }
        FeaturePropertyType featurePropertyType = FeaturePropertyType.Factory.newInstance(getXmlOptions());
        featurePropertyType.addNewAbstractFeature().set(featureCollection);
        return XmlHelper.substituteElement(featurePropertyType.getAbstractFeature(), featurePropertyType);
        // return featureCollection;
    }

    private XmlObject createFeature(AbstractFeature feature, EncodingContext ctx) throws EncodingException {
        FeaturePropertyType featurePropertyType = FeaturePropertyType.Factory.newInstance(getXmlOptions());
        if (isNotSamplingFeature(feature) || ctx.has(XmlBeansEncodingFlags.REFERENCED)) {
            featurePropertyType.setHref(feature.getIdentifierCodeWithAuthority().getValue());
            return featurePropertyType;
        } else {
            AbstractSamplingFeature samplingFeature = (AbstractSamplingFeature) feature;
            if (samplingFeature.isSetGmlID()) {
                featurePropertyType.setHref("#" + samplingFeature.getGmlId());
                return featurePropertyType;
            } else {
                if (ctx.has(XmlBeansEncodingFlags.ENCODE) && !ctx.getBoolean(XmlBeansEncodingFlags.ENCODE)
                        || !samplingFeature.isEncode()) {
                    featurePropertyType.setHref(feature.getIdentifierCodeWithAuthority().getValue());
                    if (feature instanceof SamplingFeature && samplingFeature.isSetName()) {
                        featurePropertyType.setTitle(samplingFeature.getFirstName().getValue());
                    }
                    return featurePropertyType;
                }
                if (!samplingFeature.isSetGeometry()) {
                    featurePropertyType.setHref(samplingFeature.getIdentifierCodeWithAuthority().getValue());
                    if (samplingFeature.isSetName()) {
                        featurePropertyType.setTitle(samplingFeature.getFirstName().getValue());
                    }
                    return featurePropertyType;
                }
                if (samplingFeature.isSetUrl()) {
                    featurePropertyType.setHref(samplingFeature.getUrl());
                    if (samplingFeature.isSetName()) {
                        featurePropertyType.setTitle(samplingFeature.getFirstName().getValue());
                    }
                    return featurePropertyType;
                } else {
                    String namespace = ctx.getString(XmlEncoderFlags.ENCODE_NAMESPACE)
                            .orElseGet(() -> OMHelper.getNamespaceForFeatureType(samplingFeature.getFeatureType()));
                    XmlObject encodedXmlObject = encodeObjectToXml(namespace, samplingFeature);

                    if (encodedXmlObject != null) {
                        return encodedXmlObject;
                    } else {
                        if (feature.isSetXml()) {
                            try {
                                // TODO how set gml:id in already existing
                                // XmlDescription? <-- XmlCursor
                                return XmlObject.Factory.parse(feature.getXml());
                            } catch (XmlException xmle) {
                                throw new EncodingException("Error while encoding featurePropertyType!", xmle);
                            }
                        } else {
                            featurePropertyType.setHref(feature.getIdentifierCodeWithAuthority().getValue());
                            if (samplingFeature.isSetName()) {
                                featurePropertyType.setTitle(feature.getFirstName().getValue());
                            }
                            return featurePropertyType;
                        }
                    }
                }
            }
        }
    }

    @Override
    protected XmlObject createFeature(FeaturePropertyType featurePropertyType, AbstractFeature abstractFeature,
            EncodingContext ctx) throws EncodingException {
        return featurePropertyType.set(createFeature(abstractFeature, ctx));
    }

    private boolean isNotSamplingFeature(AbstractFeature feature) {
        return !(feature instanceof SamplingFeature);
    }

    private XmlObject createEnvelope(ReferencedEnvelope sosEnvelope) {
        int srid = sosEnvelope.getSrid();
        EnvelopeType envelopeType = EnvelopeType.Factory.newInstance();
        MinMax<String> minmax = sosEnvelope.getMinMaxFromEnvelope();
        envelopeType.addNewLowerCorner().setStringValue(minmax.getMinimum());
        envelopeType.addNewUpperCorner().setStringValue(minmax.getMaximum());
        envelopeType.setSrsName(getSrsName(srid));
        return envelopeType;
    }

    private XmlObject createTime(Time time, EncodingContext ctx) throws EncodingException {
        if (time == null) {
            return null;
        }

        if (time instanceof TimeInstant) {
            TimeInstant instant = (TimeInstant) time;

            if (ctx.has(XmlBeansEncodingFlags.DOCUMENT)) {
                return createTimeInstantDocument(instant);
            }

            if (ctx.has(XmlBeansEncodingFlags.PROPERTY_TYPE)) {
                return createTimeInstantPropertyType(instant);
            }

            return createTimeInstantType(instant);
        }

        if (time instanceof TimePeriod) {
            TimePeriod period = (TimePeriod) time;

            if (ctx.has(XmlBeansEncodingFlags.DOCUMENT)) {
                return createTimePeriodDocument(period);
            }

            if (ctx.has(XmlBeansEncodingFlags.PROPERTY_TYPE)) {
                return createTimePeriodPropertyType(period);
            }

            return createTimePeriodType(period);
        }

        throw new UnsupportedEncoderInputException(this, time);
    }

    private XmlObject createTimePeriodDocument(TimePeriod time) throws EncodingException {
        TimePeriodDocument timePeriodDoc = TimePeriodDocument.Factory.newInstance(getXmlOptions());
        createTimePeriodType(time, timePeriodDoc.addNewTimePeriod());
        return timePeriodDoc;
    }

    private XmlObject createTimePeriodPropertyType(TimePeriod time) throws EncodingException {
        TimePeriodPropertyType timePeriodPropertyType = TimePeriodPropertyType.Factory.newInstance(getXmlOptions());
        createTimePeriodType(time, timePeriodPropertyType.addNewTimePeriod());
        return timePeriodPropertyType;
    }

    /**
     * Creates a XML TimePeriod from the SOS time object.
     *
     * @param timePeriod
     *            SOS time object
     * @param timePeriodType
     *
     * @throws EncodingException
     *             * if an error occurs.
     */
    private void createTimePeriodType(TimePeriod timePeriod, TimePeriodType timePeriodType) throws EncodingException {
        if (timePeriod.getGmlId() != null && !timePeriod.getGmlId().isEmpty()) {
            timePeriodType.setId(timePeriod.getGmlId());
        } else {
            timePeriodType.setId("tp_" + JavaHelper.generateID(timePeriod.toString() + System.currentTimeMillis()));
        }
        timePeriodType.setBeginPosition(createTimePositionType(timePeriod.getStartTimePosition()));
        timePeriodType.setEndPosition(createTimePositionType(timePeriod.getEndTimePosition()));
    }

    private TimePeriodType createTimePeriodType(TimePeriod timePeriod) throws EncodingException {
        TimePeriodType timePeriodType = TimePeriodType.Factory.newInstance(getXmlOptions());
        createTimePeriodType(timePeriod, timePeriodType);
        return timePeriodType;
    }

    private XmlObject createTimeInstantDocument(TimeInstant time) throws EncodingException {
        TimeInstantDocument timeInstantDoc = TimeInstantDocument.Factory.newInstance(getXmlOptions());
        createTimeInstantType(time, timeInstantDoc.addNewTimeInstant());
        return timeInstantDoc;
    }

    private XmlObject createTimeInstantPropertyType(TimeInstant time) throws EncodingException {
        TimeInstantPropertyType timeInstantPropertyType = TimeInstantPropertyType.Factory.newInstance(getXmlOptions());
        createTimeInstantType(time, timeInstantPropertyType.addNewTimeInstant());
        return timeInstantPropertyType;
    }

    /**
     * Creates a XML TimeInstant from the SOS time object.
     *
     * @param timeInstant
     *            SOS time object
     * @param timeInstantType
     *
     *
     * @throws EncodingException
     *             if an error occurs.
     */
    private void createTimeInstantType(TimeInstant timeInstant, TimeInstantType timeInstantType)
            throws EncodingException {
        // create time instant
        if (timeInstant.isSetGmlId()) {
            timeInstantType.setId(timeInstant.getGmlId());
        } else {
            timeInstantType
                    .setId("ti_" + JavaHelper.generateID(timeInstantType.toString() + System.currentTimeMillis()));
        }
        timeInstantType.setTimePosition(createTimePositionType(timeInstant.getTimePosition()));
    }

    private TimeInstantType createTimeInstantType(TimeInstant timeInstant) throws EncodingException {
        TimeInstantType timeInstantType = TimeInstantType.Factory.newInstance(getXmlOptions());
        createTimeInstantType(timeInstant, timeInstantType);
        return timeInstantType;
    }

    private TimePositionType createTimePositionType(TimePosition timePosition) throws DateTimeFormatException {
        TimePositionType xbTimePosition = TimePositionType.Factory.newInstance();
        if (!timePosition.isSetTime()) {
            String indeterminateValue = Optional.ofNullable(timePosition.getIndeterminateValue())
                    .orElse(IndeterminateValue.UNKNOWN).getValue();
            if (TimeIndeterminateValueType.Enum.forString(indeterminateValue) != null) {
                xbTimePosition.setIndeterminatePosition(TimeIndeterminateValueType.Enum.forString(indeterminateValue));
            } else {
                xbTimePosition.setStringValue(indeterminateValue);
            }
        } else {
            String endString = DateTimeHelper.formatDateTime2String(timePosition);

            // concat minutes for timeZone offset, because gml requires
            // xs:dateTime, which needs minutes in
            // timezone offset
            // TODO enable really
            xbTimePosition.setStringValue(endString);
        }
        return xbTimePosition;
    }

    private XmlObject createGeomteryPropertyType(AbstractGeometry element, EncodingContext ctx)
            throws EncodingException {
        GeometryPropertyType geometryPropertyType = GeometryPropertyType.Factory.newInstance();
        if (element.isReferenced()) {
            geometryPropertyType.setHref(element.getGmlId());
        } else {
            AbstractGeometryType xmlObject = createAbstractGeometry(element, ctx);
            geometryPropertyType.setAbstractGeometry(xmlObject);
            XmlHelper.substituteElement(geometryPropertyType.getAbstractGeometry(), xmlObject);
        }

        return geometryPropertyType;
    }

    private AbstractGeometryType createAbstractGeometry(AbstractGeometry element, EncodingContext ctx)
            throws EncodingException {
        XmlObject xbGeometry = createPosition(element.getGeometry(), ctx);
        AbstractGeometryType abstractGeometryType = null;
        if (xbGeometry instanceof AbstractGeometryType) {
            abstractGeometryType = (AbstractGeometryType) xbGeometry;
        } else if (xbGeometry instanceof GeometryPropertyType) {
            abstractGeometryType = ((GeometryPropertyType) xbGeometry).getAbstractGeometry();
        } else {
            throw new UnsupportedEncoderInputException(this, element);
        }

        if (element.isSetIdentifier()) {
            abstractGeometryType.setIdentifier(createCodeWithAuthorityType(element.getIdentifierCodeWithAuthority()));
        }
        if (element.isSetName()) {
            for (org.n52.shetland.ogc.gml.CodeType codeType : element.getName()) {
                abstractGeometryType.addNewName().set(createCodeType(codeType));
            }
        }
        if (element.isSetDescription()) {
            abstractGeometryType.addNewDescription().setStringValue(element.getDescription());
        }
        return abstractGeometryType;
    }

    private XmlObject createPosition(Geometry geom, EncodingContext ctx) throws EncodingException {
        String foiId = ctx.<String>get(XmlBeansEncodingFlags.GMLID).orElse(null);
        if (geom instanceof Point) {
            PointType xbPoint = PointType.Factory.newInstance(getXmlOptions());
            xbPoint.setId(getGmlID(geom, foiId));
            createPointFromJtsGeometry((Point) geom, xbPoint);
            if (ctx.has(XmlBeansEncodingFlags.DOCUMENT)) {
                PointDocument xbPointDoc = PointDocument.Factory.newInstance(getXmlOptions());
                xbPointDoc.setPoint(xbPoint);
                return xbPointDoc;
            } else if (ctx.has(XmlBeansEncodingFlags.PROPERTY_TYPE)) {
                GeometryPropertyType geometryPropertyType = GeometryPropertyType.Factory.newInstance(getXmlOptions());
                geometryPropertyType.setAbstractGeometry(xbPoint);
                geometryPropertyType.getAbstractGeometry().substitute(GmlConstants.QN_POINT_32, PointType.type);
                return geometryPropertyType;
            }
            return xbPoint;
        } else if (geom instanceof LineString) {
            LineStringType xbLineString = LineStringType.Factory.newInstance(getXmlOptions());
            xbLineString.setId(getGmlID(geom, foiId));
            createLineStringFromJtsGeometry((LineString) geom, xbLineString);
            if (ctx.has(XmlBeansEncodingFlags.DOCUMENT)) {
                LineStringDocument xbLineStringDoc = LineStringDocument.Factory.newInstance(getXmlOptions());
                xbLineStringDoc.setLineString(xbLineString);
                return xbLineStringDoc;
            } else if (ctx.has(XmlBeansEncodingFlags.PROPERTY_TYPE)) {
                GeometryPropertyType geometryPropertyType = GeometryPropertyType.Factory.newInstance(getXmlOptions());
                geometryPropertyType.setAbstractGeometry(xbLineString);
                geometryPropertyType.getAbstractGeometry().substitute(GmlConstants.QN_LINESTRING_32,
                        LineStringType.type);
                return geometryPropertyType;
            }
            return xbLineString;
        } else if (geom instanceof MultiLineString) {
            MultiCurveType xbMultiCurve = MultiCurveType.Factory.newInstance(getXmlOptions());
            xbMultiCurve.setId(getGmlID(geom, foiId));
            xbMultiCurve.setSrsName(getSrsName(geom));
            for (int i = 0; i < geom.getNumGeometries(); ++i) {
                Geometry lineString = geom.getGeometryN(i);
                LineStringType xbLineString = LineStringType.Factory.newInstance(getXmlOptions());
                xbLineString.setId(getGmlID(geom, foiId));
                xbLineString.addNewPosList().setStringValue(JTSHelper.getCoordinatesString(lineString));
                CurvePropertyType xbCurveMember = xbMultiCurve.addNewCurveMember();
                xbCurveMember.addNewAbstractCurve().set(xbLineString);
                XmlHelper.substituteElement(xbCurveMember.getAbstractCurve(), xbLineString);
            }
            if (ctx.has(XmlBeansEncodingFlags.DOCUMENT)) {
                MultiCurveDocument xbMultiCurveDoc = MultiCurveDocument.Factory.newInstance(getXmlOptions());
                xbMultiCurveDoc.setMultiCurve(xbMultiCurve);
                return xbMultiCurveDoc;
            } else if (ctx.has(XmlBeansEncodingFlags.PROPERTY_TYPE)) {
                GeometryPropertyType xbGeometryProperty = GeometryPropertyType.Factory.newInstance(getXmlOptions());
                xbGeometryProperty.addNewAbstractGeometry().set(xbMultiCurve);
                XmlHelper.substituteElement(xbGeometryProperty.getAbstractGeometry(), xbMultiCurve);
                return xbGeometryProperty;
            } else {
                return xbMultiCurve;
            }
        } else if (geom instanceof Polygon) {
            PolygonType xbPolygon = PolygonType.Factory.newInstance(getXmlOptions());
            xbPolygon.setId(getGmlID(geom, foiId));
            createPolygonFromJtsGeometry((Polygon) geom, xbPolygon);
            if (ctx.has(XmlBeansEncodingFlags.DOCUMENT)) {
                PolygonDocument xbPolygonDoc = PolygonDocument.Factory.newInstance(getXmlOptions());
                xbPolygonDoc.setPolygon(xbPolygon);
                return xbPolygonDoc;
            } else if (ctx.has(XmlBeansEncodingFlags.PROPERTY_TYPE)) {
                GeometryPropertyType geometryPropertyType = GeometryPropertyType.Factory.newInstance(getXmlOptions());
                geometryPropertyType.setAbstractGeometry(xbPolygon);
                geometryPropertyType.getAbstractGeometry().substitute(GmlConstants.QN_POLYGON_32, PolygonType.type);
                return geometryPropertyType;
            }
            return xbPolygon;
        } else if (geom instanceof MultiPoint) {
            MultiPointType xbMultiPoint = MultiPointType.Factory.newInstance(getXmlOptions());
            String id = getGmlID(geom, foiId);
            xbMultiPoint.setId(id);
            createMultiPointFromJtsGeometry((MultiPoint) geom, xbMultiPoint, id);

            if (ctx.has(XmlBeansEncodingFlags.DOCUMENT)) {
                MultiPointDocument xbMultiPointDoc = MultiPointDocument.Factory.newInstance(getXmlOptions());
                xbMultiPointDoc.setMultiPoint(xbMultiPoint);
                return xbMultiPointDoc;
            } else if (ctx.has(XmlBeansEncodingFlags.PROPERTY_TYPE)) {
                GeometryPropertyType geometryPropertyType = GeometryPropertyType.Factory.newInstance(getXmlOptions());
                geometryPropertyType.setAbstractGeometry(xbMultiPoint);
                geometryPropertyType.getAbstractGeometry().substitute(GmlConstants.QN_MULTI_POINT_32,
                        PolygonType.type);
                return geometryPropertyType;
            }
            return xbMultiPoint;
        } else {
            throw new UnsupportedEncoderInputException(this, geom);
        }
    }

    @SuppressWarnings("rawtypes")
    private String getGmlID(Geometry geom, String gmlId) {
        String id;
        if (!Strings.isNullOrEmpty(gmlId)) {
            id = gmlId;
        } else if (geom.getUserData() != null && geom.getUserData() instanceof Map
                && ((Map) geom.getUserData()).containsKey(XmlBeansEncodingFlags.GMLID.name())) {
            id = (String) ((Map) geom.getUserData()).get(XmlBeansEncodingFlags.GMLID.name());
        } else {
            id = JavaHelper.generateID(geom.toText());
        }
        return geom.getGeometryType() + "_" + id;
    }

    /**
     * Creates a XML Point from a SOS Point.
     *
     * @param jtsPoint
     *            SOS Point
     * @param xbPoint
     *            XML Point
     */
    private void createPointFromJtsGeometry(Point jtsPoint, PointType xbPoint) {
        DirectPositionType xbPos = xbPoint.addNewPos();
        xbPos.setSrsName(getSrsName(jtsPoint));
        xbPos.setStringValue(JTSHelper.getCoordinatesString(jtsPoint));
    }

    /**
     * Creates a XML LineString from a SOS LineString.
     *
     * @param jtsLineString
     *            SOS LineString
     * @param xbLst
     *            XML LinetSring
     */
    private void createLineStringFromJtsGeometry(LineString jtsLineString, LineStringType xbLst) {
        String srsName = getSrsName(jtsLineString);
        xbLst.setSrsName(srsName);
        DirectPositionListType xbPosList = xbLst.addNewPosList();
        xbPosList.setSrsName(srsName);
        xbPosList.setStringValue(JTSHelper.getCoordinatesString(jtsLineString));

    }

    /**
     * Creates a XML Polygon from a SOS Polygon.
     *
     * @param jtsPolygon
     *            SOS Polygon
     * @param xbPolType
     *            XML Polygon
     */
    private void createPolygonFromJtsGeometry(Polygon jtsPolygon, PolygonType xbPolType) {
        List<?> jtsPolygons = PolygonExtracter.getPolygons(jtsPolygon);
        String srsName = getSrsName(jtsPolygon);

        for (int i = 0; i < jtsPolygons.size(); i++) {

            Polygon pol = (Polygon) jtsPolygons.get(i);

            AbstractRingPropertyType xbArpt = xbPolType.addNewExterior();
            AbstractRingType xbArt = xbArpt.addNewAbstractRing();

            LinearRingType xbLrt = LinearRingType.Factory.newInstance();

            // Exterior ring
            LineString ring = pol.getExteriorRing();
            DirectPositionListType xbPosList = xbLrt.addNewPosList();

            xbPosList.setSrsName(srsName);
            xbPosList.setStringValue(JTSHelper.getCoordinatesString(ring));
            xbArt.set(xbLrt);

            // Rename element name for output
            XmlCursor cursor = xbArpt.newCursor();
            if (cursor.toChild(GmlConstants.QN_ABSTRACT_RING_32)) {
                cursor.setName(GmlConstants.QN_LINEAR_RING_32);
            }

            // Interior ring
            int numberOfInteriorRings = pol.getNumInteriorRing();
            for (int ringNumber = 0; ringNumber < numberOfInteriorRings; ringNumber++) {
                xbArpt = xbPolType.addNewInterior();
                xbArt = xbArpt.addNewAbstractRing();

                xbLrt = LinearRingType.Factory.newInstance();

                ring = pol.getInteriorRingN(ringNumber);

                xbPosList = xbLrt.addNewPosList();
                xbPosList.setSrsName(srsName);
                xbPosList.setStringValue(JTSHelper.getCoordinatesString(ring));
                xbArt.set(xbLrt);

                // Rename element name for output
                cursor = xbArpt.newCursor();
                if (cursor.toChild(GmlConstants.QN_ABSTRACT_RING_32)) {
                    cursor.setName(GmlConstants.QN_LINEAR_RING_32);
                }
            }
        }
    }

    private void createMultiPointFromJtsGeometry(MultiPoint geom, MultiPointType xbMultiPoint, String id)
            throws EncodingException {
        for (int i = 0; i < geom.getNumGeometries(); i++) {
            Geometry geometry = geom.getGeometryN(i);
            if (geometry instanceof Point) {
                PointType pt = xbMultiPoint.addNewPointMember().addNewPoint();
                pt.setId(id + "_" + i);
                createPointFromJtsGeometry((Point) geometry, pt);
            }
        }
    }

    private XmlObject createReferenceTypeForCategroyValue(CategoryValue categoryValue) {
        ReferenceType xbRef = ReferenceType.Factory.newInstance(getXmlOptions());
        if (categoryValue.isSetValue()) {
            if (categoryValue.getValue().startsWith("http://")) {
                xbRef.setHref(categoryValue.getValue());
            } else {
                xbRef.setTitle(categoryValue.getValue());
            }
            if (categoryValue.isSetUnit()) {
                xbRef.setRole(categoryValue.getUnit());
            }
        } else {
            xbRef.setNil();
        }
        return xbRef;
    }

    private ReferenceType createReferencType(org.n52.shetland.ogc.gml.ReferenceType sosReferenceType) {
        if (!sosReferenceType.isSetHref()) {
            String exceptionText = String.format("The required 'href' parameter is empty for encoding %s!",
                    ReferenceType.class.getName());
            LOGGER.error(exceptionText);
            throw new IllegalArgumentException(exceptionText);
        }
        ReferenceType referenceType = ReferenceType.Factory.newInstance();
        referenceType.setHref(sosReferenceType.getHref());
        if (sosReferenceType.isSetTitle()) {
            referenceType.setTitle(sosReferenceType.getTitle());
        }
        if (sosReferenceType.isSetRole()) {
            referenceType.setRole(sosReferenceType.getRole());
        }
        return referenceType;
    }

    private CodeWithAuthorityType createCodeWithAuthorityType(CodeWithAuthority sosCodeWithAuthority)
            throws EncodingException {
        if (!sosCodeWithAuthority.isSetValue()) {
            throw missingValueParameter(CodeWithAuthorityType.class.getName());
        }
        CodeWithAuthorityType codeWithAuthority = CodeWithAuthorityType.Factory.newInstance(getXmlOptions());
        codeWithAuthority.setStringValue(sosCodeWithAuthority.getValue());
        codeWithAuthority.setCodeSpace(Optional.ofNullable(sosCodeWithAuthority.getCodeSpace())
                .filter(Predicates.not(String::isEmpty)).orElse(OGCConstants.UNKNOWN));
        return codeWithAuthority;
    }

    private CodeType createCodeType(org.n52.shetland.ogc.gml.CodeType sosCodeType) throws EncodingException {
        if (!sosCodeType.isSetValue()) {
            throw missingValueParameter(CodeType.class.getName());
        }
        CodeType codeType = CodeType.Factory.newInstance(getXmlOptions());
        codeType.setStringValue(sosCodeType.getValue());
        codeType.setCodeSpace(Optional.ofNullable(sosCodeType.getCodeSpace()).map(URI::toString)
                .filter(Predicates.not(String::isEmpty)).orElse(OGCConstants.UNKNOWN));
        return codeType;
    }

    private XmlObject createGenericMetaData(GenericMetaData element, EncodingContext ctx) throws EncodingException {
        GenericMetaDataDocument gmdd = GenericMetaDataDocument.Factory.newInstance(getXmlOptions());
        GenericMetaDataType gmdt = gmdd.addNewGenericMetaData();
        if (element.getContent() instanceof HasDefaultEncoding
                && ((HasDefaultEncoding<?>) element.getContent()).isSetDefaultElementEncoding()) {
            // TODO check
            gmdt.set(encodeObjectToXml(((HasDefaultEncoding<?>) element.getContent()).getDefaultElementEncoding(),
                    element.getContent(), new EncodingContext().with(XmlBeansEncodingFlags.PROPERTY_TYPE, true)));
        }
        if (ctx.has(XmlBeansEncodingFlags.DOCUMENT)) {
            return gmdd;
        }
        return gmdt;
    }

    protected MeasureType createMeasureType(QuantityValue quantityValue) throws EncodingException {
        if (!quantityValue.isSetValue()) {
            throw missingValueParameter(MeasureType.class.getName());
        }
        MeasureType measureType = MeasureType.Factory.newInstance(getXmlOptions());
        measureType.setDoubleValue(quantityValue.getValue());
        measureType.setUom(Optional.ofNullable(quantityValue.getUnit()).filter(Predicates.not(String::isEmpty))
                .orElse(OGCConstants.UNKNOWN));
        return measureType;
    }

    private XmlObject createVerticalDatum(VerticalDatum verticalDatum, EncodingContext ctx) throws EncodingException {
        VerticalDatumType vdt = VerticalDatumType.Factory.newInstance();
        addAbstractDatumValues(vdt, verticalDatum, ctx);
        if (ctx.has(XmlBeansEncodingFlags.DOCUMENT)) {
            VerticalDatumDocument vdd = VerticalDatumDocument.Factory.newInstance();
            VerticalDatumPropertyType vdpt = VerticalDatumPropertyType.Factory.newInstance();
            vdpt.setVerticalDatum(vdt);
            vdd.setVerticalDatum(vdpt);
            return vdd;
        } else if (ctx.has(XmlBeansEncodingFlags.PROPERTY_TYPE)) {
            VerticalDatumPropertyType vdpt = VerticalDatumPropertyType.Factory.newInstance();
            vdpt.setVerticalDatum(vdt);
            return vdpt;
        }
        return vdt;
    }

    private void addAbstractDatumValues(AbstractDatumType adt, AbstractDatum abstractDatum, EncodingContext ctx)
            throws EncodingException {
        addDefinitonValues(adt, abstractDatum);
        if (abstractDatum.hasAnchorDefinition()) {
            adt.setAnchorDefinition(createCodeType(abstractDatum.getAnchorDefinition()));
        }
        if (abstractDatum.hasDomainOfValidity()) {
            net.opengis.gml.x32.DomainOfValidityDocument.DomainOfValidity dov = adt.addNewDomainOfValidity();
            Referenceable<DomainOfValidity> domainOfValidity = abstractDatum.getDomainOfValidity();
            if (domainOfValidity.isReference()) {
                Reference reference = domainOfValidity.getReference();
                if (reference.getActuate().isPresent()) {
                    dov.setActuate(ActuateType.Enum.forString(reference.getActuate().get()));
                }
                if (reference.getArcrole().isPresent()) {
                    dov.setHref(reference.getArcrole().get());
                }
                if (reference.getHref().isPresent()) {
                    dov.setHref(reference.getHref().get().toString());
                }
                if (reference.getRole().isPresent()) {
                    dov.setRole(reference.getRole().get());
                }
                if (reference.getShow().isPresent()) {
                    dov.setShow(ShowType.Enum.forString(reference.getShow().get()));
                }
                if (reference.getTitle().isPresent()) {
                    dov.setTitle(reference.getTitle().get());
                }
                if (reference.getType().isPresent()) {
                    dov.setType(TypeType.Enum.forString(reference.getType().get()));
                }
            } else {
                if (domainOfValidity.isInstance()) {
                    Nillable<DomainOfValidity> nillable = domainOfValidity.getInstance();
                    if (nillable.isPresent()) {
                        net.opengis.gml.x32.DomainOfValidityDocument.DomainOfValidity xml =
                                createDomainOfValidity(nillable.get(), EncodingContext.empty());
                        if (xml != null) {
                            dov.set(xml);
                        } else {
                            dov.setNil();
                            dov.setNilReason(Nillable.missing().get());
                        }
                    } else {
                        dov.setNil();
                        if (nillable.hasReason()) {
                            dov.setNilReason(nillable.getNilReason().get());
                        } else {
                            dov.setNilReason(Nillable.missing().get());
                        }
                    }
                }
            }
        }
        if (abstractDatum.hasRealizationEpoch()) {
            adt.setRealizationEpoch(abstractDatum.getRealizationEpoch().toCalendar(Locale.ROOT));
        }
        abstractDatum.getScope().forEach(scope -> adt.addNewScope().setStringValue(scope));
    }

    private net.opengis.gml.x32.DomainOfValidityDocument.DomainOfValidity createDomainOfValidity(
            DomainOfValidity domainOfValidity, EncodingContext ctx) throws EncodingException {
        net.opengis.gml.x32.DomainOfValidityDocument.DomainOfValidity dov =
                net.opengis.gml.x32.DomainOfValidityDocument.DomainOfValidity.Factory.newInstance();
        if (domainOfValidity.hasExExtent()) {
            EXExtentType exet = dov.addNewEXExtent();
            XmlObject xml = encodeObjectToXml(domainOfValidity.getExExtent().getDefaultElementEncoding(),
                    domainOfValidity.getExExtent());
            if (xml != null) {
                exet.set(xml);
            }
        }
        return dov;
    }

    private XmlObject createVerticalCRS(VerticalCRS verticalCRS, EncodingContext ctx) throws EncodingException {
        VerticalCRSType vcrst = VerticalCRSType.Factory.newInstance();
        addAbstractCRSValues(vcrst, verticalCRS);
        // verticalCS
        Referenceable<VerticalCS> verticalCS = verticalCRS.getVerticalCS();
        VerticalCSPropertyType vcspt = vcrst.addNewVerticalCS();
        if (verticalCS.isReference()) {
            Reference reference = verticalCS.getReference();
            if (reference.getActuate().isPresent()) {
                vcspt.setActuate(ActuateType.Enum.forString(reference.getActuate().get()));
            }
            if (reference.getArcrole().isPresent()) {
                vcspt.setHref(reference.getArcrole().get());
            }
            if (reference.getHref().isPresent()) {
                vcspt.setHref(reference.getHref().get().toString());
            }
            if (reference.getRole().isPresent()) {
                vcspt.setRole(reference.getRole().get());
            }
            if (reference.getShow().isPresent()) {
                vcspt.setShow(ShowType.Enum.forString(reference.getShow().get()));
            }
            if (reference.getTitle().isPresent()) {
                vcspt.setTitle(reference.getTitle().get());
            }
            if (reference.getType().isPresent()) {
                vcspt.setType(TypeType.Enum.forString(reference.getType().get()));
            }
        } else {
            if (verticalCS.isInstance()) {
                Nillable<VerticalCS> nillable = verticalCS.getInstance();
                if (nillable.isPresent()) {
                    XmlObject xml = createVerticalCS(nillable.get(), EncodingContext.empty());
                    if (xml != null && xml instanceof VerticalCSType) {
                        vcspt.set((VerticalCSType) xml);
                    } else {
                        vcspt.setNil();
                        vcspt.setNilReason(Nillable.missing().get());
                    }
                } else {
                    vcspt.setNil();
                    if (nillable.hasReason()) {
                        vcspt.setNilReason(nillable.getNilReason().get());
                    } else {
                        vcspt.setNilReason(Nillable.missing().get());
                    }
                }
            }
        }
        // verticalDatum
        Referenceable<VerticalDatum> verticalDatum = verticalCRS.getVerticalDatum();
        VerticalDatumPropertyType vdpt = vcrst.addNewVerticalDatum();
        if (verticalDatum.isReference()) {
            Reference reference = verticalDatum.getReference();
            if (reference.getActuate().isPresent()) {
                vdpt.setActuate(ActuateType.Enum.forString(reference.getActuate().get()));
            }
            if (reference.getArcrole().isPresent()) {
                vdpt.setHref(reference.getArcrole().get());
            }
            if (reference.getHref().isPresent()) {
                vdpt.setHref(reference.getHref().get().toString());
            }
            if (reference.getRole().isPresent()) {
                vdpt.setRole(reference.getRole().get());
            }
            if (reference.getShow().isPresent()) {
                vdpt.setShow(ShowType.Enum.forString(reference.getShow().get()));
            }
            if (reference.getTitle().isPresent()) {
                vdpt.setTitle(reference.getTitle().get());
            }
            if (reference.getType().isPresent()) {
                vdpt.setType(TypeType.Enum.forString(reference.getType().get()));
            }
        } else {
            if (verticalDatum.isInstance()) {
                Nillable<VerticalDatum> nillable = verticalDatum.getInstance();
                if (nillable.isPresent()) {
                    XmlObject xml = createVerticalDatum(nillable.get(), EncodingContext.empty());
                    if (xml != null && xml instanceof VerticalDatumType) {
                        vdpt.setVerticalDatum((VerticalDatumType) xml);
                    } else {
                        vdpt.setNil();
                        vdpt.setNilReason(Nillable.missing().get());
                    }
                } else {
                    vdpt.setNil();
                    if (nillable.hasReason()) {
                        vdpt.setNilReason(nillable.getNilReason().get());
                    } else {
                        vdpt.setNilReason(Nillable.missing().get());
                    }
                }
            }
        }
        if (ctx.has(XmlBeansEncodingFlags.PROPERTY_TYPE)) {
            VerticalCRSPropertyType vcrspt = VerticalCRSPropertyType.Factory.newInstance();
            vcrspt.setVerticalCRS(vcrst);
            return vcrspt;
        }
        return vcrst;
    }

    private void addDefinitonValues(DefinitionType dt, Definition definition) throws EncodingException {
        if (!definition.isSetGmlID()) {
            definition.setGmlId("id_" + JavaHelper.generateID(definition.getGmlId()));
        }
        dt.setId(definition.getGmlId());
        if (!addIdentifier(dt, definition)) {
            dt.setIdentifier(
                    createCodeWithAuthorityType(new CodeWithAuthority(JavaHelper.generateID(definition.toString()))));
        }
        addName(dt, definition);
        addDescription(dt, definition);
        if (definition.hasRemarks()) {
            dt.setRemarks(definition.getRemarks());
        }
    }

    private void addAbstractCRSValues(AbstractCRSType acrst, AbstractCRS abstractCRS) throws EncodingException {
        addDefinitonValues(acrst, abstractCRS);
        if (abstractCRS.hasDomainOfValidity()) {
            for (Referenceable<DomainOfValidity> domainOfValidity : abstractCRS.getDomainOfValidity()) {
                net.opengis.gml.x32.DomainOfValidityDocument.DomainOfValidity dov = acrst.addNewDomainOfValidity();
                if (domainOfValidity.isReference()) {
                    Reference reference = domainOfValidity.getReference();
                    if (reference.getActuate().isPresent()) {
                        dov.setActuate(ActuateType.Enum.forString(reference.getActuate().get()));
                    }
                    if (reference.getArcrole().isPresent()) {
                        dov.setHref(reference.getArcrole().get());
                    }
                    if (reference.getHref().isPresent()) {
                        dov.setHref(reference.getHref().get().toString());
                    }
                    if (reference.getRole().isPresent()) {
                        dov.setRole(reference.getRole().get());
                    }
                    if (reference.getShow().isPresent()) {
                        dov.setShow(ShowType.Enum.forString(reference.getShow().get()));
                    }
                    if (reference.getTitle().isPresent()) {
                        dov.setTitle(reference.getTitle().get());
                    }
                    if (reference.getType().isPresent()) {
                        dov.setType(TypeType.Enum.forString(reference.getType().get()));
                    }
                } else {
                    if (domainOfValidity.isInstance()) {
                        Nillable<DomainOfValidity> nillable = domainOfValidity.getInstance();
                        if (nillable.isPresent()) {
                            net.opengis.gml.x32.DomainOfValidityDocument.DomainOfValidity xml =
                                    createDomainOfValidity(nillable.get(), EncodingContext.empty());
                            if (xml != null) {
                                dov.set(xml);
                            } else {
                                dov.setNil();
                                dov.setNilReason(Nillable.missing().get());
                            }
                        } else {
                            dov.setNil();
                            if (nillable.hasReason()) {
                                dov.setNilReason(nillable.getNilReason().get());
                            } else {
                                dov.setNilReason(Nillable.missing().get());
                            }
                        }
                    }
                }
            }
        }
        if (abstractCRS.hasScope()) {
            abstractCRS.getScope().forEach(scope -> acrst.addNewScope().setStringValue(scope));
        }
    }

    private XmlObject createVerticalCS(VerticalCS verticalCS, EncodingContext ctx) throws EncodingException {
        VerticalCSType vcst = VerticalCSType.Factory.newInstance();
        addAbstractCoordincateSystemValues(vcst, verticalCS);
        if (ctx.has(XmlBeansEncodingFlags.DOCUMENT)) {
            VerticalCSDocument vcsd = VerticalCSDocument.Factory.newInstance();
            VerticalCSPropertyType vcdpt = VerticalCSPropertyType.Factory.newInstance();
            vcdpt.setVerticalCS(vcst);
            vcsd.setVerticalCS(vcdpt);
            return vcsd;
        } else if (ctx.has(XmlBeansEncodingFlags.PROPERTY_TYPE)) {
            VerticalCSPropertyType vcdpt = VerticalCSPropertyType.Factory.newInstance();
            vcdpt.setVerticalCS(vcst);
            return vcdpt;
        }
        return vcst;
    }

    private void addAbstractCoordincateSystemValues(AbstractCoordinateSystemType acst,
            AbstractCoordinateSystem abstractCoordinateSystem) throws EncodingException {
        addDefinitonValues(acst, abstractCoordinateSystem);
        acst.setAggregationType(AggregationType.Enum.forString(abstractCoordinateSystem.getAggregation().name()));
        for (Referenceable<CoordinateSystemAxis> coordinateSystemAxis : abstractCoordinateSystem
                .getCoordinateSystemAxis()) {
            CoordinateSystemAxisPropertyType csapt = acst.addNewAxis();
            if (coordinateSystemAxis.isReference()) {
                Reference reference = coordinateSystemAxis.getReference();
                if (reference.getActuate().isPresent()) {
                    csapt.setActuate(ActuateType.Enum.forString(reference.getActuate().get()));
                }
                if (reference.getArcrole().isPresent()) {
                    csapt.setHref(reference.getArcrole().get());
                }
                if (reference.getHref().isPresent()) {
                    csapt.setHref(reference.getHref().get().toString());
                }
                if (reference.getRole().isPresent()) {
                    csapt.setRole(reference.getRole().get());
                }
                if (reference.getShow().isPresent()) {
                    csapt.setShow(ShowType.Enum.forString(reference.getShow().get()));
                }
                if (reference.getTitle().isPresent()) {
                    csapt.setTitle(reference.getTitle().get());
                }
                if (reference.getType().isPresent()) {
                    csapt.setType(TypeType.Enum.forString(reference.getType().get()));
                }
            } else {
                if (coordinateSystemAxis.isInstance()) {
                    Nillable<CoordinateSystemAxis> nillable = coordinateSystemAxis.getInstance();
                    if (nillable.isPresent()) {
                        XmlObject xml = createCoordinateSystemAxis(nillable.get(), EncodingContext.empty());
                        if (xml != null && xml instanceof CoordinateSystemAxisType) {
                            csapt.addNewCoordinateSystemAxis().set(xml);
                        } else {
                            csapt.setNil();
                            csapt.setNilReason(Nillable.missing().get());
                        }
                    } else {
                        csapt.setNil();
                        if (nillable.hasReason()) {
                            csapt.setNilReason(nillable.getNilReason().get());
                        } else {
                            csapt.setNilReason(Nillable.missing().get());
                        }
                    }
                }
            }
        }
    }

    private XmlObject createCoordinateSystemAxis(CoordinateSystemAxis coordinateSystemAxis, EncodingContext ctx)
            throws EncodingException {
        CoordinateSystemAxisType csat = CoordinateSystemAxisType.Factory.newInstance();
        addDefinitonValues(csat, coordinateSystemAxis);
        csat.setAxisAbbrev(createCodeType(coordinateSystemAxis.getAxisAbbrev()));
        csat.setAxisDirection(createCodeWithAuthorityType(coordinateSystemAxis.getAxisDirection()));
        if (coordinateSystemAxis.isSetMinimumValue()) {
            csat.setMinimumValue(coordinateSystemAxis.getMinimumValue());
        }
        if (coordinateSystemAxis.isSetMaximumValue()) {
            csat.setMaximumValue(coordinateSystemAxis.getMaximumValue());
        }
        if (coordinateSystemAxis.isSetRangeMeaning()) {
            csat.setRangeMeaning(createCodeWithAuthorityType(coordinateSystemAxis.getRangeMeaning()));
        }
        csat.setUom(coordinateSystemAxis.getUom());
        if (ctx.has(XmlBeansEncodingFlags.DOCUMENT)) {
            CoordinateSystemAxisDocument csad = CoordinateSystemAxisDocument.Factory.newInstance();
            csad.setCoordinateSystemAxis(csat);
            return csad;
        } else if (ctx.has(XmlBeansEncodingFlags.PROPERTY_TYPE)) {
            CoordinateSystemAxisPropertyType csapt = CoordinateSystemAxisPropertyType.Factory.newInstance();
            csapt.setCoordinateSystemAxis(csat);
            return csapt;
        }
        return csat;
    }

    protected String getSrsName(Geometry geom) {
        return getSrsName(geom.getSRID());
    }

    protected String getSrsName(int srid) {
        return srsNamePrefixURL.concat(String.valueOf(srid));
    }

    private static EncodingException missingValueParameter(String type) {
        return new EncodingException("The required 'value' parameter is empty for encoding %s!", type);
    }
}
