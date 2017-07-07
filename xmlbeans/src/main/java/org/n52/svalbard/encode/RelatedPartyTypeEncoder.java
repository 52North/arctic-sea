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
import java.util.Set;

import org.apache.xmlbeans.XmlObject;
import org.n52.shetland.inspire.base2.Contact;
import org.n52.shetland.inspire.base2.InspireBase2Constants;
import org.n52.shetland.inspire.base2.RelatedParty;
import org.n52.shetland.iso.gmd.GmdConstants;
import org.n52.shetland.ogc.gml.GmlConstants;
import org.n52.shetland.w3c.Nillable;
import org.n52.shetland.w3c.xlink.Reference;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.exception.UnsupportedEncoderInputException;

import com.google.common.collect.Sets;

import eu.europa.ec.inspire.schemas.base2.x20.ContactType;
import eu.europa.ec.inspire.schemas.base2.x20.ContactType.TelephoneFacsimile;
import eu.europa.ec.inspire.schemas.base2.x20.ContactType.TelephoneVoice;
import eu.europa.ec.inspire.schemas.base2.x20.ContactType.Website;
import eu.europa.ec.inspire.schemas.base2.x20.RelatedPartyType;

public class RelatedPartyTypeEncoder extends AbstractXmlEncoder<XmlObject, RelatedParty> {

    private static final Set<EncoderKey> ENCODER_KEYS =
            Sets.newHashSet(new ClassToClassEncoderKey(RelatedPartyType.class, RelatedParty.class),
                    new XmlEncoderKey(InspireBase2Constants.NS_BASE2, RelatedParty.class));

    @Override
    public Set<EncoderKey> getKeys() {
        return Collections.unmodifiableSet(ENCODER_KEYS);
    }

    @Override
    public XmlObject encode(RelatedParty relatedParty, EncodingContext context)
            throws EncodingException, UnsupportedEncoderInputException {
        return createRelatedParty(relatedParty);
    }

    private XmlObject createRelatedParty(RelatedParty relatedParty) throws EncodingException {
        RelatedPartyType rpt = RelatedPartyType.Factory.newInstance();
        addContact(rpt, relatedParty);
        addIndividualName(rpt, relatedParty);
        addOrganisationName(rpt, relatedParty);
        addPositionName(rpt, relatedParty);
        addRole(rpt, relatedParty);
        return rpt;
    }

    private void addContact(RelatedPartyType rpt, RelatedParty relatedParty) {
        if (relatedParty.isSetContact()) {
            rpt.addNewContact().setContact(createContact(relatedParty.getContact().get()));
        } else {
            rpt.setNilContact();
        }
    }

    private ContactType createContact(Contact contact) {
        ContactType ct = ContactType.Factory.newInstance();
        ct.addNewAddress().setNil();
        if (contact.getAddress().isNil() && contact.getAddress().getNilReason().isPresent()) {
            ct.getAddress().setNilReason(contact.getAddress().getNilReason().get());
        }
        ct.addNewContactInstructions();
        if (contact.getElectronicMailAddress().isPresent()) {
            ct.addNewElectronicMailAddress().setStringValue(contact.getElectronicMailAddress().get());
        } else if (contact.getElectronicMailAddress().isNil()) {
            ct.addNewElectronicMailAddress().setNil();
            if (contact.getElectronicMailAddress().getNilReason().isPresent()) {
                ct.getElectronicMailAddress().setNilReason(contact.getElectronicMailAddress().getNilReason().get());
            }
        }
        if (contact.getTelephoneFacsimile().isPresent()) {
            for (Nillable<String> telephoneFacsimile : contact.getTelephoneFacsimile().get()) {
                if (telephoneFacsimile.isPresent()) {
                    ct.addNewTelephoneFacsimile().setStringValue(telephoneFacsimile.get());
                }
            }
        } else if (contact.getTelephoneFacsimile().isNil()) {
            TelephoneFacsimile tf = ct.addNewTelephoneFacsimile();
            tf.setNil();
            if (contact.getTelephoneFacsimile().getNilReason().isPresent()) {
                tf.setNilReason(contact.getTelephoneFacsimile().getNilReason().get());
            }
        }
        if (contact.getTelephoneVoice().isPresent()) {
            for (Nillable<String> telephoneVoice : contact.getTelephoneVoice().get()) {
                if (telephoneVoice.isPresent()) {
                    ct.addNewTelephoneVoice().setStringValue(telephoneVoice.get());
                }
            }
        } else if (contact.getTelephoneVoice().isNil()) {
            TelephoneVoice tv = ct.addNewTelephoneVoice();
            tv.setNil();
            if (contact.getTelephoneVoice().getNilReason().isPresent()) {
                tv.setNilReason(contact.getTelephoneVoice().getNilReason().get());
            }
        }
        if (contact.getWebsite().isPresent()) {
            ct.addNewWebsite().setStringValue(contact.getWebsite().get());
        } else if (contact.getWebsite().isNil()) {
            Website w = ct.addNewWebsite();
            w.setNil();
            if (contact.getWebsite().getNilReason().isPresent()) {
                w.setNilReason(contact.getWebsite().getNilReason().get());
            }
        }
        return ct;
    }

    private void addIndividualName(RelatedPartyType rpt, RelatedParty relatedParty) throws EncodingException {
        if (relatedParty.isSetIndividualName()) {
            rpt.addNewIndividualName().addNewPTFreeText().set(encodeGMD(relatedParty.getIndividualName()));
        }
    }

    private void addOrganisationName(RelatedPartyType rpt, RelatedParty relatedParty) throws EncodingException {
        if (relatedParty.isSetOrganisationName()) {
            rpt.addNewOrganisationName().addNewPTFreeText().set(encodeGMD(relatedParty.getOrganisationName()));
        }
    }

    private void addPositionName(RelatedPartyType rpt, RelatedParty relatedParty) throws EncodingException {
        if (relatedParty.isSetPositionName()) {
            rpt.addNewPositionName().addNewPTFreeText().set(encodeGMD(relatedParty.getPositionName()));
        }
    }

    private void addRole(RelatedPartyType rpt, RelatedParty relatedParty) throws EncodingException {
        if (relatedParty.isSetRole()) {
            for (Nillable<Reference> role : relatedParty.getRoles()) {
                if (role.isPresent()) {
                    rpt.addNewRole().set(encodeGML(role.get()));
                }
            }
        }
    }

    protected XmlObject encodeGML(Object o) throws EncodingException {
        return encodeObjectToXml(GmlConstants.NS_GML_32, o);
    }

    protected XmlObject encodeGMD(Object o) throws EncodingException {
        return encodeObjectToXml(GmdConstants.NS_GMD, o);
    }

    protected XmlObject encodeGMD(Object o, EncodingContext context) throws EncodingException {
        return encodeObjectToXml(GmdConstants.NS_GMD, o, context);
    }

}
