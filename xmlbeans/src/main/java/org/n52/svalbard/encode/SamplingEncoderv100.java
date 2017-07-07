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

import org.apache.xmlbeans.XmlObject;
import org.joda.time.DateTime;
import org.n52.shetland.ogc.OGCConstants;
import org.n52.shetland.ogc.SupportedType;
import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.gml.AbstractMetaData;
import org.n52.shetland.ogc.gml.CodeType;
import org.n52.shetland.ogc.gml.GmlConstants;
import org.n52.shetland.ogc.om.features.FeatureCollection;
import org.n52.shetland.ogc.om.features.SfConstants;
import org.n52.shetland.ogc.om.features.samplingFeatures.AbstractSamplingFeature;
import org.n52.shetland.ogc.sos.FeatureType;
import org.n52.shetland.util.CollectionHelper;
import org.n52.shetland.w3c.SchemaLocation;
import org.n52.svalbard.decode.exception.DecodingException;
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
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

import net.opengis.gml.FeaturePropertyType;
import net.opengis.sampling.x10.SamplingCurveDocument;
import net.opengis.sampling.x10.SamplingCurveType;
import net.opengis.sampling.x10.SamplingFeatureCollectionDocument;
import net.opengis.sampling.x10.SamplingFeatureCollectionType;
import net.opengis.sampling.x10.SamplingFeaturePropertyType;
import net.opengis.sampling.x10.SamplingFeatureType;
import net.opengis.sampling.x10.SamplingPointDocument;
import net.opengis.sampling.x10.SamplingPointType;
import net.opengis.sampling.x10.SamplingSurfaceDocument;
import net.opengis.sampling.x10.SamplingSurfaceType;

/**
 * @since 1.0.0
 *
 */
