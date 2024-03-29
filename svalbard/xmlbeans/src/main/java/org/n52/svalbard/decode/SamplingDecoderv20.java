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
import java.util.List;
import java.util.Set;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.n52.shetland.ogc.OGCConstants;
import org.n52.shetland.ogc.SupportedType;
import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.gml.CodeType;
import org.n52.shetland.ogc.gml.CodeWithAuthority;
import org.n52.shetland.ogc.om.features.SfConstants;
import org.n52.shetland.ogc.om.features.samplingFeatures.AbstractSamplingFeature;
import org.n52.shetland.ogc.om.features.samplingFeatures.SamplingFeature;
import org.n52.shetland.ogc.sos.FeatureType;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.util.CollectionHelper;
import org.n52.svalbard.ConformanceClasses;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.decode.exception.UnsupportedDecoderXmlInputException;
import org.n52.svalbard.util.CodingHelper;
import org.n52.svalbard.util.XmlHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import net.opengis.gml.x32.FeaturePropertyType;
import net.opengis.gml.x32.ReferenceType;
import net.opengis.samplingSpatial.x20.SFSpatialSamplingFeatureDocument;
import net.opengis.samplingSpatial.x20.SFSpatialSamplingFeatureType;
import net.opengis.samplingSpatial.x20.ShapeType;

/**
 * @since 1.0.0
 *
 */
