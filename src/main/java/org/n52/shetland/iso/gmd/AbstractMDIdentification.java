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

public abstract class AbstractMDIdentification
        extends AbstractObject
        implements AttributeSimpleAttrs {

    private SimpleAttrs simpleAttrs;
    /**
     * 1..1
     */
    private GmdCitation citation;
    /**
     * 1..1
     */
    private String abstrakt;

    public AbstractMDIdentification(SimpleAttrs simpleAttrs) {
        this.simpleAttrs = simpleAttrs;
    }

    public AbstractMDIdentification(GmdCitation citation, String abstrakt) {
        this.citation = citation;
        this.abstrakt = abstrakt;
    }

    @Override
    public void setSimpleAttrs(SimpleAttrs simpleAttrs) {
        this.simpleAttrs = simpleAttrs;
    }

    @Override
    public SimpleAttrs getSimpleAttrs() {
        return simpleAttrs;
    }

    /**
     * @return the citation
     */
    public GmdCitation getCitation() {
        return citation;
    }

    /**
     * @return the abstrakt
     */
    public String getAbstrakt() {
        return abstrakt;
    }

}
