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
package org.n52.shetland.ogc.gml;

import org.n52.shetland.ogc.om.features.samplingFeatures.FeatureOfInterestVisitor;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;

import com.google.common.base.Strings;

import org.n52.shetland.ogc.HasDefaultEncoding;

/**
 * Abstract class for encoding the feature of interest. Necessary because
 * different feature types should be supported. The database or another
 * feature source (e.g. WFS) should provide information about the application
 * schema.
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public abstract class AbstractFeature extends AbstractGML implements HasDefaultEncoding<AbstractFeature> {

    private String defaultEncoding;
    private String xml;
    private boolean wasEncoded;

    public AbstractFeature(String identifier) {
        super(identifier);
    }

    /**
     * constructor
     *
     * @param featureIdentifier
     *            Feature identifier
     */
    public AbstractFeature(CodeWithAuthority featureIdentifier) {
        super(featureIdentifier);
    }

    /**
     * constructor
     *
     * @param featureIdentifier
     *            Feature identifier
     * @param gmlId
     *            GML id
     */
    public AbstractFeature(CodeWithAuthority featureIdentifier, String gmlId) {
        super(featureIdentifier, gmlId);
    }

    public void copyTo(AbstractFeature copyOf) {
        super.copyTo(copyOf);
    }

    public AbstractFeature setDefaultElementEncoding(String defaultEncoding) {
        this.defaultEncoding = defaultEncoding;
        return this;
    }

    public String getDefaultElementEncoding() {
        return defaultEncoding;
    }

    public boolean isSetDefaultElementEncoding() {
        return this.defaultEncoding != null && !this.defaultEncoding.isEmpty();
    }

    /**
     * @return the xml
     */
    public String getXml() {
        return xml;
    }

    /**
     * @param xml the xml to set
     */
    public AbstractFeature setXml(String xml) {
        this.xml = xml;
        return this;
    }

    public boolean isSetXml() {
        return !Strings.isNullOrEmpty(getXml());
    }

    public boolean isEncoded() {
        return wasEncoded;
    }

    public void wasEncoded() {
        this.wasEncoded  = true;
    }

    public <X> X accept(FeatureOfInterestVisitor<X> visitor) throws OwsExceptionReport {
        return null;
    }
}
