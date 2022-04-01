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

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;

import org.n52.shetland.util.CollectionHelper;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class OwsPhone {

    private final SortedSet<String> voice;
    private final SortedSet<String> facsimile;

    public OwsPhone(Set<String> voice, Set<String> facsimile) {
        this.voice = CollectionHelper.newSortedSet(voice);
        this.facsimile = CollectionHelper.newSortedSet(facsimile);
    }

    public OwsPhone(String voice, String facsimile) {
        this(toSet(voice), toSet(facsimile));
    }

    public SortedSet<String> getVoice() {
        return Collections.unmodifiableSortedSet(voice);
    }

    public SortedSet<String> getFacsimile() {
        return Collections.unmodifiableSortedSet(facsimile);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.voice);
        hash = 79 * hash + Objects.hashCode(this.facsimile);
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
        final OwsPhone other = (OwsPhone) obj;
        if (!Objects.equals(this.voice, other.voice)) {
            return false;
        }
        if (!Objects.equals(this.facsimile, other.facsimile)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "OwsPhone{" + "voice=" + voice + ", facsimile=" + facsimile + '}';
    }

    private static <T> Set<T> toSet(T t) {
        return Optional.ofNullable(t).map(Collections::singleton).orElseGet(Collections::emptySet);
    }



}
