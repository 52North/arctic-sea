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

import static java.util.stream.Collectors.joining;

import java.util.Collections;
import java.util.Set;

import org.apache.xmlbeans.XmlObject;
import org.n52.shetland.ogc.swe.SweAbstractDataComponent;
import org.n52.shetland.ogc.swe.SweConstants;
import org.n52.shetland.ogc.swes.SwesConstants;
import org.n52.shetland.ogc.swes.SwesExtension;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.exception.UnsupportedEncoderInputException;
import org.n52.svalbard.util.CodingHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">J&uuml;rrens, Eike Hinderk</a>
 *
 * @since 3.2.0
 */
public class SwesExtensionEncoderv20 extends AbstractXmlEncoder<XmlObject, SwesExtension<?>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SwesExtensionEncoderv20.class);

    private static final Set<EncoderKey> ENCODER_KEYS = CodingHelper.encoderKeysForElements(SwesConstants.NS_SWES_20,
            SwesExtension.class);

    public SwesExtensionEncoderv20() {
        LOGGER.debug("Encoder for the following keys initialized successfully: {}!",
                ENCODER_KEYS.stream().map(EncoderKey::toString).collect(joining(", ")));
    }

    @Override
    public XmlObject encode(SwesExtension<?> swesExtension, EncodingContext additionalValues)
            throws EncodingException {
        if (swesExtension == null) {
            throw new UnsupportedEncoderInputException(this, "null");
        }
        Object value = swesExtension.getValue();
        if (value == null) {
            throw new UnsupportedEncoderInputException(this, "null extension content");
        }
        if (!(value instanceof SweAbstractDataComponent)) {
            throw new UnsupportedEncoderInputException(this, value.getClass());
        }
        return encodeObjectToXml(
                SweConstants.NS_SWE_20,
                value,
                EncodingContext.of(XmlBeansEncodingFlags.PROPERTY_TYPE));
    }

    @Override
    public Set<EncoderKey> getKeys() {
        return Collections.unmodifiableSet(ENCODER_KEYS);
    }

}