public class SamplingDecoderv20
        extends AbstractGmlDecoderv321<XmlObject, AbstractFeature> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SamplingDecoderv20.class);

    private static final Set<SupportedType> SUPPORTED_TYPES =
            ImmutableSet.<SupportedType> builder().add(new FeatureType(OGCConstants.UNKNOWN))
                    .add(new FeatureType(SfConstants.SAMPLING_FEAT_TYPE_SF_SAMPLING_POINT))
                    .add(new FeatureType(SfConstants.SAMPLING_FEAT_TYPE_SF_SAMPLING_CURVE))
                    .add(new FeatureType(SfConstants.SAMPLING_FEAT_TYPE_SF_SAMPLING_SURFACE)).build();

    private static final Set<String> CONFORMANCE_CLASSES =
            Sets.newHashSet(ConformanceClasses.OM_V2_SPATIAL_SAMPLING, ConformanceClasses.OM_V2_SAMPLING_POINT,
                    ConformanceClasses.OM_V2_SAMPLING_CURVE, ConformanceClasses.OM_V2_SAMPLING_SURFACE);

    private static final Set<DecoderKey> DECODER_KEYS = CollectionHelper.union(
            CodingHelper.decoderKeysForElements(SfConstants.NS_SF, SFSpatialSamplingFeatureDocument.class,
                    SFSpatialSamplingFeatureType.class),
            CodingHelper.decoderKeysForElements(SfConstants.NS_SAMS, SFSpatialSamplingFeatureDocument.class,
                    SFSpatialSamplingFeatureType.class));

    public SamplingDecoderv20() {
        LOGGER.debug("Decoder for the following keys initialized successfully: {}!",
                Joiner.on(", ").join(DECODER_KEYS));
    }

    @Override
    public Set<DecoderKey> getKeys() {
        return Collections.unmodifiableSet(DECODER_KEYS);
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
    public AbstractFeature decode(XmlObject element) throws DecodingException {
        // validate XmlObject
        XmlHelper.validateDocument(element);
        if (element instanceof SFSpatialSamplingFeatureDocument) {
            return parseSpatialSamplingFeature(
                    ((SFSpatialSamplingFeatureDocument) element).getSFSpatialSamplingFeature());
        } else if (element instanceof SFSpatialSamplingFeatureType) {
            return parseSpatialSamplingFeature((SFSpatialSamplingFeatureType) element);
        }
        throw new UnsupportedDecoderXmlInputException(this, element);
    }

    private AbstractFeature parseSpatialSamplingFeature(final SFSpatialSamplingFeatureType spatialSamplingFeature)
            throws DecodingException {
        final SamplingFeature sosFeat = new SamplingFeature(null, spatialSamplingFeature.getId());
        return parseSpatialSamplingFeature(spatialSamplingFeature, sosFeat);
    }

    protected AbstractFeature parseSpatialSamplingFeature(SFSpatialSamplingFeatureType sfssft,
            AbstractSamplingFeature abstractSamplingFeature) throws DecodingException {
        // parse identifier, names, description
        parseAbstractFeatureType(sfssft, abstractSamplingFeature);
        abstractSamplingFeature.setFeatureType(getFeatureType(sfssft.getType()));
        abstractSamplingFeature.setSampledFeatures(getSampledFeatures(sfssft.getSampledFeatureArray()));
        abstractSamplingFeature.setXml(getXmlDescription(sfssft));
        abstractSamplingFeature.setGeometry(getGeometry(sfssft.getShape()));
        checkTypeAndGeometry(abstractSamplingFeature);
        abstractSamplingFeature.setGmlId(sfssft.getId());
        return abstractSamplingFeature;
    }

    private String getXmlDescription(final SFSpatialSamplingFeatureType spatialSamplingFeature) {
        final SFSpatialSamplingFeatureDocument featureDoc =
                SFSpatialSamplingFeatureDocument.Factory.newInstance(getXmlOptions());
        featureDoc.setSFSpatialSamplingFeature(spatialSamplingFeature);
        return featureDoc.xmlText(getXmlOptions());
    }

    private String getFeatureType(final ReferenceType type) {
        if (type != null && type.getHref() != null && !type.getHref().isEmpty()) {
            return type.getHref();
        }
        return null;
    }

    /**
     * Parse {@link FeaturePropertyType} sampledFeatures to
     * {@link AbstractFeature} list.
     *
     * @param sampledFeatureArray
     *            SampledFeatures to parse
     * @return List with the parsed sampledFeatures
     * @throws DecodingException
     *             If an error occurs
     */
    private List<AbstractFeature> getSampledFeatures(FeaturePropertyType[] sampledFeatureArray)
            throws DecodingException {
        final List<AbstractFeature> sampledFeatures = Lists.newArrayList();
        for (FeaturePropertyType featurePropertyType : sampledFeatureArray) {
            sampledFeatures.addAll(getSampledFeatures(featurePropertyType));
        }
        return sampledFeatures;
    }

    /**
     * Parse {@link FeaturePropertyType} sampledFeature to
     * {@link AbstractFeature} list.
     *
     * @param sampledFeature
     *            SampledFeature to parse
     * @return List with the parsed sampledFeature
     * @throws DecodingException
     *             If an error occurs
     */
    private List<AbstractFeature> getSampledFeatures(final FeaturePropertyType sampledFeature)
            throws DecodingException {
        final List<AbstractFeature> sampledFeatures = new ArrayList<>(1);
        if (sampledFeature != null && !sampledFeature.isNil()) {
            // if xlink:href is set
            if (sampledFeature.getHref() != null && !sampledFeature.getHref().isEmpty()) {
                if (sampledFeature.getHref().startsWith("#")) {
                    sampledFeatures.add(new SamplingFeature(null, sampledFeature.getHref().replace("#", "")));
                } else {
                    final SamplingFeature sampFeat =
                            new SamplingFeature(new CodeWithAuthority(sampledFeature.getHref()));
                    if (sampledFeature.getTitle() != null && !sampledFeature.getTitle().isEmpty()) {
                        sampFeat.addName(new CodeType(sampledFeature.getTitle()));
                    }
                    sampledFeatures.add(sampFeat);
                }
            } else {
                XmlObject abstractFeature = null;
                if (sampledFeature.getAbstractFeature() != null) {
                    abstractFeature = sampledFeature.getAbstractFeature();
                } else if (sampledFeature.getDomNode().hasChildNodes()) {
                    try {
                        abstractFeature = XmlObject.Factory
                                .parse(XmlHelper.getNodeFromNodeList(sampledFeature.getDomNode().getChildNodes()));
                    } catch (XmlException xmle) {
                        throw new DecodingException("Error while parsing feature request!", xmle);
                    }
                }
                if (abstractFeature != null) {
                    final Object decodedObject = decodeXmlObject(abstractFeature);
                    if (decodedObject instanceof AbstractFeature) {
                        sampledFeatures.add((AbstractFeature) decodedObject);
                    }
                }
                throw new DecodingException(Sos2Constants.InsertObservationParams.observation,
                        "The requested sampledFeature type is not supported by this service!");
            }
        }
        return sampledFeatures;
    }

    private Geometry getGeometry(final ShapeType shape) throws DecodingException {
        final Object decodedObject = decodeXmlElement(shape.getAbstractGeometry());
        if (decodedObject instanceof Geometry) {
            return (Geometry) decodedObject;
        }
        throw new DecodingException(Sos2Constants.InsertObservationParams.observation,
                "The requested geometry type of featureOfInterest is not supported by this service!");
    }

    private void checkTypeAndGeometry(final AbstractSamplingFeature sosFeat) throws DecodingException {
        final String featTypeForGeometry = getFeatTypeForGeometry(sosFeat.getGeometry());
        if (sosFeat.getFeatureType() == null) {
            sosFeat.setFeatureType(featTypeForGeometry);
        } else {
            if (!featTypeForGeometry.equals(sosFeat.getFeatureType())) {

                throw new DecodingException("The requested observation is invalid! The featureOfInterest type "
                        + "does not comply with the defined type (%s)!", sosFeat.getFeatureType());
            }
        }

    }

    private String getFeatTypeForGeometry(final Geometry geometry) {
        if (geometry instanceof Point || geometry instanceof MultiPoint) {
            return SfConstants.SAMPLING_FEAT_TYPE_SF_SAMPLING_POINT;
        } else if (geometry instanceof LineString || geometry instanceof MultiLineString) {
            return SfConstants.SAMPLING_FEAT_TYPE_SF_SAMPLING_CURVE;
        } else if (geometry instanceof Polygon || geometry instanceof MultiPolygon) {
            return SfConstants.SAMPLING_FEAT_TYPE_SF_SAMPLING_SURFACE;
        }
        return SfConstants.SAMPLING_FEAT_TYPE_SF_SAMPLING_FEATURE;
    }

}
