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
package org.n52.svalbard.read;

import static org.hamcrest.core.Is.is;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.xml.stream.XMLStreamException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.n52.shetland.inspire.GeographicalName;
import org.n52.shetland.inspire.Spelling;
import org.n52.shetland.ogc.gml.CodeType;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.shetland.w3c.Nillable;
import org.n52.svalbard.decode.exception.DecodingException;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class GeographicalNameReaderTest {
    @Rule
    public final ErrorCollector errors = new ErrorCollector();

    @Test
    public void test() throws UnsupportedEncodingException, XMLStreamException, URISyntaxException, DecodingException {

        String xml = "<gn:GeographicalName xmlns:gn=\"urn:x-inspire:specification:gmlas:GeographicalNames:3.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                     "  <gn:language>eng</gn:language>\n" +
                     "  <gn:nativeness>&lt;asdfasdf</gn:nativeness>\n" +
                     "  <gn:nameStatus xsi:nil=\"true\" nilReason=\"unknown\"/>\n" +
                     "  <gn:sourceOfName xsi:nil=\"true\" nilReason=\"missing\"/>\n" +
                     "  <gn:pronunciation>\n" +
                     "    <gn:PronunciationOfName>\n" +
                     "      <gn:pronunciationSoundLink>http://asdfasdf</gn:pronunciationSoundLink>\n" +
                     "      <gn:pronunciationIPA>asdfasdf</gn:pronunciationIPA>\n" +
                     "    </gn:PronunciationOfName>\n" +
                     "  </gn:pronunciation>\n" +
                     "  <gn:spelling>\n" +
                     "    <gn:SpellingOfName>\n" +
                     "      <gn:text>asdfasdf</gn:text>\n" +
                     "      <gn:script>asdfasdf</gn:script>\n" +
                     "      <gn:transliterationScheme>asdfasdfasdf</gn:transliterationScheme>\n" +
                     "    </gn:SpellingOfName>\n" +
                     "  </gn:spelling>\n" +
                     "  <gn:grammaticalGender codeSpace=\"b\">a</gn:grammaticalGender>\n" +
                     "  <gn:grammaticalNumber codeSpace=\"d\">c</gn:grammaticalNumber>\n" +
                     "</gn:GeographicalName>";

        GeographicalName gn = new GeographicalNameReader()
                .read(new ByteArrayInputStream(xml.getBytes("UTF-8")));

        errors.checkThat(gn.getGrammaticalGender(), is(Nillable.of(new CodeType("a", new URI("b")))));
        errors.checkThat(gn.getGrammaticalNumber(), is(Nillable.of(new CodeType("c", new URI("d")))));
        errors.checkThat(gn.getLanguage(), is(Nillable.of("eng")));
        errors.checkThat(gn.getNativeness(), is(Nillable.of(new CodeType("<asdfasdf"))));
        errors.checkThat(gn.getNameStatus(), is(Nillable.<CodeType>unknown()));

        for (Spelling sp : gn.getSpelling()) {
            errors.checkThat(sp.getText(), is("asdfasdf"));
            errors.checkThat(sp.getScript(), is(Nillable.of("asdfasdf")));
            errors.checkThat(sp.getTransliterationScheme(), is(Nillable.of("asdfasdfasdf")));
        }

        errors.checkThat(gn.getPronunciation().get().getIPA(), is(Nillable.of("asdfasdf")));
        errors.checkThat(gn.getPronunciation().get().getSoundLink(), is(Nillable.of(URI.create("http://asdfasdf"))));

    }
}
