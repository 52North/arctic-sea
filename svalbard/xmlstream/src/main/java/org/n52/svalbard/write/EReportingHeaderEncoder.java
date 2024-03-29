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
package org.n52.svalbard.write;

import java.io.OutputStream;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.joda.time.DateTime;

import org.n52.janmayen.NcName;
import org.n52.shetland.aqd.AqdConstants;
import org.n52.shetland.aqd.EReportingChange;
import org.n52.shetland.aqd.EReportingHeader;
import org.n52.shetland.inspire.GeographicalName;
import org.n52.shetland.inspire.Pronunciation;
import org.n52.shetland.inspire.Spelling;
import org.n52.shetland.inspire.ad.AddressRepresentation;
import org.n52.shetland.inspire.base.Identifier;
import org.n52.shetland.inspire.base2.Contact;
import org.n52.shetland.inspire.base2.RelatedParty;
import org.n52.shetland.iso.GcoConstants;
import org.n52.shetland.iso.gmd.LocalisedCharacterString;
import org.n52.shetland.iso.gmd.PT_FreeText;
import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.gml.CodeType;
import org.n52.shetland.ogc.gml.GmlConstants;
import org.n52.shetland.ogc.gml.time.Time;
import org.n52.shetland.ogc.gml.time.Time.TimeFormat;
import org.n52.shetland.ogc.gml.time.TimeInstant;
import org.n52.shetland.ogc.gml.time.TimePeriod;
import org.n52.shetland.util.DateTimeHelper;
import org.n52.shetland.util.IdGenerator;
import org.n52.shetland.w3c.Nillable;
import org.n52.shetland.w3c.W3CConstants;
import org.n52.shetland.w3c.xlink.Reference;
import org.n52.shetland.w3c.xlink.Referenceable;
import org.n52.svalbard.encode.EncodingContext;
import org.n52.svalbard.encode.exception.EncodingException;

