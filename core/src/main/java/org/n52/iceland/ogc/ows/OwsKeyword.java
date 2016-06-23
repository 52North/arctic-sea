/*
 * Copyright 2015-2016 52°North Initiative for Geospatial Open Source
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
package org.n52.iceland.ogc.ows;

import java.util.Optional;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class OwsKeyword {

    private final OwsLanguageString keyword;
    private final Optional<OwsCode> type;

    public OwsKeyword(OwsLanguageString keyword, OwsCode type) {
        this.keyword = keyword;
        this.type = Optional.ofNullable(type);
    }

    public OwsKeyword(OwsLanguageString keyword) {
        this(keyword, null);
    }

    public OwsKeyword(String keyword) {
        this(keyword, null);
    }

    public OwsKeyword(String keyword, OwsCode type) {
        this(new OwsLanguageString(keyword), type);
    }

    public OwsLanguageString getKeyword() {
        return keyword;
    }

    public Optional<OwsCode> getType() {
        return type;
    }
}
