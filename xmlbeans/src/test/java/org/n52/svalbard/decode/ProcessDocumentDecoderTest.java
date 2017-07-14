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

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.Arrays;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.junit.Before;
import org.junit.Test;
import org.n52.janmayen.Producer;
import org.n52.shetland.inspire.ompr.Process;
import org.n52.svalbard.decode.exception.DecodingException;

public class ProcessDocumentDecoderTest extends AbtractProcessDecodingTest {

    @Test
    public void test_decoding() throws XmlException, IOException, DecodingException {
        Process process = createProcessFromFile();
        assertThat(process.isSetIdentifier(), is(true));
    }

}
