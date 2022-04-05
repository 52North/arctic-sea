/*
 * Copyright 2015-2022 52°North Spatial Information Research GmbH
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
package org.n52.shetland.aqd;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public enum ReportObligationType {
    // B("B", "Information on zones and agglomerations"),
    // C("C", "Information on the assessment regime"),
    // D("D", "Information on the assessment methods"),
    E1A("E1a", "Primary validated assessment data - measurements"), E1B("E1b",
            "Primary validated assessment data - modelled"), E2A("E2a",
                    "Primary up-to-date assessment data - measurements");
    private final String title;

    private final String description;

    ReportObligationType(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return description;
    }

    public static ReportObligationType from(String v) {
        for (ReportObligationType rot : ReportObligationType.values()) {
            if (rot.getTitle().equalsIgnoreCase(v)) {
                return rot;
            }
        }
        throw new IllegalArgumentException(v);
    }
}
