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
package org.n52.svalbard.encode;

import org.apache.xmlbeans.XmlObject;
import org.n52.shetland.inspire.ef.AnyDomainLink;
import org.n52.shetland.inspire.ef.EnvironmentalMonitoringFacility;
import org.n52.shetland.inspire.ef.NetworkFacility;
import org.n52.shetland.inspire.ef.OperationalActivityPeriod;
import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.gml.ReferenceType;
import org.n52.shetland.util.IdGenerator;
import org.n52.shetland.w3c.xlink.Referenceable;
import org.n52.svalbard.encode.exception.EncodingException;

import eu.europa.ec.inspire.schemas.ef.x40.EnvironmentalMonitoringFacilityDocument;
import eu.europa.ec.inspire.schemas.ef.x40.EnvironmentalMonitoringFacilityType;
import eu.europa.ec.inspire.schemas.ef.x40.EnvironmentalMonitoringFacilityType.BelongsTo;
import eu.europa.ec.inspire.schemas.ef.x40.EnvironmentalMonitoringFacilityType.RelatedTo;
import net.opengis.gml.x32.FeaturePropertyType;

public abstract class AbstractEnvironmentalMonitoringFaciltityEncoder extends AbstractMonitoringFeatureEncoder {

    //
    // private static final Map<SupportedTypeKey, Set<String>> SUPPORTED_TYPES =
    // Collections.singletonMap(
    // SupportedTypeKey.FeatureType,
    // (Set<String>) Sets.newHashSet(OGCConstants.UNKNOWN,
    // SfConstants.SAMPLING_FEAT_TYPE_SF_SAMPLING_POINT,
    // SfConstants.SAMPLING_FEAT_TYPE_SF_SAMPLING_CURVE,
    // SfConstants.SAMPLING_FEAT_TYPE_SF_SAMPLING_SURFACE));
    //
    // @Override
    // public Map<SupportedTypeKey, Set<String>> getSupportedTypes() {
    // return Collections.unmodifiableMap(SUPPORTED_TYPES);
    // }

    @Override
    protected String generateGmlId() {
        return "emf_" + IdGenerator.generate(Double.toString(System.currentTimeMillis() * Math.random()));
    }

    @Override
    protected XmlObject createFeature(FeaturePropertyType featurePropertyType, AbstractFeature abstractFeature,
            EncodingContext context) throws EncodingException {
        if (context.has(XmlBeansEncodingFlags.ENCODE) && !context.getBoolean(XmlBeansEncodingFlags.ENCODE)) {
            featurePropertyType.setHref(abstractFeature.getIdentifierCodeWithAuthority().getValue());
            if (abstractFeature.isSetName()) {
                featurePropertyType.setTitle(abstractFeature.getFirstName().getValue());
            }
            return featurePropertyType;
        }
        EnvironmentalMonitoringFacilityType emft =
                createEnvironmentalMonitoringFaciltityType((EnvironmentalMonitoringFacility) abstractFeature);
        EnvironmentalMonitoringFacilityDocument emfd =
                EnvironmentalMonitoringFacilityDocument.Factory.newInstance(getXmlOptions());
        emfd.setEnvironmentalMonitoringFacility(emft);
        return emfd;
    }

    protected EnvironmentalMonitoringFacilityType createEnvironmentalMonitoringFaciltityType(
            EnvironmentalMonitoringFacility environmentalMonitoringFacility) throws EncodingException {
        EnvironmentalMonitoringFacilityType emft = EnvironmentalMonitoringFacilityType.Factory.newInstance();
        return encodeEnvironmentalMonitoringFaciltityType(emft, environmentalMonitoringFacility);
    }

    protected EnvironmentalMonitoringFacilityType encodeEnvironmentalMonitoringFaciltityType(
            EnvironmentalMonitoringFacilityType emft, EnvironmentalMonitoringFacility environmentalMonitoringFacility)
            throws EncodingException {
        encodeAbstractMonitoringFeature(emft, environmentalMonitoringFacility);
        setRepresentativePoint(emft, environmentalMonitoringFacility);
        setMeasurementRegime(emft, environmentalMonitoringFacility);
        setMobile(emft, environmentalMonitoringFacility);
        setResultAcquisitionSource(emft, environmentalMonitoringFacility);
        setSpecialisedEMFType(emft, environmentalMonitoringFacility);
        setOperationalActivityPeriod(emft, environmentalMonitoringFacility);
        setRelatedTo(emft, environmentalMonitoringFacility);
        setBelongsTo(emft, environmentalMonitoringFacility);
        return emft;
    }

    private void setRepresentativePoint(EnvironmentalMonitoringFacilityType emft,
            EnvironmentalMonitoringFacility environmentalMonitoringFacility) throws EncodingException {
        if (environmentalMonitoringFacility.isSetRepresentativePoint()) {
            emft.addNewRepresentativePoint().addNewPoint()
                    .set(encodeGML32(environmentalMonitoringFacility.getRepresentativePoint()));
        }
    }

