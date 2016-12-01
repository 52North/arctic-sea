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
package org.n52.shetland.ogc.wps.data;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import org.n52.shetland.ogc.ows.OwsCode;

import com.google.common.base.MoreObjects;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class GroupProcessData extends ProcessData implements Iterable<ProcessData> {

    private final List<ProcessData> elements;

    public GroupProcessData() {
        this(null, null);
    }

    public GroupProcessData(OwsCode id) {
        this(id, null);
    }

    public GroupProcessData(OwsCode id, List<ProcessData> elements) {
        super(id);
        this.elements = elements == null ? new LinkedList<>() : elements;
    }

    public List<ProcessData> getElements() {
        return Collections.unmodifiableList(this.elements);
    }

    public void setElements(Collection<ProcessData> elements) {
        this.elements.clear();
        if (elements != null) {
            this.elements.addAll(elements);
        }
    }

    public void addElement(ProcessData input) {
        this.elements.add(Objects.requireNonNull(input));
    }

    @Override
    public boolean isGroup() {
        return true;
    }

    @Override
    public GroupProcessData asGroup() {
        return this;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getElements());
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
        final GroupProcessData other = (GroupProcessData) obj;
        return Objects.equals(getId(), other.getId()) &&
                Objects.equals(getElements(), other.getElements());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("id", getId())
                .add("elements", getElements())
                .toString();
    }

    @Override
    public Iterator<ProcessData> iterator() {
        return this.elements.iterator();
    }

    public Stream<ProcessData> stream() {
        return this.elements.stream();
    }



}
