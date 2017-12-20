/*
 * Copyright 2015-2017 52°North Initiative for Geospatial Open Source
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

import java.util.Optional;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class EnvelopeOrGeometry {

    private final Geometry geometry;
    private final ReferencedEnvelope envelope;

    private EnvelopeOrGeometry(Geometry geometry, ReferencedEnvelope envelope) {
        this.geometry = geometry;
        this.envelope = envelope;
    }

    public EnvelopeOrGeometry(ReferencedEnvelope envelope) {
        this(null, envelope);
    }

    public EnvelopeOrGeometry(Geometry geometry) {
        this(geometry, null);
    }

    public Optional<Geometry> getGeometry() {
        return Optional.ofNullable(this.geometry);
    }

    public Optional<ReferencedEnvelope> getEnvelope() {
        return Optional.ofNullable(this.envelope);
    }

    public boolean isGeometry() {
        return this.geometry != null;
    }

    public boolean isEnvelope() {
        return this.envelope != null;
    }

    public int getSRID() {
        if (this.envelope != null) {
            return envelope.getSrid();
        } else if (this.geometry != null) {
            return this.geometry.getSRID();
        } else {
            return -1;
        }
    }

    public Coordinate[] getCoordinates() {
        if (this.geometry != null) {
            return this.geometry.getCoordinates();
        } else if (this.envelope != null) {
            return this.envelope.getCoordinates();
        } else {
            return null;
        }
    }

    public Coordinate getCoordinate() {
        if (this.geometry != null) {
            return this.geometry.getCoordinate();
        } else if (this.envelope != null) {
            return this.envelope.getCoordinate();
        } else {
            return null;
        }
    }

    public Geometry toGeometry() {
        if (this.geometry != null) {
            return this.geometry;
        } else if (this.envelope != null) {
            return this.envelope.toGeometry();
        } else {
            return null;
        }
    }

    public ReferencedEnvelope toEnvelope() {
        if (this.envelope != null) {
            return this.envelope;
        } else if (this.geometry != null) {
            return new ReferencedEnvelope(this.geometry);
        } else {
            return null;
        }
    }

}