    private void setMeasurementRegime(EnvironmentalMonitoringFacilityType emft,
            EnvironmentalMonitoringFacility environmentalMonitoringFacility) throws EncodingException {
        if (environmentalMonitoringFacility.isSetMeasurementRegime()) {
            emft.addNewMeasurementRegime().set(encodeGML32(environmentalMonitoringFacility.getMeasurementRegime()));
        } else {
            emft.addNewMeasurementRegime().setNil();
        }
    }

    private void setMobile(EnvironmentalMonitoringFacilityType emft,
            EnvironmentalMonitoringFacility environmentalMonitoringFacility) {
        if (environmentalMonitoringFacility.isSetMobile()) {
            emft.addNewMobile().setBooleanValue(environmentalMonitoringFacility.isMobile());
        } else {
            emft.addNewMobile().setNil();
        }
    }

    private void setResultAcquisitionSource(EnvironmentalMonitoringFacilityType emft,
            EnvironmentalMonitoringFacility environmentalMonitoringFacility) throws EncodingException {
        if (environmentalMonitoringFacility.isSetResultAcquisitionSource()) {
            for (ReferenceType resultAcquisitionSource : environmentalMonitoringFacility
                    .getResultAcquisitionSource()) {
                emft.addNewResultAcquisitionSource().set(encodeGML32(resultAcquisitionSource));
            }
        }
    }

    private void setSpecialisedEMFType(EnvironmentalMonitoringFacilityType emft,
            EnvironmentalMonitoringFacility environmentalMonitoringFacility) throws EncodingException {
        if (environmentalMonitoringFacility.isSetSpecialisedEMFType()) {
            emft.addNewSpecialisedEMFType().set(encodeGML32(environmentalMonitoringFacility.getSpecialisedEMFType()));
        }
    }

    private void setOperationalActivityPeriod(EnvironmentalMonitoringFacilityType emft,
            EnvironmentalMonitoringFacility envMoniFac) throws EncodingException {
        if (envMoniFac.isSetOperationalActivityPeriod()) {
            for (Referenceable<OperationalActivityPeriod> operationalActivityPeriod : envMoniFac
                    .getOperationalActivityPeriod()) {
                if (operationalActivityPeriod.isReference()) {
                    EnvironmentalMonitoringFacilityType.OperationalActivityPeriod oap =
                            emft.addNewOperationalActivityPeriod();
                    oap.setHref(operationalActivityPeriod.getReference().getHref().toString());
                    if (operationalActivityPeriod.getReference().getTitle().isPresent()) {
                        oap.setTitle(operationalActivityPeriod.getReference().getTitle().toString());
                    }
                } else {
                    emft.addNewOperationalActivityPeriod()
                            .set(encodeEF(operationalActivityPeriod.getInstance().get()));
                }
            }
        } else {
            emft.addNewOperationalActivityPeriod().setNil();
        }
    }

    private void setRelatedTo(EnvironmentalMonitoringFacilityType emft,
            EnvironmentalMonitoringFacility environmentalMonitoringFacility) throws EncodingException {
        if (environmentalMonitoringFacility.isSetRelatedTo()) {
            for (Referenceable<AnyDomainLink> relatedTo : environmentalMonitoringFacility.getRelatedTo()) {
                if (relatedTo.isReference()) {
                    RelatedTo rt = emft.addNewRelatedTo();
                    rt.setHref(relatedTo.getReference().getHref().toString());
                    if (relatedTo.getReference().getTitle().isPresent()) {
                        rt.setTitle(relatedTo.getReference().getTitle().toString());
                    }
                } else {
                    emft.addNewRelatedTo().addNewAnyDomainLink().set(encodeEF(relatedTo.getInstance().get()));
                }
            }
        }
    }

    private void setBelongsTo(EnvironmentalMonitoringFacilityType emft,
            EnvironmentalMonitoringFacility environmentalMonitoringFacility) throws EncodingException {
        if (environmentalMonitoringFacility.isSetBelongsTo()) {
            for (Referenceable<NetworkFacility> belongsTo : environmentalMonitoringFacility.getBelongsTo()) {
                if (belongsTo.isReference()) {
                    BelongsTo bt = emft.addNewBelongsTo();
                    bt.setHref(belongsTo.getReference().getHref().toString());
                    if (belongsTo.getReference().getTitle().isPresent()) {
                        bt.setTitle(belongsTo.getReference().getTitle().toString());
                    }
                } else {
                    emft.addNewBelongsTo().addNewNetworkFacility().set(encodeEF(belongsTo.getInstance().get()));
                }
            }
        }
    }

}
