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
package org.n52.shetland.ogc.wps.description.impl;

import org.n52.shetland.ogc.wps.description.DataDescription;
import org.n52.shetland.ogc.wps.description.ProcessDescriptionBuilderFactory;

/**
 * @author Christian Autermann
 */
public abstract class AbstractDataDescription extends AbstractDescription implements DataDescription {

    protected AbstractDataDescription(AbstractBuilder<?, ?> builder) {
        super(builder);
    }

    public abstract static class AbstractBuilder<
            T extends DataDescription,
            B extends AbstractBuilder<T, B>> extends AbstractDescription.AbstractBuilder<T, B>
            implements DataDescription.Builder<T, B> {
        protected AbstractBuilder(ProcessDescriptionBuilderFactory<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> factory,
                DataDescription entity) {
            super(factory, entity);
        }

        protected AbstractBuilder(ProcessDescriptionBuilderFactory<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> factory) {
            super(factory);
        }
    }
}
