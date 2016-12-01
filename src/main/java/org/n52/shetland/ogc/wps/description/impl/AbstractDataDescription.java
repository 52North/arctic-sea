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
package org.n52.shetland.ogc.wps.description.impl;

import java.util.Set;

import org.n52.shetland.ogc.ows.OwsCode;
import org.n52.shetland.ogc.ows.OwsKeyword;
import org.n52.shetland.ogc.ows.OwsLanguageString;
import org.n52.shetland.ogc.ows.OwsMetadata;
import org.n52.shetland.ogc.wps.description.DataDescription;

/**
 *
 * @author Christian Autermann
 */
public abstract class AbstractDataDescription extends AbstractDescription
        implements DataDescription {

    public AbstractDataDescription(AbstractBuilder<?, ?> builder) {
        this(builder.getId(),
             builder.getTitle(),
             builder.getAbstract(),
             builder.getKeywords(),
             builder.getMetadata());
    }

    public AbstractDataDescription(OwsCode id,
                                   OwsLanguageString title,
                                   OwsLanguageString abstrakt,
                                   Set<OwsKeyword> keywords,
                                   Set<OwsMetadata> metadata) {
        super(id, title, abstrakt, keywords, metadata);
    }



    public static abstract class AbstractBuilder<T extends DataDescription, B extends DataDescription.Builder<T, B>>
            extends AbstractDescription.AbstractBuilder<T, B>
            implements DataDescription.Builder<T, B> {

    }
}
