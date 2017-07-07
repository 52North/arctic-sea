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

import java.util.Collections;
import java.util.Set;

import org.n52.shetland.inspire.base.Identifier;
import org.n52.shetland.inspire.base.InspireBaseConstants;
import org.n52.svalbard.encode.exception.EncodingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;

import eu.europa.ec.inspire.schemas.base.x33.IdentifierPropertyType;

public class IdentifierPropertyTypeEncoder extends AbstractIdentifierEncoder<IdentifierPropertyType> {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(IdentifierPropertyTypeEncoder.class);

    private static final Set<EncoderKey> ENCODER_KEYS = Sets.newHashSet(
            new ClassToClassEncoderKey(Identifier.class, IdentifierPropertyType.class),
            new XmlPropertyTypeEncoderKey(InspireBaseConstants.NS_BASE, Identifier.class));

    public IdentifierPropertyTypeEncoder() {
        LOGGER.debug("Encoder for the following keys initialized successfully: {}!",
                Joiner.on(", ").join(ENCODER_KEYS));
    }

    @Override
    public Set<EncoderKey> getKeys() {
        return Collections.unmodifiableSet(ENCODER_KEYS);
    }

    @Override
    public IdentifierPropertyType encode(Identifier identifier, EncodingContext context)
            throws EncodingException {
        IdentifierPropertyType ipt = IdentifierPropertyType.Factory.newInstance();
        ipt.setIdentifier(createIdentifierType(identifier));
        return ipt;
    }

}
