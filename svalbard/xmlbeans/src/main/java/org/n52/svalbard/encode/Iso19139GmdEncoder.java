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

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.isotc211.x2005.gco.CharacterStringPropertyType;
import org.isotc211.x2005.gco.CodeListValueType;
import org.isotc211.x2005.gco.RealPropertyType;
import org.isotc211.x2005.gco.UnitOfMeasurePropertyType;
import org.isotc211.x2005.gmd.AbstractMDIdentificationType;
import org.isotc211.x2005.gmd.CIAddressPropertyType;
import org.isotc211.x2005.gmd.CIAddressType;
import org.isotc211.x2005.gmd.CICitationPropertyType;
import org.isotc211.x2005.gmd.CICitationType;
import org.isotc211.x2005.gmd.CIContactPropertyType;
import org.isotc211.x2005.gmd.CIContactType;
import org.isotc211.x2005.gmd.CIDateType;
import org.isotc211.x2005.gmd.CIOnlineResourceDocument;
import org.isotc211.x2005.gmd.CIOnlineResourcePropertyType;
import org.isotc211.x2005.gmd.CIOnlineResourceType;
import org.isotc211.x2005.gmd.CIResponsiblePartyDocument;
import org.isotc211.x2005.gmd.CIResponsiblePartyPropertyType;
import org.isotc211.x2005.gmd.CIResponsiblePartyType;
import org.isotc211.x2005.gmd.CIRoleCodePropertyType;
import org.isotc211.x2005.gmd.CITelephonePropertyType;
import org.isotc211.x2005.gmd.CITelephoneType;
import org.isotc211.x2005.gmd.DQConformanceResultType;
import org.isotc211.x2005.gmd.DQDomainConsistencyDocument;
import org.isotc211.x2005.gmd.DQDomainConsistencyPropertyType;
import org.isotc211.x2005.gmd.DQDomainConsistencyType;
import org.isotc211.x2005.gmd.DQQuantitativeResultType;
import org.isotc211.x2005.gmd.DQResultPropertyType;
import org.isotc211.x2005.gmd.EXExtentDocument;
import org.isotc211.x2005.gmd.EXExtentPropertyType;
import org.isotc211.x2005.gmd.EXExtentType;
import org.isotc211.x2005.gmd.EXVerticalExtentDocument;
import org.isotc211.x2005.gmd.EXVerticalExtentPropertyType;
import org.isotc211.x2005.gmd.EXVerticalExtentType;
import org.isotc211.x2005.gmd.LocalisedCharacterStringPropertyType;
import org.isotc211.x2005.gmd.LocalisedCharacterStringType;
import org.isotc211.x2005.gmd.MDDataIdentificationDocument;
import org.isotc211.x2005.gmd.MDDataIdentificationPropertyType;
import org.isotc211.x2005.gmd.MDDataIdentificationType;
import org.isotc211.x2005.gmd.MDIdentificationPropertyType;
import org.isotc211.x2005.gmd.MDMetadataDocument;
import org.isotc211.x2005.gmd.MDMetadataPropertyType;
import org.isotc211.x2005.gmd.MDMetadataType;
import org.isotc211.x2005.gmd.PTFreeTextType;
import org.isotc211.x2005.gsr.SCCRSPropertyType;
import org.n52.shetland.iso.GcoConstants;
import org.n52.shetland.iso.gco.Role;
import org.n52.shetland.iso.gmd.AbstractMDIdentification;
import org.n52.shetland.iso.gmd.CiAddress;
import org.n52.shetland.iso.gmd.CiContact;
import org.n52.shetland.iso.gmd.CiOnlineResource;
import org.n52.shetland.iso.gmd.CiResponsibleParty;
import org.n52.shetland.iso.gmd.CiTelephone;
import org.n52.shetland.iso.gmd.EXExtent;
import org.n52.shetland.iso.gmd.EXVerticalExtent;
import org.n52.shetland.iso.gmd.GmdCitation;
import org.n52.shetland.iso.gmd.GmdCitationDate;
import org.n52.shetland.iso.gmd.GmdConformanceResult;
import org.n52.shetland.iso.gmd.GmdConstants;
import org.n52.shetland.iso.gmd.GmdDateType;
import org.n52.shetland.iso.gmd.GmdDomainConsistency;
import org.n52.shetland.iso.gmd.GmdQuantitativeResult;
import org.n52.shetland.iso.gmd.GmlBaseUnit;
import org.n52.shetland.iso.gmd.LocalisedCharacterString;
import org.n52.shetland.iso.gmd.MDDataIdentification;
import org.n52.shetland.iso.gmd.MDMetadata;
import org.n52.shetland.iso.gmd.PT_FreeText;
import org.n52.shetland.iso.gmd.ScCRS;
import org.n52.shetland.ogc.SupportedType;
import org.n52.shetland.ogc.gml.GmlConstants;
import org.n52.shetland.ogc.sensorML.SmlResponsibleParty;
import org.n52.shetland.util.CollectionHelper;
import org.n52.shetland.w3c.Nillable;
import org.n52.shetland.w3c.SchemaLocation;
import org.n52.shetland.w3c.xlink.Actuate;
import org.n52.shetland.w3c.xlink.Reference;
import org.n52.shetland.w3c.xlink.Referenceable;
import org.n52.shetland.w3c.xlink.Show;
import org.n52.shetland.w3c.xlink.Type;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.exception.UnsupportedEncoderInputException;
import org.n52.svalbard.util.CodingHelper;
import org.n52.svalbard.util.XmlHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3.x1999.xlink.ActuateType;
import org.w3.x1999.xlink.ShowType;
import org.w3.x1999.xlink.TypeType;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import net.opengis.gml.x32.AbstractCRSType;
import net.opengis.gml.x32.BaseUnitType;
import net.opengis.gml.x32.CodeType;

