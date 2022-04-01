/*
 * Copyright 2015-2022 52°North Initiative for Geospatial Open Source
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

import org.apache.xmlbeans.XmlObject;
import org.n52.shetland.inspire.base2.LegislationCitation;
import org.n52.shetland.inspire.ef.AbstractMonitoringObject;
import org.n52.shetland.inspire.ef.Hierarchy;
import org.n52.shetland.inspire.ef.ObservingCapability;
import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.gml.CodeType;
import org.n52.shetland.ogc.gml.ReferenceType;
import org.n52.shetland.w3c.xlink.Reference;
import org.n52.shetland.w3c.xlink.Referenceable;
import org.n52.svalbard.encode.exception.EncodingException;

import eu.europa.ec.inspire.schemas.ef.x40.AbstractMonitoringObjectPropertyType;
import eu.europa.ec.inspire.schemas.ef.x40.AbstractMonitoringObjectType;
import eu.europa.ec.inspire.schemas.ef.x40.AbstractMonitoringObjectType.Broader;
import eu.europa.ec.inspire.schemas.ef.x40.AbstractMonitoringObjectType.LegalBackground;
import eu.europa.ec.inspire.schemas.ef.x40.AbstractMonitoringObjectType.Narrower;
import eu.europa.ec.inspire.schemas.ef.x40.AbstractMonitoringObjectType.Supersedes;

public abstract class AbstractMonitoringObjectEncoder
        extends AbstractEnvironmentalFaciltityEncoder<XmlObject, AbstractFeature> {

    protected abstract String generateGmlId();

    protected void encodeAbstractMonitoringObject(AbstractMonitoringObjectType amot,
            AbstractMonitoringObject abstractMonitoringObject) throws EncodingException {
        setGmlId(amot, abstractMonitoringObject);
        setInspireId(amot, abstractMonitoringObject);
        setName(amot, abstractMonitoringObject);
        setAdditionalDescription(amot, abstractMonitoringObject);
        setMediaMonitored(amot, abstractMonitoringObject);
        setLegalBackground(amot, abstractMonitoringObject);
        setResponsibleParty(amot, abstractMonitoringObject);
        setGeometry(amot, abstractMonitoringObject);
        setOnlineResource(amot, abstractMonitoringObject);
        setPurpose(amot, abstractMonitoringObject);
        setObservingCapability(amot, abstractMonitoringObject);
        setBroader(amot, abstractMonitoringObject);
        setNarrower(amot, abstractMonitoringObject);
        setSupersedes(amot, abstractMonitoringObject);
        setSupersededBy(amot, abstractMonitoringObject);
    }

    private void setGmlId(AbstractMonitoringObjectType amot, AbstractMonitoringObject abstractMonitoringObject) {
        if (!abstractMonitoringObject.isSetGmlID()) {
            if (!abstractMonitoringObject.isSetGmlID()) {
                abstractMonitoringObject.setGmlId(generateGmlId());
            }
        }
        amot.setId(abstractMonitoringObject.getGmlId());
    }

    private void setInspireId(AbstractMonitoringObjectType amot, AbstractMonitoringObject abstractMonitoringObject)
            throws EncodingException {
        amot.addNewInspireId().set(encodeBASEPropertyType(abstractMonitoringObject.getInspireId()));
    }

    private void setName(AbstractMonitoringObjectType amot, AbstractMonitoringObject abstractMonitoringObject) {
        if (abstractMonitoringObject.isSetName()) {
            for (CodeType name : abstractMonitoringObject.getName()) {
                if (name.isSetValue()) {
                    amot.addNewName2().setStringValue(name.getValue());
                }
            }
        }
    }

    private void setAdditionalDescription(AbstractMonitoringObjectType amot,
            AbstractMonitoringObject abstractMonitoringObject) {
        if (abstractMonitoringObject.isSetAdditionalDescription()) {
            amot.addNewAdditionalDescription().setStringValue(abstractMonitoringObject.getAdditionalDescription());
        }
    }

    private void setMediaMonitored(AbstractMonitoringObjectType amot,
            AbstractMonitoringObject abstractMonitoringObject) throws EncodingException {
        for (ReferenceType mediaMonitored : abstractMonitoringObject.getMediaMonitored()) {
            amot.addNewMediaMonitored().set(encodeGML32(mediaMonitored));
        }
    }

    private void setLegalBackground(AbstractMonitoringObjectType amot,
            AbstractMonitoringObject abstractMonitoringObject) throws EncodingException {
        if (abstractMonitoringObject.isSetLegalBackground()) {
            for (Referenceable<LegislationCitation> legislationCitation : abstractMonitoringObject
                    .getLegalBackground()) {
                if (legislationCitation.isReference()) {
                    Reference reference = legislationCitation.getReference();
                    if (reference.getHref().isPresent()) {
                        LegalBackground lb = amot.addNewLegalBackground();
                        lb.setHref(reference.getHref().get().toString());
                        if (reference.getTitle().isPresent()) {
                            lb.setTitle(reference.getTitle().get());
                        }
                    }
                } else {
                    amot.addNewLegalBackground().addNewLegislationCitation().set(encodeEF(legislationCitation));
                }
            }
        }

    }

    private void setResponsibleParty(AbstractMonitoringObjectType amot,
            AbstractMonitoringObject abstractMonitoringObject) throws EncodingException {
        if (abstractMonitoringObject.isSetResponsibleParty()) {
            amot.addNewResponsibleParty().addNewRelatedParty()
                    .set(encodeBASE2(abstractMonitoringObject.getResponsibleParty()));
        }
    }

    private void setGeometry(AbstractMonitoringObjectType amot, AbstractMonitoringObject abstractMonitoringObject)
            throws EncodingException {
        if (abstractMonitoringObject.isSetGeometry()) {
            if (abstractMonitoringObject.isSetGmlID()) {
                amot.addNewGeometry().set(encodeGML32(abstractMonitoringObject.getGeometry(),
                        new EncodingContext().with(XmlBeansEncodingFlags.GMLID, abstractMonitoringObject.getGmlId())));
            } else {
                amot.addNewGeometry().set(encodeGML32(abstractMonitoringObject.getGeometry()));
            }
        }
    }

    private void setOnlineResource(AbstractMonitoringObjectType amot,
            AbstractMonitoringObject abstractMonitoringObject) {
        if (abstractMonitoringObject.isSetOnlineResources()) {
            for (String onlineResource : abstractMonitoringObject.getOnlineResource()) {
                amot.addNewOnlineResource().setStringValue(onlineResource);
            }
        }
    }

    private void setPurpose(AbstractMonitoringObjectType amot, AbstractMonitoringObject abstractMonitoringObject)
            throws EncodingException {
        if (abstractMonitoringObject.isSetPurpose()) {
            for (ReferenceType purpose : abstractMonitoringObject.getPurpose()) {
                amot.addNewPurpose().set(encodeGML32(purpose));
            }
        }
    }

    private void setObservingCapability(AbstractMonitoringObjectType amot,
            AbstractMonitoringObject abstractMonitoringObject) throws EncodingException {
        if (abstractMonitoringObject.isSetObservingCapability()) {
            for (Referenceable<ObservingCapability> observingCapability : abstractMonitoringObject
                    .getObservingCapability()) {
                if (observingCapability.isReference() && observingCapability.getReference().getHref().isPresent()) {
                    eu.europa.ec.inspire.schemas.ef.x40.AbstractMonitoringObjectType.ObservingCapability oc =
                            amot.addNewObservingCapability();
                    oc.setHref(observingCapability.getReference().getHref().get().toString());
                    if (observingCapability.getReference().getTitle().isPresent()) {
                        oc.setTitle(observingCapability.getReference().getTitle().get());
                    }
                } else {
                    amot.addNewObservingCapability().addNewObservingCapability().set(encodeEF(observingCapability));
                }
            }
        }
    }

    private void setBroader(AbstractMonitoringObjectType amot, AbstractMonitoringObject abstractMonitoringObject)
            throws EncodingException {
        if (abstractMonitoringObject.isSetBroader()) {
            Referenceable<Hierarchy> broader = abstractMonitoringObject.getBroader();
            if (broader.isReference()) {
                Broader b = amot.addNewBroader();
                b.setHref(broader.getReference().getHref().get().toString());
                if (broader.getReference().getTitle().isPresent()) {
                    b.setTitle(broader.getReference().getTitle().get());
                }
            } else {
                amot.addNewBroader().addNewHierarchy().set(encodeEF(broader.getInstance().get()));
            }
        }
    }

    private void setNarrower(AbstractMonitoringObjectType amot, AbstractMonitoringObject abstractMonitoringObject)
            throws EncodingException {
        if (abstractMonitoringObject.isSetNarrower()) {
            for (Referenceable<Hierarchy> narrower : abstractMonitoringObject.getNarrower()) {
                if (narrower.isReference()) {
                    Narrower n = amot.addNewNarrower();
                    n.setHref(narrower.getReference().getHref().get().toString());
                    if (narrower.getReference().getTitle().isPresent()) {
                        n.setTitle(narrower.getReference().getTitle().get());
                    }
                } else {
                    amot.addNewNarrower().addNewHierarchy().set(encodeEF(narrower.getInstance().get()));
                }
            }
        }
    }

    private void setSupersedes(AbstractMonitoringObjectType amot, AbstractMonitoringObject abstractMonitoringObject)
            throws EncodingException {
        if (abstractMonitoringObject.isSetSupersedes()) {
            for (Referenceable<AbstractMonitoringObject> supersedes : abstractMonitoringObject.getSupersedes()) {
                if (supersedes.isReference()) {
                    Supersedes s = amot.addNewSupersedes();
                    s.setHref(supersedes.getReference().getHref().get().toString());
                    if (supersedes.getReference().getTitle().isPresent()) {
                        s.setTitle(supersedes.getReference().getTitle().get());
                    }
                } else {
                    amot.addNewSupersedes().addNewAbstractMonitoringObject()
                            .set(encodeEF(supersedes.getInstance().get()));
                }
            }
        }
    }

    private void setSupersededBy(AbstractMonitoringObjectType amot, AbstractMonitoringObject abstractMonitoringObject)
            throws EncodingException {
        if (abstractMonitoringObject.isSetSupersededBy()) {
            for (Referenceable<AbstractMonitoringObject> supersededBy : abstractMonitoringObject.getSupersededBy()) {
                if (supersededBy.isReference()) {
                    AbstractMonitoringObjectPropertyType sb = amot.addNewSupersededBy();
                    sb.setHref(supersededBy.getReference().getHref().get().toString());
                    if (supersededBy.getReference().getTitle().isPresent()) {
                        sb.setTitle(supersededBy.getReference().getTitle().get());
                    }
                } else {
                    amot.addNewSupersededBy().addNewAbstractMonitoringObject()
                            .set(encodeEF(supersededBy.getInstance().get()));
                }
            }
        }
    }
}
