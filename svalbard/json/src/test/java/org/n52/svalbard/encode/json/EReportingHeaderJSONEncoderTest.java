/*
 * Copyright 2015-2018 52°North Initiative for Geospatial Open Source
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
package org.n52.svalbard.encode.json;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

import org.joda.time.DateTime;
import org.junit.Test;

import org.n52.janmayen.Json;
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
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.shetland.w3c.Nillable;
import org.n52.shetland.w3c.xlink.Reference;
import org.n52.shetland.w3c.xlink.Referenceable;
import org.n52.svalbard.encode.EncoderRepository;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.json.base.StringJSONEncoder;
import org.n52.svalbard.encode.json.base.TimeJSONEncoder;
import org.n52.svalbard.encode.json.base.URIJSONEncoder;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class EReportingHeaderJSONEncoderTest {

    @Test
    public void test()
            throws OwsExceptionReport, URISyntaxException, EncodingException {
        EReportingHeader header = new EReportingHeader()
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

        JsonNode o = getEncoder().encode(header);

        System.out.println(Json.print(o));
    }

    private EReportingHeaderJSONEncoder getEncoder() {

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
