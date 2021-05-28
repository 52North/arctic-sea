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
package org.n52.shetland.ogc.om.features.samplingFeatures;

import org.n52.shetland.ogc.gml.CodeWithAuthority;
import org.n52.shetland.ogc.om.features.SfConstants;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;

/**
 * Abstract super class for all sampling features
 *
 * @since 1.0.0
 *
 */
public class SamplingFeature extends AbstractSamplingFeature {

    /**
     * constructor
     *
     * @param featureIdentifier
     *            identifier of sampling feature
     */
    public SamplingFeature(final CodeWithAuthority featureIdentifier) {
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
    public SamplingFeature(final CodeWithAuthority featureIdentifier, final String gmlId) {
        super(featureIdentifier, gmlId);
        setDefaultElementEncoding(SfConstants.NS_SAMS);
    }

    @Override
    public <X> X accept(FeatureOfInterestVisitor<X> visitor) throws OwsExceptionReport {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return String.format(
                "SamplingFeature [name=%s, description=%s, xmlDescription=%s, geometry=%s, "
                + "featureType=%s, url=%s, sampledFeatures=%s, parameters=%s, encode=%b, relatedSamplingFeatures=%s]",
                getName(), getDescription(), getXml(), getGeometry(), getFeatureType(), getUrl(),
                getSampledFeatures(), getParameters(), isEncode(), getRelatedSamplingFeatures());
    }

}
