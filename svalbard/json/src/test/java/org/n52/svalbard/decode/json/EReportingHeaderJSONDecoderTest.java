/*
 * Copyright 2015-2021 52°North Initiative for Geospatial Open Source
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
package org.n52.svalbard.decode.json;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.net.URISyntaxException;
import java.util.Iterator;

import org.junit.jupiter.api.Test;

import org.n52.shetland.aqd.EReportingHeader;
import org.n52.shetland.inspire.GeographicalName;
import org.n52.shetland.inspire.ad.AddressRepresentation;
import org.n52.shetland.inspire.base2.RelatedParty;
import org.n52.shetland.iso.gmd.LocalisedCharacterString;
import org.n52.shetland.iso.gmd.PT_FreeText;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.shetland.w3c.Nillable;
import org.n52.svalbard.coding.AbstractEReportingHeaderCoding;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.encode.exception.EncodingException;

import com.fasterxml.jackson.databind.JsonNode;

public class EReportingHeaderJSONDecoderTest
        extends
        AbstractEReportingHeaderCoding {

    @Test
    public void test()
            throws OwsExceptionReport,
            URISyntaxException,
            EncodingException,
            DecodingException {
        EReportingHeader header = getHeader();
        JsonNode encode = getEncoder().encode(header);
        EReportingHeader o = getDecoder().decode(encode);

        assertThat(o.getInspireID() != null, is(true));
        assertThat(o.getInspireID().getLocalId(), equalTo(header.getInspireID().getLocalId()));
        assertThat(o.getInspireID().getValue(), equalTo(header.getInspireID().getValue()));
        assertThat(o.getInspireID().getVersionId(), equalTo(header.getInspireID().getVersionId()));

        assertThat(o.getChange(), equalTo(header.getChange()));

        assertThat(o.getReportingPeriod().isInstance(), is(true));
        assertThat(o.getReportingPeriod().getInstance().isPresent(), is(true));
        assertThat(o.getReportingPeriod().getInstance().get(),
                equalTo(header.getReportingPeriod().getInstance().get()));

        assertThat(o.getReportingAuthority() != null, is(true));
        assertThat(o.getReportingAuthority(), instanceOf(RelatedParty.class));

        assertThat(o.getReportingAuthority().getIndividualName().isNil(), is(true));
        assertThat(o.getReportingAuthority().getIndividualName().getNilReason().isPresent(), is(true));
        assertThat(o.getReportingAuthority().getIndividualName().getNilReason().get(),
                equalTo(header.getReportingAuthority().getIndividualName().getNilReason().get()));

        assertThat(o.getReportingAuthority().getOrganisationName().isPresent(), is(true));
        assertThat(o.getReportingAuthority().getOrganisationName().get(),
                equalTo(header.getReportingAuthority().getOrganisationName().get()));

        assertThat(o.getReportingAuthority().getPositionName().isPresent(), is(true));
        assertThat(o.getReportingAuthority().getPositionName().get(),
                equalTo(header.getReportingAuthority().getPositionName().get()));

        assertThat(o.getReportingAuthority().getRoles() != null, is(true));
        assertThat(o.getReportingAuthority().getRoles().size(), is(2));

        assertThat(o.getReportingAuthority().getRoles().get(0).isPresent(), is(true));
        assertThat(o.getReportingAuthority().getRoles().get(0).get().getHref().isPresent(), is(true));
        assertThat(o.getReportingAuthority().getRoles().get(0).get().getHref().get(),
                equalTo(header.getReportingAuthority().getRoles().get(0).get().getHref().get()));

        assertThat(o.getReportingAuthority().getRoles().get(1).isNil(), is(true));
        assertThat(o.getReportingAuthority().getRoles().get(1).getNilReason().isPresent(), is(true));
        assertThat(o.getReportingAuthority().getRoles().get(1).getNilReason(),
                equalTo(header.getReportingAuthority().getRoles().get(1).getNilReason()));

        assertThat(o.getReportingAuthority().getContact().isPresent(), is(true));

        assertThat(o.getReportingAuthority().getContact().get().getTelephoneFacsimile().isPresent(), is(true));
        assertThat(o.getReportingAuthority().getContact().get().getTelephoneFacsimile().get().size(), is(2));

        assertThat(o.getReportingAuthority().getContact().get().getTelephoneFacsimile().get().get(0).isPresent(),
                is(true));
        assertThat(o.getReportingAuthority().getContact().get().getTelephoneFacsimile().get().get(0).get(),
                equalTo(header.getReportingAuthority().getContact().get().getTelephoneFacsimile().get().get(0).get()));

        assertThat(o.getReportingAuthority().getContact().get().getTelephoneFacsimile().get().get(1).isNil(),
                is(true));
        assertThat(o.getReportingAuthority().getContact().get().getTelephoneFacsimile().get().get(1).getNilReason()
                .isPresent(), is(true));
        assertThat(o.getReportingAuthority().getContact().get().getTelephoneFacsimile().get().get(1).getNilReason(),
                equalTo(o.getReportingAuthority().getContact().get().getTelephoneFacsimile().get().get(1)
                        .getNilReason()));

        assertThat(o.getReportingAuthority().getContact().get().getTelephoneVoice().isPresent(), is(true));
        assertThat(o.getReportingAuthority().getContact().get().getTelephoneVoice().get().size(), is(1));
        assertThat(o.getReportingAuthority().getContact().get().getTelephoneVoice().get().get(0).isPresent(),
                is(true));
        assertThat(o.getReportingAuthority().getContact().get().getTelephoneVoice().get().get(0).get(),
                equalTo(header.getReportingAuthority().getContact().get().getTelephoneVoice().get().get(0).get()));

        assertThat(o.getReportingAuthority().getContact().get().getHoursOfService().isPresent(), is(true));
        checkPtFreeText(o.getReportingAuthority().getContact().get().getHoursOfService().get(),
                header.getReportingAuthority().getContact().get().getHoursOfService().get());

        assertThat(o.getReportingAuthority().getContact().get().getWebsite().isNil(), is(true));
        assertThat(o.getReportingAuthority().getContact().get().getWebsite().getNilReason().isPresent(), is(true));
        assertThat(o.getReportingAuthority().getContact().get().getWebsite().getNilReason().get(),
                equalTo(header.getReportingAuthority().getContact().get().getWebsite().getNilReason().get()));

        assertThat(o.getReportingAuthority().getContact().get().getElectronicMailAddress().isNil(), is(true));
        assertThat(o.getReportingAuthority().getContact().get().getElectronicMailAddress().getNilReason().isPresent(),
                is(true));
        assertThat(o.getReportingAuthority().getContact().get().getElectronicMailAddress().getNilReason().get(),
                equalTo(header.getReportingAuthority().getContact().get().getWebsite().getNilReason().get()));

        checkAddress(o.getReportingAuthority().getContact().get().getAddress(),
                header.getReportingAuthority().getContact().get().getAddress());

        assertThat(o.getReportingAuthority().getContact().get().getAddress().get().getAdminUnits() != null, is(true));
        assertThat(o.getReportingAuthority().getContact().get().getAddress().get().getAdminUnits().size(), is(1));
        chekGeographicalName(o.getReportingAuthority().getContact().get().getAddress().get().getAdminUnits().get(0),
                header.getReportingAuthority().getContact().get().getAddress().get().getAdminUnits().get(0));

    }

    private void checkAddress(Nillable<AddressRepresentation> address, Nillable<AddressRepresentation> original) {
        assertThat(address.isPresent(), is(true));

        assertThat(address.get().getPostCode().isPresent(), is(true));
        assertThat(address.get().getPostCode().get(), equalTo(original.get().getPostCode().get()));

        assertThat(address.get().getAddressFeature().isPresent(), is(true));
        assertThat(address.get().getAddressFeature().get().getHref().isPresent(), is(true));
        assertThat(address.get().getAddressFeature().get().getHref().get(),
                equalTo(original.get().getAddressFeature().get().getHref().get()));

        assertThat(address.get().getLocatorDesignators() != null, is(true));
        assertThat(address.get().getLocatorDesignators().size(), is(1));
        assertThat(address.get().getLocatorDesignators().get(0),
                equalTo(original.get().getLocatorDesignators().get(0)));

        assertThat(address.get().getAddressAreas() != null, is(true));
        assertThat(address.get().getAddressAreas().size(), is(2));

        assertThat(address.get().getAddressAreas().get(0).isNil(), is(true));
        assertThat(address.get().getAddressAreas().get(0).getNilReason().isPresent(), is(true));
        assertThat(address.get().getAddressAreas().get(0).getNilReason().get(),
                equalTo(original.get().getAddressAreas().get(0).getNilReason().get()));

        assertThat(address.get().getAddressAreas().get(1).isPresent(), is(true));

        chekGeographicalName(address.get().getAddressAreas().get(1).get(),
                original.get().getAddressAreas().get(1).get());

        assertThat(address.get().getPostNames() != null, is(true));
        assertThat(address.get().getPostNames().size(), is(2));

        assertThat(address.get().getPostNames().get(0).isNil(), is(true));
        assertThat(address.get().getPostNames().get(0).getNilReason().isPresent(), is(true));
        assertThat(address.get().getPostNames().get(0).getNilReason().get(),
                equalTo(original.get().getPostNames().get(0).getNilReason().get()));

        assertThat(address.get().getPostNames().get(1).isPresent(), is(true));
        chekGeographicalName(address.get().getPostNames().get(1).get(), original.get().getPostNames().get(1).get());

        assertThat(address.get().getThoroughfares() != null, is(true));
        assertThat(address.get().getThoroughfares().size(), is(2));

        assertThat(address.get().getThoroughfares().get(0).isNil(), is(true));
        assertThat(address.get().getThoroughfares().get(0).getNilReason().isPresent(), is(true));
        assertThat(address.get().getThoroughfares().get(0).getNilReason().get(),
                equalTo(original.get().getThoroughfares().get(0).getNilReason().get()));

        assertThat(address.get().getPostNames().get(1).isPresent(), is(true));
        chekGeographicalName(address.get().getThoroughfares().get(1).get(),
                original.get().getThoroughfares().get(1).get());

    }

    private void chekGeographicalName(GeographicalName geographicalName, GeographicalName original) {
        assertThat(geographicalName.getGrammaticalGender().isPresent(), is(true));
        assertThat(geographicalName.getGrammaticalGender().get().isSetCodeSpace(), is(true));
        assertThat(geographicalName.getGrammaticalGender().get().getCodeSpace(),
                equalTo(original.getGrammaticalGender().get().getCodeSpace()));
        assertThat(geographicalName.getGrammaticalGender().get().isSetValue(), is(true));
        assertThat(geographicalName.getGrammaticalGender().get().getValue(),
                equalTo(original.getGrammaticalGender().get().getValue()));

        assertThat(geographicalName.getGrammaticalNumber().isPresent(), is(true));
        assertThat(geographicalName.getGrammaticalNumber().get().isSetCodeSpace(), is(true));
        assertThat(geographicalName.getGrammaticalNumber().get().getCodeSpace(),
                equalTo(original.getGrammaticalNumber().get().getCodeSpace()));
        assertThat(geographicalName.getGrammaticalNumber().get().isSetValue(), is(true));
        assertThat(geographicalName.getGrammaticalNumber().get().getValue(),
                equalTo(original.getGrammaticalNumber().get().getValue()));

        assertThat(geographicalName.getLanguage().isPresent(), is(true));
        assertThat(geographicalName.getLanguage().get(), equalTo(original.getLanguage().get()));

        assertThat(geographicalName.getNativeness().isPresent(), is(true));
        assertThat(geographicalName.getNativeness().get().isSetCodeSpace(), is(false));
        assertThat(geographicalName.getNativeness().get().isSetValue(), is(true));
        assertThat(geographicalName.getNativeness().get().getValue(),
                equalTo(original.getNativeness().get().getValue()));

        assertThat(geographicalName.getNameStatus().isNil(), is(true));
        assertThat(geographicalName.getNameStatus().getNilReason().isPresent(), is(true));
        assertThat(geographicalName.getNameStatus().getNilReason().get(),
                equalTo(original.getNameStatus().getNilReason().get()));

        assertThat(geographicalName.getSpelling() != null, is(true));
        assertThat(geographicalName.getSpelling().size(), is(1));

        assertThat(geographicalName.getSpelling().get(0) != null, is(true));
        assertThat(geographicalName.getSpelling().get(0).getScript().isPresent(), is(true));
        assertThat(geographicalName.getSpelling().get(0).getScript().get(),
                equalTo(original.getSpelling().get(0).getScript().get()));

        assertThat(geographicalName.getSpelling().get(0).getText() != null, is(true));
        assertThat(geographicalName.getSpelling().get(0).getText(), equalTo(original.getSpelling().get(0).getText()));

        assertThat(geographicalName.getSpelling().get(0).getTransliterationScheme() != null, is(true));
        assertThat(geographicalName.getSpelling().get(0).getTransliterationScheme(),
                equalTo(original.getSpelling().get(0).getTransliterationScheme()));

        assertThat(geographicalName.getPronunciation().isPresent(), is(true));

        assertThat(geographicalName.getPronunciation().get().getIPA().isPresent(), is(true));
        assertThat(geographicalName.getPronunciation().get().getIPA().get(),
                equalTo(original.getPronunciation().get().getIPA().get()));

        assertThat(geographicalName.getPronunciation().get().getSoundLink().isPresent(), is(true));
        assertThat(geographicalName.getPronunciation().get().getSoundLink().get(),
                equalTo(original.getPronunciation().get().getSoundLink().get()));

    }

    private void checkPtFreeText(PT_FreeText decoded, PT_FreeText original) {
        assertThat(decoded.isSetTextGroup(), is(true));
        assertThat(decoded.getTextGroup().size(), equalTo(original.getTextGroup().size()));

        Iterator<LocalisedCharacterString> decodedIterator = decoded.getTextGroup().iterator();
        Iterator<LocalisedCharacterString> originalIterator = original.getTextGroup().iterator();
        while (decodedIterator.hasNext() && originalIterator.hasNext()) {
            LocalisedCharacterString decodedLCS = decodedIterator.next();
            LocalisedCharacterString originalLCS = originalIterator.next();

            assertThat(decodedLCS.isSetLocale(), equalTo(originalLCS.isSetLocale()));
            if (decodedLCS.isSetLocale() && originalLCS.isSetLocale()) {
                assertThat(decodedLCS.getLocale(), equalTo(originalLCS.getLocale()));
            }
            assertThat(decodedLCS.getValue(), equalTo(originalLCS.getValue()));
        }
    }

}
