/*
 * Copyright (C) 2015-2021 52°North Spatial Information Research GmbH
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

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URI;

import org.joda.time.DateTime;
import org.junit.jupiter.api.Test;
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
import org.n52.svalbard.encode.EncoderFlags;
import org.n52.svalbard.encode.EncoderRepository;
import org.n52.svalbard.encode.EncodingContext;
import org.n52.svalbard.read.ReportingHeaderReader;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class ReportingHeaderReaderTest {

    @Test
    public void testValidity() throws Exception {
        EReportingHeader header
                = new EReportingHeader()
                        .setInspireID(new Identifier("id", "namespace")
                                .setVersionId(Nillable.missing()))
                        .setChange(new EReportingChange("Changed because... you know"))
                        .setReportingPeriod(Referenceable.of(Nillable
                                .present(new TimeInstant(DateTime.now()))))
                        .setReportingAuthority(new RelatedParty()
                                .setIndividualName(Nillable.missing())
                                .setOrganisationName("Organisation")
                                .setPositionName("Postionti")
                                .addRole(new Reference().setHref(URI.create("http://hallo")))
                                .addRole(Nillable.withheld())
                                .setContact(new Contact()
                                        .addTelephoneFacsimile("1234")
                                        .addTelephoneFacsimile(Nillable.missing())
                                        .addTelephoneVoice("asdfasdf")
                                        .setHoursOfService(new PT_FreeText()
                                                .addTextGroup(new LocalisedCharacterString("asdfasdf")))
                                        .setWebsite(Nillable.unknown())
                                        .setElectronicMailAddress(Nillable.unknown())
                                        .setAddress(new AddressRepresentation()
                                                .setPostCode("12341234")
                                                .setAddressFeature(new Reference()
                                                        .setHref(URI.create("http://asdfasdf")))
                                                .addLocatorDesignator("localtor")
                                                .addAddressArea(Nillable.withheld())
                                                .addAddressArea(new GeographicalName()
                                                        .setGrammaticalGender(new CodeType("a", new URI("b")))
                                                        .setGrammaticalNumber(new CodeType("c", new URI("d")))
                                                        .setLanguage("eng")
                                                        .setNativeness(new CodeType("<asdfasdf"))
                                                        .setNameStatus(Nillable.unknown())
                                                        .addSpelling(new Spelling()
                                                                .setScript("asdfasdf")
                                                                .setText("asdfasdf")
                                                                .setTransliterationScheme("asdfasdfasdf")
                                                        )
                                                        .setPronunciation(new Pronunciation()
                                                                .setIPA("asdfasdf")
                                                                .setSoundLink(URI.create("http://asdfasdf"))
                                                        )
                                                )
                                                .addAdminUnit(new GeographicalName()
                                                        .setGrammaticalGender(new CodeType("a", new URI("b")))
                                                        .setGrammaticalNumber(new CodeType("c", new URI("d")))
                                                        .setLanguage("eng")
                                                        .setNativeness(new CodeType("<asdfasdf"))
                                                        .setNameStatus(Nillable.unknown())
                                                        .addSpelling(new Spelling()
                                                                .setScript("asdfasdf")
                                                                .setText("asdfasdf")
                                                                .setTransliterationScheme("asdfasdfasdf")
                                                        )
                                                        .setPronunciation(new Pronunciation()
                                                                .setIPA("asdfasdf")
                                                                .setSoundLink(URI.create("http://asdfasdf"))
                                                        ))
                                                .addPostName(Nillable.withheld())
                                                .addPostName(new GeographicalName()
                                                        .setGrammaticalGender(new CodeType("a", new URI("b")))
                                                        .setGrammaticalNumber(new CodeType("c", new URI("d")))
                                                        .setLanguage("eng")
                                                        .setNativeness(new CodeType("<asdfasdf"))
                                                        .setNameStatus(Nillable.unknown())
                                                        .addSpelling(new Spelling()
                                                                .setScript("asdfasdf")
                                                                .setText("asdfasdf")
                                                                .setTransliterationScheme("asdfasdfasdf")
                                                        )
                                                        .setPronunciation(new Pronunciation()
                                                                .setIPA("asdfasdf")
                                                                .setSoundLink(URI.create("http://asdfasdf"))
                                                        ))
                                                .addThoroughfare(Nillable.withheld())
                                                .addThoroughfare(new GeographicalName()
                                                        .setGrammaticalGender(new CodeType("a", new URI("b")))
                                                        .setGrammaticalNumber(new CodeType("c", new URI("d")))
                                                        .setLanguage("eng")
                                                        .setNativeness(new CodeType("<asdfasdf"))
                                                        .setNameStatus(Nillable.unknown())
                                                        .addSpelling(new Spelling()
                                                                .setScript("asdfasdf")
                                                                .setText("asdfasdf")
                                                                .setTransliterationScheme("asdfasdfasdf")
                                                        )
                                                        .setPronunciation(new Pronunciation()
                                                                .setIPA("asdfasdf")
                                                                .setSoundLink(URI.create("http://asdfasdf"))
                                                        )
                                                )
                                        )
                                )
                        );

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            new EReportingHeaderEncoder(EncodingContext.of(EncoderFlags.ENCODER_REPOSITORY, new EncoderRepository()),
                    baos, header).write();
            ByteArrayInputStream in = new ByteArrayInputStream(baos.toByteArray());
            EReportingHeader read = new ReportingHeaderReader().read(in);

            assertThat(read.getChange(), is(equalTo(header.getChange())));
            assertThat(read.getContent(), is(equalTo(header.getContent())));
            assertThat(read.getDelete(), is(equalTo(header.getDelete())));
            assertThat(read.getInspireID(), is(equalTo(header.getInspireID())));
            assertThat(read.getReportingPeriod(), is(equalTo(header.getReportingPeriod())));
            assertThat(read.getReportingAuthority(), is(equalTo(header.getReportingAuthority())));
            assertThat(read.getReportingAuthority().getIndividualName(),
                             is(header.getReportingAuthority().getIndividualName()));
            assertThat(read.getReportingAuthority().getOrganisationName().get(),
                             is(header.getReportingAuthority().getOrganisationName().get()));
            assertThat(read.getReportingAuthority().getPositionName().get(),
                             is(header.getReportingAuthority().getPositionName().get()));
            Contact c1 = read.getReportingAuthority().getContact().get();
            Contact c2 = header.getReportingAuthority().getContact().get();
            assertThat(c1, is(c2));
            assertThat(c1.getContactInstructions(), is(c2.getContactInstructions()));
            assertThat(c1.getElectronicMailAddress(), is(c2.getElectronicMailAddress()));
            assertThat(c1.getHoursOfService().get(), is(c2.getHoursOfService().get()));
            assertThat(c1.getTelephoneFacsimile(), is(c2.getTelephoneFacsimile()));
            assertThat(c1.getTelephoneVoice(), is(c2.getTelephoneVoice()));
            assertThat(c1.getWebsite(), is(c2.getWebsite()));
            AddressRepresentation a1 = c1.getAddress().get();
            AddressRepresentation a2 = c2.getAddress().get();

            assertThat(a1.getAddressAreas(), is(a2.getAddressAreas()));
            assertThat(a1.getAddressFeature(), is(a2.getAddressFeature()));
            assertThat(a1.getAdminUnits(), is(a2.getAdminUnits()));
            assertThat(a1.getLocatorDesignators(), is(a2.getLocatorDesignators()));
            assertThat(a1.getLocatorNames(), is(a2.getLocatorNames()));
            assertThat(a1.getPostCode(), is(a2.getPostCode()));
            assertThat(a1.getPostNames(), is(a2.getPostNames()));
            assertThat(a1.getThoroughfares(), is(a2.getThoroughfares()));
        }
    }

}
