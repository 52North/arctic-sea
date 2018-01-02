/*
 * Copyright 2016-2018 52°North Initiative for Geospatial Open Source
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

import org.n52.shetland.w3c.xlink.AttributeSimpleAttrs;
import org.n52.shetland.w3c.xlink.SimpleAttrs;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class GmdCitation
        extends AbtractGmd
        implements AttributeSimpleAttrs {

    private static final GmdCitation EC_50_2008 =
            new GmdCitation("EC/50/2008", new GmdCitationDate(GmdDateType.publication(), "2008"));
    private SimpleAttrs simpleAttrs;
    private String title;
    private GmdCitationDate date;

    public GmdCitation(SimpleAttrs simpleAttrs) {
        this.simpleAttrs = simpleAttrs;
    }

    public GmdCitation(String title, GmdCitationDate date) {
        this.title = title;
        this.date = date;
    }

    @Override
    public void setSimpleAttrs(SimpleAttrs simpleAttrs) {
        this.simpleAttrs = simpleAttrs;
    }

    @Override
    public SimpleAttrs getSimpleAttrs() {
        return simpleAttrs;
    }

    @Override
    public boolean isSetSimpleAttrs() {
        return getSimpleAttrs() != null && getSimpleAttrs().isSetHref();
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
