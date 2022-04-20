/*
 * Copyright (C) 2015-2022 52°North Spatial Information Research GmbH
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

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class GmdSpecification extends AbtractGmd {

    private static final GmdSpecification DATA_CAPTURE_SPECIFICATION
            = new GmdSpecification("Data Capture", GmdCitation
                                   .airQualityDirectiveEC502008());
    private static final GmdSpecification TIME_COVERAGE_SPECIFICATION
            = new GmdSpecification("Time Coverage", GmdCitation
                                   .airQualityDirectiveEC502008());
    private final String explanation;
    private final GmdCitation citation;

    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public GmdSpecification(String explanation, GmdCitation citation) {
        this.explanation = explanation;
        this.citation = citation;
    }

    public String getExplanation() {
        return explanation;
    }

    @SuppressFBWarnings({ "EI_EXPOSE_REP" })
    public GmdCitation getCitation() {
        return citation;
    }

    @SuppressFBWarnings({ "MS_EXPOSE_REP" })
    public static GmdSpecification dataCapture() {
        return DATA_CAPTURE_SPECIFICATION;
    }

    @SuppressFBWarnings({ "MS_EXPOSE_REP" })
    public static GmdSpecification timeCoverage() {
        return TIME_COVERAGE_SPECIFICATION;
    }

}