public class SamplingEncoderv100
        extends AbstractXmlEncoder<XmlObject, AbstractFeature> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SamplingEncoderv100.class);

    private static final Set<EncoderKey> ENCODER_KEYS =
            CollectionHelper.union(CodingHelper.encoderKeysForElements(SfConstants.NS_SA, AbstractFeature.class));

    // TODO here also the question, sa:samplingPoint sampling/1.0 vs 2.0 mapping
    // or not and where and how to handle
    private static final Set<SupportedType> SUPPORTED_TYPES =
            ImmutableSet.of(new FeatureType(OGCConstants.UNKNOWN), new FeatureType(SfConstants.EN_SAMPLINGPOINT),
                    new FeatureType(SfConstants.EN_SAMPLINGSURFACE), new FeatureType(SfConstants.EN_SAMPLINGCURVE));

    public SamplingEncoderv100() {
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
    public void addNamespacePrefixToMap(Map<String, String> nameSpacePrefixMap) {
        nameSpacePrefixMap.put(SfConstants.NS_SA, SfConstants.NS_SA_PREFIX);
    }

    @Override
    public Set<SchemaLocation> getSchemaLocations() {
        return Sets.newHashSet(SfConstants.SA_SCHEMA_LOCATION);
    }

    @Override
    public XmlObject encode(AbstractFeature abstractFeature, EncodingContext context) throws EncodingException {
        XmlObject encodedObject = createFeature(abstractFeature);
        if (LOGGER.isDebugEnabled()) {
            try {
                LOGGER.debug("Encoded object {} is valid: {}", encodedObject.schemaType().toString(),
                        XmlHelper.validateDocument(encodedObject));
            } catch (DecodingException e) {
                throw new EncodingException(e);
            }
        }
        return encodedObject;
    }

    private XmlObject createFeature(AbstractFeature absFeature) throws EncodingException {
        if (absFeature instanceof AbstractSamplingFeature) {
            AbstractSamplingFeature sampFeat = (AbstractSamplingFeature) absFeature;
            if (sampFeat.getFeatureType().equals(SfConstants.FT_SAMPLINGPOINT)
                    || sampFeat.getFeatureType().equals(SfConstants.SAMPLING_FEAT_TYPE_SF_SAMPLING_POINT)
                    || sampFeat.getGeometry() instanceof Point) {
                SamplingPointDocument xbSamplingPointDoc = SamplingPointDocument.Factory.newInstance(getXmlOptions());
                SamplingPointType xbSamplingPoint = xbSamplingPointDoc.addNewSamplingPoint();
                addValuesToFeature(xbSamplingPoint, sampFeat);
                XmlObject xbGeomety = getEncodedGeometry(sampFeat.getGeometry(), absFeature.getGmlId());
                xbSamplingPoint.addNewPosition().addNewPoint().set(xbGeomety);
                sampFeat.wasEncoded();
                return xbSamplingPointDoc;
            } else if (sampFeat.getFeatureType().equals(SfConstants.FT_SAMPLINGCURVE)
                    || sampFeat.getFeatureType().equals(SfConstants.SAMPLING_FEAT_TYPE_SF_SAMPLING_CURVE)
                    || sampFeat.getGeometry() instanceof LineString) {
                SamplingCurveDocument xbSamplingCurveDoc = SamplingCurveDocument.Factory.newInstance(getXmlOptions());
                SamplingCurveType xbSamplingCurve = xbSamplingCurveDoc.addNewSamplingCurve();
                addValuesToFeature(xbSamplingCurve, sampFeat);
                XmlObject xbGeomety = getEncodedGeometry(sampFeat.getGeometry(), absFeature.getGmlId());
                xbSamplingCurve.addNewShape().addNewCurve().set(xbGeomety);
                sampFeat.wasEncoded();
                return xbSamplingCurveDoc;
            } else if (sampFeat.getFeatureType().equals(SfConstants.FT_SAMPLINGSURFACE)
                    || sampFeat.getFeatureType().equals(SfConstants.SAMPLING_FEAT_TYPE_SF_SAMPLING_SURFACE)
                    || sampFeat.getGeometry() instanceof Polygon) {
                SamplingSurfaceDocument xbSamplingSurfaceDoc =
                        SamplingSurfaceDocument.Factory.newInstance(getXmlOptions());
                SamplingSurfaceType xbSamplingSurface = xbSamplingSurfaceDoc.addNewSamplingSurface();
                addValuesToFeature(xbSamplingSurface, sampFeat);
                XmlObject xbGeomety = getEncodedGeometry(sampFeat.getGeometry(), absFeature.getGmlId());
                xbSamplingSurface.addNewShape().addNewSurface().set(xbGeomety);
                sampFeat.wasEncoded();
                return xbSamplingSurfaceDoc;
            }
        } else if (absFeature instanceof FeatureCollection) {
            createFeatureCollection((FeatureCollection) absFeature);
        }
        throw new UnsupportedEncoderInputException(this, absFeature);
    }

    private XmlObject getEncodedGeometry(Geometry geometry, String gmlId)
            throws UnsupportedEncoderInputException, EncodingException {
        Encoder<XmlObject, Geometry> encoder = getEncoder(getEncoderKey(GmlConstants.NS_GML, geometry));
        if (encoder != null) {
            return encoder.encode(geometry, EncodingContext.of(XmlBeansEncodingFlags.GMLID, gmlId));
        } else {
            throw new EncodingException("Error while encoding geometry for feature, needed encoder is missing!");
        }
    }

    private void addValuesToFeature(SamplingFeatureType xbSamplingFeature, AbstractSamplingFeature sampFeat)
            throws EncodingException {
        xbSamplingFeature.setId(sampFeat.getGmlId());
        if (sampFeat.isSetIdentifier()) {
            sampFeat.getIdentifierCodeWithAuthority().setCodeSpace("uniquID");
            xbSamplingFeature.addNewName()
                    .set(encodeObjectToXml(GmlConstants.NS_GML, sampFeat.getIdentifierCodeWithAuthority()));
        }

        if (sampFeat.isSetName()) {
            for (CodeType sosName : sampFeat.getName()) {
                xbSamplingFeature.addNewName().set(encodeObjectToXml(GmlConstants.NS_GML, sosName));
            }
        }

        // set sampledFeatures
        // TODO: CHECK
        if (sampFeat.getSampledFeatures() != null && !sampFeat.getSampledFeatures().isEmpty()) {
            for (AbstractFeature sampledFeature : sampFeat.getSampledFeatures()) {
                FeaturePropertyType sp = xbSamplingFeature.addNewSampledFeature();
                sp.setHref(sampledFeature.getIdentifier());
                if (sampFeat.isSetName() && sampFeat.getFirstName().isSetValue()) {
                    sp.setTitle(sampFeat.getFirstName().getValue());
                }
                // xbSamplingFeature.addNewSampledFeature().set(createFeature(sampledFeature));
            }
        } else {
            xbSamplingFeature.addNewSampledFeature().setHref(GmlConstants.NIL_UNKNOWN);
        }

        // set metadataProperty
        setMetaDataProperty(xbSamplingFeature, sampFeat);
    }

    private void setMetaDataProperty(SamplingFeatureType sft, AbstractSamplingFeature sampFeat)
            throws EncodingException {
        if (sampFeat.isSetMetaDataProperty()) {
            for (AbstractMetaData abstractMetaData : sampFeat.getMetaDataProperty()) {
                XmlObject encodeObject = encodeObjectToXml(GmlConstants.NS_GML, abstractMetaData);
                sft.addNewMetaDataProperty().set(encodeObject);
                // XmlObject substituteElement = XmlHelper.substituteElement(
                // sft.addNewMetaDataProperty().addNewAbstractMetaData(),
                // encodeObject);
                // substituteElement.set(encodeObject);
            }
        }
    }

    private XmlObject createFeatureCollection(FeatureCollection sosFeatureCollection) throws EncodingException {
        SamplingFeatureCollectionDocument xbSampFeatCollDoc =
                SamplingFeatureCollectionDocument.Factory.newInstance(getXmlOptions());
        SamplingFeatureCollectionType xbSampFeatColl = xbSampFeatCollDoc.addNewSamplingFeatureCollection();
        xbSampFeatColl.setId("sfc_" + Long.toString(new DateTime().getMillis()));
        for (AbstractFeature sosAbstractFeature : sosFeatureCollection.getMembers().values()) {
            SamplingFeaturePropertyType xbFeatMember = xbSampFeatColl.addNewMember();
            xbFeatMember.set(createFeature(sosAbstractFeature));
        }
        return xbSampFeatCollDoc;
    }
}
