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
package org.n52.svalbard.decode.inspire;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.xmlbeans.XmlObject;
import org.n52.sos.decode.AbstractXmlDecoder;
import org.n52.sos.decode.DecoderKey;
import org.n52.sos.exception.ows.concrete.UnsupportedDecoderInputException;
import org.n52.sos.iso.gmd.PT_FreeText;
import org.n52.sos.ogc.gml.ReferenceType;
import org.n52.sos.ogc.ows.OwsExceptionReport;
import org.n52.sos.util.CodingHelper;
import org.n52.sos.w3c.Nillable;
import org.n52.svalbard.inspire.ad.AddressRepresentation;
import org.n52.svalbard.inspire.base2.Contact;
import org.n52.svalbard.inspire.base2.RelatedParty;
import org.n52.svalbard.inspire.ompr.InspireOMPRConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import eu.europa.ec.inspire.schemas.base2.x20.ContactType;
import eu.europa.ec.inspire.schemas.base2.x20.ContactType.TelephoneFacsimile;
import eu.europa.ec.inspire.schemas.base2.x20.ContactType.TelephoneVoice;
import eu.europa.ec.inspire.schemas.base2.x20.RelatedPartyType;

public class RelatedPartyTypeDecoder extends AbstractXmlDecoder<RelatedParty> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RelatedPartyTypeDecoder.class);

    private static final Set<DecoderKey> DECODER_KEYS = CodingHelper.decoderKeysForElements(
            InspireOMPRConstants.NS_OMPR_30, RelatedPartyType.class);

    @Override
    public Set<DecoderKey> getDecoderKeyTypes() {
        return Collections.unmodifiableSet(DECODER_KEYS);
    }

    @Override
    public RelatedParty decode(XmlObject xmlObject)
            throws OwsExceptionReport, UnsupportedDecoderInputException {
        if (xmlObject instanceof RelatedPartyType) {
            RelatedPartyType rpt = (RelatedPartyType) xmlObject;
            RelatedParty relatedParty = new RelatedParty();
            relatedParty.setContact(parseContact(rpt));
            relatedParty.setIndividualName((PT_FreeText)CodingHelper.decodeXmlElement(rpt.getIndividualName()));
            relatedParty.setOrganisationName((PT_FreeText)CodingHelper.decodeXmlElement(rpt.getOrganisationName()));
            relatedParty.setPositionName((PT_FreeText)CodingHelper.decodeXmlElement(rpt.getPositionName()));
            relatedParty.setRole(parseRole(rpt));
            return relatedParty;
        }
        throw new UnsupportedDecoderInputException(this, xmlObject);
    }

    private Contact parseContact(RelatedPartyType rpt) throws OwsExceptionReport {
        ContactType ct = rpt.getContact().getContact();
        Contact contact = new Contact();
        contact.setAddress(parseAddress(ct));
        contact.setContactInstructions(parseContactInstructions(ct));
        contact.setElectronicMailAddress(parseElectronicMailAddress(ct));
        contact.setTelephoneFacsimile(parseTelephoneFacsimile(ct));
        contact.setTelephoneVoice(parseTelephoneVoice(ct));
        contact.setWebsite(parseWebsite(ct));
        return contact;
    }

    private Nillable<AddressRepresentation> parseAddress(ContactType ct) {
        return Nillable.<AddressRepresentation>nil();
    }

    private Nillable<PT_FreeText> parseContactInstructions(ContactType ct) throws OwsExceptionReport {
        if (ct.isNilContactInstructions()) {
            if (ct.getContactInstructions().isSetNilReason()) {
                return Nillable.<PT_FreeText>nil(ct.getContactInstructions().getNilReason().toString());
            }
            return Nillable.<PT_FreeText>nil();
        }
        return Nillable.<PT_FreeText>of((PT_FreeText)CodingHelper.decodeXmlElement(ct.getContactInstructions()));
    }

    private Nillable<String> parseElectronicMailAddress(ContactType ct) {
        if (ct.isNilElectronicMailAddress()) {
            if (ct.getElectronicMailAddress().isSetNilReason()) {
                return Nillable.<String>nil(ct.getElectronicMailAddress().getNilReason().toString());
            }
            return Nillable.<String>nil();
        }
        return Nillable.<String>of(ct.getElectronicMailAddress().getStringValue());
    }

    private Nillable<List<String>> parseTelephoneFacsimile(ContactType ct) {
        List<String> list = Lists.newArrayList();
        for (TelephoneFacsimile tf : ct.getTelephoneFacsimileArray()) {
            if (tf.isNil() && tf.isSetNilReason()) {
                return Nillable.<List<String>>nil(tf.getNilReason().toString());
            } else {
                list.add(tf.getStringValue());
            }
        }
        return Nillable.of(list);
    }

    private Nillable<List<String>> parseTelephoneVoice(ContactType ct) {
        List<String> list = Lists.newArrayList();
        for (TelephoneVoice tv : ct.getTelephoneVoiceArray()) {
            if (tv.isNil() && tv.isSetNilReason()) {
                return Nillable.<List<String>>nil(tv.getNilReason().toString());
            } else {
                list.add(tv.getStringValue());
            }
        }
        return Nillable.of(list);
    }

    private Nillable<String> parseWebsite(ContactType ct) {
        if (ct.isNilWebsite()) {
            if (ct.getWebsite().isSetNilReason()) {
                return Nillable.<String>nil(ct.getWebsite().getNilReason().toString());
            }
            return Nillable.<String>nil();
        } else {
            return Nillable.<String>present(ct.getWebsite().getStringValue());
        }
    }

    private Set<ReferenceType> parseRole(RelatedPartyType rpt) throws OwsExceptionReport {
        Set<ReferenceType> set = Sets.newHashSet();
        for (net.opengis.gml.x32.ReferenceType rt : rpt.getRoleArray()) {
            set.add((ReferenceType)CodingHelper.decodeXmlElement(rt));
        }
        return set;
    }

}
