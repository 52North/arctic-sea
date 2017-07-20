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

import java.io.OutputStream;
import java.util.Collections;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.janmayen.http.MediaTypes;
import org.n52.shetland.ogc.ows.service.OwsOperationKey;
import org.n52.shetland.ogc.ows.service.OwsServiceResponse;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.exception.UnsupportedEncoderInputException;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;

/**
 * TODO JavaDoc
 *
 * @param <T> the response type
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
public abstract class AbstractResponseEncoder<T extends OwsServiceResponse> extends AbstractXmlResponseEncoder<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractResponseEncoder.class);

    private final Set<EncoderKey> encoderKeys;

    /**
     * constructor
     *
     * @param service      Service
     * @param version      Service version
     * @param operation    Service operation name
     * @param namespace    Service XML schema namespace
     * @param prefix       Service XML schema prefix
     * @param responseType Response type
     * @param validate     Indicator if the created/encoded object should be validated
     */
    public AbstractResponseEncoder(String service, String version, String operation, String namespace, String prefix,
                                   Class<T> responseType, boolean validate) {
        super(service, version, operation, namespace, prefix, responseType, validate);
        OwsOperationKey key = new OwsOperationKey(service, version, operation);
        this.encoderKeys = Sets.newHashSet(new XmlEncoderKey(namespace, responseType),
                                           new OperationResponseEncoderKey(key, MediaTypes.TEXT_XML),
                                           new OperationResponseEncoderKey(key, MediaTypes.APPLICATION_XML));
        LOGGER.debug("Encoder for the following keys initialized successfully: {}!",
                     Joiner.on(", ").join(encoderKeys));
    }

    /**
     * constructor
     *
     * @param service      Service
     * @param version      Service version
     * @param operation    Service operation name
     * @param namespace    Service XML schema namespace
     * @param prefix       Service XML schema prefix
     * @param responseType Response type
     */
    public AbstractResponseEncoder(String service, String version, String operation, String namespace, String prefix,
                                   Class<T> responseType) {
        this(service, version, operation, namespace, prefix, responseType, false);
    }

    @Override
    public Set<EncoderKey> getKeys() {
        return Collections.unmodifiableSet(encoderKeys);
    }

    @Override
    public void encode(T response, OutputStream outputStream, EncodingContext context) throws EncodingException {
        if (response == null) {
            throw new UnsupportedEncoderInputException(this);
        }
        create(response, outputStream, context);
    }

}
