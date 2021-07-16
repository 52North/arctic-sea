/*
 * Copyright (C) 2015-2021 52°North Spatial Information Research GmbH
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
package org.n52.shetland.ogc.swe.simpleType;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.n52.shetland.ogc.gml.ReferenceType;

public class SweQualityHolder {

    private List<SweQuality> quality = new LinkedList<>();

    private Map<String, ReferenceType> references = new LinkedHashMap<>();

    public List<SweQuality> getQuality() {
        return quality;
    }

    public SweQualityHolder addQuality(SweQuality quality) {
        if (quality != null) {
            this.quality.add(quality);
        }
        return this;
    }

    public SweQualityHolder setQuality(Collection<SweQuality> quality) {
        this.quality.clear();
        if (quality != null) {
            this.quality.addAll(quality);
        }
        return this;
    }

    public boolean isSetQuality() {
        return getQuality() != null && !getQuality().isEmpty();
    }

    public void addReference(String key, ReferenceType reference) {
        if (reference != null) {
            this.references.put(key, reference);
        }
    }

    public void setReferences(Map<String, ReferenceType> references) {
        this.references.clear();
        if (references != null) {
            this.references.putAll(references);
        }
    }

    public Map<String, ReferenceType> getReferences() {
        return references;
    }

    public boolean isSetReferences() {
        return getReferences() != null && !getReferences().isEmpty();
    }

    public SweQualityHolder copy() {
        SweQualityHolder holder = new SweQualityHolder();
        if (isSetQuality()) {
            holder.setQuality(getQuality());
        }
        if (isSetReferences()) {
            holder.setReferences(getReferences());
        }
        return holder;
    }

    public boolean isEmpty() {
        return !isSetQuality();
    }

}
