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
import org.n52.shetland.ogc.gml.GmlConstants;
import org.n52.shetland.ogc.om.features.SfConstants;
import org.n52.shetland.ogc.om.features.samplingFeatures.SfSpecimen;
import org.n52.shetland.ogc.sos.FeatureType;
import org.n52.shetland.util.CollectionHelper;
import org.n52.shetland.w3c.SchemaLocation;
import org.n52.shetland.w3c.xlink.Reference;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.util.CodingHelper;
import org.n52.svalbard.util.GmlHelper;
import org.n52.svalbard.util.XmlHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3.x1999.xlink.ActuateType;
import org.w3.x1999.xlink.ShowType;
import org.w3.x1999.xlink.TypeType;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;

import net.opengis.sampling.x20.SFProcessPropertyType;
import net.opengis.samplingSpecimen.x20.LocationPropertyType;
import net.opengis.samplingSpecimen.x20.SFSpecimenDocument;
import net.opengis.samplingSpecimen.x20.SFSpecimenType;
import net.opengis.samplingSpecimen.x20.SFSpecimenType.Size;

public class SpecimenEncoderv20
        extends SamplingEncoderv20 {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpecimenEncoderv20.class);

    private static final Set<EncoderKey> ENCODER_KEYS = CollectionHelper.union(
            CodingHelper.encoderKeysForElements(SfConstants.NS_SPEC, AbstractFeature.class, SfSpecimen.class),
            CodingHelper.encoderKeysForElements(SfConstants.NS_SF, AbstractFeature.class, SfSpecimen.class));

    private static final Set<SupportedType> SUPPORTED_TYPES = Sets.newHashSet(new FeatureType(OGCConstants.UNKNOWN),
            new FeatureType(SfConstants.SAMPLING_FEAT_TYPE_SF_SPECIMEN));

    public SpecimenEncoderv20() {
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
    public void addNamespacePrefixToMap(final Map<String, String> nameSpacePrefixMap) {
        nameSpacePrefixMap.put(SfConstants.NS_SPEC, SfConstants.NS_SPEC_PREFIX);
        nameSpacePrefixMap.put(SfConstants.NS_SF, SfConstants.NS_SF_PREFIX);
    }

    @Override
    public Set<SchemaLocation> getSchemaLocations() {
        return Sets.newHashSet(SfConstants.SF_SCHEMA_LOCATION, SfConstants.SPEC_SCHEMA_LOCATION);
    }

    @Override
    public XmlObject encode(final AbstractFeature abstractFeature, final EncodingContext ec) throws EncodingException {
        XmlObject encodedObject;
        if (abstractFeature instanceof SfSpecimen) {
            encodedObject = createSpecimen((SfSpecimen) abstractFeature);
        } else {
            encodedObject = createFeature(abstractFeature);
        }
        // LOGGER.debug("Encoded object {} is valid: {}",
        // encodedObject.schemaType().toString(),
        // XmlHelper.validateDocument(encodedObject));
        return encodedObject;
    }

    private XmlObject createSpecimen(SfSpecimen specimen) throws EncodingException {
        SFSpecimenDocument sfsd = SFSpecimenDocument.Factory.newInstance(getXmlOptions());
        if (specimen.isSetXml()) {
            try {
                final XmlObject feature = XmlObject.Factory.parse(specimen.getXml(), getXmlOptions());
                XmlHelper.updateGmlIDs(feature.getDomNode().getFirstChild(), specimen.getGmlId(), null);
                if (XmlHelper.getNamespace(feature).equals(SfConstants.NS_SPEC) && feature instanceof SFSpecimenType) {
                    sfsd.setSFSpecimen((SFSpecimenType) feature);
                    addName(sfsd.getSFSpecimen(), specimen);
                    addDescription(sfsd.getSFSpecimen(), specimen);
                    return sfsd;
                }
                addName(((SFSpecimenDocument) feature).getSFSpecimen(), specimen);
                addDescription(((SFSpecimenDocument) feature).getSFSpecimen(), specimen);
                return feature;
            } catch (final XmlException xmle) {
                throw new EncodingException(
                        "Error while encoding GetFeatureOfInterest response, invalid specimen description!", xmle);
            }
        }
        final SFSpecimenType sfst = sfsd.addNewSFSpecimen();
        // TODO: CHECK for all fields set gml:id
        addId(sfst, specimen);
        addIdentifier(sfst, specimen);
        // set type
        addFeatureType(sfst, specimen);
        addName(sfst, specimen);
        addDescription(sfst, specimen);
        // set sampledFeatures
        addSampledFeatures(sfst, specimen);
        addParameter(sfst, specimen);
        // set specimen specific data
        addMaterialClass(sfst, specimen);
        addSamplingTime(sfst, specimen);
        addSamplingMethod(sfst, specimen);
        addSamplingLocation(sfst, specimen);
        addProcessingDetails(sfst, specimen);
        addSize(sfst, specimen);
        addCurrentLocation(sfst, specimen);
        addSpecimenType(sfst, specimen);
        specimen.wasEncoded();
        return sfsd;
    }

    private void addMaterialClass(SFSpecimenType sfst, SfSpecimen specimen) throws EncodingException {
        sfst.addNewMaterialClass().set(encodeGML32(specimen.getMaterialClass()));
    }

    private void addSamplingTime(SFSpecimenType sfst, SfSpecimen specimen) throws EncodingException {
        XmlObject xmlObject = encodeGML32(specimen.getSamplingTime());
        XmlObject substitution = sfst.addNewSamplingTime().addNewAbstractTimeObject()
                .substitute(GmlHelper.getGml321QnameForITime(specimen.getSamplingTime()), xmlObject.schemaType());
        substitution.set(xmlObject);
    }

    private void addSamplingMethod(SFSpecimenType sfst, SfSpecimen specimen) {
        if (specimen.isSetSamplingMethod()) {
            if (!specimen.getSamplingMethod().getInstance().isPresent()) {
                sfst.addNewSamplingMethod()
                        .setHref(specimen.getSamplingMethod().getReference().getHref().get().toString());
                Reference ref = specimen.getCurrentLocation().getReference();
                SFProcessPropertyType sfppt = sfst.addNewSamplingMethod();
                if (ref.getHref().isPresent()) {
                    sfppt.setHref(ref.getHref().get().toString());
                }
                if (ref.getTitle().isPresent()) {
                    sfppt.setTitle(ref.getTitle().get());
                }
                if (ref.getActuate().isPresent()) {
                    sfppt.setActuate(ActuateType.Enum.forString(ref.getActuate().get()));
                }
                if (ref.getArcrole().isPresent()) {
                    sfppt.setArcrole(ref.getArcrole().get());
                }
                if (ref.getRole().isPresent()) {
                    sfppt.setRole(ref.getRole().get());
                }
                if (ref.getShow().isPresent()) {
                    sfppt.setShow(ShowType.Enum.forString(ref.getShow().get()));
                }
                if (ref.getType().isPresent()) {
                    sfppt.setType(TypeType.Enum.forString(ref.getType().get()));
                }
            }
        }
    }

    private void addSamplingLocation(SFSpecimenType sfst, SfSpecimen specimen) throws EncodingException {
        if (specimen.isSetSamplingLocation()) {
            sfst.addNewSamplingLocation().set(encodeGML32(specimen.getSamplingLocation(),
                    EncodingContext.of(XmlBeansEncodingFlags.PROPERTY_TYPE, "true")));
        }
    }

    private void addProcessingDetails(SFSpecimenType sfst, SfSpecimen specimen) {
        // if (specimen.isSetProcessingDetails()) {
        // for (PreparationStep preparationStep :
        // specimen.getProcessingDetails()) {
        // // TODO
        // }
        // }
    }

    private void addSize(SFSpecimenType sfst, SfSpecimen specimen) {
        if (specimen.isSetSize()) {
            Size size = sfst.addNewSize();
            size.setDoubleValue(specimen.getSize().getValue());
            if (specimen.getSize().isSetUnit()) {
                size.setUom(specimen.getSize().getUnit());
            }
        }
    }

    private void addCurrentLocation(SFSpecimenType sfst, SfSpecimen specimen) {
        if (specimen.isSetCurrentLocation()) {
            if (!specimen.getCurrentLocation().getInstance().isPresent()) {
                Reference ref = specimen.getCurrentLocation().getReference();
                LocationPropertyType lpt = sfst.addNewCurrentLocation();
                if (ref.getHref().isPresent()) {
                    lpt.setHref(ref.getHref().get().toString());
                }
                if (ref.getTitle().isPresent()) {
                    lpt.setTitle(ref.getTitle().get());
                }
                if (ref.getActuate().isPresent()) {
                    lpt.setActuate(ActuateType.Enum.forString(ref.getActuate().get()));
                }
                if (ref.getArcrole().isPresent()) {
                    lpt.setArcrole(ref.getArcrole().get());
                }
                if (ref.getRole().isPresent()) {
                    lpt.setRole(ref.getRole().get());
                }
                if (ref.getShow().isPresent()) {
                    lpt.setShow(ShowType.Enum.forString(ref.getShow().get()));
                }
                if (ref.getType().isPresent()) {
                    lpt.setType(TypeType.Enum.forString(ref.getType().get()));
                }
            }
        }
    }

    private void addSpecimenType(SFSpecimenType sfst, SfSpecimen specimen) throws EncodingException {
        if (specimen.isSetSpecimenType()) {
            sfst.addNewSpecimenType().set(encodeGML32(specimen.getSpecimenType()));
        }
    }

    private XmlObject encodeGML32(Object o, EncodingContext ec) throws EncodingException {
        return encodeObjectToXml(GmlConstants.NS_GML_32, o, ec);
    }

    private XmlObject encodeGML32(Object o) throws EncodingException {
        return encodeObjectToXml(GmlConstants.NS_GML_32, o);
    }

}