/**
 * {@link AbstractXmlEncoder} class to decode ISO TC211 Geographic MetaData
 * (GMD) extensible markup language.
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public class Iso19139GmdEncoder
        extends AbstractIso19139GcoEncoder {

    private static final Logger LOGGER = LoggerFactory.getLogger(Iso19139GmdEncoder.class);

    private static final QName QN_GCO_DATE = new QName(GcoConstants.NS_GCO, "Date", GcoConstants.NS_GCO_PREFIX);

    private static final QName QN_GMD_CONFORMANCE_RESULT =
            new QName(GmdConstants.NS_GMD, "DQ_ConformanceResult", GmdConstants.NS_GMD_PREFIX);

    private static final QName QN_GMD_QUANTITATIVE_RESULT =
            new QName(GmdConstants.NS_GMD, "DQ_QuantitativeResult", GmdConstants.NS_GMD_PREFIX);

    private static final QName QN_GML_BASE_UNIT =
            new QName(GmlConstants.NS_GML_32, "BaseUnit", GmlConstants.NS_GML_PREFIX);

    private static final Set<EncoderKey> ENCODER_KEYS = CollectionHelper.union(
            CodingHelper.encoderKeysForElements(GmdConstants.NS_GMD, SmlResponsibleParty.class,
                    GmdQuantitativeResult.class, GmdConformanceResult.class, CiResponsibleParty.class,
                    MDMetadata.class, PT_FreeText.class, CiOnlineResource.class, EXExtent.class,
                    EXVerticalExtent.class),
            CodingHelper.encoderKeysForElements(null, GmdQuantitativeResult.class, GmdConformanceResult.class));

    public Iso19139GmdEncoder() {
        LOGGER.debug("Encoder for the following keys initialized successfully: {}!",
                Joiner.on(", ").join(ENCODER_KEYS));
    }

    @Override
    public Set<EncoderKey> getKeys() {
        return Collections.unmodifiableSet(ENCODER_KEYS);
    }

    @Override
    public Set<SupportedType> getSupportedTypes() {
        return Collections.emptySet();
    }

    @Override
    public void addNamespacePrefixToMap(final Map<String, String> nameSpacePrefixMap) {
        nameSpacePrefixMap.put(GmdConstants.NS_GMD, GmdConstants.NS_GMD_PREFIX);
    }

    @Override
    public Set<SchemaLocation> getSchemaLocations() {
        return Sets.newHashSet(GmdConstants.GMD_SCHEMA_LOCATION);
    }

    @Override
    public XmlObject encode(Object element, EncodingContext additionalValues)
            throws EncodingException, UnsupportedEncoderInputException {
        XmlObject encodedObject = null;
        // try {
        if (element instanceof SmlResponsibleParty) {
            encodedObject = encodeResponsibleParty((SmlResponsibleParty) element, additionalValues);
        } else if (element instanceof CiResponsibleParty) {
            encodedObject = encodeResponsibleParty((CiResponsibleParty) element, additionalValues);
        } else if (element instanceof MDMetadata) {
            encodedObject = encodeMDMetadata((MDMetadata) element, additionalValues);
        } else if (element instanceof MDDataIdentification) {
            encodedObject = encodeMDDataIdentification((MDDataIdentification) element, additionalValues);
        } else if (element instanceof PT_FreeText) {
            encodedObject = encodePTFreeText((PT_FreeText) element, additionalValues);
        } else if (element instanceof CiOnlineResource) {
            encodedObject = encodeCiOnlineResource((CiOnlineResource) element, additionalValues);
        } else if (element instanceof EXExtent) {
            encodedObject = encodeEXExtent((EXExtent) element, additionalValues);
        } else if (element instanceof EXVerticalExtent) {
            encodedObject = encodeEXVerticalExtent((EXVerticalExtent) element, additionalValues);
        } else {
            if (element instanceof GmdDomainConsistency) {
                encodedObject = encodeGmdDomainConsistency((GmdDomainConsistency) element, additionalValues);
            } else {
                throw new UnsupportedEncoderInputException(this, element);
            }
        }
        // } catch (final XmlException xmle) {
        // throw new NoApplicableCodeException().causedBy(xmle);
        // }
        if (encodedObject != null) {
            XmlHelper.validateDocument(encodedObject, EncodingException::new);
        }
        return encodedObject;
    }

    private XmlObject encodeMDMetadata(MDMetadata mdMetadata, EncodingContext context) throws EncodingException {
        MDMetadataType mdmt = MDMetadataType.Factory.newInstance(getXmlOptions());
        encodeAbstractObject(mdmt, mdMetadata);
        // add contacts
        for (Referenceable<CiResponsibleParty> contact : mdMetadata.getContact()) {
            if (contact.isReference()) {
                CIResponsiblePartyPropertyType cirppt =
                        CIResponsiblePartyPropertyType.Factory.newInstance(getXmlOptions());
                cirppt.setHref(contact.getReference().getHref().get().toString());
                if (contact.getReference().getTitle().isPresent()) {
                    cirppt.setTitle(contact.getReference().getTitle().get());
                }
                if (contact.getReference().getRole().isPresent()) {
                    cirppt.setRole(contact.getReference().getRole().get());
                }
                return cirppt;
            } else if (!contact.isAbsent()) {
                mdmt.addNewContact().set(encodeResponsibleParty(contact.getInstance().get(),
                        EncodingContext.of(XmlBeansEncodingFlags.PROPERTY_TYPE, true)));
            }
        }
        // add dateStamp
        mdmt.addNewDateStamp().setDateTime(mdMetadata.getDateStamp().toCalendar(Locale.ROOT));
        // add identificationInfo
        for (Referenceable<AbstractMDIdentification> identificationInfo : mdMetadata.getIdentificationInfo()) {
            if (identificationInfo.isReference()) {
                MDIdentificationPropertyType mdipt = mdmt.addNewIdentificationInfo();
                mdipt.setHref(identificationInfo.getReference().getHref().get().toString());
                if (identificationInfo.getReference().getTitle().isPresent()) {
                    mdipt.setTitle(identificationInfo.getReference().getTitle().get());
                }
                if (identificationInfo.getReference().getRole().isPresent()) {
                    mdipt.setRole(identificationInfo.getReference().getRole().get());
                }
            } else if (!identificationInfo.isAbsent()) {
                mdmt.addNewIdentificationInfo().set(encode(identificationInfo.getInstance().get(),
                        EncodingContext.of(XmlBeansEncodingFlags.PROPERTY_TYPE)));
                // TODO substitution???
            }
        }
        // TODO all other optional elements if required
        if (context.has(XmlBeansEncodingFlags.PROPERTY_TYPE)) {
            MDMetadataPropertyType mdmpt =
                    MDMetadataPropertyType.Factory.newInstance(getXmlOptions());
            mdmpt.setMDMetadata(mdmt);
            return mdmpt;
        } else if (context.has(XmlBeansEncodingFlags.DOCUMENT)) {
            MDMetadataDocument mdmd =
                    MDMetadataDocument.Factory.newInstance(getXmlOptions());
            mdmd.setMDMetadata(mdmt);
            return mdmd;
        }
        return mdmt;
    }

    private void encodeIdentificationInfo(AbstractMDIdentificationType amdit,
            AbstractMDIdentification abstractMDIdentification) {
        encodeAbstractObject(amdit, abstractMDIdentification);
        // citation
        encodeCiCitation(amdit.addNewCitation(), abstractMDIdentification.getCitation());
        // abstract
        amdit.addNewAbstract().setCharacterString(abstractMDIdentification.getAbstrakt());
        // TODO all other optional elements if required
    }

    private void encodeCiCitation(CICitationPropertyType cicpt, GmdCitation citation) {
        CICitationType cict = cicpt.addNewCICitation();
        cict.addNewTitle().setCharacterString(citation.getTitle());
        CIDateType cidt = cict.addNewDate().addNewCIDate();
        CodeListValueType clvt = cidt.addNewDateType().addNewCIDateTypeCode();
        GmdCitationDate gmdCitationDate = citation.getDate();
        GmdDateType gmdDateType = gmdCitationDate.getDateType();
        clvt.setCodeList(gmdDateType.getCodeList());
        clvt.setCodeListValue(gmdDateType.getCodeListValue());
        if (gmdDateType.getCodeSpace() != null && !gmdDateType.getCodeSpace().isEmpty()) {
            clvt.setCodeSpace(gmdDateType.getCodeSpace());
        }
        clvt.setStringValue(gmdDateType.getValue());
        XmlCursor newCursor = cidt.addNewDate().newCursor();
        newCursor.toNextToken();
        newCursor.beginElement(QN_GCO_DATE);
        newCursor.insertChars(gmdCitationDate.getDate());
        newCursor.dispose();
    }

    private XmlObject encodeMDDataIdentification(MDDataIdentification mdDataIdentification, EncodingContext context) {
        MDDataIdentificationType mddit =
                MDDataIdentificationType.Factory.newInstance(getXmlOptions());
        encodeIdentificationInfo(mddit, mdDataIdentification);
        // language
        mddit.addNewLanguage().setCharacterString(mdDataIdentification.getLanguage());
        // TODO all other optional elements if required
        if (context.has(XmlBeansEncodingFlags.PROPERTY_TYPE)) {
            MDDataIdentificationPropertyType mddipt = MDDataIdentificationPropertyType.Factory
                    .newInstance(getXmlOptions());
            mddipt.setMDDataIdentification(mddit);
            return mddipt;
        } else if (context.has(XmlBeansEncodingFlags.DOCUMENT)) {
            MDDataIdentificationDocument mddid =
                    MDDataIdentificationDocument.Factory.newInstance(getXmlOptions());
            mddid.setMDDataIdentification(mddit);
            return mddit;
        }
        return mddit;
    }

    private XmlObject encodeResponsibleParty(CiResponsibleParty responsibleParty, EncodingContext context)
            throws EncodingException {
        CIResponsiblePartyType cirpt =
                CIResponsiblePartyType.Factory.newInstance(getXmlOptions());
        if (responsibleParty.isSetIndividualName()) {
            cirpt.addNewIndividualName().setCharacterString(responsibleParty.getIndividualName());
        }
        if (responsibleParty.isSetOrganizationName()) {
            cirpt.addNewOrganisationName().setCharacterString(responsibleParty.getOrganizationName());
        }
        if (responsibleParty.isSetPositionName()) {
            cirpt.addNewPositionName().setCharacterString(responsibleParty.getPositionName());
        }
        // set contact
        if (responsibleParty.isSetContactInfo()) {
            encodeContact(cirpt.addNewContactInfo(), responsibleParty.getContactInfo());
        }
        // set role
        encodeRole(cirpt.addNewRole(), responsibleParty.getRoleNillable());
        if (responsibleParty.isSetId()) {
            cirpt.setId(responsibleParty.getId());
        }
        if (responsibleParty.isSetUuid()) {
            cirpt.setUuid(responsibleParty.getUuid());
        }
        if (context.has(XmlBeansEncodingFlags.PROPERTY_TYPE)) {
            CIResponsiblePartyPropertyType cirppt =
                    CIResponsiblePartyPropertyType.Factory.newInstance(getXmlOptions());
            cirppt.setCIResponsibleParty(cirpt);
            return cirppt;
        } else if (context.has(XmlBeansEncodingFlags.DOCUMENT)) {
            CIResponsiblePartyDocument cirpd =
                    CIResponsiblePartyDocument.Factory.newInstance(getXmlOptions());
            cirpd.setCIResponsibleParty(cirpt);
            return cirpd;
        }
        return cirpt;
    }

    private XmlObject encodeResponsibleParty(SmlResponsibleParty responsibleParty, EncodingContext additionalValues)
            throws EncodingException {
        if (responsibleParty.isSetHref()) {
            CIResponsiblePartyPropertyType cirppt =
                    CIResponsiblePartyPropertyType.Factory.newInstance(getXmlOptions());
            cirppt.setHref(responsibleParty.getHref());
            if (responsibleParty.isSetTitle()) {
                cirppt.setTitle(responsibleParty.getTitle());
            }
            if (responsibleParty.isSetRole()) {
                cirppt.setRole(responsibleParty.getRole());
            }
            return cirppt;
        }
        CIResponsiblePartyType cirpt = CIResponsiblePartyType.Factory.newInstance(getXmlOptions());
        if (responsibleParty.isSetIndividualName()) {
            cirpt.addNewIndividualName().setCharacterString(responsibleParty.getIndividualName());
        }
        if (responsibleParty.isSetOrganizationName()) {
            cirpt.addNewOrganisationName().setCharacterString(responsibleParty.getOrganizationName());
        }
        if (responsibleParty.isSetPositionName()) {
            cirpt.addNewPositionName().setCharacterString(responsibleParty.getPositionName());
        }
        // set contact
        encodeContact(cirpt.addNewContactInfo().addNewCIContact(), responsibleParty);
        // set role
        encodeRole(cirpt.addNewRole(), responsibleParty.getRoleObject());
        if (additionalValues.has(XmlBeansEncodingFlags.PROPERTY_TYPE)) {
            CIResponsiblePartyPropertyType cirppt =
                    CIResponsiblePartyPropertyType.Factory.newInstance(getXmlOptions());
            cirppt.setCIResponsibleParty(cirpt);
            return cirppt;
        } else if (additionalValues.has(XmlBeansEncodingFlags.DOCUMENT)) {
            CIResponsiblePartyDocument cirpd = CIResponsiblePartyDocument.Factory.newInstance(getXmlOptions());
            cirpd.setCIResponsibleParty(cirpt);
        }
        return cirpt;
    }

    private void encodeContact(CIContactPropertyType cicpt, Referenceable<CiContact> referenceable) {
        if (referenceable.isReference()) {
            Reference reference = referenceable.getReference();
            reference.getActuate().map(Actuate::toString).map(ActuateType.Enum::forString)
                    .ifPresent(cicpt::setActuate);
            reference.getArcrole().ifPresent(cicpt::setArcrole);
            reference.getHref().map(URI::toString).ifPresent(cicpt::setHref);
            reference.getRole().ifPresent(cicpt::setRole);
            reference.getShow().map(Show::toString).map(ShowType.Enum::forString).ifPresent(cicpt::setShow);
            reference.getTitle().ifPresent(cicpt::setTitle);
            reference.getType().map(Type::toString).map(TypeType.Enum::forString).ifPresent(cicpt::setType);
        } else {
            if (referenceable.isInstance()) {
                Nillable<CiContact> nillable = referenceable.getInstance();
                if (nillable.isPresent()) {
                    CiContact ciContact = referenceable.getInstance().get();
                    CIContactType cict = cicpt.addNewCIContact();
                    if (ciContact.getAddress() != null) {
                        encodeCiAddress(cict.addNewAddress(), ciContact.getAddress());
                    }
                    if (ciContact.getContactInstructionsNillable() != null) {
                        if (ciContact.getContactInstructionsNillable().isPresent()) {
                            cict.addNewContactInstructions().setCharacterString(ciContact.getContactInstructions());
                        } else if (ciContact.getContactInstructionsNillable().hasReason()) {
                            cict.addNewContactInstructions()
                                    .setNilReason(ciContact.getContactInstructionsNillable().getNilReason().get());
                        }
                    }
                    if (ciContact.isSetHoursOfService()) {
                        if (ciContact.getHoursOfServiceNillable().isPresent()) {
                            cict.addNewHoursOfService().setCharacterString(ciContact.getHoursOfService());
                        } else if (ciContact.getHoursOfServiceNillable().hasReason()) {
                            cict.addNewHoursOfService()
                                    .setNilReason(ciContact.getHoursOfServiceNillable().getNilReason().get());
                        }
                    }
                    if (ciContact.getOnlineResourceReferenceable() != null) {
                        encodeOnlineResource(cict.addNewOnlineResource(), ciContact.getOnlineResourceReferenceable());
                    }
                    if (ciContact.isSetPhone()) {
                        encodePhone(cict.addNewPhone(), ciContact.getPhone());
                    }
                } else {
                    if (nillable.hasReason()) {
                        cicpt.setNilReason(nillable.getNilReason().get());
                    }
                }
            }
        }
    }

    private void encodeContact(CIContactType cic, SmlResponsibleParty responsibleParty) {
        if (responsibleParty.isSetAddress()) {
            encodeCiAddress(cic.addNewAddress().addNewCIAddress(), responsibleParty);
        }
        if (responsibleParty.isSetContactInstructions()) {
            cic.addNewContactInstructions().setCharacterString(responsibleParty.getContactInstructions());
        }
        if (responsibleParty.isSetHoursOfService()) {
            cic.addNewHoursOfService().setCharacterString(responsibleParty.getHoursOfService());
        }
        if (responsibleParty.isSetOnlineResources()) {
            cic.addNewOnlineResource().setHref(responsibleParty.getOnlineResources().get(0));
        }
        if (responsibleParty.isSetPhone()) {
            encodePhone(cic.addNewPhone().addNewCITelephone(), responsibleParty);
        }

    }

    private void encodeCiAddress(CIAddressPropertyType ciapt, Referenceable<CiAddress> referenceable) {
        if (referenceable.isReference()) {
            Reference reference = referenceable.getReference();
            reference.getActuate().map(Actuate::toString).map(ActuateType.Enum::forString)
                    .ifPresent(ciapt::setActuate);
            reference.getArcrole().ifPresent(ciapt::setArcrole);
            reference.getHref().map(URI::toString).ifPresent(ciapt::setHref);
            reference.getRole().ifPresent(ciapt::setRole);
            reference.getShow().map(Show::toString).map(ShowType.Enum::forString).ifPresent(ciapt::setShow);
            reference.getTitle().ifPresent(ciapt::setTitle);
            reference.getType().map(Type::toString).map(TypeType.Enum::forString).ifPresent(ciapt::setType);
        } else {
            if (referenceable.isInstance()) {
                Nillable<CiAddress> nillable = referenceable.getInstance();
                if (nillable.isPresent()) {
                    CiAddress ciAddress = referenceable.getInstance().get();
                    CIAddressType ciat = ciapt.addNewCIAddress();
                    if (ciAddress.isSetAdministrativeArea()) {
                        ciat.addNewAdministrativeArea().setCharacterString(ciAddress.getAdministrativeArea());
                    }
                    if (ciAddress.isSetCity()) {
                        ciat.addNewCity().setCharacterString(ciAddress.getCity());
                    }
                    if (ciAddress.isSetCountry()) {
                        ciat.addNewCountry().setCharacterString(ciAddress.getCountry());
                    }
                    if (ciAddress.isSetPostalCode()) {
                        ciat.addNewPostalCode().setCharacterString(ciAddress.getPostalCode());
                    }
                    if (ciAddress.hasDeliveryPoints()) {
                        ciat.setDeliveryPointArray(
                                listToCharacterStringPropertyTypeArray(ciAddress.getDeliveryPoints()));

                    }
                    if (ciAddress.hasElectronicMailAddresses()) {
                        ciat.setElectronicMailAddressArray(listToCharacterStringPropertyTypeArray(
                                Lists.newArrayList(ciAddress.getElectronicMailAddresses())));
                    }
                    if (ciAddress.isSetId()) {
                        ciat.setId(ciAddress.getId());
                    }
                    if (ciAddress.isSetUuid()) {
                        ciat.setUuid(ciAddress.getUuid());
                    }
                } else {
                    if (nillable.hasReason()) {
                        ciapt.setNilReason(nillable.getNilReason().get());
                    }
                }
            }
        }
    }

    private void encodeCiAddress(CIAddressType ciat, SmlResponsibleParty responsibleParty) {
        if (responsibleParty.isSetAdministrativeArea()) {
            ciat.addNewAdministrativeArea().setCharacterString(responsibleParty.getAdministrativeArea());
        }
        if (responsibleParty.isSetCity()) {
            ciat.addNewCity().setCharacterString(responsibleParty.getCity());
        }
        if (responsibleParty.isSetCountry()) {
            ciat.addNewCountry().setCharacterString(responsibleParty.getCountry());
        }
        if (responsibleParty.isSetPostalCode()) {
            ciat.addNewPostalCode().setCharacterString(responsibleParty.getPostalCode());
        }
        if (responsibleParty.isSetDeliveryPoint()) {
            ciat.setDeliveryPointArray(listToCharacterStringPropertyTypeArray(responsibleParty.getDeliveryPoint()));

        }
        if (responsibleParty.isSetEmail()) {
            ciat.setElectronicMailAddressArray(
                    listToCharacterStringPropertyTypeArray(Lists.newArrayList(responsibleParty.getEmail())));
        }
    }

    private void encodePhone(CITelephonePropertyType citpt, Referenceable<CiTelephone> referenceable) {
        if (referenceable.isReference()) {
            Reference reference = referenceable.getReference();
            reference.getActuate().map(Actuate::toString).map(ActuateType.Enum::forString)
                    .ifPresent(citpt::setActuate);
            reference.getArcrole().ifPresent(citpt::setArcrole);
            reference.getHref().map(URI::toString).ifPresent(citpt::setHref);
            reference.getRole().ifPresent(citpt::setRole);
            reference.getShow().map(Show::toString).map(ShowType.Enum::forString).ifPresent(citpt::setShow);
            reference.getTitle().ifPresent(citpt::setTitle);
            reference.getType().map(Type::toString).map(TypeType.Enum::forString).ifPresent(citpt::setType);
        } else {
            if (referenceable.isInstance()) {
                Nillable<CiTelephone> nillable = referenceable.getInstance();
                if (nillable.isPresent()) {
                    CiTelephone ciTelephone = referenceable.getInstance().get();
                    CITelephoneType citt = citpt.addNewCITelephone();
                    if (ciTelephone.isSetVoice()) {
                        citt.setVoiceArray(listToCharacterStringPropertyTypeArray(ciTelephone.getVoice()));
                    }
                    if (ciTelephone.isSetFacsimile()) {
                        citt.setFacsimileArray(listToCharacterStringPropertyTypeArray(ciTelephone.getFacsimile()));
                    }
                    if (ciTelephone.isSetId()) {
                        citt.setId(ciTelephone.getId());
                    }
                    if (ciTelephone.isSetUuid()) {
                        citt.setUuid(ciTelephone.getUuid());
                    }
                } else {
                    if (nillable.hasReason()) {
                        citpt.setNilReason(nillable.getNilReason().get());
                    }
                }
            }
        }
    }

    private void encodePhone(CITelephoneType citt, SmlResponsibleParty responsibleParty) {
        if (responsibleParty.isSetPhoneVoice()) {
            citt.setVoiceArray(listToCharacterStringPropertyTypeArray(responsibleParty.getPhoneVoice()));
        }
        if (responsibleParty.isSetPhoneFax()) {
            citt.setFacsimileArray(listToCharacterStringPropertyTypeArray(responsibleParty.getPhoneFax()));
        }
    }

    private void encodeRole(CIRoleCodePropertyType circpt, Nillable<Role> nillable) throws EncodingException {
        if (nillable.isPresent()) {
            XmlObject encodeObjectToXml = encodeObjectToXml(GcoConstants.NS_GCO, nillable.get());
            if (encodeObjectToXml != null) {
                circpt.addNewCIRoleCode().set(encodeObjectToXml);
            }
        } else {
            if (nillable.hasReason()) {
                circpt.setNilReason(nillable.getNilReason().get());
            }
        }
    }

    private void encodeRole(CIRoleCodePropertyType circpt, org.n52.shetland.ogc.sensorML.Role role)
            throws EncodingException {
        XmlObject encodeObjectToXml = encodeObjectToXml(GcoConstants.NS_GCO, role);
        if (encodeObjectToXml != null) {
            circpt.addNewCIRoleCode().set(encodeObjectToXml);
        }
    }

    private XmlObject encodeGmdDomainConsistency(GmdDomainConsistency element, EncodingContext additionalValues)
            throws EncodingException {
        if (additionalValues.has(XmlBeansEncodingFlags.DOCUMENT)) {
            DQDomainConsistencyDocument document = DQDomainConsistencyDocument.Factory.newInstance(getXmlOptions());
            DQResultPropertyType addNewResult = document.addNewDQDomainConsistency().addNewResult();
            encodeGmdDomainConsistency(addNewResult, element);
            return document;
        } else if (additionalValues.has(XmlBeansEncodingFlags.PROPERTY_TYPE)) {
            DQDomainConsistencyPropertyType propertyType =
                    DQDomainConsistencyPropertyType.Factory.newInstance(getXmlOptions());
            DQResultPropertyType addNewResult = propertyType.addNewDQDomainConsistency().addNewResult();
            encodeGmdDomainConsistency(addNewResult, element);
            return propertyType;
        } else {
            DQDomainConsistencyType type = DQDomainConsistencyType.Factory.newInstance(getXmlOptions());
            DQResultPropertyType addNewResult = type.addNewResult();
            encodeGmdDomainConsistency(addNewResult, element);
            return type;
        }
    }

    private void encodeGmdDomainConsistency(DQResultPropertyType xbResult, GmdDomainConsistency domainConsistency)
            throws EncodingException {
        if (domainConsistency instanceof GmdConformanceResult) {
            encodeGmdConformanceResult(xbResult, (GmdConformanceResult) domainConsistency);
        } else if (domainConsistency instanceof GmdQuantitativeResult) {
            encodeGmdQuantitativeResult(xbResult, (GmdQuantitativeResult) domainConsistency);
        } else {
            throw new UnsupportedEncoderInputException(this, domainConsistency);
        }
    }

    private void encodeGmdConformanceResult(DQResultPropertyType xbResult, GmdConformanceResult gmdConformanceResult) {
        DQConformanceResultType dqConformanceResultType = (DQConformanceResultType) xbResult.addNewAbstractDQResult()
                .substitute(QN_GMD_CONFORMANCE_RESULT, DQConformanceResultType.type);
        if (gmdConformanceResult.isSetPassNilReason()) {
            dqConformanceResultType.addNewPass().setNilReason(gmdConformanceResult.getPassNilReason().name());
        } else {
            dqConformanceResultType.addNewPass().setBoolean(gmdConformanceResult.isPass());
        }
        dqConformanceResultType.addNewExplanation()
                .setCharacterString(gmdConformanceResult.getSpecification().getExplanation());
        encodeCiCitation(dqConformanceResultType.addNewSpecification(),
                gmdConformanceResult.getSpecification().getCitation());
    }

    private void encodeGmdQuantitativeResult(DQResultPropertyType xbResult,
            GmdQuantitativeResult gmdQuantitativeResult) {
        DQQuantitativeResultType dqQuantitativeResultType = (DQQuantitativeResultType) xbResult
                .addNewAbstractDQResult().substitute(QN_GMD_QUANTITATIVE_RESULT, DQQuantitativeResultType.type);
        GmlBaseUnit unit = gmdQuantitativeResult.getUnit();
        UnitOfMeasurePropertyType valueUnit = dqQuantitativeResultType.addNewValueUnit();
        BaseUnitType xbBaseUnit =
                (BaseUnitType) valueUnit.addNewUnitDefinition().substitute(QN_GML_BASE_UNIT, BaseUnitType.type);
        CodeType xbCatalogSymbol = xbBaseUnit.addNewCatalogSymbol();
        xbCatalogSymbol.setCodeSpace(unit.getCatalogSymbol().getCodeSpace().toString());
        xbCatalogSymbol.setStringValue(unit.getCatalogSymbol().getValue());
        xbBaseUnit.setId(unit.getId());
        xbBaseUnit.addNewUnitsSystem().setHref(unit.getUnitSystem());
        xbBaseUnit.addNewIdentifier().setCodeSpace(unit.getIdentifier());
        if (gmdQuantitativeResult.isSetValueNilReason()) {
            dqQuantitativeResultType.addNewValue().setNilReason(gmdQuantitativeResult.getValueNilReason().name());
        } else {
            XmlCursor cursor = dqQuantitativeResultType.addNewValue().addNewRecord().newCursor();
            cursor.toNextToken();
            cursor.insertChars(gmdQuantitativeResult.getValue());
            cursor.dispose();
        }
    }

    private PTFreeTextType encodePTFreeText(PT_FreeText element, EncodingContext context) {
        PTFreeTextType ptftt = PTFreeTextType.Factory.newInstance();
        for (LocalisedCharacterString localisedCharacterString : element.getTextGroup()) {
            ptftt.addNewTextGroup().set(encodeLocalisedCharacterStringPropertyType(localisedCharacterString));
        }
        return ptftt;
    }

    private LocalisedCharacterStringPropertyType encodeLocalisedCharacterStringPropertyType(
            LocalisedCharacterString localisedCharacterString) {
        LocalisedCharacterStringPropertyType lcspt = LocalisedCharacterStringPropertyType.Factory.newInstance();
        lcspt.setLocalisedCharacterString(encodeLocalisedCharacterStringType(localisedCharacterString));
        return lcspt;
    }

    private LocalisedCharacterStringType encodeLocalisedCharacterStringType(
            LocalisedCharacterString localisedCharacterString) {
        LocalisedCharacterStringType lcst = LocalisedCharacterStringType.Factory.newInstance();
        lcst.setStringValue(localisedCharacterString.getValue());
        if (localisedCharacterString.isSetLocale()) {
            lcst.setLocale(localisedCharacterString.getLocale());
        }
        return lcst;
    }

    private CharacterStringPropertyType[] listToCharacterStringPropertyTypeArray(List<String> list) {
        return list.stream().map(string -> {
            CharacterStringPropertyType cspt = CharacterStringPropertyType.Factory.newInstance();
            cspt.setCharacterString(string);
            return cspt;
        }).toArray(CharacterStringPropertyType[]::new);
    }

    private void encodeOnlineResource(CIOnlineResourcePropertyType ciorpt,
            Referenceable<CiOnlineResource> referenceable) {
        if (referenceable.isReference()) {
            Reference reference = referenceable.getReference();
            reference.getActuate().map(Actuate::toString).map(ActuateType.Enum::forString)
                    .ifPresent(ciorpt::setActuate);
            reference.getArcrole().ifPresent(ciorpt::setArcrole);
            reference.getHref().map(URI::toString).ifPresent(ciorpt::setHref);
            reference.getRole().ifPresent(ciorpt::setRole);
            reference.getShow().map(Show::toString).map(ShowType.Enum::forString).ifPresent(ciorpt::setShow);
            reference.getTitle().ifPresent(ciorpt::setTitle);
            reference.getType().map(Type::toString).map(TypeType.Enum::forString).ifPresent(ciorpt::setType);
        } else {
            if (referenceable.isInstance()) {
                Nillable<CiOnlineResource> nillable = referenceable.getInstance();
                if (nillable.isPresent()) {
                    CIOnlineResourceType ciort = ciorpt.addNewCIOnlineResource();
                    encodeOnlineResource(ciort, referenceable.getInstance().get());
                } else {
                    if (nillable.hasReason()) {
                        ciorpt.setNilReason(nillable.getNilReason().get());
                    }
                }
            }
        }

    }

    private void encodeOnlineResource(CIOnlineResourceType ciort, CiOnlineResource onlineResource) {
        // linkage
        if (onlineResource.getLinkage().isPresent()) {
            ciort.addNewLinkage().setURL(onlineResource.getLinkage().get().toString());
        } else {
            if (onlineResource.getLinkage().hasReason()) {
                ciort.addNewLinkage().setNilReason(onlineResource.getLinkage().getNilReason().get());
            }
        }
        // protocol
        if (onlineResource.isSetProtocol()) {
            if (onlineResource.getProtocol().isPresent()) {
                ciort.addNewProtocol().setCharacterString(onlineResource.getProtocol().get());
            } else {
                if (onlineResource.getProtocol().getNilReason().isPresent()) {
                    ciort.addNewProtocol().setNilReason(onlineResource.getProtocol().getNilReason().get());
                }
            }
        }
        if (onlineResource.isSetApplicationProfile()) {
            ciort.addNewApplicationProfile().setCharacterString(onlineResource.getApplicationProfile());
        }
        if (onlineResource.isSetDescription()) {
            ciort.addNewDescription().setCharacterString(onlineResource.getDescription());
        }
        if (onlineResource.isSetName()) {
            ciort.addNewName().setCharacterString(onlineResource.getName());
        }
        if (onlineResource.isSetFunction()) {
            ciort.addNewFunction().addNewCIOnLineFunctionCode().setStringValue(onlineResource.getFunction());
        }
        if (onlineResource.isSetId()) {
            ciort.setId(onlineResource.getId());
        }
        if (onlineResource.isSetUuid()) {
            ciort.setUuid(onlineResource.getUuid());
        }
    }

    private XmlObject encodeCiOnlineResource(CiOnlineResource element, EncodingContext context) {
        CIOnlineResourceType ciort = CIOnlineResourceType.Factory.newInstance(getXmlOptions());
        encodeOnlineResource(ciort, element);
        if (context.has(XmlBeansEncodingFlags.PROPERTY_TYPE)) {
            CIOnlineResourcePropertyType ciorpt = CIOnlineResourcePropertyType.Factory.newInstance(getXmlOptions());
            ciorpt.setCIOnlineResource(ciort);
            return ciorpt;
        } else if (context.has(XmlBeansEncodingFlags.DOCUMENT)) {
            CIOnlineResourceDocument ciord = CIOnlineResourceDocument.Factory.newInstance(getXmlOptions());
            ciord.setCIOnlineResource(ciort);
            return ciord;
        }
        return ciort;
    }

    private XmlObject encodeEXExtent(EXExtent exExtent, EncodingContext context) throws EncodingException {
        EXExtentType exet = EXExtentType.Factory.newInstance();
        if (exExtent.hasDescription()) {
            exet.addNewDescription().setCharacterString(exExtent.getDescription());
        }
        if (exExtent.hasVerticalExtent()) {
            for (Referenceable<EXVerticalExtent> verticalExtent : exExtent.getExVerticalExtent()) {
                EXVerticalExtentPropertyType exvept = exet.addNewVerticalElement();
                if (verticalExtent.isReference()) {
                    Reference reference = verticalExtent.getReference();
                    reference.getActuate().map(Actuate::toString).map(ActuateType.Enum::forString)
                            .ifPresent(exvept::setActuate);
                    reference.getArcrole().ifPresent(exvept::setArcrole);
                    reference.getHref().map(URI::toString).ifPresent(exvept::setHref);
                    reference.getRole().ifPresent(exvept::setRole);
                    reference.getShow().map(Show::toString).map(ShowType.Enum::forString).ifPresent(exvept::setShow);
                    reference.getTitle().ifPresent(exvept::setTitle);
                    reference.getType().map(Type::toString).map(TypeType.Enum::forString).ifPresent(exvept::setType);
                } else {
                    if (verticalExtent.isInstance()) {
                        Nillable<EXVerticalExtent> nillable = verticalExtent.getInstance();
                        if (nillable.isPresent()) {
                            XmlObject xml = encodeEXVerticalExtent(nillable.get(), EncodingContext.empty());
                            if (xml != null && xml instanceof EXVerticalExtentType) {
                                exvept.setEXVerticalExtent((EXVerticalExtentType) xml);
                            } else {
                                exvept.setNil();
                                exvept.setNilReason(Nillable.missing().get());
                            }
                        } else {
                            exvept.setNil();
                            if (nillable.hasReason()) {
                                exvept.setNilReason(nillable.getNilReason().get());
                            } else {
                                exvept.setNilReason(Nillable.missing().get());
                            }
                        }
                    }
                }
            }
        }
        if (context.has(XmlBeansEncodingFlags.PROPERTY_TYPE)) {
            EXExtentPropertyType exept = EXExtentPropertyType.Factory.newInstance(getXmlOptions());
            exept.setEXExtent(exet);
            return exept;
        } else if (context.has(XmlBeansEncodingFlags.DOCUMENT)) {
            EXExtentDocument exed = EXExtentDocument.Factory.newInstance(getXmlOptions());
            exed.setEXExtent(exet);
            return exed;
        }
        return exet;
    }

    private XmlObject encodeEXVerticalExtent(EXVerticalExtent exVerticalExtent, EncodingContext context)
            throws EncodingException {
        EXVerticalExtentType exvet = EXVerticalExtentType.Factory.newInstance();
        if (exVerticalExtent.isSetId()) {
            exvet.setId(exVerticalExtent.getId());
        }
        if (exVerticalExtent.isSetUuid()) {
            exvet.setUuid(exVerticalExtent.getUuid());
        }
        // min value
        Nillable<Double> minNillable = exVerticalExtent.getMinimumValue();
        RealPropertyType rptMinValue = exvet.addNewMinimumValue();
        if (minNillable.isPresent()) {
            rptMinValue.setReal(minNillable.get());
        } else {
            rptMinValue.setNil();
            if (minNillable.hasReason()) {
                rptMinValue.setNilReason(minNillable.getNilReason().get());
            } else {
                rptMinValue.setNilReason(Nillable.missing().get());
            }
        }
        // max value
        Nillable<Double> maxNillable = exVerticalExtent.getMaximumValue();
        RealPropertyType rptMinMaxValue = exvet.addNewMaximumValue();
        if (maxNillable.isPresent()) {
            rptMinMaxValue.setReal(maxNillable.get());
        } else {
            rptMinMaxValue.setNil();
            if (maxNillable.hasReason()) {
                rptMinMaxValue.setNilReason(maxNillable.getNilReason().get());
            } else {
                rptMinMaxValue.setNilReason(Nillable.missing().get());
            }
        }
        // verticalCRS
        SCCRSPropertyType sccrspt = exvet.addNewVerticalCRS();
        Referenceable<ScCRS> verticalCRS = exVerticalExtent.getVerticalCRS();
        if (verticalCRS.isReference()) {
            Reference reference = verticalCRS.getReference();
            reference.getActuate().map(Actuate::toString).map(ActuateType.Enum::forString)
                    .ifPresent(sccrspt::setActuate);
            reference.getArcrole().ifPresent(sccrspt::setArcrole);
            reference.getHref().map(URI::toString).ifPresent(sccrspt::setHref);
            reference.getRole().ifPresent(sccrspt::setRole);
            reference.getShow().map(Show::toString).map(ShowType.Enum::forString).ifPresent(sccrspt::setShow);
            reference.getTitle().ifPresent(sccrspt::setTitle);
            reference.getType().map(Type::toString).map(TypeType.Enum::forString).ifPresent(sccrspt::setType);
        } else {
            if (verticalCRS.isInstance()) {
                Nillable<ScCRS> nillable = verticalCRS.getInstance();
                if (nillable.isPresent()) {
                    XmlObject xml = encodeObjectToXml(GmlConstants.NS_GML_32, nillable.get().getAbstractCrs());
                    if (xml != null && xml instanceof AbstractCRSType) {
                        final XmlObject substituteElement =
                                XmlHelper.substituteElement(sccrspt.addNewAbstractCRS(), xml);
                        substituteElement.set(xml);
                    } else {
                        sccrspt.setNil();
                        sccrspt.setNilReason(Nillable.missing().get());
                    }
                } else {
                    sccrspt.setNil();
                    if (nillable.hasReason()) {
                        sccrspt.setNilReason(nillable.getNilReason().get());
                    } else {
                        sccrspt.setNilReason(Nillable.missing().get());
                    }
                }
            }
        }
        if (context.has(XmlBeansEncodingFlags.PROPERTY_TYPE)) {
            EXVerticalExtentPropertyType exvept = EXVerticalExtentPropertyType.Factory.newInstance(getXmlOptions());
            exvept.setEXVerticalExtent(exvet);
            return exvept;
        } else if (context.has(XmlBeansEncodingFlags.DOCUMENT)) {
            EXVerticalExtentDocument exved = EXVerticalExtentDocument.Factory.newInstance(getXmlOptions());
            exved.setEXVerticalExtent(exvet);
            return exved;
        }
        return exvet;
    }

}
