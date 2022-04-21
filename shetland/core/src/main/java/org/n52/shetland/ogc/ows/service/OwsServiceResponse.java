/*
 * Copyright (C) 2015-2022 52°North Spatial Information Research GmbH
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
package org.n52.shetland.ogc.ows.service;

import java.util.Optional;

import org.n52.janmayen.http.MediaType;
import org.n52.shetland.ogc.ows.HasExtension;
import org.n52.shetland.ogc.ows.extension.Extensions;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * abstract super class for all service request classes
 *
 * @since 1.0.0
 */
public abstract class OwsServiceResponse extends OwsServiceCommunicationObject
        implements HasExtension<OwsServiceResponse>, AutoCloseable {

    private MediaType contentType;
    private Extensions extensions = new Extensions();

    public OwsServiceResponse() {
    }

    public OwsServiceResponse(String service, String version) {
        super(service, version);
    }

    public OwsServiceResponse(String service, String version, String operationName) {
        super(service, version, operationName);
    }

    @Override
    @SuppressFBWarnings({ "EI_EXPOSE_REP" })
    public Extensions getExtensions() {
        return this.extensions;
    }

    @Override
    public OwsServiceResponse setExtensions(Extensions extensions) {
        this.extensions = Optional.ofNullable(extensions).orElseGet(Extensions::new);
        return this;
    }

    public OwsServiceResponse setContentType(MediaType contentType) {
        this.contentType = contentType;
        return this;
    }

    public MediaType getContentType() {
        return this.contentType;
    }

    public boolean isSetContentType() {
        return getContentType() != null;
    }

    @Override
    public void close() {
    }
}
