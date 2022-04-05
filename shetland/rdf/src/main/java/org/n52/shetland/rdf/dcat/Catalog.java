/*
 * Copyright 2015-2022 52°North Spatial Information Research GmbH
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
package org.n52.shetland.rdf.dcat;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.vocabulary.DCAT;
import org.n52.shetland.rdf.AbstractTitleDescription;
import org.n52.shetland.rdf.RDFElement;
import org.n52.shetland.rdf.dct.Description;
import org.n52.shetland.rdf.dct.Issued;
import org.n52.shetland.rdf.dct.Language;
import org.n52.shetland.rdf.dct.License;
import org.n52.shetland.rdf.dct.Modified;
import org.n52.shetland.rdf.dct.Publisher;
import org.n52.shetland.rdf.dct.Title;
import org.n52.shetland.rdf.foaf.Homepage;

public class Catalog extends AbstractTitleDescription<Catalog> implements RDFElement, DcatRdfPrefix {

    /*
     * 1..1
     */
    private Publisher publisher;

    /*
     * 1..n
     */
    private List<Dataset> datasets = new LinkedList<Dataset>();

    /*
     * 0..1
     */
    private Homepage homepage;

    /*
     * 0..n
     */
    private List<Language> languages = new LinkedList<Language>();

    /*
     * 0..1
     */
    private License license;

    /*
     * 0..1
     */
    private Issued issued;

    /*
     * 0..1
     */
    private List<ThemeTaxonomy> themeTaxonomies = new LinkedList<ThemeTaxonomy>();

    /*
     * 0..1
     */
    private Modified modified;

    /*
     * title -> 1..n description -> 1..n
     */
    public Catalog(Title title, Description description, Publisher publisher, Dataset dataset) {
        addTitle(title);
        addDesctiption(description);
        setPublisher(publisher);
        addDataset(dataset);
    }

    public Catalog(Collection<Title> titles, Collection<Description> descriptions, Publisher publisher,
            Dataset dataset) {
        setTitles(titles);
        setDesctiptions(descriptions);
        setPublisher(publisher);
        addDataset(dataset);
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public Catalog setPublisher(Publisher publisher) {
        this.publisher = publisher;
        return this;
    }

    public List<Dataset> getDatasets() {
        return datasets;
    }

    public Catalog setDatasets(Collection<Dataset> datasets) {
        this.getDatasets().clear();
        if (datasets != null) {
            this.getDatasets().addAll(datasets);
        }
        return this;
    }

    public Catalog addDataset(Dataset datasets) {
        if (datasets != null) {
            this.getDatasets().add(datasets);
        }
        return this;
    }

    public List<Language> getLanguages() {
        return languages;
    }

    public Homepage getHomepage() {
        return homepage;
    }

    public Catalog setHomepage(Homepage homepage) {
        this.homepage = homepage;
        return this;
    }

    public Catalog setLanguages(Collection<Language> language) {
        this.getLanguages().clear();
        if (language != null) {
            this.getLanguages().addAll(language);
        }
        return this;
    }

    public Catalog addLanguage(Language language) {
        if (language != null) {
            this.getLanguages().add(language);
        }
        return this;
    }

    public Issued getIssued() {
        return issued;
    }

    public License getLicense() {
        return license;
    }

    public Catalog setLicense(License license) {
        this.license = license;
        return this;
    }

    public Catalog setIssued(Issued issued) {
        this.issued = issued;
        return this;
    }

    public List<ThemeTaxonomy> getThemeTaxonomies() {
        return themeTaxonomies;
    }

    public Catalog setThemeTaxonomies(Collection<ThemeTaxonomy> themeTaxonomies) {
        this.getThemeTaxonomies().clear();
        if (themeTaxonomies != null) {
            this.getThemeTaxonomies().addAll(themeTaxonomies);
        }
        return this;
    }

    public Catalog addThemeTaxonomy(ThemeTaxonomy themeTaxonomies) {
        if (themeTaxonomies != null) {
            this.getThemeTaxonomies().add(themeTaxonomies);
        }
        return this;
    }

    public Modified getModified() {
        return modified;
    }

    public Catalog setModified(Modified modified) {
        this.modified = modified;
        return this;
    }

    @Override
    public Model addToModel(Model model) {
        addNsPrefix(model);
        Resource catalog = model.createResource(DCAT.Catalog);

        addTitleAndDescription(model, catalog);

        if (getPublisher() != null) {
            getPublisher().addToResource(model, catalog);
        }

        for (Dataset dataset : getDatasets()) {
            dataset.addToResource(model, catalog);
        }

        for (Language language : getLanguages()) {
            language.addToResource(model, catalog);
        }

        if (getHomepage() != null) {
            getHomepage().addToResource(model, catalog);
        }

        if (getIssued() != null) {
            getIssued().addToResource(model, catalog);
        }

        for (ThemeTaxonomy themeTaxonomy : getThemeTaxonomies()) {
            themeTaxonomy.addToResource(model, catalog);
        }

        if (getModified() != null) {
            getModified().addToResource(model, catalog);
        }
        return model;
    }
}
