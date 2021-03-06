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
package org.n52.shetland.ogc.gml;

import org.locationtech.jts.geom.Geometry;

public class AbstractGeometry extends AbstractGML {
    /**
     * Geometry
     */
    private Geometry geometry;

    /**
     * constructor
     */
    public AbstractGeometry() {
    }

    /**
     * constructor
     *
     * @param id
     *           GML id
     */
    public AbstractGeometry(String id) {
        setGmlId(id);
    }

    /**
     * Get geometry
     *
     * @return the geometry
     */
    public Geometry getGeometry() {
        return geometry;
    }

    /**
     * set geometry
     *
     * @param geometry
     *                 the geometry to set
     */
    public AbstractGeometry setGeometry(Geometry geometry) {
        this.geometry = geometry;
        return this;
    }

    /**
     * Is geometry set
     *
     * @return true if geometry is set
     */
    public boolean isSetGeometry() {
        return this.geometry != null;
    }
}
