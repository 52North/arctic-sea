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
package org.n52.shetland.ogc.gml;

import org.n52.shetland.iso.gmd.EXExtent;

/**
 * Internal representation of the OGC GML DomainOfValidity.
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public class DomainOfValidity {

    /* 0..1 */
    private EXExtent exExtent;

    /**
     * @return the exExtent
     */
    public EXExtent getExExtent() {
        return exExtent;
    }

    /**
     * @param exExtent the exExtent to set
     */
    public DomainOfValidity setExExtent(EXExtent exExtent) {
        this.exExtent = exExtent;
        return this;
    }

    public boolean hasExExtent() {
        return getExExtent() != null;
    }
}
