/*
 * Copyright 2015-2017 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.ogc.om.features.samplingFeatures;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.n52.shetland.ogc.OGCConstants;
import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.gml.CodeWithAuthority;
import org.n52.shetland.ogc.gml.FeatureWith.FeatureWithEncode;
import org.n52.shetland.ogc.gml.FeatureWith.FeatureWithFeatureType;
import org.n52.shetland.ogc.gml.FeatureWith.FeatureWithGeometry;
import org.n52.shetland.ogc.gml.FeatureWith.FeatureWithUrl;
import org.n52.shetland.ogc.om.NamedValue;
import org.n52.shetland.util.CollectionHelper;
import org.n52.shetland.util.JavaHelper;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.locationtech.jts.geom.Geometry;

public abstract class AbstractSamplingFeature
        extends AbstractFeature
        implements FeatureWithGeometry, FeatureWithFeatureType, FeatureWithUrl, FeatureWithEncode {

    /**
     * Feature geometry
     */
    private Geometry geometry;

    /**
     * Type of this feature
     */
    private String featureType = OGCConstants.UNKNOWN;

    /**
     * URL to feature representation, e.g. to a WFS
     */
    private String url;

    /**
     * Sampled features, domain feature
     */
    private final List<AbstractFeature> sampledFeatures = new LinkedList<AbstractFeature>();

    /**
     * Parameters
     */
    private final List<NamedValue<?>> parameters = new LinkedList<NamedValue<?>>();

    /**
     * Should this feature be encoded in response
     */
    private boolean encode = true;

    /**
     * Related sampling features
     */
    private Collection<SamplingFeatureComplex> relatedSamplingFeatures;

    private boolean wasEncoded;

    /**
     * constructor
     *
     * @param featureIdentifier
     *            identifier of sampling feature
     */
    public AbstractSamplingFeature(final CodeWithAuthority featureIdentifier) {
        this(featureIdentifier, null);
    }

    /**
     * constructor
     *
     * @param featureIdentifier
     *            identifier of sampling feature
     * @param gmlId
     *            GML of this feature
     */
    public AbstractSamplingFeature(final CodeWithAuthority featureIdentifier, final String gmlId) {
        super(featureIdentifier, gmlId);
    }

    @Override
    public boolean isSetGmlID() {
        return super.isSetGmlID() && wasEncoded;
    }

    @Override
    public String getGmlId() {
        if (!super.isSetGmlID()) {
            final StringBuilder builder = new StringBuilder();
            builder.append("ssf_");
            builder.append(JavaHelper.generateID(getIdentifierCodeWithAuthority().getValue()));
            setGmlId(builder.toString());
        }
        return super.getGmlId();
    }

    @Override
    public Geometry getGeometry() {
        return geometry;
    }

    @Override
    public void setGeometry(final Geometry geometry) throws InvalidSridException {
        if (geometry != null && geometry.getSRID() == 0) {
            throw new InvalidSridException(0);
        }
        this.geometry = geometry;
    }

    @Override
    public String getFeatureType() {
        return featureType;
    }

    @Override
    public void setFeatureType(final String featureType) {
        this.featureType = featureType;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public void setUrl(final String url) {
        this.url = url;
    }

    /**
     * Set sampled features
     *
     * @param sampledFeatures
     *            Sampled fearure list
     */
    public void setSampledFeatures(final List<AbstractFeature> sampledFeatures) {
        this.sampledFeatures.addAll(sampledFeatures);
    }

    /**
     * Get sampled feaures
     *
     * @return Sampled feature list
     */
    public List<AbstractFeature> getSampledFeatures() {
        if (isSetSampledFeatures()) {
            return Collections.unmodifiableList(sampledFeatures);
        }
        return Collections.emptyList();
    }

    /**
     * Check whether sampled features are set
     *
     * @return <code>true</code>, if sampled features are set
     */
    public boolean isSetSampledFeatures() {
        return CollectionHelper.isNotEmpty(sampledFeatures);
    }

    /**
     * Add parameter
     *
     * @param namedValue
     *            Parameter ro add
     */
    public void addParameter(final NamedValue<?> namedValue) {
        parameters.add(namedValue);
    }

    /**
     * Add parameters
     *
     * @param parameters
     *            Parameters to add
     */
    public void setParameters(final Collection<NamedValue<?>> parameters) {
        this.parameters.addAll(parameters);
    }

    /**
     * Get parameters
     *
     * @return Parameter list
     */
    public List<NamedValue<?>> getParameters() {
        return parameters;
    }

    @Override
    public boolean isSetParameter() {
        return CollectionHelper.isNotEmpty(parameters);
    }

    @Override
    public boolean isEncode() {
        return encode;
    }

    /**
     * Set indicator if feature should be encoded
     *
     * @param encode
     *            Encoding indicator
     */
    public void setEncode(final boolean encode) {
        this.encode = encode;
    }

    public void wasEncoded() {
        this.wasEncoded = true;
    }

    /**
     * Add related sampling feature
     *
     * @param relatedSamplingFeature
     *            Related sampling feature to add
     */
    public void addRelatedSamplingFeature(final SamplingFeatureComplex relatedSamplingFeature) {
        if (!isSetRelatedSamplingFeatures()) {
            relatedSamplingFeatures = Sets.newHashSet();
        }
        if (relatedSamplingFeature != null) {
            relatedSamplingFeatures.add(relatedSamplingFeature);
        }
    }

    /**
     * Add related sampling features
     *
     * @param relatedSamplingFeatures
     *            Related sampling features to add
     */
    public void addAllRelatedSamplingFeatures(final Collection<SamplingFeatureComplex> relatedSamplingFeatures) {
        if (isSetRelatedSamplingFeatures()) {
            this.relatedSamplingFeatures.addAll(relatedSamplingFeatures);
        } else {
            this.relatedSamplingFeatures = relatedSamplingFeatures;
        }
    }

    /**
     * Set related sampling features
     *
     * @param relatedSamplingFeatures
     *            Related sampling features to set
     */
    public void setRelatedSamplingFeatures(final Collection<SamplingFeatureComplex> relatedSamplingFeatures) {
        this.relatedSamplingFeatures = relatedSamplingFeatures;
    }

    /**
     * Get related sampling features
     *
     * @return Related sampling features
     */
    public List<SamplingFeatureComplex> getRelatedSamplingFeatures() {
        return relatedSamplingFeatures != null ? Lists.newArrayList(relatedSamplingFeatures)
                : Collections.<SamplingFeatureComplex> emptyList();
    }

    /**
     * Check whether related sampling features are set
     *
     * @return <code>true</code>, if related sampling features are set
     */
    public boolean isSetRelatedSamplingFeatures() {
        return CollectionHelper.isNotEmpty(relatedSamplingFeatures);
    }

    @Override
    public String toString() {
        return String.format(
                "AbstractSamplingFeature [name=%s, description=%s, xmlDescription=%s, geometry=%s, "
                + "featureType=%s, url=%s, sampledFeatures=%s, parameters=%s, encode=%b, relatedSamplingFeatures=%s]",
                getName(), getDescription(), getXml(), getGeometry(), getFeatureType(), getUrl(), getSampledFeatures(),
                getParameters(), isEncode(), getRelatedSamplingFeatures());
    }
}
