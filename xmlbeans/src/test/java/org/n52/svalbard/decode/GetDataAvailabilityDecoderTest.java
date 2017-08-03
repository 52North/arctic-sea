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

import org.junit.Before;
import org.junit.Test;
import org.n52.svalbard.decode.exception.DecodingException;

import net.opengis.sosgda.x10.GetDataAvailabilityDocument;

public class GetDataAvailabilityDecoderTest {

    private GetDataAvailabilityXmlDecoder decoder;

    private GetDataAvailabilityDocument doc;

    @Before
    public void init() {
        decoder = new GetDataAvailabilityXmlDecoder();
        doc = GetDataAvailabilityDocument.Factory.newInstance();
        doc.addNewGetDataAvailability();
    }


    @Test
    public void test_decoding() throws DecodingException {
        decoder.decode(doc);
    }

}
