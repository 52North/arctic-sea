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
package org.n52.shetland.inspire.base2;

import java.util.List;

import org.joda.time.DateTime;
import org.n52.shetland.ogc.gml.AbstractGML;
import org.n52.shetland.w3c.Nillable;

import com.google.common.collect.Lists;

public class DocumentCitation extends AbstractGML {

    private Nillable<DateTime> date;
    private List<Nillable<String>> links = Lists.newArrayList();

    /**
     * @return the date
     */
    public Nillable<DateTime> getDate() {
        return date;
    }

    /**
     * @param date the date to set
     * @return this {@link DocumentCitation}
     */
    public DocumentCitation setDate(Nillable<DateTime> date) {
        this.date = date;
        return this;
    }

    /**
     * @param date the date to set
     * @return this {@link DocumentCitation}
     */
    public DocumentCitation setDate(DateTime date) {
        setDate(Nillable.present(date));
        return this;
    }

    /**
     * @return the links
     */
    public List<Nillable<String>> getLinks() {
        return links;
    }

    /**
     * @param links the links to set
     * @return this {@link DocumentCitation}
     */
    public DocumentCitation setLinks(List<Nillable<String>> links) {
        getLinks().clear();
        getLinks().addAll(links);
        return this;
    }

    public DocumentCitation addLink(String link) {
        addLink(Nillable.present(link));
        return this;
    }

    public DocumentCitation addLink(Nillable<String> link) {
        getLinks().add(link);
        return this;
    }

    public boolean isSetDate() {
        return getDate() != null && getDate().isPresent();
    }

    public boolean isSetLinks() {
        return getLinks() != null && !getLinks().isEmpty();
    }

}
