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

import org.n52.shetland.w3c.soap.SoapConstants;
import org.n52.shetland.w3c.soap.SoapRequest;
import org.n52.svalbard.decode.exception.DecodingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;

/**
 * Abstract SOAP {@link Decoder} for {@link String} XML representation
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public abstract class AbstractSoapStringDecoder extends AbstractXmlDecoder<String, SoapRequest> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractSoapStringDecoder.class);

    private final Set<DecoderKey> decoderKeys;

    public AbstractSoapStringDecoder(String namespace) {
        this.decoderKeys = Collections.<DecoderKey> singleton(
                new XmlNamespaceOperationDecoderKey(namespace, SoapConstants.EN_SOAP_ENVELOPE));
        LOGGER.debug("Decoder for the following keys initialized successfully: {}!", Joiner.on(", ").join(getKeys()));
    }

    @Override
    public Set<DecoderKey> getKeys() {
        return Collections.unmodifiableSet(decoderKeys);
    }

    @Override
    public SoapRequest decode(String xmlString) throws DecodingException {
        return (SoapRequest) decodeXmlObject(xmlString);
    }
}
