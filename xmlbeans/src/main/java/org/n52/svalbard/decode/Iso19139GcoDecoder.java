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

import java.util.Collections;
import java.util.Set;

import org.apache.xmlbeans.XmlObject;
import org.isotc211.x2005.gco.CodeListValueType;

import org.n52.shetland.iso.GcoConstants;
import org.n52.shetland.ogc.sensorML.Role;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.decode.exception.UnsupportedDecoderXmlInputException;
import org.n52.svalbard.util.CodingHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;

/**
 * {@link Decoder} class to decode ISO TC211 Geographic COmmon (GCO) extensible
 * markup language.
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public class Iso19139GcoDecoder implements Decoder<Object, XmlObject> {

    private static final Logger LOGGER = LoggerFactory.getLogger(Iso19139GcoDecoder.class);

    private static final Set<DecoderKey> DECODER_KEYS =
            CodingHelper.decoderKeysForElements(GcoConstants.NS_GCO, CodeListValueType.class);

    public Iso19139GcoDecoder() {
        LOGGER.debug("Decoder for the following keys initialized successfully: {}!",
                Joiner.on(", ").join(DECODER_KEYS));
    }

    @Override
    public Set<DecoderKey> getKeys() {
        return Collections.unmodifiableSet(DECODER_KEYS);
    }

    @Override
    public Object decode(XmlObject element) throws DecodingException {
        if (element instanceof CodeListValueType) {
            return encodeCodeListValue((CodeListValueType) element);
        } else {
            throw new UnsupportedDecoderXmlInputException(this, element);
        }
    }

    private Role encodeCodeListValue(CodeListValueType circ) {
        Role role = new Role(circ.getStringValue());
        role.setCodeList(circ.getCodeList());
        role.setCodeListValue(circ.getCodeListValue());
        return role;
    }

}
