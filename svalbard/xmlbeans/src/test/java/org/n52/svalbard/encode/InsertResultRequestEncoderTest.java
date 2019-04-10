/*
 * Copyright 2015-2019 52°North Initiative for Geospatial Open Source
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlString;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.sos.request.InsertResultRequest;
import org.n52.svalbard.encode.exception.EncodingException;

import net.opengis.sos.x20.InsertResultDocument;
import net.opengis.sos.x20.InsertResultType;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">J&uuml;rrens, Eike Hinderk</a>
 */
public class InsertResultRequestEncoderTest {

    private InsertResultRequestEncoder encoder;

    @BeforeEach
    public void setup() {
        encoder = new InsertResultRequestEncoder();
        encoder.setXmlOptions(XmlOptions::new);
    }

    @Test
    public void shouldThrowExceptionWhenNullValueReceived() throws EncodingException {
        EncodingException assertThrows = assertThrows(EncodingException.class, () -> {
            encoder.create(null);
        });
        assertEquals("Encoder " +
                InsertResultRequestEncoder.class.getSimpleName() +
                " can not encode 'null'", assertThrows.getMessage());
    }

    @Test
    public void shouldThrowExceptionIfServiceIsMissing() throws EncodingException {
        EncodingException assertThrows = assertThrows(EncodingException.class, () -> {
            encoder.create(new InsertResultRequest());
        });
        assertEquals("Encoder " +
                InsertResultRequestEncoder.class.getSimpleName() +
                " can not encode 'missing service'", assertThrows.getMessage());
    }

    @Test
    public void shouldThrowExceptionIfVersionIsMissing() throws EncodingException {
        EncodingException assertThrows = assertThrows(EncodingException.class, () -> {
            encoder.create(new InsertResultRequest("SOS", ""));
        });
        assertEquals("Encoder " +
                InsertResultRequestEncoder.class.getSimpleName() +
                " can not encode 'missing version'", assertThrows.getMessage());
    }
//
//    @Test
//    public void shouldThrowExceptionWhenNullValueReceived() throws EncodingException {
//        thrown.expect(UnsupportedEncoderInputException.class);
//        thrown.expectMessage(Is.is("Encoder " +
//                InsertResultRequestEncoder.class.getSimpleName() +
//                " can not encode 'null'"));
//
//        encoder.create(null);
//    }
//
//    @Test
//    public void shouldThrowExceptionIfServiceIsMissing() throws EncodingException {
//        thrown.expect(UnsupportedEncoderInputException.class);
//        thrown.expectMessage(Is.is("Encoder " +
//                InsertResultRequestEncoder.class.getSimpleName() +
//                " can not encode 'missing service'"));
//
//        encoder.create(new InsertResultRequest());
//    }
//
//    @Test
//    public void shouldThrowExceptionIfVersionIsMissing() throws EncodingException {
//        thrown.expect(UnsupportedEncoderInputException.class);
//        thrown.expectMessage(Is.is("Encoder " +
//                InsertResultRequestEncoder.class.getSimpleName() +
//                " can not encode 'missing version'"));
//
//        encoder.create(new InsertResultRequest("service", ""));
//    }

    @Test
    public void shouldThrowExceptionIfTemplateIdIsMissing() throws EncodingException {
        EncodingException assertThrows = assertThrows(EncodingException.class, () -> {
            encoder.create(new InsertResultRequest("service", "version"));
        });
        assertEquals("Encoder " +
                InsertResultRequestEncoder.class.getSimpleName() +
                " can not encode 'missing templateIdentifier'", assertThrows.getMessage());
    }

    @Test
    public void shouldThrowExceptionIfResultValuesAreMissing() throws EncodingException {
        EncodingException assertThrows = assertThrows(EncodingException.class, () -> {
            InsertResultRequest request = new InsertResultRequest("service", "version");
            request.setTemplateIdentifier("templateIdentifier");

            encoder.create(request);
        });
        assertEquals("Encoder " +
                InsertResultRequestEncoder.class.getSimpleName() +
                " can not encode 'missing resultValues'", assertThrows.getMessage());
    }

    @Test
    public void shouldEncodeInsertResultRequest() throws EncodingException {
        String version = Sos2Constants.SERVICEVERSION;
        String service = SosConstants.SOS;
        String templateIdentifier = "test-template-identifier";
        String resultValues = "test-result-values";
        InsertResultRequest request = new InsertResultRequest(service, version);
        request.setTemplateIdentifier(templateIdentifier);
        request.setResultValues(resultValues);

        XmlObject encodedRequest = encoder.create(request);
        assertThat(encodedRequest, Matchers.instanceOf(InsertResultDocument.class));
        assertThat(((InsertResultDocument)encodedRequest).getInsertResult(), Matchers.notNullValue());
        InsertResultType xbRequest = ((InsertResultDocument)encodedRequest).getInsertResult();
        assertThat(xbRequest.getService(), Is.is(service));
        assertThat(xbRequest.getVersion(), Is.is(version));
        assertThat(xbRequest.getTemplate(), Is.is(templateIdentifier));
        assertThat(xbRequest.getResultValues(), Matchers.instanceOf(XmlString.class));
        assertThat(((XmlString)xbRequest.getResultValues()).getStringValue(), Is.is(resultValues));
    }

}
