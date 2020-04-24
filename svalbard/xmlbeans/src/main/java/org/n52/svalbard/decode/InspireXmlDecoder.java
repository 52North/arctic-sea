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
package org.n52.svalbard.decode;

import java.util.Collections;
import java.util.Set;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.impl.values.XmlAnyTypeImpl;
import org.n52.shetland.inspire.InspireConstants;
import org.n52.shetland.inspire.InspireSupportedCRS;
import org.n52.shetland.inspire.InspireSupportedLanguages;
import org.n52.shetland.inspire.dls.InspireExtendedCapabilities;
import org.n52.shetland.ogc.ows.extension.CapabilitiesExtension;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.util.CodingHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;

/**
 * XML decoder class for the INSPIRE schema
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public class InspireXmlDecoder extends AbstractXmlDecoder<XmlObject, Object> {

    private static final Logger LOGGER = LoggerFactory.getLogger(InspireXmlDecoder.class);

    private static final Set<DecoderKey> DECODER_KEYS = Sets.union(
            CodingHelper.decoderKeysForElements(InspireConstants.NS_INSPIRE_DLS, XmlAnyTypeImpl.class,
                    InspireExtendedCapabilities.class),
            CodingHelper.decoderKeysForElements(InspireConstants.NS_INSPIRE_COMMON, XmlAnyTypeImpl.class,
                    InspireSupportedLanguages.class, InspireSupportedCRS.class));

    public InspireXmlDecoder() {
        LOGGER.debug("Decoder for the following keys initialized successfully: {}!",
                Joiner.on(", ").join(DECODER_KEYS));
    }
    @Override
    public Set<DecoderKey> getKeys() {
        return Collections.unmodifiableSet(DECODER_KEYS);
    }

    @Override
    public Object decode(XmlObject objectToDecode) throws DecodingException {
        // TODO implement parsing
        return new CapabilitiesExtension<>();
    }

}
