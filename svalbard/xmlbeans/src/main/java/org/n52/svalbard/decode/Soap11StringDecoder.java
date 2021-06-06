/*
 * Copyright 2015-2021 52°North Initiative for Geospatial Open Source
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

import org.n52.shetland.w3c.soap.SoapConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;

/**
 * SOAP 1.1 {@link Decoder} for {@link String} XML representation
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public class Soap11StringDecoder extends AbstractSoapStringDecoder {

    private static final Logger LOGGER = LoggerFactory.getLogger(Soap11StringDecoder.class);

    public Soap11StringDecoder() {
        super(SoapConstants.NS_SOAP_11);
        LOGGER.debug("Decoder for the following keys initialized successfully: {}!", Joiner.on(", ").join(getKeys()));
    }

}
