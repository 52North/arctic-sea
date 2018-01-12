/*
 * Copyright 2015-2018 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.ogc.om.series.tsml;

import org.n52.shetland.ogc.gml.CodeWithAuthority;
import org.n52.shetland.ogc.om.features.samplingFeatures.FeatureOfInterestVisitor;
import org.n52.shetland.ogc.om.series.AbstractMonitoringFeature;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;

public class TsmlMonitoringFeature
        extends AbstractMonitoringFeature {

    // private representativePoint
    // TODO(specki): Add representative Point

    public TsmlMonitoringFeature(CodeWithAuthority featureIdentifier) {
        this(featureIdentifier, null);
    }

    public TsmlMonitoringFeature(CodeWithAuthority featureIdentifier, String gmlId) {
        super(featureIdentifier, gmlId);
        setDefaultElementEncoding(TimeseriesMLConstants.NS_TSML_10);
    }

    @Override
    public <X> X accept(FeatureOfInterestVisitor<X> visitor) throws OwsExceptionReport {
        //TODO(specki): refactor visitor to also accept TsmlMonitoringPoint OR refactor to general MonitoringPoint
        //return visitor.visit(this);
        return null;
    }

    @Override
    public String toString() {
        return String.format(
                "MonitoringPoint [name=%s, description=%s, xmlDescription=%s, geometry=%s, "
                + "featureType=%s, url=%s, sampledFeatures=%s, parameters=%s, encode=%b, relatedSamplingFeatures=%s]",
                getName(), getDescription(), getXml(), getGeometry(), getFeatureType(), getUrl(), getSampledFeatures(),
                getParameters(), isEncode(), getRelatedSamplingFeatures());
    }

}
