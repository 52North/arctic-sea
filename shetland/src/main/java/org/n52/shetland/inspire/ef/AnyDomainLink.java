/*
 * Copyright 2015-2019 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.inspire.ef;

import org.n52.shetland.ogc.gml.AbstractGML;
import org.n52.shetland.w3c.xlink.AttributeSimpleAttrs;
import org.n52.shetland.w3c.xlink.SimpleAttrs;

public class AnyDomainLink
        extends AbstractGML
        implements AttributeSimpleAttrs {

    private SimpleAttrs simpleAttrs;

    /**
     * 1..1
     */
    private String comment;

    /**
     * 1..1
     */
    private EnvironmentalMonitoringFacility relatedTo;

    public AnyDomainLink(SimpleAttrs simpleAttrs) {
        this.simpleAttrs = simpleAttrs;
    }

    public AnyDomainLink(String comment, EnvironmentalMonitoringFacility relatedTo) {
        this.comment = comment;
        this.relatedTo = relatedTo;
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

    /**
     * @return the comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * @return the relatedTo
     */
    public EnvironmentalMonitoringFacility getRelatedTo() {
        return relatedTo;
    }
}