public class EReportingHeaderEncoder
        extends XmlStreamWriter<EReportingHeader> {
    public EReportingHeaderEncoder(EncodingContext context, OutputStream outputStream, EReportingHeader element)
            throws XMLStreamException {
        super(context, outputStream, element);
    }

    @Override
    public void write() throws XMLStreamException, EncodingException {
        EReportingHeader h = getElement();
        start(AqdConstants.QN_AQD_REPORTING_HEADER);
        namespace(AqdConstants.NS_AD_PREFIX, AqdConstants.NS_AD);
        namespace(AqdConstants.NS_AQD_PREFIX, AqdConstants.NS_AQD);
        namespace(AqdConstants.NS_BASE_PREFIX, AqdConstants.NS_BASE);
        namespace(AqdConstants.NS_BASE2_PREFIX, AqdConstants.NS_BASE2);
        namespace(AqdConstants.NS_GN_PREFIX, AqdConstants.NS_GN);
        namespace(GmlConstants.NS_GML_PREFIX, GmlConstants.NS_GML_32);
        namespace(W3CConstants.NS_XLINK_PREFIX, W3CConstants.NS_XLINK);
        namespace(W3CConstants.NS_XSI_PREFIX, W3CConstants.NS_XSI);
        namespace(GcoConstants.NS_GCO_PREFIX, GcoConstants.NS_GCO);
        if (isAddSchemaLocation()) {
            schemaLocation(Collections.singleton(AqdConstants.NS_AQD_SCHEMA_LOCATION));
        }

        attr(GmlConstants.QN_ID_32, getGMLId(h));

        encodeChange(h.getChange());
        encodeInpireID(h.getInspireID());
        encodeReportingAuthority(h.getReportingAuthority());
        encodeReportingPeriod(h.getReportingPeriod());
        if (h.isSetContent()) {
            encodeContent(h.getContent());
        }
        end(AqdConstants.QN_AQD_REPORTING_HEADER);
    }

    protected String getGMLId(Object h) {
        String gmlId = IdGenerator.generate(h.toString() + System.currentTimeMillis());
        return NcName.makeValid(gmlId);
    }

    private void encodeReportingPeriod(Referenceable<? extends Time> v) throws XMLStreamException, EncodingException {
        if (!v.isAbsent()) {
            if (v.isReference()) {
                empty(AqdConstants.QN_AQD_REPORTING_PERIOD);
                encodeReferenceAttr(v.getReference());
            } else {
                if (v.getInstance().isNil()) {
                    empty(AqdConstants.QN_AQD_REPORTING_PERIOD);
                    encodeNilAttr(v.getInstance().getNilReason());
                } else {
                    start(AqdConstants.QN_AQD_REPORTING_PERIOD);
                    encodeTime(v.getInstance().get());
                    end(AqdConstants.QN_AQD_REPORTING_PERIOD);
                }
            }
        }
    }

    private void encodeReportingAuthority(RelatedParty v) throws XMLStreamException {
        start(AqdConstants.QN_AQD_REPORTING_AUTHORITY);
        encodeRelatedParty(v);
        end(AqdConstants.QN_AQD_REPORTING_AUTHORITY);
    }

    private void encodeInpireID(Identifier v) throws XMLStreamException {
        start(AqdConstants.QN_AQD_INSPIRE_ID);
        start(AqdConstants.QN_BASE_IDENTIFIER);
        encodeString(AqdConstants.QN_BASE_LOCAL_ID, v.getLocalId());
        encodeString(AqdConstants.QN_BASE_NAMESPACE, v.getNamespace());
        encodeNillableString(AqdConstants.QN_BASE_VERSION_ID, v.getVersionId());
        end(AqdConstants.QN_BASE_IDENTIFIER);
        end(AqdConstants.QN_AQD_INSPIRE_ID);
    }

    private void encodeChange(EReportingChange v) throws XMLStreamException {
        start(AqdConstants.QN_AQD_CHANGE);
        chars(Boolean.toString(v.isChange()));
        endInline(AqdConstants.QN_AQD_CHANGE);
        if (v.getDescription().isPresent()) {
            start(AqdConstants.QN_AQD_CHANGE_DESCRIPTION);
            chars(v.getDescription().get());
            endInline(AqdConstants.QN_AQD_CHANGE_DESCRIPTION);
        }
    }

    private void encodeRelatedParty(RelatedParty v) throws XMLStreamException {
        start(AqdConstants.QN_BASE2_RELATED_PARTY);

        encodeNillableFreeText(AqdConstants.QN_BASE2_INDIVIDUAL_NAME, v.getIndividualName());
        encodeNillableFreeText(AqdConstants.QN_BASE2_ORGANISATION_NAME, v.getOrganisationName());
        encodeNillableFreeText(AqdConstants.QN_BASE2_POSITION_NAME, v.getPositionName());
        encodeContact(v.getContact());
        for (Nillable<Reference> role : v.getRoles()) {
            encodeNillableReference(AqdConstants.QN_BASE2_ROLE, role);
        }
        end(AqdConstants.QN_BASE2_RELATED_PARTY);
    }

    private void encodeContent(List<Referenceable<AbstractFeature>> content) throws XMLStreamException {
        for (Referenceable<AbstractFeature> v : content) {
            if (v.isReference()) {
                empty(AqdConstants.QN_AQD_CONTENT);
                encodeReferenceAttr(v.getReference());
            } else {
                empty(AqdConstants.QN_AQD_CONTENT);
                attr(W3CConstants.QN_XLINK_HREF, v.getInstance().get().getIdentifier());
            }
        }
    }

    private void encodeReferenceAttr(Reference v) throws XMLStreamException {
        attr(W3CConstants.QN_XLINK_HREF, v.getHref());
        attr(W3CConstants.QN_XLINK_ACTUATE, v.getActuate());
        attr(W3CConstants.QN_XLINK_ARCROLE, v.getArcrole());
        attr(W3CConstants.QN_XLINK_ROLE, v.getRole());
        attr(W3CConstants.QN_XLINK_SHOW, v.getShow());
        attr(W3CConstants.QN_XLINK_TITLE, v.getTitle());
        attr(W3CConstants.QN_XLINK_TYPE, v.getType());
        attr(GmlConstants.QN_REMOTE_SCHEMA, v.getRemoteSchema());
    }

    protected void attr(QName qn, Optional<?> v) throws XMLStreamException {
        if (v.isPresent()) {
            attr(qn, v.get().toString());
        }
    }

    protected void encodeNillableString(QName qn, Nillable<?> v) throws XMLStreamException {
        if (!v.isAbsent()) {
            if (v.isNil()) {
                empty(qn);
                encodeNilAttr(v.getNilReason());
            } else {
                start(qn);
                chars(v.get().toString());
                endInline(qn);
            }
        }
    }

    protected void encodeString(QName qn, String v) throws XMLStreamException {
        start(qn);
        chars(v);
        endInline(qn);
    }

    private void encodeNilAttr(Optional<String> reason) throws XMLStreamException {
        attr(W3CConstants.QN_XSI_NIL, Boolean.toString(true));
        if (reason.isPresent()) {
            attr(AqdConstants.AN_NIL_REASON, reason.get());
        }
    }

    private void encodeGCONilAttr(Nillable<?> v) throws XMLStreamException {
        if (v.isNil()) {
            attr(W3CConstants.QN_XSI_NIL, Boolean.toString(true));
            if (v.getNilReason().isPresent()) {
                attr(GcoConstants.QN_GCO_NIL_REASON, v.getNilReason().get());
            }
        }
    }

    protected void encodeNillableFreeText(QName qn, Nillable<PT_FreeText> nillable) throws XMLStreamException {
        if (!nillable.isAbsent()) {
            if (nillable.isNil()) {
                empty(qn);
                encodeGCONilAttr(nillable);
            } else {
                for (LocalisedCharacterString lcs : nillable.get().getTextGroup()) {
                    start(qn);
                    start(GcoConstants.QN_GCO_CHARACTER_STRING);
                    chars(lcs.getValue());
                    endInline(GcoConstants.QN_GCO_CHARACTER_STRING);
                    end(qn);
                }
            }
        }
    }

    private void encodeContact(Nillable<Contact> v) throws XMLStreamException {
        if (!v.isAbsent()) {
            if (v.isNil()) {
                empty(AqdConstants.QN_BASE2_CONTACT);
                encodeNilAttr(v.getNilReason());
            } else {
                start(AqdConstants.QN_BASE2_CONTACT);
                encodeContact(v.get());
                end(AqdConstants.QN_BASE2_CONTACT);
            }
        }
    }

    protected void encodeContact(Contact c) throws XMLStreamException {
        start(AqdConstants.QN_BASE2_C_ONTACT);
        encodeAddress(c.getAddress());
        encodeNillableFreeText(AqdConstants.QN_BASE2_CONTACT_INSTRUCTIONS, c.getContactInstructions());
        encodeNillableString(AqdConstants.QN_BASE2_ELECTRONIC_MAIL_ADDRESS, c.getElectronicMailAddress());
        encodeNillableFreeText(AqdConstants.QN_BASE2_HOURS_OF_SERVICE, c.getHoursOfService());
        if (c.getTelephoneFacsimile().isPresent()) {
            for (Nillable<String> value : c.getTelephoneFacsimile().get()) {
                encodeNillableString(AqdConstants.QN_BASE2_TELEPHONE_FACSIMILE, value);
            }
        }
        if (c.getTelephoneVoice().isPresent()) {
            for (Nillable<String> value : c.getTelephoneVoice().get()) {
                encodeNillableString(AqdConstants.QN_BASE2_TELEPHONE_VOICE, value);
            }
        }
        encodeNillableString(AqdConstants.QN_BASE2_WEBSITE, c.getWebsite());
        end(AqdConstants.QN_BASE2_C_ONTACT);
    }

    private void encodeAddress(Nillable<AddressRepresentation> v) throws XMLStreamException {
        if (!v.isAbsent()) {
            if (v.isNil()) {
                empty(AqdConstants.QN_BASE2_ADDRESS);
                encodeNilAttr(v.getNilReason());
            } else {
                start(AqdConstants.QN_BASE2_ADDRESS);
                encodeAddress(v.get());
                end(AqdConstants.QN_BASE2_ADDRESS);
            }
        }
    }

    private void encodeAddress(AddressRepresentation v) throws XMLStreamException {
        start(AqdConstants.QN_AD_ADDRESS_REPRESENTATION);

        for (GeographicalName value : v.getAdminUnits()) {
            start(AqdConstants.QN_AD_ADMIN_UNIT);
            encodeGeographicalName(value);
            end(AqdConstants.QN_AD_ADMIN_UNIT);
        }
        for (String value : v.getLocatorDesignators()) {
            start(AqdConstants.QN_AD_LOCATOR_DESIGNATOR);
            chars(value);
            endInline(AqdConstants.QN_AD_LOCATOR_DESIGNATOR);
        }
        for (GeographicalName value : v.getLocatorNames()) {
            start(AqdConstants.QN_AD_LOCATOR_NAME);
            encodeGeographicalName(value);
            end(AqdConstants.QN_AD_LOCATOR_NAME);
        }
        for (Nillable<GeographicalName> value : v.getAddressAreas()) {
            encodeNillableGeographicalName(AqdConstants.QN_AD_ADDRESS_AREA, value);
        }
        for (Nillable<GeographicalName> value : v.getPostNames()) {
            encodeNillableGeographicalName(AqdConstants.QN_AD_POST_NAME, value);
        }
        encodeNillableString(AqdConstants.QN_AD_POST_CODE, v.getPostCode());
        for (Nillable<GeographicalName> value : v.getThoroughfares()) {
            encodeNillableGeographicalName(AqdConstants.QN_AD_THOROUGHFARE, value);
        }
        encodeNillableReference(AqdConstants.QN_AD_ADDRESS_FEATURE, v.getAddressFeature());

        end(AqdConstants.QN_AD_ADDRESS_REPRESENTATION);
    }

    protected void encodeNillableGeographicalName(QName qn, Nillable<GeographicalName> v) throws XMLStreamException {
        if (!v.isAbsent()) {
            if (v.isNil()) {
                empty(qn);
                encodeNilAttr(v.getNilReason());
            } else {
                start(qn);
                encodeGeographicalName(v.get());
                end(qn);
            }
        }
    }

    protected void encodeNillableReference(QName qn, Nillable<Reference> v) throws XMLStreamException {
        if (!v.isAbsent()) {
            empty(qn);
            if (v.isNil()) {
                encodeNilAttr(v.getNilReason());
            } else {
                encodeReferenceAttr(v.get());
            }
        }
    }

    private void encodeGeographicalName(GeographicalName v) throws XMLStreamException {
        start(AqdConstants.QN_GN_GEOGRAPHICAL_NAME);
        encodeNillableString(AqdConstants.QN_GN_LANGUAGE, v.getLanguage());
        encodeNillableCodeType(AqdConstants.QN_GN_NATIVENESS, v.getNativeness());
        encodeNillableCodeType(AqdConstants.QN_GN_NAME_STATUS, v.getNameStatus());
        encodeNillableString(AqdConstants.QN_GN_SOURCE_OF_NAME, v.getSourceOfName());
        encodeNillablePronunciation(AqdConstants.QN_GN_PRONUNCIATION, v.getPronunciation());
        for (Spelling value : v.getSpelling()) {
            start(AqdConstants.QN_GN_SPELLING);
            encodeSpellingOfName(value);
            end(AqdConstants.QN_GN_SPELLING);
        }
        encodeNillableCodeType(AqdConstants.QN_GN_GRAMMATICAL_GENDER, v.getGrammaticalGender());
        encodeNillableCodeType(AqdConstants.QN_GN_GRAMMATICAL_NUMBER, v.getGrammaticalNumber());
        end(AqdConstants.QN_GN_GEOGRAPHICAL_NAME);
    }

    protected void encodeNillableCodeType(QName qn, Nillable<CodeType> v) throws XMLStreamException {
        if (!v.isAbsent()) {
            if (v.isNil()) {
                empty(qn);
                encodeNilAttr(v.getNilReason());
            } else {
                start(qn);
                if (v.get().isSetCodeSpace()) {
                    attr(AqdConstants.AN_CODE_SPACE, v.get().getCodeSpace().toString());
                }
                chars(v.get().getValue());
                endInline(qn);
            }
        }
    }

    protected void encodeNillablePronunciation(QName qn, Nillable<Pronunciation> v) throws XMLStreamException {
        if (!v.isAbsent()) {
            if (v.isNil()) {
                empty(qn);
                encodeNilAttr(v.getNilReason());
            } else {
                start(qn);
                encodePronunciationOfName(v.get());
                end(qn);
            }
        }
    }

    protected void encodePronunciationOfName(Pronunciation pronunciation) throws XMLStreamException {
        start(AqdConstants.QN_GN_PRONUNCIATION_OF_NAME);
        encodeNillableString(AqdConstants.QN_GN_PRONUNCIATION_SOUND_LINK, pronunciation.getSoundLink());
        encodeNillableString(AqdConstants.QN_GN_PRONUNCIATION_IPA, pronunciation.getIPA());
        end(AqdConstants.QN_GN_PRONUNCIATION_OF_NAME);
    }

    protected void encodeNillableSpelling(QName qn, Nillable<Spelling> v) throws XMLStreamException {
        if (!v.isAbsent()) {
            if (v.isNil()) {
                empty(qn);
                encodeNilAttr(v.getNilReason());
            } else {
                start(qn);
                encodeSpellingOfName(v.get());
                end(qn);
            }
        }
    }

    protected void encodeSpellingOfName(Spelling spelling) throws XMLStreamException {
        start(AqdConstants.QN_GN_SPELLING_OF_NAME);
        encodeString(AqdConstants.QN_GN_TEXT, spelling.getText());
        encodeNillableString(AqdConstants.QN_GN_SCRIPT, spelling.getScript());
        encodeNillableString(AqdConstants.QN_GN_TRANSLITERATION_SCHEME, spelling.getTransliterationScheme());
        end(AqdConstants.QN_GN_SPELLING_OF_NAME);
    }

    private void encodeTime(Time v) throws XMLStreamException, EncodingException {
        if (v instanceof TimePeriod) {
            encodeTimePeriod((TimePeriod) v);
        } else if (v instanceof TimeInstant) {
            encodeTimeInstant((TimeInstant) v);
        }
    }

    private void encodeTimeInstant(TimeInstant ti) throws XMLStreamException, EncodingException {
        start(GmlConstants.QN_TIME_INSTANT_32);
        attr(GmlConstants.QN_ID_32, getGMLId(ti));
        start(GmlConstants.QN_TIME_POSITION_32);
        encodeTimeString(ti.getValue(), ti.getTimeFormat());
        endInline(GmlConstants.QN_TIME_POSITION_32);
        end(GmlConstants.QN_TIME_INSTANT_32);
    }

    private void encodeTimePeriod(TimePeriod tp) throws XMLStreamException, EncodingException {
        start(GmlConstants.QN_TIME_PERIOD_32);
        attr(GmlConstants.QN_ID_32, getGMLId(tp));
        start(GmlConstants.QN_BEGIN_POSITION_32);
        encodeTimeString(tp.getStart(), tp.getTimeFormat());
        endInline(GmlConstants.QN_BEGIN_POSITION_32);
        start(GmlConstants.QN_END_POSITION_32);
        encodeTimeString(tp.getEnd(), tp.getTimeFormat());
        endInline(GmlConstants.QN_END_POSITION_32);
        end(GmlConstants.QN_TIME_PERIOD_32);
    }

    protected void encodeTimeString(DateTime time, TimeFormat format) throws XMLStreamException, EncodingException {
        chars(DateTimeHelper.formatDateTime2String(time, format));
    }

}
