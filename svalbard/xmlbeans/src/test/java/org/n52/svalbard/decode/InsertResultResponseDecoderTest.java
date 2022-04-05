/*
 * Copyright 2015-${currentYear} 52°North Spatial Information Research GmbH
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
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;

import org.apache.xmlbeans.XmlObject;
import org.junit.jupiter.api.Test;
import org.n52.shetland.ogc.sos.response.InsertResultResponse;
import org.n52.svalbard.decode.exception.DecodingException;

import net.opengis.sos.x20.InsertResultResponseDocument;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">J&uuml;rrens, Eike Hinderk</a>
 *
 */
public class InsertResultResponseDecoderTest implements InsertDecoderTest {

    @Test
    public void shouldCreateInsertResultResponse() throws DecodingException {
        InsertResultResponseDocument isrd = InsertResultResponseDocument.Factory.newInstance();
        isrd.addNewInsertResultResponse();
        InsertResultResponse decodedResponse = new InsertResultResponseDecoder().decode(isrd);

        assertThat(decodedResponse, is(notNullValue(InsertResultResponse.class)));
    }

    @Override
    public Decoder<?, XmlObject> getDecoder() {
        return new InsertResultResponseDecoder();
    }

    @Override
    public XmlObject getDocument() {
        return InsertResultResponseDocument.Factory.newInstance();
    }

}
