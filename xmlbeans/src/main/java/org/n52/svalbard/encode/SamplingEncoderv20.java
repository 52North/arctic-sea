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

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.n52.shetland.ogc.OGCConstants;
import org.n52.shetland.ogc.SupportedType;
import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.gml.AbstractMetaData;
import org.n52.shetland.ogc.gml.GmlConstants;
import org.n52.shetland.ogc.om.NamedValue;
import org.n52.shetland.ogc.om.OmConstants;
import org.n52.shetland.ogc.om.features.SfConstants;
import org.n52.shetland.ogc.om.features.samplingFeatures.AbstractSamplingFeature;
import org.n52.shetland.ogc.sos.FeatureType;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.util.CollectionHelper;
import org.n52.shetland.util.OMHelper;
import org.n52.shetland.w3c.SchemaLocation;
import org.n52.svalbard.ConformanceClass;
import org.n52.svalbard.ConformanceClasses;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.exception.UnsupportedEncoderInputException;
import org.n52.svalbard.util.CodingHelper;
import org.n52.svalbard.util.XmlHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

import net.opengis.gml.x32.FeaturePropertyType;
import net.opengis.sampling.x20.SFSamplingFeatureType;
import net.opengis.samplingSpatial.x20.SFSpatialSamplingFeatureDocument;
import net.opengis.samplingSpatial.x20.SFSpatialSamplingFeatureType;
import net.opengis.samplingSpatial.x20.ShapeType;

/**
 * @since 1.0.0
 *
 */
