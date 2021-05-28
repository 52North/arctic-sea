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
package org.n52.shetland.rdf;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.n52.shetland.rdf.dct.Description;
import org.n52.shetland.rdf.dct.Title;

public abstract class AbstractTitleDescription<T extends AbstractTitleDescription<?>> {

    private List<Title> titles = new LinkedList<Title>();

    private List<Description> desctiptions = new LinkedList<Description>();

    public List<Title> getTitles() {
        return titles;
    }

    @SuppressWarnings("unchecked")
    public T setTitles(Collection<Title> titles) {
        this.getTitles().clear();
        if (titles != null) {
            this.getTitles().addAll(titles);
        }
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T addTitle(Title title) {
        if (title != null) {
            this.getTitles().add(title);
        }
        return (T) this;
    }

    public List<Description> getDesctiptions() {
        return desctiptions;
    }

    @SuppressWarnings("unchecked")
    public T setDesctiptions(Collection<Description> desctiptions) {
        this.getDesctiptions().clear();
        if (desctiptions != null) {
            this.getDesctiptions().addAll(desctiptions);
        }
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T addDesctiption(Description description) {
        if (description != null) {
            this.getDesctiptions().add(description);
        }
        return (T) this;
    }

    public void addTitleAndDescription(Model model, Resource parent) {
        for (Title title : getTitles()) {
            title.addToResource(model, parent);
        }

        for (Description description : getDesctiptions()) {
            description.addToResource(model, parent);
        }
    }

}
