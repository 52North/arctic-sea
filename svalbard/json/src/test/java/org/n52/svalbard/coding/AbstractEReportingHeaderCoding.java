/*
 * Copyright 2015-2020 52°North Initiative for Geospatial Open Source
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
package org.n52.svalbard.coding;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

import org.joda.time.DateTime;
import org.n52.shetland.aqd.EReportingChange;
import org.n52.shetland.aqd.EReportingHeader;
import org.n52.shetland.inspire.GeographicalName;
import org.n52.shetland.inspire.Pronunciation;
import org.n52.shetland.inspire.Spelling;
import org.n52.shetland.inspire.ad.AddressRepresentation;
import org.n52.shetland.inspire.base.Identifier;
import org.n52.shetland.inspire.base2.Contact;
import org.n52.shetland.inspire.base2.RelatedParty;
import org.n52.shetland.iso.gmd.LocalisedCharacterString;
import org.n52.shetland.iso.gmd.PT_FreeText;
import org.n52.shetland.ogc.gml.CodeType;
import org.n52.shetland.ogc.gml.time.TimeInstant;
import org.n52.shetland.w3c.Nillable;
import org.n52.shetland.w3c.xlink.Reference;
import org.n52.shetland.w3c.xlink.Referenceable;
import org.n52.svalbard.decode.DecoderRepository;
import org.n52.svalbard.decode.json.AddressJSONDecoder;
import org.n52.svalbard.decode.json.ContactJSONDecoder;
import org.n52.svalbard.decode.json.EReportingChangeJSONDecoder;
import org.n52.svalbard.decode.json.EReportingHeaderJSONDecoder;
import org.n52.svalbard.decode.json.GeographicalNameJSONDecoder;
import org.n52.svalbard.decode.json.InspireIDJSONDecoder;
import org.n52.svalbard.decode.json.PronunciationJSONDecoder;
import org.n52.svalbard.decode.json.RelatedPartyJSONDecoder;
import org.n52.svalbard.decode.json.SpellingJSONDecoder;
import org.n52.svalbard.encode.EncoderRepository;
import org.n52.svalbard.encode.json.AddressJSONEncoder;
import org.n52.svalbard.encode.json.CodeTypeJSONEncoder;
import org.n52.svalbard.encode.json.ContactJSONEncoder;
import org.n52.svalbard.encode.json.EReportingChangeJSONEncoder;
import org.n52.svalbard.encode.json.EReportingHeaderJSONEncoder;
import org.n52.svalbard.encode.json.GeographicNameJSONEncoder;
import org.n52.svalbard.encode.json.InspireIDJSONEncoder;
import org.n52.svalbard.encode.json.IterableJSONEncoder;
import org.n52.svalbard.encode.json.NillableJSONEncoder;
import org.n52.svalbard.encode.json.PTFreeTextJSONEncoder;
import org.n52.svalbard.encode.json.PronunciationJSONEncoder;
import org.n52.svalbard.encode.json.ReferenceJSONEncoder;
import org.n52.svalbard.encode.json.ReferenceableJSONEncoder;
import org.n52.svalbard.encode.json.RelatedPartyJSONEncoder;
import org.n52.svalbard.encode.json.SpellingJSONEncoder;
import org.n52.svalbard.encode.json.base.StringJSONEncoder;
import org.n52.svalbard.encode.json.base.TimeJSONEncoder;
import org.n52.svalbard.encode.json.base.URIJSONEncoder;

public abstract class AbstractEReportingHeaderCoding {

    protected EReportingHeader getHeader() throws URISyntaxException {
        return new EReportingHeader()
        .setInspireID(new Identifier("id", "namespace").setVersionId(Nillable.missing()))
        .setChange(new EReportingChange("Changed because... you know"))
        .setReportingPeriod(Referenceable.of(Nillable.present(new TimeInstant(DateTime.now()))))
        .setReportingAuthority(new RelatedParty().setIndividualName(Nillable.missing())
                .setOrganisationName("Organisation").setPositionName("Postionti")
                .addRole(new Reference().setHref(URI.create("http://hallo"))).addRole(Nillable.withheld())
                .setContact(new Contact().addTelephoneFacsimile("1234")
                        .addTelephoneFacsimile(
                                Nillable.missing())
                        .addTelephoneVoice("asdfasdf")
                        .setHoursOfService(
                                new PT_FreeText().addTextGroup(new LocalisedCharacterString("asdfasdf")))
                        .setWebsite(Nillable.unknown()).setElectronicMailAddress(Nillable.unknown())
                        .setAddress(new AddressRepresentation().setPostCode("12341234")
                                .setAddressFeature(new Reference().setHref(URI.create("http://asdfasdf")))
                                .addLocatorDesignator("localtor").addAddressArea(Nillable.withheld())
                                .addAddressArea(new GeographicalName()
                                        .setGrammaticalGender(new CodeType("a", new URI("b")))
                                        .setGrammaticalNumber(new CodeType("c", new URI("d")))
                                        .setLanguage("eng").setNativeness(new CodeType("<asdfasdf"))
                                        .setNameStatus(Nillable.unknown())
                                        .addSpelling(new Spelling().setScript("asdfasdf").setText("asdfasdf")
                                                .setTransliterationScheme("asdfasdfasdf"))
                                        .setPronunciation(new Pronunciation().setIPA("asdfasdf")
                                                .setSoundLink(URI.create("http://asdfasdf"))))
                                .addAdminUnit(new GeographicalName()
                                        .setGrammaticalGender(new CodeType("a", new URI("b")))
                                        .setGrammaticalNumber(new CodeType("c", new URI("d")))
                                        .setLanguage("eng").setNativeness(new CodeType("<asdfasdf"))
                                        .setNameStatus(Nillable.unknown())
                                        .addSpelling(new Spelling().setScript("asdfasdf").setText("asdfasdf")
                                                .setTransliterationScheme("asdfasdfasdf"))
                                        .setPronunciation(new Pronunciation().setIPA("asdfasdf")
                                                .setSoundLink(URI.create("http://asdfasdf"))))
                                .addPostName(Nillable.withheld())
                                .addPostName(new GeographicalName()
                                        .setGrammaticalGender(new CodeType("a", new URI("b")))
                                        .setGrammaticalNumber(new CodeType("c", new URI("d")))
                                        .setLanguage("eng").setNativeness(new CodeType("<asdfasdf"))
                                        .setNameStatus(Nillable.unknown())
                                        .addSpelling(new Spelling().setScript("asdfasdf").setText("asdfasdf")
                                                .setTransliterationScheme("asdfasdfasdf"))
                                        .setPronunciation(new Pronunciation().setIPA("asdfasdf")
                                                .setSoundLink(URI.create("http://asdfasdf"))))
                                .addThoroughfare(Nillable.withheld())
                                .addThoroughfare(new GeographicalName()
                                        .setGrammaticalGender(new CodeType("a", new URI("b")))
                                        .setGrammaticalNumber(new CodeType("c", new URI("d")))
                                        .setLanguage("eng").setNativeness(new CodeType("<asdfasdf"))
                                        .setNameStatus(Nillable.unknown())
                                        .addSpelling(new Spelling().setScript("asdfasdf").setText("asdfasdf")
                                                .setTransliterationScheme("asdfasdfasdf"))
                                        .setPronunciation(new Pronunciation().setIPA("asdfasdf")
                                                .setSoundLink(URI.create("http://asdfasdf")))))));
    }

    protected EReportingHeaderJSONDecoder getDecoder() {
        DecoderRepository decoderRepository = new DecoderRepository();
        EReportingHeaderJSONDecoder eReportingHeaderJSONDecoder = new EReportingHeaderJSONDecoder();
        eReportingHeaderJSONDecoder.setDecoderRepository(decoderRepository);

        EReportingChangeJSONDecoder eReportingChangeJSONDecoder = new EReportingChangeJSONDecoder();
        eReportingChangeJSONDecoder.setDecoderRepository(decoderRepository);

        InspireIDJSONDecoder inspireIDJSONDecoder = new InspireIDJSONDecoder();
        inspireIDJSONDecoder.setDecoderRepository(decoderRepository);

        RelatedPartyJSONDecoder relatedPartyJSONDecoder = new RelatedPartyJSONDecoder();
        relatedPartyJSONDecoder.setDecoderRepository(decoderRepository);

        ContactJSONDecoder contactJSONDecoder = new ContactJSONDecoder();
        contactJSONDecoder.setDecoderRepository(decoderRepository);

        AddressJSONDecoder addressJSONDecoder = new AddressJSONDecoder();
        addressJSONDecoder.setDecoderRepository(decoderRepository);

        PronunciationJSONDecoder pronunciationJSONDecoder = new PronunciationJSONDecoder();
        pronunciationJSONDecoder.setDecoderRepository(decoderRepository);

        SpellingJSONDecoder spellingJSONDecoder = new SpellingJSONDecoder();
        spellingJSONDecoder.setDecoderRepository(decoderRepository);

        GeographicalNameJSONDecoder gographicalNameJSONDecoder = new GeographicalNameJSONDecoder();
        gographicalNameJSONDecoder.setDecoderRepository(decoderRepository);


        decoderRepository.setDecoders(Arrays.asList(eReportingHeaderJSONDecoder,
                                                    eReportingChangeJSONDecoder,
                                                    inspireIDJSONDecoder,
                                                    relatedPartyJSONDecoder,
                                                    contactJSONDecoder,
                                                    addressJSONDecoder,
                                                    pronunciationJSONDecoder,
                                                    gographicalNameJSONDecoder,
                                                    spellingJSONDecoder));
        decoderRepository.init();
        return eReportingHeaderJSONDecoder;
    }

    protected EReportingHeaderJSONEncoder getEncoder() {

        EncoderRepository encoderRepository = new EncoderRepository();
        EReportingHeaderJSONEncoder eReportingHeaderJSONEncoder = new EReportingHeaderJSONEncoder();
        eReportingHeaderJSONEncoder.setEncoderRepository(encoderRepository);

        EReportingChangeJSONEncoder eReportingChangeJSONEncoder = new EReportingChangeJSONEncoder();
        eReportingChangeJSONEncoder.setEncoderRepository(encoderRepository);

        InspireIDJSONEncoder inspireIDJSONEncoder = new InspireIDJSONEncoder();
        inspireIDJSONEncoder.setEncoderRepository(encoderRepository);

        NillableJSONEncoder nillableJSONEncoder = new NillableJSONEncoder();
        nillableJSONEncoder.setEncoderRepository(encoderRepository);

        IterableJSONEncoder iterableJSONEncoder = new IterableJSONEncoder();
        iterableJSONEncoder.setEncoderRepository(encoderRepository);

        RelatedPartyJSONEncoder relatedPartyJSONEncoder = new RelatedPartyJSONEncoder();
        relatedPartyJSONEncoder.setEncoderRepository(encoderRepository);

        ContactJSONEncoder contactJSONEncoder = new ContactJSONEncoder();
        contactJSONEncoder.setEncoderRepository(encoderRepository);

        AddressJSONEncoder addressJSONEncoder = new AddressJSONEncoder();
        addressJSONEncoder.setEncoderRepository(encoderRepository);

        GeographicNameJSONEncoder geographicNameJSONEncoder = new GeographicNameJSONEncoder();
        geographicNameJSONEncoder.setEncoderRepository(encoderRepository);

        CodeTypeJSONEncoder codeTypeJSONEncoder = new CodeTypeJSONEncoder();
        codeTypeJSONEncoder.setEncoderRepository(encoderRepository);

        StringJSONEncoder stringJSONEncoder = new StringJSONEncoder();
        stringJSONEncoder.setEncoderRepository(encoderRepository);

        PronunciationJSONEncoder pronunciationJSONEncoder = new PronunciationJSONEncoder();
        pronunciationJSONEncoder.setEncoderRepository(encoderRepository);

        URIJSONEncoder uriJSONEncoder = new URIJSONEncoder();
        uriJSONEncoder.setEncoderRepository(encoderRepository);

        SpellingJSONEncoder spellingJSONEncoder = new SpellingJSONEncoder();
        spellingJSONEncoder.setEncoderRepository(encoderRepository);

        ReferenceJSONEncoder referenceJSONEncoder = new ReferenceJSONEncoder();
        referenceJSONEncoder.setEncoderRepository(encoderRepository);

        PTFreeTextJSONEncoder ptFreeTextJSONEncoder = new PTFreeTextJSONEncoder();
        ptFreeTextJSONEncoder.setEncoderRepository(encoderRepository);

        ReferenceableJSONEncoder referenceableJSONEncoder = new ReferenceableJSONEncoder();
        referenceableJSONEncoder.setEncoderRepository(encoderRepository);

        TimeJSONEncoder timeJSONEncoder = new TimeJSONEncoder();
        timeJSONEncoder.setEncoderRepository(encoderRepository);

        encoderRepository.setEncoders(Arrays.asList(eReportingHeaderJSONEncoder,
                                                    eReportingChangeJSONEncoder,
                                                    inspireIDJSONEncoder,
                                                    nillableJSONEncoder,
                                                    iterableJSONEncoder,
                                                    relatedPartyJSONEncoder,
                                                    contactJSONEncoder,
                                                    addressJSONEncoder,
                                                    geographicNameJSONEncoder,
                                                    codeTypeJSONEncoder,
                                                    stringJSONEncoder,
                                                    pronunciationJSONEncoder,
                                                    uriJSONEncoder,
                                                    spellingJSONEncoder,
                                                    referenceJSONEncoder,
                                                    ptFreeTextJSONEncoder,
                                                    referenceableJSONEncoder,
                                                    timeJSONEncoder));
        encoderRepository.init();
        return eReportingHeaderJSONEncoder;
    }
}