public class SamplingEncoderv20
        extends AbstractGmlEncoderv321<XmlObject, AbstractFeature>
        implements ConformanceClass {

    private static final Logger LOGGER = LoggerFactory.getLogger(SamplingEncoderv20.class);

    private static final Set<EncoderKey> ENCODER_KEYS =
            CollectionHelper.union(CodingHelper.encoderKeysForElements(SfConstants.NS_SAMS, AbstractFeature.class),
                    CodingHelper.encoderKeysForElements(SfConstants.NS_SF, AbstractFeature.class));

    private static final Set<String> CONFORMANCE_CLASSES =
            Sets.newHashSet(ConformanceClasses.OM_V2_SPATIAL_SAMPLING, ConformanceClasses.OM_V2_SAMPLING_POINT,
                    ConformanceClasses.OM_V2_SAMPLING_CURVE, ConformanceClasses.OM_V2_SAMPLING_SURFACE);

    private static final Set<SupportedType> SUPPORTED_TYPES =
            ImmutableSet.<SupportedType> builder().add(new FeatureType(OGCConstants.UNKNOWN))
                    .add(new FeatureType(SfConstants.SAMPLING_FEAT_TYPE_SF_SAMPLING_POINT))
                    .add(new FeatureType(SfConstants.SAMPLING_FEAT_TYPE_SF_SAMPLING_CURVE))
                    .add(new FeatureType(SfConstants.SAMPLING_FEAT_TYPE_SF_SAMPLING_SURFACE)).build();

    public SamplingEncoderv20() {
        LOGGER.debug("Encoder for the following keys initialized successfully: {}!",
                Joiner.on(", ").join(ENCODER_KEYS));
    }

    @Override
    public Set<EncoderKey> getKeys() {
        return Collections.unmodifiableSet(ENCODER_KEYS);
    }

    @Override
    public Set<SupportedType> getSupportedTypes() {
        return Collections.unmodifiableSet(SUPPORTED_TYPES);
    }

    @Override
    public Set<String> getConformanceClasses(String service, String version) {
        if (SosConstants.SOS.equals(service) && Sos2Constants.SERVICEVERSION.equals(version)) {
            return Collections.unmodifiableSet(CONFORMANCE_CLASSES);
        }
        return Collections.emptySet();
    }

    @Override
    public void addNamespacePrefixToMap(final Map<String, String> nameSpacePrefixMap) {
        nameSpacePrefixMap.put(SfConstants.NS_SAMS, SfConstants.NS_SAMS_PREFIX);
        nameSpacePrefixMap.put(SfConstants.NS_SF, SfConstants.NS_SF_PREFIX);
    }

    @Override
    public Set<SchemaLocation> getSchemaLocations() {
        return Sets.newHashSet(SfConstants.SF_SCHEMA_LOCATION, SfConstants.SAMS_SCHEMA_LOCATION);
    }

    @Override
    public XmlObject encode(final AbstractFeature abstractFeature, final EncodingContext ctx)
            throws EncodingException {
        final XmlObject encodedObject = createFeature(abstractFeature);
        // LOGGER.debug("Encoded object {} is valid: {}",
        // encodedObject.schemaType().toString(),
        // XmlHelper.validateDocument(encodedObject));
        return encodedObject;
    }

    protected XmlObject createFeature(final AbstractFeature absFeature) throws EncodingException {
        if (absFeature instanceof AbstractSamplingFeature) {
            final AbstractSamplingFeature sampFeat = (AbstractSamplingFeature) absFeature;
            final SFSpatialSamplingFeatureDocument xbSampFeatDoc =
                    SFSpatialSamplingFeatureDocument.Factory.newInstance(getXmlOptions());
            if (sampFeat.isSetXml()) {
                try {
                    final XmlObject feature = XmlObject.Factory.parse(sampFeat.getXml(), getXmlOptions());
                    if (XmlHelper.getNamespace(feature).equals(SfConstants.NS_SAMS)) {
                        XmlHelper.updateGmlIDs(feature.getDomNode().getFirstChild(), absFeature.getGmlId(), null);
                        if (feature instanceof SFSpatialSamplingFeatureType) {
                            xbSampFeatDoc.setSFSpatialSamplingFeature((SFSpatialSamplingFeatureType) feature);
                            encodeShape(xbSampFeatDoc.getSFSpatialSamplingFeature().getShape(), sampFeat);
                            addNameDescription(xbSampFeatDoc.getSFSpatialSamplingFeature(), sampFeat);
                            return xbSampFeatDoc;
                        }
                        encodeShape(
                                ((SFSpatialSamplingFeatureDocument) feature).getSFSpatialSamplingFeature().getShape(),
                                sampFeat);
                        addNameDescription(((SFSpatialSamplingFeatureDocument) feature).getSFSpatialSamplingFeature(),
                                sampFeat);
                        sampFeat.wasEncoded();
                        return feature;
                    } else {
                        return encodeObjectToXml(XmlHelper.getNamespace(feature), absFeature);
                    }
                } catch (final XmlException xmle) {
                    throw new EncodingException(
                            "Error while encoding GetFeatureOfInterest response, invalid samplingFeature description!",
                            xmle);
                }
            }
            final SFSpatialSamplingFeatureType xbSampFeature = xbSampFeatDoc.addNewSFSpatialSamplingFeature();
            // TODO: CHECK for all fields set gml:id
            addId(xbSampFeature, sampFeat);
            addIdentifier(xbSampFeature, sampFeat);
            // set type
            addFeatureType(xbSampFeature, sampFeat);
            // set type
            addNameDescription(xbSampFeature, sampFeat);
            setMetaDataProperty(xbSampFeature, sampFeat);
            // set sampledFeatures
            // TODO: CHECK
            addSampledFeatures(xbSampFeature, sampFeat);

            addParameter(xbSampFeature, sampFeat);

            // set position
            encodeShape(xbSampFeature.addNewShape(), sampFeat);
            sampFeat.wasEncoded();
            return xbSampFeatDoc;
        }
        throw new UnsupportedEncoderInputException(this, absFeature);
    }

    @Override
    protected XmlObject createFeature(FeaturePropertyType featurePropertyType, AbstractFeature abstractFeature,
            EncodingContext context) throws EncodingException {
        if (abstractFeature instanceof AbstractSamplingFeature) {
            final AbstractSamplingFeature samplingFeature = (AbstractSamplingFeature) abstractFeature;
            String namespace;
            if (context.has(XmlEncoderFlags.ENCODE_NAMESPACE)
                    && context.get(XmlEncoderFlags.ENCODE_NAMESPACE).isPresent()
                    && context.get(XmlEncoderFlags.ENCODE_NAMESPACE).get() instanceof String) {
                namespace = (String) context.get(XmlEncoderFlags.ENCODE_NAMESPACE).get();
            } else {
                namespace = OMHelper.getNamespaceForFeatureType(samplingFeature.getFeatureType());
            }
            final XmlObject encodedXmlObject = encodeObjectToXml(namespace, samplingFeature);

            if (encodedXmlObject != null) {
                return encodedXmlObject;
            } else {
                if (samplingFeature.isSetXml()) {
                    try {
                        // TODO how set gml:id in already existing
                        // XmlDescription? <-- XmlCursor
                        return XmlObject.Factory.parse(samplingFeature.getXml());
                    } catch (final XmlException xmle) {
                        throw new EncodingException("Error while encoding featurePropertyType!", xmle);
                    }
                } else {
                    featurePropertyType.setHref(samplingFeature.getIdentifierCodeWithAuthority().getValue());
                    if (samplingFeature.isSetName()) {
                        featurePropertyType.setTitle(samplingFeature.getFirstName().getValue());
                    }
                    return featurePropertyType;
                }
            }
        }
        return featurePropertyType;
    }

    protected void addSampledFeatures(SFSamplingFeatureType sfsft, AbstractSamplingFeature sampFeat)
            throws EncodingException {
        if (sampFeat.isSetSampledFeatures()) {
            for (AbstractFeature sampledFeature : sampFeat.getSampledFeatures()) {
                XmlObject encodeObjectToXml = encodeObjectToXml(GmlConstants.NS_GML_32, sampledFeature,
                        new EncodingContext().with(XmlBeansEncodingFlags.REFERENCED));
                sfsft.addNewSampledFeature().set(encodeObjectToXml);
            }
        } else {
            sfsft.addNewSampledFeature().setHref(OGCConstants.UNKNOWN);
        }
    }

    protected void addFeatureType(SFSamplingFeatureType sfsft, AbstractSamplingFeature sampFeat) {
        if (sampFeat.isSetFeatureType() && !OGCConstants.UNKNOWN.equals(sampFeat.getFeatureType())) {
            sfsft.addNewType().setHref(sampFeat.getFeatureType());
        } else {
            if (sampFeat.isSetGeometry()) {
                addFeatureTypeForGeometry(sfsft, sampFeat.getGeometry());
            }
        }
    }

    private void addFeatureTypeForGeometry(SFSamplingFeatureType xbSampFeature, Geometry geometry) {
        xbSampFeature.addNewType().setHref(getFeatureType(geometry));
    }

    private String getFeatureType(Geometry geometry) {
        if (geometry instanceof Point || geometry instanceof MultiPoint) {
            return SfConstants.SAMPLING_FEAT_TYPE_SF_SAMPLING_POINT;
        } else if (geometry instanceof LineString || geometry instanceof MultiLineString) {
            return SfConstants.SAMPLING_FEAT_TYPE_SF_SAMPLING_CURVE;
        } else if (geometry instanceof Polygon || geometry instanceof MultiPolygon) {
            return SfConstants.SAMPLING_FEAT_TYPE_SF_SAMPLING_SURFACE;
        } else {
            return SfConstants.SAMPLING_FEAT_TYPE_SF_SAMPLING_FEATURE;
        }
    }

    private void encodeShape(ShapeType xbShape, AbstractSamplingFeature sampFeat) throws EncodingException {
        Encoder<XmlObject, Geometry> encoder = getEncoder(GmlConstants.NS_GML_32, sampFeat.getGeometry());
        if (encoder != null) {
            XmlObject xmlObject = encoder.encode(sampFeat.getGeometry(),
                    EncodingContext.of(XmlBeansEncodingFlags.GMLID, sampFeat.getGmlId()));
            if (xbShape.isSetAbstractGeometry()) {
                xbShape.getAbstractGeometry().set(xmlObject);
            } else {
                xbShape.addNewAbstractGeometry().set(xmlObject);
            }
            XmlHelper.substituteElement(xbShape.getAbstractGeometry(), xmlObject);
        } else {
            throw new EncodingException("Error while encoding geometry for feature, needed encoder is missing!");
        }
    }

    protected void addParameter(SFSamplingFeatureType xbSampFeature, AbstractSamplingFeature sampFeat)
            throws EncodingException {
        for (NamedValue<?> namedValuePair : sampFeat.getParameters()) {
            XmlObject encodeObjectToXml = encodeObjectToXml(OmConstants.NS_OM_2, namedValuePair);
            if (encodeObjectToXml != null) {
                xbSampFeature.addNewParameter().addNewNamedValue().set(encodeObjectToXml);
            }
        }
    }

    protected void addNameDescription(SFSamplingFeatureType xbSamplingFeature, AbstractSamplingFeature samplingFeature)
            throws EncodingException {
        addName(xbSamplingFeature, samplingFeature);
        addDescription(xbSamplingFeature, samplingFeature);
    }

    protected void setMetaDataProperty(SFSamplingFeatureType sfssft, AbstractSamplingFeature sampFeat)
            throws EncodingException {
        if (sampFeat.isSetMetaDataProperty()) {
            for (AbstractMetaData abstractMetaData : sampFeat.getMetaDataProperty()) {
                XmlObject encodeObject = encodeObjectToXml(GmlConstants.NS_GML_32, abstractMetaData);
                XmlObject substituteElement = XmlHelper
                        .substituteElement(sfssft.addNewMetaDataProperty().addNewAbstractMetaData(), encodeObject);
                substituteElement.set(encodeObject);
            }
        }
    }
}
