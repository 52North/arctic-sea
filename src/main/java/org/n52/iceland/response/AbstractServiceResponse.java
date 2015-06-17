/*
 * Copyright 2015 52°North Initiative for Geospatial Open Source
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
package org.n52.iceland.response;

import org.n52.iceland.ogc.ows.Extension;
import org.n52.iceland.ogc.ows.Extensions;
import org.n52.iceland.ogc.ows.OWSConstants.HasExtension;
import org.n52.iceland.service.AbstractServiceCommunicationObject;
import org.n52.iceland.util.http.MediaType;

/**
 * abstract super class for all service request classes
 *
 * @since 1.0.0
 */
public abstract class AbstractServiceResponse
        extends AbstractServiceCommunicationObject
        implements HasExtension<AbstractServiceResponse> {

    private MediaType contentType;

    private Extensions extensions;

    @Override
    public Extensions getExtensions() {
        return extensions;
    }

    @Override
    public AbstractServiceResponse setExtensions(final Extensions extensions) {
        this.extensions = extensions;
        return this;
    }

    @Override
    public AbstractServiceResponse addExtensions(final Extensions extensions) {
        if (getExtensions() == null) {
            setExtensions(extensions);
        } else {
            getExtensions().addExtension(extensions.getExtensions());
        }
        return this;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public AbstractServiceResponse addExtension(final Extension extension) {
        if (getExtensions() == null) {
            setExtensions(new Extensions());
        }
        getExtensions().addExtension(extension);
        return this;
    }

    @Override
    public boolean isSetExtensions() {
        return extensions != null && !extensions.isEmpty();
    }

    public AbstractServiceResponse setContentType(MediaType contentType) {
        this.contentType = contentType;
        return this;
    }

    public MediaType getContentType() {
        return this.contentType;
    }

    public boolean isSetContentType() {
        return getContentType() != null;
    }

}
