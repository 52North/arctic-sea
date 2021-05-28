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

import java.util.Collections;
import java.util.Set;

import org.apache.xmlbeans.XmlObject;

import org.n52.shetland.w3c.W3CConstants;
import org.n52.shetland.w3c.xlink.W3CHrefAttribute;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.decode.exception.UnsupportedDecoderXmlInputException;
import org.n52.svalbard.util.CodingHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3.x1999.xlink.HrefAttribute;

public class XlinkDecoderv1999 implements Decoder<Object, XmlObject> {

    private static final Logger LOGGER = LoggerFactory.getLogger(XlinkDecoderv1999.class);

    private static final Set<DecoderKey> DECODER_KEYS =
            CodingHelper.decoderKeysForElements(W3CConstants.NS_XLINK, HrefAttribute.class);

    public XlinkDecoderv1999() {
        StringBuilder builder = new StringBuilder();
        for (DecoderKey decoderKeyType : DECODER_KEYS) {
            builder.append(decoderKeyType.toString());
            builder.append(", ");
        }
        builder.delete(builder.lastIndexOf(", "), builder.length());
        LOGGER.debug("Decoder for the following keys initialized successfully: " + builder.toString() + "!");
    }

    @Override
    public Set<DecoderKey> getKeys() {
        return Collections.unmodifiableSet(DECODER_KEYS);
    }

    @Override
    public Object decode(XmlObject xmlObject) throws DecodingException {
        if (xmlObject instanceof HrefAttribute) {
            return encodeHrefAttribute((HrefAttribute) xmlObject);
        } else {
            throw new UnsupportedDecoderXmlInputException(this, xmlObject);
        }
    }

    private W3CHrefAttribute encodeHrefAttribute(HrefAttribute xmlObject) {
        W3CHrefAttribute href = new W3CHrefAttribute();
        if (xmlObject.isSetHref()) {
            href.setHref(xmlObject.getHref());
        }
        return href;
    }

}
