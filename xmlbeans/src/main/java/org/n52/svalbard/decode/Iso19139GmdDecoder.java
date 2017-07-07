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

import org.apache.xmlbeans.XmlObject;
import org.isotc211.x2005.gco.CharacterStringPropertyType;
import org.isotc211.x2005.gmd.CIAddressType;
import org.isotc211.x2005.gmd.CIContactPropertyType;
import org.isotc211.x2005.gmd.CIContactType;
import org.isotc211.x2005.gmd.CIOnlineResourceType;
import org.isotc211.x2005.gmd.CIResponsiblePartyDocument;
import org.isotc211.x2005.gmd.CIResponsiblePartyPropertyType;
import org.isotc211.x2005.gmd.CIResponsiblePartyType;
import org.isotc211.x2005.gmd.CITelephoneType;
import org.isotc211.x2005.gmd.LocalisedCharacterStringPropertyType;
import org.isotc211.x2005.gmd.PTFreeTextDocument;
import org.isotc211.x2005.gmd.PTFreeTextPropertyType;
import org.isotc211.x2005.gmd.PTFreeTextType;
import org.n52.shetland.iso.gmd.GmdConstants;
import org.n52.shetland.iso.gmd.LocalisedCharacterString;
import org.n52.shetland.iso.gmd.PT_FreeText;
import org.n52.shetland.ogc.sensorML.Role;
import org.n52.shetland.ogc.sensorML.SmlResponsibleParty;
import org.n52.shetland.util.CollectionHelper;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.decode.exception.UnsupportedDecoderInputException;
import org.n52.svalbard.decode.exception.UnsupportedDecoderXmlInputException;
import org.n52.svalbard.util.CodingHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

