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
package org.n52.svalbard;

import java.util.Collections;
import java.util.Set;

import org.apache.xmlbeans.XmlObject;
import org.n52.shetland.w3c.SchemaLocation;
import org.n52.shetland.w3c.W3CConstants;
import org.n52.shetland.w3c.xlink.W3CHrefAttribute;
import org.n52.svalbard.encode.AbstractXmlEncoder;
import org.n52.svalbard.encode.EncoderKey;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.exception.UnsupportedEncoderInputException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3.x1999.xlink.HrefAttribute;

import com.google.common.base.Joiner;

public class XlinkEncoderv1999 extends AbstractXmlEncoder<XmlObject, Object> {
    private static final Logger LOGGER = LoggerFactory.getLogger(XlinkEncoderv1999.class);

    private static final Set<EncoderKey> ENCODER_KEYS = CodingHelper.encoderKeysForElements(W3CConstants.NS_XLINK,
            W3CHrefAttribute.class);

    public XlinkEncoderv1999() {
        LOGGER.debug("Encoder for the following keys initialized successfully: {}!", Joiner.on(", ")
                .join(ENCODER_KEYS));
    }


    @Override
    public Set<EncoderKey> getKeys() {
        return Collections.unmodifiableSet(ENCODER_KEYS);
    }

    @Override
    public XmlObject encode(Object element, EncodingContext additionalValues)
            throws EncodingException {
        XmlObject encodedObject = null;
        if (element instanceof W3CHrefAttribute) {
            encodedObject = encodeHrefAttribute((W3CHrefAttribute) element);
        } else {
            throw new UnsupportedEncoderInputException(this, element);
        }
        return encodedObject;
    }

    private XmlObject encodeHrefAttribute(W3CHrefAttribute hrefAttribute) {
        HrefAttribute xmlHrefAttribute = HrefAttribute.Factory.newInstance(getXmlOptions());
        xmlHrefAttribute.setHref(hrefAttribute.getHref());
        return xmlHrefAttribute;
    }

    @Override
    public Set<SchemaLocation> getSchemaLocations() {
        return Collections.singleton(W3CConstants.XLINK_SCHEMA_LOCATION);
    }

}
