/*
 * Copyright 2015-2022 52°North Initiative for Geospatial Open Source
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

import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;

import org.n52.janmayen.Optionals;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class OwsKeyword implements Comparable<OwsKeyword> {
    private static final Comparator<OwsKeyword> COMPARATOR
            = Comparator.nullsLast(Comparator
                    .comparing(OwsKeyword::getType, Optionals.nullsLast())
                    .thenComparing(OwsKeyword::getKeyword, Comparator
                                   .comparing(OwsLanguageString::getLang, Optionals.nullsLast())
                                   .thenComparing(OwsLanguageString::getValue)));

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

    @Override
    public int compareTo(OwsKeyword o) {
        return COMPARATOR.compare(this, o);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 19 * hash + Objects.hashCode(this.keyword);
        hash = 19 * hash + Objects.hashCode(this.type);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final OwsKeyword other = (OwsKeyword) obj;
        if (!Objects.equals(this.keyword, other.keyword)) {
            return false;
        }
        if (!Objects.equals(this.type, other.type)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "OwsKeyword{" + "keyword=" + keyword + ", type=" + type + '}';
    }

}
