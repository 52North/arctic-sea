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

import javax.inject.Inject;

import org.n52.janmayen.http.MediaTypes;
import org.n52.janmayen.lifecycle.Constructable;
import org.n52.shetland.exi.EXIObject;
import org.n52.shetland.ogc.ows.service.OwsOperationKey;
import org.n52.shetland.ogc.ows.service.OwsServiceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract response encoder class for {@link EXIObject}
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 */
public class ExiResponseEncoder
        extends ExiEncoder<OwsServiceResponse>
        implements Constructable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExiResponseEncoder.class);
    private EncoderKey key;
    private String version;
    private String service;
    private String operation;

    @Inject
    public void setVersion(String version) {
        this.version = version;
    }

    @Inject
    public void setService(String service) {
        this.service = service;
    }

    @Inject
    public void setOperation(String operation) {
        this.operation = operation;
    }

    @Override
    public void init() {
        this.key = new OperationResponseEncoderKey(this.service, this.version, this.operation,
                MediaTypes.APPLICATION_EXI);
        LOGGER.debug("Encoder for the following keys initialized successfully: {}!", this.key);
    }

    @Override
    public Set<EncoderKey> getKeys() {
        return Collections.singleton(this.key);
    }

    @Override
    protected EncoderKey getKey(OwsServiceResponse response) {
        return new OperationResponseEncoderKey(new OwsOperationKey(response), getEncodedContentType(response));
    }

    @Override
    public String toString() {
        return String.format("%s{key=%s}", ExiResponseEncoder.class.getName(), key);
    }

}
