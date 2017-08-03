/*
 * Copyright 2016-2017 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.util;

import java.io.Serializable;

import com.google.common.base.Joiner;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

/**
 * Class for internal Envelope representation TODO should this class offer
 * merging capabilities like SosEnvelope.expandTo(SosEnvelope) considering
 * coordinate transformations?
 *
 * @since 1.0.0
 */
public class ReferencedEnvelope implements Serializable {
    private static final long serialVersionUID = 6525679408878064331L;

    /**
     * JTS envelope object
     */
    private Envelope envelope;

    /**
     * SRID
     */
    private int srid = -1;

    public ReferencedEnvelope() {
    }


    public ReferencedEnvelope(Geometry geometry) {
        this(geometry.getEnvelopeInternal(), geometry.getSRID());
    }

    /**
     * constructor
     *
     * @param envelope
     *            JTS envelope
     * @param srid
     *            SRID
     */
    public ReferencedEnvelope(Envelope envelope, int srid) {
        this.envelope = envelope != null ? envelope : new Envelope();
        this.srid = srid;
    }

    /**
     * Get envelope
     *
     * @return the envelope
     */
    public Envelope getEnvelope() {
        return envelope;
    }

    /**
     * Expand this envelope to include the given envelope.
     *
     * @param e the envelope (may be <code>null</code>)
     */
    public void expandToInclude(Envelope e) {
        if (e != null) {
            if (isSetEnvelope()) {
                getEnvelope().expandToInclude(e);
            } else {
                setEnvelope(e);
            }
        }
    }

    /**
     * Expand this envelope to include the given envelope.
     *
     * @param e the envelope (may be <code>null</code>)
     */
    public void expandToInclude(ReferencedEnvelope e) {
        if (e != null && e.isSetEnvelope()) {
            expandToInclude(e.getEnvelope());
        }
    }

    /**
     * Set envelope
     *
     * @param envelope
     *            the envelope to set
     */
    public ReferencedEnvelope setEnvelope(Envelope envelope) {
        this.envelope = envelope;
        return this;
    }

    /**
     * Creates the minimum and maximum values of this envelope in the default
     * EPSG.
     *
     * @return the {@code MinMax} describing the envelope
     */
    public MinMax<String> getMinMaxFromEnvelope() {
        if (isSetEnvelope()) {
            Joiner joiner = Joiner.on(' ');
            return new MinMax<String>()
                    .setMaximum(joiner.join(envelope.getMaxX(), envelope.getMaxY()))
                    .setMinimum(joiner.join(envelope.getMinX(), envelope.getMinY()));
        }
        return new MinMax<String>();
    }

    /**
     * Get SRID
     *
     * @return the srid
     */
    public int getSrid() {
        return srid;
    }

    public boolean isSetSrid() {
        return getSrid() > 0;
    }

    /**
     * Set SRID
     *
     * @param srid
     *            the srid to set
     */
    public ReferencedEnvelope setSrid(int srid) {
        this.srid = srid;
        return this;
    }

    public boolean isSetEnvelope() {
        return getEnvelope() != null && !getEnvelope().isNull();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getEnvelope() == null) ? 0 : getEnvelope().hashCode());
        result = prime * result + getSrid();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof ReferencedEnvelope)) {
            return false;
        }
        final ReferencedEnvelope other = (ReferencedEnvelope) obj;
        if (getEnvelope() == null) {
            if (other.getEnvelope() != null) {
                return false;
            }
        } else if (!getEnvelope().equals(other.getEnvelope())) {
            return false;
        }
        if (getSrid() != other.getSrid()) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format("SosEnvelope[envelope=%s, srid=%s]", getEnvelope(), getSrid());
    }

    public double getMinX() {
        return envelope.getMinX();
    }

    public double getMaxX() {
        return envelope.getMaxX();
    }

    public double getMinY() {
        return envelope.getMinY();
    }

    public double getMaxY() {
        return envelope.getMaxY();
    }

    public Coordinate[] getCoordinates() {
        return toGeometry().getCoordinates();
    }

    public Coordinate getCoordinate() {
        return toGeometry().getCoordinate();
    }

    public Geometry toGeometry() {
        GeometryFactory factory = JTSHelper.getGeometryFactoryForSRID(srid);
        return factory.toGeometry(this.envelope);
    }

    /**
     * Static method to check if an SosEnvelope is not null and is not empty
     *
     * @param envelope
     *            SosEnvelope to check
     * @return <code>true</code>, if SosEnvelope is not null and not empty.
     */
    public static boolean isNotNullOrEmpty(ReferencedEnvelope envelope) {
        return envelope != null && envelope.isSetEnvelope();
    }

}
