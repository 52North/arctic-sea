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

import org.apache.xmlbeans.XmlObject;
import org.n52.shetland.inspire.base2.LegislationCitation;
import org.n52.shetland.inspire.ef.AbstractMonitoringObject;
import org.n52.shetland.inspire.ef.Hierarchy;
import org.n52.shetland.inspire.ef.ObservingCapability;
import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.gml.CodeType;
import org.n52.shetland.ogc.gml.ReferenceType;
import org.n52.shetland.w3c.xlink.SimpleAttrs;
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
            for (LegislationCitation legislationCitation : abstractMonitoringObject.getLegalBackground()) {
                if (legislationCitation.isSetSimpleAttrs()) {
                    SimpleAttrs simpleAttrs = legislationCitation.getSimpleAttrs();
                    if (simpleAttrs.isSetHref()) {
                        LegalBackground lb = amot.addNewLegalBackground();
                        lb.setHref(simpleAttrs.getHref());
                        if (simpleAttrs.isSetTitle()) {
                            lb.setTitle(simpleAttrs.getTitle());
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
            for (ObservingCapability observingCapability : abstractMonitoringObject.getObservingCapability()) {
                if (observingCapability.isSetHref()) {
                    eu.europa.ec.inspire.schemas.ef.x40.AbstractMonitoringObjectType.ObservingCapability oc =
                            amot.addNewObservingCapability();
                    oc.setHref(observingCapability.getHref());
                    if (observingCapability.isSetTitle()) {
                        oc.setTitle(observingCapability.getTitle());
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
            Hierarchy broader = abstractMonitoringObject.getBroader();
            if (broader.isSetSimpleAttrs()) {
                Broader b = amot.addNewBroader();
                b.setHref(broader.getSimpleAttrs().getHref());
                if (broader.getSimpleAttrs().isSetTitle()) {
                    b.setTitle(broader.getSimpleAttrs().getTitle());
                }
            } else {
                amot.addNewBroader().addNewHierarchy().set(encodeEF(broader));
            }
        }
    }

    private void setNarrower(AbstractMonitoringObjectType amot, AbstractMonitoringObject abstractMonitoringObject)
            throws EncodingException {
        if (abstractMonitoringObject.isSetNarrower()) {
            for (Hierarchy narrower : abstractMonitoringObject.getNarrower()) {
                if (narrower.isSetSimpleAttrs()) {
                    Narrower n = amot.addNewNarrower();
                    n.setHref(narrower.getSimpleAttrs().getHref());
                    if (narrower.getSimpleAttrs().isSetTitle()) {
                        n.setTitle(narrower.getSimpleAttrs().getTitle());
                    }
                } else {
                    amot.addNewNarrower().addNewHierarchy().set(encodeEF(narrower));
                }
            }
        }
    }

    private void setSupersedes(AbstractMonitoringObjectType amot, AbstractMonitoringObject abstractMonitoringObject)
            throws EncodingException {
        if (abstractMonitoringObject.isSetSupersedes()) {
            for (AbstractMonitoringObject supersedes : abstractMonitoringObject.getSupersedes()) {
                if (supersedes.isSetSimpleAttrs()) {
                    Supersedes s = amot.addNewSupersedes();
                    s.setHref(supersedes.getSimpleAttrs().getHref());
                    if (supersedes.getSimpleAttrs().isSetTitle()) {
                        s.setTitle(supersedes.getSimpleAttrs().getTitle());
                    }
                } else {
                    amot.addNewSupersedes().addNewAbstractMonitoringObject().set(encodeEF(supersedes));
                }
            }
        }
    }

    private void setSupersededBy(AbstractMonitoringObjectType amot, AbstractMonitoringObject abstractMonitoringObject)
            throws EncodingException {
        if (abstractMonitoringObject.isSetSupersededBy()) {
            for (AbstractMonitoringObject supersededBy : abstractMonitoringObject.getSupersededBy()) {
                if (supersededBy.isSetSimpleAttrs()) {
                    AbstractMonitoringObjectPropertyType sb = amot.addNewSupersededBy();
                    sb.setHref(supersededBy.getSimpleAttrs().getHref());
                    if (supersededBy.getSimpleAttrs().isSetTitle()) {
                        sb.setTitle(supersededBy.getSimpleAttrs().getTitle());
                    }
                } else {
                    amot.addNewSupersededBy().addNewAbstractMonitoringObject().set(encodeEF(supersededBy));
                }
            }
        }
    }
}
