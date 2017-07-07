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
package org.n52.svalbard.encode;

import java.util.Set;

import org.apache.xmlbeans.XmlObject;
import org.n52.svalbard.encode.AbstractSensorMLEncoder;
import org.n52.svalbard.encode.EncoderKey;
import org.n52.svalbard.encode.EncodingContext;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.exception.UnsupportedEncoderInputException;

/**
 * Test class implementation for {@link AbstractSensorMLEncoder}
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public class TestAbstractSensorMLEncoder extends AbstractSensorMLEncoder {

    @Override
    public Set<String> getSupportedProcedureDescriptionFormats(String service, String version) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public XmlObject encode(Object objectToEncode, EncodingContext additionalValues)
            throws EncodingException, UnsupportedEncoderInputException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<EncoderKey> getKeys() {
        // TODO Auto-generated method stub
        return null;
    }


}
