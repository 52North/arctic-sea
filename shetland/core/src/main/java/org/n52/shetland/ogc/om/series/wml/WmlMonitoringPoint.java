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
package org.n52.shetland.ogc.om.series.wml;

import org.n52.shetland.ogc.gml.CodeWithAuthority;
import org.n52.shetland.ogc.om.features.samplingFeatures.FeatureOfInterestVisitor;
import org.n52.shetland.ogc.om.series.AbstractMonitoringFeature;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;

public class WmlMonitoringPoint
        extends AbstractMonitoringFeature {

    public WmlMonitoringPoint(CodeWithAuthority featureIdentifier) {
        this(featureIdentifier, null);
    }

    public WmlMonitoringPoint(CodeWithAuthority featureIdentifier, String gmlId) {
        super(featureIdentifier, gmlId);
        setDefaultElementEncoding(WaterMLConstants.NS_WML_20);
    }

    @Override
    public <X> X accept(FeatureOfInterestVisitor<X> visitor) throws OwsExceptionReport {
        return visitor.visit(this);
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
