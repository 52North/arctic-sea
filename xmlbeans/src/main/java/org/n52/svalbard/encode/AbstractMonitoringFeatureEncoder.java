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

import org.n52.shetland.inspire.ef.AbstractMonitoringFeature;
import org.n52.shetland.inspire.ef.EnvironmentalMonitoringActivity;
import org.n52.shetland.inspire.ef.ReportToLegalAct;
import org.n52.shetland.ogc.om.OmObservation;
import org.n52.shetland.w3c.xlink.Reference;
import org.n52.shetland.w3c.xlink.Referenceable;
import org.n52.svalbard.encode.exception.EncodingException;

import eu.europa.ec.inspire.schemas.ef.x40.AbstractMonitoringFeatureType;
import eu.europa.ec.inspire.schemas.ef.x40.AbstractMonitoringFeatureType.InvolvedIn;

public abstract class AbstractMonitoringFeatureEncoder
        extends AbstractMonitoringObjectEncoder {

    protected void encodeAbstractMonitoringFeature(AbstractMonitoringFeatureType amft,
            AbstractMonitoringFeature abstractMonitoringFeature) throws EncodingException {
        encodeAbstractMonitoringObject(amft, abstractMonitoringFeature);
        setReportedTo(amft, abstractMonitoringFeature);
        setHasObservation(amft, abstractMonitoringFeature);
        setInvolvedIn(amft, abstractMonitoringFeature);
    }

    private void setReportedTo(AbstractMonitoringFeatureType amft, AbstractMonitoringFeature abstractMonitoringFeature)
            throws EncodingException {
        if (abstractMonitoringFeature.isSetReportedTo()) {
            for (ReportToLegalAct reportToLegalAct : abstractMonitoringFeature.getReportedTo()) {
                amft.addNewReportedTo().addNewReportToLegalAct().set(encodeEF(reportToLegalAct));
            }
        }
    }

    private void setHasObservation(AbstractMonitoringFeatureType amft,
            AbstractMonitoringFeature abstractMonitoringFeature) {
        if (abstractMonitoringFeature.isSetHasObservation()) {
            for (Referenceable<OmObservation> referenceable : abstractMonitoringFeature.getHasObservation()) {
                if (referenceable.isReference()) {
                    Reference reference = referenceable.getReference();
                    if (reference.getHref().isPresent()) {
                        amft.addNewHasObservation().setHref(reference.getHref().toString());
                    }
                }
            }
        }
    }

    private void setInvolvedIn(AbstractMonitoringFeatureType amft, AbstractMonitoringFeature abstractMonitoringFeature)
            throws EncodingException {
        if (abstractMonitoringFeature.isSetInvolvedIn()) {
            for (EnvironmentalMonitoringActivity environmentalMonitoringActivity : abstractMonitoringFeature
                    .getInvolvedIn()) {
                if (environmentalMonitoringActivity.isSetSimpleAttrs()) {
                    InvolvedIn ii = amft.addNewInvolvedIn();
                    ii.setHref(environmentalMonitoringActivity.getSimpleAttrs().getHref());
                    if (environmentalMonitoringActivity.getSimpleAttrs().isSetTitle()) {
                        ii.setTitle(environmentalMonitoringActivity.getSimpleAttrs().getTitle());
                    }
                } else {
                    amft.addNewInvolvedIn().addNewEnvironmentalMonitoringActivity()
                            .set(encodeEF(environmentalMonitoringActivity));
                }
            }
        }
    }

}
