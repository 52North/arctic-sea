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
package org.n52.shetland.iso.gmd;

import org.n52.shetland.ogc.gml.GmlConstants;
import org.n52.shetland.ogc.om.quality.OmResultQuality;

public abstract class GmdDomainConsistency
        extends AbtractGmd
        implements OmResultQuality {

    public static GmdConformanceResult dataCapture(boolean pass) {
        return new GmdConformanceResult(pass, GmdSpecification.dataCapture());
    }

    public static OmResultQuality dataCapture(GmlConstants.NilReason nilReason) {
        return new GmdConformanceResult(nilReason, GmdSpecification.dataCapture());
    }

    public static GmdConformanceResult timeCoverage(boolean pass) {
        return new GmdConformanceResult(pass, GmdSpecification.timeCoverage());
    }

    public static GmdConformanceResult timeCoverage(GmlConstants.NilReason nilReason) {
        return new GmdConformanceResult(nilReason, GmdSpecification.timeCoverage());
    }

    public static GmdQuantitativeResult uncertaintyEstimation(double value) {
        return new GmdQuantitativeResult(GmlBaseUnit.uncertaintyEstimation().unifyId(value), Double.toString(value));
    }

    public static GmdQuantitativeResult uncertaintyEstimation(GmlConstants.NilReason nilReason) {
        return new GmdQuantitativeResult(GmlBaseUnit.uncertaintyEstimation().unifyId(nilReason), nilReason);
    }

}
