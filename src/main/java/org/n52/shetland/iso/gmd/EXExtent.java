/*
 * Copyright 2016-2017 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.iso.gmd;

import java.util.ArrayList;
import java.util.List;

import org.n52.shetland.util.CollectionHelper;
import org.n52.shetland.w3c.xlink.Referenceable;

import com.google.common.base.Strings;

/**
 * Internal representation of the ISO GMD ExExtent.
 *
 * @author Carsten Hollmann <c.hollmann@52north.org>
 * @since 4.4.0
 *
 */
public class EXExtent extends AbstractObject {

    private String description;
//    private List<Object> exGeographicalExtent = new ArrayList<>();
//    private List<Object> exTemporalExtent = new ArrayList<>();
    private List<Referenceable<EXVerticalExtent>> exVerticalExtent = new ArrayList<>();

    public String getDescription() {
        return description;
    }

    public EXExtent setDescription(String description) {
        this.description = description;
        return this;
    }

    public boolean hasDescription() {
        return !Strings.isNullOrEmpty(getDescription());
    }

//    public List<Object> getExGeographicalExtent() {
//        return exGeographicalExtent;
//    }
//
//    public EXExtent setExGeographicalExtent(List<Object> exGeographicalExtent) {
//        this.exGeographicalExtent.clear();
//        if (!CollectionHelper.nullEmptyOrContainsOnlyNulls(exGeographicalExtent)) {
//            this.exGeographicalExtent.addAll(exGeographicalExtent);
//        }
//        return this;
//    }
//
//    public EXExtent addExGeographicalExtent(List<Object> exGeographicalExtent) {
//        if (!CollectionHelper.nullEmptyOrContainsOnlyNulls(exGeographicalExtent)) {
//            this.exGeographicalExtent.addAll(exGeographicalExtent);
//        }
//        return this;
//    }
//
//    public EXExtent addExGeographicalExtent(Object exGeographicalExtent) {
//        if (exGeographicalExtent != null) {
//            this.exGeographicalExtent.add(exGeographicalExtent);
//        }
//        return this;
//    }
//
//    public boolean hasGeographicalExtent() {
//        return getExGeographicalExtent() != null && !getExGeographicalExtent().isEmpty();
//    }
//
//    public List<Object> getExTemporalExtent() {
//        return exTemporalExtent;
//    }
//
//    public EXExtent setExTemporalExtent(List<Object> exTemporalExtent) {
//        this.exTemporalExtent.clear();
//        if (!CollectionHelper.nullEmptyOrContainsOnlyNulls(exTemporalExtent)) {
//            this.exTemporalExtent.addAll(exTemporalExtent);
//        }
//        return this;
//    }
//
//    public EXExtent addExTemporalExtent(List<Object> exTemporalExtent) {
//        if (!CollectionHelper.nullEmptyOrContainsOnlyNulls(exTemporalExtent)) {
//            this.exTemporalExtent.addAll(exTemporalExtent);
//        }
//        return this;
//    }
//
//    public EXExtent addExTemporalExtent(Object exTemporalExtent) {
//        if (exTemporalExtent != null) {
//            this.exTemporalExtent.add(exTemporalExtent);
//        }
//        return this;
//    }

    public List<Referenceable<EXVerticalExtent>> getExVerticalExtent() {
        return exVerticalExtent;
    }

    public EXExtent setVerticalExtent(List<Referenceable<EXVerticalExtent>> exVerticalExtent) {
        this.exVerticalExtent.clear();
        if (!CollectionHelper.nullEmptyOrContainsOnlyNulls(exVerticalExtent)) {
            this.exVerticalExtent.addAll(exVerticalExtent);
        }
        return this;
    }

    public EXExtent addVerticalExtent(List<Referenceable<EXVerticalExtent>> exVerticalExtent) {
        if (!CollectionHelper.nullEmptyOrContainsOnlyNulls(exVerticalExtent)) {
            this.exVerticalExtent.addAll(exVerticalExtent);
        }
        return this;
    }

    public EXExtent addVerticalExtent(Referenceable<EXVerticalExtent> exVerticalExtent) {
        if (exVerticalExtent != null) {
            this.exVerticalExtent.add(exVerticalExtent);
        }
        return this;
    }

    public EXExtent addVerticalExtent(EXVerticalExtent exVerticalExtent) {
        if (exVerticalExtent != null) {
            this.exVerticalExtent.add(Referenceable.of(exVerticalExtent));
        }
        return this;
    }

    public boolean hasVerticalExtent() {
        return getExVerticalExtent() != null && !getExVerticalExtent().isEmpty();
    }

}
