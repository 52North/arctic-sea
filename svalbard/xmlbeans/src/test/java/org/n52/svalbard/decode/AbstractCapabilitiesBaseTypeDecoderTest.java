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
package org.n52.svalbard.decode;

import java.util.Set;

import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;
import org.n52.shetland.ogc.ows.OwsCapabilities;
import org.n52.svalbard.decode.exception.DecodingException;

import net.opengis.ows.x11.CapabilitiesBaseType;

public class AbstractCapabilitiesBaseTypeDecoderTest {

    @Test
    public void shouldNotThrowNullPointerExceptionWhenServiceIdentificationIsMissing() throws DecodingException {
        AbstractCapabilitiesBaseTypeDecoder<CapabilitiesBaseType, OwsCapabilities> decoder = new TestSeam();
        CapabilitiesBaseType cbt = CapabilitiesBaseType.Factory.newInstance();
        cbt.setVersion("2.0.0");
        cbt.setUpdateSequence("nothing-to-see-here");
        Assert.assertThat(decoder.parseCapabilitiesBaseType("SOS", cbt).getServiceIdentification().isPresent(), Is.is(false));
    }

    private class TestSeam extends AbstractCapabilitiesBaseTypeDecoder<CapabilitiesBaseType, OwsCapabilities> {

        @Override
        public OwsCapabilities decode(CapabilitiesBaseType objectToDecode) throws DecodingException {
            return null;
        }

        @Override
        public Set<DecoderKey> getKeys() {
            return null;
        }

    }

}
