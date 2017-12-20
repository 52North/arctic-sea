/*
 * Copyright 2015-2017 52°North Initiative for Geospatial Open Source
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
import org.apache.xmlbeans.XmlString;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.sos.request.InsertResultRequest;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.exception.UnsupportedEncoderInputException;

import net.opengis.sos.x20.InsertResultDocument;
import net.opengis.sos.x20.InsertResultType;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">J&uuml;rrens, Eike Hinderk</a>
 */
public class InsertResultRequestEncoderTest {

    private InsertResultRequestEncoder encoder;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setup() {
        encoder = new InsertResultRequestEncoder();
        encoder.setXmlOptions(XmlOptions::new);
    }

    @Test
    public void shouldThrowExceptionWhenNullValueReceived() throws EncodingException {
        thrown.expect(UnsupportedEncoderInputException.class);
        thrown.expectMessage(Is.is("Encoder " +
                InsertResultRequestEncoder.class.getSimpleName() +
                " can not encode 'null'"));

        encoder.create(null);
    }

    @Test
    public void shouldThrowExceptionIfServiceIsMissing() throws EncodingException {
        thrown.expect(UnsupportedEncoderInputException.class);
        thrown.expectMessage(Is.is("Encoder " +
                InsertResultRequestEncoder.class.getSimpleName() +
                " can not encode 'missing service'"));

        encoder.create(new InsertResultRequest());
    }

    @Test
    public void shouldThrowExceptionIfVersionIsMissing() throws EncodingException {
        thrown.expect(UnsupportedEncoderInputException.class);
        thrown.expectMessage(Is.is("Encoder " +
                InsertResultRequestEncoder.class.getSimpleName() +
                " can not encode 'missing version'"));

        encoder.create(new InsertResultRequest("service", ""));
    }

    @Test
    public void shouldThrowExceptionIfTemplateIdIsMissing() throws EncodingException {
        thrown.expect(UnsupportedEncoderInputException.class);
        thrown.expectMessage(Is.is("Encoder " +
                InsertResultRequestEncoder.class.getSimpleName() +
                " can not encode 'missing templateIdentifier'"));

        encoder.create(new InsertResultRequest("service", "version"));
    }

    @Test
    public void shouldThrowExceptionIfResultValuesAreMissing() throws EncodingException {
        thrown.expect(UnsupportedEncoderInputException.class);
        thrown.expectMessage(Is.is("Encoder " +
                InsertResultRequestEncoder.class.getSimpleName() +
                " can not encode 'missing resultValues'"));

        InsertResultRequest request = new InsertResultRequest("service", "version");
        request.setTemplateIdentifier("templateIdentifier");

        encoder.create(request);
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
        Assert.assertThat(encodedRequest, Matchers.instanceOf(InsertResultDocument.class));
        Assert.assertThat(((InsertResultDocument)encodedRequest).getInsertResult(), Matchers.notNullValue());
        InsertResultType xbRequest = ((InsertResultDocument)encodedRequest).getInsertResult();
        Assert.assertThat(xbRequest.getService(), Is.is(service));
        Assert.assertThat(xbRequest.getVersion(), Is.is(version));
        Assert.assertThat(xbRequest.getTemplate(), Is.is(templateIdentifier));
        Assert.assertThat(xbRequest.getResultValues(), Matchers.instanceOf(XmlString.class));
        Assert.assertThat(((XmlString)xbRequest.getResultValues()).getStringValue(), Is.is(resultValues));
    }

}
