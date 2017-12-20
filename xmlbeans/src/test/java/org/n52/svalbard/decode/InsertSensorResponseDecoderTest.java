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

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.apache.xmlbeans.XmlObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.n52.shetland.ogc.sos.response.InsertSensorResponse;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.decode.exception.UnsupportedDecoderInputException;

import net.opengis.swes.x20.InsertSensorResponseDocument;
import net.opengis.swes.x20.InsertSensorResponseType;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">J&uuml;rrens, Eike Hinderk</a>
 */
public class InsertSensorResponseDecoderTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void shouldThrowExceptionOnWrongInputType() throws DecodingException {
        thrown.expect(UnsupportedDecoderInputException.class);
        thrown.expectMessage(
                "Decoder InsertSensorResponseDecoder can not decode 'org.apache.xmlbeans.impl.values.XmlAnyTypeImpl'");

        new InsertSensorResponseDecoder().decode(XmlObject.Factory.newInstance());
    }

    @Test
    public void shouldThrowExceptionOnNullInput() throws DecodingException {
        thrown.expect(UnsupportedDecoderInputException.class);
        thrown.expectMessage(
                "Decoder InsertSensorResponseDecoder can not decode 'null'");

        new InsertSensorResponseDecoder().decode(null);
    }

    @Test
    public void shouldThrowExceptionOnMissingTypeInDocument() throws DecodingException {
        thrown.expect(DecodingException.class);
        thrown.expectMessage("Received XML document is not valid. Set log level to debug to get more details");

        new InsertSensorResponseDecoder().decode(InsertSensorResponseDocument.Factory.newInstance());
    }

    @Test
    public void shouldCreateInsertSensorResponse() throws DecodingException {
        String offering = "test-offering";
        String procedure = "test-procedure";
        InsertSensorResponseDocument isrd = InsertSensorResponseDocument.Factory.newInstance();
        InsertSensorResponseType response = isrd.addNewInsertSensorResponse();
        response.setAssignedOffering(offering);
        response.setAssignedProcedure(procedure);

        InsertSensorResponse decodedResponse = new InsertSensorResponseDecoder().decode(isrd);

        assertThat(decodedResponse.getAssignedOffering(), is(offering));
        assertThat(decodedResponse.getAssignedProcedure(), is(procedure));
    }

}
