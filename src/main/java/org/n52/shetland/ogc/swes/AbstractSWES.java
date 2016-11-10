/*
 * Copyright 2016 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.ogc.swes;

import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.ows.HasExtension;
import org.n52.shetland.ogc.ows.exception.InvalidParameterValueException;
import org.n52.shetland.ogc.ows.extension.Extension;
import org.n52.shetland.ogc.ows.extension.Extensions;

/**
 * SOS internal representation of AbstractSWES element
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 4.1.0
 *
 */
public abstract class AbstractSWES extends AbstractFeature implements HasExtension<AbstractSWES> {

    private static final long serialVersionUID = -7371500673994109819L;

    private Extensions extensions;

    public AbstractSWES() {
        super("");
    }

    @Override
    public Extensions getExtensions() {
        return extensions;
    }

    @Override
    public AbstractSWES setExtensions(final Extensions extensions) {
        this.extensions = extensions;
        return this;
    }

    @Override
    public AbstractSWES addExtensions(Extensions extensions) {
        if (getExtensions() == null) {
            setExtensions(extensions);
        } else {
            getExtensions().addExtension(extensions.getExtensions());
        }
        return this;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public AbstractSWES addExtension(Extension extension) {
        if (getExtensions() == null) {
            setExtensions(new Extensions());
        }
        getExtensions().addExtension(extension);
        return this;
    }

    @Override
    public boolean isSetExtensions() {
        return getExtensions() != null && !getExtensions().isEmpty();
    }

    @Override
    public boolean hasExtension(Enum identifier) {
        if (isSetExtensions()) {
            return getExtensions().containsExtension(identifier);
        }
        return false;
    }

    @Override
    public boolean hasExtension(String identifier) {
        if (isSetExtensions()) {
            return getExtensions().containsExtension(identifier);
        }
        return false;
    }

    @Override
    public Extension<?> getExtension(Enum identifier) throws InvalidParameterValueException {
        if (hasExtension(identifier)) {
            return getExtensions().getExtension(identifier);
        }
        return null;
    }

    @Override
    public Extension<?> getExtension(String identifier) throws InvalidParameterValueException {
        if (hasExtension(identifier)) {
            return getExtensions().getExtension(identifier);
        }
        return null;
    }

}
