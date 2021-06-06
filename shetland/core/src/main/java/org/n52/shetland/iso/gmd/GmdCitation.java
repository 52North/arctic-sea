/*
 * Copyright 2015-2021 52°North Initiative for Geospatial Open Source
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

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class GmdCitation
        extends AbtractGmd {

    private static final GmdCitation EC_50_2008 =
            new GmdCitation("EC/50/2008", new GmdCitationDate(GmdDateType.publication(), "2008"));
    private String title;
    private GmdCitationDate date;

    public GmdCitation(String title, GmdCitationDate date) {
        this.title = title;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public GmdCitationDate getDate() {
        return date;
    }

    public static GmdCitation airQualityDirectiveEC502008() {
        return EC_50_2008;
    }

}
