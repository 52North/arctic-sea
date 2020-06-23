/*
 * Copyright 2015-2020 52°North Initiative for Geospatial Open Source
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

import java.util.Collections;
import java.util.Set;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.impl.xb.substwsdl.DefinitionsDocument;
import org.apache.xmlbeans.impl.xb.substwsdl.DefinitionsDocument.Definitions;
import org.n52.janmayen.http.MediaType;
import org.n52.janmayen.http.MediaTypes;
import org.n52.shetland.w3c.wsa.WsaConstants;
import org.n52.shetland.w3c.wsa.WsaHeader;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.util.CodingHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;

public class WsdlEncoderv11 extends AbstractXmlEncoder<XmlObject, Object> {

    private static final Logger LOGGER = LoggerFactory.getLogger(WsdlEncoderv11.class);

    private static final Set<EncoderKey> ENCODER_KEYS = Collections.EMPTY_SET;
//            CodingHelper.encoderKeysForElements(WSDLCo.NS_WSA, WsaHeader.class);

    public WsdlEncoderv11() {
        LOGGER.debug("Encoder for the following keys initialized successfully: {}!",
                Joiner.on(", ").join(ENCODER_KEYS));
    }

    @Override
    public Set<EncoderKey> getKeys() {
        return Collections.unmodifiableSet(ENCODER_KEYS);
    }

    @Override
    public MediaType getContentType() {
        return MediaTypes.APPLICATION_XML;
    }

    @Override
    public XmlObject encode(Object objectToEncode, EncodingContext additionalValues) throws EncodingException {
        DefinitionsDocument doc = DefinitionsDocument.Factory.newInstance(getXmlOptions());
        Definitions def = doc.addNewDefinitions();
        XmlObject addNewService = def.addNewService();
        // TODO Auto-generated method stub
        return null;
    }

}
