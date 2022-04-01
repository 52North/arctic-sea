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
package org.n52.shetland.rdf.dcat;

import java.util.LinkedList;
import java.util.List;

import org.apache.jena.rdf.model.Resource;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.vocabulary.DCAT;
import org.apache.jena.vocabulary.RDF;
import org.n52.shetland.rdf.AbstractTitleDescription;
import org.n52.shetland.rdf.RDFElement;
import org.n52.shetland.rdf.dct.Format;
import org.n52.shetland.rdf.dct.License;

public class Distribution extends AbstractTitleDescription<Distribution> implements RDFElement, DcatRdfPrefix {
    /*
     * title -> 0..n description -> 0..n
     */

    /*
     * 1..n
     */
    private List<AccessURL> accessURLs = new LinkedList<AccessURL>();

    /*
     * 0..n
     */
    private List<DownloadURL> downloadURLs = new LinkedList<DownloadURL>();

    /*
     * 0..1
     */
    private Format format;

    /*
     * 0..1
     */
    private License license;

    /*
     * title -> 0..n description -> 0..n
     */

    public Distribution(AccessURL accessURL) {
        addAccessURL(accessURL);
    }

    public List<AccessURL> getAccessURLs() {
        return accessURLs;
    }

    public Distribution setAccessURLs(List<AccessURL> accessURLs) {
        this.getAccessURLs().clear();
        if (accessURLs != null) {
            this.getAccessURLs().addAll(accessURLs);
        }
        return this;
    }

    public Distribution addAccessURL(AccessURL accessURL) {
        if (accessURL != null) {
            this.getAccessURLs().add(accessURL);
        }
        return this;
    }

    public List<DownloadURL> getDownloadURLs() {
        return downloadURLs;
    }

    public Distribution setDownloadURLs(List<DownloadURL> downloadURLs) {
        this.getDownloadURLs().clear();
        if (downloadURLs != null) {
            this.getDownloadURLs().addAll(downloadURLs);
        }
        return this;
    }

    public Distribution addDownloadURL(DownloadURL downloadURL) {
        if (downloadURL != null) {
            this.getDownloadURLs().add(downloadURL);
        }
        return this;
    }

    public Format getFormat() {
        return format;
    }

    public void setFormat(Format format) {
        this.format = format;
    }

    public License getLicense() {
        return license;
    }

    public void setLicense(License license) {
        this.license = license;
    }

    public Resource getResource(Model model) {
        Resource distribution = DCAT.NAMESPACE;
        distribution.addProperty(RDF.type, DCAT.Distribution);
        addValues(model, distribution);
        return addValues(model, distribution);
    }

    @Override
    public Model addToModel(Model model) {
        addNsPrefix(model);
        Resource distribution = addValues(model, model.createResource(DCAT.Distribution));
        addTitleAndDescription(model, distribution);
        return model;
    }

    private Resource addValues(Model model, Resource distribution) {
        distribution.addProperty(RDF.type, DCAT.Distribution);
        addTitleAndDescription(model, distribution);

        for (AccessURL accessURL : getAccessURLs()) {
            accessURL.addToResource(model, distribution);
        }

        for (DownloadURL downloadURL : getDownloadURLs()) {
            downloadURL.addToResource(model, distribution);
        }

        if (getFormat() != null) {
            getFormat().addToResource(model, distribution);
        }

        if (getLicense() != null) {
            getLicense().addToResource(model, distribution);
        }

        return distribution;
    }

}
