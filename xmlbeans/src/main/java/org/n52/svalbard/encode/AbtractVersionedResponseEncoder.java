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

import org.n52.janmayen.http.MediaTypes;
import org.n52.shetland.ogc.ows.service.OwsOperationKey;
import org.n52.shetland.ogc.ows.service.OwsServiceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;

public abstract class AbtractVersionedResponseEncoder<T extends OwsServiceResponse>
        extends AbstractResponseEncoder<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbtractVersionedResponseEncoder.class);

    private final Set<EncoderKey> encoderKeys;

    /**
     * constructor
     *
     * @param service
     *            Service
     * @param version
     *            Service version
     * @param operation
     *            Service operation name
     * @param namespace
     *            Service XML schema namespace
     * @param prefix
     *            Service XML schema prefix
     * @param responseType
     *            Response type
     * @param validationEnabled
     *            Indicator if the created/encoded object can be validated
     */
    public AbtractVersionedResponseEncoder(
            String service, String version, String operation, String namespace, String prefix, Class<T> responseType,
            boolean validationEnabled, String operationVersion) {
        super(service, version, operationVersion, namespace, prefix, responseType, validationEnabled);
        OwsOperationKey key = new OwsOperationKey(service, version, operation);
        this.encoderKeys = Sets.newHashSet(new XmlEncoderKey(namespace, responseType),
                new VersionedOperationEncoderKey(key, MediaTypes.TEXT_XML, operationVersion),
                new VersionedOperationEncoderKey(key, MediaTypes.APPLICATION_XML, operationVersion));
        LOGGER.debug("Encoder for the following keys initialized successfully: {}!",
                Joiner.on(", ").join(encoderKeys));
    }

    /**
     * constructor
     *
     * @param service
     *            Service
     * @param version
     *            Service version
     * @param operation
     *            Service operation name
     * @param namespace
     *            Service XML schema namespace
     * @param prefix
     *            Service XML schema prefix
     * @param responseType
     *            Response type
     */
    public AbtractVersionedResponseEncoder(
            String service, String version, String operation, String namespace, String prefix, Class<T> responseType,
            String operationVersion) {
        this(service, version, operation, namespace, prefix, responseType, true, operationVersion);
    }

    @Override
    public Set<EncoderKey> getKeys() {
        return Collections.unmodifiableSet(encoderKeys);
    }

}
