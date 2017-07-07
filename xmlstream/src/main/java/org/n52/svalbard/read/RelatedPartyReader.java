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
package org.n52.svalbard.read;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.n52.shetland.aqd.AqdConstants;
import org.n52.shetland.inspire.base2.RelatedParty;
import org.n52.svalbard.decode.exception.DecodingException;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class RelatedPartyReader extends XmlReader<RelatedParty> {

    private RelatedParty relatedParty;

    @Override
    protected void begin() {
        this.relatedParty = new RelatedParty();
    }

    @Override
    protected void read(QName name)
            throws XMLStreamException, DecodingException {
        if (name.equals(AqdConstants.QN_BASE2_INDIVIDUAL_NAME)) {
            this.relatedParty
                    .setIndividualName(delegate(new NillableFreeTextReader()));
        } else if (name.equals(AqdConstants.QN_BASE2_ORGANISATION_NAME)) {
            this.relatedParty
                    .setOrganisationName(delegate(new NillableFreeTextReader()));
        } else if (name.equals(AqdConstants.QN_BASE2_POSITION_NAME)) {
            this.relatedParty
                    .setPositionName(delegate(new NillableFreeTextReader()));
        } else if (name.equals(AqdConstants.QN_BASE2_CONTACT)) {
            this.relatedParty.setContact(delegate(new NillableContactReader()));
        } else if (name.equals(AqdConstants.QN_BASE2_ROLE)) {
            this.relatedParty.addRole(delegate(new NillableReferenceReader()));
        } else {
            ignore();
        }
    }

    @Override
    protected RelatedParty finish()
            throws DecodingException {
        return this.relatedParty;
    }

}
