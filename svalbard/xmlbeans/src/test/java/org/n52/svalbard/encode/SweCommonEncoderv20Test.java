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
package org.n52.svalbard.encode;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.shetland.ogc.swe.SweDataRecord;
import org.n52.shetland.ogc.swe.SweField;
import org.n52.shetland.ogc.swe.encoding.SweTextEncoding;
import org.n52.shetland.ogc.swe.simpleType.SweCount;
import org.n52.shetland.ogc.swe.simpleType.SweText;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.util.XmlHelper;

import net.opengis.swe.x20.TextEncodingType;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk
 *         J&uuml;rrens</a>
 *
 * @since 1.0.0
 */
public class SweCommonEncoderv20Test {
    private SweCommonEncoderv20 sweCommonEncoderv20;

    @Before
    public void setup() {
        sweCommonEncoderv20 = new SweCommonEncoderv20();
        sweCommonEncoderv20.setXmlOptions(XmlOptions::new);
    }

    @Test
    public void shouldEncodeDataRecordWithSweTextField() throws OwsExceptionReport, EncodingException {
        final SweDataRecord record = new SweDataRecord();
        record.addField(new SweField("text", new SweText().setValue("textValue").setDefinition("textDef")));
        record.addField(new SweField("count", new SweCount().setValue(2).setDefinition("countDef")));

        final XmlObject encoded = sweCommonEncoderv20.encode(record);

        // validateDocument throws exceptions if the document is invalid
        XmlHelper.validateDocument(encoded, EncodingException::new);
    }

    @Test
    public void shouldEncodeTextEncoding() throws EncodingException {
        String tokenSeparator = "@";
        String blockSeparator = ";";
        SweTextEncoding textEncoding = new SweTextEncoding();
        textEncoding.setBlockSeparator(blockSeparator);
        textEncoding.setTokenSeparator(tokenSeparator);

        XmlObject encodedEncoding = sweCommonEncoderv20.encode(textEncoding);

        Assert.assertThat(encodedEncoding, Matchers.instanceOf(TextEncodingType.class));
        TextEncodingType xbTextEncoding = (TextEncodingType) encodedEncoding;
        Assert.assertThat(xbTextEncoding.getBlockSeparator(), Is.is(blockSeparator));
        Assert.assertThat(xbTextEncoding.getTokenSeparator(), Is.is(tokenSeparator));
    }

}
