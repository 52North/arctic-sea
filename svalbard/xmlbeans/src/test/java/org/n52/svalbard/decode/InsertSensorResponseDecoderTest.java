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
package org.n52.svalbard.decode;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.apache.xmlbeans.XmlObject;
import org.junit.jupiter.api.Test;
import org.n52.shetland.ogc.sos.response.InsertSensorResponse;
import org.n52.svalbard.decode.exception.DecodingException;

import net.opengis.swes.x20.InsertSensorResponseDocument;
import net.opengis.swes.x20.InsertSensorResponseType;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">J&uuml;rrens, Eike Hinderk</a>
 */
public class InsertSensorResponseDecoderTest implements InsertDecoderTest {

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

    @Override
    public Decoder<?, XmlObject> getDecoder() {
        return new InsertSensorResponseDecoder();
    }

    @Override
    public XmlObject getDocument() {
        return InsertSensorResponseDocument.Factory.newInstance();
    }

}
