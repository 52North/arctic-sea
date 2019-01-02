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
package org.n52.shetland.ogc.filter;

import org.locationtech.jts.geom.Geometry;

import org.n52.shetland.ogc.filter.FilterConstants.SpatialOperator;
import org.n52.shetland.util.EnvelopeOrGeometry;
import org.n52.shetland.util.ReferencedEnvelope;

/**
 * Spatial filter class
 *
 * @since 1.0.0
 *
 */
public class SpatialFilter extends Filter<SpatialOperator> {

    private SpatialOperator operator;

    private EnvelopeOrGeometry geometry;

    public SpatialFilter() {
        this(null, (Geometry) null, null);
    }

    /**
     * constructor
     *
     * @param operator       Spatial operator
     * @param geometry       Filter geometry
     * @param valueReference Filter valueReference
     */
    public SpatialFilter(SpatialOperator operator, Geometry geometry, String valueReference) {
        super(valueReference);
        this.operator = operator;
        this.geometry = new EnvelopeOrGeometry(geometry);
    }

    /**
     * constructor
     *
     * @param operator       Spatial operator
     * @param geometry       Filter geometry
     * @param valueReference Filter valueReference
     */
    public SpatialFilter(SpatialOperator operator, ReferencedEnvelope geometry, String valueReference) {
        super(valueReference);
        this.operator = operator;
        this.geometry = new EnvelopeOrGeometry(geometry);
    }

    @Override
    public SpatialOperator getOperator() {
        return operator;
    }

    @Override
    public SpatialFilter setOperator(SpatialOperator operator) {
        this.operator = operator;
        return this;
    }

    /**
     * Get SRID
     *
     * @return SRID
     */
    public int getSrid() {
        return geometry.getSRID();
    }

    /**
     * Get filter geometry
     *
     * @return filter geometry
     */
    public EnvelopeOrGeometry getGeometry() {
        return geometry;
    }

    /**
     * Set filter geometry
     *
     * @param geometry filter geometry
     *
     * @return This filter
     */
    public SpatialFilter setGeometry(Geometry geometry) {
        this.geometry = new EnvelopeOrGeometry(geometry);
        return this;
    }

    /**
     * Set filter geometry
     *
     * @param geometry filter geometry
     *
     * @return This filter
     */
    public SpatialFilter setGeometry(ReferencedEnvelope geometry) {
        this.geometry = new EnvelopeOrGeometry(geometry);
        return this;
    }

    @Override
    public String toString() {
        return "Spatial filter: " + operator + " " + geometry;
    }

}
