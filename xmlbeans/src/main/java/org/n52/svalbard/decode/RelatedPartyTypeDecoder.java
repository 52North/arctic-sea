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

import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.xml.stream.XMLStreamException;

import org.apache.xmlbeans.XmlObject;
import org.n52.shetland.inspire.ad.AddressRepresentation;
import org.n52.shetland.inspire.base2.Contact;
import org.n52.shetland.inspire.base2.RelatedParty;
import org.n52.shetland.inspire.ompr.InspireOMPRConstants;
import org.n52.shetland.iso.gmd.PT_FreeText;
import org.n52.shetland.w3c.Nillable;
import org.n52.shetland.w3c.xlink.Reference;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.decode.exception.UnsupportedDecoderInputException;
import org.n52.svalbard.read.NillableReferenceReader;
import org.n52.svalbard.util.CodingHelper;

import com.google.common.collect.Lists;

import eu.europa.ec.inspire.schemas.base2.x20.ContactType;
import eu.europa.ec.inspire.schemas.base2.x20.ContactType.TelephoneFacsimile;
import eu.europa.ec.inspire.schemas.base2.x20.ContactType.TelephoneVoice;
import eu.europa.ec.inspire.schemas.base2.x20.RelatedPartyType;

public class RelatedPartyTypeDecoder
        extends AbstractXmlDecoder<XmlObject, RelatedParty> {

    private static final Set<DecoderKey> DECODER_KEYS =
            CodingHelper.decoderKeysForElements(InspireOMPRConstants.NS_OMPR_30, RelatedPartyType.class);

    @Override
    public Set<DecoderKey> getKeys() {
        return Collections.unmodifiableSet(DECODER_KEYS);
    }

    @Override
    public RelatedParty decode(XmlObject xmlObject) throws DecodingException {
        if (xmlObject instanceof RelatedPartyType) {
            RelatedPartyType rpt = (RelatedPartyType) xmlObject;
            RelatedParty relatedParty = new RelatedParty();
            relatedParty.setContact(parseContact(rpt));
            relatedParty.setIndividualName((PT_FreeText) decodeXmlElement(rpt.getIndividualName()));
            relatedParty.setOrganisationName((PT_FreeText) decodeXmlElement(rpt.getOrganisationName()));
            relatedParty.setPositionName((PT_FreeText) decodeXmlElement(rpt.getPositionName()));
            relatedParty.setRoles(parseRole(rpt));
            return relatedParty;
        }
        throw new UnsupportedDecoderInputException(this, xmlObject);
    }

    private Contact parseContact(RelatedPartyType rpt) throws DecodingException {
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
        return Nillable.<AddressRepresentation> nil();
    }

    private Nillable<PT_FreeText> parseContactInstructions(ContactType ct) throws DecodingException {
        if (ct.isNilContactInstructions()) {
            if (ct.getContactInstructions().isSetNilReason()) {
                return Nillable.<PT_FreeText> nil(ct.getContactInstructions().getNilReason().toString());
            }
            return Nillable.<PT_FreeText> nil();
        }
        return Nillable.<PT_FreeText> of((PT_FreeText) decodeXmlElement(ct.getContactInstructions()));
    }

    private Nillable<String> parseElectronicMailAddress(ContactType ct) {
        if (ct.isNilElectronicMailAddress()) {
            if (ct.getElectronicMailAddress().isSetNilReason()) {
                return Nillable.<String> nil(ct.getElectronicMailAddress().getNilReason().toString());
            }
            return Nillable.<String> nil();
        }
        return Nillable.<String> of(ct.getElectronicMailAddress().getStringValue());
    }

    private Nillable<List<Nillable<String>>> parseTelephoneFacsimile(ContactType ct) {
        List<Nillable<String>> list = Lists.newArrayList();
        for (TelephoneFacsimile tf : ct.getTelephoneFacsimileArray()) {
            if (tf.isNil() && tf.isSetNilReason()) {
                return Nillable.<List<Nillable<String>>> nil(tf.getNilReason().toString());
            } else {
                list.add(Nillable.of(tf.getStringValue()));
            }
        }
        return Nillable.of(list);
    }

    private Nillable<List<Nillable<String>>> parseTelephoneVoice(ContactType ct) {
        List<Nillable<String>> list = Lists.newArrayList();
        for (TelephoneVoice tv : ct.getTelephoneVoiceArray()) {
            if (tv.isNil() && tv.isSetNilReason()) {
                return Nillable.<List<Nillable<String>>> nil(tv.getNilReason().toString());
            } else {
                list.add(Nillable.of(tv.getStringValue()));
            }
        }
        return Nillable.of(list);
    }

    private Nillable<String> parseWebsite(ContactType ct) {
        if (ct.isNilWebsite()) {
            if (ct.getWebsite().isSetNilReason()) {
                return Nillable.<String> nil(ct.getWebsite().getNilReason().toString());
            }
            return Nillable.<String> nil();
        } else {
            return Nillable.<String> present(ct.getWebsite().getStringValue());
        }
    }

    private List<Nillable<Reference>> parseRole(RelatedPartyType rpt) throws DecodingException {
        try {
            List<Nillable<Reference>> list = Lists.newArrayList();
            for (net.opengis.gml.x32.ReferenceType rt : rpt.getRoleArray()) {
                list.add(new NillableReferenceReader().read(rt.newInputStream()));
            }
            return list;
        } catch (XMLStreamException e) {
            throw new DecodingException(e);
        }
    }

}
