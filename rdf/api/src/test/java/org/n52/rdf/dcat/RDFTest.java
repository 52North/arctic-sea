/*
 * Copyright 2015-2019 52°North Initiative for Geospatial Open Source
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
package org.n52.rdf.dcat;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFWriter;
import org.junit.jupiter.api.Test;
import org.n52.rdf.AbstractDatatype.DataType;
import org.n52.rdf.RDF;
import org.n52.rdf.dct.AccessRights;
import org.n52.rdf.dct.AccrualPeriodicity;
import org.n52.rdf.dct.Description;
import org.n52.rdf.dct.Format;
import org.n52.rdf.dct.Identifier;
import org.n52.rdf.dct.Issued;
import org.n52.rdf.dct.Language;
import org.n52.rdf.dct.License;
import org.n52.rdf.dct.Location;
import org.n52.rdf.dct.Modified;
import org.n52.rdf.dct.Publisher;
import org.n52.rdf.dct.Spatial;
import org.n52.rdf.dct.Theme;
import org.n52.rdf.dct.Title;
import org.n52.rdf.foaf.Homepage;
import org.n52.rdf.foaf.MBox;
import org.n52.rdf.foaf.Name;
import org.n52.rdf.foaf.Organization;
import org.n52.rdf.locn.Geometry;
import org.n52.rdf.vcard4.FN;
import org.n52.rdf.vcard4.HasEmail;
import org.n52.rdf.vcard4.VCardOrganization;

public class RDFTest {

    private static final String CATALOG = "catalog";

    private static final String DISTRIBUTION = "distribution";

    private static final String DATASET = "dataset";

    @Test
    public void testModelCreation() {
        RDF rdf = new RDF();
        Dataset dataset = getDataset(CATALOG);
        Catalog catalog = createCatalog(CATALOG, dataset);
        catalog.addLanguage(new Language("en"));
        catalog.setHomepage(new Homepage(createValue(CATALOG, "homepage")));
        catalog.setModified(new Modified(DataType.DateTime, "2019-04-17T00:00:00Z"));
        catalog.setIssued(new Issued(DataType.DateTime, "2019-04-17T00:00:00Z"));
        catalog.addLanguage(new Language("en"));
        catalog.addThemeTaxonomy(new ThemeTaxonomy("http://publications.europa.eu/resource/authority/data-theme"));
        rdf.addElements(catalog);

        Distribution distribution = new Distribution(new AccessURL("http://accessurl.test.org"));
        distribution.addTitle(createTitle(DISTRIBUTION));
        distribution.addDownloadURL(new DownloadURL("http://downloadurl.test.org"));
        distribution.setFormat(new Format(createValue(DISTRIBUTION, "format")));
        distribution.setLicense(new License(createValue(DISTRIBUTION, "license")));
        rdf.addElements(distribution);

        Organization organization = new Organization();
        organization.setName(new Name("name"));
        organization.setmBox(new MBox("test@test.org"));
        rdf.addElements(organization);

        Model model = ModelFactory.createDefaultModel();
        rdf.addToModel(model);

        RDFWriter w = model.getWriter("RDF/XML-ABBREV");
        w.setProperty("showXMLDeclaration", "true");
        w.setProperty("tab", "4");

        w.write(model, System.out, "test");

        // System.out.println("--------------------------");
        //
        // model.getWriter("RDFJSON").write(model, System.out, "test");

    }

    private Catalog createCatalog(String prefix) {
        return createCatalog(prefix, createDataset(prefix));
    }

    private Catalog createCatalog(String prefix, Dataset dataset) {
        return new Catalog(createTitle(prefix), createDescription(prefix), createPubisher(prefix), dataset);
    }

    private Title createTitle(String prefix) {
        return new Title(createValue(prefix, "title"), "en");
    }

    private Description createDescription(String prefix) {
        return new Description(createValue(prefix, "description"), "en");
    }

    private Publisher createPubisher(String prefix) {
        return new Publisher(createValue(prefix, "publisher"));
    }

    private Dataset createDataset(String prefix) {
        return new Dataset(createTitle(prefix), createDescription(prefix));
    }

    private Dataset getDataset(String prefix) {
        Dataset dataset = createDataset(prefix);
        dataset.addIdentifier(new Identifier(createValue(prefix, "identifier")));
        dataset.addLanguage(new Language("en"));
        dataset.addKeyword(createKeyword(DATASET));
        dataset.setAccrualPeriodicity(createAccrualPeriodicity());
        dataset.addTheme(createTheme());
        dataset.setPublisher(createPubisher(DATASET));
        dataset.setModified(new Modified(DataType.DateTime, "2019-04-17T00:00:00Z"));
        dataset.setIssued(new Issued(DataType.DateTime, "2019-04-17T00:00:00Z"));
        dataset.addContactPoint(createContactPoint(DATASET));
        dataset.setAccessRights(createAccessRights());
        dataset.addLandingPage(createLandingPage(DATASET));
        dataset.addDistribution(createDistribution(DATASET));
        dataset.addSpatial(createSpatial());
        return dataset;
    }

    private Keyword createKeyword(String prefix) {
        return new Keyword(createValue(prefix, "keyword"));
    }

    private AccrualPeriodicity createAccrualPeriodicity() {
        return new AccrualPeriodicity("http://publications.europa.eu/resource/authority/frequency/ANNUAL");
    }

    private Theme createTheme() {
        return new Theme("http://publications.europa.eu/resource/authority/data-theme/AGRI");
    }

    private ContactPoint createContactPoint(String prefix) {
        return new ContactPoint(createVcardOrganization(prefix));
    }

    private VCardOrganization createVcardOrganization(String prefix) {
        VCardOrganization organization = new VCardOrganization();
        organization.setFn(new FN("org_name"));
        organization.setHasEmail(new HasEmail("mail@org.org"));
        return organization;
    }

    private AccessRights createAccessRights() {
        return new AccessRights("http://publications.europa.eu/resource/authority/access-right/RESTRICTED");
    }

    private LandingPage createLandingPage(String prefix) {
        return new LandingPage(createValue(prefix, "landingPage"));
    }

    private DistributionProperty createDistribution(String prefix) {
        return new DistributionProperty(createValue(prefix, "distribution"));
    }

    private Spatial createSpatial() {
        return new Spatial(createLocation());
    }

    private Location createLocation() {
        Location location = new Location(new Geometry(DataType.WKT_LITERAL,
                "POLYGON ((-0.0711 51.5077, -0.1125 51.5077, -0.1125 51.5231, -0.0711 51.5231, -0.0711 51.5077))"));
        location.addGeometry(new Geometry(DataType.GEO_JSON,
                "{\"type\": \"Polygon\", \"coordinates\": [[[-0.071127, 51.507675], [-0.112547, 51.507675], "
                + "[-0.112547, 51.523074], [-0.071127, 51.523074], [-0.071127, 51.507675]]]}"));
        return location;
    }

    private String createValue(String prefix, String value) {
        return prefix != null && !prefix.isEmpty() ? prefix + "-" + value : value;
    }
}