/**
 * {@link Decoder} class to decode ISO TC211 Geographic MetaData (GMD)
 * extensible markup language.
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 */
public class Iso19139GmdDecoder
        extends AbstractXmlDecoder<XmlObject, Object> {

    private static final Logger LOGGER = LoggerFactory.getLogger(Iso19139GmdDecoder.class);

    private static final Set<DecoderKey> DECODER_KEYS = CodingHelper.decoderKeysForElements(GmdConstants.NS_GMD,
            CIResponsiblePartyDocument.class, CIResponsiblePartyPropertyType.class, CIResponsiblePartyType.class);

    public Iso19139GmdDecoder() {
        LOGGER.debug("Decoder for the following keys initialized successfully: {}!",
                Joiner.on(", ").join(DECODER_KEYS));
    }

    @Override
    public Set<DecoderKey> getKeys() {
        return Collections.unmodifiableSet(DECODER_KEYS);
    }

    @Override
    public Object decode(XmlObject element) throws DecodingException {
        if (element instanceof CIResponsiblePartyDocument) {
            return decodeCIResponsibleParty(((CIResponsiblePartyDocument) element).getCIResponsibleParty());
        } else if (element instanceof CIResponsiblePartyPropertyType) {
            return decodeCIResponsiblePartyPropertyType((CIResponsiblePartyPropertyType) element);
        } else if (element instanceof CIResponsiblePartyType) {
            return decodeCIResponsibleParty((CIResponsiblePartyType) element);
        } else if (element instanceof PTFreeTextDocument) {
            return decodePTFreeTextType(((PTFreeTextDocument) element).getPTFreeText());
        } else if (element instanceof PTFreeTextPropertyType) {
            return decodePTFreeTextType(((PTFreeTextPropertyType) element).getPTFreeText());
        } else if (element instanceof PTFreeTextType) {
            return decodePTFreeTextType((PTFreeTextType) element);
        } else {
            throw new UnsupportedDecoderXmlInputException(this, element);
        }
    }

    private PT_FreeText decodePTFreeTextType(PTFreeTextType ptftt) {
        PT_FreeText ptFreeText = new PT_FreeText();
        for (LocalisedCharacterStringPropertyType lcspt : ptftt.getTextGroupArray()) {
            ptFreeText
                    .addTextGroup(new LocalisedCharacterString(lcspt.getLocalisedCharacterString().getStringValue()));
        }
        return ptFreeText;
    }

    private Object decodeCIResponsiblePartyPropertyType(CIResponsiblePartyPropertyType element)
            throws DecodingException {
        if (element.isSetCIResponsibleParty()) {
            return decodeCIResponsibleParty(element.getCIResponsibleParty());
        } else if (element.isSetHref()) {
            SmlResponsibleParty responsibleParty = new SmlResponsibleParty();
            responsibleParty.setHref(element.getHref());
            if (element.isSetTitle()) {
                responsibleParty.setTitle(element.getTitle());
            }
            if (element.isSetRole()) {
                responsibleParty.setRole(element.getRole());
            }
            return responsibleParty;
        }
        throw new UnsupportedDecoderInputException(this, element);
    }

    private Object decodeCIResponsibleParty(CIResponsiblePartyType element) throws DecodingException {
        SmlResponsibleParty responsibleParty = new SmlResponsibleParty();
        if (element.isSetIndividualName()) {
            responsibleParty.setIndividualName(element.getIndividualName().getCharacterString());
        }
        if (element.isSetOrganisationName()) {
            responsibleParty.setOrganizationName(element.getOrganisationName().getCharacterString());
        }
        if (element.isSetPositionName()) {
            responsibleParty.setPositionName(element.getPositionName().getCharacterString());
        }
        if (element.isSetContactInfo()) {
            decodeContactInfo(element.getContactInfo(), responsibleParty);
        }
        if (element.getRole().isSetCIRoleCode()) {
            Object decodeXmlElement = decodeXmlElement(element.getRole().getCIRoleCode());
            if (decodeXmlElement instanceof Role) {
                responsibleParty.setRole((Role) decodeXmlElement);
            }
        }
        return responsibleParty;
    }

    private void decodeContactInfo(CIContactPropertyType cicpt, SmlResponsibleParty responsibleParty) {
        if (cicpt.isSetCIContact()) {
            decodeContact(cicpt.getCIContact(), responsibleParty);
        }
    }

    private void decodeContact(CIContactType cic, SmlResponsibleParty responsibleParty) {
        if (cic.isSetAddress()) {
            decodeCiAddress(cic.getAddress().getCIAddress(), responsibleParty);
        }
        if (cic.isSetContactInstructions()) {
            responsibleParty.setContactInstructions(cic.getContactInstructions().getCharacterString());
        }
        if (cic.isSetHoursOfService()) {
            responsibleParty.setHoursOfService(cic.getHoursOfService().getCharacterString());
        }
        if (cic.isSetOnlineResource() && cic.getOnlineResource().isSetHref()) {
            responsibleParty.setOnlineResource(Lists.newArrayList(cic.getOnlineResource().getHref()));
        }
        if (cic.isSetPhone() && cic.getPhone().isSetCITelephone()) {
            decodePhone(cic.getPhone().getCITelephone(), responsibleParty);
        }
    }

    private void decodeCiAddress(CIAddressType ciat, SmlResponsibleParty responsibleParty) {
        if (ciat.isSetAdministrativeArea()) {
            responsibleParty.setAdministrativeArea(ciat.getAdministrativeArea().getCharacterString());
        }
        if (ciat.isSetCity()) {
            responsibleParty.setCity(ciat.getCity().getCharacterString());
        }
        if (ciat.isSetCountry()) {
            responsibleParty.setCountry(ciat.getCountry().getCharacterString());
        }
        if (ciat.isSetPostalCode()) {
            responsibleParty.setPostalCode(ciat.getPostalCode().getCharacterString());
        }
    }

    private void decodeOnlineResource(CIOnlineResourceType ciort, SmlResponsibleParty responsibleParty) {
        // TODO Auto-generated method stub
    }

    private void decodePhone(CITelephoneType citt, SmlResponsibleParty responsibleParty) {
        if (CollectionHelper.isNotNullOrEmpty(citt.getVoiceArray())) {
            responsibleParty.setPhoneVoice(characterStringPropertyTypeArrayToList(citt.getVoiceArray()));
        }
        if (CollectionHelper.isNotNullOrEmpty(citt.getFacsimileArray())) {
            responsibleParty.setPhoneFax(characterStringPropertyTypeArrayToList(citt.getFacsimileArray()));
        }

    }

    private List<String> characterStringPropertyTypeArrayToList(CharacterStringPropertyType[] array) {
        List<String> values = Lists.newArrayList();
        for (CharacterStringPropertyType cspt : array) {
            values.add(cspt.getCharacterString());
        }
        return values;
    }

}
