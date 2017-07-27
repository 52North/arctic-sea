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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.n52.shetland.ogc.OGCConstants;
import org.n52.shetland.ogc.SupportedType;
import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.gml.AbstractGeometry;
import org.n52.shetland.ogc.gml.CodeType;
import org.n52.shetland.ogc.gml.CodeWithAuthority;
import org.n52.shetland.ogc.gml.ReferenceType;
import org.n52.shetland.ogc.gml.time.Time;
import org.n52.shetland.ogc.om.features.SfConstants;
import org.n52.shetland.ogc.om.features.samplingFeatures.AbstractSamplingFeature;
import org.n52.shetland.ogc.om.features.samplingFeatures.SamplingFeature;
import org.n52.shetland.ogc.om.features.samplingFeatures.SfSpecimen;
import org.n52.shetland.ogc.om.values.QuantityValue;
import org.n52.shetland.ogc.sos.FeatureType;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.util.CodingHelper;
import org.n52.svalbard.util.XmlHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.vividsolutions.jts.geom.Geometry;

import net.opengis.gml.x32.FeaturePropertyType;
import net.opengis.samplingSpecimen.x20.SFSpecimenDocument;
import net.opengis.samplingSpecimen.x20.SFSpecimenType;
import net.opengis.samplingSpecimen.x20.SFSpecimenType.Size;

public class SpecimenDecoderv20
        extends SamplingDecoderv20 {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpecimenDecoderv20.class);

    private static final Set<SupportedType> SUPPORTED_TYPES = Sets.newHashSet(new FeatureType(OGCConstants.UNKNOWN),
            new FeatureType(SfConstants.SAMPLING_FEAT_TYPE_SF_SPECIMEN));

    private static final Set<DecoderKey> DECODER_KEYS =
            CodingHelper.decoderKeysForElements(SfConstants.NS_SPEC, SFSpecimenDocument.class, SFSpecimenType.class);

    public SpecimenDecoderv20() {
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
    public AbstractFeature decode(final XmlObject element) throws DecodingException {
        // validate XmlObject
        XmlHelper.validateDocument(element);
        if (element instanceof SFSpecimenDocument) {
            return parseSpatialSamplingFeature(((SFSpecimenDocument) element).getSFSpecimen());
        } else if (element instanceof SFSpecimenType) {
            return parseSpatialSamplingFeature((SFSpecimenType) element);
        }
        return super.decode(element);
    }

    private AbstractFeature parseSpatialSamplingFeature(final SFSpecimenType sfst) throws DecodingException {
        final SfSpecimen specimen = new SfSpecimen(null, sfst.getId());
        // parse identifier, names, description
        parseAbstractFeatureType(sfst, specimen);
        specimen.setSampledFeatures(getSampledFeatures(sfst.getSampledFeatureArray()));
        specimen.setXml(getXmlDescription(sfst));
        if (sfst.getParameterArray() != null) {
            specimen.setParameters(parseNamedValueTypeArray(sfst.getParameterArray()));
        }
        // TODO
        sfst.getMaterialClass();
        specimen.setMaterialClass((ReferenceType) decodeXmlElement(sfst.getMaterialClass()));
        specimen.setSamplingTime(getSamplingTime(sfst));
        // if (sfst.isSetSamplingMethod()) {
        // specimen.setSamplingMethod(sfst.getSamplingMethod());
        // }
        // samplingLocation
        if (sfst.isSetSamplingLocation()) {
            specimen.setSamplingLocation(getGeometry(sfst));
        }
        // sfst.getProcessingDetailsArray();
        if (sfst.isSetSize()) {
            specimen.setSize(getSize(sfst.getSize()));
        }
        // if (sfst.isSetCurrentLocation()) {
        // sfst.getCurrentLocation();
        // specimen.setCurrentLocation(currentLocation);
        // }
        if (sfst.isSetSpecimenType()) {
            specimen.setSpecimenType((ReferenceType) decodeXmlElement(sfst.getSpecimenType()));
        }
        return specimen;
    }

    private Time getSamplingTime(SFSpecimenType sfst) throws DecodingException {
        if (sfst.getSamplingTime().isSetAbstractTimeObject()) {
            Object decodedObject = decodeXmlObject(sfst.getSamplingTime().getAbstractTimeObject());
            if (decodedObject instanceof Time) {
                return (Time) decodedObject;
            }
        }
        return null;
    }

    private QuantityValue getSize(Size size) {
        QuantityValue quantityValue = new QuantityValue(size.getDoubleValue());
        quantityValue.setUnit(size.getUom());
        return quantityValue;
    }

    private String getXmlDescription(final SFSpecimenType sfst) {
        final SFSpecimenDocument sfsd = SFSpecimenDocument.Factory.newInstance(getXmlOptions());
        sfsd.setSFSpecimen(sfst);
        return sfsd.xmlText(getXmlOptions());
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
        final List<AbstractFeature> sampledFeatures = new ArrayList<AbstractFeature>(1);
        if (sampledFeature != null && !sampledFeature.isNil()) {
            // if xlink:href is set
            if (sampledFeature.getHref() != null && !sampledFeature.getHref().isEmpty()) {
                if (sampledFeature.getHref().startsWith("#")) {
                    sampledFeatures.add(new SamplingFeature(null, sampledFeature.getHref().replace("#", "")));
                } else {
                    final AbstractSamplingFeature sampFeat =
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
                    } catch (final XmlException xmle) {
                        throw new DecodingException("Error while parsing feature request!", xmle);
                    }
                }
                if (abstractFeature != null) {
                    final Object decodedObject = decodeXmlObject(abstractFeature);
                    if (decodedObject instanceof AbstractFeature) {
                        sampledFeatures.add((AbstractFeature) decodedObject);
                    }
                }
                throw new DecodingException("The requested sampledFeature type is not supported by this service!");
            }
        }
        return sampledFeatures;
    }

    private Geometry getGeometry(final SFSpecimenType sfst) throws DecodingException {
        final Object decodedObject = decodeXmlElement(sfst.getSamplingLocation());
        if (decodedObject instanceof Geometry) {
            return (Geometry) decodedObject;
        } else if (decodedObject instanceof AbstractGeometry) {
            return ((AbstractGeometry) decodedObject).getGeometry();
        }
        throw new DecodingException(
                "The requested geometry type of featureOfInterest is not supported by this service!");
    }

}
