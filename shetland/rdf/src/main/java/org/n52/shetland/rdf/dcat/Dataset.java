/*
 * Copyright 2015-2020 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.rdf.dcat;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.jena.rdf.model.Resource;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.vocabulary.DCAT;
import org.n52.shetland.rdf.AbstractTitleDescription;
import org.n52.shetland.rdf.ResourceAdder;
import org.n52.shetland.rdf.dct.AccessRights;
import org.n52.shetland.rdf.dct.AccrualPeriodicity;
import org.n52.shetland.rdf.dct.Description;
import org.n52.shetland.rdf.dct.Identifier;
import org.n52.shetland.rdf.dct.Issued;
import org.n52.shetland.rdf.dct.Language;
import org.n52.shetland.rdf.dct.License;
import org.n52.shetland.rdf.dct.Modified;
import org.n52.shetland.rdf.dct.Publisher;
import org.n52.shetland.rdf.dct.Spatial;
import org.n52.shetland.rdf.dct.Theme;
import org.n52.shetland.rdf.dct.Title;

public class Dataset extends AbstractTitleDescription<Dataset> implements ResourceAdder, DcatRdfPrefix {

    /*
     * 0..n
     */
    private List<Identifier> identifiers = new LinkedList<Identifier>();

    /*
     * 0..n
     */
    private List<Keyword> keywords = new LinkedList<Keyword>();

    /*
     * 0..1
     */
    private AccrualPeriodicity accrualPeriodicity;

    /*
     * 0..n
     */
    private List<Theme> themes = new LinkedList<Theme>();

    /*
     * 0..1
     */
    private Publisher publisher;

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
     * 0..1
     */
    private List<ContactPoint> contactPoints = new LinkedList<ContactPoint>();

    /*
     * 0..1
     */
    private AccessRights accessRights;

    /*
     * 0..n
     */
    private List<LandingPage> landingPages = new LinkedList<LandingPage>();

    /*
     * 0..n
     */
    private List<DistributionProperty> distributions = new LinkedList<DistributionProperty>();

    private List<Spatial> spatials = new LinkedList<Spatial>();

    /*
     * 0..n
     */
    // private List<Locations> spatials = new LinkedList<Locations>();

    /*
     * title -> 1..n description -> 1..n
     */
    public Dataset(Title title, Description description) {
        addTitle(title);
        addDesctiption(description);
    }

    public Dataset(Collection<Title> titles, Collection<Description> descriptions) {
        setTitles(titles);
        setDesctiptions(descriptions);
    }

    public List<Identifier> getIdentifiers() {
        return identifiers;
    }

    public Dataset setIdentifiers(Collection<Identifier> identifiers) {
        this.getIdentifiers().clear();
        if (identifiers != null) {
            this.getIdentifiers().addAll(identifiers);
        }
        return this;
    }

    public Dataset addIdentifier(Identifier identifier) {
        if (identifier != null) {
            this.getIdentifiers().add(identifier);
        }
        return this;
    }

    public List<Keyword> getKeywords() {
        return keywords;
    }

    public Dataset setKeywords(Collection<Keyword> keywords) {
        this.getKeywords().clear();
        if (keywords != null) {
            this.getKeywords().addAll(keywords);
        }
        return this;
    }

    public Dataset addKeyword(Keyword keyword) {
        if (keyword != null) {
            this.getKeywords().add(keyword);
        }
        return this;
    }

    public AccrualPeriodicity getAccrualPeriodicity() {
        return accrualPeriodicity;
    }

    public Dataset setAccrualPeriodicity(AccrualPeriodicity accrualPeriodicity) {
        this.accrualPeriodicity = accrualPeriodicity;
        return this;
    }

    public List<Theme> getThemes() {
        return themes;
    }

    public Dataset setThemes(Collection<Theme> themes) {
        this.getThemes().clear();
        if (themes != null) {
            this.getThemes().addAll(themes);
        }
        return this;
    }

    public Dataset addTheme(Theme theme) {
        if (theme != null) {
            this.getThemes().add(theme);
        }
        return this;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public Dataset setPublisher(Publisher publisher) {
        this.publisher = publisher;
        return this;
    }

    public List<Language> getLanguages() {
        return languages;
    }

    public Dataset setLanguages(Collection<Language> languages) {
        this.getLanguages().clear();
        if (languages != null) {
            this.getLanguages().addAll(languages);
        }
        return this;
    }

    public Dataset addLanguage(Language language) {
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

    public Dataset setLicense(License license) {
        this.license = license;
        return this;
    }

    public Dataset setIssued(Issued issued) {
        this.issued = issued;
        return this;
    }

    public List<ThemeTaxonomy> getThemeTaxonomies() {
        return themeTaxonomies;
    }

    public Dataset setThemeTaxonomies(Collection<ThemeTaxonomy> themeTaxonomies) {
        this.getThemeTaxonomies().clear();
        if (themeTaxonomies != null) {
            this.getThemeTaxonomies().addAll(themeTaxonomies);
        }
        return this;
    }

    public Dataset addThemeTaxonomy(ThemeTaxonomy themeTaxonomy) {
        if (themeTaxonomy != null) {
            this.getThemeTaxonomies().add(themeTaxonomy);
        }
        return this;
    }

    public Modified getModified() {
        return modified;
    }

    public Dataset setModified(Modified modified) {
        this.modified = modified;
        return this;
    }

    public List<ContactPoint> getContactPoints() {
        return contactPoints;
    }

    public Dataset setContactPoints(Collection<ContactPoint> contactPoints) {
        this.getContactPoints().clear();
        if (contactPoints != null) {
            this.getContactPoints().addAll(contactPoints);
        }
        return this;
    }

    public Dataset addContactPoint(ContactPoint contactPoint) {
        if (contactPoint != null) {
            this.getContactPoints().add(contactPoint);
        }
        return this;
    }

    public AccessRights getAccessRights() {
        return accessRights;
    }

    public Dataset setAccessRights(AccessRights accessRights) {
        this.accessRights = accessRights;
        return this;
    }

    public List<LandingPage> getLandingPages() {
        return landingPages;
    }

    public Dataset setLandingPages(Collection<LandingPage> landingPages) {
        this.getLandingPages().clear();
        if (landingPages != null) {
            this.getLandingPages().addAll(landingPages);
        }
        return this;
    }

    public List<DistributionProperty> getDistributions() {
        return distributions;
    }

    public Dataset setDistributions(Collection<DistributionProperty> distributions) {
        this.getDistributions().clear();
        if (distributions != null) {
            this.getDistributions().addAll(distributions);
        }
        return this;
    }

    public Dataset addDistribution(DistributionProperty distribution) {
        if (distribution != null) {
            this.getDistributions().add(distribution);
        }
        return this;
    }

    public Dataset addLandingPage(LandingPage landingPage) {
        if (landingPage != null) {
            this.getLandingPages().add(landingPage);
        }
        return this;
    }

    public List<Spatial> getSpatials() {
        return spatials;
    }

    public Dataset setSpatials(Collection<Spatial> spatials) {
        this.getSpatials().clear();
        if (spatials != null) {
            this.getSpatials().addAll(spatials);
        }
        return this;
    }

    public Dataset addSpatial(Spatial spatial) {
        if (spatial != null) {
            this.getSpatials().add(spatial);
        }
        return this;
    }

    @Override
    public Resource addToResource(Model model, Resource parent) {
        addNsPrefix(model);
        Resource dataset = model.createResource(DCAT.Dataset);
        addTitleAndDescription(model, dataset);

        for (Identifier identifier : getIdentifiers()) {
            identifier.addToResource(model, dataset);
        }

        for (Keyword keyword : getKeywords()) {
            keyword.addToResource(model, dataset);
        }

        if (getAccrualPeriodicity() != null) {
            getAccrualPeriodicity().addToResource(model, dataset);
        }

        for (Theme theme : getThemes()) {
            theme.addToResource(model, dataset);
        }

        if (getPublisher() != null) {
            getPublisher().addToResource(model, dataset);
        }

        for (Language language : getLanguages()) {
            language.addToResource(model, dataset);
        }

        if (getIssued() != null) {
            getIssued().addToResource(model, dataset);
        }

        for (ThemeTaxonomy themeTaxonomy : getThemeTaxonomies()) {
            themeTaxonomy.addToResource(model, dataset);
        }

        if (getModified() != null) {
            getModified().addToResource(model, dataset);
        }

        for (ContactPoint contactPoint : getContactPoints()) {
            contactPoint.addToResource(model, dataset);
        }

        if (getAccessRights() != null) {
            getAccessRights().addToResource(model, dataset);
        }

        for (LandingPage landingPage : getLandingPages()) {
            landingPage.addToResource(model, dataset);
        }

        for (DistributionProperty distribution : getDistributions()) {
            distribution.addToResource(model, dataset);
        }

        for (Spatial spatial : getSpatials()) {
            spatial.addToResource(model, dataset);
        }

        parent.addProperty(DCAT.dataset, dataset);
        return parent;
    }

}
