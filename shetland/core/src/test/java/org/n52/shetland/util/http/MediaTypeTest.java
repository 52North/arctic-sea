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
package org.n52.shetland.util.http;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;

import org.n52.janmayen.http.MediaType;
import org.n52.shetland.ogc.OGCConstants;
import org.n52.shetland.ogc.sensorML.SensorMLConstants;

/**
 * TODO JavaDoc
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
public class MediaTypeTest {

    @Test
    public void applicationXml() {
        MediaType mt = MediaType.parse("application/xml");
        assertThat(mt.getType(), is("application"));
        assertThat(mt.getSubtype(), is("xml"));
        assertThat(mt.isWildcard(), is(false));
        assertThat(mt.isWildcardType(), is(false));
        assertThat(mt.isWildcardSubtype(), is(false));
        assertThat(mt.getParameters().size(), is(0));
    }

    @Test
    public void applicationXmlWithQ() {
        MediaType mt = MediaType.parse("application/xml;q=1");
        assertThat(mt.getType(), is("application"));
        assertThat(mt.getSubtype(), is("xml"));
        assertThat(mt.getParameters().size(), is(1));
        assertThat(mt.getParameter("q").iterator().next(), is("1"));
    }

    @Test
    public void applicationXmlWithQAndSpace() {
        MediaType mt = MediaType.parse("application/xml; q=1");
        assertThat(mt.getType(), is("application"));
        assertThat(mt.getSubtype(), is("xml"));
        assertThat(mt.getParameters().size(), is(1));
        assertThat(mt.getParameter("q").iterator().next(), is("1"));
    }

    @Test
    public void applicationXmlWithQAndTwoSpace() {
        MediaType mt = MediaType.parse("application/xml; q=1");
        assertThat(mt.getType(), is("application"));
        assertThat(mt.getSubtype(), is("xml"));
        assertThat(mt.getParameters().size(), is(1));
        assertThat(mt.getParameter("q").iterator().next(), is("1"));
    }

    @Test
    public void applicationXmlWithQAndThreeSpace() {
        MediaType mt = MediaType.parse("application/xml; q=1");
        assertThat(mt.getType(), is("application"));
        assertThat(mt.getSubtype(), is("xml"));
        assertThat(mt.getParameters().size(), is(1));
        assertThat(mt.getParameter("q").iterator().next(), is("1"));
    }

    @Test
    public void applicationXmlWithQAndFourSpace() {
        MediaType mt = MediaType.parse("application/xml; q=1");
        assertThat(mt.getType(), is("application"));
        assertThat(mt.getSubtype(), is("xml"));
        assertThat(mt.getParameters().size(), is(1));
        assertThat(mt.getParameter("q").iterator().next(), is("1"));
    }

    @Test
    public void applicationXmlWithQAndFiveSpace() {
        MediaType mt = MediaType.parse("application/xml; q=1 ");
        assertThat(mt.getType(), is("application"));
        assertThat(mt.getSubtype(), is("xml"));
        assertThat(mt.getParameters().size(), is(1));
        assertThat(mt.getParameter("q").iterator().next(), is("1"));
    }

    @Test
    public void applicationXmlWithQAndSixSpace() {
        MediaType mt = MediaType.parse(" application/xml; q=1 ");
        assertThat(mt.getType(), is("application"));
        assertThat(mt.getSubtype(), is("xml"));
        assertThat(mt.getParameters().size(), is(1));
        assertThat(mt.getParameter("q").iterator().next(), is("1"));
    }

    @Test
    public void applicationXmlWithQAndV() {
        MediaType mt = MediaType.parse("application/xml;q=1;v=2");
        assertThat(mt.getType(), is("application"));
        assertThat(mt.getSubtype(), is("xml"));
        assertThat(mt.getParameters().size(), is(2));
        assertThat(mt.getParameter("q").iterator().next(), is("1"));
        assertThat(mt.getParameter("v").iterator().next(), is("2"));
    }

    @Test
    public void applicationXmlWithQAndVAndSpaces() {
        MediaType mt = MediaType.parse(" application/xml; q=1; v=2 ");
        assertThat(mt.getType(), is("application"));
        assertThat(mt.getSubtype(), is("xml"));
        assertThat(mt.getParameters().size(), is(2));
        assertThat(mt.getParameter("q").iterator().next(), is("1"));
        assertThat(mt.getParameter("v").iterator().next(), is("2"));
    }

    @Test
    public void applicationXmlWithQuotedParameter() {
        MediaType mt = MediaType.parse(" application/xml; a=\"asdf\"");
        assertThat(mt.getType(), is("application"));
        assertThat(mt.getSubtype(), is("xml"));
        assertThat(mt.getParameters().size(), is(1));
        assertThat(mt.getParameter("a").iterator().next(), is("asdf"));
    }

    @Test
    public void applicationXmlWithQuotedParameterAndSpaces() {
        MediaType mt = MediaType.parse(" application/xml; a=\"asdf\" ");
        assertThat(mt.getType(), is("application"));
        assertThat(mt.getSubtype(), is("xml"));
        assertThat(mt.getParameters().size(), is(1));
        assertThat(mt.getParameter("a").iterator().next(), is("asdf"));
    }

    @Test
    public void applicationXmlWithQuotedParameterAndSpacesWithin() {
        MediaType mt = MediaType.parse(" application/xml; a=\"as df\" ");
        assertThat(mt.getType(), is("application"));
        assertThat(mt.getSubtype(), is("xml"));
        assertThat(mt.getParameters().size(), is(1));
        assertThat(mt.getParameter("a").iterator().next(), is("as df"));
    }

    @Test
    public void applicationXmlWithQuotedParameterAndEqualSignWithin() {
        MediaType mt = MediaType.parse(" application/xml; a=\"as = df\" ");
        assertThat(mt.getType(), is("application"));
        assertThat(mt.getSubtype(), is("xml"));
        assertThat(mt.getParameters().size(), is(1));
        assertThat(mt.getParameter("a").iterator().next(), is("as = df"));
    }

    @Test
    public void applicationXmlWithQuotedParameterAndQuotesWithin() {
        MediaType mt = MediaType.parse("application/xml;a=\"as\\\" = df\"");
        assertThat(mt.getType(), is("application"));
        assertThat(mt.getSubtype(), is("xml"));
        assertThat(mt.getParameters().size(), is(1));
        assertThat(mt.getParameter("a").iterator().next(), is("as\" = df"));
    }

    @Test
    public void applicationXmlWithQuotedParameterFollowingChars() {
        assertThrows(IllegalArgumentException.class, () -> {
            MediaType.parse("application/xml;a=\"as\\\" = df\"aa");
        });
    }

    @Test
    public void applicationXmlWithUnquotedParameterWithQuoteWithin() {
        assertThrows(IllegalArgumentException.class, () -> {
            MediaType.parse("application/xml;a=a\"b");
        });
    }

    @Test
    public void applicationXmlWithUnquotedParameterWithQuotedQuoteWithin() {
        assertThrows(IllegalArgumentException.class, () -> {
            MediaType.parse("application/xml;a=a\\\"b");
        });
    }

    @Test
    public void applicationXmlWithQuotedParameterWithUnescapedSlash() {
        MediaType mt = MediaType.parse("application/xml;a=\"a\\\\b\"");
        assertThat(mt.getType(), is("application"));
        assertThat(mt.getSubtype(), is("xml"));
        assertThat(mt.getParameters().size(), is(1));
        assertThat(mt.getParameter("a").size(), is(1));
        assertThat(mt.getParameter("a").iterator().next(), is("a\\b"));
    }

    @Test
    public void applicationXmlWithUnquotedParameterWithSpaceWithin() {
        assertThrows(IllegalArgumentException.class, () -> {
            MediaType.parse("application/xml;a=a b");
        });
    }

    @Test
    public void missingSubtype() {
        assertThrows(IllegalArgumentException.class, () -> {
            MediaType.parse("application/;a=b");
        });
    }

    @Test
    public void singleType() {
        assertThrows(IllegalArgumentException.class, () -> {
            MediaType.parse("application;a=b");
        });
    }

    @Test
    public void missingType() {
        assertThrows(IllegalArgumentException.class, () -> {
            MediaType.parse("/xml;a=b");
        });
    }

    @Test
    public void missingParameterName() {
        assertThrows(IllegalArgumentException.class, () -> {
            MediaType.parse("application/xml;=b");
        });
    }

    @Test
    public void missingParameterNameWithSpace() {
        assertThrows(IllegalArgumentException.class, () -> {
            MediaType.parse("application/xml; =b");
        });
    }

    @Test
    public void missingParameterValue() {
        assertThrows(IllegalArgumentException.class, () -> {
            MediaType.parse("application/xml;a=");
        });
    }

    @Test
    public void missingParameterValueWithSpace() {
        assertThrows(IllegalArgumentException.class, () -> {
            MediaType.parse("application/xml;a= ");
        });
    }

    @Test
    public void emptyInput() {
        assertThrows(IllegalArgumentException.class, () -> {
            MediaType.parse("");
        });
    }

    @Test
    public void nullInput() {
        assertThrows(IllegalArgumentException.class, () -> {
            MediaType.parse(null);
        });
    }

    @Test
    public void wildCard() {
        MediaType mt = MediaType.parse("*/*");
        assertThat(mt.getType(), is("*"));
        assertThat(mt.getSubtype(), is("*"));
        assertThat(mt.isWildcard(), is(true));
        assertThat(mt.isWildcardType(), is(true));
        assertThat(mt.isWildcardSubtype(), is(true));
        assertThat(mt.getParameters().size(), is(0));
    }

    @Test
    public void wildCardSubtype() {
        MediaType mt = MediaType.parse("application/*");
        assertThat(mt.getType(), is("application"));
        assertThat(mt.getSubtype(), is("*"));
        assertThat(mt.isWildcard(), is(false));
        assertThat(mt.isWildcardType(), is(false));
        assertThat(mt.isWildcardSubtype(), is(true));
        assertThat(mt.getParameters().size(), is(0));
    }

    @Test
    public void testSlashEscape() {
        MediaType mt = MediaType.parse("a/b; x=\"a/1\"");
        assertThat(mt.toString(), is("a/b; x=\"a/1\""));
    }

    @Test
    public void testSlashEscape2() {
        assertThrows(IllegalArgumentException.class, () -> {
            MediaType.parse("a/b; x=a/1");
        });
    }

    @Test
    public void testSlashEscape3() {
        MediaType mt = new MediaType("a", "b", "x", "a/1");
        assertThat(mt.toString(), is("a/b; x=\"a/1\""));
    }

    @Test
    public void testUrn() {
        assertThrows(IllegalArgumentException.class, () -> {
            MediaType.parse(OGCConstants.URN_IDENTIFIER_IDENTIFICATION);
        });
    }

    @Test
    public void testOgcUrlUnknown() {
        assertThrows(IllegalArgumentException.class, () -> {
            MediaType.parse(OGCConstants.UNKNOWN);
        });
    }

    @Test
    public void testSensorMLUrl() {
        assertThrows(IllegalArgumentException.class, () -> {
            MediaType.parse(SensorMLConstants.SENSORML_OUTPUT_FORMAT_URL);
        });
    }

    @Test
    public void testCompatibleXmlTypes() {
        MediaType applicationXml = MediaType.parse("application/xml");
        MediaType textXml = MediaType.parse("text/xml");
        MediaType textPlain = MediaType.parse("text/plain");
        assertThat(applicationXml.isCompatible(textXml), is(true));
        assertThat(textXml.isCompatible(applicationXml), is(true));
        assertThat(applicationXml.isCompatible(textPlain), is(false));
        assertThat(textXml.isCompatible(textPlain), is(false));
    }
}
