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
package org.n52.shetland.ogc.ows;

import java.util.Optional;
import java.util.Set;

import org.n52.janmayen.i18n.MultilingualString;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public abstract class OwsBasicIdentification extends OwsDescription {

    private final Optional<OwsCode> identifier;

    public OwsBasicIdentification(OwsCode identifier, MultilingualString title,
                                  MultilingualString abstrakt,
                                  Set<OwsKeyword> keywords) {
        super(title, abstrakt, keywords);
        this.identifier = Optional.ofNullable(identifier);
    }

    public Optional<OwsCode> getIdentifier() {
        return identifier;
    }

}
