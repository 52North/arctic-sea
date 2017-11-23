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
package org.n52.svalbard.decode;

import org.apache.xmlbeans.XmlObject;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.n52.shetland.ogc.sos.response.InsertResultTemplateResponse;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.decode.exception.UnsupportedDecoderInputException;
import org.n52.svalbard.util.XmlHelper;

import net.opengis.sos.x20.InsertResultTemplateResponseDocument;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">J&uuml;rrens, Eike Hinderk</a>
 */
public class InsertResultTemplateResponseDecoderTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void shouldThrowExceptionOnWrongInputType() throws DecodingException {
        thrown.expect(UnsupportedDecoderInputException.class);
        thrown.expectMessage(
                "Decoder InsertResultTemplateResponseDecoder can not decode 'org.apache.xmlbeans.impl.values.XmlAnyTypeImpl'");

        new InsertResultTemplateResponseDecoder().decode(XmlObject.Factory.newInstance());
    }

    @Test
    public void shouldThrowExceptionOnNullInput() throws DecodingException {
        thrown.expect(UnsupportedDecoderInputException.class);
        thrown.expectMessage(
                "Decoder InsertResultTemplateResponseDecoder can not decode 'null'");

        new InsertResultTemplateResponseDecoder().decode(null);
    }

    @Test
    public void shouldThrowExceptionOnMissingTypeInDocument() throws DecodingException {
        thrown.expect(DecodingException.class);
        thrown.expectMessage("Received XML document is not valid. Set log level to debug to get more details");

        new InsertResultTemplateResponseDecoder().decode(InsertResultTemplateResponseDocument.Factory.newInstance());
    }

    @Test
    public void shouldCreateInsertSensorResponse() throws DecodingException {
        String templateId = "test-template-id";
        InsertResultTemplateResponseDocument isrd = InsertResultTemplateResponseDocument.Factory.newInstance();
        isrd.addNewInsertResultTemplateResponse().setAcceptedTemplate(templateId );

        InsertResultTemplateResponse decodedResponse = new InsertResultTemplateResponseDecoder().decode(isrd);

        XmlHelper.validateDocument(isrd);

        Assert.assertThat(decodedResponse.getAcceptedTemplate(), Is.is(templateId));
    }

}
